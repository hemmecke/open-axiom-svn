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

c05adf() ==
  htInitPage('"C05ADF - Zero of continuous function in given interval, Bus and Dekker algorithm",nil)
  htMakePage '(
    (domainConditions 
       (isDomain EM $EmptyMode)
       (isDomain F  (Float)))
    (text . "\windowlink{Manual Page}{manpageXXc05adf} for this routine ")
    (text . "\newline ")
    (text . "\lispwindowlink{Browser operation page}{(|oPageFrom| '|c05adf| '|NagRootFindingPackage|)} for this routine")
    (text . "\newline \horizontalline ")
    (text . "\newline ")
    (text . "C05ADF locates a zero of a continuous function in a ")
    (text . "interval by a combination of the methods of linear ")
    (text . "interpolation, extrapolation and bisection. ")
    (text . "\blankline")
    (text . "\newline ")
    (text . "\menuitemstyle{}\tab{2} ")
    (text . "Enter the function whose zero is to be determined ")
    (text . "as a function of X, {\it f}: ")
    (text . "\newline\tab{2} ")
    (bcStrings (55 "exp(-X)-X" expression EM))
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{}\tab{2}")
    (text . "\newline Lower bound of the interval {\it a}:")
    (text . "\tab{32} \menuitemstyle{}\tab{34}")
    (text . "Upper bound {\it b}: ")
    (text . "\newline\tab{2} ")
    (bcStrings (10 "0.0" a F))
    (text . "\tab{34} ")
    (bcStrings (10 "1.0" b F))
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{}\tab{2}")
    (text . "\newline Absolute tolerance {\it eps}:")
    (text . "\tab{32} \menuitemstyle{}\tab{34}")
    (text . "Value tolerance {\it eta}:")
    (text . "\newline\tab{2} ")
    (bcStrings (10 "1.0e-5" eps F))
    (text . "\tab{34} ")
    (bcStrings (10 "0.0" eta F))
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{}\tab{2}")
    (text . "\newline Ifail value:")
    (radioButtons ifail
        ("" " -1, Print error messages" minusOne)
        ("" "  1, Suppress error messages" one)))
  htMakeDoneButton('"Continue", 'c05adfGen)
  htShowPage()

c05adfGen htPage ==
  a     := htpLabelInputString(htPage,'a)
  b     := htpLabelInputString(htPage,'b)
  eps   := htpLabelInputString(htPage,'eps)
  eta   := htpLabelInputString(htPage,'eta)  
  temp  := READ_-FROM_-STRING(eps) 
  error := htpButtonValue(htPage,'ifail)
  ifail :=
    error = 'one => 1
    -1
  temp1 :=
    temp >  0.0 => eps
    '"1.0e-5"
  expression := htpLabelInputString(htPage, 'expression)
  prefix := STRCONC('"c05adf(",a,",",b,",",temp1,",",eta,",",STRINGIMAGE ifail)
  linkGen STRCONC (prefix,",(",expression,")::ASP1(F))")


c05nbf() ==
  htInitPage('"C05NBF - Solution of system of nonlinear equations using function values only",nil)
  htMakePage '(
    (domainConditions 
       (isDomain PI (PositiveInteger)))
    (text . "\windowlink{Manual Page}{manpageXXc05nbf} for this routine ")
    (text . "\newline ")
    (text . "\lispwindowlink{Browser operation page}{(|oPageFrom| '|c05nbf| '|NagRootFindingPackage|)} for this routine")
    (text . "\newline \horizontalline ")
    (text . "\newline ")
    (text . "C05NBF finds a solution of a system of nonlinear equations ")
    (text . "by a modification of the Powell hybrid method. ")
    (text . "\blankline ")
    (text . "\menuitemstyle{}\tab{2}")
    (text . "Enter the number of equations in the system {\it n}:")
    (text . "\newline\tab{2} ")
    (bcStrings (5 9 n PI))
    (text . "\newline ")
    (text . "\blankline ")
    (text . "\menuitemstyle{}\tab{2}")
    (text . "Accuracy required {\it xtol}:")
    (text . "\newline\tab{2} ")
    -- should be sqrt(machine precision)
    (bcStrings (10 "1.0e-9" xtol F))
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{}\tab{2}")
    (text . "Ifail value:")
    (radioButtons ifail
        ("" " -1, Print error messages" minusOne)
        ("" "  1, Suppress error messages" one)))
  htMakeDoneButton('"Continue", 'c05nbfSolve)
  htShowPage()

c05nbfSolve htPage ==
  n :=
    $bcParseOnly => PARSE_-INTEGER htpLabelInputString(htPage, 'n)
    objValUnwrap htpLabelSpadValue(htPage, 'n)
  xtol := htpLabelInputString(htPage,'xtol)
  error := htpButtonValue(htPage,'ifail)
  ifail :=
    error = 'one => '1
    '-1
  n = '9 => c05nbfDefaultSolve(htPage,ifail,xtol)
  funcList := 
    "append"/[fa(i) for i in 1..n] where fa(i) ==
      prefix := ('"\newline {\em Function ")
      prefix := STRCONC(prefix,STRINGIMAGE i,'":} \space{1}")
      funct := STRCONC ('"X[",STRINGIMAGE i ,"] + 1")
      nam := INTERN STRCONC ('"n",STRINGIMAGE i)
      [['text,:prefix],['bcStrings,[42, funct, nam, 'EM]]]
  middle := ('"\blankline \menuitemstyle{}\tab{2} Enter initial guess ")
  middle := STRCONC(middle,'"of the solution vector {\it x}: \newline ")
  middle := cons('text,middle)
  vecList := 
    [fb(i) for i in 1..n] where fb(i) ==
      xnam := INTERN STRCONC ('"x",STRINGIMAGE i)
      ['bcStrings,[4, -1.0, xnam, 'F]]
  funcList := [:funcList,middle,:vecList]
  equationPart := [
     '(domainConditions 
        (isDomain EM $EmptyMode)
          (isDomain F (Float))
            (isDomain I (Integer))),
                :funcList]
  page := htInitPage("C05NBF - Solution of system of nonlinear equations using function values only", htpPropertyList htPage)
  htSay '"\menuitemstyle{}\tab{2} "
  htSay '"Enter the functions \htbitmap{fi} below in terms of X[1]...X[n]: "
  htSay '"\newline "
  htMakePage equationPart
  htMakeDoneButton('"Continue",'c05nbfGen)
  htpSetProperty(page,'n,n)
  htpSetProperty(page,'ifail,ifail)
  htpSetProperty(page,'xtol,xtol)
  htpSetProperty(page,'inputArea, htpInputAreaAlist htPage)
  htShowPage()


c05nbfDefaultSolve (htPage,ifail,xtol) ==
  n := '9
  page := htInitPage("C05NBF - Solution of system of nonlinear equations using function values only", nil)
  htMakePage '(
    (domainConditions 
       (isDomain EM $EmptyMode)
       (isDomain F (Float))
       (isDomain I (Integer)))
    (text . "\newline ")
    (text . "\menuitemstyle{}\tab{2}")
    (text . "Enter the functions \htbitmap{fi} below in terms of X[1]...X[n]; ")
    (text . " \newline ")
    (text . "\newline {\em Function 1:} \space{1}")
    (bcStrings (42 "3*X[1] - 2*X[1]**2 - 2*X[2] + 1" n1 EM))
    (text . "\newline {\em Function 2:} \space{1}")
    (bcStrings (42 "-X[1] + 3*X[2] - 2*X[2]**2 - 2*X[3] + 1" n2 EM))
    (text . "\newline {\em Function 3:} \space{1}")
    (bcStrings (42 "-X[2] + 3*X[3] - 2*X[3]**2 - 2*X[4] + 1" n3 EM))
    (text . "\newline {\em Function 4:} \space{1}")
    (bcStrings (42 "-X[3] + 3*X[4] - 2*X[4]**2 - 2*X[5] + 1" n4 EM))
    (text . "\newline {\em Function 5:} \space{1}")
    (bcStrings (42 "-X[4] + 3*X[5] - 2*X[5]**2 - 2*X[6] + 1" n5 EM))
    (text . "\newline {\em Function 6:} \space{1}")
    (bcStrings (42 "-X[5] + 3*X[6] - 2*X[6]**2 - 2*X[7] + 1" n6 EM))
    (text . "\newline {\em Function 7:} \space{1}")
    (bcStrings (42 "-X[6] + 3*X[7] - 2*X[7]**2 - 2*X[8] + 1" n7 EM))
    (text . "\newline {\em Function 8:} \space{1}")
    (bcStrings (42 "-X[7] + 3*X[8] - 2*X[8]**2 - 2*X[9] + 1" n8 EM))
    (text . "\newline {\em Function 9:} \space{1}")
    (bcStrings (42 "-X[8] + 3*X[9] - 2*X[9]**2 + 1" n9 EM))
    (text . "\blankline ")
    (text . "\menuitemstyle{}\tab{2}")
    (text . "Enter initial guess of the solution vector {\it x}: \newline ")
    (bcStrings (4 "-1.0" x1 F))
    (bcStrings (4 "-1.0" x2 F))
    (bcStrings (4 "-1.0" x3 F))
    (bcStrings (4 "-1.0" x4 F))
    (bcStrings (4 "-1.0" x5 F))
    (bcStrings (4 "-1.0" x6 F))
    (bcStrings (4 "-1.0" x7 F))
    (bcStrings (4 "-1.0" x8 F))
    (bcStrings (4 "-1.0" x9 F)))
  htMakeDoneButton('"Continue",'c05nbfGen)
  htpSetProperty(page,'n,n)
  htpSetProperty(page,'ifail,ifail)
  htpSetProperty(page,'xtol,xtol)
  htpSetProperty(page,'inputArea, htpInputAreaAlist htPage)
  htShowPage()

c05nbfGen htPage ==
  n := htpProperty(htPage, 'n)
  ifail := htpProperty(htPage,'ifail)
  xtol := htpProperty(htPage,'xtol)
  alist := htpInputAreaAlist htPage
  y := alist
  i := 1
  while y repeat
    if i < (n+1) then 
      temp1 := STRCONC ((first y).1," ")
      temp1list := [temp1,:temp1list]   
    else
      temp2 := (first y).1
      temp2list := [temp2,:temp2list]   
    y := rest y
    i := i + 1
  string1 := bcwords2liststring temp1list
  string2 := bcwords2liststring temp2list
  lwa := n*(3*n+13)/2
  prefix := STRCONC ("c05nbf(",STRINGIMAGE n,",",STRINGIMAGE lwa,",[",string1,"],")
  middle := STRCONC (xtol,",",STRINGIMAGE ifail,",")
  linkGen STRCONC (prefix,middle,"(",string2,"::Vector Expression(Float))::ASP6(FCN))")

c05pbf() ==
  htInitPage('"C05PBF - Solution of system of nonlinear equations using first derivatives",nil)
  htMakePage '(
    (domainConditions 
       (isDomain PI (PositiveInteger)))
    (text . "\windowlink{Manual Page}{manpageXXc05pbf} for this routine ")
    (text . "\newline ")
    (text . "\lispwindowlink{Browser operation page}{(|oPageFrom| '|c05pbf| '|NagRootFindingPackage|)} for this routine")
    (text . "\newline \horizontalline ")
    (text . "\newline ")
    (text . "C05PBF finds a solution of a system of nonlinear equations ")
    (text . "by a modification of the Powell hybrid method. ")
    (text . "\blankline ")
    (text . "\menuitemstyle{}\tab{2}")
    (text . "Enter the number of equations in the system {\it n}:")
    (text . "\newline\tab{2} ")
    (bcStrings (5 9 n PI))
    (text . "\newline ")
    (text . "\blankline ")
    (text . "\menuitemstyle{}\tab{2}")
    (text . "Accuracy required {\it xtol}:")
    (text . "\newline\tab{2} ")
    -- should be sqrt(machine precision)
    (bcStrings (10 "1.0e-9" xtol F))
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{}\tab{2}")
    (text . "Ifail value:")
    (radioButtons ifail
        ("" " -1, Print error messages" minusOne)
        ("" "  1, Suppress error messages" one)))
  htMakeDoneButton('"Continue", 'c05pbfSolve)
  htShowPage()

c05pbfSolve htPage ==
  n :=
    $bcParseOnly => PARSE_-INTEGER htpLabelInputString(htPage, 'n)
    objValUnwrap htpLabelSpadValue(htPage, 'n)
  xtol := htpLabelInputString(htPage,'xtol)
  error := htpButtonValue(htPage,'ifail)
  ifail :=
    error = 'one => '1
    '-1
  n = '9 => c05pbfDefaultSolve(htPage,ifail,xtol)
  funcList := 
    "append"/[fa(i) for i in 1..n] where fa(i) ==
      prefix := ('"\newline {\em Function ")
      prefix := STRCONC(prefix,STRINGIMAGE i,'":} \space{1}")
      funct := STRCONC ('"X[",STRINGIMAGE i ,"] + 1")
      nam := INTERN STRCONC ('"n",STRINGIMAGE i)
      [['text,:prefix],['bcStrings,[42, funct, nam, 'EM]]]
  middle := ('"\blankline \menuitemstyle{}\tab{2} Enter initial guess ")
  middle := STRCONC(middle,'"of the solution vector {\it x}: \newline ")
  middle := cons('text,middle)
  vecList := 
    [fb(i) for i in 1..n] where fb(i) ==
      xnam := INTERN STRCONC ('"x",STRINGIMAGE i)
      ['bcStrings,[4, -1.0, xnam, 'F]]
  funcList := [:funcList,middle,:vecList]
  equationPart := [
     '(domainConditions 
        (isDomain EM $EmptyMode)
          (isDomain F (Float))
            (isDomain I (Integer))),
                :funcList]
  page := htInitPage('"C05PBF - Solution of system of nonlinear equations using first derivatives",htpPropertyList htPage)
  htSay '"\menuitemstyle{}\tab{2} "
  htSay '"Enter the functions \htbitmap{fi} below in terms of X[1]...X[n]: "
  htSay '"\newline "
  htMakePage equationPart
  htMakeDoneButton('"Continue",'c05pbfGen)
  htpSetProperty(page,'n,n)
  htpSetProperty(page,'ifail,ifail)
  htpSetProperty(page,'xtol,xtol)
  htpSetProperty(page,'inputArea, htpInputAreaAlist htPage)
  htShowPage()


c05pbfDefaultSolve (htPage,ifail,xtol) ==
  n := '9
  page := htInitPage('"C05PBF - Solution of system of nonlinear equations using first derivatives",nil)
  htMakePage '(
    (domainConditions 
       (isDomain EM $EmptyMode)
       (isDomain F (Float))
       (isDomain I (Integer)))
    (text . "\newline ")
    (text . "\menuitemstyle{}\tab{2}")
    (text . "Enter the functions \htbitmap{fi} below in terms of X[1]...X[n]: ")
    (text . "\newline ")
    (text . "\newline {\em Function 1:} \space{1}")
    (bcStrings (42 "3*X[1] - 2*X[1]**2 - 2*X[2] + 1" n1 EM))
    (text . "\newline {\em Function 2:} \space{1}")
    (bcStrings (42 "-X[1] + 3*X[2] - 2*X[2]**2 - 2*X[3] + 1" n2 EM))
    (text . "\newline {\em Function 3:} \space{1}")
    (bcStrings (42 "-X[2] + 3*X[3] - 2*X[3]**2 - 2*X[4] + 1" n3 EM))
    (text . "\newline {\em Function 4:} \space{1}")
    (bcStrings (42 "-X[3] + 3*X[4] - 2*X[4]**2 - 2*X[5] + 1" n4 EM))
    (text . "\newline {\em Function 5:} \space{1}")
    (bcStrings (42 "-X[4] + 3*X[5] - 2*X[5]**2 - 2*X[6] + 1" n5 EM))
    (text . "\newline {\em Function 6:} \space{1}")
    (bcStrings (42 "-X[5] + 3*X[6] - 2*X[6]**2 - 2*X[7] + 1" n6 EM))
    (text . "\newline {\em Function 7:} \space{1}")
    (bcStrings (42 "-X[6] + 3*X[7] - 2*X[7]**2 - 2*X[8] + 1" n7 EM))
    (text . "\newline {\em Function 8:} \space{1}")
    (bcStrings (42 "-X[7] + 3*X[8] - 2*X[8]**2 - 2*X[9] + 1" n8 EM))
    (text . "\newline {\em Function 9:} \space{1}")
    (bcStrings (42 "-X[8] + 3*X[9] - 2*X[9]**2 + 1" n9 EM))
    (text . "\blankline ")
    (text . "\menuitemstyle{}\tab{2}")
    (text . "Enter initial guess of the solution vector {\it x}: \newline ")
    (bcStrings (4 "-1.0" x1 F))
    (bcStrings (4 "-1.0" x2 F))
    (bcStrings (4 "-1.0" x3 F))
    (bcStrings (4 "-1.0" x4 F))
    (bcStrings (4 "-1.0" x5 F))
    (bcStrings (4 "-1.0" x6 F))
    (bcStrings (4 "-1.0" x7 F))
    (bcStrings (4 "-1.0" x8 F))
    (bcStrings (4 "-1.0" x9 F)))
  htMakeDoneButton('"Continue",'c05pbfGen)
  htpSetProperty(page,'n,n)
  htpSetProperty(page,'ifail,ifail)
  htpSetProperty(page,'xtol,xtol)
  htpSetProperty(page,'inputArea, htpInputAreaAlist htPage)
  htShowPage()

c05pbfGen htPage ==
  n := htpProperty(htPage, 'n)
  ifail := htpProperty(htPage,'ifail)
  xtol := htpProperty(htPage,'xtol)
  alist := htpInputAreaAlist htPage
  y := alist
  i := 1
  while y repeat
    if i < (n+1) then 
      temp1 := STRCONC ((first y).1," ")
      temp1list := [temp1,:temp1list]   
    else
      temp2 := (first y).1
      temp2list := [temp2,:temp2list]   
    y := rest y
    i := i + 1
  string1 := bcwords2liststring temp1list
  string2 := bcwords2liststring temp2list
  lwa := n*(n+13)/2
  prefix := STRCONC("c05pbf(",STRINGIMAGE n,",",STRINGIMAGE n)
  middle := STRCONC(",",STRINGIMAGE lwa,",[",string1,"],")
  middle := STRCONC (middle,xtol,",",STRINGIMAGE ifail,",")
  linkGen STRCONC (prefix,middle,"(",string2,"::Vector Expression(Float))::ASP35(FCN))")

