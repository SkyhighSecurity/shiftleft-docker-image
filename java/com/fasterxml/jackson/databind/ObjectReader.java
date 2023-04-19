/*      */ package com.fasterxml.jackson.databind;
/*      */ 
/*      */ import com.fasterxml.jackson.core.Base64Variant;
/*      */ import com.fasterxml.jackson.core.FormatFeature;
/*      */ import com.fasterxml.jackson.core.FormatSchema;
/*      */ import com.fasterxml.jackson.core.JsonFactory;
/*      */ import com.fasterxml.jackson.core.JsonGenerator;
/*      */ import com.fasterxml.jackson.core.JsonParseException;
/*      */ import com.fasterxml.jackson.core.JsonParser;
/*      */ import com.fasterxml.jackson.core.JsonPointer;
/*      */ import com.fasterxml.jackson.core.JsonProcessingException;
/*      */ import com.fasterxml.jackson.core.JsonToken;
/*      */ import com.fasterxml.jackson.core.ObjectCodec;
/*      */ import com.fasterxml.jackson.core.TreeNode;
/*      */ import com.fasterxml.jackson.core.Version;
/*      */ import com.fasterxml.jackson.core.Versioned;
/*      */ import com.fasterxml.jackson.core.filter.FilteringParserDelegate;
/*      */ import com.fasterxml.jackson.core.filter.JsonPointerBasedFilter;
/*      */ import com.fasterxml.jackson.core.filter.TokenFilter;
/*      */ import com.fasterxml.jackson.core.type.ResolvedType;
/*      */ import com.fasterxml.jackson.core.type.TypeReference;
/*      */ import com.fasterxml.jackson.databind.cfg.ContextAttributes;
/*      */ import com.fasterxml.jackson.databind.cfg.PackageVersion;
/*      */ import com.fasterxml.jackson.databind.deser.DataFormatReaders;
/*      */ import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;
/*      */ import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;
/*      */ import com.fasterxml.jackson.databind.node.JsonNodeFactory;
/*      */ import com.fasterxml.jackson.databind.node.NullNode;
/*      */ import com.fasterxml.jackson.databind.node.TreeTraversingParser;
/*      */ import com.fasterxml.jackson.databind.type.TypeFactory;
/*      */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*      */ import java.io.DataInput;
/*      */ import java.io.File;
/*      */ import java.io.FileInputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.Reader;
/*      */ import java.io.Serializable;
/*      */ import java.lang.reflect.Type;
/*      */ import java.net.URL;
/*      */ import java.util.Iterator;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.TimeZone;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class ObjectReader
/*      */   extends ObjectCodec
/*      */   implements Versioned, Serializable
/*      */ {
/*      */   private static final long serialVersionUID = 2L;
/*      */   protected final DeserializationConfig _config;
/*      */   protected final DefaultDeserializationContext _context;
/*      */   protected final JsonFactory _parserFactory;
/*      */   protected final boolean _unwrapRoot;
/*      */   private final TokenFilter _filter;
/*      */   protected final JavaType _valueType;
/*      */   protected final JsonDeserializer<Object> _rootDeserializer;
/*      */   protected final Object _valueToUpdate;
/*      */   protected final FormatSchema _schema;
/*      */   protected final InjectableValues _injectableValues;
/*      */   protected final DataFormatReaders _dataFormatReaders;
/*      */   protected final ConcurrentHashMap<JavaType, JsonDeserializer<Object>> _rootDeserializers;
/*      */   protected transient JavaType _jsonNodeType;
/*      */   
/*      */   protected ObjectReader(ObjectMapper mapper, DeserializationConfig config) {
/*  170 */     this(mapper, config, null, null, null, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected ObjectReader(ObjectMapper mapper, DeserializationConfig config, JavaType valueType, Object valueToUpdate, FormatSchema schema, InjectableValues injectableValues) {
/*  181 */     this._config = config;
/*  182 */     this._context = mapper._deserializationContext;
/*  183 */     this._rootDeserializers = mapper._rootDeserializers;
/*  184 */     this._parserFactory = mapper._jsonFactory;
/*  185 */     this._valueType = valueType;
/*  186 */     this._valueToUpdate = valueToUpdate;
/*  187 */     this._schema = schema;
/*  188 */     this._injectableValues = injectableValues;
/*  189 */     this._unwrapRoot = config.useRootWrapping();
/*      */     
/*  191 */     this._rootDeserializer = _prefetchRootDeserializer(valueType);
/*  192 */     this._dataFormatReaders = null;
/*  193 */     this._filter = null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected ObjectReader(ObjectReader base, DeserializationConfig config, JavaType valueType, JsonDeserializer<Object> rootDeser, Object valueToUpdate, FormatSchema schema, InjectableValues injectableValues, DataFormatReaders dataFormatReaders) {
/*  204 */     this._config = config;
/*  205 */     this._context = base._context;
/*      */     
/*  207 */     this._rootDeserializers = base._rootDeserializers;
/*  208 */     this._parserFactory = base._parserFactory;
/*      */     
/*  210 */     this._valueType = valueType;
/*  211 */     this._rootDeserializer = rootDeser;
/*  212 */     this._valueToUpdate = valueToUpdate;
/*  213 */     this._schema = schema;
/*  214 */     this._injectableValues = injectableValues;
/*  215 */     this._unwrapRoot = config.useRootWrapping();
/*  216 */     this._dataFormatReaders = dataFormatReaders;
/*  217 */     this._filter = base._filter;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected ObjectReader(ObjectReader base, DeserializationConfig config) {
/*  225 */     this._config = config;
/*  226 */     this._context = base._context;
/*      */     
/*  228 */     this._rootDeserializers = base._rootDeserializers;
/*  229 */     this._parserFactory = base._parserFactory;
/*      */     
/*  231 */     this._valueType = base._valueType;
/*  232 */     this._rootDeserializer = base._rootDeserializer;
/*  233 */     this._valueToUpdate = base._valueToUpdate;
/*  234 */     this._schema = base._schema;
/*  235 */     this._injectableValues = base._injectableValues;
/*  236 */     this._unwrapRoot = config.useRootWrapping();
/*  237 */     this._dataFormatReaders = base._dataFormatReaders;
/*  238 */     this._filter = base._filter;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected ObjectReader(ObjectReader base, JsonFactory f) {
/*  244 */     this
/*  245 */       ._config = (DeserializationConfig)base._config.with(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, f.requiresPropertyOrdering());
/*  246 */     this._context = base._context;
/*      */     
/*  248 */     this._rootDeserializers = base._rootDeserializers;
/*  249 */     this._parserFactory = f;
/*      */     
/*  251 */     this._valueType = base._valueType;
/*  252 */     this._rootDeserializer = base._rootDeserializer;
/*  253 */     this._valueToUpdate = base._valueToUpdate;
/*  254 */     this._schema = base._schema;
/*  255 */     this._injectableValues = base._injectableValues;
/*  256 */     this._unwrapRoot = base._unwrapRoot;
/*  257 */     this._dataFormatReaders = base._dataFormatReaders;
/*  258 */     this._filter = base._filter;
/*      */   }
/*      */   
/*      */   protected ObjectReader(ObjectReader base, TokenFilter filter) {
/*  262 */     this._config = base._config;
/*  263 */     this._context = base._context;
/*  264 */     this._rootDeserializers = base._rootDeserializers;
/*  265 */     this._parserFactory = base._parserFactory;
/*  266 */     this._valueType = base._valueType;
/*  267 */     this._rootDeserializer = base._rootDeserializer;
/*  268 */     this._valueToUpdate = base._valueToUpdate;
/*  269 */     this._schema = base._schema;
/*  270 */     this._injectableValues = base._injectableValues;
/*  271 */     this._unwrapRoot = base._unwrapRoot;
/*  272 */     this._dataFormatReaders = base._dataFormatReaders;
/*  273 */     this._filter = filter;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Version version() {
/*  282 */     return PackageVersion.VERSION;
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
/*      */   protected ObjectReader _new(ObjectReader base, JsonFactory f) {
/*  299 */     return new ObjectReader(base, f);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected ObjectReader _new(ObjectReader base, DeserializationConfig config) {
/*  308 */     return new ObjectReader(base, config);
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
/*      */   protected ObjectReader _new(ObjectReader base, DeserializationConfig config, JavaType valueType, JsonDeserializer<Object> rootDeser, Object valueToUpdate, FormatSchema schema, InjectableValues injectableValues, DataFormatReaders dataFormatReaders) {
/*  320 */     return new ObjectReader(base, config, valueType, rootDeser, valueToUpdate, schema, injectableValues, dataFormatReaders);
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
/*      */   protected <T> MappingIterator<T> _newIterator(JsonParser p, DeserializationContext ctxt, JsonDeserializer<?> deser, boolean parserManaged) {
/*  333 */     return new MappingIterator<>(this._valueType, p, ctxt, deser, parserManaged, this._valueToUpdate);
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
/*      */   protected JsonToken _initForReading(DeserializationContext ctxt, JsonParser p) throws IOException {
/*  346 */     if (this._schema != null) {
/*  347 */       p.setSchema(this._schema);
/*      */     }
/*  349 */     this._config.initialize(p);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  355 */     JsonToken t = p.getCurrentToken();
/*  356 */     if (t == null) {
/*  357 */       t = p.nextToken();
/*  358 */       if (t == null)
/*      */       {
/*  360 */         ctxt.reportInputMismatch(this._valueType, "No content to map due to end-of-input", new Object[0]);
/*      */       }
/*      */     } 
/*      */     
/*  364 */     return t;
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
/*      */   protected void _initForMultiRead(DeserializationContext ctxt, JsonParser p) throws IOException {
/*  379 */     if (this._schema != null) {
/*  380 */       p.setSchema(this._schema);
/*      */     }
/*  382 */     this._config.initialize(p);
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
/*      */   public ObjectReader with(DeserializationFeature feature) {
/*  396 */     return _with(this._config.with(feature));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectReader with(DeserializationFeature first, DeserializationFeature... other) {
/*  406 */     return _with(this._config.with(first, other));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectReader withFeatures(DeserializationFeature... features) {
/*  414 */     return _with(this._config.withFeatures(features));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectReader without(DeserializationFeature feature) {
/*  422 */     return _with(this._config.without(feature));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectReader without(DeserializationFeature first, DeserializationFeature... other) {
/*  431 */     return _with(this._config.without(first, other));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectReader withoutFeatures(DeserializationFeature... features) {
/*  439 */     return _with(this._config.withoutFeatures(features));
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
/*      */   public ObjectReader with(JsonParser.Feature feature) {
/*  453 */     return _with(this._config.with(feature));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectReader withFeatures(JsonParser.Feature... features) {
/*  461 */     return _with(this._config.withFeatures(features));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectReader without(JsonParser.Feature feature) {
/*  469 */     return _with(this._config.without(feature));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectReader withoutFeatures(JsonParser.Feature... features) {
/*  477 */     return _with(this._config.withoutFeatures(features));
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
/*      */   public ObjectReader with(FormatFeature feature) {
/*  493 */     return _with(this._config.with(feature));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectReader withFeatures(FormatFeature... features) {
/*  503 */     return _with(this._config.withFeatures(features));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectReader without(FormatFeature feature) {
/*  513 */     return _with(this._config.without(feature));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectReader withoutFeatures(FormatFeature... features) {
/*  523 */     return _with(this._config.withoutFeatures(features));
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
/*      */   public ObjectReader at(String pointerExpr) {
/*  538 */     _assertNotNull("pointerExpr", pointerExpr);
/*  539 */     return new ObjectReader(this, (TokenFilter)new JsonPointerBasedFilter(pointerExpr));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectReader at(JsonPointer pointer) {
/*  548 */     _assertNotNull("pointer", pointer);
/*  549 */     return new ObjectReader(this, (TokenFilter)new JsonPointerBasedFilter(pointer));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectReader with(DeserializationConfig config) {
/*  560 */     return _with(config);
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
/*      */   public ObjectReader with(InjectableValues injectableValues) {
/*  572 */     if (this._injectableValues == injectableValues) {
/*  573 */       return this;
/*      */     }
/*  575 */     return _new(this, this._config, this._valueType, this._rootDeserializer, this._valueToUpdate, this._schema, injectableValues, this._dataFormatReaders);
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
/*      */   public ObjectReader with(JsonNodeFactory f) {
/*  589 */     return _with(this._config.with(f));
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
/*      */   public ObjectReader with(JsonFactory f) {
/*  604 */     if (f == this._parserFactory) {
/*  605 */       return this;
/*      */     }
/*  607 */     ObjectReader r = _new(this, f);
/*      */     
/*  609 */     if (f.getCodec() == null) {
/*  610 */       f.setCodec(r);
/*      */     }
/*  612 */     return r;
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
/*      */   public ObjectReader withRootName(String rootName) {
/*  625 */     return _with((DeserializationConfig)this._config.withRootName(rootName));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectReader withRootName(PropertyName rootName) {
/*  632 */     return _with(this._config.withRootName(rootName));
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
/*      */   public ObjectReader withoutRootName() {
/*  646 */     return _with(this._config.withRootName(PropertyName.NO_NAME));
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
/*      */   public ObjectReader with(FormatSchema schema) {
/*  659 */     if (this._schema == schema) {
/*  660 */       return this;
/*      */     }
/*  662 */     _verifySchemaType(schema);
/*  663 */     return _new(this, this._config, this._valueType, this._rootDeserializer, this._valueToUpdate, schema, this._injectableValues, this._dataFormatReaders);
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
/*      */   public ObjectReader forType(JavaType valueType) {
/*  678 */     if (valueType != null && valueType.equals(this._valueType)) {
/*  679 */       return this;
/*      */     }
/*  681 */     JsonDeserializer<Object> rootDeser = _prefetchRootDeserializer(valueType);
/*      */     
/*  683 */     DataFormatReaders det = this._dataFormatReaders;
/*  684 */     if (det != null) {
/*  685 */       det = det.withType(valueType);
/*      */     }
/*  687 */     return _new(this, this._config, valueType, rootDeser, this._valueToUpdate, this._schema, this._injectableValues, det);
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
/*      */   public ObjectReader forType(Class<?> valueType) {
/*  701 */     return forType(this._config.constructType(valueType));
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
/*      */   public ObjectReader forType(TypeReference<?> valueTypeRef) {
/*  714 */     return forType(this._config.getTypeFactory().constructType(valueTypeRef.getType()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public ObjectReader withType(JavaType valueType) {
/*  722 */     return forType(valueType);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public ObjectReader withType(Class<?> valueType) {
/*  730 */     return forType(this._config.constructType(valueType));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public ObjectReader withType(Type valueType) {
/*  738 */     return forType(this._config.getTypeFactory().constructType(valueType));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public ObjectReader withType(TypeReference<?> valueTypeRef) {
/*  746 */     return forType(this._config.getTypeFactory().constructType(valueTypeRef.getType()));
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
/*      */   public ObjectReader withValueToUpdate(Object value) {
/*      */     JavaType t;
/*  759 */     if (value == this._valueToUpdate) return this; 
/*  760 */     if (value == null)
/*      */     {
/*      */       
/*  763 */       return _new(this, this._config, this._valueType, this._rootDeserializer, null, this._schema, this._injectableValues, this._dataFormatReaders);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  772 */     if (this._valueType == null) {
/*  773 */       t = this._config.constructType(value.getClass());
/*      */     } else {
/*  775 */       t = this._valueType;
/*      */     } 
/*  777 */     return _new(this, this._config, t, this._rootDeserializer, value, this._schema, this._injectableValues, this._dataFormatReaders);
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
/*      */   public ObjectReader withView(Class<?> activeView) {
/*  789 */     return _with(this._config.withView(activeView));
/*      */   }
/*      */   
/*      */   public ObjectReader with(Locale l) {
/*  793 */     return _with((DeserializationConfig)this._config.with(l));
/*      */   }
/*      */   
/*      */   public ObjectReader with(TimeZone tz) {
/*  797 */     return _with((DeserializationConfig)this._config.with(tz));
/*      */   }
/*      */   
/*      */   public ObjectReader withHandler(DeserializationProblemHandler h) {
/*  801 */     return _with(this._config.withHandler(h));
/*      */   }
/*      */   
/*      */   public ObjectReader with(Base64Variant defaultBase64) {
/*  805 */     return _with((DeserializationConfig)this._config.with(defaultBase64));
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
/*      */   public ObjectReader withFormatDetection(ObjectReader... readers) {
/*  831 */     return withFormatDetection(new DataFormatReaders(readers));
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
/*      */   public ObjectReader withFormatDetection(DataFormatReaders readers) {
/*  850 */     return _new(this, this._config, this._valueType, this._rootDeserializer, this._valueToUpdate, this._schema, this._injectableValues, readers);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectReader with(ContextAttributes attrs) {
/*  858 */     return _with(this._config.with(attrs));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectReader withAttributes(Map<?, ?> attrs) {
/*  865 */     return _with((DeserializationConfig)this._config.withAttributes(attrs));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectReader withAttribute(Object key, Object value) {
/*  872 */     return _with((DeserializationConfig)this._config.withAttribute(key, value));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectReader withoutAttribute(Object key) {
/*  879 */     return _with((DeserializationConfig)this._config.withoutAttribute(key));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected ObjectReader _with(DeserializationConfig newConfig) {
/*  889 */     if (newConfig == this._config) {
/*  890 */       return this;
/*      */     }
/*  892 */     ObjectReader r = _new(this, newConfig);
/*  893 */     if (this._dataFormatReaders != null) {
/*  894 */       r = r.withFormatDetection(this._dataFormatReaders.with(newConfig));
/*      */     }
/*  896 */     return r;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isEnabled(DeserializationFeature f) {
/*  906 */     return this._config.isEnabled(f);
/*      */   }
/*      */   
/*      */   public boolean isEnabled(MapperFeature f) {
/*  910 */     return this._config.isEnabled(f);
/*      */   }
/*      */   
/*      */   public boolean isEnabled(JsonParser.Feature f) {
/*  914 */     return this._parserFactory.isEnabled(f);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public DeserializationConfig getConfig() {
/*  921 */     return this._config;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JsonFactory getFactory() {
/*  929 */     return this._parserFactory;
/*      */   }
/*      */   
/*      */   public TypeFactory getTypeFactory() {
/*  933 */     return this._config.getTypeFactory();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ContextAttributes getAttributes() {
/*  940 */     return this._config.getAttributes();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public InjectableValues getInjectableValues() {
/*  947 */     return this._injectableValues;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JavaType getValueType() {
/*  954 */     return this._valueType;
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
/*      */   public <T> T readValue(JsonParser p) throws IOException {
/*  976 */     _assertNotNull("p", p);
/*  977 */     return (T)_bind(p, this._valueToUpdate);
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
/*      */   public <T> T readValue(JsonParser p, Class<T> valueType) throws IOException {
/*  994 */     _assertNotNull("p", p);
/*  995 */     return forType(valueType).readValue(p);
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
/*      */   public <T> T readValue(JsonParser p, TypeReference<T> valueTypeRef) throws IOException {
/* 1012 */     _assertNotNull("p", p);
/* 1013 */     return forType(valueTypeRef).readValue(p);
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
/*      */   public <T> T readValue(JsonParser p, ResolvedType valueType) throws IOException {
/* 1029 */     _assertNotNull("p", p);
/* 1030 */     return forType((JavaType)valueType).readValue(p);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> T readValue(JsonParser p, JavaType valueType) throws IOException {
/* 1041 */     _assertNotNull("p", p);
/* 1042 */     return forType(valueType).readValue(p);
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
/*      */   public <T> Iterator<T> readValues(JsonParser p, Class<T> valueType) throws IOException {
/* 1066 */     _assertNotNull("p", p);
/* 1067 */     return forType(valueType).readValues(p);
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
/*      */   public <T> Iterator<T> readValues(JsonParser p, TypeReference<T> valueTypeRef) throws IOException {
/* 1091 */     _assertNotNull("p", p);
/* 1092 */     return forType(valueTypeRef).readValues(p);
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
/*      */   public <T> Iterator<T> readValues(JsonParser p, ResolvedType valueType) throws IOException {
/* 1116 */     _assertNotNull("p", p);
/* 1117 */     return readValues(p, (JavaType)valueType);
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
/*      */   public <T> Iterator<T> readValues(JsonParser p, JavaType valueType) throws IOException {
/* 1140 */     _assertNotNull("p", p);
/* 1141 */     return forType(valueType).readValues(p);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JsonNode createArrayNode() {
/* 1152 */     return (JsonNode)this._config.getNodeFactory().arrayNode();
/*      */   }
/*      */ 
/*      */   
/*      */   public JsonNode createObjectNode() {
/* 1157 */     return (JsonNode)this._config.getNodeFactory().objectNode();
/*      */   }
/*      */ 
/*      */   
/*      */   public JsonNode missingNode() {
/* 1162 */     return this._config.getNodeFactory().missingNode();
/*      */   }
/*      */ 
/*      */   
/*      */   public JsonNode nullNode() {
/* 1167 */     return (JsonNode)this._config.getNodeFactory().nullNode();
/*      */   }
/*      */ 
/*      */   
/*      */   public JsonParser treeAsTokens(TreeNode n) {
/* 1172 */     _assertNotNull("n", n);
/*      */ 
/*      */     
/* 1175 */     ObjectReader codec = withValueToUpdate(null);
/* 1176 */     return (JsonParser)new TreeTraversingParser((JsonNode)n, codec);
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
/*      */   public <T extends TreeNode> T readTree(JsonParser p) throws IOException {
/* 1200 */     _assertNotNull("p", p);
/* 1201 */     return (T)_bindAsTreeOrNull(p);
/*      */   }
/*      */ 
/*      */   
/*      */   public void writeTree(JsonGenerator g, TreeNode rootNode) {
/* 1206 */     throw new UnsupportedOperationException();
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
/*      */   public <T> T readValue(InputStream src) throws IOException {
/* 1224 */     _assertNotNull("src", src);
/* 1225 */     if (this._dataFormatReaders != null) {
/* 1226 */       return (T)_detectBindAndClose(this._dataFormatReaders.findFormat(src), false);
/*      */     }
/* 1228 */     return (T)_bindAndClose(_considerFilter(this._parserFactory.createParser(src), false));
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
/*      */   public <T> T readValue(Reader src) throws IOException {
/* 1240 */     _assertNotNull("src", src);
/* 1241 */     if (this._dataFormatReaders != null) {
/* 1242 */       _reportUndetectableSource(src);
/*      */     }
/* 1244 */     return (T)_bindAndClose(_considerFilter(this._parserFactory.createParser(src), false));
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
/*      */   public <T> T readValue(String src) throws JsonProcessingException, JsonMappingException {
/* 1256 */     _assertNotNull("src", src);
/* 1257 */     if (this._dataFormatReaders != null) {
/* 1258 */       _reportUndetectableSource(src);
/*      */     }
/*      */     try {
/* 1261 */       return (T)_bindAndClose(_considerFilter(this._parserFactory.createParser(src), false));
/* 1262 */     } catch (JsonProcessingException e) {
/* 1263 */       throw e;
/* 1264 */     } catch (IOException e) {
/* 1265 */       throw JsonMappingException.fromUnexpectedIOE(e);
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
/*      */   public <T> T readValue(byte[] src) throws IOException {
/* 1278 */     _assertNotNull("src", src);
/* 1279 */     if (this._dataFormatReaders != null) {
/* 1280 */       return (T)_detectBindAndClose(src, 0, src.length);
/*      */     }
/* 1282 */     return (T)_bindAndClose(_considerFilter(this._parserFactory.createParser(src), false));
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
/*      */   public <T> T readValue(byte[] src, int offset, int length) throws IOException {
/* 1294 */     _assertNotNull("src", src);
/* 1295 */     if (this._dataFormatReaders != null) {
/* 1296 */       return (T)_detectBindAndClose(src, offset, length);
/*      */     }
/* 1298 */     return (T)_bindAndClose(_considerFilter(this._parserFactory.createParser(src, offset, length), false));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> T readValue(File src) throws IOException {
/* 1305 */     _assertNotNull("src", src);
/* 1306 */     if (this._dataFormatReaders != null) {
/* 1307 */       return (T)_detectBindAndClose(this._dataFormatReaders.findFormat(_inputStream(src)), true);
/*      */     }
/*      */     
/* 1310 */     return (T)_bindAndClose(_considerFilter(this._parserFactory.createParser(src), false));
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
/*      */   public <T> T readValue(URL src) throws IOException {
/* 1329 */     _assertNotNull("src", src);
/* 1330 */     if (this._dataFormatReaders != null) {
/* 1331 */       return (T)_detectBindAndClose(this._dataFormatReaders.findFormat(_inputStream(src)), true);
/*      */     }
/* 1333 */     return (T)_bindAndClose(_considerFilter(this._parserFactory.createParser(src), false));
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
/*      */   public <T> T readValue(JsonNode src) throws IOException {
/* 1346 */     _assertNotNull("src", src);
/* 1347 */     if (this._dataFormatReaders != null) {
/* 1348 */       _reportUndetectableSource(src);
/*      */     }
/* 1350 */     return (T)_bindAndClose(_considerFilter(treeAsTokens(src), false));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> T readValue(DataInput src) throws IOException {
/* 1359 */     _assertNotNull("src", src);
/* 1360 */     if (this._dataFormatReaders != null) {
/* 1361 */       _reportUndetectableSource(src);
/*      */     }
/* 1363 */     return (T)_bindAndClose(_considerFilter(this._parserFactory.createParser(src), false));
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
/*      */   public JsonNode readTree(InputStream src) throws IOException {
/* 1391 */     _assertNotNull("src", src);
/* 1392 */     if (this._dataFormatReaders != null) {
/* 1393 */       return _detectBindAndCloseAsTree(src);
/*      */     }
/* 1395 */     return _bindAndCloseAsTree(_considerFilter(this._parserFactory.createParser(src), false));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JsonNode readTree(Reader src) throws IOException {
/* 1404 */     _assertNotNull("src", src);
/* 1405 */     if (this._dataFormatReaders != null) {
/* 1406 */       _reportUndetectableSource(src);
/*      */     }
/* 1408 */     return _bindAndCloseAsTree(_considerFilter(this._parserFactory.createParser(src), false));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JsonNode readTree(String json) throws JsonProcessingException, JsonMappingException {
/* 1417 */     _assertNotNull("json", json);
/* 1418 */     if (this._dataFormatReaders != null) {
/* 1419 */       _reportUndetectableSource(json);
/*      */     }
/*      */     try {
/* 1422 */       return _bindAndCloseAsTree(_considerFilter(this._parserFactory.createParser(json), false));
/* 1423 */     } catch (JsonProcessingException e) {
/* 1424 */       throw e;
/* 1425 */     } catch (IOException e) {
/* 1426 */       throw JsonMappingException.fromUnexpectedIOE(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JsonNode readTree(byte[] json) throws IOException {
/* 1436 */     _assertNotNull("json", json);
/* 1437 */     if (this._dataFormatReaders != null) {
/* 1438 */       _reportUndetectableSource(json);
/*      */     }
/* 1440 */     return _bindAndCloseAsTree(_considerFilter(this._parserFactory.createParser(json), false));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JsonNode readTree(byte[] json, int offset, int len) throws IOException {
/* 1449 */     _assertNotNull("json", json);
/* 1450 */     if (this._dataFormatReaders != null) {
/* 1451 */       _reportUndetectableSource(json);
/*      */     }
/* 1453 */     return _bindAndCloseAsTree(_considerFilter(this._parserFactory.createParser(json, offset, len), false));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JsonNode readTree(DataInput src) throws IOException {
/* 1462 */     _assertNotNull("src", src);
/* 1463 */     if (this._dataFormatReaders != null) {
/* 1464 */       _reportUndetectableSource(src);
/*      */     }
/* 1466 */     return _bindAndCloseAsTree(_considerFilter(this._parserFactory.createParser(src), false));
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
/*      */   public <T> MappingIterator<T> readValues(JsonParser p) throws IOException {
/* 1488 */     _assertNotNull("p", p);
/* 1489 */     DefaultDeserializationContext defaultDeserializationContext = createDeserializationContext(p);
/*      */     
/* 1491 */     return _newIterator(p, (DeserializationContext)defaultDeserializationContext, _findRootDeserializer((DeserializationContext)defaultDeserializationContext), false);
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
/*      */   public <T> MappingIterator<T> readValues(InputStream src) throws IOException {
/* 1516 */     _assertNotNull("src", src);
/* 1517 */     if (this._dataFormatReaders != null) {
/* 1518 */       return _detectBindAndReadValues(this._dataFormatReaders.findFormat(src), false);
/*      */     }
/*      */     
/* 1521 */     return _bindAndReadValues(_considerFilter(this._parserFactory.createParser(src), true));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> MappingIterator<T> readValues(Reader src) throws IOException {
/* 1530 */     _assertNotNull("src", src);
/* 1531 */     if (this._dataFormatReaders != null) {
/* 1532 */       _reportUndetectableSource(src);
/*      */     }
/* 1534 */     JsonParser p = _considerFilter(this._parserFactory.createParser(src), true);
/* 1535 */     DefaultDeserializationContext defaultDeserializationContext = createDeserializationContext(p);
/* 1536 */     _initForMultiRead((DeserializationContext)defaultDeserializationContext, p);
/* 1537 */     p.nextToken();
/* 1538 */     return _newIterator(p, (DeserializationContext)defaultDeserializationContext, _findRootDeserializer((DeserializationContext)defaultDeserializationContext), true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> MappingIterator<T> readValues(String json) throws IOException {
/* 1549 */     _assertNotNull("json", json);
/* 1550 */     if (this._dataFormatReaders != null) {
/* 1551 */       _reportUndetectableSource(json);
/*      */     }
/* 1553 */     JsonParser p = _considerFilter(this._parserFactory.createParser(json), true);
/* 1554 */     DefaultDeserializationContext defaultDeserializationContext = createDeserializationContext(p);
/* 1555 */     _initForMultiRead((DeserializationContext)defaultDeserializationContext, p);
/* 1556 */     p.nextToken();
/* 1557 */     return _newIterator(p, (DeserializationContext)defaultDeserializationContext, _findRootDeserializer((DeserializationContext)defaultDeserializationContext), true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> MappingIterator<T> readValues(byte[] src, int offset, int length) throws IOException {
/* 1565 */     _assertNotNull("src", src);
/* 1566 */     if (this._dataFormatReaders != null) {
/* 1567 */       return _detectBindAndReadValues(this._dataFormatReaders.findFormat(src, offset, length), false);
/*      */     }
/* 1569 */     return _bindAndReadValues(_considerFilter(this._parserFactory.createParser(src, offset, length), true));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final <T> MappingIterator<T> readValues(byte[] src) throws IOException {
/* 1577 */     _assertNotNull("src", src);
/* 1578 */     return readValues(src, 0, src.length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> MappingIterator<T> readValues(File src) throws IOException {
/* 1586 */     _assertNotNull("src", src);
/* 1587 */     if (this._dataFormatReaders != null) {
/* 1588 */       return _detectBindAndReadValues(this._dataFormatReaders
/* 1589 */           .findFormat(_inputStream(src)), false);
/*      */     }
/* 1591 */     return _bindAndReadValues(_considerFilter(this._parserFactory.createParser(src), true));
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
/*      */   public <T> MappingIterator<T> readValues(URL src) throws IOException {
/* 1607 */     _assertNotNull("src", src);
/* 1608 */     if (this._dataFormatReaders != null) {
/* 1609 */       return _detectBindAndReadValues(this._dataFormatReaders
/* 1610 */           .findFormat(_inputStream(src)), true);
/*      */     }
/* 1612 */     return _bindAndReadValues(_considerFilter(this._parserFactory.createParser(src), true));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> MappingIterator<T> readValues(DataInput src) throws IOException {
/* 1620 */     _assertNotNull("src", src);
/* 1621 */     if (this._dataFormatReaders != null) {
/* 1622 */       _reportUndetectableSource(src);
/*      */     }
/* 1624 */     return _bindAndReadValues(_considerFilter(this._parserFactory.createParser(src), true));
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
/*      */   public <T> T treeToValue(TreeNode n, Class<T> valueType) throws JsonProcessingException {
/* 1636 */     _assertNotNull("n", n);
/*      */     try {
/* 1638 */       return readValue(treeAsTokens(n), valueType);
/* 1639 */     } catch (JsonProcessingException e) {
/* 1640 */       throw e;
/* 1641 */     } catch (IOException e) {
/* 1642 */       throw JsonMappingException.fromUnexpectedIOE(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void writeValue(JsonGenerator gen, Object value) throws IOException {
/* 1648 */     throw new UnsupportedOperationException("Not implemented for ObjectReader");
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
/*      */   protected Object _bind(JsonParser p, Object valueToUpdate) throws IOException {
/*      */     Object result;
/* 1666 */     DefaultDeserializationContext defaultDeserializationContext = createDeserializationContext(p);
/* 1667 */     JsonToken t = _initForReading((DeserializationContext)defaultDeserializationContext, p);
/* 1668 */     if (t == JsonToken.VALUE_NULL) {
/* 1669 */       if (valueToUpdate == null) {
/* 1670 */         result = _findRootDeserializer((DeserializationContext)defaultDeserializationContext).getNullValue((DeserializationContext)defaultDeserializationContext);
/*      */       } else {
/* 1672 */         result = valueToUpdate;
/*      */       } 
/* 1674 */     } else if (t == JsonToken.END_ARRAY || t == JsonToken.END_OBJECT) {
/* 1675 */       result = valueToUpdate;
/*      */     } else {
/* 1677 */       JsonDeserializer<Object> deser = _findRootDeserializer((DeserializationContext)defaultDeserializationContext);
/* 1678 */       if (this._unwrapRoot) {
/* 1679 */         result = _unwrapAndDeserialize(p, (DeserializationContext)defaultDeserializationContext, this._valueType, deser);
/*      */       }
/* 1681 */       else if (valueToUpdate == null) {
/* 1682 */         result = deser.deserialize(p, (DeserializationContext)defaultDeserializationContext);
/*      */       }
/*      */       else {
/*      */         
/* 1686 */         result = deser.deserialize(p, (DeserializationContext)defaultDeserializationContext, valueToUpdate);
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 1691 */     p.clearCurrentToken();
/* 1692 */     if (this._config.isEnabled(DeserializationFeature.FAIL_ON_TRAILING_TOKENS)) {
/* 1693 */       _verifyNoTrailingTokens(p, (DeserializationContext)defaultDeserializationContext, this._valueType);
/*      */     }
/* 1695 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   protected Object _bindAndClose(JsonParser p0) throws IOException {
/* 1700 */     try (JsonParser p = p0) {
/*      */       Object result;
/*      */       
/* 1703 */       DefaultDeserializationContext defaultDeserializationContext = createDeserializationContext(p);
/* 1704 */       JsonToken t = _initForReading((DeserializationContext)defaultDeserializationContext, p);
/* 1705 */       if (t == JsonToken.VALUE_NULL) {
/* 1706 */         if (this._valueToUpdate == null) {
/* 1707 */           result = _findRootDeserializer((DeserializationContext)defaultDeserializationContext).getNullValue((DeserializationContext)defaultDeserializationContext);
/*      */         } else {
/* 1709 */           result = this._valueToUpdate;
/*      */         } 
/* 1711 */       } else if (t == JsonToken.END_ARRAY || t == JsonToken.END_OBJECT) {
/* 1712 */         result = this._valueToUpdate;
/*      */       } else {
/* 1714 */         JsonDeserializer<Object> deser = _findRootDeserializer((DeserializationContext)defaultDeserializationContext);
/* 1715 */         if (this._unwrapRoot) {
/* 1716 */           result = _unwrapAndDeserialize(p, (DeserializationContext)defaultDeserializationContext, this._valueType, deser);
/*      */         }
/* 1718 */         else if (this._valueToUpdate == null) {
/* 1719 */           result = deser.deserialize(p, (DeserializationContext)defaultDeserializationContext);
/*      */         } else {
/* 1721 */           deser.deserialize(p, (DeserializationContext)defaultDeserializationContext, this._valueToUpdate);
/* 1722 */           result = this._valueToUpdate;
/*      */         } 
/*      */       } 
/*      */       
/* 1726 */       if (this._config.isEnabled(DeserializationFeature.FAIL_ON_TRAILING_TOKENS)) {
/* 1727 */         _verifyNoTrailingTokens(p, (DeserializationContext)defaultDeserializationContext, this._valueType);
/*      */       }
/* 1729 */       return result;
/*      */     } 
/*      */   }
/*      */   
/*      */   protected final JsonNode _bindAndCloseAsTree(JsonParser p0) throws IOException {
/* 1734 */     try (JsonParser p = p0) {
/* 1735 */       return _bindAsTree(p);
/*      */     } 
/*      */   }
/*      */   
/*      */   protected final JsonNode _bindAsTree(JsonParser p) throws IOException {
/*      */     DefaultDeserializationContext defaultDeserializationContext;
/*      */     JsonNode resultNode;
/* 1742 */     this._config.initialize(p);
/* 1743 */     if (this._schema != null) {
/* 1744 */       p.setSchema(this._schema);
/*      */     }
/*      */     
/* 1747 */     JsonToken t = p.getCurrentToken();
/* 1748 */     if (t == null) {
/* 1749 */       t = p.nextToken();
/* 1750 */       if (t == null) {
/* 1751 */         return this._config.getNodeFactory().missingNode();
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 1756 */     boolean checkTrailing = this._config.isEnabled(DeserializationFeature.FAIL_ON_TRAILING_TOKENS);
/*      */     
/* 1758 */     if (t == JsonToken.VALUE_NULL) {
/* 1759 */       NullNode nullNode = this._config.getNodeFactory().nullNode();
/* 1760 */       if (!checkTrailing) {
/* 1761 */         return (JsonNode)nullNode;
/*      */       }
/* 1763 */       defaultDeserializationContext = createDeserializationContext(p);
/*      */     } else {
/* 1765 */       defaultDeserializationContext = createDeserializationContext(p);
/* 1766 */       JsonDeserializer<Object> deser = _findTreeDeserializer((DeserializationContext)defaultDeserializationContext);
/* 1767 */       if (this._unwrapRoot) {
/* 1768 */         resultNode = (JsonNode)_unwrapAndDeserialize(p, (DeserializationContext)defaultDeserializationContext, _jsonNodeType(), deser);
/*      */       } else {
/* 1770 */         resultNode = (JsonNode)deser.deserialize(p, (DeserializationContext)defaultDeserializationContext);
/*      */       } 
/*      */     } 
/* 1773 */     if (this._config.isEnabled(DeserializationFeature.FAIL_ON_TRAILING_TOKENS)) {
/* 1774 */       _verifyNoTrailingTokens(p, (DeserializationContext)defaultDeserializationContext, _jsonNodeType());
/*      */     }
/* 1776 */     return resultNode;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final JsonNode _bindAsTreeOrNull(JsonParser p) throws IOException {
/*      */     DefaultDeserializationContext defaultDeserializationContext;
/*      */     JsonNode resultNode;
/* 1785 */     this._config.initialize(p);
/* 1786 */     if (this._schema != null) {
/* 1787 */       p.setSchema(this._schema);
/*      */     }
/* 1789 */     JsonToken t = p.getCurrentToken();
/* 1790 */     if (t == null) {
/* 1791 */       t = p.nextToken();
/* 1792 */       if (t == null) {
/* 1793 */         return null;
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 1798 */     boolean checkTrailing = this._config.isEnabled(DeserializationFeature.FAIL_ON_TRAILING_TOKENS);
/* 1799 */     if (t == JsonToken.VALUE_NULL) {
/* 1800 */       NullNode nullNode = this._config.getNodeFactory().nullNode();
/* 1801 */       if (!checkTrailing) {
/* 1802 */         return (JsonNode)nullNode;
/*      */       }
/* 1804 */       defaultDeserializationContext = createDeserializationContext(p);
/*      */     } else {
/* 1806 */       defaultDeserializationContext = createDeserializationContext(p);
/* 1807 */       JsonDeserializer<Object> deser = _findTreeDeserializer((DeserializationContext)defaultDeserializationContext);
/* 1808 */       if (this._unwrapRoot) {
/* 1809 */         resultNode = (JsonNode)_unwrapAndDeserialize(p, (DeserializationContext)defaultDeserializationContext, _jsonNodeType(), deser);
/*      */       } else {
/* 1811 */         resultNode = (JsonNode)deser.deserialize(p, (DeserializationContext)defaultDeserializationContext);
/*      */       } 
/*      */     } 
/* 1814 */     if (checkTrailing) {
/* 1815 */       _verifyNoTrailingTokens(p, (DeserializationContext)defaultDeserializationContext, _jsonNodeType());
/*      */     }
/* 1817 */     return resultNode;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected <T> MappingIterator<T> _bindAndReadValues(JsonParser p) throws IOException {
/* 1825 */     DefaultDeserializationContext defaultDeserializationContext = createDeserializationContext(p);
/* 1826 */     _initForMultiRead((DeserializationContext)defaultDeserializationContext, p);
/* 1827 */     p.nextToken();
/* 1828 */     return _newIterator(p, (DeserializationContext)defaultDeserializationContext, _findRootDeserializer((DeserializationContext)defaultDeserializationContext), true);
/*      */   }
/*      */ 
/*      */   
/*      */   protected Object _unwrapAndDeserialize(JsonParser p, DeserializationContext ctxt, JavaType rootType, JsonDeserializer<Object> deser) throws IOException {
/*      */     Object result;
/* 1834 */     PropertyName expRootName = this._config.findRootName(rootType);
/*      */     
/* 1836 */     String expSimpleName = expRootName.getSimpleName();
/*      */     
/* 1838 */     if (p.getCurrentToken() != JsonToken.START_OBJECT) {
/* 1839 */       ctxt.reportWrongTokenException(rootType, JsonToken.START_OBJECT, "Current token not START_OBJECT (needed to unwrap root name '%s'), but %s", new Object[] { expSimpleName, p
/*      */             
/* 1841 */             .getCurrentToken() });
/*      */     }
/* 1843 */     if (p.nextToken() != JsonToken.FIELD_NAME) {
/* 1844 */       ctxt.reportWrongTokenException(rootType, JsonToken.FIELD_NAME, "Current token not FIELD_NAME (to contain expected root name '%s'), but %s", new Object[] { expSimpleName, p
/*      */             
/* 1846 */             .getCurrentToken() });
/*      */     }
/* 1848 */     String actualName = p.getCurrentName();
/* 1849 */     if (!expSimpleName.equals(actualName)) {
/* 1850 */       ctxt.reportPropertyInputMismatch(rootType, actualName, "Root name '%s' does not match expected ('%s') for type %s", new Object[] { actualName, expSimpleName, rootType });
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1855 */     p.nextToken();
/*      */     
/* 1857 */     if (this._valueToUpdate == null) {
/* 1858 */       result = deser.deserialize(p, ctxt);
/*      */     } else {
/* 1860 */       deser.deserialize(p, ctxt, this._valueToUpdate);
/* 1861 */       result = this._valueToUpdate;
/*      */     } 
/*      */     
/* 1864 */     if (p.nextToken() != JsonToken.END_OBJECT) {
/* 1865 */       ctxt.reportWrongTokenException(rootType, JsonToken.END_OBJECT, "Current token not END_OBJECT (to match wrapper object with root name '%s'), but %s", new Object[] { expSimpleName, p
/*      */             
/* 1867 */             .getCurrentToken() });
/*      */     }
/* 1869 */     if (this._config.isEnabled(DeserializationFeature.FAIL_ON_TRAILING_TOKENS)) {
/* 1870 */       _verifyNoTrailingTokens(p, ctxt, this._valueType);
/*      */     }
/* 1872 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected JsonParser _considerFilter(JsonParser p, boolean multiValue) {
/* 1881 */     return (this._filter == null || FilteringParserDelegate.class.isInstance(p)) ? p : (JsonParser)new FilteringParserDelegate(p, this._filter, false, multiValue);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final void _verifyNoTrailingTokens(JsonParser p, DeserializationContext ctxt, JavaType bindType) throws IOException {
/* 1892 */     JsonToken t = p.nextToken();
/* 1893 */     if (t != null) {
/* 1894 */       Class<?> bt = ClassUtil.rawClass(bindType);
/* 1895 */       if (bt == null && 
/* 1896 */         this._valueToUpdate != null) {
/* 1897 */         bt = this._valueToUpdate.getClass();
/*      */       }
/*      */       
/* 1900 */       ctxt.reportTrailingTokens(bt, p, t);
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
/*      */   protected Object _detectBindAndClose(byte[] src, int offset, int length) throws IOException {
/* 1913 */     DataFormatReaders.Match match = this._dataFormatReaders.findFormat(src, offset, length);
/* 1914 */     if (!match.hasMatch()) {
/* 1915 */       _reportUnkownFormat(this._dataFormatReaders, match);
/*      */     }
/* 1917 */     JsonParser p = match.createParserWithMatch();
/* 1918 */     return match.getReader()._bindAndClose(p);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Object _detectBindAndClose(DataFormatReaders.Match match, boolean forceClosing) throws IOException {
/* 1925 */     if (!match.hasMatch()) {
/* 1926 */       _reportUnkownFormat(this._dataFormatReaders, match);
/*      */     }
/* 1928 */     JsonParser p = match.createParserWithMatch();
/*      */ 
/*      */     
/* 1931 */     if (forceClosing) {
/* 1932 */       p.enable(JsonParser.Feature.AUTO_CLOSE_SOURCE);
/*      */     }
/*      */     
/* 1935 */     return match.getReader()._bindAndClose(p);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected <T> MappingIterator<T> _detectBindAndReadValues(DataFormatReaders.Match match, boolean forceClosing) throws IOException {
/* 1942 */     if (!match.hasMatch()) {
/* 1943 */       _reportUnkownFormat(this._dataFormatReaders, match);
/*      */     }
/* 1945 */     JsonParser p = match.createParserWithMatch();
/*      */ 
/*      */     
/* 1948 */     if (forceClosing) {
/* 1949 */       p.enable(JsonParser.Feature.AUTO_CLOSE_SOURCE);
/*      */     }
/*      */     
/* 1952 */     return match.getReader()._bindAndReadValues(p);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected JsonNode _detectBindAndCloseAsTree(InputStream in) throws IOException {
/* 1958 */     DataFormatReaders.Match match = this._dataFormatReaders.findFormat(in);
/* 1959 */     if (!match.hasMatch()) {
/* 1960 */       _reportUnkownFormat(this._dataFormatReaders, match);
/*      */     }
/* 1962 */     JsonParser p = match.createParserWithMatch();
/* 1963 */     p.enable(JsonParser.Feature.AUTO_CLOSE_SOURCE);
/* 1964 */     return match.getReader()._bindAndCloseAsTree(p);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void _reportUnkownFormat(DataFormatReaders detector, DataFormatReaders.Match match) throws JsonProcessingException {
/* 1975 */     throw new JsonParseException(null, "Cannot detect format from input, does not look like any of detectable formats " + detector
/* 1976 */         .toString());
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
/*      */   protected void _verifySchemaType(FormatSchema schema) {
/* 1990 */     if (schema != null && 
/* 1991 */       !this._parserFactory.canUseSchema(schema)) {
/* 1992 */       throw new IllegalArgumentException("Cannot use FormatSchema of type " + schema.getClass().getName() + " for format " + this._parserFactory
/* 1993 */           .getFormatName());
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected DefaultDeserializationContext createDeserializationContext(JsonParser p) {
/* 2004 */     return this._context.createInstance(this._config, p, this._injectableValues);
/*      */   }
/*      */   
/*      */   protected InputStream _inputStream(URL src) throws IOException {
/* 2008 */     return src.openStream();
/*      */   }
/*      */   
/*      */   protected InputStream _inputStream(File f) throws IOException {
/* 2012 */     return new FileInputStream(f);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void _reportUndetectableSource(Object src) throws JsonParseException {
/* 2018 */     throw new JsonParseException(null, "Cannot use source of type " + src
/* 2019 */         .getClass().getName() + " with format auto-detection: must be byte- not char-based");
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
/*      */   protected JsonDeserializer<Object> _findRootDeserializer(DeserializationContext ctxt) throws JsonMappingException {
/* 2034 */     if (this._rootDeserializer != null) {
/* 2035 */       return this._rootDeserializer;
/*      */     }
/*      */ 
/*      */     
/* 2039 */     JavaType t = this._valueType;
/* 2040 */     if (t == null) {
/* 2041 */       ctxt.reportBadDefinition((JavaType)null, "No value type configured for ObjectReader");
/*      */     }
/*      */ 
/*      */     
/* 2045 */     JsonDeserializer<Object> deser = this._rootDeserializers.get(t);
/* 2046 */     if (deser != null) {
/* 2047 */       return deser;
/*      */     }
/*      */     
/* 2050 */     deser = ctxt.findRootValueDeserializer(t);
/* 2051 */     if (deser == null) {
/* 2052 */       ctxt.reportBadDefinition(t, "Cannot find a deserializer for type " + t);
/*      */     }
/* 2054 */     this._rootDeserializers.put(t, deser);
/* 2055 */     return deser;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected JsonDeserializer<Object> _findTreeDeserializer(DeserializationContext ctxt) throws JsonMappingException {
/* 2064 */     JavaType nodeType = _jsonNodeType();
/* 2065 */     JsonDeserializer<Object> deser = this._rootDeserializers.get(nodeType);
/* 2066 */     if (deser == null) {
/*      */       
/* 2068 */       deser = ctxt.findRootValueDeserializer(nodeType);
/* 2069 */       if (deser == null) {
/* 2070 */         ctxt.reportBadDefinition(nodeType, "Cannot find a deserializer for type " + nodeType);
/*      */       }
/*      */       
/* 2073 */       this._rootDeserializers.put(nodeType, deser);
/*      */     } 
/* 2075 */     return deser;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected JsonDeserializer<Object> _prefetchRootDeserializer(JavaType valueType) {
/* 2085 */     if (valueType == null || !this._config.isEnabled(DeserializationFeature.EAGER_DESERIALIZER_FETCH)) {
/* 2086 */       return null;
/*      */     }
/*      */     
/* 2089 */     JsonDeserializer<Object> deser = this._rootDeserializers.get(valueType);
/* 2090 */     if (deser == null) {
/*      */       
/*      */       try {
/* 2093 */         DefaultDeserializationContext defaultDeserializationContext = createDeserializationContext(null);
/* 2094 */         deser = defaultDeserializationContext.findRootValueDeserializer(valueType);
/* 2095 */         if (deser != null) {
/* 2096 */           this._rootDeserializers.put(valueType, deser);
/*      */         }
/* 2098 */         return deser;
/* 2099 */       } catch (JsonProcessingException jsonProcessingException) {}
/*      */     }
/*      */ 
/*      */     
/* 2103 */     return deser;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final JavaType _jsonNodeType() {
/* 2110 */     JavaType t = this._jsonNodeType;
/* 2111 */     if (t == null) {
/* 2112 */       t = getTypeFactory().constructType(JsonNode.class);
/* 2113 */       this._jsonNodeType = t;
/*      */     } 
/* 2115 */     return t;
/*      */   }
/*      */   
/*      */   protected final void _assertNotNull(String paramName, Object src) {
/* 2119 */     if (src == null)
/* 2120 */       throw new IllegalArgumentException(String.format("argument \"%s\" is null", new Object[] { paramName })); 
/*      */   }
/*      */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\ObjectReader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */