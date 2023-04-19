/*    */ package org.springframework.beans.factory;
/*    */ 
/*    */ import org.springframework.beans.FatalBeanException;
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
/*    */ public class CannotLoadBeanClassException
/*    */   extends FatalBeanException
/*    */ {
/*    */   private String resourceDescription;
/*    */   private String beanName;
/*    */   private String beanClassName;
/*    */   
/*    */   public CannotLoadBeanClassException(String resourceDescription, String beanName, String beanClassName, ClassNotFoundException cause) {
/* 49 */     super("Cannot find class [" + beanClassName + "] for bean with name '" + beanName + "'" + ((resourceDescription != null) ? (" defined in " + resourceDescription) : ""), cause);
/*    */     
/* 51 */     this.resourceDescription = resourceDescription;
/* 52 */     this.beanName = beanName;
/* 53 */     this.beanClassName = beanClassName;
/*    */   }
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
/*    */   public CannotLoadBeanClassException(String resourceDescription, String beanName, String beanClassName, LinkageError cause) {
/* 67 */     super("Error loading class [" + beanClassName + "] for bean with name '" + beanName + "'" + ((resourceDescription != null) ? (" defined in " + resourceDescription) : "") + ": problem with class file or dependent class", cause);
/*    */ 
/*    */     
/* 70 */     this.resourceDescription = resourceDescription;
/* 71 */     this.beanName = beanName;
/* 72 */     this.beanClassName = beanClassName;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getResourceDescription() {
/* 81 */     return this.resourceDescription;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getBeanName() {
/* 88 */     return this.beanName;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getBeanClassName() {
/* 95 */     return this.beanClassName;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\CannotLoadBeanClassException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */