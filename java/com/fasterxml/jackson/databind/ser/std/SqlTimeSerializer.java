/*    */ package com.fasterxml.jackson.databind.ser.std;
/*    */ import com.fasterxml.jackson.core.JsonGenerator;
/*    */ import com.fasterxml.jackson.databind.JavaType;
/*    */ import com.fasterxml.jackson.databind.JsonMappingException;
/*    */ import com.fasterxml.jackson.databind.JsonNode;
/*    */ import com.fasterxml.jackson.databind.SerializerProvider;
/*    */ import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
/*    */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
/*    */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonValueFormat;
/*    */ import java.io.IOException;
/*    */ import java.lang.reflect.Type;
/*    */ import java.sql.Time;
/*    */ 
/*    */ @JacksonStdImpl
/*    */ public class SqlTimeSerializer extends StdScalarSerializer<Time> {
/*    */   public SqlTimeSerializer() {
/* 17 */     super(Time.class);
/*    */   }
/*    */ 
/*    */   
/*    */   public void serialize(Time value, JsonGenerator g, SerializerProvider provider) throws IOException {
/* 22 */     g.writeString(value.toString());
/*    */   }
/*    */ 
/*    */   
/*    */   public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
/* 27 */     return (JsonNode)createSchemaNode("string", true);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
/* 34 */     visitStringFormat(visitor, typeHint, JsonValueFormat.DATE_TIME);
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\ser\std\SqlTimeSerializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */