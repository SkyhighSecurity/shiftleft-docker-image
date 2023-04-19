/*    */ package org.springframework.web.method.annotation;
/*    */ 
/*    */ import org.springframework.beans.ConversionNotSupportedException;
/*    */ import org.springframework.core.MethodParameter;
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
/*    */ public class MethodArgumentConversionNotSupportedException
/*    */   extends ConversionNotSupportedException
/*    */ {
/*    */   private final String name;
/*    */   private final MethodParameter parameter;
/*    */   
/*    */   public MethodArgumentConversionNotSupportedException(Object value, Class<?> requiredType, String name, MethodParameter param, Throwable cause) {
/* 41 */     super(value, requiredType, cause);
/* 42 */     this.name = name;
/* 43 */     this.parameter = param;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getName() {
/* 51 */     return this.name;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public MethodParameter getParameter() {
/* 58 */     return this.parameter;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\method\annotation\MethodArgumentConversionNotSupportedException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */