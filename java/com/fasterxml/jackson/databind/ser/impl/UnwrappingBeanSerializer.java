/*     */ package com.fasterxml.jackson.databind.ser.impl;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.SerializationFeature;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*     */ import com.fasterxml.jackson.databind.ser.std.BeanSerializerBase;
/*     */ import com.fasterxml.jackson.databind.util.NameTransformer;
/*     */ import java.io.IOException;
/*     */ import java.io.Serializable;
/*     */ import java.util.Set;
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
/*     */ public class UnwrappingBeanSerializer
/*     */   extends BeanSerializerBase
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final NameTransformer _nameTransformer;
/*     */   
/*     */   public UnwrappingBeanSerializer(BeanSerializerBase src, NameTransformer transformer) {
/*  36 */     super(src, transformer);
/*  37 */     this._nameTransformer = transformer;
/*     */   }
/*     */ 
/*     */   
/*     */   public UnwrappingBeanSerializer(UnwrappingBeanSerializer src, ObjectIdWriter objectIdWriter) {
/*  42 */     super(src, objectIdWriter);
/*  43 */     this._nameTransformer = src._nameTransformer;
/*     */   }
/*     */ 
/*     */   
/*     */   public UnwrappingBeanSerializer(UnwrappingBeanSerializer src, ObjectIdWriter objectIdWriter, Object filterId) {
/*  48 */     super(src, objectIdWriter, filterId);
/*  49 */     this._nameTransformer = src._nameTransformer;
/*     */   }
/*     */   
/*     */   protected UnwrappingBeanSerializer(UnwrappingBeanSerializer src, Set<String> toIgnore) {
/*  53 */     super(src, toIgnore);
/*  54 */     this._nameTransformer = src._nameTransformer;
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
/*     */   public JsonSerializer<Object> unwrappingSerializer(NameTransformer transformer) {
/*  66 */     return (JsonSerializer<Object>)new UnwrappingBeanSerializer(this, transformer);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isUnwrappingSerializer() {
/*  71 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public BeanSerializerBase withObjectIdWriter(ObjectIdWriter objectIdWriter) {
/*  76 */     return new UnwrappingBeanSerializer(this, objectIdWriter);
/*     */   }
/*     */ 
/*     */   
/*     */   public BeanSerializerBase withFilterId(Object filterId) {
/*  81 */     return new UnwrappingBeanSerializer(this, this._objectIdWriter, filterId);
/*     */   }
/*     */ 
/*     */   
/*     */   protected BeanSerializerBase withIgnorals(Set<String> toIgnore) {
/*  86 */     return new UnwrappingBeanSerializer(this, toIgnore);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected BeanSerializerBase asArraySerializer() {
/*  95 */     return this;
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
/*     */   public final void serialize(Object bean, JsonGenerator gen, SerializerProvider provider) throws IOException {
/* 112 */     gen.setCurrentValue(bean);
/* 113 */     if (this._objectIdWriter != null) {
/* 114 */       _serializeWithObjectId(bean, gen, provider, false);
/*     */       return;
/*     */     } 
/* 117 */     if (this._propertyFilterId != null) {
/* 118 */       serializeFieldsFiltered(bean, gen, provider);
/*     */     } else {
/* 120 */       serializeFields(bean, gen, provider);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void serializeWithType(Object bean, JsonGenerator gen, SerializerProvider provider, TypeSerializer typeSer) throws IOException {
/* 128 */     if (provider.isEnabled(SerializationFeature.FAIL_ON_UNWRAPPED_TYPE_IDENTIFIERS)) {
/* 129 */       provider.reportBadDefinition(handledType(), "Unwrapped property requires use of type information: cannot serialize without disabling `SerializationFeature.FAIL_ON_UNWRAPPED_TYPE_IDENTIFIERS`");
/*     */     }
/*     */     
/* 132 */     gen.setCurrentValue(bean);
/* 133 */     if (this._objectIdWriter != null) {
/* 134 */       _serializeWithObjectId(bean, gen, provider, typeSer);
/*     */       return;
/*     */     } 
/* 137 */     if (this._propertyFilterId != null) {
/* 138 */       serializeFieldsFiltered(bean, gen, provider);
/*     */     } else {
/* 140 */       serializeFields(bean, gen, provider);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 151 */     return "UnwrappingBeanSerializer for " + handledType().getName();
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\ser\impl\UnwrappingBeanSerializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */