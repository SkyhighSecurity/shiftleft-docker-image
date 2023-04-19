/*    */ package org.springframework.aop.aspectj;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import org.springframework.core.Ordered;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SingletonAspectInstanceFactory
/*    */   implements AspectInstanceFactory, Serializable
/*    */ {
/*    */   private final Object aspectInstance;
/*    */   
/*    */   public SingletonAspectInstanceFactory(Object aspectInstance) {
/* 45 */     Assert.notNull(aspectInstance, "Aspect instance must not be null");
/* 46 */     this.aspectInstance = aspectInstance;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public final Object getAspectInstance() {
/* 52 */     return this.aspectInstance;
/*    */   }
/*    */ 
/*    */   
/*    */   public ClassLoader getAspectClassLoader() {
/* 57 */     return this.aspectInstance.getClass().getClassLoader();
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
/* 70 */     if (this.aspectInstance instanceof Ordered) {
/* 71 */       return ((Ordered)this.aspectInstance).getOrder();
/*    */     }
/* 73 */     return getOrderForAspectClass(this.aspectInstance.getClass());
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
/* 84 */     return Integer.MAX_VALUE;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\aspectj\SingletonAspectInstanceFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */