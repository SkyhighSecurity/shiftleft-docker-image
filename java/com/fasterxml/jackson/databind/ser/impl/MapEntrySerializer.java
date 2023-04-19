/*     */ package com.fasterxml.jackson.databind.ser.impl;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonInclude;
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.core.type.WritableTypeId;
/*     */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
/*     */ import com.fasterxml.jackson.databind.cfg.MapperConfig;
/*     */ import com.fasterxml.jackson.databind.introspect.Annotated;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*     */ import com.fasterxml.jackson.databind.ser.ContainerSerializer;
/*     */ import com.fasterxml.jackson.databind.ser.ContextualSerializer;
/*     */ import com.fasterxml.jackson.databind.util.ArrayBuilders;
/*     */ import com.fasterxml.jackson.databind.util.BeanUtil;
/*     */ import java.io.IOException;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @JacksonStdImpl
/*     */ public class MapEntrySerializer
/*     */   extends ContainerSerializer<Map.Entry<?, ?>>
/*     */   implements ContextualSerializer
/*     */ {
/*  33 */   public static final Object MARKER_FOR_EMPTY = JsonInclude.Include.NON_EMPTY;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final BeanProperty _property;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final boolean _valueTypeIsStatic;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final JavaType _entryType;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final JavaType _keyType;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final JavaType _valueType;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected JsonSerializer<Object> _keySerializer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected JsonSerializer<Object> _valueSerializer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final TypeSerializer _valueTypeSerializer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected PropertySerializerMap _dynamicValueSerializers;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final Object _suppressableValue;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final boolean _suppressNulls;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MapEntrySerializer(JavaType type, JavaType keyType, JavaType valueType, boolean staticTyping, TypeSerializer vts, BeanProperty property) {
/* 111 */     super(type);
/* 112 */     this._entryType = type;
/* 113 */     this._keyType = keyType;
/* 114 */     this._valueType = valueType;
/* 115 */     this._valueTypeIsStatic = staticTyping;
/* 116 */     this._valueTypeSerializer = vts;
/* 117 */     this._property = property;
/* 118 */     this._dynamicValueSerializers = PropertySerializerMap.emptyForProperties();
/* 119 */     this._suppressableValue = null;
/* 120 */     this._suppressNulls = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected MapEntrySerializer(MapEntrySerializer src, BeanProperty property, TypeSerializer vts, JsonSerializer<?> keySer, JsonSerializer<?> valueSer) {
/* 128 */     this(src, property, vts, keySer, valueSer, src._suppressableValue, src._suppressNulls);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected MapEntrySerializer(MapEntrySerializer src, BeanProperty property, TypeSerializer vts, JsonSerializer<?> keySer, JsonSerializer<?> valueSer, Object suppressableValue, boolean suppressNulls) {
/* 138 */     super(Map.class, false);
/* 139 */     this._entryType = src._entryType;
/* 140 */     this._keyType = src._keyType;
/* 141 */     this._valueType = src._valueType;
/* 142 */     this._valueTypeIsStatic = src._valueTypeIsStatic;
/* 143 */     this._valueTypeSerializer = src._valueTypeSerializer;
/* 144 */     this._keySerializer = (JsonSerializer)keySer;
/* 145 */     this._valueSerializer = (JsonSerializer)valueSer;
/*     */     
/* 147 */     this._dynamicValueSerializers = PropertySerializerMap.emptyForProperties();
/* 148 */     this._property = src._property;
/* 149 */     this._suppressableValue = suppressableValue;
/* 150 */     this._suppressNulls = suppressNulls;
/*     */   }
/*     */ 
/*     */   
/*     */   public ContainerSerializer<?> _withValueTypeSerializer(TypeSerializer vts) {
/* 155 */     return new MapEntrySerializer(this, this._property, vts, this._keySerializer, this._valueSerializer, this._suppressableValue, this._suppressNulls);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MapEntrySerializer withResolved(BeanProperty property, JsonSerializer<?> keySerializer, JsonSerializer<?> valueSerializer, Object suppressableValue, boolean suppressNulls) {
/* 165 */     return new MapEntrySerializer(this, property, this._valueTypeSerializer, keySerializer, valueSerializer, suppressableValue, suppressNulls);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MapEntrySerializer withContentInclusion(Object suppressableValue, boolean suppressNulls) {
/* 174 */     if (this._suppressableValue == suppressableValue && this._suppressNulls == suppressNulls)
/*     */     {
/* 176 */       return this;
/*     */     }
/* 178 */     return new MapEntrySerializer(this, this._property, this._valueTypeSerializer, this._keySerializer, this._valueSerializer, suppressableValue, suppressNulls);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonSerializer<?> createContextual(SerializerProvider provider, BeanProperty property) throws JsonMappingException {
/* 186 */     JsonSerializer<?> ser = null;
/* 187 */     JsonSerializer<?> keySer = null;
/* 188 */     AnnotationIntrospector intr = provider.getAnnotationIntrospector();
/* 189 */     AnnotatedMember propertyAcc = (property == null) ? null : property.getMember();
/*     */ 
/*     */     
/* 192 */     if (propertyAcc != null && intr != null) {
/* 193 */       Object serDef = intr.findKeySerializer((Annotated)propertyAcc);
/* 194 */       if (serDef != null) {
/* 195 */         keySer = provider.serializerInstance((Annotated)propertyAcc, serDef);
/*     */       }
/* 197 */       serDef = intr.findContentSerializer((Annotated)propertyAcc);
/* 198 */       if (serDef != null) {
/* 199 */         ser = provider.serializerInstance((Annotated)propertyAcc, serDef);
/*     */       }
/*     */     } 
/* 202 */     if (ser == null) {
/* 203 */       ser = this._valueSerializer;
/*     */     }
/*     */     
/* 206 */     ser = findContextualConvertingSerializer(provider, property, ser);
/* 207 */     if (ser == null)
/*     */     {
/*     */ 
/*     */       
/* 211 */       if (this._valueTypeIsStatic && !this._valueType.isJavaLangObject()) {
/* 212 */         ser = provider.findValueSerializer(this._valueType, property);
/*     */       }
/*     */     }
/* 215 */     if (keySer == null) {
/* 216 */       keySer = this._keySerializer;
/*     */     }
/* 218 */     if (keySer == null) {
/* 219 */       keySer = provider.findKeySerializer(this._keyType, property);
/*     */     } else {
/* 221 */       keySer = provider.handleSecondaryContextualization(keySer, property);
/*     */     } 
/*     */     
/* 224 */     Object valueToSuppress = this._suppressableValue;
/* 225 */     boolean suppressNulls = this._suppressNulls;
/* 226 */     if (property != null) {
/* 227 */       JsonInclude.Value inclV = property.findPropertyInclusion((MapperConfig)provider.getConfig(), null);
/* 228 */       if (inclV != null) {
/* 229 */         JsonInclude.Include incl = inclV.getContentInclusion();
/* 230 */         if (incl != JsonInclude.Include.USE_DEFAULTS) {
/* 231 */           MapEntrySerializer mser; switch (incl) {
/*     */             case NON_DEFAULT:
/* 233 */               valueToSuppress = BeanUtil.getDefaultValue(this._valueType);
/* 234 */               suppressNulls = true;
/* 235 */               if (valueToSuppress != null && 
/* 236 */                 valueToSuppress.getClass().isArray()) {
/* 237 */                 valueToSuppress = ArrayBuilders.getArrayComparator(valueToSuppress);
/*     */               }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 273 */               mser = withResolved(property, keySer, ser, valueToSuppress, suppressNulls);
/*     */ 
/*     */               
/* 276 */               return (JsonSerializer<?>)mser;case NON_ABSENT: suppressNulls = true; valueToSuppress = this._valueType.isReferenceType() ? MARKER_FOR_EMPTY : null; mser = withResolved(property, keySer, ser, valueToSuppress, suppressNulls); return (JsonSerializer<?>)mser;case NON_EMPTY: suppressNulls = true; valueToSuppress = MARKER_FOR_EMPTY; mser = withResolved(property, keySer, ser, valueToSuppress, suppressNulls); return (JsonSerializer<?>)mser;case CUSTOM: valueToSuppress = provider.includeFilterInstance(null, mser.getContentFilter()); if (valueToSuppress == null) { suppressNulls = true; } else { suppressNulls = provider.includeFilterSuppressNulls(valueToSuppress); }  mser = withResolved(property, keySer, ser, valueToSuppress, suppressNulls); return (JsonSerializer<?>)mser;case NON_NULL: valueToSuppress = null; suppressNulls = true; mser = withResolved(property, keySer, ser, valueToSuppress, suppressNulls); return (JsonSerializer<?>)mser;
/*     */           } 
/*     */           valueToSuppress = null;
/*     */           suppressNulls = false;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     return (JsonSerializer<?>)withResolved(property, keySer, ser, valueToSuppress, suppressNulls);
/*     */   }
/*     */   
/*     */   public JavaType getContentType() {
/* 287 */     return this._valueType;
/*     */   }
/*     */ 
/*     */   
/*     */   public JsonSerializer<?> getContentSerializer() {
/* 292 */     return this._valueSerializer;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasSingleElement(Map.Entry<?, ?> value) {
/* 297 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty(SerializerProvider prov, Map.Entry<?, ?> entry) {
/* 303 */     Object value = entry.getValue();
/* 304 */     if (value == null) {
/* 305 */       return this._suppressNulls;
/*     */     }
/* 307 */     if (this._suppressableValue == null) {
/* 308 */       return false;
/*     */     }
/* 310 */     JsonSerializer<Object> valueSer = this._valueSerializer;
/* 311 */     if (valueSer == null) {
/*     */ 
/*     */       
/* 314 */       Class<?> cc = value.getClass();
/* 315 */       valueSer = this._dynamicValueSerializers.serializerFor(cc);
/* 316 */       if (valueSer == null) {
/*     */         try {
/* 318 */           valueSer = _findAndAddDynamic(this._dynamicValueSerializers, cc, prov);
/* 319 */         } catch (JsonMappingException e) {
/* 320 */           return false;
/*     */         } 
/*     */       }
/*     */     } 
/* 324 */     if (this._suppressableValue == MARKER_FOR_EMPTY) {
/* 325 */       return valueSer.isEmpty(prov, value);
/*     */     }
/* 327 */     return this._suppressableValue.equals(value);
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
/*     */   public void serialize(Map.Entry<?, ?> value, JsonGenerator gen, SerializerProvider provider) throws IOException {
/* 340 */     gen.writeStartObject(value);
/* 341 */     serializeDynamic(value, gen, provider);
/* 342 */     gen.writeEndObject();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void serializeWithType(Map.Entry<?, ?> value, JsonGenerator g, SerializerProvider provider, TypeSerializer typeSer) throws IOException {
/* 350 */     g.setCurrentValue(value);
/* 351 */     WritableTypeId typeIdDef = typeSer.writeTypePrefix(g, typeSer
/* 352 */         .typeId(value, JsonToken.START_OBJECT));
/* 353 */     serializeDynamic(value, g, provider);
/* 354 */     typeSer.writeTypeSuffix(g, typeIdDef);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void serializeDynamic(Map.Entry<?, ?> value, JsonGenerator gen, SerializerProvider provider) throws IOException {
/*     */     JsonSerializer<Object> keySerializer, valueSer;
/* 361 */     TypeSerializer vts = this._valueTypeSerializer;
/* 362 */     Object keyElem = value.getKey();
/*     */ 
/*     */     
/* 365 */     if (keyElem == null) {
/* 366 */       keySerializer = provider.findNullKeySerializer(this._keyType, this._property);
/*     */     } else {
/* 368 */       keySerializer = this._keySerializer;
/*     */     } 
/*     */     
/* 371 */     Object valueElem = value.getValue();
/*     */ 
/*     */     
/* 374 */     if (valueElem == null) {
/* 375 */       if (this._suppressNulls) {
/*     */         return;
/*     */       }
/* 378 */       valueSer = provider.getDefaultNullValueSerializer();
/*     */     } else {
/* 380 */       valueSer = this._valueSerializer;
/* 381 */       if (valueSer == null) {
/* 382 */         Class<?> cc = valueElem.getClass();
/* 383 */         valueSer = this._dynamicValueSerializers.serializerFor(cc);
/* 384 */         if (valueSer == null) {
/* 385 */           if (this._valueType.hasGenericTypes()) {
/* 386 */             valueSer = _findAndAddDynamic(this._dynamicValueSerializers, provider
/* 387 */                 .constructSpecializedType(this._valueType, cc), provider);
/*     */           } else {
/* 389 */             valueSer = _findAndAddDynamic(this._dynamicValueSerializers, cc, provider);
/*     */           } 
/*     */         }
/*     */       } 
/*     */       
/* 394 */       if (this._suppressableValue != null) {
/* 395 */         if (this._suppressableValue == MARKER_FOR_EMPTY && 
/* 396 */           valueSer.isEmpty(provider, valueElem)) {
/*     */           return;
/*     */         }
/* 399 */         if (this._suppressableValue.equals(valueElem)) {
/*     */           return;
/*     */         }
/*     */       } 
/*     */     } 
/* 404 */     keySerializer.serialize(keyElem, gen, provider);
/*     */     try {
/* 406 */       if (vts == null) {
/* 407 */         valueSer.serialize(valueElem, gen, provider);
/*     */       } else {
/* 409 */         valueSer.serializeWithType(valueElem, gen, provider, vts);
/*     */       } 
/* 411 */     } catch (Exception e) {
/* 412 */       String keyDesc = "" + keyElem;
/* 413 */       wrapAndThrow(provider, e, value, keyDesc);
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
/*     */   protected final JsonSerializer<Object> _findAndAddDynamic(PropertySerializerMap map, Class<?> type, SerializerProvider provider) throws JsonMappingException {
/* 426 */     PropertySerializerMap.SerializerAndMapResult result = map.findAndAddSecondarySerializer(type, provider, this._property);
/* 427 */     if (map != result.map) {
/* 428 */       this._dynamicValueSerializers = result.map;
/*     */     }
/* 430 */     return result.serializer;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected final JsonSerializer<Object> _findAndAddDynamic(PropertySerializerMap map, JavaType type, SerializerProvider provider) throws JsonMappingException {
/* 436 */     PropertySerializerMap.SerializerAndMapResult result = map.findAndAddSecondarySerializer(type, provider, this._property);
/* 437 */     if (map != result.map) {
/* 438 */       this._dynamicValueSerializers = result.map;
/*     */     }
/* 440 */     return result.serializer;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\ser\impl\MapEntrySerializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */