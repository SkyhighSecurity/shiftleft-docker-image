/*    */ package com.fasterxml.jackson.databind.node;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonGenerator;
/*    */ import com.fasterxml.jackson.core.JsonToken;
/*    */ import com.fasterxml.jackson.databind.JsonNode;
/*    */ import com.fasterxml.jackson.databind.SerializerProvider;
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
/*    */ public class NullNode
/*    */   extends ValueNode
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/* 21 */   public static final NullNode instance = new NullNode();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected Object readResolve() {
/* 31 */     return instance;
/*    */   }
/*    */   public static NullNode getInstance() {
/* 34 */     return instance;
/*    */   }
/*    */   
/*    */   public JsonNodeType getNodeType() {
/* 38 */     return JsonNodeType.NULL;
/*    */   }
/*    */   public JsonToken asToken() {
/* 41 */     return JsonToken.VALUE_NULL;
/*    */   }
/* 43 */   public String asText(String defaultValue) { return defaultValue; } public String asText() {
/* 44 */     return "null";
/*    */   }
/*    */ 
/*    */   
/*    */   public JsonNode requireNonNull() {
/* 49 */     return (JsonNode)_reportRequiredViolation("requireNonNull() called on `NullNode`", new Object[0]);
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
/*    */   public final void serialize(JsonGenerator g, SerializerProvider provider) throws IOException {
/* 65 */     provider.defaultSerializeNull(g);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 72 */     return (o == this || o instanceof NullNode);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 77 */     return JsonNodeType.NULL.ordinal();
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\node\NullNode.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */