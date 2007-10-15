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

c02aff() ==
  htInitPage('"C02AFF - All Zeros of a Complex Polynomial",nil)
  htMakePage '(
    (domainConditions 
       (isDomain PI (PositiveInteger)))
    (text . "\windowlink{Manual Page}{manpageXXc02aff} for this routine ")
    (text . "\newline ")
    (text . "\lispwindowlink{Browser operation page}{(|oPageFrom| '|c02aff| '|NagPolynomialRootsPackage|)} for this routine")
    (text . "\newline \horizontalline ")
    (text . "Finds all the roots of the complex polynomial equation ")
    (text . "\htbitmap{c02aff}, using a variant of Laguerre's method. ")
    (text . "\blankline ")
    (text . "\newline \menuitemstyle{} ")
    (text . "\tab{2} Enter the degree {\em n} of the polynomial:")
    (text . "\newline\tab{2} ")
    (bcStrings (5 5 n PI))
    (text . "\blankline")
    (text . "\newline")
    (text . "\newline \menuitemstyle{} \tab{2} Scale value:")
    (radioButtons scale
        ("" "  True" true)
        ("" "  False" false))
    (text . "\blankline ")
    (text . "\newline \menuitemstyle{} \tab{2} Ifail value:")
    (radioButtons ifail
        ("" " -1, Print error messages" minusOne)
        ("" "  1, Suppress error messages" one)))
  htMakeDoneButton('"Continue", 'c02affSolve)
  htShowPage()

c02affSolve htPage ==
  n :=
    $bcParseOnly => PARSE_-INTEGER htpLabelInputString(htPage, 'n)
    objValUnwrap htpLabelSpadValue(htPage, 'n)
  logical := htpButtonValue(htPage,'scale)
  scale := 
    logical = 'true => '"true"
    '"false"
  error := htpButtonValue(htPage,'ifail)
  ifail :=
    error = 'one => '1
    '-1
  n = '5 => c02affDefaultSolve(htPage,scale,ifail)
  labelList :=
    "append"/[f(i,n) for i in 1..(n+1)] where f(i,n) ==
      prefix := ('"\newline z**")
      prefix := STRCONC(prefix,STRINGIMAGE (n-i+1),'"\space{1}") 
      post   := ('"\tab{30} ")
      post   := STRCONC(post,'"\space{1}")
      rnam := INTERN STRCONC ('"r",STRINGIMAGE i)
      inam := INTERN STRCONC ('"i",STRINGIMAGE i)
      [['text,:prefix],['bcStrings,[10, 0.0, rnam, 'P]], 
       ['text,:post],['bcStrings,[10, 0.0, inam, 'P]]]
  equationPart := [
     '(domainConditions 
        (isDomain P (Polynomial $EmptyMode))
         (isDomain S (String))
          (isDomain PI (PositiveInteger))),
            :labelList]
  page := htInitPage("C02AFF - All Zeros of a Complex Polynomial", htpPropertyList htPage)
  htSay '"\menuitemstyle{} \tab{2} Enter the coefficients of the polynomial: "
  htSay '"\blankline "
  htSay '"Real parts \tab{30} Imaginary parts "
  htSay '"\newline "
  htMakePage equationPart
  htMakeDoneButton('"Continue",'c02affGen)
  htpSetProperty(page,'n,n)
  htpSetProperty(page,'scale,scale)
  htpSetProperty(page,'ifail,ifail)
  htpSetProperty(page,'inputArea, htpInputAreaAlist htPage)
  htShowPage()

c02affDefaultSolve (htPage, scale, ifail) ==
  n := '5
  page := htInitPage('"C02AFF - All Zeros of a Complex Polynomial",nil)
  htMakePage '(
    (domainConditions 
       (isDomain F (Float)))
    (text . "\newline \menuitemstyle{} \tab{2} ")
    (text . "Enter the coefficients of the polynomial: ")
    (text . "\blankline ")
    (text . "Real parts \tab{30} Imaginary parts ")
    (text . "\newline z**5 \space{1} ")
    (bcStrings (10 "5.0" r1 F))
    (text . "\tab{30} ")
    (text . "\space{1} ")
    (bcStrings (10 "6.0" i1 F))
    (text . "\newline ")
    (text . "z**4 \space{1} ")
    (bcStrings (10 "30.0" r2 F))
    (text . "\tab{30} ")
    (text . "\space{1} ")
    (bcStrings (10 "20.0" i2 F))
    (text . "\newline ")
    (text . "z**3 \space{1} ")
    (bcStrings (10 "-0.2" r3 F))
    (text . "\tab{30} ")
    (text . "\space{1} ")
    (bcStrings (10 "-6.0" i3 F))
    (text . "\newline ")
    (text . "z**2 \space{1} ")
    (bcStrings (10 "50.0" r4 F))
    (text . "\tab{30} ")
    (text . "\space{1} ")
    (bcStrings (10 "100000.0" i4 F))
    (text . "\newline ")
    (text . "z**1 \space{1} ")
    (bcStrings (10 "-2.0" r5 F))
    (text . "\tab{30} ")
    (text . "\space{1} ")
    (bcStrings (10 "40.0" i5 F))
    (text . "\newline ")
    (text . "z**0 \space{1} ")
    (bcStrings (10 "10.0" r6 F))
    (text . "\tab{30} ")
    (text . "\space{1} ")
    (bcStrings (10 "1.0" i6 F))
    (text . "\newline ")
    (text . "\blankline"))
  htMakeDoneButton('"Continue",'c02affGen)      
  htpSetProperty(page,'n,n)
  htpSetProperty(page,'scale,scale)
  htpSetProperty(page,'ifail,ifail)
  htpSetProperty(page,'inputArea, htpInputAreaAlist htPage)
  htShowPage()


c02affGen htPage ==
  n := htpProperty(htPage,'n)
  scale := htpProperty(htPage,'scale)
  ifail := htpProperty(htPage,'ifail)
  alist := htpInputAreaAlist htPage
  y := alist
  while y repeat
    right := STRCONC((first y).1," ")
    y := rest y
    left := STRCONC((first y).1," ")
    y := rest y
    reallist := [left,:reallist]
    imaglist := [right,:imaglist]
  realstring := bcwords2liststring reallist
  imagstring := bcwords2liststring imaglist
  linkGen STRCONC ('"c02aff([",realstring,",",imagstring,"],",STRINGIMAGE n,",",scale,",",STRINGIMAGE ifail,")")

c02agf() ==
  htInitPage('"C02AGF - All Zeros of a Real Polynomial",nil)
  htMakePage '(
    (domainConditions 
       (isDomain PI (PositiveInteger)))
    (text . "\windowlink{Manual Page}{manpageXXc02agf} for this routine ")
    (text . "\newline ")
    (text . "\lispwindowlink{Browser operation page}{(|oPageFrom| '|c02agf| '|NagPolynomialRootsPackage|)} for this routine")
    (text . "\newline \horizontalline ")
    (text . "Finds all the roots of the real polynomial equation ")
    (text . "\htbitmap{c02aff}, using a variant of Laguerre's method. ")
    (text . "\blankline ")
    (text . "\newline \menuitemstyle{} ")
    (text . "\tab{2} Enter the degree {\em n} of the polynomial:")
    (text . "\newline\tab{2} ")
    (bcStrings (5 5 n PI))
    (text . "\blankline")
    (text . "\newline")
    (text . "\newline \menuitemstyle{} \tab{2} Scale value:")
    (radioButtons scale
        ("" "  True" true)
        ("" "  False" false))
    (text . "\blankline ")
    (text . "\newline \menuitemstyle{} \tab{2} Ifail value:")
    (radioButtons ifail
        ("" " -1, Print error messages" minusOne)
        ("" "  1, Suppress error messages" one)))
  htMakeDoneButton('"Continue", 'c02agfSolve)
  htShowPage()

c02agfSolve htPage ==
  n :=
    $bcParseOnly => PARSE_-INTEGER htpLabelInputString(htPage, 'n)
    objValUnwrap htpLabelSpadValue(htPage, 'n)
  logical := htpButtonValue(htPage,'scale)
  scale := 
    logical = 'true => '"true"
    '"false"
  error := htpButtonValue(htPage,'ifail)
  ifail :=
    error = 'one => '1
    '-1
  n = '5 => c02agfDefaultSolve(htPage,scale,ifail)
  labelList :=
    "append"/[f(i,n) for i in 1..(n+1)] where f(i,n) ==
      prefix := ('"\newline z**")
      prefix := STRCONC(prefix,STRINGIMAGE (n-i+1),'"\space{1}") 
      rnam := INTERN STRCONC ('"r",STRINGIMAGE i)
      [['text,:prefix],['bcStrings,[10, 0.0, rnam, 'P]]]
  equationPart := [ 
     '(domainConditions 
        (isDomain P (Polynomial $EmptyMode))
         (isDomain S (String))
          (isDomain PI (PositiveInteger))),
            :labelList]
  page := htInitPage("C02AGF - All Zeros of a Real Polynomial", htpPropertyList htPage)
  htSay '"\menuitemstyle{} \tab{2} "
  htSay '"Enter the coefficients of the polynomial: "
  htSay '"\newline "
  htMakePage equationPart
  htMakeDoneButton('"Continue",'c02agfGen)
  htpSetProperty(page,'n,n)
  htpSetProperty(page,'scale,scale)
  htpSetProperty(page,'ifail,ifail)
  htpSetProperty(page,'inputArea, htpInputAreaAlist htPage)
  htShowPage()  


c02agfDefaultSolve (htPage, scale, ifail) ==
  n := '5
  page := htInitPage('"C02AGF - All Zeros of a Real Polynomial",nil)
  htMakePage '(
    (domainConditions 
       (isDomain F (Float)))
    (text . "\newline \menuitemstyle{} \tab{2} ")
    (text . "Enter the coefficients of the polynomial: ")
    (text . "\newline ")
    (text . "z**5 \space{1} ")
    (bcStrings (10 "1.0" r1 F))
    (text . "\newline ")
    (text . "z**4 \space{1} ")
    (bcStrings (10 "2.0" r2 F))
    (text . "\newline ")
    (text . "z**3 \space{1} ")
    (bcStrings (10 "3.0" r3 F))
    (text . "\newline ")
    (text . "z**2 \space{1} ")
    (bcStrings (10 "4.0" r4 F))
    (text . "\newline ")
    (text . "z**1 \space{1} ")
    (bcStrings (10 "5.0" r5 F))
    (text . "\newline ")
    (text . "z**0 \space{1} ")
    (bcStrings (10 "6.0" r6 F))
    (text . "\newline ")
    (text . "\blankline"))
  htMakeDoneButton('"Continue",'c02agfGen)      
  htpSetProperty(page,'n,n)
  htpSetProperty(page,'scale,scale)
  htpSetProperty(page,'ifail,ifail)
  htpSetProperty(page,'inputArea, htpInputAreaAlist htPage)
  htShowPage()


c02agfGen htPage ==
  n := htpProperty(htPage,'n)
  scale := htpProperty(htPage,'scale)
  ifail := htpProperty(htPage,'ifail)
  alist := htpInputAreaAlist htPage
  y := alist
  while y repeat
    left := STRCONC((first y).1," ")
    y := rest y
    reallist := [left,:reallist]
  realstring := bcwords2liststring reallist
  linkGen STRCONC ('"c02agf([",realstring,"],",STRINGIMAGE n,",",scale,",",STRINGIMAGE ifail,")")


