/*     */ package org.springframework.aop.aspectj;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.aopalliance.intercept.MethodInvocation;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.aspectj.util.FuzzyBoolean;
/*     */ import org.aspectj.weaver.patterns.NamePattern;
/*     */ import org.aspectj.weaver.reflect.ReflectionWorld;
/*     */ import org.aspectj.weaver.reflect.ShadowMatchImpl;
/*     */ import org.aspectj.weaver.tools.ContextBasedMatcher;
/*     */ import org.aspectj.weaver.tools.FuzzyBoolean;
/*     */ import org.aspectj.weaver.tools.JoinPointMatch;
/*     */ import org.aspectj.weaver.tools.MatchingContext;
/*     */ import org.aspectj.weaver.tools.PointcutDesignatorHandler;
/*     */ import org.aspectj.weaver.tools.PointcutExpression;
/*     */ import org.aspectj.weaver.tools.PointcutParameter;
/*     */ import org.aspectj.weaver.tools.PointcutParser;
/*     */ import org.aspectj.weaver.tools.PointcutPrimitive;
/*     */ import org.aspectj.weaver.tools.ShadowMatch;
/*     */ import org.springframework.aop.ClassFilter;
/*     */ import org.springframework.aop.IntroductionAwareMethodMatcher;
/*     */ import org.springframework.aop.MethodMatcher;
/*     */ import org.springframework.aop.ProxyMethodInvocation;
/*     */ import org.springframework.aop.framework.autoproxy.ProxyCreationContext;
/*     */ import org.springframework.aop.interceptor.ExposeInvocationInterceptor;
/*     */ import org.springframework.aop.support.AbstractExpressionPointcut;
/*     */ import org.springframework.aop.support.AopUtils;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryAware;
/*     */ import org.springframework.beans.factory.BeanFactoryUtils;
/*     */ import org.springframework.beans.factory.FactoryBean;
/*     */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ObjectUtils;
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
/*     */ public class AspectJExpressionPointcut
/*     */   extends AbstractExpressionPointcut
/*     */   implements ClassFilter, IntroductionAwareMethodMatcher, BeanFactoryAware
/*     */ {
/*  84 */   private static final Set<PointcutPrimitive> SUPPORTED_PRIMITIVES = new HashSet<PointcutPrimitive>();
/*     */   
/*     */   static {
/*  87 */     SUPPORTED_PRIMITIVES.add(PointcutPrimitive.EXECUTION);
/*  88 */     SUPPORTED_PRIMITIVES.add(PointcutPrimitive.ARGS);
/*  89 */     SUPPORTED_PRIMITIVES.add(PointcutPrimitive.REFERENCE);
/*  90 */     SUPPORTED_PRIMITIVES.add(PointcutPrimitive.THIS);
/*  91 */     SUPPORTED_PRIMITIVES.add(PointcutPrimitive.TARGET);
/*  92 */     SUPPORTED_PRIMITIVES.add(PointcutPrimitive.WITHIN);
/*  93 */     SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_ANNOTATION);
/*  94 */     SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_WITHIN);
/*  95 */     SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_ARGS);
/*  96 */     SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_TARGET);
/*     */   }
/*     */ 
/*     */   
/* 100 */   private static final Log logger = LogFactory.getLog(AspectJExpressionPointcut.class);
/*     */   
/*     */   private Class<?> pointcutDeclarationScope;
/*     */   
/* 104 */   private String[] pointcutParameterNames = new String[0];
/*     */   
/* 106 */   private Class<?>[] pointcutParameterTypes = new Class[0];
/*     */   
/*     */   private BeanFactory beanFactory;
/*     */   
/*     */   private transient ClassLoader pointcutClassLoader;
/*     */   
/*     */   private transient PointcutExpression pointcutExpression;
/*     */   
/* 114 */   private transient Map<Method, ShadowMatch> shadowMatchCache = new ConcurrentHashMap<Method, ShadowMatch>(32);
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
/*     */   public AspectJExpressionPointcut(Class<?> declarationScope, String[] paramNames, Class<?>[] paramTypes) {
/* 130 */     this.pointcutDeclarationScope = declarationScope;
/* 131 */     if (paramNames.length != paramTypes.length) {
/* 132 */       throw new IllegalStateException("Number of pointcut parameter names must match number of pointcut parameter types");
/*     */     }
/*     */     
/* 135 */     this.pointcutParameterNames = paramNames;
/* 136 */     this.pointcutParameterTypes = paramTypes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPointcutDeclarationScope(Class<?> pointcutDeclarationScope) {
/* 144 */     this.pointcutDeclarationScope = pointcutDeclarationScope;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setParameterNames(String... names) {
/* 151 */     this.pointcutParameterNames = names;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setParameterTypes(Class<?>... types) {
/* 158 */     this.pointcutParameterTypes = types;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBeanFactory(BeanFactory beanFactory) {
/* 163 */     this.beanFactory = beanFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassFilter getClassFilter() {
/* 169 */     checkReadyToMatch();
/* 170 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public MethodMatcher getMethodMatcher() {
/* 175 */     checkReadyToMatch();
/* 176 */     return (MethodMatcher)this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkReadyToMatch() {
/* 185 */     if (getExpression() == null) {
/* 186 */       throw new IllegalStateException("Must set property 'expression' before attempting to match");
/*     */     }
/* 188 */     if (this.pointcutExpression == null) {
/* 189 */       this.pointcutClassLoader = determinePointcutClassLoader();
/* 190 */       this.pointcutExpression = buildPointcutExpression(this.pointcutClassLoader);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ClassLoader determinePointcutClassLoader() {
/* 198 */     if (this.beanFactory instanceof ConfigurableBeanFactory) {
/* 199 */       return ((ConfigurableBeanFactory)this.beanFactory).getBeanClassLoader();
/*     */     }
/* 201 */     if (this.pointcutDeclarationScope != null) {
/* 202 */       return this.pointcutDeclarationScope.getClassLoader();
/*     */     }
/* 204 */     return ClassUtils.getDefaultClassLoader();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private PointcutExpression buildPointcutExpression(ClassLoader classLoader) {
/* 211 */     PointcutParser parser = initializePointcutParser(classLoader);
/* 212 */     PointcutParameter[] pointcutParameters = new PointcutParameter[this.pointcutParameterNames.length];
/* 213 */     for (int i = 0; i < pointcutParameters.length; i++) {
/* 214 */       pointcutParameters[i] = parser.createPointcutParameter(this.pointcutParameterNames[i], this.pointcutParameterTypes[i]);
/*     */     }
/*     */     
/* 217 */     return parser.parsePointcutExpression(replaceBooleanOperators(getExpression()), this.pointcutDeclarationScope, pointcutParameters);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private PointcutParser initializePointcutParser(ClassLoader cl) {
/* 226 */     PointcutParser parser = PointcutParser.getPointcutParserSupportingSpecifiedPrimitivesAndUsingSpecifiedClassLoaderForResolution(SUPPORTED_PRIMITIVES, cl);
/*     */     
/* 228 */     parser.registerPointcutDesignatorHandler(new BeanNamePointcutDesignatorHandler());
/* 229 */     return parser;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String replaceBooleanOperators(String pcExpr) {
/* 240 */     String result = StringUtils.replace(pcExpr, " and ", " && ");
/* 241 */     result = StringUtils.replace(result, " or ", " || ");
/* 242 */     result = StringUtils.replace(result, " not ", " ! ");
/* 243 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PointcutExpression getPointcutExpression() {
/* 251 */     checkReadyToMatch();
/* 252 */     return this.pointcutExpression;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean matches(Class<?> targetClass) {
/* 257 */     checkReadyToMatch();
/*     */     
/*     */     try {
/* 260 */       return this.pointcutExpression.couldMatchJoinPointsInType(targetClass);
/*     */     }
/* 262 */     catch (org.aspectj.weaver.reflect.ReflectionWorld.ReflectionWorldException ex) {
/* 263 */       logger.debug("PointcutExpression matching rejected target class - trying fallback expression", (Throwable)ex);
/*     */       
/* 265 */       PointcutExpression fallbackExpression = getFallbackPointcutExpression(targetClass);
/* 266 */       if (fallbackExpression != null) {
/* 267 */         return fallbackExpression.couldMatchJoinPointsInType(targetClass);
/*     */       
/*     */       }
/*     */     }
/* 271 */     catch (Throwable ex) {
/* 272 */       logger.debug("PointcutExpression matching rejected target class", ex);
/*     */     } 
/* 274 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean matches(Method method, Class<?> targetClass, boolean beanHasIntroductions) {
/* 279 */     checkReadyToMatch();
/* 280 */     Method targetMethod = AopUtils.getMostSpecificMethod(method, targetClass);
/* 281 */     ShadowMatch shadowMatch = getShadowMatch(targetMethod, method);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 286 */     if (shadowMatch.alwaysMatches()) {
/* 287 */       return true;
/*     */     }
/* 289 */     if (shadowMatch.neverMatches()) {
/* 290 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 294 */     if (beanHasIntroductions) {
/* 295 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 301 */     RuntimeTestWalker walker = getRuntimeTestWalker(shadowMatch);
/* 302 */     return (!walker.testsSubtypeSensitiveVars() || walker.testTargetInstanceOfResidue(targetClass));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean matches(Method method, Class<?> targetClass) {
/* 308 */     return matches(method, targetClass, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isRuntime() {
/* 313 */     checkReadyToMatch();
/* 314 */     return this.pointcutExpression.mayNeedDynamicTest();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean matches(Method method, Class<?> targetClass, Object... args) {
/* 319 */     checkReadyToMatch();
/* 320 */     ShadowMatch shadowMatch = getShadowMatch(AopUtils.getMostSpecificMethod(method, targetClass), method);
/* 321 */     ShadowMatch originalShadowMatch = getShadowMatch(method, method);
/*     */ 
/*     */ 
/*     */     
/* 325 */     ProxyMethodInvocation pmi = null;
/* 326 */     Object targetObject = null;
/* 327 */     Object thisObject = null;
/*     */     try {
/* 329 */       MethodInvocation mi = ExposeInvocationInterceptor.currentInvocation();
/* 330 */       targetObject = mi.getThis();
/* 331 */       if (!(mi instanceof ProxyMethodInvocation)) {
/* 332 */         throw new IllegalStateException("MethodInvocation is not a Spring ProxyMethodInvocation: " + mi);
/*     */       }
/* 334 */       pmi = (ProxyMethodInvocation)mi;
/* 335 */       thisObject = pmi.getProxy();
/*     */     }
/* 337 */     catch (IllegalStateException ex) {
/*     */       
/* 339 */       if (logger.isDebugEnabled()) {
/* 340 */         logger.debug("Could not access current invocation - matching with limited context: " + ex);
/*     */       }
/*     */     } 
/*     */     
/*     */     try {
/* 345 */       JoinPointMatch joinPointMatch = shadowMatch.matchesJoinPoint(thisObject, targetObject, args);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 355 */       if (pmi != null) {
/* 356 */         RuntimeTestWalker originalMethodResidueTest = getRuntimeTestWalker(originalShadowMatch);
/* 357 */         if (!originalMethodResidueTest.testThisInstanceOfResidue(thisObject.getClass())) {
/* 358 */           return false;
/*     */         }
/* 360 */         if (joinPointMatch.matches()) {
/* 361 */           bindParameters(pmi, joinPointMatch);
/*     */         }
/*     */       } 
/*     */       
/* 365 */       return joinPointMatch.matches();
/*     */     }
/* 367 */     catch (Throwable ex) {
/* 368 */       if (logger.isDebugEnabled()) {
/* 369 */         logger.debug("Failed to evaluate join point for arguments " + Arrays.<Object>asList(args) + " - falling back to non-match", ex);
/*     */       }
/*     */       
/* 372 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   protected String getCurrentProxiedBeanName() {
/* 377 */     return ProxyCreationContext.getCurrentProxiedBeanName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private PointcutExpression getFallbackPointcutExpression(Class<?> targetClass) {
/*     */     try {
/* 386 */       ClassLoader classLoader = targetClass.getClassLoader();
/* 387 */       if (classLoader != null && classLoader != this.pointcutClassLoader) {
/* 388 */         return buildPointcutExpression(classLoader);
/*     */       }
/*     */     }
/* 391 */     catch (Throwable ex) {
/* 392 */       logger.debug("Failed to create fallback PointcutExpression", ex);
/*     */     } 
/* 394 */     return null;
/*     */   }
/*     */   
/*     */   private RuntimeTestWalker getRuntimeTestWalker(ShadowMatch shadowMatch) {
/* 398 */     if (shadowMatch instanceof DefensiveShadowMatch) {
/* 399 */       return new RuntimeTestWalker(((DefensiveShadowMatch)shadowMatch).primary);
/*     */     }
/* 401 */     return new RuntimeTestWalker(shadowMatch);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void bindParameters(ProxyMethodInvocation invocation, JoinPointMatch jpm) {
/* 411 */     invocation.setUserAttribute(getExpression(), jpm);
/*     */   }
/*     */ 
/*     */   
/*     */   private ShadowMatch getShadowMatch(Method targetMethod, Method originalMethod) {
/* 416 */     ShadowMatch shadowMatch = this.shadowMatchCache.get(targetMethod);
/* 417 */     if (shadowMatch == null) {
/* 418 */       synchronized (this.shadowMatchCache) {
/*     */         
/* 420 */         PointcutExpression fallbackExpression = null;
/* 421 */         Method methodToMatch = targetMethod;
/* 422 */         shadowMatch = this.shadowMatchCache.get(targetMethod);
/* 423 */         if (shadowMatch == null) {
/*     */           ShadowMatchImpl shadowMatchImpl; try {
/*     */             try {
/* 426 */               shadowMatch = this.pointcutExpression.matchesMethodExecution(methodToMatch);
/*     */             }
/* 428 */             catch (org.aspectj.weaver.reflect.ReflectionWorld.ReflectionWorldException ex) {
/*     */ 
/*     */               
/*     */               try {
/* 432 */                 fallbackExpression = getFallbackPointcutExpression(methodToMatch.getDeclaringClass());
/* 433 */                 if (fallbackExpression != null) {
/* 434 */                   shadowMatch = fallbackExpression.matchesMethodExecution(methodToMatch);
/*     */                 }
/*     */               }
/* 437 */               catch (org.aspectj.weaver.reflect.ReflectionWorld.ReflectionWorldException ex2) {
/* 438 */                 fallbackExpression = null;
/*     */               } 
/*     */             } 
/* 441 */             if (shadowMatch == null && targetMethod != originalMethod) {
/* 442 */               methodToMatch = originalMethod;
/*     */               try {
/* 444 */                 shadowMatch = this.pointcutExpression.matchesMethodExecution(methodToMatch);
/*     */               }
/* 446 */               catch (org.aspectj.weaver.reflect.ReflectionWorld.ReflectionWorldException ex3) {
/*     */ 
/*     */                 
/*     */                 try {
/* 450 */                   fallbackExpression = getFallbackPointcutExpression(methodToMatch.getDeclaringClass());
/* 451 */                   if (fallbackExpression != null) {
/* 452 */                     shadowMatch = fallbackExpression.matchesMethodExecution(methodToMatch);
/*     */                   }
/*     */                 }
/* 455 */                 catch (org.aspectj.weaver.reflect.ReflectionWorld.ReflectionWorldException ex4) {
/* 456 */                   fallbackExpression = null;
/*     */                 }
/*     */               
/*     */               } 
/*     */             } 
/* 461 */           } catch (Throwable ex) {
/*     */             
/* 463 */             logger.debug("PointcutExpression matching rejected target method", ex);
/* 464 */             fallbackExpression = null;
/*     */           } 
/* 466 */           if (shadowMatch == null) {
/* 467 */             shadowMatchImpl = new ShadowMatchImpl(FuzzyBoolean.NO, null, null, null);
/*     */           }
/* 469 */           else if (shadowMatchImpl.maybeMatches() && fallbackExpression != null) {
/*     */             
/* 471 */             shadowMatch = new DefensiveShadowMatch((ShadowMatch)shadowMatchImpl, fallbackExpression.matchesMethodExecution(methodToMatch));
/*     */           } 
/* 473 */           this.shadowMatchCache.put(targetMethod, shadowMatch);
/*     */         } 
/*     */       } 
/*     */     }
/* 477 */     return shadowMatch;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 483 */     if (this == other) {
/* 484 */       return true;
/*     */     }
/* 486 */     if (!(other instanceof AspectJExpressionPointcut)) {
/* 487 */       return false;
/*     */     }
/* 489 */     AspectJExpressionPointcut otherPc = (AspectJExpressionPointcut)other;
/* 490 */     return (ObjectUtils.nullSafeEquals(getExpression(), otherPc.getExpression()) && 
/* 491 */       ObjectUtils.nullSafeEquals(this.pointcutDeclarationScope, otherPc.pointcutDeclarationScope) && 
/* 492 */       ObjectUtils.nullSafeEquals(this.pointcutParameterNames, otherPc.pointcutParameterNames) && 
/* 493 */       ObjectUtils.nullSafeEquals(this.pointcutParameterTypes, otherPc.pointcutParameterTypes));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 498 */     int hashCode = ObjectUtils.nullSafeHashCode(getExpression());
/* 499 */     hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode(this.pointcutDeclarationScope);
/* 500 */     hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode((Object[])this.pointcutParameterNames);
/* 501 */     hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode((Object[])this.pointcutParameterTypes);
/* 502 */     return hashCode;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 507 */     StringBuilder sb = new StringBuilder();
/* 508 */     sb.append("AspectJExpressionPointcut: ");
/* 509 */     if (this.pointcutParameterNames != null && this.pointcutParameterTypes != null) {
/* 510 */       sb.append("(");
/* 511 */       for (int i = 0; i < this.pointcutParameterTypes.length; i++) {
/* 512 */         sb.append(this.pointcutParameterTypes[i].getName());
/* 513 */         sb.append(" ");
/* 514 */         sb.append(this.pointcutParameterNames[i]);
/* 515 */         if (i + 1 < this.pointcutParameterTypes.length) {
/* 516 */           sb.append(", ");
/*     */         }
/*     */       } 
/* 519 */       sb.append(")");
/*     */     } 
/* 521 */     sb.append(" ");
/* 522 */     if (getExpression() != null) {
/* 523 */       sb.append(getExpression());
/*     */     } else {
/*     */       
/* 526 */       sb.append("<pointcut expression not set>");
/*     */     } 
/* 528 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class BeanNamePointcutDesignatorHandler
/*     */     implements PointcutDesignatorHandler
/*     */   {
/*     */     private static final String BEAN_DESIGNATOR_NAME = "bean";
/*     */ 
/*     */ 
/*     */     
/*     */     private BeanNamePointcutDesignatorHandler() {}
/*     */ 
/*     */ 
/*     */     
/*     */     public String getDesignatorName() {
/* 546 */       return "bean";
/*     */     }
/*     */ 
/*     */     
/*     */     public ContextBasedMatcher parse(String expression) {
/* 551 */       return new AspectJExpressionPointcut.BeanNameContextMatcher(expression);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class BeanNameContextMatcher
/*     */     implements ContextBasedMatcher
/*     */   {
/*     */     private final NamePattern expressionPattern;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public BeanNameContextMatcher(String expression) {
/* 568 */       this.expressionPattern = new NamePattern(expression);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public boolean couldMatchJoinPointsInType(Class<?> someClass) {
/* 575 */       return (contextMatch(someClass) == FuzzyBoolean.YES);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public boolean couldMatchJoinPointsInType(Class<?> someClass, MatchingContext context) {
/* 582 */       return (contextMatch(someClass) == FuzzyBoolean.YES);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean matchesDynamically(MatchingContext context) {
/* 587 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public FuzzyBoolean matchesStatically(MatchingContext context) {
/* 592 */       return contextMatch(null);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean mayNeedDynamicTest() {
/* 597 */       return false;
/*     */     }
/*     */     
/*     */     private FuzzyBoolean contextMatch(Class<?> targetType) {
/* 601 */       String advisedBeanName = AspectJExpressionPointcut.this.getCurrentProxiedBeanName();
/* 602 */       if (advisedBeanName == null)
/*     */       {
/* 604 */         return FuzzyBoolean.MAYBE;
/*     */       }
/* 606 */       if (BeanFactoryUtils.isGeneratedBeanName(advisedBeanName)) {
/* 607 */         return FuzzyBoolean.NO;
/*     */       }
/* 609 */       if (targetType != null) {
/* 610 */         boolean isFactory = FactoryBean.class.isAssignableFrom(targetType);
/* 611 */         return FuzzyBoolean.fromBoolean(
/* 612 */             matchesBeanName(isFactory ? ("&" + advisedBeanName) : advisedBeanName));
/*     */       } 
/*     */       
/* 615 */       return FuzzyBoolean.fromBoolean((matchesBeanName(advisedBeanName) || 
/* 616 */           matchesBeanName("&" + advisedBeanName)));
/*     */     }
/*     */ 
/*     */     
/*     */     private boolean matchesBeanName(String advisedBeanName) {
/* 621 */       if (this.expressionPattern.matches(advisedBeanName)) {
/* 622 */         return true;
/*     */       }
/* 624 */       if (AspectJExpressionPointcut.this.beanFactory != null) {
/* 625 */         String[] aliases = AspectJExpressionPointcut.this.beanFactory.getAliases(advisedBeanName);
/* 626 */         for (String alias : aliases) {
/* 627 */           if (this.expressionPattern.matches(alias)) {
/* 628 */             return true;
/*     */           }
/*     */         } 
/*     */       } 
/* 632 */       return false;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
/* 643 */     ois.defaultReadObject();
/*     */ 
/*     */ 
/*     */     
/* 647 */     this.shadowMatchCache = new ConcurrentHashMap<Method, ShadowMatch>(32);
/*     */   }
/*     */   
/*     */   public AspectJExpressionPointcut() {}
/*     */   
/*     */   private static class DefensiveShadowMatch
/*     */     implements ShadowMatch {
/*     */     private final ShadowMatch primary;
/*     */     private final ShadowMatch other;
/*     */     
/*     */     public DefensiveShadowMatch(ShadowMatch primary, ShadowMatch other) {
/* 658 */       this.primary = primary;
/* 659 */       this.other = other;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean alwaysMatches() {
/* 664 */       return this.primary.alwaysMatches();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean maybeMatches() {
/* 669 */       return this.primary.maybeMatches();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean neverMatches() {
/* 674 */       return this.primary.neverMatches();
/*     */     }
/*     */ 
/*     */     
/*     */     public JoinPointMatch matchesJoinPoint(Object thisObject, Object targetObject, Object[] args) {
/*     */       try {
/* 680 */         return this.primary.matchesJoinPoint(thisObject, targetObject, args);
/*     */       }
/* 682 */       catch (org.aspectj.weaver.reflect.ReflectionWorld.ReflectionWorldException ex) {
/* 683 */         return this.other.matchesJoinPoint(thisObject, targetObject, args);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void setMatchingContext(MatchingContext aMatchContext) {
/* 689 */       this.primary.setMatchingContext(aMatchContext);
/* 690 */       this.other.setMatchingContext(aMatchContext);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\aspectj\AspectJExpressionPointcut.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */