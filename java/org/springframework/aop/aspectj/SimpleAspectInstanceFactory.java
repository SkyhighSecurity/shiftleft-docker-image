/*    */ package org.springframework.aop.aspectj;
/*    */ 
/*    */ import org.springframework.aop.framework.AopConfigException;
/*    */ import org.springframework.util.Assert;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SimpleAspectInstanceFactory
/*    */   implements AspectInstanceFactory
/*    */ {
/*    */   private final Class<?> aspectClass;
/*    */   
/*    */   public SimpleAspectInstanceFactory(Class<?> aspectClass) {
/* 40 */     Assert.notNull(aspectClass, "Aspect class must not be null");
/* 41 */     this.aspectClass = aspectClass;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final Class<?> getAspectClass() {
/* 49 */     return this.aspectClass;
/*    */   }
/*    */ 
/*    */   
/*    */   public final Object getAspectInstance() {
/*    */     try {
/* 55 */       return this.aspectClass.newInstance();
/*    */     }
/* 57 */     catch (InstantiationException ex) {
/* 58 */       throw new AopConfigException("Unable to instantiate aspect class: " + this.aspectClass
/* 59 */           .getName(), ex);
/*    */     }
/* 61 */     catch (IllegalAccessException ex) {
/* 62 */       throw new AopConfigException("Could not access aspect constructor: " + this.aspectClass
/* 63 */           .getName(), ex);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public ClassLoader getAspectClassLoader() {
/* 69 */     return this.aspectClass.getClassLoader();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getOrder() {
/* 82 */     return getOrderForAspectClass(this.aspectClass);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected int getOrderForAspectClass(Class<?> aspectClass) {
/* 93 */     return Integer.MAX_VALUE;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\aspectj\SimpleAspectInstanceFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */