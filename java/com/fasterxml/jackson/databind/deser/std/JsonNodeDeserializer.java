/*     */ package com.fasterxml.jackson.databind.deser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonProcessingException;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.databind.DeserializationConfig;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*     */ import com.fasterxml.jackson.databind.node.ArrayNode;
/*     */ import com.fasterxml.jackson.databind.node.ObjectNode;
/*     */ import java.io.IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JsonNodeDeserializer
/*     */   extends BaseNodeDeserializer<JsonNode>
/*     */ {
/*  23 */   private static final JsonNodeDeserializer instance = new JsonNodeDeserializer();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected JsonNodeDeserializer() {
/*  29 */     super(JsonNode.class, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static JsonDeserializer<? extends JsonNode> getDeserializer(Class<?> nodeClass) {
/*  37 */     if (nodeClass == ObjectNode.class) {
/*  38 */       return ObjectDeserializer.getInstance();
/*     */     }
/*  40 */     if (nodeClass == ArrayNode.class) {
/*  41 */       return ArrayDeserializer.getInstance();
/*     */     }
/*     */     
/*  44 */     return instance;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonNode getNullValue(DeserializationContext ctxt) {
/*  55 */     return (JsonNode)ctxt.getNodeFactory().nullNode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonNode deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
/*  66 */     switch (p.getCurrentTokenId()) {
/*     */       case 1:
/*  68 */         return (JsonNode)deserializeObject(p, ctxt, ctxt.getNodeFactory());
/*     */       case 3:
/*  70 */         return (JsonNode)deserializeArray(p, ctxt, ctxt.getNodeFactory());
/*     */     } 
/*     */     
/*  73 */     return deserializeAny(p, ctxt, ctxt.getNodeFactory());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final class ObjectDeserializer
/*     */     extends BaseNodeDeserializer<ObjectNode>
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */ 
/*     */ 
/*     */     
/*  87 */     protected static final ObjectDeserializer _instance = new ObjectDeserializer();
/*     */     protected ObjectDeserializer() {
/*  89 */       super(ObjectNode.class, Boolean.valueOf(true));
/*     */     } public static ObjectDeserializer getInstance() {
/*  91 */       return _instance;
/*     */     }
/*     */ 
/*     */     
/*     */     public ObjectNode deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
/*  96 */       if (p.isExpectedStartObjectToken()) {
/*  97 */         return deserializeObject(p, ctxt, ctxt.getNodeFactory());
/*     */       }
/*  99 */       if (p.hasToken(JsonToken.FIELD_NAME)) {
/* 100 */         return deserializeObjectAtName(p, ctxt, ctxt.getNodeFactory());
/*     */       }
/*     */ 
/*     */       
/* 104 */       if (p.hasToken(JsonToken.END_OBJECT)) {
/* 105 */         return ctxt.getNodeFactory().objectNode();
/*     */       }
/* 107 */       return (ObjectNode)ctxt.handleUnexpectedToken(ObjectNode.class, p);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ObjectNode deserialize(JsonParser p, DeserializationContext ctxt, ObjectNode node) throws IOException {
/* 119 */       if (p.isExpectedStartObjectToken() || p.hasToken(JsonToken.FIELD_NAME)) {
/* 120 */         return (ObjectNode)updateObject(p, ctxt, node);
/*     */       }
/* 122 */       return (ObjectNode)ctxt.handleUnexpectedToken(ObjectNode.class, p);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final class ArrayDeserializer
/*     */     extends BaseNodeDeserializer<ArrayNode>
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/* 131 */     protected static final ArrayDeserializer _instance = new ArrayDeserializer();
/*     */     protected ArrayDeserializer() {
/* 133 */       super(ArrayNode.class, Boolean.valueOf(true));
/*     */     } public static ArrayDeserializer getInstance() {
/* 135 */       return _instance;
/*     */     }
/*     */ 
/*     */     
/*     */     public ArrayNode deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
/* 140 */       if (p.isExpectedStartArrayToken()) {
/* 141 */         return deserializeArray(p, ctxt, ctxt.getNodeFactory());
/*     */       }
/* 143 */       return (ArrayNode)ctxt.handleUnexpectedToken(ArrayNode.class, p);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ArrayNode deserialize(JsonParser p, DeserializationContext ctxt, ArrayNode node) throws IOException {
/* 155 */       if (p.isExpectedStartArrayToken()) {
/* 156 */         return (ArrayNode)updateArray(p, ctxt, node);
/*     */       }
/* 158 */       return (ArrayNode)ctxt.handleUnexpectedToken(ArrayNode.class, p);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\deser\std\JsonNodeDeserializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */