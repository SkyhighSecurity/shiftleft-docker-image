/*     */ package org.springframework.beans;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Method;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BeanInstantiationException
/*     */   extends FatalBeanException
/*     */ {
/*     */   private Class<?> beanClass;
/*     */   private Constructor<?> constructor;
/*     */   private Method constructingMethod;
/*     */   
/*     */   public BeanInstantiationException(Class<?> beanClass, String msg) {
/*  45 */     this(beanClass, msg, (Throwable)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanInstantiationException(Class<?> beanClass, String msg, Throwable cause) {
/*  55 */     super("Failed to instantiate [" + beanClass.getName() + "]: " + msg, cause);
/*  56 */     this.beanClass = beanClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanInstantiationException(Constructor<?> constructor, String msg, Throwable cause) {
/*  67 */     super("Failed to instantiate [" + constructor.getDeclaringClass().getName() + "]: " + msg, cause);
/*  68 */     this.beanClass = constructor.getDeclaringClass();
/*  69 */     this.constructor = constructor;
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
/*     */   public BeanInstantiationException(Method constructingMethod, String msg, Throwable cause) {
/*  81 */     super("Failed to instantiate [" + constructingMethod.getReturnType().getName() + "]: " + msg, cause);
/*  82 */     this.beanClass = constructingMethod.getReturnType();
/*  83 */     this.constructingMethod = constructingMethod;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?> getBeanClass() {
/*  92 */     return this.beanClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Constructor<?> getConstructor() {
/* 102 */     return this.constructor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Method getConstructingMethod() {
/* 112 */     return this.constructingMethod;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\BeanInstantiationException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */