/*    */ package com.fasterxml.jackson.databind.jsontype.impl;
/*    */ 
/*    */ import com.fasterxml.jackson.annotation.JsonTypeInfo;
/*    */ import com.fasterxml.jackson.core.JsonGenerator;
/*    */ import com.fasterxml.jackson.databind.BeanProperty;
/*    */ import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
/*    */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*    */ import java.io.IOException;
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
/*    */ public class AsExternalTypeSerializer
/*    */   extends TypeSerializerBase
/*    */ {
/*    */   protected final String _typePropertyName;
/*    */   
/*    */   public AsExternalTypeSerializer(TypeIdResolver idRes, BeanProperty property, String propName) {
/* 29 */     super(idRes, property);
/* 30 */     this._typePropertyName = propName;
/*    */   }
/*    */ 
/*    */   
/*    */   public AsExternalTypeSerializer forProperty(BeanProperty prop) {
/* 35 */     return (this._property == prop) ? this : new AsExternalTypeSerializer(this._idResolver, prop, this._typePropertyName);
/*    */   }
/*    */   
/*    */   public String getPropertyName() {
/* 39 */     return this._typePropertyName;
/*    */   }
/*    */   public JsonTypeInfo.As getTypeInclusion() {
/* 42 */     return JsonTypeInfo.As.EXTERNAL_PROPERTY;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected final void _writeScalarPrefix(Object value, JsonGenerator g) throws IOException {}
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected final void _writeObjectPrefix(Object value, JsonGenerator g) throws IOException {
/* 54 */     g.writeStartObject();
/*    */   }
/*    */   
/*    */   protected final void _writeArrayPrefix(Object value, JsonGenerator g) throws IOException {
/* 58 */     g.writeStartArray();
/*    */   }
/*    */   
/*    */   protected final void _writeScalarSuffix(Object value, JsonGenerator g, String typeId) throws IOException {
/* 62 */     if (typeId != null) {
/* 63 */       g.writeStringField(this._typePropertyName, typeId);
/*    */     }
/*    */   }
/*    */   
/*    */   protected final void _writeObjectSuffix(Object value, JsonGenerator g, String typeId) throws IOException {
/* 68 */     g.writeEndObject();
/* 69 */     if (typeId != null) {
/* 70 */       g.writeStringField(this._typePropertyName, typeId);
/*    */     }
/*    */   }
/*    */   
/*    */   protected final void _writeArraySuffix(Object value, JsonGenerator g, String typeId) throws IOException {
/* 75 */     g.writeEndArray();
/* 76 */     if (typeId != null)
/* 77 */       g.writeStringField(this._typePropertyName, typeId); 
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\jsontype\impl\AsExternalTypeSerializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */