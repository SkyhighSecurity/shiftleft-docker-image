/*     */ package org.springframework.beans.factory.support;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
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
/*     */ public class LookupOverride
/*     */   extends MethodOverride
/*     */ {
/*     */   private final String beanName;
/*     */   private Method method;
/*     */   
/*     */   public LookupOverride(String methodName, String beanName) {
/*  47 */     super(methodName);
/*  48 */     this.beanName = beanName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LookupOverride(Method method, String beanName) {
/*  58 */     super(method.getName());
/*  59 */     this.method = method;
/*  60 */     this.beanName = beanName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getBeanName() {
/*  68 */     return this.beanName;
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
/*     */   public boolean matches(Method method) {
/*  81 */     if (this.method != null) {
/*  82 */       return method.equals(this.method);
/*     */     }
/*     */     
/*  85 */     return (method.getName().equals(getMethodName()) && (!isOverloaded() || 
/*  86 */       Modifier.isAbstract(method.getModifiers()) || (method.getParameterTypes()).length == 0));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/*  93 */     if (!(other instanceof LookupOverride) || !super.equals(other)) {
/*  94 */       return false;
/*     */     }
/*  96 */     LookupOverride that = (LookupOverride)other;
/*  97 */     return (ObjectUtils.nullSafeEquals(this.method, that.method) && 
/*  98 */       ObjectUtils.nullSafeEquals(this.beanName, that.beanName));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 103 */     return 29 * super.hashCode() + ObjectUtils.nullSafeHashCode(this.beanName);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 108 */     return "LookupOverride for method '" + getMethodName() + "'";
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\support\LookupOverride.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */