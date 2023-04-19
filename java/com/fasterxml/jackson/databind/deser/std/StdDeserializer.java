/*      */ package com.fasterxml.jackson.databind.deser.std;
/*      */ 
/*      */ import com.fasterxml.jackson.annotation.JsonFormat;
/*      */ import com.fasterxml.jackson.annotation.Nulls;
/*      */ import com.fasterxml.jackson.core.JsonParseException;
/*      */ import com.fasterxml.jackson.core.JsonParser;
/*      */ import com.fasterxml.jackson.core.JsonToken;
/*      */ import com.fasterxml.jackson.core.io.NumberInput;
/*      */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*      */ import com.fasterxml.jackson.databind.BeanProperty;
/*      */ import com.fasterxml.jackson.databind.DeserializationContext;
/*      */ import com.fasterxml.jackson.databind.DeserializationFeature;
/*      */ import com.fasterxml.jackson.databind.JavaType;
/*      */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*      */ import com.fasterxml.jackson.databind.JsonMappingException;
/*      */ import com.fasterxml.jackson.databind.KeyDeserializer;
/*      */ import com.fasterxml.jackson.databind.MapperFeature;
/*      */ import com.fasterxml.jackson.databind.PropertyMetadata;
/*      */ import com.fasterxml.jackson.databind.cfg.MapperConfig;
/*      */ import com.fasterxml.jackson.databind.deser.BeanDeserializerBase;
/*      */ import com.fasterxml.jackson.databind.deser.NullValueProvider;
/*      */ import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
/*      */ import com.fasterxml.jackson.databind.deser.ValueInstantiator;
/*      */ import com.fasterxml.jackson.databind.deser.impl.NullsAsEmptyProvider;
/*      */ import com.fasterxml.jackson.databind.deser.impl.NullsConstantProvider;
/*      */ import com.fasterxml.jackson.databind.deser.impl.NullsFailProvider;
/*      */ import com.fasterxml.jackson.databind.introspect.Annotated;
/*      */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*      */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*      */ import com.fasterxml.jackson.databind.util.AccessPattern;
/*      */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*      */ import com.fasterxml.jackson.databind.util.Converter;
/*      */ import java.io.IOException;
/*      */ import java.io.Serializable;
/*      */ import java.util.Collection;
/*      */ import java.util.Date;
/*      */ import java.util.Map;
/*      */ 
/*      */ public abstract class StdDeserializer<T>
/*      */   extends JsonDeserializer<T>
/*      */   implements Serializable
/*      */ {
/*      */   private static final long serialVersionUID = 1L;
/*   44 */   protected static final int F_MASK_INT_COERCIONS = DeserializationFeature.USE_BIG_INTEGER_FOR_INTS
/*   45 */     .getMask() | DeserializationFeature.USE_LONG_FOR_INTS
/*   46 */     .getMask();
/*      */ 
/*      */   
/*   49 */   protected static final int F_MASK_ACCEPT_ARRAYS = DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS
/*   50 */     .getMask() | DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT
/*   51 */     .getMask();
/*      */ 
/*      */ 
/*      */   
/*      */   protected final Class<?> _valueClass;
/*      */ 
/*      */ 
/*      */   
/*      */   protected final JavaType _valueType;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected StdDeserializer(Class<?> vc) {
/*   65 */     this._valueClass = vc;
/*   66 */     this._valueType = null;
/*      */   }
/*      */ 
/*      */   
/*      */   protected StdDeserializer(JavaType valueType) {
/*   71 */     this._valueClass = (valueType == null) ? Object.class : valueType.getRawClass();
/*   72 */     this._valueType = valueType;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected StdDeserializer(StdDeserializer<?> src) {
/*   82 */     this._valueClass = src._valueClass;
/*   83 */     this._valueType = src._valueType;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Class<?> handledType() {
/*   93 */     return this._valueClass;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public final Class<?> getValueClass() {
/*  105 */     return this._valueClass;
/*      */   }
/*      */ 
/*      */   
/*      */   public JavaType getValueType() {
/*  110 */     return this._valueType;
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
/*      */   public JavaType getValueType(DeserializationContext ctxt) {
/*  126 */     if (this._valueType != null) {
/*  127 */       return this._valueType;
/*      */     }
/*  129 */     return ctxt.constructType(this._valueClass);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean isDefaultDeserializer(JsonDeserializer<?> deserializer) {
/*  139 */     return ClassUtil.isJacksonStdImpl(deserializer);
/*      */   }
/*      */   
/*      */   protected boolean isDefaultKeyDeserializer(KeyDeserializer keyDeser) {
/*  143 */     return ClassUtil.isJacksonStdImpl(keyDeser);
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
/*      */   public Object deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException {
/*  160 */     return typeDeserializer.deserializeTypedFromAny(p, ctxt);
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
/*      */   protected final boolean _parseBooleanPrimitive(JsonParser p, DeserializationContext ctxt) throws IOException {
/*  173 */     JsonToken t = p.getCurrentToken();
/*  174 */     if (t == JsonToken.VALUE_TRUE) return true; 
/*  175 */     if (t == JsonToken.VALUE_FALSE) return false; 
/*  176 */     if (t == JsonToken.VALUE_NULL) {
/*  177 */       _verifyNullForPrimitive(ctxt);
/*  178 */       return false;
/*      */     } 
/*      */ 
/*      */     
/*  182 */     if (t == JsonToken.VALUE_NUMBER_INT) {
/*  183 */       return _parseBooleanFromInt(p, ctxt);
/*      */     }
/*      */     
/*  186 */     if (t == JsonToken.VALUE_STRING) {
/*  187 */       String text = p.getText().trim();
/*      */       
/*  189 */       if ("true".equals(text) || "True".equals(text)) {
/*  190 */         return true;
/*      */       }
/*  192 */       if ("false".equals(text) || "False".equals(text)) {
/*  193 */         return false;
/*      */       }
/*  195 */       if (_isEmptyOrTextualNull(text)) {
/*  196 */         _verifyNullForPrimitiveCoercion(ctxt, text);
/*  197 */         return false;
/*      */       } 
/*  199 */       Boolean b = (Boolean)ctxt.handleWeirdStringValue(this._valueClass, text, "only \"true\" or \"false\" recognized", new Object[0]);
/*      */       
/*  201 */       return Boolean.TRUE.equals(b);
/*      */     } 
/*      */     
/*  204 */     if (t == JsonToken.START_ARRAY && ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS)) {
/*  205 */       p.nextToken();
/*  206 */       boolean parsed = _parseBooleanPrimitive(p, ctxt);
/*  207 */       _verifyEndArrayForSingle(p, ctxt);
/*  208 */       return parsed;
/*      */     } 
/*      */     
/*  211 */     return ((Boolean)ctxt.handleUnexpectedToken(this._valueClass, p)).booleanValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean _parseBooleanFromInt(JsonParser p, DeserializationContext ctxt) throws IOException {
/*  221 */     _verifyNumberForScalarCoercion(ctxt, p);
/*      */ 
/*      */     
/*  224 */     return !"0".equals(p.getText());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected final byte _parseBytePrimitive(JsonParser p, DeserializationContext ctxt) throws IOException {
/*  230 */     int value = _parseIntPrimitive(p, ctxt);
/*      */     
/*  232 */     if (_byteOverflow(value)) {
/*  233 */       Number v = (Number)ctxt.handleWeirdStringValue(this._valueClass, String.valueOf(value), "overflow, value cannot be represented as 8-bit value", new Object[0]);
/*      */       
/*  235 */       return _nonNullNumber(v).byteValue();
/*      */     } 
/*  237 */     return (byte)value;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected final short _parseShortPrimitive(JsonParser p, DeserializationContext ctxt) throws IOException {
/*  243 */     int value = _parseIntPrimitive(p, ctxt);
/*      */     
/*  245 */     if (_shortOverflow(value)) {
/*  246 */       Number v = (Number)ctxt.handleWeirdStringValue(this._valueClass, String.valueOf(value), "overflow, value cannot be represented as 16-bit value", new Object[0]);
/*      */       
/*  248 */       return _nonNullNumber(v).shortValue();
/*      */     } 
/*  250 */     return (short)value;
/*      */   }
/*      */ 
/*      */   
/*      */   protected final int _parseIntPrimitive(JsonParser p, DeserializationContext ctxt) throws IOException {
/*      */     String text;
/*  256 */     if (p.hasToken(JsonToken.VALUE_NUMBER_INT)) {
/*  257 */       return p.getIntValue();
/*      */     }
/*  259 */     switch (p.getCurrentTokenId()) {
/*      */       case 6:
/*  261 */         text = p.getText().trim();
/*  262 */         if (_isEmptyOrTextualNull(text)) {
/*  263 */           _verifyNullForPrimitiveCoercion(ctxt, text);
/*  264 */           return 0;
/*      */         } 
/*  266 */         return _parseIntPrimitive(ctxt, text);
/*      */       case 8:
/*  268 */         if (!ctxt.isEnabled(DeserializationFeature.ACCEPT_FLOAT_AS_INT)) {
/*  269 */           _failDoubleToIntCoercion(p, ctxt, "int");
/*      */         }
/*  271 */         return p.getValueAsInt();
/*      */       case 11:
/*  273 */         _verifyNullForPrimitive(ctxt);
/*  274 */         return 0;
/*      */       case 3:
/*  276 */         if (ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS)) {
/*  277 */           p.nextToken();
/*  278 */           int parsed = _parseIntPrimitive(p, ctxt);
/*  279 */           _verifyEndArrayForSingle(p, ctxt);
/*  280 */           return parsed;
/*      */         } 
/*      */         break;
/*      */     } 
/*      */ 
/*      */     
/*  286 */     return ((Number)ctxt.handleUnexpectedToken(this._valueClass, p)).intValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final int _parseIntPrimitive(DeserializationContext ctxt, String text) throws IOException {
/*      */     try {
/*  295 */       if (text.length() > 9) {
/*  296 */         long l = Long.parseLong(text);
/*  297 */         if (_intOverflow(l)) {
/*  298 */           Number v = (Number)ctxt.handleWeirdStringValue(this._valueClass, text, "Overflow: numeric value (%s) out of range of int (%d -%d)", new Object[] { text, 
/*      */                 
/*  300 */                 Integer.valueOf(-2147483648), Integer.valueOf(2147483647) });
/*  301 */           return _nonNullNumber(v).intValue();
/*      */         } 
/*  303 */         return (int)l;
/*      */       } 
/*  305 */       return NumberInput.parseInt(text);
/*  306 */     } catch (IllegalArgumentException iae) {
/*  307 */       Number v = (Number)ctxt.handleWeirdStringValue(this._valueClass, text, "not a valid int value", new Object[0]);
/*      */       
/*  309 */       return _nonNullNumber(v).intValue();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected final long _parseLongPrimitive(JsonParser p, DeserializationContext ctxt) throws IOException {
/*      */     String text;
/*  316 */     if (p.hasToken(JsonToken.VALUE_NUMBER_INT)) {
/*  317 */       return p.getLongValue();
/*      */     }
/*  319 */     switch (p.getCurrentTokenId()) {
/*      */       case 6:
/*  321 */         text = p.getText().trim();
/*  322 */         if (_isEmptyOrTextualNull(text)) {
/*  323 */           _verifyNullForPrimitiveCoercion(ctxt, text);
/*  324 */           return 0L;
/*      */         } 
/*  326 */         return _parseLongPrimitive(ctxt, text);
/*      */       case 8:
/*  328 */         if (!ctxt.isEnabled(DeserializationFeature.ACCEPT_FLOAT_AS_INT)) {
/*  329 */           _failDoubleToIntCoercion(p, ctxt, "long");
/*      */         }
/*  331 */         return p.getValueAsLong();
/*      */       case 11:
/*  333 */         _verifyNullForPrimitive(ctxt);
/*  334 */         return 0L;
/*      */       case 3:
/*  336 */         if (ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS)) {
/*  337 */           p.nextToken();
/*  338 */           long parsed = _parseLongPrimitive(p, ctxt);
/*  339 */           _verifyEndArrayForSingle(p, ctxt);
/*  340 */           return parsed;
/*      */         } 
/*      */         break;
/*      */     } 
/*  344 */     return ((Number)ctxt.handleUnexpectedToken(this._valueClass, p)).longValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final long _parseLongPrimitive(DeserializationContext ctxt, String text) throws IOException {
/*      */     try {
/*  353 */       return NumberInput.parseLong(text);
/*  354 */     } catch (IllegalArgumentException illegalArgumentException) {
/*      */       
/*  356 */       Number v = (Number)ctxt.handleWeirdStringValue(this._valueClass, text, "not a valid long value", new Object[0]);
/*      */       
/*  358 */       return _nonNullNumber(v).longValue();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected final float _parseFloatPrimitive(JsonParser p, DeserializationContext ctxt) throws IOException {
/*      */     String text;
/*  365 */     if (p.hasToken(JsonToken.VALUE_NUMBER_FLOAT)) {
/*  366 */       return p.getFloatValue();
/*      */     }
/*  368 */     switch (p.getCurrentTokenId()) {
/*      */       case 6:
/*  370 */         text = p.getText().trim();
/*  371 */         if (_isEmptyOrTextualNull(text)) {
/*  372 */           _verifyNullForPrimitiveCoercion(ctxt, text);
/*  373 */           return 0.0F;
/*      */         } 
/*  375 */         return _parseFloatPrimitive(ctxt, text);
/*      */       case 7:
/*  377 */         return p.getFloatValue();
/*      */       case 11:
/*  379 */         _verifyNullForPrimitive(ctxt);
/*  380 */         return 0.0F;
/*      */       case 3:
/*  382 */         if (ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS)) {
/*  383 */           p.nextToken();
/*  384 */           float parsed = _parseFloatPrimitive(p, ctxt);
/*  385 */           _verifyEndArrayForSingle(p, ctxt);
/*  386 */           return parsed;
/*      */         } 
/*      */         break;
/*      */     } 
/*      */     
/*  391 */     return ((Number)ctxt.handleUnexpectedToken(this._valueClass, p)).floatValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final float _parseFloatPrimitive(DeserializationContext ctxt, String text) throws IOException {
/*  400 */     switch (text.charAt(0)) {
/*      */       case 'I':
/*  402 */         if (_isPosInf(text)) {
/*  403 */           return Float.POSITIVE_INFINITY;
/*      */         }
/*      */         break;
/*      */       case 'N':
/*  407 */         if (_isNaN(text)) return Float.NaN; 
/*      */         break;
/*      */       case '-':
/*  410 */         if (_isNegInf(text)) {
/*  411 */           return Float.NEGATIVE_INFINITY;
/*      */         }
/*      */         break;
/*      */     } 
/*      */     try {
/*  416 */       return Float.parseFloat(text);
/*  417 */     } catch (IllegalArgumentException illegalArgumentException) {
/*  418 */       Number v = (Number)ctxt.handleWeirdStringValue(this._valueClass, text, "not a valid float value", new Object[0]);
/*      */       
/*  420 */       return _nonNullNumber(v).floatValue();
/*      */     } 
/*      */   }
/*      */   
/*      */   protected final double _parseDoublePrimitive(JsonParser p, DeserializationContext ctxt) throws IOException {
/*      */     String text;
/*  426 */     if (p.hasToken(JsonToken.VALUE_NUMBER_FLOAT)) {
/*  427 */       return p.getDoubleValue();
/*      */     }
/*  429 */     switch (p.getCurrentTokenId()) {
/*      */       case 6:
/*  431 */         text = p.getText().trim();
/*  432 */         if (_isEmptyOrTextualNull(text)) {
/*  433 */           _verifyNullForPrimitiveCoercion(ctxt, text);
/*  434 */           return 0.0D;
/*      */         } 
/*  436 */         return _parseDoublePrimitive(ctxt, text);
/*      */       case 7:
/*  438 */         return p.getDoubleValue();
/*      */       case 11:
/*  440 */         _verifyNullForPrimitive(ctxt);
/*  441 */         return 0.0D;
/*      */       case 3:
/*  443 */         if (ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS)) {
/*  444 */           p.nextToken();
/*  445 */           double parsed = _parseDoublePrimitive(p, ctxt);
/*  446 */           _verifyEndArrayForSingle(p, ctxt);
/*  447 */           return parsed;
/*      */         } 
/*      */         break;
/*      */     } 
/*      */     
/*  452 */     return ((Number)ctxt.handleUnexpectedToken(this._valueClass, p)).doubleValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final double _parseDoublePrimitive(DeserializationContext ctxt, String text) throws IOException {
/*  461 */     switch (text.charAt(0)) {
/*      */       case 'I':
/*  463 */         if (_isPosInf(text)) {
/*  464 */           return Double.POSITIVE_INFINITY;
/*      */         }
/*      */         break;
/*      */       case 'N':
/*  468 */         if (_isNaN(text)) {
/*  469 */           return Double.NaN;
/*      */         }
/*      */         break;
/*      */       case '-':
/*  473 */         if (_isNegInf(text)) {
/*  474 */           return Double.NEGATIVE_INFINITY;
/*      */         }
/*      */         break;
/*      */     } 
/*      */     try {
/*  479 */       return parseDouble(text);
/*  480 */     } catch (IllegalArgumentException illegalArgumentException) {
/*  481 */       Number v = (Number)ctxt.handleWeirdStringValue(this._valueClass, text, "not a valid double value (as String to convert)", new Object[0]);
/*      */       
/*  483 */       return _nonNullNumber(v).doubleValue();
/*      */     } 
/*      */   }
/*      */   
/*      */   protected Date _parseDate(JsonParser p, DeserializationContext ctxt) throws IOException {
/*      */     long ts;
/*  489 */     switch (p.getCurrentTokenId()) {
/*      */       case 6:
/*  491 */         return _parseDate(p.getText().trim(), ctxt);
/*      */ 
/*      */       
/*      */       case 7:
/*      */         try {
/*  496 */           ts = p.getLongValue();
/*      */         
/*      */         }
/*  499 */         catch (JsonParseException|com.fasterxml.jackson.core.exc.InputCoercionException e) {
/*  500 */           Number v = (Number)ctxt.handleWeirdNumberValue(this._valueClass, p.getNumberValue(), "not a valid 64-bit long for creating `java.util.Date`", new Object[0]);
/*      */           
/*  502 */           ts = v.longValue();
/*      */         } 
/*  504 */         return new Date(ts);
/*      */       
/*      */       case 11:
/*  507 */         return (Date)getNullValue(ctxt);
/*      */       case 3:
/*  509 */         return _parseDateFromArray(p, ctxt);
/*      */     } 
/*  511 */     return (Date)ctxt.handleUnexpectedToken(this._valueClass, p);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Date _parseDateFromArray(JsonParser p, DeserializationContext ctxt) throws IOException {
/*      */     JsonToken t;
/*  519 */     if (ctxt.hasSomeOfFeatures(F_MASK_ACCEPT_ARRAYS)) {
/*  520 */       t = p.nextToken();
/*  521 */       if (t == JsonToken.END_ARRAY && 
/*  522 */         ctxt.isEnabled(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT)) {
/*  523 */         return (Date)getNullValue(ctxt);
/*      */       }
/*      */       
/*  526 */       if (ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS)) {
/*  527 */         Date parsed = _parseDate(p, ctxt);
/*  528 */         _verifyEndArrayForSingle(p, ctxt);
/*  529 */         return parsed;
/*      */       } 
/*      */     } else {
/*  532 */       t = p.getCurrentToken();
/*      */     } 
/*  534 */     return (Date)ctxt.handleUnexpectedToken(this._valueClass, t, p, null, new Object[0]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Date _parseDate(String value, DeserializationContext ctxt) throws IOException {
/*      */     try {
/*  545 */       if (_isEmptyOrTextualNull(value)) {
/*  546 */         return (Date)getNullValue(ctxt);
/*      */       }
/*  548 */       return ctxt.parseDate(value);
/*  549 */     } catch (IllegalArgumentException iae) {
/*  550 */       return (Date)ctxt.handleWeirdStringValue(this._valueClass, value, "not a valid representation (error: %s)", new Object[] {
/*      */             
/*  552 */             ClassUtil.exceptionMessage(iae)
/*      */           });
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static final double parseDouble(String numStr) throws NumberFormatException {
/*  563 */     if ("2.2250738585072012e-308".equals(numStr)) {
/*  564 */       return 2.2250738585072014E-308D;
/*      */     }
/*  566 */     return Double.parseDouble(numStr);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final String _parseString(JsonParser p, DeserializationContext ctxt) throws IOException {
/*  577 */     JsonToken t = p.getCurrentToken();
/*  578 */     if (t == JsonToken.VALUE_STRING) {
/*  579 */       return p.getText();
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
/*  592 */     String value = p.getValueAsString();
/*  593 */     if (value != null) {
/*  594 */       return value;
/*      */     }
/*  596 */     return (String)ctxt.handleUnexpectedToken(String.class, p);
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
/*      */   protected T _deserializeFromEmpty(JsonParser p, DeserializationContext ctxt) throws IOException {
/*  609 */     JsonToken t = p.getCurrentToken();
/*  610 */     if (t == JsonToken.START_ARRAY) {
/*  611 */       if (ctxt.isEnabled(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT)) {
/*  612 */         t = p.nextToken();
/*  613 */         if (t == JsonToken.END_ARRAY) {
/*  614 */           return null;
/*      */         }
/*  616 */         return (T)ctxt.handleUnexpectedToken(handledType(), p);
/*      */       } 
/*  618 */     } else if (t == JsonToken.VALUE_STRING && 
/*  619 */       ctxt.isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)) {
/*  620 */       String str = p.getText().trim();
/*  621 */       if (str.isEmpty()) {
/*  622 */         return null;
/*      */       }
/*      */     } 
/*      */     
/*  626 */     return (T)ctxt.handleUnexpectedToken(handledType(), p);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean _hasTextualNull(String value) {
/*  637 */     return "null".equals(value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean _isEmptyOrTextualNull(String value) {
/*  644 */     return (value.isEmpty() || "null".equals(value));
/*      */   }
/*      */   
/*      */   protected final boolean _isNegInf(String text) {
/*  648 */     return ("-Infinity".equals(text) || "-INF".equals(text));
/*      */   }
/*      */   
/*      */   protected final boolean _isPosInf(String text) {
/*  652 */     return ("Infinity".equals(text) || "INF".equals(text));
/*      */   }
/*      */   protected final boolean _isNaN(String text) {
/*  655 */     return "NaN".equals(text);
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
/*      */   protected T _deserializeFromArray(JsonParser p, DeserializationContext ctxt) throws IOException {
/*  683 */     if (ctxt.hasSomeOfFeatures(F_MASK_ACCEPT_ARRAYS)) {
/*  684 */       JsonToken t = p.nextToken();
/*  685 */       if (t == JsonToken.END_ARRAY && 
/*  686 */         ctxt.isEnabled(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT)) {
/*  687 */         return (T)getNullValue(ctxt);
/*      */       }
/*      */       
/*  690 */       if (ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS)) {
/*  691 */         T parsed = (T)deserialize(p, ctxt);
/*  692 */         if (p.nextToken() != JsonToken.END_ARRAY) {
/*  693 */           handleMissingEndArrayForSingle(p, ctxt);
/*      */         }
/*  695 */         return parsed;
/*      */       } 
/*      */     } else {
/*  698 */       JsonToken t = p.getCurrentToken();
/*      */     } 
/*      */     
/*  701 */     T result = (T)ctxt.handleUnexpectedToken(getValueType(ctxt), p.getCurrentToken(), p, null, new Object[0]);
/*  702 */     return result;
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
/*      */   protected T _deserializeWrappedValue(JsonParser p, DeserializationContext ctxt) throws IOException {
/*  717 */     if (p.hasToken(JsonToken.START_ARRAY)) {
/*  718 */       String msg = String.format("Cannot deserialize instance of %s out of %s token: nested Arrays not allowed with %s", new Object[] {
/*      */             
/*  720 */             ClassUtil.nameOf(this._valueClass), JsonToken.START_ARRAY, "DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS"
/*      */           });
/*      */       
/*  723 */       T result = (T)ctxt.handleUnexpectedToken(getValueType(ctxt), p.getCurrentToken(), p, msg, new Object[0]);
/*  724 */       return result;
/*      */     } 
/*  726 */     return (T)deserialize(p, ctxt);
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
/*      */   protected void _failDoubleToIntCoercion(JsonParser p, DeserializationContext ctxt, String type) throws IOException {
/*  738 */     ctxt.reportInputMismatch(handledType(), "Cannot coerce a floating-point value ('%s') into %s (enable `DeserializationFeature.ACCEPT_FLOAT_AS_INT` to allow)", new Object[] { p
/*      */           
/*  740 */           .getValueAsString(), type });
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
/*      */   protected Object _coerceIntegral(JsonParser p, DeserializationContext ctxt) throws IOException {
/*  756 */     int feats = ctxt.getDeserializationFeatures();
/*  757 */     if (DeserializationFeature.USE_BIG_INTEGER_FOR_INTS.enabledIn(feats)) {
/*  758 */       return p.getBigIntegerValue();
/*      */     }
/*  760 */     if (DeserializationFeature.USE_LONG_FOR_INTS.enabledIn(feats)) {
/*  761 */       return Long.valueOf(p.getLongValue());
/*      */     }
/*  763 */     return p.getBigIntegerValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Object _coerceNullToken(DeserializationContext ctxt, boolean isPrimitive) throws JsonMappingException {
/*  774 */     if (isPrimitive) {
/*  775 */       _verifyNullForPrimitive(ctxt);
/*      */     }
/*  777 */     return getNullValue(ctxt);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Object _coerceTextualNull(DeserializationContext ctxt, boolean isPrimitive) throws JsonMappingException {
/*      */     DeserializationFeature deserializationFeature;
/*      */     boolean enable;
/*  790 */     if (!ctxt.isEnabled(MapperFeature.ALLOW_COERCION_OF_SCALARS)) {
/*  791 */       MapperFeature mapperFeature = MapperFeature.ALLOW_COERCION_OF_SCALARS;
/*  792 */       enable = true;
/*  793 */     } else if (isPrimitive && ctxt.isEnabled(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)) {
/*  794 */       deserializationFeature = DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES;
/*  795 */       enable = false;
/*      */     } else {
/*  797 */       return getNullValue(ctxt);
/*      */     } 
/*  799 */     _reportFailedNullCoerce(ctxt, enable, (Enum<?>)deserializationFeature, "String \"null\"");
/*  800 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Object _coerceEmptyString(DeserializationContext ctxt, boolean isPrimitive) throws JsonMappingException {
/*      */     DeserializationFeature deserializationFeature;
/*      */     boolean enable;
/*  813 */     if (!ctxt.isEnabled(MapperFeature.ALLOW_COERCION_OF_SCALARS)) {
/*  814 */       MapperFeature mapperFeature = MapperFeature.ALLOW_COERCION_OF_SCALARS;
/*  815 */       enable = true;
/*  816 */     } else if (isPrimitive && ctxt.isEnabled(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)) {
/*  817 */       deserializationFeature = DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES;
/*  818 */       enable = false;
/*      */     } else {
/*  820 */       return getNullValue(ctxt);
/*      */     } 
/*  822 */     _reportFailedNullCoerce(ctxt, enable, (Enum<?>)deserializationFeature, "empty String (\"\")");
/*  823 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected final void _verifyNullForPrimitive(DeserializationContext ctxt) throws JsonMappingException {
/*  829 */     if (ctxt.isEnabled(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)) {
/*  830 */       ctxt.reportInputMismatch(this, "Cannot coerce `null` %s (disable `DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES` to allow)", new Object[] {
/*      */             
/*  832 */             _coercedTypeDesc()
/*      */           });
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final void _verifyNullForPrimitiveCoercion(DeserializationContext ctxt, String str) throws JsonMappingException {
/*      */     DeserializationFeature deserializationFeature;
/*      */     boolean enable;
/*  843 */     if (!ctxt.isEnabled(MapperFeature.ALLOW_COERCION_OF_SCALARS)) {
/*  844 */       MapperFeature mapperFeature = MapperFeature.ALLOW_COERCION_OF_SCALARS;
/*  845 */       enable = true;
/*  846 */     } else if (ctxt.isEnabled(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)) {
/*  847 */       deserializationFeature = DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES;
/*  848 */       enable = false;
/*      */     } else {
/*      */       return;
/*      */     } 
/*  852 */     String strDesc = str.isEmpty() ? "empty String (\"\")" : String.format("String \"%s\"", new Object[] { str });
/*  853 */     _reportFailedNullCoerce(ctxt, enable, (Enum<?>)deserializationFeature, strDesc);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final void _verifyNullForScalarCoercion(DeserializationContext ctxt, String str) throws JsonMappingException {
/*  860 */     if (!ctxt.isEnabled(MapperFeature.ALLOW_COERCION_OF_SCALARS)) {
/*  861 */       String strDesc = str.isEmpty() ? "empty String (\"\")" : String.format("String \"%s\"", new Object[] { str });
/*  862 */       _reportFailedNullCoerce(ctxt, true, (Enum<?>)MapperFeature.ALLOW_COERCION_OF_SCALARS, strDesc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void _verifyStringForScalarCoercion(DeserializationContext ctxt, String str) throws JsonMappingException {
/*  869 */     MapperFeature feat = MapperFeature.ALLOW_COERCION_OF_SCALARS;
/*  870 */     if (!ctxt.isEnabled(feat)) {
/*  871 */       ctxt.reportInputMismatch(this, "Cannot coerce String \"%s\" %s (enable `%s.%s` to allow)", new Object[] { str, 
/*  872 */             _coercedTypeDesc(), feat.getClass().getSimpleName(), feat.name() });
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void _verifyNumberForScalarCoercion(DeserializationContext ctxt, JsonParser p) throws IOException {
/*  879 */     MapperFeature feat = MapperFeature.ALLOW_COERCION_OF_SCALARS;
/*  880 */     if (!ctxt.isEnabled(feat)) {
/*      */ 
/*      */       
/*  883 */       String valueDesc = p.getText();
/*  884 */       ctxt.reportInputMismatch(this, "Cannot coerce Number (%s) %s (enable `%s.%s` to allow)", new Object[] { valueDesc, 
/*  885 */             _coercedTypeDesc(), feat.getClass().getSimpleName(), feat.name() });
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void _reportFailedNullCoerce(DeserializationContext ctxt, boolean state, Enum<?> feature, String inputDesc) throws JsonMappingException {
/*  892 */     String enableDesc = state ? "enable" : "disable";
/*  893 */     ctxt.reportInputMismatch(this, "Cannot coerce %s to Null value %s (%s `%s.%s` to allow)", new Object[] { inputDesc, 
/*  894 */           _coercedTypeDesc(), enableDesc, feature.getClass().getSimpleName(), feature.name() });
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
/*      */   protected String _coercedTypeDesc() {
/*      */     boolean structured;
/*      */     String typeDesc;
/*  910 */     JavaType t = getValueType();
/*  911 */     if (t != null && !t.isPrimitive()) {
/*  912 */       structured = (t.isContainerType() || t.isReferenceType());
/*      */       
/*  914 */       typeDesc = "'" + t.toString() + "'";
/*      */     } else {
/*  916 */       Class<?> cls = handledType();
/*      */       
/*  918 */       structured = (cls.isArray() || Collection.class.isAssignableFrom(cls) || Map.class.isAssignableFrom(cls));
/*  919 */       typeDesc = ClassUtil.nameOf(cls);
/*      */     } 
/*  921 */     if (structured) {
/*  922 */       return "as content of type " + typeDesc;
/*      */     }
/*  924 */     return "for type " + typeDesc;
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
/*      */   protected JsonDeserializer<Object> findDeserializer(DeserializationContext ctxt, JavaType type, BeanProperty property) throws JsonMappingException {
/*  946 */     return ctxt.findContextualValueDeserializer(type, property);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final boolean _isIntNumber(String text) {
/*  955 */     int len = text.length();
/*  956 */     if (len > 0) {
/*  957 */       char c = text.charAt(0);
/*      */       
/*  959 */       int i = (c == '-' || c == '+') ? 1 : 0;
/*  960 */       for (; i < len; i++) {
/*  961 */         int ch = text.charAt(i);
/*  962 */         if (ch > 57 || ch < 48) {
/*  963 */           return false;
/*      */         }
/*      */       } 
/*  966 */       return true;
/*      */     } 
/*  968 */     return false;
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
/*      */   protected JsonDeserializer<?> findConvertingContentDeserializer(DeserializationContext ctxt, BeanProperty prop, JsonDeserializer<?> existingDeserializer) throws JsonMappingException {
/*  991 */     AnnotationIntrospector intr = ctxt.getAnnotationIntrospector();
/*  992 */     if (_neitherNull(intr, prop)) {
/*  993 */       AnnotatedMember member = prop.getMember();
/*  994 */       if (member != null) {
/*  995 */         Object convDef = intr.findDeserializationContentConverter(member);
/*  996 */         if (convDef != null) {
/*  997 */           Converter<Object, Object> conv = ctxt.converterInstance((Annotated)prop.getMember(), convDef);
/*  998 */           JavaType delegateType = conv.getInputType(ctxt.getTypeFactory());
/*  999 */           if (existingDeserializer == null) {
/* 1000 */             existingDeserializer = ctxt.findContextualValueDeserializer(delegateType, prop);
/*      */           }
/* 1002 */           return new StdDelegatingDeserializer(conv, delegateType, existingDeserializer);
/*      */         } 
/*      */       } 
/*      */     } 
/* 1006 */     return existingDeserializer;
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
/*      */   protected JsonFormat.Value findFormatOverrides(DeserializationContext ctxt, BeanProperty prop, Class<?> typeForDefaults) {
/* 1027 */     if (prop != null) {
/* 1028 */       return prop.findPropertyFormat((MapperConfig)ctxt.getConfig(), typeForDefaults);
/*      */     }
/*      */     
/* 1031 */     return ctxt.getDefaultPropertyFormat(typeForDefaults);
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
/*      */   protected Boolean findFormatFeature(DeserializationContext ctxt, BeanProperty prop, Class<?> typeForDefaults, JsonFormat.Feature feat) {
/* 1047 */     JsonFormat.Value format = findFormatOverrides(ctxt, prop, typeForDefaults);
/* 1048 */     if (format != null) {
/* 1049 */       return format.getFeature(feat);
/*      */     }
/* 1051 */     return null;
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
/*      */   protected final NullValueProvider findValueNullProvider(DeserializationContext ctxt, SettableBeanProperty prop, PropertyMetadata propMetadata) throws JsonMappingException {
/* 1065 */     if (prop != null) {
/* 1066 */       return _findNullProvider(ctxt, (BeanProperty)prop, propMetadata.getValueNulls(), prop
/* 1067 */           .getValueDeserializer());
/*      */     }
/* 1069 */     return null;
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
/*      */   protected NullValueProvider findContentNullProvider(DeserializationContext ctxt, BeanProperty prop, JsonDeserializer<?> valueDeser) throws JsonMappingException {
/* 1084 */     Nulls nulls = findContentNullStyle(ctxt, prop);
/* 1085 */     if (nulls == Nulls.SKIP) {
/* 1086 */       return (NullValueProvider)NullsConstantProvider.skipper();
/*      */     }
/* 1088 */     NullValueProvider prov = _findNullProvider(ctxt, prop, nulls, valueDeser);
/* 1089 */     if (prov != null) {
/* 1090 */       return prov;
/*      */     }
/* 1092 */     return (NullValueProvider)valueDeser;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected Nulls findContentNullStyle(DeserializationContext ctxt, BeanProperty prop) throws JsonMappingException {
/* 1098 */     if (prop != null) {
/* 1099 */       return prop.getMetadata().getContentNulls();
/*      */     }
/* 1101 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final NullValueProvider _findNullProvider(DeserializationContext ctxt, BeanProperty prop, Nulls nulls, JsonDeserializer<?> valueDeser) throws JsonMappingException {
/* 1109 */     if (nulls == Nulls.FAIL) {
/* 1110 */       if (prop == null) {
/* 1111 */         return (NullValueProvider)NullsFailProvider.constructForRootValue(ctxt.constructType(valueDeser.handledType()));
/*      */       }
/* 1113 */       return (NullValueProvider)NullsFailProvider.constructForProperty(prop);
/*      */     } 
/* 1115 */     if (nulls == Nulls.AS_EMPTY) {
/*      */ 
/*      */       
/* 1118 */       if (valueDeser == null) {
/* 1119 */         return null;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1125 */       if (valueDeser instanceof BeanDeserializerBase) {
/* 1126 */         ValueInstantiator vi = ((BeanDeserializerBase)valueDeser).getValueInstantiator();
/* 1127 */         if (!vi.canCreateUsingDefault()) {
/* 1128 */           JavaType type = prop.getType();
/* 1129 */           ctxt.reportBadDefinition(type, 
/* 1130 */               String.format("Cannot create empty instance of %s, no default Creator", new Object[] { type }));
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/* 1135 */       AccessPattern access = valueDeser.getEmptyAccessPattern();
/* 1136 */       if (access == AccessPattern.ALWAYS_NULL) {
/* 1137 */         return (NullValueProvider)NullsConstantProvider.nuller();
/*      */       }
/* 1139 */       if (access == AccessPattern.CONSTANT) {
/* 1140 */         return (NullValueProvider)NullsConstantProvider.forValue(valueDeser.getEmptyValue(ctxt));
/*      */       }
/*      */       
/* 1143 */       return (NullValueProvider)new NullsAsEmptyProvider(valueDeser);
/*      */     } 
/* 1145 */     if (nulls == Nulls.SKIP) {
/* 1146 */       return (NullValueProvider)NullsConstantProvider.skipper();
/*      */     }
/* 1148 */     return null;
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
/*      */   protected void handleUnknownProperty(JsonParser p, DeserializationContext ctxt, Object<?> instanceOrClass, String propName) throws IOException {
/* 1175 */     if (instanceOrClass == null) {
/* 1176 */       instanceOrClass = (Object<?>)handledType();
/*      */     }
/*      */     
/* 1179 */     if (ctxt.handleUnknownProperty(p, this, instanceOrClass, propName)) {
/*      */       return;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1185 */     p.skipChildren();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void handleMissingEndArrayForSingle(JsonParser p, DeserializationContext ctxt) throws IOException {
/* 1191 */     ctxt.reportWrongTokenException(this, JsonToken.END_ARRAY, "Attempted to unwrap '%s' value from an array (with `DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS`) but it contains more than one value", new Object[] {
/*      */           
/* 1193 */           handledType().getName()
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void _verifyEndArrayForSingle(JsonParser p, DeserializationContext ctxt) throws IOException {
/* 1200 */     JsonToken t = p.nextToken();
/* 1201 */     if (t != JsonToken.END_ARRAY) {
/* 1202 */       handleMissingEndArrayForSingle(p, ctxt);
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
/*      */   protected static final boolean _neitherNull(Object a, Object b) {
/* 1216 */     return (a != null && b != null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final boolean _byteOverflow(int value) {
/* 1225 */     return (value < -128 || value > 255);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final boolean _shortOverflow(int value) {
/* 1232 */     return (value < -32768 || value > 32767);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final boolean _intOverflow(long value) {
/* 1239 */     return (value < -2147483648L || value > 2147483647L);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Number _nonNullNumber(Number n) {
/* 1246 */     if (n == null) {
/* 1247 */       n = Integer.valueOf(0);
/*      */     }
/* 1249 */     return n;
/*      */   }
/*      */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\deser\std\StdDeserializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */