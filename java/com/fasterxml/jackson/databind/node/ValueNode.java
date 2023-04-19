/*     */ package com.fasterxml.jackson.databind.node;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.core.JsonPointer;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.core.TreeNode;
/*     */ import com.fasterxml.jackson.core.type.WritableTypeId;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class ValueNode
/*     */   extends BaseJsonNode
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   
/*     */   protected JsonNode _at(JsonPointer ptr) {
/*  28 */     return MissingNode.getInstance();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T extends JsonNode> T deepCopy() {
/*  37 */     return (T)this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void serializeWithType(JsonGenerator g, SerializerProvider provider, TypeSerializer typeSer) throws IOException {
/*  46 */     WritableTypeId typeIdDef = typeSer.writeTypePrefix(g, typeSer
/*  47 */         .typeId(this, asToken()));
/*  48 */     serialize(g, provider);
/*  49 */     typeSer.writeTypeSuffix(g, typeIdDef);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/*  59 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final JsonNode get(int index) {
/*  68 */     return null;
/*     */   }
/*     */   public final JsonNode path(int index) {
/*  71 */     return MissingNode.getInstance();
/*     */   }
/*     */   public final boolean has(int index) {
/*  74 */     return false;
/*     */   }
/*     */   public final boolean hasNonNull(int index) {
/*  77 */     return false;
/*     */   }
/*     */   public final JsonNode get(String fieldName) {
/*  80 */     return null;
/*     */   }
/*     */   public final JsonNode path(String fieldName) {
/*  83 */     return MissingNode.getInstance();
/*     */   }
/*     */   public final boolean has(String fieldName) {
/*  86 */     return false;
/*     */   }
/*     */   public final boolean hasNonNull(String fieldName) {
/*  89 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final JsonNode findValue(String fieldName) {
/*  99 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final ObjectNode findParent(String fieldName) {
/* 105 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public final List<JsonNode> findValues(String fieldName, List<JsonNode> foundSoFar) {
/* 110 */     return foundSoFar;
/*     */   }
/*     */ 
/*     */   
/*     */   public final List<String> findValuesAsText(String fieldName, List<String> foundSoFar) {
/* 115 */     return foundSoFar;
/*     */   }
/*     */ 
/*     */   
/*     */   public final List<JsonNode> findParents(String fieldName, List<JsonNode> foundSoFar) {
/* 120 */     return foundSoFar;
/*     */   }
/*     */   
/*     */   public abstract JsonToken asToken();
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\node\ValueNode.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */