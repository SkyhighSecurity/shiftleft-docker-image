/*    */ package com.fasterxml.jackson.databind.exc;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonLocation;
/*    */ import com.fasterxml.jackson.core.JsonParser;
/*    */ import com.fasterxml.jackson.databind.JavaType;
/*    */ import com.fasterxml.jackson.databind.JsonMappingException;
/*    */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*    */ import java.io.Closeable;
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
/*    */ public class MismatchedInputException
/*    */   extends JsonMappingException
/*    */ {
/*    */   protected Class<?> _targetType;
/*    */   
/*    */   protected MismatchedInputException(JsonParser p, String msg) {
/* 35 */     this(p, msg, (JavaType)null);
/*    */   }
/*    */   
/*    */   protected MismatchedInputException(JsonParser p, String msg, JsonLocation loc) {
/* 39 */     super((Closeable)p, msg, loc);
/*    */   }
/*    */   
/*    */   protected MismatchedInputException(JsonParser p, String msg, Class<?> targetType) {
/* 43 */     super((Closeable)p, msg);
/* 44 */     this._targetType = targetType;
/*    */   }
/*    */   
/*    */   protected MismatchedInputException(JsonParser p, String msg, JavaType targetType) {
/* 48 */     super((Closeable)p, msg);
/* 49 */     this._targetType = ClassUtil.rawClass(targetType);
/*    */   }
/*    */ 
/*    */   
/*    */   @Deprecated
/*    */   public static MismatchedInputException from(JsonParser p, String msg) {
/* 55 */     return from(p, (Class)null, msg);
/*    */   }
/*    */   
/*    */   public static MismatchedInputException from(JsonParser p, JavaType targetType, String msg) {
/* 59 */     return new MismatchedInputException(p, msg, targetType);
/*    */   }
/*    */   
/*    */   public static MismatchedInputException from(JsonParser p, Class<?> targetType, String msg) {
/* 63 */     return new MismatchedInputException(p, msg, targetType);
/*    */   }
/*    */   
/*    */   public MismatchedInputException setTargetType(JavaType t) {
/* 67 */     this._targetType = t.getRawClass();
/* 68 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Class<?> getTargetType() {
/* 76 */     return this._targetType;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\exc\MismatchedInputException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */