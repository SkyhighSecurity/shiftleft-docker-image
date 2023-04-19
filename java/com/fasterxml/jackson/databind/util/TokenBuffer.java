/*      */ package com.fasterxml.jackson.databind.util;
/*      */ 
/*      */ import com.fasterxml.jackson.core.Base64Variant;
/*      */ import com.fasterxml.jackson.core.JsonGenerationException;
/*      */ import com.fasterxml.jackson.core.JsonGenerator;
/*      */ import com.fasterxml.jackson.core.JsonLocation;
/*      */ import com.fasterxml.jackson.core.JsonParseException;
/*      */ import com.fasterxml.jackson.core.JsonParser;
/*      */ import com.fasterxml.jackson.core.JsonStreamContext;
/*      */ import com.fasterxml.jackson.core.JsonToken;
/*      */ import com.fasterxml.jackson.core.ObjectCodec;
/*      */ import com.fasterxml.jackson.core.SerializableString;
/*      */ import com.fasterxml.jackson.core.TreeNode;
/*      */ import com.fasterxml.jackson.core.Version;
/*      */ import com.fasterxml.jackson.core.base.ParserMinimalBase;
/*      */ import com.fasterxml.jackson.core.json.JsonWriteContext;
/*      */ import com.fasterxml.jackson.core.util.ByteArrayBuilder;
/*      */ import com.fasterxml.jackson.databind.DeserializationContext;
/*      */ import com.fasterxml.jackson.databind.DeserializationFeature;
/*      */ import com.fasterxml.jackson.databind.cfg.PackageVersion;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.math.BigDecimal;
/*      */ import java.math.BigInteger;
/*      */ import java.util.TreeMap;
/*      */ 
/*      */ 
/*      */ public class TokenBuffer
/*      */   extends JsonGenerator
/*      */ {
/*   32 */   protected static final int DEFAULT_GENERATOR_FEATURES = JsonGenerator.Feature.collectDefaults();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected ObjectCodec _objectCodec;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected JsonStreamContext _parentContext;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected int _generatorFeatures;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean _closed;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean _hasNativeTypeIds;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean _hasNativeObjectIds;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean _mayHaveNativeIds;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean _forceBigDecimal;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Segment _first;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Segment _last;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected int _appendAt;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Object _typeId;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Object _objectId;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean _hasNativeId = false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected JsonWriteContext _writeContext;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public TokenBuffer(ObjectCodec codec, boolean hasNativeIds) {
/*  151 */     this._objectCodec = codec;
/*  152 */     this._generatorFeatures = DEFAULT_GENERATOR_FEATURES;
/*  153 */     this._writeContext = JsonWriteContext.createRootContext(null);
/*      */     
/*  155 */     this._first = this._last = new Segment();
/*  156 */     this._appendAt = 0;
/*  157 */     this._hasNativeTypeIds = hasNativeIds;
/*  158 */     this._hasNativeObjectIds = hasNativeIds;
/*      */     
/*  160 */     this._mayHaveNativeIds = this._hasNativeTypeIds | this._hasNativeObjectIds;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public TokenBuffer(JsonParser p) {
/*  167 */     this(p, (DeserializationContext)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public TokenBuffer(JsonParser p, DeserializationContext ctxt) {
/*  175 */     this._objectCodec = p.getCodec();
/*  176 */     this._parentContext = p.getParsingContext();
/*  177 */     this._generatorFeatures = DEFAULT_GENERATOR_FEATURES;
/*  178 */     this._writeContext = JsonWriteContext.createRootContext(null);
/*      */     
/*  180 */     this._first = this._last = new Segment();
/*  181 */     this._appendAt = 0;
/*  182 */     this._hasNativeTypeIds = p.canReadTypeId();
/*  183 */     this._hasNativeObjectIds = p.canReadObjectId();
/*  184 */     this._mayHaveNativeIds = this._hasNativeTypeIds | this._hasNativeObjectIds;
/*  185 */     this
/*  186 */       ._forceBigDecimal = (ctxt == null) ? false : ctxt.isEnabled(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
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
/*      */   public static TokenBuffer asCopyOfValue(JsonParser p) throws IOException {
/*  200 */     TokenBuffer b = new TokenBuffer(p);
/*  201 */     b.copyCurrentStructure(p);
/*  202 */     return b;
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
/*      */   public TokenBuffer overrideParentContext(JsonStreamContext ctxt) {
/*  214 */     this._parentContext = ctxt;
/*  215 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public TokenBuffer forceUseOfBigDecimal(boolean b) {
/*  222 */     this._forceBigDecimal = b;
/*  223 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public Version version() {
/*  228 */     return PackageVersion.VERSION;
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
/*      */   public JsonParser asParser() {
/*  242 */     return asParser(this._objectCodec);
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
/*      */   public JsonParser asParserOnFirstToken() throws IOException {
/*  256 */     JsonParser p = asParser(this._objectCodec);
/*  257 */     p.nextToken();
/*  258 */     return p;
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
/*      */   public JsonParser asParser(ObjectCodec codec) {
/*  276 */     return (JsonParser)new Parser(this._first, codec, this._hasNativeTypeIds, this._hasNativeObjectIds, this._parentContext);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JsonParser asParser(JsonParser src) {
/*  285 */     Parser p = new Parser(this._first, src.getCodec(), this._hasNativeTypeIds, this._hasNativeObjectIds, this._parentContext);
/*  286 */     p.setLocation(src.getTokenLocation());
/*  287 */     return (JsonParser)p;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JsonToken firstToken() {
/*  298 */     return this._first.type(0);
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
/*      */   public TokenBuffer append(TokenBuffer other) throws IOException {
/*  318 */     if (!this._hasNativeTypeIds) {
/*  319 */       this._hasNativeTypeIds = other.canWriteTypeId();
/*      */     }
/*  321 */     if (!this._hasNativeObjectIds) {
/*  322 */       this._hasNativeObjectIds = other.canWriteObjectId();
/*      */     }
/*  324 */     this._mayHaveNativeIds = this._hasNativeTypeIds | this._hasNativeObjectIds;
/*      */     
/*  326 */     JsonParser p = other.asParser();
/*  327 */     while (p.nextToken() != null) {
/*  328 */       copyCurrentStructure(p);
/*      */     }
/*  330 */     return this;
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
/*      */   public void serialize(JsonGenerator gen) throws IOException {
/*  345 */     Segment segment = this._first;
/*  346 */     int ptr = -1;
/*      */     
/*  348 */     boolean checkIds = this._mayHaveNativeIds;
/*  349 */     boolean hasIds = (checkIds && segment.hasIds());
/*      */     while (true) {
/*      */       Object ob, n, value;
/*  352 */       if (++ptr >= 16) {
/*  353 */         ptr = 0;
/*  354 */         segment = segment.next();
/*  355 */         if (segment == null)
/*  356 */           break;  hasIds = (checkIds && segment.hasIds());
/*      */       } 
/*  358 */       JsonToken t = segment.type(ptr);
/*  359 */       if (t == null)
/*      */         break; 
/*  361 */       if (hasIds) {
/*  362 */         Object id = segment.findObjectId(ptr);
/*  363 */         if (id != null) {
/*  364 */           gen.writeObjectId(id);
/*      */         }
/*  366 */         id = segment.findTypeId(ptr);
/*  367 */         if (id != null) {
/*  368 */           gen.writeTypeId(id);
/*      */         }
/*      */       } 
/*      */ 
/*      */       
/*  373 */       switch (t) {
/*      */         case INT:
/*  375 */           gen.writeStartObject();
/*      */           continue;
/*      */         case BIG_INTEGER:
/*  378 */           gen.writeEndObject();
/*      */           continue;
/*      */         case BIG_DECIMAL:
/*  381 */           gen.writeStartArray();
/*      */           continue;
/*      */         case FLOAT:
/*  384 */           gen.writeEndArray();
/*      */           continue;
/*      */ 
/*      */         
/*      */         case LONG:
/*  389 */           ob = segment.get(ptr);
/*  390 */           if (ob instanceof SerializableString) {
/*  391 */             gen.writeFieldName((SerializableString)ob); continue;
/*      */           } 
/*  393 */           gen.writeFieldName((String)ob);
/*      */           continue;
/*      */ 
/*      */ 
/*      */         
/*      */         case null:
/*  399 */           ob = segment.get(ptr);
/*  400 */           if (ob instanceof SerializableString) {
/*  401 */             gen.writeString((SerializableString)ob); continue;
/*      */           } 
/*  403 */           gen.writeString((String)ob);
/*      */           continue;
/*      */ 
/*      */ 
/*      */         
/*      */         case null:
/*  409 */           n = segment.get(ptr);
/*  410 */           if (n instanceof Integer) {
/*  411 */             gen.writeNumber(((Integer)n).intValue()); continue;
/*  412 */           }  if (n instanceof BigInteger) {
/*  413 */             gen.writeNumber((BigInteger)n); continue;
/*  414 */           }  if (n instanceof Long) {
/*  415 */             gen.writeNumber(((Long)n).longValue()); continue;
/*  416 */           }  if (n instanceof Short) {
/*  417 */             gen.writeNumber(((Short)n).shortValue()); continue;
/*      */           } 
/*  419 */           gen.writeNumber(((Number)n).intValue());
/*      */           continue;
/*      */ 
/*      */ 
/*      */         
/*      */         case null:
/*  425 */           n = segment.get(ptr);
/*  426 */           if (n instanceof Double) {
/*  427 */             gen.writeNumber(((Double)n).doubleValue()); continue;
/*  428 */           }  if (n instanceof BigDecimal) {
/*  429 */             gen.writeNumber((BigDecimal)n); continue;
/*  430 */           }  if (n instanceof Float) {
/*  431 */             gen.writeNumber(((Float)n).floatValue()); continue;
/*  432 */           }  if (n == null) {
/*  433 */             gen.writeNull(); continue;
/*  434 */           }  if (n instanceof String) {
/*  435 */             gen.writeNumber((String)n); continue;
/*      */           } 
/*  437 */           throw new JsonGenerationException(String.format("Unrecognized value type for VALUE_NUMBER_FLOAT: %s, cannot serialize", new Object[] { n
/*      */                   
/*  439 */                   .getClass().getName() }), gen);
/*      */ 
/*      */ 
/*      */         
/*      */         case null:
/*  444 */           gen.writeBoolean(true);
/*      */           continue;
/*      */         case null:
/*  447 */           gen.writeBoolean(false);
/*      */           continue;
/*      */         case null:
/*  450 */           gen.writeNull();
/*      */           continue;
/*      */         
/*      */         case null:
/*  454 */           value = segment.get(ptr);
/*      */ 
/*      */ 
/*      */           
/*  458 */           if (value instanceof RawValue) {
/*  459 */             ((RawValue)value).serialize(gen); continue;
/*  460 */           }  if (value instanceof com.fasterxml.jackson.databind.JsonSerializable) {
/*  461 */             gen.writeObject(value); continue;
/*      */           } 
/*  463 */           gen.writeEmbeddedObject(value);
/*      */           continue;
/*      */       } 
/*      */ 
/*      */       
/*  468 */       throw new RuntimeException("Internal error: should never end up through this code path");
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
/*      */   public TokenBuffer deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
/*  480 */     if (p.getCurrentTokenId() != JsonToken.FIELD_NAME.id()) {
/*  481 */       copyCurrentStructure(p);
/*  482 */       return this;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  489 */     writeStartObject(); JsonToken t;
/*      */     do {
/*  491 */       copyCurrentStructure(p);
/*  492 */     } while ((t = p.nextToken()) == JsonToken.FIELD_NAME);
/*  493 */     if (t != JsonToken.END_OBJECT) {
/*  494 */       ctxt.reportWrongTokenException(TokenBuffer.class, JsonToken.END_OBJECT, "Expected END_OBJECT after copying contents of a JsonParser into TokenBuffer, got " + t, new Object[0]);
/*      */     }
/*      */ 
/*      */     
/*  498 */     writeEndObject();
/*  499 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString() {
/*  507 */     int MAX_COUNT = 100;
/*      */     
/*  509 */     StringBuilder sb = new StringBuilder();
/*  510 */     sb.append("[TokenBuffer: ");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  517 */     JsonParser jp = asParser();
/*  518 */     int count = 0;
/*  519 */     boolean hasNativeIds = (this._hasNativeTypeIds || this._hasNativeObjectIds);
/*      */ 
/*      */     
/*      */     while (true) {
/*      */       try {
/*  524 */         JsonToken t = jp.nextToken();
/*  525 */         if (t == null)
/*      */           break; 
/*  527 */         if (hasNativeIds) {
/*  528 */           _appendNativeIds(sb);
/*      */         }
/*      */         
/*  531 */         if (count < 100) {
/*  532 */           if (count > 0) {
/*  533 */             sb.append(", ");
/*      */           }
/*  535 */           sb.append(t.toString());
/*  536 */           if (t == JsonToken.FIELD_NAME) {
/*  537 */             sb.append('(');
/*  538 */             sb.append(jp.getCurrentName());
/*  539 */             sb.append(')');
/*      */           } 
/*      */         } 
/*  542 */       } catch (IOException ioe) {
/*  543 */         throw new IllegalStateException(ioe);
/*      */       } 
/*  545 */       count++;
/*      */     } 
/*      */     
/*  548 */     if (count >= 100) {
/*  549 */       sb.append(" ... (truncated ").append(count - 100).append(" entries)");
/*      */     }
/*  551 */     sb.append(']');
/*  552 */     return sb.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private final void _appendNativeIds(StringBuilder sb) {
/*  557 */     Object objectId = this._last.findObjectId(this._appendAt - 1);
/*  558 */     if (objectId != null) {
/*  559 */       sb.append("[objectId=").append(String.valueOf(objectId)).append(']');
/*      */     }
/*  561 */     Object typeId = this._last.findTypeId(this._appendAt - 1);
/*  562 */     if (typeId != null) {
/*  563 */       sb.append("[typeId=").append(String.valueOf(typeId)).append(']');
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
/*      */   public JsonGenerator enable(JsonGenerator.Feature f) {
/*  575 */     this._generatorFeatures |= f.getMask();
/*  576 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public JsonGenerator disable(JsonGenerator.Feature f) {
/*  581 */     this._generatorFeatures &= f.getMask() ^ 0xFFFFFFFF;
/*  582 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isEnabled(JsonGenerator.Feature f) {
/*  589 */     return ((this._generatorFeatures & f.getMask()) != 0);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getFeatureMask() {
/*  594 */     return this._generatorFeatures;
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public JsonGenerator setFeatureMask(int mask) {
/*  600 */     this._generatorFeatures = mask;
/*  601 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public JsonGenerator overrideStdFeatures(int values, int mask) {
/*  606 */     int oldState = getFeatureMask();
/*  607 */     this._generatorFeatures = oldState & (mask ^ 0xFFFFFFFF) | values & mask;
/*  608 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public JsonGenerator useDefaultPrettyPrinter() {
/*  614 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public JsonGenerator setCodec(ObjectCodec oc) {
/*  619 */     this._objectCodec = oc;
/*  620 */     return this;
/*      */   }
/*      */   
/*      */   public ObjectCodec getCodec() {
/*  624 */     return this._objectCodec;
/*      */   }
/*      */   public final JsonWriteContext getOutputContext() {
/*  627 */     return this._writeContext;
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
/*      */   public boolean canWriteBinaryNatively() {
/*  640 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void flush() throws IOException {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void close() throws IOException {
/*  654 */     this._closed = true;
/*      */   }
/*      */   
/*      */   public boolean isClosed() {
/*  658 */     return this._closed;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void writeStartArray() throws IOException {
/*  669 */     this._writeContext.writeValue();
/*  670 */     _appendStartMarker(JsonToken.START_ARRAY);
/*  671 */     this._writeContext = this._writeContext.createChildArrayContext();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void writeStartArray(int size) throws IOException {
/*  677 */     this._writeContext.writeValue();
/*  678 */     _appendStartMarker(JsonToken.START_ARRAY);
/*  679 */     this._writeContext = this._writeContext.createChildArrayContext();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void writeEndArray() throws IOException {
/*  687 */     _appendEndMarker(JsonToken.END_ARRAY);
/*      */     
/*  689 */     JsonWriteContext c = this._writeContext.getParent();
/*  690 */     if (c != null) {
/*  691 */       this._writeContext = c;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void writeStartObject() throws IOException {
/*  698 */     this._writeContext.writeValue();
/*  699 */     _appendStartMarker(JsonToken.START_OBJECT);
/*  700 */     this._writeContext = this._writeContext.createChildObjectContext();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void writeStartObject(Object forValue) throws IOException {
/*  706 */     this._writeContext.writeValue();
/*  707 */     _appendStartMarker(JsonToken.START_OBJECT);
/*      */     
/*  709 */     JsonWriteContext ctxt = this._writeContext.createChildObjectContext();
/*  710 */     this._writeContext = ctxt;
/*  711 */     if (forValue != null) {
/*  712 */       ctxt.setCurrentValue(forValue);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void writeEndObject() throws IOException {
/*  721 */     _appendEndMarker(JsonToken.END_OBJECT);
/*      */     
/*  723 */     JsonWriteContext c = this._writeContext.getParent();
/*  724 */     if (c != null) {
/*  725 */       this._writeContext = c;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void writeFieldName(String name) throws IOException {
/*  732 */     this._writeContext.writeFieldName(name);
/*  733 */     _appendFieldName(name);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void writeFieldName(SerializableString name) throws IOException {
/*  739 */     this._writeContext.writeFieldName(name.getValue());
/*  740 */     _appendFieldName(name);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void writeString(String text) throws IOException {
/*  751 */     if (text == null) {
/*  752 */       writeNull();
/*      */     } else {
/*  754 */       _appendValue(JsonToken.VALUE_STRING, text);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void writeString(char[] text, int offset, int len) throws IOException {
/*  760 */     writeString(new String(text, offset, len));
/*      */   }
/*      */ 
/*      */   
/*      */   public void writeString(SerializableString text) throws IOException {
/*  765 */     if (text == null) {
/*  766 */       writeNull();
/*      */     } else {
/*  768 */       _appendValue(JsonToken.VALUE_STRING, text);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void writeRawUTF8String(byte[] text, int offset, int length) throws IOException {
/*  776 */     _reportUnsupportedOperation();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void writeUTF8String(byte[] text, int offset, int length) throws IOException {
/*  783 */     _reportUnsupportedOperation();
/*      */   }
/*      */ 
/*      */   
/*      */   public void writeRaw(String text) throws IOException {
/*  788 */     _reportUnsupportedOperation();
/*      */   }
/*      */ 
/*      */   
/*      */   public void writeRaw(String text, int offset, int len) throws IOException {
/*  793 */     _reportUnsupportedOperation();
/*      */   }
/*      */ 
/*      */   
/*      */   public void writeRaw(SerializableString text) throws IOException {
/*  798 */     _reportUnsupportedOperation();
/*      */   }
/*      */ 
/*      */   
/*      */   public void writeRaw(char[] text, int offset, int len) throws IOException {
/*  803 */     _reportUnsupportedOperation();
/*      */   }
/*      */ 
/*      */   
/*      */   public void writeRaw(char c) throws IOException {
/*  808 */     _reportUnsupportedOperation();
/*      */   }
/*      */ 
/*      */   
/*      */   public void writeRawValue(String text) throws IOException {
/*  813 */     _appendValue(JsonToken.VALUE_EMBEDDED_OBJECT, new RawValue(text));
/*      */   }
/*      */ 
/*      */   
/*      */   public void writeRawValue(String text, int offset, int len) throws IOException {
/*  818 */     if (offset > 0 || len != text.length()) {
/*  819 */       text = text.substring(offset, offset + len);
/*      */     }
/*  821 */     _appendValue(JsonToken.VALUE_EMBEDDED_OBJECT, new RawValue(text));
/*      */   }
/*      */ 
/*      */   
/*      */   public void writeRawValue(char[] text, int offset, int len) throws IOException {
/*  826 */     _appendValue(JsonToken.VALUE_EMBEDDED_OBJECT, new String(text, offset, len));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void writeNumber(short i) throws IOException {
/*  837 */     _appendValue(JsonToken.VALUE_NUMBER_INT, Short.valueOf(i));
/*      */   }
/*      */ 
/*      */   
/*      */   public void writeNumber(int i) throws IOException {
/*  842 */     _appendValue(JsonToken.VALUE_NUMBER_INT, Integer.valueOf(i));
/*      */   }
/*      */ 
/*      */   
/*      */   public void writeNumber(long l) throws IOException {
/*  847 */     _appendValue(JsonToken.VALUE_NUMBER_INT, Long.valueOf(l));
/*      */   }
/*      */ 
/*      */   
/*      */   public void writeNumber(double d) throws IOException {
/*  852 */     _appendValue(JsonToken.VALUE_NUMBER_FLOAT, Double.valueOf(d));
/*      */   }
/*      */ 
/*      */   
/*      */   public void writeNumber(float f) throws IOException {
/*  857 */     _appendValue(JsonToken.VALUE_NUMBER_FLOAT, Float.valueOf(f));
/*      */   }
/*      */ 
/*      */   
/*      */   public void writeNumber(BigDecimal dec) throws IOException {
/*  862 */     if (dec == null) {
/*  863 */       writeNull();
/*      */     } else {
/*  865 */       _appendValue(JsonToken.VALUE_NUMBER_FLOAT, dec);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void writeNumber(BigInteger v) throws IOException {
/*  871 */     if (v == null) {
/*  872 */       writeNull();
/*      */     } else {
/*  874 */       _appendValue(JsonToken.VALUE_NUMBER_INT, v);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void writeNumber(String encodedValue) throws IOException {
/*  883 */     _appendValue(JsonToken.VALUE_NUMBER_FLOAT, encodedValue);
/*      */   }
/*      */ 
/*      */   
/*      */   public void writeBoolean(boolean state) throws IOException {
/*  888 */     _appendValue(state ? JsonToken.VALUE_TRUE : JsonToken.VALUE_FALSE);
/*      */   }
/*      */ 
/*      */   
/*      */   public void writeNull() throws IOException {
/*  893 */     _appendValue(JsonToken.VALUE_NULL);
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
/*      */   public void writeObject(Object value) throws IOException {
/*  905 */     if (value == null) {
/*  906 */       writeNull();
/*      */       return;
/*      */     } 
/*  909 */     Class<?> raw = value.getClass();
/*  910 */     if (raw == byte[].class || value instanceof RawValue) {
/*  911 */       _appendValue(JsonToken.VALUE_EMBEDDED_OBJECT, value);
/*      */       return;
/*      */     } 
/*  914 */     if (this._objectCodec == null) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  919 */       _appendValue(JsonToken.VALUE_EMBEDDED_OBJECT, value);
/*      */     } else {
/*  921 */       this._objectCodec.writeValue(this, value);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void writeTree(TreeNode node) throws IOException {
/*  928 */     if (node == null) {
/*  929 */       writeNull();
/*      */       
/*      */       return;
/*      */     } 
/*  933 */     if (this._objectCodec == null) {
/*      */       
/*  935 */       _appendValue(JsonToken.VALUE_EMBEDDED_OBJECT, node);
/*      */     } else {
/*  937 */       this._objectCodec.writeTree(this, node);
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
/*      */   public void writeBinary(Base64Variant b64variant, byte[] data, int offset, int len) throws IOException {
/*  956 */     byte[] copy = new byte[len];
/*  957 */     System.arraycopy(data, offset, copy, 0, len);
/*  958 */     writeObject(copy);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int writeBinary(Base64Variant b64variant, InputStream data, int dataLength) {
/*  969 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean canWriteTypeId() {
/*  980 */     return this._hasNativeTypeIds;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean canWriteObjectId() {
/*  985 */     return this._hasNativeObjectIds;
/*      */   }
/*      */ 
/*      */   
/*      */   public void writeTypeId(Object id) {
/*  990 */     this._typeId = id;
/*  991 */     this._hasNativeId = true;
/*      */   }
/*      */ 
/*      */   
/*      */   public void writeObjectId(Object id) {
/*  996 */     this._objectId = id;
/*  997 */     this._hasNativeId = true;
/*      */   }
/*      */ 
/*      */   
/*      */   public void writeEmbeddedObject(Object object) throws IOException {
/* 1002 */     _appendValue(JsonToken.VALUE_EMBEDDED_OBJECT, object);
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
/*      */   public void copyCurrentEvent(JsonParser p) throws IOException {
/* 1014 */     if (this._mayHaveNativeIds) {
/* 1015 */       _checkNativeIds(p);
/*      */     }
/* 1017 */     switch (p.getCurrentToken()) {
/*      */       case INT:
/* 1019 */         writeStartObject();
/*      */         return;
/*      */       case BIG_INTEGER:
/* 1022 */         writeEndObject();
/*      */         return;
/*      */       case BIG_DECIMAL:
/* 1025 */         writeStartArray();
/*      */         return;
/*      */       case FLOAT:
/* 1028 */         writeEndArray();
/*      */         return;
/*      */       case LONG:
/* 1031 */         writeFieldName(p.getCurrentName());
/*      */         return;
/*      */       case null:
/* 1034 */         if (p.hasTextCharacters()) {
/* 1035 */           writeString(p.getTextCharacters(), p.getTextOffset(), p.getTextLength());
/*      */         } else {
/* 1037 */           writeString(p.getText());
/*      */         } 
/*      */         return;
/*      */       case null:
/* 1041 */         switch (p.getNumberType()) {
/*      */           case INT:
/* 1043 */             writeNumber(p.getIntValue());
/*      */             return;
/*      */           case BIG_INTEGER:
/* 1046 */             writeNumber(p.getBigIntegerValue());
/*      */             return;
/*      */         } 
/* 1049 */         writeNumber(p.getLongValue());
/*      */         return;
/*      */       
/*      */       case null:
/* 1053 */         if (this._forceBigDecimal) {
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1058 */           writeNumber(p.getDecimalValue());
/*      */         } else {
/* 1060 */           switch (p.getNumberType()) {
/*      */             case BIG_DECIMAL:
/* 1062 */               writeNumber(p.getDecimalValue());
/*      */               return;
/*      */             case FLOAT:
/* 1065 */               writeNumber(p.getFloatValue());
/*      */               return;
/*      */           } 
/* 1068 */           writeNumber(p.getDoubleValue());
/*      */         } 
/*      */         return;
/*      */       
/*      */       case null:
/* 1073 */         writeBoolean(true);
/*      */         return;
/*      */       case null:
/* 1076 */         writeBoolean(false);
/*      */         return;
/*      */       case null:
/* 1079 */         writeNull();
/*      */         return;
/*      */       case null:
/* 1082 */         writeObject(p.getEmbeddedObject());
/*      */         return;
/*      */     } 
/* 1085 */     throw new RuntimeException("Internal error: unexpected token: " + p.getCurrentToken());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void copyCurrentStructure(JsonParser p) throws IOException {
/* 1092 */     JsonToken t = p.getCurrentToken();
/*      */ 
/*      */     
/* 1095 */     if (t == JsonToken.FIELD_NAME) {
/* 1096 */       if (this._mayHaveNativeIds) {
/* 1097 */         _checkNativeIds(p);
/*      */       }
/* 1099 */       writeFieldName(p.getCurrentName());
/* 1100 */       t = p.nextToken();
/*      */     }
/* 1102 */     else if (t == null) {
/* 1103 */       throw new IllegalStateException("No token available from argument `JsonParser`");
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1109 */     switch (t) {
/*      */       case BIG_DECIMAL:
/* 1111 */         if (this._mayHaveNativeIds) {
/* 1112 */           _checkNativeIds(p);
/*      */         }
/* 1114 */         writeStartArray();
/* 1115 */         _copyBufferContents(p);
/*      */         return;
/*      */       case INT:
/* 1118 */         if (this._mayHaveNativeIds) {
/* 1119 */           _checkNativeIds(p);
/*      */         }
/* 1121 */         writeStartObject();
/* 1122 */         _copyBufferContents(p);
/*      */         return;
/*      */       case FLOAT:
/* 1125 */         writeEndArray();
/*      */         return;
/*      */       case BIG_INTEGER:
/* 1128 */         writeEndObject();
/*      */         return;
/*      */     } 
/* 1131 */     _copyBufferValue(p, t);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void _copyBufferContents(JsonParser p) throws IOException {
/* 1137 */     int depth = 1;
/*      */     
/*      */     JsonToken t;
/* 1140 */     while ((t = p.nextToken()) != null) {
/* 1141 */       switch (t) {
/*      */         case LONG:
/* 1143 */           if (this._mayHaveNativeIds) {
/* 1144 */             _checkNativeIds(p);
/*      */           }
/* 1146 */           writeFieldName(p.getCurrentName());
/*      */           continue;
/*      */         
/*      */         case BIG_DECIMAL:
/* 1150 */           if (this._mayHaveNativeIds) {
/* 1151 */             _checkNativeIds(p);
/*      */           }
/* 1153 */           writeStartArray();
/* 1154 */           depth++;
/*      */           continue;
/*      */         
/*      */         case INT:
/* 1158 */           if (this._mayHaveNativeIds) {
/* 1159 */             _checkNativeIds(p);
/*      */           }
/* 1161 */           writeStartObject();
/* 1162 */           depth++;
/*      */           continue;
/*      */         
/*      */         case FLOAT:
/* 1166 */           writeEndArray();
/* 1167 */           if (--depth == 0) {
/*      */             return;
/*      */           }
/*      */           continue;
/*      */         case BIG_INTEGER:
/* 1172 */           writeEndObject();
/* 1173 */           if (--depth == 0) {
/*      */             return;
/*      */           }
/*      */           continue;
/*      */       } 
/*      */       
/* 1179 */       _copyBufferValue(p, t);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void _copyBufferValue(JsonParser p, JsonToken t) throws IOException {
/* 1187 */     if (this._mayHaveNativeIds) {
/* 1188 */       _checkNativeIds(p);
/*      */     }
/* 1190 */     switch (t) {
/*      */       case null:
/* 1192 */         if (p.hasTextCharacters()) {
/* 1193 */           writeString(p.getTextCharacters(), p.getTextOffset(), p.getTextLength());
/*      */         } else {
/* 1195 */           writeString(p.getText());
/*      */         } 
/*      */         return;
/*      */       case null:
/* 1199 */         switch (p.getNumberType()) {
/*      */           case INT:
/* 1201 */             writeNumber(p.getIntValue());
/*      */             return;
/*      */           case BIG_INTEGER:
/* 1204 */             writeNumber(p.getBigIntegerValue());
/*      */             return;
/*      */         } 
/* 1207 */         writeNumber(p.getLongValue());
/*      */         return;
/*      */       
/*      */       case null:
/* 1211 */         if (this._forceBigDecimal) {
/* 1212 */           writeNumber(p.getDecimalValue());
/*      */         } else {
/* 1214 */           switch (p.getNumberType()) {
/*      */             case BIG_DECIMAL:
/* 1216 */               writeNumber(p.getDecimalValue());
/*      */               return;
/*      */             case FLOAT:
/* 1219 */               writeNumber(p.getFloatValue());
/*      */               return;
/*      */           } 
/* 1222 */           writeNumber(p.getDoubleValue());
/*      */         } 
/*      */         return;
/*      */       
/*      */       case null:
/* 1227 */         writeBoolean(true);
/*      */         return;
/*      */       case null:
/* 1230 */         writeBoolean(false);
/*      */         return;
/*      */       case null:
/* 1233 */         writeNull();
/*      */         return;
/*      */       case null:
/* 1236 */         writeObject(p.getEmbeddedObject());
/*      */         return;
/*      */     } 
/* 1239 */     throw new RuntimeException("Internal error: unexpected token: " + t);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private final void _checkNativeIds(JsonParser p) throws IOException {
/* 1245 */     if ((this._typeId = p.getTypeId()) != null) {
/* 1246 */       this._hasNativeId = true;
/*      */     }
/* 1248 */     if ((this._objectId = p.getObjectId()) != null) {
/* 1249 */       this._hasNativeId = true;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final void _appendValue(JsonToken type) {
/*      */     Segment next;
/* 1302 */     this._writeContext.writeValue();
/*      */     
/* 1304 */     if (this._hasNativeId) {
/* 1305 */       next = this._last.append(this._appendAt, type, this._objectId, this._typeId);
/*      */     } else {
/* 1307 */       next = this._last.append(this._appendAt, type);
/*      */     } 
/* 1309 */     if (next == null) {
/* 1310 */       this._appendAt++;
/*      */     } else {
/* 1312 */       this._last = next;
/* 1313 */       this._appendAt = 1;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final void _appendValue(JsonToken type, Object value) {
/*      */     Segment next;
/* 1325 */     this._writeContext.writeValue();
/*      */     
/* 1327 */     if (this._hasNativeId) {
/* 1328 */       next = this._last.append(this._appendAt, type, value, this._objectId, this._typeId);
/*      */     } else {
/* 1330 */       next = this._last.append(this._appendAt, type, value);
/*      */     } 
/* 1332 */     if (next == null) {
/* 1333 */       this._appendAt++;
/*      */     } else {
/* 1335 */       this._last = next;
/* 1336 */       this._appendAt = 1;
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
/*      */   protected final void _appendFieldName(Object value) {
/*      */     Segment next;
/* 1350 */     if (this._hasNativeId) {
/* 1351 */       next = this._last.append(this._appendAt, JsonToken.FIELD_NAME, value, this._objectId, this._typeId);
/*      */     } else {
/* 1353 */       next = this._last.append(this._appendAt, JsonToken.FIELD_NAME, value);
/*      */     } 
/* 1355 */     if (next == null) {
/* 1356 */       this._appendAt++;
/*      */     } else {
/* 1358 */       this._last = next;
/* 1359 */       this._appendAt = 1;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final void _appendStartMarker(JsonToken type) {
/*      */     Segment next;
/* 1371 */     if (this._hasNativeId) {
/* 1372 */       next = this._last.append(this._appendAt, type, this._objectId, this._typeId);
/*      */     } else {
/* 1374 */       next = this._last.append(this._appendAt, type);
/*      */     } 
/* 1376 */     if (next == null) {
/* 1377 */       this._appendAt++;
/*      */     } else {
/* 1379 */       this._last = next;
/* 1380 */       this._appendAt = 1;
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
/*      */   protected final void _appendEndMarker(JsonToken type) {
/* 1392 */     Segment next = this._last.append(this._appendAt, type);
/* 1393 */     if (next == null) {
/* 1394 */       this._appendAt++;
/*      */     } else {
/* 1396 */       this._last = next;
/* 1397 */       this._appendAt = 1;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected void _reportUnsupportedOperation() {
/* 1403 */     throw new UnsupportedOperationException("Called operation not supported for TokenBuffer");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static final class Parser
/*      */     extends ParserMinimalBase
/*      */   {
/*      */     protected ObjectCodec _codec;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected final boolean _hasNativeTypeIds;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected final boolean _hasNativeObjectIds;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected final boolean _hasNativeIds;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected TokenBuffer.Segment _segment;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected int _segmentPtr;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected TokenBufferReadContext _parsingContext;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected boolean _closed;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected transient ByteArrayBuilder _byteBuilder;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1461 */     protected JsonLocation _location = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     public Parser(TokenBuffer.Segment firstSeg, ObjectCodec codec, boolean hasNativeTypeIds, boolean hasNativeObjectIds) {
/* 1473 */       this(firstSeg, codec, hasNativeTypeIds, hasNativeObjectIds, (JsonStreamContext)null);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Parser(TokenBuffer.Segment firstSeg, ObjectCodec codec, boolean hasNativeTypeIds, boolean hasNativeObjectIds, JsonStreamContext parentContext) {
/* 1480 */       super(0);
/* 1481 */       this._segment = firstSeg;
/* 1482 */       this._segmentPtr = -1;
/* 1483 */       this._codec = codec;
/* 1484 */       this._parsingContext = TokenBufferReadContext.createRootContext(parentContext);
/* 1485 */       this._hasNativeTypeIds = hasNativeTypeIds;
/* 1486 */       this._hasNativeObjectIds = hasNativeObjectIds;
/* 1487 */       this._hasNativeIds = hasNativeTypeIds | hasNativeObjectIds;
/*      */     }
/*      */     
/*      */     public void setLocation(JsonLocation l) {
/* 1491 */       this._location = l;
/*      */     }
/*      */     
/*      */     public ObjectCodec getCodec() {
/* 1495 */       return this._codec;
/*      */     }
/*      */     public void setCodec(ObjectCodec c) {
/* 1498 */       this._codec = c;
/*      */     }
/*      */     
/*      */     public Version version() {
/* 1502 */       return PackageVersion.VERSION;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public JsonToken peekNextToken() throws IOException {
/* 1514 */       if (this._closed) return null; 
/* 1515 */       TokenBuffer.Segment seg = this._segment;
/* 1516 */       int ptr = this._segmentPtr + 1;
/* 1517 */       if (ptr >= 16) {
/* 1518 */         ptr = 0;
/* 1519 */         seg = (seg == null) ? null : seg.next();
/*      */       } 
/* 1521 */       return (seg == null) ? null : seg.type(ptr);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void close() throws IOException {
/* 1532 */       if (!this._closed) {
/* 1533 */         this._closed = true;
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public JsonToken nextToken() throws IOException {
/* 1547 */       if (this._closed || this._segment == null) return null;
/*      */ 
/*      */       
/* 1550 */       if (++this._segmentPtr >= 16) {
/* 1551 */         this._segmentPtr = 0;
/* 1552 */         this._segment = this._segment.next();
/* 1553 */         if (this._segment == null) {
/* 1554 */           return null;
/*      */         }
/*      */       } 
/* 1557 */       this._currToken = this._segment.type(this._segmentPtr);
/*      */       
/* 1559 */       if (this._currToken == JsonToken.FIELD_NAME) {
/* 1560 */         Object ob = _currentObject();
/* 1561 */         String name = (ob instanceof String) ? (String)ob : ob.toString();
/* 1562 */         this._parsingContext.setCurrentName(name);
/* 1563 */       } else if (this._currToken == JsonToken.START_OBJECT) {
/* 1564 */         this._parsingContext = this._parsingContext.createChildObjectContext();
/* 1565 */       } else if (this._currToken == JsonToken.START_ARRAY) {
/* 1566 */         this._parsingContext = this._parsingContext.createChildArrayContext();
/* 1567 */       } else if (this._currToken == JsonToken.END_OBJECT || this._currToken == JsonToken.END_ARRAY) {
/*      */ 
/*      */         
/* 1570 */         this._parsingContext = this._parsingContext.parentOrCopy();
/*      */       } 
/* 1572 */       return this._currToken;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String nextFieldName() throws IOException {
/* 1579 */       if (this._closed || this._segment == null) {
/* 1580 */         return null;
/*      */       }
/*      */       
/* 1583 */       int ptr = this._segmentPtr + 1;
/* 1584 */       if (ptr < 16 && this._segment.type(ptr) == JsonToken.FIELD_NAME) {
/* 1585 */         this._segmentPtr = ptr;
/* 1586 */         this._currToken = JsonToken.FIELD_NAME;
/* 1587 */         Object ob = this._segment.get(ptr);
/* 1588 */         String name = (ob instanceof String) ? (String)ob : ob.toString();
/* 1589 */         this._parsingContext.setCurrentName(name);
/* 1590 */         return name;
/*      */       } 
/* 1592 */       return (nextToken() == JsonToken.FIELD_NAME) ? getCurrentName() : null;
/*      */     }
/*      */     
/*      */     public boolean isClosed() {
/* 1596 */       return this._closed;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public JsonStreamContext getParsingContext() {
/* 1605 */       return this._parsingContext;
/*      */     }
/*      */     public JsonLocation getTokenLocation() {
/* 1608 */       return getCurrentLocation();
/*      */     }
/*      */     
/*      */     public JsonLocation getCurrentLocation() {
/* 1612 */       return (this._location == null) ? JsonLocation.NA : this._location;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public String getCurrentName() {
/* 1618 */       if (this._currToken == JsonToken.START_OBJECT || this._currToken == JsonToken.START_ARRAY) {
/* 1619 */         JsonStreamContext parent = this._parsingContext.getParent();
/* 1620 */         return parent.getCurrentName();
/*      */       } 
/* 1622 */       return this._parsingContext.getCurrentName();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void overrideCurrentName(String name) {
/* 1629 */       JsonStreamContext ctxt = this._parsingContext;
/* 1630 */       if (this._currToken == JsonToken.START_OBJECT || this._currToken == JsonToken.START_ARRAY) {
/* 1631 */         ctxt = ctxt.getParent();
/*      */       }
/* 1633 */       if (ctxt instanceof TokenBufferReadContext) {
/*      */         try {
/* 1635 */           ((TokenBufferReadContext)ctxt).setCurrentName(name);
/* 1636 */         } catch (IOException e) {
/* 1637 */           throw new RuntimeException(e);
/*      */         } 
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String getText() {
/* 1652 */       if (this._currToken == JsonToken.VALUE_STRING || this._currToken == JsonToken.FIELD_NAME) {
/*      */         
/* 1654 */         Object ob = _currentObject();
/* 1655 */         if (ob instanceof String) {
/* 1656 */           return (String)ob;
/*      */         }
/* 1658 */         return ClassUtil.nullOrToString(ob);
/*      */       } 
/* 1660 */       if (this._currToken == null) {
/* 1661 */         return null;
/*      */       }
/* 1663 */       switch (this._currToken) {
/*      */         case null:
/*      */         case null:
/* 1666 */           return ClassUtil.nullOrToString(_currentObject());
/*      */       } 
/* 1668 */       return this._currToken.asString();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public char[] getTextCharacters() {
/* 1674 */       String str = getText();
/* 1675 */       return (str == null) ? null : str.toCharArray();
/*      */     }
/*      */ 
/*      */     
/*      */     public int getTextLength() {
/* 1680 */       String str = getText();
/* 1681 */       return (str == null) ? 0 : str.length();
/*      */     }
/*      */     
/*      */     public int getTextOffset() {
/* 1685 */       return 0;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean hasTextCharacters() {
/* 1690 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean isNaN() {
/* 1702 */       if (this._currToken == JsonToken.VALUE_NUMBER_FLOAT) {
/* 1703 */         Object value = _currentObject();
/* 1704 */         if (value instanceof Double) {
/* 1705 */           Double v = (Double)value;
/* 1706 */           return (v.isNaN() || v.isInfinite());
/*      */         } 
/* 1708 */         if (value instanceof Float) {
/* 1709 */           Float v = (Float)value;
/* 1710 */           return (v.isNaN() || v.isInfinite());
/*      */         } 
/*      */       } 
/* 1713 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public BigInteger getBigIntegerValue() throws IOException {
/* 1719 */       Number n = getNumberValue();
/* 1720 */       if (n instanceof BigInteger) {
/* 1721 */         return (BigInteger)n;
/*      */       }
/* 1723 */       if (getNumberType() == JsonParser.NumberType.BIG_DECIMAL) {
/* 1724 */         return ((BigDecimal)n).toBigInteger();
/*      */       }
/*      */       
/* 1727 */       return BigInteger.valueOf(n.longValue());
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public BigDecimal getDecimalValue() throws IOException {
/* 1733 */       Number n = getNumberValue();
/* 1734 */       if (n instanceof BigDecimal) {
/* 1735 */         return (BigDecimal)n;
/*      */       }
/* 1737 */       switch (getNumberType()) {
/*      */         case INT:
/*      */         case LONG:
/* 1740 */           return BigDecimal.valueOf(n.longValue());
/*      */         case BIG_INTEGER:
/* 1742 */           return new BigDecimal((BigInteger)n);
/*      */       } 
/*      */ 
/*      */       
/* 1746 */       return BigDecimal.valueOf(n.doubleValue());
/*      */     }
/*      */ 
/*      */     
/*      */     public double getDoubleValue() throws IOException {
/* 1751 */       return getNumberValue().doubleValue();
/*      */     }
/*      */ 
/*      */     
/*      */     public float getFloatValue() throws IOException {
/* 1756 */       return getNumberValue().floatValue();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int getIntValue() throws IOException {
/* 1763 */       Number n = (this._currToken == JsonToken.VALUE_NUMBER_INT) ? (Number)_currentObject() : getNumberValue();
/* 1764 */       if (n instanceof Integer || _smallerThanInt(n)) {
/* 1765 */         return n.intValue();
/*      */       }
/* 1767 */       return _convertNumberToInt(n);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public long getLongValue() throws IOException {
/* 1773 */       Number n = (this._currToken == JsonToken.VALUE_NUMBER_INT) ? (Number)_currentObject() : getNumberValue();
/* 1774 */       if (n instanceof Long || _smallerThanLong(n)) {
/* 1775 */         return n.longValue();
/*      */       }
/* 1777 */       return _convertNumberToLong(n);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public JsonParser.NumberType getNumberType() throws IOException {
/* 1783 */       Number n = getNumberValue();
/* 1784 */       if (n instanceof Integer) return JsonParser.NumberType.INT; 
/* 1785 */       if (n instanceof Long) return JsonParser.NumberType.LONG; 
/* 1786 */       if (n instanceof Double) return JsonParser.NumberType.DOUBLE; 
/* 1787 */       if (n instanceof BigDecimal) return JsonParser.NumberType.BIG_DECIMAL; 
/* 1788 */       if (n instanceof BigInteger) return JsonParser.NumberType.BIG_INTEGER; 
/* 1789 */       if (n instanceof Float) return JsonParser.NumberType.FLOAT; 
/* 1790 */       if (n instanceof Short) return JsonParser.NumberType.INT; 
/* 1791 */       return null;
/*      */     }
/*      */ 
/*      */     
/*      */     public final Number getNumberValue() throws IOException {
/* 1796 */       _checkIsNumber();
/* 1797 */       Object value = _currentObject();
/* 1798 */       if (value instanceof Number) {
/* 1799 */         return (Number)value;
/*      */       }
/*      */ 
/*      */ 
/*      */       
/* 1804 */       if (value instanceof String) {
/* 1805 */         String str = (String)value;
/* 1806 */         if (str.indexOf('.') >= 0) {
/* 1807 */           return Double.valueOf(Double.parseDouble(str));
/*      */         }
/* 1809 */         return Long.valueOf(Long.parseLong(str));
/*      */       } 
/* 1811 */       if (value == null) {
/* 1812 */         return null;
/*      */       }
/* 1814 */       throw new IllegalStateException("Internal error: entry should be a Number, but is of type " + value
/* 1815 */           .getClass().getName());
/*      */     }
/*      */     
/*      */     private final boolean _smallerThanInt(Number n) {
/* 1819 */       return (n instanceof Short || n instanceof Byte);
/*      */     }
/*      */     
/*      */     private final boolean _smallerThanLong(Number n) {
/* 1823 */       return (n instanceof Integer || n instanceof Short || n instanceof Byte);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected int _convertNumberToInt(Number n) throws IOException {
/* 1830 */       if (n instanceof Long) {
/* 1831 */         long l = n.longValue();
/* 1832 */         int result = (int)l;
/* 1833 */         if (result != l) {
/* 1834 */           reportOverflowInt();
/*      */         }
/* 1836 */         return result;
/*      */       } 
/* 1838 */       if (n instanceof BigInteger) {
/* 1839 */         BigInteger big = (BigInteger)n;
/* 1840 */         if (BI_MIN_INT.compareTo(big) > 0 || BI_MAX_INT
/* 1841 */           .compareTo(big) < 0)
/* 1842 */           reportOverflowInt(); 
/*      */       } else {
/* 1844 */         if (n instanceof Double || n instanceof Float) {
/* 1845 */           double d = n.doubleValue();
/*      */           
/* 1847 */           if (d < -2.147483648E9D || d > 2.147483647E9D) {
/* 1848 */             reportOverflowInt();
/*      */           }
/* 1850 */           return (int)d;
/* 1851 */         }  if (n instanceof BigDecimal) {
/* 1852 */           BigDecimal big = (BigDecimal)n;
/* 1853 */           if (BD_MIN_INT.compareTo(big) > 0 || BD_MAX_INT
/* 1854 */             .compareTo(big) < 0) {
/* 1855 */             reportOverflowInt();
/*      */           }
/*      */         } else {
/* 1858 */           _throwInternal();
/*      */         } 
/* 1860 */       }  return n.intValue();
/*      */     }
/*      */ 
/*      */     
/*      */     protected long _convertNumberToLong(Number n) throws IOException {
/* 1865 */       if (n instanceof BigInteger) {
/* 1866 */         BigInteger big = (BigInteger)n;
/* 1867 */         if (BI_MIN_LONG.compareTo(big) > 0 || BI_MAX_LONG
/* 1868 */           .compareTo(big) < 0)
/* 1869 */           reportOverflowLong(); 
/*      */       } else {
/* 1871 */         if (n instanceof Double || n instanceof Float) {
/* 1872 */           double d = n.doubleValue();
/*      */           
/* 1874 */           if (d < -9.223372036854776E18D || d > 9.223372036854776E18D) {
/* 1875 */             reportOverflowLong();
/*      */           }
/* 1877 */           return (long)d;
/* 1878 */         }  if (n instanceof BigDecimal) {
/* 1879 */           BigDecimal big = (BigDecimal)n;
/* 1880 */           if (BD_MIN_LONG.compareTo(big) > 0 || BD_MAX_LONG
/* 1881 */             .compareTo(big) < 0) {
/* 1882 */             reportOverflowLong();
/*      */           }
/*      */         } else {
/* 1885 */           _throwInternal();
/*      */         } 
/* 1887 */       }  return n.longValue();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Object getEmbeddedObject() {
/* 1899 */       if (this._currToken == JsonToken.VALUE_EMBEDDED_OBJECT) {
/* 1900 */         return _currentObject();
/*      */       }
/* 1902 */       return null;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public byte[] getBinaryValue(Base64Variant b64variant) throws IOException, JsonParseException {
/* 1910 */       if (this._currToken == JsonToken.VALUE_EMBEDDED_OBJECT) {
/*      */         
/* 1912 */         Object ob = _currentObject();
/* 1913 */         if (ob instanceof byte[]) {
/* 1914 */           return (byte[])ob;
/*      */         }
/*      */       } 
/*      */       
/* 1918 */       if (this._currToken != JsonToken.VALUE_STRING) {
/* 1919 */         throw _constructError("Current token (" + this._currToken + ") not VALUE_STRING (or VALUE_EMBEDDED_OBJECT with byte[]), cannot access as binary");
/*      */       }
/* 1921 */       String str = getText();
/* 1922 */       if (str == null) {
/* 1923 */         return null;
/*      */       }
/* 1925 */       ByteArrayBuilder builder = this._byteBuilder;
/* 1926 */       if (builder == null) {
/* 1927 */         this._byteBuilder = builder = new ByteArrayBuilder(100);
/*      */       } else {
/* 1929 */         this._byteBuilder.reset();
/*      */       } 
/* 1931 */       _decodeBase64(str, builder, b64variant);
/* 1932 */       return builder.toByteArray();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public int readBinaryValue(Base64Variant b64variant, OutputStream out) throws IOException {
/* 1938 */       byte[] data = getBinaryValue(b64variant);
/* 1939 */       if (data != null) {
/* 1940 */         out.write(data, 0, data.length);
/* 1941 */         return data.length;
/*      */       } 
/* 1943 */       return 0;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean canReadObjectId() {
/* 1954 */       return this._hasNativeObjectIds;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canReadTypeId() {
/* 1959 */       return this._hasNativeTypeIds;
/*      */     }
/*      */ 
/*      */     
/*      */     public Object getTypeId() {
/* 1964 */       return this._segment.findTypeId(this._segmentPtr);
/*      */     }
/*      */ 
/*      */     
/*      */     public Object getObjectId() {
/* 1969 */       return this._segment.findObjectId(this._segmentPtr);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected final Object _currentObject() {
/* 1979 */       return this._segment.get(this._segmentPtr);
/*      */     }
/*      */ 
/*      */     
/*      */     protected final void _checkIsNumber() throws JsonParseException {
/* 1984 */       if (this._currToken == null || !this._currToken.isNumeric()) {
/* 1985 */         throw _constructError("Current token (" + this._currToken + ") not numeric, cannot use numeric value accessors");
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     protected void _handleEOF() throws JsonParseException {
/* 1991 */       _throwInternal();
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
/*      */   protected static final class Segment
/*      */   {
/*      */     public static final int TOKENS_PER_SEGMENT = 16;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2013 */     private static final JsonToken[] TOKEN_TYPES_BY_INDEX = new JsonToken[16]; static {
/* 2014 */       JsonToken[] t = JsonToken.values();
/*      */       
/* 2016 */       System.arraycopy(t, 1, TOKEN_TYPES_BY_INDEX, 1, Math.min(15, t.length - 1));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected Segment _next;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected long _tokenTypes;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2034 */     protected final Object[] _tokens = new Object[16];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected TreeMap<Integer, Object> _nativeIds;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public JsonToken type(int index) {
/* 2047 */       long l = this._tokenTypes;
/* 2048 */       if (index > 0) {
/* 2049 */         l >>= index << 2;
/*      */       }
/* 2051 */       int ix = (int)l & 0xF;
/* 2052 */       return TOKEN_TYPES_BY_INDEX[ix];
/*      */     }
/*      */ 
/*      */     
/*      */     public int rawType(int index) {
/* 2057 */       long l = this._tokenTypes;
/* 2058 */       if (index > 0) {
/* 2059 */         l >>= index << 2;
/*      */       }
/* 2061 */       int ix = (int)l & 0xF;
/* 2062 */       return ix;
/*      */     }
/*      */     
/*      */     public Object get(int index) {
/* 2066 */       return this._tokens[index];
/*      */     }
/*      */     public Segment next() {
/* 2069 */       return this._next;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean hasIds() {
/* 2076 */       return (this._nativeIds != null);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Segment append(int index, JsonToken tokenType) {
/* 2083 */       if (index < 16) {
/* 2084 */         set(index, tokenType);
/* 2085 */         return null;
/*      */       } 
/* 2087 */       this._next = new Segment();
/* 2088 */       this._next.set(0, tokenType);
/* 2089 */       return this._next;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public Segment append(int index, JsonToken tokenType, Object objectId, Object typeId) {
/* 2095 */       if (index < 16) {
/* 2096 */         set(index, tokenType, objectId, typeId);
/* 2097 */         return null;
/*      */       } 
/* 2099 */       this._next = new Segment();
/* 2100 */       this._next.set(0, tokenType, objectId, typeId);
/* 2101 */       return this._next;
/*      */     }
/*      */ 
/*      */     
/*      */     public Segment append(int index, JsonToken tokenType, Object value) {
/* 2106 */       if (index < 16) {
/* 2107 */         set(index, tokenType, value);
/* 2108 */         return null;
/*      */       } 
/* 2110 */       this._next = new Segment();
/* 2111 */       this._next.set(0, tokenType, value);
/* 2112 */       return this._next;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public Segment append(int index, JsonToken tokenType, Object value, Object objectId, Object typeId) {
/* 2118 */       if (index < 16) {
/* 2119 */         set(index, tokenType, value, objectId, typeId);
/* 2120 */         return null;
/*      */       } 
/* 2122 */       this._next = new Segment();
/* 2123 */       this._next.set(0, tokenType, value, objectId, typeId);
/* 2124 */       return this._next;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private void set(int index, JsonToken tokenType) {
/* 2178 */       long typeCode = tokenType.ordinal();
/* 2179 */       if (index > 0) {
/* 2180 */         typeCode <<= index << 2;
/*      */       }
/* 2182 */       this._tokenTypes |= typeCode;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     private void set(int index, JsonToken tokenType, Object objectId, Object typeId) {
/* 2188 */       long typeCode = tokenType.ordinal();
/* 2189 */       if (index > 0) {
/* 2190 */         typeCode <<= index << 2;
/*      */       }
/* 2192 */       this._tokenTypes |= typeCode;
/* 2193 */       assignNativeIds(index, objectId, typeId);
/*      */     }
/*      */ 
/*      */     
/*      */     private void set(int index, JsonToken tokenType, Object value) {
/* 2198 */       this._tokens[index] = value;
/* 2199 */       long typeCode = tokenType.ordinal();
/* 2200 */       if (index > 0) {
/* 2201 */         typeCode <<= index << 2;
/*      */       }
/* 2203 */       this._tokenTypes |= typeCode;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     private void set(int index, JsonToken tokenType, Object value, Object objectId, Object typeId) {
/* 2209 */       this._tokens[index] = value;
/* 2210 */       long typeCode = tokenType.ordinal();
/* 2211 */       if (index > 0) {
/* 2212 */         typeCode <<= index << 2;
/*      */       }
/* 2214 */       this._tokenTypes |= typeCode;
/* 2215 */       assignNativeIds(index, objectId, typeId);
/*      */     }
/*      */ 
/*      */     
/*      */     private final void assignNativeIds(int index, Object objectId, Object typeId) {
/* 2220 */       if (this._nativeIds == null) {
/* 2221 */         this._nativeIds = new TreeMap<>();
/*      */       }
/* 2223 */       if (objectId != null) {
/* 2224 */         this._nativeIds.put(Integer.valueOf(_objectIdIndex(index)), objectId);
/*      */       }
/* 2226 */       if (typeId != null) {
/* 2227 */         this._nativeIds.put(Integer.valueOf(_typeIdIndex(index)), typeId);
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private Object findObjectId(int index) {
/* 2235 */       return (this._nativeIds == null) ? null : this._nativeIds.get(Integer.valueOf(_objectIdIndex(index)));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private Object findTypeId(int index) {
/* 2242 */       return (this._nativeIds == null) ? null : this._nativeIds.get(Integer.valueOf(_typeIdIndex(index)));
/*      */     }
/*      */     
/* 2245 */     private final int _typeIdIndex(int i) { return i + i; } private final int _objectIdIndex(int i) {
/* 2246 */       return i + i + 1;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databin\\util\TokenBuffer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */