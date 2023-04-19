/*     */ package com.fasterxml.jackson.databind.ser;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.MapperFeature;
/*     */ import com.fasterxml.jackson.databind.SerializationConfig;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*     */ import com.fasterxml.jackson.databind.ser.std.MapSerializer;
/*     */ import java.util.Map;
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
/*     */ public class AnyGetterWriter
/*     */ {
/*     */   protected final BeanProperty _property;
/*     */   protected final AnnotatedMember _accessor;
/*     */   protected JsonSerializer<Object> _serializer;
/*     */   protected MapSerializer _mapSerializer;
/*     */   
/*     */   public AnyGetterWriter(BeanProperty property, AnnotatedMember accessor, JsonSerializer<?> serializer) {
/*  32 */     this._accessor = accessor;
/*  33 */     this._property = property;
/*  34 */     this._serializer = (JsonSerializer)serializer;
/*  35 */     if (serializer instanceof MapSerializer) {
/*  36 */       this._mapSerializer = (MapSerializer)serializer;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void fixAccess(SerializationConfig config) {
/*  44 */     this._accessor.fixAccess(config
/*  45 */         .isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void getAndSerialize(Object bean, JsonGenerator gen, SerializerProvider provider) throws Exception {
/*  51 */     Object value = this._accessor.getValue(bean);
/*  52 */     if (value == null) {
/*     */       return;
/*     */     }
/*  55 */     if (!(value instanceof Map)) {
/*  56 */       provider.reportBadDefinition(this._property.getType(), String.format("Value returned by 'any-getter' %s() not java.util.Map but %s", new Object[] { this._accessor
/*     */               
/*  58 */               .getName(), value.getClass().getName() }));
/*     */     }
/*     */     
/*  61 */     if (this._mapSerializer != null) {
/*  62 */       this._mapSerializer.serializeFields((Map)value, gen, provider);
/*     */       return;
/*     */     } 
/*  65 */     this._serializer.serialize(value, gen, provider);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void getAndFilter(Object bean, JsonGenerator gen, SerializerProvider provider, PropertyFilter filter) throws Exception {
/*  75 */     Object value = this._accessor.getValue(bean);
/*  76 */     if (value == null) {
/*     */       return;
/*     */     }
/*  79 */     if (!(value instanceof Map)) {
/*  80 */       provider.reportBadDefinition(this._property.getType(), 
/*  81 */           String.format("Value returned by 'any-getter' (%s()) not java.util.Map but %s", new Object[] {
/*  82 */               this._accessor.getName(), value.getClass().getName()
/*     */             }));
/*     */     }
/*  85 */     if (this._mapSerializer != null) {
/*  86 */       this._mapSerializer.serializeFilteredAnyProperties(provider, gen, bean, (Map)value, filter, null);
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/*  91 */     this._serializer.serialize(value, gen, provider);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void resolve(SerializerProvider provider) throws JsonMappingException {
/*  99 */     if (this._serializer instanceof ContextualSerializer) {
/* 100 */       JsonSerializer<?> ser = provider.handlePrimaryContextualization(this._serializer, this._property);
/* 101 */       this._serializer = (JsonSerializer)ser;
/* 102 */       if (ser instanceof MapSerializer)
/* 103 */         this._mapSerializer = (MapSerializer)ser; 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\ser\AnyGetterWriter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */