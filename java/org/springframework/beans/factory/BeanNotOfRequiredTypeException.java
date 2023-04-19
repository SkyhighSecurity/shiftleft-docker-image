/*    */ package org.springframework.beans.factory;
/*    */ 
/*    */ import org.springframework.beans.BeansException;
/*    */ import org.springframework.util.ClassUtils;
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
/*    */ public class BeanNotOfRequiredTypeException
/*    */   extends BeansException
/*    */ {
/*    */   private String beanName;
/*    */   private Class<?> requiredType;
/*    */   private Class<?> actualType;
/*    */   
/*    */   public BeanNotOfRequiredTypeException(String beanName, Class<?> requiredType, Class<?> actualType) {
/* 49 */     super("Bean named '" + beanName + "' is expected to be of type '" + ClassUtils.getQualifiedName(requiredType) + "' but was actually of type '" + 
/* 50 */         ClassUtils.getQualifiedName(actualType) + "'");
/* 51 */     this.beanName = beanName;
/* 52 */     this.requiredType = requiredType;
/* 53 */     this.actualType = actualType;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getBeanName() {
/* 61 */     return this.beanName;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Class<?> getRequiredType() {
/* 68 */     return this.requiredType;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Class<?> getActualType() {
/* 75 */     return this.actualType;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\BeanNotOfRequiredTypeException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */