/*     */ package com.fasterxml.jackson.databind.node;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.core.JsonPointer;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.core.TreeNode;
/*     */ import com.fasterxml.jackson.core.type.WritableTypeId;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.SerializationFeature;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*     */ import com.fasterxml.jackson.databind.util.RawValue;
/*     */ import java.io.IOException;
/*     */ import java.io.Serializable;
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class ObjectNode
/*     */   extends ContainerNode<ObjectNode> implements Serializable {
/*     */   public ObjectNode(JsonNodeFactory nc) {
/*  29 */     super(nc);
/*  30 */     this._children = new LinkedHashMap<>();
/*     */   }
/*     */   
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final Map<String, JsonNode> _children;
/*     */   
/*     */   public ObjectNode(JsonNodeFactory nc, Map<String, JsonNode> kids) {
/*  37 */     super(nc);
/*  38 */     this._children = kids;
/*     */   }
/*     */ 
/*     */   
/*     */   protected JsonNode _at(JsonPointer ptr) {
/*  43 */     return get(ptr.getMatchingProperty());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjectNode deepCopy() {
/*  54 */     ObjectNode ret = new ObjectNode(this._nodeFactory);
/*     */     
/*  56 */     for (Map.Entry<String, JsonNode> entry : this._children.entrySet()) {
/*  57 */       ret._children.put(entry.getKey(), ((JsonNode)entry.getValue()).deepCopy());
/*     */     }
/*  59 */     return ret;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty(SerializerProvider serializers) {
/*  70 */     return this._children.isEmpty();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonNodeType getNodeType() {
/*  81 */     return JsonNodeType.OBJECT;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isObject() {
/*  86 */     return true;
/*     */   }
/*     */   public JsonToken asToken() {
/*  89 */     return JsonToken.START_OBJECT;
/*     */   }
/*     */   
/*     */   public int size() {
/*  93 */     return this._children.size();
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/*  97 */     return this._children.isEmpty();
/*     */   }
/*     */   
/*     */   public Iterator<JsonNode> elements() {
/* 101 */     return this._children.values().iterator();
/*     */   }
/*     */   
/*     */   public JsonNode get(int index) {
/* 105 */     return null;
/*     */   }
/*     */   
/*     */   public JsonNode get(String fieldName) {
/* 109 */     return this._children.get(fieldName);
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<String> fieldNames() {
/* 114 */     return this._children.keySet().iterator();
/*     */   }
/*     */ 
/*     */   
/*     */   public JsonNode path(int index) {
/* 119 */     return MissingNode.getInstance();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonNode path(String fieldName) {
/* 125 */     JsonNode n = this._children.get(fieldName);
/* 126 */     if (n != null) {
/* 127 */       return n;
/*     */     }
/* 129 */     return MissingNode.getInstance();
/*     */   }
/*     */ 
/*     */   
/*     */   public JsonNode required(String fieldName) {
/* 134 */     JsonNode n = this._children.get(fieldName);
/* 135 */     if (n != null) {
/* 136 */       return n;
/*     */     }
/* 138 */     return (JsonNode)_reportRequiredViolation("No value for property '%s' of `ObjectNode`", new Object[] { fieldName });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator<Map.Entry<String, JsonNode>> fields() {
/* 147 */     return this._children.entrySet().iterator();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjectNode with(String propertyName) {
/* 153 */     JsonNode n = this._children.get(propertyName);
/* 154 */     if (n != null) {
/* 155 */       if (n instanceof ObjectNode) {
/* 156 */         return (ObjectNode)n;
/*     */       }
/* 158 */       throw new UnsupportedOperationException("Property '" + propertyName + "' has value that is not of type ObjectNode (but " + n
/*     */           
/* 160 */           .getClass().getName() + ")");
/*     */     } 
/* 162 */     ObjectNode result = objectNode();
/* 163 */     this._children.put(propertyName, result);
/* 164 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ArrayNode withArray(String propertyName) {
/* 171 */     JsonNode n = this._children.get(propertyName);
/* 172 */     if (n != null) {
/* 173 */       if (n instanceof ArrayNode) {
/* 174 */         return (ArrayNode)n;
/*     */       }
/* 176 */       throw new UnsupportedOperationException("Property '" + propertyName + "' has value that is not of type ArrayNode (but " + n
/*     */           
/* 178 */           .getClass().getName() + ")");
/*     */     } 
/* 180 */     ArrayNode result = arrayNode();
/* 181 */     this._children.put(propertyName, result);
/* 182 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Comparator<JsonNode> comparator, JsonNode o) {
/* 188 */     if (!(o instanceof ObjectNode)) {
/* 189 */       return false;
/*     */     }
/* 191 */     ObjectNode other = (ObjectNode)o;
/* 192 */     Map<String, JsonNode> m1 = this._children;
/* 193 */     Map<String, JsonNode> m2 = other._children;
/*     */     
/* 195 */     int len = m1.size();
/* 196 */     if (m2.size() != len) {
/* 197 */       return false;
/*     */     }
/*     */     
/* 200 */     for (Map.Entry<String, JsonNode> entry : m1.entrySet()) {
/* 201 */       JsonNode v2 = m2.get(entry.getKey());
/* 202 */       if (v2 == null || !((JsonNode)entry.getValue()).equals(comparator, v2)) {
/* 203 */         return false;
/*     */       }
/*     */     } 
/* 206 */     return true;
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
/*     */   public JsonNode findValue(String fieldName) {
/* 218 */     for (Map.Entry<String, JsonNode> entry : this._children.entrySet()) {
/* 219 */       if (fieldName.equals(entry.getKey())) {
/* 220 */         return entry.getValue();
/*     */       }
/* 222 */       JsonNode value = ((JsonNode)entry.getValue()).findValue(fieldName);
/* 223 */       if (value != null) {
/* 224 */         return value;
/*     */       }
/*     */     } 
/* 227 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<JsonNode> findValues(String fieldName, List<JsonNode> foundSoFar) {
/* 233 */     for (Map.Entry<String, JsonNode> entry : this._children.entrySet()) {
/* 234 */       if (fieldName.equals(entry.getKey())) {
/* 235 */         if (foundSoFar == null) {
/* 236 */           foundSoFar = new ArrayList<>();
/*     */         }
/* 238 */         foundSoFar.add(entry.getValue()); continue;
/*     */       } 
/* 240 */       foundSoFar = ((JsonNode)entry.getValue()).findValues(fieldName, foundSoFar);
/*     */     } 
/*     */     
/* 243 */     return foundSoFar;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<String> findValuesAsText(String fieldName, List<String> foundSoFar) {
/* 249 */     for (Map.Entry<String, JsonNode> entry : this._children.entrySet()) {
/* 250 */       if (fieldName.equals(entry.getKey())) {
/* 251 */         if (foundSoFar == null) {
/* 252 */           foundSoFar = new ArrayList<>();
/*     */         }
/* 254 */         foundSoFar.add(((JsonNode)entry.getValue()).asText()); continue;
/*     */       } 
/* 256 */       foundSoFar = ((JsonNode)entry.getValue()).findValuesAsText(fieldName, foundSoFar);
/*     */     } 
/*     */ 
/*     */     
/* 260 */     return foundSoFar;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjectNode findParent(String fieldName) {
/* 266 */     for (Map.Entry<String, JsonNode> entry : this._children.entrySet()) {
/* 267 */       if (fieldName.equals(entry.getKey())) {
/* 268 */         return this;
/*     */       }
/* 270 */       JsonNode value = ((JsonNode)entry.getValue()).findParent(fieldName);
/* 271 */       if (value != null) {
/* 272 */         return (ObjectNode)value;
/*     */       }
/*     */     } 
/* 275 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<JsonNode> findParents(String fieldName, List<JsonNode> foundSoFar) {
/* 281 */     for (Map.Entry<String, JsonNode> entry : this._children.entrySet()) {
/* 282 */       if (fieldName.equals(entry.getKey())) {
/* 283 */         if (foundSoFar == null) {
/* 284 */           foundSoFar = new ArrayList<>();
/*     */         }
/* 286 */         foundSoFar.add(this);
/*     */         continue;
/*     */       } 
/* 289 */       foundSoFar = ((JsonNode)entry.getValue()).findParents(fieldName, foundSoFar);
/*     */     } 
/*     */     
/* 292 */     return foundSoFar;
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
/*     */   public void serialize(JsonGenerator g, SerializerProvider provider) throws IOException {
/* 311 */     boolean trimEmptyArray = (provider != null && !provider.isEnabled(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS));
/* 312 */     g.writeStartObject(this);
/* 313 */     for (Map.Entry<String, JsonNode> en : this._children.entrySet()) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 319 */       BaseJsonNode value = (BaseJsonNode)en.getValue();
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 324 */       if (trimEmptyArray && value.isArray() && value.isEmpty(provider)) {
/*     */         continue;
/*     */       }
/* 327 */       g.writeFieldName(en.getKey());
/* 328 */       value.serialize(g, provider);
/*     */     } 
/* 330 */     g.writeEndObject();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void serializeWithType(JsonGenerator g, SerializerProvider provider, TypeSerializer typeSer) throws IOException {
/* 340 */     boolean trimEmptyArray = (provider != null && !provider.isEnabled(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS));
/*     */     
/* 342 */     WritableTypeId typeIdDef = typeSer.writeTypePrefix(g, typeSer
/* 343 */         .typeId(this, JsonToken.START_OBJECT));
/* 344 */     for (Map.Entry<String, JsonNode> en : this._children.entrySet()) {
/* 345 */       BaseJsonNode value = (BaseJsonNode)en.getValue();
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 350 */       if (trimEmptyArray && value.isArray() && value.isEmpty(provider)) {
/*     */         continue;
/*     */       }
/*     */       
/* 354 */       g.writeFieldName(en.getKey());
/* 355 */       value.serialize(g, provider);
/*     */     } 
/* 357 */     typeSer.writeTypeSuffix(g, typeIdDef);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T extends JsonNode> T set(String fieldName, JsonNode value) {
/* 387 */     if (value == null) {
/* 388 */       value = nullNode();
/*     */     }
/* 390 */     this._children.put(fieldName, value);
/* 391 */     return (T)this;
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
/*     */   public <T extends JsonNode> T setAll(Map<String, ? extends JsonNode> properties) {
/* 409 */     for (Map.Entry<String, ? extends JsonNode> en : properties.entrySet()) {
/* 410 */       JsonNode n = en.getValue();
/* 411 */       if (n == null) {
/* 412 */         n = nullNode();
/*     */       }
/* 414 */       this._children.put(en.getKey(), n);
/*     */     } 
/* 416 */     return (T)this;
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
/*     */   public <T extends JsonNode> T setAll(ObjectNode other) {
/* 434 */     this._children.putAll(other._children);
/* 435 */     return (T)this;
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
/*     */   public JsonNode replace(String fieldName, JsonNode value) {
/* 452 */     if (value == null) {
/* 453 */       value = nullNode();
/*     */     }
/* 455 */     return this._children.put(fieldName, value);
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
/*     */   public <T extends JsonNode> T without(String fieldName) {
/* 471 */     this._children.remove(fieldName);
/* 472 */     return (T)this;
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
/*     */   public <T extends JsonNode> T without(Collection<String> fieldNames) {
/* 490 */     this._children.keySet().removeAll(fieldNames);
/* 491 */     return (T)this;
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
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public JsonNode put(String fieldName, JsonNode value) {
/* 515 */     if (value == null) {
/* 516 */       value = nullNode();
/*     */     }
/* 518 */     return this._children.put(fieldName, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonNode remove(String fieldName) {
/* 529 */     return this._children.remove(fieldName);
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
/*     */   public ObjectNode remove(Collection<String> fieldNames) {
/* 542 */     this._children.keySet().removeAll(fieldNames);
/* 543 */     return this;
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
/*     */   public ObjectNode removeAll() {
/* 555 */     this._children.clear();
/* 556 */     return this;
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
/*     */   @Deprecated
/*     */   public JsonNode putAll(Map<String, ? extends JsonNode> properties) {
/* 571 */     return setAll(properties);
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
/*     */   @Deprecated
/*     */   public JsonNode putAll(ObjectNode other) {
/* 586 */     return setAll(other);
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
/*     */   public ObjectNode retain(Collection<String> fieldNames) {
/* 599 */     this._children.keySet().retainAll(fieldNames);
/* 600 */     return this;
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
/*     */   public ObjectNode retain(String... fieldNames) {
/* 612 */     return retain(Arrays.asList(fieldNames));
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
/*     */   
/*     */   public ArrayNode putArray(String fieldName) {
/* 634 */     ArrayNode n = arrayNode();
/* 635 */     _put(fieldName, n);
/* 636 */     return n;
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
/*     */   public ObjectNode putObject(String fieldName) {
/* 652 */     ObjectNode n = objectNode();
/* 653 */     _put(fieldName, n);
/* 654 */     return n;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjectNode putPOJO(String fieldName, Object pojo) {
/* 661 */     return _put(fieldName, pojoNode(pojo));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjectNode putRawValue(String fieldName, RawValue raw) {
/* 668 */     return _put(fieldName, rawValueNode(raw));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjectNode putNull(String fieldName) {
/* 676 */     this._children.put(fieldName, nullNode());
/* 677 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjectNode put(String fieldName, short v) {
/* 686 */     return _put(fieldName, numberNode(v));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjectNode put(String fieldName, Short v) {
/* 696 */     return _put(fieldName, (v == null) ? nullNode() : 
/* 697 */         numberNode(v.shortValue()));
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
/*     */   public ObjectNode put(String fieldName, int v) {
/* 710 */     return _put(fieldName, numberNode(v));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjectNode put(String fieldName, Integer v) {
/* 720 */     return _put(fieldName, (v == null) ? nullNode() : 
/* 721 */         numberNode(v.intValue()));
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
/*     */   public ObjectNode put(String fieldName, long v) {
/* 734 */     return _put(fieldName, numberNode(v));
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
/*     */   public ObjectNode put(String fieldName, Long v) {
/* 750 */     return _put(fieldName, (v == null) ? nullNode() : 
/* 751 */         numberNode(v.longValue()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjectNode put(String fieldName, float v) {
/* 760 */     return _put(fieldName, numberNode(v));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjectNode put(String fieldName, Float v) {
/* 770 */     return _put(fieldName, (v == null) ? nullNode() : 
/* 771 */         numberNode(v.floatValue()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjectNode put(String fieldName, double v) {
/* 780 */     return _put(fieldName, numberNode(v));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjectNode put(String fieldName, Double v) {
/* 790 */     return _put(fieldName, (v == null) ? nullNode() : 
/* 791 */         numberNode(v.doubleValue()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjectNode put(String fieldName, BigDecimal v) {
/* 800 */     return _put(fieldName, (v == null) ? nullNode() : 
/* 801 */         numberNode(v));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjectNode put(String fieldName, BigInteger v) {
/* 812 */     return _put(fieldName, (v == null) ? nullNode() : 
/* 813 */         numberNode(v));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjectNode put(String fieldName, String v) {
/* 822 */     return _put(fieldName, (v == null) ? nullNode() : 
/* 823 */         textNode(v));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjectNode put(String fieldName, boolean v) {
/* 832 */     return _put(fieldName, booleanNode(v));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjectNode put(String fieldName, Boolean v) {
/* 842 */     return _put(fieldName, (v == null) ? nullNode() : 
/* 843 */         booleanNode(v.booleanValue()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjectNode put(String fieldName, byte[] v) {
/* 852 */     return _put(fieldName, (v == null) ? nullNode() : 
/* 853 */         binaryNode(v));
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
/*     */   public boolean equals(Object o) {
/* 865 */     if (o == this) return true; 
/* 866 */     if (o == null) return false; 
/* 867 */     if (o instanceof ObjectNode) {
/* 868 */       return _childrenEqual((ObjectNode)o);
/*     */     }
/* 870 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean _childrenEqual(ObjectNode other) {
/* 878 */     return this._children.equals(other._children);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 884 */     return this._children.hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ObjectNode _put(String fieldName, JsonNode value) {
/* 895 */     this._children.put(fieldName, value);
/* 896 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\node\ObjectNode.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */