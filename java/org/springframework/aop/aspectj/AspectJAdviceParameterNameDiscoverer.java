/*     */ package org.springframework.aop.aspectj;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.aspectj.lang.JoinPoint;
/*     */ import org.aspectj.lang.ProceedingJoinPoint;
/*     */ import org.aspectj.weaver.tools.PointcutParser;
/*     */ import org.aspectj.weaver.tools.PointcutPrimitive;
/*     */ import org.springframework.core.ParameterNameDiscoverer;
/*     */ import org.springframework.util.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AspectJAdviceParameterNameDiscoverer
/*     */   implements ParameterNameDiscoverer
/*     */ {
/*     */   private static final String THIS_JOIN_POINT = "thisJoinPoint";
/*     */   private static final String THIS_JOIN_POINT_STATIC_PART = "thisJoinPointStaticPart";
/*     */   private static final int STEP_JOIN_POINT_BINDING = 1;
/*     */   private static final int STEP_THROWING_BINDING = 2;
/*     */   private static final int STEP_ANNOTATION_BINDING = 3;
/*     */   private static final int STEP_RETURNING_BINDING = 4;
/*     */   private static final int STEP_PRIMITIVE_ARGS_BINDING = 5;
/*     */   private static final int STEP_THIS_TARGET_ARGS_BINDING = 6;
/*     */   private static final int STEP_REFERENCE_PCUT_BINDING = 7;
/*     */   private static final int STEP_FINISHED = 8;
/* 134 */   private static final Set<String> singleValuedAnnotationPcds = new HashSet<String>();
/* 135 */   private static final Set<String> nonReferencePointcutTokens = new HashSet<String>();
/*     */   private String pointcutExpression;
/*     */   
/*     */   static {
/* 139 */     singleValuedAnnotationPcds.add("@this");
/* 140 */     singleValuedAnnotationPcds.add("@target");
/* 141 */     singleValuedAnnotationPcds.add("@within");
/* 142 */     singleValuedAnnotationPcds.add("@withincode");
/* 143 */     singleValuedAnnotationPcds.add("@annotation");
/*     */     
/* 145 */     Set<PointcutPrimitive> pointcutPrimitives = PointcutParser.getAllSupportedPointcutPrimitives();
/* 146 */     for (PointcutPrimitive primitive : pointcutPrimitives) {
/* 147 */       nonReferencePointcutTokens.add(primitive.getName());
/*     */     }
/* 149 */     nonReferencePointcutTokens.add("&&");
/* 150 */     nonReferencePointcutTokens.add("!");
/* 151 */     nonReferencePointcutTokens.add("||");
/* 152 */     nonReferencePointcutTokens.add("and");
/* 153 */     nonReferencePointcutTokens.add("or");
/* 154 */     nonReferencePointcutTokens.add("not");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean raiseExceptions;
/*     */ 
/*     */ 
/*     */   
/*     */   private String returningName;
/*     */ 
/*     */   
/*     */   private String throwingName;
/*     */ 
/*     */   
/*     */   private Class<?>[] argumentTypes;
/*     */ 
/*     */   
/*     */   private String[] parameterNameBindings;
/*     */ 
/*     */   
/*     */   private int numberOfRemainingUnboundArguments;
/*     */ 
/*     */ 
/*     */   
/*     */   public AspectJAdviceParameterNameDiscoverer(String pointcutExpression) {
/* 181 */     this.pointcutExpression = pointcutExpression;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRaiseExceptions(boolean raiseExceptions) {
/* 191 */     this.raiseExceptions = raiseExceptions;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setReturningName(String returningName) {
/* 200 */     this.returningName = returningName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setThrowingName(String throwingName) {
/* 209 */     this.throwingName = throwingName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getParameterNames(Method method) {
/* 222 */     this.argumentTypes = method.getParameterTypes();
/* 223 */     this.numberOfRemainingUnboundArguments = this.argumentTypes.length;
/* 224 */     this.parameterNameBindings = new String[this.numberOfRemainingUnboundArguments];
/*     */     
/* 226 */     int minimumNumberUnboundArgs = 0;
/* 227 */     if (this.returningName != null) {
/* 228 */       minimumNumberUnboundArgs++;
/*     */     }
/* 230 */     if (this.throwingName != null) {
/* 231 */       minimumNumberUnboundArgs++;
/*     */     }
/* 233 */     if (this.numberOfRemainingUnboundArguments < minimumNumberUnboundArgs) {
/* 234 */       throw new IllegalStateException("Not enough arguments in method to satisfy binding of returning and throwing variables");
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/* 239 */       int algorithmicStep = 1;
/* 240 */       while (this.numberOfRemainingUnboundArguments > 0 && algorithmicStep < 8) {
/* 241 */         switch (algorithmicStep++) {
/*     */           case 1:
/* 243 */             if (!maybeBindThisJoinPoint()) {
/* 244 */               maybeBindThisJoinPointStaticPart();
/*     */             }
/*     */             continue;
/*     */           case 2:
/* 248 */             maybeBindThrowingVariable();
/*     */             continue;
/*     */           case 3:
/* 251 */             maybeBindAnnotationsFromPointcutExpression();
/*     */             continue;
/*     */           case 4:
/* 254 */             maybeBindReturningVariable();
/*     */             continue;
/*     */           case 5:
/* 257 */             maybeBindPrimitiveArgsFromPointcutExpression();
/*     */             continue;
/*     */           case 6:
/* 260 */             maybeBindThisOrTargetOrArgsFromPointcutExpression();
/*     */             continue;
/*     */           case 7:
/* 263 */             maybeBindReferencePointcutParameter();
/*     */             continue;
/*     */         } 
/* 266 */         throw new IllegalStateException("Unknown algorithmic step: " + (algorithmicStep - 1));
/*     */       }
/*     */     
/*     */     }
/* 270 */     catch (AmbiguousBindingException ambigEx) {
/* 271 */       if (this.raiseExceptions) {
/* 272 */         throw ambigEx;
/*     */       }
/*     */       
/* 275 */       return null;
/*     */     
/*     */     }
/* 278 */     catch (IllegalArgumentException ex) {
/* 279 */       if (this.raiseExceptions) {
/* 280 */         throw ex;
/*     */       }
/*     */       
/* 283 */       return null;
/*     */     } 
/*     */ 
/*     */     
/* 287 */     if (this.numberOfRemainingUnboundArguments == 0) {
/* 288 */       return this.parameterNameBindings;
/*     */     }
/*     */     
/* 291 */     if (this.raiseExceptions) {
/* 292 */       throw new IllegalStateException("Failed to bind all argument names: " + this.numberOfRemainingUnboundArguments + " argument(s) could not be bound");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 297 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getParameterNames(Constructor<?> ctor) {
/* 310 */     if (this.raiseExceptions) {
/* 311 */       throw new UnsupportedOperationException("An advice method can never be a constructor");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 316 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void bindParameterName(int index, String name) {
/* 322 */     this.parameterNameBindings[index] = name;
/* 323 */     this.numberOfRemainingUnboundArguments--;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean maybeBindThisJoinPoint() {
/* 331 */     if (this.argumentTypes[0] == JoinPoint.class || this.argumentTypes[0] == ProceedingJoinPoint.class) {
/* 332 */       bindParameterName(0, "thisJoinPoint");
/* 333 */       return true;
/*     */     } 
/*     */     
/* 336 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private void maybeBindThisJoinPointStaticPart() {
/* 341 */     if (this.argumentTypes[0] == JoinPoint.StaticPart.class) {
/* 342 */       bindParameterName(0, "thisJoinPointStaticPart");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void maybeBindThrowingVariable() {
/* 351 */     if (this.throwingName == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 356 */     int throwableIndex = -1;
/* 357 */     for (int i = 0; i < this.argumentTypes.length; i++) {
/* 358 */       if (isUnbound(i) && isSubtypeOf(Throwable.class, i)) {
/* 359 */         if (throwableIndex == -1) {
/* 360 */           throwableIndex = i;
/*     */         }
/*     */         else {
/*     */           
/* 364 */           throw new AmbiguousBindingException("Binding of throwing parameter '" + this.throwingName + "' is ambiguous: could be bound to argument " + throwableIndex + " or argument " + i);
/*     */         } 
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 371 */     if (throwableIndex == -1) {
/* 372 */       throw new IllegalStateException("Binding of throwing parameter '" + this.throwingName + "' could not be completed as no available arguments are a subtype of Throwable");
/*     */     }
/*     */ 
/*     */     
/* 376 */     bindParameterName(throwableIndex, this.throwingName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void maybeBindReturningVariable() {
/* 384 */     if (this.numberOfRemainingUnboundArguments == 0) {
/* 385 */       throw new IllegalStateException("Algorithm assumes that there must be at least one unbound parameter on entry to this method");
/*     */     }
/*     */ 
/*     */     
/* 389 */     if (this.returningName != null) {
/* 390 */       if (this.numberOfRemainingUnboundArguments > 1) {
/* 391 */         throw new AmbiguousBindingException("Binding of returning parameter '" + this.returningName + "' is ambiguous, there are " + this.numberOfRemainingUnboundArguments + " candidates.");
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 396 */       for (int i = 0; i < this.parameterNameBindings.length; i++) {
/* 397 */         if (this.parameterNameBindings[i] == null) {
/* 398 */           bindParameterName(i, this.returningName);
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void maybeBindAnnotationsFromPointcutExpression() {
/* 414 */     List<String> varNames = new ArrayList<String>();
/* 415 */     String[] tokens = StringUtils.tokenizeToStringArray(this.pointcutExpression, " ");
/* 416 */     for (int i = 0; i < tokens.length; i++) {
/* 417 */       String toMatch = tokens[i];
/* 418 */       int firstParenIndex = toMatch.indexOf('(');
/* 419 */       if (firstParenIndex != -1) {
/* 420 */         toMatch = toMatch.substring(0, firstParenIndex);
/*     */       }
/* 422 */       if (singleValuedAnnotationPcds.contains(toMatch)) {
/* 423 */         PointcutBody body = getPointcutBody(tokens, i);
/* 424 */         i += body.numTokensConsumed;
/* 425 */         String varName = maybeExtractVariableName(body.text);
/* 426 */         if (varName != null) {
/* 427 */           varNames.add(varName);
/*     */         }
/*     */       }
/* 430 */       else if (tokens[i].startsWith("@args(") || tokens[i].equals("@args")) {
/* 431 */         PointcutBody body = getPointcutBody(tokens, i);
/* 432 */         i += body.numTokensConsumed;
/* 433 */         maybeExtractVariableNamesFromArgs(body.text, varNames);
/*     */       } 
/*     */     } 
/*     */     
/* 437 */     bindAnnotationsFromVarNames(varNames);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void bindAnnotationsFromVarNames(List<String> varNames) {
/* 444 */     if (!varNames.isEmpty()) {
/*     */       
/* 446 */       int numAnnotationSlots = countNumberOfUnboundAnnotationArguments();
/* 447 */       if (numAnnotationSlots > 1) {
/* 448 */         throw new AmbiguousBindingException("Found " + varNames.size() + " potential annotation variable(s), and " + numAnnotationSlots + " potential argument slots");
/*     */       }
/*     */ 
/*     */       
/* 452 */       if (numAnnotationSlots == 1) {
/* 453 */         if (varNames.size() == 1) {
/*     */           
/* 455 */           findAndBind(Annotation.class, varNames.get(0));
/*     */         }
/*     */         else {
/*     */           
/* 459 */           throw new IllegalArgumentException("Found " + varNames.size() + " candidate annotation binding variables but only one potential argument binding slot");
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String maybeExtractVariableName(String candidateToken) {
/* 474 */     if (!StringUtils.hasLength(candidateToken)) {
/* 475 */       return null;
/*     */     }
/* 477 */     if (Character.isJavaIdentifierStart(candidateToken.charAt(0)) && 
/* 478 */       Character.isLowerCase(candidateToken.charAt(0))) {
/* 479 */       char[] tokenChars = candidateToken.toCharArray();
/* 480 */       for (char tokenChar : tokenChars) {
/* 481 */         if (!Character.isJavaIdentifierPart(tokenChar)) {
/* 482 */           return null;
/*     */         }
/*     */       } 
/* 485 */       return candidateToken;
/*     */     } 
/*     */     
/* 488 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void maybeExtractVariableNamesFromArgs(String argsSpec, List<String> varNames) {
/* 497 */     if (argsSpec == null) {
/*     */       return;
/*     */     }
/* 500 */     String[] tokens = StringUtils.tokenizeToStringArray(argsSpec, ",");
/* 501 */     for (int i = 0; i < tokens.length; i++) {
/* 502 */       tokens[i] = StringUtils.trimWhitespace(tokens[i]);
/* 503 */       String varName = maybeExtractVariableName(tokens[i]);
/* 504 */       if (varName != null) {
/* 505 */         varNames.add(varName);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void maybeBindThisOrTargetOrArgsFromPointcutExpression() {
/* 515 */     if (this.numberOfRemainingUnboundArguments > 1) {
/* 516 */       throw new AmbiguousBindingException("Still " + this.numberOfRemainingUnboundArguments + " unbound args at this(),target(),args() binding stage, with no way to determine between them");
/*     */     }
/*     */ 
/*     */     
/* 520 */     List<String> varNames = new ArrayList<String>();
/* 521 */     String[] tokens = StringUtils.tokenizeToStringArray(this.pointcutExpression, " ");
/* 522 */     for (int i = 0; i < tokens.length; i++) {
/* 523 */       if (tokens[i].equals("this") || tokens[i]
/* 524 */         .startsWith("this(") || tokens[i]
/* 525 */         .equals("target") || tokens[i]
/* 526 */         .startsWith("target(")) {
/* 527 */         PointcutBody body = getPointcutBody(tokens, i);
/* 528 */         i += body.numTokensConsumed;
/* 529 */         String varName = maybeExtractVariableName(body.text);
/* 530 */         if (varName != null) {
/* 531 */           varNames.add(varName);
/*     */         }
/*     */       }
/* 534 */       else if (tokens[i].equals("args") || tokens[i].startsWith("args(")) {
/* 535 */         PointcutBody body = getPointcutBody(tokens, i);
/* 536 */         i += body.numTokensConsumed;
/* 537 */         List<String> candidateVarNames = new ArrayList<String>();
/* 538 */         maybeExtractVariableNamesFromArgs(body.text, candidateVarNames);
/*     */ 
/*     */         
/* 541 */         for (String varName : candidateVarNames) {
/* 542 */           if (!alreadyBound(varName)) {
/* 543 */             varNames.add(varName);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 550 */     if (varNames.size() > 1) {
/* 551 */       throw new AmbiguousBindingException("Found " + varNames.size() + " candidate this(), target() or args() variables but only one unbound argument slot");
/*     */     }
/*     */     
/* 554 */     if (varNames.size() == 1) {
/* 555 */       for (int j = 0; j < this.parameterNameBindings.length; j++) {
/* 556 */         if (isUnbound(j)) {
/* 557 */           bindParameterName(j, varNames.get(0));
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void maybeBindReferencePointcutParameter() {
/* 566 */     if (this.numberOfRemainingUnboundArguments > 1) {
/* 567 */       throw new AmbiguousBindingException("Still " + this.numberOfRemainingUnboundArguments + " unbound args at reference pointcut binding stage, with no way to determine between them");
/*     */     }
/*     */ 
/*     */     
/* 571 */     List<String> varNames = new ArrayList<String>();
/* 572 */     String[] tokens = StringUtils.tokenizeToStringArray(this.pointcutExpression, " ");
/* 573 */     for (int i = 0; i < tokens.length; i++) {
/* 574 */       String toMatch = tokens[i];
/* 575 */       if (toMatch.startsWith("!")) {
/* 576 */         toMatch = toMatch.substring(1);
/*     */       }
/* 578 */       int firstParenIndex = toMatch.indexOf('(');
/* 579 */       if (firstParenIndex != -1) {
/* 580 */         toMatch = toMatch.substring(0, firstParenIndex);
/*     */       } else {
/*     */         
/* 583 */         if (tokens.length < i + 2) {
/*     */           continue;
/*     */         }
/*     */ 
/*     */         
/* 588 */         String nextToken = tokens[i + 1];
/* 589 */         if (nextToken.charAt(0) != '(') {
/*     */           continue;
/*     */         }
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 598 */       PointcutBody body = getPointcutBody(tokens, i);
/* 599 */       i += body.numTokensConsumed;
/*     */       
/* 601 */       if (!nonReferencePointcutTokens.contains(toMatch)) {
/*     */         
/* 603 */         String varName = maybeExtractVariableName(body.text);
/* 604 */         if (varName != null) {
/* 605 */           varNames.add(varName);
/*     */         }
/*     */       } 
/*     */       continue;
/*     */     } 
/* 610 */     if (varNames.size() > 1) {
/* 611 */       throw new AmbiguousBindingException("Found " + varNames.size() + " candidate reference pointcut variables but only one unbound argument slot");
/*     */     }
/*     */     
/* 614 */     if (varNames.size() == 1) {
/* 615 */       for (int j = 0; j < this.parameterNameBindings.length; j++) {
/* 616 */         if (isUnbound(j)) {
/* 617 */           bindParameterName(j, varNames.get(0));
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private PointcutBody getPointcutBody(String[] tokens, int startIndex) {
/* 630 */     int numTokensConsumed = 0;
/* 631 */     String currentToken = tokens[startIndex];
/* 632 */     int bodyStart = currentToken.indexOf('(');
/* 633 */     if (currentToken.charAt(currentToken.length() - 1) == ')')
/*     */     {
/* 635 */       return new PointcutBody(0, currentToken.substring(bodyStart + 1, currentToken.length() - 1));
/*     */     }
/*     */     
/* 638 */     StringBuilder sb = new StringBuilder();
/* 639 */     if (bodyStart >= 0 && bodyStart != currentToken.length() - 1) {
/* 640 */       sb.append(currentToken.substring(bodyStart + 1));
/* 641 */       sb.append(" ");
/*     */     } 
/* 643 */     numTokensConsumed++;
/* 644 */     int currentIndex = startIndex + numTokensConsumed;
/* 645 */     while (currentIndex < tokens.length) {
/* 646 */       if (tokens[currentIndex].equals("(")) {
/* 647 */         currentIndex++;
/*     */         
/*     */         continue;
/*     */       } 
/* 651 */       if (tokens[currentIndex].endsWith(")")) {
/* 652 */         sb.append(tokens[currentIndex].substring(0, tokens[currentIndex].length() - 1));
/* 653 */         return new PointcutBody(numTokensConsumed, sb.toString().trim());
/*     */       } 
/*     */       
/* 656 */       String toAppend = tokens[currentIndex];
/* 657 */       if (toAppend.startsWith("(")) {
/* 658 */         toAppend = toAppend.substring(1);
/*     */       }
/* 660 */       sb.append(toAppend);
/* 661 */       sb.append(" ");
/* 662 */       currentIndex++;
/* 663 */       numTokensConsumed++;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 669 */     return new PointcutBody(numTokensConsumed, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void maybeBindPrimitiveArgsFromPointcutExpression() {
/* 676 */     int numUnboundPrimitives = countNumberOfUnboundPrimitiveArguments();
/* 677 */     if (numUnboundPrimitives > 1) {
/* 678 */       throw new AmbiguousBindingException("Found '" + numUnboundPrimitives + "' unbound primitive arguments with no way to distinguish between them.");
/*     */     }
/*     */     
/* 681 */     if (numUnboundPrimitives == 1) {
/*     */       
/* 683 */       List<String> varNames = new ArrayList<String>();
/* 684 */       String[] tokens = StringUtils.tokenizeToStringArray(this.pointcutExpression, " "); int i;
/* 685 */       for (i = 0; i < tokens.length; i++) {
/* 686 */         if (tokens[i].equals("args") || tokens[i].startsWith("args(")) {
/* 687 */           PointcutBody body = getPointcutBody(tokens, i);
/* 688 */           i += body.numTokensConsumed;
/* 689 */           maybeExtractVariableNamesFromArgs(body.text, varNames);
/*     */         } 
/*     */       } 
/* 692 */       if (varNames.size() > 1) {
/* 693 */         throw new AmbiguousBindingException("Found " + varNames.size() + " candidate variable names but only one candidate binding slot when matching primitive args");
/*     */       }
/*     */       
/* 696 */       if (varNames.size() == 1)
/*     */       {
/* 698 */         for (i = 0; i < this.argumentTypes.length; i++) {
/* 699 */           if (isUnbound(i) && this.argumentTypes[i].isPrimitive()) {
/* 700 */             bindParameterName(i, varNames.get(0));
/*     */             break;
/*     */           } 
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isUnbound(int i) {
/* 713 */     return (this.parameterNameBindings[i] == null);
/*     */   }
/*     */   
/*     */   private boolean alreadyBound(String varName) {
/* 717 */     for (int i = 0; i < this.parameterNameBindings.length; i++) {
/* 718 */       if (!isUnbound(i) && varName.equals(this.parameterNameBindings[i])) {
/* 719 */         return true;
/*     */       }
/*     */     } 
/* 722 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isSubtypeOf(Class<?> supertype, int argumentNumber) {
/* 730 */     return supertype.isAssignableFrom(this.argumentTypes[argumentNumber]);
/*     */   }
/*     */   
/*     */   private int countNumberOfUnboundAnnotationArguments() {
/* 734 */     int count = 0;
/* 735 */     for (int i = 0; i < this.argumentTypes.length; i++) {
/* 736 */       if (isUnbound(i) && isSubtypeOf(Annotation.class, i)) {
/* 737 */         count++;
/*     */       }
/*     */     } 
/* 740 */     return count;
/*     */   }
/*     */   
/*     */   private int countNumberOfUnboundPrimitiveArguments() {
/* 744 */     int count = 0;
/* 745 */     for (int i = 0; i < this.argumentTypes.length; i++) {
/* 746 */       if (isUnbound(i) && this.argumentTypes[i].isPrimitive()) {
/* 747 */         count++;
/*     */       }
/*     */     } 
/* 750 */     return count;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void findAndBind(Class<?> argumentType, String varName) {
/* 758 */     for (int i = 0; i < this.argumentTypes.length; i++) {
/* 759 */       if (isUnbound(i) && isSubtypeOf(argumentType, i)) {
/* 760 */         bindParameterName(i, varName);
/*     */         return;
/*     */       } 
/*     */     } 
/* 764 */     throw new IllegalStateException("Expected to find an unbound argument of type '" + argumentType
/* 765 */         .getName() + "'");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class PointcutBody
/*     */   {
/*     */     private int numTokensConsumed;
/*     */ 
/*     */     
/*     */     private String text;
/*     */ 
/*     */ 
/*     */     
/*     */     public PointcutBody(int tokens, String text) {
/* 780 */       this.numTokensConsumed = tokens;
/* 781 */       this.text = text;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class AmbiguousBindingException
/*     */     extends RuntimeException
/*     */   {
/*     */     public AmbiguousBindingException(String msg) {
/* 798 */       super(msg);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\aspectj\AspectJAdviceParameterNameDiscoverer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */