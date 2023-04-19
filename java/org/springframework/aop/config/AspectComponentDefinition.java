/*    */ package org.springframework.aop.config;
/*    */ 
/*    */ import org.springframework.beans.factory.config.BeanDefinition;
/*    */ import org.springframework.beans.factory.config.BeanReference;
/*    */ import org.springframework.beans.factory.parsing.CompositeComponentDefinition;
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
/*    */ public class AspectComponentDefinition
/*    */   extends CompositeComponentDefinition
/*    */ {
/*    */   private final BeanDefinition[] beanDefinitions;
/*    */   private final BeanReference[] beanReferences;
/*    */   
/*    */   public AspectComponentDefinition(String aspectName, BeanDefinition[] beanDefinitions, BeanReference[] beanReferences, Object source) {
/* 43 */     super(aspectName, source);
/* 44 */     this.beanDefinitions = (beanDefinitions != null) ? beanDefinitions : new BeanDefinition[0];
/* 45 */     this.beanReferences = (beanReferences != null) ? beanReferences : new BeanReference[0];
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public BeanDefinition[] getBeanDefinitions() {
/* 51 */     return this.beanDefinitions;
/*    */   }
/*    */ 
/*    */   
/*    */   public BeanReference[] getBeanReferences() {
/* 56 */     return this.beanReferences;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\config\AspectComponentDefinition.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */