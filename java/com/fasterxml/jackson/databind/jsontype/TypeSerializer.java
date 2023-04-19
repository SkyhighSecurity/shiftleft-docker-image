/*     */ package com.fasterxml.jackson.databind.jsontype;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonTypeInfo;
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.core.type.WritableTypeId;
/*     */ import com.fasterxml.jackson.core.util.VersionUtil;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import java.io.IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class TypeSerializer
/*     */ {
/*     */   public abstract TypeSerializer forProperty(BeanProperty paramBeanProperty);
/*     */   
/*     */   public abstract JsonTypeInfo.As getTypeInclusion();
/*     */   
/*     */   public abstract String getPropertyName();
/*     */   
/*     */   public abstract TypeIdResolver getTypeIdResolver();
/*     */   
/*     */   public WritableTypeId typeId(Object value, JsonToken valueShape) {
/*  80 */     WritableTypeId typeIdDef = new WritableTypeId(value, valueShape);
/*  81 */     switch (getTypeInclusion())
/*     */     { case EXISTING_PROPERTY:
/*  83 */         typeIdDef.include = WritableTypeId.Inclusion.PAYLOAD_PROPERTY;
/*  84 */         typeIdDef.asProperty = getPropertyName();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 103 */         return typeIdDef;case EXTERNAL_PROPERTY: typeIdDef.include = WritableTypeId.Inclusion.PARENT_PROPERTY; typeIdDef.asProperty = getPropertyName(); return typeIdDef;case PROPERTY: typeIdDef.include = WritableTypeId.Inclusion.METADATA_PROPERTY; typeIdDef.asProperty = getPropertyName(); return typeIdDef;case WRAPPER_ARRAY: typeIdDef.include = WritableTypeId.Inclusion.WRAPPER_ARRAY; return typeIdDef;case WRAPPER_OBJECT: typeIdDef.include = WritableTypeId.Inclusion.WRAPPER_OBJECT; return typeIdDef; }  VersionUtil.throwInternal(); return typeIdDef;
/*     */   }
/*     */ 
/*     */   
/*     */   public WritableTypeId typeId(Object value, JsonToken valueShape, Object id) {
/* 108 */     WritableTypeId typeId = typeId(value, valueShape);
/* 109 */     typeId.id = id;
/* 110 */     return typeId;
/*     */   }
/*     */ 
/*     */   
/*     */   public WritableTypeId typeId(Object value, Class<?> typeForId, JsonToken valueShape) {
/* 115 */     WritableTypeId typeId = typeId(value, valueShape);
/* 116 */     typeId.forValueType = typeForId;
/* 117 */     return typeId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract WritableTypeId writeTypePrefix(JsonGenerator paramJsonGenerator, WritableTypeId paramWritableTypeId) throws IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract WritableTypeId writeTypeSuffix(JsonGenerator paramJsonGenerator, WritableTypeId paramWritableTypeId) throws IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void writeTypePrefixForScalar(Object value, JsonGenerator g) throws IOException {
/* 179 */     writeTypePrefix(g, typeId(value, JsonToken.VALUE_STRING));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void writeTypePrefixForObject(Object value, JsonGenerator g) throws IOException {
/* 191 */     writeTypePrefix(g, typeId(value, JsonToken.START_OBJECT));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void writeTypePrefixForArray(Object value, JsonGenerator g) throws IOException {
/* 203 */     writeTypePrefix(g, typeId(value, JsonToken.START_ARRAY));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void writeTypeSuffixForScalar(Object value, JsonGenerator g) throws IOException {
/* 215 */     _writeLegacySuffix(g, typeId(value, JsonToken.VALUE_STRING));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void writeTypeSuffixForObject(Object value, JsonGenerator g) throws IOException {
/* 227 */     _writeLegacySuffix(g, typeId(value, JsonToken.START_OBJECT));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void writeTypeSuffixForArray(Object value, JsonGenerator g) throws IOException {
/* 239 */     _writeLegacySuffix(g, typeId(value, JsonToken.START_ARRAY));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void writeTypePrefixForScalar(Object value, JsonGenerator g, Class<?> type) throws IOException {
/* 251 */     writeTypePrefix(g, typeId(value, type, JsonToken.VALUE_STRING));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void writeTypePrefixForObject(Object value, JsonGenerator g, Class<?> type) throws IOException {
/* 263 */     writeTypePrefix(g, typeId(value, type, JsonToken.START_OBJECT));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void writeTypePrefixForArray(Object value, JsonGenerator g, Class<?> type) throws IOException {
/* 275 */     writeTypePrefix(g, typeId(value, type, JsonToken.START_ARRAY));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void writeCustomTypePrefixForScalar(Object value, JsonGenerator g, String typeId) throws IOException {
/* 293 */     writeTypePrefix(g, typeId(value, JsonToken.VALUE_STRING, typeId));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void writeCustomTypePrefixForObject(Object value, JsonGenerator g, String typeId) throws IOException {
/* 305 */     writeTypePrefix(g, typeId(value, JsonToken.START_OBJECT, typeId));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void writeCustomTypePrefixForArray(Object value, JsonGenerator g, String typeId) throws IOException {
/* 317 */     writeTypePrefix(g, typeId(value, JsonToken.START_ARRAY, typeId));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void writeCustomTypeSuffixForScalar(Object value, JsonGenerator g, String typeId) throws IOException {
/* 325 */     _writeLegacySuffix(g, typeId(value, JsonToken.VALUE_STRING, typeId));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void writeCustomTypeSuffixForObject(Object value, JsonGenerator g, String typeId) throws IOException {
/* 333 */     _writeLegacySuffix(g, typeId(value, JsonToken.START_OBJECT, typeId));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void writeCustomTypeSuffixForArray(Object value, JsonGenerator g, String typeId) throws IOException {
/* 341 */     _writeLegacySuffix(g, typeId(value, JsonToken.START_ARRAY, typeId));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void _writeLegacySuffix(JsonGenerator g, WritableTypeId typeId) throws IOException {
/* 355 */     typeId.wrapperWritten = !g.canWriteTypeId();
/* 356 */     writeTypeSuffix(g, typeId);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\jsontype\TypeSerializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */