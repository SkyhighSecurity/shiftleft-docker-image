/*     */ package com.fasterxml.jackson.databind.deser.impl;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.ObjectIdGenerator;
/*     */ import com.fasterxml.jackson.annotation.ObjectIdResolver;
/*     */ import com.fasterxml.jackson.core.JsonLocation;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
/*     */ import java.io.IOException;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
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
/*     */ public class ReadableObjectId
/*     */ {
/*     */   protected Object _item;
/*     */   protected final ObjectIdGenerator.IdKey _key;
/*     */   protected LinkedList<Referring> _referringProperties;
/*     */   protected ObjectIdResolver _resolver;
/*     */   
/*     */   public ReadableObjectId(ObjectIdGenerator.IdKey key) {
/*  33 */     this._key = key;
/*     */   }
/*     */   
/*     */   public void setResolver(ObjectIdResolver resolver) {
/*  37 */     this._resolver = resolver;
/*     */   }
/*     */   
/*     */   public ObjectIdGenerator.IdKey getKey() {
/*  41 */     return this._key;
/*     */   }
/*     */   
/*     */   public void appendReferring(Referring currentReferring) {
/*  45 */     if (this._referringProperties == null) {
/*  46 */       this._referringProperties = new LinkedList<>();
/*     */     }
/*  48 */     this._referringProperties.add(currentReferring);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void bindItem(Object ob) throws IOException {
/*  57 */     this._resolver.bindItem(this._key, ob);
/*  58 */     this._item = ob;
/*  59 */     Object id = this._key.key;
/*  60 */     if (this._referringProperties != null) {
/*  61 */       Iterator<Referring> it = this._referringProperties.iterator();
/*  62 */       this._referringProperties = null;
/*  63 */       while (it.hasNext()) {
/*  64 */         ((Referring)it.next()).handleResolvedForwardReference(id, ob);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public Object resolve() {
/*  70 */     return this._item = this._resolver.resolveId(this._key);
/*     */   }
/*     */   
/*     */   public boolean hasReferringProperties() {
/*  74 */     return (this._referringProperties != null && !this._referringProperties.isEmpty());
/*     */   }
/*     */   
/*     */   public Iterator<Referring> referringProperties() {
/*  78 */     if (this._referringProperties == null) {
/*  79 */       return Collections.<Referring>emptyList().iterator();
/*     */     }
/*  81 */     return this._referringProperties.iterator();
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
/*     */   public boolean tryToResolveUnresolved(DeserializationContext ctxt) {
/* 101 */     return false;
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
/*     */   public ObjectIdResolver getResolver() {
/* 114 */     return this._resolver;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 119 */     return String.valueOf(this._key);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static abstract class Referring
/*     */   {
/*     */     private final UnresolvedForwardReference _reference;
/*     */ 
/*     */     
/*     */     private final Class<?> _beanType;
/*     */ 
/*     */     
/*     */     public Referring(UnresolvedForwardReference ref, Class<?> beanType) {
/* 133 */       this._reference = ref;
/* 134 */       this._beanType = beanType;
/*     */     }
/*     */     
/*     */     public Referring(UnresolvedForwardReference ref, JavaType beanType) {
/* 138 */       this._reference = ref;
/* 139 */       this._beanType = beanType.getRawClass();
/*     */     }
/*     */     
/* 142 */     public JsonLocation getLocation() { return this._reference.getLocation(); } public Class<?> getBeanType() {
/* 143 */       return this._beanType;
/*     */     }
/*     */     public abstract void handleResolvedForwardReference(Object param1Object1, Object param1Object2) throws IOException;
/*     */     public boolean hasId(Object id) {
/* 147 */       return id.equals(this._reference.getUnresolvedId());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\deser\impl\ReadableObjectId.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */