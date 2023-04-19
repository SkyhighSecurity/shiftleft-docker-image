/*     */ package org.springframework.aop.interceptor;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import org.aopalliance.intercept.MethodInvocation;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractMonitoringInterceptor
/*     */   extends AbstractTraceInterceptor
/*     */ {
/*  42 */   private String prefix = "";
/*     */   
/*  44 */   private String suffix = "";
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean logTargetClassInvocation = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPrefix(String prefix) {
/*  54 */     this.prefix = (prefix != null) ? prefix : "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getPrefix() {
/*  61 */     return this.prefix;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSuffix(String suffix) {
/*  69 */     this.suffix = (suffix != null) ? suffix : "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getSuffix() {
/*  76 */     return this.suffix;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLogTargetClassInvocation(boolean logTargetClassInvocation) {
/*  86 */     this.logTargetClassInvocation = logTargetClassInvocation;
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
/*     */   protected String createInvocationTraceName(MethodInvocation invocation) {
/*  99 */     StringBuilder sb = new StringBuilder(getPrefix());
/* 100 */     Method method = invocation.getMethod();
/* 101 */     Class<?> clazz = method.getDeclaringClass();
/* 102 */     if (this.logTargetClassInvocation && clazz.isInstance(invocation.getThis())) {
/* 103 */       clazz = invocation.getThis().getClass();
/*     */     }
/* 105 */     sb.append(clazz.getName());
/* 106 */     sb.append('.').append(method.getName());
/* 107 */     sb.append(getSuffix());
/* 108 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\interceptor\AbstractMonitoringInterceptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */