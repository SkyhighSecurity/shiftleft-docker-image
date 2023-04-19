/*     */ package com.fasterxml.jackson.databind.ser;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.ser.impl.BeanAsArraySerializer;
/*     */ import com.fasterxml.jackson.databind.ser.impl.ObjectIdWriter;
/*     */ import com.fasterxml.jackson.databind.ser.impl.UnwrappingBeanSerializer;
/*     */ import com.fasterxml.jackson.databind.ser.std.BeanSerializerBase;
/*     */ import com.fasterxml.jackson.databind.util.NameTransformer;
/*     */ import java.io.IOException;
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
/*     */ public class BeanSerializer
/*     */   extends BeanSerializerBase
/*     */ {
/*     */   private static final long serialVersionUID = 29L;
/*     */   
/*     */   public BeanSerializer(JavaType type, BeanSerializerBuilder builder, BeanPropertyWriter[] properties, BeanPropertyWriter[] filteredProperties) {
/*  45 */     super(type, builder, properties, filteredProperties);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected BeanSerializer(BeanSerializerBase src) {
/*  54 */     super(src);
/*     */   }
/*     */ 
/*     */   
/*     */   protected BeanSerializer(BeanSerializerBase src, ObjectIdWriter objectIdWriter) {
/*  59 */     super(src, objectIdWriter);
/*     */   }
/*     */ 
/*     */   
/*     */   protected BeanSerializer(BeanSerializerBase src, ObjectIdWriter objectIdWriter, Object filterId) {
/*  64 */     super(src, objectIdWriter, filterId);
/*     */   }
/*     */   
/*     */   protected BeanSerializer(BeanSerializerBase src, Set<String> toIgnore) {
/*  68 */     super(src, toIgnore);
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
/*     */   @Deprecated
/*     */   public static BeanSerializer createDummy(JavaType forType) {
/*  83 */     return new BeanSerializer(forType, null, NO_PROPS, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static BeanSerializer createDummy(JavaType forType, BeanSerializerBuilder builder) {
/*  94 */     return new BeanSerializer(forType, builder, NO_PROPS, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public JsonSerializer<Object> unwrappingSerializer(NameTransformer unwrapper) {
/*  99 */     return (JsonSerializer<Object>)new UnwrappingBeanSerializer(this, unwrapper);
/*     */   }
/*     */ 
/*     */   
/*     */   public BeanSerializerBase withObjectIdWriter(ObjectIdWriter objectIdWriter) {
/* 104 */     return new BeanSerializer(this, objectIdWriter, this._propertyFilterId);
/*     */   }
/*     */ 
/*     */   
/*     */   public BeanSerializerBase withFilterId(Object filterId) {
/* 109 */     return new BeanSerializer(this, this._objectIdWriter, filterId);
/*     */   }
/*     */ 
/*     */   
/*     */   protected BeanSerializerBase withIgnorals(Set<String> toIgnore) {
/* 114 */     return new BeanSerializer(this, toIgnore);
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
/*     */   protected BeanSerializerBase asArraySerializer() {
/* 132 */     if (this._objectIdWriter == null && this._anyGetterWriter == null && this._propertyFilterId == null)
/*     */     {
/*     */ 
/*     */       
/* 136 */       return (BeanSerializerBase)new BeanAsArraySerializer(this);
/*     */     }
/*     */     
/* 139 */     return this;
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
/*     */   public final void serialize(Object bean, JsonGenerator gen, SerializerProvider provider) throws IOException {
/* 157 */     if (this._objectIdWriter != null) {
/* 158 */       gen.setCurrentValue(bean);
/* 159 */       _serializeWithObjectId(bean, gen, provider, true);
/*     */       return;
/*     */     } 
/* 162 */     gen.writeStartObject(bean);
/* 163 */     if (this._propertyFilterId != null) {
/* 164 */       serializeFieldsFiltered(bean, gen, provider);
/*     */     } else {
/* 166 */       serializeFields(bean, gen, provider);
/*     */     } 
/* 168 */     gen.writeEndObject();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 178 */     return "BeanSerializer for " + handledType().getName();
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\ser\BeanSerializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */