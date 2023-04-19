/*    */ package org.springframework.beans.factory.access.el;
/*    */ 
/*    */ import javax.el.ELContext;
/*    */ import org.springframework.beans.factory.BeanFactory;
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
/*    */ public class SimpleSpringBeanELResolver
/*    */   extends SpringBeanELResolver
/*    */ {
/*    */   private final BeanFactory beanFactory;
/*    */   
/*    */   public SimpleSpringBeanELResolver(BeanFactory beanFactory) {
/* 41 */     Assert.notNull(beanFactory, "BeanFactory must not be null");
/* 42 */     this.beanFactory = beanFactory;
/*    */   }
/*    */ 
/*    */   
/*    */   protected BeanFactory getBeanFactory(ELContext elContext) {
/* 47 */     return this.beanFactory;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\access\el\SimpleSpringBeanELResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */