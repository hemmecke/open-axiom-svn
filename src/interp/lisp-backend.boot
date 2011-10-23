-- Copyright (C) 2011, Gabriel Dos Reis.
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

--%
--% The purpose of this module is to implement the Lisp backend
--% of the OpenAxiom platform.  This is achieved by necessary 
--% collection runtime datatypes and Common Lisp code generation
--% routines.
--%

import sys_-macros
namespace BOOT

module lisp_-backend where
  expandToVMForm: %Thing -> %Thing
  eval: %Thing -> %Thing


--%
--% Iteration control structures
--%
--% Code generation for an iterator produces a sequence of 
--% length 5, whose components have the following meanings:
--%  0. list of loop-wide variables and their initializers
--%  1. list of body-wide variables and their initializers
--%  2. update code for next iteration
--%  3. predicate guarding loop body execution
--%  4. loop termination predicate

++ Dummy free var name.
$freeVarName == KEYWORD::freeVar

loopVarInit(x,y) ==
  x is ['%free,:id] => [id,[$freeVarName,middleEndExpand ['%LET,id,y]]]
  if x is ['%local,:.] then
    x := x.rest
  [x,[x,middleEndExpand y]]

++ Generate code that sequentially visits each component of a list.
expandIN(x,l,early?) ==
  g := gensym()           -- rest of the list yet to be visited
  early? =>               -- give the loop variable a wider scope.
    [x,init] := loopVarInit(x,'%nil)
    [[[g,middleEndExpand l],init],
      nil,[['SETQ,g,['CDR,g]]],
        nil,[['NOT,['CONSP,g]],['PROGN,['SETQ,x,['CAR,g]],'NIL]]]
  [x,init] := loopVarInit(x,['%head,g])
  [[[g,middleEndExpand l]],
    [init],[['SETQ,g,['CDR,g]]],
      nil,[['NOT,['CONSP,g]]]]

expandON(x,l) ==
  [x,init] := loopVarInit(x,l)
  [[init],nil,[["SETQ",x,["CDR",x]]],nil,[["ATOM",x]]]
  
++ Generate code that traverses an interval with lower bound 'lo',
++ arithmetic progression `step, and possible upper bound `final'.
expandSTEP(id,lo,step,final)==
  step := middleEndExpand step
  final := middleEndExpand final
  [id,init] := loopVarInit(id,lo)
  loopvar := [init]
  inc :=
    atomic? step => step
    g1 := gensym()
    loopvar := [:loopvar,[g1,step]]
    g1
  final :=
    final isnt [.,:.] => final
    final is [hi] and atomic? hi => hi
    g2 := gensym()
    loopvar := [:loopvar,[g2,:final]]
    g2
  ex :=
     final = nil => nil
     integer? inc =>
       pred :=
	 inc < 0 => "<"
	 ">"
       [[pred,id,final]]
     [['COND,[['MINUSP,inc],
	   ["<",id,final]],['T,[">",id,final]]]]
  suc := [["SETQ",id,["+",id,inc]]]
  [loopvar,nil,suc,nil,ex]

++ Generate code for iterators that filter out execution state
++ not satisfying predicate `p'.
expandSUCHTHAT p == 
  [nil,nil,nil,[middleEndExpand p],nil]

++ Generate code for iterators that stop loop iteration when the
++ state fails predicate `p'.
expandWHILE p == 
  [nil,nil,nil,nil,[["NOT",middleEndExpand p]]]

expandUNTIL p ==
  g := gensym()
  [[[g,false]],nil,[["SETQ",g,middleEndExpand p]],nil,[g]]

expandInit(var,val) ==
  [[[var,middleEndExpand val]],nil,nil,nil,nil]

expandIterators iters ==
  -- Exit predicates may reference iterator variables.  In that case,
  -- the scope the variables must cover the generated loop body.  The
  -- following is much more coarse approximation than we may want,
  -- but it will do.  For now.
  early? := or/[ it.op in '(WHILE UNTIL) for it in iters]
  [toLisp(it,early?) or leave "failed" for it in iters] where
     toLisp(it,early?) ==
       it is ["STEP",var,lo,inc,:hi] => expandSTEP(var,lo,inc,hi)
       it is ["IN",var,seq] => expandIN(var,seq,early?)
       it is ["ON",var,seq] => expandON(var,seq)
       it is ["WHILE",pred] => expandWHILE pred
       it is [op,pred] and op in '(SUCHTHAT _|) => expandSUCHTHAT pred
       it is ["UNTIL",pred] => expandUNTIL pred
       it is ["%init",var,val] => expandInit(var,val)
       nil

massageFreeVarInits(body,inits) ==
  inits = nil => body
  inits is [[var,init]] and sameObject?(var,$freeVarName) =>
    ['SEQ,init,['EXIT,body]]
  for init in inits repeat
    sameObject?(init.first,$freeVarName) =>
      init.first := gensym()
  ['LET,inits,body]


expandLoop ['%loop,:iters,body,ret] ==
  itersCode := expandIterators iters
  itersCode = "failed" => systemErrorHere ["expandLoop",iters]
  body := middleEndExpand body
  itersCode := "coagulate"/itersCode
    where
      coagulate(it1,it2) == [append(it1.k,it2.k) for k in 0..4]
  [loopInits,bodyInits,cont,filters,exits] := itersCode
  -- Guard the execution of the body by the filters.
  if filters ~= nil then
    body := mkpf([:filters,body],"AND")
  -- If there is any body-wide initialization, now is the time.
  body := massageFreeVarInits(body,bodyInits)
  exits := ["COND",
             [mkpf(exits,"OR"),["RETURN",expandToVMForm ret]],
               [true,body]]
  body := ["LOOP",exits,:cont]
  -- Finally, set up loop-wide initializations.
  massageFreeVarInits(body,loopInits)

++ Generate code for list comprehension.
expandCollect ['%collect,:iters,body] ==
  val := gensym()    -- result of the list comprehension
  -- Transform the body to build the list as we go.
  body := ['%store,val,['%pair,body,val]]
  -- Initialize the variable holding the result; expand as 
  -- if ordinary loop.  But don't forget we built the result
  -- in reverse order.
  expandLoop ['%loop,:iters,["%init",val,nil],body,['%lreverse!,val]]

expandList(x is ['%list,:args]) ==
  args := [expandToVMForm arg for arg in args]
  args = nil => nil
  args' := [simpleValue? arg or leave 'failed for arg in args]
     where simpleValue? arg ==
             integer? arg or string? arg => arg
             arg is ['QUOTE,form] => form
             nil
  args' = 'failed => ['LIST,:args]
  quote args'

expandReturn(x is ['%return,.,y]) ==
  $FUNNAME = nil => systemErrorHere ['expandReturn,x]
  ['RETURN_-FROM,$FUNNAME,expandToVMForm y]
  
-- Pointer operations
expandPeq ['%peq,x,y] ==
  x = '%nil => ['NULL,expandToVMForm y]
  y = '%nil => ['NULL,expandToVMForm x]
  ['EQ,expandToVMForm x, expandToVMForm y]

expandBefore? ['%before?,x,y] ==
  ['GGREATERP,expandToVMForm y,expandToVMForm x]

-- Byte operations
expandBcompl ['%bcompl,x] ==
  integer? x => 255 - x
  ['_+,256,['LOGNOT,expandToVMForm x]]

-- Character operations
expandCcst ['%ccst,s] ==
  -- FIXME: this expander should return forms, instead of character constants
  not string? s => error "operand is not a string constant"
  stringToChar s

++ string-to-character conversion.  
expandS2c ['%s2c, x] ==
  string? x => expandCcst ['%ccst, x]
  ['stringToChar, x]

-- Integer operations
expandIneg ['%ineg,x] ==
  x := expandToVMForm x
  integer? x => -x
  ['_-,x]

expandIdivide ['%idivide,x,y] ==
  ['MULTIPLE_-VALUE_-CALL,['FUNCTION,'CONS],
    ['TRUNCATE,expandToVMForm x,expandToVMForm y]]

expandIeq ['%ieq,a,b] ==
  a := expandToVMForm a
  integer? a and a = 0 => ['ZEROP,expandToVMForm b]
  b := expandToVMForm b
  integer? b and b = 0 => ['ZEROP,a]
  ['EQL,a,b]

expandIlt ['%ilt,x,y] ==
  integer? x and x = 0 =>
    integer? y => y > 0
    ['PLUSP,expandToVMForm y]
  integer? y and y = 0 =>
    integer? x => x < 0
    ['MINUSP,expandToVMForm x]
  ['_<,expandToVMForm x,expandToVMForm y]

expandIgt ['%igt,x,y] ==
  expandIlt ['%ilt,y,x]

-- Floating point support

expandFbase ['%fbase] ==
  FLOAT_-RADIX $DoubleFloatMaximum

expandFprec ['%fprec] ==
  FLOAT_-DIGITS $DoubleFloatMaximum

expandFminval ['%fminval] ==
  '$DoubleFloatMinimum

expandFmaxval ['%fmaxval] ==
  '$DoubleFloatMaximum

expandI2f ['%i2f,x] ==
  x := expandToVMForm x
  integer? x and (x = 0 or x = 1) => FLOAT(x,$DoubleFloatMaximum)
  ['FLOAT,x,'$DoubleFloatMaximum]

expandFneg ['%fneg,x] ==
  ['_-,expandToVMForm x]

expandFeq ['%feq,a,b] ==
  a is ['%i2f,0] => ['ZEROP,expandToVMForm b]
  b is ['%i2f,0] => ['ZEROP,expandToVMForm a]
  ['_=,expandToVMForm a,expandToVMForm b]

expandFlt ['%flt,x,y] ==
  x is ['%i2f,0] => ['PLUSP,expandToVMForm y]
  y is ['%i2f,0] => ['MINUSP,expandToVMForm x]
  ['_<,expandToVMForm x,expandToVMForm y]

expandFgt ['%fgt,x,y] ==
  expandFlt ['%flt,y,x]

expandFcstpi ['%fcstpi] ==
  ['COERCE,'PI,quote '%DoubleFloat]

expandFsqrt ['%fsqrt,x] ==
  ['C_-TO_-R,['SQRT,expandToVMForm x]]

expandFpowf ['%fpowf,x,y] ==
  ['C_-TO_-R,['EXPT,expandToVMForm x,expandToVMForm y]]

expandFlog ['%flog,x] ==
  ['C_-TO_-R,['LOG,expandToVMForm x]]

expandFlog2 ['%flog2,x] ==
  ['C_-TO_-R,['LOG,expandToVMForm x,2]]

expandFlog10 ['%flog10,x] ==
  ['C_-TO_-R,['LOG,expandToVMForm x,10]]

expandFasin ['%fasin,x] ==
  ['C_-TO_-R,['ASIN,expandToVMForm x]]

expandFacos ['%facos,x] ==
  ['C_-TO_-R,['ACOS,expandToVMForm x]]

expandFacosh ['%facosh,x] ==
  ['C_-TO_-R,['ACOSH,expandToVMForm x]]

expandFatanh ['%fatanh,x] ==
  ['C_-TO_-R,['ATANH,expandToVMForm x]]

expandFacoth ['%facoth,x] ==
  ['C_-TO_-R,['ACOTH,expandToVMForm x]]

expandFdecode ['%fdecode,x] ==
  ['MULTIPLE_-VALUE_-CALL,['FUNCTION,'LIST],
    ['INTEGER_-DECODE_-FLOAT,expandToVMForm x]]

-- String operations

++ string equality comparison
expandStreq ['%streq,x,y] ==
  expandToVMForm ['%not,['%peq,['STRING_=,x,y],'%nil]]

++ string lexicographic comparison  
expandStrlt ['%strlt,x,y] ==
  expandToVMForm ['%not,['%peq,['STRING_<,x,y],'%nil]]

++ deposit a character `z' at slot number `y' in string object `x'.  
expandStrstc ['%strstc,x,y,z] ==
  expandToVMForm ['%store,['%schar,x,y],z]

expandBytevec2str ['%bytevec2str,x] ==
  ['MAP,quote 'STRING, --FIXME: should be '%String, fix when SBCL is fixed.
    ['FUNCTION,['LAMBDA,['c],['CODE_-CHAR,'c]]],expandToVMForm x]

expandStr2bytevec ['%str2bytevec,x] ==
  ['MAP,quote ['%Vector,'%Byte],
    ['FUNCTION,['LAMBDA,['c],['CHAR_-CODE,'c]]],expandToVMForm x]

-- bit vector operations
expandBitvecnot ['%bitvecnot,x] ==
  ['BIT_-NOT,expandToVMForm x]

expandBitvecand ['%bitvecand,x,y] ==
  ['BIT_-AND,expandToVMForm x,expandToVMForm y]

expandBitvecnand ['%bitvecnand,x,y] ==
  ['BIT_-NAND,expandToVMForm x,expandToVMForm y]

expandBitvecor ['%bitvecor,x,y] ==
  ['BIT_-IOR,expandToVMForm x,expandToVMForm y]

expandBitvecnor ['%bitvecnor,x,y] ==
  ['BIT_-NOR,expandToVMForm x,expandToVMForm y]

expandBitvecxor ['%bitvecxor,x,y] ==
  ['BIT_-XOR,expandToVMForm x,expandToVMForm y]

expandBitveclength ['%bitveclength,x] ==
  ['LENGTH,expandToVMForm x]

expandBitveccopy ['%bitveccopy,x] ==
  ['COPY_-SEQ,expandToVMForm x]

expandBitvecconc ['%bitvecconc,x,y] ==
  ['CONCATENATE, quote '%BitVector,expandToVMForm x,expandToVMForm y]

expandBitvecref ['%bitvecref,x,y] ==
  ['SBIT,expandToVMForm x,expandToVMForm y]

expandBitveceq ['%bitveceq,x,y] ==
  ['EQUAL,expandToVMForm x,expandToVMForm y]

expandBitveclt ['%bitveclt,x,y] ==
  ['BVEC_-GREATER,expandToVMForm y,expandToVMForm x]

expandBitvector ['%bitvector,x,y] ==
  ['MAKE_-ARRAY,['LIST,expandToVMForm x],
     KEYWORD::ELEMENT_-TYPE,quote '%Bit,
       KEYWORD::INITIAL_-ELEMENT,expandToVMForm y]

--% complex number conversions
--% An OpenAxiom complex number is a pair (real and imaginary parts.)

-- convert an OpenAxiom complex number to a Lisp complex number
expandVal2z ['%val2z,x] ==
  cons? x =>
    g := gensym()
    expandToVMForm ['%bind,[[g,x]],['%zlit,['%head,g],['%tail,g]]]
  expandToVMForm ['%zlit,['%head,x],['%tail,x]]

-- convert a Lisp complex number to an OpenAxiom complex number
expandZ2val ['%z2val,x] ==
  cons? x =>
    g := gensym()
    expandToVMForm ['%bind,[[g,x]],['%pair,['%zreal,g],['%zimag,g]]]
  expandToVMForm ['%pair,['%zreal,x],['%zimag,x]]
  

-- Local variable bindings
expandBind ['%bind,inits,:body] ==
  body := expandToVMForm body
  inits := [[first x,expandToVMForm second x] for x in inits]
  -- FIXME: we should consider turning LET* into LET or direct inlining.
  op :=
    or/[CONTAINED(v,x) for [[v,.],:x] in tails inits] => 'LET_*
    'LET
  [op,inits,:body]

-- Memory load/store

expandDynval ["%dynval",:args] ==
  ["SYMBOL-VALUE",:expandToVMForm args]

expandStore ["%store",place,value] ==
  value := expandToVMForm value
  place is ['%head,x] => ['RPLACA,expandToVMForm x,value]
  place is ['%tail,x] => ['RPLACD,expandToVMForm x,value]
  place := expandToVMForm place
  cons? place => ["SETF",place,value]
  ["SETQ",place,value]

-- non-local control transfer

$OpenAxiomCatchTag == KEYWORD::OpenAxiomCatchPoint

expandThrow ['%throw,m,x] ==
  ['THROW,$OpenAxiomCatchTag,
    ['CONS,$OpenAxiomCatchTag,
      ['CONS,expandToVMForm m,expandToVMForm x]]]

++ Subroutine of expandTry.  Generate code for domain matching
++ of object `obj' with domain `dom'.
domainMatchCode(dom,obj) ==
  -- FIXME: Instead of domain equality, we should also consider
  -- FIXME: cases of sub-domains, or domain schemes with constraints.
  ['domainEqual,dom,['%head,obj]]

expandTry ['%try,expr,handlers,cleanup] ==
  g := gensym()              -- hold the exception object
  ys := [[domainMatchCode(mode,['%tail,g]),
          ['%bind,[[var,['%tail,['%tail,g]]]],stmt]]
            for [.,var,mode,stmt] in handlers]
  handlerBody :=
    ys = nil => g
    ys := [:ys,['%otherwise,['THROW,$OpenAxiomCatchTag,g]]]
    ['%when,
      [['%and,['%pair?,g],
        ['%peq,['%head,g],$OpenAxiomCatchTag]], ['%when,:ys]],
          ['%otherwise,g]]
  tryBlock := expandBind
    ['%bind,[[g,['CATCH,$OpenAxiomCatchTag,expr]]],handlerBody]
  cleanup = nil => tryBlock
  ['UNWIND_-PROTECT,tryBlock,:expandToVMForm rest cleanup]
  
expandApply ['%apply,op,:args] ==
  ['SPADCALL,:args,op]

++ Opcodes with direct mapping to target operations.
for x in [
    -- Boolean constants
    -- ['%false, :'NIL],
    ['%true,  :'T],
    -- unary Boolean operations
    ['%not,   :'NOT],
    -- binary Boolean operations
    ['%and,   :'AND],
    ['%or,    :'OR],

    -- character operations
    ['%ccstmax,:'_$CharCodeMaximum],
    ['%ceq,    :'CHAR_=],
    ['%clt,    :'CHAR_<],
    ['%cle,    :'CHAR_<_=],
    ['%cgt,    :'CHAR_>],
    ['%cge,    :'CHAR_>_=],
    ['%cup,    :'CHAR_-UPCASE],
    ['%cdown,  :'CHAR_-DOWNCASE],
    ['%c2i,    :'CHAR_-CODE],
    ['%i2c,    :'CODE_-CHAR],
    ['%c2s,    :'STRING],

    -- byte operations
    ['%beq, :'byteEqual],
    ['%blt, :'byteLessThan],

    -- integer constants
    ['%icst0,   :0],
    ['%icst1,   :1],
    ['%icstmin, :'_$ShortMinimum],
    ['%icstmax, :'_$ShortMaximum],
    -- unary integer operations.
    ['%iabs,    :'ABS],
    ['%ieven?,  :'EVENP],
    ['%integer?,:'INTEGERP],
    ['%iodd?,   :'ODDP],
    ['%ismall?, :'SMINTP],
    ['%i2s,   :'WRITE_-TO_-STRING],
    ['%ilength, :'INTEGER_-LENGTH],
    ['%ibit,    :'INTEGER_-BIT],
    ['%irandom, :'RANDOM],
    -- binary integer operations.
    ['%iadd,    :"+"],
    ['%igcd,    :'GCD],
    ['%ige,     :">="],
    ['%iinc,    :"1+"],
    ['%idec,    :"1-"],
    ['%ilcm,    :'LCM],
    ['%ile,     :"<="],
    ['%imax,    :'MAX],
    ['%imin,    :'MIN],
    ['%imul,    :"*"],
    ['%imulf,   :"*"],                   -- integer * float
    ['%irem,    :'REM],
    ['%iquo,    :'TRUNCATE],
    ['%ipow,    :'EXPT],
    ['%isub,    :"-"],
    ['%bitand,  :'LOGAND],
    ['%bitior,  :'LOGIOR],
    ['%bitxor,  :'LOGXOR],
    ['%bitnot,  :'LOGNOT],

    -- unary float operations.
    ['%fabs,  :'ABS],
    ['%float?,:'FLOATP],
    ['%ftrunc,:'TRUNCATE],
    -- binary float operations.
    ['%fadd,  :"+"],
    ['%fdiv,  :"/"],
    ['%fdivi, :"/"],                     -- float / integer
    ['%fge,   :">="],
    ['%fle,   :"<="],
    ['%fmax,  :'MAX],
    ['%fmin,  :'MIN],
    ['%fmul,  :"*"],
    ['%fpowi, :'EXPT],
    ['%fsub,  :"-"],
    ['%fmanexp, :'MANEXP],               -- (mantissa, exponent) pair.

    ['%fexp,   :'EXP],
    ['%fsin,   :'SIN],
    ['%fcos,   :'COS],
    ['%ftan,   :'TAN],
    ['%fcot,   :'COT],
    ['%fatan,  :'ATAN],
    ['%facot,  :'ACOT],
    ['%fsinh,  :'SINH],
    ['%fcosh,  :'COSH],
    ['%ftanh,  :'TANH],
    ['%fasinh, :'ASINH],

    -- complex number operations
    ['%zlit,     :'COMPLEX],
    ['%zreal,    :'REALPART],
    ['%zimag,    :'IMAGPART],
    ['%zexp,     :'EXP],
    ['%zlog,     :'LOG],
    ['%zsin,     :'SIN],
    ['%zcos,     :'COS],
    ['%ztan,     :'TAN],
    ['%zasin,    :'ASIN],
    ['%zacos,    :'ACOS],
    ['%zatan,    :'ATAN],
    ['%zsinh,    :'SINH],
    ['%zcosh,    :'COSH],
    ['%ztanh,    :'TANH],
    ['%zasinh,   :'ASINH],
    ['%zacosh,   :'ACOSH],
    ['%zatanh,   :'ATANH],

    -- string operations
    ['%f2s,   :'DFLOAT_-FORMAT_-GENERAL],

    -- list contants
    -- ['%nil, :'NIL],
    -- unary list operations
    ['%head,      :'CAR],
    ['%pair,  :'CONS],
    ['%lempty?,   :'NULL],
    ['%lfirst,    :'CAR],
    ['%llength,   :'LIST_-LENGTH],
    ['%lreverse,  :'reverse],
    ['%lreverse!, :'reverse!],
    ['%lsecond,   :'CADR],
    ['%lthird,    :'CADDR],
    ['%pair?,     :'CONSP],
    ['%tail,      :'CDR],
    -- binary list operations
    ['%lconcat,   :'APPEND],

    -- simple vector operations
    ['%vfill,     :'FILL],
    ['%vlength,   :'sizeOfSimpleArray],
    ['%vector,    :'VECTOR],
    ['%vref,      :'SVREF],
    ['%aref,      :'getSimpleArrayEntry],
    ['%makevector,:'MAKE_-ARRAY],
    ['%vcopy,     :'COPY_-SEQ],

    -- symbol unary functions
    ['%gensym,  :'GENSYM],
    ['%sname,   :'SYMBOL_-NAME],
    ['%ident?,  :'ident?],
    ['%property,:'GET],

    -- string functions
    ['%string?, :'STRINGP],
    ['%strlength, :'LENGTH],
    ['%schar,     :'CHAR],
    ['%strconc,   :'STRCONC],
    ['%strcopy,   :'COPY_-SEQ],

    -- general utility
    ['%hash,     :'SXHASH],
    ['%equal,    :'EQUAL],
    ['%tref,     :'shellEntry],
    ['%sptreq,    :'EQL],               -- system pointer equality
    ['%lam,      :'LAMBDA],
    ['%leave,    :'RETURN],
    ['%otherwise,:'T],
    ['%funcall,  :'FUNCALL],
    ['%function, :'FUNCTION],
    ['%when,     :'COND],

    -- I/O stream functions
    ['%writeString,  :'WRITE_-STRING],
    ['%writeNewline, :'TERPRI],
    ['%writeLine,    :'WRITE_-LINE]
  ] repeat property(first x,'%Rename) := rest x

++ Table of opcode-expander pairs.  
for x in [
   ['%list,    :function expandList],
   ['%collect, :function expandCollect],
   ['%loop,    :function expandLoop],
   ['%return,  :function expandReturn],

   ['%bcompl,  :function expandBcompl],

   ['%ccst,    :function expandCcst],
   ['%s2c,     :function expandS2c],

   ['%ieq,     :function expandIeq],
   ['%igt,     :function expandIgt],
   ['%ilt,     :function expandIlt],
   ['%ineg,    :function expandIneg],
   ['%idivide, :function expandIdivide],

   ['%i2f,     :function expandI2f],
   ['%fdecode, :function expandFdecode],
   ['%fbase,   :function expandFbase],
   ['%feq,     :function expandFeq],
   ['%fgt,     :function expandFgt],
   ['%flt,     :function expandFlt],
   ['%fmaxval, :function expandFmaxval],
   ['%fminval, :function expandFminval],
   ['%fneg,    :function expandFneg],
   ['%fprec,   :function expandFprec],
   ['%fcstpi,  :function expandFcstpi],
   ['%fsqrt,   :function expandFsqrt],
   ['%fpowf,   :function expandFpowf],
   ['%flog,    :function expandFlog],
   ['%flog2,   :function expandFlog2],
   ['%flog10,  :function expandFlog10],
   ['%fasin,   :function expandFasin],
   ['%facos,   :function expandFacos],
   ['%facosh,  :function expandFacosh],
   ['%fatanh,  :function expandFatanh],
   ['%facoth,  :function expandFacoth],

   ['%z2val,   :function expandZ2val],
   ['%val2z,   :function expandVal2z],

   ['%streq,   :function expandStreq],
   ['%strlt,   :function expandStrlt],
   ['%strstc,  :function expandStrstc],
   ['%bytevec2str, :function expandBytevec2str],
   ['%str2bytevec, :function expandStr2bytevec],

   ['%bitvecnot,    :function expandBitvecnot],
   ['%bitvecand,    :function expandBitvecand],
   ['%bitvecnand,   :function expandBitvecnand],
   ['%bitvecor,     :function expandBitvecor],
   ['%bitvecxor,    :function expandBitvecxor],
   ['%bitvecnor,    :function expandBitvecnor],
   ['%bitveclength, :function expandBitveclength],
   ['%bitveccopy,   :function expandBitveccopy],
   ['%bitvecconc,   :function expandBitvecconc],
   ['%bitveceq,     :function expandBitveceq],
   ['%bitveclt,     :function expandBitveclt],
   ['%bitvecref,   :function expandBitvecref],
   ['%bitvector,   :function expandBitvector],

   ['%peq,     :function expandPeq],
   ['%before?, :function expandBefore?],

   ['%bind,   :function expandBind],
   ['%store,  :function expandStore],
   ['%dynval, :function expandDynval],
   ['%throw,  :function expandThrow],
   ['%try,    :function expandTry],
   ['%apply,  :function expandApply]
 ] repeat property(first x,'%Expander) := rest x

++ Return the expander of a middle-end opcode, or nil if there is none.
getOpcodeExpander op ==
  op has %Expander

++ Expand all opcodes contained in the form `x' into a form
++ suitable for evaluation by the VM.
expandToVMForm x ==
  x = '%false or x = '%nil => 'NIL
  ident? x and (x' := x has %Rename) => x'
  atomic? x => x
  [op,:args] := x
  ident? op and (fun:= getOpcodeExpander op) => apply(fun,x,nil)
  op' := expandToVMForm op
  args' := expandToVMForm args
  sameObject?(op,op') and sameObject?(args,args') => x
  [op',:args']
  


++ Evaluate an OpenAxiom VM form.  Eventually, this function is
++ to be provided as a builtin by a OpenAxiom target machine.
eval x ==
  EVAL expandToVMForm x

