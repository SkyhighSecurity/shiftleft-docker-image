/*     */ package org.springframework.aop.support;
/*     */ 
/*     */ import org.aopalliance.intercept.MethodInvocation;
/*     */ import org.springframework.aop.DynamicIntroductionAdvice;
/*     */ import org.springframework.aop.IntroductionInterceptor;
/*     */ import org.springframework.aop.ProxyMethodInvocation;
/*     */ import org.springframework.util.Assert;
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
/*     */ public class DelegatingIntroductionInterceptor
/*     */   extends IntroductionInfoSupport
/*     */   implements IntroductionInterceptor
/*     */ {
/*     */   private Object delegate;
/*     */   
/*     */   public DelegatingIntroductionInterceptor(Object delegate) {
/*  68 */     init(delegate);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected DelegatingIntroductionInterceptor() {
/*  77 */     init(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void init(Object delegate) {
/*  87 */     Assert.notNull(delegate, "Delegate must not be null");
/*  88 */     this.delegate = delegate;
/*  89 */     implementInterfacesOnObject(delegate);
/*     */ 
/*     */     
/*  92 */     suppressInterface(IntroductionInterceptor.class);
/*  93 */     suppressInterface(DynamicIntroductionAdvice.class);
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
/* 104 */     if (isMethodOnIntroducedInterface(mi)) {
/*     */ 
/*     */ 
/*     */       
/* 108 */       Object retVal = AopUtils.invokeJoinpointUsingReflection(this.delegate, mi.getMethod(), mi.getArguments());
/*     */ 
/*     */ 
/*     */       
/* 112 */       if (retVal == this.delegate && mi instanceof ProxyMethodInvocation) {
/* 113 */         Object proxy = ((ProxyMethodInvocation)mi).getProxy();
/* 114 */         if (mi.getMethod().getReturnType().isInstance(proxy)) {
/* 115 */           retVal = proxy;
/*     */         }
/*     */       } 
/* 118 */       return retVal;
/*     */     } 
/*     */     
/* 121 */     return doProceed(mi);
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
/* 133 */     return mi.proceed();
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\support\DelegatingIntroductionInterceptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */