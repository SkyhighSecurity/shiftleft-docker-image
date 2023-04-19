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
/*     */ import com.fasterxml.jackson.databind.util.RawValue;
/*     */ import java.io.IOException;
/*     */ import java.io.Serializable;
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ArrayNode
/*     */   extends ContainerNode<ArrayNode>
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private final List<JsonNode> _children;
/*     */   
/*     */   public ArrayNode(JsonNodeFactory nf) {
/*  33 */     super(nf);
/*  34 */     this._children = new ArrayList<>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ArrayNode(JsonNodeFactory nf, int capacity) {
/*  41 */     super(nf);
/*  42 */     this._children = new ArrayList<>(capacity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ArrayNode(JsonNodeFactory nf, List<JsonNode> children) {
/*  49 */     super(nf);
/*  50 */     this._children = children;
/*     */   }
/*     */ 
/*     */   
/*     */   protected JsonNode _at(JsonPointer ptr) {
/*  55 */     return get(ptr.getMatchingIndex());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ArrayNode deepCopy() {
/*  63 */     ArrayNode ret = new ArrayNode(this._nodeFactory);
/*     */     
/*  65 */     for (JsonNode element : this._children) {
/*  66 */       ret._children.add(element.deepCopy());
/*     */     }
/*  68 */     return ret;
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
/*  79 */     return this._children.isEmpty();
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
/*  90 */     return JsonNodeType.ARRAY;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isArray() {
/*  95 */     return true;
/*     */   }
/*     */   public JsonToken asToken() {
/*  98 */     return JsonToken.START_ARRAY;
/*     */   }
/*     */   
/*     */   public int size() {
/* 102 */     return this._children.size();
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/* 106 */     return this._children.isEmpty();
/*     */   }
/*     */   
/*     */   public Iterator<JsonNode> elements() {
/* 110 */     return this._children.iterator();
/*     */   }
/*     */ 
/*     */   
/*     */   public JsonNode get(int index) {
/* 115 */     if (index >= 0 && index < this._children.size()) {
/* 116 */       return this._children.get(index);
/*     */     }
/* 118 */     return null;
/*     */   }
/*     */   
/*     */   public JsonNode get(String fieldName) {
/* 122 */     return null;
/*     */   }
/*     */   public JsonNode path(String fieldName) {
/* 125 */     return MissingNode.getInstance();
/*     */   }
/*     */   
/*     */   public JsonNode path(int index) {
/* 129 */     if (index >= 0 && index < this._children.size()) {
/* 130 */       return this._children.get(index);
/*     */     }
/* 132 */     return MissingNode.getInstance();
/*     */   }
/*     */ 
/*     */   
/*     */   public JsonNode required(int index) {
/* 137 */     if (index >= 0 && index < this._children.size()) {
/* 138 */       return this._children.get(index);
/*     */     }
/* 140 */     return (JsonNode)_reportRequiredViolation("No value at index #%d [0, %d) of `ArrayNode`", new Object[] {
/* 141 */           Integer.valueOf(index), Integer.valueOf(this._children.size())
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Comparator<JsonNode> comparator, JsonNode o) {
/* 147 */     if (!(o instanceof ArrayNode)) {
/* 148 */       return false;
/*     */     }
/* 150 */     ArrayNode other = (ArrayNode)o;
/* 151 */     int len = this._children.size();
/* 152 */     if (other.size() != len) {
/* 153 */       return false;
/*     */     }
/* 155 */     List<JsonNode> l1 = this._children;
/* 156 */     List<JsonNode> l2 = other._children;
/* 157 */     for (int i = 0; i < len; i++) {
/* 158 */       if (!((JsonNode)l1.get(i)).equals(comparator, l2.get(i))) {
/* 159 */         return false;
/*     */       }
/*     */     } 
/* 162 */     return true;
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
/*     */   public void serialize(JsonGenerator f, SerializerProvider provider) throws IOException {
/* 174 */     List<JsonNode> c = this._children;
/* 175 */     int size = c.size();
/* 176 */     f.writeStartArray(size);
/* 177 */     for (int i = 0; i < size; i++) {
/*     */       
/* 179 */       JsonNode n = c.get(i);
/* 180 */       ((BaseJsonNode)n).serialize(f, provider);
/*     */     } 
/* 182 */     f.writeEndArray();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void serializeWithType(JsonGenerator g, SerializerProvider provider, TypeSerializer typeSer) throws IOException {
/* 189 */     WritableTypeId typeIdDef = typeSer.writeTypePrefix(g, typeSer
/* 190 */         .typeId(this, JsonToken.START_ARRAY));
/* 191 */     for (JsonNode n : this._children) {
/* 192 */       ((BaseJsonNode)n).serialize(g, provider);
/*     */     }
/* 194 */     typeSer.writeTypeSuffix(g, typeIdDef);
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
/* 206 */     for (JsonNode node : this._children) {
/* 207 */       JsonNode value = node.findValue(fieldName);
/* 208 */       if (value != null) {
/* 209 */         return value;
/*     */       }
/*     */     } 
/* 212 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<JsonNode> findValues(String fieldName, List<JsonNode> foundSoFar) {
/* 218 */     for (JsonNode node : this._children) {
/* 219 */       foundSoFar = node.findValues(fieldName, foundSoFar);
/*     */     }
/* 221 */     return foundSoFar;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<String> findValuesAsText(String fieldName, List<String> foundSoFar) {
/* 227 */     for (JsonNode node : this._children) {
/* 228 */       foundSoFar = node.findValuesAsText(fieldName, foundSoFar);
/*     */     }
/* 230 */     return foundSoFar;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjectNode findParent(String fieldName) {
/* 236 */     for (JsonNode node : this._children) {
/* 237 */       JsonNode parent = node.findParent(fieldName);
/* 238 */       if (parent != null) {
/* 239 */         return (ObjectNode)parent;
/*     */       }
/*     */     } 
/* 242 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<JsonNode> findParents(String fieldName, List<JsonNode> foundSoFar) {
/* 248 */     for (JsonNode node : this._children) {
/* 249 */       foundSoFar = node.findParents(fieldName, foundSoFar);
/*     */     }
/* 251 */     return foundSoFar;
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
/*     */   public JsonNode set(int index, JsonNode value) {
/* 273 */     if (value == null) {
/* 274 */       value = nullNode();
/*     */     }
/* 276 */     if (index < 0 || index >= this._children.size()) {
/* 277 */       throw new IndexOutOfBoundsException("Illegal index " + index + ", array size " + size());
/*     */     }
/* 279 */     return this._children.set(index, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ArrayNode add(JsonNode value) {
/* 289 */     if (value == null) {
/* 290 */       value = nullNode();
/*     */     }
/* 292 */     _add(value);
/* 293 */     return this;
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
/*     */   public ArrayNode addAll(ArrayNode other) {
/* 306 */     this._children.addAll(other._children);
/* 307 */     return this;
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
/*     */   public ArrayNode addAll(Collection<? extends JsonNode> nodes) {
/* 319 */     for (JsonNode node : nodes) {
/* 320 */       add(node);
/*     */     }
/* 322 */     return this;
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
/*     */   public ArrayNode insert(int index, JsonNode value) {
/* 336 */     if (value == null) {
/* 337 */       value = nullNode();
/*     */     }
/* 339 */     _insert(index, value);
/* 340 */     return this;
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
/*     */   public JsonNode remove(int index) {
/* 352 */     if (index >= 0 && index < this._children.size()) {
/* 353 */       return this._children.remove(index);
/*     */     }
/* 355 */     return null;
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
/*     */   public ArrayNode removeAll() {
/* 367 */     this._children.clear();
/* 368 */     return this;
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
/*     */   public ArrayNode addArray() {
/* 385 */     ArrayNode n = arrayNode();
/* 386 */     _add(n);
/* 387 */     return n;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjectNode addObject() {
/* 398 */     ObjectNode n = objectNode();
/* 399 */     _add(n);
/* 400 */     return n;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ArrayNode addPOJO(Object value) {
/* 411 */     if (value == null) {
/* 412 */       addNull();
/*     */     } else {
/* 414 */       _add(pojoNode(value));
/*     */     } 
/* 416 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ArrayNode addRawValue(RawValue raw) {
/* 425 */     if (raw == null) {
/* 426 */       addNull();
/*     */     } else {
/* 428 */       _add(rawValueNode(raw));
/*     */     } 
/* 430 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ArrayNode addNull() {
/* 440 */     _add(nullNode());
/* 441 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ArrayNode add(int v) {
/* 450 */     _add(numberNode(v));
/* 451 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ArrayNode add(Integer value) {
/* 461 */     if (value == null) {
/* 462 */       return addNull();
/*     */     }
/* 464 */     return _add(numberNode(value.intValue()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ArrayNode add(long v) {
/* 472 */     return _add(numberNode(v));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ArrayNode add(Long value) {
/* 481 */     if (value == null) {
/* 482 */       return addNull();
/*     */     }
/* 484 */     return _add(numberNode(value.longValue()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ArrayNode add(float v) {
/* 493 */     return _add(numberNode(v));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ArrayNode add(Float value) {
/* 503 */     if (value == null) {
/* 504 */       return addNull();
/*     */     }
/* 506 */     return _add(numberNode(value.floatValue()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ArrayNode add(double v) {
/* 515 */     return _add(numberNode(v));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ArrayNode add(Double value) {
/* 525 */     if (value == null) {
/* 526 */       return addNull();
/*     */     }
/* 528 */     return _add(numberNode(value.doubleValue()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ArrayNode add(BigDecimal v) {
/* 537 */     if (v == null) {
/* 538 */       return addNull();
/*     */     }
/* 540 */     return _add(numberNode(v));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ArrayNode add(BigInteger v) {
/* 551 */     if (v == null) {
/* 552 */       return addNull();
/*     */     }
/* 554 */     return _add(numberNode(v));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ArrayNode add(String v) {
/* 563 */     if (v == null) {
/* 564 */       return addNull();
/*     */     }
/* 566 */     return _add(textNode(v));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ArrayNode add(boolean v) {
/* 575 */     return _add(booleanNode(v));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ArrayNode add(Boolean value) {
/* 585 */     if (value == null) {
/* 586 */       return addNull();
/*     */     }
/* 588 */     return _add(booleanNode(value.booleanValue()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ArrayNode add(byte[] v) {
/* 598 */     if (v == null) {
/* 599 */       return addNull();
/*     */     }
/* 601 */     return _add(binaryNode(v));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ArrayNode insertArray(int index) {
/* 612 */     ArrayNode n = arrayNode();
/* 613 */     _insert(index, n);
/* 614 */     return n;
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
/*     */   public ObjectNode insertObject(int index) {
/* 626 */     ObjectNode n = objectNode();
/* 627 */     _insert(index, n);
/* 628 */     return n;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ArrayNode insertPOJO(int index, Object value) {
/* 639 */     if (value == null) {
/* 640 */       return insertNull(index);
/*     */     }
/* 642 */     return _insert(index, pojoNode(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ArrayNode insertNull(int index) {
/* 653 */     _insert(index, nullNode());
/* 654 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ArrayNode insert(int index, int v) {
/* 664 */     _insert(index, numberNode(v));
/* 665 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ArrayNode insert(int index, Integer value) {
/* 675 */     if (value == null) {
/* 676 */       insertNull(index);
/*     */     } else {
/* 678 */       _insert(index, numberNode(value.intValue()));
/*     */     } 
/* 680 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ArrayNode insert(int index, long v) {
/* 690 */     return _insert(index, numberNode(v));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ArrayNode insert(int index, Long value) {
/* 700 */     if (value == null) {
/* 701 */       return insertNull(index);
/*     */     }
/* 703 */     return _insert(index, numberNode(value.longValue()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ArrayNode insert(int index, float v) {
/* 713 */     return _insert(index, numberNode(v));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ArrayNode insert(int index, Float value) {
/* 723 */     if (value == null) {
/* 724 */       return insertNull(index);
/*     */     }
/* 726 */     return _insert(index, numberNode(value.floatValue()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ArrayNode insert(int index, double v) {
/* 736 */     return _insert(index, numberNode(v));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ArrayNode insert(int index, Double value) {
/* 746 */     if (value == null) {
/* 747 */       return insertNull(index);
/*     */     }
/* 749 */     return _insert(index, numberNode(value.doubleValue()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ArrayNode insert(int index, BigDecimal v) {
/* 759 */     if (v == null) {
/* 760 */       return insertNull(index);
/*     */     }
/* 762 */     return _insert(index, numberNode(v));
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
/*     */   public ArrayNode insert(int index, BigInteger v) {
/* 774 */     if (v == null) {
/* 775 */       return insertNull(index);
/*     */     }
/* 777 */     return _insert(index, numberNode(v));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ArrayNode insert(int index, String v) {
/* 787 */     if (v == null) {
/* 788 */       return insertNull(index);
/*     */     }
/* 790 */     return _insert(index, textNode(v));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ArrayNode insert(int index, boolean v) {
/* 800 */     return _insert(index, booleanNode(v));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ArrayNode insert(int index, Boolean value) {
/* 810 */     if (value == null) {
/* 811 */       return insertNull(index);
/*     */     }
/* 813 */     return _insert(index, booleanNode(value.booleanValue()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ArrayNode insert(int index, byte[] v) {
/* 824 */     if (v == null) {
/* 825 */       return insertNull(index);
/*     */     }
/* 827 */     return _insert(index, binaryNode(v));
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
/* 839 */     if (o == this) return true; 
/* 840 */     if (o == null) return false; 
/* 841 */     if (o instanceof ArrayNode) {
/* 842 */       return this._children.equals(((ArrayNode)o)._children);
/*     */     }
/* 844 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean _childrenEqual(ArrayNode other) {
/* 851 */     return this._children.equals(other._children);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 856 */     return this._children.hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ArrayNode _add(JsonNode node) {
/* 866 */     this._children.add(node);
/* 867 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   protected ArrayNode _insert(int index, JsonNode node) {
/* 872 */     if (index < 0) {
/* 873 */       this._children.add(0, node);
/* 874 */     } else if (index >= this._children.size()) {
/* 875 */       this._children.add(node);
/*     */     } else {
/* 877 */       this._children.add(index, node);
/*     */     } 
/* 879 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\node\ArrayNode.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */