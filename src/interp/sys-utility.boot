-- Copyright (C) 2007-2011 Gabriel Dos Reis.
-- All rights reserved.
--
-- Redistribution and use in source and binary forms, with or without
-- modification, are permitted provided that the following conditions are
-- met:
--
--     - Redistributions of source code must retain the above copyright
--       notice, this list of conditions and the following disclaimer.
--
--     - Redistributions in binary form must reproduce the above copyright
--       notice, this list of conditions and the following disclaimer in
--       the documentation and/or other materials provided with the
--       distribution.
--
--     - Neither the name of The Numerical Algorithms Group Ltd. nor the
--       names of its contributors may be used to endorse or promote products
--       derived from this software without specific prior written permission.
--
-- THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
-- IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
-- TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
-- PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER
-- OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
-- EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
-- PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
-- PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
-- LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
-- NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
-- SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
--

-- This file defines some utility functions common to both the compiler
-- and interpreter.

import sys_-os
import vmlisp
import hash
namespace BOOT

module sys_-utility where
  probeReadableFile : %String -> %Maybe %String
  remove!: (%List %Thing,%Thing) -> %List %Thing

--%
$COMBLOCKLIST := nil

++ Constants describing byte order
%littleEndian == KEYWORD::%littleEndian
%bigEndian == KEYWORD::%bigEndian
%unknownEndian == KEYWORD::%unknownEndian

++ The byte order of the host machine running OpenAxiom.
%hostByteOrder() ==
  getHostByteOrder() = 1 => %littleEndian
  getHostByteOrder() = 2 => %bigEndian
  %unknownEndian

--%

++ getVMType returns an approximation of the underlying object type
++ representation of a domain, as a Lisp type specifier as seen by
++ the runtime system.
getVMType d ==
  IDENTP d => 
    d is "*" => d
    "%Thing"
  string? d => "%Thing"            -- literal flag parameter
  case (d' := devaluate d) of
    Void => "%Void"
    Identifier => "%Symbol"
    Boolean => "%Boolean"
    Byte => "%Byte"
    Character => "%Char"
    SingleInteger => "%Short"
    Integer => "%Integer"
    NonNegativeInteger => ["%IntegerSection",0]
    PositiveInteger => ["%IntegerSection",1]
    IntegerMod => "%Integer"
    DoubleFloat => "%DoubleFloat"
    String => "%String"
    List => ["%List",getVMType second d']
    Vector => ["%Vector",getVMType second d']
    PrimitiveArray => ["%SimpleArray", getVMType second d']
    Pair => ["%Pair",getVMType second d',getVMType third d']
    Union => ["%Pair",'%Short,'%Thing]
    Record =>
      #rest d' > 2 => "%Shell"
      ["%Pair",'%Thing,'%Thing]
    IndexedList => ["%List", getVMType second d']
    Int8 => ["SIGNED-BYTE", 8]
    Int16 => ["SIGNED-BYTE", 16]
    Int32 => ["SIGNED-BYTE", 32]
    UInt8 => ["UNSIGNED-BYTE", 8]
    UInt16 => ["UNSIGNED-BYTE", 16]
    UInt32 => ["UNSIGNED-BYTE", 32]
    otherwise => "%Thing"                 -- good enough, for now.

--%

++ returns true if `f' is bound to a macro.
macrop: %Thing -> %Boolean
macrop f ==
  IDENTP f and not null MACRO_-FUNCTION f

++ returns true if `f' is bound to a function
functionp: %Thing -> %Boolean
functionp f ==
  IDENTP f => FBOUNDP f and null MACRO_-FUNCTION f
  function? f

++ returns true if `x' is contained in `y'.
CONTAINED: (%Thing,%Thing) -> %Boolean
CONTAINED(x,y) == main where
  main() ==
    symbol? x => eq(x,y)
    equal(x,y)
  eq(x,y) ==
    cons? y => eq(x, first y) or eq(x, rest y)
    symbolEq?(x,y)
  equal(x,y) ==
    atom y => x = y
    equal(x, first y) or equal(x, rest y)

++ Returns all the keys of association list `x'
-- ??? Should not this be named `alistAllKeys'?
ASSOCLEFT: %Thing -> %Thing
ASSOCLEFT x ==
  atom x => x
  [first p for p in x]

++ Returns all the datums of association list `x'.
-- ??? Should not this be named `alistAllValues'?
ASSOCRIGHT: %Thing -> %Thing
ASSOCRIGHT x ==
  atom x => x
  [rest p for p in x]

++ Put the association list pair `(x . y)' into `l', erasing any 
++ previous association for `x'.
ADDASSOC: (%Thing,%Thing,%Alist(%Thing,%Thing)) -> %Alist(%Thing,%Thing)
ADDASSOC(x,y,l) ==
  atom l => [[x,:y],:l]
  x = first first l => [[x,:y],:rest l]
  [first l,:ADDASSOC(x,y,rest l)]


++ Remove any assocation pair `(u . x)' from list `v'.
DELLASOS: (%Thing,%Alist(%Thing,%Thing)) -> %Alist(%Thing,%Thing)
DELLASOS(u,v) ==
  atom v => nil
  u = first first v => rest v
  [first v,:DELLASOS(u,rest v)]


++ Return the datum associated with key `x' in association list `y'.
-- ??? Should not this be named `alistValue'?
LASSOC: (%Thing,%Alist(%Thing,%Thing)) -> %Thing
LASSOC(x,y) ==
  atom y => nil
  x = first first y => rest first y
  LASSOC(x,rest y)

++ Return the key associated with datum `x' in association list `y'.
rassoc: (%Thing,%Alist(%Thing,%Thing)) -> %Thing
rassoc(x,y) ==
  atom y => nil
  x = rest first y => first first y
  rassoc(x,rest y)

++ Reclaim unreachable objects.
RECLAIM() ==
)if %hasFeature KEYWORD::GCL
  SI::GBC true
)elseif %hasFeature KEYWORD::SBCL
  SB_-EXT::GC()
)elseif %hasFeature KEYWORD::CLISP
  EXT::GC()
)else
  nil
)endif

++
makeAbsoluteFilename: %String -> %String
makeAbsoluteFilename name ==
  strconc(systemRootDirectory(),name)

++ returns true if `file' exists as a pathname.
existingFile? file ==
  PROBE_-FILE file => true
  false

probeReadableFile file ==
  readablep file > 0 => file
  nil

++ original version returned 0 on success, and 1 on failure
++ ??? fix that to return -1 on failure.
$ERASE(:filearg) ==
  -removeFile MAKE_-FULL_-NAMESTRING filearg

++
$REPLACE(filespec1,filespec2) ==
  $ERASE(filespec1 := MAKE_-FULL_-NAMESTRING filespec1)
  renameFile(MAKE_-FULL_-NAMESTRING filespec2, filespec1)

++
checkMkdir path ==
  mkdir path = 0 => true
  systemError ['"cannot create directory",:bright path]

++ return the pathname to the system module designated by `m'.
getSystemModulePath m ==
  d := systemAlgebraDirectory() => strconc(d,m,'".",$faslType)
  strconc(systemRootDirectory(),'"algebra/",m,'".",$faslType)

++ load module in `path' that supposedly will define the function 
++ indicated by `name'.
loadModule: (%String,%Symbol) -> %Thing
loadModule(path,name) ==
  FMAKUNBOUND name
  LOAD path

loadExports name ==
  loadFileIfPresent strconc(STRING name,'".sig")

--% numerics
log10 x ==
  LOG(x,10)

bitand: (%Short,%Short) -> %Short
bitand(x,y) ==
  BOOLE(BOOLE_-AND,x,y)

bitior: (%Short,%Short) -> %Short
bitior(x,y) ==
  BOOLE(BOOLE_-IOR,x,y)


--% Back ends 

++ compile a function definition, augmenting the current
++ evaluation environement with the result of the compilation.
COMPILE_-DEFUN(name,body) ==
  eval body
  COMPILE name

++ Augment the current evaluation environment with a function definition.
EVAL_-DEFUN(name,body) ==
  eval MACROEXPANDALL body

PRINT_-AND_-EVAL_-DEFUN(name,body) ==
  eval body
  PRINT_-DEFUN(name,body)


--% Hash table

hashTable cmp ==
  testFun :=
    cmp in '(ID EQ) => function sameObject?
    cmp = 'EQL => function scalarEq?
    cmp = 'EQUAL => function EQUAL
    error '"bad arg to hashTable"
  MAKE_-HASH_-TABLE(KEYWORD::TEST,testFun)

--% Trees to Graphs

minimalise x ==
  min(x,hashTable 'EQUAL) where
    min(x,ht) ==
      y := tableValue(ht,x)
      y => y
      cons? x =>
        z := min(first x,ht)
        if not sameObject?(z,first x) then x.first := z
        z := min(rest x,ht)
        if not sameObject?(z,rest x) then x.rest := z
        hashCheck(x,ht)
      vector? x =>
        for i in 0..maxIndex x repeat
          x.i := min(x.i,ht)
        hashCheck(x,ht)
      string? x => hashCheck(x,ht)
      x
    hashCheck(x,ht) ==
      y := tableValue(ht,x)
      y => y
      tableValue(ht,x) := x
      x

--% File IO
$InputIOMode == KEYWORD::INPUT
$OutputIOMode == KEYWORD::OUTPUT
$BothWaysIOMode == KEYWORD::IO
$ClosedIOMode == KEYWORD::CLOSED

++ return a binary stream open for `file' in mode `mode'; nil
++ if something went wrong.  This function is used by the Algebra.
openBinaryFile(file,mode) ==
  mode = $InputIOMode =>
    OPEN(file,KEYWORD::DIRECTION,mode,
      KEYWORD::IF_-DOES_-NOT_-EXIST,nil,
        KEYWORD::ELEMENT_-TYPE,"%Byte")
  OPEN(file,KEYWORD::DIRECTION,mode,
    KEYWORD::IF_-EXISTS,KEYWORD::SUPERSEDE,
      KEYWORD::ELEMENT_-TYPE,"%Byte")

++ Write byte `b' to output binary file `ofile'.
writeByteToFile(ofile,b) ==
  writeByte(b,ofile)

--%
stringImage x ==
  symbol? x => symbolName x
  string? x => strconc('"_"",x,'"_"")
  toString x

--% Socket I/O

++ Attempt to establish a client TCP/IP socket connection.  The IP numeric
++ address is specified by the first argument; second argument is the
++ version of IP used (4 or 6); third argument is the desired port.
++ Return %nothing on failure, otherwise the file descriptor corresponding
++ to the obtained client socket.
connectToHostAndPort(addr,prot,port) ==
  (socket := doConnectToHostAndPort(addr,prot,port)) < 0 => %nothing
  socket

++ Attempt to read a byte from the socket `s'.  If unsuccessful,
++ return %nothing.
readByteFromStreamSocket s ==
  (byte := doReadByteFromStreamSocket s) < 0 => %nothing
  COERCE(byte,"%Byte")

writeByteToStreamSocket(s,b) ==
  (byte := doWriteByteToStreamSocket(s,b)) < 0 => %nothing
  COERCE(byte,"%Byte")

--%
makeByteBuffer(n,b == 0) ==
  MAKE_-ARRAY(n,KEYWORD::ELEMENT_-TYPE,"%Byte",KEYWORD::INITIAL_-ELEMENT,b)

++ return the sub-string of `s' starting from `f'.
++ When non-nil, `n' designates the length of the sub-string.
subString(s,f,n == nil) ==
  n = nil => subSequence(s,f)
  subSequence(s,f,f + n)

quoteForm t ==
  ["QUOTE",t]

--% assoc

symbolAssoc(s,l) ==
  or/[symbolEq?(s,first x) and leave x for x in l | cons? x] or nil

scalarAssoc(c,l) ==
  or/[scalarEq?(c,first x) and leave x for x in l | cons? x] or nil

stringAssoc(s,l) ==
  or/[stringEq?(s,first x) and leave x for x in l | cons? x] or nil

--% lassoc

symbolLassoc(s,l) ==
  p := symbolAssoc(s,l) => rest p
  nil

--%
remove!(l,x) ==
  l = nil => nil
  valueEq?(first l,x) => rest l
  p := l
  repeat
    p isnt [.,.,:.] => return l
    valueEq?(second p,x) =>
      p.rest := p.rest.rest
      return l
    p := rest p
      
