/*     */ package com.fasterxml.jackson.databind.ser.impl;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*     */ import com.fasterxml.jackson.databind.ser.ContainerSerializer;
/*     */ import com.fasterxml.jackson.databind.ser.std.AsArraySerializerBase;
/*     */ import java.io.IOException;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ @JacksonStdImpl
/*     */ public class IteratorSerializer
/*     */   extends AsArraySerializerBase<Iterator<?>> {
/*     */   public IteratorSerializer(JavaType elemType, boolean staticTyping, TypeSerializer vts) {
/*  19 */     super(Iterator.class, elemType, staticTyping, vts, null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public IteratorSerializer(IteratorSerializer src, BeanProperty property, TypeSerializer vts, JsonSerializer<?> valueSerializer, Boolean unwrapSingle) {
/*  25 */     super(src, property, vts, valueSerializer, unwrapSingle);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty(SerializerProvider prov, Iterator<?> value) {
/*  30 */     return !value.hasNext();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasSingleElement(Iterator<?> value) {
/*  36 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public ContainerSerializer<?> _withValueTypeSerializer(TypeSerializer vts) {
/*  41 */     return (ContainerSerializer<?>)new IteratorSerializer(this, this._property, vts, this._elementSerializer, this._unwrapSingle);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IteratorSerializer withResolved(BeanProperty property, TypeSerializer vts, JsonSerializer<?> elementSerializer, Boolean unwrapSingle) {
/*  48 */     return new IteratorSerializer(this, property, vts, elementSerializer, unwrapSingle);
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
/*     */   public final void serialize(Iterator<?> value, JsonGenerator gen, SerializerProvider provider) throws IOException {
/*  66 */     gen.writeStartArray();
/*  67 */     serializeContents(value, gen, provider);
/*  68 */     gen.writeEndArray();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void serializeContents(Iterator<?> value, JsonGenerator g, SerializerProvider provider) throws IOException {
/*  75 */     if (!value.hasNext()) {
/*     */       return;
/*     */     }
/*  78 */     JsonSerializer<Object> serializer = this._elementSerializer;
/*  79 */     if (serializer == null) {
/*  80 */       _serializeDynamicContents(value, g, provider);
/*     */       return;
/*     */     } 
/*  83 */     TypeSerializer typeSer = this._valueTypeSerializer;
/*     */     do {
/*  85 */       Object elem = value.next();
/*  86 */       if (elem == null) {
/*  87 */         provider.defaultSerializeNull(g);
/*  88 */       } else if (typeSer == null) {
/*  89 */         serializer.serialize(elem, g, provider);
/*     */       } else {
/*  91 */         serializer.serializeWithType(elem, g, provider, typeSer);
/*     */       } 
/*  93 */     } while (value.hasNext());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void _serializeDynamicContents(Iterator<?> value, JsonGenerator g, SerializerProvider provider) throws IOException {
/*  99 */     TypeSerializer typeSer = this._valueTypeSerializer;
/* 100 */     PropertySerializerMap serializers = this._dynamicSerializers;
/*     */     do {
/* 102 */       Object elem = value.next();
/* 103 */       if (elem == null) {
/* 104 */         provider.defaultSerializeNull(g);
/*     */       } else {
/*     */         
/* 107 */         Class<?> cc = elem.getClass();
/* 108 */         JsonSerializer<Object> serializer = serializers.serializerFor(cc);
/* 109 */         if (serializer == null) {
/* 110 */           if (this._elementType.hasGenericTypes()) {
/* 111 */             serializer = _findAndAddDynamic(serializers, provider
/* 112 */                 .constructSpecializedType(this._elementType, cc), provider);
/*     */           } else {
/* 114 */             serializer = _findAndAddDynamic(serializers, cc, provider);
/*     */           } 
/* 116 */           serializers = this._dynamicSerializers;
/*     */         } 
/* 118 */         if (typeSer == null)
/* 119 */         { serializer.serialize(elem, g, provider); }
/*     */         else
/* 121 */         { serializer.serializeWithType(elem, g, provider, typeSer); } 
/*     */       } 
/* 123 */     } while (value.hasNext());
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\ser\impl\IteratorSerializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */