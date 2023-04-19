/*    */ package com.fasterxml.jackson.databind.ser.std;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonGenerator;
/*    */ import com.fasterxml.jackson.core.JsonToken;
/*    */ import com.fasterxml.jackson.core.type.WritableTypeId;
/*    */ import com.fasterxml.jackson.databind.JavaType;
/*    */ import com.fasterxml.jackson.databind.JsonMappingException;
/*    */ import com.fasterxml.jackson.databind.JsonNode;
/*    */ import com.fasterxml.jackson.databind.SerializerProvider;
/*    */ import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
/*    */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonArrayFormatVisitor;
/*    */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatTypes;
/*    */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
/*    */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*    */ import com.fasterxml.jackson.databind.node.ObjectNode;
/*    */ import java.io.IOException;
/*    */ import java.lang.reflect.Type;
/*    */ 
/*    */ 
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
/*    */ public class ByteArraySerializer
/*    */   extends StdSerializer<byte[]>
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public ByteArraySerializer() {
/* 36 */     super((Class)byte[].class);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isEmpty(SerializerProvider prov, byte[] value) {
/* 41 */     return (value.length == 0);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void serialize(byte[] value, JsonGenerator g, SerializerProvider provider) throws IOException {
/* 48 */     g.writeBinary(provider.getConfig().getBase64Variant(), value, 0, value.length);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void serializeWithType(byte[] value, JsonGenerator g, SerializerProvider provider, TypeSerializer typeSer) throws IOException {
/* 58 */     WritableTypeId typeIdDef = typeSer.writeTypePrefix(g, typeSer
/* 59 */         .typeId(value, JsonToken.VALUE_EMBEDDED_OBJECT));
/* 60 */     g.writeBinary(provider.getConfig().getBase64Variant(), value, 0, value.length);
/*    */     
/* 62 */     typeSer.writeTypeSuffix(g, typeIdDef);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
/* 75 */     ObjectNode o = createSchemaNode("array", true);
/* 76 */     ObjectNode itemSchema = createSchemaNode("byte");
/* 77 */     return o.set("items", (JsonNode)itemSchema);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
/* 90 */     JsonArrayFormatVisitor v2 = visitor.expectArrayFormat(typeHint);
/* 91 */     if (v2 != null)
/* 92 */       v2.itemsFormat(JsonFormatTypes.INTEGER); 
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\ser\std\ByteArraySerializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */