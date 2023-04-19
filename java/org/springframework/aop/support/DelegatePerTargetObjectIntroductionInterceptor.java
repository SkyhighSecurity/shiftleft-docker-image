/*     */ package org.springframework.aop.support;
/*     */ 
/*     */ import java.util.Map;
/*     */ import java.util.WeakHashMap;
/*     */ import org.aopalliance.intercept.MethodInvocation;
/*     */ import org.springframework.aop.DynamicIntroductionAdvice;
/*     */ import org.springframework.aop.IntroductionInterceptor;
/*     */ import org.springframework.aop.ProxyMethodInvocation;
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
/*     */ public class DelegatePerTargetObjectIntroductionInterceptor
/*     */   extends IntroductionInfoSupport
/*     */   implements IntroductionInterceptor
/*     */ {
/*  60 */   private final Map<Object, Object> delegateMap = new WeakHashMap<Object, Object>();
/*     */   
/*     */   private Class<?> defaultImplType;
/*     */   
/*     */   private Class<?> interfaceType;
/*     */ 
/*     */   
/*     */   public DelegatePerTargetObjectIntroductionInterceptor(Class<?> defaultImplType, Class<?> interfaceType) {
/*  68 */     this.defaultImplType = defaultImplType;
/*  69 */     this.interfaceType = interfaceType;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  74 */     Object delegate = createNewDelegate();
/*  75 */     implementInterfacesOnObject(delegate);
/*  76 */     suppressInterface(IntroductionInterceptor.class);
/*  77 */     suppressInterface(DynamicIntroductionAdvice.class);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object invoke(MethodInvocation mi) throws Throwable {
/*  88 */     if (isMethodOnIntroducedInterface(mi)) {
/*  89 */       Object delegate = getIntroductionDelegateFor(mi.getThis());
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  94 */       Object retVal = AopUtils.invokeJoinpointUsingReflection(delegate, mi.getMethod(), mi.getArguments());
/*     */ 
/*     */ 
/*     */       
/*  98 */       if (retVal == delegate && mi instanceof ProxyMethodInvocation) {
/*  99 */         retVal = ((ProxyMethodInvocation)mi).getProxy();
/*     */       }
/* 101 */       return retVal;
/*     */     } 
/*     */     
/* 104 */     return doProceed(mi);
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
/*     */   protected Object doProceed(MethodInvocation mi) throws Throwable {
/* 116 */     return mi.proceed();
/*     */   }
/*     */   
/*     */   private Object getIntroductionDelegateFor(Object targetObject) {
/* 120 */     synchronized (this.delegateMap) {
/* 121 */       if (this.delegateMap.containsKey(targetObject)) {
/* 122 */         return this.delegateMap.get(targetObject);
/*     */       }
/*     */       
/* 125 */       Object delegate = createNewDelegate();
/* 126 */       this.delegateMap.put(targetObject, delegate);
/* 127 */       return delegate;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private Object createNewDelegate() {
/*     */     try {
/* 134 */       return this.defaultImplType.newInstance();
/*     */     }
/* 136 */     catch (Throwable ex) {
/* 137 */       throw new IllegalArgumentException("Cannot create default implementation for '" + this.interfaceType
/* 138 */           .getName() + "' mixin (" + this.defaultImplType.getName() + "): " + ex);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\support\DelegatePerTargetObjectIntroductionInterceptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */