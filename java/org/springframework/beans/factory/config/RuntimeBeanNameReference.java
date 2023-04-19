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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RuntimeBeanNameReference
/*    */   implements BeanReference
/*    */ {
/*    */   private final String beanName;
/*    */   private Object source;
/*    */   
/*    */   public RuntimeBeanNameReference(String beanName) {
/* 43 */     Assert.hasText(beanName, "'beanName' must not be empty");
/* 44 */     this.beanName = beanName;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getBeanName() {
/* 49 */     return this.beanName;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setSource(Object source) {
/* 57 */     this.source = source;
/*    */   }
/*    */ 
/*    */   
/*    */   public Object getSource() {
/* 62 */     return this.source;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(Object other) {
/* 68 */     if (this == other) {
/* 69 */       return true;
/*    */     }
/* 71 */     if (!(other instanceof RuntimeBeanNameReference)) {
/* 72 */       return false;
/*    */     }
/* 74 */     RuntimeBeanNameReference that = (RuntimeBeanNameReference)other;
/* 75 */     return this.beanName.equals(that.beanName);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 80 */     return this.beanName.hashCode();
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 85 */     return '<' + getBeanName() + '>';
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\config\RuntimeBeanNameReference.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */