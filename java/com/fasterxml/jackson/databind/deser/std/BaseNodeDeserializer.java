/*     */ package com.fasterxml.jackson.databind.deser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonProcessingException;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.databind.DeserializationConfig;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.DeserializationFeature;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*     */ import com.fasterxml.jackson.databind.node.ArrayNode;
/*     */ import com.fasterxml.jackson.databind.node.BooleanNode;
/*     */ import com.fasterxml.jackson.databind.node.JsonNodeFactory;
/*     */ import com.fasterxml.jackson.databind.node.NullNode;
/*     */ import com.fasterxml.jackson.databind.node.ObjectNode;
/*     */ import com.fasterxml.jackson.databind.node.TextNode;
/*     */ import com.fasterxml.jackson.databind.util.RawValue;
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
/*     */ 
/*     */ abstract class BaseNodeDeserializer<T extends JsonNode>
/*     */   extends StdDeserializer<T>
/*     */ {
/*     */   protected final Boolean _supportsUpdates;
/*     */   
/*     */   public BaseNodeDeserializer(Class<T> vc, Boolean supportsUpdates) {
/* 174 */     super(vc);
/* 175 */     this._supportsUpdates = supportsUpdates;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException {
/* 184 */     return typeDeserializer.deserializeTypedFromAny(p, ctxt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isCachable() {
/* 192 */     return true;
/*     */   }
/*     */   
/*     */   public Boolean supportsUpdate(DeserializationConfig config) {
/* 196 */     return this._supportsUpdates;
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
/*     */   protected void _handleDuplicateField(JsonParser p, DeserializationContext ctxt, JsonNodeFactory nodeFactory, String fieldName, ObjectNode objectNode, JsonNode oldValue, JsonNode newValue) throws JsonProcessingException {
/* 225 */     if (ctxt.isEnabled(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY))
/*     */     {
/*     */ 
/*     */       
/* 229 */       ctxt.reportInputMismatch(JsonNode.class, "Duplicate field '%s' for `ObjectNode`: not allowed when `DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY` enabled", new Object[] { fieldName });
/*     */     }
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
/*     */   protected final ObjectNode deserializeObject(JsonParser p, DeserializationContext ctxt, JsonNodeFactory nodeFactory) throws IOException {
/* 248 */     ObjectNode node = nodeFactory.objectNode();
/* 249 */     String key = p.nextFieldName();
/* 250 */     for (; key != null; key = p.nextFieldName()) {
/*     */       ObjectNode objectNode; ArrayNode arrayNode; JsonNode jsonNode2; TextNode textNode; JsonNode jsonNode1; BooleanNode booleanNode2, booleanNode1; NullNode nullNode; JsonNode value;
/* 252 */       JsonToken t = p.nextToken();
/* 253 */       if (t == null) {
/* 254 */         t = JsonToken.NOT_AVAILABLE;
/*     */       }
/* 256 */       switch (t.id()) {
/*     */         case 1:
/* 258 */           objectNode = deserializeObject(p, ctxt, nodeFactory);
/*     */           break;
/*     */         case 3:
/* 261 */           arrayNode = deserializeArray(p, ctxt, nodeFactory);
/*     */           break;
/*     */         case 12:
/* 264 */           jsonNode2 = _fromEmbedded(p, ctxt, nodeFactory);
/*     */           break;
/*     */         case 6:
/* 267 */           textNode = nodeFactory.textNode(p.getText());
/*     */           break;
/*     */         case 7:
/* 270 */           jsonNode1 = _fromInt(p, ctxt, nodeFactory);
/*     */           break;
/*     */         case 9:
/* 273 */           booleanNode2 = nodeFactory.booleanNode(true);
/*     */           break;
/*     */         case 10:
/* 276 */           booleanNode1 = nodeFactory.booleanNode(false);
/*     */           break;
/*     */         case 11:
/* 279 */           nullNode = nodeFactory.nullNode();
/*     */           break;
/*     */         default:
/* 282 */           value = deserializeAny(p, ctxt, nodeFactory); break;
/*     */       } 
/* 284 */       JsonNode old = node.replace(key, value);
/* 285 */       if (old != null) {
/* 286 */         _handleDuplicateField(p, ctxt, nodeFactory, key, node, old, value);
/*     */       }
/*     */     } 
/*     */     
/* 290 */     return node;
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
/*     */   protected final ObjectNode deserializeObjectAtName(JsonParser p, DeserializationContext ctxt, JsonNodeFactory nodeFactory) throws IOException {
/* 302 */     ObjectNode node = nodeFactory.objectNode();
/* 303 */     String key = p.getCurrentName();
/* 304 */     for (; key != null; key = p.nextFieldName()) {
/*     */       ObjectNode objectNode; ArrayNode arrayNode; JsonNode jsonNode2; TextNode textNode; JsonNode jsonNode1; BooleanNode booleanNode2, booleanNode1; NullNode nullNode; JsonNode value;
/* 306 */       JsonToken t = p.nextToken();
/* 307 */       if (t == null) {
/* 308 */         t = JsonToken.NOT_AVAILABLE;
/*     */       }
/* 310 */       switch (t.id()) {
/*     */         case 1:
/* 312 */           objectNode = deserializeObject(p, ctxt, nodeFactory);
/*     */           break;
/*     */         case 3:
/* 315 */           arrayNode = deserializeArray(p, ctxt, nodeFactory);
/*     */           break;
/*     */         case 12:
/* 318 */           jsonNode2 = _fromEmbedded(p, ctxt, nodeFactory);
/*     */           break;
/*     */         case 6:
/* 321 */           textNode = nodeFactory.textNode(p.getText());
/*     */           break;
/*     */         case 7:
/* 324 */           jsonNode1 = _fromInt(p, ctxt, nodeFactory);
/*     */           break;
/*     */         case 9:
/* 327 */           booleanNode2 = nodeFactory.booleanNode(true);
/*     */           break;
/*     */         case 10:
/* 330 */           booleanNode1 = nodeFactory.booleanNode(false);
/*     */           break;
/*     */         case 11:
/* 333 */           nullNode = nodeFactory.nullNode();
/*     */           break;
/*     */         default:
/* 336 */           value = deserializeAny(p, ctxt, nodeFactory); break;
/*     */       } 
/* 338 */       JsonNode old = node.replace(key, value);
/* 339 */       if (old != null) {
/* 340 */         _handleDuplicateField(p, ctxt, nodeFactory, key, node, old, value);
/*     */       }
/*     */     } 
/*     */     
/* 344 */     return node;
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
/*     */   protected final JsonNode updateObject(JsonParser p, DeserializationContext ctxt, ObjectNode node) throws IOException {
/* 357 */     if (p.isExpectedStartObjectToken()) {
/* 358 */       str = p.nextFieldName();
/*     */     } else {
/* 360 */       if (!p.hasToken(JsonToken.FIELD_NAME)) {
/* 361 */         return (JsonNode)deserialize(p, ctxt);
/*     */       }
/* 363 */       str = p.getCurrentName();
/*     */     }  String str;
/* 365 */     for (; str != null; str = p.nextFieldName()) {
/*     */       ObjectNode objectNode; ArrayNode arrayNode; JsonNode jsonNode2; TextNode textNode; JsonNode jsonNode1; BooleanNode booleanNode2, booleanNode1; NullNode nullNode; JsonNode value;
/* 367 */       JsonToken t = p.nextToken();
/*     */ 
/*     */       
/* 370 */       JsonNode old = node.get(str);
/* 371 */       if (old != null) {
/* 372 */         if (old instanceof ObjectNode) {
/* 373 */           JsonNode newValue = updateObject(p, ctxt, (ObjectNode)old);
/* 374 */           if (newValue != old) {
/* 375 */             node.set(str, newValue);
/*     */           }
/*     */           continue;
/*     */         } 
/* 379 */         if (old instanceof ArrayNode) {
/* 380 */           JsonNode newValue = updateArray(p, ctxt, (ArrayNode)old);
/* 381 */           if (newValue != old) {
/* 382 */             node.set(str, newValue);
/*     */           }
/*     */           continue;
/*     */         } 
/*     */       } 
/* 387 */       if (t == null) {
/* 388 */         t = JsonToken.NOT_AVAILABLE;
/*     */       }
/*     */       
/* 391 */       JsonNodeFactory nodeFactory = ctxt.getNodeFactory();
/* 392 */       switch (t.id()) {
/*     */         case 1:
/* 394 */           objectNode = deserializeObject(p, ctxt, nodeFactory);
/*     */           break;
/*     */         case 3:
/* 397 */           arrayNode = deserializeArray(p, ctxt, nodeFactory);
/*     */           break;
/*     */         case 12:
/* 400 */           jsonNode2 = _fromEmbedded(p, ctxt, nodeFactory);
/*     */           break;
/*     */         case 6:
/* 403 */           textNode = nodeFactory.textNode(p.getText());
/*     */           break;
/*     */         case 7:
/* 406 */           jsonNode1 = _fromInt(p, ctxt, nodeFactory);
/*     */           break;
/*     */         case 9:
/* 409 */           booleanNode2 = nodeFactory.booleanNode(true);
/*     */           break;
/*     */         case 10:
/* 412 */           booleanNode1 = nodeFactory.booleanNode(false);
/*     */           break;
/*     */         case 11:
/* 415 */           nullNode = nodeFactory.nullNode();
/*     */           break;
/*     */         default:
/* 418 */           value = deserializeAny(p, ctxt, nodeFactory); break;
/*     */       } 
/* 420 */       if (old != null) {
/* 421 */         _handleDuplicateField(p, ctxt, nodeFactory, str, node, old, value);
/*     */       }
/*     */       
/* 424 */       node.set(str, value); continue;
/*     */     } 
/* 426 */     return (JsonNode)node;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected final ArrayNode deserializeArray(JsonParser p, DeserializationContext ctxt, JsonNodeFactory nodeFactory) throws IOException {
/* 432 */     ArrayNode node = nodeFactory.arrayNode();
/*     */     while (true) {
/* 434 */       JsonToken t = p.nextToken();
/* 435 */       switch (t.id()) {
/*     */         case 1:
/* 437 */           node.add((JsonNode)deserializeObject(p, ctxt, nodeFactory));
/*     */           continue;
/*     */         case 3:
/* 440 */           node.add((JsonNode)deserializeArray(p, ctxt, nodeFactory));
/*     */           continue;
/*     */         case 4:
/* 443 */           return node;
/*     */         case 12:
/* 445 */           node.add(_fromEmbedded(p, ctxt, nodeFactory));
/*     */           continue;
/*     */         case 6:
/* 448 */           node.add((JsonNode)nodeFactory.textNode(p.getText()));
/*     */           continue;
/*     */         case 7:
/* 451 */           node.add(_fromInt(p, ctxt, nodeFactory));
/*     */           continue;
/*     */         case 9:
/* 454 */           node.add((JsonNode)nodeFactory.booleanNode(true));
/*     */           continue;
/*     */         case 10:
/* 457 */           node.add((JsonNode)nodeFactory.booleanNode(false));
/*     */           continue;
/*     */         case 11:
/* 460 */           node.add((JsonNode)nodeFactory.nullNode());
/*     */           continue;
/*     */       } 
/* 463 */       node.add(deserializeAny(p, ctxt, nodeFactory));
/*     */     } 
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
/*     */   protected final JsonNode updateArray(JsonParser p, DeserializationContext ctxt, ArrayNode node) throws IOException {
/* 478 */     JsonNodeFactory nodeFactory = ctxt.getNodeFactory();
/*     */     while (true) {
/* 480 */       JsonToken t = p.nextToken();
/* 481 */       switch (t.id()) {
/*     */         case 1:
/* 483 */           node.add((JsonNode)deserializeObject(p, ctxt, nodeFactory));
/*     */           continue;
/*     */         case 3:
/* 486 */           node.add((JsonNode)deserializeArray(p, ctxt, nodeFactory));
/*     */           continue;
/*     */         case 4:
/* 489 */           return (JsonNode)node;
/*     */         case 12:
/* 491 */           node.add(_fromEmbedded(p, ctxt, nodeFactory));
/*     */           continue;
/*     */         case 6:
/* 494 */           node.add((JsonNode)nodeFactory.textNode(p.getText()));
/*     */           continue;
/*     */         case 7:
/* 497 */           node.add(_fromInt(p, ctxt, nodeFactory));
/*     */           continue;
/*     */         case 9:
/* 500 */           node.add((JsonNode)nodeFactory.booleanNode(true));
/*     */           continue;
/*     */         case 10:
/* 503 */           node.add((JsonNode)nodeFactory.booleanNode(false));
/*     */           continue;
/*     */         case 11:
/* 506 */           node.add((JsonNode)nodeFactory.nullNode());
/*     */           continue;
/*     */       } 
/* 509 */       node.add(deserializeAny(p, ctxt, nodeFactory));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final JsonNode deserializeAny(JsonParser p, DeserializationContext ctxt, JsonNodeFactory nodeFactory) throws IOException {
/* 518 */     switch (p.getCurrentTokenId()) {
/*     */       case 2:
/* 520 */         return (JsonNode)nodeFactory.objectNode();
/*     */       case 5:
/* 522 */         return (JsonNode)deserializeObjectAtName(p, ctxt, nodeFactory);
/*     */       case 12:
/* 524 */         return _fromEmbedded(p, ctxt, nodeFactory);
/*     */       case 6:
/* 526 */         return (JsonNode)nodeFactory.textNode(p.getText());
/*     */       case 7:
/* 528 */         return _fromInt(p, ctxt, nodeFactory);
/*     */       case 8:
/* 530 */         return _fromFloat(p, ctxt, nodeFactory);
/*     */       case 9:
/* 532 */         return (JsonNode)nodeFactory.booleanNode(true);
/*     */       case 10:
/* 534 */         return (JsonNode)nodeFactory.booleanNode(false);
/*     */       case 11:
/* 536 */         return (JsonNode)nodeFactory.nullNode();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 553 */     return (JsonNode)ctxt.handleUnexpectedToken(handledType(), p);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected final JsonNode _fromInt(JsonParser p, DeserializationContext ctxt, JsonNodeFactory nodeFactory) throws IOException {
/*     */     JsonParser.NumberType nt;
/* 560 */     int feats = ctxt.getDeserializationFeatures();
/* 561 */     if ((feats & F_MASK_INT_COERCIONS) != 0) {
/* 562 */       if (DeserializationFeature.USE_BIG_INTEGER_FOR_INTS.enabledIn(feats)) {
/* 563 */         nt = JsonParser.NumberType.BIG_INTEGER;
/* 564 */       } else if (DeserializationFeature.USE_LONG_FOR_INTS.enabledIn(feats)) {
/* 565 */         nt = JsonParser.NumberType.LONG;
/*     */       } else {
/* 567 */         nt = p.getNumberType();
/*     */       } 
/*     */     } else {
/* 570 */       nt = p.getNumberType();
/*     */     } 
/* 572 */     if (nt == JsonParser.NumberType.INT) {
/* 573 */       return (JsonNode)nodeFactory.numberNode(p.getIntValue());
/*     */     }
/* 575 */     if (nt == JsonParser.NumberType.LONG) {
/* 576 */       return (JsonNode)nodeFactory.numberNode(p.getLongValue());
/*     */     }
/* 578 */     return (JsonNode)nodeFactory.numberNode(p.getBigIntegerValue());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected final JsonNode _fromFloat(JsonParser p, DeserializationContext ctxt, JsonNodeFactory nodeFactory) throws IOException {
/* 584 */     JsonParser.NumberType nt = p.getNumberType();
/* 585 */     if (nt == JsonParser.NumberType.BIG_DECIMAL) {
/* 586 */       return (JsonNode)nodeFactory.numberNode(p.getDecimalValue());
/*     */     }
/* 588 */     if (ctxt.isEnabled(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS)) {
/*     */ 
/*     */       
/* 591 */       if (p.isNaN()) {
/* 592 */         return (JsonNode)nodeFactory.numberNode(p.getDoubleValue());
/*     */       }
/* 594 */       return (JsonNode)nodeFactory.numberNode(p.getDecimalValue());
/*     */     } 
/* 596 */     if (nt == JsonParser.NumberType.FLOAT) {
/* 597 */       return (JsonNode)nodeFactory.numberNode(p.getFloatValue());
/*     */     }
/* 599 */     return (JsonNode)nodeFactory.numberNode(p.getDoubleValue());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected final JsonNode _fromEmbedded(JsonParser p, DeserializationContext ctxt, JsonNodeFactory nodeFactory) throws IOException {
/* 605 */     Object ob = p.getEmbeddedObject();
/* 606 */     if (ob == null) {
/* 607 */       return (JsonNode)nodeFactory.nullNode();
/*     */     }
/* 609 */     Class<?> type = ob.getClass();
/* 610 */     if (type == byte[].class) {
/* 611 */       return (JsonNode)nodeFactory.binaryNode((byte[])ob);
/*     */     }
/*     */     
/* 614 */     if (ob instanceof RawValue) {
/* 615 */       return (JsonNode)nodeFactory.rawValueNode((RawValue)ob);
/*     */     }
/* 617 */     if (ob instanceof JsonNode)
/*     */     {
/* 619 */       return (JsonNode)ob;
/*     */     }
/*     */     
/* 622 */     return (JsonNode)nodeFactory.pojoNode(ob);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\deser\std\BaseNodeDeserializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */