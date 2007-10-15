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

e01baf() ==
  htInitPage('"E01BAF - Interpolating functions, cubic spline interpolant, one variable", nil)
  htMakePage '(
    (domainConditions 
       (isDomain PI (PositiveInteger)))
    (text . "\windowlink{Manual Page}{manpageXXe01baf} for this routine ")
    (text . "\newline ")
    (text . "\lispwindowlink{Browser operation page}{(|oPageFrom| '|e01baf| '|NagInterpolationPackage|)} for this routine")
    (text . "\newline \horizontalline ")
    (text . "Determines a cubic B-spline interpolant ")
    (text . "\center{s(x) = \htbitmap{e01baf}} to the points ")
    (text . "(\htbitmap{xiii}, \htbitmap{yi}), for i = 1,2,...,m. ")
    (text . "\blankline ")
    (text . "\menuitemstyle{} \tab{2} ")
    (text . "Enter the number of data points, {\it m}:")
    (text . "\newline\tab{2} ")
    (bcStrings (5 7 m PI))
    (text . "\blankline")
    (text . "\newline")
    (text . "\menuitemstyle{} \tab{2} ")
    (text . "\newline \tab{2} ")
    (text . "Ifail value: ")
    (radioButtons ifail
        ("" " -1, Print error messages" minusOne)
        ("" "  1, Suppress error messages" one)))
  htMakeDoneButton('"Continue", 'e01bafSolve)
  htShowPage()

e01bafSolve htPage ==
  m :=
    $bcParseOnly => PARSE_-INTEGER htpLabelInputString(htPage, 'm)
    objValUnwrap htpLabelSpadValue(htPage, 'm)
  error := htpButtonValue(htPage,'ifail)
  ifail :=
    error = 'one => '1
    '-1
  m = '7 => e01bafDefaultSolve(htPage,ifail)
  labelList :=
    "append"/[f(i) for i in 1..m] where f(i) ==
      prefix := ('"\newline \tab{2} ")
      post   := ('"\tab{32} ")
      xnam := INTERN STRCONC ('"x",STRINGIMAGE i)
      ynam := INTERN STRCONC ('"y",STRINGIMAGE i)
      num := INTERN STRCONC (STRINGIMAGE (QUOTIENT(i,10)),".",STRINGIMAGE (REM(i,10)))
      [['text,:prefix],['bcStrings,[10, num, xnam, 'F]], 
       ['text,:post],['bcStrings,[10, 0.0, ynam, 'F]]]
  equationPart := [
     '(domainConditions 
        (isDomain P (Polynomial $EmptyMode))
         (isDomain S (String))
          (isDomain F (Float))
           (isDomain PI (PositiveInteger))),
            :labelList]
  page := htInitPage("E01BAF - Interpolating functions, cubic spline interpolant, one variable",htpPropertyList htPage)
  htSay '"\menuitemstyle{}\tab{2} Values of x: \tab{30} "
  htSay '"\menuitemstyle{}\tab{32} Corresponding values of y: "
  htMakePage equationPart
  htSay '"\blankline "
  htMakeDoneButton('"Continue",'e01bafGen)
  htpSetProperty(page,'m,m)
  htpSetProperty(page,'ifail,ifail)
  htpSetProperty(page,'inputArea, htpInputAreaAlist htPage)
  htShowPage()

e01bafDefaultSolve (htPage, ifail) ==
  m := '7
  page := htInitPage('"E01BAF - Interpolating functions, cubic spline interpolant, one variable",htpPropertyList htPage)
  htMakePage '(
    (domainConditions 
       (isDomain F (Float)))
    (text . "\newline ")
    (text . "\menuitemstyle{}\tab{2} Values of x: \tab{30} ")
    (text . "\menuitemstyle{}\tab{32} Corresponding values of y: ")
    (text . "\newline \tab{2}")
    (bcStrings (10 "0.0" x1 F))
    (text . "\tab{32} ")
    (bcStrings (10 "1.0000" y1 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "0.2" x2 F))
    (text . "\tab{32} ")
    (bcStrings (10 "1.2214" y2 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "0.4" x3 F))
    (text . "\tab{32} ")
    (bcStrings (10 "1.4918" y3 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "0.6" x4 F))
    (text . "\tab{32} ")
    (bcStrings (10 "1.8221" y4 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "0.75" x5 F))
    (text . "\tab{32} ")
    (bcStrings (10 "2.1170" y5 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "0.9" x6 F))
    (text . "\tab{32} ")
    (bcStrings (10 "2.4596" y6 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "1.0" x7 F))
    (text . "\tab{32} ")
    (bcStrings (10 "2.7183" y7 F))
    (text . "\blankline"))
  htMakeDoneButton('"Continue",'e01bafGen)      
  htpSetProperty(page,'m,m)
  htpSetProperty(page,'ifail,ifail)
  htpSetProperty(page,'inputArea, htpInputAreaAlist htPage)
  htShowPage()


e01bafGen htPage ==
  m := htpProperty(htPage,'m)
  ifail := htpProperty(htPage,'ifail)
  alist := htpInputAreaAlist htPage
  lck := m + 4
  lwrk := 6*m+16
  y := alist
  while y repeat
    right := STRCONC ((first y).1," ")
    y := rest y
    left :=  STRCONC ((first y).1," ")
    y := rest y
    reallist := [left,:reallist]
    imaglist := [right,:imaglist]
  realstring := bcwords2liststring reallist
  imagstring := bcwords2liststring imaglist
  pre := STRCONC ('"e01baf(",STRINGIMAGE m,",[",realstring,"],[",imagstring)
  post := STRCONC ('"],",STRINGIMAGE lck,",",STRINGIMAGE lwrk,",")
  linkGen STRCONC (pre,post,STRINGIMAGE ifail,")")

e01bef() ==
  htInitPage('"E01BEF - Interpolating functions, monoticity preserving, piecewise cubic Hermite, one variable", nil)
  htMakePage '(
    (domainConditions 
       (isDomain PI (PositiveInteger)))
    (text . "\windowlink{Manual Page}{manpageXXe01bef} for this routine ")
    (text . "\newline ")
    (text . "\lispwindowlink{Brow[Cser operation page}{(|oPageFrom| '|e01bef| '|NagInterpolationPackage|)} for this routine")
    (text . "\newline \horizontalline ")
    (text . "Determines derivative estimates defining a monoticity preserving")
    (text . " piecewise cubic Hermite interpolant to the set of points ")
    (text . "(\htbitmap{xr}, \htbitmap{fr}), ")
    (text . "for r = 1,2,...,m. The interpolant, its derivative, and its ")
    (text . "integral can be evaluated by calls to E01BFF, E01BGF or E01BHF. ")
    (text . "\blankline ")
    (text . "\menuitemstyle{} \tab{2} ")
    (text . "Enter the number of data points {\it n} \htbitmap{great=} 2:")
    (text . "\newline\tab{2} ")
    (bcStrings (5 9 n PI))
    (text . "\blankline")
    (text . "\newline")
    (text . "\menuitemstyle{} \tab{2} ")
    (text . "\newline \tab{2} ")
    (text . "Ifail value: ")
    (radioButtons ifail
        ("" " -1, Print error messages" minusOne)
        ("" "  1, Suppress error messages" one)))
  htMakeDoneButton('"Continue", 'e01befSolve)
  htShowPage()

e01befSolve htPage ==
  n :=
    $bcParseOnly => PARSE_-INTEGER htpLabelInputString(htPage, 'n)
    objValUnwrap htpLabelSpadValue(htPage, 'n)
  error := htpButtonValue(htPage,'ifail)
  ifail :=
    error = 'one => '1
    error = 'zero => '0
    '-1
  n = '9 => e01befDefaultSolve(htPage,ifail)
  labelList :=
    "append"/[f(i) for i in 1..n] where f(i) ==
      prefix := ('"\newline \tab{2} ")
      post   := ('"\tab{32} ")
      xnam := INTERN STRCONC ('"x",STRINGIMAGE i)
      ynam := INTERN STRCONC ('"y",STRINGIMAGE i)
      num := INTERN STRCONC (STRINGIMAGE (QUOTIENT(i,10)),".",STRINGIMAGE (REM(i,10)))
      [['text,:prefix],['bcStrings,[10, num, xnam, 'F]], 
       ['text,:post],['bcStrings,[10, 0.0, ynam, 'F]]]
  equationPart := [
     '(domainConditions 
        (isDomain P (Polynomial $EmptyMode))
         (isDomain S (String))
          (isDomain F (Float))
           (isDomain PI (PositiveInteger))),
            :labelList]
  page := htInitPage("E01BEF - Interpolating functions, monoticity preserving, piecewise cubic Hermite, one variable",htpPropertyList htPage)
  htSay '"\menuitemstyle{}\tab{2} Values of \space{1} "
  htSay '"\htbitmap{xr}: \tab{30} "
  htSay '"\menuitemstyle{}\tab{32} Values of \space{1} "
  htSay '"\htbitmap{fr}: "
  htMakePage equationPart
  htSay '"\blankline "
  htMakeDoneButton('"Continue",'e01befGen)
  htpSetProperty(page,'n,n)
  htpSetProperty(page,'ifail,ifail)
  htpSetProperty(page,'inputArea, htpInputAreaAlist htPage)
  htShowPage()

e01befDefaultSolve (htPage, ifail) ==
  n := '9
  page := htInitPage('"E01BEF - Interpolating functions, monoticity preserving, piecewise cubic Hermite, one variable",htpPropertyList htPage)
  htMakePage '(
    (domainConditions 
       (isDomain F (Float)))
    (text . "\newline ")
    (text . "\menuitemstyle{}\tab{2} Values of \space{1} ")
    (text . "\htbitmap{xr}: \tab{30} ")
    (text . "\menuitemstyle{}\tab{32} Values of \space{1} \htbitmap{fr}: ")
    (text . "\newline \tab{2}")
    (bcStrings (10 "7.99" x1 F))
    (text . "\tab{32} ")
    (bcStrings (10 "0.00000e+0" y1 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "8.09" x2 F))
    (text . "\tab{32} ")
    (bcStrings (10 "0.27643e-4" y2 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "8.19" x3 F))
    (text . "\tab{32} ")
    (bcStrings (10 "0.43750e-1" y3 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "8.70" x4 F))
    (text . "\tab{32} ")
    (bcStrings (10 "0.16918" y4 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "9.20" x5 F))
    (text . "\tab{32} ")
    (bcStrings (10 "0.46943" y5 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "10.00" x6 F))
    (text . "\tab{32} ")
    (bcStrings (10 "0.94374" y6 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "12.00" x7 F))
    (text . "\tab{32} ")
    (bcStrings (10 "0.99864" y7 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "15.00" x8 F))
    (text . "\tab{32} ")
    (bcStrings (10 "0.99992" y8 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "20.00" x9 F))
    (text . "\tab{32} ")
    (bcStrings (10 "0.99999" y9 F))
    (text . "\blankline"))
  htMakeDoneButton('"Continue",'e01befGen)      
  htpSetProperty(page,'n,n)
  htpSetProperty(page,'ifail,ifail)
  htpSetProperty(page,'inputArea, htpInputAreaAlist htPage)
  htShowPage()


e01befGen htPage ==
  n := htpProperty(htPage,'n)
  ifail := htpProperty(htPage,'ifail)
  alist := htpInputAreaAlist htPage
  lck := n + 4
  lwrk := 6*n+16
  y := alist
  while y repeat
    right := STRCONC ((first y).1," ")
    y := rest y
    left :=  STRCONC ((first y).1," ")
    y := rest y
    reallist := [left,:reallist]
    imaglist := [right,:imaglist]
  realstring := bcwords2liststring reallist
  imagstring := bcwords2liststring imaglist
  linkGen STRCONC ('"e01bef(",STRINGIMAGE n,",[",realstring,"],[",imagstring,"],",STRINGIMAGE ifail,")")


e01bff() ==
  htInitPage('"E01BFF - Interpolated values, interpolant computed by E01BEF, function only, one variable", nil)
  htMakePage '(
    (domainConditions 
       (isDomain PI (PositiveInteger)))
    (text . "\windowlink{Manual Page}{manpageXXe01bff} for this routine ")
    (text . "\newline ")
    (text . "\lispwindowlink{Browser operation page}{(|oPageFrom| '|e01bff| '|NagInterpolationPackage|)} for this routine")
    (text . "\newline \horizontalline ")
    (text . "Evaluates the piecewise cubic Hermite interpolant computed ")
    (text . "by E01BEF at the set of points \htbitmap{xiii}, ")
    (text . "for i = 1,2,...,m. ")
    (text . "\blankline ")
    (text . "\menuitemstyle{} \tab{2} ")
    (text . "Enter the number of data points {\em n}: ")
    (text . "\newline\tab{2} ")
    (bcStrings (5 9 n PI))
    (text . "\blankline ")
    (text . "\menuitemstyle{} \tab{2} ")
    (text . "Enter the number of evaluation points {\em m}: ")
    (text . "\newline\tab{2} ")
    (bcStrings (5 11 m PI))
    (text . "\blankline")
    (text . "\newline")
    (text . "\menuitemstyle{} \tab{2} ")
    (text . "\newline \tab{2} ")
    (text . "Ifail value: ")
    (radioButtons ifail
        ("" " -1, Print error messages" minusOne)
        ("" "  1, Suppress error messages" one)))
  htMakeDoneButton('"Continue", 'e01bffSolve)
  htShowPage()

e01bffSolve htPage ==
  n :=
    $bcParseOnly => PARSE_-INTEGER htpLabelInputString(htPage, 'n)
    objValUnwrap htpLabelSpadValue(htPage, 'n)
  m :=
    $bcParseOnly => PARSE_-INTEGER htpLabelInputString(htPage, 'm)
    objValUnwrap htpLabelSpadValue(htPage, 'm)
  error := htpButtonValue(htPage,'ifail)
  ifail :=
    error = 'one => '1
    '-1
  (n = '9 and m = '11) => e01bffDefaultSolve(htPage,ifail)
  labelList :=
    "append"/[f(i) for i in 1..n] where f(i) ==
      prefix := ('"\newline \tab{2} ")
      middle := ('"\tab{22} ")
      post   := ('" \tab{42} ")
      xnam := INTERN STRCONC ('"x",STRINGIMAGE i)
      ynam := INTERN STRCONC ('"y",STRINGIMAGE i)
      znam := INTERN STRCONC ('"z",STRINGIMAGE i)
      num := INTERN STRCONC (STRINGIMAGE (QUOTIENT(i,10)),".",STRINGIMAGE (REM(i,10)))
      [['text,:prefix],['bcStrings,[10, num, xnam, 'F]], 
       ['text,:middle],['bcStrings,[10, 0.0, ynam, 'F]],
        ['text,:post],['bcStrings,[10, 0.0, znam, 'F]]]
  pxwords := ('"\blankline \menuitemstyle{}\tab{2} Values of ")
  pxwords := STRCONC(pxwords,'"array {\it Px}: \newline ")
  pxwords := cons('text,pxwords)
  pointList :=
    "append"/[g(j) for j in 1..m] where g(j) ==
      preamb := ('"\newline \tab{2} ")
      pnam := INTERN STRCONC ('"px",STRINGIMAGE j)
      [['text,:preamb],['bcStrings,[10, 0.0, pnam, 'F]]]
  labelList := [:labelList,pxwords,:pointList]
  equationPart := [
     '(domainConditions 
        (isDomain P (Polynomial $EmptyMode))
         (isDomain S (String))
          (isDomain F (Float))
           (isDomain PI (PositiveInteger))),
            :labelList]
  page := htInitPage("E01BFF - Interpolated values, interpolant computed by E01BEF, function only, one variable",htpPropertyList htPage)
  htSay '"\menuitemstyle{}\tab{2} Values of \space{1} "
  htSay '"\htbitmap{xr}: \tab{20} \menuitemstyle{}\tab{22} "
  htSay '"Values of \space{1} \htbitmap{fr}: \tab{40}"
  htSay '"\menuitemstyle{}\tab{42} Values of \space{1} "
  htSay '"\htbitmap{dr}: "
  htMakePage equationPart
  htSay '"\blankline "
  htMakeDoneButton('"Continue",'e01bffGen)
  htpSetProperty(page,'n,n)
  htpSetProperty(page,'m,m)
  htpSetProperty(page,'ifail,ifail)
  htpSetProperty(page,'inputArea, htpInputAreaAlist htPage)
  htShowPage()

e01bffDefaultSolve (htPage, ifail) ==
  n := '9
  m := '11
  page := htInitPage('"E01BFF - Interpolated values, interpolant computed by E01BEF, function only, one variable",htpPropertyList htPage)
  htMakePage '(
    (domainConditions 
       (isDomain F (Float)))
    (text . "\newline ")
    (text . "\menuitemstyle{}\tab{2} Values of \space{1} ")
    (text . "\htbitmap{xr}: \tab{20} \menuitemstyle{}\tab{22} ")
    (text . "Values of \space{1} \htbitmap{fr}: \tab{40} ")
    (text . "\menuitemstyle{}\tab{42} Values of \space{1} ")
    (text . "\htbitmap{dr}: ")
    (text . "\newline \tab{2}")
    (bcStrings (10 "7.99" x1 F))
    (text . "\tab{22} ")
    (bcStrings (10 "0.00000e+0" y1 F))
    (text . "\tab{42} ")
    (bcStrings (10 "0.00000e+0" z1 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "8.09" x2 F))
    (text . "\tab{22} ")
    (bcStrings (10 "0.27643e-4" y2 F))
    (text . "\tab{42} ")
    (bcStrings (10 "5.52510e-4" z2 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "8.19" x3 F))
    (text . "\tab{22} ")
    (bcStrings (10 "0.43750e-1" y3 F))
    (text . "\tab{42} ")
    (bcStrings (10 "0.33587" z3 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "8.70" x4 F))
    (text . "\tab{22} ")
    (bcStrings (10 "0.16918" y4 F))
    (text . "\tab{42} ")
    (bcStrings (10 "0.34944" z4 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "9.20" x5 F))
    (text . "\tab{22} ")
    (bcStrings (10 "0.46943" y5 F))
    (text . "\tab{42} ")
    (bcStrings (10 "0.59696" z5 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "10.00" x6 F))
    (text . "\tab{22} ")
    (bcStrings (10 "0.94374" y6 F))
    (text . "\tab{42} ")
    (bcStrings (10 "6.03260e-2" z6 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "12.00" x7 F))
    (text . "\tab{22} ")
    (bcStrings (10 "0.99864" y7 F))
    (text . "\tab{42} ")
    (bcStrings (10 "8.98335e-4" z7 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "15.00" x8 F))
    (text . "\tab{22} ")
    (bcStrings (10 "0.99992" y8 F))
    (text . "\tab{42} ")
    (bcStrings (10 "2.93954e-5" z8 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "20.00" x9 F))
    (text . "\tab{22} ")
    (bcStrings (10 "0.99999" y9 F))
    (text . "\tab{42} ")
    (bcStrings (10 "0.00000" z9 F))
    (text . "\blankline")
    (text . "\menuitemstyle{}\tab{2} ")
    (text . "Values of array {\it Px}: ")
    (text . "\newline \tab{2} ")
    (bcStrings (10 "7.99" px1 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "9.191" px2 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "10.392" px3 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "11.593" px4 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "12.794" px5 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "13.995" px6 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "15.196" px7 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "16.397" px8 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "17.598" px9 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "18.799" px10 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "20.0" px11 F))
    (text . "\blankline"))
  htMakeDoneButton('"Continue",'e01bffGen)      
  htpSetProperty(page,'n,n)
  htpSetProperty(page,'m,m)
  htpSetProperty(page,'ifail,ifail)
  htpSetProperty(page,'inputArea, htpInputAreaAlist htPage)
  htShowPage()


e01bffGen htPage ==
  n := htpProperty(htPage,'n)
  m := htpProperty(htPage,'m)
  ifail := htpProperty(htPage,'ifail)
  alist := htpInputAreaAlist htPage
  lck := n + 4
  lwrk := 6*n+16
  y := alist
  for i in 1..m repeat
    px := STRCONC ((first y).1," ")
    y := rest y
    pxlist := [px,:pxlist]
  pxstring := bcwords2liststring pxlist
  while y repeat
    right := STRCONC ((first y).1," ")
    y := rest y    
    mid :=  STRCONC ((first y).1," ")
    y := rest y
    left :=  STRCONC ((first y).1," ")
    y := rest y
    xlist := [left,:xlist]
    flist := [mid,:flist]
    dlist := [right,:dlist]
  xstring := bcwords2liststring xlist
  fstring := bcwords2liststring flist
  dstring := bcwords2liststring dlist
  prefix := STRCONC('"e01bff(",STRINGIMAGE n,",[",xstring,"],[",fstring)
  prefix := STRCONC(prefix,"],[",dstring,"],",STRINGIMAGE m,",[",pxstring,"],")
  prefix := STRCONC(prefix,STRINGIMAGE ifail,")")
  linkGen prefix

e01bgf() ==
  htInitPage('"E01BGF - Interpolated values, interpolant computed by E01BEF, function and 1st derivative, one variable", nil)
  htMakePage '(
    (domainConditions 
       (isDomain PI (PositiveInteger)))
    (text . "\windowlink{Manual Page}{manpageXXe01bgf} for this routine ")
    (text . "\newline ")
    (text . "\lispwindowlink{Browser operation page}{(|oPageFrom| '|e01bgf| '|NagInterpolationPackage|)} for this routine")
    (text . "\newline \horizontalline ")
    (text . "Evaluates the piecewise cubic Hermite interpolant computed ")
    (text . "by E01BEF and its 1st derivative at the set of points \space{1} ")
    (text . "\htbitmap{xiii}, for i = 1,2,...,m. ")
    (text . "\blankline ")
    (text . "\menuitemstyle{} \tab{2} ")
    (text . "Enter the number of data points {\em n}: ")
    (text . "\newline\tab{2} ")
    (bcStrings (5 9 n PI))
    (text . "\blankline ")
    (text . "\menuitemstyle{} \tab{2} ")
    (text . "Enter the number of evaluation points {\em m}: ")
    (text . "\newline\tab{2} ")
    (bcStrings (5 11 m PI))
    (text . "\blankline")
    (text . "\newline")
    (text . "\menuitemstyle{} \tab{2} ")
    (text . "\newline \tab{2} ")
    (text . "Ifail value: ")
    (radioButtons ifail
        ("" " -1, Print error messages" minusOne)
        ("" "  1, Suppress error messages" one)))
  htMakeDoneButton('"Continue", 'e01bgfSolve)
  htShowPage()

e01bgfSolve htPage ==
  n :=
    $bcParseOnly => PARSE_-INTEGER htpLabelInputString(htPage, 'n)
    objValUnwrap htpLabelSpadValue(htPage, 'n)
  m :=
    $bcParseOnly => PARSE_-INTEGER htpLabelInputString(htPage, 'm)
    objValUnwrap htpLabelSpadValue(htPage, 'm)
  error := htpButtonValue(htPage,'ifail)
  ifail :=
    error = 'one => '1
    '-1
  (n = '9 and m = '11) => e01bgfDefaultSolve(htPage,ifail)
  labelList :=
    "append"/[f(i) for i in 1..n] where f(i) ==
      prefix := ('"\newline \tab{2} ")
      middle := ('"\tab{22} ")
      post   := ('" \tab{42} ")
      xnam := INTERN STRCONC ('"x",STRINGIMAGE i)
      ynam := INTERN STRCONC ('"y",STRINGIMAGE i)
      znam := INTERN STRCONC ('"z",STRINGIMAGE i)
      num := INTERN STRCONC (STRINGIMAGE (QUOTIENT(i,10)),".",STRINGIMAGE (REM(i,10)))
      [['text,:prefix],['bcStrings,[10, num, xnam, 'F]], 
       ['text,:middle],['bcStrings,[10, 0.0, ynam, 'F]],
        ['text,:post],['bcStrings,[10, 0.0, znam, 'F]]]
  pxwords := ('"\blankline \menuitemstyle{}\tab{2} Values of ")
  pxwords := STRCONC(pxwords,'"array {\it Px}: \newline ")
  pxwords := cons('text,pxwords)
  pointList :=
    "append"/[g(j) for j in 1..m] where g(j) ==
      preamb := ('"\newline \tab{2} ")
      pnam := INTERN STRCONC ('"px",STRINGIMAGE j)
      [['text,:preamb],['bcStrings,[10, 0.0, pnam, 'F]]]
  labelList := [:labelList,pxwords,:pointList]
  equationPart := [
     '(domainConditions 
        (isDomain P (Polynomial $EmptyMode))
         (isDomain S (String))
          (isDomain F (Float))
           (isDomain PI (PositiveInteger))),
            :labelList]
  page := htInitPage("E01BGF - Interpolated values, interpolant computed by E01BEF, function and 1st derivative, one variable",htpPropertyList htPage)
  htSay '"\menuitemstyle{}\tab{2} Values of \space{1} "
  htSay '"\htbitmap{xr}: \tab{20} \menuitemstyle{}\tab{22} "
  htSay '"Values of \space{1} \htbitmap{fr}: \tab{40}"
  htSay '"\menuitemstyle{}\tab{42} Values of \space{1} "
  htSay '"\htbitmap{dr}: "
  htMakePage equationPart
  htSay '"\blankline "
  htMakeDoneButton('"Continue",'e01bgfGen)
  htpSetProperty(page,'n,n)
  htpSetProperty(page,'m,m)
  htpSetProperty(page,'ifail,ifail)
  htpSetProperty(page,'inputArea, htpInputAreaAlist htPage)
  htShowPage()

e01bgfDefaultSolve (htPage, ifail) ==
  n := '9
  m := '11
  page := htInitPage('"E01BGF - Interpolated values, interpolant computed by E01BEF, function and 1st derivative, one variable",htpPropertyList htPage)
  htMakePage '(
    (domainConditions 
       (isDomain F (Float)))
    (text . "\menuitemstyle{}\tab{2} Values of \space{1} ")
    (text . "\htbitmap{xr}: \tab{20} \menuitemstyle{}\tab{22} ")
    (text . "Values of \space{1} \htbitmap{fr}: \tab{40} ")
    (text . "\menuitemstyle{}\tab{42} Values of \space{1} ")
    (text . "\htbitmap{dr}: ")
    (text . "\newline \tab{2}")
    (bcStrings (10 "7.99" x1 F))
    (text . "\tab{22} ")
    (bcStrings (10 "0.00000e+0" y1 F))
    (text . "\tab{42} ")
    (bcStrings (10 "0.00000e+0" z1 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "8.09" x2 F))
    (text . "\tab{22} ")
    (bcStrings (10 "0.27643e-4" y2 F))
    (text . "\tab{42} ")
    (bcStrings (10 "5.52510e-4" z2 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "8.19" x3 F))
    (text . "\tab{22} ")
    (bcStrings (10 "0.43750e-1" y3 F))
    (text . "\tab{42} ")
    (bcStrings (10 "0.33587" z3 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "8.70" x4 F))
    (text . "\tab{22} ")
    (bcStrings (10 "0.16918" y4 F))
    (text . "\tab{42} ")
    (bcStrings (10 "0.34944" z4 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "9.20" x5 F))
    (text . "\tab{22} ")
    (bcStrings (10 "0.46943" y5 F))
    (text . "\tab{42} ")
    (bcStrings (10 "0.59696" z5 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "10.00" x6 F))
    (text . "\tab{22} ")
    (bcStrings (10 "0.94374" y6 F))
    (text . "\tab{42} ")
    (bcStrings (10 "6.03260e-2" z6 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "12.00" x7 F))
    (text . "\tab{22} ")
    (bcStrings (10 "0.99864" y7 F))
    (text . "\tab{42} ")
    (bcStrings (10 "8.98335e-4" z7 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "15.00" x8 F))
    (text . "\tab{22} ")
    (bcStrings (10 "0.99992" y8 F))
    (text . "\tab{42} ")
    (bcStrings (10 "2.93954e-5" z8 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "20.00" x9 F))
    (text . "\tab{22} ")
    (bcStrings (10 "0.99999" y9 F))
    (text . "\tab{42} ")
    (bcStrings (10 "0.00000" z9 F))
    (text . "\blankline")
    (text . "\menuitemstyle{}\tab{2} ")
    (text . "Values of array {\it Px}: ")
    (text . "\newline \tab{2} ")
    (bcStrings (10 "7.99" px1 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "9.191" px2 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "10.392" px3 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "11.593" px4 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "12.794" px5 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "13.995" px6 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "15.196" px7 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "16.397" px8 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "17.598" px9 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "18.799" px10 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "20.0" px11 F))
    (text . "\blankline"))
  htMakeDoneButton('"Continue",'e01bgfGen)      
  htpSetProperty(page,'n,n)
  htpSetProperty(page,'m,m)
  htpSetProperty(page,'ifail,ifail)
  htpSetProperty(page,'inputArea, htpInputAreaAlist htPage)
  htShowPage()


e01bgfGen htPage ==
  n := htpProperty(htPage,'n)
  m := htpProperty(htPage,'m)
  ifail := htpProperty(htPage,'ifail)
  alist := htpInputAreaAlist htPage
  lck := n + 4
  lwrk := 6*n+16
  y := alist
  for i in 1..m repeat
    px := STRCONC ((first y).1," ")
    y := rest y
    pxlist := [px,:pxlist]
  pxstring := bcwords2liststring pxlist
  while y repeat
    right := STRCONC ((first y).1," ")
    y := rest y    
    mid :=  STRCONC ((first y).1," ")
    y := rest y
    left :=  STRCONC ((first y).1," ")
    y := rest y
    xlist := [left,:xlist]
    flist := [mid,:flist]
    dlist := [right,:dlist]
  xstring := bcwords2liststring xlist
  fstring := bcwords2liststring flist
  dstring := bcwords2liststring dlist
  prefix := STRCONC('"e01bgf(",STRINGIMAGE n,",[",xstring,"],[",fstring)
  prefix := STRCONC(prefix,"],[",dstring,"],",STRINGIMAGE m,",[",pxstring,"],")
  prefix := STRCONC(prefix,STRINGIMAGE ifail,")")
  linkGen prefix

e01bhf() ==
  htInitPage('"E01BHF - Interpolated values, interpolant computed by E01BEF, definite integral, one variable", nil)
  htMakePage '(
    (domainConditions 
       (isDomain PI (PositiveInteger)))
    (text . "\windowlink{Manual Page}{manpageXXe01bhf} for this routine ")
    (text . "\newline ")
    (text . "\lispwindowlink{Browser operation page}{(|oPageFrom| '|e01bhf| '|NagInterpolationPackage|)} for this routine")
    (text . "\newline \horizontalline ")
    (text . "Evaluates the definite integral of the piecewise cubic Hermite ")
    (text . "interpolant computed by E01BEF over the interval [a,b]. ")
    (text . "\blankline ")
    (text . "\menuitemstyle{} \tab{2} ")
    (text . "Enter the number of data points {\em n}: ")
    (text . "\newline\tab{2} ")
    (bcStrings (5 9 n PI))
    (text . "\blankline ")
    (text . "\newline \menuitemstyle{}\tab{2} ")
    (text . "\newline {\em Lower} bound {\it a}: ")
    (text . "\tab{32} \menuitemstyle{}\tab{34} ")
    (text . "{\em Upper} bound {\it b}:")
    (text . "\newline\tab{2} ")
    (bcStrings (20 "7.99" a F))
    (text . "\tab{34} ")
    (bcStrings (20 "20.0" b EM))
    (text . "\blankline")
    (text . "\newline")
    (text . "\menuitemstyle{} \tab{2} ")
    (text . "\newline \tab{2} ")
    (text . "Ifail value: ")
    (radioButtons ifail
        ("" " -1, Print error messages" minusOne)
        ("" "  1, Suppress error messages" one)))
  htMakeDoneButton('"Continue", 'e01bhfSolve)
  htShowPage()

e01bhfSolve htPage ==
  n :=
    $bcParseOnly => PARSE_-INTEGER htpLabelInputString(htPage, 'n)
    objValUnwrap htpLabelSpadValue(htPage, 'n)
  a := htpLabelInputString(htPage,'a)
  b := htpLabelInputString(htPage,'b)
  error := htpButtonValue(htPage,'ifail)
  ifail :=
    error = 'one => '1
    '-1
  n = '9 => e01bhfDefaultSolve(htPage,a,b,ifail)
  labelList :=
    "append"/[f(i) for i in 1..n] where f(i) ==
      prefix := ('"\newline \tab{2} ")
      middle := ('"\tab{22} ")
      post   := ('" \tab{42} ")
      xnam := INTERN STRCONC ('"x",STRINGIMAGE i)
      ynam := INTERN STRCONC ('"y",STRINGIMAGE i)
      znam := INTERN STRCONC ('"z",STRINGIMAGE i)
      num := INTERN STRCONC (STRINGIMAGE (QUOTIENT(i,10)),".",STRINGIMAGE (REM(i,10)))
      [['text,:prefix],['bcStrings,[10, num, xnam, 'F]], 
       ['text,:middle],['bcStrings,[10, 0.0, ynam, 'F]],
        ['text,:post],['bcStrings,[10, 0.0, znam, 'F]]]
  equationPart := [
     '(domainConditions 
        (isDomain P (Polynomial $EmptyMode))
         (isDomain S (String))
          (isDomain F (Float))
           (isDomain PI (PositiveInteger))),
            :labelList]
  page := htInitPage("E01BHF - Interpolated values, interpolant computed by E01BEF, definite integral, one variable",htpPropertyList htPage)
  htSay '"\menuitemstyle{}\tab{2} Values of \space{1} "
  htSay '"\htbitmap{xr}: \tab{20} \menuitemstyle{}\tab{22} "
  htSay '"Values of \space{1} \htbitmap{fr}: \tab{40}"
  htSay '"\menuitemstyle{}\tab{42} Values of \space{1} "
  htSay '"\htbitmap{dr}: "
  htMakePage equationPart
  htSay '"\blankline "
  htMakeDoneButton('"Continue",'e01bhfGen)
  htpSetProperty(page,'n,n)
  htpSetProperty(page,'a,a)
  htpSetProperty(page,'b,b)
  htpSetProperty(page,'ifail,ifail)
  htpSetProperty(page,'inputArea, htpInputAreaAlist htPage)
  htShowPage()

e01bhfDefaultSolve (htPage,a,b,ifail) ==
  n := '9
  page := htInitPage('"E01BHF - Interpolated values, interpolant computed by E01BEF, definite integral, one variable",htpPropertyList htPage)
  htMakePage '(
    (domainConditions 
       (isDomain F (Float)))
    (text . "\menuitemstyle{}\tab{2} Values of \space{1} ")
    (text . "\htbitmap{xr}: \tab{20} \menuitemstyle{}\tab{22} ")
    (text . "Values of \space{1} \htbitmap{fr}: \tab{40} ")
    (text . "\menuitemstyle{}\tab{42} Values of \space{1} ")
    (text . "\htbitmap{dr}: ")
    (text . "\newline \tab{2}")
    (bcStrings (10 "7.99" x1 F))
    (text . "\tab{22} ")
    (bcStrings (10 "0.00000e+0" y1 F))
    (text . "\tab{42} ")
    (bcStrings (10 "0.00000e+0" z1 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "8.09" x2 F))
    (text . "\tab{22} ")
    (bcStrings (10 "0.27643e-4" y2 F))
    (text . "\tab{42} ")
    (bcStrings (10 "5.52510e-4" z2 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "8.19" x3 F))
    (text . "\tab{22} ")
    (bcStrings (10 "0.43750e-1" y3 F))
    (text . "\tab{42} ")
    (bcStrings (10 "0.33587" z3 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "8.70" x4 F))
    (text . "\tab{22} ")
    (bcStrings (10 "0.16918" y4 F))
    (text . "\tab{42} ")
    (bcStrings (10 "0.34944" z4 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "9.20" x5 F))
    (text . "\tab{22} ")
    (bcStrings (10 "0.46943" y5 F))
    (text . "\tab{42} ")
    (bcStrings (10 "0.59696" z5 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "10.00" x6 F))
    (text . "\tab{22} ")
    (bcStrings (10 "0.94374" y6 F))
    (text . "\tab{42} ")
    (bcStrings (10 "6.03260e-2" z6 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "12.00" x7 F))
    (text . "\tab{22} ")
    (bcStrings (10 "0.99864" y7 F))
    (text . "\tab{42} ")
    (bcStrings (10 "8.98335e-4" z7 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "15.00" x8 F))
    (text . "\tab{22} ")
    (bcStrings (10 "0.99992" y8 F))
    (text . "\tab{42} ")
    (bcStrings (10 "2.93954e-5" z8 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "20.00" x9 F))
    (text . "\tab{22} ")
    (bcStrings (10 "0.99999" y9 F))
    (text . "\tab{42} ")
    (bcStrings (10 "0.00000" z9 F))
    (text . "\blankline"))
  htMakeDoneButton('"Continue",'e01bhfGen)      
  htpSetProperty(page,'n,n)
  htpSetProperty(page,'a,a)
  htpSetProperty(page,'b,b)
  htpSetProperty(page,'ifail,ifail)
  htpSetProperty(page,'inputArea, htpInputAreaAlist htPage)
  htShowPage()


e01bhfGen htPage ==
  n := htpProperty(htPage,'n)
  a := htpProperty(htPage,'a)
  b := htpProperty(htPage,'b)
  ifail := htpProperty(htPage,'ifail)
  alist := htpInputAreaAlist htPage
  lck := n + 4
  lwrk := 6*n+16
  y := alist
  while y repeat
    right := STRCONC ((first y).1," ")
    y := rest y    
    mid :=  STRCONC ((first y).1," ")
    y := rest y
    left :=  STRCONC ((first y).1," ")
    y := rest y
    xlist := [left,:xlist]
    flist := [mid,:flist]
    dlist := [right,:dlist]
  xstring := bcwords2liststring xlist
  fstring := bcwords2liststring flist
  dstring := bcwords2liststring dlist
  prefix := STRCONC('"e01bhf(",STRINGIMAGE n,",[",xstring,"],[",fstring,"],[")
  prefix := STRCONC(prefix,dstring,"],",STRINGIMAGE a,",",STRINGIMAGE b,",",STRINGIMAGE ifail,")")
  linkGen prefix


e01daf() ==
  htInitPage('"E01DAF - Interpolating functions, fitting bicubic spline, data on a rectangular grid", nil)
  htMakePage '(
    (domainConditions 
       (isDomain PI (PositiveInteger)))
    (text . "\windowlink{Manual Page}{manpageXXe01daf} for this routine ")
    (text . "\newline ")
    (text . "\lispwindowlink{Browser operation page}{(|oPageFrom| '|e01daf| '|NagInterpolationPackage|)} for this routine")
    (text . "\newline \horizontalline ")
    (text . "Determines a bicubic spline surface interpolating the set of ")
    (text . "data values (\htbitmap{xr}, \htbitmap{yr}, \htbitmap{fqr}) ")
    (text . "given on a rectangular grid. The grid is defined by ")
    (text . "\space{1} \htbitmap{mx} points along the x-axis and ")
    (text . "\space{1} \htbitmap{my} points along the y-axis. The ")
    (text . "spline has \space{1} \htbitmap{px} knots ")
    (text . "\htbitmap{lamdai} and \space{1}\htbitmap{py}")
    (text . " knots  \htbitmap{mui} in the x- and y-directions ")
    (text . "respectively, and is given in the B-spline representation ")
    (text . "\center{s(x,y) = \htbitmap{e01daf1}} ")
    (text . "\blankline ")
    (text . "\newline \menuitemstyle{}\tab{2} ")
    (text . "\newline The value \space{1} \htbitmap{mx}: ")
    (text . "\tab{32} \menuitemstyle{}\tab{34} ")
    (text . "The value \space{1} \htbitmap{my}:")
    (text . "\newline\tab{2} ")
    (bcStrings (6 7 mx PI))
    (text . "\tab{34} ")
    (bcStrings (6 6 my PI))
    (text . "\blankline")
    (text . "\newline")
    (text . "\menuitemstyle{} \tab{2} ")
    (text . "\newline \tab{2} ")
    (text . "Ifail value: ")
    (radioButtons ifail
        ("" " -1, Print error messages" minusOne)
        ("" "  1, Suppress error messages" one)))
  htMakeDoneButton('"Continue", 'e01dafSolve)
  htShowPage()

e01dafSolve htPage ==
  mx :=
    $bcParseOnly => PARSE_-INTEGER htpLabelInputString(htPage, 'mx)
    objValUnwrap htpLabelSpadValue(htPage, 'mx)
  my :=
    $bcParseOnly => PARSE_-INTEGER htpLabelInputString(htPage, 'my)
    objValUnwrap htpLabelSpadValue(htPage, 'my)
  error := htpButtonValue(htPage,'ifail)
  ifail :=
    error = 'one => '1
    '-1
  (mx = '7 and my = '6) => e01dafDefaultSolve(htPage,ifail)
  xList :=
    "append"/[f(i) for i in 1..mx] where f(i) ==
      xnam := INTERN STRCONC ('"x",STRINGIMAGE i)
      [['bcStrings,[6, 0.0, xnam, 'F]]]
  prefix := ('"\newline \menuitemstyle{}\tab{2} Values of X(1) to X(MX): \newline ")
  xList := [['text,:prefix],:xList]
  yList :=
    "append"/[g(i) for i in 1..my] where g(i) ==
      ynam := INTERN STRCONC ('"y",STRINGIMAGE i)
      [['bcStrings,[6, 0.0, ynam, 'F]]]
  prefix := ('"\blankline\menuitemstyle{}\tab{2}Values of Y(1) to Y(MY): \newline ")
  yList := [['text,:prefix],:yList]
  fList := 
    "append"/[h(j,my) for j in 1..mx] where h(j,my) ==
      tempList := 
        "append"/[k(j,m) for m in 1..my] where k(j,m) == 
          fnam := INTERN STRCONC ('"f",STRINGIMAGE j, STRINGIMAGE m)
          [['bcStrings,[6, 0.0, fnam, 'F]]]
      prefix := ('"\newline ")
      tempList := [['text,:prefix],:tempList]
  prefix := ('"\blankline \menuitemstyle{} \tab{2} Values of F(MX*MY) ")
  prefix := STRCONC(prefix,'"(x down, y across): ")
  fList := [['text,:prefix],:fList]
  equationPart := [
     '(domainConditions 
        (isDomain P (Polynomial $EmptyMode))
         (isDomain S (String))
          (isDomain F (Float))
           (isDomain PI (PositiveInteger))),
               :xList,:yList,:fList]
  page := htInitPage("E01DAF - Interpolating functions, fitting bicubic spline, data on a rectanglar grid",htpPropertyList htPage)
  htMakePage equationPart
  htSay '"\blankline "
  htMakeDoneButton('"Continue",'e01dafGen)
  htpSetProperty(page,'mx,mx)
  htpSetProperty(page,'my,my)
  htpSetProperty(page,'ifail,ifail)
  htpSetProperty(page,'inputArea, htpInputAreaAlist htPage)
  htShowPage()

e01dafDefaultSolve (htPage,ifail) ==
  mx := '7
  my := '6
  page := htInitPage('"E01DAF - Interpolating functions, fitting bicubic spline, data on rectangular grid",htpPropertyList htPage)
  htMakePage '(
    (domainConditions 
       (isDomain F (Float)))
    (text . "\menuitemstyle{}\tab{2} Values of X(1) to X(MX): ")
    (text . "\newline ")
    (bcStrings (6 "1.00" x1 F))
    (bcStrings (6 "1.10" x2 F))
    (bcStrings (6 "1.30" x3 F))
    (bcStrings (6 "1.50" x4 F))
    (bcStrings (6 "1.60" x5 F))
    (bcStrings (6 "1.80" x6 F))
    (bcStrings (6 "2.00" x7 F))
    (text . "\blankline ")
    (text . "\newline ")
    (text ."\menuitemstyle{} \tab{2} Values of Y(1) to Y(MY): ")
    (text . "\newline  ")
    (bcStrings (6 "0.00" y1 F))
    (bcStrings (6 "0.10" y2 F))
    (bcStrings (6 "0.40" y3 F))
    (bcStrings (6 "0.70" y4 F))
    (bcStrings (6 "0.90" y5 F))
    (bcStrings (6 "1.00" y6 F))
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{} \tab{2} Values of F(MX*MY) (x down, y across): ")
    (text . "\newline ")
    (bcStrings (6 "1.00" z11 F))
    (bcStrings (6 "1.10" z21 F))
    (bcStrings (6 "1.40" z31 F))
    (bcStrings (6 "1.70" z41 F))
    (bcStrings (6 "1.90" z51 F))
    (bcStrings (6 "2.00" z61 F))
    (text . "\newline ")
    (bcStrings (6 "1.21" z12 F))
    (bcStrings (6 "1.31" z22 F))
    (bcStrings (6 "1.61" z32 F))
    (bcStrings (6 "1.91" z42 F))
    (bcStrings (6 "2.11" z52 F))
    (bcStrings (6 "2.21" z62 F))
    (text . "\newline ")
    (bcStrings (6 "1.69" z13 F))
    (bcStrings (6 "1.79" z23 F))
    (bcStrings (6 "2.09" z33 F))
    (bcStrings (6 "2.39" z43 F))
    (bcStrings (6 "2.59" z53 F))
    (bcStrings (6 "2.69" z63 F))
    (text . "\newline ")
    (bcStrings (6 "2.25" z14 F))
    (bcStrings (6 "2.35" z24 F))
    (bcStrings (6 "2.65" z34 F))
    (bcStrings (6 "2.95" z44 F))
    (bcStrings (6 "3.15" z54 F))
    (bcStrings (6 "3.25" z64 F))
    (text . "\newline ")
    (bcStrings (6 "2.56" z15 F))
    (bcStrings (6 "2.66" z25 F))
    (bcStrings (6 "2.96" z35 F))
    (bcStrings (6 "3.26" z45 F))
    (bcStrings (6 "3.46" z55 F))
    (bcStrings (6 "3.56" z65 F))
    (text . "\newline ")
    (bcStrings (6 "3.24" z16 F))
    (bcStrings (6 "3.34" z26 F))
    (bcStrings (6 "3.64" z36 F))
    (bcStrings (6 "3.94" z46 F))
    (bcStrings (6 "4.14" z56 F))
    (bcStrings (6 "4.24" z66 F))
    (text . "\newline ")
    (bcStrings (6 "4.00" z17 F))
    (bcStrings (6 "4.10" z27 F))
    (bcStrings (6 "4.40" z37 F))
    (bcStrings (6 "4.70" z47 F))
    (bcStrings (6 "4.90" z57 F))
    (bcStrings (6 "5.00" z67 F))
    (text . "\blankline"))
  htMakeDoneButton('"Continue",'e01dafGen)      
  htpSetProperty(page,'mx,mx)
  htpSetProperty(page,'my,my)
  htpSetProperty(page,'ifail,ifail)
  htpSetProperty(page,'inputArea, htpInputAreaAlist htPage)
  htShowPage()


e01dafGen htPage ==
  mx := htpProperty(htPage,'mx)
  my := htpProperty(htPage,'my)
  ifail := htpProperty(htPage,'ifail)
  alist := htpInputAreaAlist htPage
  y := alist
  while y repeat
    right := STRCONC ((first y).1, " ")
    y := rest y
    xlist := [right,:xlist]
  for i in 1..mx repeat
    xmx := [:xmx,(first xlist)] 
    xlist := rest xlist
  xstring := bcwords2liststring xmx
  for i in 1..my repeat
    ymy := [:ymy,(first xlist)]
    xlist := rest xlist
  ystring := bcwords2liststring ymy
  fstring := bcwords2liststring xlist
  prefix := STRCONC('"e01daf(",STRINGIMAGE mx,", ",STRINGIMAGE my,",[")
  midd := STRCONC(xstring, "], [",ystring,"], [",fstring,"], ")
  linkGen STRCONC(prefix,midd,STRINGIMAGE ifail,")")

e01saf() ==
  htInitPage('"E01SAF - Interpolating functions, method of Renka and Cline, two variables", nil)
  htMakePage '(
    (domainConditions 
       (isDomain PI (PositiveInteger)))
    (text . "\windowlink{Manual Page}{manpageXXe01saf} for this routine ")
    (text . "\newline ")
    (text . "\lispwindowlink{Browser operation page}{(|oPageFrom| '|e01saf| '|NagInterpolationPackage|)} for this routine")
    (text . "\newline \horizontalline ")
    (text . "Determines a \htbitmap{c1} piecewise polynomial ")
    (text . "surface F(x,y) interpolating the set of scattered points ")
    (text . "(\htbitmap{xr}, \htbitmap{yr}, \htbitmap{fqr}), ")
    (text . "for r = 1,2,...,m, using a method of Renka and Cline. ")
    (text . "The interpolant can be evaluated using E01SBF. ")
    (text . "\blankline ")
    (text . "\newline \menuitemstyle{}\tab{2} \newline ")
    (text . "Number of data points {\em m} \htbitmap{great=} 3:")
    (text . "\newline \tab{2} ")
    (bcStrings (6 30 m PI))
    (text . "\blankline")
    (text . "\newline")
    (text . "\menuitemstyle{} \tab{2} ")
    (text . "\newline \tab{2} ")
    (text . "Ifail value: ")
    (radioButtons ifail
        ("" " -1, Print error messages" minusOne)
        ("" "  1, Suppress error messages" one)))
  htMakeDoneButton('"Continue", 'e01safSolve)
  htShowPage()

e01safSolve htPage ==
  m :=
    $bcParseOnly => PARSE_-INTEGER htpLabelInputString(htPage, 'm)
    objValUnwrap htpLabelSpadValue(htPage, 'm)
  error := htpButtonValue(htPage,'ifail)
  ifail :=
    error = 'one => '1
    '-1
  m = '30 => e01safDefaultSolve(htPage,ifail)
  labelList :=
    "append"/[f(i) for i in 1..m] where f(i) ==
      prefix := ('"\newline \tab{2} ")
      middle := ('"\tab{22} ")
      post   := ('" \tab{42} ")
      xnam := INTERN STRCONC ('"x",STRINGIMAGE i)
      ynam := INTERN STRCONC ('"y",STRINGIMAGE i)
      znam := INTERN STRCONC ('"z",STRINGIMAGE i)
      num := INTERN STRCONC (STRINGIMAGE (QUOTIENT(i,10)),".",STRINGIMAGE (REM(i,10)))
      [['text,:prefix],['bcStrings,[10, num, xnam, 'F]], 
       ['text,:middle],['bcStrings,[10, 0.0, ynam, 'F]],
        ['text,:post],['bcStrings,[10, 0.0, znam, 'F]]]
  equationPart := [
     '(domainConditions 
        (isDomain P (Polynomial $EmptyMode))
         (isDomain S (String))
          (isDomain F (Float))
           (isDomain PI (PositiveInteger))),
            :labelList]
  page := htInitPage("E01SAF - Interpolating functions, method of Renka and Cline,two variables",htpPropertyList htPage)
  htSay '"\menuitemstyle{}\tab{2} Values of \space{1} "
  htSay '"\htbitmap{xr}: \tab{20} \menuitemstyle{}\tab{22} "
  htSay '"Values of \space{1} \htbitmap{fr}: \tab{40}"
  htSay '"\menuitemstyle{}\tab{42} Values of \space{1} "
  htSay '"\htbitmap{dr}: "
  htMakePage equationPart
  htSay '"\blankline "
  htMakeDoneButton('"Continue",'e01safGen)
  htpSetProperty(page,'m,m)
  htpSetProperty(page,'ifail,ifail)
  htpSetProperty(page,'inputArea, htpInputAreaAlist htPage)
  htShowPage()


e01safDefaultSolve  (htPage, ifail) ==
  m := '30
  page := htInitPage('"E01SAF - Interpolating functions, method of Renka and Cline, two variables",htpPropertyList htPage)
  htMakePage '(
    (domainConditions 
       (isDomain F (Float)))
    (text . "\menuitemstyle{}\tab{2} Values of \space{1} ")
    (text . "\htbitmap{xr}: \tab{20} \menuitemstyle{}\tab{22} ")
    (text . "Values of \space{1} \htbitmap{fr}: \tab{40} ")
    (text . "\menuitemstyle{}\tab{42} Values of \space{1} ")
    (text . "\htbitmap{dr}: ")
    (text . "\newline \tab{2}")
    (bcStrings (10 "11.16" x1 F))
    (text . "\tab{22} ")
    (bcStrings (10 "1.24" y1 F))
    (text . "\tab{42} ")
    (bcStrings (10 "22.15" z1 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "12.85" x2 F))
    (text . "\tab{22} ")
    (bcStrings (10 "3.06" y2 F))
    (text . "\tab{42} ")
    (bcStrings (10 "22.11" z2 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "19.85" x3 F))
    (text . "\tab{22} ")
    (bcStrings (10 "10.72" y3 F))
    (text . "\tab{42} ")
    (bcStrings (10 "7.97" z3 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "19.72" x4 F))
    (text . "\tab{22} ")
    (bcStrings (10 "1.39" y4 F))
    (text . "\tab{42} ")
    (bcStrings (10 "16.83" z4 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "15.91" x5 F))
    (text . "\tab{22} ")
    (bcStrings (10 "7.74" y5 F))
    (text . "\tab{42} ")
    (bcStrings (10 "15.30" z5 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "0.00" x6 F))
    (text . "\tab{22} ")
    (bcStrings (10 "20.00" y6 F))
    (text . "\tab{42} ")
    (bcStrings (10 "34.60" z6 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "20.87" x7 F))
    (text . "\tab{22} ")
    (bcStrings (10 "20.00" y7 F))
    (text . "\tab{42} ")
    (bcStrings (10 "5.74" z7 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "3.45" x8 F))
    (text . "\tab{22} ")
    (bcStrings (10 "12.78" y8 F))
    (text . "\tab{42} ")
    (bcStrings (10 "41.24" z8 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "14.26" x9 F))
    (text . "\tab{22} ")
    (bcStrings (10 "17.87" y9 F))
    (text . "\tab{42} ")
    (bcStrings (10 "10.74" z9 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "17.43" x10 F))
    (text . "\tab{22} ")
    (bcStrings (10 "3.46" y10 F))
    (text . "\tab{42} ")
    (bcStrings (10 "18.60" z10 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "22.80" x11 F))
    (text . "\tab{22} ")
    (bcStrings (10 "12.39" y11 F))
    (text . "\tab{42} ")
    (bcStrings (10 "5.47" z11 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "7.58" x12 F))
    (text . "\tab{22} ")
    (bcStrings (10 "1.98" y12 F))
    (text . "\tab{42} ")
    (bcStrings (10 "29.87" z12 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "25.00" x13 F))
    (text . "\tab{22} ")
    (bcStrings (10 "11.87" y13 F))
    (text . "\tab{42} ")
    (bcStrings (10 "4.40" z13 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "0.00" x14 F))
    (text . "\tab{22} ")
    (bcStrings (10 "0.00" y14 F))
    (text . "\tab{42} ")
    (bcStrings (10 "58.20" z14 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "9.66" x15 F))
    (text . "\tab{22} ")
    (bcStrings (10 "20.00" y15 F))
    (text . "\tab{42} ")
    (bcStrings (10 "4.73" z15 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "5.22" x16 F))
    (text . "\tab{22} ")
    (bcStrings (10 "14.66" y16 F))
    (text . "\tab{42} ")
    (bcStrings (10 "40.36" z16 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "17.25" x17 F))
    (text . "\tab{22} ")
    (bcStrings (10 "19.57" y17 F))
    (text . "\tab{42} ")
    (bcStrings (10 "6.43" z17 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "25.00" x18 F))
    (text . "\tab{22} ")
    (bcStrings (10 "3.87" y18 F))
    (text . "\tab{42} ")
    (bcStrings (10 "8.74" z18 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "12.13" x19 F))
    (text . "\tab{22} ")
    (bcStrings (10 "10.79" y19 F))
    (text . "\tab{42} ")
    (bcStrings (10 "13.71" z19 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "22.23" x20 F))
    (text . "\tab{22} ")
    (bcStrings (10 "6.21" y20 F))
    (text . "\tab{42} ")
    (bcStrings (10 "10.25" z20 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "11.52" x21 F))
    (text . "\tab{22} ")
    (bcStrings (10 "8.53" y21 F))
    (text . "\tab{42} ")
    (bcStrings (10 "15.74" z21 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "15.20" x22 F))
    (text . "\tab{22} ")
    (bcStrings (10 "0.0" y22 F))
    (text . "\tab{42} ")
    (bcStrings (10 "21.60" z22 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "7.54" x23 F))
    (text . "\tab{22} ")
    (bcStrings (10 "10.69" y23 F))
    (text . "\tab{42} ")
    (bcStrings (10 "19.31" z23 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "17.32" x24 F))
    (text . "\tab{22} ")
    (bcStrings (10 "13.78" y24 F))
    (text . "\tab{42} ")
    (bcStrings (10 "12.11" z24 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "2.14" x25 F))
    (text . "\tab{22} ")
    (bcStrings (10 "15.03" y25 F))
    (text . "\tab{42} ")
    (bcStrings (10 "53.10" z25 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "0.51" x26 F))
    (text . "\tab{22} ")
    (bcStrings (10 "8.37" y26 F))
    (text . "\tab{42} ")
    (bcStrings (10 "49.43" z26 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "22.69" x27 F))
    (text . "\tab{22} ")
    (bcStrings (10 "19.63" y27 F))
    (text . "\tab{42} ")
    (bcStrings (10 "3.25" z27 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "5.47" x28 F))
    (text . "\tab{22} ")
    (bcStrings (10 "17.13" y28 F))
    (text . "\tab{42} ")
    (bcStrings (10 "28.63" z28 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "21.67" x29 F))
    (text . "\tab{22} ")
    (bcStrings (10 "14.36" y29 F))
    (text . "\tab{42} ")
    (bcStrings (10 "5.52" z29 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "3.31" x30 F))
    (text . "\tab{22} ")
    (bcStrings (10 "0.33" y30 F))
    (text . "\tab{42} ")
    (bcStrings (10 "44.08" z30 F))
    (text . "\blankline"))
  htMakeDoneButton('"Continue",'e01safGen)      
  htpSetProperty(page,'m,m)
  htpSetProperty(page,'ifail,ifail)
  htpSetProperty(page,'inputArea, htpInputAreaAlist htPage)
  htShowPage()

e01safGen htPage ==
  m := htpProperty(htPage,'m)
  ifail := htpProperty(htPage,'ifail)
  alist := htpInputAreaAlist htPage
  y := alist
  while y repeat
    right := STRCONC ((first y).1," ")
    y := rest y    
    mid :=  STRCONC ((first y).1," ")
    y := rest y
    left :=  STRCONC ((first y).1," ")
    y := rest y
    xlist := [left,:xlist]
    flist := [mid,:flist]
    dlist := [right,:dlist]
  xstring := bcwords2liststring xlist
  fstring := bcwords2liststring flist
  dstring := bcwords2liststring dlist
  prefix := STRCONC('"e01saf(",STRINGIMAGE m,",[",xstring,"],[",fstring,"],[")
  prefix := STRCONC(prefix,dstring,"],",STRINGIMAGE ifail,")")
  linkGen prefix

e01sef() ==
  htInitPage('"E01SEF - Interpolating functions, modified Shepard's method, two variables", nil)
  htMakePage '(
    (domainConditions 
       (isDomain PI (PositiveInteger)))
    (text . "\windowlink{Manual Page}{manpageXXe01sef} for this routine ")
    (text . "\newline ")
    (text . "\lispwindowlink{Browser operation page}{(|oPageFrom| '|e01sef| '|NagInterpolationPackage|)} for this routine")
    (text . "\newline \horizontalline ")
    (text . "Determines a \htbitmap{c1} piecewise polynomial ")
    (text . "surface F(x,y) interpolating the set of scattered points ")
    (text . "(\htbitmap{xr}, \htbitmap{yr}, \htbitmap{fqr}), ")
    (text . "for r = 1,2,...,m, using a modified Shepard method. ")
    (text . "The interpolant can be evaluated using E01SFF. ")
    (text . "\blankline ")
    (text . "\newline \menuitemstyle{}\tab{2} \newline ")
    (text . "Number of data points {\em m} \htbitmap{great=} 3:")
    (text . "\newline \tab{2} ")
    (bcStrings (6 30 m PI))
    (text . "\blankline ")
    (text . "Note: RNW, RNQ, NW, NQ set to zero for default value. ")
    (text . "On exit, they contain the value actually used. ")
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{}\tab{2}")
    (text . "\newline {\em RNW} weight locality radius: ")
    (text . "\tab{32} \menuitemstyle{}\tab{34}")
    (text . "{\em RNQ} point locality radius:")
    (text . "\newline\tab{2} ")
    (bcStrings (6 "0.0" rnw F))
    (text . "\tab{34} ")
    (bcStrings (6 "0.0" rnq F))
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{}\tab{2} \newline")
    (text . "{\em NW} average number of points within RNW of each point: ")
    (text . "\newline \tab{2} ")
    (bcStrings (6 0 nw I))
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{}\tab{2} \newline")
    (text . "{\em NQ} average number of points within RNQ of each point: ")
    (text . "\newline \tab{2} ")
    (bcStrings (6 0  nq I))
    (text . "\blankline")
    (text . "\newline")
    (text . "\menuitemstyle{} \tab{2} ")
    (text . "\newline \tab{2} ")
    (text . "Ifail value: ")
    (radioButtons ifail
        ("" " -1, Print error messages" minusOne)
        ("" "  1, Suppress error messages" one)))
  htMakeDoneButton('"Continue", 'e01sefSolve)
  htShowPage()

e01sefSolve htPage ==
  m :=
    $bcParseOnly => PARSE_-INTEGER htpLabelInputString(htPage, 'm)
    objValUnwrap htpLabelSpadValue(htPage, 'm)
  nw :=
    $bcParseOnly => PARSE_-INTEGER htpLabelInputString(htPage, 'nw)
    objValUnwrap htpLabelSpadValue(htPage, 'nw)
  nq :=
    $bcParseOnly => PARSE_-INTEGER htpLabelInputString(htPage, 'nq)
    objValUnwrap htpLabelSpadValue(htPage, 'nq)
  rnq := htpLabelInputString(htPage,'rnq)
  rnw := htpLabelInputString(htPage,'rnw)
  error := htpButtonValue(htPage,'ifail)
  ifail :=
    error = 'one => '1
    '-1
  m = '30 => e01sefDefaultSolve(htPage,rnq,rnw,nq,nw,ifail)
  labelList :=
    "append"/[f(i) for i in 1..m] where f(i) ==
      prefix := ('"\newline \tab{2} ")
      middle := ('"\tab{22} ")
      post   := ('" \tab{42} ")
      xnam := INTERN STRCONC ('"x",STRINGIMAGE i)
      ynam := INTERN STRCONC ('"y",STRINGIMAGE i)
      znam := INTERN STRCONC ('"z",STRINGIMAGE i)
      num := INTERN STRCONC (STRINGIMAGE (QUOTIENT(i,10)),".",STRINGIMAGE (REM(i,10)))
      [['text,:prefix],['bcStrings,[10, num, xnam, 'F]], 
       ['text,:middle],['bcStrings,[10, 0.0, ynam, 'F]],
        ['text,:post],['bcStrings,[10, 0.0, znam, 'F]]]
  equationPart := [
     '(domainConditions 
        (isDomain P (Polynomial $EmptyMode))
         (isDomain S (String))
          (isDomain F (Float))
           (isDomain PI (PositiveInteger))),
            :labelList]
  page := htInitPage("E01SEF - Interpolating functions, modified Shepard's method, two variables",htpPropertyList htPage)
  htSay '"\menuitemstyle{}\tab{2} Values of \space{1} "
  htSay '"\htbitmap{xr}: \tab{20} \menuitemstyle{}\tab{22} "
  htSay '"Values of \space{1} \htbitmap{fr}: \tab{40}"
  htSay '"\menuitemstyle{}\tab{42} Values of \space{1} "
  htSay '"\htbitmap{dr}: "
  htMakePage equationPart
  htSay '"\blankline "
  htMakeDoneButton('"Continue",'e01sefGen)
  htpSetProperty(page,'m,m)
  htpSetProperty(page,'rnq,rnq)
  htpSetProperty(page,'rnw,rnw)
  htpSetProperty(page,'nq,nq)
  htpSetProperty(page,'nw,nw)
  htpSetProperty(page,'ifail,ifail)
  htpSetProperty(page,'inputArea, htpInputAreaAlist htPage)
  htShowPage()


e01sefDefaultSolve  (htPage,rnq,rnw,nq,nw,ifail) ==
  m := '30
  page := htInitPage('"E01SEF - Interpolating functions, modified Shepard's method, two variables",htpPropertyList htPage)
  htMakePage '(
    (domainConditions 
       (isDomain F (Float)))
    (text . "\menuitemstyle{}\tab{2} Values of \space{1} ")
    (text . "\htbitmap{xr}: \tab{20} \menuitemstyle{}\tab{22} ")
    (text . "Values of \space{1} \htbitmap{fr}: \tab{40} ")
    (text . "\menuitemstyle{}\tab{42} Values of \space{1} ")
    (text . "\htbitmap{dr}: ")
    (text . "\newline \tab{2}")
    (bcStrings (10 "11.16" x1 F))
    (text . "\tab{22} ")
    (bcStrings (10 "1.24" y1 F))
    (text . "\tab{42} ")
    (bcStrings (10 "22.15" z1 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "12.85" x2 F))
    (text . "\tab{22} ")
    (bcStrings (10 "3.06" y2 F))
    (text . "\tab{42} ")
    (bcStrings (10 "22.11" z2 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "19.85" x3 F))
    (text . "\tab{22} ")
    (bcStrings (10 "10.72" y3 F))
    (text . "\tab{42} ")
    (bcStrings (10 "7.97" z3 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "19.72" x4 F))
    (text . "\tab{22} ")
    (bcStrings (10 "1.39" y4 F))
    (text . "\tab{42} ")
    (bcStrings (10 "16.83" z4 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "15.91" x5 F))
    (text . "\tab{22} ")
    (bcStrings (10 "7.74" y5 F))
    (text . "\tab{42} ")
    (bcStrings (10 "15.30" z5 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "0.00" x6 F))
    (text . "\tab{22} ")
    (bcStrings (10 "20.00" y6 F))
    (text . "\tab{42} ")
    (bcStrings (10 "34.60" z6 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "20.87" x7 F))
    (text . "\tab{22} ")
    (bcStrings (10 "20.00" y7 F))
    (text . "\tab{42} ")
    (bcStrings (10 "5.74" z7 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "3.45" x8 F))
    (text . "\tab{22} ")
    (bcStrings (10 "12.78" y8 F))
    (text . "\tab{42} ")
    (bcStrings (10 "41.24" z8 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "14.26" x9 F))
    (text . "\tab{22} ")
    (bcStrings (10 "17.87" y9 F))
    (text . "\tab{42} ")
    (bcStrings (10 "10.74" z9 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "17.43" x10 F))
    (text . "\tab{22} ")
    (bcStrings (10 "3.46" y10 F))
    (text . "\tab{42} ")
    (bcStrings (10 "18.60" z10 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "22.80" x11 F))
    (text . "\tab{22} ")
    (bcStrings (10 "12.39" y11 F))
    (text . "\tab{42} ")
    (bcStrings (10 "5.47" z11 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "7.58" x12 F))
    (text . "\tab{22} ")
    (bcStrings (10 "1.98" y12 F))
    (text . "\tab{42} ")
    (bcStrings (10 "29.87" z12 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "25.00" x13 F))
    (text . "\tab{22} ")
    (bcStrings (10 "11.87" y13 F))
    (text . "\tab{42} ")
    (bcStrings (10 "4.40" z13 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "0.00" x14 F))
    (text . "\tab{22} ")
    (bcStrings (10 "0.00" y14 F))
    (text . "\tab{42} ")
    (bcStrings (10 "58.20" z14 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "9.66" x15 F))
    (text . "\tab{22} ")
    (bcStrings (10 "20.00" y15 F))
    (text . "\tab{42} ")
    (bcStrings (10 "4.73" z15 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "5.22" x16 F))
    (text . "\tab{22} ")
    (bcStrings (10 "14.66" y16 F))
    (text . "\tab{42} ")
    (bcStrings (10 "40.36" z16 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "17.25" x17 F))
    (text . "\tab{22} ")
    (bcStrings (10 "19.57" y17 F))
    (text . "\tab{42} ")
    (bcStrings (10 "6.43" z17 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "25.00" x18 F))
    (text . "\tab{22} ")
    (bcStrings (10 "3.87" y18 F))
    (text . "\tab{42} ")
    (bcStrings (10 "8.74" z18 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "12.13" x19 F))
    (text . "\tab{22} ")
    (bcStrings (10 "10.79" y19 F))
    (text . "\tab{42} ")
    (bcStrings (10 "13.71" z19 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "22.23" x20 F))
    (text . "\tab{22} ")
    (bcStrings (10 "6.21" y20 F))
    (text . "\tab{42} ")
    (bcStrings (10 "10.25" z20 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "11.52" x21 F))
    (text . "\tab{22} ")
    (bcStrings (10 "8.53" y21 F))
    (text . "\tab{42} ")
    (bcStrings (10 "15.74" z21 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "15.20" x22 F))
    (text . "\tab{22} ")
    (bcStrings (10 "0.0" y22 F))
    (text . "\tab{42} ")
    (bcStrings (10 "21.60" z22 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "7.54" x23 F))
    (text . "\tab{22} ")
    (bcStrings (10 "10.69" y23 F))
    (text . "\tab{42} ")
    (bcStrings (10 "19.31" z23 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "17.32" x24 F))
    (text . "\tab{22} ")
    (bcStrings (10 "13.78" y24 F))
    (text . "\tab{42} ")
    (bcStrings (10 "12.11" z24 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "2.14" x25 F))
    (text . "\tab{22} ")
    (bcStrings (10 "15.03" y25 F))
    (text . "\tab{42} ")
    (bcStrings (10 "53.10" z25 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "0.51" x26 F))
    (text . "\tab{22} ")
    (bcStrings (10 "8.37" y26 F))
    (text . "\tab{42} ")
    (bcStrings (10 "49.43" z26 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "22.69" x27 F))
    (text . "\tab{22} ")
    (bcStrings (10 "19.63" y27 F))
    (text . "\tab{42} ")
    (bcStrings (10 "3.25" z27 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "5.47" x28 F))
    (text . "\tab{22} ")
    (bcStrings (10 "17.13" y28 F))
    (text . "\tab{42} ")
    (bcStrings (10 "28.63" z28 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "21.67" x29 F))
    (text . "\tab{22} ")
    (bcStrings (10 "14.36" y29 F))
    (text . "\tab{42} ")
    (bcStrings (10 "5.52" z29 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "3.31" x30 F))
    (text . "\tab{22} ")
    (bcStrings (10 "0.33" y30 F))
    (text . "\tab{42} ")
    (bcStrings (10 "44.08" z30 F))
    (text . "\blankline"))
  htMakeDoneButton('"Continue",'e01sefGen)      
  htpSetProperty(page,'m,m)
  htpSetProperty(page,'rnq,rnq)
  htpSetProperty(page,'rnw,rnw)
  htpSetProperty(page,'nq,nq)
  htpSetProperty(page,'nw,nw)
  htpSetProperty(page,'ifail,ifail)
  htpSetProperty(page,'inputArea, htpInputAreaAlist htPage)
  htShowPage()

e01sefGen htPage ==
  m := htpProperty(htPage,'m)
  rnw := htpProperty(htPage,'rnw)
  rnq := htpProperty(htPage,'rnq)
  nw := htpProperty(htPage,'nw)
  nq := htpProperty(htPage,'nq)
  ifail := htpProperty(htPage,'ifail)
  alist := htpInputAreaAlist htPage
  y := alist
  while y repeat
    right := STRCONC ((first y).1," ")
    y := rest y    
    mid :=  STRCONC ((first y).1," ")
    y := rest y
    left :=  STRCONC ((first y).1," ")
    y := rest y
    xlist := [left,:xlist]
    flist := [mid,:flist]
    dlist := [right,:dlist]
  xstring := bcwords2liststring xlist
  fstring := bcwords2liststring flist
  dstring := bcwords2liststring dlist
  prefix := STRCONC('"e01sef(",STRINGIMAGE m,",[",xstring,"],[",fstring,"],[")
  prefix := STRCONC(prefix,dstring,"],",STRINGIMAGE nw,", ",STRINGIMAGE nq)
  prefix := STRCONC(prefix,", ",rnw,", ",rnq,", ",STRINGIMAGE ifail,")")
  linkGen prefix
