/*    */ package com.fasterxml.jackson.databind.ser.std;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonGenerator;
/*    */ import com.fasterxml.jackson.databind.JavaType;
/*    */ import com.fasterxml.jackson.databind.JsonMappingException;
/*    */ import com.fasterxml.jackson.databind.JsonSerializable;
/*    */ import com.fasterxml.jackson.databind.SerializerProvider;
/*    */ import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
/*    */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
/*    */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @JacksonStdImpl
/*    */ public class SerializableSerializer
/*    */   extends StdSerializer<JsonSerializable>
/*    */ {
/* 25 */   public static final SerializableSerializer instance = new SerializableSerializer();
/*    */   protected SerializableSerializer() {
/* 27 */     super(JsonSerializable.class);
/*    */   }
/*    */   
/*    */   public boolean isEmpty(SerializerProvider serializers, JsonSerializable value) {
/* 31 */     if (value instanceof JsonSerializable.Base) {
/* 32 */       return ((JsonSerializable.Base)value).isEmpty(serializers);
/*    */     }
/* 34 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public void serialize(JsonSerializable value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
/* 39 */     value.serialize(gen, serializers);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public final void serializeWithType(JsonSerializable value, JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer) throws IOException {
/* 45 */     value.serializeWithType(gen, serializers, typeSer);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
/* 52 */     visitor.expectAnyFormat(typeHint);
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\ser\std\SerializableSerializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */