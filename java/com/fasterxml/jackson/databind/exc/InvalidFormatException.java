/*    */ package com.fasterxml.jackson.databind.exc;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonLocation;
/*    */ import com.fasterxml.jackson.core.JsonParser;
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
/*    */ public class InvalidFormatException
/*    */   extends MismatchedInputException
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   protected final Object _value;
/*    */   
/*    */   @Deprecated
/*    */   public InvalidFormatException(String msg, Object value, Class<?> targetType) {
/* 37 */     super(null, msg);
/* 38 */     this._value = value;
/* 39 */     this._targetType = targetType;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Deprecated
/*    */   public InvalidFormatException(String msg, JsonLocation loc, Object value, Class<?> targetType) {
/* 49 */     super((JsonParser)null, msg, loc);
/* 50 */     this._value = value;
/* 51 */     this._targetType = targetType;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public InvalidFormatException(JsonParser p, String msg, Object value, Class<?> targetType) {
/* 60 */     super(p, msg, targetType);
/* 61 */     this._value = value;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static InvalidFormatException from(JsonParser p, String msg, Object value, Class<?> targetType) {
/* 67 */     return new InvalidFormatException(p, msg, value, targetType);
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
/*    */ 
/*    */   
/*    */   public Object getValue() {
/* 83 */     return this._value;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\exc\InvalidFormatException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */