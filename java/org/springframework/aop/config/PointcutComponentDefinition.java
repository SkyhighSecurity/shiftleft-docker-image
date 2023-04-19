/*    */ package org.springframework.aop.config;
/*    */ 
/*    */ import org.springframework.beans.factory.config.BeanDefinition;
/*    */ import org.springframework.beans.factory.parsing.AbstractComponentDefinition;
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
/*    */ public class PointcutComponentDefinition
/*    */   extends AbstractComponentDefinition
/*    */ {
/*    */   private final String pointcutBeanName;
/*    */   private final BeanDefinition pointcutDefinition;
/*    */   private final String description;
/*    */   
/*    */   public PointcutComponentDefinition(String pointcutBeanName, BeanDefinition pointcutDefinition, String expression) {
/* 40 */     Assert.notNull(pointcutBeanName, "Bean name must not be null");
/* 41 */     Assert.notNull(pointcutDefinition, "Pointcut definition must not be null");
/* 42 */     Assert.notNull(expression, "Expression must not be null");
/* 43 */     this.pointcutBeanName = pointcutBeanName;
/* 44 */     this.pointcutDefinition = pointcutDefinition;
/* 45 */     this.description = "Pointcut <name='" + pointcutBeanName + "', expression=[" + expression + "]>";
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getName() {
/* 51 */     return this.pointcutBeanName;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getDescription() {
/* 56 */     return this.description;
/*    */   }
/*    */ 
/*    */   
/*    */   public BeanDefinition[] getBeanDefinitions() {
/* 61 */     return new BeanDefinition[] { this.pointcutDefinition };
/*    */   }
/*    */ 
/*    */   
/*    */   public Object getSource() {
/* 66 */     return this.pointcutDefinition.getSource();
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\config\PointcutComponentDefinition.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */