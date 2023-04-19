/*      */ package com.fasterxml.jackson.databind.ser;
/*      */ import com.fasterxml.jackson.annotation.JsonFormat;
/*      */ import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/*      */ import com.fasterxml.jackson.annotation.JsonInclude;
/*      */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*      */ import com.fasterxml.jackson.databind.BeanDescription;
/*      */ import com.fasterxml.jackson.databind.JavaType;
/*      */ import com.fasterxml.jackson.databind.JsonMappingException;
/*      */ import com.fasterxml.jackson.databind.JsonSerializer;
/*      */ import com.fasterxml.jackson.databind.MapperFeature;
/*      */ import com.fasterxml.jackson.databind.SerializationConfig;
/*      */ import com.fasterxml.jackson.databind.SerializationFeature;
/*      */ import com.fasterxml.jackson.databind.SerializerProvider;
/*      */ import com.fasterxml.jackson.databind.annotation.JsonSerialize;
/*      */ import com.fasterxml.jackson.databind.cfg.MapperConfig;
/*      */ import com.fasterxml.jackson.databind.cfg.SerializerFactoryConfig;
/*      */ import com.fasterxml.jackson.databind.introspect.Annotated;
/*      */ import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
/*      */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*      */ import com.fasterxml.jackson.databind.jsontype.NamedType;
/*      */ import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
/*      */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*      */ import com.fasterxml.jackson.databind.ser.impl.IndexedStringListSerializer;
/*      */ import com.fasterxml.jackson.databind.ser.impl.MapEntrySerializer;
/*      */ import com.fasterxml.jackson.databind.ser.impl.StringArraySerializer;
/*      */ import com.fasterxml.jackson.databind.ser.impl.StringCollectionSerializer;
/*      */ import com.fasterxml.jackson.databind.ser.std.AtomicReferenceSerializer;
/*      */ import com.fasterxml.jackson.databind.ser.std.BooleanSerializer;
/*      */ import com.fasterxml.jackson.databind.ser.std.CalendarSerializer;
/*      */ import com.fasterxml.jackson.databind.ser.std.DateSerializer;
/*      */ import com.fasterxml.jackson.databind.ser.std.EnumSerializer;
/*      */ import com.fasterxml.jackson.databind.ser.std.JsonValueSerializer;
/*      */ import com.fasterxml.jackson.databind.ser.std.MapSerializer;
/*      */ import com.fasterxml.jackson.databind.ser.std.NumberSerializer;
/*      */ import com.fasterxml.jackson.databind.ser.std.ObjectArraySerializer;
/*      */ import com.fasterxml.jackson.databind.ser.std.StdKeySerializers;
/*      */ import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
/*      */ import com.fasterxml.jackson.databind.type.ArrayType;
/*      */ import com.fasterxml.jackson.databind.type.CollectionLikeType;
/*      */ import com.fasterxml.jackson.databind.type.CollectionType;
/*      */ import com.fasterxml.jackson.databind.type.MapLikeType;
/*      */ import com.fasterxml.jackson.databind.type.MapType;
/*      */ import com.fasterxml.jackson.databind.type.ReferenceType;
/*      */ import com.fasterxml.jackson.databind.type.TypeFactory;
/*      */ import com.fasterxml.jackson.databind.util.ArrayBuilders;
/*      */ import com.fasterxml.jackson.databind.util.BeanUtil;
/*      */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*      */ import com.fasterxml.jackson.databind.util.Converter;
/*      */ import java.math.BigDecimal;
/*      */ import java.math.BigInteger;
/*      */ import java.util.Calendar;
/*      */ import java.util.Collection;
/*      */ import java.util.Date;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.atomic.AtomicReference;
/*      */ 
/*      */ public abstract class BasicSerializerFactory extends SerializerFactory implements Serializable {
/*      */   static {
/*   62 */     HashMap<String, Class<? extends JsonSerializer<?>>> concLazy = new HashMap<>();
/*      */     
/*   64 */     HashMap<String, JsonSerializer<?>> concrete = new HashMap<>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*   71 */     concrete.put(String.class.getName(), new StringSerializer());
/*   72 */     ToStringSerializer sls = ToStringSerializer.instance;
/*   73 */     concrete.put(StringBuffer.class.getName(), sls);
/*   74 */     concrete.put(StringBuilder.class.getName(), sls);
/*   75 */     concrete.put(Character.class.getName(), sls);
/*   76 */     concrete.put(char.class.getName(), sls);
/*      */ 
/*      */     
/*   79 */     NumberSerializers.addAll(concrete);
/*   80 */     concrete.put(boolean.class.getName(), new BooleanSerializer(true));
/*   81 */     concrete.put(Boolean.class.getName(), new BooleanSerializer(false));
/*      */ 
/*      */     
/*   84 */     concrete.put(BigInteger.class.getName(), new NumberSerializer(BigInteger.class));
/*   85 */     concrete.put(BigDecimal.class.getName(), new NumberSerializer(BigDecimal.class));
/*      */ 
/*      */ 
/*      */     
/*   89 */     concrete.put(Calendar.class.getName(), CalendarSerializer.instance);
/*   90 */     concrete.put(Date.class.getName(), DateSerializer.instance);
/*      */ 
/*      */     
/*   93 */     for (Map.Entry<Class<?>, Object> en : (Iterable<Map.Entry<Class<?>, Object>>)StdJdkSerializers.all()) {
/*   94 */       Object value = en.getValue();
/*   95 */       if (value instanceof JsonSerializer) {
/*   96 */         concrete.put(((Class)en.getKey()).getName(), (JsonSerializer)value);
/*      */         continue;
/*      */       } 
/*   99 */       Class<? extends JsonSerializer<?>> cls = (Class<? extends JsonSerializer<?>>)value;
/*  100 */       concLazy.put(((Class)en.getKey()).getName(), cls);
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  106 */     concLazy.put(TokenBuffer.class.getName(), TokenBufferSerializer.class);
/*      */     
/*  108 */     _concrete = concrete;
/*  109 */     _concreteLazy = concLazy;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static final HashMap<String, JsonSerializer<?>> _concrete;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static final HashMap<String, Class<? extends JsonSerializer<?>>> _concreteLazy;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final SerializerFactoryConfig _factoryConfig;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected BasicSerializerFactory(SerializerFactoryConfig config) {
/*  136 */     this._factoryConfig = (config == null) ? new SerializerFactoryConfig() : config;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public SerializerFactoryConfig getFactoryConfig() {
/*  147 */     return this._factoryConfig;
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
/*      */   public final SerializerFactory withAdditionalSerializers(Serializers additional) {
/*  168 */     return withConfig(this._factoryConfig.withAdditionalSerializers(additional));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final SerializerFactory withAdditionalKeySerializers(Serializers additional) {
/*  177 */     return withConfig(this._factoryConfig.withAdditionalKeySerializers(additional));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final SerializerFactory withSerializerModifier(BeanSerializerModifier modifier) {
/*  186 */     return withConfig(this._factoryConfig.withSerializerModifier(modifier));
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
/*      */   public JsonSerializer<Object> createKeySerializer(SerializationConfig config, JavaType keyType, JsonSerializer<Object> defaultImpl) {
/*  208 */     BeanDescription beanDesc = config.introspectClassAnnotations(keyType.getRawClass());
/*  209 */     JsonSerializer<?> ser = null;
/*      */     
/*  211 */     if (this._factoryConfig.hasKeySerializers())
/*      */     {
/*  213 */       for (Serializers serializers : this._factoryConfig.keySerializers()) {
/*  214 */         ser = serializers.findSerializer(config, keyType, beanDesc);
/*  215 */         if (ser != null) {
/*      */           break;
/*      */         }
/*      */       } 
/*      */     }
/*  220 */     if (ser == null) {
/*  221 */       ser = defaultImpl;
/*  222 */       if (ser == null) {
/*  223 */         ser = StdKeySerializers.getStdKeySerializer(config, keyType.getRawClass(), false);
/*      */         
/*  225 */         if (ser == null) {
/*  226 */           beanDesc = config.introspect(keyType);
/*  227 */           AnnotatedMember am = beanDesc.findJsonValueAccessor();
/*  228 */           if (am != null) {
/*  229 */             Class<?> rawType = am.getRawType();
/*  230 */             JsonSerializer<?> delegate = StdKeySerializers.getStdKeySerializer(config, rawType, true);
/*      */             
/*  232 */             if (config.canOverrideAccessModifiers()) {
/*  233 */               ClassUtil.checkAndFixAccess(am.getMember(), config
/*  234 */                   .isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS));
/*      */             }
/*  236 */             JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(am, delegate);
/*      */           } else {
/*  238 */             ser = StdKeySerializers.getFallbackKeySerializer(config, keyType.getRawClass());
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  245 */     if (this._factoryConfig.hasSerializerModifiers()) {
/*  246 */       for (BeanSerializerModifier mod : this._factoryConfig.serializerModifiers()) {
/*  247 */         ser = mod.modifyKeySerializer(config, keyType, beanDesc, ser);
/*      */       }
/*      */     }
/*  250 */     return (JsonSerializer)ser;
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
/*      */   public TypeSerializer createTypeSerializer(SerializationConfig config, JavaType baseType) {
/*  262 */     BeanDescription bean = config.introspectClassAnnotations(baseType.getRawClass());
/*  263 */     AnnotatedClass ac = bean.getClassInfo();
/*  264 */     AnnotationIntrospector ai = config.getAnnotationIntrospector();
/*  265 */     TypeResolverBuilder<?> b = ai.findTypeResolver((MapperConfig)config, ac, baseType);
/*      */ 
/*      */ 
/*      */     
/*  269 */     Collection<NamedType> subtypes = null;
/*  270 */     if (b == null) {
/*  271 */       b = config.getDefaultTyper(baseType);
/*      */     } else {
/*  273 */       subtypes = config.getSubtypeResolver().collectAndResolveSubtypesByClass((MapperConfig)config, ac);
/*      */     } 
/*  275 */     if (b == null) {
/*  276 */       return null;
/*      */     }
/*      */ 
/*      */     
/*  280 */     return b.buildTypeSerializer(config, baseType, subtypes);
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
/*      */   protected final JsonSerializer<?> findSerializerByLookup(JavaType type, SerializationConfig config, BeanDescription beanDesc, boolean staticTyping) {
/*  305 */     Class<?> raw = type.getRawClass();
/*  306 */     String clsName = raw.getName();
/*  307 */     JsonSerializer<?> ser = _concrete.get(clsName);
/*  308 */     if (ser == null) {
/*  309 */       Class<? extends JsonSerializer<?>> serClass = _concreteLazy.get(clsName);
/*  310 */       if (serClass != null)
/*      */       {
/*      */ 
/*      */         
/*  314 */         return (JsonSerializer)ClassUtil.createInstance(serClass, false);
/*      */       }
/*      */     } 
/*  317 */     return ser;
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
/*      */   protected final JsonSerializer<?> findSerializerByAnnotations(SerializerProvider prov, JavaType type, BeanDescription beanDesc) throws JsonMappingException {
/*  340 */     Class<?> raw = type.getRawClass();
/*      */     
/*  342 */     if (JsonSerializable.class.isAssignableFrom(raw)) {
/*  343 */       return (JsonSerializer<?>)SerializableSerializer.instance;
/*      */     }
/*      */     
/*  346 */     AnnotatedMember valueAccessor = beanDesc.findJsonValueAccessor();
/*  347 */     if (valueAccessor != null) {
/*  348 */       if (prov.canOverrideAccessModifiers()) {
/*  349 */         ClassUtil.checkAndFixAccess(valueAccessor.getMember(), prov
/*  350 */             .isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS));
/*      */       }
/*  352 */       JsonSerializer<Object> ser = findSerializerFromAnnotation(prov, (Annotated)valueAccessor);
/*  353 */       return (JsonSerializer<?>)new JsonValueSerializer(valueAccessor, ser);
/*      */     } 
/*      */     
/*  356 */     return null;
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
/*      */   protected final JsonSerializer<?> findSerializerByPrimaryType(SerializerProvider prov, JavaType type, BeanDescription beanDesc, boolean staticTyping) throws JsonMappingException {
/*  371 */     Class<?> raw = type.getRawClass();
/*      */ 
/*      */     
/*  374 */     JsonSerializer<?> ser = findOptionalStdSerializer(prov, type, beanDesc, staticTyping);
/*  375 */     if (ser != null) {
/*  376 */       return ser;
/*      */     }
/*      */     
/*  379 */     if (Calendar.class.isAssignableFrom(raw)) {
/*  380 */       return (JsonSerializer<?>)CalendarSerializer.instance;
/*      */     }
/*  382 */     if (Date.class.isAssignableFrom(raw)) {
/*  383 */       return (JsonSerializer<?>)DateSerializer.instance;
/*      */     }
/*  385 */     if (Map.Entry.class.isAssignableFrom(raw)) {
/*      */       
/*  387 */       JavaType mapEntryType = type.findSuperType(Map.Entry.class);
/*      */ 
/*      */       
/*  390 */       JavaType kt = mapEntryType.containedTypeOrUnknown(0);
/*  391 */       JavaType vt = mapEntryType.containedTypeOrUnknown(1);
/*  392 */       return buildMapEntrySerializer(prov, type, beanDesc, staticTyping, kt, vt);
/*      */     } 
/*  394 */     if (ByteBuffer.class.isAssignableFrom(raw)) {
/*  395 */       return (JsonSerializer<?>)new ByteBufferSerializer();
/*      */     }
/*  397 */     if (InetAddress.class.isAssignableFrom(raw)) {
/*  398 */       return (JsonSerializer<?>)new InetAddressSerializer();
/*      */     }
/*  400 */     if (InetSocketAddress.class.isAssignableFrom(raw)) {
/*  401 */       return (JsonSerializer<?>)new InetSocketAddressSerializer();
/*      */     }
/*  403 */     if (TimeZone.class.isAssignableFrom(raw)) {
/*  404 */       return (JsonSerializer<?>)new TimeZoneSerializer();
/*      */     }
/*  406 */     if (Charset.class.isAssignableFrom(raw)) {
/*  407 */       return (JsonSerializer<?>)ToStringSerializer.instance;
/*      */     }
/*  409 */     if (Number.class.isAssignableFrom(raw)) {
/*      */       
/*  411 */       JsonFormat.Value format = beanDesc.findExpectedFormat(null);
/*  412 */       if (format != null) {
/*  413 */         switch (format.getShape()) {
/*      */           case NON_DEFAULT:
/*  415 */             return (JsonSerializer<?>)ToStringSerializer.instance;
/*      */           case NON_ABSENT:
/*      */           case NON_EMPTY:
/*  418 */             return null;
/*      */         } 
/*      */       
/*      */       }
/*  422 */       return (JsonSerializer<?>)NumberSerializer.instance;
/*      */     } 
/*  424 */     if (Enum.class.isAssignableFrom(raw)) {
/*  425 */       return buildEnumSerializer(prov.getConfig(), type, beanDesc);
/*      */     }
/*  427 */     return null;
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
/*      */   protected JsonSerializer<?> findOptionalStdSerializer(SerializerProvider prov, JavaType type, BeanDescription beanDesc, boolean staticTyping) throws JsonMappingException {
/*  439 */     return OptionalHandlerFactory.instance.findSerializer(prov.getConfig(), type, beanDesc);
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
/*      */   protected final JsonSerializer<?> findSerializerByAddonType(SerializationConfig config, JavaType javaType, BeanDescription beanDesc, boolean staticTyping) throws JsonMappingException {
/*  453 */     Class<?> rawType = javaType.getRawClass();
/*      */     
/*  455 */     if (Iterator.class.isAssignableFrom(rawType)) {
/*  456 */       JavaType[] params = config.getTypeFactory().findTypeParameters(javaType, Iterator.class);
/*      */       
/*  458 */       JavaType vt = (params == null || params.length != 1) ? TypeFactory.unknownType() : params[0];
/*  459 */       return buildIteratorSerializer(config, javaType, beanDesc, staticTyping, vt);
/*      */     } 
/*  461 */     if (Iterable.class.isAssignableFrom(rawType)) {
/*  462 */       JavaType[] params = config.getTypeFactory().findTypeParameters(javaType, Iterable.class);
/*      */       
/*  464 */       JavaType vt = (params == null || params.length != 1) ? TypeFactory.unknownType() : params[0];
/*  465 */       return buildIterableSerializer(config, javaType, beanDesc, staticTyping, vt);
/*      */     } 
/*  467 */     if (CharSequence.class.isAssignableFrom(rawType)) {
/*  468 */       return (JsonSerializer<?>)ToStringSerializer.instance;
/*      */     }
/*  470 */     return null;
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
/*      */   protected JsonSerializer<Object> findSerializerFromAnnotation(SerializerProvider prov, Annotated a) throws JsonMappingException {
/*  485 */     Object serDef = prov.getAnnotationIntrospector().findSerializer(a);
/*  486 */     if (serDef == null) {
/*  487 */       return null;
/*      */     }
/*  489 */     JsonSerializer<Object> ser = prov.serializerInstance(a, serDef);
/*      */     
/*  491 */     return (JsonSerializer)findConvertingSerializer(prov, a, ser);
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
/*      */   protected JsonSerializer<?> findConvertingSerializer(SerializerProvider prov, Annotated a, JsonSerializer<?> ser) throws JsonMappingException {
/*  504 */     Converter<Object, Object> conv = findConverter(prov, a);
/*  505 */     if (conv == null) {
/*  506 */       return ser;
/*      */     }
/*  508 */     JavaType delegateType = conv.getOutputType(prov.getTypeFactory());
/*  509 */     return (JsonSerializer<?>)new StdDelegatingSerializer(conv, delegateType, ser);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Converter<Object, Object> findConverter(SerializerProvider prov, Annotated a) throws JsonMappingException {
/*  516 */     Object convDef = prov.getAnnotationIntrospector().findSerializationConverter(a);
/*  517 */     if (convDef == null) {
/*  518 */       return null;
/*      */     }
/*  520 */     return prov.converterInstance(a, convDef);
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
/*      */   protected JsonSerializer<?> buildContainerSerializer(SerializerProvider prov, JavaType type, BeanDescription beanDesc, boolean staticTyping) throws JsonMappingException {
/*  536 */     SerializationConfig config = prov.getConfig();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  542 */     if (!staticTyping && type.useStaticType() && (
/*  543 */       !type.isContainerType() || !type.getContentType().isJavaLangObject())) {
/*  544 */       staticTyping = true;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  549 */     JavaType elementType = type.getContentType();
/*  550 */     TypeSerializer elementTypeSerializer = createTypeSerializer(config, elementType);
/*      */ 
/*      */ 
/*      */     
/*  554 */     if (elementTypeSerializer != null) {
/*  555 */       staticTyping = false;
/*      */     }
/*  557 */     JsonSerializer<Object> elementValueSerializer = _findContentSerializer(prov, (Annotated)beanDesc
/*  558 */         .getClassInfo());
/*  559 */     if (type.isMapLikeType()) {
/*  560 */       MapLikeType mlt = (MapLikeType)type;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  566 */       JsonSerializer<Object> keySerializer = _findKeySerializer(prov, (Annotated)beanDesc.getClassInfo());
/*  567 */       if (mlt.isTrueMapType()) {
/*  568 */         return buildMapSerializer(prov, (MapType)mlt, beanDesc, staticTyping, keySerializer, elementTypeSerializer, elementValueSerializer);
/*      */       }
/*      */ 
/*      */       
/*  572 */       JsonSerializer<?> ser = null;
/*  573 */       MapLikeType mlType = (MapLikeType)type;
/*  574 */       for (Serializers serializers : customSerializers()) {
/*  575 */         ser = serializers.findMapLikeSerializer(config, mlType, beanDesc, keySerializer, elementTypeSerializer, elementValueSerializer);
/*      */         
/*  577 */         if (ser != null) {
/*      */           break;
/*      */         }
/*      */       } 
/*  581 */       if (ser == null) {
/*  582 */         ser = findSerializerByAnnotations(prov, type, beanDesc);
/*      */       }
/*  584 */       if (ser != null && 
/*  585 */         this._factoryConfig.hasSerializerModifiers()) {
/*  586 */         for (BeanSerializerModifier mod : this._factoryConfig.serializerModifiers()) {
/*  587 */           ser = mod.modifyMapLikeSerializer(config, mlType, beanDesc, ser);
/*      */         }
/*      */       }
/*      */       
/*  591 */       return ser;
/*      */     } 
/*  593 */     if (type.isCollectionLikeType()) {
/*  594 */       CollectionLikeType clt = (CollectionLikeType)type;
/*  595 */       if (clt.isTrueCollectionType()) {
/*  596 */         return buildCollectionSerializer(prov, (CollectionType)clt, beanDesc, staticTyping, elementTypeSerializer, elementValueSerializer);
/*      */       }
/*      */ 
/*      */       
/*  600 */       JsonSerializer<?> ser = null;
/*  601 */       CollectionLikeType clType = (CollectionLikeType)type;
/*  602 */       for (Serializers serializers : customSerializers()) {
/*  603 */         ser = serializers.findCollectionLikeSerializer(config, clType, beanDesc, elementTypeSerializer, elementValueSerializer);
/*      */         
/*  605 */         if (ser != null) {
/*      */           break;
/*      */         }
/*      */       } 
/*  609 */       if (ser == null) {
/*  610 */         ser = findSerializerByAnnotations(prov, type, beanDesc);
/*      */       }
/*  612 */       if (ser != null && 
/*  613 */         this._factoryConfig.hasSerializerModifiers()) {
/*  614 */         for (BeanSerializerModifier mod : this._factoryConfig.serializerModifiers()) {
/*  615 */           ser = mod.modifyCollectionLikeSerializer(config, clType, beanDesc, ser);
/*      */         }
/*      */       }
/*      */       
/*  619 */       return ser;
/*      */     } 
/*  621 */     if (type.isArrayType()) {
/*  622 */       return buildArraySerializer(prov, (ArrayType)type, beanDesc, staticTyping, elementTypeSerializer, elementValueSerializer);
/*      */     }
/*      */     
/*  625 */     return null;
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
/*      */   protected JsonSerializer<?> buildCollectionSerializer(SerializerProvider prov, CollectionType type, BeanDescription beanDesc, boolean staticTyping, TypeSerializer elementTypeSerializer, JsonSerializer<Object> elementValueSerializer) throws JsonMappingException {
/*      */     ContainerSerializer<?> containerSerializer;
/*      */     JsonSerializer<?> jsonSerializer1;
/*  639 */     SerializationConfig config = prov.getConfig();
/*  640 */     JsonSerializer<?> ser = null;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  645 */     for (Serializers serializers : customSerializers()) {
/*  646 */       ser = serializers.findCollectionSerializer(config, type, beanDesc, elementTypeSerializer, elementValueSerializer);
/*      */       
/*  648 */       if (ser != null) {
/*      */         break;
/*      */       }
/*      */     } 
/*      */     
/*  653 */     if (ser == null) {
/*  654 */       ser = findSerializerByAnnotations(prov, (JavaType)type, beanDesc);
/*  655 */       if (ser == null) {
/*      */ 
/*      */         
/*  658 */         JsonFormat.Value format = beanDesc.findExpectedFormat(null);
/*  659 */         if (format != null && format.getShape() == JsonFormat.Shape.OBJECT) {
/*  660 */           return null;
/*      */         }
/*  662 */         Class<?> raw = type.getRawClass();
/*  663 */         if (EnumSet.class.isAssignableFrom(raw)) {
/*      */           
/*  665 */           JavaType enumType = type.getContentType();
/*      */           
/*  667 */           if (!enumType.isEnumType()) {
/*  668 */             enumType = null;
/*      */           }
/*  670 */           ser = buildEnumSetSerializer(enumType);
/*      */         } else {
/*  672 */           StringCollectionSerializer stringCollectionSerializer; Class<?> elementRaw = type.getContentType().getRawClass();
/*  673 */           if (isIndexedList(raw)) {
/*  674 */             if (elementRaw == String.class) {
/*      */               
/*  676 */               if (ClassUtil.isJacksonStdImpl(elementValueSerializer)) {
/*  677 */                 IndexedStringListSerializer indexedStringListSerializer = IndexedStringListSerializer.instance;
/*      */               }
/*      */             } else {
/*  680 */               containerSerializer = buildIndexedListSerializer(type.getContentType(), staticTyping, elementTypeSerializer, elementValueSerializer);
/*      */             }
/*      */           
/*  683 */           } else if (elementRaw == String.class) {
/*      */             
/*  685 */             if (ClassUtil.isJacksonStdImpl(elementValueSerializer)) {
/*  686 */               stringCollectionSerializer = StringCollectionSerializer.instance;
/*      */             }
/*      */           } 
/*  689 */           if (stringCollectionSerializer == null) {
/*  690 */             containerSerializer = buildCollectionSerializer(type.getContentType(), staticTyping, elementTypeSerializer, elementValueSerializer);
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  697 */     if (this._factoryConfig.hasSerializerModifiers()) {
/*  698 */       for (BeanSerializerModifier mod : this._factoryConfig.serializerModifiers()) {
/*  699 */         jsonSerializer1 = mod.modifyCollectionSerializer(config, type, beanDesc, (JsonSerializer<?>)containerSerializer);
/*      */       }
/*      */     }
/*  702 */     return jsonSerializer1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean isIndexedList(Class<?> cls) {
/*  713 */     return RandomAccess.class.isAssignableFrom(cls);
/*      */   }
/*      */ 
/*      */   
/*      */   public ContainerSerializer<?> buildIndexedListSerializer(JavaType elemType, boolean staticTyping, TypeSerializer vts, JsonSerializer<Object> valueSerializer) {
/*  718 */     return (ContainerSerializer<?>)new IndexedListSerializer(elemType, staticTyping, vts, valueSerializer);
/*      */   }
/*      */ 
/*      */   
/*      */   public ContainerSerializer<?> buildCollectionSerializer(JavaType elemType, boolean staticTyping, TypeSerializer vts, JsonSerializer<Object> valueSerializer) {
/*  723 */     return (ContainerSerializer<?>)new CollectionSerializer(elemType, staticTyping, vts, valueSerializer);
/*      */   }
/*      */   
/*      */   public JsonSerializer<?> buildEnumSetSerializer(JavaType enumType) {
/*  727 */     return (JsonSerializer<?>)new EnumSetSerializer(enumType);
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
/*      */   protected JsonSerializer<?> buildMapSerializer(SerializerProvider prov, MapType type, BeanDescription beanDesc, boolean staticTyping, JsonSerializer<Object> keySerializer, TypeSerializer elementTypeSerializer, JsonSerializer<Object> elementValueSerializer) throws JsonMappingException {
/*      */     MapSerializer mapSerializer;
/*      */     JsonSerializer<?> jsonSerializer1;
/*  748 */     JsonFormat.Value format = beanDesc.findExpectedFormat(null);
/*  749 */     if (format != null && format.getShape() == JsonFormat.Shape.OBJECT) {
/*  750 */       return null;
/*      */     }
/*      */     
/*  753 */     JsonSerializer<?> ser = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  760 */     SerializationConfig config = prov.getConfig();
/*  761 */     for (Serializers serializers : customSerializers()) {
/*  762 */       ser = serializers.findMapSerializer(config, type, beanDesc, keySerializer, elementTypeSerializer, elementValueSerializer);
/*      */       
/*  764 */       if (ser != null)
/*      */         break; 
/*  766 */     }  if (ser == null) {
/*  767 */       ser = findSerializerByAnnotations(prov, (JavaType)type, beanDesc);
/*  768 */       if (ser == null) {
/*  769 */         Object filterId = findFilterId(config, beanDesc);
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  774 */         JsonIgnoreProperties.Value ignorals = config.getDefaultPropertyIgnorals(Map.class, beanDesc
/*  775 */             .getClassInfo());
/*      */         
/*  777 */         Set<String> ignored = (ignorals == null) ? null : ignorals.findIgnoredForSerialization();
/*  778 */         MapSerializer mapSer = MapSerializer.construct(ignored, (JavaType)type, staticTyping, elementTypeSerializer, keySerializer, elementValueSerializer, filterId);
/*      */ 
/*      */         
/*  781 */         mapSerializer = _checkMapContentInclusion(prov, beanDesc, mapSer);
/*      */       } 
/*      */     } 
/*      */     
/*  785 */     if (this._factoryConfig.hasSerializerModifiers()) {
/*  786 */       for (BeanSerializerModifier mod : this._factoryConfig.serializerModifiers()) {
/*  787 */         jsonSerializer1 = mod.modifyMapSerializer(config, type, beanDesc, (JsonSerializer<?>)mapSerializer);
/*      */       }
/*      */     }
/*  790 */     return jsonSerializer1;
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
/*      */   protected MapSerializer _checkMapContentInclusion(SerializerProvider prov, BeanDescription beanDesc, MapSerializer mapSer) throws JsonMappingException {
/*  804 */     JavaType contentType = mapSer.getContentType();
/*  805 */     JsonInclude.Value inclV = _findInclusionWithContent(prov, beanDesc, contentType, Map.class);
/*      */ 
/*      */ 
/*      */     
/*  809 */     JsonInclude.Include incl = (inclV == null) ? JsonInclude.Include.USE_DEFAULTS : inclV.getContentInclusion();
/*  810 */     if (incl == JsonInclude.Include.USE_DEFAULTS || incl == JsonInclude.Include.ALWAYS) {
/*      */       
/*  812 */       if (!prov.isEnabled(SerializationFeature.WRITE_NULL_MAP_VALUES)) {
/*  813 */         return mapSer.withContentInclusion(null, true);
/*      */       }
/*  815 */       return mapSer;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  821 */     boolean suppressNulls = true;
/*      */     
/*  823 */     switch (incl)
/*      */     { case NON_DEFAULT:
/*  825 */         valueToSuppress = BeanUtil.getDefaultValue(contentType);
/*  826 */         if (valueToSuppress != null && 
/*  827 */           valueToSuppress.getClass().isArray()) {
/*  828 */           valueToSuppress = ArrayBuilders.getArrayComparator(valueToSuppress);
/*      */         }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  853 */         return mapSer.withContentInclusion(valueToSuppress, suppressNulls);case NON_ABSENT: valueToSuppress = contentType.isReferenceType() ? MapSerializer.MARKER_FOR_EMPTY : null; return mapSer.withContentInclusion(valueToSuppress, suppressNulls);case NON_EMPTY: valueToSuppress = MapSerializer.MARKER_FOR_EMPTY; return mapSer.withContentInclusion(valueToSuppress, suppressNulls);case CUSTOM: valueToSuppress = prov.includeFilterInstance(null, inclV.getContentFilter()); if (valueToSuppress == null) { suppressNulls = true; } else { suppressNulls = prov.includeFilterSuppressNulls(valueToSuppress); }  return mapSer.withContentInclusion(valueToSuppress, suppressNulls); }  Object valueToSuppress = null; return mapSer.withContentInclusion(valueToSuppress, suppressNulls);
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
/*      */   protected JsonSerializer<?> buildMapEntrySerializer(SerializerProvider prov, JavaType type, BeanDescription beanDesc, boolean staticTyping, JavaType keyType, JavaType valueType) throws JsonMappingException {
/*  866 */     JsonFormat.Value formatOverride = prov.getDefaultPropertyFormat(Map.Entry.class);
/*  867 */     JsonFormat.Value formatFromAnnotation = beanDesc.findExpectedFormat(null);
/*  868 */     JsonFormat.Value format = JsonFormat.Value.merge(formatFromAnnotation, formatOverride);
/*  869 */     if (format.getShape() == JsonFormat.Shape.OBJECT) {
/*  870 */       return null;
/*      */     }
/*      */     
/*  873 */     MapEntrySerializer ser = new MapEntrySerializer(valueType, keyType, valueType, staticTyping, createTypeSerializer(prov.getConfig(), valueType), null);
/*      */     
/*  875 */     JavaType contentType = ser.getContentType();
/*  876 */     JsonInclude.Value inclV = _findInclusionWithContent(prov, beanDesc, contentType, Map.Entry.class);
/*      */ 
/*      */ 
/*      */     
/*  880 */     JsonInclude.Include incl = (inclV == null) ? JsonInclude.Include.USE_DEFAULTS : inclV.getContentInclusion();
/*  881 */     if (incl == JsonInclude.Include.USE_DEFAULTS || incl == JsonInclude.Include.ALWAYS)
/*      */     {
/*  883 */       return (JsonSerializer<?>)ser;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  889 */     boolean suppressNulls = true;
/*      */     
/*  891 */     switch (incl)
/*      */     { case NON_DEFAULT:
/*  893 */         valueToSuppress = BeanUtil.getDefaultValue(contentType);
/*  894 */         if (valueToSuppress != null && 
/*  895 */           valueToSuppress.getClass().isArray()) {
/*  896 */           valueToSuppress = ArrayBuilders.getArrayComparator(valueToSuppress);
/*      */         }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  920 */         return (JsonSerializer<?>)ser.withContentInclusion(valueToSuppress, suppressNulls);case NON_ABSENT: valueToSuppress = contentType.isReferenceType() ? MapSerializer.MARKER_FOR_EMPTY : null; return (JsonSerializer<?>)ser.withContentInclusion(valueToSuppress, suppressNulls);case NON_EMPTY: valueToSuppress = MapSerializer.MARKER_FOR_EMPTY; return (JsonSerializer<?>)ser.withContentInclusion(valueToSuppress, suppressNulls);case CUSTOM: valueToSuppress = prov.includeFilterInstance(null, inclV.getContentFilter()); if (valueToSuppress == null) { suppressNulls = true; } else { suppressNulls = prov.includeFilterSuppressNulls(valueToSuppress); }  return (JsonSerializer<?>)ser.withContentInclusion(valueToSuppress, suppressNulls); }  Object valueToSuppress = null; return (JsonSerializer<?>)ser.withContentInclusion(valueToSuppress, suppressNulls);
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
/*      */   protected JsonInclude.Value _findInclusionWithContent(SerializerProvider prov, BeanDescription beanDesc, JavaType contentType, Class<?> configType) throws JsonMappingException {
/*  936 */     SerializationConfig config = prov.getConfig();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  943 */     JsonInclude.Value inclV = beanDesc.findPropertyInclusion(config.getDefaultPropertyInclusion());
/*  944 */     inclV = config.getDefaultPropertyInclusion(configType, inclV);
/*      */ 
/*      */ 
/*      */     
/*  948 */     JsonInclude.Value valueIncl = config.getDefaultPropertyInclusion(contentType.getRawClass(), null);
/*      */     
/*  950 */     if (valueIncl != null) {
/*  951 */       switch (valueIncl.getValueInclusion()) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         case USE_DEFAULTS:
/*  961 */           return inclV;
/*      */         case CUSTOM:
/*      */           inclV = inclV.withContentFilter(valueIncl.getContentFilter());
/*      */       } 
/*      */       inclV = inclV.withContentInclusion(valueIncl.getValueInclusion());
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
/*      */   protected JsonSerializer<?> buildArraySerializer(SerializerProvider prov, ArrayType type, BeanDescription beanDesc, boolean staticTyping, TypeSerializer elementTypeSerializer, JsonSerializer<Object> elementValueSerializer) throws JsonMappingException {
/*      */     ObjectArraySerializer objectArraySerializer;
/*      */     JsonSerializer<?> jsonSerializer1;
/*  984 */     SerializationConfig config = prov.getConfig();
/*  985 */     JsonSerializer<?> ser = null;
/*      */     
/*  987 */     for (Serializers serializers : customSerializers()) {
/*  988 */       ser = serializers.findArraySerializer(config, type, beanDesc, elementTypeSerializer, elementValueSerializer);
/*      */       
/*  990 */       if (ser != null) {
/*      */         break;
/*      */       }
/*      */     } 
/*      */     
/*  995 */     if (ser == null) {
/*  996 */       Class<?> raw = type.getRawClass();
/*      */       
/*  998 */       if (elementValueSerializer == null || ClassUtil.isJacksonStdImpl(elementValueSerializer)) {
/*  999 */         if (String[].class == raw) {
/* 1000 */           StringArraySerializer stringArraySerializer = StringArraySerializer.instance;
/*      */         } else {
/*      */           
/* 1003 */           ser = StdArraySerializers.findStandardImpl(raw);
/*      */         } 
/*      */       }
/* 1006 */       if (ser == null) {
/* 1007 */         objectArraySerializer = new ObjectArraySerializer(type.getContentType(), staticTyping, elementTypeSerializer, elementValueSerializer);
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 1012 */     if (this._factoryConfig.hasSerializerModifiers()) {
/* 1013 */       for (BeanSerializerModifier mod : this._factoryConfig.serializerModifiers()) {
/* 1014 */         jsonSerializer1 = mod.modifyArraySerializer(config, type, beanDesc, (JsonSerializer<?>)objectArraySerializer);
/*      */       }
/*      */     }
/* 1017 */     return jsonSerializer1;
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
/*      */   public JsonSerializer<?> findReferenceSerializer(SerializerProvider prov, ReferenceType refType, BeanDescription beanDesc, boolean staticTyping) throws JsonMappingException {
/* 1034 */     JavaType contentType = refType.getContentType();
/* 1035 */     TypeSerializer contentTypeSerializer = (TypeSerializer)contentType.getTypeHandler();
/* 1036 */     SerializationConfig config = prov.getConfig();
/* 1037 */     if (contentTypeSerializer == null) {
/* 1038 */       contentTypeSerializer = createTypeSerializer(config, contentType);
/*      */     }
/* 1040 */     JsonSerializer<Object> contentSerializer = (JsonSerializer<Object>)contentType.getValueHandler();
/* 1041 */     for (Serializers serializers : customSerializers()) {
/* 1042 */       JsonSerializer<?> ser = serializers.findReferenceSerializer(config, refType, beanDesc, contentTypeSerializer, contentSerializer);
/*      */       
/* 1044 */       if (ser != null) {
/* 1045 */         return ser;
/*      */       }
/*      */     } 
/* 1048 */     if (refType.isTypeOrSubTypeOf(AtomicReference.class)) {
/* 1049 */       return buildAtomicReferenceSerializer(prov, refType, beanDesc, staticTyping, contentTypeSerializer, contentSerializer);
/*      */     }
/*      */     
/* 1052 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected JsonSerializer<?> buildAtomicReferenceSerializer(SerializerProvider prov, ReferenceType refType, BeanDescription beanDesc, boolean staticTyping, TypeSerializer contentTypeSerializer, JsonSerializer<Object> contentSerializer) throws JsonMappingException {
/*      */     Object valueToSuppress;
/*      */     boolean suppressNulls;
/* 1060 */     JavaType contentType = refType.getReferencedType();
/* 1061 */     JsonInclude.Value inclV = _findInclusionWithContent(prov, beanDesc, contentType, AtomicReference.class);
/*      */ 
/*      */ 
/*      */     
/* 1065 */     JsonInclude.Include incl = (inclV == null) ? JsonInclude.Include.USE_DEFAULTS : inclV.getContentInclusion();
/*      */ 
/*      */ 
/*      */     
/* 1069 */     if (incl == JsonInclude.Include.USE_DEFAULTS || incl == JsonInclude.Include.ALWAYS)
/*      */     
/* 1071 */     { valueToSuppress = null;
/* 1072 */       suppressNulls = false; }
/*      */     else
/* 1074 */     { AtomicReferenceSerializer ser; suppressNulls = true;
/* 1075 */       switch (incl)
/*      */       { case NON_DEFAULT:
/* 1077 */           valueToSuppress = BeanUtil.getDefaultValue(contentType);
/* 1078 */           if (valueToSuppress != null && 
/* 1079 */             valueToSuppress.getClass().isArray()) {
/* 1080 */             valueToSuppress = ArrayBuilders.getArrayComparator(valueToSuppress);
/*      */           }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1105 */           ser = new AtomicReferenceSerializer(refType, staticTyping, contentTypeSerializer, contentSerializer);
/*      */           
/* 1107 */           return (JsonSerializer<?>)ser.withContentInclusion(valueToSuppress, suppressNulls);case NON_ABSENT: valueToSuppress = contentType.isReferenceType() ? MapSerializer.MARKER_FOR_EMPTY : null; ser = new AtomicReferenceSerializer(refType, staticTyping, contentTypeSerializer, contentSerializer); return (JsonSerializer<?>)ser.withContentInclusion(valueToSuppress, suppressNulls);case NON_EMPTY: valueToSuppress = MapSerializer.MARKER_FOR_EMPTY; ser = new AtomicReferenceSerializer(refType, staticTyping, contentTypeSerializer, contentSerializer); return (JsonSerializer<?>)ser.withContentInclusion(valueToSuppress, suppressNulls);case CUSTOM: valueToSuppress = prov.includeFilterInstance(null, inclV.getContentFilter()); if (valueToSuppress == null) { suppressNulls = true; } else { suppressNulls = prov.includeFilterSuppressNulls(valueToSuppress); }  ser = new AtomicReferenceSerializer(refType, staticTyping, contentTypeSerializer, contentSerializer); return (JsonSerializer<?>)ser.withContentInclusion(valueToSuppress, suppressNulls); }  valueToSuppress = null; }  AtomicReferenceSerializer atomicReferenceSerializer = new AtomicReferenceSerializer(refType, staticTyping, contentTypeSerializer, contentSerializer); return (JsonSerializer<?>)atomicReferenceSerializer.withContentInclusion(valueToSuppress, suppressNulls);
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
/*      */   protected JsonSerializer<?> buildIteratorSerializer(SerializationConfig config, JavaType type, BeanDescription beanDesc, boolean staticTyping, JavaType valueType) throws JsonMappingException {
/* 1124 */     return (JsonSerializer<?>)new IteratorSerializer(valueType, staticTyping, createTypeSerializer(config, valueType));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected JsonSerializer<?> buildIterableSerializer(SerializationConfig config, JavaType type, BeanDescription beanDesc, boolean staticTyping, JavaType valueType) throws JsonMappingException {
/* 1135 */     return (JsonSerializer<?>)new IterableSerializer(valueType, staticTyping, createTypeSerializer(config, valueType));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected JsonSerializer<?> buildEnumSerializer(SerializationConfig config, JavaType type, BeanDescription beanDesc) throws JsonMappingException {
/*      */     JsonSerializer<?> jsonSerializer;
/* 1147 */     JsonFormat.Value format = beanDesc.findExpectedFormat(null);
/* 1148 */     if (format != null && format.getShape() == JsonFormat.Shape.OBJECT) {
/*      */       
/* 1150 */       ((BasicBeanDescription)beanDesc).removeProperty("declaringClass");
/*      */       
/* 1152 */       return null;
/*      */     } 
/*      */     
/* 1155 */     Class<Enum<?>> enumClass = type.getRawClass();
/* 1156 */     EnumSerializer enumSerializer = EnumSerializer.construct(enumClass, config, beanDesc, format);
/*      */     
/* 1158 */     if (this._factoryConfig.hasSerializerModifiers()) {
/* 1159 */       for (BeanSerializerModifier mod : this._factoryConfig.serializerModifiers()) {
/* 1160 */         jsonSerializer = mod.modifyEnumSerializer(config, type, beanDesc, (JsonSerializer<?>)enumSerializer);
/*      */       }
/*      */     }
/* 1163 */     return jsonSerializer;
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
/*      */   protected JsonSerializer<Object> _findKeySerializer(SerializerProvider prov, Annotated a) throws JsonMappingException {
/* 1181 */     AnnotationIntrospector intr = prov.getAnnotationIntrospector();
/* 1182 */     Object serDef = intr.findKeySerializer(a);
/* 1183 */     if (serDef != null) {
/* 1184 */       return prov.serializerInstance(a, serDef);
/*      */     }
/* 1186 */     return null;
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
/*      */   protected JsonSerializer<Object> _findContentSerializer(SerializerProvider prov, Annotated a) throws JsonMappingException {
/* 1198 */     AnnotationIntrospector intr = prov.getAnnotationIntrospector();
/* 1199 */     Object serDef = intr.findContentSerializer(a);
/* 1200 */     if (serDef != null) {
/* 1201 */       return prov.serializerInstance(a, serDef);
/*      */     }
/* 1203 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Object findFilterId(SerializationConfig config, BeanDescription beanDesc) {
/* 1211 */     return config.getAnnotationIntrospector().findFilterId((Annotated)beanDesc.getClassInfo());
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
/*      */   protected boolean usesStaticTyping(SerializationConfig config, BeanDescription beanDesc, TypeSerializer typeSer) {
/* 1228 */     if (typeSer != null) {
/* 1229 */       return false;
/*      */     }
/* 1231 */     AnnotationIntrospector intr = config.getAnnotationIntrospector();
/* 1232 */     JsonSerialize.Typing t = intr.findSerializationTyping((Annotated)beanDesc.getClassInfo());
/* 1233 */     if (t != null && t != JsonSerialize.Typing.DEFAULT_TYPING) {
/* 1234 */       return (t == JsonSerialize.Typing.STATIC);
/*      */     }
/* 1236 */     return config.isEnabled(MapperFeature.USE_STATIC_TYPING);
/*      */   }
/*      */   
/*      */   public abstract SerializerFactory withConfig(SerializerFactoryConfig paramSerializerFactoryConfig);
/*      */   
/*      */   public abstract JsonSerializer<Object> createSerializer(SerializerProvider paramSerializerProvider, JavaType paramJavaType) throws JsonMappingException;
/*      */   
/*      */   protected abstract Iterable<Serializers> customSerializers();
/*      */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\ser\BasicSerializerFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */