/*     */ package com.fasterxml.jackson.databind.deser.impl;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.databind.DeserializationConfig;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.PropertyName;
/*     */ import com.fasterxml.jackson.databind.deser.NullValueProvider;
/*     */ import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
/*     */ import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*     */ import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public class ObjectIdReferenceProperty
/*     */   extends SettableBeanProperty
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private final SettableBeanProperty _forward;
/*     */   
/*     */   public ObjectIdReferenceProperty(SettableBeanProperty forward, ObjectIdInfo objectIdInfo) {
/*  23 */     super(forward);
/*  24 */     this._forward = forward;
/*  25 */     this._objectIdInfo = objectIdInfo;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjectIdReferenceProperty(ObjectIdReferenceProperty src, JsonDeserializer<?> deser, NullValueProvider nva) {
/*  31 */     super(src, deser, nva);
/*  32 */     this._forward = src._forward;
/*  33 */     this._objectIdInfo = src._objectIdInfo;
/*     */   }
/*     */ 
/*     */   
/*     */   public ObjectIdReferenceProperty(ObjectIdReferenceProperty src, PropertyName newName) {
/*  38 */     super(src, newName);
/*  39 */     this._forward = src._forward;
/*  40 */     this._objectIdInfo = src._objectIdInfo;
/*     */   }
/*     */ 
/*     */   
/*     */   public SettableBeanProperty withName(PropertyName newName) {
/*  45 */     return new ObjectIdReferenceProperty(this, newName);
/*     */   }
/*     */ 
/*     */   
/*     */   public SettableBeanProperty withValueDeserializer(JsonDeserializer<?> deser) {
/*  50 */     if (this._valueDeserializer == deser) {
/*  51 */       return this;
/*     */     }
/*     */     
/*  54 */     NullValueProvider nvp = (this._valueDeserializer == this._nullProvider) ? (NullValueProvider)deser : this._nullProvider;
/*  55 */     return new ObjectIdReferenceProperty(this, deser, nvp);
/*     */   }
/*     */ 
/*     */   
/*     */   public SettableBeanProperty withNullProvider(NullValueProvider nva) {
/*  60 */     return new ObjectIdReferenceProperty(this, this._valueDeserializer, nva);
/*     */   }
/*     */ 
/*     */   
/*     */   public void fixAccess(DeserializationConfig config) {
/*  65 */     if (this._forward != null) {
/*  66 */       this._forward.fixAccess(config);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public <A extends java.lang.annotation.Annotation> A getAnnotation(Class<A> acls) {
/*  72 */     return (A)this._forward.getAnnotation(acls);
/*     */   }
/*     */ 
/*     */   
/*     */   public AnnotatedMember getMember() {
/*  77 */     return this._forward.getMember();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getCreatorIndex() {
/*  82 */     return this._forward.getCreatorIndex();
/*     */   }
/*     */ 
/*     */   
/*     */   public void deserializeAndSet(JsonParser p, DeserializationContext ctxt, Object instance) throws IOException {
/*  87 */     deserializeSetAndReturn(p, ctxt, instance);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object deserializeSetAndReturn(JsonParser p, DeserializationContext ctxt, Object instance) throws IOException {
/*     */     try {
/*  94 */       return setAndReturn(instance, deserialize(p, ctxt));
/*  95 */     } catch (UnresolvedForwardReference reference) {
/*  96 */       boolean usingIdentityInfo = (this._objectIdInfo != null || this._valueDeserializer.getObjectIdReader() != null);
/*  97 */       if (!usingIdentityInfo) {
/*  98 */         throw JsonMappingException.from(p, "Unresolved forward reference but no identity info", reference);
/*     */       }
/* 100 */       reference.getRoid().appendReferring(new PropertyReferring(this, reference, this._type.getRawClass(), instance));
/* 101 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void set(Object instance, Object value) throws IOException {
/* 107 */     this._forward.set(instance, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object setAndReturn(Object instance, Object value) throws IOException {
/* 112 */     return this._forward.setAndReturn(instance, value);
/*     */   }
/*     */   
/*     */   public static final class PropertyReferring
/*     */     extends ReadableObjectId.Referring
/*     */   {
/*     */     private final ObjectIdReferenceProperty _parent;
/*     */     public final Object _pojo;
/*     */     
/*     */     public PropertyReferring(ObjectIdReferenceProperty parent, UnresolvedForwardReference ref, Class<?> type, Object ob) {
/* 122 */       super(ref, type);
/* 123 */       this._parent = parent;
/* 124 */       this._pojo = ob;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void handleResolvedForwardReference(Object id, Object value) throws IOException {
/* 130 */       if (!hasId(id)) {
/* 131 */         throw new IllegalArgumentException("Trying to resolve a forward reference with id [" + id + "] that wasn't previously seen as unresolved.");
/*     */       }
/*     */       
/* 134 */       this._parent.set(this._pojo, value);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\deser\impl\ObjectIdReferenceProperty.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */