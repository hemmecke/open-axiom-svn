-- Copyright (c) 1991-2002, The Numerical ALgorithms Group Ltd.
-- All rights reserved.
-- Copyright (C) 2007, Gabriel Dos Reis.
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
--     - Neither the name of The Numerical ALgorithms Group Ltd. nor the
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


)package "BOOT"

f01brf() ==
  htInitPage("F01BRF - LU factorization of real sparse matrix",nil)
  htMakePage '(
    (domainConditions
       (isDomain F (Float)))
    (text . "\windowlink{Manual Page}{manpageXXf01brf} for this routine ")
    (text . "\newline ")
    (text . "\lispwindowlink{Browser operation page}{(|oPageFrom| '|f01brf| '|NagMatrixOperationsPackage|)} for this routine")
    (text . "\newline \horizontalline ")
    (text . "Factorizes a real sparse matrix A of order n. The routine forms ")
    (text . "the {\it LU} factorization of the entire matrix, or ,")
    (text . "optionally, first permutes the matrix to block lower ")
    (text . "triangular form and then only factorizes the diagonal block. ")
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{}\tab{2}")
    (text . "Enter the order {\em n} of the matrix A ")
    (text . "\htbitmap{great=} 1:")
    (text . "\newline\tab{2} ")
    (bcStrings (8 6 n PI))
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{}\tab{2}")
    (text . "Number of non-zero elements {\it nz}:")
    (text . "\tab{32} \menuitemstyle{}\tab{34}")
    (text . "{\it pivot}:")
    (text . "\newline \tab{2} ")
    (bcStrings (8 15 nz PI))
    (text . "\tab{34} ")
    (bcStrings (8 "0.1" pivot PI))
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{}\tab{2}")
    (text . "Dimension of A & ICN {\it licn}:")
    (text . "\tab{32} \menuitemstyle{}\tab{34}")
    (text . "Dimension of IRN {\it lirn}:")
    (text . "\newline \tab{2} ")
    (bcStrings (6 150 licn PI))
    (text . "\tab{34} ")
    (bcStrings (6 75 lirn PI))
    (text . "\blankline")
    (text . "\menuitemstyle{}\tab{2} Grow value:")
    (radioButtons grow
        ("" "  True" gr_true)
        ("" "  False" gr_false))
    (text . "\blankline")
    (text . "\menuitemstyle{}\tab{2} Lblock value:")
    (radioButtons lblock
        ("" "  True" lb_true)
        ("" "  False" lb_false))
    (text . "\blankline ")
    (text . "\newline \tab{2} ")
    (text . "Ifail is input in three components: ")
    (text . "\blankline ")
    (text . "\menuitemstyle{}\tab{2}")
    (text . "{\it a} ")
    (radioButtons afail
        ("" "  0, hard failure" azero)
        ("" "  1, soft failure" aone))
    (text . "\blankline ")
    (text . "\menuitemstyle{}\tab{2}")
    (text . "{\it b} ")
    (radioButtons bfail
        ("" "  1, print error messages" bone)
        ("" "  0, suppress error messages" bzero))
    (text . "\blankline ")
    (text . "\menuitemstyle{}\tab{2}")
    (text . "{\it c} ")
    (radioButtons cfail
        ("" "  1, print warning messages" cone)
        ("" "  0, suppress warning messages" czero)))
  htMakeDoneButton('"Continue", 'f01brfSolve)
  htShowPage()

f01brfSolve htPage ==
  n :=
    $bcParseOnly => PARSE_-INTEGER htpLabelInputString(htPage, 'n)
    objValUnwrap htpLabelSpadValue(htPage, 'n)
  nz :=
    $bcParseOnly => PARSE_-INTEGER htpLabelInputString(htPage, 'nz)
    objValUnwrap htpLabelSpadValue(htPage, 'nz)
  licn :=
    $bcParseOnly => PARSE_-INTEGER htpLabelInputString(htPage, 'licn)
    objValUnwrap htpLabelSpadValue(htPage, 'licn)
  lirn :=
    $bcParseOnly => PARSE_-INTEGER htpLabelInputString(htPage, 'lirn)
    objValUnwrap htpLabelSpadValue(htPage, 'lirn)
  pivot := htpLabelInputString(htPage, 'pivot)
  gr := htpButtonValue(htPage,'grow)
  grow :=
    gr = 'gr_true => '"true"
    '"false"
  lb := htpButtonValue(htPage,'lblock)
  lblock :=
    lb = 'lb_true => '"true"
    '"false"
  aerror := htpButtonValue(htPage,'afail)
  afail :=
    aerror = 'azero => '0
    '1
  berror := htpButtonValue(htPage,'bfail)
  bfail :=
    berror = 'bone => '1
    '0
  cerror := htpButtonValue(htPage,'cfail)
  cfail :=
    cerror = 'cone => '1
    '0
  ifail := 100*cfail + 10*bfail + afail
  ((n = '6 and nz = '15) and (licn = '150 and lirn = '75))
                => f01brfDefaultSolve(htPage,pivot,grow,lblock,ifail)
  labelList :=
    "append"/[f(i) for i in 1..nz] where f(i) ==
      prefix := ('"\newline \tab{2} ")
      anam := INTERN STRCONC ('"a",STRINGIMAGE i)
      mid := ('"\tab{32} ")
      rnam := INTERN STRCONC ('"irn",STRINGIMAGE i)
      end := ('"\tab{42} ")
      cnam := INTERN STRCONC ('"icn",STRINGIMAGE i)
      [['text,:prefix],['bcStrings,[8, 0.0, anam, 'F]],
        ['text,:mid],['bcStrings,[4, 0, rnam, 'PI]],
         ['text,:end],['bcStrings,[4, 0, cnam, 'PI]]]
  abortList :=
    [['bcStrings,[6, '"true", 'abortone, 'EM]],
      ['bcStrings,[6, '"true", 'aborttwo, 'EM]],
        ['bcStrings,[6, '"false", 'abortthree, 'EM]],
          ['bcStrings,[6, '"true", 'abortfour, 'EM]]]
  prefix := ('"\blankline \menuitemstyle{}\tab{2} Abort: ")
  abortList := [['text,:prefix],:abortList]
  equationPart := [
     '(domainConditions 
        (isDomain P (Polynomial $EmptyMode))
         (isDomain S (String))
          (isDomain EM ($EmptyMode))
           (isDomain F (Float))
            (isDomain PI (PositiveInteger))),
             :labelList,:abortList]
  page :=  htInitPage("F01BRF - LU factorization of real sparse matrix",nil)
  htSay '"\menuitemstyle{}\tab{2} Non-zero elements of A: "
  htSay '"\tab{30} \menuitemstyle{}\tab{32} Row: "
  htSay '"\tab{40} \menuitemstyle{}\tab{42} Column: "
  htMakePage equationPart
  htSay '"\blankline "
  htMakeDoneButton('"Continue",'f01brfGen)
  htpSetProperty(page,'n,n)
  htpSetProperty(page,'nz,nz)
  htpSetProperty(page,'licn,licn)
  htpSetProperty(page,'lirn,lirn)
  htpSetProperty(page,'pivot,pivot)
  htpSetProperty(page,'grow,grow)
  htpSetProperty(page,'lblock,lblock)
  htpSetProperty(page,'ifail,ifail)
  htpSetProperty(page,'inputArea, htpInputAreaAlist htPage)
  htShowPage()


f01brfDefaultSolve(htPage,pivot,grow,lblock,ifail) ==
  n := '6
  nz := '15
  licn := '150
  lirn := '75
  page :=  htInitPage("F01BRF - LU factorization of real sparse matrix",nil)
  htMakePage '(
    (domainConditions 
      (isDomain PI (Positive Integer))
       (isDomain EM $EmptyMode)
       (isDomain F (Float)))
    (text . "\newline ")
    (text . "\menuitemstyle{}\tab{2} Non-zero elements of A: ")
    (text . "\tab{30} \menuitemstyle{}\tab{32} Row: ")
    (text . "\tab{40} \menuitemstyle{}\tab{42} Column: ")
    (text . "\newline \tab{2}")
    (bcStrings (8 "5.0" a1 F)) 
    (text . "\tab{32} ")  
    (bcStrings (4 1 irn1 PI))
    (text . "\tab{42} ")
    (bcStrings (4 1 icn1 PI))
    (text . "\newline \tab{2}")
    (bcStrings (8 "2.0" a2 F)) 
    (text . "\tab{32} ")  
    (bcStrings (4 2 irn2 PI))
    (text . "\tab{42} ")
    (bcStrings (4 2 icn2 PI))
    (text . "\newline \tab{2}")
    (bcStrings (8 "-1.0" a3 F)) 
    (text . "\tab{32} ")  
    (bcStrings (4 2 irn3 PI))
    (text . "\tab{42} ")
    (bcStrings (4 3 icn3 PI))
    (text . "\newline \tab{2}")
    (bcStrings (8 "2.0" a4 F)) 
    (text . "\tab{32} ")  
    (bcStrings (4 2 irn4 PI))
    (text . "\tab{42} ")
    (bcStrings (4 4 icn4 PI))
    (text . "\newline \tab{2}")
    (bcStrings (8 "3.0" a5 F)) 
    (text . "\tab{32} ")  
    (bcStrings (4 3 irn5 PI))
    (text . "\tab{42} ")
    (bcStrings (4 3 icn5 PI))
    (text . "\newline \tab{2}")
    (bcStrings (8 "-2.0" a6 F)) 
    (text . "\tab{32} ")  
    (bcStrings (4 4 irn6 PI))
    (text . "\tab{42} ")
    (bcStrings (4 1 icn6 PI))
    (text . "\newline \tab{2}")
    (bcStrings (8 "1.0" a7 F)) 
    (text . "\tab{32} ")  
    (bcStrings (4 4 irn7 PI))
    (text . "\tab{42} ")
    (bcStrings (4 4 icn7 PI))
    (text . "\newline \tab{2}")
    (bcStrings (8 "1.0" a8 F)) 
    (text . "\tab{32} ")  
    (bcStrings (4 4 irn8 PI))
    (text . "\tab{42} ")
    (bcStrings (4 5 icn8 PI))
    (text . "\newline \tab{2}")
    (bcStrings (8 "-1.0" a9 F)) 
    (text . "\tab{32} ")  
    (bcStrings (4 5 irn9 PI))
    (text . "\tab{42} ")
    (bcStrings (4 1 icn9 PI))
    (text . "\newline \tab{2}")
    (bcStrings (8 "-1.0" a10 F)) 
    (text . "\tab{32} ")  
    (bcStrings (4 5 irn10 PI))
    (text . "\tab{42} ")
    (bcStrings (4 4 icn10 PI))
    (text . "\newline \tab{2}")
    (bcStrings (8 "2.0" a11 F)) 
    (text . "\tab{32} ")  
    (bcStrings (4 5 irn11 PI))
    (text . "\tab{42} ")
    (bcStrings (4 5 icn11 PI))
    (text . "\newline \tab{2}")
    (bcStrings (8 "-3.0" a12 F)) 
    (text . "\tab{32} ")  
    (bcStrings (4 5 irn12 PI))
    (text . "\tab{42} ")
    (bcStrings (4 6 icn12 PI))
    (text . "\newline \tab{2}")
    (bcStrings (8 "-1.0" a13 F)) 
    (text . "\tab{32} ")  
    (bcStrings (4 6 irn13 PI))
    (text . "\tab{42} ")
    (bcStrings (4 1 icn13 PI))
    (text . "\newline \tab{2}")
    (bcStrings (8 "-1.0" a14 F)) 
    (text . "\tab{32} ")  
    (bcStrings (4 6 irn14 PI))
    (text . "\tab{42} ")
    (bcStrings (4 2 icn14 PI))
    (text . "\newline \tab{2}")
    (bcStrings (8 "6.0" a15 F)) 
    (text . "\tab{32} ")  
    (bcStrings (4 6 irn15 PI))
    (text . "\tab{42} ")
    (bcStrings (4 6 icn15 PI))
    (text . "\blankline ")
    (text . "\menuitemstyle{} \tab{2} Abort :")
    (bcStrings (8 "true" abort_one EM))
    (bcStrings (8 "true" abort_two EM))
    (bcStrings (8 "false" abort_three EM))
    (bcStrings (8 "true" abort_four EM)))
  htMakeDoneButton('"Continue",'f01brfGen)      
  htpSetProperty(page,'n,n)
  htpSetProperty(page,'nz,nz)
  htpSetProperty(page,'licn,licn)
  htpSetProperty(page,'lirn,lirn)
  htpSetProperty(page,'pivot,pivot)
  htpSetProperty(page,'grow,grow)
  htpSetProperty(page,'lblock,lblock)
  htpSetProperty(page,'ifail,ifail)
  htpSetProperty(page,'inputArea, htpInputAreaAlist htPage)
  htShowPage()

f01brfGen htPage ==
  n := htpProperty(htPage,'n)
  nz := htpProperty(htPage,'nz)
  licn := htpProperty(htPage,'licn)
  lirn := htpProperty(htPage,'lirn)
  pivot := htpProperty(htPage,'pivot)
  grow := htpProperty(htPage,'grow)
  lblock := htpProperty(htPage,'lblock)
  ifail := htpProperty(htPage,'ifail)
  alist := htpInputAreaAlist htPage
  y := alist
  for i in 1..4 repeat
    abort := STRCONC((first y).1," ")
    y := rest y
    abortList := [abort,:abortList]
  astring := bcwords2liststring abortList
  while y repeat
    end := STRCONC ((first y).1," ")
    y := rest y
    mid := STRCONC ((first y).1," ")
    y := rest y
    top := STRCONC ((first y).1," ")
    y := rest y
    cList := [end,:cList]
    rList := [mid,:rList]
    matList := [top,:matList]
  for i in 1..(licn-nz) repeat
    cList := [:cList,'"0 "]
    matList := [:matList,'"0 "]
  for i in 1..(lirn-nz) repeat
    rList := [:rList,'"0 "]
  cstring := bcwords2liststring cList
  rstring := bcwords2liststring rList
  matstring := bcwords2liststring matList
  prefix := STRCONC('"f01brf(",STRINGIMAGE n,", ",STRINGIMAGE nz,", ")
  prefix := STRCONC(prefix,STRINGIMAGE licn,", ",STRINGIMAGE lirn,", ",pivot)
  prefix := STRCONC(prefix,", ",lblock,", ",grow,", ",astring,",[",matstring)
  prefix := STRCONC(prefix,"],[",rstring,"],[",cstring,"], ")
  linkGen STRCONC(prefix,STRINGIMAGE ifail,")")

f01bsf() ==
  htInitPage("F01BSF - LU factorization of real sparse matrix with known sparsity pattern",nil)
  htMakePage '(
    (domainConditions
       (isDomain F (Float)))
    (text . "\windowlink{Manual Page}{manpageXXf01bsf} for this routine ")
    (text . "\newline ")
    (text . "\lispwindowlink{Browser operation page}{(|oPageFrom| '|f01bsf| '|NagMatrixOperationsPackage|)} for this routine")
    (text . "\newline \horizontalline ")
    (text . "Factorizes a real sparse matrix A of order n using the pivotal ")
    (text . "sequence previously obtained by F01BRF when a matrix of the ")
    (text . "same sparsity pattern was factorized. ")
    (text . "\blankline ")
    (text . "Read the input file to see the example program. ")
    (text . "\spadpaste{)read f01bsf \bound{s0}} ")
    (text . "\blankline")
    (text . "\newline "))
  htShowPage()

f01maf() ==
  htInitPage("F01MAF - \htbitmap{llt} factorization of real sparse symmetric positive-definite matrix",nil)
  htMakePage '(
    (domainConditions
       (isDomain F (Float)))
    (text . "\windowlink{Manual Page}{manpageXXf01maf} for this routine ")
    (text . "\newline ")
    (text . "\lispwindowlink{Browser operation page}{(|oPageFrom| '|f01maf| '|NagMatrixOperationsPackage|)} for this routine")
    (text . "\newline \horizontalline ")
    (text . "Computes an incomplete Cholesky factorization of a real ")
    (text . "sparse symmetric positive-definite matrix A of order n. ")
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{}\tab{2}")
    (text . "Enter the order {\em n} of the matrix A ")
    (text . "\htbitmap{great=} 1:")
    (text . "\newline\tab{2} ")
    (bcStrings (8 16 n PI))
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{}\tab{2}")
    (text . "Number of non-zero elements {\it nz}:")
    (text . "\newline \tab{2} ")
    (bcStrings (8 40 nz PI))
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{}\tab{2}")
    (text . "Dimension of A & ICN {\it licn}:")
    (text . "\tab{32} \menuitemstyle{}\tab{34}")
    (text . "Dimension of IRN {\it lirn}:")
    (text . "\newline \tab{2} ")
    (bcStrings (6 90 licn PI))
    (text . "\tab{34} ")
    (bcStrings (6 50 lirn PI))
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{}\tab{2}")
    (text . "Tolerance {\it droptl}: ")
    (text . "\tab{32} \menuitemstyle{}\tab{34}")
    (text . "{\it densw}:")
    (text . "\newline \tab{2} ")
    (bcStrings (6 "0.1" droptl F))
    (text . "\tab{34} ")
    (bcStrings (6 "0.8" densw F))
    (text . "\blankline ")
    (text . "\newline \tab{2} ")
    (text . "Ifail is input in three components: ")
    (text . "\blankline ")
    (text . "\menuitemstyle{}\tab{2}")
    (text . "{\it a} ")
    (radioButtons afail
        ("" "  0, hard failure" azero)
        ("" "  1, soft failure" aone))
    (text . "\blankline ")
    (text . "\menuitemstyle{}\tab{2}")
    (text . "{\it b} ")
    (radioButtons bfail
        ("" "  1, print error messages" bone)
        ("" "  0, suppress error messages" bzero))
    (text . "\blankline ")
    (text . "\menuitemstyle{}\tab{2}")
    (text . "{\it c} ")
    (radioButtons cfail
        ("" "  1, print warning messages" cone)
        ("" "  0, suppress warning messages" czero)))
  htMakeDoneButton('"Continue", 'f01mafSolve)
  htShowPage()

f01mafSolve htPage ==
  n :=
    $bcParseOnly => PARSE_-INTEGER htpLabelInputString(htPage, 'n)
    objValUnwrap htpLabelSpadValue(htPage, 'n)
  nz :=
    $bcParseOnly => PARSE_-INTEGER htpLabelInputString(htPage, 'nz)
    objValUnwrap htpLabelSpadValue(htPage, 'nz)
  licn :=
    $bcParseOnly => PARSE_-INTEGER htpLabelInputString(htPage, 'licn)
    objValUnwrap htpLabelSpadValue(htPage, 'licn)
  lirn :=
    $bcParseOnly => PARSE_-INTEGER htpLabelInputString(htPage, 'lirn)
    objValUnwrap htpLabelSpadValue(htPage, 'lirn)
  aerror := htpButtonValue(htPage,'afail)
  afail :=
    aerror = 'azero => '0
    '1
  berror := htpButtonValue(htPage,'bfail)
  bfail :=
    berror = 'bone => '1
    '0
  cerror := htpButtonValue(htPage,'cfail)
  cfail :=
    cerror = 'cone => '1
    '0
  ifail := 100*cfail + 10*bfail + afail
  droptl := htpLabelInputString(htPage, 'droptl)
  densw := htpLabelInputString(htPage, 'densw)
  ((n = '16 and nz = '40) and (licn = '90 and lirn = '50))
                => f01mafDefaultSolve(htPage,droptl,densw,ifail)
  labelList :=
    "append"/[f(i) for i in 1..nz] where f(i) ==
      prefix := ('"\newline \tab{2} ")
      anam := INTERN STRCONC ('"a",STRINGIMAGE i)
      mid := ('"\tab{32} ")
      rnam := INTERN STRCONC ('"irn",STRINGIMAGE i)
      end := ('"\tab{42} ")
      cnam := INTERN STRCONC ('"icn",STRINGIMAGE i)
      [['text,:prefix],['bcStrings,[8, 0.0, anam, 'F]],
        ['text,:mid],['bcStrings,[4, 0, rnam, 'PI]],
         ['text,:end],['bcStrings,[4, 0, cnam, 'PI]]]
  abortList :=
    [['bcStrings,[6, '"true", 'abortone, 'EM]],
      ['bcStrings,[6, '"true", 'aborttwo, 'EM]],
        ['bcStrings,[6, '"true", 'abortthree, 'EM]]]
  prefix := ('"\blankline \menuitemstyle{}\tab{2} Abort: ")
  abortList := [['text,:prefix],:abortList]
  equationPart := [
     '(domainConditions 
        (isDomain P (Polynomial $EmptyMode))
         (isDomain S (String))
          (isDomain EM ($EmptyMode))
           (isDomain F (Float))
            (isDomain PI (PositiveInteger))),
             :labelList,:abortList]
  page :=  htInitPage("F01MAF - \htbitmap{llt} factorization of real sparse symmetric positive-definite matrix",nil)
  htSay '"\menuitemstyle{}\tab{2} Non-zero elements of A: "
  htSay '"\tab{30} \menuitemstyle{}\tab{32} Row: "
  htSay '"\tab{40} \menuitemstyle{}\tab{42} Column: "
  htMakePage equationPart
  htSay '"\blankline "
  htMakeDoneButton('"Continue",'f01mafGen)
  htpSetProperty(page,'n,n)
  htpSetProperty(page,'nz,nz)
  htpSetProperty(page,'licn,licn)
  htpSetProperty(page,'lirn,lirn)
  htpSetProperty(page,'droptl,droptl)
  htpSetProperty(page,'densw,densw)
  htpSetProperty(page,'ifail,ifail)
  htpSetProperty(page,'inputArea, htpInputAreaAlist htPage)
  htShowPage()


f01mafDefaultSolve(htPage,droptl,densw,ifail) ==
  n := '16
  nz := '40
  licn := '90
  lirn := '50
  page :=  htInitPage("F01MAF - \htbitmap{llt} factorization of real sparse symmetric positive-definite matrix",nil)
  htMakePage '(
    (domainConditions 
      (isDomain PI (Positive Integer))
       (isDomain EM $EmptyMode)
       (isDomain F (Float)))
    (text . "\newline ")
    (text . "\menuitemstyle{}\tab{2} Non-zero elements of A: ")
    (text . "\tab{30} \menuitemstyle{}\tab{32} Row: ")
    (text . "\tab{40} \menuitemstyle{}\tab{42} Column: ")
    (text . "\newline \tab{2}")
    (bcStrings (8 "1.0" a1 F)) 
    (text . "\tab{32} ")  
    (bcStrings (4 1 irn1 PI))
    (text . "\tab{42} ")
    (bcStrings (4 1 icn1 PI))
    (text . "\newline \tab{2}")
    (bcStrings (8 "1.0" a2 F)) 
    (text . "\tab{32} ")  
    (bcStrings (4 2 irn2 PI))
    (text . "\tab{42} ")
    (bcStrings (4 2 icn2 PI))
    (text . "\newline \tab{2}")
    (bcStrings (8 "1.0" a3 F)) 
    (text . "\tab{32} ")  
    (bcStrings (4 3 irn3 PI))
    (text . "\tab{42} ")
    (bcStrings (4 3 icn3 PI))
    (text . "\newline \tab{2}")
    (bcStrings (8 "1.0" a4 F)) 
    (text . "\tab{32} ")  
    (bcStrings (4 4 irn4 PI))
    (text . "\tab{42} ")
    (bcStrings (4 4 icn4 PI))
    (text . "\newline \tab{2}")
    (bcStrings (8 "1.0" a5 F)) 
    (text . "\tab{32} ")  
    (bcStrings (4 5 irn5 PI))
    (text . "\tab{42} ")
    (bcStrings (4 5 icn5 PI))
    (text . "\newline \tab{2}")
    (bcStrings (8 "1.0" a6 F)) 
    (text . "\tab{32} ")  
    (bcStrings (4 6 irn6 PI))
    (text . "\tab{42} ")
    (bcStrings (4 6 icn6 PI))
    (text . "\newline \tab{2}")
    (bcStrings (8 "1.0" a7 F)) 
    (text . "\tab{32} ")  
    (bcStrings (4 7 irn7 PI))
    (text . "\tab{42} ")
    (bcStrings (4 7 icn7 PI))
    (text . "\newline \tab{2}")
    (bcStrings (8 "1.0" a8 F)) 
    (text . "\tab{32} ")  
    (bcStrings (4 8 irn8 PI))
    (text . "\tab{42} ")
    (bcStrings (4 8 icn8 PI))
    (text . "\newline \tab{2}")
    (bcStrings (8 "1.0" a9 F)) 
    (text . "\tab{32} ")  
    (bcStrings (4 9 irn9 PI))
    (text . "\tab{42} ")
    (bcStrings (4 9 icn9 PI))
    (text . "\newline \tab{2}")
    (bcStrings (8 "1.0" a10 F)) 
    (text . "\tab{32} ")  
    (bcStrings (4 10 irn10 PI))
    (text . "\tab{42} ")
    (bcStrings (4 10 icn10 PI))
    (text . "\newline \tab{2}")
    (bcStrings (8 "1.0" a11 F)) 
    (text . "\tab{32} ")  
    (bcStrings (4 11 irn11 PI))
    (text . "\tab{42} ")
    (bcStrings (4 11 icn11 PI))
    (text . "\newline \tab{2}")
    (bcStrings (8 "1.0" a12 F)) 
    (text . "\tab{32} ")  
    (bcStrings (4 12 irn12 PI))
    (text . "\tab{42} ")
    (bcStrings (4 12 icn12 PI))
    (text . "\newline \tab{2}")
    (bcStrings (8 "1.0" a13 F)) 
    (text . "\tab{32} ")  
    (bcStrings (4 13 irn13 PI))
    (text . "\tab{42} ")
    (bcStrings (4 13 icn13 PI))
    (text . "\newline \tab{2}")
    (bcStrings (8 "1.0" a14 F)) 
    (text . "\tab{32} ")  
    (bcStrings (4 14 irn14 PI))
    (text . "\tab{42} ")
    (bcStrings (4 14 icn14 PI))
    (text . "\newline \tab{2}")
    (bcStrings (8 "1.0" a15 F)) 
    (text . "\tab{32} ")  
    (bcStrings (4 15 irn15 PI))
    (text . "\tab{42} ")
    (bcStrings (4 15 icn15 PI))
    (text . "\blankline ")
    (text . "\newline \tab{2}")
    (bcStrings (8 "1.0" a16 F)) 
    (text . "\tab{32} ")  
    (bcStrings (4 16 irn16 PI))
    (text . "\tab{42} ")
    (bcStrings (4 16 icn16 PI))
    (text . "\newline \tab{2}")
    (bcStrings (8 "-0.25" a17 F)) 
    (text . "\tab{32} ")  
    (bcStrings (4 1 irn17 PI))
    (text . "\tab{42} ")
    (bcStrings (4 2 icn17 PI))
    (text . "\newline \tab{2}")
    (bcStrings (8 "-0.25" a18 F)) 
    (text . "\tab{32} ")  
    (bcStrings (4 2 irn18 PI))
    (text . "\tab{42} ")
    (bcStrings (4 3 icn18 PI))
    (text . "\newline \tab{2}")
    (bcStrings (8 "-0.25" a19 F)) 
    (text . "\tab{32} ")  
    (bcStrings (4 3 irn19 PI))
    (text . "\tab{42} ")
    (bcStrings (4 4 icn19 PI))
    (text . "\newline \tab{2}")
    (bcStrings (8 "-0.25" a20 F)) 
    (text . "\tab{32} ")  
    (bcStrings (4 5 irn20 PI))
    (text . "\tab{42} ")
    (bcStrings (4 6 icn20 PI))
    (text . "\newline \tab{2}")
    (bcStrings (8 "-0.25" a21 F)) 
    (text . "\tab{32} ")  
    (bcStrings (4 6 irn21 PI))
    (text . "\tab{42} ")
    (bcStrings (4 7 icn21 PI))
    (text . "\newline \tab{2}")
    (bcStrings (8 "-0.25" a22 F)) 
    (text . "\tab{32} ")  
    (bcStrings (4 7 irn22 PI))
    (text . "\tab{42} ")
    (bcStrings (4 8 icn22 PI))
    (text . "\newline \tab{2}")
    (bcStrings (8 "-0.25" a23 F)) 
    (text . "\tab{32} ")  
    (bcStrings (4 9 irn23 PI))
    (text . "\tab{42} ")
    (bcStrings (4 10 icn23 PI))
    (text . "\newline \tab{2}")
    (bcStrings (8 "-0.25" a24 F)) 
    (text . "\tab{32} ")  
    (bcStrings (4 10 irn24 PI))
    (text . "\tab{42} ")
    (bcStrings (4 11 icn24 PI))
    (text . "\newline \tab{2}")
    (bcStrings (8 "-0.25" a25 F)) 
    (text . "\tab{32} ")  
    (bcStrings (4 11 irn25 PI))
    (text . "\tab{42} ")
    (bcStrings (4 12 icn25 PI))
    (text . "\newline \tab{2}")
    (bcStrings (8 "-0.25" a26 F)) 
    (text . "\tab{32} ")  
    (bcStrings (4 13 irn26 PI))
    (text . "\tab{42} ")
    (bcStrings (4 14 icn26 PI))
    (text . "\newline \tab{2}")
    (bcStrings (8 "-0.25" a27 F)) 
    (text . "\tab{32} ")  
    (bcStrings (4 14 irn27 PI))
    (text . "\tab{42} ")
    (bcStrings (4 15 icn27 PI))
    (text . "\newline \tab{2}")
    (bcStrings (8 "-0.25" a28 F)) 
    (text . "\tab{32} ")  
    (bcStrings (4 15 irn28 PI))
    (text . "\tab{42} ")
    (bcStrings (4 16 icn28 PI))
    (text . "\newline \tab{2}")
    (bcStrings (8 "-0.25" a29 F)) 
    (text . "\tab{32} ")  
    (bcStrings (4 1 irn29 PI))
    (text . "\tab{42} ")
    (bcStrings (4 5 icn29 PI))
    (text . "\newline \tab{2}")
    (bcStrings (8 "-0.25" a30 F)) 
    (text . "\tab{32} ")  
    (bcStrings (4 2 irn30 PI))
    (text . "\tab{42} ")
    (bcStrings (4 6 icn30 PI))
    (text . "\newline \tab{2}")
    (bcStrings (8 "-0.25" a31 F)) 
    (text . "\tab{32} ")  
    (bcStrings (4 3 irn31 PI))
    (text . "\tab{42} ")
    (bcStrings (4 7 icn31 PI))
    (text . "\newline \tab{2}")
    (bcStrings (8 "-0.25" a32 F)) 
    (text . "\tab{32} ")  
    (bcStrings (4 4 irn32 PI))
    (text . "\tab{42} ")
    (bcStrings (4 8 icn32 PI))
    (text . "\newline \tab{2}")
    (bcStrings (8 "-0.25" a33 F)) 
    (text . "\tab{32} ")  
    (bcStrings (4 5 irn33 PI))
    (text . "\tab{42} ")
    (bcStrings (4 9 icn33 PI))
    (text . "\newline \tab{2}")
    (bcStrings (8 "-0.25" a34 F)) 
    (text . "\tab{32} ")  
    (bcStrings (4 6 irn34 PI))
    (text . "\tab{42} ")
    (bcStrings (4 10 icn34 PI))
    (text . "\newline \tab{2}")
    (bcStrings (8 "-0.25" a35 F)) 
    (text . "\tab{32} ")  
    (bcStrings (4 7 irn35 PI))
    (text . "\tab{42} ")
    (bcStrings (4 11 icn35 PI))
    (text . "\newline \tab{2}")
    (bcStrings (8 "-0.25" a36 F)) 
    (text . "\tab{32} ")  
    (bcStrings (4 8 irn36 PI))
    (text . "\tab{42} ")
    (bcStrings (4 12 icn36 PI))
    (text . "\newline \tab{2}")
    (bcStrings (8 "-0.25" a37 F)) 
    (text . "\tab{32} ")  
    (bcStrings (4 9 irn37 PI))
    (text . "\tab{42} ")
    (bcStrings (4 13 icn37 PI))
    (text . "\newline \tab{2}")
    (bcStrings (8 "-0.25" a38 F)) 
    (text . "\tab{32} ")  
    (bcStrings (4 10 irn38 PI))
    (text . "\tab{42} ")
    (bcStrings (4 14 icn38 PI))
    (text . "\newline \tab{2}")
    (bcStrings (8 "-0.25" a39 F)) 
    (text . "\tab{32} ")  
    (bcStrings (4 11 irn39 PI))
    (text . "\tab{42} ")
    (bcStrings (4 15 icn39 PI))
    (text . "\newline \tab{2}")
    (bcStrings (8 "-0.25" a40 F)) 
    (text . "\tab{32} ")  
    (bcStrings (4 12 irn40 PI))
    (text . "\tab{42} ")
    (bcStrings (4 16 icn40 PI))
    (text . "\blankline ")
    (text . "\menuitemstyle{} \tab{2} Abort :")
    (bcStrings (8 "true" abort_one EM))
    (bcStrings (8 "true" abort_two EM))
    (bcStrings (8 "true" abort_three EM)))
  htMakeDoneButton('"Continue",'f01mafGen)      
  htpSetProperty(page,'n,n)
  htpSetProperty(page,'nz,nz)
  htpSetProperty(page,'licn,licn)
  htpSetProperty(page,'lirn,lirn)
  htpSetProperty(page,'droptl,droptl)
  htpSetProperty(page,'densw,densw)
  htpSetProperty(page,'ifail,ifail)
  htpSetProperty(page,'inputArea, htpInputAreaAlist htPage)
  htShowPage()

f01mafGen htPage ==
  n := htpProperty(htPage,'n)
  nz := htpProperty(htPage,'nz)
  licn := htpProperty(htPage,'licn)
  lirn := htpProperty(htPage,'lirn)
  droptl := htpProperty(htPage,'droptl)
  densw := htpProperty(htPage,'densw)
  ifail := htpProperty(htPage,'ifail)
  alist := htpInputAreaAlist htPage
  y := alist
  for i in 1..3 repeat
    abort := STRCONC((first y).1," ")
    y := rest y
    abortList := [abort,:abortList]
  astring := bcwords2liststring abortList
  while y repeat
    end := STRCONC ((first y).1," ")
    y := rest y
    mid := STRCONC ((first y).1," ")
    y := rest y
    top := STRCONC ((first y).1," ")
    y := rest y
    cList := [end,:cList]
    rList := [mid,:rList]
    matList := [top,:matList]
  for i in 1..(licn-nz) repeat
    cList := [:cList,'"0 "]
    matList := [:matList,'"0 "]
  for i in 1..(lirn-nz) repeat
    rList := [:rList,'"0 "]
  cstring := bcwords2liststring cList
  rstring := bcwords2liststring rList
  matstring := bcwords2liststring matList
  prefix := STRCONC('"f01maf(",STRINGIMAGE n,", ",STRINGIMAGE nz,", ")
  prefix := STRCONC(prefix,STRINGIMAGE licn,", ",STRINGIMAGE lirn,", ")
  prefix := STRCONC(prefix,astring,",[",matstring)
  prefix := STRCONC(prefix,"],[",rstring,"],[",cstring,"], ",droptl,", ",densw)
  linkGen STRCONC(prefix,", ",STRINGIMAGE ifail,")")




f01mcf() ==
  htInitPage("F01MCF - \htbitmap{ldlt} factorization of real symmetric positive-definite variable-bandwidth matrix",nil)
  htMakePage '(
    (domainConditions
       (isDomain F (Float)))
    (text . "\windowlink{Manual Page}{manpageXXf01mcf} for this routine ")
    (text . "\newline ")
    (text . "\lispwindowlink{Browser operation page}{(|oPageFrom| '|f01mcf| '|NagMatrixOperationsPackage|)} for this routine")
    (text . "\newline \horizontalline ")
    (text . "Computes the Cholesky factorization of a real symmetric positive")
    (text . "-definite variable-bandwidth matrix {\it A} or order {\it n}. ")
    (text . "That is, {\it A = }\htbitmap{ldlt}, where {\it L} is ")
    (text . "a unit lower triangular matrix and {\it D} is a diagonal matrix.")
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{}\tab{2}")
    (text . "Enter the order {\em n} of the matrix A ")
    (text . "\htbitmap{great=} 1:")
    (text . "\newline\tab{2} ")
    (bcStrings (9 6 n PI))
    (text . "\blankline")
    (text . "\newline ")
    (text . "\menuitemstyle{}\tab{2}")
    (text . "\newline Enter the number of elements: ")
    (text . "\newline\tab{2} ")
    (bcStrings (9 14 lal PI))
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{}\tab{2}")
    (text . "Ifail value:")
    (radioButtons ifail
        ("" " -1, Print error messages" minusOne)
        ("" "  1, Suppress error messages" one)))
  htMakeDoneButton('"Continue", 'f01mcfSolve)
  htShowPage()

f01mcfSolve htPage ==
  n :=
    $bcParseOnly => PARSE_-INTEGER htpLabelInputString(htPage, 'n)
    objValUnwrap htpLabelSpadValue(htPage, 'n)
  lal :=
    $bcParseOnly => PARSE_-INTEGER htpLabelInputString(htPage, 'lal)
    objValUnwrap htpLabelSpadValue(htPage, 'lal)
  error := htpButtonValue(htPage,'ifail)
  ifail :=
    error = 'one => '1
    '-1
  (n = '6 and lal = '14) => f01mcfDefaultSolve(htPage,ifail)
  labelList :=
    "append"/[f(i) for i in 1..lal] where f(i) ==
      xnam := INTERN STRCONC ('"x",STRINGIMAGE i)
      [['bcStrings,[6, 0.0, xnam, 'F]]]
  nrowList :=
    "append"/[g(j) for j in 1..n] where g(j) ==
      nam := INTERN STRCONC ('"n",STRINGIMAGE j) 
      [['bcStrings,[6, 0, nam, 'PI]]]
  prefix := ('"\blankline \menuitemstyle{}\tab{2} {\it NROW(n)} the width ")
  prefix := STRCONC(prefix,"of the ith row of A: \newline \tab{2} ")
  nrowList := [['text,:prefix],:nrowList]
  equationPart := [
     '(domainConditions 
        (isDomain P (Polynomial $EmptyMode))
         (isDomain S (String))
          (isDomain F (Float))
           (isDomain PI (PositiveInteger))),
            :labelList,:nrowList]
  page := htInitPage("F01MCF - \htbitmap{ldlt} factorization of real symmetric positive-definite variable-bandwidth matrix",nil)
  htSay '"\menuitemstyle{}\tab{2} Elements of matrix {\it A} in row by row "
  htSay '"order: \newline \tab{2} "
  htMakePage equationPart
  htSay '"\blankline "
  htMakeDoneButton('"Continue",'f01mcfGen)
  htpSetProperty(page,'n,n)
  htpSetProperty(page,'lal,lal)
  htpSetProperty(page,'ifail,ifail)
  htpSetProperty(page,'inputArea, htpInputAreaAlist htPage)
  htShowPage()


f01mcfDefaultSolve  (htPage,ifail) ==
  n := '6
  lal := '14
  page :=   htInitPage("F01MCF - \htbitmap{ldlt} factorization of real symmetric positive-definite variable-bandwidth matrix",nil)
  htMakePage '(
    (domainConditions 
      (isDomain PI (Positive Integer))
       (isDomain F (Float)))
    (text . "\newline ")
    (text . "\menuitemstyle{}\tab{2} Elements of matrix {\it A} in row by ")
    (text . "row order: ") 
    (text . "\newline ")
    (bcStrings (6 "1.0" x1 F))
    (bcStrings (6 "2.0" x2 F))
    (bcStrings (6 "5.0" x3 F))
    (bcStrings (6 "3.0" x4 F))
    (bcStrings (6 "13.0" x5 F))
    (bcStrings (6 "16.0" x6 F))
    (bcStrings (6 "5.0" x7 F))
    (bcStrings (6 "14.0" x8 F))
    (bcStrings (6 "18.0" x9 F))
    (bcStrings (6 "8.0" x10 F))
    (bcStrings (6 "55.0" x11 F))
    (bcStrings (6 "24.0" x12 F))
    (bcStrings (6 "17.0" x13 F))
    (bcStrings (6 "77.0" x14 F))
    (text . "\blankline ")
    (text . "\menuitemstyle{}\tab{2} {\it NROW(n)} the width of the ith row ")
    (text . "of A: ") 
    (text . "\newline ")
    (bcStrings (6 1 n1 PI))
    (bcStrings (6 2 n2 PI))
    (bcStrings (6 2 n3 PI))
    (bcStrings (6 1 n4 PI))
    (bcStrings (6 5 n5 PI))
    (bcStrings (6 3 n6 PI))
    (text . "\blankline "))
  htMakeDoneButton('"Continue",'f01mcfGen)      
  htpSetProperty(page,'n,n)
  htpSetProperty(page,'lal,lal)
  htpSetProperty(page,'ifail,ifail)
  htpSetProperty(page,'inputArea, htpInputAreaAlist htPage)
  htShowPage()

f01mcfGen htPage ==
  n := htpProperty(htPage,'n)
  lal := htpProperty(htPage,'lal)
  ifail := htpProperty(htPage,'ifail)
  alist := htpInputAreaAlist htPage
  y := alist
  for i in 1..n repeat
    right := STRCONC ((first y).1," ")
    y := rest y    
    nrowList := [right,:nrowList]
  nrowstring := bcwords2liststring nrowList
  while y repeat
    right := STRCONC ((first y).1," ")
    y := rest y    
    matList := [right,:matList]
  matstring := bcwords2liststring matList
  prefix := STRCONC('"f01mcf(",STRINGIMAGE n,", [",matstring,"], ")
  prefix := STRCONC(prefix,STRINGIMAGE lal,", [",nrowstring,"], ")
  linkGen STRCONC(prefix,STRINGIMAGE ifail,")")


f01qcf() ==
  htInitPage('"F01QCF - QR factorization or real m by n matrix (m \htbitmap{great=} n)",nil)
  htMakePage '(
    (domainConditions 
       (isDomain F (Float))
         (isDomain PI (PositiveInteger)))
    (text . "\windowlink{Manual Page}{manpageXXf01qcf} for this routine ")
    (text . "\newline ")
    (text . "\lispwindowlink{Browser operation page}{(|oPageFrom| '|f01qcf| '|NagMatrixOperationsPackage|)} for this routine")
    (text . "\newline \horizontalline ")
    (text . "\newline ")
    (text . "Finds the QR factorization of a real {\it m} by {it n} ({\it m ")
    (text . "\htbitmap{great=} n}) matrix {\it A}, which ")
    (text . "is factorized as \htbitmap{f01qcf}, ")
    (text . "where {\it m > n} and {\it A = QR } when {\it m = n }, where ")
    (text . "{\it Q} is an {\it m} by {\it m } orthogonal matrix and {\it R} ")
    (text . "is an {\it n} by {\it n} upper triangular matrix. The {\it k}th ")
    (text . "transformation matrix,{\it Qk}, ")
    (text . "which is used to introduce zeros into the {\it k}th column of ")
    (text . "{\it A}, is given in the form ")
    (text . "\htbitmap{f01qcf1}, ")
    (text . "where \htbitmap{f01qcf2}, ")
    (text . "\htbitmap{f01qcf3},  ")
    (text . "\htbitmap{zetak} is a scalar and ")
    (text . "\htbitmap{zk} is an (m-k) element vector. ")
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{} \tab{2} ")
    (text . "\newline Rows of matrix A, {\it m}: ")
    (text . "\tab{32}  \menuitemstyle{} \tab{34} ")
    (text . "Columns of matrix A, {\it n}: \newline \tab{2} ")
    (bcStrings (6 5 m PI))
    (text . "\tab{34} ")
    (bcStrings (6 3 n PI))
--    (text . "\blankline ")
--    (text . "\newline ")
--    (text . "\menuitemstyle{} \tab{2} ")
--    (text . "\newline First dimension of A, {\it lda} ")
--    (text . "\htbitmap{great=} m: ")
--    (text . "\newline \tab{2} ")
--    (bcStrings (6 5 lda PI))
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{} \tab{2} ")
    (text . "\newline \tab{2} ")
    (text . "Ifail value: ")
    (radioButtons ifail
        ("" " -1, Print error messages" minusOne)
        ("" "  1, Suppress error messages" one)))
  htMakeDoneButton('"Continue", 'f01qcfSolve)
  htShowPage()

f01qcfSolve htPage ==
  m :=
    $bcParseOnly => PARSE_-INTEGER htpLabelInputString(htPage, 'm)
    objValUnwrap htpLabelSpadValue(htPage, 'm)
  n :=
    $bcParseOnly => PARSE_-INTEGER htpLabelInputString(htPage, 'n)
    objValUnwrap htpLabelSpadValue(htPage, 'n)
  lda := m
--    $bcParseOnly => PARSE_-INTEGER htpLabelInputString(htPage, 'lda)
--    objValUnwrap htpLabelSpadValue(htPage, 'lda)
  error := htpButtonValue(htPage,'ifail)
  ifail :=
    error = 'one => '1
    '-1
  (m = '5 and n = '3)  => f01qcfDefaultSolve(htPage,lda,ifail)
  matList :=
    "append"/[f(i,n) for i in 1..lda] where f(i,n) ==
       labelList := 
         "append"/[g(i,j) for j in 1..n] where g(i,j) ==
            anam := INTERN STRCONC ('"a",STRINGIMAGE i, STRINGIMAGE j)
            [['bcStrings,[6, "0.0", anam, 'F]]]
       prefix := ('"\newline \tab{2} ")
       labelList := [['text,:prefix],:labelList]
  equationPart := [
     '(domainConditions 
        (isDomain P (Polynomial $EmptyMode))
         (isDomain S (String))
          (isDomain F (Float))
           (isDomain PI (PositiveInteger))),
            :matList]
  page := htInitPage("F01QCF - QR factorization or real m by n matrix (m \htbitmap{great=} n)",nil)
  htSay '"\newline \menuitemstyle{}\tab{2} Enter values of {\it A}: "
  htSay '"\newline \tab{2} "
  htMakePage equationPart
  htSay '"\blankline "
  htMakeDoneButton('"Continue",'f01qcfGen)
  htpSetProperty(page,'n,n)
  htpSetProperty(page,'m,m)
--  htpSetProperty(page,'lda,lda)
  htpSetProperty(page,'ifail,ifail)
  htpSetProperty(page,'inputArea, htpInputAreaAlist htPage)
  htShowPage()


f01qcfDefaultSolve  (htPage,lda,ifail) == 
  n := '3
  m := '5
  page := htInitPage('"F01QCF - QR factorization or real m by n matrix (m \htbitmap{great=} n)",nil)
  htMakePage '(
    (domainConditions
       (isDomain F (Float))
         (isDomain I (Integer)))
    (text . "\newline ")
    (text . "\menuitemstyle{}\tab{2} Enter values of {\it A}: ")
    (text . "\newline \tab{2} ")
    (bcStrings (6 "2.0" a11 F))
    (bcStrings (6 "2.5" a12 F))
    (bcStrings (6 "2.5" a13 F))
    (text . "\newline \tab{2} ")
    (bcStrings (6 "2.0" a21 F))
    (bcStrings (6 "2.5" a22 F))
    (bcStrings (6 "2.5" a23 F))
    (text . "\newline \tab{2} ")
    (bcStrings (6 "1.6" a31 F))
    (bcStrings (6 "-0.4" a32 F))
    (bcStrings (6 "2.8" a33 F))
    (text . "\newline \tab{2} ")
    (bcStrings (6 "2.0" a41 F))
    (bcStrings (6 "-0.5" a42 F))
    (bcStrings (6 "0.5" a43 F))
    (text . "\newline \tab{2} ")
    (bcStrings (6 "1.2" a51 F))
    (bcStrings (6 "-0.3" a52 F))
    (bcStrings (6 "-2.9" a53 F)))
  htMakeDoneButton('"Continue",'f01qcfGen)
  htpSetProperty(page,'n,n)
  htpSetProperty(page,'m,m)
--  htpSetProperty(page,'lda,lda)
  htpSetProperty(page,'ifail,ifail)
  htpSetProperty(page,'inputArea, htpInputAreaAlist htPage)
  htShowPage()

f01qcfGen htPage ==
  n := htpProperty(htPage,'n)
  m := htpProperty(htPage,'m)
--  lda := htpProperty(htPage,'lda)
  lda := m
  ifail := htpProperty(htPage,'ifail)
  alist := htpInputAreaAlist htPage
  y := alist
  y := REVERSE y
  for i in 1..lda repeat
    for j in 1..n repeat
      elm := STRCONC((first y).1," ")
      rowList := [:rowList,elm]
      y := rest y
    matform := [:matform,rowList]
    rowList := []
  matstring := bcwords2liststring [bcwords2liststring x for x in matform]
  prefix := STRCONC('"f01qcf(",STRINGIMAGE m,", ",STRINGIMAGE n,", ")
  prefix := STRCONC(prefix,STRINGIMAGE lda,", ",matstring,", ")
  prefix := STRCONC(prefix,STRINGIMAGE ifail,")")
  linkGen prefix

f01qdf() ==
  htInitPage('"F01QDF - Operations with orthogonal matrices, compute {\it QB} or \htbitmap{f01qdf} after factorization by F01QCF or F01QDF",nil)
  htMakePage '(
    (domainConditions 
       (isDomain F (Float))
         (isDomain PI (PositiveInteger)))
    (text . "\windowlink{Manual Page}{manpageXXf01qdf} for this routine ")
    (text . "\newline ")
    (text . "\lispwindowlink{Browser operation page}{(|oPageFrom| '|f01qdf| '|NagMatrixOperationsPackage|)} for this routine")
    (text . "\newline \horizontalline ")
    (text . "\newline ")
    (text . "Performs one of the transformations {\it B = QB or B = }")
    (text . "\htbitmap{f01qdf}, where {\it B} is a real {\it m} ")
    (text . "by {\it ncolb} matrix and {\it Q} is an {\it m} by {\it m} ")
    (text . "orthogonal matrix assumed to be given by {\it Q = }")
    (text . "\htbitmap{f01qdf1}, \htbitmap{f01qdf2} ")
    (text . "being given in the form ")
    (text . "\htbitmap{f01qcf1}, ")
    (text . "where \htbitmap{f01qcf2}, ")
    (text . "\htbitmap{f01qcf3},  ")
    (text . "\htbitmap{zetak} is a scalar and ")
    (text . "\htbitmap{zk} is an (m-k) element vector. ")
    (text . "The routine is intended for use following F01QCF or F01QFF. ")
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{} \tab{2} ")
    (text . "\newline Rows of matrix A, {\it m}: ")
    (text . "\tab{32}  \menuitemstyle{} \tab{34} ")
    (text . "Columns of matrix A, {\it n}: \newline \tab{2} ")
    (bcStrings (6 5 m PI))
    (text . "\tab{34} ")
    (bcStrings (6 3 n PI))
--    (text . "\blankline ")
--    (text . "\newline ")
--    (text . "\menuitemstyle{} \tab{2} ")
--    (text . "\newline First dimension of A, {\it lda} ")
--    (text . "\htbitmap{great=} m: ")
--    (text . "\tab{32}  \menuitemstyle{} \tab{34} ")
--    (text . "First dimension of B, {\it ldb} ")
--    (text . "\htbitmap{great=} m: ")
--    (text . "\newline \tab{2} ")
--    (bcStrings (6 5 lda PI))
--    (text . "\tab{34} ")
--    (bcStrings (6 5 ldb PI))
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{} \tab{2} ")
    (text . "\newline \tab{2} ")
    (text . "Number of columns of matrix B {\it ncolb}: ")
    (text . "\newline \tab{2} ")
    (bcStrings (6 2 ncolb PI))
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{} \tab{2} ")
    (text . "\newline \tab{2} ")
    (text . "Transformation to be performed: ")
    (radioButtons trans
        (" " "  {\it B = QB}" no_trans)
        (" " "  {\it B =} \htbitmap{f01qdf}" trans))
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{} \tab{2} ")
    (text . "\newline \tab{2} ")
    (text . "Where the elements can be found: ")
    (radioButtons wheret
        (" " "  the elements of \zeta are in A" in_a)
        (" " "  the elements of \zeta are in ZETA, returned by F01QCF/F01QFF" seperate))
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{} \tab{2} ")
    (text . "\newline \tab{2} ")
    (text . "Ifail value: ")
    (radioButtons ifail
        ("" " -1, Print error messages" minusOne)
        ("" "  1, Suppress error messages" one)))
  htMakeDoneButton('"Continue", 'f01qdfSolve)
  htShowPage()

f01qdfSolve htPage ==
  m :=
    $bcParseOnly => PARSE_-INTEGER htpLabelInputString(htPage, 'm)
    objValUnwrap htpLabelSpadValue(htPage, 'm)
  n :=
    $bcParseOnly => PARSE_-INTEGER htpLabelInputString(htPage, 'n)
    objValUnwrap htpLabelSpadValue(htPage, 'n)
  lda := m
--    $bcParseOnly => PARSE_-INTEGER htpLabelInputString(htPage, 'lda)
--    objValUnwrap htpLabelSpadValue(htPage, 'lda)
  ldb := m
--    $bcParseOnly => PARSE_-INTEGER htpLabelInputString(htPage, 'ldb)
--    objValUnwrap htpLabelSpadValue(htPage, 'ldb)
  ncolb :=
    $bcParseOnly => PARSE_-INTEGER htpLabelInputString(htPage, 'ncolb)
    objValUnwrap htpLabelSpadValue(htPage, 'ncolb)
  operation := htpButtonValue(htPage,'trans)
  trans :=
    operation = 'no_trans => '"n"
    '"t"
  elements := htpButtonValue(htPage,'wheret)
  wheret :=
    elements = 'in_a => '"i"
    '"s"
  error := htpButtonValue(htPage,'ifail)
  ifail :=
    error = 'one => '1
    '-1
  ((m = '5 and n = '3) and ncolb = '2)  => f01qdfDefaultSolve(htPage,lda,ldb,trans,wheret,ifail)
  matList :=
    "append"/[fa(i,n) for i in 1..lda] where fa(i,n) ==
       labelList := 
         "append"/[ga(i,j) for j in 1..n] where ga(i,j) ==
            anam := INTERN STRCONC ('"a",STRINGIMAGE i, STRINGIMAGE j)
            [['bcStrings,[6, "0.0", anam, 'F]]]
       prefix := ('"\newline \tab{2} ")
       labelList := [['text,:prefix],:labelList]
  bList :=
    "append"/[fb(i,ncolb) for i in 1..ldb] where fb(i,ncolb) ==
       labelList := 
         "append"/[gb(i,j) for j in 1..ncolb] where gb(i,j) ==
            bnam := INTERN STRCONC ('"b",STRINGIMAGE i, STRINGIMAGE j)
            [['bcStrings,[6, "0.0", bnam, 'F]]]
       prefix := ('"\newline \tab{2} ")
       labelList := [['text,:prefix],:labelList]
  prefix := ("\blankline \menuitemstyle{}\tab{2} Enter values of {\it B}: ")
  bList := [['text,:prefix],:bList]
  zList :=
    "append"/[fz(i) for i in 1..n] where fz(i) ==
       znam := INTERN STRCONC ('"z",STRINGIMAGE i)
       [['bcStrings,[6, "0.0", znam, 'F]]]
  prefix := ("\blankline \menuitemstyle{}\tab{2} Enter values of \zeta ")
  prefix := STRCONC(prefix,"(if required): \newline \tab{2}")
  zList := [['text,:prefix],:zList]
  equationPart := [
     '(domainConditions 
        (isDomain P (Polynomial $EmptyMode))
         (isDomain S (String))
          (isDomain F (Float))
           (isDomain PI (PositiveInteger))),
            :matList,:bList,:zList]
  page := htInitPage('"F01QDF - Operations with orthogonal matrices, compute {\it QB} or \htbitmap{f01qdf} after factorization by F01QCF or F01QDF",nil)
  htSay '"\newline \menuitemstyle{}\tab{2} Enter values of {\it A}: "
  htSay '"\newline \tab{2} "
  htMakePage equationPart
  htSay '"\blankline "
  htMakeDoneButton('"Continue",'f01qdfGen)
  htpSetProperty(page,'n,n)
  htpSetProperty(page,'m,m)
--  htpSetProperty(page,'lda,lda)
--  htpSetProperty(page,'ldb,ldb)
  htpSetProperty(page,'ncolb,ncolb)
  htpSetProperty(page,'trans,trans)
  htpSetProperty(page,'wheret,wheret)
  htpSetProperty(page,'ifail,ifail)
  htpSetProperty(page,'inputArea, htpInputAreaAlist htPage)
  htShowPage()


f01qdfDefaultSolve  (htPage,lda,ldb,trans,wheret,ifail) == 
  n := '3
  m := '5
  ncolb := '2
  page := htInitPage('"F01QDF - Operations with orthogonal matrices, compute {\it QB} or \htbitmap{f01qdf} after factorization by F01QCF or F01QDF",nil)
  htMakePage '(
    (domainConditions
       (isDomain F (Float))
         (isDomain I (Integer)))
    (text . "\newline ")
    (text . "\menuitemstyle{}\tab{2} Enter values of {\it A}: ")
    (text . "\newline \tab{2} ")
    (bcStrings (6 "2.0" a11 F))
    (bcStrings (6 "2.5" a12 F))
    (bcStrings (6 "2.5" a13 F))
    (text . "\newline \tab{2} ")
    (bcStrings (6 "2.0" a21 F))
    (bcStrings (6 "2.5" a22 F))
    (bcStrings (6 "2.5" a23 F))
    (text . "\newline \tab{2} ")
    (bcStrings (6 "1.6" a31 F))
    (bcStrings (6 "-0.4" a32 F))
    (bcStrings (6 "2.8" a33 F))
    (text . "\newline \tab{2} ")
    (bcStrings (6 "2.0" a41 F))
    (bcStrings (6 "-0.5" a42 F))
    (bcStrings (6 "0.5" a43 F))
    (text . "\newline \tab{2} ")
    (bcStrings (6 "1.2" a51 F))
    (bcStrings (6 "-0.3" a52 F))
    (bcStrings (6 "-2.9" a53 F))
    (text . "\blankline ")
    (text . "\menuitemstyle{}\tab{2} Enter values of {\it B}: ")
    (text . "\newline \tab{2} ")
    (bcStrings (6 "1.1" b11 F))
    (bcStrings (6 "0.0" b12 F))
    (text . "\newline \tab{2} ")
    (bcStrings (6 "0.9" b21 F))
    (bcStrings (6 "0.0" b22 F))
    (text . "\newline \tab{2} ")
    (bcStrings (6 "0.6" b31 F))
    (bcStrings (6 "1.32" b32 F))
    (text . "\newline \tab{2} ")
    (bcStrings (6 "0.0" b41 F))
    (bcStrings (6 "1.1" b42 F))
    (text . "\newline \tab{2} ")
    (bcStrings (6 "-0.8" b51 F))
    (bcStrings (6 "-0.26" b52 F))
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{}\tab{2} Enter values of \zeta (if required): ")
    (text . "\newline \tab{2} ")
    (bcStrings (10 "0.0" z1 F))
    (bcStrings (10 "0.0" z2 F))
    (bcStrings (10 "0.0" z3 F))
    (text . "\blankline "))
  htMakeDoneButton('"Continue",'f01qdfGen)
  htpSetProperty(page,'n,n)
  htpSetProperty(page,'m,m)
--  htpSetProperty(page,'lda,lda)
--  htpSetProperty(page,'ldb,ldb)
  htpSetProperty(page,'ncolb,ncolb)
  htpSetProperty(page,'trans,trans)
  htpSetProperty(page,'wheret,wheret)
  htpSetProperty(page,'ifail,ifail)
  htpSetProperty(page,'inputArea, htpInputAreaAlist htPage)
  htShowPage()

f01qdfGen htPage ==
  n := htpProperty(htPage,'n)
  m := htpProperty(htPage,'m)
--  lda := htpProperty(htPage,'lda)
--  ldb := htpProperty(htPage,'ldb)
  lda := m
  ldb := m
  ncolb := htpProperty(htPage,'ncolb)
  trans := htpProperty(htPage,'trans)
  wheret := htpProperty(htPage,'wheret)
  ifail := htpProperty(htPage,'ifail)
  alist := htpInputAreaAlist htPage
  y := alist
  for i in 1..n repeat
    left := STRCONC((first y).1," ")
    y := rest y
    zetalist := [left,:zetalist]
  zetastring := bcwords2liststring zetalist
  y := REVERSE y
  for i in 1..lda repeat
    for j in 1..n repeat
      elm := STRCONC((first y).1," ")
      rowList := [:rowList,elm]
      y := rest y
    matform := [:matform,rowList]
    rowList := []
  matstring := bcwords2liststring [bcwords2liststring x for x in matform]
  for i in 1..ldb repeat
    for j in 1..ncolb repeat
      elm := STRCONC((first y).1," ")
      rowList := [:rowList,elm]
      y := rest y
    bform := [:bform,rowList]
    rowList := []
  bstring := bcwords2liststring [bcwords2liststring x for x in bform]
  prefix := STRCONC('"f01qdf(_"",trans,"_",_"",wheret,"_",",STRINGIMAGE m,", ")
  prefix := STRCONC(prefix,STRINGIMAGE n,", ",matstring,", ",STRINGIMAGE lda)
  prefix := STRCONC(prefix,",[",zetastring,"],",STRINGIMAGE ncolb,", ")
  prefix := STRCONC(prefix,STRINGIMAGE ldb,", ",bstring,", ",STRINGIMAGE ifail,")")
  linkGen prefix


f01qef() ==
  htInitPage('"F01QEF - Operations with orthogonal matrices, form columns of {\it Q} after factorization by F01QCF or F01QFF",nil)
  htMakePage '(
    (domainConditions 
       (isDomain F (Float))
         (isDomain PI (PositiveInteger)))
    (text . "\windowlink{Manual Page}{manpageXXf01qef} for this routine ")
    (text . "\newline ")
    (text . "\lispwindowlink{Browser operation page}{(|oPageFrom| '|f01qef| '|NagMatrixOperationsPackage|)} for this routine")
    (text . "\newline \horizontalline ")
    (text . "\newline ")
    (text . "Returns the first {\it ncolq} columns of the real {\it m} by ")
    (text . "{\it n} orthogonal matrix {\it Q}, where {\it Q} is assumed ")
    (text . "to be given by {\it Q = }\htbitmap{f01qdf1}, ")
    (text . "\htbitmap{f01qdf2} being given in the form ")
    (text . "\htbitmap{f01qcf1}, ")
    (text . "where \htbitmap{f01qcf2}, ")
    (text . "\htbitmap{f01qcf3},  ")
    (text . "\htbitmap{zetak} is a scalar and ")
    (text . "\htbitmap{zk} is an (m-k) element vector. ")
    (text . "The routine is intended for use following F01QCF or F01QFF. ")
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{} \tab{2} ")
    (text . "\newline Rows of matrix A, {\it m}: ")
    (text . "\tab{32}  \menuitemstyle{} \tab{34} ")
    (text . "Columns of matrix A, {\it n}: \newline \tab{2} ")
    (bcStrings (6 5 m PI))
    (text . "\tab{34} ")
    (bcStrings (6 3 n PI))
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{} \tab{2} ")
--    (text . "\newline First dimension of A, {\it lda} ")
--    (text . "\htbitmap{great=} m: ")
--    (text . "\tab{32}  \menuitemstyle{} \tab{34} ")
    (text . "Number columns of matrix Q {\it ncolq}: ")
    (text . "\newline \tab{2} ")
--    (bcStrings (6 5 lda PI))
--    (text . "\tab{34} ")
    (bcStrings (6 5 ncolq PI))
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{} \tab{2} ")
    (text . "\newline \tab{2} ")
    (text . "Where the elements can be found: ")
    (radioButtons wheret
        (" " "  the elements of \zeta are in ZETA, returned by F01QCF/F01QFF" subsequent)
        (" " "  the elements of \zeta are in A" initial))
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{} \tab{2} ")
    (text . "\newline \tab{2} ")
    (text . "Ifail value: ")
    (radioButtons ifail
        ("" " -1, Print error messages" minusOne)
        ("" "  1, Suppress error messages" one)))
  htMakeDoneButton('"Continue", 'f01qefSolve)
  htShowPage()

f01qefSolve htPage ==
  m :=
    $bcParseOnly => PARSE_-INTEGER htpLabelInputString(htPage, 'm)
    objValUnwrap htpLabelSpadValue(htPage, 'm)
  n :=
    $bcParseOnly => PARSE_-INTEGER htpLabelInputString(htPage, 'n)
    objValUnwrap htpLabelSpadValue(htPage, 'n)
  lda := m
--    $bcParseOnly => PARSE_-INTEGER htpLabelInputString(htPage, 'lda)
--    objValUnwrap htpLabelSpadValue(htPage, 'lda)
  ncolq :=
    $bcParseOnly => PARSE_-INTEGER htpLabelInputString(htPage, 'ncolq)
    objValUnwrap htpLabelSpadValue(htPage, 'ncolq)
  elements := htpButtonValue(htPage,'wheret)
  wheret :=
    elements = 'initial => '"i"
    '"s"
  error := htpButtonValue(htPage,'ifail)
  ifail :=
    error = 'one => '1
    '-1
  ((m = '5 and n = '3) and ncolq = '5)  => f01qefDefaultSolve(htPage,lda,wheret,ifail)
  matList :=
    "append"/[fa(i,ncolq) for i in 1..lda] where fa(i,ncolq) ==
       labelList := 
         "append"/[ga(i,j) for j in 1..ncolq] where ga(i,j) ==
            anam := INTERN STRCONC ('"a",STRINGIMAGE i, STRINGIMAGE j)
            [['bcStrings,[7, "0.0", anam, 'F]]]
       prefix := ('"\newline ")
       labelList := [['text,:prefix],:labelList]
  zList :=
    "append"/[fz(i) for i in 1..n] where fz(i) ==
       znam := INTERN STRCONC ('"z",STRINGIMAGE i)
       [['bcStrings,[7, "0.0", znam, 'F]]]
  prefix := ("\blankline \menuitemstyle{}\tab{2} Enter values of \zeta ")
  prefix := STRCONC(prefix,"(if required): \newline ")
  zList := [['text,:prefix],:zList]
  equationPart := [
     '(domainConditions 
        (isDomain P (Polynomial $EmptyMode))
         (isDomain S (String))
          (isDomain F (Float))
           (isDomain PI (PositiveInteger))),
            :matList,:zList]
  page := htInitPage('"F01QEF - Operations with orthogonal matrices, form columns of {\it Q} after factorization by F01QCF or F01QFF",nil)
  htSay '"\newline \menuitemstyle{}\tab{2} Enter values of {\it Q}: "
  htSay '"\newline  "
  htMakePage equationPart
  htSay '"\blankline "
  htMakeDoneButton('"Continue",'f01qefGen)
  htpSetProperty(page,'n,n)
  htpSetProperty(page,'m,m)
--  htpSetProperty(page,'lda,lda)
  htpSetProperty(page,'ncolq,ncolq)
  htpSetProperty(page,'wheret,wheret)
  htpSetProperty(page,'ifail,ifail)
  htpSetProperty(page,'inputArea, htpInputAreaAlist htPage)
  htShowPage()


f01qefDefaultSolve  (htPage,lda,wheret,ifail) == 
  n := '3
  m := '5
  ncolq := '5
  page := htInitPage('"F01QEF - Operations with orthogonal matrices, form columns of {\it Q} after factorization by F01QCF or F01QFF",nil)
  htMakePage '(
    (domainConditions
       (isDomain F (Float))
         (isDomain I (Integer)))
    (text . "\newline ")
    (text . "\menuitemstyle{}\tab{2} Enter values of {\it Q}")
    (text . "(in this case returned by the default entries of F01QCF) : ")
    (text . "\newline ")
    (bcStrings (7 "-4.0" a11 F))
    (bcStrings (7 "-2.0" a12 F))
    (bcStrings (7 "-3.0" a13 F))
    (bcStrings (7 "0.0" a14 F))
    (bcStrings (7 "0.0" a15 F))
    (text . "\newline  ")
    (bcStrings (7 "0.4085" a21 F))
    (bcStrings (7 "-3.0" a22 F))
    (bcStrings (7 "-2.0" a23 F))
    (bcStrings (7 "0.0" a24 F))
    (bcStrings (7 "0.0" a25 F))
    (text . "\newline  ")
    (bcStrings (7 "0.3266" a31 F))
    (bcStrings (7 "-0.4619" a32 F))
    (bcStrings (7 "-4.0" a33 F))
    (bcStrings (7 "0.0" a34 F))
    (bcStrings (7 "0.0" a35 F))
    (text . "\newline  ")
    (bcStrings (7 "0.4082" a41 F))
    (bcStrings (7 "-0.5774" a42 F))
    (bcStrings (7 "0.0" a43 F))
    (bcStrings (7 "0.0" a44 F))
    (bcStrings (7 "0.0" a45 F))
    (text . "\newline  ")
    (bcStrings (7 "0.2449" a51 F))
    (bcStrings (7 "-0.3464" a52 F))
    (bcStrings (7 "-0.6326" a53 F))
    (bcStrings (7 "0.0" a54 F))
    (bcStrings (7 "0.0" a55 F))
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{}\tab{2} Enter values of \zeta: ")
    (text . "\newline ")
    (bcStrings (10 "1.2247" z1 F))
    (bcStrings (10 "1.1547" z2 F))
    (bcStrings (10 "1.2649" z3 F))
    (text . "\blankline "))
  htMakeDoneButton('"Continue",'f01qefGen)
  htpSetProperty(page,'n,n)
  htpSetProperty(page,'m,m)
--  htpSetProperty(page,'lda,lda)
  htpSetProperty(page,'ncolq,ncolq)
  htpSetProperty(page,'wheret,wheret)
  htpSetProperty(page,'ifail,ifail)
  htpSetProperty(page,'inputArea, htpInputAreaAlist htPage)
  htShowPage()

f01qefGen htPage ==
  n := htpProperty(htPage,'n)
  m := htpProperty(htPage,'m)
--  lda := htpProperty(htPage,'lda)
  lda := m
  ncolq := htpProperty(htPage,'ncolq)
  wheret := htpProperty(htPage,'wheret)
  ifail := htpProperty(htPage,'ifail)
  alist := htpInputAreaAlist htPage
  y := alist
  for i in 1..n repeat
    left := STRCONC((first y).1," ")
    y := rest y
    zetalist := [left,:zetalist]
  zetastring := bcwords2liststring zetalist
  y := REVERSE y
  for i in 1..lda repeat
    for j in 1..ncolq repeat
      elm := STRCONC((first y).1," ")
      rowList := [:rowList,elm]
      y := rest y
    matform := [:matform,rowList]
    rowList := []
  matstring := bcwords2liststring [bcwords2liststring x for x in matform]
  prefix := STRCONC('"f01qef(_"",wheret,"_",",STRINGIMAGE m,", ")
  prefix := STRCONC(prefix,STRINGIMAGE n,", ",STRINGIMAGE lda,", ")
  prefix := STRCONC(prefix,STRINGIMAGE ncolq,",[",zetastring,"],")
  prefix := STRCONC(prefix,matstring,", ",STRINGIMAGE ifail,")")
  linkGen prefix

f01rcf() ==
  htInitPage('"F01RCF - {\it QR} factorization of complex {\it m} by {\it n} matrix (m \htbitmap{great=} n)",nil)
  htMakePage '(
    (domainConditions 
       (isDomain F (Float))
         (isDomain PI (PositiveInteger)))
    (text . "\windowlink{Manual Page}{manpageXXf01rcf} for this routine ")
    (text . "\newline ")
    (text . "\lispwindowlink{Browser operation page}{(|oPageFrom| '|f01rcf| '|NagMatrixOperationsPackage|)} for this routine")
    (text . "\newline \horizontalline ")
    (text . "\newline ")
    (text . "Finds the QR factorization of the complex m by n matrix {\it A},")
    (text . " which is factorized as \htbitmap{f01qcf}, where m > n")
    (text . " and A = QR  when m = n , where Q is an m by m unitary matrix ")
    (text . "and R is an n by n upper triangular matrix with real diagonal ")
    (text . "elements. The {\it k}th transformation matrix,{\it Qk}, ")
    (text . "which is used to introduce zeros into the {\it k}th column of ")
    (text . "{\it A}, is given in the form ")
    (text . "\htbitmap{f01qcf1}, ")
    (text . "where \htbitmap{f01rdf2}, ")
    (text . "\htbitmap{f01qcf3},  ")
    (text . "\htbitmap{gammak} is a scalar for which Re ")
    (text . "\htbitmap{gammak} = 1.0, \htbitmap{zetak} ")
    (text . "is a real scalar and \htbitmap{zk} is an ")
    (text . "(m-k) element vector. ")
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{} \tab{2} ")
    (text . "\newline Rows of matrix A, {\it m}: ")
    (text . "\tab{32}  \menuitemstyle{} \tab{34} ")
    (text . "Columns of matrix A, {\it n}: \newline \tab{2} ")
    (bcStrings (6 5 m PI))
    (text . "\tab{34} ")
    (bcStrings (6 3 n PI))
--    (text . "\blankline ")
--    (text . "\newline ")
--    (text . "\menuitemstyle{} \tab{2} ")
--    (text . "\newline First dimension of A, {\it lda} ")
--    (text . "\htbitmap{great=} m: ")
--    (text . "\newline \tab{2} ")
--    (bcStrings (6 5 lda PI))
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{} \tab{2} ")
    (text . "\newline \tab{2} ")
    (text . "Ifail value: ")
    (radioButtons ifail
        ("" " -1, Print error messages" minusOne)
        ("" "  1, Suppress error messages" one)))
  htMakeDoneButton('"Continue", 'f01rcfSolve)
  htShowPage()

f01rcfSolve htPage ==
  m :=
    $bcParseOnly => PARSE_-INTEGER htpLabelInputString(htPage, 'm)
    objValUnwrap htpLabelSpadValue(htPage, 'm)
  n :=
    $bcParseOnly => PARSE_-INTEGER htpLabelInputString(htPage, 'n)
    objValUnwrap htpLabelSpadValue(htPage, 'n)
  lda := m
--    $bcParseOnly => PARSE_-INTEGER htpLabelInputString(htPage, 'lda)
--    objValUnwrap htpLabelSpadValue(htPage, 'lda)
  error := htpButtonValue(htPage,'ifail)
  ifail :=
    error = 'one => '1
    '-1
  (m = '5 and n = '3) => f01rcfDefaultSolve(htPage,ifail)
  matList :=
    "append"/[fa(i,n) for i in 1..lda] where fa(i,n) ==
       labelList := 
         "append"/[ga(i,j) for j in 1..n] where ga(i,j) ==
            anam := INTERN STRCONC ('"a",STRINGIMAGE i, STRINGIMAGE j)
            [['bcStrings,[16, "0.0 + 0.0*%i", anam, 'F]]]
       prefix := ('"\newline \tab{2} ")
       labelList := [['text,:prefix],:labelList]
  equationPart := [
     '(domainConditions 
        (isDomain P (Polynomial $EmptyMode))
         (isDomain S (String))
          (isDomain F (Float))
           (isDomain PI (PositiveInteger))),
            :matList]
  page := htInitPage('"F01RCF - {\it QR} factorization of complex {\it m} by {\it n} matrix (m \htbitmap{great=} n)",nil)
  htSay '"\newline \menuitemstyle{}\tab{2} Enter values of {\it A}: "
  htSay '"\newline \tab{2} "
  htMakePage equationPart
  htSay '"\blankline "
  htMakeDoneButton('"Continue",'f01rcfGen)
  htpSetProperty(page,'n,n)
  htpSetProperty(page,'m,m)
--  htpSetProperty(page,'lda,lda)
  htpSetProperty(page,'ifail,ifail)
  htpSetProperty(page,'inputArea, htpInputAreaAlist htPage)
  htShowPage()


f01rcfDefaultSolve  (htPage,ifail) == 
  n := '3
  m := '5
  lda := '5
  page := htInitPage('"F01RCF - {\it QR} factorization of complex {\it m} by {\it n} matrix (m \htbitmap{great=} n)",nil)
  htMakePage '(
    (domainConditions
       (isDomain F (Float))
         (isDomain I (Integer)))
    (text . "\newline ")
    (text . "\menuitemstyle{}\tab{2} Enter values of {\it A}: ")
    (text . "\newline \tab{2} ")
    (bcStrings (15 "0.5*%i" a11 F))
    (bcStrings (15 "-0.5 + 1.5*%i" a12 F))
    (bcStrings (15 "-1.0 + 1.0*%i" a13 F))
    (text . "\newline \tab{2} ")
    (bcStrings (15 "0.4 + 0.3*%i" a21 F))
    (bcStrings (15 "0.9 + 1.3*%i" a22 F))
    (bcStrings (15 "0.2 + 1.4*%i" a23 F))
    (text . "\newline \tab{2} ")
    (bcStrings (15 "0.4" a31 F))
    (bcStrings (15 "-0.4 + 0.4*%i" a32 F))
    (bcStrings (15 "1.8" a33 F))
    (text . "\newline \tab{2} ")
    (bcStrings (15 "0.3 - 0.4*%i" a41 F))
    (bcStrings (15 "0.1 + 0.7*%i" a42 F))
    (bcStrings (15 "0.0" a43 F))
    (text . "\newline \tab{2} ")
    (bcStrings (15 "-0.3*%i" a51 F))
    (bcStrings (15 "0.3 + 0.3*%i" a52 F))
    (bcStrings (15 "2.4*%i" a53 F))
    (text . "\blankline "))
  htMakeDoneButton('"Continue",'f01rcfGen)
  htpSetProperty(page,'n,n)
  htpSetProperty(page,'m,m)
--  htpSetProperty(page,'lda,lda)
  htpSetProperty(page,'ifail,ifail)
  htpSetProperty(page,'inputArea, htpInputAreaAlist htPage)
  htShowPage()

f01rcfGen htPage ==
  n := htpProperty(htPage,'n)
  m := htpProperty(htPage,'m)
--  lda := htpProperty(htPage,'lda)
  lda := m
  ifail := htpProperty(htPage,'ifail)
  alist := htpInputAreaAlist htPage
  y := alist
  y := REVERSE y
  for i in 1..lda repeat
    for j in 1..n repeat
      elm := STRCONC((first y).1," ")
      rowList := [:rowList,elm]
      y := rest y
    matform := [:matform,rowList]
    rowList := []
  matstring := bcwords2liststring [bcwords2liststring x for x in matform]
  prefix := STRCONC('"f01rcf(",STRINGIMAGE m,", ",STRINGIMAGE n,", ")
  prefix := STRCONC(prefix,STRINGIMAGE lda,", ",matstring)
  linkGen STRCONC(prefix,", ",STRINGIMAGE ifail,")")

f01rdf() ==
  htInitPage('"F01RDF - Operations with unitary matrices, compute {\it QB} or \htbitmap{f01rdf} after factorization by F01QCF",nil)
  htMakePage '(
    (domainConditions 
       (isDomain F (Float))
         (isDomain PI (PositiveInteger)))
    (text . "\windowlink{Manual Page}{manpageXXf01rdf} for this routine ")
    (text . "\newline ")
    (text . "\lispwindowlink{Browser operation page}{(|oPageFrom| '|f01rdf| '|NagMatrixOperationsPackage|)} for this routine")
    (text . "\newline \horizontalline ")
    (text . "\newline ")
    (text . "Performs one of the transformations B = QB or B = ")
    (text . "\htbitmap{f01rdf}, where B is an m ")
    (text . "by ncolb matrix and Q is an m by  m ")
    (text . "unitary matrix assumed to be given by Q = ")
    (text . "\htbitmap{f01rdf1}, \htbitmap{f01qdf2} ")
    (text . "being given in the form \htbitmap{f01qcf1}, ")
    (text . "where \htbitmap{f01rdf2}, \htbitmap{f01qcf3}")
    (text . ", \htbitmap{gammak} is a scalar for which Re ")
    (text . "\htbitmap{gammak} = 1.0, \htbitmap{zetak} ")
    (text . "is a real scalar and \htbitmap{zk} is an ")
    (text . "(m-k) element vector. ")
    (text . "The routine is intended for use following F01QCF or F01QFF. ")
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{} \tab{2} ")
    (text . "\newline Rows of matrix A, {\it m}: ")
    (text . "\tab{32}  \menuitemstyle{} \tab{34} ")
    (text . "Columns of matrix A, {\it n}: \newline \tab{2} ")
    (bcStrings (6 5 m PI))
    (text . "\tab{34} ")
    (bcStrings (6 3 n PI))
--    (text . "\blankline ")
--    (text . "\newline ")
--    (text . "\menuitemstyle{} \tab{2} ")
--    (text . "\newline First dimension of A, {\it lda} ")
--    (text . "\htbitmap{great=} m: ")
--    (text . "\tab{32}  \menuitemstyle{} \tab{34} ")
--    (text . "First dimension of B, {\it ldb} ")
--    (text . "\htbitmap{great=} m: ")
--    (text . "\newline \tab{2} ")
--    (bcStrings (6 5 lda PI))
--    (text . "\tab{34} ")
--    (bcStrings (6 5 ldb PI))
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{} \tab{2} ")
    (text . "\newline \tab{2} ")
    (text . "Number of columns of matrix B {\it ncolb}: ")
    (text . "\newline \tab{2} ")
    (bcStrings (6 2 ncolb PI))
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{} \tab{2} ")
    (text . "\newline \tab{2} ")
    (text . "Transformation to be performed: ")
    (radioButtons trans
        (" " "  {\it B = QB}" no_trans)
        (" " "  {\it B =} \htbitmap{f01rdf} (Conjugate Transpose)" trans))
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{} \tab{2} ")
    (text . "\newline \tab{2} ")
    (text . "Where the elements can be found: ")
    (radioButtons wheret
        (" " "  the elements of \theta are in A" in_a)
        (" " "  the elements of \theta are in THETA" seperate))
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{} \tab{2} ")
    (text . "\newline \tab{2} ")
    (text . "Ifail value: ")
    (radioButtons ifail
        ("" " -1, Print error messages" minusOne)
        ("" "  1, Suppress error messages" one)))
  htMakeDoneButton('"Continue", 'f01rdfSolve)
  htShowPage()

f01rdfSolve htPage ==
  m :=
    $bcParseOnly => PARSE_-INTEGER htpLabelInputString(htPage, 'm)
    objValUnwrap htpLabelSpadValue(htPage, 'm)
  n :=
    $bcParseOnly => PARSE_-INTEGER htpLabelInputString(htPage, 'n)
    objValUnwrap htpLabelSpadValue(htPage, 'n)
  lda := m
--    $bcParseOnly => PARSE_-INTEGER htpLabelInputString(htPage, 'lda)
--    objValUnwrap htpLabelSpadValue(htPage, 'lda)
  ldb := m
--    $bcParseOnly => PARSE_-INTEGER htpLabelInputString(htPage, 'ldb)
--    objValUnwrap htpLabelSpadValue(htPage, 'ldb)
  ncolb :=
    $bcParseOnly => PARSE_-INTEGER htpLabelInputString(htPage, 'ncolb)
    objValUnwrap htpLabelSpadValue(htPage, 'ncolb)
  operation := htpButtonValue(htPage,'trans)
  trans :=
    operation = 'no_trans => '"n"
    '"c"
  elements := htpButtonValue(htPage,'wheret)
  wheret :=
    elements = 'in_a => '"i"
    '"c"
  error := htpButtonValue(htPage,'ifail)
  ifail :=
    error = 'one => '1
    '-1
  ((m = '5 and n = '3) and ncolb = '2)  => f01rdfDefaultSolve(htPage,lda,ldb,trans,wheret,ifail)
  matList :=
    "append"/[fa(i,n) for i in 1..lda] where fa(i,n) ==
       labelList := 
         "append"/[ga(i,j) for j in 1..n] where ga(i,j) ==
            anam := INTERN STRCONC ('"a",STRINGIMAGE i, STRINGIMAGE j)
            [['bcStrings,[16, "0.0 + 0.0*%i", anam, 'F]]]
       prefix := ('"\newline \tab{2} ")
       labelList := [['text,:prefix],:labelList]
  bList :=
    "append"/[fb(i,ncolb) for i in 1..ldb] where fb(i,ncolb) ==
       labelList := 
         "append"/[gb(i,j) for j in 1..ncolb] where gb(i,j) ==
            bnam := INTERN STRCONC ('"b",STRINGIMAGE i, STRINGIMAGE j)
            [['bcStrings,[16, "0.0 + 0.0*%i", bnam, 'F]]]
       prefix := ('"\newline \tab{2} ")
       labelList := [['text,:prefix],:labelList]
  prefix := ("\blankline \menuitemstyle{}\tab{2} Enter values of {\it B}: ")
  bList := [['text,:prefix],:bList]
  zList :=
    "append"/[fz(i) for i in 1..n] where fz(i) ==
       znam := INTERN STRCONC ('"z",STRINGIMAGE i)
       [['bcStrings,[16, "0.0", znam, 'F]]]
  prefix := ("\blankline \menuitemstyle{}\tab{2} Enter values of \theta ")
  prefix := STRCONC(prefix,"(if required): \newline \tab{2}")
  zList := [['text,:prefix],:zList]
  equationPart := [
     '(domainConditions 
        (isDomain P (Polynomial $EmptyMode))
         (isDomain S (String))
          (isDomain F (Float))
           (isDomain PI (PositiveInteger))),
            :matList,:bList,:zList]
  page := htInitPage('"F01RDF - Operations with orthogonal matrices, compute {\it QB} or \htbitmap{f01rdf} after factorization by F01QCF or F01RDF",nil)
  htSay '"\newline \menuitemstyle{}\tab{2} Enter values of {\it A}: "
  htSay '"\newline \tab{2} "
  htMakePage equationPart
  htSay '"\blankline "
  htMakeDoneButton('"Continue",'f01rdfGen)
  htpSetProperty(page,'n,n)
  htpSetProperty(page,'m,m)
--  htpSetProperty(page,'lda,lda)
--  htpSetProperty(page,'ldb,ldb)
  htpSetProperty(page,'ncolb,ncolb)
  htpSetProperty(page,'trans,trans)
  htpSetProperty(page,'wheret,wheret)
  htpSetProperty(page,'ifail,ifail)
  htpSetProperty(page,'inputArea, htpInputAreaAlist htPage)
  htShowPage()


f01rdfDefaultSolve  (htPage,lda,ldb,trans,wheret,ifail) == 
  n := '3
  m := '5
  ncolb := '2
  page := htInitPage('"F01RDF - Operations with orthogonal matrices, compute {\it QB} or \htbitmap{f01rdf} after factorization by F01QCF or F01RDF",nil)
  htMakePage '(
    (domainConditions
       (isDomain F (Float))
         (isDomain I (Integer)))
    (text . "\newline ")
    (text . "\menuitemstyle{}\tab{2} Enter values of {\it A}: ")
    (text . "\newline \tab{2} ")
    (bcStrings (15 "0.5*%i" a11 F))
    (bcStrings (15 "-0.5 + 1.5*%i" a12 F))
    (bcStrings (15 "-1.0 + 1.0*%i" a13 F))
    (text . "\newline \tab{2} ")
    (bcStrings (15 "0.4 + 0.3*%i" a21 F))
    (bcStrings (15 "0.9 + 1.3*%i" a22 F))
    (bcStrings (15 "0.2 + 1.4*%i" a23 F))
    (text . "\newline \tab{2} ")
    (bcStrings (15 "0.4" a31 F))
    (bcStrings (15 "-0.4 + 0.4*%i" a32 F))
    (bcStrings (15 "1.8" a33 F))
    (text . "\newline \tab{2} ")
    (bcStrings (15 "0.3 - 0.4*%i" a41 F))
    (bcStrings (15 "0.1 + 0.7*%i" a42 F))
    (bcStrings (15 "0.0" a43 F))
    (text . "\newline \tab{2} ")
    (bcStrings (15 "-0.3*%i" a51 F))
    (bcStrings (15 "0.3 + 0.3*%i" a52 F))
    (bcStrings (15 "2.4" a53 F))
    (text . "\blankline ")
    (text . "\menuitemstyle{}\tab{2} Enter values of {\it B}: ")
    (text . "\newline \tab{2} ")
    (bcStrings (15 "-0.55 + 1.05*%i" b11 F))
    (bcStrings (15 "0.45 + 1.05*%i" b12 F))
    (text . "\newline \tab{2} ")
    (bcStrings (15 "0.49 + 0.93*%i" b21 F))
    (bcStrings (15 "1.09 + 0.13*%i" b22 F))
    (text . "\newline \tab{2} ")
    (bcStrings (15 "0.56 - 0.16*%i" b31 F))
    (bcStrings (15 "0.64 + 0.16*%i" b32 F))
    (text . "\newline \tab{2} ")
    (bcStrings (15 "0.39 + 0.23*%i" b41 F))
    (bcStrings (15 "-0.39 - 0.23*%i" b42 F))
    (text . "\newline \tab{2} ")
    (bcStrings (15 "1.13 + 0.83*%i" b51 F))
    (bcStrings (15 "-1.13 + 0.77*%i" b52 F))
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{}\tab{2} Enter values of \theta (if required): ")
    (text . "\newline \tab{2} ")
    (bcStrings (15 "0.0" z1 F))
    (bcStrings (15 "0.0" z2 F))
    (bcStrings (15 "0.0" z3 F))
    (text . "\blankline "))
  htMakeDoneButton('"Continue",'f01rdfGen)
  htpSetProperty(page,'n,n)
  htpSetProperty(page,'m,m)
--  htpSetProperty(page,'lda,lda)
--  htpSetProperty(page,'ldb,ldb)
  htpSetProperty(page,'ncolb,ncolb)
  htpSetProperty(page,'trans,trans)
  htpSetProperty(page,'wheret,wheret)
  htpSetProperty(page,'ifail,ifail)
  htpSetProperty(page,'inputArea, htpInputAreaAlist htPage)
  htShowPage()

f01rdfGen htPage ==
  n := htpProperty(htPage,'n)
  m := htpProperty(htPage,'m)
--  lda := htpProperty(htPage,'lda)
--  ldb := htpProperty(htPage,'ldb)
  lda := m
  ldb := m
  ncolb := htpProperty(htPage,'ncolb)
  trans := htpProperty(htPage,'trans)
  wheret := htpProperty(htPage,'wheret)
  ifail := htpProperty(htPage,'ifail)
  alist := htpInputAreaAlist htPage
  y := alist
  for i in 1..n repeat
    left := STRCONC((first y).1," ")
    y := rest y
    zetalist := [left,:zetalist]
  zetastring := bcwords2liststring zetalist
  y := REVERSE y
  for i in 1..lda repeat
    for j in 1..n repeat
      elm := STRCONC((first y).1," ")
      rowList := [:rowList,elm]
      y := rest y
    matform := [:matform,rowList]
    rowList := []
  matstring := bcwords2liststring [bcwords2liststring x for x in matform]
  for i in 1..ldb repeat
    for j in 1..ncolb repeat
      elm := STRCONC((first y).1," ")
      rowList := [:rowList,elm]
      y := rest y
    bform := [:bform,rowList]
    rowList := []
  bstring := bcwords2liststring [bcwords2liststring x for x in bform]
  prefix := STRCONC('"f01rdf(_"",trans,"_",_"",wheret,"_",",STRINGIMAGE m,", ")
  prefix := STRCONC(prefix,STRINGIMAGE n,", ",matstring,", ",STRINGIMAGE lda)
  prefix := STRCONC(prefix,",[",zetastring,"],",STRINGIMAGE ncolb,", ")
  prefix := STRCONC(prefix,STRINGIMAGE ldb,", ",bstring,", ",STRINGIMAGE ifail,")")
  linkGen prefix


f01ref() ==
  htInitPage('"F01REF - Operations with unitary matrices, form columns of {\it Q} after factorization by F01RCF",nil)
  htMakePage '(
    (domainConditions 
       (isDomain F (Float))
         (isDomain PI (PositiveInteger)))
    (text . "\windowlink{Manual Page}{manpageXXf01ref} for this routine ")
    (text . "\newline ")
    (text . "\lispwindowlink{Browser operation page}{(|oPageFrom| '|f01ref| '|NagMatrixOperationsPackage|)} for this routine")
    (text . "\newline \horizontalline ")
    (text . "\newline ")
    (text . "Returns the first {\it ncolq} columns of the real {\it m} by ")
    (text . "{\it m} unitary matrix {\it Q}, where {\it Q} is assumed ")
    (text . "to be given by {\it Q = }\htbitmap{f01rdf1}, ")
    (text . "\htbitmap{f01qdf2} being given in the form ")
    (text . "\htbitmap{f01qcf1}, ")
    (text . "where \htbitmap{f01rdf2}, ")
    (text . "\htbitmap{f01qcf3},  ")
    (text . "\htbitmap{gammak} is a scalar for which Re ")
    (text . "\htbitmap{gammak} = 1.0, \htbitmap{zetak} ")
    (text . "is a real scalar and \htbitmap{zk} is an ")
    (text . "(m-k) element vector. ")
    (text . "The routine is intended for use following F01RCF or F01RFF. ")
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{} \tab{2} ")
    (text . "\newline Rows of matrix A, {\it m}: ")
    (text . "\tab{32}  \menuitemstyle{} \tab{34} ")
    (text . "Columns of matrix A, {\it n}: \newline \tab{2} ")
    (bcStrings (6 5 m PI))
    (text . "\tab{34} ")
    (bcStrings (6 3 n PI))
--    (text . "\blankline ")
--    (text . "\newline ")
--    (text . "\menuitemstyle{} \tab{2} ")
--    (text . "\newline First dimension of A, {\it lda} ")
--    (text . "\htbitmap{great=} m: ")
--    (text . "\newline \tab{2} ")
--    (bcStrings (6 5 lda PI))
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{} \tab{2} ")
    (text . "\newline \tab{2} ")
    (text . "Required number of columns  of matrix Q {\it ncolq}: ")
    (text . "\newline \tab{2} ")
    (bcStrings (6 2 ncolq PI))
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{} \tab{2} ")
    (text . "\newline \tab{2} ")
    (text . "Where the elements can be found: ")
    (radioButtons wheret
        (" " "  the elements of \theta are in THETA" seperate)
        (" " "  the elements of \theta are in A" in_a))
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{} \tab{2} ")
    (text . "\newline \tab{2} ")
    (text . "Ifail value: ")
    (radioButtons ifail
        ("" " -1, Print error messages" minusOne)
        ("" "  1, Suppress error messages" one)))
  htMakeDoneButton('"Continue", 'f01refSolve)
  htShowPage()

f01refSolve htPage ==
  m :=
    $bcParseOnly => PARSE_-INTEGER htpLabelInputString(htPage, 'm)
    objValUnwrap htpLabelSpadValue(htPage, 'm)
  n :=
    $bcParseOnly => PARSE_-INTEGER htpLabelInputString(htPage, 'n)
    objValUnwrap htpLabelSpadValue(htPage, 'n)
  lda := m
--    $bcParseOnly => PARSE_-INTEGER htpLabelInputString(htPage, 'lda)
--    objValUnwrap htpLabelSpadValue(htPage, 'lda)
  ncolq :=
    $bcParseOnly => PARSE_-INTEGER htpLabelInputString(htPage, 'ncolq)
    objValUnwrap htpLabelSpadValue(htPage, 'ncolq)
  elements := htpButtonValue(htPage,'wheret)
  wheret :=
    elements = 'in_a => '"i"
    '"s"
  error := htpButtonValue(htPage,'ifail)
  ifail :=
    error = 'one => '1
    '-1
  ((m = '5 and n = '3) and ncolq = '2)  => f01refDefaultSolve(htPage,lda,wheret,ifail)
  matList :=
    "append"/[fa(i,n) for i in 1..lda] where fa(i,n) ==
       labelList := 
         "append"/[ga(i,j) for j in 1..n] where ga(i,j) ==
            anam := INTERN STRCONC ('"a",STRINGIMAGE i, STRINGIMAGE j)
            [['bcStrings,[20, "0.0 + 0.0*%i", anam, 'F]]]
       prefix := ('"\newline \tab{2} ")
       labelList := [['text,:prefix],:labelList]
  zList :=
    "append"/[fz(i) for i in 1..n] where fz(i) ==
       znam := INTERN STRCONC ('"z",STRINGIMAGE i)
       [['bcStrings,[20, "0.0", znam, 'F]]]
  prefix := ("\blankline \menuitemstyle{}\tab{2} Enter values of \theta ")
  prefix := STRCONC(prefix,"(if required): \newline \tab{2}")
  zList := [['text,:prefix],:zList]
  equationPart := [
     '(domainConditions 
        (isDomain P (Polynomial $EmptyMode))
         (isDomain S (String))
          (isDomain F (Float))
           (isDomain PI (PositiveInteger))),
            :matList,:zList]
  page := htInitPage('"F01REF - Operations with unitary matrices, form columns of {\it Q} after factorization by F01RCF",nil)
  htSay '"\newline \menuitemstyle{}\tab{2} Enter values of {\it A}: "
  htSay '"\newline \tab{2} "
  htMakePage equationPart
  htSay '"\blankline "
  htMakeDoneButton('"Continue",'f01refGen)
  htpSetProperty(page,'n,n)
  htpSetProperty(page,'m,m)
--  htpSetProperty(page,'lda,lda)
  htpSetProperty(page,'ncolq,ncolq)
  htpSetProperty(page,'wheret,wheret)
  htpSetProperty(page,'ifail,ifail)
  htpSetProperty(page,'inputArea, htpInputAreaAlist htPage)
  htShowPage()


f01refDefaultSolve  (htPage,lda,wheret,ifail) == 
  n := '3
  m := '5
  ncolq := '2
  page := htInitPage('"F01REF - Operations with unitary matrices, form columns of {\it Q} after factorization by F01RCF",nil)
  htMakePage '(
    (domainConditions
       (isDomain F (Float))
         (isDomain I (Integer)))
    (text . "\newline ")
    (text . "\menuitemstyle{}\tab{2} Enter values of {\it A}: ")
    (text . "\newline \tab{2} ")
    (bcStrings (16 "1" a11 F))
    (bcStrings (16 "1 + %i" a12 F))
    (bcStrings (16 "1 + %i" a13 F))
    (text . "\newline \tab{2} ")
    (bcStrings (16 "-0.2-0.4*%i" a21 F))
    (bcStrings (16 "-2" a22 F))
    (bcStrings (16 "-1 - %i" a23 F))
    (text . "\newline \tab{2} ")
    (bcStrings (16 "-0.32 - 0.16*%i" a31 F))
    (bcStrings (16 "-0.3505+0.263*%i" a32 F))
    (bcStrings (16 "-3" a33 F))
    (text . "\newline \tab{2} ")
    (bcStrings (16 "-0.4 + 0.2*%i" a41 F))
    (bcStrings (16 "0.5477*%i" a42 F))
    (bcStrings (16 "0.0" a43 F))
    (text . "\newline \tab{2} ")
    (bcStrings (16 "-0.12 + 0.24*%i" a51 F))
    (bcStrings (16 "0.1972+0.2629*%i" a52 F))
    (bcStrings (16 "0.6325" a53 F))
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{}\tab{2} Enter values of \theta: ")
    (text . "\newline \tab{2} ")
    (bcStrings (16 "1 + 0.5*%i" z1 F))
    (bcStrings (16 "1.0954-0.3333*%i" z2 F))
    (bcStrings (16 "1.2649-1.1565*%i" z3 F))
    (text . "\blankline "))
  htMakeDoneButton('"Continue",'f01refGen)
  htpSetProperty(page,'n,n)
  htpSetProperty(page,'m,m)
--  htpSetProperty(page,'lda,lda)
  htpSetProperty(page,'ncolq,ncolq)
  htpSetProperty(page,'wheret,wheret)
  htpSetProperty(page,'ifail,ifail)
  htpSetProperty(page,'inputArea, htpInputAreaAlist htPage)
  htShowPage()

f01refGen htPage ==
  n := htpProperty(htPage,'n)
  m := htpProperty(htPage,'m)
--  lda := htpProperty(htPage,'lda)
  lda := m
  ncolq := htpProperty(htPage,'ncolq)
  wheret := htpProperty(htPage,'wheret)
  ifail := htpProperty(htPage,'ifail)
  alist := htpInputAreaAlist htPage
  y := alist
  for i in 1..n repeat
    left := STRCONC((first y).1," ")
    y := rest y
    thetalist := [left,:thetalist]
  thetastring := bcwords2liststring thetalist
  y := REVERSE y
  for i in 1..lda repeat
    for j in 1..n repeat
      elm := STRCONC((first y).1," ")
      rowList := [:rowList,elm]
      y := rest y
    matform := [:matform,rowList]
    rowList := []
  matstring := bcwords2liststring [bcwords2liststring x for x in matform]
  prefix := STRCONC('"f01ref(_"",wheret,"_",",STRINGIMAGE m,", ")
  prefix := STRCONC(prefix,STRINGIMAGE n,", ",STRINGIMAGE ncolq,", ")
  prefix := STRCONC(prefix,STRINGIMAGE lda,",[",thetastring,"],")
  prefix := STRCONC(prefix,matstring,", ",STRINGIMAGE ifail,")")
  linkGen prefix

