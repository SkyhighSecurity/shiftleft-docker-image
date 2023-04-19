/*      */ package com.fasterxml.jackson.databind.ser.std;
/*      */ import com.fasterxml.jackson.annotation.JsonFormat;
/*      */ import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/*      */ import com.fasterxml.jackson.annotation.JsonInclude;
/*      */ import com.fasterxml.jackson.core.JsonGenerator;
/*      */ import com.fasterxml.jackson.core.JsonToken;
/*      */ import com.fasterxml.jackson.core.type.WritableTypeId;
/*      */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*      */ import com.fasterxml.jackson.databind.BeanProperty;
/*      */ import com.fasterxml.jackson.databind.JavaType;
/*      */ import com.fasterxml.jackson.databind.JsonMappingException;
/*      */ import com.fasterxml.jackson.databind.JsonNode;
/*      */ import com.fasterxml.jackson.databind.JsonSerializer;
/*      */ import com.fasterxml.jackson.databind.SerializationFeature;
/*      */ import com.fasterxml.jackson.databind.SerializerProvider;
/*      */ import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
/*      */ import com.fasterxml.jackson.databind.cfg.MapperConfig;
/*      */ import com.fasterxml.jackson.databind.introspect.Annotated;
/*      */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*      */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitable;
/*      */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
/*      */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonMapFormatVisitor;
/*      */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*      */ import com.fasterxml.jackson.databind.ser.ContainerSerializer;
/*      */ import com.fasterxml.jackson.databind.ser.ContextualSerializer;
/*      */ import com.fasterxml.jackson.databind.ser.PropertyFilter;
/*      */ import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;
/*      */ import com.fasterxml.jackson.databind.type.TypeFactory;
/*      */ import com.fasterxml.jackson.databind.util.ArrayBuilders;
/*      */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*      */ import java.io.IOException;
/*      */ import java.lang.reflect.Type;
/*      */ import java.util.HashSet;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.TreeMap;
/*      */ 
/*      */ @JacksonStdImpl
/*      */ public class MapSerializer extends ContainerSerializer<Map<?, ?>> implements ContextualSerializer {
/*   40 */   protected static final JavaType UNSPECIFIED_TYPE = TypeFactory.unknownType();
/*      */ 
/*      */   
/*      */   private static final long serialVersionUID = 1L;
/*      */   
/*   45 */   public static final Object MARKER_FOR_EMPTY = JsonInclude.Include.NON_EMPTY;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final BeanProperty _property;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final boolean _valueTypeIsStatic;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final JavaType _keyType;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final JavaType _valueType;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected JsonSerializer<Object> _keySerializer;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected JsonSerializer<Object> _valueSerializer;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final TypeSerializer _valueTypeSerializer;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected PropertySerializerMap _dynamicValueSerializers;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final Set<String> _ignoredEntries;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final Object _filterId;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final Object _suppressableValue;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final boolean _suppressNulls;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final boolean _sortKeys;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected MapSerializer(Set<String> ignoredEntries, JavaType keyType, JavaType valueType, boolean valueTypeIsStatic, TypeSerializer vts, JsonSerializer<?> keySerializer, JsonSerializer<?> valueSerializer) {
/*  168 */     super(Map.class, false);
/*  169 */     this._ignoredEntries = (ignoredEntries == null || ignoredEntries.isEmpty()) ? null : ignoredEntries;
/*      */     
/*  171 */     this._keyType = keyType;
/*  172 */     this._valueType = valueType;
/*  173 */     this._valueTypeIsStatic = valueTypeIsStatic;
/*  174 */     this._valueTypeSerializer = vts;
/*  175 */     this._keySerializer = (JsonSerializer)keySerializer;
/*  176 */     this._valueSerializer = (JsonSerializer)valueSerializer;
/*  177 */     this._dynamicValueSerializers = PropertySerializerMap.emptyForProperties();
/*  178 */     this._property = null;
/*  179 */     this._filterId = null;
/*  180 */     this._sortKeys = false;
/*  181 */     this._suppressableValue = null;
/*  182 */     this._suppressNulls = false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected MapSerializer(MapSerializer src, BeanProperty property, JsonSerializer<?> keySerializer, JsonSerializer<?> valueSerializer, Set<String> ignoredEntries) {
/*  190 */     super(Map.class, false);
/*  191 */     this._ignoredEntries = (ignoredEntries == null || ignoredEntries.isEmpty()) ? null : ignoredEntries;
/*      */     
/*  193 */     this._keyType = src._keyType;
/*  194 */     this._valueType = src._valueType;
/*  195 */     this._valueTypeIsStatic = src._valueTypeIsStatic;
/*  196 */     this._valueTypeSerializer = src._valueTypeSerializer;
/*  197 */     this._keySerializer = (JsonSerializer)keySerializer;
/*  198 */     this._valueSerializer = (JsonSerializer)valueSerializer;
/*      */     
/*  200 */     this._dynamicValueSerializers = PropertySerializerMap.emptyForProperties();
/*  201 */     this._property = property;
/*  202 */     this._filterId = src._filterId;
/*  203 */     this._sortKeys = src._sortKeys;
/*  204 */     this._suppressableValue = src._suppressableValue;
/*  205 */     this._suppressNulls = src._suppressNulls;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected MapSerializer(MapSerializer src, TypeSerializer vts, Object suppressableValue, boolean suppressNulls) {
/*  214 */     super(Map.class, false);
/*  215 */     this._ignoredEntries = src._ignoredEntries;
/*  216 */     this._keyType = src._keyType;
/*  217 */     this._valueType = src._valueType;
/*  218 */     this._valueTypeIsStatic = src._valueTypeIsStatic;
/*  219 */     this._valueTypeSerializer = vts;
/*  220 */     this._keySerializer = src._keySerializer;
/*  221 */     this._valueSerializer = src._valueSerializer;
/*      */ 
/*      */     
/*  224 */     this._dynamicValueSerializers = src._dynamicValueSerializers;
/*  225 */     this._property = src._property;
/*  226 */     this._filterId = src._filterId;
/*  227 */     this._sortKeys = src._sortKeys;
/*  228 */     this._suppressableValue = suppressableValue;
/*  229 */     this._suppressNulls = suppressNulls;
/*      */   }
/*      */ 
/*      */   
/*      */   protected MapSerializer(MapSerializer src, Object filterId, boolean sortKeys) {
/*  234 */     super(Map.class, false);
/*  235 */     this._ignoredEntries = src._ignoredEntries;
/*  236 */     this._keyType = src._keyType;
/*  237 */     this._valueType = src._valueType;
/*  238 */     this._valueTypeIsStatic = src._valueTypeIsStatic;
/*  239 */     this._valueTypeSerializer = src._valueTypeSerializer;
/*  240 */     this._keySerializer = src._keySerializer;
/*  241 */     this._valueSerializer = src._valueSerializer;
/*      */     
/*  243 */     this._dynamicValueSerializers = PropertySerializerMap.emptyForProperties();
/*  244 */     this._property = src._property;
/*  245 */     this._filterId = filterId;
/*  246 */     this._sortKeys = sortKeys;
/*  247 */     this._suppressableValue = src._suppressableValue;
/*  248 */     this._suppressNulls = src._suppressNulls;
/*      */   }
/*      */ 
/*      */   
/*      */   public MapSerializer _withValueTypeSerializer(TypeSerializer vts) {
/*  253 */     if (this._valueTypeSerializer == vts) {
/*  254 */       return this;
/*      */     }
/*  256 */     _ensureOverride("_withValueTypeSerializer");
/*  257 */     return new MapSerializer(this, vts, this._suppressableValue, this._suppressNulls);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public MapSerializer withResolved(BeanProperty property, JsonSerializer<?> keySerializer, JsonSerializer<?> valueSerializer, Set<String> ignored, boolean sortKeys) {
/*  267 */     _ensureOverride("withResolved");
/*  268 */     MapSerializer ser = new MapSerializer(this, property, keySerializer, valueSerializer, ignored);
/*  269 */     if (sortKeys != ser._sortKeys) {
/*  270 */       ser = new MapSerializer(ser, this._filterId, sortKeys);
/*      */     }
/*  272 */     return ser;
/*      */   }
/*      */ 
/*      */   
/*      */   public MapSerializer withFilterId(Object filterId) {
/*  277 */     if (this._filterId == filterId) {
/*  278 */       return this;
/*      */     }
/*  280 */     _ensureOverride("withFilterId");
/*  281 */     return new MapSerializer(this, filterId, this._sortKeys);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public MapSerializer withContentInclusion(Object suppressableValue, boolean suppressNulls) {
/*  291 */     if (suppressableValue == this._suppressableValue && suppressNulls == this._suppressNulls) {
/*  292 */       return this;
/*      */     }
/*  294 */     _ensureOverride("withContentInclusion");
/*  295 */     return new MapSerializer(this, this._valueTypeSerializer, suppressableValue, suppressNulls);
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
/*      */   public static MapSerializer construct(Set<String> ignoredEntries, JavaType mapType, boolean staticValueType, TypeSerializer vts, JsonSerializer<Object> keySerializer, JsonSerializer<Object> valueSerializer, Object filterId) {
/*      */     JavaType keyType, valueType;
/*  308 */     if (mapType == null) {
/*  309 */       keyType = valueType = UNSPECIFIED_TYPE;
/*      */     } else {
/*  311 */       keyType = mapType.getKeyType();
/*  312 */       valueType = mapType.getContentType();
/*      */     } 
/*      */     
/*  315 */     if (!staticValueType) {
/*  316 */       staticValueType = (valueType != null && valueType.isFinal());
/*      */     
/*      */     }
/*  319 */     else if (valueType.getRawClass() == Object.class) {
/*  320 */       staticValueType = false;
/*      */     } 
/*      */     
/*  323 */     MapSerializer ser = new MapSerializer(ignoredEntries, keyType, valueType, staticValueType, vts, keySerializer, valueSerializer);
/*      */     
/*  325 */     if (filterId != null) {
/*  326 */       ser = ser.withFilterId(filterId);
/*      */     }
/*  328 */     return ser;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void _ensureOverride(String method) {
/*  335 */     ClassUtil.verifyMustOverride(MapSerializer.class, this, method);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   protected void _ensureOverride() {
/*  343 */     _ensureOverride("N/A");
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
/*      */   @Deprecated
/*      */   protected MapSerializer(MapSerializer src, TypeSerializer vts, Object suppressableValue) {
/*  360 */     this(src, vts, suppressableValue, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public MapSerializer withContentInclusion(Object suppressableValue) {
/*  368 */     return new MapSerializer(this, this._valueTypeSerializer, suppressableValue, this._suppressNulls);
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
/*      */   @Deprecated
/*      */   public static MapSerializer construct(String[] ignoredList, JavaType mapType, boolean staticValueType, TypeSerializer vts, JsonSerializer<Object> keySerializer, JsonSerializer<Object> valueSerializer, Object filterId) {
/*  382 */     Set<String> ignoredEntries = ArrayBuilders.arrayToSet((Object[])ignoredList);
/*  383 */     return construct(ignoredEntries, mapType, staticValueType, vts, keySerializer, valueSerializer, filterId);
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
/*      */   public JsonSerializer<?> createContextual(SerializerProvider provider, BeanProperty property) throws JsonMappingException {
/*  398 */     JsonSerializer<?> ser = null;
/*  399 */     JsonSerializer<?> keySer = null;
/*  400 */     AnnotationIntrospector intr = provider.getAnnotationIntrospector();
/*  401 */     AnnotatedMember propertyAcc = (property == null) ? null : property.getMember();
/*      */ 
/*      */     
/*  404 */     if (_neitherNull(propertyAcc, intr)) {
/*  405 */       Object serDef = intr.findKeySerializer((Annotated)propertyAcc);
/*  406 */       if (serDef != null) {
/*  407 */         keySer = provider.serializerInstance((Annotated)propertyAcc, serDef);
/*      */       }
/*  409 */       serDef = intr.findContentSerializer((Annotated)propertyAcc);
/*  410 */       if (serDef != null) {
/*  411 */         ser = provider.serializerInstance((Annotated)propertyAcc, serDef);
/*      */       }
/*      */     } 
/*  414 */     if (ser == null) {
/*  415 */       ser = this._valueSerializer;
/*      */     }
/*      */     
/*  418 */     ser = findContextualConvertingSerializer(provider, property, ser);
/*  419 */     if (ser == null)
/*      */     {
/*      */ 
/*      */       
/*  423 */       if (this._valueTypeIsStatic && !this._valueType.isJavaLangObject()) {
/*  424 */         ser = provider.findValueSerializer(this._valueType, property);
/*      */       }
/*      */     }
/*  427 */     if (keySer == null) {
/*  428 */       keySer = this._keySerializer;
/*      */     }
/*  430 */     if (keySer == null) {
/*  431 */       keySer = provider.findKeySerializer(this._keyType, property);
/*      */     } else {
/*  433 */       keySer = provider.handleSecondaryContextualization(keySer, property);
/*      */     } 
/*  435 */     Set<String> ignored = this._ignoredEntries;
/*  436 */     boolean sortKeys = false;
/*  437 */     if (_neitherNull(propertyAcc, intr)) {
/*  438 */       JsonIgnoreProperties.Value ignorals = intr.findPropertyIgnorals((Annotated)propertyAcc);
/*  439 */       if (ignorals != null) {
/*  440 */         Set<String> newIgnored = ignorals.findIgnoredForSerialization();
/*  441 */         if (_nonEmpty(newIgnored)) {
/*  442 */           ignored = (ignored == null) ? new HashSet<>() : new HashSet<>(ignored);
/*  443 */           for (String str : newIgnored) {
/*  444 */             ignored.add(str);
/*      */           }
/*      */         } 
/*      */       } 
/*  448 */       Boolean b = intr.findSerializationSortAlphabetically((Annotated)propertyAcc);
/*  449 */       sortKeys = Boolean.TRUE.equals(b);
/*      */     } 
/*  451 */     JsonFormat.Value format = findFormatOverrides(provider, property, Map.class);
/*  452 */     if (format != null) {
/*  453 */       Boolean B = format.getFeature(JsonFormat.Feature.WRITE_SORTED_MAP_ENTRIES);
/*  454 */       if (B != null) {
/*  455 */         sortKeys = B.booleanValue();
/*      */       }
/*      */     } 
/*  458 */     MapSerializer mser = withResolved(property, keySer, ser, ignored, sortKeys);
/*      */ 
/*      */     
/*  461 */     if (property != null) {
/*  462 */       AnnotatedMember m = property.getMember();
/*  463 */       if (m != null) {
/*  464 */         Object filterId = intr.findFilterId((Annotated)m);
/*  465 */         if (filterId != null) {
/*  466 */           mser = mser.withFilterId(filterId);
/*      */         }
/*      */       } 
/*  469 */       JsonInclude.Value inclV = property.findPropertyInclusion((MapperConfig)provider.getConfig(), null);
/*  470 */       if (inclV != null) {
/*  471 */         JsonInclude.Include incl = inclV.getContentInclusion();
/*      */         
/*  473 */         if (incl != JsonInclude.Include.USE_DEFAULTS) {
/*      */           Object valueToSuppress;
/*      */           boolean suppressNulls;
/*  476 */           switch (incl) {
/*      */             case NON_DEFAULT:
/*  478 */               valueToSuppress = BeanUtil.getDefaultValue(this._valueType);
/*  479 */               suppressNulls = true;
/*  480 */               if (valueToSuppress != null && 
/*  481 */                 valueToSuppress.getClass().isArray()) {
/*  482 */                 valueToSuppress = ArrayBuilders.getArrayComparator(valueToSuppress);
/*      */               }
/*      */               break;
/*      */             
/*      */             case NON_ABSENT:
/*  487 */               suppressNulls = true;
/*  488 */               valueToSuppress = this._valueType.isReferenceType() ? MARKER_FOR_EMPTY : null;
/*      */               break;
/*      */             case NON_EMPTY:
/*  491 */               suppressNulls = true;
/*  492 */               valueToSuppress = MARKER_FOR_EMPTY;
/*      */               break;
/*      */             case CUSTOM:
/*  495 */               valueToSuppress = provider.includeFilterInstance(null, inclV.getContentFilter());
/*  496 */               if (valueToSuppress == null) {
/*  497 */                 suppressNulls = true; break;
/*      */               } 
/*  499 */               suppressNulls = provider.includeFilterSuppressNulls(valueToSuppress);
/*      */               break;
/*      */             
/*      */             case NON_NULL:
/*  503 */               valueToSuppress = null;
/*  504 */               suppressNulls = true;
/*      */               break;
/*      */             
/*      */             default:
/*  508 */               valueToSuppress = null;
/*      */ 
/*      */               
/*  511 */               suppressNulls = false;
/*      */               break;
/*      */           } 
/*  514 */           mser = mser.withContentInclusion(valueToSuppress, suppressNulls);
/*      */         } 
/*      */       } 
/*      */     } 
/*  518 */     return (JsonSerializer<?>)mser;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JavaType getContentType() {
/*  529 */     return this._valueType;
/*      */   }
/*      */ 
/*      */   
/*      */   public JsonSerializer<?> getContentSerializer() {
/*  534 */     return this._valueSerializer;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isEmpty(SerializerProvider prov, Map<?, ?> value) {
/*  540 */     if (value.isEmpty()) {
/*  541 */       return true;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  546 */     Object supp = this._suppressableValue;
/*  547 */     if (supp == null && !this._suppressNulls) {
/*  548 */       return false;
/*      */     }
/*  550 */     JsonSerializer<Object> valueSer = this._valueSerializer;
/*  551 */     boolean checkEmpty = (MARKER_FOR_EMPTY == supp);
/*  552 */     if (valueSer != null) {
/*  553 */       for (Object elemValue : value.values()) {
/*  554 */         if (elemValue == null) {
/*  555 */           if (this._suppressNulls) {
/*      */             continue;
/*      */           }
/*  558 */           return false;
/*      */         } 
/*  560 */         if (checkEmpty) {
/*  561 */           if (!valueSer.isEmpty(prov, elemValue))
/*  562 */             return false;  continue;
/*      */         } 
/*  564 */         if (supp == null || !supp.equals(value)) {
/*  565 */           return false;
/*      */         }
/*      */       } 
/*  568 */       return true;
/*      */     } 
/*      */     
/*  571 */     for (Object elemValue : value.values()) {
/*  572 */       if (elemValue == null) {
/*  573 */         if (this._suppressNulls) {
/*      */           continue;
/*      */         }
/*  576 */         return false;
/*      */       } 
/*      */       try {
/*  579 */         valueSer = _findSerializer(prov, elemValue);
/*  580 */       } catch (JsonMappingException e) {
/*      */         
/*  582 */         return false;
/*      */       } 
/*  584 */       if (checkEmpty) {
/*  585 */         if (!valueSer.isEmpty(prov, elemValue))
/*  586 */           return false;  continue;
/*      */       } 
/*  588 */       if (supp == null || !supp.equals(value)) {
/*  589 */         return false;
/*      */       }
/*      */     } 
/*  592 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean hasSingleElement(Map<?, ?> value) {
/*  597 */     return (value.size() == 1);
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
/*      */   public JsonSerializer<?> getKeySerializer() {
/*  617 */     return this._keySerializer;
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
/*      */   public void serialize(Map<?, ?> value, JsonGenerator gen, SerializerProvider provider) throws IOException {
/*  630 */     gen.writeStartObject(value);
/*  631 */     if (!value.isEmpty()) {
/*  632 */       if (this._sortKeys || provider.isEnabled(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS)) {
/*  633 */         value = _orderEntries(value, gen, provider);
/*      */       }
/*      */       PropertyFilter pf;
/*  636 */       if (this._filterId != null && (pf = findPropertyFilter(provider, this._filterId, value)) != null) {
/*  637 */         serializeFilteredFields(value, gen, provider, pf, this._suppressableValue);
/*  638 */       } else if (this._suppressableValue != null || this._suppressNulls) {
/*  639 */         serializeOptionalFields(value, gen, provider, this._suppressableValue);
/*  640 */       } else if (this._valueSerializer != null) {
/*  641 */         serializeFieldsUsing(value, gen, provider, this._valueSerializer);
/*      */       } else {
/*  643 */         serializeFields(value, gen, provider);
/*      */       } 
/*      */     } 
/*  646 */     gen.writeEndObject();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void serializeWithType(Map<?, ?> value, JsonGenerator gen, SerializerProvider provider, TypeSerializer typeSer) throws IOException {
/*  655 */     gen.setCurrentValue(value);
/*  656 */     WritableTypeId typeIdDef = typeSer.writeTypePrefix(gen, typeSer
/*  657 */         .typeId(value, JsonToken.START_OBJECT));
/*  658 */     if (!value.isEmpty()) {
/*  659 */       if (this._sortKeys || provider.isEnabled(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS)) {
/*  660 */         value = _orderEntries(value, gen, provider);
/*      */       }
/*      */       PropertyFilter pf;
/*  663 */       if (this._filterId != null && (pf = findPropertyFilter(provider, this._filterId, value)) != null) {
/*  664 */         serializeFilteredFields(value, gen, provider, pf, this._suppressableValue);
/*  665 */       } else if (this._suppressableValue != null || this._suppressNulls) {
/*  666 */         serializeOptionalFields(value, gen, provider, this._suppressableValue);
/*  667 */       } else if (this._valueSerializer != null) {
/*  668 */         serializeFieldsUsing(value, gen, provider, this._valueSerializer);
/*      */       } else {
/*  670 */         serializeFields(value, gen, provider);
/*      */       } 
/*      */     } 
/*  673 */     typeSer.writeTypeSuffix(gen, typeIdDef);
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
/*      */   public void serializeFields(Map<?, ?> value, JsonGenerator gen, SerializerProvider provider) throws IOException {
/*  691 */     if (this._valueTypeSerializer != null) {
/*  692 */       serializeTypedFields(value, gen, provider, (Object)null);
/*      */       return;
/*      */     } 
/*  695 */     JsonSerializer<Object> keySerializer = this._keySerializer;
/*  696 */     Set<String> ignored = this._ignoredEntries;
/*  697 */     Object keyElem = null;
/*      */     
/*      */     try {
/*  700 */       for (Map.Entry<?, ?> entry : value.entrySet()) {
/*  701 */         Object valueElem = entry.getValue();
/*      */         
/*  703 */         keyElem = entry.getKey();
/*  704 */         if (keyElem == null) {
/*  705 */           provider.findNullKeySerializer(this._keyType, this._property).serialize(null, gen, provider);
/*      */         } else {
/*      */           
/*  708 */           if (ignored != null && ignored.contains(keyElem)) {
/*      */             continue;
/*      */           }
/*  711 */           keySerializer.serialize(keyElem, gen, provider);
/*      */         } 
/*      */         
/*  714 */         if (valueElem == null) {
/*  715 */           provider.defaultSerializeNull(gen);
/*      */           continue;
/*      */         } 
/*  718 */         JsonSerializer<Object> serializer = this._valueSerializer;
/*  719 */         if (serializer == null) {
/*  720 */           serializer = _findSerializer(provider, valueElem);
/*      */         }
/*  722 */         serializer.serialize(valueElem, gen, provider);
/*      */       } 
/*  724 */     } catch (Exception e) {
/*  725 */       wrapAndThrow(provider, e, value, String.valueOf(keyElem));
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
/*      */   public void serializeOptionalFields(Map<?, ?> value, JsonGenerator gen, SerializerProvider provider, Object suppressableValue) throws IOException {
/*  737 */     if (this._valueTypeSerializer != null) {
/*  738 */       serializeTypedFields(value, gen, provider, suppressableValue);
/*      */       return;
/*      */     } 
/*  741 */     Set<String> ignored = this._ignoredEntries;
/*  742 */     boolean checkEmpty = (MARKER_FOR_EMPTY == suppressableValue);
/*      */     
/*  744 */     for (Map.Entry<?, ?> entry : value.entrySet()) {
/*      */       JsonSerializer<Object> keySerializer, valueSer;
/*  746 */       Object keyElem = entry.getKey();
/*      */       
/*  748 */       if (keyElem == null) {
/*  749 */         keySerializer = provider.findNullKeySerializer(this._keyType, this._property);
/*      */       } else {
/*  751 */         if (ignored != null && ignored.contains(keyElem))
/*  752 */           continue;  keySerializer = this._keySerializer;
/*      */       } 
/*      */ 
/*      */       
/*  756 */       Object valueElem = entry.getValue();
/*      */       
/*  758 */       if (valueElem == null) {
/*  759 */         if (this._suppressNulls) {
/*      */           continue;
/*      */         }
/*  762 */         valueSer = provider.getDefaultNullValueSerializer();
/*      */       } else {
/*  764 */         valueSer = this._valueSerializer;
/*  765 */         if (valueSer == null) {
/*  766 */           valueSer = _findSerializer(provider, valueElem);
/*      */         }
/*      */         
/*  769 */         if (checkEmpty ? 
/*  770 */           valueSer.isEmpty(provider, valueElem) : (
/*      */ 
/*      */           
/*  773 */           suppressableValue != null && 
/*  774 */           suppressableValue.equals(valueElem))) {
/*      */           continue;
/*      */         }
/*      */       } 
/*      */ 
/*      */       
/*      */       try {
/*  781 */         keySerializer.serialize(keyElem, gen, provider);
/*  782 */         valueSer.serialize(valueElem, gen, provider);
/*  783 */       } catch (Exception e) {
/*  784 */         wrapAndThrow(provider, e, value, String.valueOf(keyElem));
/*      */       } 
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
/*      */   public void serializeFieldsUsing(Map<?, ?> value, JsonGenerator gen, SerializerProvider provider, JsonSerializer<Object> ser) throws IOException {
/*  798 */     JsonSerializer<Object> keySerializer = this._keySerializer;
/*  799 */     Set<String> ignored = this._ignoredEntries;
/*  800 */     TypeSerializer typeSer = this._valueTypeSerializer;
/*      */     
/*  802 */     for (Map.Entry<?, ?> entry : value.entrySet()) {
/*  803 */       Object keyElem = entry.getKey();
/*  804 */       if (ignored != null && ignored.contains(keyElem))
/*      */         continue; 
/*  806 */       if (keyElem == null) {
/*  807 */         provider.findNullKeySerializer(this._keyType, this._property).serialize(null, gen, provider);
/*      */       } else {
/*  809 */         keySerializer.serialize(keyElem, gen, provider);
/*      */       } 
/*  811 */       Object valueElem = entry.getValue();
/*  812 */       if (valueElem == null) {
/*  813 */         provider.defaultSerializeNull(gen); continue;
/*      */       } 
/*      */       try {
/*  816 */         if (typeSer == null) {
/*  817 */           ser.serialize(valueElem, gen, provider); continue;
/*      */         } 
/*  819 */         ser.serializeWithType(valueElem, gen, provider, typeSer);
/*      */       }
/*  821 */       catch (Exception e) {
/*  822 */         wrapAndThrow(provider, e, value, String.valueOf(keyElem));
/*      */       } 
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
/*      */   public void serializeFilteredFields(Map<?, ?> value, JsonGenerator gen, SerializerProvider provider, PropertyFilter filter, Object suppressableValue) throws IOException {
/*  839 */     Set<String> ignored = this._ignoredEntries;
/*  840 */     MapProperty prop = new MapProperty(this._valueTypeSerializer, this._property);
/*  841 */     boolean checkEmpty = (MARKER_FOR_EMPTY == suppressableValue);
/*      */     
/*  843 */     for (Map.Entry<?, ?> entry : value.entrySet()) {
/*      */       JsonSerializer<Object> keySerializer, valueSer;
/*  845 */       Object keyElem = entry.getKey();
/*  846 */       if (ignored != null && ignored.contains(keyElem)) {
/*      */         continue;
/*      */       }
/*  849 */       if (keyElem == null) {
/*  850 */         keySerializer = provider.findNullKeySerializer(this._keyType, this._property);
/*      */       } else {
/*  852 */         keySerializer = this._keySerializer;
/*      */       } 
/*      */       
/*  855 */       Object valueElem = entry.getValue();
/*      */ 
/*      */ 
/*      */       
/*  859 */       if (valueElem == null) {
/*  860 */         if (this._suppressNulls) {
/*      */           continue;
/*      */         }
/*  863 */         valueSer = provider.getDefaultNullValueSerializer();
/*      */       } else {
/*  865 */         valueSer = this._valueSerializer;
/*  866 */         if (valueSer == null) {
/*  867 */           valueSer = _findSerializer(provider, valueElem);
/*      */         }
/*      */         
/*  870 */         if (checkEmpty ? 
/*  871 */           valueSer.isEmpty(provider, valueElem) : (
/*      */ 
/*      */           
/*  874 */           suppressableValue != null && 
/*  875 */           suppressableValue.equals(valueElem))) {
/*      */           continue;
/*      */         }
/*      */       } 
/*      */ 
/*      */       
/*  881 */       prop.reset(keyElem, valueElem, keySerializer, valueSer);
/*      */       try {
/*  883 */         filter.serializeAsField(value, gen, provider, prop);
/*  884 */       } catch (Exception e) {
/*  885 */         wrapAndThrow(provider, e, value, String.valueOf(keyElem));
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void serializeTypedFields(Map<?, ?> value, JsonGenerator gen, SerializerProvider provider, Object suppressableValue) throws IOException {
/*  897 */     Set<String> ignored = this._ignoredEntries;
/*  898 */     boolean checkEmpty = (MARKER_FOR_EMPTY == suppressableValue);
/*      */     
/*  900 */     for (Map.Entry<?, ?> entry : value.entrySet()) {
/*  901 */       JsonSerializer<Object> keySerializer, valueSer; Object keyElem = entry.getKey();
/*      */       
/*  903 */       if (keyElem == null) {
/*  904 */         keySerializer = provider.findNullKeySerializer(this._keyType, this._property);
/*      */       } else {
/*      */         
/*  907 */         if (ignored != null && ignored.contains(keyElem))
/*  908 */           continue;  keySerializer = this._keySerializer;
/*      */       } 
/*  910 */       Object valueElem = entry.getValue();
/*      */ 
/*      */ 
/*      */       
/*  914 */       if (valueElem == null) {
/*  915 */         if (this._suppressNulls) {
/*      */           continue;
/*      */         }
/*  918 */         valueSer = provider.getDefaultNullValueSerializer();
/*      */       } else {
/*  920 */         valueSer = this._valueSerializer;
/*  921 */         if (valueSer == null) {
/*  922 */           valueSer = _findSerializer(provider, valueElem);
/*      */         }
/*      */         
/*  925 */         if (checkEmpty ? 
/*  926 */           valueSer.isEmpty(provider, valueElem) : (
/*      */ 
/*      */           
/*  929 */           suppressableValue != null && 
/*  930 */           suppressableValue.equals(valueElem))) {
/*      */           continue;
/*      */         }
/*      */       } 
/*      */       
/*  935 */       keySerializer.serialize(keyElem, gen, provider);
/*      */       try {
/*  937 */         valueSer.serializeWithType(valueElem, gen, provider, this._valueTypeSerializer);
/*  938 */       } catch (Exception e) {
/*  939 */         wrapAndThrow(provider, e, value, String.valueOf(keyElem));
/*      */       } 
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
/*      */   public void serializeFilteredAnyProperties(SerializerProvider provider, JsonGenerator gen, Object bean, Map<?, ?> value, PropertyFilter filter, Object suppressableValue) throws IOException {
/*  957 */     Set<String> ignored = this._ignoredEntries;
/*  958 */     MapProperty prop = new MapProperty(this._valueTypeSerializer, this._property);
/*  959 */     boolean checkEmpty = (MARKER_FOR_EMPTY == suppressableValue);
/*      */     
/*  961 */     for (Map.Entry<?, ?> entry : value.entrySet()) {
/*      */       JsonSerializer<Object> keySerializer, valueSer;
/*  963 */       Object keyElem = entry.getKey();
/*  964 */       if (ignored != null && ignored.contains(keyElem)) {
/*      */         continue;
/*      */       }
/*  967 */       if (keyElem == null) {
/*  968 */         keySerializer = provider.findNullKeySerializer(this._keyType, this._property);
/*      */       } else {
/*  970 */         keySerializer = this._keySerializer;
/*      */       } 
/*      */       
/*  973 */       Object valueElem = entry.getValue();
/*      */ 
/*      */ 
/*      */       
/*  977 */       if (valueElem == null) {
/*  978 */         if (this._suppressNulls) {
/*      */           continue;
/*      */         }
/*  981 */         valueSer = provider.getDefaultNullValueSerializer();
/*      */       } else {
/*  983 */         valueSer = this._valueSerializer;
/*  984 */         if (valueSer == null) {
/*  985 */           valueSer = _findSerializer(provider, valueElem);
/*      */         }
/*      */         
/*  988 */         if (checkEmpty ? 
/*  989 */           valueSer.isEmpty(provider, valueElem) : (
/*      */ 
/*      */           
/*  992 */           suppressableValue != null && 
/*  993 */           suppressableValue.equals(valueElem))) {
/*      */           continue;
/*      */         }
/*      */       } 
/*      */ 
/*      */       
/*  999 */       prop.reset(keyElem, valueElem, keySerializer, valueSer);
/*      */       try {
/* 1001 */         filter.serializeAsField(bean, gen, provider, prop);
/* 1002 */       } catch (Exception e) {
/* 1003 */         wrapAndThrow(provider, e, value, String.valueOf(keyElem));
/*      */       } 
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
/*      */   public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
/* 1019 */     return (JsonNode)createSchemaNode("object", true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
/* 1026 */     JsonMapFormatVisitor v2 = visitor.expectMapFormat(typeHint);
/* 1027 */     if (v2 != null) {
/* 1028 */       v2.keyFormat((JsonFormatVisitable)this._keySerializer, this._keyType);
/* 1029 */       JsonSerializer<?> valueSer = this._valueSerializer;
/* 1030 */       if (valueSer == null) {
/* 1031 */         valueSer = _findAndAddDynamic(this._dynamicValueSerializers, this._valueType, visitor
/* 1032 */             .getProvider());
/*      */       }
/* 1034 */       v2.valueFormat((JsonFormatVisitable)valueSer, this._valueType);
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
/*      */   protected final JsonSerializer<Object> _findAndAddDynamic(PropertySerializerMap map, Class<?> type, SerializerProvider provider) throws JsonMappingException {
/* 1047 */     PropertySerializerMap.SerializerAndMapResult result = map.findAndAddSecondarySerializer(type, provider, this._property);
/*      */     
/* 1049 */     if (map != result.map) {
/* 1050 */       this._dynamicValueSerializers = result.map;
/*      */     }
/* 1052 */     return result.serializer;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected final JsonSerializer<Object> _findAndAddDynamic(PropertySerializerMap map, JavaType type, SerializerProvider provider) throws JsonMappingException {
/* 1058 */     PropertySerializerMap.SerializerAndMapResult result = map.findAndAddSecondarySerializer(type, provider, this._property);
/* 1059 */     if (map != result.map) {
/* 1060 */       this._dynamicValueSerializers = result.map;
/*      */     }
/* 1062 */     return result.serializer;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Map<?, ?> _orderEntries(Map<?, ?> input, JsonGenerator gen, SerializerProvider provider) throws IOException {
/* 1069 */     if (input instanceof java.util.SortedMap) {
/* 1070 */       return input;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1076 */     if (_hasNullKey(input)) {
/* 1077 */       TreeMap<Object, Object> result = new TreeMap<>();
/* 1078 */       for (Map.Entry<?, ?> entry : input.entrySet()) {
/* 1079 */         Object key = entry.getKey();
/* 1080 */         if (key == null) {
/* 1081 */           _writeNullKeyedEntry(gen, provider, entry.getValue());
/*      */           continue;
/*      */         } 
/* 1084 */         result.put(key, entry.getValue());
/*      */       } 
/* 1086 */       return result;
/*      */     } 
/* 1088 */     return new TreeMap<>(input);
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
/*      */   protected boolean _hasNullKey(Map<?, ?> input) {
/* 1104 */     return (input instanceof java.util.HashMap && input.containsKey(null));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void _writeNullKeyedEntry(JsonGenerator gen, SerializerProvider provider, Object value) throws IOException {
/* 1110 */     JsonSerializer<Object> valueSer, keySerializer = provider.findNullKeySerializer(this._keyType, this._property);
/*      */     
/* 1112 */     if (value == null) {
/* 1113 */       if (this._suppressNulls) {
/*      */         return;
/*      */       }
/* 1116 */       valueSer = provider.getDefaultNullValueSerializer();
/*      */     } else {
/* 1118 */       valueSer = this._valueSerializer;
/* 1119 */       if (valueSer == null) {
/* 1120 */         valueSer = _findSerializer(provider, value);
/*      */       }
/* 1122 */       if (this._suppressableValue == MARKER_FOR_EMPTY) {
/* 1123 */         if (valueSer.isEmpty(provider, value)) {
/*      */           return;
/*      */         }
/* 1126 */       } else if (this._suppressableValue != null && this._suppressableValue
/* 1127 */         .equals(value)) {
/*      */         return;
/*      */       } 
/*      */     } 
/*      */     
/*      */     try {
/* 1133 */       keySerializer.serialize(null, gen, provider);
/* 1134 */       valueSer.serialize(value, gen, provider);
/* 1135 */     } catch (Exception e) {
/* 1136 */       wrapAndThrow(provider, e, value, "");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private final JsonSerializer<Object> _findSerializer(SerializerProvider provider, Object value) throws JsonMappingException {
/* 1143 */     Class<?> cc = value.getClass();
/* 1144 */     JsonSerializer<Object> valueSer = this._dynamicValueSerializers.serializerFor(cc);
/* 1145 */     if (valueSer != null) {
/* 1146 */       return valueSer;
/*      */     }
/* 1148 */     if (this._valueType.hasGenericTypes()) {
/* 1149 */       return _findAndAddDynamic(this._dynamicValueSerializers, provider
/* 1150 */           .constructSpecializedType(this._valueType, cc), provider);
/*      */     }
/* 1152 */     return _findAndAddDynamic(this._dynamicValueSerializers, cc, provider);
/*      */   }
/*      */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\ser\std\MapSerializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */