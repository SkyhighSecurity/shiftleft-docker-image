/*      */ package com.fasterxml.jackson.databind;
/*      */ 
/*      */ import com.fasterxml.jackson.core.JsonPointer;
/*      */ import com.fasterxml.jackson.core.TreeNode;
/*      */ import com.fasterxml.jackson.databind.node.JsonNodeType;
/*      */ import com.fasterxml.jackson.databind.node.MissingNode;
/*      */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*      */ import java.io.IOException;
/*      */ import java.math.BigDecimal;
/*      */ import java.math.BigInteger;
/*      */ import java.util.Collections;
/*      */ import java.util.Comparator;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class JsonNode
/*      */   extends JsonSerializable.Base
/*      */   implements TreeNode, Iterable<JsonNode>
/*      */ {
/*      */   public int size() {
/*   82 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isEmpty() {
/*   93 */     return (size() == 0);
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isValueNode() {
/*   98 */     switch (getNodeType()) { case ARRAY: case OBJECT:
/*      */       case MISSING:
/*  100 */         return false; }
/*      */     
/*  102 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isContainerNode() {
/*  108 */     JsonNodeType type = getNodeType();
/*  109 */     return (type == JsonNodeType.OBJECT || type == JsonNodeType.ARRAY);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isMissingNode() {
/*  114 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isArray() {
/*  119 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isObject() {
/*  124 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JsonNode get(String fieldName) {
/*  167 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Iterator<String> fieldNames() {
/*  194 */     return ClassUtil.emptyIterator();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final JsonNode at(JsonPointer ptr) {
/*  211 */     if (ptr.matches()) {
/*  212 */       return this;
/*      */     }
/*  214 */     JsonNode n = _at(ptr);
/*  215 */     if (n == null) {
/*  216 */       return (JsonNode)MissingNode.getInstance();
/*      */     }
/*  218 */     return n.at(ptr.tail());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final JsonNode at(String jsonPtrExpr) {
/*  241 */     return at(JsonPointer.compile(jsonPtrExpr));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isPojo() {
/*  272 */     return (getNodeType() == JsonNodeType.POJO);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isNumber() {
/*  279 */     return (getNodeType() == JsonNodeType.NUMBER);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isIntegralNumber() {
/*  287 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isFloatingPointNumber() {
/*  293 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isShort() {
/*  305 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isInt() {
/*  317 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isLong() {
/*  329 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isFloat() {
/*  334 */     return false;
/*      */   }
/*  336 */   public boolean isDouble() { return false; }
/*  337 */   public boolean isBigDecimal() { return false; } public boolean isBigInteger() {
/*  338 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isTextual() {
/*  345 */     return (getNodeType() == JsonNodeType.STRING);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isBoolean() {
/*  353 */     return (getNodeType() == JsonNodeType.BOOLEAN);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isNull() {
/*  361 */     return (getNodeType() == JsonNodeType.NULL);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isBinary() {
/*  373 */     return (getNodeType() == JsonNodeType.BINARY);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean canConvertToInt() {
/*  390 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean canConvertToLong() {
/*  406 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String textValue() {
/*  424 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte[] binaryValue() throws IOException {
/*  437 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean booleanValue() {
/*  448 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Number numberValue() {
/*  458 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public short shortValue() {
/*  470 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int intValue() {
/*  482 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long longValue() {
/*  494 */     return 0L;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float floatValue() {
/*  507 */     return 0.0F;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public double doubleValue() {
/*  520 */     return 0.0D;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public BigDecimal decimalValue() {
/*  529 */     return BigDecimal.ZERO;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public BigInteger bigIntegerValue() {
/*  538 */     return BigInteger.ZERO;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String asText(String defaultValue) {
/*  563 */     String str = asText();
/*  564 */     return (str == null) ? defaultValue : str;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int asInt() {
/*  578 */     return asInt(0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int asInt(int defaultValue) {
/*  592 */     return defaultValue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long asLong() {
/*  606 */     return asLong(0L);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long asLong(long defaultValue) {
/*  620 */     return defaultValue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public double asDouble() {
/*  634 */     return asDouble(0.0D);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public double asDouble(double defaultValue) {
/*  648 */     return defaultValue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean asBoolean() {
/*  662 */     return asBoolean(false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean asBoolean(boolean defaultValue) {
/*  676 */     return defaultValue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T extends JsonNode> T require() {
/*  689 */     return _this();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T extends JsonNode> T requireNonNull() {
/*  696 */     return _this();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JsonNode required(String fieldName) {
/*  703 */     return _reportRequiredViolation("Node of type `%s` has no fields", new Object[] { getClass().getName() });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JsonNode required(int index) {
/*  710 */     return _reportRequiredViolation("Node of type `%s` has no indexed values", new Object[] { getClass().getName() });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JsonNode requiredAt(String pathExpr) {
/*  717 */     return requiredAt(JsonPointer.compile(pathExpr));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final JsonNode requiredAt(JsonPointer pathExpr) {
/*  724 */     JsonPointer currentExpr = pathExpr;
/*  725 */     JsonNode curr = this;
/*      */ 
/*      */     
/*      */     while (true) {
/*  729 */       if (currentExpr.matches()) {
/*  730 */         return curr;
/*      */       }
/*  732 */       curr = curr._at(currentExpr);
/*  733 */       if (curr == null) {
/*  734 */         _reportRequiredViolation("No node at '%s' (unmatched part: '%s')", new Object[] { pathExpr, currentExpr });
/*      */       }
/*      */       
/*  737 */       currentExpr = currentExpr.tail();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean has(String fieldName) {
/*  768 */     return (get(fieldName) != null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean has(int index) {
/*  794 */     return (get(index) != null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasNonNull(String fieldName) {
/*  809 */     JsonNode n = get(fieldName);
/*  810 */     return (n != null && !n.isNull());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasNonNull(int index) {
/*  825 */     JsonNode n = get(index);
/*  826 */     return (n != null && !n.isNull());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Iterator<JsonNode> iterator() {
/*  841 */     return elements();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Iterator<JsonNode> elements() {
/*  850 */     return ClassUtil.emptyIterator();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Iterator<Map.Entry<String, JsonNode>> fields() {
/*  858 */     return ClassUtil.emptyIterator();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final List<JsonNode> findValues(String fieldName) {
/*  889 */     List<JsonNode> result = findValues(fieldName, null);
/*  890 */     if (result == null) {
/*  891 */       return Collections.emptyList();
/*      */     }
/*  893 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final List<String> findValuesAsText(String fieldName) {
/*  902 */     List<String> result = findValuesAsText(fieldName, null);
/*  903 */     if (result == null) {
/*  904 */       return Collections.emptyList();
/*      */     }
/*  906 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final List<JsonNode> findParents(String fieldName) {
/*  945 */     List<JsonNode> result = findParents(fieldName, null);
/*  946 */     if (result == null) {
/*  947 */       return Collections.emptyList();
/*      */     }
/*  949 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T extends JsonNode> T with(String propertyName) {
/*  973 */     throw new UnsupportedOperationException("JsonNode not of type ObjectNode (but " + 
/*  974 */         getClass().getName() + "), cannot call with() on it");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T extends JsonNode> T withArray(String propertyName) {
/*  988 */     throw new UnsupportedOperationException("JsonNode not of type ObjectNode (but " + 
/*  989 */         getClass().getName() + "), cannot call withArray() on it");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean equals(Comparator<JsonNode> comparator, JsonNode other) {
/* 1017 */     return (comparator.compare(this, other) == 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toPrettyString() {
/* 1051 */     return toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected <T extends JsonNode> T _this() {
/* 1075 */     return (T)this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected <T> T _reportRequiredViolation(String msgTemplate, Object... args) {
/* 1084 */     throw new IllegalArgumentException(String.format(msgTemplate, args));
/*      */   }
/*      */   
/*      */   public abstract <T extends JsonNode> T deepCopy();
/*      */   
/*      */   public abstract JsonNode get(int paramInt);
/*      */   
/*      */   public abstract JsonNode path(String paramString);
/*      */   
/*      */   public abstract JsonNode path(int paramInt);
/*      */   
/*      */   protected abstract JsonNode _at(JsonPointer paramJsonPointer);
/*      */   
/*      */   public abstract JsonNodeType getNodeType();
/*      */   
/*      */   public abstract String asText();
/*      */   
/*      */   public abstract JsonNode findValue(String paramString);
/*      */   
/*      */   public abstract JsonNode findPath(String paramString);
/*      */   
/*      */   public abstract JsonNode findParent(String paramString);
/*      */   
/*      */   public abstract List<JsonNode> findValues(String paramString, List<JsonNode> paramList);
/*      */   
/*      */   public abstract List<String> findValuesAsText(String paramString, List<String> paramList);
/*      */   
/*      */   public abstract List<JsonNode> findParents(String paramString, List<JsonNode> paramList);
/*      */   
/*      */   public abstract String toString();
/*      */   
/*      */   public abstract boolean equals(Object paramObject);
/*      */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\JsonNode.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */