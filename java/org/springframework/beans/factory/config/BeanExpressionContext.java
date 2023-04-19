/*    */ package org.springframework.beans.factory.config;
/*    */ 
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
/*    */ public class BeanExpressionContext
/*    */ {
/*    */   private final ConfigurableBeanFactory beanFactory;
/*    */   private final Scope scope;
/*    */   
/*    */   public BeanExpressionContext(ConfigurableBeanFactory beanFactory, Scope scope) {
/* 35 */     Assert.notNull(beanFactory, "BeanFactory must not be null");
/* 36 */     this.beanFactory = beanFactory;
/* 37 */     this.scope = scope;
/*    */   }
/*    */   
/*    */   public final ConfigurableBeanFactory getBeanFactory() {
/* 41 */     return this.beanFactory;
/*    */   }
/*    */   
/*    */   public final Scope getScope() {
/* 45 */     return this.scope;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean containsObject(String key) {
/* 50 */     return (this.beanFactory.containsBean(key) || (this.scope != null && this.scope
/* 51 */       .resolveContextualObject(key) != null));
/*    */   }
/*    */   
/*    */   public Object getObject(String key) {
/* 55 */     if (this.beanFactory.containsBean(key)) {
/* 56 */       return this.beanFactory.getBean(key);
/*    */     }
/* 58 */     if (this.scope != null) {
/* 59 */       return this.scope.resolveContextualObject(key);
/*    */     }
/*    */     
/* 62 */     return null;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(Object other) {
/* 69 */     if (this == other) {
/* 70 */       return true;
/*    */     }
/* 72 */     if (!(other instanceof BeanExpressionContext)) {
/* 73 */       return false;
/*    */     }
/* 75 */     BeanExpressionContext otherContext = (BeanExpressionContext)other;
/* 76 */     return (this.beanFactory == otherContext.beanFactory && this.scope == otherContext.scope);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 81 */     return this.beanFactory.hashCode();
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\config\BeanExpressionContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */