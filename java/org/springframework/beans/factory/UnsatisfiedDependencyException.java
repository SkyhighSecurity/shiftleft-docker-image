/*     */ package org.springframework.beans.factory;
/*     */ 
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.util.ClassUtils;
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
/*     */ public class UnsatisfiedDependencyException
/*     */   extends BeanCreationException
/*     */ {
/*     */   private InjectionPoint injectionPoint;
/*     */   
/*     */   public UnsatisfiedDependencyException(String resourceDescription, String beanName, String propertyName, String msg) {
/*  48 */     super(resourceDescription, beanName, "Unsatisfied dependency expressed through bean property '" + propertyName + "'" + (
/*     */         
/*  50 */         StringUtils.hasLength(msg) ? (": " + msg) : ""));
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
/*     */   public UnsatisfiedDependencyException(String resourceDescription, String beanName, String propertyName, BeansException ex) {
/*  63 */     this(resourceDescription, beanName, propertyName, "");
/*  64 */     initCause((Throwable)ex);
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
/*     */   public UnsatisfiedDependencyException(String resourceDescription, String beanName, InjectionPoint injectionPoint, String msg) {
/*  78 */     super(resourceDescription, beanName, "Unsatisfied dependency expressed through " + injectionPoint + (
/*     */         
/*  80 */         StringUtils.hasLength(msg) ? (": " + msg) : ""));
/*  81 */     this.injectionPoint = injectionPoint;
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
/*     */   public UnsatisfiedDependencyException(String resourceDescription, String beanName, InjectionPoint injectionPoint, BeansException ex) {
/*  95 */     this(resourceDescription, beanName, injectionPoint, "");
/*  96 */     initCause((Throwable)ex);
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
/*     */   @Deprecated
/*     */   public UnsatisfiedDependencyException(String resourceDescription, String beanName, int ctorArgIndex, Class<?> ctorArgType, String msg) {
/* 112 */     super(resourceDescription, beanName, "Unsatisfied dependency expressed through constructor argument with index " + ctorArgIndex + " of type [" + 
/*     */         
/* 114 */         ClassUtils.getQualifiedName(ctorArgType) + "]" + ((msg != null) ? (": " + msg) : ""));
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
/*     */   @Deprecated
/*     */   public UnsatisfiedDependencyException(String resourceDescription, String beanName, int ctorArgIndex, Class<?> ctorArgType, BeansException ex) {
/* 131 */     this(resourceDescription, beanName, ctorArgIndex, ctorArgType, (ex != null) ? ex.getMessage() : "");
/* 132 */     initCause((Throwable)ex);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InjectionPoint getInjectionPoint() {
/* 141 */     return this.injectionPoint;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\UnsatisfiedDependencyException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */