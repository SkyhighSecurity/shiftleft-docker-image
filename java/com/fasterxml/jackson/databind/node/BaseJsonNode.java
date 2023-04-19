/*     */ package com.fasterxml.jackson.databind.node;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonProcessingException;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.core.ObjectCodec;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*     */ import java.io.IOException;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class BaseJsonNode
/*     */   extends JsonNode
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   
/*     */   Object writeReplace() {
/*  28 */     return NodeSerialization.from(this);
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
/*     */   public final JsonNode findPath(String fieldName) {
/*  42 */     JsonNode value = findValue(fieldName);
/*  43 */     if (value == null) {
/*  44 */       return MissingNode.getInstance();
/*     */     }
/*  46 */     return value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract int hashCode();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonNode required(String fieldName) {
/*  60 */     return (JsonNode)_reportRequiredViolation("Node of type `%s` has no fields", new Object[] {
/*  61 */           getClass().getSimpleName()
/*     */         });
/*     */   }
/*     */   
/*     */   public JsonNode required(int index) {
/*  66 */     return (JsonNode)_reportRequiredViolation("Node of type `%s` has no indexed values", new Object[] {
/*  67 */           getClass().getSimpleName()
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonParser traverse() {
/*  78 */     return (JsonParser)new TreeTraversingParser(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public JsonParser traverse(ObjectCodec codec) {
/*  83 */     return (JsonParser)new TreeTraversingParser(this, codec);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract JsonToken asToken();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonParser.NumberType numberType() {
/* 103 */     return null;
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
/*     */   public abstract void serialize(JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider) throws IOException, JsonProcessingException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void serializeWithType(JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider, TypeSerializer paramTypeSerializer) throws IOException, JsonProcessingException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 136 */     return InternalNodeMapper.nodeToString(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toPrettyString() {
/* 141 */     return InternalNodeMapper.nodeToPrettyString(this);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\node\BaseJsonNode.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */