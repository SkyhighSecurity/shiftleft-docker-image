/*     */ package com.fasterxml.jackson.databind.deser.impl;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.PropertyMetadata;
/*     */ import com.fasterxml.jackson.databind.PropertyName;
/*     */ import com.fasterxml.jackson.databind.deser.NullValueProvider;
/*     */ import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*     */ import java.io.IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ObjectIdValueProperty
/*     */   extends SettableBeanProperty
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final ObjectIdReader _objectIdReader;
/*     */   
/*     */   public ObjectIdValueProperty(ObjectIdReader objectIdReader, PropertyMetadata metadata) {
/*  27 */     super(objectIdReader.propertyName, objectIdReader.getIdType(), metadata, objectIdReader
/*  28 */         .getDeserializer());
/*  29 */     this._objectIdReader = objectIdReader;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected ObjectIdValueProperty(ObjectIdValueProperty src, JsonDeserializer<?> deser, NullValueProvider nva) {
/*  35 */     super(src, deser, nva);
/*  36 */     this._objectIdReader = src._objectIdReader;
/*     */   }
/*     */   
/*     */   protected ObjectIdValueProperty(ObjectIdValueProperty src, PropertyName newName) {
/*  40 */     super(src, newName);
/*  41 */     this._objectIdReader = src._objectIdReader;
/*     */   }
/*     */ 
/*     */   
/*     */   public SettableBeanProperty withName(PropertyName newName) {
/*  46 */     return new ObjectIdValueProperty(this, newName);
/*     */   }
/*     */ 
/*     */   
/*     */   public SettableBeanProperty withValueDeserializer(JsonDeserializer<?> deser) {
/*  51 */     if (this._valueDeserializer == deser) {
/*  52 */       return this;
/*     */     }
/*     */     
/*  55 */     NullValueProvider nvp = (this._valueDeserializer == this._nullProvider) ? (NullValueProvider)deser : this._nullProvider;
/*  56 */     return new ObjectIdValueProperty(this, deser, nvp);
/*     */   }
/*     */ 
/*     */   
/*     */   public SettableBeanProperty withNullProvider(NullValueProvider nva) {
/*  61 */     return new ObjectIdValueProperty(this, this._valueDeserializer, nva);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <A extends java.lang.annotation.Annotation> A getAnnotation(Class<A> acls) {
/*  68 */     return null;
/*     */   }
/*     */   public AnnotatedMember getMember() {
/*  71 */     return null;
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
/*     */   public void deserializeAndSet(JsonParser p, DeserializationContext ctxt, Object instance) throws IOException {
/*  83 */     deserializeSetAndReturn(p, ctxt, instance);
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
/*     */   public Object deserializeSetAndReturn(JsonParser p, DeserializationContext ctxt, Object instance) throws IOException {
/*  96 */     if (p.hasToken(JsonToken.VALUE_NULL)) {
/*  97 */       return null;
/*     */     }
/*  99 */     Object id = this._valueDeserializer.deserialize(p, ctxt);
/* 100 */     ReadableObjectId roid = ctxt.findObjectId(id, this._objectIdReader.generator, this._objectIdReader.resolver);
/* 101 */     roid.bindItem(instance);
/*     */     
/* 103 */     SettableBeanProperty idProp = this._objectIdReader.idProperty;
/* 104 */     if (idProp != null) {
/* 105 */       return idProp.setAndReturn(instance, id);
/*     */     }
/* 107 */     return instance;
/*     */   }
/*     */ 
/*     */   
/*     */   public void set(Object instance, Object value) throws IOException {
/* 112 */     setAndReturn(instance, value);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object setAndReturn(Object instance, Object value) throws IOException {
/* 118 */     SettableBeanProperty idProp = this._objectIdReader.idProperty;
/* 119 */     if (idProp == null) {
/* 120 */       throw new UnsupportedOperationException("Should not call set() on ObjectIdProperty that has no SettableBeanProperty");
/*     */     }
/*     */     
/* 123 */     return idProp.setAndReturn(instance, value);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\deser\impl\ObjectIdValueProperty.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */