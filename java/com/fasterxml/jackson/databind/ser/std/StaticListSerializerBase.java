/*     */ package com.fasterxml.jackson.databind.ser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonFormat;
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.introspect.Annotated;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonArrayFormatVisitor;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*     */ import com.fasterxml.jackson.databind.ser.ContextualSerializer;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.Collection;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class StaticListSerializerBase<T extends Collection<?>>
/*     */   extends StdSerializer<T>
/*     */   implements ContextualSerializer
/*     */ {
/*     */   protected final Boolean _unwrapSingle;
/*     */   
/*     */   protected StaticListSerializerBase(Class<?> cls) {
/*  35 */     super(cls, false);
/*  36 */     this._unwrapSingle = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected StaticListSerializerBase(StaticListSerializerBase<?> src, Boolean unwrapSingle) {
/*  44 */     super(src);
/*  45 */     this._unwrapSingle = unwrapSingle;
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
/*     */   public JsonSerializer<?> createContextual(SerializerProvider serializers, BeanProperty property) throws JsonMappingException {
/*  66 */     JsonSerializer<?> ser = null;
/*  67 */     Boolean unwrapSingle = null;
/*     */     
/*  69 */     if (property != null) {
/*  70 */       AnnotationIntrospector intr = serializers.getAnnotationIntrospector();
/*  71 */       AnnotatedMember m = property.getMember();
/*  72 */       if (m != null) {
/*  73 */         Object serDef = intr.findContentSerializer((Annotated)m);
/*  74 */         if (serDef != null) {
/*  75 */           ser = serializers.serializerInstance((Annotated)m, serDef);
/*     */         }
/*     */       } 
/*     */     } 
/*  79 */     JsonFormat.Value format = findFormatOverrides(serializers, property, handledType());
/*  80 */     if (format != null) {
/*  81 */       unwrapSingle = format.getFeature(JsonFormat.Feature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED);
/*     */     }
/*     */     
/*  84 */     ser = findContextualConvertingSerializer(serializers, property, ser);
/*  85 */     if (ser == null) {
/*  86 */       ser = serializers.findValueSerializer(String.class, property);
/*     */     }
/*     */     
/*  89 */     if (isDefaultSerializer(ser)) {
/*  90 */       if (unwrapSingle == this._unwrapSingle) {
/*  91 */         return this;
/*     */       }
/*  93 */       return _withResolved(property, unwrapSingle);
/*     */     } 
/*     */ 
/*     */     
/*  97 */     return (JsonSerializer<?>)new CollectionSerializer(serializers.constructType(String.class), true, null, ser);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty(SerializerProvider provider, T value) {
/* 103 */     return (value == null || value.size() == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
/* 108 */     return createSchemaNode("array", true).set("items", contentSchema());
/*     */   }
/*     */ 
/*     */   
/*     */   public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
/* 113 */     JsonArrayFormatVisitor v2 = visitor.expectArrayFormat(typeHint);
/* 114 */     if (v2 != null)
/* 115 */       acceptContentVisitor(v2); 
/*     */   }
/*     */   
/*     */   public abstract JsonSerializer<?> _withResolved(BeanProperty paramBeanProperty, Boolean paramBoolean);
/*     */   
/*     */   protected abstract JsonNode contentSchema();
/*     */   
/*     */   protected abstract void acceptContentVisitor(JsonArrayFormatVisitor paramJsonArrayFormatVisitor) throws JsonMappingException;
/*     */   
/*     */   public abstract void serializeWithType(T paramT, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider, TypeSerializer paramTypeSerializer) throws IOException;
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\ser\std\StaticListSerializerBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */