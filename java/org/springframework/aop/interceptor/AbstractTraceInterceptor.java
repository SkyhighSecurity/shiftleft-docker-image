/*     */ package org.springframework.aop.interceptor;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import org.aopalliance.intercept.MethodInterceptor;
/*     */ import org.aopalliance.intercept.MethodInvocation;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.aop.support.AopUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractTraceInterceptor
/*     */   implements MethodInterceptor, Serializable
/*     */ {
/*  53 */   protected transient Log defaultLogger = LogFactory.getLog(getClass());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean hideProxyClassNames = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean logExceptionStackTrace = true;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUseDynamicLogger(boolean useDynamicLogger) {
/*  80 */     this.defaultLogger = useDynamicLogger ? null : LogFactory.getLog(getClass());
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
/*     */   public void setLoggerName(String loggerName) {
/*  96 */     this.defaultLogger = LogFactory.getLog(loggerName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHideProxyClassNames(boolean hideProxyClassNames) {
/* 104 */     this.hideProxyClassNames = hideProxyClassNames;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLogExceptionStackTrace(boolean logExceptionStackTrace) {
/* 115 */     this.logExceptionStackTrace = logExceptionStackTrace;
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
/*     */   public Object invoke(MethodInvocation invocation) throws Throwable {
/* 127 */     Log logger = getLoggerForInvocation(invocation);
/* 128 */     if (isInterceptorEnabled(invocation, logger)) {
/* 129 */       return invokeUnderTrace(invocation, logger);
/*     */     }
/*     */     
/* 132 */     return invocation.proceed();
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
/*     */   protected Log getLoggerForInvocation(MethodInvocation invocation) {
/* 147 */     if (this.defaultLogger != null) {
/* 148 */       return this.defaultLogger;
/*     */     }
/*     */     
/* 151 */     Object target = invocation.getThis();
/* 152 */     return LogFactory.getLog(getClassForLogging(target));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Class<?> getClassForLogging(Object target) {
/* 163 */     return this.hideProxyClassNames ? AopUtils.getTargetClass(target) : target.getClass();
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
/*     */   protected boolean isInterceptorEnabled(MethodInvocation invocation, Log logger) {
/* 178 */     return isLogEnabled(logger);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isLogEnabled(Log logger) {
/* 188 */     return logger.isTraceEnabled();
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
/*     */   protected void writeToLog(Log logger, String message) {
/* 200 */     writeToLog(logger, message, null);
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
/*     */   protected void writeToLog(Log logger, String message, Throwable ex) {
/* 217 */     if (ex != null && this.logExceptionStackTrace) {
/* 218 */       logger.trace(message, ex);
/*     */     } else {
/*     */       
/* 221 */       logger.trace(message);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected abstract Object invokeUnderTrace(MethodInvocation paramMethodInvocation, Log paramLog) throws Throwable;
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\interceptor\AbstractTraceInterceptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */