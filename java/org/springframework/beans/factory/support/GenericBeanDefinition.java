/*    */ package org.springframework.beans.factory.support;
/*    */ 
/*    */ import org.springframework.beans.factory.config.BeanDefinition;
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
/*    */ public class GenericBeanDefinition
/*    */   extends AbstractBeanDefinition
/*    */ {
/*    */   private String parentName;
/*    */   
/*    */   public GenericBeanDefinition() {}
/*    */   
/*    */   public GenericBeanDefinition(BeanDefinition original) {
/* 62 */     super(original);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setParentName(String parentName) {
/* 68 */     this.parentName = parentName;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getParentName() {
/* 73 */     return this.parentName;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public AbstractBeanDefinition cloneBeanDefinition() {
/* 79 */     return new GenericBeanDefinition(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object other) {
/* 84 */     return (this == other || (other instanceof GenericBeanDefinition && super.equals(other)));
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 89 */     StringBuilder sb = new StringBuilder("Generic bean");
/* 90 */     if (this.parentName != null) {
/* 91 */       sb.append(" with parent '").append(this.parentName).append("'");
/*    */     }
/* 93 */     sb.append(": ").append(super.toString());
/* 94 */     return sb.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\support\GenericBeanDefinition.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */