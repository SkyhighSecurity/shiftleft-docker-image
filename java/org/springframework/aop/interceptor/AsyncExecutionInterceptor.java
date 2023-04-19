/*     */ package org.springframework.aop.interceptor;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.Future;
/*     */ import org.aopalliance.intercept.MethodInterceptor;
/*     */ import org.aopalliance.intercept.MethodInvocation;
/*     */ import org.springframework.aop.support.AopUtils;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.core.BridgeMethodResolver;
/*     */ import org.springframework.core.Ordered;
/*     */ import org.springframework.core.task.AsyncTaskExecutor;
/*     */ import org.springframework.core.task.SimpleAsyncTaskExecutor;
/*     */ import org.springframework.util.ClassUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AsyncExecutionInterceptor
/*     */   extends AsyncExecutionAspectSupport
/*     */   implements MethodInterceptor, Ordered
/*     */ {
/*     */   public AsyncExecutionInterceptor(Executor defaultExecutor) {
/*  77 */     super(defaultExecutor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AsyncExecutionInterceptor(Executor defaultExecutor, AsyncUncaughtExceptionHandler exceptionHandler) {
/*  88 */     super(defaultExecutor, exceptionHandler);
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
/*     */   public Object invoke(final MethodInvocation invocation) throws Throwable {
/* 101 */     Class<?> targetClass = (invocation.getThis() != null) ? AopUtils.getTargetClass(invocation.getThis()) : null;
/* 102 */     Method specificMethod = ClassUtils.getMostSpecificMethod(invocation.getMethod(), targetClass);
/* 103 */     final Method userDeclaredMethod = BridgeMethodResolver.findBridgedMethod(specificMethod);
/*     */     
/* 105 */     AsyncTaskExecutor executor = determineAsyncExecutor(userDeclaredMethod);
/* 106 */     if (executor == null) {
/* 107 */       throw new IllegalStateException("No executor specified and no default executor set on AsyncExecutionInterceptor either");
/*     */     }
/*     */ 
/*     */     
/* 111 */     Callable<Object> task = new Callable()
/*     */       {
/*     */         public Object call() throws Exception {
/*     */           try {
/* 115 */             Object result = invocation.proceed();
/* 116 */             if (result instanceof Future) {
/* 117 */               return ((Future)result).get();
/*     */             }
/*     */           }
/* 120 */           catch (ExecutionException ex) {
/* 121 */             AsyncExecutionInterceptor.this.handleError(ex.getCause(), userDeclaredMethod, invocation.getArguments());
/*     */           }
/* 123 */           catch (Throwable ex) {
/* 124 */             AsyncExecutionInterceptor.this.handleError(ex, userDeclaredMethod, invocation.getArguments());
/*     */           } 
/* 126 */           return null;
/*     */         }
/*     */       };
/*     */     
/* 130 */     return doSubmit(task, executor, invocation.getMethod().getReturnType());
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
/*     */   protected String getExecutorQualifier(Method method) {
/* 143 */     return null;
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
/*     */   protected Executor getDefaultExecutor(BeanFactory beanFactory) {
/* 156 */     Executor defaultExecutor = super.getDefaultExecutor(beanFactory);
/* 157 */     return (defaultExecutor != null) ? defaultExecutor : (Executor)new SimpleAsyncTaskExecutor();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getOrder() {
/* 162 */     return Integer.MIN_VALUE;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\interceptor\AsyncExecutionInterceptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */