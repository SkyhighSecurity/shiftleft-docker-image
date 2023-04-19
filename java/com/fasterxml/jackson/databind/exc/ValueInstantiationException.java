/*    */ package com.fasterxml.jackson.databind.exc;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonParser;
/*    */ import com.fasterxml.jackson.databind.JavaType;
/*    */ import com.fasterxml.jackson.databind.JsonMappingException;
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
/*    */ public class ValueInstantiationException
/*    */   extends JsonMappingException
/*    */ {
/*    */   protected final JavaType _type;
/*    */   
/*    */   protected ValueInstantiationException(JsonParser p, String msg, JavaType type, Throwable cause) {
/* 30 */     super((Closeable)p, msg, cause);
/* 31 */     this._type = type;
/*    */   }
/*    */ 
/*    */   
/*    */   protected ValueInstantiationException(JsonParser p, String msg, JavaType type) {
/* 36 */     super((Closeable)p, msg);
/* 37 */     this._type = type;
/*    */   }
/*    */ 
/*    */   
/*    */   public static ValueInstantiationException from(JsonParser p, String msg, JavaType type) {
/* 42 */     return new ValueInstantiationException(p, msg, type);
/*    */   }
/*    */ 
/*    */   
/*    */   public static ValueInstantiationException from(JsonParser p, String msg, JavaType type, Throwable cause) {
/* 47 */     return new ValueInstantiationException(p, msg, type, cause);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public JavaType getType() {
/* 55 */     return this._type;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\exc\ValueInstantiationException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */