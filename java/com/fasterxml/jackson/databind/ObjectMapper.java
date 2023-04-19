/*      */ package com.fasterxml.jackson.databind;
/*      */ 
/*      */ import com.fasterxml.jackson.annotation.JsonAutoDetect;
/*      */ import com.fasterxml.jackson.annotation.JsonInclude;
/*      */ import com.fasterxml.jackson.annotation.JsonSetter;
/*      */ import com.fasterxml.jackson.annotation.JsonTypeInfo;
/*      */ import com.fasterxml.jackson.annotation.PropertyAccessor;
/*      */ import com.fasterxml.jackson.core.Base64Variant;
/*      */ import com.fasterxml.jackson.core.Base64Variants;
/*      */ import com.fasterxml.jackson.core.FormatSchema;
/*      */ import com.fasterxml.jackson.core.JsonEncoding;
/*      */ import com.fasterxml.jackson.core.JsonFactory;
/*      */ import com.fasterxml.jackson.core.JsonGenerationException;
/*      */ import com.fasterxml.jackson.core.JsonGenerator;
/*      */ import com.fasterxml.jackson.core.JsonParseException;
/*      */ import com.fasterxml.jackson.core.JsonParser;
/*      */ import com.fasterxml.jackson.core.JsonProcessingException;
/*      */ import com.fasterxml.jackson.core.JsonToken;
/*      */ import com.fasterxml.jackson.core.ObjectCodec;
/*      */ import com.fasterxml.jackson.core.PrettyPrinter;
/*      */ import com.fasterxml.jackson.core.StreamReadFeature;
/*      */ import com.fasterxml.jackson.core.StreamWriteFeature;
/*      */ import com.fasterxml.jackson.core.TreeNode;
/*      */ import com.fasterxml.jackson.core.Version;
/*      */ import com.fasterxml.jackson.core.Versioned;
/*      */ import com.fasterxml.jackson.core.io.CharacterEscapes;
/*      */ import com.fasterxml.jackson.core.io.SegmentedStringWriter;
/*      */ import com.fasterxml.jackson.core.type.ResolvedType;
/*      */ import com.fasterxml.jackson.core.type.TypeReference;
/*      */ import com.fasterxml.jackson.core.util.ByteArrayBuilder;
/*      */ import com.fasterxml.jackson.databind.cfg.BaseSettings;
/*      */ import com.fasterxml.jackson.databind.cfg.ConfigOverrides;
/*      */ import com.fasterxml.jackson.databind.cfg.ContextAttributes;
/*      */ import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
/*      */ import com.fasterxml.jackson.databind.cfg.MapperConfig;
/*      */ import com.fasterxml.jackson.databind.cfg.MutableConfigOverride;
/*      */ import com.fasterxml.jackson.databind.cfg.PackageVersion;
/*      */ import com.fasterxml.jackson.databind.deser.BeanDeserializerFactory;
/*      */ import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
/*      */ import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;
/*      */ import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;
/*      */ import com.fasterxml.jackson.databind.deser.DeserializerFactory;
/*      */ import com.fasterxml.jackson.databind.deser.Deserializers;
/*      */ import com.fasterxml.jackson.databind.deser.KeyDeserializers;
/*      */ import com.fasterxml.jackson.databind.deser.ValueInstantiators;
/*      */ import com.fasterxml.jackson.databind.exc.MismatchedInputException;
/*      */ import com.fasterxml.jackson.databind.introspect.BasicClassIntrospector;
/*      */ import com.fasterxml.jackson.databind.introspect.ClassIntrospector;
/*      */ import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
/*      */ import com.fasterxml.jackson.databind.introspect.SimpleMixInResolver;
/*      */ import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
/*      */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
/*      */ import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
/*      */ import com.fasterxml.jackson.databind.jsontype.NamedType;
/*      */ import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
/*      */ import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
/*      */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*      */ import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
/*      */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*      */ import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
/*      */ import com.fasterxml.jackson.databind.jsontype.impl.StdSubtypeResolver;
/*      */ import com.fasterxml.jackson.databind.jsontype.impl.StdTypeResolverBuilder;
/*      */ import com.fasterxml.jackson.databind.node.ArrayNode;
/*      */ import com.fasterxml.jackson.databind.node.JsonNodeFactory;
/*      */ import com.fasterxml.jackson.databind.node.NullNode;
/*      */ import com.fasterxml.jackson.databind.node.ObjectNode;
/*      */ import com.fasterxml.jackson.databind.node.POJONode;
/*      */ import com.fasterxml.jackson.databind.node.TreeTraversingParser;
/*      */ import com.fasterxml.jackson.databind.ser.BeanSerializerFactory;
/*      */ import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
/*      */ import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
/*      */ import com.fasterxml.jackson.databind.ser.FilterProvider;
/*      */ import com.fasterxml.jackson.databind.ser.SerializerFactory;
/*      */ import com.fasterxml.jackson.databind.ser.Serializers;
/*      */ import com.fasterxml.jackson.databind.type.TypeFactory;
/*      */ import com.fasterxml.jackson.databind.type.TypeModifier;
/*      */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*      */ import com.fasterxml.jackson.databind.util.RootNameLookup;
/*      */ import com.fasterxml.jackson.databind.util.StdDateFormat;
/*      */ import com.fasterxml.jackson.databind.util.TokenBuffer;
/*      */ import java.io.Closeable;
/*      */ import java.io.DataInput;
/*      */ import java.io.DataOutput;
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.io.Reader;
/*      */ import java.io.Serializable;
/*      */ import java.io.Writer;
/*      */ import java.lang.reflect.Type;
/*      */ import java.net.URL;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.text.DateFormat;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.Objects;
/*      */ import java.util.ServiceLoader;
/*      */ import java.util.Set;
/*      */ import java.util.TimeZone;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import java.util.concurrent.atomic.AtomicReference;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class ObjectMapper
/*      */   extends ObjectCodec
/*      */   implements Versioned, Serializable
/*      */ {
/*      */   private static final long serialVersionUID = 2L;
/*      */   
/*      */   public enum DefaultTyping
/*      */   {
/*  157 */     JAVA_LANG_OBJECT,
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  167 */     OBJECT_AND_NON_CONCRETE,
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  176 */     NON_CONCRETE_AND_ARRAYS,
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  187 */     NON_FINAL,
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  203 */     EVERYTHING;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class DefaultTypeResolverBuilder
/*      */     extends StdTypeResolverBuilder
/*      */     implements Serializable
/*      */   {
/*      */     private static final long serialVersionUID = 1L;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected final ObjectMapper.DefaultTyping _appliesFor;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected final PolymorphicTypeValidator _subtypeValidator;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     public DefaultTypeResolverBuilder(ObjectMapper.DefaultTyping t) {
/*  241 */       this(t, (PolymorphicTypeValidator)LaissezFaireSubTypeValidator.instance);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public DefaultTypeResolverBuilder(ObjectMapper.DefaultTyping t, PolymorphicTypeValidator ptv) {
/*  248 */       this._appliesFor = Objects.<ObjectMapper.DefaultTyping>requireNonNull(t, "Can not pass `null` DefaultTyping");
/*  249 */       this._subtypeValidator = Objects.<PolymorphicTypeValidator>requireNonNull(ptv, "Can not pass `null` PolymorphicTypeValidator");
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static DefaultTypeResolverBuilder construct(ObjectMapper.DefaultTyping t, PolymorphicTypeValidator ptv) {
/*  257 */       return new DefaultTypeResolverBuilder(t, ptv);
/*      */     }
/*      */ 
/*      */     
/*      */     public PolymorphicTypeValidator subTypeValidator(MapperConfig<?> config) {
/*  262 */       return this._subtypeValidator;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public TypeDeserializer buildTypeDeserializer(DeserializationConfig config, JavaType baseType, Collection<NamedType> subtypes) {
/*  269 */       return useForType(baseType) ? super.buildTypeDeserializer(config, baseType, subtypes) : null;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public TypeSerializer buildTypeSerializer(SerializationConfig config, JavaType baseType, Collection<NamedType> subtypes) {
/*  276 */       return useForType(baseType) ? super.buildTypeSerializer(config, baseType, subtypes) : null;
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
/*      */     public boolean useForType(JavaType t) {
/*  291 */       if (t.isPrimitive()) {
/*  292 */         return false;
/*      */       }
/*      */       
/*  295 */       switch (this._appliesFor) {
/*      */         case NON_CONCRETE_AND_ARRAYS:
/*  297 */           while (t.isArrayType()) {
/*  298 */             t = t.getContentType();
/*      */           }
/*      */ 
/*      */         
/*      */         case OBJECT_AND_NON_CONCRETE:
/*  303 */           while (t.isReferenceType()) {
/*  304 */             t = t.getReferencedType();
/*      */           }
/*  306 */           return (t.isJavaLangObject() || (
/*  307 */             !t.isConcrete() && 
/*      */             
/*  309 */             !TreeNode.class.isAssignableFrom(t.getRawClass())));
/*      */         
/*      */         case NON_FINAL:
/*  312 */           while (t.isArrayType()) {
/*  313 */             t = t.getContentType();
/*      */           }
/*      */           
/*  316 */           while (t.isReferenceType()) {
/*  317 */             t = t.getReferencedType();
/*      */           }
/*      */           
/*  320 */           return (!t.isFinal() && !TreeNode.class.isAssignableFrom(t.getRawClass()));
/*      */ 
/*      */         
/*      */         case EVERYTHING:
/*  324 */           return true;
/*      */       } 
/*      */       
/*  327 */       return t.isJavaLangObject();
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
/*  341 */   protected static final AnnotationIntrospector DEFAULT_ANNOTATION_INTROSPECTOR = (AnnotationIntrospector)new JacksonAnnotationIntrospector();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  347 */   protected static final BaseSettings DEFAULT_BASE = new BaseSettings(null, DEFAULT_ANNOTATION_INTROSPECTOR, null, 
/*      */ 
/*      */       
/*  350 */       TypeFactory.defaultInstance(), null, (DateFormat)StdDateFormat.instance, null, 
/*      */       
/*  352 */       Locale.getDefault(), null, 
/*      */       
/*  354 */       Base64Variants.getDefaultVariant(), (PolymorphicTypeValidator)LaissezFaireSubTypeValidator.instance);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final JsonFactory _jsonFactory;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected TypeFactory _typeFactory;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected InjectableValues _injectableValues;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected SubtypeResolver _subtypeResolver;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final ConfigOverrides _configOverrides;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected SimpleMixInResolver _mixIns;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected SerializationConfig _serializationConfig;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected DefaultSerializerProvider _serializerProvider;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected SerializerFactory _serializerFactory;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected DeserializationConfig _deserializationConfig;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected DefaultDeserializationContext _deserializationContext;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Set<Object> _registeredModuleTypes;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  509 */   protected final ConcurrentHashMap<JavaType, JsonDeserializer<Object>> _rootDeserializers = new ConcurrentHashMap<>(64, 0.6F, 2);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectMapper() {
/*  531 */     this(null, null, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectMapper(JsonFactory jf) {
/*  540 */     this(jf, null, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected ObjectMapper(ObjectMapper src) {
/*  550 */     this._jsonFactory = src._jsonFactory.copy();
/*  551 */     this._jsonFactory.setCodec(this);
/*  552 */     this._subtypeResolver = src._subtypeResolver;
/*  553 */     this._typeFactory = src._typeFactory;
/*  554 */     this._injectableValues = src._injectableValues;
/*  555 */     this._configOverrides = src._configOverrides.copy();
/*  556 */     this._mixIns = src._mixIns.copy();
/*      */     
/*  558 */     RootNameLookup rootNames = new RootNameLookup();
/*  559 */     this._serializationConfig = new SerializationConfig(src._serializationConfig, this._mixIns, rootNames, this._configOverrides);
/*      */     
/*  561 */     this._deserializationConfig = new DeserializationConfig(src._deserializationConfig, this._mixIns, rootNames, this._configOverrides);
/*      */     
/*  563 */     this._serializerProvider = src._serializerProvider.copy();
/*  564 */     this._deserializationContext = src._deserializationContext.copy();
/*      */ 
/*      */     
/*  567 */     this._serializerFactory = src._serializerFactory;
/*      */ 
/*      */     
/*  570 */     Set<Object> reg = src._registeredModuleTypes;
/*  571 */     if (reg == null) {
/*  572 */       this._registeredModuleTypes = null;
/*      */     } else {
/*  574 */       this._registeredModuleTypes = new LinkedHashSet(reg);
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
/*      */   public ObjectMapper(JsonFactory jf, DefaultSerializerProvider sp, DefaultDeserializationContext dc) {
/*  599 */     if (jf == null) {
/*  600 */       this._jsonFactory = new MappingJsonFactory(this);
/*      */     } else {
/*  602 */       this._jsonFactory = jf;
/*  603 */       if (jf.getCodec() == null) {
/*  604 */         this._jsonFactory.setCodec(this);
/*      */       }
/*      */     } 
/*  607 */     this._subtypeResolver = (SubtypeResolver)new StdSubtypeResolver();
/*  608 */     RootNameLookup rootNames = new RootNameLookup();
/*      */     
/*  610 */     this._typeFactory = TypeFactory.defaultInstance();
/*      */     
/*  612 */     SimpleMixInResolver mixins = new SimpleMixInResolver(null);
/*  613 */     this._mixIns = mixins;
/*  614 */     BaseSettings base = DEFAULT_BASE.withClassIntrospector(defaultClassIntrospector());
/*  615 */     this._configOverrides = new ConfigOverrides();
/*  616 */     this._serializationConfig = new SerializationConfig(base, this._subtypeResolver, mixins, rootNames, this._configOverrides);
/*      */     
/*  618 */     this._deserializationConfig = new DeserializationConfig(base, this._subtypeResolver, mixins, rootNames, this._configOverrides);
/*      */ 
/*      */ 
/*      */     
/*  622 */     boolean needOrder = this._jsonFactory.requiresPropertyOrdering();
/*  623 */     if (needOrder ^ this._serializationConfig.isEnabled(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY)) {
/*  624 */       configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, needOrder);
/*      */     }
/*      */     
/*  627 */     this._serializerProvider = (sp == null) ? (DefaultSerializerProvider)new DefaultSerializerProvider.Impl() : sp;
/*  628 */     this._deserializationContext = (dc == null) ? (DefaultDeserializationContext)new DefaultDeserializationContext.Impl((DeserializerFactory)BeanDeserializerFactory.instance) : dc;
/*      */ 
/*      */ 
/*      */     
/*  632 */     this._serializerFactory = (SerializerFactory)BeanSerializerFactory.instance;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected ClassIntrospector defaultClassIntrospector() {
/*  642 */     return (ClassIntrospector)new BasicClassIntrospector();
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
/*      */   public ObjectMapper copy() {
/*  667 */     _checkInvalidCopy(ObjectMapper.class);
/*  668 */     return new ObjectMapper(this);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void _checkInvalidCopy(Class<?> exp) {
/*  676 */     if (getClass() != exp)
/*      */     {
/*  678 */       throw new IllegalStateException("Failed copy(): " + getClass().getName() + " (version: " + 
/*  679 */           version() + ") does not override copy(); it has to");
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
/*      */   protected ObjectReader _newReader(DeserializationConfig config) {
/*  697 */     return new ObjectReader(this, config);
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
/*      */   protected ObjectReader _newReader(DeserializationConfig config, JavaType valueType, Object valueToUpdate, FormatSchema schema, InjectableValues injectableValues) {
/*  709 */     return new ObjectReader(this, config, valueType, valueToUpdate, schema, injectableValues);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected ObjectWriter _newWriter(SerializationConfig config) {
/*  719 */     return new ObjectWriter(this, config);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected ObjectWriter _newWriter(SerializationConfig config, FormatSchema schema) {
/*  729 */     return new ObjectWriter(this, config, schema);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected ObjectWriter _newWriter(SerializationConfig config, JavaType rootType, PrettyPrinter pp) {
/*  740 */     return new ObjectWriter(this, config, rootType, pp);
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
/*      */   public Version version() {
/*  755 */     return PackageVersion.VERSION;
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
/*      */   public ObjectMapper registerModule(Module module) {
/*  773 */     _assertNotNull("module", module);
/*      */ 
/*      */ 
/*      */     
/*  777 */     String name = module.getModuleName();
/*  778 */     if (name == null) {
/*  779 */       throw new IllegalArgumentException("Module without defined name");
/*      */     }
/*  781 */     Version version = module.version();
/*  782 */     if (version == null) {
/*  783 */       throw new IllegalArgumentException("Module without defined version");
/*      */     }
/*      */ 
/*      */     
/*  787 */     for (Module dep : module.getDependencies()) {
/*  788 */       registerModule(dep);
/*      */     }
/*      */ 
/*      */     
/*  792 */     if (isEnabled(MapperFeature.IGNORE_DUPLICATE_MODULE_REGISTRATIONS)) {
/*  793 */       Object typeId = module.getTypeId();
/*  794 */       if (typeId != null) {
/*  795 */         if (this._registeredModuleTypes == null)
/*      */         {
/*      */           
/*  798 */           this._registeredModuleTypes = new LinkedHashSet();
/*      */         }
/*      */         
/*  801 */         if (!this._registeredModuleTypes.add(typeId)) {
/*  802 */           return this;
/*      */         }
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  808 */     module.setupModule(new Module.SetupContext()
/*      */         {
/*      */ 
/*      */           
/*      */           public Version getMapperVersion()
/*      */           {
/*  814 */             return ObjectMapper.this.version();
/*      */           }
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           public <C extends ObjectCodec> C getOwner() {
/*  821 */             return (C)ObjectMapper.this;
/*      */           }
/*      */ 
/*      */           
/*      */           public TypeFactory getTypeFactory() {
/*  826 */             return ObjectMapper.this._typeFactory;
/*      */           }
/*      */ 
/*      */           
/*      */           public boolean isEnabled(MapperFeature f) {
/*  831 */             return ObjectMapper.this.isEnabled(f);
/*      */           }
/*      */ 
/*      */           
/*      */           public boolean isEnabled(DeserializationFeature f) {
/*  836 */             return ObjectMapper.this.isEnabled(f);
/*      */           }
/*      */ 
/*      */           
/*      */           public boolean isEnabled(SerializationFeature f) {
/*  841 */             return ObjectMapper.this.isEnabled(f);
/*      */           }
/*      */ 
/*      */           
/*      */           public boolean isEnabled(JsonFactory.Feature f) {
/*  846 */             return ObjectMapper.this.isEnabled(f);
/*      */           }
/*      */ 
/*      */           
/*      */           public boolean isEnabled(JsonParser.Feature f) {
/*  851 */             return ObjectMapper.this.isEnabled(f);
/*      */           }
/*      */ 
/*      */           
/*      */           public boolean isEnabled(JsonGenerator.Feature f) {
/*  856 */             return ObjectMapper.this.isEnabled(f);
/*      */           }
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           public MutableConfigOverride configOverride(Class<?> type) {
/*  863 */             return ObjectMapper.this.configOverride(type);
/*      */           }
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           public void addDeserializers(Deserializers d) {
/*  870 */             DeserializerFactory df = ObjectMapper.this._deserializationContext._factory.withAdditionalDeserializers(d);
/*  871 */             ObjectMapper.this._deserializationContext = ObjectMapper.this._deserializationContext.with(df);
/*      */           }
/*      */ 
/*      */           
/*      */           public void addKeyDeserializers(KeyDeserializers d) {
/*  876 */             DeserializerFactory df = ObjectMapper.this._deserializationContext._factory.withAdditionalKeyDeserializers(d);
/*  877 */             ObjectMapper.this._deserializationContext = ObjectMapper.this._deserializationContext.with(df);
/*      */           }
/*      */ 
/*      */           
/*      */           public void addBeanDeserializerModifier(BeanDeserializerModifier modifier) {
/*  882 */             DeserializerFactory df = ObjectMapper.this._deserializationContext._factory.withDeserializerModifier(modifier);
/*  883 */             ObjectMapper.this._deserializationContext = ObjectMapper.this._deserializationContext.with(df);
/*      */           }
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           public void addSerializers(Serializers s) {
/*  890 */             ObjectMapper.this._serializerFactory = ObjectMapper.this._serializerFactory.withAdditionalSerializers(s);
/*      */           }
/*      */ 
/*      */           
/*      */           public void addKeySerializers(Serializers s) {
/*  895 */             ObjectMapper.this._serializerFactory = ObjectMapper.this._serializerFactory.withAdditionalKeySerializers(s);
/*      */           }
/*      */ 
/*      */           
/*      */           public void addBeanSerializerModifier(BeanSerializerModifier modifier) {
/*  900 */             ObjectMapper.this._serializerFactory = ObjectMapper.this._serializerFactory.withSerializerModifier(modifier);
/*      */           }
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           public void addAbstractTypeResolver(AbstractTypeResolver resolver) {
/*  907 */             DeserializerFactory df = ObjectMapper.this._deserializationContext._factory.withAbstractTypeResolver(resolver);
/*  908 */             ObjectMapper.this._deserializationContext = ObjectMapper.this._deserializationContext.with(df);
/*      */           }
/*      */ 
/*      */           
/*      */           public void addTypeModifier(TypeModifier modifier) {
/*  913 */             TypeFactory f = ObjectMapper.this._typeFactory;
/*  914 */             f = f.withModifier(modifier);
/*  915 */             ObjectMapper.this.setTypeFactory(f);
/*      */           }
/*      */ 
/*      */           
/*      */           public void addValueInstantiators(ValueInstantiators instantiators) {
/*  920 */             DeserializerFactory df = ObjectMapper.this._deserializationContext._factory.withValueInstantiators(instantiators);
/*  921 */             ObjectMapper.this._deserializationContext = ObjectMapper.this._deserializationContext.with(df);
/*      */           }
/*      */ 
/*      */           
/*      */           public void setClassIntrospector(ClassIntrospector ci) {
/*  926 */             ObjectMapper.this._deserializationConfig = (DeserializationConfig)ObjectMapper.this._deserializationConfig.with(ci);
/*  927 */             ObjectMapper.this._serializationConfig = (SerializationConfig)ObjectMapper.this._serializationConfig.with(ci);
/*      */           }
/*      */ 
/*      */           
/*      */           public void insertAnnotationIntrospector(AnnotationIntrospector ai) {
/*  932 */             ObjectMapper.this._deserializationConfig = (DeserializationConfig)ObjectMapper.this._deserializationConfig.withInsertedAnnotationIntrospector(ai);
/*  933 */             ObjectMapper.this._serializationConfig = (SerializationConfig)ObjectMapper.this._serializationConfig.withInsertedAnnotationIntrospector(ai);
/*      */           }
/*      */ 
/*      */           
/*      */           public void appendAnnotationIntrospector(AnnotationIntrospector ai) {
/*  938 */             ObjectMapper.this._deserializationConfig = (DeserializationConfig)ObjectMapper.this._deserializationConfig.withAppendedAnnotationIntrospector(ai);
/*  939 */             ObjectMapper.this._serializationConfig = (SerializationConfig)ObjectMapper.this._serializationConfig.withAppendedAnnotationIntrospector(ai);
/*      */           }
/*      */ 
/*      */           
/*      */           public void registerSubtypes(Class<?>... subtypes) {
/*  944 */             ObjectMapper.this.registerSubtypes(subtypes);
/*      */           }
/*      */ 
/*      */           
/*      */           public void registerSubtypes(NamedType... subtypes) {
/*  949 */             ObjectMapper.this.registerSubtypes(subtypes);
/*      */           }
/*      */ 
/*      */           
/*      */           public void registerSubtypes(Collection<Class<?>> subtypes) {
/*  954 */             ObjectMapper.this.registerSubtypes(subtypes);
/*      */           }
/*      */ 
/*      */           
/*      */           public void setMixInAnnotations(Class<?> target, Class<?> mixinSource) {
/*  959 */             ObjectMapper.this.addMixIn(target, mixinSource);
/*      */           }
/*      */ 
/*      */           
/*      */           public void addDeserializationProblemHandler(DeserializationProblemHandler handler) {
/*  964 */             ObjectMapper.this.addHandler(handler);
/*      */           }
/*      */ 
/*      */           
/*      */           public void setNamingStrategy(PropertyNamingStrategy naming) {
/*  969 */             ObjectMapper.this.setPropertyNamingStrategy(naming);
/*      */           }
/*      */         });
/*      */     
/*  973 */     return this;
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
/*      */   public ObjectMapper registerModules(Module... modules) {
/*  989 */     for (Module module : modules) {
/*  990 */       registerModule(module);
/*      */     }
/*  992 */     return this;
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
/*      */   public ObjectMapper registerModules(Iterable<? extends Module> modules) {
/* 1008 */     _assertNotNull("modules", modules);
/* 1009 */     for (Module module : modules) {
/* 1010 */       registerModule(module);
/*      */     }
/* 1012 */     return this;
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
/*      */   public Set<Object> getRegisteredModuleIds() {
/* 1024 */     return (this._registeredModuleTypes == null) ? 
/* 1025 */       Collections.<Object>emptySet() : Collections.<Object>unmodifiableSet(this._registeredModuleTypes);
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
/*      */   public static List<Module> findModules() {
/* 1038 */     return findModules(null);
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
/*      */   public static List<Module> findModules(ClassLoader classLoader) {
/* 1052 */     ArrayList<Module> modules = new ArrayList<>();
/* 1053 */     ServiceLoader<Module> loader = secureGetServiceLoader(Module.class, classLoader);
/* 1054 */     for (Module module : loader) {
/* 1055 */       modules.add(module);
/*      */     }
/* 1057 */     return modules;
/*      */   }
/*      */   
/*      */   private static <T> ServiceLoader<T> secureGetServiceLoader(final Class<T> clazz, final ClassLoader classLoader) {
/* 1061 */     SecurityManager sm = System.getSecurityManager();
/* 1062 */     if (sm == null) {
/* 1063 */       return (classLoader == null) ? 
/* 1064 */         ServiceLoader.<T>load(clazz) : ServiceLoader.<T>load(clazz, classLoader);
/*      */     }
/* 1066 */     return AccessController.<ServiceLoader<T>>doPrivileged((PrivilegedAction)new PrivilegedAction<ServiceLoader<ServiceLoader<T>>>()
/*      */         {
/*      */           public ServiceLoader<T> run() {
/* 1069 */             return (classLoader == null) ? 
/* 1070 */               ServiceLoader.<T>load(clazz) : ServiceLoader.<T>load(clazz, classLoader);
/*      */           }
/*      */         });
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
/*      */   public ObjectMapper findAndRegisterModules() {
/* 1088 */     return registerModules(findModules());
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
/*      */   public SerializationConfig getSerializationConfig() {
/* 1106 */     return this._serializationConfig;
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
/*      */   public DeserializationConfig getDeserializationConfig() {
/* 1119 */     return this._deserializationConfig;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public DeserializationContext getDeserializationContext() {
/* 1130 */     return (DeserializationContext)this._deserializationContext;
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
/*      */   public ObjectMapper setSerializerFactory(SerializerFactory f) {
/* 1144 */     this._serializerFactory = f;
/* 1145 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public SerializerFactory getSerializerFactory() {
/* 1156 */     return this._serializerFactory;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectMapper setSerializerProvider(DefaultSerializerProvider p) {
/* 1165 */     this._serializerProvider = p;
/* 1166 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public SerializerProvider getSerializerProvider() {
/* 1177 */     return (SerializerProvider)this._serializerProvider;
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
/*      */   public SerializerProvider getSerializerProviderInstance() {
/* 1189 */     return (SerializerProvider)_serializerProvider(this._serializationConfig);
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
/*      */   public ObjectMapper setMixIns(Map<Class<?>, Class<?>> sourceMixins) {
/* 1218 */     this._mixIns.setLocalDefinitions(sourceMixins);
/* 1219 */     return this;
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
/*      */   public ObjectMapper addMixIn(Class<?> target, Class<?> mixinSource) {
/* 1236 */     this._mixIns.addLocalDefinition(target, mixinSource);
/* 1237 */     return this;
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
/*      */   public ObjectMapper setMixInResolver(ClassIntrospector.MixInResolver resolver) {
/* 1250 */     SimpleMixInResolver r = this._mixIns.withOverrides(resolver);
/* 1251 */     if (r != this._mixIns) {
/* 1252 */       this._mixIns = r;
/* 1253 */       this._deserializationConfig = new DeserializationConfig(this._deserializationConfig, r);
/* 1254 */       this._serializationConfig = new SerializationConfig(this._serializationConfig, r);
/*      */     } 
/* 1256 */     return this;
/*      */   }
/*      */   
/*      */   public Class<?> findMixInClassFor(Class<?> cls) {
/* 1260 */     return this._mixIns.findMixInClassFor(cls);
/*      */   }
/*      */ 
/*      */   
/*      */   public int mixInCount() {
/* 1265 */     return this._mixIns.localSize();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public void setMixInAnnotations(Map<Class<?>, Class<?>> sourceMixins) {
/* 1273 */     setMixIns(sourceMixins);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public final void addMixInAnnotations(Class<?> target, Class<?> mixinSource) {
/* 1281 */     addMixIn(target, mixinSource);
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
/*      */   public VisibilityChecker<?> getVisibilityChecker() {
/* 1296 */     return this._serializationConfig.getDefaultVisibilityChecker();
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
/*      */   public ObjectMapper setVisibility(VisibilityChecker<?> vc) {
/* 1310 */     this._configOverrides.setDefaultVisibility(vc);
/* 1311 */     return this;
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
/*      */   public ObjectMapper setVisibility(PropertyAccessor forMethod, JsonAutoDetect.Visibility visibility) {
/* 1340 */     VisibilityChecker<?> vc = this._configOverrides.getDefaultVisibility();
/* 1341 */     vc = vc.withVisibility(forMethod, visibility);
/* 1342 */     this._configOverrides.setDefaultVisibility(vc);
/* 1343 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public SubtypeResolver getSubtypeResolver() {
/* 1350 */     return this._subtypeResolver;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectMapper setSubtypeResolver(SubtypeResolver str) {
/* 1357 */     this._subtypeResolver = str;
/* 1358 */     this._deserializationConfig = this._deserializationConfig.with(str);
/* 1359 */     this._serializationConfig = this._serializationConfig.with(str);
/* 1360 */     return this;
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
/*      */   public ObjectMapper setAnnotationIntrospector(AnnotationIntrospector ai) {
/* 1374 */     this._serializationConfig = (SerializationConfig)this._serializationConfig.with(ai);
/* 1375 */     this._deserializationConfig = (DeserializationConfig)this._deserializationConfig.with(ai);
/* 1376 */     return this;
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
/*      */   public ObjectMapper setAnnotationIntrospectors(AnnotationIntrospector serializerAI, AnnotationIntrospector deserializerAI) {
/* 1396 */     this._serializationConfig = (SerializationConfig)this._serializationConfig.with(serializerAI);
/* 1397 */     this._deserializationConfig = (DeserializationConfig)this._deserializationConfig.with(deserializerAI);
/* 1398 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectMapper setPropertyNamingStrategy(PropertyNamingStrategy s) {
/* 1405 */     this._serializationConfig = (SerializationConfig)this._serializationConfig.with(s);
/* 1406 */     this._deserializationConfig = (DeserializationConfig)this._deserializationConfig.with(s);
/* 1407 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PropertyNamingStrategy getPropertyNamingStrategy() {
/* 1415 */     return this._serializationConfig.getPropertyNamingStrategy();
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
/*      */   public ObjectMapper setDefaultPrettyPrinter(PrettyPrinter pp) {
/* 1429 */     this._serializationConfig = this._serializationConfig.withDefaultPrettyPrinter(pp);
/* 1430 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public void setVisibilityChecker(VisibilityChecker<?> vc) {
/* 1438 */     setVisibility(vc);
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
/*      */   public ObjectMapper setPolymorphicTypeValidator(PolymorphicTypeValidator ptv) {
/* 1450 */     BaseSettings s = this._deserializationConfig.getBaseSettings().with(ptv);
/* 1451 */     this._deserializationConfig = this._deserializationConfig._withBase(s);
/* 1452 */     return this;
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
/*      */   public PolymorphicTypeValidator getPolymorphicTypeValidator() {
/* 1464 */     return this._deserializationConfig.getBaseSettings().getPolymorphicTypeValidator();
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
/*      */   public ObjectMapper setSerializationInclusion(JsonInclude.Include incl) {
/* 1483 */     setPropertyInclusion(JsonInclude.Value.construct(incl, incl));
/* 1484 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public ObjectMapper setPropertyInclusion(JsonInclude.Value incl) {
/* 1493 */     return setDefaultPropertyInclusion(incl);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectMapper setDefaultPropertyInclusion(JsonInclude.Value incl) {
/* 1504 */     this._configOverrides.setDefaultInclusion(incl);
/* 1505 */     return this;
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
/*      */   public ObjectMapper setDefaultPropertyInclusion(JsonInclude.Include incl) {
/* 1517 */     this._configOverrides.setDefaultInclusion(JsonInclude.Value.construct(incl, incl));
/* 1518 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectMapper setDefaultSetterInfo(JsonSetter.Value v) {
/* 1529 */     this._configOverrides.setDefaultSetterInfo(v);
/* 1530 */     return this;
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
/*      */   public ObjectMapper setDefaultVisibility(JsonAutoDetect.Value vis) {
/* 1542 */     this._configOverrides.setDefaultVisibility((VisibilityChecker)VisibilityChecker.Std.construct(vis));
/* 1543 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectMapper setDefaultMergeable(Boolean b) {
/* 1554 */     this._configOverrides.setDefaultMergeable(b);
/* 1555 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectMapper setDefaultLeniency(Boolean b) {
/* 1562 */     this._configOverrides.setDefaultLeniency(b);
/* 1563 */     return this;
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
/*      */   public void registerSubtypes(Class<?>... classes) {
/* 1580 */     getSubtypeResolver().registerSubtypes(classes);
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
/*      */   public void registerSubtypes(NamedType... types) {
/* 1592 */     getSubtypeResolver().registerSubtypes(types);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void registerSubtypes(Collection<Class<?>> subtypes) {
/* 1599 */     getSubtypeResolver().registerSubtypes(subtypes);
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
/*      */   public ObjectMapper activateDefaultTyping(PolymorphicTypeValidator ptv) {
/* 1623 */     return activateDefaultTyping(ptv, DefaultTyping.OBJECT_AND_NON_CONCRETE);
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
/*      */   public ObjectMapper activateDefaultTyping(PolymorphicTypeValidator ptv, DefaultTyping applicability) {
/* 1644 */     return activateDefaultTyping(ptv, applicability, JsonTypeInfo.As.WRAPPER_ARRAY);
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
/*      */   public ObjectMapper activateDefaultTyping(PolymorphicTypeValidator ptv, DefaultTyping applicability, JsonTypeInfo.As includeAs) {
/* 1672 */     if (includeAs == JsonTypeInfo.As.EXTERNAL_PROPERTY) {
/* 1673 */       throw new IllegalArgumentException("Cannot use includeAs of " + includeAs);
/*      */     }
/*      */     
/* 1676 */     TypeResolverBuilder<?> typer = _constructDefaultTypeResolverBuilder(applicability, ptv);
/*      */     
/* 1678 */     typer = typer.init(JsonTypeInfo.Id.CLASS, null);
/* 1679 */     typer = typer.inclusion(includeAs);
/* 1680 */     return setDefaultTyping(typer);
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
/*      */   public ObjectMapper activateDefaultTypingAsProperty(PolymorphicTypeValidator ptv, DefaultTyping applicability, String propertyName) {
/* 1705 */     TypeResolverBuilder<?> typer = _constructDefaultTypeResolverBuilder(applicability, 
/* 1706 */         getPolymorphicTypeValidator());
/*      */ 
/*      */     
/* 1709 */     typer = typer.init(JsonTypeInfo.Id.CLASS, null);
/* 1710 */     typer = typer.inclusion(JsonTypeInfo.As.PROPERTY);
/* 1711 */     typer = typer.typeProperty(propertyName);
/* 1712 */     return setDefaultTyping(typer);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectMapper deactivateDefaultTyping() {
/* 1722 */     return setDefaultTyping(null);
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
/*      */   public ObjectMapper setDefaultTyping(TypeResolverBuilder<?> typer) {
/* 1741 */     this._deserializationConfig = (DeserializationConfig)this._deserializationConfig.with(typer);
/* 1742 */     this._serializationConfig = (SerializationConfig)this._serializationConfig.with(typer);
/* 1743 */     return this;
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
/*      */   public ObjectMapper enableDefaultTyping() {
/* 1757 */     return activateDefaultTyping(getPolymorphicTypeValidator());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public ObjectMapper enableDefaultTyping(DefaultTyping dti) {
/* 1765 */     return enableDefaultTyping(dti, JsonTypeInfo.As.WRAPPER_ARRAY);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public ObjectMapper enableDefaultTyping(DefaultTyping applicability, JsonTypeInfo.As includeAs) {
/* 1773 */     return activateDefaultTyping(getPolymorphicTypeValidator(), applicability, includeAs);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public ObjectMapper enableDefaultTypingAsProperty(DefaultTyping applicability, String propertyName) {
/* 1781 */     return activateDefaultTypingAsProperty(getPolymorphicTypeValidator(), applicability, propertyName);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public ObjectMapper disableDefaultTyping() {
/* 1789 */     return setDefaultTyping(null);
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
/*      */   public MutableConfigOverride configOverride(Class<?> type) {
/* 1816 */     return this._configOverrides.findOrCreateOverride(type);
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
/*      */   public TypeFactory getTypeFactory() {
/* 1829 */     return this._typeFactory;
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
/*      */   public ObjectMapper setTypeFactory(TypeFactory f) {
/* 1841 */     this._typeFactory = f;
/* 1842 */     this._deserializationConfig = (DeserializationConfig)this._deserializationConfig.with(f);
/* 1843 */     this._serializationConfig = (SerializationConfig)this._serializationConfig.with(f);
/* 1844 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JavaType constructType(Type t) {
/* 1853 */     _assertNotNull("t", t);
/* 1854 */     return this._typeFactory.constructType(t);
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
/*      */   public JsonNodeFactory getNodeFactory() {
/* 1874 */     return this._deserializationConfig.getNodeFactory();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectMapper setNodeFactory(JsonNodeFactory f) {
/* 1883 */     this._deserializationConfig = this._deserializationConfig.with(f);
/* 1884 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectMapper addHandler(DeserializationProblemHandler h) {
/* 1892 */     this._deserializationConfig = this._deserializationConfig.withHandler(h);
/* 1893 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectMapper clearProblemHandlers() {
/* 1901 */     this._deserializationConfig = this._deserializationConfig.withNoProblemHandlers();
/* 1902 */     return this;
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
/*      */   public ObjectMapper setConfig(DeserializationConfig config) {
/* 1920 */     _assertNotNull("config", config);
/* 1921 */     this._deserializationConfig = config;
/* 1922 */     return this;
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
/*      */   public void setFilters(FilterProvider filterProvider) {
/* 1936 */     this._serializationConfig = this._serializationConfig.withFilters(filterProvider);
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
/*      */   public ObjectMapper setFilterProvider(FilterProvider filterProvider) {
/* 1951 */     this._serializationConfig = this._serializationConfig.withFilters(filterProvider);
/* 1952 */     return this;
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
/*      */   public ObjectMapper setBase64Variant(Base64Variant v) {
/* 1966 */     this._serializationConfig = (SerializationConfig)this._serializationConfig.with(v);
/* 1967 */     this._deserializationConfig = (DeserializationConfig)this._deserializationConfig.with(v);
/* 1968 */     return this;
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
/*      */   public ObjectMapper setConfig(SerializationConfig config) {
/* 1986 */     _assertNotNull("config", config);
/* 1987 */     this._serializationConfig = config;
/* 1988 */     return this;
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
/*      */   public JsonFactory tokenStreamFactory() {
/* 2016 */     return this._jsonFactory;
/*      */   }
/*      */   public JsonFactory getFactory() {
/* 2019 */     return this._jsonFactory;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public JsonFactory getJsonFactory() {
/* 2026 */     return getFactory();
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
/*      */   public ObjectMapper setDateFormat(DateFormat dateFormat) {
/* 2040 */     this._deserializationConfig = (DeserializationConfig)this._deserializationConfig.with(dateFormat);
/* 2041 */     this._serializationConfig = this._serializationConfig.with(dateFormat);
/* 2042 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public DateFormat getDateFormat() {
/* 2050 */     return this._serializationConfig.getDateFormat();
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
/*      */   public Object setHandlerInstantiator(HandlerInstantiator hi) {
/* 2062 */     this._deserializationConfig = (DeserializationConfig)this._deserializationConfig.with(hi);
/* 2063 */     this._serializationConfig = (SerializationConfig)this._serializationConfig.with(hi);
/* 2064 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectMapper setInjectableValues(InjectableValues injectableValues) {
/* 2072 */     this._injectableValues = injectableValues;
/* 2073 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public InjectableValues getInjectableValues() {
/* 2080 */     return this._injectableValues;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectMapper setLocale(Locale l) {
/* 2088 */     this._deserializationConfig = (DeserializationConfig)this._deserializationConfig.with(l);
/* 2089 */     this._serializationConfig = (SerializationConfig)this._serializationConfig.with(l);
/* 2090 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectMapper setTimeZone(TimeZone tz) {
/* 2098 */     this._deserializationConfig = (DeserializationConfig)this._deserializationConfig.with(tz);
/* 2099 */     this._serializationConfig = (SerializationConfig)this._serializationConfig.with(tz);
/* 2100 */     return this;
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
/*      */   public boolean isEnabled(MapperFeature f) {
/* 2114 */     return this._serializationConfig.isEnabled(f);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectMapper configure(MapperFeature f, boolean state) {
/* 2121 */     this
/* 2122 */       ._serializationConfig = state ? (SerializationConfig)this._serializationConfig.with(new MapperFeature[] { f }) : (SerializationConfig)this._serializationConfig.without(new MapperFeature[] { f });
/* 2123 */     this
/* 2124 */       ._deserializationConfig = state ? (DeserializationConfig)this._deserializationConfig.with(new MapperFeature[] { f }) : (DeserializationConfig)this._deserializationConfig.without(new MapperFeature[] { f });
/* 2125 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectMapper enable(MapperFeature... f) {
/* 2132 */     this._deserializationConfig = (DeserializationConfig)this._deserializationConfig.with(f);
/* 2133 */     this._serializationConfig = (SerializationConfig)this._serializationConfig.with(f);
/* 2134 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectMapper disable(MapperFeature... f) {
/* 2141 */     this._deserializationConfig = (DeserializationConfig)this._deserializationConfig.without(f);
/* 2142 */     this._serializationConfig = (SerializationConfig)this._serializationConfig.without(f);
/* 2143 */     return this;
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
/*      */   public boolean isEnabled(SerializationFeature f) {
/* 2157 */     return this._serializationConfig.isEnabled(f);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectMapper configure(SerializationFeature f, boolean state) {
/* 2165 */     this
/* 2166 */       ._serializationConfig = state ? this._serializationConfig.with(f) : this._serializationConfig.without(f);
/* 2167 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectMapper enable(SerializationFeature f) {
/* 2175 */     this._serializationConfig = this._serializationConfig.with(f);
/* 2176 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectMapper enable(SerializationFeature first, SerializationFeature... f) {
/* 2185 */     this._serializationConfig = this._serializationConfig.with(first, f);
/* 2186 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectMapper disable(SerializationFeature f) {
/* 2194 */     this._serializationConfig = this._serializationConfig.without(f);
/* 2195 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectMapper disable(SerializationFeature first, SerializationFeature... f) {
/* 2204 */     this._serializationConfig = this._serializationConfig.without(first, f);
/* 2205 */     return this;
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
/*      */   public boolean isEnabled(DeserializationFeature f) {
/* 2219 */     return this._deserializationConfig.isEnabled(f);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectMapper configure(DeserializationFeature f, boolean state) {
/* 2227 */     this
/* 2228 */       ._deserializationConfig = state ? this._deserializationConfig.with(f) : this._deserializationConfig.without(f);
/* 2229 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectMapper enable(DeserializationFeature feature) {
/* 2237 */     this._deserializationConfig = this._deserializationConfig.with(feature);
/* 2238 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectMapper enable(DeserializationFeature first, DeserializationFeature... f) {
/* 2247 */     this._deserializationConfig = this._deserializationConfig.with(first, f);
/* 2248 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectMapper disable(DeserializationFeature feature) {
/* 2256 */     this._deserializationConfig = this._deserializationConfig.without(feature);
/* 2257 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectMapper disable(DeserializationFeature first, DeserializationFeature... f) {
/* 2266 */     this._deserializationConfig = this._deserializationConfig.without(first, f);
/* 2267 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isEnabled(JsonParser.Feature f) {
/* 2277 */     return this._deserializationConfig.isEnabled(f, this._jsonFactory);
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
/*      */   public ObjectMapper configure(JsonParser.Feature f, boolean state) {
/* 2292 */     this._jsonFactory.configure(f, state);
/* 2293 */     return this;
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
/*      */   public ObjectMapper enable(JsonParser.Feature... features) {
/* 2309 */     for (JsonParser.Feature f : features) {
/* 2310 */       this._jsonFactory.enable(f);
/*      */     }
/* 2312 */     return this;
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
/*      */   public ObjectMapper disable(JsonParser.Feature... features) {
/* 2328 */     for (JsonParser.Feature f : features) {
/* 2329 */       this._jsonFactory.disable(f);
/*      */     }
/* 2331 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isEnabled(JsonGenerator.Feature f) {
/* 2341 */     return this._serializationConfig.isEnabled(f, this._jsonFactory);
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
/*      */   public ObjectMapper configure(JsonGenerator.Feature f, boolean state) {
/* 2356 */     this._jsonFactory.configure(f, state);
/* 2357 */     return this;
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
/*      */   public ObjectMapper enable(JsonGenerator.Feature... features) {
/* 2373 */     for (JsonGenerator.Feature f : features) {
/* 2374 */       this._jsonFactory.enable(f);
/*      */     }
/* 2376 */     return this;
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
/*      */   public ObjectMapper disable(JsonGenerator.Feature... features) {
/* 2392 */     for (JsonGenerator.Feature f : features) {
/* 2393 */       this._jsonFactory.disable(f);
/*      */     }
/* 2395 */     return this;
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
/*      */   public boolean isEnabled(JsonFactory.Feature f) {
/* 2411 */     return this._jsonFactory.isEnabled(f);
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
/*      */   public boolean isEnabled(StreamReadFeature f) {
/* 2424 */     return isEnabled(f.mappedFeature());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isEnabled(StreamWriteFeature f) {
/* 2431 */     return isEnabled(f.mappedFeature());
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
/*      */   public <T> T readValue(JsonParser p, Class<T> valueType) throws IOException, JsonParseException, JsonMappingException {
/* 2466 */     _assertNotNull("p", p);
/* 2467 */     return (T)_readValue(getDeserializationConfig(), p, this._typeFactory.constructType(valueType));
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
/*      */   public <T> T readValue(JsonParser p, TypeReference<T> valueTypeRef) throws IOException, JsonParseException, JsonMappingException {
/* 2491 */     _assertNotNull("p", p);
/* 2492 */     return (T)_readValue(getDeserializationConfig(), p, this._typeFactory.constructType(valueTypeRef));
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
/*      */   public final <T> T readValue(JsonParser p, ResolvedType valueType) throws IOException, JsonParseException, JsonMappingException {
/* 2515 */     _assertNotNull("p", p);
/* 2516 */     return (T)_readValue(getDeserializationConfig(), p, (JavaType)valueType);
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
/*      */   public <T> T readValue(JsonParser p, JavaType valueType) throws IOException, JsonParseException, JsonMappingException {
/* 2535 */     _assertNotNull("p", p);
/* 2536 */     return (T)_readValue(getDeserializationConfig(), p, valueType);
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
/*      */   public <T extends TreeNode> T readTree(JsonParser p) throws IOException, JsonProcessingException {
/*      */     NullNode nullNode;
/* 2566 */     _assertNotNull("p", p);
/*      */     
/* 2568 */     DeserializationConfig cfg = getDeserializationConfig();
/* 2569 */     JsonToken t = p.getCurrentToken();
/* 2570 */     if (t == null) {
/* 2571 */       t = p.nextToken();
/* 2572 */       if (t == null) {
/* 2573 */         return null;
/*      */       }
/*      */     } 
/*      */     
/* 2577 */     JsonNode n = (JsonNode)_readValue(cfg, p, constructType(JsonNode.class));
/* 2578 */     if (n == null) {
/* 2579 */       nullNode = getNodeFactory().nullNode();
/*      */     }
/*      */     
/* 2582 */     return (T)nullNode;
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
/*      */   public <T> MappingIterator<T> readValues(JsonParser p, ResolvedType valueType) throws IOException, JsonProcessingException {
/* 2608 */     return readValues(p, (JavaType)valueType);
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
/*      */   public <T> MappingIterator<T> readValues(JsonParser p, JavaType valueType) throws IOException, JsonProcessingException {
/* 2622 */     _assertNotNull("p", p);
/* 2623 */     DeserializationConfig config = getDeserializationConfig();
/* 2624 */     DefaultDeserializationContext defaultDeserializationContext = createDeserializationContext(p, config);
/* 2625 */     JsonDeserializer<?> deser = _findRootDeserializer((DeserializationContext)defaultDeserializationContext, valueType);
/*      */     
/* 2627 */     return new MappingIterator<>(valueType, p, (DeserializationContext)defaultDeserializationContext, deser, false, null);
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
/*      */   public <T> MappingIterator<T> readValues(JsonParser p, Class<T> valueType) throws IOException, JsonProcessingException {
/* 2643 */     return readValues(p, this._typeFactory.constructType(valueType));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> MappingIterator<T> readValues(JsonParser p, TypeReference<T> valueTypeRef) throws IOException, JsonProcessingException {
/* 2653 */     return readValues(p, this._typeFactory.constructType(valueTypeRef));
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
/*      */   public JsonNode readTree(InputStream in) throws IOException {
/* 2691 */     _assertNotNull("in", in);
/* 2692 */     return _readTreeAndClose(this._jsonFactory.createParser(in));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JsonNode readTree(Reader r) throws IOException {
/* 2700 */     _assertNotNull("r", r);
/* 2701 */     return _readTreeAndClose(this._jsonFactory.createParser(r));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JsonNode readTree(String content) throws JsonProcessingException, JsonMappingException {
/* 2709 */     _assertNotNull("content", content);
/*      */     try {
/* 2711 */       return _readTreeAndClose(this._jsonFactory.createParser(content));
/* 2712 */     } catch (JsonProcessingException e) {
/* 2713 */       throw e;
/* 2714 */     } catch (IOException e) {
/* 2715 */       throw JsonMappingException.fromUnexpectedIOE(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JsonNode readTree(byte[] content) throws IOException {
/* 2724 */     _assertNotNull("content", content);
/* 2725 */     return _readTreeAndClose(this._jsonFactory.createParser(content));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JsonNode readTree(byte[] content, int offset, int len) throws IOException {
/* 2733 */     _assertNotNull("content", content);
/* 2734 */     return _readTreeAndClose(this._jsonFactory.createParser(content, offset, len));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JsonNode readTree(File file) throws IOException, JsonProcessingException {
/* 2744 */     _assertNotNull("file", file);
/* 2745 */     return _readTreeAndClose(this._jsonFactory.createParser(file));
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
/*      */   public JsonNode readTree(URL source) throws IOException {
/* 2759 */     _assertNotNull("source", source);
/* 2760 */     return _readTreeAndClose(this._jsonFactory.createParser(source));
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
/*      */   public void writeValue(JsonGenerator g, Object value) throws IOException, JsonGenerationException, JsonMappingException {
/* 2778 */     _assertNotNull("g", g);
/* 2779 */     SerializationConfig config = getSerializationConfig();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2787 */     if (config.isEnabled(SerializationFeature.INDENT_OUTPUT) && 
/* 2788 */       g.getPrettyPrinter() == null) {
/* 2789 */       g.setPrettyPrinter(config.constructDefaultPrettyPrinter());
/*      */     }
/*      */     
/* 2792 */     if (config.isEnabled(SerializationFeature.CLOSE_CLOSEABLE) && value instanceof Closeable) {
/* 2793 */       _writeCloseableValue(g, value, config);
/*      */     } else {
/* 2795 */       _serializerProvider(config).serializeValue(g, value);
/* 2796 */       if (config.isEnabled(SerializationFeature.FLUSH_AFTER_WRITE_VALUE)) {
/* 2797 */         g.flush();
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
/*      */   public void writeTree(JsonGenerator g, TreeNode rootNode) throws IOException, JsonProcessingException {
/* 2812 */     _assertNotNull("g", g);
/* 2813 */     SerializationConfig config = getSerializationConfig();
/* 2814 */     _serializerProvider(config).serializeValue(g, rootNode);
/* 2815 */     if (config.isEnabled(SerializationFeature.FLUSH_AFTER_WRITE_VALUE)) {
/* 2816 */       g.flush();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void writeTree(JsonGenerator g, JsonNode rootNode) throws IOException, JsonProcessingException {
/* 2827 */     _assertNotNull("g", g);
/* 2828 */     SerializationConfig config = getSerializationConfig();
/* 2829 */     _serializerProvider(config).serializeValue(g, rootNode);
/* 2830 */     if (config.isEnabled(SerializationFeature.FLUSH_AFTER_WRITE_VALUE)) {
/* 2831 */       g.flush();
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
/*      */   public ObjectNode createObjectNode() {
/* 2844 */     return this._deserializationConfig.getNodeFactory().objectNode();
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
/*      */   public ArrayNode createArrayNode() {
/* 2856 */     return this._deserializationConfig.getNodeFactory().arrayNode();
/*      */   }
/*      */ 
/*      */   
/*      */   public JsonNode missingNode() {
/* 2861 */     return this._deserializationConfig.getNodeFactory().missingNode();
/*      */   }
/*      */ 
/*      */   
/*      */   public JsonNode nullNode() {
/* 2866 */     return (JsonNode)this._deserializationConfig.getNodeFactory().nullNode();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JsonParser treeAsTokens(TreeNode n) {
/* 2877 */     _assertNotNull("n", n);
/* 2878 */     return (JsonParser)new TreeTraversingParser((JsonNode)n, this);
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
/*      */   public <T> T treeToValue(TreeNode n, Class<T> valueType) throws JsonProcessingException {
/* 2895 */     if (n == null) {
/* 2896 */       return null;
/*      */     }
/*      */ 
/*      */     
/*      */     try {
/* 2901 */       if (TreeNode.class.isAssignableFrom(valueType) && valueType
/* 2902 */         .isAssignableFrom(n.getClass())) {
/* 2903 */         return (T)n;
/*      */       }
/* 2905 */       JsonToken tt = n.asToken();
/*      */       
/* 2907 */       if (tt == JsonToken.VALUE_NULL) {
/* 2908 */         return null;
/*      */       }
/*      */ 
/*      */       
/* 2912 */       if (tt == JsonToken.VALUE_EMBEDDED_OBJECT && 
/* 2913 */         n instanceof POJONode) {
/* 2914 */         Object ob = ((POJONode)n).getPojo();
/* 2915 */         if (ob == null || valueType.isInstance(ob)) {
/* 2916 */           return (T)ob;
/*      */         }
/*      */       } 
/*      */       
/* 2920 */       return readValue(treeAsTokens(n), valueType);
/* 2921 */     } catch (JsonProcessingException e) {
/* 2922 */       throw e;
/* 2923 */     } catch (IOException e) {
/* 2924 */       throw new IllegalArgumentException(e.getMessage(), e);
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
/*      */   public <T extends JsonNode> T valueToTree(Object fromValue) throws IllegalArgumentException {
/*      */     JsonNode result;
/* 2955 */     if (fromValue == null) {
/* 2956 */       return (T)getNodeFactory().nullNode();
/*      */     }
/* 2958 */     TokenBuffer buf = new TokenBuffer(this, false);
/* 2959 */     if (isEnabled(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS)) {
/* 2960 */       buf = buf.forceUseOfBigDecimal(true);
/*      */     }
/*      */     
/*      */     try {
/* 2964 */       writeValue((JsonGenerator)buf, fromValue);
/* 2965 */       JsonParser p = buf.asParser();
/* 2966 */       result = readTree(p);
/* 2967 */       p.close();
/* 2968 */     } catch (IOException e) {
/* 2969 */       throw new IllegalArgumentException(e.getMessage(), e);
/*      */     } 
/* 2971 */     return (T)result;
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
/*      */   public boolean canSerialize(Class<?> type) {
/* 2996 */     return _serializerProvider(getSerializationConfig()).hasSerializerFor(type, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean canSerialize(Class<?> type, AtomicReference<Throwable> cause) {
/* 3007 */     return _serializerProvider(getSerializationConfig()).hasSerializerFor(type, cause);
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
/*      */   public boolean canDeserialize(JavaType type) {
/* 3029 */     return createDeserializationContext(null, 
/* 3030 */         getDeserializationConfig()).hasValueDeserializerFor(type, null);
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
/*      */   public boolean canDeserialize(JavaType type, AtomicReference<Throwable> cause) {
/* 3042 */     return createDeserializationContext(null, 
/* 3043 */         getDeserializationConfig()).hasValueDeserializerFor(type, cause);
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
/*      */   public <T> T readValue(File src, Class<T> valueType) throws IOException, JsonParseException, JsonMappingException {
/* 3069 */     _assertNotNull("src", src);
/* 3070 */     return (T)_readMapAndClose(this._jsonFactory.createParser(src), this._typeFactory.constructType(valueType));
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
/*      */   public <T> T readValue(File src, TypeReference<T> valueTypeRef) throws IOException, JsonParseException, JsonMappingException {
/* 3089 */     _assertNotNull("src", src);
/* 3090 */     return (T)_readMapAndClose(this._jsonFactory.createParser(src), this._typeFactory.constructType(valueTypeRef));
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
/*      */   public <T> T readValue(File src, JavaType valueType) throws IOException, JsonParseException, JsonMappingException {
/* 3109 */     _assertNotNull("src", src);
/* 3110 */     return (T)_readMapAndClose(this._jsonFactory.createParser(src), valueType);
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
/*      */   public <T> T readValue(URL src, Class<T> valueType) throws IOException, JsonParseException, JsonMappingException {
/* 3135 */     _assertNotNull("src", src);
/* 3136 */     return (T)_readMapAndClose(this._jsonFactory.createParser(src), this._typeFactory.constructType(valueType));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> T readValue(URL src, TypeReference<T> valueTypeRef) throws IOException, JsonParseException, JsonMappingException {
/* 3146 */     _assertNotNull("src", src);
/* 3147 */     return (T)_readMapAndClose(this._jsonFactory.createParser(src), this._typeFactory.constructType(valueTypeRef));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> T readValue(URL src, JavaType valueType) throws IOException, JsonParseException, JsonMappingException {
/* 3157 */     _assertNotNull("src", src);
/* 3158 */     return (T)_readMapAndClose(this._jsonFactory.createParser(src), valueType);
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
/*      */   public <T> T readValue(String content, Class<T> valueType) throws JsonProcessingException, JsonMappingException {
/* 3172 */     _assertNotNull("content", content);
/* 3173 */     return readValue(content, this._typeFactory.constructType(valueType));
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
/*      */   public <T> T readValue(String content, TypeReference<T> valueTypeRef) throws JsonProcessingException, JsonMappingException {
/* 3187 */     _assertNotNull("content", content);
/* 3188 */     return readValue(content, this._typeFactory.constructType(valueTypeRef));
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
/*      */   public <T> T readValue(String content, JavaType valueType) throws JsonProcessingException, JsonMappingException {
/* 3203 */     _assertNotNull("content", content);
/*      */     try {
/* 3205 */       return (T)_readMapAndClose(this._jsonFactory.createParser(content), valueType);
/* 3206 */     } catch (JsonProcessingException e) {
/* 3207 */       throw e;
/* 3208 */     } catch (IOException e) {
/* 3209 */       throw JsonMappingException.fromUnexpectedIOE(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> T readValue(Reader src, Class<T> valueType) throws IOException, JsonParseException, JsonMappingException {
/* 3217 */     _assertNotNull("src", src);
/* 3218 */     return (T)_readMapAndClose(this._jsonFactory.createParser(src), this._typeFactory.constructType(valueType));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> T readValue(Reader src, TypeReference<T> valueTypeRef) throws IOException, JsonParseException, JsonMappingException {
/* 3225 */     _assertNotNull("src", src);
/* 3226 */     return (T)_readMapAndClose(this._jsonFactory.createParser(src), this._typeFactory.constructType(valueTypeRef));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> T readValue(Reader src, JavaType valueType) throws IOException, JsonParseException, JsonMappingException {
/* 3233 */     _assertNotNull("src", src);
/* 3234 */     return (T)_readMapAndClose(this._jsonFactory.createParser(src), valueType);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> T readValue(InputStream src, Class<T> valueType) throws IOException, JsonParseException, JsonMappingException {
/* 3241 */     _assertNotNull("src", src);
/* 3242 */     return (T)_readMapAndClose(this._jsonFactory.createParser(src), this._typeFactory.constructType(valueType));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> T readValue(InputStream src, TypeReference<T> valueTypeRef) throws IOException, JsonParseException, JsonMappingException {
/* 3249 */     _assertNotNull("src", src);
/* 3250 */     return (T)_readMapAndClose(this._jsonFactory.createParser(src), this._typeFactory.constructType(valueTypeRef));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> T readValue(InputStream src, JavaType valueType) throws IOException, JsonParseException, JsonMappingException {
/* 3257 */     _assertNotNull("src", src);
/* 3258 */     return (T)_readMapAndClose(this._jsonFactory.createParser(src), valueType);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> T readValue(byte[] src, Class<T> valueType) throws IOException, JsonParseException, JsonMappingException {
/* 3265 */     _assertNotNull("src", src);
/* 3266 */     return (T)_readMapAndClose(this._jsonFactory.createParser(src), this._typeFactory.constructType(valueType));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> T readValue(byte[] src, int offset, int len, Class<T> valueType) throws IOException, JsonParseException, JsonMappingException {
/* 3274 */     _assertNotNull("src", src);
/* 3275 */     return (T)_readMapAndClose(this._jsonFactory.createParser(src, offset, len), this._typeFactory.constructType(valueType));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> T readValue(byte[] src, TypeReference<T> valueTypeRef) throws IOException, JsonParseException, JsonMappingException {
/* 3282 */     _assertNotNull("src", src);
/* 3283 */     return (T)_readMapAndClose(this._jsonFactory.createParser(src), this._typeFactory.constructType(valueTypeRef));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> T readValue(byte[] src, int offset, int len, TypeReference<T> valueTypeRef) throws IOException, JsonParseException, JsonMappingException {
/* 3291 */     _assertNotNull("src", src);
/* 3292 */     return (T)_readMapAndClose(this._jsonFactory.createParser(src, offset, len), this._typeFactory.constructType(valueTypeRef));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> T readValue(byte[] src, JavaType valueType) throws IOException, JsonParseException, JsonMappingException {
/* 3299 */     _assertNotNull("src", src);
/* 3300 */     return (T)_readMapAndClose(this._jsonFactory.createParser(src), valueType);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> T readValue(byte[] src, int offset, int len, JavaType valueType) throws IOException, JsonParseException, JsonMappingException {
/* 3308 */     _assertNotNull("src", src);
/* 3309 */     return (T)_readMapAndClose(this._jsonFactory.createParser(src, offset, len), valueType);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> T readValue(DataInput src, Class<T> valueType) throws IOException {
/* 3315 */     _assertNotNull("src", src);
/* 3316 */     return (T)_readMapAndClose(this._jsonFactory.createParser(src), this._typeFactory
/* 3317 */         .constructType(valueType));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> T readValue(DataInput src, JavaType valueType) throws IOException {
/* 3323 */     _assertNotNull("src", src);
/* 3324 */     return (T)_readMapAndClose(this._jsonFactory.createParser(src), valueType);
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
/*      */   public void writeValue(File resultFile, Object value) throws IOException, JsonGenerationException, JsonMappingException {
/* 3341 */     _assertNotNull("resultFile", resultFile);
/* 3342 */     _configAndWriteValue(this._jsonFactory.createGenerator(resultFile, JsonEncoding.UTF8), value);
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
/*      */   public void writeValue(OutputStream out, Object value) throws IOException, JsonGenerationException, JsonMappingException {
/* 3359 */     _assertNotNull("out", out);
/* 3360 */     _configAndWriteValue(this._jsonFactory.createGenerator(out, JsonEncoding.UTF8), value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void writeValue(DataOutput out, Object value) throws IOException {
/* 3368 */     _assertNotNull("out", out);
/* 3369 */     _configAndWriteValue(this._jsonFactory.createGenerator(out, JsonEncoding.UTF8), value);
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
/*      */   public void writeValue(Writer w, Object value) throws IOException, JsonGenerationException, JsonMappingException {
/* 3385 */     _assertNotNull("w", w);
/* 3386 */     _configAndWriteValue(this._jsonFactory.createGenerator(w), value);
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
/*      */   public String writeValueAsString(Object value) throws JsonProcessingException {
/* 3402 */     SegmentedStringWriter sw = new SegmentedStringWriter(this._jsonFactory._getBufferRecycler());
/*      */     try {
/* 3404 */       _configAndWriteValue(this._jsonFactory.createGenerator((Writer)sw), value);
/* 3405 */     } catch (JsonProcessingException e) {
/* 3406 */       throw e;
/* 3407 */     } catch (IOException e) {
/* 3408 */       throw JsonMappingException.fromUnexpectedIOE(e);
/*      */     } 
/* 3410 */     return sw.getAndClear();
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
/*      */   public byte[] writeValueAsBytes(Object value) throws JsonProcessingException {
/* 3426 */     ByteArrayBuilder bb = new ByteArrayBuilder(this._jsonFactory._getBufferRecycler());
/*      */     try {
/* 3428 */       _configAndWriteValue(this._jsonFactory.createGenerator((OutputStream)bb, JsonEncoding.UTF8), value);
/* 3429 */     } catch (JsonProcessingException e) {
/* 3430 */       throw e;
/* 3431 */     } catch (IOException e) {
/* 3432 */       throw JsonMappingException.fromUnexpectedIOE(e);
/*      */     } 
/* 3434 */     byte[] result = bb.toByteArray();
/* 3435 */     bb.release();
/* 3436 */     return result;
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
/*      */   public ObjectWriter writer() {
/* 3451 */     return _newWriter(getSerializationConfig());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectWriter writer(SerializationFeature feature) {
/* 3460 */     return _newWriter(getSerializationConfig().with(feature));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectWriter writer(SerializationFeature first, SerializationFeature... other) {
/* 3470 */     return _newWriter(getSerializationConfig().with(first, other));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectWriter writer(DateFormat df) {
/* 3479 */     return _newWriter(getSerializationConfig().with(df));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectWriter writerWithView(Class<?> serializationView) {
/* 3487 */     return _newWriter(getSerializationConfig().withView(serializationView));
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
/*      */   public ObjectWriter writerFor(Class<?> rootType) {
/* 3502 */     return _newWriter(getSerializationConfig(), (rootType == null) ? null : this._typeFactory
/* 3503 */         .constructType(rootType), null);
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
/*      */   public ObjectWriter writerFor(TypeReference<?> rootType) {
/* 3519 */     return _newWriter(getSerializationConfig(), (rootType == null) ? null : this._typeFactory
/* 3520 */         .constructType(rootType), null);
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
/*      */   public ObjectWriter writerFor(JavaType rootType) {
/* 3536 */     return _newWriter(getSerializationConfig(), rootType, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectWriter writer(PrettyPrinter pp) {
/* 3545 */     if (pp == null) {
/* 3546 */       pp = ObjectWriter.NULL_PRETTY_PRINTER;
/*      */     }
/* 3548 */     return _newWriter(getSerializationConfig(), null, pp);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectWriter writerWithDefaultPrettyPrinter() {
/* 3556 */     SerializationConfig config = getSerializationConfig();
/* 3557 */     return _newWriter(config, null, config
/* 3558 */         .getDefaultPrettyPrinter());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectWriter writer(FilterProvider filterProvider) {
/* 3566 */     return _newWriter(getSerializationConfig().withFilters(filterProvider));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectWriter writer(FormatSchema schema) {
/* 3577 */     _verifySchemaType(schema);
/* 3578 */     return _newWriter(getSerializationConfig(), schema);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectWriter writer(Base64Variant defaultBase64) {
/* 3588 */     return _newWriter((SerializationConfig)getSerializationConfig().with(defaultBase64));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectWriter writer(CharacterEscapes escapes) {
/* 3598 */     return _newWriter(getSerializationConfig()).with(escapes);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectWriter writer(ContextAttributes attrs) {
/* 3608 */     return _newWriter(getSerializationConfig().with(attrs));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public ObjectWriter writerWithType(Class<?> rootType) {
/* 3616 */     return _newWriter(getSerializationConfig(), (rootType == null) ? null : this._typeFactory
/*      */         
/* 3618 */         .constructType(rootType), null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public ObjectWriter writerWithType(TypeReference<?> rootType) {
/* 3627 */     return _newWriter(getSerializationConfig(), (rootType == null) ? null : this._typeFactory
/*      */         
/* 3629 */         .constructType(rootType), null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public ObjectWriter writerWithType(JavaType rootType) {
/* 3638 */     return _newWriter(getSerializationConfig(), rootType, null);
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
/*      */   public ObjectReader reader() {
/* 3654 */     return _newReader(getDeserializationConfig()).with(this._injectableValues);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectReader reader(DeserializationFeature feature) {
/* 3665 */     return _newReader(getDeserializationConfig().with(feature));
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
/*      */   public ObjectReader reader(DeserializationFeature first, DeserializationFeature... other) {
/* 3677 */     return _newReader(getDeserializationConfig().with(first, other));
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
/*      */   public ObjectReader readerForUpdating(Object valueToUpdate) {
/* 3691 */     JavaType t = this._typeFactory.constructType(valueToUpdate.getClass());
/* 3692 */     return _newReader(getDeserializationConfig(), t, valueToUpdate, null, this._injectableValues);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectReader readerFor(JavaType type) {
/* 3703 */     return _newReader(getDeserializationConfig(), type, null, null, this._injectableValues);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectReader readerFor(Class<?> type) {
/* 3714 */     return _newReader(getDeserializationConfig(), this._typeFactory.constructType(type), null, null, this._injectableValues);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectReader readerFor(TypeReference<?> type) {
/* 3725 */     return _newReader(getDeserializationConfig(), this._typeFactory.constructType(type), null, null, this._injectableValues);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectReader reader(JsonNodeFactory f) {
/* 3734 */     return _newReader(getDeserializationConfig()).with(f);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectReader reader(FormatSchema schema) {
/* 3745 */     _verifySchemaType(schema);
/* 3746 */     return _newReader(getDeserializationConfig(), null, null, schema, this._injectableValues);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectReader reader(InjectableValues injectableValues) {
/* 3757 */     return _newReader(getDeserializationConfig(), null, null, null, injectableValues);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectReader readerWithView(Class<?> view) {
/* 3766 */     return _newReader(getDeserializationConfig().withView(view));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectReader reader(Base64Variant defaultBase64) {
/* 3776 */     return _newReader((DeserializationConfig)getDeserializationConfig().with(defaultBase64));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectReader reader(ContextAttributes attrs) {
/* 3786 */     return _newReader(getDeserializationConfig().with(attrs));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public ObjectReader reader(JavaType type) {
/* 3794 */     return _newReader(getDeserializationConfig(), type, null, null, this._injectableValues);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public ObjectReader reader(Class<?> type) {
/* 3803 */     return _newReader(getDeserializationConfig(), this._typeFactory.constructType(type), null, null, this._injectableValues);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public ObjectReader reader(TypeReference<?> type) {
/* 3812 */     return _newReader(getDeserializationConfig(), this._typeFactory.constructType(type), null, null, this._injectableValues);
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
/*      */   public <T> T convertValue(Object fromValue, Class<T> toValueType) throws IllegalArgumentException {
/* 3853 */     return (T)_convert(fromValue, this._typeFactory.constructType(toValueType));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> T convertValue(Object fromValue, TypeReference<T> toValueTypeRef) throws IllegalArgumentException {
/* 3863 */     return (T)_convert(fromValue, this._typeFactory.constructType(toValueTypeRef));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> T convertValue(Object fromValue, JavaType toValueType) throws IllegalArgumentException {
/* 3873 */     return (T)_convert(fromValue, toValueType);
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
/*      */   protected Object _convert(Object fromValue, JavaType toValueType) throws IllegalArgumentException {
/* 3891 */     TokenBuffer buf = new TokenBuffer(this, false);
/* 3892 */     if (isEnabled(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS)) {
/* 3893 */       buf = buf.forceUseOfBigDecimal(true);
/*      */     }
/*      */     
/*      */     try {
/*      */       Object result;
/* 3898 */       SerializationConfig config = getSerializationConfig().without(SerializationFeature.WRAP_ROOT_VALUE);
/*      */       
/* 3900 */       _serializerProvider(config).serializeValue((JsonGenerator)buf, fromValue);
/*      */ 
/*      */       
/* 3903 */       JsonParser p = buf.asParser();
/*      */ 
/*      */       
/* 3906 */       DeserializationConfig deserConfig = getDeserializationConfig();
/* 3907 */       JsonToken t = _initForReading(p, toValueType);
/* 3908 */       if (t == JsonToken.VALUE_NULL) {
/* 3909 */         DefaultDeserializationContext defaultDeserializationContext = createDeserializationContext(p, deserConfig);
/* 3910 */         result = _findRootDeserializer((DeserializationContext)defaultDeserializationContext, toValueType).getNullValue((DeserializationContext)defaultDeserializationContext);
/* 3911 */       } else if (t == JsonToken.END_ARRAY || t == JsonToken.END_OBJECT) {
/* 3912 */         result = null;
/*      */       } else {
/* 3914 */         DefaultDeserializationContext defaultDeserializationContext = createDeserializationContext(p, deserConfig);
/* 3915 */         JsonDeserializer<Object> deser = _findRootDeserializer((DeserializationContext)defaultDeserializationContext, toValueType);
/*      */         
/* 3917 */         result = deser.deserialize(p, (DeserializationContext)defaultDeserializationContext);
/*      */       } 
/* 3919 */       p.close();
/* 3920 */       return result;
/* 3921 */     } catch (IOException e) {
/* 3922 */       throw new IllegalArgumentException(e.getMessage(), e);
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
/*      */   public <T> T updateValue(T valueToUpdate, Object overrides) throws JsonMappingException {
/* 3965 */     T result = valueToUpdate;
/* 3966 */     if (valueToUpdate != null && overrides != null) {
/* 3967 */       TokenBuffer buf = new TokenBuffer(this, false);
/* 3968 */       if (isEnabled(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS)) {
/* 3969 */         buf = buf.forceUseOfBigDecimal(true);
/*      */       }
/*      */       
/*      */       try {
/* 3973 */         SerializationConfig config = getSerializationConfig().without(SerializationFeature.WRAP_ROOT_VALUE);
/* 3974 */         _serializerProvider(config).serializeValue((JsonGenerator)buf, overrides);
/* 3975 */         JsonParser p = buf.asParser();
/* 3976 */         result = readerForUpdating(valueToUpdate).readValue(p);
/* 3977 */         p.close();
/* 3978 */       } catch (IOException e) {
/* 3979 */         if (e instanceof JsonMappingException) {
/* 3980 */           throw (JsonMappingException)e;
/*      */         }
/*      */         
/* 3983 */         throw JsonMappingException.fromUnexpectedIOE(e);
/*      */       } 
/*      */     } 
/* 3986 */     return result;
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
/*      */   @Deprecated
/*      */   public JsonSchema generateJsonSchema(Class<?> t) throws JsonMappingException {
/* 4008 */     return _serializerProvider(getSerializationConfig()).generateJsonSchema(t);
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
/*      */   public void acceptJsonFormatVisitor(Class<?> type, JsonFormatVisitorWrapper visitor) throws JsonMappingException {
/* 4025 */     acceptJsonFormatVisitor(this._typeFactory.constructType(type), visitor);
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
/*      */   public void acceptJsonFormatVisitor(JavaType type, JsonFormatVisitorWrapper visitor) throws JsonMappingException {
/* 4043 */     if (type == null) {
/* 4044 */       throw new IllegalArgumentException("type must be provided");
/*      */     }
/* 4046 */     _serializerProvider(getSerializationConfig()).acceptJsonFormatVisitor(type, visitor);
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
/*      */   protected TypeResolverBuilder<?> _constructDefaultTypeResolverBuilder(DefaultTyping applicability, PolymorphicTypeValidator ptv) {
/* 4063 */     return (TypeResolverBuilder<?>)DefaultTypeResolverBuilder.construct(applicability, ptv);
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
/*      */   protected DefaultSerializerProvider _serializerProvider(SerializationConfig config) {
/* 4077 */     return this._serializerProvider.createInstance(config, this._serializerFactory);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final void _configAndWriteValue(JsonGenerator g, Object value) throws IOException {
/* 4087 */     SerializationConfig cfg = getSerializationConfig();
/* 4088 */     cfg.initialize(g);
/* 4089 */     if (cfg.isEnabled(SerializationFeature.CLOSE_CLOSEABLE) && value instanceof Closeable) {
/* 4090 */       _configAndWriteCloseable(g, value, cfg);
/*      */       return;
/*      */     } 
/*      */     try {
/* 4094 */       _serializerProvider(cfg).serializeValue(g, value);
/* 4095 */     } catch (Exception e) {
/* 4096 */       ClassUtil.closeOnFailAndThrowAsIOE(g, e);
/*      */       return;
/*      */     } 
/* 4099 */     g.close();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final void _configAndWriteCloseable(JsonGenerator g, Object value, SerializationConfig cfg) throws IOException {
/* 4109 */     Closeable toClose = (Closeable)value;
/*      */     try {
/* 4111 */       _serializerProvider(cfg).serializeValue(g, value);
/* 4112 */       Closeable tmpToClose = toClose;
/* 4113 */       toClose = null;
/* 4114 */       tmpToClose.close();
/* 4115 */     } catch (Exception e) {
/* 4116 */       ClassUtil.closeOnFailAndThrowAsIOE(g, toClose, e);
/*      */       return;
/*      */     } 
/* 4119 */     g.close();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final void _writeCloseableValue(JsonGenerator g, Object value, SerializationConfig cfg) throws IOException {
/* 4129 */     Closeable toClose = (Closeable)value;
/*      */     try {
/* 4131 */       _serializerProvider(cfg).serializeValue(g, value);
/* 4132 */       if (cfg.isEnabled(SerializationFeature.FLUSH_AFTER_WRITE_VALUE)) {
/* 4133 */         g.flush();
/*      */       }
/* 4135 */     } catch (Exception e) {
/* 4136 */       ClassUtil.closeOnFailAndThrowAsIOE(null, toClose, e);
/*      */       return;
/*      */     } 
/* 4139 */     toClose.close();
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
/*      */   protected Object _readValue(DeserializationConfig cfg, JsonParser p, JavaType valueType) throws IOException {
/*      */     Object result;
/* 4160 */     JsonToken t = _initForReading(p, valueType);
/* 4161 */     DefaultDeserializationContext defaultDeserializationContext = createDeserializationContext(p, cfg);
/* 4162 */     if (t == JsonToken.VALUE_NULL) {
/*      */       
/* 4164 */       result = _findRootDeserializer((DeserializationContext)defaultDeserializationContext, valueType).getNullValue((DeserializationContext)defaultDeserializationContext);
/* 4165 */     } else if (t == JsonToken.END_ARRAY || t == JsonToken.END_OBJECT) {
/* 4166 */       result = null;
/*      */     } else {
/* 4168 */       JsonDeserializer<Object> deser = _findRootDeserializer((DeserializationContext)defaultDeserializationContext, valueType);
/*      */       
/* 4170 */       if (cfg.useRootWrapping()) {
/* 4171 */         result = _unwrapAndDeserialize(p, (DeserializationContext)defaultDeserializationContext, cfg, valueType, deser);
/*      */       } else {
/* 4173 */         result = deser.deserialize(p, (DeserializationContext)defaultDeserializationContext);
/*      */       } 
/*      */     } 
/*      */     
/* 4177 */     p.clearCurrentToken();
/* 4178 */     if (cfg.isEnabled(DeserializationFeature.FAIL_ON_TRAILING_TOKENS)) {
/* 4179 */       _verifyNoTrailingTokens(p, (DeserializationContext)defaultDeserializationContext, valueType);
/*      */     }
/* 4181 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected Object _readMapAndClose(JsonParser p0, JavaType valueType) throws IOException {
/* 4187 */     try (JsonParser p = p0) {
/*      */       Object result;
/* 4189 */       JsonToken t = _initForReading(p, valueType);
/* 4190 */       DeserializationConfig cfg = getDeserializationConfig();
/* 4191 */       DefaultDeserializationContext defaultDeserializationContext = createDeserializationContext(p, cfg);
/* 4192 */       if (t == JsonToken.VALUE_NULL) {
/*      */         
/* 4194 */         result = _findRootDeserializer((DeserializationContext)defaultDeserializationContext, valueType).getNullValue((DeserializationContext)defaultDeserializationContext);
/* 4195 */       } else if (t == JsonToken.END_ARRAY || t == JsonToken.END_OBJECT) {
/* 4196 */         result = null;
/*      */       } else {
/* 4198 */         JsonDeserializer<Object> deser = _findRootDeserializer((DeserializationContext)defaultDeserializationContext, valueType);
/* 4199 */         if (cfg.useRootWrapping()) {
/* 4200 */           result = _unwrapAndDeserialize(p, (DeserializationContext)defaultDeserializationContext, cfg, valueType, deser);
/*      */         } else {
/* 4202 */           result = deser.deserialize(p, (DeserializationContext)defaultDeserializationContext);
/*      */         } 
/* 4204 */         defaultDeserializationContext.checkUnresolvedObjectId();
/*      */       } 
/* 4206 */       if (cfg.isEnabled(DeserializationFeature.FAIL_ON_TRAILING_TOKENS)) {
/* 4207 */         _verifyNoTrailingTokens(p, (DeserializationContext)defaultDeserializationContext, valueType);
/*      */       }
/* 4209 */       return result;
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
/*      */   protected JsonNode _readTreeAndClose(JsonParser p0) throws IOException {
/* 4221 */     try (JsonParser p = p0) {
/* 4222 */       DefaultDeserializationContext defaultDeserializationContext; JsonNode resultNode; JavaType valueType = constructType(JsonNode.class);
/*      */       
/* 4224 */       DeserializationConfig cfg = getDeserializationConfig();
/*      */ 
/*      */ 
/*      */       
/* 4228 */       cfg.initialize(p);
/* 4229 */       JsonToken t = p.getCurrentToken();
/* 4230 */       if (t == null) {
/* 4231 */         t = p.nextToken();
/* 4232 */         if (t == null)
/*      */         {
/*      */           
/* 4235 */           return cfg.getNodeFactory().missingNode();
/*      */         }
/*      */       } 
/* 4238 */       boolean checkTrailing = cfg.isEnabled(DeserializationFeature.FAIL_ON_TRAILING_TOKENS);
/*      */ 
/*      */ 
/*      */       
/* 4242 */       if (t == JsonToken.VALUE_NULL) {
/* 4243 */         NullNode nullNode = cfg.getNodeFactory().nullNode();
/* 4244 */         if (!checkTrailing) {
/* 4245 */           return (JsonNode)nullNode;
/*      */         }
/* 4247 */         defaultDeserializationContext = createDeserializationContext(p, cfg);
/*      */       } else {
/* 4249 */         defaultDeserializationContext = createDeserializationContext(p, cfg);
/* 4250 */         JsonDeserializer<Object> deser = _findRootDeserializer((DeserializationContext)defaultDeserializationContext, valueType);
/* 4251 */         if (cfg.useRootWrapping()) {
/* 4252 */           resultNode = (JsonNode)_unwrapAndDeserialize(p, (DeserializationContext)defaultDeserializationContext, cfg, valueType, deser);
/*      */         } else {
/* 4254 */           resultNode = (JsonNode)deser.deserialize(p, (DeserializationContext)defaultDeserializationContext);
/*      */         } 
/*      */       } 
/* 4257 */       if (checkTrailing) {
/* 4258 */         _verifyNoTrailingTokens(p, (DeserializationContext)defaultDeserializationContext, valueType);
/*      */       }
/*      */ 
/*      */       
/* 4262 */       return resultNode;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Object _unwrapAndDeserialize(JsonParser p, DeserializationContext ctxt, DeserializationConfig config, JavaType rootType, JsonDeserializer<Object> deser) throws IOException {
/* 4271 */     PropertyName expRootName = config.findRootName(rootType);
/*      */     
/* 4273 */     String expSimpleName = expRootName.getSimpleName();
/* 4274 */     if (p.getCurrentToken() != JsonToken.START_OBJECT) {
/* 4275 */       ctxt.reportWrongTokenException(rootType, JsonToken.START_OBJECT, "Current token not START_OBJECT (needed to unwrap root name '%s'), but %s", new Object[] { expSimpleName, p
/*      */             
/* 4277 */             .getCurrentToken() });
/*      */     }
/* 4279 */     if (p.nextToken() != JsonToken.FIELD_NAME) {
/* 4280 */       ctxt.reportWrongTokenException(rootType, JsonToken.FIELD_NAME, "Current token not FIELD_NAME (to contain expected root name '%s'), but %s", new Object[] { expSimpleName, p
/*      */             
/* 4282 */             .getCurrentToken() });
/*      */     }
/* 4284 */     String actualName = p.getCurrentName();
/* 4285 */     if (!expSimpleName.equals(actualName)) {
/* 4286 */       ctxt.reportPropertyInputMismatch(rootType, actualName, "Root name '%s' does not match expected ('%s') for type %s", new Object[] { actualName, expSimpleName, rootType });
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 4291 */     p.nextToken();
/* 4292 */     Object result = deser.deserialize(p, ctxt);
/*      */     
/* 4294 */     if (p.nextToken() != JsonToken.END_OBJECT) {
/* 4295 */       ctxt.reportWrongTokenException(rootType, JsonToken.END_OBJECT, "Current token not END_OBJECT (to match wrapper object with root name '%s'), but %s", new Object[] { expSimpleName, p
/*      */             
/* 4297 */             .getCurrentToken() });
/*      */     }
/* 4299 */     if (config.isEnabled(DeserializationFeature.FAIL_ON_TRAILING_TOKENS)) {
/* 4300 */       _verifyNoTrailingTokens(p, ctxt, rootType);
/*      */     }
/* 4302 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected DefaultDeserializationContext createDeserializationContext(JsonParser p, DeserializationConfig cfg) {
/* 4312 */     return this._deserializationContext.createInstance(cfg, p, this._injectableValues);
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
/*      */   protected JsonToken _initForReading(JsonParser p, JavaType targetType) throws IOException {
/* 4332 */     this._deserializationConfig.initialize(p);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4337 */     JsonToken t = p.getCurrentToken();
/* 4338 */     if (t == null) {
/*      */       
/* 4340 */       t = p.nextToken();
/* 4341 */       if (t == null)
/*      */       {
/*      */         
/* 4344 */         throw MismatchedInputException.from(p, targetType, "No content to map due to end-of-input");
/*      */       }
/*      */     } 
/*      */     
/* 4348 */     return t;
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   protected JsonToken _initForReading(JsonParser p) throws IOException {
/* 4353 */     return _initForReading(p, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final void _verifyNoTrailingTokens(JsonParser p, DeserializationContext ctxt, JavaType bindType) throws IOException {
/* 4363 */     JsonToken t = p.nextToken();
/* 4364 */     if (t != null) {
/* 4365 */       Class<?> bt = ClassUtil.rawClass(bindType);
/* 4366 */       ctxt.reportTrailingTokens(bt, p, t);
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
/*      */   protected JsonDeserializer<Object> _findRootDeserializer(DeserializationContext ctxt, JavaType valueType) throws JsonMappingException {
/* 4384 */     JsonDeserializer<Object> deser = this._rootDeserializers.get(valueType);
/* 4385 */     if (deser != null) {
/* 4386 */       return deser;
/*      */     }
/*      */     
/* 4389 */     deser = ctxt.findRootValueDeserializer(valueType);
/* 4390 */     if (deser == null) {
/* 4391 */       return ctxt.<JsonDeserializer<Object>>reportBadDefinition(valueType, "Cannot find a deserializer for type " + valueType);
/*      */     }
/*      */     
/* 4394 */     this._rootDeserializers.put(valueType, deser);
/* 4395 */     return deser;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void _verifySchemaType(FormatSchema schema) {
/* 4403 */     if (schema != null && 
/* 4404 */       !this._jsonFactory.canUseSchema(schema)) {
/* 4405 */       throw new IllegalArgumentException("Cannot use FormatSchema of type " + schema.getClass().getName() + " for format " + this._jsonFactory
/* 4406 */           .getFormatName());
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   protected final void _assertNotNull(String paramName, Object src) {
/* 4412 */     if (src == null)
/* 4413 */       throw new IllegalArgumentException(String.format("argument \"%s\" is null", new Object[] { paramName })); 
/*      */   }
/*      */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\ObjectMapper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */