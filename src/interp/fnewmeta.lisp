;; Copyright (c) 1991-2002, The Numerical ALgorithms Group Ltd.
;; All rights reserved.
;; Copyright (C) 2007-2011, Gabriel Dos Reis.
;; All rights reserved.
;;
;; Redistribution and use in source and binary forms, with or without
;; modification, are permitted provided that the following conditions are
;; met:
;;
;;     - Redistributions of source code must retain the above copyright
;;       notice, this list of conditions and the following disclaimer.
;;
;;     - Redistributions in binary form must reproduce the above copyright
;;       notice, this list of conditions and the following disclaimer in
;;       the documentation and/or other materials provided with the
;;       distribution.
;;
;;     - Neither the name of The Numerical ALgorithms Group Ltd. nor the
;;       names of its contributors may be used to endorse or promote products
;;       derived from this software without specific prior written permission.
;;
;; THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
;; IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
;; TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
;; PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER
;; OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
;; EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
;; PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
;; PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
;; LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
;; NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
;; SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

;; % Binding powers stored under the Led and Red properties of an operator
;; % are set up by the file BOTTOMUP.LISP.  The format for a Led property
;; % is <Operator Left-Power Right-Power>, and the same for a Nud, except that
;; % it may also have a fourth component <Special-Handler>. ELEMN attempts to
;; % get the Nth indicator, counting from 1.

(IMPORT-MODULE "parsing")
(IN-PACKAGE "BOOT" )


(DEFPARAMETER |tmptok| NIL)
(DEFPARAMETER TOK NIL)
(DEFPARAMETER |ParseMode| NIL)
(DEFPARAMETER DEFINITION_NAME NIL)
(DEFPARAMETER LABLASOC NIL)

(defun |isTokenDelimiter| () 
       (MEMBER (|currentSymbol|) '(\) END\_UNIT NIL)))

(DEFUN |PARSE-NewExpr| ()
  (OR (AND (MATCH-STRING ")") (ACTION (|processSynonyms|))
           (MUST (|PARSE-Command|)))
      (AND (ACTION (SETQ DEFINITION_NAME (|currentSymbol|)))
           (|PARSE-Statement|)))) 


(DEFUN |PARSE-Command| ()
  (AND (MATCH-ADVANCE-STRING ")") (MUST (|PARSE-SpecialKeyWord|))
       (MUST (|PARSE-SpecialCommand|))
       (|pushReduction| '|PARSE-Command| NIL))) 


(DEFUN |PARSE-SpecialKeyWord| ()
  (AND (|matchCurrentToken| 'IDENTIFIER)
       (ACTION (SETF (|tokenSymbol| (|currentToken|))
                     (|unAbbreviateKeyword| (|currentSymbol|)))))) 


(DEFUN |PARSE-SpecialCommand| ()
  (OR (AND (MATCH-ADVANCE-STRING "show")
           (BANG FIL_TEST
                 (OPTIONAL
                     (OR (MATCH-ADVANCE-STRING "?")
                         (|PARSE-Expression|))))
           (|pushReduction| '|PARSE-SpecialCommand|
               (CONS '|show| (CONS (|popStack1|) NIL)))
           (MUST (|PARSE-CommandTail|)))
      (AND (MEMBER (|currentSymbol|) |$noParseCommands|)
           (ACTION (FUNCALL (|currentSymbol|))))
      (AND (MEMBER (|currentSymbol|) |$tokenCommands|)
           (|PARSE-TokenList|) (MUST (|PARSE-TokenCommandTail|)))
      (AND (STAR REPEATOR (|PARSE-PrimaryOrQM|))
           (MUST (|PARSE-CommandTail|))))) 


(DEFUN |PARSE-TokenList| ()
  (STAR REPEATOR
        (AND (NOT (|isTokenDelimiter|))
             (|pushReduction| '|PARSE-TokenList| (|currentSymbol|))
             (ACTION (|advanceToken|))))) 


(DEFUN |PARSE-TokenCommandTail| ()
  (AND (BANG FIL_TEST (OPTIONAL (STAR REPEATOR (|PARSE-TokenOption|))))
       (|atEndOfLine|)
       (|pushReduction| '|PARSE-TokenCommandTail|
           (CONS (|popStack2|) (APPEND (|popStack1|) NIL)))
       (ACTION (|systemCommand| (|popStack1|))))) 


(DEFUN |PARSE-TokenOption| ()
  (AND (MATCH-ADVANCE-STRING ")") (MUST (|PARSE-TokenList|)))) 


(DEFUN |PARSE-CommandTail| ()
  (AND (BANG FIL_TEST (OPTIONAL (STAR REPEATOR (|PARSE-Option|))))
       (|atEndOfLine|)
       (|pushReduction| '|PARSE-CommandTail|
           (CONS (|popStack2|) (APPEND (|popStack1|) NIL)))
       (ACTION (|systemCommand| (|popStack1|))))) 


(DEFUN |PARSE-PrimaryOrQM| ()
  (OR (AND (MATCH-ADVANCE-STRING "?")
           (|pushReduction| '|PARSE-PrimaryOrQM| '?))
      (|PARSE-Primary|))) 


(DEFUN |PARSE-Option| ()
  (AND (MATCH-ADVANCE-STRING ")")
       (MUST (STAR REPEATOR (|PARSE-PrimaryOrQM|))))) 


(DEFUN |PARSE-Statement| ()
  (AND (|PARSE-Expr| 0)
       (OPTIONAL
           (AND (STAR REPEATOR
                      (AND (MATCH-ADVANCE-STRING ",")
                           (MUST (|PARSE-Expr| 0))))
                (|pushReduction| '|PARSE-Statement|
                    (CONS '|Series|
                          (CONS (|popStack2|)
                                (APPEND (|popStack1|) NIL)))))))) 


(DEFUN |PARSE-InfixWith| ()
  (AND (|PARSE-With|)
       (|pushReduction| '|PARSE-InfixWith|
           (CONS '|Join| (CONS (|popStack2|) (CONS (|popStack1|) NIL)))))) 


(DEFUN |PARSE-With| ()
  (AND (MATCH-ADVANCE-KEYWORD "with") (MUST (|PARSE-Category|))
       (|pushReduction| '|PARSE-With|
           (CONS '|with| (CONS (|popStack1|) NIL))))) 


(DEFUN |PARSE-Category| ()
  (PROG (G1)
    (RETURN
      (OR (AND (MATCH-ADVANCE-KEYWORD "if") (MUST (|PARSE-Expression|))
               (MUST (MATCH-ADVANCE-KEYWORD "then"))
               (MUST (|PARSE-Category|))
               (BANG FIL_TEST
                     (OPTIONAL
                         (AND (MATCH-ADVANCE-KEYWORD "else")
                              (MUST (|PARSE-Category|)))))
               (|pushReduction| '|PARSE-Category|
                   (CONS '|if|
                         (CONS (|popStack3|)
                               (CONS (|popStack2|)
                                     (CONS (|popStack1|) NIL))))))
          (AND (MATCH-ADVANCE-STRING "(") (MUST (|PARSE-Category|))
               (BANG FIL_TEST
                     (OPTIONAL
                         (STAR REPEATOR
                               (AND (MATCH-ADVANCE-STRING ";")
                                    (MUST (|PARSE-Category|))))))
               (MUST (MATCH-ADVANCE-STRING ")"))
               (|pushReduction| '|PARSE-Category|
                   (CONS 'CATEGORY
                         (CONS (|popStack2|)
                               (APPEND (|popStack1|) NIL)))))
          (AND (ACTION (SETQ G1 (|lineNumber| |$spadLine|)))
               (OR (|PARSE-Application|)
		   (|PARSE-OperatorFunctionName|))
               (MUST (OR (AND (MATCH-ADVANCE-STRING ":")
                              (MUST (|PARSE-Expression|))
                              (|pushReduction| '|PARSE-Category|
                                  (CONS '|%Signature|
                                        (CONS (|popStack2|)
                                         (CONS (|popStack1|) NIL))))
                              (ACTION (|recordSignatureDocumentation|
                                       (|nthStack| 1) G1)))
                         (AND (|pushReduction| '|PARSE-Category|
                                  (CONS '|%Attribute|
                                        (CONS (|popStack1|) NIL)))
                              (ACTION (|recordAttributeDocumentation|
                                       (|nthStack| 1) G1)))))))))) 


(DEFUN |PARSE-Expression| ()
  (AND (|PARSE-Expr|
           (|PARSE-rightBindingPowerOf| (|makeSymbolOf| |$priorToken|)
               |ParseMode|))
       (|pushReduction| '|PARSE-Expression| (|popStack1|)))) 


(DEFUN |PARSE-Import| ()
  (AND (MATCH-ADVANCE-KEYWORD "import") 
       (MUST (|PARSE-Expr| 1000))
       (OR (AND (MATCH-ADVANCE-STRING ":")
                (MUST (|PARSE-Expression|))
                (MUST (MATCH-ADVANCE-KEYWORD "from"))
                (MUST (|PARSE-Expr| 1000))
                (|pushReduction| '|PARSE-Import|
		    (CONS '|%SignatureImport|
			  (CONS (|popStack3|)
				(CONS (|popStack2|)
				      (CONS (|popStack1|) NIL))))))
           (AND (BANG FIL_TEST
		      (OPTIONAL
		       (STAR REPEATOR
			     (AND (MATCH-ADVANCE-STRING ",")
				  (MUST (|PARSE-Expr| 1000))))))
		(|pushReduction| '|PARSE-Import|
                    (CONS '|import|
			  (CONS (|popStack2|) (APPEND (|popStack1|) NIL))))))))

;; domain inlining.  Same syntax as import directive; except
;; deliberate restriction on naming one type at a time.
;; -- gdr, 2009-02-28.
(DEFUN |PARSE-Inline| ()
  (AND (MATCH-ADVANCE-KEYWORD "inline")
       (MUST (|PARSE-Expr| 1000))
       (|pushReduction| '|PARSE-Inline|
           (CONS '|%Inline| (CONS (|popStack1|) NIL)))))

;; quantified types.  At the moment, these are used only in
;; pattern-mathing cases.
;; -- gdr, 2009-06-14.
(DEFUN |PARSE-Scheme| ()
  (OR (AND (|PARSE-Quantifier|)
	   (MUST (|PARSE-QuantifiedVariableList|))
	   (MUST (MATCH-ADVANCE-STRING "."))
	   (MUST (|PARSE-Expr| 200))
	   (MUST (|pushReduction| '|PARSE-Forall|
				 (CONS (|popStack3|)
				       (CONS (|popStack2|)
					     (CONS (|popStack1|) NIL))))))
      (|PARSE-Application|)))

(DEFUN |PARSE-Quantifier| ()
  (OR (AND (MATCH-ADVANCE-KEYWORD "forall")
	   (MUST (|pushReduction| '|PARSE-Quantifier| '|%Forall|)))
      (AND (MATCH-ADVANCE-KEYWORD "exist")
	   (MUST (|pushReduction| '|PARSE-Quantifier| '|%Exist|)))))

(DEFUN |PARSE-QuantifiedVariableList| ()
  (AND (MATCH-ADVANCE-STRING "(")
       (MUST (|PARSE-QuantifiedVariable|))
       (OPTIONAL 
	(AND (STAR REPEATOR 
		   (AND (MATCH-ADVANCE-STRING ",")
			(MUST (|PARSE-QuantifiedVariable|))))
	     (|pushReduction| '|PARSE-QuantifiedVariableList|
			     (CONS '|%Sequence|
				   (CONS (|popStack2|) 
					 (APPEND (|popStack1|) NIL))))))
       (MUST (MATCH-ADVANCE-STRING ")"))))

(DEFUN |PARSE-QuantifiedVariable| ()
  (AND (|PARSE-Name|)
       (MUST (MATCH-ADVANCE-STRING ":"))
       (MUST (|PARSE-Application|))
       (MUST (|pushReduction| '|PARSE-QuantifiedVariable|
			     (CONS '|:| 
				   (CONS (|popStack2|)
					 (CONS (|popStack1|) NIL)))))))

(DEFUN |PARSE-Infix| ()
  (AND (|pushReduction| '|PARSE-Infix| (|currentSymbol|))
       (ACTION (|advanceToken|)) (OPTIONAL (|PARSE-TokTail|))
       (MUST (|PARSE-Expression|))
       (|pushReduction| '|PARSE-Infix|
           (CONS (|popStack2|)
                 (CONS (|popStack2|) (CONS (|popStack1|) NIL)))))) 


(DEFUN |PARSE-Prefix| ()
  (AND (|pushReduction| '|PARSE-Prefix| (|currentSymbol|))
       (ACTION (|advanceToken|)) (OPTIONAL (|PARSE-TokTail|))
       (MUST (|PARSE-Expression|))
       (|pushReduction| '|PARSE-Prefix|
           (CONS (|popStack2|) (CONS (|popStack1|) NIL))))) 


(DEFUN |PARSE-Suffix| ()
  (AND (|pushReduction| '|PARSE-Suffix| (|currentSymbol|))
       (ACTION (|advanceToken|)) (OPTIONAL (|PARSE-TokTail|))
       (|pushReduction| '|PARSE-Suffix|
           (CONS (|popStack1|) (CONS (|popStack1|) NIL))))) 


(DEFUN |PARSE-TokTail| ()
  (PROG (G1)
    (RETURN
      (AND (EQ (|currentSymbol|) '$)
           (OR (ALPHA-CHAR-P (|currentChar|))
               (CHAR-EQ (|currentChar|) "$")
               (CHAR-EQ (|currentChar|) "%")
               (CHAR-EQ (|currentChar|) "("))
           (ACTION (SETQ G1 (|copyToken| |$priorToken|)))
           (|PARSE-Qualification|) (ACTION (SETQ |$priorToken| G1)))))) 


(DEFUN |PARSE-Qualification| ()
  (AND (MATCH-ADVANCE-STRING "$") (MUST (|PARSE-Primary1|))
       (|pushReduction| '|PARSE-Qualification|
           (|dollarTran| (|popStack1|) (|popStack1|))))) 


(DEFUN |PARSE-SemiColon| ()
  (AND (MATCH-ADVANCE-STRING ";")
       (MUST (OR (|PARSE-Expr| 82)
                 (|pushReduction| '|PARSE-SemiColon| '|/throwAway|)))
       (|pushReduction| '|PARSE-SemiColon|
           (CONS '|;| (CONS (|popStack2|) (CONS (|popStack1|) NIL)))))) 

;; We should factorize these boilerplates
(DEFUN |PARSE-Return| ()
  (AND (MATCH-ADVANCE-KEYWORD "return") (MUST (|PARSE-Expression|))
       (|pushReduction| '|PARSE-Return|
           (CONS '|return| (CONS (|popStack1|) NIL))))) 

(DEFUN |PARSE-Throw| ()
  (AND (MATCH-ADVANCE-KEYWORD "throw")
       (MUST (|PARSE-Expression|))
       (|pushReduction| '|PARSE-Throw|
           (CONS '|%Throw| (CONS (|popStack1|) NIL))))) 

(DEFUN |PARSE-Catch| ()
  (AND (MATCH-SPECIAL ";")
       (MATCH-KEYWORD-NEXT "catch")
       (ACTION (|advanceToken|))
       (ACTION (|advanceToken|))
       (MUST (|PARSE-GlyphTok| "("))
       (MUST (|PARSE-QuantifiedVariable|))
       (MUST (MATCH-ADVANCE-SPECIAL ")"))
       (MUST (|PARSE-GlyphTok| "=>"))
       (MUST (|PARSE-Expression|))
       (|pushReduction| '|PARSE-Catch|
	  (CONS (|popStack2|)
		(CONS (|popStack1|) NIL)))))

(DEFUN |PARSE-Finally| ()
  (AND (MATCH-SPECIAL ";")
       (MATCH-KEYWORD-NEXT "finally")
       (ACTION (|advanceToken|))
       (ACTION (|advanceToken|))
       (MUST (|PARSE-Expression|))))

(DEFUN |PARSE-Try| ()
  (AND (MATCH-ADVANCE-KEYWORD "try")
       (MUST (|PARSE-Expression|))
       ;; exception handlers: either a finally-expression, or
       ;; a series of catch-expressions optionally followed by
       ;; a finally-expression.
       (MUST (OR (AND (|PARSE-Finally|)
		      (|pushReduction| '|PARSE-Try|
                         (CONS '|%Try| 
			       (CONS (|popStack2|)
				     (CONS NIL
					   (CONS (|popStack1|) NIL))))))
		 (AND (MUST (STAR REPEATOR (|PARSE-Catch|)))
		      (BANG FIL_TEST
			    (OPTIONAL (|PARSE-Finally|)))
		      (|pushReduction| '|PARSE-Try|
                         (CONS '|%Try|
			       (CONS (|popStack3|)
				     (CONS (|popStack2|)
					   (CONS (|popStack1|) 
						 NIL))))))))))


(DEFUN |PARSE-Jump| ()
  (LET ((S (|currentSymbol|)))
       (AND S 
	    (ACTION (|advanceToken|))
	    (|pushReduction| '|PARSE-Jump| S))))


(DEFUN |PARSE-Exit| ()
  (AND (MATCH-ADVANCE-KEYWORD "exit")
       (MUST (OR (|PARSE-Expression|)
                 (|pushReduction| '|PARSE-Exit| '|$NoValue|)))
       (|pushReduction| '|PARSE-Exit|
           (CONS '|exit| (CONS (|popStack1|) NIL)))))


(DEFUN |PARSE-Leave| ()
  (AND (MATCH-ADVANCE-KEYWORD "leave")
       (MUST (OR (|PARSE-Expression|)
                 (|pushReduction| '|PARSE-Leave| '|$NoValue|)))
       (MUST (|pushReduction| '|PARSE-Leave|
                 (CONS '|leave| (CONS (|popStack1|) NIL))))))


(DEFUN |PARSE-Seg| ()
  (AND (|PARSE-GlyphTok| "..")
       (BANG FIL_TEST (OPTIONAL (|PARSE-Expression|)))
       (|pushReduction| '|PARSE-Seg|
           (CONS 'SEGMENT
                 (CONS (|popStack2|) (CONS (|popStack1|) NIL)))))) 


(DEFUN |PARSE-Conditional| ()
  (AND (MATCH-ADVANCE-KEYWORD "if") (MUST (|PARSE-Expression|))
       (MUST (MATCH-ADVANCE-KEYWORD "then")) (MUST (|PARSE-Expression|))
       (BANG FIL_TEST
             (OPTIONAL
                 (AND (MATCH-ADVANCE-KEYWORD "else")
                      (MUST (|PARSE-ElseClause|)))))
       (|pushReduction| '|PARSE-Conditional|
           (CONS '|if|
                 (CONS (|popStack3|)
                       (CONS (|popStack2|) (CONS (|popStack1|) NIL))))))) 


(DEFUN |PARSE-ElseClause| ()
  (OR (AND (EQ (|currentSymbol|) '|if|) (|PARSE-Conditional|))
      (|PARSE-Expression|))) 


(DEFUN |PARSE-Loop| ()
  (OR (AND (STAR REPEATOR (|PARSE-Iterator|))
           (MUST (MATCH-ADVANCE-KEYWORD "repeat"))
           (MUST (|PARSE-Expr| 110))
           (|pushReduction| '|PARSE-Loop|
               (CONS 'REPEAT
                     (APPEND (|popStack2|) (CONS (|popStack1|) NIL)))))
      (AND (MATCH-ADVANCE-KEYWORD "repeat") (MUST (|PARSE-Expr| 110))
           (|pushReduction| '|PARSE-Loop|
               (CONS 'REPEAT (CONS (|popStack1|) NIL)))))) 


(DEFUN |PARSE-Variable| ()
  (OR (AND (|PARSE-Name|)
	   (OPTIONAL (AND (MATCH-ADVANCE-STRING ":")
			  (MUST (|PARSE-Application|))
			  (MUST (|pushReduction| '|PARSE-Variable|
				   (CONS '|:| 
				      (CONS (|popStack2|)
					    (CONS (|popStack1|) NIL))))))))
      (|PARSE-Primary|)))

(DEFUN |PARSE-Iterator| ()
  (OR (AND (MATCH-ADVANCE-KEYWORD "for") (MUST (|PARSE-Variable|))
           (MUST (MATCH-ADVANCE-KEYWORD "in"))
           (MUST (|PARSE-Expression|))
           (MUST (OR (AND (MATCH-ADVANCE-KEYWORD "by")
                          (MUST (|PARSE-Expr| 200))
                          (|pushReduction| '|PARSE-Iterator|
                              (CONS 'INBY
                                    (CONS (|popStack3|)
                                     (CONS (|popStack2|)
                                      (CONS (|popStack1|) NIL))))))
                     (|pushReduction| '|PARSE-Iterator|
                         (CONS 'IN
                               (CONS (|popStack2|)
                                     (CONS (|popStack1|) NIL))))))
           (OPTIONAL
               (AND (MATCH-ADVANCE-STRING "|")
                    (MUST (|PARSE-Expr| 111))
                    (|pushReduction| '|PARSE-Iterator|
                        (CONS '|\|| (CONS (|popStack1|) NIL))))))
      (AND (MATCH-ADVANCE-KEYWORD "while") (MUST (|PARSE-Expr| 190))
           (|pushReduction| '|PARSE-Iterator|
               (CONS 'WHILE (CONS (|popStack1|) NIL))))
      (AND (MATCH-ADVANCE-KEYWORD "until") (MUST (|PARSE-Expr| 190))
           (|pushReduction| '|PARSE-Iterator|
               (CONS 'UNTIL (CONS (|popStack1|) NIL)))))) 


(DEFUN |PARSE-Match| ()
  (AND (MATCH-ADVANCE-KEYWORD "case")
       (MUST (|PARSE-Expr| 400))
       (MATCH-ADVANCE-KEYWORD "is")
       (MUST (|PARSE-Expr| 110))
       (|pushReduction| '|PARSE-Match|
		       (CONS '|%Match|
			     (CONS (|popStack2|)
				   (CONS (|popStack1|) NIL))))))

(DEFUN |PARSE-Expr| (RBP)
  (DECLARE (SPECIAL RBP))
  (AND (|PARSE-NudPart| RBP)
       (OPTIONAL (STAR OPT_EXPR (|PARSE-LedPart| RBP)))
       (|pushReduction| '|PARSE-Expr| (|popStack1|)))) 


(DEFUN |PARSE-Label| ()
  (AND (MATCH-ADVANCE-STRING "<<") (MUST (|PARSE-Name|))
       (MUST (MATCH-ADVANCE-STRING ">>")))) 


(DEFUN |PARSE-LedPart| (RBP)
  (DECLARE (SPECIAL RBP))
  (AND (|PARSE-Operation| '|Led| RBP)
       (|pushReduction| '|PARSE-LedPart| (|popStack1|)))) 


(DEFUN |PARSE-NudPart| (RBP)
  (DECLARE (SPECIAL RBP))
  (AND (OR (|PARSE-Operation| '|Nud| RBP) (|PARSE-Reduction|)
           (|PARSE-Form|))
       (|pushReduction| '|PARSE-NudPart| (|popStack1|)))) 


(DEFUN |PARSE-Operation| (|ParseMode| RBP)
  (DECLARE (SPECIAL |ParseMode| RBP))
  (AND (NOT (|matchCurrentToken| 'IDENTIFIER))
       (GETL (SETQ |tmptok| (|currentSymbol|)) |ParseMode|)
       (LT RBP (|PARSE-leftBindingPowerOf| |tmptok| |ParseMode|))
       (ACTION (SETQ RBP
                     (|PARSE-rightBindingPowerOf| |tmptok| |ParseMode|)))
       (|PARSE-getSemanticForm| |tmptok| |ParseMode|
           (ELEMN (GETL |tmptok| |ParseMode|) 5 NIL)))) 


(DEFUN |PARSE-leftBindingPowerOf| (X IND)
  (DECLARE (SPECIAL X IND))
  (LET ((Y (GETL X IND))) (IF Y (ELEMN Y 3 0) 0))) 


(DEFUN |PARSE-rightBindingPowerOf| (X IND)
  (DECLARE (SPECIAL X IND))
  (LET ((Y (GETL X IND))) (IF Y (ELEMN Y 4 105) 105))) 


(DEFUN |PARSE-getSemanticForm| (X IND Y)
  (DECLARE (SPECIAL X IND Y))
  (OR (AND Y (EVAL Y)) (AND (EQ IND '|Nud|) (|PARSE-Prefix|))
      (AND (EQ IND '|Led|) (|PARSE-Infix|)))) 


(DEFUN |PARSE-Reduction| ()
  (AND (|PARSE-ReductionOp|) (MUST (|PARSE-Expr| 1000))
       (|pushReduction| '|PARSE-Reduction|
           (CONS '|%Reduce|
                 (CONS (|popStack2|) (CONS (|popStack1|) NIL)))))) 


(DEFUN |PARSE-ReductionOp| ()
  (AND (GETL (|currentSymbol|) '|Led|)
       (|matchNextToken| 'GLIPH '/)
       (|pushReduction| '|PARSE-ReductionOp| (|currentSymbol|))
       (ACTION (|advanceToken|)) (ACTION (|advanceToken|)))) 


(DEFUN |PARSE-Form| ()
  (OR (AND (MATCH-ADVANCE-KEYWORD "iterate")
           (BANG FIL_TEST
                 (OPTIONAL
                     (AND (MATCH-ADVANCE-KEYWORD "from")
                          (MUST (|PARSE-Label|))
                          (|pushReduction| '|PARSE-Form|
                              (CONS (|popStack1|) NIL)))))
           (|pushReduction| '|PARSE-Form|
               (CONS '|iterate| (APPEND (|popStack1|) NIL))))
      (AND (MATCH-ADVANCE-KEYWORD "yield") (MUST (|PARSE-Application|))
           (|pushReduction| '|PARSE-Form|
               (CONS '|yield| (CONS (|popStack1|) NIL))))
      (|PARSE-Application|))) 


(DEFUN |PARSE-Application| ()
  (AND (|PARSE-Primary|) (OPTIONAL (STAR OPT_EXPR (|PARSE-Selector|)))
       (OPTIONAL
           (AND (|PARSE-Application|)
                (|pushReduction| '|PARSE-Application|
                    (CONS (|popStack2|) (CONS (|popStack1|) NIL))))))) 


(DEFUN |PARSE-Selector| ()
  (OR (AND |$nonblank| (EQ (|currentSymbol|) '|.|)
           (CHAR-NE (|currentChar|) '| |) (MATCH-ADVANCE-STRING ".")
           (MUST (|PARSE-PrimaryNoFloat|))
           (MUST (|pushReduction| '|PARSE-Selector|
                     (CONS (|popStack2|) (CONS (|popStack1|) NIL)))))
      (AND (OR (|PARSE-Float|)
               (AND (MATCH-ADVANCE-STRING ".")
                    (MUST (|PARSE-Primary|))))
           (MUST (|pushReduction| '|PARSE-Selector|
                     (CONS (|popStack2|) (CONS (|popStack1|) NIL))))))) 


(DEFUN |PARSE-PrimaryNoFloat| ()
  (AND (|PARSE-Primary1|) (OPTIONAL (|PARSE-TokTail|)))) 


(DEFUN |PARSE-Primary| ()
  (OR (|PARSE-Float|) (|PARSE-PrimaryNoFloat|))) 


(DEFUN |PARSE-Primary1| ()
  (OR (AND (|PARSE-VarForm|)
           (OPTIONAL
               (AND |$nonblank| (EQ (|currentSymbol|) '|(|)
                    (MUST (|PARSE-Primary1|))
                    (|pushReduction| '|PARSE-Primary1|
                        (CONS (|popStack2|) (CONS (|popStack1|) NIL))))))
      (|PARSE-Quad|) (|PARSE-String|) (|PARSE-IntegerTok|)
      (|PARSE-FormalParameter|)
      (AND (MATCH-ADVANCE-STRING "'")
           (MUST (AND (MUST (|PARSE-Data|))
                      (|pushReduction| '|PARSE-Primary1| (|popStack1|)))))
      (|PARSE-Sequence|) (|PARSE-Enclosure|))) 


(DEFUN |PARSE-Float| ()
  (AND (|PARSE-FloatBase|)
       (MUST (OR (AND |$nonblank| (|PARSE-FloatExponent|))
                 (|pushReduction| '|PARSE-Float| 0)))
       (|pushReduction| '|PARSE-Float|
           (MAKE-FLOAT (|popStack4|) (|popStack2|) (|popStack2|)
               (|popStack1|))))) 


(DEFUN |PARSE-FloatBase| ()
  (OR (AND (INTEGERP (|currentSymbol|)) (CHAR-EQ (|currentChar|) ".")
           (CHAR-NE (|nextChar|) ".") (|PARSE-IntegerTok|)
           (MUST (|PARSE-FloatBasePart|)))
      (AND (INTEGERP (|currentSymbol|))
           (CHAR-EQ (CHAR-UPCASE (|currentChar|)) 'E)
           (|PARSE-IntegerTok|) (|pushReduction| '|PARSE-FloatBase| 0)
           (|pushReduction| '|PARSE-FloatBase| 0))
      (AND (DIGITP (|currentChar|)) (EQ (|currentSymbol|) '|.|)
           (|pushReduction| '|PARSE-FloatBase| 0)
           (|PARSE-FloatBasePart|)))) 


(DEFUN |PARSE-FloatBasePart| ()
  (AND (MATCH-ADVANCE-STRING ".")
       (MUST (OR (AND (DIGITP (|currentChar|))
                      (|pushReduction| '|PARSE-FloatBasePart|
                          (|tokenNonblank?| (|currentToken|)))
                      (|PARSE-IntegerTok|))
                 (AND (|pushReduction| '|PARSE-FloatBasePart| 0)
                      (|pushReduction| '|PARSE-FloatBasePart| 0)))))) 


(DEFUN |PARSE-FloatExponent| ()
  (PROG (G1)
    (RETURN
      (OR (AND (MEMBER (|currentSymbol|) '(E |e|))
               (FIND (|currentChar|) "+-") (ACTION (|advanceToken|))
               (MUST (OR (|PARSE-IntegerTok|)
                         (AND (MATCH-ADVANCE-STRING "+")
                              (MUST (|PARSE-IntegerTok|)))
                         (AND (MATCH-ADVANCE-STRING "-")
                              (MUST (|PARSE-IntegerTok|))
                              (|pushReduction| '|PARSE-FloatExponent|
                                  (MINUS (|popStack1|))))
                         (|pushReduction| '|PARSE-FloatExponent| 0))))
          (AND (IDENTP (|currentSymbol|))
               (SETQ G1 (FLOATEXPID (|currentSymbol|)))
               (ACTION (|advanceToken|))
               (|pushReduction| '|PARSE-FloatExponent| G1)))))) 


(DEFUN |PARSE-Enclosure| ()
  (OR (AND (MATCH-ADVANCE-STRING "(")
           (MUST (OR (AND (|PARSE-Expr| 6)
                          (MUST (MATCH-ADVANCE-STRING ")")))
                     (AND (MATCH-ADVANCE-STRING ")")
                          (|pushReduction| '|PARSE-Enclosure|
                              (CONS '|%Comma| NIL))))))
      (AND (MATCH-ADVANCE-STRING "{")
           (MUST (OR (AND (|PARSE-Expr| 6)
                          (MUST (MATCH-ADVANCE-STRING "}"))
                          (|pushReduction| '|PARSE-Enclosure|
                              (CONS '|brace|
                                    (CONS
                                     (CONS '|construct|
                                      (CONS (|popStack1|) NIL))
                                     NIL))))
                     (AND (MATCH-ADVANCE-STRING "}")
                          (|pushReduction| '|PARSE-Enclosure|
                              (CONS '|brace| NIL))))))
      (AND (MATCH-ADVANCE-STRING "[|")
	   (MUST (AND (|PARSE-Statement|)
		      (MUST (MATCH-ADVANCE-STRING "|]"))
		      (|pushReduction| '|PARSE-Enclosure|
				      (CONS '|[\|\|]|
					    (CONS (|popStack1|) NIL)))
		      )))
      )) 


(DEFUN |PARSE-IntegerTok| () (PARSE-NUMBER)) 


(DEFUN |PARSE-FloatTok| ()
  (AND (PARSE-NUMBER)
       (|pushReduction| '|PARSE-FloatTok| (|popStack1|)))) 


(DEFUN |PARSE-FormalParameter| () (|PARSE-FormalParameterTok|)) 


(DEFUN |PARSE-FormalParameterTok| () (PARSE-ARGUMENT-DESIGNATOR)) 


(DEFUN |PARSE-Quad| ()
  (AND (MATCH-ADVANCE-STRING "$")
       (|pushReduction| '|PARSE-Quad| '$)))


(DEFUN |PARSE-String| () (PARSE-SPADSTRING)) 


(DEFUN |PARSE-VarForm| ()
  (AND (|PARSE-Name|)
       (OPTIONAL
           (AND (|PARSE-Scripts|)
                (|pushReduction| '|PARSE-VarForm|
                    (CONS '|Scripts|
                          (CONS (|popStack2|) (CONS (|popStack1|) NIL))))))
       (|pushReduction| '|PARSE-VarForm| (|popStack1|)))) 


(DEFUN |PARSE-Scripts| ()
  (AND |$nonblank| (MATCH-ADVANCE-STRING "[") (MUST (|PARSE-ScriptItem|))
       (MUST (MATCH-ADVANCE-STRING "]")))) 


(DEFUN |PARSE-ScriptItem| ()
  (OR (AND (|PARSE-Expr| 90)
           (OPTIONAL
               (AND (STAR REPEATOR
                          (AND (MATCH-ADVANCE-STRING ";")
                               (MUST (|PARSE-ScriptItem|))))
                    (|pushReduction| '|PARSE-ScriptItem|
                        (CONS '|;|
                              (CONS (|popStack2|)
                                    (APPEND (|popStack1|) NIL)))))))
      (AND (MATCH-ADVANCE-STRING ";") (MUST (|PARSE-ScriptItem|))
           (|pushReduction| '|PARSE-ScriptItem|
               (CONS '|PrefixSC| (CONS (|popStack1|) NIL)))))) 


(DEFUN |PARSE-Name| ()
  (AND (PARSE-IDENTIFIER) (|pushReduction| '|PARSE-Name| (|popStack1|)))) 


(DEFUN |PARSE-Data| ()
  (AND (ACTION (SETQ LABLASOC NIL)) (|PARSE-Sexpr|)
       (|pushReduction| '|PARSE-Data|
           (CONS 'QUOTE (CONS (TRANSLABEL (|popStack1|) LABLASOC) NIL))))) 


(DEFUN |PARSE-Sexpr| ()
  (AND (ACTION (|advanceToken|)) (|PARSE-Sexpr1|))) 


(DEFUN |PARSE-Sexpr1| ()
  (OR (|PARSE-IntegerTok|)
      (|PARSE-String|)
      (AND (|PARSE-AnyId|)
           (OPTIONAL
               (AND (|PARSE-NBGliphTok| '=) (MUST (|PARSE-Sexpr1|))
                    (ACTION (SETQ LABLASOC
                                  (CONS (CONS (|popStack2|)
                                         (|nthStack| 1))
                                        LABLASOC))))))
      (AND (MATCH-ADVANCE-STRING "'") (MUST (|PARSE-Sexpr1|))
           (|pushReduction| '|PARSE-Sexpr1|
               (CONS 'QUOTE (CONS (|popStack1|) NIL))))
      ;; next form disabled -- gdr, 2009-06-15.
;      (AND (MATCH-ADVANCE-STRING "-") (MUST (|PARSE-IntegerTok|))
;           (|pushReduction| '|PARSE-Sexpr1| (MINUS (|popStack1|))))
      (AND (MATCH-ADVANCE-STRING "[")
           (BANG FIL_TEST (OPTIONAL (STAR REPEATOR (|PARSE-Sexpr1|))))
           (MUST (MATCH-ADVANCE-STRING "]"))
           (|pushReduction| '|PARSE-Sexpr1| (LIST2VEC (|popStack1|))))
      (AND (MATCH-ADVANCE-STRING "(")
           (BANG FIL_TEST
                 (OPTIONAL
                     (AND (STAR REPEATOR (|PARSE-Sexpr1|))
                          (OPTIONAL
                              (AND (|PARSE-GlyphTok| ".")
                                   (MUST (|PARSE-Sexpr1|))
                                   (|pushReduction| '|PARSE-Sexpr1|
                                    (|append!| (|popStack2|) (|popStack1|))))))))
           (MUST (MATCH-ADVANCE-STRING ")"))))) 


(DEFUN |PARSE-NBGliphTok| (|tok|)
  (DECLARE (SPECIAL |tok|))
  (AND (|matchCurrentToken| 'GLIPH |tok|) |$nonblank|
       (ACTION (|advanceToken|)))) 


(DEFUN |PARSE-GlyphTok| (|tok|)
  (DECLARE (SPECIAL |tok|))
  (AND (|matchCurrentToken| 'GLIPH (INTERN |tok|))
       (ACTION (|advanceToken|)))) 


(DEFUN |PARSE-AnyId| ()
  (OR (|PARSE-Name|)
      (OR (AND (MATCH-STRING "$")
               (|pushReduction| '|PARSE-AnyId| (|currentSymbol|))
               (ACTION (|advanceToken|)))
          (PARSE-KEYWORD)
	  (|PARSE-OperatorFunctionName|))))


(DEFUN |PARSE-Sequence| ()
  (OR (AND (|PARSE-OpenBracket|) (MUST (|PARSE-Sequence1|))
           (MUST (MATCH-ADVANCE-STRING "]")))
      (AND (|PARSE-OpenBrace|) (MUST (|PARSE-Sequence1|))
           (MUST (MATCH-ADVANCE-STRING "}"))
           (|pushReduction| '|PARSE-Sequence|
               (CONS '|brace| (CONS (|popStack1|) NIL)))))) 


(DEFUN |PARSE-Sequence1| ()
  (AND (OR (AND (|PARSE-Expression|)
                (|pushReduction| '|PARSE-Sequence1|
                    (CONS (|popStack2|) (CONS (|popStack1|) NIL))))
           (|pushReduction| '|PARSE-Sequence1| (CONS (|popStack1|) NIL)))
       (OPTIONAL
           (AND (|PARSE-IteratorTail|)
                (|pushReduction| '|PARSE-Sequence1|
                    (CONS 'COLLECT
                          (APPEND (|popStack1|)
                                  (CONS (|popStack1|) NIL)))))))) 


(DEFUN |PARSE-OpenBracket| ()
  (LET ((G1 (|currentSymbol|)))
    (AND (EQ (|getToken| G1) '[)
	 (MUST (OR (AND (EQCAR G1 '|elt|)
			(|pushReduction| '|PARSE-OpenBracket|
			    (CONS '|elt|
				  (CONS (CADR G1)
				   (CONS '|construct| NIL)))))
		   (|pushReduction| '|PARSE-OpenBracket| '|construct|)))
	 (ACTION (|advanceToken|)))))


(DEFUN |PARSE-OpenBrace| ()
  (LET ((G1 (|currentSymbol|)))
    (AND (EQ (|getToken| G1) '{)
	 (MUST (OR (AND (EQCAR G1 '|elt|)
			(|pushReduction| '|PARSE-OpenBrace|
			    (CONS '|elt|
				  (CONS (CADR G1)
				   (CONS '|brace| NIL)))))
		   (|pushReduction| '|PARSE-OpenBrace| '|construct|)))
	 (ACTION (|advanceToken|)))))


(DEFUN |PARSE-IteratorTail| ()
  (OR (AND (MATCH-ADVANCE-KEYWORD "repeat")
           (BANG FIL_TEST
                 (OPTIONAL (STAR REPEATOR (|PARSE-Iterator|)))))
      (STAR REPEATOR (|PARSE-Iterator|)))) 

