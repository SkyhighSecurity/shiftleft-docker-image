/*    */ package com.fasterxml.jackson.databind.jsontype.impl;
/*    */ 
/*    */ import com.fasterxml.jackson.annotation.JsonTypeInfo;
/*    */ import com.fasterxml.jackson.databind.BeanProperty;
/*    */ import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
/*    */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AsExistingPropertyTypeSerializer
/*    */   extends AsPropertyTypeSerializer
/*    */ {
/*    */   public AsExistingPropertyTypeSerializer(TypeIdResolver idRes, BeanProperty property, String propName) {
/* 18 */     super(idRes, property, propName);
/*    */   }
/*    */ 
/*    */   
/*    */   public AsExistingPropertyTypeSerializer forProperty(BeanProperty prop) {
/* 23 */     return (this._property == prop) ? this : new AsExistingPropertyTypeSerializer(this._idResolver, prop, this._typePropertyName);
/*    */   }
/*    */ 
/*    */   
/*    */   public JsonTypeInfo.As getTypeInclusion() {
/* 28 */     return JsonTypeInfo.As.EXISTING_PROPERTY;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\jsontype\impl\AsExistingPropertyTypeSerializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */