/*     */ package org.springframework.aop.aspectj;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.aopalliance.aop.Advice;
/*     */ import org.aopalliance.intercept.MethodInvocation;
/*     */ import org.aspectj.lang.JoinPoint;
/*     */ import org.aspectj.lang.ProceedingJoinPoint;
/*     */ import org.aspectj.weaver.tools.JoinPointMatch;
/*     */ import org.aspectj.weaver.tools.PointcutParameter;
/*     */ import org.springframework.aop.AopInvocationException;
/*     */ import org.springframework.aop.MethodMatcher;
/*     */ import org.springframework.aop.Pointcut;
/*     */ import org.springframework.aop.ProxyMethodInvocation;
/*     */ import org.springframework.aop.interceptor.ExposeInvocationInterceptor;
/*     */ import org.springframework.aop.support.ComposablePointcut;
/*     */ import org.springframework.aop.support.MethodMatchers;
/*     */ import org.springframework.aop.support.StaticMethodMatcher;
/*     */ import org.springframework.core.DefaultParameterNameDiscoverer;
/*     */ import org.springframework.core.ParameterNameDiscoverer;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.util.ReflectionUtils;
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
/*     */ public abstract class AbstractAspectJAdvice
/*     */   implements Advice, AspectJPrecedenceInformation, Serializable
/*     */ {
/*  67 */   protected static final String JOIN_POINT_KEY = JoinPoint.class.getName();
/*     */   
/*     */   private final Class<?> declaringClass;
/*     */   
/*     */   private final String methodName;
/*     */   
/*     */   private final Class<?>[] parameterTypes;
/*     */   protected transient Method aspectJAdviceMethod;
/*     */   private final AspectJExpressionPointcut pointcut;
/*     */   
/*     */   public static JoinPoint currentJoinPoint() {
/*     */     MethodInvocationProceedingJoinPoint methodInvocationProceedingJoinPoint;
/*  79 */     MethodInvocation mi = ExposeInvocationInterceptor.currentInvocation();
/*  80 */     if (!(mi instanceof ProxyMethodInvocation)) {
/*  81 */       throw new IllegalStateException("MethodInvocation is not a Spring ProxyMethodInvocation: " + mi);
/*     */     }
/*  83 */     ProxyMethodInvocation pmi = (ProxyMethodInvocation)mi;
/*  84 */     JoinPoint jp = (JoinPoint)pmi.getUserAttribute(JOIN_POINT_KEY);
/*  85 */     if (jp == null) {
/*  86 */       methodInvocationProceedingJoinPoint = new MethodInvocationProceedingJoinPoint(pmi);
/*  87 */       pmi.setUserAttribute(JOIN_POINT_KEY, methodInvocationProceedingJoinPoint);
/*     */     } 
/*  89 */     return (JoinPoint)methodInvocationProceedingJoinPoint;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final AspectInstanceFactory aspectInstanceFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String aspectName;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int declarationOrder;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String[] argumentNames;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String throwingName;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String returningName;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 129 */   private Class<?> discoveredReturningType = Object.class;
/*     */   
/* 131 */   private Class<?> discoveredThrowingType = Object.class;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 137 */   private int joinPointArgumentIndex = -1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 143 */   private int joinPointStaticPartArgumentIndex = -1;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Map<String, Integer> argumentBindings;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean argumentsIntrospected = false;
/*     */ 
/*     */ 
/*     */   
/*     */   private Type discoveredReturningGenericType;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractAspectJAdvice(Method aspectJAdviceMethod, AspectJExpressionPointcut pointcut, AspectInstanceFactory aspectInstanceFactory) {
/* 163 */     Assert.notNull(aspectJAdviceMethod, "Advice method must not be null");
/* 164 */     this.declaringClass = aspectJAdviceMethod.getDeclaringClass();
/* 165 */     this.methodName = aspectJAdviceMethod.getName();
/* 166 */     this.parameterTypes = aspectJAdviceMethod.getParameterTypes();
/* 167 */     this.aspectJAdviceMethod = aspectJAdviceMethod;
/* 168 */     this.pointcut = pointcut;
/* 169 */     this.aspectInstanceFactory = aspectInstanceFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Method getAspectJAdviceMethod() {
/* 177 */     return this.aspectJAdviceMethod;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final AspectJExpressionPointcut getPointcut() {
/* 184 */     calculateArgumentBindings();
/* 185 */     return this.pointcut;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Pointcut buildSafePointcut() {
/* 194 */     AspectJExpressionPointcut aspectJExpressionPointcut = getPointcut();
/* 195 */     MethodMatcher safeMethodMatcher = MethodMatchers.intersection((MethodMatcher)new AdviceExcludingMethodMatcher(this.aspectJAdviceMethod), aspectJExpressionPointcut
/* 196 */         .getMethodMatcher());
/* 197 */     return (Pointcut)new ComposablePointcut(aspectJExpressionPointcut.getClassFilter(), safeMethodMatcher);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final AspectInstanceFactory getAspectInstanceFactory() {
/* 204 */     return this.aspectInstanceFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ClassLoader getAspectClassLoader() {
/* 211 */     return this.aspectInstanceFactory.getAspectClassLoader();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getOrder() {
/* 216 */     return this.aspectInstanceFactory.getOrder();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAspectName(String name) {
/* 221 */     this.aspectName = name;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getAspectName() {
/* 226 */     return this.aspectName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDeclarationOrder(int order) {
/* 233 */     this.declarationOrder = order;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDeclarationOrder() {
/* 238 */     return this.declarationOrder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setArgumentNames(String argNames) {
/* 248 */     String[] tokens = StringUtils.commaDelimitedListToStringArray(argNames);
/* 249 */     setArgumentNamesFromStringArray(tokens);
/*     */   }
/*     */   
/*     */   public void setArgumentNamesFromStringArray(String... args) {
/* 253 */     this.argumentNames = new String[args.length];
/* 254 */     for (int i = 0; i < args.length; i++) {
/* 255 */       this.argumentNames[i] = StringUtils.trimWhitespace(args[i]);
/* 256 */       if (!isVariableName(this.argumentNames[i])) {
/* 257 */         throw new IllegalArgumentException("'argumentNames' property of AbstractAspectJAdvice contains an argument name '" + this.argumentNames[i] + "' that is not a valid Java identifier");
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 262 */     if (this.argumentNames != null && (
/* 263 */       this.aspectJAdviceMethod.getParameterTypes()).length == this.argumentNames.length + 1) {
/*     */       
/* 265 */       Class<?> firstArgType = this.aspectJAdviceMethod.getParameterTypes()[0];
/* 266 */       if (firstArgType == JoinPoint.class || firstArgType == ProceedingJoinPoint.class || firstArgType == JoinPoint.StaticPart.class) {
/*     */ 
/*     */         
/* 269 */         String[] oldNames = this.argumentNames;
/* 270 */         this.argumentNames = new String[oldNames.length + 1];
/* 271 */         this.argumentNames[0] = "THIS_JOIN_POINT";
/* 272 */         System.arraycopy(oldNames, 0, this.argumentNames, 1, oldNames.length);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setReturningName(String name) {
/* 279 */     throw new UnsupportedOperationException("Only afterReturning advice can be used to bind a return value");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setReturningNameNoCheck(String name) {
/* 288 */     if (isVariableName(name)) {
/* 289 */       this.returningName = name;
/*     */     } else {
/*     */ 
/*     */       
/*     */       try {
/* 294 */         this.discoveredReturningType = ClassUtils.forName(name, getAspectClassLoader());
/*     */       }
/* 296 */       catch (Throwable ex) {
/* 297 */         throw new IllegalArgumentException("Returning name '" + name + "' is neither a valid argument name nor the fully-qualified name of a Java type on the classpath. Root cause: " + ex);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Class<?> getDiscoveredReturningType() {
/* 305 */     return this.discoveredReturningType;
/*     */   }
/*     */   
/*     */   protected Type getDiscoveredReturningGenericType() {
/* 309 */     return this.discoveredReturningGenericType;
/*     */   }
/*     */   
/*     */   public void setThrowingName(String name) {
/* 313 */     throw new UnsupportedOperationException("Only afterThrowing advice can be used to bind a thrown exception");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setThrowingNameNoCheck(String name) {
/* 322 */     if (isVariableName(name)) {
/* 323 */       this.throwingName = name;
/*     */     } else {
/*     */ 
/*     */       
/*     */       try {
/* 328 */         this.discoveredThrowingType = ClassUtils.forName(name, getAspectClassLoader());
/*     */       }
/* 330 */       catch (Throwable ex) {
/* 331 */         throw new IllegalArgumentException("Throwing name '" + name + "' is neither a valid argument name nor the fully-qualified name of a Java type on the classpath. Root cause: " + ex);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Class<?> getDiscoveredThrowingType() {
/* 339 */     return this.discoveredThrowingType;
/*     */   }
/*     */   
/*     */   private boolean isVariableName(String name) {
/* 343 */     char[] chars = name.toCharArray();
/* 344 */     if (!Character.isJavaIdentifierStart(chars[0])) {
/* 345 */       return false;
/*     */     }
/* 347 */     for (int i = 1; i < chars.length; i++) {
/* 348 */       if (!Character.isJavaIdentifierPart(chars[i])) {
/* 349 */         return false;
/*     */       }
/*     */     } 
/* 352 */     return true;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final synchronized void calculateArgumentBindings() {
/* 371 */     if (this.argumentsIntrospected || this.parameterTypes.length == 0) {
/*     */       return;
/*     */     }
/*     */     
/* 375 */     int numUnboundArgs = this.parameterTypes.length;
/* 376 */     Class<?>[] parameterTypes = this.aspectJAdviceMethod.getParameterTypes();
/* 377 */     if (maybeBindJoinPoint(parameterTypes[0]) || maybeBindProceedingJoinPoint(parameterTypes[0]) || 
/* 378 */       maybeBindJoinPointStaticPart(parameterTypes[0])) {
/* 379 */       numUnboundArgs--;
/*     */     }
/*     */     
/* 382 */     if (numUnboundArgs > 0)
/*     */     {
/* 384 */       bindArgumentsByName(numUnboundArgs);
/*     */     }
/*     */     
/* 387 */     this.argumentsIntrospected = true;
/*     */   }
/*     */   
/*     */   private boolean maybeBindJoinPoint(Class<?> candidateParameterType) {
/* 391 */     if (JoinPoint.class == candidateParameterType) {
/* 392 */       this.joinPointArgumentIndex = 0;
/* 393 */       return true;
/*     */     } 
/*     */     
/* 396 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean maybeBindProceedingJoinPoint(Class<?> candidateParameterType) {
/* 401 */     if (ProceedingJoinPoint.class == candidateParameterType) {
/* 402 */       if (!supportsProceedingJoinPoint()) {
/* 403 */         throw new IllegalArgumentException("ProceedingJoinPoint is only supported for around advice");
/*     */       }
/* 405 */       this.joinPointArgumentIndex = 0;
/* 406 */       return true;
/*     */     } 
/*     */     
/* 409 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean supportsProceedingJoinPoint() {
/* 414 */     return false;
/*     */   }
/*     */   
/*     */   private boolean maybeBindJoinPointStaticPart(Class<?> candidateParameterType) {
/* 418 */     if (JoinPoint.StaticPart.class == candidateParameterType) {
/* 419 */       this.joinPointStaticPartArgumentIndex = 0;
/* 420 */       return true;
/*     */     } 
/*     */     
/* 423 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private void bindArgumentsByName(int numArgumentsExpectingToBind) {
/* 428 */     if (this.argumentNames == null) {
/* 429 */       this.argumentNames = createParameterNameDiscoverer().getParameterNames(this.aspectJAdviceMethod);
/*     */     }
/* 431 */     if (this.argumentNames != null) {
/*     */       
/* 433 */       bindExplicitArguments(numArgumentsExpectingToBind);
/*     */     } else {
/*     */       
/* 436 */       throw new IllegalStateException("Advice method [" + this.aspectJAdviceMethod.getName() + "] requires " + numArgumentsExpectingToBind + " arguments to be bound by name, but the argument names were not specified and could not be discovered.");
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
/*     */   protected ParameterNameDiscoverer createParameterNameDiscoverer() {
/* 450 */     DefaultParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();
/*     */     
/* 452 */     AspectJAdviceParameterNameDiscoverer adviceParameterNameDiscoverer = new AspectJAdviceParameterNameDiscoverer(this.pointcut.getExpression());
/* 453 */     adviceParameterNameDiscoverer.setReturningName(this.returningName);
/* 454 */     adviceParameterNameDiscoverer.setThrowingName(this.throwingName);
/*     */     
/* 456 */     adviceParameterNameDiscoverer.setRaiseExceptions(true);
/* 457 */     discoverer.addDiscoverer(adviceParameterNameDiscoverer);
/* 458 */     return (ParameterNameDiscoverer)discoverer;
/*     */   }
/*     */   
/*     */   private void bindExplicitArguments(int numArgumentsLeftToBind) {
/* 462 */     this.argumentBindings = new HashMap<String, Integer>();
/*     */     
/* 464 */     int numExpectedArgumentNames = (this.aspectJAdviceMethod.getParameterTypes()).length;
/* 465 */     if (this.argumentNames.length != numExpectedArgumentNames) {
/* 466 */       throw new IllegalStateException("Expecting to find " + numExpectedArgumentNames + " arguments to bind by name in advice, but actually found " + this.argumentNames.length + " arguments.");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 472 */     int argumentIndexOffset = this.parameterTypes.length - numArgumentsLeftToBind;
/* 473 */     for (int i = argumentIndexOffset; i < this.argumentNames.length; i++) {
/* 474 */       this.argumentBindings.put(this.argumentNames[i], Integer.valueOf(i));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 479 */     if (this.returningName != null) {
/* 480 */       if (!this.argumentBindings.containsKey(this.returningName)) {
/* 481 */         throw new IllegalStateException("Returning argument name '" + this.returningName + "' was not bound in advice arguments");
/*     */       }
/*     */ 
/*     */       
/* 485 */       Integer index = this.argumentBindings.get(this.returningName);
/* 486 */       this.discoveredReturningType = this.aspectJAdviceMethod.getParameterTypes()[index.intValue()];
/* 487 */       this.discoveredReturningGenericType = this.aspectJAdviceMethod.getGenericParameterTypes()[index.intValue()];
/*     */     } 
/*     */     
/* 490 */     if (this.throwingName != null) {
/* 491 */       if (!this.argumentBindings.containsKey(this.throwingName)) {
/* 492 */         throw new IllegalStateException("Throwing argument name '" + this.throwingName + "' was not bound in advice arguments");
/*     */       }
/*     */ 
/*     */       
/* 496 */       Integer index = this.argumentBindings.get(this.throwingName);
/* 497 */       this.discoveredThrowingType = this.aspectJAdviceMethod.getParameterTypes()[index.intValue()];
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 502 */     configurePointcutParameters(argumentIndexOffset);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void configurePointcutParameters(int argumentIndexOffset) {
/* 511 */     int numParametersToRemove = argumentIndexOffset;
/* 512 */     if (this.returningName != null) {
/* 513 */       numParametersToRemove++;
/*     */     }
/* 515 */     if (this.throwingName != null) {
/* 516 */       numParametersToRemove++;
/*     */     }
/* 518 */     String[] pointcutParameterNames = new String[this.argumentNames.length - numParametersToRemove];
/* 519 */     Class<?>[] pointcutParameterTypes = new Class[pointcutParameterNames.length];
/* 520 */     Class<?>[] methodParameterTypes = this.aspectJAdviceMethod.getParameterTypes();
/*     */     
/* 522 */     int index = 0;
/* 523 */     for (int i = 0; i < this.argumentNames.length; i++) {
/* 524 */       if (i >= argumentIndexOffset)
/*     */       {
/*     */         
/* 527 */         if (!this.argumentNames[i].equals(this.returningName) && 
/* 528 */           !this.argumentNames[i].equals(this.throwingName)) {
/*     */ 
/*     */           
/* 531 */           pointcutParameterNames[index] = this.argumentNames[i];
/* 532 */           pointcutParameterTypes[index] = methodParameterTypes[i];
/* 533 */           index++;
/*     */         }  } 
/*     */     } 
/* 536 */     this.pointcut.setParameterNames(pointcutParameterNames);
/* 537 */     this.pointcut.setParameterTypes(pointcutParameterTypes);
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
/*     */   protected Object[] argBinding(JoinPoint jp, JoinPointMatch jpMatch, Object returnValue, Throwable ex) {
/* 550 */     calculateArgumentBindings();
/*     */ 
/*     */     
/* 553 */     Object[] adviceInvocationArgs = new Object[this.parameterTypes.length];
/* 554 */     int numBound = 0;
/*     */     
/* 556 */     if (this.joinPointArgumentIndex != -1) {
/* 557 */       adviceInvocationArgs[this.joinPointArgumentIndex] = jp;
/* 558 */       numBound++;
/*     */     }
/* 560 */     else if (this.joinPointStaticPartArgumentIndex != -1) {
/* 561 */       adviceInvocationArgs[this.joinPointStaticPartArgumentIndex] = jp.getStaticPart();
/* 562 */       numBound++;
/*     */     } 
/*     */     
/* 565 */     if (!CollectionUtils.isEmpty(this.argumentBindings)) {
/*     */       
/* 567 */       if (jpMatch != null) {
/* 568 */         PointcutParameter[] parameterBindings = jpMatch.getParameterBindings();
/* 569 */         for (PointcutParameter parameter : parameterBindings) {
/* 570 */           String name = parameter.getName();
/* 571 */           Integer index = this.argumentBindings.get(name);
/* 572 */           adviceInvocationArgs[index.intValue()] = parameter.getBinding();
/* 573 */           numBound++;
/*     */         } 
/*     */       } 
/*     */       
/* 577 */       if (this.returningName != null) {
/* 578 */         Integer index = this.argumentBindings.get(this.returningName);
/* 579 */         adviceInvocationArgs[index.intValue()] = returnValue;
/* 580 */         numBound++;
/*     */       } 
/*     */       
/* 583 */       if (this.throwingName != null) {
/* 584 */         Integer index = this.argumentBindings.get(this.throwingName);
/* 585 */         adviceInvocationArgs[index.intValue()] = ex;
/* 586 */         numBound++;
/*     */       } 
/*     */     } 
/*     */     
/* 590 */     if (numBound != this.parameterTypes.length) {
/* 591 */       throw new IllegalStateException("Required to bind " + this.parameterTypes.length + " arguments, but only bound " + numBound + " (JoinPointMatch " + ((jpMatch == null) ? "was NOT" : "WAS") + " bound in invocation)");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 596 */     return adviceInvocationArgs;
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
/*     */   protected Object invokeAdviceMethod(JoinPointMatch jpMatch, Object returnValue, Throwable ex) throws Throwable {
/* 609 */     return invokeAdviceMethodWithGivenArgs(argBinding(getJoinPoint(), jpMatch, returnValue, ex));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object invokeAdviceMethod(JoinPoint jp, JoinPointMatch jpMatch, Object returnValue, Throwable t) throws Throwable {
/* 616 */     return invokeAdviceMethodWithGivenArgs(argBinding(jp, jpMatch, returnValue, t));
/*     */   }
/*     */   
/*     */   protected Object invokeAdviceMethodWithGivenArgs(Object[] args) throws Throwable {
/* 620 */     Object[] actualArgs = args;
/* 621 */     if ((this.aspectJAdviceMethod.getParameterTypes()).length == 0) {
/* 622 */       actualArgs = null;
/*     */     }
/*     */     try {
/* 625 */       ReflectionUtils.makeAccessible(this.aspectJAdviceMethod);
/*     */       
/* 627 */       return this.aspectJAdviceMethod.invoke(this.aspectInstanceFactory.getAspectInstance(), actualArgs);
/*     */     }
/* 629 */     catch (IllegalArgumentException ex) {
/* 630 */       throw new AopInvocationException("Mismatch on arguments to advice method [" + this.aspectJAdviceMethod + "]; pointcut expression [" + this.pointcut
/*     */           
/* 632 */           .getPointcutExpression() + "]", ex);
/*     */     }
/* 634 */     catch (InvocationTargetException ex) {
/* 635 */       throw ex.getTargetException();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected JoinPoint getJoinPoint() {
/* 643 */     return currentJoinPoint();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected JoinPointMatch getJoinPointMatch() {
/* 650 */     MethodInvocation mi = ExposeInvocationInterceptor.currentInvocation();
/* 651 */     if (!(mi instanceof ProxyMethodInvocation)) {
/* 652 */       throw new IllegalStateException("MethodInvocation is not a Spring ProxyMethodInvocation: " + mi);
/*     */     }
/* 654 */     return getJoinPointMatch((ProxyMethodInvocation)mi);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected JoinPointMatch getJoinPointMatch(ProxyMethodInvocation pmi) {
/* 664 */     return (JoinPointMatch)pmi.getUserAttribute(this.pointcut.getExpression());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 670 */     return getClass().getName() + ": advice method [" + this.aspectJAdviceMethod + "]; aspect name '" + this.aspectName + "'";
/*     */   }
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
/* 675 */     inputStream.defaultReadObject();
/*     */     try {
/* 677 */       this.aspectJAdviceMethod = this.declaringClass.getMethod(this.methodName, this.parameterTypes);
/*     */     }
/* 679 */     catch (NoSuchMethodException ex) {
/* 680 */       throw new IllegalStateException("Failed to find advice method on deserialization", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class AdviceExcludingMethodMatcher
/*     */     extends StaticMethodMatcher
/*     */   {
/*     */     private final Method adviceMethod;
/*     */ 
/*     */ 
/*     */     
/*     */     public AdviceExcludingMethodMatcher(Method adviceMethod) {
/* 694 */       this.adviceMethod = adviceMethod;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean matches(Method method, Class<?> targetClass) {
/* 699 */       return !this.adviceMethod.equals(method);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object other) {
/* 704 */       if (this == other) {
/* 705 */         return true;
/*     */       }
/* 707 */       if (!(other instanceof AdviceExcludingMethodMatcher)) {
/* 708 */         return false;
/*     */       }
/* 710 */       AdviceExcludingMethodMatcher otherMm = (AdviceExcludingMethodMatcher)other;
/* 711 */       return this.adviceMethod.equals(otherMm.adviceMethod);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 716 */       return this.adviceMethod.hashCode();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\aspectj\AbstractAspectJAdvice.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */