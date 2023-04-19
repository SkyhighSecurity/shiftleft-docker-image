/*     */ package com.fasterxml.jackson.databind.node;
/*     */ 
/*     */ import com.fasterxml.jackson.core.Base64Variant;
/*     */ import com.fasterxml.jackson.core.JsonLocation;
/*     */ import com.fasterxml.jackson.core.JsonParseException;
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonStreamContext;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.core.ObjectCodec;
/*     */ import com.fasterxml.jackson.core.Version;
/*     */ import com.fasterxml.jackson.core.base.ParserMinimalBase;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.cfg.PackageVersion;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TreeTraversingParser
/*     */   extends ParserMinimalBase
/*     */ {
/*     */   protected ObjectCodec _objectCodec;
/*     */   protected NodeCursor _nodeCursor;
/*     */   protected JsonToken _nextToken;
/*     */   protected boolean _startContainer;
/*     */   protected boolean _closed;
/*     */   
/*     */   public TreeTraversingParser(JsonNode n) {
/*  65 */     this(n, (ObjectCodec)null);
/*     */   }
/*     */   
/*     */   public TreeTraversingParser(JsonNode n, ObjectCodec codec) {
/*  69 */     super(0);
/*  70 */     this._objectCodec = codec;
/*  71 */     if (n.isArray()) {
/*  72 */       this._nextToken = JsonToken.START_ARRAY;
/*  73 */       this._nodeCursor = new NodeCursor.ArrayCursor(n, null);
/*  74 */     } else if (n.isObject()) {
/*  75 */       this._nextToken = JsonToken.START_OBJECT;
/*  76 */       this._nodeCursor = new NodeCursor.ObjectCursor(n, null);
/*     */     } else {
/*  78 */       this._nodeCursor = new NodeCursor.RootCursor(n, null);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCodec(ObjectCodec c) {
/*  84 */     this._objectCodec = c;
/*     */   }
/*     */ 
/*     */   
/*     */   public ObjectCodec getCodec() {
/*  89 */     return this._objectCodec;
/*     */   }
/*     */ 
/*     */   
/*     */   public Version version() {
/*  94 */     return PackageVersion.VERSION;
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
/*     */   public void close() throws IOException {
/* 106 */     if (!this._closed) {
/* 107 */       this._closed = true;
/* 108 */       this._nodeCursor = null;
/* 109 */       this._currToken = null;
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
/*     */   public JsonToken nextToken() throws IOException, JsonParseException {
/* 122 */     if (this._nextToken != null) {
/* 123 */       this._currToken = this._nextToken;
/* 124 */       this._nextToken = null;
/* 125 */       return this._currToken;
/*     */     } 
/*     */     
/* 128 */     if (this._startContainer) {
/* 129 */       this._startContainer = false;
/*     */       
/* 131 */       if (!this._nodeCursor.currentHasChildren()) {
/* 132 */         this._currToken = (this._currToken == JsonToken.START_OBJECT) ? JsonToken.END_OBJECT : JsonToken.END_ARRAY;
/*     */         
/* 134 */         return this._currToken;
/*     */       } 
/* 136 */       this._nodeCursor = this._nodeCursor.iterateChildren();
/* 137 */       this._currToken = this._nodeCursor.nextToken();
/* 138 */       if (this._currToken == JsonToken.START_OBJECT || this._currToken == JsonToken.START_ARRAY) {
/* 139 */         this._startContainer = true;
/*     */       }
/* 141 */       return this._currToken;
/*     */     } 
/*     */     
/* 144 */     if (this._nodeCursor == null) {
/* 145 */       this._closed = true;
/* 146 */       return null;
/*     */     } 
/*     */     
/* 149 */     this._currToken = this._nodeCursor.nextToken();
/* 150 */     if (this._currToken != null) {
/* 151 */       if (this._currToken == JsonToken.START_OBJECT || this._currToken == JsonToken.START_ARRAY) {
/* 152 */         this._startContainer = true;
/*     */       }
/* 154 */       return this._currToken;
/*     */     } 
/*     */     
/* 157 */     this._currToken = this._nodeCursor.endToken();
/* 158 */     this._nodeCursor = this._nodeCursor.getParent();
/* 159 */     return this._currToken;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonParser skipChildren() throws IOException, JsonParseException {
/* 168 */     if (this._currToken == JsonToken.START_OBJECT) {
/* 169 */       this._startContainer = false;
/* 170 */       this._currToken = JsonToken.END_OBJECT;
/* 171 */     } else if (this._currToken == JsonToken.START_ARRAY) {
/* 172 */       this._startContainer = false;
/* 173 */       this._currToken = JsonToken.END_ARRAY;
/*     */     } 
/* 175 */     return (JsonParser)this;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isClosed() {
/* 180 */     return this._closed;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCurrentName() {
/* 191 */     return (this._nodeCursor == null) ? null : this._nodeCursor.getCurrentName();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void overrideCurrentName(String name) {
/* 197 */     if (this._nodeCursor != null) {
/* 198 */       this._nodeCursor.overrideCurrentName(name);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public JsonStreamContext getParsingContext() {
/* 204 */     return this._nodeCursor;
/*     */   }
/*     */ 
/*     */   
/*     */   public JsonLocation getTokenLocation() {
/* 209 */     return JsonLocation.NA;
/*     */   }
/*     */ 
/*     */   
/*     */   public JsonLocation getCurrentLocation() {
/* 214 */     return JsonLocation.NA;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getText() {
/*     */     JsonNode n;
/* 226 */     if (this._closed) {
/* 227 */       return null;
/*     */     }
/*     */     
/* 230 */     switch (this._currToken) {
/*     */       case FIELD_NAME:
/* 232 */         return this._nodeCursor.getCurrentName();
/*     */       case VALUE_STRING:
/* 234 */         return currentNode().textValue();
/*     */       case VALUE_NUMBER_INT:
/*     */       case VALUE_NUMBER_FLOAT:
/* 237 */         return String.valueOf(currentNode().numberValue());
/*     */       case VALUE_EMBEDDED_OBJECT:
/* 239 */         n = currentNode();
/* 240 */         if (n != null && n.isBinary())
/*     */         {
/* 242 */           return n.asText(); } 
/*     */         break;
/*     */     } 
/* 245 */     return (this._currToken == null) ? null : this._currToken.asString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public char[] getTextCharacters() throws IOException, JsonParseException {
/* 251 */     return getText().toCharArray();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getTextLength() throws IOException, JsonParseException {
/* 256 */     return getText().length();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getTextOffset() throws IOException, JsonParseException {
/* 261 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasTextCharacters() {
/* 267 */     return false;
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
/*     */   public JsonParser.NumberType getNumberType() throws IOException {
/* 280 */     JsonNode n = currentNumericNode();
/* 281 */     return (n == null) ? null : n.numberType();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BigInteger getBigIntegerValue() throws IOException {
/* 287 */     return currentNumericNode().bigIntegerValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public BigDecimal getDecimalValue() throws IOException {
/* 292 */     return currentNumericNode().decimalValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public double getDoubleValue() throws IOException {
/* 297 */     return currentNumericNode().doubleValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public float getFloatValue() throws IOException {
/* 302 */     return (float)currentNumericNode().doubleValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getIntValue() throws IOException {
/* 307 */     NumericNode node = (NumericNode)currentNumericNode();
/* 308 */     if (!node.canConvertToInt()) {
/* 309 */       reportOverflowInt();
/*     */     }
/* 311 */     return node.intValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public long getLongValue() throws IOException {
/* 316 */     NumericNode node = (NumericNode)currentNumericNode();
/* 317 */     if (!node.canConvertToLong()) {
/* 318 */       reportOverflowLong();
/*     */     }
/* 320 */     return node.longValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public Number getNumberValue() throws IOException {
/* 325 */     return currentNumericNode().numberValue();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getEmbeddedObject() {
/* 331 */     if (!this._closed) {
/* 332 */       JsonNode n = currentNode();
/* 333 */       if (n != null) {
/* 334 */         if (n.isPojo()) {
/* 335 */           return ((POJONode)n).getPojo();
/*     */         }
/* 337 */         if (n.isBinary()) {
/* 338 */           return ((BinaryNode)n).binaryValue();
/*     */         }
/*     */       } 
/*     */     } 
/* 342 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isNaN() {
/* 347 */     if (!this._closed) {
/* 348 */       JsonNode n = currentNode();
/* 349 */       if (n instanceof NumericNode) {
/* 350 */         return ((NumericNode)n).isNaN();
/*     */       }
/*     */     } 
/* 353 */     return false;
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
/*     */   public byte[] getBinaryValue(Base64Variant b64variant) throws IOException, JsonParseException {
/* 367 */     JsonNode n = currentNode();
/* 368 */     if (n != null) {
/*     */ 
/*     */       
/* 371 */       if (n instanceof TextNode) {
/* 372 */         return ((TextNode)n).getBinaryValue(b64variant);
/*     */       }
/* 374 */       return n.binaryValue();
/*     */     } 
/*     */     
/* 377 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int readBinaryValue(Base64Variant b64variant, OutputStream out) throws IOException, JsonParseException {
/* 385 */     byte[] data = getBinaryValue(b64variant);
/* 386 */     if (data != null) {
/* 387 */       out.write(data, 0, data.length);
/* 388 */       return data.length;
/*     */     } 
/* 390 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected JsonNode currentNode() {
/* 400 */     if (this._closed || this._nodeCursor == null) {
/* 401 */       return null;
/*     */     }
/* 403 */     return this._nodeCursor.currentNode();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected JsonNode currentNumericNode() throws JsonParseException {
/* 409 */     JsonNode n = currentNode();
/* 410 */     if (n == null || !n.isNumber()) {
/* 411 */       JsonToken t = (n == null) ? null : n.asToken();
/* 412 */       throw _constructError("Current token (" + t + ") not numeric, cannot use numeric value accessors");
/*     */     } 
/* 414 */     return n;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _handleEOF() throws JsonParseException {
/* 419 */     _throwInternal();
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\node\TreeTraversingParser.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */