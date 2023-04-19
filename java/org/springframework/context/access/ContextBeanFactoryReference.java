/*    */ package org.springframework.context.access;
/*    */ 
/*    */ import org.springframework.beans.factory.BeanFactory;
/*    */ import org.springframework.beans.factory.access.BeanFactoryReference;
/*    */ import org.springframework.context.ApplicationContext;
/*    */ import org.springframework.context.ConfigurableApplicationContext;
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
/*    */ 
/*    */ public class ContextBeanFactoryReference
/*    */   implements BeanFactoryReference
/*    */ {
/*    */   private ApplicationContext applicationContext;
/*    */   
/*    */   public ContextBeanFactoryReference(ApplicationContext applicationContext) {
/* 47 */     this.applicationContext = applicationContext;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public BeanFactory getFactory() {
/* 53 */     if (this.applicationContext == null) {
/* 54 */       throw new IllegalStateException("ApplicationContext owned by this BeanFactoryReference has been released");
/*    */     }
/*    */     
/* 57 */     return (BeanFactory)this.applicationContext;
/*    */   }
/*    */ 
/*    */   
/*    */   public void release() {
/* 62 */     if (this.applicationContext != null) {
/*    */       ApplicationContext savedCtx;
/*    */ 
/*    */       
/* 66 */       synchronized (this) {
/* 67 */         savedCtx = this.applicationContext;
/* 68 */         this.applicationContext = null;
/*    */       } 
/*    */       
/* 71 */       if (savedCtx != null && savedCtx instanceof ConfigurableApplicationContext)
/* 72 */         ((ConfigurableApplicationContext)savedCtx).close(); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\access\ContextBeanFactoryReference.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */