/*    */ package com.fasterxml.jackson.databind.ser.impl;
/*    */ import com.fasterxml.jackson.core.JsonGenerator;
/*    */ import com.fasterxml.jackson.core.JsonToken;
/*    */ import com.fasterxml.jackson.core.type.WritableTypeId;
/*    */ import com.fasterxml.jackson.databind.JavaType;
/*    */ import com.fasterxml.jackson.databind.JsonMappingException;
/*    */ import com.fasterxml.jackson.databind.JsonNode;
/*    */ import com.fasterxml.jackson.databind.SerializationFeature;
/*    */ import com.fasterxml.jackson.databind.SerializerProvider;
/*    */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
/*    */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*    */ import com.fasterxml.jackson.databind.ser.std.StdSerializer;
/*    */ import java.io.IOException;
/*    */ import java.lang.reflect.Type;
/*    */ 
/*    */ public class UnknownSerializer extends StdSerializer<Object> {
/*    */   public UnknownSerializer() {
/* 18 */     super(Object.class);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public UnknownSerializer(Class<?> cls) {
/* 25 */     super(cls, false);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
/* 32 */     if (provider.isEnabled(SerializationFeature.FAIL_ON_EMPTY_BEANS)) {
/* 33 */       failForEmpty(provider, value);
/*    */     }
/*    */     
/* 36 */     gen.writeStartObject();
/* 37 */     gen.writeEndObject();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final void serializeWithType(Object value, JsonGenerator gen, SerializerProvider provider, TypeSerializer typeSer) throws IOException {
/* 44 */     if (provider.isEnabled(SerializationFeature.FAIL_ON_EMPTY_BEANS)) {
/* 45 */       failForEmpty(provider, value);
/*    */     }
/* 47 */     WritableTypeId typeIdDef = typeSer.writeTypePrefix(gen, typeSer
/* 48 */         .typeId(value, JsonToken.START_OBJECT));
/* 49 */     typeSer.writeTypeSuffix(gen, typeIdDef);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isEmpty(SerializerProvider provider, Object value) {
/* 54 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public JsonNode getSchema(SerializerProvider provider, Type typeHint) throws JsonMappingException {
/* 59 */     return null;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
/* 66 */     visitor.expectAnyFormat(typeHint);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void failForEmpty(SerializerProvider prov, Object value) throws JsonMappingException {
/* 71 */     prov.reportBadDefinition(handledType(), String.format("No serializer found for class %s and no properties discovered to create BeanSerializer (to avoid exception, disable SerializationFeature.FAIL_ON_EMPTY_BEANS)", new Object[] { value
/*    */             
/* 73 */             .getClass().getName() }));
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\ser\impl\UnknownSerializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */