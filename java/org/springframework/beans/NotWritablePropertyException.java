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
/*    */ public class NotWritablePropertyException
/*    */   extends InvalidPropertyException
/*    */ {
/* 30 */   private String[] possibleMatches = null;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public NotWritablePropertyException(Class<?> beanClass, String propertyName) {
/* 39 */     super(beanClass, propertyName, "Bean property '" + propertyName + "' is not writable or has an invalid setter method: Does the return type of the getter match the parameter type of the setter?");
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
/*    */   public NotWritablePropertyException(Class<?> beanClass, String propertyName, String msg) {
/* 51 */     super(beanClass, propertyName, msg);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public NotWritablePropertyException(Class<?> beanClass, String propertyName, String msg, Throwable cause) {
/* 62 */     super(beanClass, propertyName, msg, cause);
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
/*    */   public NotWritablePropertyException(Class<?> beanClass, String propertyName, String msg, String[] possibleMatches) {
/* 74 */     super(beanClass, propertyName, msg);
/* 75 */     this.possibleMatches = possibleMatches;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String[] getPossibleMatches() {
/* 84 */     return this.possibleMatches;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\NotWritablePropertyException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */