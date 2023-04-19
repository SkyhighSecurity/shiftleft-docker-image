/*    */ package org.springframework.aop.aspectj.annotation;
/*    */ 
/*    */ import org.springframework.aop.aspectj.SimpleAspectInstanceFactory;
/*    */ import org.springframework.core.annotation.OrderUtils;
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
/*    */ public class SimpleMetadataAwareAspectInstanceFactory
/*    */   extends SimpleAspectInstanceFactory
/*    */   implements MetadataAwareAspectInstanceFactory
/*    */ {
/*    */   private final AspectMetadata metadata;
/*    */   
/*    */   public SimpleMetadataAwareAspectInstanceFactory(Class<?> aspectClass, String aspectName) {
/* 43 */     super(aspectClass);
/* 44 */     this.metadata = new AspectMetadata(aspectClass, aspectName);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public final AspectMetadata getAspectMetadata() {
/* 50 */     return this.metadata;
/*    */   }
/*    */ 
/*    */   
/*    */   public Object getAspectCreationMutex() {
/* 55 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getOrderForAspectClass(Class<?> aspectClass) {
/* 60 */     return OrderUtils.getOrder(aspectClass, Integer.valueOf(2147483647)).intValue();
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\aspectj\annotation\SimpleMetadataAwareAspectInstanceFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */