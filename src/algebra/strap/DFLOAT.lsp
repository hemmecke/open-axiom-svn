
(/VERSIONCHECK 2) 

(DEFUN |DFLOAT;OMwrite;$S;1| (|x| $)
  (PROG (|sp| |dev| |s|)
    (RETURN
      (SEQ (LETT |s| "" |DFLOAT;OMwrite;$S;1|)
           (LETT |sp| (OM-STRINGTOSTRINGPTR |s|) |DFLOAT;OMwrite;$S;1|)
           (LETT |dev|
                 (SPADCALL |sp| (SPADCALL (|getShellEntry| $ 7))
                     (|getShellEntry| $ 10))
                 |DFLOAT;OMwrite;$S;1|)
           (SPADCALL |dev| (|getShellEntry| $ 12))
           (SPADCALL |dev| |x| (|getShellEntry| $ 14))
           (SPADCALL |dev| (|getShellEntry| $ 15))
           (SPADCALL |dev| (|getShellEntry| $ 16))
           (LETT |s| (OM-STRINGPTRTOSTRING |sp|) |DFLOAT;OMwrite;$S;1|)
           (EXIT |s|))))) 

(DEFUN |DFLOAT;OMwrite;$BS;2| (|x| |wholeObj| $)
  (PROG (|sp| |dev| |s|)
    (RETURN
      (SEQ (LETT |s| "" |DFLOAT;OMwrite;$BS;2|)
           (LETT |sp| (OM-STRINGTOSTRINGPTR |s|)
                 |DFLOAT;OMwrite;$BS;2|)
           (LETT |dev|
                 (SPADCALL |sp| (SPADCALL (|getShellEntry| $ 7))
                     (|getShellEntry| $ 10))
                 |DFLOAT;OMwrite;$BS;2|)
           (COND (|wholeObj| (SPADCALL |dev| (|getShellEntry| $ 12))))
           (SPADCALL |dev| |x| (|getShellEntry| $ 14))
           (COND (|wholeObj| (SPADCALL |dev| (|getShellEntry| $ 15))))
           (SPADCALL |dev| (|getShellEntry| $ 16))
           (LETT |s| (OM-STRINGPTRTOSTRING |sp|)
                 |DFLOAT;OMwrite;$BS;2|)
           (EXIT |s|))))) 

(DEFUN |DFLOAT;OMwrite;Omd$V;3| (|dev| |x| $)
  (SEQ (SPADCALL |dev| (|getShellEntry| $ 12))
       (SPADCALL |dev| |x| (|getShellEntry| $ 14))
       (EXIT (SPADCALL |dev| (|getShellEntry| $ 15))))) 

(DEFUN |DFLOAT;OMwrite;Omd$BV;4| (|dev| |x| |wholeObj| $)
  (SEQ (COND (|wholeObj| (SPADCALL |dev| (|getShellEntry| $ 12))))
       (SPADCALL |dev| |x| (|getShellEntry| $ 14))
       (EXIT (COND
               (|wholeObj| (SPADCALL |dev| (|getShellEntry| $ 15))))))) 

(PUT '|DFLOAT;checkComplex| '|SPADreplace| 'C-TO-R) 

(DEFUN |DFLOAT;checkComplex| (|x| $) (C-TO-R |x|)) 

(PUT '|DFLOAT;base;Pi;6| '|SPADreplace| '(XLAM NIL (FLOAT-RADIX 0.0))) 

(DEFUN |DFLOAT;base;Pi;6| ($) (FLOAT-RADIX 0.0)) 

(DEFUN |DFLOAT;mantissa;$I;7| (|x| $) (QCAR (|DFLOAT;manexp| |x| $))) 

(DEFUN |DFLOAT;exponent;$I;8| (|x| $) (QCDR (|DFLOAT;manexp| |x| $))) 

(PUT '|DFLOAT;precision;Pi;9| '|SPADreplace|
     '(XLAM NIL (FLOAT-DIGITS 0.0))) 

(DEFUN |DFLOAT;precision;Pi;9| ($) (FLOAT-DIGITS 0.0)) 

(DEFUN |DFLOAT;bits;Pi;10| ($)
  (PROG (#0=#:G1422)
    (RETURN
      (COND
        ((EQL (FLOAT-RADIX 0.0) 2) (FLOAT-DIGITS 0.0))
        ((EQL (FLOAT-RADIX 0.0) 16) (* 4 (FLOAT-DIGITS 0.0)))
        ('T
         (PROG1 (LETT #0#
                      (FIX (SPADCALL (FLOAT-DIGITS 0.0)
                               (SPADCALL
                                   (FLOAT (FLOAT-RADIX 0.0)
                                    |$DoubleFloatMaximum|)
                                   (|getShellEntry| $ 28))
                               (|getShellEntry| $ 29)))
                      |DFLOAT;bits;Pi;10|)
           (|check-subtype| (> #0# 0) '(|PositiveInteger|) #0#))))))) 

(PUT '|DFLOAT;max;$;11| '|SPADreplace|
     '(XLAM NIL |$DoubleFloatMaximum|)) 

(DEFUN |DFLOAT;max;$;11| ($) |$DoubleFloatMaximum|) 

(PUT '|DFLOAT;min;$;12| '|SPADreplace|
     '(XLAM NIL |$DoubleFloatMinimum|)) 

(DEFUN |DFLOAT;min;$;12| ($) |$DoubleFloatMinimum|) 

(DEFUN |DFLOAT;order;$I;13| (|a| $)
  (- (+ (FLOAT-DIGITS 0.0) (SPADCALL |a| (|getShellEntry| $ 26))) 1)) 

(PUT '|DFLOAT;Zero;$;14| '|SPADreplace|
     '(XLAM NIL (FLOAT 0 |$DoubleFloatMaximum|))) 

(DEFUN |DFLOAT;Zero;$;14| ($) (FLOAT 0 |$DoubleFloatMaximum|)) 

(PUT '|DFLOAT;One;$;15| '|SPADreplace|
     '(XLAM NIL (FLOAT 1 |$DoubleFloatMaximum|))) 

(DEFUN |DFLOAT;One;$;15| ($) (FLOAT 1 |$DoubleFloatMaximum|)) 

(DEFUN |DFLOAT;exp1;$;16| ($)
  (/ (FLOAT 534625820200 |$DoubleFloatMaximum|)
     (FLOAT 196677847971 |$DoubleFloatMaximum|))) 

(PUT '|DFLOAT;pi;$;17| '|SPADreplace| '(XLAM NIL PI)) 

(DEFUN |DFLOAT;pi;$;17| ($) PI) 

(DEFUN |DFLOAT;coerce;$Of;18| (|x| $)
  (SPADCALL |x| (|getShellEntry| $ 39))) 

(DEFUN |DFLOAT;convert;$If;19| (|x| $)
  (SPADCALL |x| (|getShellEntry| $ 42))) 

(PUT '|DFLOAT;<;2$B;20| '|SPADreplace| '<) 

(DEFUN |DFLOAT;<;2$B;20| (|x| |y| $) (< |x| |y|)) 

(PUT '|DFLOAT;-;2$;21| '|SPADreplace| '-) 

(DEFUN |DFLOAT;-;2$;21| (|x| $) (- |x|)) 

(PUT '|DFLOAT;+;3$;22| '|SPADreplace| '+) 

(DEFUN |DFLOAT;+;3$;22| (|x| |y| $) (+ |x| |y|)) 

(PUT '|DFLOAT;-;3$;23| '|SPADreplace| '-) 

(DEFUN |DFLOAT;-;3$;23| (|x| |y| $) (- |x| |y|)) 

(PUT '|DFLOAT;*;3$;24| '|SPADreplace| '*) 

(DEFUN |DFLOAT;*;3$;24| (|x| |y| $) (* |x| |y|)) 

(PUT '|DFLOAT;*;I2$;25| '|SPADreplace| '*) 

(DEFUN |DFLOAT;*;I2$;25| (|i| |x| $) (* |i| |x|)) 

(PUT '|DFLOAT;max;3$;26| '|SPADreplace| 'MAX) 

(DEFUN |DFLOAT;max;3$;26| (|x| |y| $) (MAX |x| |y|)) 

(PUT '|DFLOAT;min;3$;27| '|SPADreplace| 'MIN) 

(DEFUN |DFLOAT;min;3$;27| (|x| |y| $) (MIN |x| |y|)) 

(PUT '|DFLOAT;=;2$B;28| '|SPADreplace| '=) 

(DEFUN |DFLOAT;=;2$B;28| (|x| |y| $) (= |x| |y|)) 

(PUT '|DFLOAT;/;$I$;29| '|SPADreplace| '/) 

(DEFUN |DFLOAT;/;$I$;29| (|x| |i| $) (/ |x| |i|)) 

(DEFUN |DFLOAT;sqrt;2$;30| (|x| $)
  (|DFLOAT;checkComplex| (SQRT |x|) $)) 

(DEFUN |DFLOAT;log10;2$;31| (|x| $)
  (|DFLOAT;checkComplex| (|log| |x|) $)) 

(PUT '|DFLOAT;**;$I$;32| '|SPADreplace| 'EXPT) 

(DEFUN |DFLOAT;**;$I$;32| (|x| |i| $) (EXPT |x| |i|)) 

(DEFUN |DFLOAT;**;3$;33| (|x| |y| $)
  (|DFLOAT;checkComplex| (EXPT |x| |y|) $)) 

(PUT '|DFLOAT;coerce;I$;34| '|SPADreplace|
     '(XLAM (|i|) (FLOAT |i| |$DoubleFloatMaximum|))) 

(DEFUN |DFLOAT;coerce;I$;34| (|i| $)
  (FLOAT |i| |$DoubleFloatMaximum|)) 

(PUT '|DFLOAT;exp;2$;35| '|SPADreplace| 'EXP) 

(DEFUN |DFLOAT;exp;2$;35| (|x| $) (EXP |x|)) 

(DEFUN |DFLOAT;log;2$;36| (|x| $) (|DFLOAT;checkComplex| (LN |x|) $)) 

(DEFUN |DFLOAT;log2;2$;37| (|x| $)
  (|DFLOAT;checkComplex| (LOG2 |x|) $)) 

(PUT '|DFLOAT;sin;2$;38| '|SPADreplace| 'SIN) 

(DEFUN |DFLOAT;sin;2$;38| (|x| $) (SIN |x|)) 

(PUT '|DFLOAT;cos;2$;39| '|SPADreplace| 'COS) 

(DEFUN |DFLOAT;cos;2$;39| (|x| $) (COS |x|)) 

(PUT '|DFLOAT;tan;2$;40| '|SPADreplace| 'TAN) 

(DEFUN |DFLOAT;tan;2$;40| (|x| $) (TAN |x|)) 

(PUT '|DFLOAT;cot;2$;41| '|SPADreplace| 'COT) 

(DEFUN |DFLOAT;cot;2$;41| (|x| $) (COT |x|)) 

(PUT '|DFLOAT;sec;2$;42| '|SPADreplace| 'SEC) 

(DEFUN |DFLOAT;sec;2$;42| (|x| $) (SEC |x|)) 

(PUT '|DFLOAT;csc;2$;43| '|SPADreplace| 'CSC) 

(DEFUN |DFLOAT;csc;2$;43| (|x| $) (CSC |x|)) 

(DEFUN |DFLOAT;asin;2$;44| (|x| $)
  (|DFLOAT;checkComplex| (ASIN |x|) $)) 

(DEFUN |DFLOAT;acos;2$;45| (|x| $)
  (|DFLOAT;checkComplex| (ACOS |x|) $)) 

(PUT '|DFLOAT;atan;2$;46| '|SPADreplace| 'ATAN) 

(DEFUN |DFLOAT;atan;2$;46| (|x| $) (ATAN |x|)) 

(DEFUN |DFLOAT;acsc;2$;47| (|x| $)
  (|DFLOAT;checkComplex| (ACSC |x|) $)) 

(PUT '|DFLOAT;acot;2$;48| '|SPADreplace| 'ACOT) 

(DEFUN |DFLOAT;acot;2$;48| (|x| $) (ACOT |x|)) 

(DEFUN |DFLOAT;asec;2$;49| (|x| $)
  (|DFLOAT;checkComplex| (ASEC |x|) $)) 

(PUT '|DFLOAT;sinh;2$;50| '|SPADreplace| 'SINH) 

(DEFUN |DFLOAT;sinh;2$;50| (|x| $) (SINH |x|)) 

(PUT '|DFLOAT;cosh;2$;51| '|SPADreplace| 'COSH) 

(DEFUN |DFLOAT;cosh;2$;51| (|x| $) (COSH |x|)) 

(PUT '|DFLOAT;tanh;2$;52| '|SPADreplace| 'TANH) 

(DEFUN |DFLOAT;tanh;2$;52| (|x| $) (TANH |x|)) 

(PUT '|DFLOAT;csch;2$;53| '|SPADreplace| 'CSCH) 

(DEFUN |DFLOAT;csch;2$;53| (|x| $) (CSCH |x|)) 

(PUT '|DFLOAT;coth;2$;54| '|SPADreplace| 'COTH) 

(DEFUN |DFLOAT;coth;2$;54| (|x| $) (COTH |x|)) 

(PUT '|DFLOAT;sech;2$;55| '|SPADreplace| 'SECH) 

(DEFUN |DFLOAT;sech;2$;55| (|x| $) (SECH |x|)) 

(PUT '|DFLOAT;asinh;2$;56| '|SPADreplace| 'ASINH) 

(DEFUN |DFLOAT;asinh;2$;56| (|x| $) (ASINH |x|)) 

(DEFUN |DFLOAT;acosh;2$;57| (|x| $)
  (|DFLOAT;checkComplex| (ACOSH |x|) $)) 

(DEFUN |DFLOAT;atanh;2$;58| (|x| $)
  (|DFLOAT;checkComplex| (ATANH |x|) $)) 

(PUT '|DFLOAT;acsch;2$;59| '|SPADreplace| 'ACSCH) 

(DEFUN |DFLOAT;acsch;2$;59| (|x| $) (ACSCH |x|)) 

(DEFUN |DFLOAT;acoth;2$;60| (|x| $)
  (|DFLOAT;checkComplex| (ACOTH |x|) $)) 

(DEFUN |DFLOAT;asech;2$;61| (|x| $)
  (|DFLOAT;checkComplex| (ASECH |x|) $)) 

(PUT '|DFLOAT;/;3$;62| '|SPADreplace| '/) 

(DEFUN |DFLOAT;/;3$;62| (|x| |y| $) (/ |x| |y|)) 

(PUT '|DFLOAT;negative?;$B;63| '|SPADreplace| 'MINUSP) 

(DEFUN |DFLOAT;negative?;$B;63| (|x| $) (MINUSP |x|)) 

(PUT '|DFLOAT;zero?;$B;64| '|SPADreplace| 'ZEROP) 

(DEFUN |DFLOAT;zero?;$B;64| (|x| $) (ZEROP |x|)) 

(PUT '|DFLOAT;hash;$Si;65| '|SPADreplace| 'HASHEQ) 

(DEFUN |DFLOAT;hash;$Si;65| (|x| $) (HASHEQ |x|)) 

(DEFUN |DFLOAT;recip;$U;66| (|x| $)
  (COND ((ZEROP |x|) (CONS 1 "failed")) ('T (CONS 0 (/ 1.0 |x|))))) 

(PUT '|DFLOAT;differentiate;2$;67| '|SPADreplace| '(XLAM (|x|) 0.0)) 

(DEFUN |DFLOAT;differentiate;2$;67| (|x| $) 0.0) 

(DEFUN |DFLOAT;Gamma;2$;68| (|x| $)
  (SPADCALL |x| (|getShellEntry| $ 94))) 

(DEFUN |DFLOAT;Beta;3$;69| (|x| |y| $)
  (SPADCALL |x| |y| (|getShellEntry| $ 96))) 

(PUT '|DFLOAT;wholePart;$I;70| '|SPADreplace| 'FIX) 

(DEFUN |DFLOAT;wholePart;$I;70| (|x| $) (FIX |x|)) 

(DEFUN |DFLOAT;float;2IPi$;71| (|ma| |ex| |b| $)
  (* |ma| (EXPT (FLOAT |b| |$DoubleFloatMaximum|) |ex|))) 

(PUT '|DFLOAT;convert;2$;72| '|SPADreplace| '(XLAM (|x|) |x|)) 

(DEFUN |DFLOAT;convert;2$;72| (|x| $) |x|) 

(DEFUN |DFLOAT;convert;$F;73| (|x| $)
  (SPADCALL |x| (|getShellEntry| $ 102))) 

(DEFUN |DFLOAT;rationalApproximation;$NniF;74| (|x| |d| $)
  (SPADCALL |x| |d| 10 (|getShellEntry| $ 106))) 

(DEFUN |DFLOAT;atan;3$;75| (|x| |y| $)
  (PROG (|theta|)
    (RETURN
      (SEQ (COND
             ((= |x| 0.0)
              (COND
                ((< 0.0 |y|) (/ PI 2))
                ((< |y| 0.0) (- (/ PI 2)))
                ('T 0.0)))
             ('T
              (SEQ (LETT |theta| (ATAN (FLOAT-SIGN 1.0 (/ |y| |x|)))
                         |DFLOAT;atan;3$;75|)
                   (COND
                     ((< |x| 0.0)
                      (LETT |theta| (- PI |theta|) |DFLOAT;atan;3$;75|)))
                   (COND
                     ((< |y| 0.0)
                      (LETT |theta| (- |theta|) |DFLOAT;atan;3$;75|)))
                   (EXIT |theta|)))))))) 

(DEFUN |DFLOAT;retract;$F;76| (|x| $)
  (PROG (#0=#:G1497)
    (RETURN
      (SPADCALL |x|
          (PROG1 (LETT #0# (- (FLOAT-DIGITS 0.0) 1)
                       |DFLOAT;retract;$F;76|)
            (|check-subtype| (>= #0# 0) '(|NonNegativeInteger|) #0#))
          (FLOAT-RADIX 0.0) (|getShellEntry| $ 106))))) 

(DEFUN |DFLOAT;retractIfCan;$U;77| (|x| $)
  (PROG (#0=#:G1502)
    (RETURN
      (CONS 0
            (SPADCALL |x|
                (PROG1 (LETT #0# (- (FLOAT-DIGITS 0.0) 1)
                             |DFLOAT;retractIfCan;$U;77|)
                  (|check-subtype| (>= #0# 0) '(|NonNegativeInteger|)
                      #0#))
                (FLOAT-RADIX 0.0) (|getShellEntry| $ 106)))))) 

(DEFUN |DFLOAT;retract;$I;78| (|x| $)
  (PROG (|n|)
    (RETURN
      (SEQ (LETT |n| (FIX |x|) |DFLOAT;retract;$I;78|)
           (EXIT (COND
                   ((= |x| (FLOAT |n| |$DoubleFloatMaximum|)) |n|)
                   ('T (|error| "Not an integer")))))))) 

(DEFUN |DFLOAT;retractIfCan;$U;79| (|x| $)
  (PROG (|n|)
    (RETURN
      (SEQ (LETT |n| (FIX |x|) |DFLOAT;retractIfCan;$U;79|)
           (EXIT (COND
                   ((= |x| (FLOAT |n| |$DoubleFloatMaximum|))
                    (CONS 0 |n|))
                   ('T (CONS 1 "failed")))))))) 

(DEFUN |DFLOAT;sign;$I;80| (|x| $)
  (SPADCALL (FLOAT-SIGN |x| 1.0) (|getShellEntry| $ 112))) 

(PUT '|DFLOAT;abs;2$;81| '|SPADreplace|
     '(XLAM (|x|) (FLOAT-SIGN 1.0 |x|))) 

(DEFUN |DFLOAT;abs;2$;81| (|x| $) (FLOAT-SIGN 1.0 |x|)) 

(DEFUN |DFLOAT;manexp| (|x| $)
  (PROG (|s| #0=#:G1523 |me| |two53|)
    (RETURN
      (SEQ (EXIT (COND
                   ((ZEROP |x|) (CONS 0 0))
                   ('T
                    (SEQ (LETT |s|
                               (SPADCALL |x| (|getShellEntry| $ 115))
                               |DFLOAT;manexp|)
                         (LETT |x| (FLOAT-SIGN 1.0 |x|)
                               |DFLOAT;manexp|)
                         (COND
                           ((< |$DoubleFloatMaximum| |x|)
                            (PROGN
                              (LETT #0#
                                    (CONS
                                     (+
                                      (* |s|
                                       (SPADCALL |$DoubleFloatMaximum|
                                        (|getShellEntry| $ 25)))
                                      1)
                                     (SPADCALL |$DoubleFloatMaximum|
                                      (|getShellEntry| $ 26)))
                                    |DFLOAT;manexp|)
                              (GO #0#))))
                         (LETT |me| (MANEXP |x|) |DFLOAT;manexp|)
                         (LETT |two53|
                               (SPADCALL (FLOAT-RADIX 0.0)
                                   (FLOAT-DIGITS 0.0)
                                   (|getShellEntry| $ 117))
                               |DFLOAT;manexp|)
                         (EXIT (CONS (* |s|
                                      (FIX (* |two53| (QCAR |me|))))
                                     (- (QCDR |me|) (FLOAT-DIGITS 0.0))))))))
           #0# (EXIT #0#))))) 

(DEFUN |DFLOAT;rationalApproximation;$2NniF;83| (|f| |d| |b| $)
  (PROG (|#G102| |nu| |ex| BASE #0=#:G1525 |de| |tol| |#G103| |q| |r|
                 |p2| |q2| #1=#:G1541 |#G104| |#G105| |p0| |p1| |#G106|
                 |#G107| |q0| |q1| |#G108| |#G109| |s| |t| #2=#:G1539)
    (RETURN
      (SEQ (EXIT (SEQ (PROGN
                        (LETT |#G102| (|DFLOAT;manexp| |f| $)
                              |DFLOAT;rationalApproximation;$2NniF;83|)
                        (LETT |nu| (QCAR |#G102|)
                              |DFLOAT;rationalApproximation;$2NniF;83|)
                        (LETT |ex| (QCDR |#G102|)
                              |DFLOAT;rationalApproximation;$2NniF;83|)
                        |#G102|)
                      (LETT BASE (FLOAT-RADIX 0.0)
                            |DFLOAT;rationalApproximation;$2NniF;83|)
                      (EXIT (COND
                              ((< |ex| 0)
                               (SEQ (LETT |de|
                                     (EXPT BASE
                                      (PROG1
                                       (LETT #0# (- |ex|)
                                        |DFLOAT;rationalApproximation;$2NniF;83|)
                                        (|check-subtype| (>= #0# 0)
                                         '(|NonNegativeInteger|) #0#)))
                                     |DFLOAT;rationalApproximation;$2NniF;83|)
                                    (EXIT
                                     (COND
                                       ((< |b| 2)
                                        (|error| "base must be > 1"))
                                       ('T
                                        (SEQ
                                         (LETT |tol| (EXPT |b| |d|)
                                          |DFLOAT;rationalApproximation;$2NniF;83|)
                                         (LETT |s| |nu|
                                          |DFLOAT;rationalApproximation;$2NniF;83|)
                                         (LETT |t| |de|
                                          |DFLOAT;rationalApproximation;$2NniF;83|)
                                         (LETT |p0| 0
                                          |DFLOAT;rationalApproximation;$2NniF;83|)
                                         (LETT |p1| 1
                                          |DFLOAT;rationalApproximation;$2NniF;83|)
                                         (LETT |q0| 1
                                          |DFLOAT;rationalApproximation;$2NniF;83|)
                                         (LETT |q1| 0
                                          |DFLOAT;rationalApproximation;$2NniF;83|)
                                         (EXIT
                                          (SEQ G190 NIL
                                           (SEQ
                                            (PROGN
                                              (LETT |#G103|
                                               (DIVIDE2 |s| |t|)
                                               |DFLOAT;rationalApproximation;$2NniF;83|)
                                              (LETT |q| (QCAR |#G103|)
                                               |DFLOAT;rationalApproximation;$2NniF;83|)
                                              (LETT |r| (QCDR |#G103|)
                                               |DFLOAT;rationalApproximation;$2NniF;83|)
                                              |#G103|)
                                            (LETT |p2|
                                             (+ (* |q| |p1|) |p0|)
                                             |DFLOAT;rationalApproximation;$2NniF;83|)
                                            (LETT |q2|
                                             (+ (* |q| |q1|) |q0|)
                                             |DFLOAT;rationalApproximation;$2NniF;83|)
                                            (COND
                                              ((OR (EQL |r| 0)
                                                (<
                                                 (SPADCALL |tol|
                                                  (ABS
                                                   (- (* |nu| |q2|)
                                                    (* |de| |p2|)))
                                                  (|getShellEntry| $
                                                   120))
                                                 (* |de| (ABS |p2|))))
                                               (EXIT
                                                (PROGN
                                                  (LETT #1#
                                                   (SPADCALL |p2| |q2|
                                                    (|getShellEntry| $
                                                     119))
                                                   |DFLOAT;rationalApproximation;$2NniF;83|)
                                                  (GO #1#)))))
                                            (PROGN
                                              (LETT |#G104| |p1|
                                               |DFLOAT;rationalApproximation;$2NniF;83|)
                                              (LETT |#G105| |p2|
                                               |DFLOAT;rationalApproximation;$2NniF;83|)
                                              (LETT |p0| |#G104|
                                               |DFLOAT;rationalApproximation;$2NniF;83|)
                                              (LETT |p1| |#G105|
                                               |DFLOAT;rationalApproximation;$2NniF;83|))
                                            (PROGN
                                              (LETT |#G106| |q1|
                                               |DFLOAT;rationalApproximation;$2NniF;83|)
                                              (LETT |#G107| |q2|
                                               |DFLOAT;rationalApproximation;$2NniF;83|)
                                              (LETT |q0| |#G106|
                                               |DFLOAT;rationalApproximation;$2NniF;83|)
                                              (LETT |q1| |#G107|
                                               |DFLOAT;rationalApproximation;$2NniF;83|))
                                            (EXIT
                                             (PROGN
                                               (LETT |#G108| |t|
                                                |DFLOAT;rationalApproximation;$2NniF;83|)
                                               (LETT |#G109| |r|
                                                |DFLOAT;rationalApproximation;$2NniF;83|)
                                               (LETT |s| |#G108|
                                                |DFLOAT;rationalApproximation;$2NniF;83|)
                                               (LETT |t| |#G109|
                                                |DFLOAT;rationalApproximation;$2NniF;83|))))
                                           NIL (GO G190) G191
                                           (EXIT NIL)))))))))
                              ('T
                               (SPADCALL
                                   (* |nu|
                                    (EXPT BASE
                                     (PROG1
                                      (LETT #2# |ex|
                                       |DFLOAT;rationalApproximation;$2NniF;83|)
                                       (|check-subtype| (>= #2# 0)
                                        '(|NonNegativeInteger|) #2#))))
                                   (|getShellEntry| $ 121)))))))
           #1# (EXIT #1#))))) 

(DEFUN |DFLOAT;**;$F$;84| (|x| |r| $)
  (PROG (|n| |d| #0=#:G1550)
    (RETURN
      (SEQ (EXIT (COND
                   ((ZEROP |x|)
                    (COND
                      ((SPADCALL |r| (|getShellEntry| $ 122))
                       (|error| "0**0 is undefined"))
                      ((SPADCALL |r| (|getShellEntry| $ 123))
                       (|error| "division by 0"))
                      ('T 0.0)))
                   ((OR (SPADCALL |r| (|getShellEntry| $ 122))
                        (= |x| 1.0))
                    1.0)
                   ('T
                    (COND
                      ((SPADCALL |r| (|spadConstant| $ 124)
                           (|getShellEntry| $ 125))
                       |x|)
                      ('T
                       (SEQ (LETT |n|
                                  (SPADCALL |r|
                                      (|getShellEntry| $ 126))
                                  |DFLOAT;**;$F$;84|)
                            (LETT |d|
                                  (SPADCALL |r|
                                      (|getShellEntry| $ 127))
                                  |DFLOAT;**;$F$;84|)
                            (EXIT (COND
                                    ((MINUSP |x|)
                                     (COND
                                       ((ODDP |d|)
                                        (COND
                                          ((ODDP |n|)
                                           (PROGN
                                             (LETT #0#
                                              (-
                                               (SPADCALL (- |x|) |r|
                                                (|getShellEntry| $ 128)))
                                              |DFLOAT;**;$F$;84|)
                                             (GO #0#)))
                                          ('T
                                           (PROGN
                                             (LETT #0#
                                              (SPADCALL (- |x|) |r|
                                               (|getShellEntry| $ 128))
                                              |DFLOAT;**;$F$;84|)
                                             (GO #0#)))))
                                       ('T (|error| "negative root"))))
                                    ((EQL |d| 2)
                                     (EXPT
                                      (SPADCALL |x|
                                       (|getShellEntry| $ 54))
                                      |n|))
                                    ('T
                                     (SPADCALL |x|
                                      (/
                                       (FLOAT |n|
                                        |$DoubleFloatMaximum|)
                                       (FLOAT |d|
                                        |$DoubleFloatMaximum|))
                                      (|getShellEntry| $ 57)))))))))))
           #0# (EXIT #0#))))) 

(DEFUN |DoubleFloat| ()
  (PROG ()
    (RETURN
      (PROG (#0=#:G1563)
        (RETURN
          (COND
            ((LETT #0# (HGET |$ConstructorCache| '|DoubleFloat|)
                   |DoubleFloat|)
             (|CDRwithIncrement| (CDAR #0#)))
            ('T
             (UNWIND-PROTECT
               (PROG1 (CDDAR (HPUT |$ConstructorCache| '|DoubleFloat|
                                   (LIST
                                    (CONS NIL
                                     (CONS 1 (|DoubleFloat;|))))))
                 (LETT #0# T |DoubleFloat|))
               (COND
                 ((NOT #0#) (HREM |$ConstructorCache| '|DoubleFloat|))))))))))) 

(DEFUN |DoubleFloat;| ()
  (PROG (|dv$| $ |pv$|)
    (RETURN
      (PROGN
        (LETT |dv$| '(|DoubleFloat|) . #0=(|DoubleFloat|))
        (LETT $ (|newShell| 141) . #0#)
        (|setShellEntry| $ 0 |dv$|)
        (|setShellEntry| $ 3
            (LETT |pv$| (|buildPredVector| 0 0 NIL) . #0#))
        (|haddProp| |$ConstructorCache| '|DoubleFloat| NIL (CONS 1 $))
        (|stuffDomainSlots| $)
        $)))) 

(MAKEPROP '|DoubleFloat| '|infovec|
    (LIST '#(NIL NIL NIL NIL NIL NIL (|OpenMathEncoding|)
             (0 . |OMencodingXML|) (|String|) (|OpenMathDevice|)
             (4 . |OMopenString|) (|Void|) (10 . |OMputObject|)
             (|DoubleFloat|) (15 . |OMputFloat|)
             (21 . |OMputEndObject|) (26 . |OMclose|)
             |DFLOAT;OMwrite;$S;1| (|Boolean|) |DFLOAT;OMwrite;$BS;2|
             |DFLOAT;OMwrite;Omd$V;3| |DFLOAT;OMwrite;Omd$BV;4|
             (|PositiveInteger|) |DFLOAT;base;Pi;6| (|Integer|)
             |DFLOAT;mantissa;$I;7| |DFLOAT;exponent;$I;8|
             |DFLOAT;precision;Pi;9| |DFLOAT;log2;2$;37| (31 . *)
             |DFLOAT;bits;Pi;10| |DFLOAT;max;$;11| |DFLOAT;min;$;12|
             |DFLOAT;order;$I;13|
             (CONS IDENTITY
                   (FUNCALL (|dispatchFunction| |DFLOAT;Zero;$;14|) $))
             (CONS IDENTITY
                   (FUNCALL (|dispatchFunction| |DFLOAT;One;$;15|) $))
             |DFLOAT;exp1;$;16| |DFLOAT;pi;$;17| (|OutputForm|)
             (37 . |outputForm|) |DFLOAT;coerce;$Of;18| (|InputForm|)
             (42 . |convert|) |DFLOAT;convert;$If;19| |DFLOAT;<;2$B;20|
             |DFLOAT;-;2$;21| |DFLOAT;+;3$;22| |DFLOAT;-;3$;23|
             |DFLOAT;*;3$;24| |DFLOAT;*;I2$;25| |DFLOAT;max;3$;26|
             |DFLOAT;min;3$;27| |DFLOAT;=;2$B;28| |DFLOAT;/;$I$;29|
             |DFLOAT;sqrt;2$;30| |DFLOAT;log10;2$;31|
             |DFLOAT;**;$I$;32| |DFLOAT;**;3$;33| |DFLOAT;coerce;I$;34|
             |DFLOAT;exp;2$;35| |DFLOAT;log;2$;36| |DFLOAT;sin;2$;38|
             |DFLOAT;cos;2$;39| |DFLOAT;tan;2$;40| |DFLOAT;cot;2$;41|
             |DFLOAT;sec;2$;42| |DFLOAT;csc;2$;43| |DFLOAT;asin;2$;44|
             |DFLOAT;acos;2$;45| |DFLOAT;atan;2$;46|
             |DFLOAT;acsc;2$;47| |DFLOAT;acot;2$;48|
             |DFLOAT;asec;2$;49| |DFLOAT;sinh;2$;50|
             |DFLOAT;cosh;2$;51| |DFLOAT;tanh;2$;52|
             |DFLOAT;csch;2$;53| |DFLOAT;coth;2$;54|
             |DFLOAT;sech;2$;55| |DFLOAT;asinh;2$;56|
             |DFLOAT;acosh;2$;57| |DFLOAT;atanh;2$;58|
             |DFLOAT;acsch;2$;59| |DFLOAT;acoth;2$;60|
             |DFLOAT;asech;2$;61| |DFLOAT;/;3$;62|
             |DFLOAT;negative?;$B;63| |DFLOAT;zero?;$B;64|
             (|SingleInteger|) |DFLOAT;hash;$Si;65|
             (|Union| $ '"failed") |DFLOAT;recip;$U;66|
             |DFLOAT;differentiate;2$;67|
             (|DoubleFloatSpecialFunctions|) (47 . |Gamma|)
             |DFLOAT;Gamma;2$;68| (52 . |Beta|) |DFLOAT;Beta;3$;69|
             |DFLOAT;wholePart;$I;70| |DFLOAT;float;2IPi$;71|
             |DFLOAT;convert;2$;72| (|Float|) (58 . |convert|)
             |DFLOAT;convert;$F;73| (|Fraction| 24)
             (|NonNegativeInteger|)
             |DFLOAT;rationalApproximation;$2NniF;83|
             |DFLOAT;rationalApproximation;$NniF;74|
             |DFLOAT;atan;3$;75| |DFLOAT;retract;$F;76|
             (|Union| 104 '"failed") |DFLOAT;retractIfCan;$U;77|
             |DFLOAT;retract;$I;78| (|Union| 24 '"failed")
             |DFLOAT;retractIfCan;$U;79| |DFLOAT;sign;$I;80|
             |DFLOAT;abs;2$;81| (63 . **) (69 . |Zero|) (73 . /)
             (79 . *) (85 . |coerce|) (90 . |zero?|) (95 . |negative?|)
             (100 . |One|) (104 . =) (110 . |numer|) (115 . |denom|)
             |DFLOAT;**;$F$;84| (|PatternMatchResult| 101 $)
             (|Pattern| 101) (|Factored| $)
             (|Record| (|:| |coef1| $) (|:| |coef2| $))
             (|Union| 132 '"failed") (|List| $) (|Union| 134 '"failed")
             (|Record| (|:| |coef1| $) (|:| |coef2| $)
                 (|:| |generator| $))
             (|Record| (|:| |quotient| $) (|:| |remainder| $))
             (|SparseUnivariatePolynomial| $)
             (|Record| (|:| |coef| 134) (|:| |generator| $))
             (|Record| (|:| |unit| $) (|:| |canonical| $)
                 (|:| |associate| $)))
          '#(~= 120 |zero?| 126 |wholePart| 131 |unitNormal| 136
             |unitCanonical| 141 |unit?| 146 |truncate| 151 |tanh| 156
             |tan| 161 |subtractIfCan| 166 |squareFreePart| 172
             |squareFree| 177 |sqrt| 182 |sizeLess?| 187 |sinh| 193
             |sin| 198 |sign| 203 |sech| 208 |sec| 213 |sample| 218
             |round| 222 |retractIfCan| 227 |retract| 237 |rem| 247
             |recip| 253 |rationalApproximation| 258 |quo| 271
             |principalIdeal| 277 |prime?| 282 |precision| 287
             |positive?| 291 |pi| 296 |patternMatch| 300 |order| 307
             |one?| 312 |nthRoot| 317 |norm| 323 |negative?| 328
             |multiEuclidean| 333 |min| 339 |max| 349 |mantissa| 359
             |log2| 364 |log10| 369 |log| 374 |lcm| 379 |latex| 390
             |inv| 395 |hash| 400 |gcdPolynomial| 405 |gcd| 411
             |fractionPart| 422 |floor| 427 |float| 432 |factor| 445
             |extendedEuclidean| 450 |exquo| 463 |expressIdealMember|
             469 |exponent| 475 |exp1| 480 |exp| 484 |euclideanSize|
             489 |divide| 494 |digits| 500 |differentiate| 504 |csch|
             515 |csc| 520 |coth| 525 |cot| 530 |cosh| 535 |cos| 540
             |convert| 545 |coerce| 565 |characteristic| 595 |ceiling|
             599 |bits| 604 |base| 608 |atanh| 612 |atan| 617
             |associates?| 628 |asinh| 634 |asin| 639 |asech| 644
             |asec| 649 |acsch| 654 |acsc| 659 |acoth| 664 |acot| 669
             |acosh| 674 |acos| 679 |abs| 684 |Zero| 689 |One| 693
             |OMwrite| 697 |Gamma| 721 D 726 |Beta| 737 >= 743 > 749 =
             755 <= 761 < 767 / 773 - 785 + 796 ** 802 * 832)
          '((|approximate| . 0) (|canonicalsClosed| . 0)
            (|canonicalUnitNormal| . 0) (|noZeroDivisors| . 0)
            ((|commutative| "*") . 0) (|rightUnitary| . 0)
            (|leftUnitary| . 0) (|unitsKnown| . 0))
          (CONS (|makeByteWordVec2| 1
                    '(0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
                      0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
                      0 0 0 0 0 0))
                (CONS '#(|FloatingPointSystem&| |RealNumberSystem&|
                         |Field&| |EuclideanDomain&| NIL
                         |UniqueFactorizationDomain&| |GcdDomain&|
                         |DivisionRing&| |IntegralDomain&| |Algebra&|
                         |Algebra&| |DifferentialRing&| NIL
                         |OrderedRing&| |Module&| NIL NIL |Module&| NIL
                         NIL |Ring&| NIL NIL NIL NIL NIL NIL NIL
                         |AbelianGroup&| NIL NIL NIL |AbelianMonoid&|
                         |Monoid&| NIL |OrderedSet&|
                         |AbelianSemiGroup&| |SemiGroup&|
                         |TranscendentalFunctionCategory&| NIL
                         |SetCategory&| NIL
                         |ElementaryFunctionCategory&| NIL
                         |HyperbolicFunctionCategory&|
                         |ArcTrigonometricFunctionCategory&|
                         |TrigonometricFunctionCategory&| NIL NIL
                         |RadicalCategory&| |RetractableTo&|
                         |RetractableTo&| NIL NIL |BasicType&| NIL)
                      (CONS '#((|FloatingPointSystem|)
                               (|RealNumberSystem|) (|Field|)
                               (|EuclideanDomain|)
                               (|PrincipalIdealDomain|)
                               (|UniqueFactorizationDomain|)
                               (|GcdDomain|) (|DivisionRing|)
                               (|IntegralDomain|) (|Algebra| 104)
                               (|Algebra| $$) (|DifferentialRing|)
                               (|CharacteristicZero|) (|OrderedRing|)
                               (|Module| 104) (|EntireRing|)
                               (|CommutativeRing|) (|Module| $$)
                               (|BiModule| 104 104) (|BiModule| $$ $$)
                               (|Ring|) (|OrderedAbelianGroup|)
                               (|RightModule| 104) (|LeftModule| 104)
                               (|LeftModule| $$) (|Rng|)
                               (|RightModule| $$)
                               (|OrderedCancellationAbelianMonoid|)
                               (|AbelianGroup|)
                               (|OrderedAbelianMonoid|)
                               (|CancellationAbelianMonoid|)
                               (|OrderedAbelianSemiGroup|)
                               (|AbelianMonoid|) (|Monoid|)
                               (|PatternMatchable| 101) (|OrderedSet|)
                               (|AbelianSemiGroup|) (|SemiGroup|)
                               (|TranscendentalFunctionCategory|)
                               (|RealConstant|) (|SetCategory|)
                               (|ConvertibleTo| 41)
                               (|ElementaryFunctionCategory|)
                               (|ArcHyperbolicFunctionCategory|)
                               (|HyperbolicFunctionCategory|)
                               (|ArcTrigonometricFunctionCategory|)
                               (|TrigonometricFunctionCategory|)
                               (|OpenMath|) (|ConvertibleTo| 130)
                               (|RadicalCategory|)
                               (|RetractableTo| 104)
                               (|RetractableTo| 24)
                               (|ConvertibleTo| 101)
                               (|ConvertibleTo| 13) (|BasicType|)
                               (|CoercibleTo| 38))
                            (|makeByteWordVec2| 140
                                '(0 6 0 7 2 9 0 8 6 10 1 9 11 0 12 2 9
                                  11 0 13 14 1 9 11 0 15 1 9 11 0 16 2
                                  0 0 22 0 29 1 38 0 13 39 1 41 0 13 42
                                  1 93 13 13 94 2 93 13 13 13 96 1 101
                                  0 13 102 2 24 0 0 22 117 0 104 0 118
                                  2 104 0 24 24 119 2 24 0 105 0 120 1
                                  104 0 24 121 1 104 18 0 122 1 104 18
                                  0 123 0 104 0 124 2 104 18 0 0 125 1
                                  104 24 0 126 1 104 24 0 127 2 0 18 0
                                  0 1 1 0 18 0 87 1 0 24 0 98 1 0 140 0
                                  1 1 0 0 0 1 1 0 18 0 1 1 0 0 0 1 1 0
                                  0 0 75 1 0 0 0 63 2 0 90 0 0 1 1 0 0
                                  0 1 1 0 131 0 1 1 0 0 0 54 2 0 18 0 0
                                  1 1 0 0 0 73 1 0 0 0 61 1 0 24 0 115
                                  1 0 0 0 78 1 0 0 0 65 0 0 0 1 1 0 0 0
                                  1 1 0 110 0 111 1 0 113 0 114 1 0 104
                                  0 109 1 0 24 0 112 2 0 0 0 0 1 1 0 90
                                  0 91 2 0 104 0 105 107 3 0 104 0 105
                                  105 106 2 0 0 0 0 1 1 0 139 134 1 1 0
                                  18 0 1 0 0 22 27 1 0 18 0 1 0 0 0 37
                                  3 0 129 0 130 129 1 1 0 24 0 33 1 0
                                  18 0 1 2 0 0 0 24 1 1 0 0 0 1 1 0 18
                                  0 86 2 0 135 134 0 1 0 0 0 32 2 0 0 0
                                  0 51 0 0 0 31 2 0 0 0 0 50 1 0 24 0
                                  25 1 0 0 0 28 1 0 0 0 55 1 0 0 0 60 2
                                  0 0 0 0 1 1 0 0 134 1 1 0 8 0 1 1 0 0
                                  0 1 1 0 88 0 89 2 0 138 138 138 1 1 0
                                  0 134 1 2 0 0 0 0 1 1 0 0 0 1 1 0 0 0
                                  1 3 0 0 24 24 22 99 2 0 0 24 24 1 1 0
                                  131 0 1 3 0 133 0 0 0 1 2 0 136 0 0 1
                                  2 0 90 0 0 1 2 0 135 134 0 1 1 0 24 0
                                  26 0 0 0 36 1 0 0 0 59 1 0 105 0 1 2
                                  0 137 0 0 1 0 0 22 1 1 0 0 0 92 2 0 0
                                  0 105 1 1 0 0 0 76 1 0 0 0 66 1 0 0 0
                                  77 1 0 0 0 64 1 0 0 0 74 1 0 0 0 62 1
                                  0 41 0 43 1 0 130 0 1 1 0 101 0 103 1
                                  0 13 0 100 1 0 0 104 1 1 0 0 24 58 1
                                  0 0 104 1 1 0 0 24 58 1 0 0 0 1 1 0
                                  38 0 40 0 0 105 1 1 0 0 0 1 0 0 22 30
                                  0 0 22 23 1 0 0 0 81 2 0 0 0 0 108 1
                                  0 0 0 69 2 0 18 0 0 1 1 0 0 0 79 1 0
                                  0 0 67 1 0 0 0 84 1 0 0 0 72 1 0 0 0
                                  82 1 0 0 0 70 1 0 0 0 83 1 0 0 0 71 1
                                  0 0 0 80 1 0 0 0 68 1 0 0 0 116 0 0 0
                                  34 0 0 0 35 2 0 11 9 0 20 3 0 11 9 0
                                  18 21 1 0 8 0 17 2 0 8 0 18 19 1 0 0
                                  0 95 1 0 0 0 1 2 0 0 0 105 1 2 0 0 0
                                  0 97 2 0 18 0 0 1 2 0 18 0 0 1 2 0 18
                                  0 0 52 2 0 18 0 0 1 2 0 18 0 0 44 2 0
                                  0 0 24 53 2 0 0 0 0 85 2 0 0 0 0 47 1
                                  0 0 0 45 2 0 0 0 0 46 2 0 0 0 0 57 2
                                  0 0 0 104 128 2 0 0 0 24 56 2 0 0 0
                                  105 1 2 0 0 0 22 1 2 0 0 104 0 1 2 0
                                  0 0 104 1 2 0 0 0 0 48 2 0 0 24 0 49
                                  2 0 0 105 0 1 2 0 0 22 0 29)))))
          '|lookupComplete|)) 

(SETQ |$CategoryFrame|
      (|put| '|DoubleFloat| '|isFunctor|
             '(((|rationalApproximation|
                    ((|Fraction| (|Integer|)) $ (|NonNegativeInteger|)
                     (|NonNegativeInteger|)))
                T (ELT $ 106))
               ((|rationalApproximation|
                    ((|Fraction| (|Integer|)) $ (|NonNegativeInteger|)))
                T (ELT $ 107))
               ((|Beta| ($ $ $)) T (ELT $ 97))
               ((|Gamma| ($ $)) T (ELT $ 95))
               ((|atan| ($ $ $)) T (ELT $ 108))
               ((|log10| ($ $)) T (ELT $ 55))
               ((|log2| ($ $)) T (ELT $ 28))
               ((|exp1| ($)) T (ELT $ 36))
               ((/ ($ $ (|Integer|))) T (ELT $ 53))
               ((|convert| ((|InputForm|) $)) T (ELT $ 43))
               ((|tan| ($ $)) T (ELT $ 63))
               ((|sin| ($ $)) T (ELT $ 61))
               ((|sec| ($ $)) T (ELT $ 65))
               ((|csc| ($ $)) T (ELT $ 66))
               ((|cot| ($ $)) T (ELT $ 64))
               ((|cos| ($ $)) T (ELT $ 62))
               ((|acos| ($ $)) T (ELT $ 68))
               ((|acot| ($ $)) T (ELT $ 71))
               ((|acsc| ($ $)) T (ELT $ 70))
               ((|asec| ($ $)) T (ELT $ 72))
               ((|asin| ($ $)) T (ELT $ 67))
               ((|atan| ($ $)) T (ELT $ 69))
               ((|cosh| ($ $)) T (ELT $ 74))
               ((|coth| ($ $)) T (ELT $ 77))
               ((|csch| ($ $)) T (ELT $ 76))
               ((|sech| ($ $)) T (ELT $ 78))
               ((|sinh| ($ $)) T (ELT $ 73))
               ((|tanh| ($ $)) T (ELT $ 75))
               ((|acosh| ($ $)) T (ELT $ 80))
               ((|acoth| ($ $)) T (ELT $ 83))
               ((|acsch| ($ $)) T (ELT $ 82))
               ((|asech| ($ $)) T (ELT $ 84))
               ((|asinh| ($ $)) T (ELT $ 79))
               ((|atanh| ($ $)) T (ELT $ 81))
               ((|log| ($ $)) T (ELT $ 60))
               ((|exp| ($ $)) T (ELT $ 59)) ((** ($ $ $)) T (ELT $ 57))
               ((|pi| ($)) T (ELT $ 37))
               ((|OMwrite| ((|Void|) (|OpenMathDevice|) $ (|Boolean|)))
                T (ELT $ 21))
               ((|OMwrite| ((|Void|) (|OpenMathDevice|) $)) T
                (ELT $ 20))
               ((|OMwrite| ((|String|) $ (|Boolean|))) T (ELT $ 19))
               ((|OMwrite| ((|String|) $)) T (ELT $ 17))
               ((|differentiate| ($ $)) T (ELT $ 92))
               ((D ($ $)) T (ELT $ NIL))
               ((|differentiate| ($ $ (|NonNegativeInteger|))) T
                (ELT $ NIL))
               ((D ($ $ (|NonNegativeInteger|))) T (ELT $ NIL))
               ((|max| ($))
                (AND (|not| (|has| $ (ATTRIBUTE |arbitraryExponent|)))
                     (|not| (|has| $ (ATTRIBUTE |arbitraryPrecision|))))
                (ELT $ 31))
               ((|min| ($))
                (AND (|not| (|has| $ (ATTRIBUTE |arbitraryExponent|)))
                     (|not| (|has| $ (ATTRIBUTE |arbitraryPrecision|))))
                (ELT $ 32))
               ((|decreasePrecision| ((|PositiveInteger|) (|Integer|)))
                (|has| $ (ATTRIBUTE |arbitraryPrecision|)) (ELT $ NIL))
               ((|increasePrecision| ((|PositiveInteger|) (|Integer|)))
                (|has| $ (ATTRIBUTE |arbitraryPrecision|)) (ELT $ NIL))
               ((|precision| ((|PositiveInteger|) (|PositiveInteger|)))
                (|has| $ (ATTRIBUTE |arbitraryPrecision|)) (ELT $ NIL))
               ((|digits| ((|PositiveInteger|) (|PositiveInteger|)))
                (|has| $ (ATTRIBUTE |arbitraryPrecision|)) (ELT $ NIL))
               ((|bits| ((|PositiveInteger|) (|PositiveInteger|)))
                (|has| $ (ATTRIBUTE |arbitraryPrecision|)) (ELT $ NIL))
               ((|precision| ((|PositiveInteger|))) T (ELT $ 27))
               ((|digits| ((|PositiveInteger|))) T (ELT $ NIL))
               ((|bits| ((|PositiveInteger|))) T (ELT $ 30))
               ((|mantissa| ((|Integer|) $)) T (ELT $ 25))
               ((|exponent| ((|Integer|) $)) T (ELT $ 26))
               ((|base| ((|PositiveInteger|))) T (ELT $ 23))
               ((|order| ((|Integer|) $)) T (ELT $ 33))
               ((|float| ($ (|Integer|) (|Integer|)
                            (|PositiveInteger|)))
                T (ELT $ 99))
               ((|float| ($ (|Integer|) (|Integer|))) T (ELT $ NIL))
               ((|round| ($ $)) T (ELT $ NIL))
               ((|truncate| ($ $)) T (ELT $ NIL))
               ((|fractionPart| ($ $)) T (ELT $ NIL))
               ((|wholePart| ((|Integer|) $)) T (ELT $ 98))
               ((|floor| ($ $)) T (ELT $ NIL))
               ((|ceiling| ($ $)) T (ELT $ NIL))
               ((|norm| ($ $)) T (ELT $ NIL))
               ((|patternMatch|
                    ((|PatternMatchResult| (|Float|) $) $
                     (|Pattern| (|Float|))
                     (|PatternMatchResult| (|Float|) $)))
                T (ELT $ NIL))
               ((|convert| ((|Pattern| (|Float|)) $)) T (ELT $ NIL))
               ((** ($ $ (|Fraction| (|Integer|)))) T (ELT $ 128))
               ((|nthRoot| ($ $ (|Integer|))) T (ELT $ NIL))
               ((|sqrt| ($ $)) T (ELT $ 54))
               ((|retract| ((|Fraction| (|Integer|)) $)) T (ELT $ 109))
               ((|retractIfCan|
                    ((|Union| (|Fraction| (|Integer|)) "failed") $))
                T (ELT $ 111))
               ((|coerce| ($ (|Fraction| (|Integer|)))) T (ELT $ NIL))
               ((|retract| ((|Integer|) $)) T (ELT $ 112))
               ((|retractIfCan| ((|Union| (|Integer|) "failed") $)) T
                (ELT $ 114))
               ((|coerce| ($ (|Integer|))) T (ELT $ 58))
               ((|convert| ((|DoubleFloat|) $)) T (ELT $ 100))
               ((|convert| ((|Float|) $)) T (ELT $ 103))
               ((< ((|Boolean|) $ $)) T (ELT $ 44))
               ((> ((|Boolean|) $ $)) T (ELT $ NIL))
               ((>= ((|Boolean|) $ $)) T (ELT $ NIL))
               ((<= ((|Boolean|) $ $)) T (ELT $ NIL))
               ((|max| ($ $ $)) T (ELT $ 50))
               ((|min| ($ $ $)) T (ELT $ 51))
               ((|positive?| ((|Boolean|) $)) T (ELT $ NIL))
               ((|negative?| ((|Boolean|) $)) T (ELT $ 86))
               ((|sign| ((|Integer|) $)) T (ELT $ 115))
               ((|abs| ($ $)) T (ELT $ 116)) ((/ ($ $ $)) T (ELT $ 85))
               ((|coerce| ($ (|Fraction| (|Integer|)))) T (ELT $ NIL))
               ((* ($ (|Fraction| (|Integer|)) $)) T (ELT $ NIL))
               ((* ($ $ (|Fraction| (|Integer|)))) T (ELT $ NIL))
               ((** ($ $ (|Integer|))) T (ELT $ 56))
               ((|inv| ($ $)) T (ELT $ NIL))
               ((|prime?| ((|Boolean|) $)) T (ELT $ NIL))
               ((|squareFree| ((|Factored| $) $)) T (ELT $ NIL))
               ((|squareFreePart| ($ $)) T (ELT $ NIL))
               ((|factor| ((|Factored| $) $)) T (ELT $ NIL))
               ((|multiEuclidean|
                    ((|Union| (|List| $) "failed") (|List| $) $))
                T (ELT $ NIL))
               ((|extendedEuclidean|
                    ((|Union| (|Record| (|:| |coef1| $)
                                  (|:| |coef2| $))
                              "failed")
                     $ $ $))
                T (ELT $ NIL))
               ((|extendedEuclidean|
                    ((|Record| (|:| |coef1| $) (|:| |coef2| $)
                         (|:| |generator| $))
                     $ $))
                T (ELT $ NIL))
               ((|rem| ($ $ $)) T (ELT $ NIL))
               ((|quo| ($ $ $)) T (ELT $ NIL))
               ((|divide|
                    ((|Record| (|:| |quotient| $) (|:| |remainder| $))
                     $ $))
                T (ELT $ NIL))
               ((|euclideanSize| ((|NonNegativeInteger|) $)) T
                (ELT $ NIL))
               ((|sizeLess?| ((|Boolean|) $ $)) T (ELT $ NIL))
               ((|expressIdealMember|
                    ((|Union| (|List| $) "failed") (|List| $) $))
                T (ELT $ NIL))
               ((|principalIdeal|
                    ((|Record| (|:| |coef| (|List| $))
                         (|:| |generator| $))
                     (|List| $)))
                T (ELT $ NIL))
               ((|gcdPolynomial|
                    ((|SparseUnivariatePolynomial| $)
                     (|SparseUnivariatePolynomial| $)
                     (|SparseUnivariatePolynomial| $)))
                T (ELT $ NIL))
               ((|lcm| ($ (|List| $))) T (ELT $ NIL))
               ((|lcm| ($ $ $)) T (ELT $ NIL))
               ((|gcd| ($ (|List| $))) T (ELT $ NIL))
               ((|gcd| ($ $ $)) T (ELT $ NIL))
               ((|unit?| ((|Boolean|) $)) T (ELT $ NIL))
               ((|associates?| ((|Boolean|) $ $)) T (ELT $ NIL))
               ((|unitCanonical| ($ $)) T (ELT $ NIL))
               ((|unitNormal|
                    ((|Record| (|:| |unit| $) (|:| |canonical| $)
                         (|:| |associate| $))
                     $))
                T (ELT $ NIL))
               ((|exquo| ((|Union| $ "failed") $ $)) T (ELT $ NIL))
               ((|coerce| ($ $)) T (ELT $ NIL))
               ((|coerce| ($ (|Integer|))) T (ELT $ 58))
               ((|characteristic| ((|NonNegativeInteger|))) T
                (ELT $ NIL))
               ((|One| ($)) T (CONST $ 35))
               ((|one?| ((|Boolean|) $)) T (ELT $ NIL))
               ((** ($ $ (|NonNegativeInteger|))) T (ELT $ NIL))
               ((|recip| ((|Union| $ "failed") $)) T (ELT $ 91))
               ((* ($ $ $)) T (ELT $ 48))
               ((** ($ $ (|PositiveInteger|))) T (ELT $ NIL))
               ((* ($ (|Integer|) $)) T (ELT $ 49))
               ((- ($ $ $)) T (ELT $ 47)) ((- ($ $)) T (ELT $ 45))
               ((|subtractIfCan| ((|Union| $ "failed") $ $)) T
                (ELT $ NIL))
               ((* ($ (|NonNegativeInteger|) $)) T (ELT $ NIL))
               ((|zero?| ((|Boolean|) $)) T (ELT $ 87))
               ((|sample| ($)) T (CONST $ NIL))
               ((|Zero| ($)) T (CONST $ 34))
               ((* ($ (|PositiveInteger|) $)) T (ELT $ 29))
               ((+ ($ $ $)) T (ELT $ 46))
               ((|latex| ((|String|) $)) T (ELT $ NIL))
               ((|hash| ((|SingleInteger|) $)) T (ELT $ 89))
               ((|coerce| ((|OutputForm|) $)) T (ELT $ 40))
               ((= ((|Boolean|) $ $)) T (ELT $ 52))
               ((~= ((|Boolean|) $ $)) T (ELT $ NIL)))
             (|addModemap| '|DoubleFloat| '(|DoubleFloat|)
                 '((|Join| (|FloatingPointSystem|) (|DifferentialRing|)
                           (|OpenMath|)
                           (|TranscendentalFunctionCategory|)
                           (|ConvertibleTo| (|InputForm|))
                           (CATEGORY |domain|
                               (SIGNATURE / ($ $ (|Integer|)))
                               (SIGNATURE ** ($ $ $))
                               (SIGNATURE |exp1| ($))
                               (SIGNATURE |log2| ($ $))
                               (SIGNATURE |log10| ($ $))
                               (SIGNATURE |atan| ($ $ $))
                               (SIGNATURE |Gamma| ($ $))
                               (SIGNATURE |Beta| ($ $ $))
                               (SIGNATURE |rationalApproximation|
                                   ((|Fraction| (|Integer|)) $
                                    (|NonNegativeInteger|)))
                               (SIGNATURE |rationalApproximation|
                                   ((|Fraction| (|Integer|)) $
                                    (|NonNegativeInteger|)
                                    (|NonNegativeInteger|))))))
                 T '|DoubleFloat|
                 (|put| '|DoubleFloat| '|mode|
                        '(|Mapping|
                             (|Join| (|FloatingPointSystem|)
                                     (|DifferentialRing|) (|OpenMath|)
                                     (|TranscendentalFunctionCategory|)
                                     (|ConvertibleTo| (|InputForm|))
                                     (CATEGORY |domain|
                                      (SIGNATURE / ($ $ (|Integer|)))
                                      (SIGNATURE ** ($ $ $))
                                      (SIGNATURE |exp1| ($))
                                      (SIGNATURE |log2| ($ $))
                                      (SIGNATURE |log10| ($ $))
                                      (SIGNATURE |atan| ($ $ $))
                                      (SIGNATURE |Gamma| ($ $))
                                      (SIGNATURE |Beta| ($ $ $))
                                      (SIGNATURE
                                       |rationalApproximation|
                                       ((|Fraction| (|Integer|)) $
                                        (|NonNegativeInteger|)))
                                      (SIGNATURE
                                       |rationalApproximation|
                                       ((|Fraction| (|Integer|)) $
                                        (|NonNegativeInteger|)
                                        (|NonNegativeInteger|))))))
                        |$CategoryFrame|)))) 

(MAKEPROP '|DoubleFloat| 'NILADIC T) 
