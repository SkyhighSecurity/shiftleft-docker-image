/*     */ package org.springframework.aop.interceptor;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import org.aopalliance.aop.Advice;
/*     */ import org.aopalliance.intercept.MethodInterceptor;
/*     */ import org.aopalliance.intercept.MethodInvocation;
/*     */ import org.springframework.aop.Advisor;
/*     */ import org.springframework.aop.support.DefaultPointcutAdvisor;
/*     */ import org.springframework.core.NamedThreadLocal;
/*     */ import org.springframework.core.PriorityOrdered;
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
/*     */ public class ExposeInvocationInterceptor
/*     */   implements MethodInterceptor, PriorityOrdered, Serializable
/*     */ {
/*  47 */   public static final ExposeInvocationInterceptor INSTANCE = new ExposeInvocationInterceptor();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  53 */   public static final Advisor ADVISOR = (Advisor)new DefaultPointcutAdvisor((Advice)INSTANCE)
/*     */     {
/*     */       public String toString() {
/*  56 */         return ExposeInvocationInterceptor.class.getName() + ".ADVISOR";
/*     */       }
/*     */     };
/*     */   
/*  60 */   private static final ThreadLocal<MethodInvocation> invocation = (ThreadLocal<MethodInvocation>)new NamedThreadLocal("Current AOP method invocation");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static MethodInvocation currentInvocation() throws IllegalStateException {
/*  71 */     MethodInvocation mi = invocation.get();
/*  72 */     if (mi == null) {
/*  73 */       throw new IllegalStateException("No MethodInvocation found: Check that an AOP invocation is in progress, and that the ExposeInvocationInterceptor is upfront in the interceptor chain. Specifically, note that advices with order HIGHEST_PRECEDENCE will execute before ExposeInvocationInterceptor!");
/*     */     }
/*     */ 
/*     */     
/*  77 */     return mi;
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
/*     */   public Object invoke(MethodInvocation mi) throws Throwable {
/*  89 */     MethodInvocation oldInvocation = invocation.get();
/*  90 */     invocation.set(mi);
/*     */     try {
/*  92 */       return mi.proceed();
/*     */     } finally {
/*     */       
/*  95 */       invocation.set(oldInvocation);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int getOrder() {
/* 101 */     return -2147483647;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Object readResolve() {
/* 110 */     return INSTANCE;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\interceptor\ExposeInvocationInterceptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */