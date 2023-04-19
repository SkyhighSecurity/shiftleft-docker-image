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
/*    */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
/*    */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*    */ import com.fasterxml.jackson.databind.util.TokenBuffer;
/*    */ import java.io.IOException;
/*    */ import java.lang.reflect.Type;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @JacksonStdImpl
/*    */ public class TokenBufferSerializer
/*    */   extends StdSerializer<TokenBuffer>
/*    */ {
/*    */   public TokenBufferSerializer() {
/* 28 */     super(TokenBuffer.class);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void serialize(TokenBuffer value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
/* 34 */     value.serialize(jgen);
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final void serializeWithType(TokenBuffer value, JsonGenerator g, SerializerProvider provider, TypeSerializer typeSer) throws IOException {
/* 53 */     WritableTypeId typeIdDef = typeSer.writeTypePrefix(g, typeSer
/* 54 */         .typeId(value, JsonToken.VALUE_EMBEDDED_OBJECT));
/* 55 */     serialize(value, g, provider);
/* 56 */     typeSer.writeTypeSuffix(g, typeIdDef);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
/* 64 */     return (JsonNode)createSchemaNode("any", true);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
/* 73 */     visitor.expectAnyFormat(typeHint);
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\ser\std\TokenBufferSerializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */