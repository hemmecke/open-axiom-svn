/*
  Copyright (C) 1991-2002, The Numerical Algorithms Group Ltd.
  All rights reserved.
  Copyright (C) 2007-2009, Gabriel Dos Reis.
  All rights reserved.

  Redistribution and use in source and binary forms, with or without
  modification, are permitted provided that the following conditions are
  met:

      - Redistributions of source code must retain the above copyright
        notice, this list of conditions and the following disclaimer.

      - Redistributions in binary form must reproduce the above copyright
        notice, this list of conditions and the following disclaimer in
        the documentation and/or other materials provided with the
        distribution.

      - Neither the name of The Numerical Algorithms Group Ltd. nor the
        names of its contributors may be used to endorse or promote products
        derived from this software without specific prior written permission.

  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
  IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
  TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
  PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER
  OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
  EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
  PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
  PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
  LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

/* HyperDoc database file manager */


#define _HTADD_C
#include "openaxiom-c-macros.h"

#include <errno.h>
#include <setjmp.h>
#include <stdlib.h>
#include <sys/stat.h>
#include <stdlib.h>
#include <locale.h>

#include "cfuns.h"
#include "hash.h"
#include "lex.h"
#include "sockio.h"
#include "addfile.h"
#include "node.h"

static void add_file(char*, char*, int);
static void add_new_pages(FILE*, FILE*, char*, char*);
static int build_db_filename(short, char*, char*);
static void delete_db(FILE*, FILE*, char*);
static int delete_file(char*, char*);
static void get_filename(void);
static void parse_args(char**, char*, char**, short*);
static void update_db(FILE*, FILE*, FILE*, char*, char*, int);



/*
 * These are variables that htadd needs to have declared because it shares
 * the lexical analyzer with HyperDoc
 */

int gTtFontIs850=0;
HDWindow *gWindow = NULL;
extern int line_number;         /* keeps track of which line a page starts on
                                 * in a file. This way someone can start
                                 * including a line number counter into
                                 * HyperDoc. */
/* for compatibility with HyperDoc */
openaxiom_sio *spad_socket = NULL;
int fresh = 0;

#define Delete 1
#define System 2
#define Current  4
#define Named  8

#define usage "usage: htadd [-s|-l|-f db-directory] [-d|-n] filenames"


int
main(int argc, char **argv)
{
    /*int i;*/
    char db_dir[256];           /* the directory where the db file is */
    char dbfilename[256];       /* the database filename */
    char *filenames[1000];      /* the files to be added */
    char **fnames = filenames;
    short flag;                 /* flag for deleting or adding */

    putenv("LC_ALL=C");
    setlocale(LC_ALL, "");
    parse_args(argv, db_dir, filenames, &flag);

    if (!filenames[0]) {
        fprintf(stderr, "%s\n", usage);
        return -1;
    }

    parser_init();

    build_db_filename(flag, db_dir, dbfilename);

    if (fresh)
        oa_unlink(dbfilename);

    if (flag & Delete)
        while (*fnames)
            delete_file(dbfilename, *fnames++);
    else
        while (*fnames)
            add_file(dbfilename, *fnames++, fresh);
    return 0;
}

/*
 * This routine parses the command line arguments. It parses
 * the command line arguments. It returns a flag which tells the calling
 * routine what database file to use, and whether or not to delete files.
 */


static void
parse_args(char **argv, char *db_dir, char **filenames, short *fl)
{
    *fl = 0;

    while (*++argv) {
        if (strcmp(*argv, "-d") == 0)
            *fl |= Delete;
        else if (strcmp(*argv, "-s") == 0) {
            if (*fl & Current || *fl & Named) {
                fprintf(stderr, "%s\n", usage);
                exit(-1);
            }
            *fl |= System;
        }
        else if (strcmp(*argv, "-n") == 0) {
            fresh = 1;
        }
        else if (strcmp(*argv, "-l") == 0) {
            if (*fl & System || *fl & Named) {
                fprintf(stderr, "%s\n", usage);
                exit(-1);
            }
            *fl |= Current;
        }
        else if (strcmp(*argv, "-f") == 0) {
            if (*fl & System || *fl & Current) {
                fprintf(stderr, "%s\n", usage);
                exit(-1);
            }
            *fl |= Named;
            strcpy(db_dir, *++argv);
        }
        else
            *filenames++ = *argv;
    }

    *filenames = NULL;
}



/* check to see if the user has permission */

/*
 * This procedure builds the db filename. Subsequently, it is passed onto all
 * the add files that are called.
 */


static int
build_db_filename(short flag, char *db_dir, char *dbfilename)
{
    int ret_status;
    char *SPAD;
    struct stat buff;
    char path[256];


    if (flag & System) {
        SPAD = oa_getenv("AXIOM");
        if (SPAD == NULL) {
            fprintf(stderr,
                    "build_db_filename: cannot find system root directory\n");
            exit(-1);
        }
        sprintf(dbfilename, "%s/share/hypertex/pages/%s", SPAD, db_file_name);
        sprintf(path, "%s/share/hypertex/pages", SPAD);
    }
    else if (flag & Named) {
        sprintf(dbfilename, "%s/%s", db_dir, db_file_name);
        strcpy(path, db_dir);
    }
    else {                      /* use the current directory */
        sprintf(dbfilename, "./%s", db_file_name);
        sprintf(path, "./");
    }
/*    fprintf(stderr,"htadd:build_db_filename:dbfilename=%s\n",dbfilename);*/
    /* Now see if I can write to the file  */
    errno = 0;
    ret_status = stat(dbfilename, &buff);
    if (ret_status == -1) {
      if (errno == ENOENT) 
	/* If the file does not exist, check the path.  */
	ret_status = stat(path, &buff);
      if (ret_status == -1) {
	perror("build_db_file");
	exit(1);
      }
    }
    
    if (writeablep(dbfilename) > 0)
      return 1;

    fprintf(stderr, "build_db_filename: '%s' is not writable\n",dbfilename);
    exit(1);
    return 0;
}


/***

   This procedure now works as follows:
     (1) It adds the files to the db_file without full pathnames.
           Two names are going to be used when adding a file -
             addname <-- The name without any paths
             fullname <-- The name with a path prepended to it
     (2) If the user specifies a pathname, then it is the path name that
           is used. If the user does not dpecify a path name, then possible
           paths are found as follows:
              (i) If the user has an environment variable HTPATH set, the
              paths mentioned are used.
              (ii) If not, then the $AXIOM environment variable is used.
****/

static void
add_file(char *dbname, char *name, int fresh)
{
    char fullname[256];
    char temp_db_file[256];
    FILE *db_fp = NULL;
    FILE *temp_db_fp = NULL;
    FILE *ht_fp = NULL;
    char addname[100];
    /*char *HTPATH;*/
    /*char *trace;*/
    /*char *spad;*/


    /** First thing I should do is find the proper file and open it **/
    ht_fp = ht_file_open(fullname, addname, name);

    /*
     * Now I should try to open the two database files. The one to work with,
     * and the temporary one; Send it a 1 so it checks for write access
     */
    if (fresh) {
        if ((db_fp = fopen(dbname, "a")) == NULL) {
            fprintf(stderr, "Can't open database: %s file for appending\n", dbname);
            exit(-1);
        }
    }
    else {
        if ((db_fp = fopen(dbname, "r")) == NULL) {
            exit(-1);
        }
    }
    if (!fresh)
        temp_db_fp = temp_file_open(temp_db_file);

    /** Now actually update the file by adding the changes ***/
    update_db(db_fp, temp_db_fp, ht_fp, addname, fullname, fresh);

    if (!fresh)
        fclose(temp_db_fp);
    fclose(ht_fp);
    if (db_fp != NULL)
        fclose(db_fp);
    if (!fresh) {
       if (oa_copy_file(temp_db_file, dbname) < 0)
          exit(-1);
        unlink(temp_db_file);
    }
}

static void
update_db(FILE *db, FILE *temp_db, FILE *new_file,
          char *addname, char *fullname, int fresh)
{
    char *fname;
    int c, file_there = 0, mtime;

    if (fresh) {
        add_new_pages(db, new_file, addname, fullname);
        return;
    }
    if (db == NULL) {
        add_new_pages(temp_db, new_file, addname, fullname);
        return;
    }
    init_scanner();
    cfile = db;
    c = get_char();
    do {
        if (c == '\t') {
            get_filename();
            fname = alloc_string(token.id);
            get_token();
            mtime = atoi(token.id);
            if (strcmp(fname, addname) == 0) {
                save_scanner_state();
                add_new_pages(temp_db, new_file, addname, fullname);
                restore_scanner_state();
                file_there = 1;
                while ((c = get_char()) != EOF) {
                    if (c == '\t')
                        break;
                }
            }
            else {
                fprintf(temp_db, "\t%s %d", fname, mtime);
                while ((c = get_char()) != EOF) {
                    if (c == '\t')
                        break;
                    putc(c, temp_db);
                }
            }
            free(fname);
        }
        else
            c = get_char();
    } while (c != EOF);
    if (!file_there) {
        add_new_pages(temp_db, new_file, addname, fullname);
    }
}

#define Special(t) \
  (( t == openaxiom_Page_token \
    || t == openaxiom_NewCommand_token \
    || t == openaxiom_Patch_token )?(1):(0))
#define ptype(c, t) (strcpy(c, t));

static void
add_new_pages(FILE *temp_db, FILE *new_file, char *addname, char *fullname)
{
    char type[15];
    int pos;
    int present_type;
    int pages = 0;
    struct stat fstats;

    stat(fullname, &fstats);
    fprintf(temp_db, "\t%s %d\n", addname, (int)fstats.st_mtime);
    cfile = new_file;
    init_scanner();
    while (get_token() != EOF) {
        if (Special(token.type)) {
            ptype(type, token.id);
            present_type = token.type;
            pos = keyword_fpos;
            get_token();
            if (token.type != openaxiom_Lbrace_token) {
                fprintf(stderr, "missing left brace after a page, macro or patch \
                         declaration\n");
                fprintf(stderr, "In the file %s on line %d\n", fullname, line_number);
                exit(-1);
            }
            get_token();
            if (present_type == openaxiom_Page_token
                && token.type != openaxiom_Word_token) {
                fprintf(stderr, "missing page name after \\begin{page}\n");
                fprintf(stderr, "In the file %s on line %d\n", fullname, line_number);
                exit(-1);
            }
            else if (present_type == openaxiom_Macro_token
                     && token.type != openaxiom_Macro_token) {
                fprintf(stderr, "Expected a \\macro name after newcommand, got %s\n",
                        token.id);
                fprintf(stderr, "In the file %s on line %d\n", fullname, line_number);
                exit(-1);
            }
            else if (present_type == openaxiom_Patch_token
                     && token.type != openaxiom_Word_token) {
                fprintf(stderr, "Missing patch name after a \\begin{patch}\n");
                fprintf(stderr, "In the file %s on line %d\n", fullname, line_number);
                exit(-1);
            }
            fprintf(temp_db, "\\%s %s %d %d\n", type,
                    token.id, pos, line_number);
            pages++;
        }
    }
    printf("Added %3d pages and/or macros from %s\n", pages, addname);
}

#define whitespace(c) ((c) == ' ' || (c) == '\t' || (c) == '\n')
#define delim(c) \
  (whitespace(c) )


static void
get_filename(void)
{
    int c, ws;
    static char buffer[256];
    char *buf = buffer;

    do {
        keyword_fpos = fpos;
        c = get_char();
        ws = whitespace(c);
    } while (ws);
    switch (c) {
      case EOF:
        fprintf(stderr, "Error trying to read ht.db, unexpected EOF\n");
        exit(-1);
      case '%':
      case '\\':
      case '{':
      case '}':
        fprintf(stderr, "Error unexpexted character %c\n",c);
        exit(-1);
      default:
        do {
            *buf++ = c;
        } while ((c = get_char()) != EOF && !delim(c));
        unget_char(c);
        *buf = '\0';
        token.type = openaxiom_Word_token;
        token.id = buffer;
        break;
    }
}

static int
delete_file(char *dbname, char *name)
{
    char temp_db_file[256];
    FILE *db_fp, *temp_db_fp;
    char dname[256];


    strcpy(dname, name);
    extend_ht(dname);

    /* Open both the tmp database and the real one */
    if ((db_fp = fopen(dbname, "r")) == NULL) {
        fprintf(stderr, "database file is empty, nothing to delete\n");
        return 1;
    }

    temp_db_fp = temp_file_open(temp_db_file);

    /** Now actually update the file by deleting the pages */
    delete_db(db_fp, temp_db_fp, dname);

    fclose(temp_db_fp);
    if (db_fp != NULL)
        fclose(db_fp);
    if (oa_copy_file(temp_db_file, dbname) < 0)
       return -1;
    unlink(temp_db_file);
    return 0;
}

static void
delete_db(FILE *db, FILE *temp_db, char *name)
{
    char *fname;
    int c/*, file_there = 0*/, mtime;

    init_scanner();
    cfile = db;
    c = get_char();
    do {
        if (c == '\t') {
            get_filename();
            fname = alloc_string(token.id);
            get_token();
            mtime = atoi(token.id);
            if (strcmp(fname, name) == 0) {
                while ((c = get_char()) != EOF) {
                    if (c == '\t')
                        break;
                }
            }
            else {
                fprintf(temp_db, "\t%s %d", fname, mtime);
                while ((c = get_char()) != EOF) {
                    if (c == '\t')
                        break;
                    putc(c, temp_db);
                }
            }
            free(fname);
        }
        else
            c = get_char();
    } while (c != EOF);
}
