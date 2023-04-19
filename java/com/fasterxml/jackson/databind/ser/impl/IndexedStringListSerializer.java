/*     */ package com.fasterxml.jackson.databind.ser.impl;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.core.type.WritableTypeId;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.SerializationFeature;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonArrayFormatVisitor;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatTypes;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*     */ import com.fasterxml.jackson.databind.ser.std.StaticListSerializerBase;
/*     */ import java.io.IOException;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ @JacksonStdImpl
/*     */ public final class IndexedStringListSerializer
/*     */   extends StaticListSerializerBase<List<String>>
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  28 */   public static final IndexedStringListSerializer instance = new IndexedStringListSerializer();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected IndexedStringListSerializer() {
/*  37 */     super(List.class);
/*     */   }
/*     */ 
/*     */   
/*     */   public IndexedStringListSerializer(IndexedStringListSerializer src, Boolean unwrapSingle) {
/*  42 */     super(src, unwrapSingle);
/*     */   }
/*     */ 
/*     */   
/*     */   public JsonSerializer<?> _withResolved(BeanProperty prop, Boolean unwrapSingle) {
/*  47 */     return (JsonSerializer<?>)new IndexedStringListSerializer(this, unwrapSingle);
/*     */   }
/*     */   protected JsonNode contentSchema() {
/*  50 */     return (JsonNode)createSchemaNode("string", true);
/*     */   }
/*     */   
/*     */   protected void acceptContentVisitor(JsonArrayFormatVisitor visitor) throws JsonMappingException {
/*  54 */     visitor.itemsFormat(JsonFormatTypes.STRING);
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
/*     */   public void serialize(List<String> value, JsonGenerator g, SerializerProvider provider) throws IOException {
/*  67 */     int len = value.size();
/*  68 */     if (len == 1 && ((
/*  69 */       this._unwrapSingle == null && provider
/*  70 */       .isEnabled(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED)) || this._unwrapSingle == Boolean.TRUE)) {
/*     */       
/*  72 */       serializeContents(value, g, provider, 1);
/*     */       
/*     */       return;
/*     */     } 
/*  76 */     g.writeStartArray(len);
/*  77 */     serializeContents(value, g, provider, len);
/*  78 */     g.writeEndArray();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void serializeWithType(List<String> value, JsonGenerator g, SerializerProvider provider, TypeSerializer typeSer) throws IOException {
/*  86 */     WritableTypeId typeIdDef = typeSer.writeTypePrefix(g, typeSer
/*  87 */         .typeId(value, JsonToken.START_ARRAY));
/*  88 */     serializeContents(value, g, provider, value.size());
/*  89 */     typeSer.writeTypeSuffix(g, typeIdDef);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private final void serializeContents(List<String> value, JsonGenerator g, SerializerProvider provider, int len) throws IOException {
/*  95 */     g.setCurrentValue(value);
/*  96 */     int i = 0;
/*     */     try {
/*  98 */       for (; i < len; i++) {
/*  99 */         String str = value.get(i);
/* 100 */         if (str == null) {
/* 101 */           provider.defaultSerializeNull(g);
/*     */         } else {
/* 103 */           g.writeString(str);
/*     */         } 
/*     */       } 
/* 106 */     } catch (Exception e) {
/* 107 */       wrapAndThrow(provider, e, value, i);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\ser\impl\IndexedStringListSerializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */