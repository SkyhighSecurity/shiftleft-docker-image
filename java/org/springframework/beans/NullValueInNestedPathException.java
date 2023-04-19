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
/*    */ public class NullValueInNestedPathException
/*    */   extends InvalidPropertyException
/*    */ {
/*    */   public NullValueInNestedPathException(Class<?> beanClass, String propertyName) {
/* 38 */     super(beanClass, propertyName, "Value of nested property '" + propertyName + "' is null");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public NullValueInNestedPathException(Class<?> beanClass, String propertyName, String msg) {
/* 48 */     super(beanClass, propertyName, msg);
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
/*    */   public NullValueInNestedPathException(Class<?> beanClass, String propertyName, String msg, Throwable cause) {
/* 60 */     super(beanClass, propertyName, msg, cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\NullValueInNestedPathException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */