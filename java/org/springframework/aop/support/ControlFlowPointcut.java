/*     */ package org.springframework.aop.support;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Method;
/*     */ import org.springframework.aop.ClassFilter;
/*     */ import org.springframework.aop.MethodMatcher;
/*     */ import org.springframework.aop.Pointcut;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ControlFlowPointcut
/*     */   implements Pointcut, ClassFilter, MethodMatcher, Serializable
/*     */ {
/*     */   private Class<?> clazz;
/*     */   private String methodName;
/*     */   private volatile int evaluations;
/*     */   
/*     */   public ControlFlowPointcut(Class<?> clazz) {
/*  52 */     this(clazz, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ControlFlowPointcut(Class<?> clazz, String methodName) {
/*  63 */     Assert.notNull(clazz, "Class must not be null");
/*  64 */     this.clazz = clazz;
/*  65 */     this.methodName = methodName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean matches(Class<?> clazz) {
/*  74 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean matches(Method method, Class<?> targetClass) {
/*  83 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isRuntime() {
/*  88 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean matches(Method method, Class<?> targetClass, Object... args) {
/*  93 */     this.evaluations++;
/*     */     
/*  95 */     for (StackTraceElement element : (new Throwable()).getStackTrace()) {
/*  96 */       if (element.getClassName().equals(this.clazz.getName()) && (this.methodName == null || element
/*  97 */         .getMethodName().equals(this.methodName))) {
/*  98 */         return true;
/*     */       }
/*     */     } 
/* 101 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getEvaluations() {
/* 108 */     return this.evaluations;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassFilter getClassFilter() {
/* 114 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public MethodMatcher getMethodMatcher() {
/* 119 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 125 */     if (this == other) {
/* 126 */       return true;
/*     */     }
/* 128 */     if (!(other instanceof ControlFlowPointcut)) {
/* 129 */       return false;
/*     */     }
/* 131 */     ControlFlowPointcut that = (ControlFlowPointcut)other;
/* 132 */     return (this.clazz.equals(that.clazz) && ObjectUtils.nullSafeEquals(that.methodName, this.methodName));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 137 */     int code = this.clazz.hashCode();
/* 138 */     if (this.methodName != null) {
/* 139 */       code = 37 * code + this.methodName.hashCode();
/*     */     }
/* 141 */     return code;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\support\ControlFlowPointcut.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */