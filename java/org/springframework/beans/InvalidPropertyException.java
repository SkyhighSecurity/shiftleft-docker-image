/*    */ package org.springframework.beans;
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
/*    */ public class InvalidPropertyException
/*    */   extends FatalBeanException
/*    */ {
/*    */   private Class<?> beanClass;
/*    */   private String propertyName;
/*    */   
/*    */   public InvalidPropertyException(Class<?> beanClass, String propertyName, String msg) {
/* 41 */     this(beanClass, propertyName, msg, null);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public InvalidPropertyException(Class<?> beanClass, String propertyName, String msg, Throwable cause) {
/* 52 */     super("Invalid property '" + propertyName + "' of bean class [" + beanClass.getName() + "]: " + msg, cause);
/* 53 */     this.beanClass = beanClass;
/* 54 */     this.propertyName = propertyName;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Class<?> getBeanClass() {
/* 61 */     return this.beanClass;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getPropertyName() {
/* 68 */     return this.propertyName;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\InvalidPropertyException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */