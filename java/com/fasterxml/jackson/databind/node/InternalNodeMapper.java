/*    */ package com.fasterxml.jackson.databind.node;
/*    */ 
/*    */ import com.fasterxml.jackson.databind.JsonNode;
/*    */ import com.fasterxml.jackson.databind.ObjectReader;
/*    */ import com.fasterxml.jackson.databind.ObjectWriter;
/*    */ import com.fasterxml.jackson.databind.json.JsonMapper;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class InternalNodeMapper
/*    */ {
/* 18 */   private static final JsonMapper JSON_MAPPER = new JsonMapper();
/*    */   
/* 20 */   private static final ObjectWriter STD_WRITER = JSON_MAPPER.writer();
/* 21 */   private static final ObjectWriter PRETTY_WRITER = JSON_MAPPER.writer()
/* 22 */     .withDefaultPrettyPrinter();
/*    */   
/* 24 */   private static final ObjectReader NODE_READER = JSON_MAPPER.readerFor(JsonNode.class);
/*    */ 
/*    */ 
/*    */   
/*    */   public static String nodeToString(JsonNode n) {
/*    */     try {
/* 30 */       return STD_WRITER.writeValueAsString(n);
/* 31 */     } catch (IOException e) {
/* 32 */       throw new RuntimeException(e);
/*    */     } 
/*    */   }
/*    */   
/*    */   public static String nodeToPrettyString(JsonNode n) {
/*    */     try {
/* 38 */       return PRETTY_WRITER.writeValueAsString(n);
/* 39 */     } catch (IOException e) {
/* 40 */       throw new RuntimeException(e);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static byte[] valueToBytes(Object value) throws IOException {
/* 47 */     return JSON_MAPPER.writeValueAsBytes(value);
/*    */   }
/*    */   
/*    */   public static JsonNode bytesToNode(byte[] json) throws IOException {
/* 51 */     return (JsonNode)NODE_READER.readValue(json);
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\node\InternalNodeMapper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */