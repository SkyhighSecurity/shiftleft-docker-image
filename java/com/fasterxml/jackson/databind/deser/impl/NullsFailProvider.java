/*    */ package com.fasterxml.jackson.databind.deser.impl;
/*    */ 
/*    */ import com.fasterxml.jackson.databind.BeanProperty;
/*    */ import com.fasterxml.jackson.databind.DeserializationContext;
/*    */ import com.fasterxml.jackson.databind.JavaType;
/*    */ import com.fasterxml.jackson.databind.JsonMappingException;
/*    */ import com.fasterxml.jackson.databind.PropertyName;
/*    */ import com.fasterxml.jackson.databind.deser.NullValueProvider;
/*    */ import com.fasterxml.jackson.databind.exc.InvalidNullException;
/*    */ import com.fasterxml.jackson.databind.util.AccessPattern;
/*    */ import java.io.Serializable;
/*    */ 
/*    */ public class NullsFailProvider
/*    */   implements NullValueProvider, Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   protected final PropertyName _name;
/*    */   protected final JavaType _type;
/*    */   
/*    */   protected NullsFailProvider(PropertyName name, JavaType type) {
/* 21 */     this._name = name;
/* 22 */     this._type = type;
/*    */   }
/*    */   
/*    */   public static NullsFailProvider constructForProperty(BeanProperty prop) {
/* 26 */     return new NullsFailProvider(prop.getFullName(), prop.getType());
/*    */   }
/*    */   
/*    */   public static NullsFailProvider constructForRootValue(JavaType t) {
/* 30 */     return new NullsFailProvider(null, t);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public AccessPattern getNullAccessPattern() {
/* 36 */     return AccessPattern.DYNAMIC;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Object getNullValue(DeserializationContext ctxt) throws JsonMappingException {
/* 42 */     throw InvalidNullException.from(ctxt, this._name, this._type);
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\deser\impl\NullsFailProvider.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */