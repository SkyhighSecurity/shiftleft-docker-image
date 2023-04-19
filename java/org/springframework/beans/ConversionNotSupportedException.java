/*    */ package org.springframework.beans;
/*    */ 
/*    */ import java.beans.PropertyChangeEvent;
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
/*    */ public class ConversionNotSupportedException
/*    */   extends TypeMismatchException
/*    */ {
/*    */   public ConversionNotSupportedException(PropertyChangeEvent propertyChangeEvent, Class<?> requiredType, Throwable cause) {
/* 39 */     super(propertyChangeEvent, requiredType, cause);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ConversionNotSupportedException(Object value, Class<?> requiredType, Throwable cause) {
/* 49 */     super(value, requiredType, cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\ConversionNotSupportedException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */