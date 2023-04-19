/*    */ package com.fasterxml.jackson.databind.ser.std;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonGenerator;
/*    */ import com.fasterxml.jackson.databind.BeanProperty;
/*    */ import com.fasterxml.jackson.databind.JavaType;
/*    */ import com.fasterxml.jackson.databind.JsonSerializer;
/*    */ import com.fasterxml.jackson.databind.SerializationFeature;
/*    */ import com.fasterxml.jackson.databind.SerializerProvider;
/*    */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*    */ import com.fasterxml.jackson.databind.ser.ContainerSerializer;
/*    */ import java.io.IOException;
/*    */ import java.util.EnumSet;
/*    */ 
/*    */ public class EnumSetSerializer
/*    */   extends AsArraySerializerBase<EnumSet<? extends Enum<?>>>
/*    */ {
/*    */   public EnumSetSerializer(JavaType elemType) {
/* 18 */     super(EnumSet.class, elemType, true, (TypeSerializer)null, (JsonSerializer)null);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public EnumSetSerializer(EnumSetSerializer src, BeanProperty property, TypeSerializer vts, JsonSerializer<?> valueSerializer, Boolean unwrapSingle) {
/* 24 */     super(src, property, vts, valueSerializer, unwrapSingle);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public EnumSetSerializer _withValueTypeSerializer(TypeSerializer vts) {
/* 30 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public EnumSetSerializer withResolved(BeanProperty property, TypeSerializer vts, JsonSerializer<?> elementSerializer, Boolean unwrapSingle) {
/* 37 */     return new EnumSetSerializer(this, property, vts, elementSerializer, unwrapSingle);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isEmpty(SerializerProvider prov, EnumSet<? extends Enum<?>> value) {
/* 42 */     return value.isEmpty();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean hasSingleElement(EnumSet<? extends Enum<?>> value) {
/* 47 */     return (value.size() == 1);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final void serialize(EnumSet<? extends Enum<?>> value, JsonGenerator gen, SerializerProvider provider) throws IOException {
/* 54 */     int len = value.size();
/* 55 */     if (len == 1 && ((
/* 56 */       this._unwrapSingle == null && provider
/* 57 */       .isEnabled(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED)) || this._unwrapSingle == Boolean.TRUE)) {
/*    */       
/* 59 */       serializeContents(value, gen, provider);
/*    */       
/*    */       return;
/*    */     } 
/* 63 */     gen.writeStartArray(len);
/* 64 */     serializeContents(value, gen, provider);
/* 65 */     gen.writeEndArray();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void serializeContents(EnumSet<? extends Enum<?>> value, JsonGenerator gen, SerializerProvider provider) throws IOException {
/* 73 */     JsonSerializer<Object> enumSer = this._elementSerializer;
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 78 */     for (Enum<?> en : value) {
/* 79 */       if (enumSer == null)
/*    */       {
/*    */ 
/*    */         
/* 83 */         enumSer = provider.findValueSerializer(en.getDeclaringClass(), this._property);
/*    */       }
/* 85 */       enumSer.serialize(en, gen, provider);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\ser\std\EnumSetSerializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */