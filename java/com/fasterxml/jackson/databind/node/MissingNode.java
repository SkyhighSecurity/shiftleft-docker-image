/*     */ package com.fasterxml.jackson.databind.node;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.core.JsonProcessingException;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*     */ import java.io.IOException;
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
/*     */ public final class MissingNode
/*     */   extends ValueNode
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  27 */   private static final MissingNode instance = new MissingNode();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object readResolve() {
/*  38 */     return instance;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isMissingNode() {
/*  43 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T extends JsonNode> T deepCopy() {
/*  49 */     return (T)this;
/*     */   } public static MissingNode getInstance() {
/*  51 */     return instance;
/*     */   }
/*     */ 
/*     */   
/*     */   public JsonNodeType getNodeType() {
/*  56 */     return JsonNodeType.MISSING;
/*     */   }
/*     */   public JsonToken asToken() {
/*  59 */     return JsonToken.NOT_AVAILABLE;
/*     */   } public String asText() {
/*  61 */     return "";
/*     */   } public String asText(String defaultValue) {
/*  63 */     return defaultValue;
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
/*     */ 
/*     */ 
/*     */   
/*     */   public final void serialize(JsonGenerator jg, SerializerProvider provider) throws IOException, JsonProcessingException {
/*  84 */     jg.writeNull();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void serializeWithType(JsonGenerator g, SerializerProvider provider, TypeSerializer typeSer) throws IOException, JsonProcessingException {
/*  92 */     g.writeNull();
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
/*     */   public boolean equals(Object o) {
/* 106 */     return (o == this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonNode require() {
/* 112 */     return (JsonNode)_reportRequiredViolation("require() called on `MissingNode`", new Object[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonNode requireNonNull() {
/* 118 */     return (JsonNode)_reportRequiredViolation("requireNonNull() called on `MissingNode`", new Object[0]);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 123 */     return JsonNodeType.MISSING.ordinal();
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\node\MissingNode.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */