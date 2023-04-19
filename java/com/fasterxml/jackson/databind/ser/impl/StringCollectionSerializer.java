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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @JacksonStdImpl
/*     */ public class StringCollectionSerializer
/*     */   extends StaticListSerializerBase<Collection<String>>
/*     */ {
/*  27 */   public static final StringCollectionSerializer instance = new StringCollectionSerializer();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected StringCollectionSerializer() {
/*  36 */     super(Collection.class);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected StringCollectionSerializer(StringCollectionSerializer src, Boolean unwrapSingle) {
/*  42 */     super(src, unwrapSingle);
/*     */   }
/*     */ 
/*     */   
/*     */   public JsonSerializer<?> _withResolved(BeanProperty prop, Boolean unwrapSingle) {
/*  47 */     return (JsonSerializer<?>)new StringCollectionSerializer(this, unwrapSingle);
/*     */   }
/*     */   
/*     */   protected JsonNode contentSchema() {
/*  51 */     return (JsonNode)createSchemaNode("string", true);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void acceptContentVisitor(JsonArrayFormatVisitor visitor) throws JsonMappingException {
/*  57 */     visitor.itemsFormat(JsonFormatTypes.STRING);
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
/*     */   public void serialize(Collection<String> value, JsonGenerator g, SerializerProvider provider) throws IOException {
/*  70 */     g.setCurrentValue(value);
/*  71 */     int len = value.size();
/*  72 */     if (len == 1 && ((
/*  73 */       this._unwrapSingle == null && provider
/*  74 */       .isEnabled(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED)) || this._unwrapSingle == Boolean.TRUE)) {
/*     */       
/*  76 */       serializeContents(value, g, provider);
/*     */       
/*     */       return;
/*     */     } 
/*  80 */     g.writeStartArray(len);
/*  81 */     serializeContents(value, g, provider);
/*  82 */     g.writeEndArray();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void serializeWithType(Collection<String> value, JsonGenerator g, SerializerProvider provider, TypeSerializer typeSer) throws IOException {
/*  90 */     g.setCurrentValue(value);
/*  91 */     WritableTypeId typeIdDef = typeSer.writeTypePrefix(g, typeSer
/*  92 */         .typeId(value, JsonToken.START_ARRAY));
/*  93 */     serializeContents(value, g, provider);
/*  94 */     typeSer.writeTypeSuffix(g, typeIdDef);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final void serializeContents(Collection<String> value, JsonGenerator g, SerializerProvider provider) throws IOException {
/* 101 */     int i = 0;
/*     */     
/*     */     try {
/* 104 */       for (String str : value) {
/* 105 */         if (str == null) {
/* 106 */           provider.defaultSerializeNull(g);
/*     */         } else {
/* 108 */           g.writeString(str);
/*     */         } 
/* 110 */         i++;
/*     */       } 
/* 112 */     } catch (Exception e) {
/* 113 */       wrapAndThrow(provider, e, value, i);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\ser\impl\StringCollectionSerializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */