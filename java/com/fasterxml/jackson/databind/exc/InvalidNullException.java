/*    */ package com.fasterxml.jackson.databind.exc;
/*    */ 
/*    */ import com.fasterxml.jackson.databind.DeserializationContext;
/*    */ import com.fasterxml.jackson.databind.JavaType;
/*    */ import com.fasterxml.jackson.databind.PropertyName;
/*    */ import com.fasterxml.jackson.databind.util.ClassUtil;
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
/*    */ public class InvalidNullException
/*    */   extends MismatchedInputException
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   protected final PropertyName _propertyName;
/*    */   
/*    */   protected InvalidNullException(DeserializationContext ctxt, String msg, PropertyName pname) {
/* 33 */     super(ctxt.getParser(), msg);
/* 34 */     this._propertyName = pname;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static InvalidNullException from(DeserializationContext ctxt, PropertyName name, JavaType type) {
/* 40 */     String msg = String.format("Invalid `null` value encountered for property %s", new Object[] {
/* 41 */           ClassUtil.quotedOr(name, "<UNKNOWN>") });
/* 42 */     InvalidNullException exc = new InvalidNullException(ctxt, msg, name);
/* 43 */     if (type != null) {
/* 44 */       exc.setTargetType(type);
/*    */     }
/* 46 */     return exc;
/*    */   }
/*    */   
/*    */   public PropertyName getPropertyName() {
/* 50 */     return this._propertyName;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\exc\InvalidNullException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */