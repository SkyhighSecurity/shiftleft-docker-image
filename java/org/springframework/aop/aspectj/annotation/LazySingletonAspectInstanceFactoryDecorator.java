/*    */ package org.springframework.aop.aspectj.annotation;
/*    */ 
/*    */ import java.io.Serializable;
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
/*    */ public class LazySingletonAspectInstanceFactoryDecorator
/*    */   implements MetadataAwareAspectInstanceFactory, Serializable
/*    */ {
/*    */   private final MetadataAwareAspectInstanceFactory maaif;
/*    */   private volatile Object materialized;
/*    */   
/*    */   public LazySingletonAspectInstanceFactoryDecorator(MetadataAwareAspectInstanceFactory maaif) {
/* 43 */     Assert.notNull(maaif, "AspectInstanceFactory must not be null");
/* 44 */     this.maaif = maaif;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Object getAspectInstance() {
/* 50 */     if (this.materialized == null) {
/* 51 */       Object mutex = this.maaif.getAspectCreationMutex();
/* 52 */       if (mutex == null) {
/* 53 */         this.materialized = this.maaif.getAspectInstance();
/*    */       } else {
/*    */         
/* 56 */         synchronized (mutex) {
/* 57 */           if (this.materialized == null) {
/* 58 */             this.materialized = this.maaif.getAspectInstance();
/*    */           }
/*    */         } 
/*    */       } 
/*    */     } 
/* 63 */     return this.materialized;
/*    */   }
/*    */   
/*    */   public boolean isMaterialized() {
/* 67 */     return (this.materialized != null);
/*    */   }
/*    */ 
/*    */   
/*    */   public ClassLoader getAspectClassLoader() {
/* 72 */     return this.maaif.getAspectClassLoader();
/*    */   }
/*    */ 
/*    */   
/*    */   public AspectMetadata getAspectMetadata() {
/* 77 */     return this.maaif.getAspectMetadata();
/*    */   }
/*    */ 
/*    */   
/*    */   public Object getAspectCreationMutex() {
/* 82 */     return this.maaif.getAspectCreationMutex();
/*    */   }
/*    */ 
/*    */   
/*    */   public int getOrder() {
/* 87 */     return this.maaif.getOrder();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 93 */     return "LazySingletonAspectInstanceFactoryDecorator: decorating " + this.maaif;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\aspectj\annotation\LazySingletonAspectInstanceFactoryDecorator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */