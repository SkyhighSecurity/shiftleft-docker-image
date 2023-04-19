/*      */ package com.fasterxml.jackson.databind;
/*      */ 
/*      */ import com.fasterxml.jackson.annotation.JsonFormat;
/*      */ import com.fasterxml.jackson.annotation.ObjectIdGenerator;
/*      */ import com.fasterxml.jackson.annotation.ObjectIdResolver;
/*      */ import com.fasterxml.jackson.core.Base64Variant;
/*      */ import com.fasterxml.jackson.core.JsonParser;
/*      */ import com.fasterxml.jackson.core.JsonToken;
/*      */ import com.fasterxml.jackson.databind.cfg.ContextAttributes;
/*      */ import com.fasterxml.jackson.databind.cfg.MapperConfig;
/*      */ import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
/*      */ import com.fasterxml.jackson.databind.deser.ContextualKeyDeserializer;
/*      */ import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;
/*      */ import com.fasterxml.jackson.databind.deser.DeserializerCache;
/*      */ import com.fasterxml.jackson.databind.deser.DeserializerFactory;
/*      */ import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
/*      */ import com.fasterxml.jackson.databind.deser.ValueInstantiator;
/*      */ import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader;
/*      */ import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
/*      */ import com.fasterxml.jackson.databind.deser.impl.TypeWrappedDeserializer;
/*      */ import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
/*      */ import com.fasterxml.jackson.databind.exc.InvalidFormatException;
/*      */ import com.fasterxml.jackson.databind.exc.InvalidTypeIdException;
/*      */ import com.fasterxml.jackson.databind.exc.MismatchedInputException;
/*      */ import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
/*      */ import com.fasterxml.jackson.databind.exc.ValueInstantiationException;
/*      */ import com.fasterxml.jackson.databind.introspect.Annotated;
/*      */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*      */ import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
/*      */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*      */ import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
/*      */ import com.fasterxml.jackson.databind.node.JsonNodeFactory;
/*      */ import com.fasterxml.jackson.databind.type.TypeFactory;
/*      */ import com.fasterxml.jackson.databind.util.ArrayBuilders;
/*      */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*      */ import com.fasterxml.jackson.databind.util.LinkedNode;
/*      */ import com.fasterxml.jackson.databind.util.Named;
/*      */ import com.fasterxml.jackson.databind.util.ObjectBuffer;
/*      */ import java.io.IOException;
/*      */ import java.io.Serializable;
/*      */ import java.text.DateFormat;
/*      */ import java.text.ParseException;
/*      */ import java.util.Calendar;
/*      */ import java.util.Collection;
/*      */ import java.util.Date;
/*      */ import java.util.Locale;
/*      */ import java.util.Objects;
/*      */ import java.util.TimeZone;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class DeserializationContext
/*      */   extends DatabindContext
/*      */   implements Serializable
/*      */ {
/*      */   private static final long serialVersionUID = 1L;
/*      */   protected final DeserializerCache _cache;
/*      */   protected final DeserializerFactory _factory;
/*      */   protected final DeserializationConfig _config;
/*      */   protected final int _featureFlags;
/*      */   protected final Class<?> _view;
/*      */   protected transient JsonParser _parser;
/*      */   protected final InjectableValues _injectableValues;
/*      */   protected transient ArrayBuilders _arrayBuilders;
/*      */   protected transient ObjectBuffer _objectBuffer;
/*      */   protected transient DateFormat _dateFormat;
/*      */   protected transient ContextAttributes _attributes;
/*      */   protected LinkedNode<JavaType> _currentType;
/*      */   
/*      */   protected DeserializationContext(DeserializerFactory df) {
/*  152 */     this(df, (DeserializerCache)null);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected DeserializationContext(DeserializerFactory df, DeserializerCache cache) {
/*  158 */     this._factory = Objects.<DeserializerFactory>requireNonNull(df, "Cannot pass null DeserializerFactory");
/*  159 */     if (cache == null) {
/*  160 */       cache = new DeserializerCache();
/*      */     }
/*  162 */     this._cache = cache;
/*  163 */     this._featureFlags = 0;
/*  164 */     this._config = null;
/*  165 */     this._injectableValues = null;
/*  166 */     this._view = null;
/*  167 */     this._attributes = null;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected DeserializationContext(DeserializationContext src, DeserializerFactory factory) {
/*  173 */     this._cache = src._cache;
/*  174 */     this._factory = factory;
/*      */     
/*  176 */     this._config = src._config;
/*  177 */     this._featureFlags = src._featureFlags;
/*  178 */     this._view = src._view;
/*  179 */     this._parser = src._parser;
/*  180 */     this._injectableValues = src._injectableValues;
/*  181 */     this._attributes = src._attributes;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected DeserializationContext(DeserializationContext src, DeserializationConfig config, JsonParser p, InjectableValues injectableValues) {
/*  191 */     this._cache = src._cache;
/*  192 */     this._factory = src._factory;
/*      */     
/*  194 */     this._config = config;
/*  195 */     this._featureFlags = config.getDeserializationFeatures();
/*  196 */     this._view = config.getActiveView();
/*  197 */     this._parser = p;
/*  198 */     this._injectableValues = injectableValues;
/*  199 */     this._attributes = config.getAttributes();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected DeserializationContext(DeserializationContext src) {
/*  206 */     this._cache = new DeserializerCache();
/*  207 */     this._factory = src._factory;
/*      */     
/*  209 */     this._config = src._config;
/*  210 */     this._featureFlags = src._featureFlags;
/*  211 */     this._view = src._view;
/*  212 */     this._injectableValues = null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public DeserializationConfig getConfig() {
/*  222 */     return this._config;
/*      */   }
/*      */   public final Class<?> getActiveView() {
/*  225 */     return this._view;
/*      */   }
/*      */   
/*      */   public final boolean canOverrideAccessModifiers() {
/*  229 */     return this._config.canOverrideAccessModifiers();
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isEnabled(MapperFeature feature) {
/*  234 */     return this._config.isEnabled(feature);
/*      */   }
/*      */ 
/*      */   
/*      */   public final JsonFormat.Value getDefaultPropertyFormat(Class<?> baseType) {
/*  239 */     return this._config.getDefaultPropertyFormat(baseType);
/*      */   }
/*      */ 
/*      */   
/*      */   public final AnnotationIntrospector getAnnotationIntrospector() {
/*  244 */     return this._config.getAnnotationIntrospector();
/*      */   }
/*      */ 
/*      */   
/*      */   public final TypeFactory getTypeFactory() {
/*  249 */     return this._config.getTypeFactory();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Locale getLocale() {
/*  260 */     return this._config.getLocale();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public TimeZone getTimeZone() {
/*  271 */     return this._config.getTimeZone();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object getAttribute(Object key) {
/*  282 */     return this._attributes.getAttribute(key);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public DeserializationContext setAttribute(Object key, Object value) {
/*  288 */     this._attributes = this._attributes.withPerCallAttribute(key, value);
/*  289 */     return this;
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
/*      */   public JavaType getContextualType() {
/*  306 */     return (this._currentType == null) ? null : (JavaType)this._currentType.value();
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
/*      */   public DeserializerFactory getFactory() {
/*  319 */     return this._factory;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isEnabled(DeserializationFeature feat) {
/*  330 */     return ((this._featureFlags & feat.getMask()) != 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getDeserializationFeatures() {
/*  340 */     return this._featureFlags;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean hasDeserializationFeatures(int featureMask) {
/*  350 */     return ((this._featureFlags & featureMask) == featureMask);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean hasSomeOfFeatures(int featureMask) {
/*  360 */     return ((this._featureFlags & featureMask) != 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final JsonParser getParser() {
/*  371 */     return this._parser;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final Object findInjectableValue(Object valueId, BeanProperty forProperty, Object beanInstance) throws JsonMappingException {
/*  377 */     if (this._injectableValues == null) {
/*  378 */       reportBadDefinition(ClassUtil.classOf(valueId), String.format("No 'injectableValues' configured, cannot inject value with id [%s]", new Object[] { valueId }));
/*      */     }
/*      */     
/*  381 */     return this._injectableValues.findInjectableValue(valueId, this, forProperty, beanInstance);
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
/*      */   public final Base64Variant getBase64Variant() {
/*  393 */     return this._config.getBase64Variant();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final JsonNodeFactory getNodeFactory() {
/*  403 */     return this._config.getNodeFactory();
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
/*      */   public boolean hasValueDeserializerFor(JavaType type, AtomicReference<Throwable> cause) {
/*      */     try {
/*  421 */       return this._cache.hasValueDeserializerFor(this, this._factory, type);
/*  422 */     } catch (JsonMappingException e) {
/*  423 */       if (cause != null) {
/*  424 */         cause.set(e);
/*      */       }
/*  426 */     } catch (RuntimeException e) {
/*  427 */       if (cause == null) {
/*  428 */         throw e;
/*      */       }
/*  430 */       cause.set(e);
/*      */     } 
/*  432 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final JsonDeserializer<Object> findContextualValueDeserializer(JavaType type, BeanProperty prop) throws JsonMappingException {
/*  443 */     JsonDeserializer<Object> deser = this._cache.findValueDeserializer(this, this._factory, type);
/*  444 */     if (deser != null) {
/*  445 */       deser = (JsonDeserializer)handleSecondaryContextualization(deser, prop, type);
/*      */     }
/*  447 */     return deser;
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
/*      */   public final JsonDeserializer<Object> findNonContextualValueDeserializer(JavaType type) throws JsonMappingException {
/*  466 */     return this._cache.findValueDeserializer(this, this._factory, type);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final JsonDeserializer<Object> findRootValueDeserializer(JavaType type) throws JsonMappingException {
/*  476 */     JsonDeserializer<Object> deser = this._cache.findValueDeserializer(this, this._factory, type);
/*      */     
/*  478 */     if (deser == null) {
/*  479 */       return null;
/*      */     }
/*  481 */     deser = (JsonDeserializer)handleSecondaryContextualization(deser, (BeanProperty)null, type);
/*  482 */     TypeDeserializer typeDeser = this._factory.findTypeDeserializer(this._config, type);
/*  483 */     if (typeDeser != null) {
/*      */       
/*  485 */       typeDeser = typeDeser.forProperty(null);
/*  486 */       return (JsonDeserializer<Object>)new TypeWrappedDeserializer(typeDeser, deser);
/*      */     } 
/*  488 */     return deser;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final KeyDeserializer findKeyDeserializer(JavaType keyType, BeanProperty prop) throws JsonMappingException {
/*  499 */     KeyDeserializer kd = this._cache.findKeyDeserializer(this, this._factory, keyType);
/*      */ 
/*      */     
/*  502 */     if (kd instanceof ContextualKeyDeserializer) {
/*  503 */       kd = ((ContextualKeyDeserializer)kd).createContextual(this, prop);
/*      */     }
/*  505 */     return kd;
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
/*      */   public final JavaType constructType(Class<?> cls) {
/*  542 */     return (cls == null) ? null : this._config.constructType(cls);
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
/*      */   public Class<?> findClass(String className) throws ClassNotFoundException {
/*  556 */     return getTypeFactory().findClass(className);
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
/*      */   public final ObjectBuffer leaseObjectBuffer() {
/*  573 */     ObjectBuffer buf = this._objectBuffer;
/*  574 */     if (buf == null) {
/*  575 */       buf = new ObjectBuffer();
/*      */     } else {
/*  577 */       this._objectBuffer = null;
/*      */     } 
/*  579 */     return buf;
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
/*      */   public final void returnObjectBuffer(ObjectBuffer buf) {
/*  593 */     if (this._objectBuffer == null || buf
/*  594 */       .initialCapacity() >= this._objectBuffer.initialCapacity()) {
/*  595 */       this._objectBuffer = buf;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final ArrayBuilders getArrayBuilders() {
/*  605 */     if (this._arrayBuilders == null) {
/*  606 */       this._arrayBuilders = new ArrayBuilders();
/*      */     }
/*  608 */     return this._arrayBuilders;
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
/*      */   public JsonDeserializer<?> handlePrimaryContextualization(JsonDeserializer<?> deser, BeanProperty prop, JavaType type) throws JsonMappingException {
/*  647 */     if (deser instanceof ContextualDeserializer) {
/*  648 */       this._currentType = new LinkedNode(type, this._currentType);
/*      */       try {
/*  650 */         deser = ((ContextualDeserializer)deser).createContextual(this, prop);
/*      */       } finally {
/*  652 */         this._currentType = this._currentType.next();
/*      */       } 
/*      */     } 
/*  655 */     return deser;
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
/*      */   public JsonDeserializer<?> handleSecondaryContextualization(JsonDeserializer<?> deser, BeanProperty prop, JavaType type) throws JsonMappingException {
/*  678 */     if (deser instanceof ContextualDeserializer) {
/*  679 */       this._currentType = new LinkedNode(type, this._currentType);
/*      */       try {
/*  681 */         deser = ((ContextualDeserializer)deser).createContextual(this, prop);
/*      */       } finally {
/*  683 */         this._currentType = this._currentType.next();
/*      */       } 
/*      */     } 
/*  686 */     return deser;
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
/*      */   public Date parseDate(String dateStr) throws IllegalArgumentException {
/*      */     try {
/*  708 */       DateFormat df = getDateFormat();
/*  709 */       return df.parse(dateStr);
/*  710 */     } catch (ParseException e) {
/*  711 */       throw new IllegalArgumentException(String.format("Failed to parse Date value '%s': %s", new Object[] { dateStr, 
/*      */               
/*  713 */               ClassUtil.exceptionMessage(e) }));
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Calendar constructCalendar(Date d) {
/*  723 */     Calendar c = Calendar.getInstance(getTimeZone());
/*  724 */     c.setTime(d);
/*  725 */     return c;
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
/*      */   public <T> T readValue(JsonParser p, Class<T> type) throws IOException {
/*  746 */     return readValue(p, getTypeFactory().constructType(type));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> T readValue(JsonParser p, JavaType type) throws IOException {
/*  754 */     JsonDeserializer<Object> deser = findRootValueDeserializer(type);
/*  755 */     if (deser == null) {
/*  756 */       reportBadDefinition(type, "Could not find JsonDeserializer for type " + 
/*  757 */           ClassUtil.getTypeDescription(type));
/*      */     }
/*  759 */     return (T)deser.deserialize(p, this);
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
/*      */   public <T> T readPropertyValue(JsonParser p, BeanProperty prop, Class<T> type) throws IOException {
/*  771 */     return readPropertyValue(p, prop, getTypeFactory().constructType(type));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> T readPropertyValue(JsonParser p, BeanProperty prop, JavaType type) throws IOException {
/*  779 */     JsonDeserializer<Object> deser = findContextualValueDeserializer(type, prop);
/*  780 */     if (deser == null)
/*  781 */       return reportBadDefinition(type, String.format("Could not find JsonDeserializer for type %s (via property %s)", new Object[] {
/*      */               
/*  783 */               ClassUtil.getTypeDescription(type), ClassUtil.nameOf(prop)
/*      */             })); 
/*  785 */     return (T)deser.deserialize(p, this);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JsonNode readTree(JsonParser p) throws IOException {
/*  792 */     JsonToken t = p.currentToken();
/*  793 */     if (t == null) {
/*  794 */       t = p.nextToken();
/*  795 */       if (t == null) {
/*  796 */         return getNodeFactory().missingNode();
/*      */       }
/*      */     } 
/*  799 */     if (t == JsonToken.VALUE_NULL) {
/*  800 */       return (JsonNode)getNodeFactory().nullNode();
/*      */     }
/*  802 */     return (JsonNode)findRootValueDeserializer(this._config.constructType(JsonNode.class))
/*  803 */       .deserialize(p, this);
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
/*      */   public boolean handleUnknownProperty(JsonParser p, JsonDeserializer<?> deser, Object instanceOrClass, String propName) throws IOException {
/*  825 */     LinkedNode<DeserializationProblemHandler> h = this._config.getProblemHandlers();
/*  826 */     while (h != null) {
/*      */       
/*  828 */       if (((DeserializationProblemHandler)h.value()).handleUnknownProperty(this, p, deser, instanceOrClass, propName)) {
/*  829 */         return true;
/*      */       }
/*  831 */       h = h.next();
/*      */     } 
/*      */     
/*  834 */     if (!isEnabled(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)) {
/*  835 */       p.skipChildren();
/*  836 */       return true;
/*      */     } 
/*      */     
/*  839 */     Collection<Object> propIds = (deser == null) ? null : deser.getKnownPropertyNames();
/*  840 */     throw UnrecognizedPropertyException.from(this._parser, instanceOrClass, propName, propIds);
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
/*      */   public Object handleWeirdKey(Class<?> keyClass, String keyValue, String msg, Object... msgArgs) throws IOException {
/*  868 */     msg = _format(msg, msgArgs);
/*  869 */     LinkedNode<DeserializationProblemHandler> h = this._config.getProblemHandlers();
/*  870 */     while (h != null) {
/*      */       
/*  872 */       Object key = ((DeserializationProblemHandler)h.value()).handleWeirdKey(this, keyClass, keyValue, msg);
/*  873 */       if (key != DeserializationProblemHandler.NOT_HANDLED) {
/*      */         
/*  875 */         if (key == null || keyClass.isInstance(key)) {
/*  876 */           return key;
/*      */         }
/*  878 */         throw weirdStringException(keyValue, keyClass, String.format("DeserializationProblemHandler.handleWeirdStringValue() for type %s returned value of type %s", new Object[] {
/*      */                 
/*  880 */                 ClassUtil.getClassDescription(keyClass), 
/*  881 */                 ClassUtil.getClassDescription(key)
/*      */               }));
/*      */       } 
/*  884 */       h = h.next();
/*      */     } 
/*  886 */     throw weirdKeyException(keyClass, keyValue, msg);
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
/*      */   public Object handleWeirdStringValue(Class<?> targetClass, String value, String msg, Object... msgArgs) throws IOException {
/*  914 */     msg = _format(msg, msgArgs);
/*  915 */     LinkedNode<DeserializationProblemHandler> h = this._config.getProblemHandlers();
/*  916 */     while (h != null) {
/*      */       
/*  918 */       Object instance = ((DeserializationProblemHandler)h.value()).handleWeirdStringValue(this, targetClass, value, msg);
/*  919 */       if (instance != DeserializationProblemHandler.NOT_HANDLED) {
/*      */         
/*  921 */         if (_isCompatible(targetClass, instance)) {
/*  922 */           return instance;
/*      */         }
/*  924 */         throw weirdStringException(value, targetClass, String.format("DeserializationProblemHandler.handleWeirdStringValue() for type %s returned value of type %s", new Object[] {
/*      */                 
/*  926 */                 ClassUtil.getClassDescription(targetClass), 
/*  927 */                 ClassUtil.getClassDescription(instance)
/*      */               }));
/*      */       } 
/*  930 */       h = h.next();
/*      */     } 
/*  932 */     throw weirdStringException(value, targetClass, msg);
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
/*      */   public Object handleWeirdNumberValue(Class<?> targetClass, Number value, String msg, Object... msgArgs) throws IOException {
/*  959 */     msg = _format(msg, msgArgs);
/*  960 */     LinkedNode<DeserializationProblemHandler> h = this._config.getProblemHandlers();
/*  961 */     while (h != null) {
/*      */       
/*  963 */       Object key = ((DeserializationProblemHandler)h.value()).handleWeirdNumberValue(this, targetClass, value, msg);
/*  964 */       if (key != DeserializationProblemHandler.NOT_HANDLED) {
/*      */         
/*  966 */         if (_isCompatible(targetClass, key)) {
/*  967 */           return key;
/*      */         }
/*  969 */         throw weirdNumberException(value, targetClass, _format("DeserializationProblemHandler.handleWeirdNumberValue() for type %s returned value of type %s", new Object[] {
/*      */                 
/*  971 */                 ClassUtil.getClassDescription(targetClass), 
/*  972 */                 ClassUtil.getClassDescription(key)
/*      */               }));
/*      */       } 
/*  975 */       h = h.next();
/*      */     } 
/*  977 */     throw weirdNumberException(value, targetClass, msg);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object handleWeirdNativeValue(JavaType targetType, Object badValue, JsonParser p) throws IOException {
/*  984 */     LinkedNode<DeserializationProblemHandler> h = this._config.getProblemHandlers();
/*  985 */     Class<?> raw = targetType.getRawClass();
/*  986 */     for (; h != null; h = h.next()) {
/*      */       
/*  988 */       Object goodValue = ((DeserializationProblemHandler)h.value()).handleWeirdNativeValue(this, targetType, badValue, p);
/*  989 */       if (goodValue != DeserializationProblemHandler.NOT_HANDLED) {
/*      */         
/*  991 */         if (goodValue == null || raw.isInstance(goodValue)) {
/*  992 */           return goodValue;
/*      */         }
/*  994 */         throw JsonMappingException.from(p, _format("DeserializationProblemHandler.handleWeirdNativeValue() for type %s returned value of type %s", new Object[] {
/*      */                 
/*  996 */                 ClassUtil.getClassDescription(targetType), 
/*  997 */                 ClassUtil.getClassDescription(goodValue)
/*      */               }));
/*      */       } 
/*      */     } 
/* 1001 */     throw weirdNativeValueException(badValue, raw);
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
/*      */   public Object handleMissingInstantiator(Class<?> instClass, ValueInstantiator valueInst, JsonParser p, String msg, Object... msgArgs) throws IOException {
/* 1026 */     if (p == null) {
/* 1027 */       p = getParser();
/*      */     }
/* 1029 */     msg = _format(msg, msgArgs);
/* 1030 */     LinkedNode<DeserializationProblemHandler> h = this._config.getProblemHandlers();
/* 1031 */     while (h != null) {
/*      */       
/* 1033 */       Object instance = ((DeserializationProblemHandler)h.value()).handleMissingInstantiator(this, instClass, valueInst, p, msg);
/*      */       
/* 1035 */       if (instance != DeserializationProblemHandler.NOT_HANDLED) {
/*      */         
/* 1037 */         if (_isCompatible(instClass, instance)) {
/* 1038 */           return instance;
/*      */         }
/* 1040 */         reportBadDefinition(constructType(instClass), String.format("DeserializationProblemHandler.handleMissingInstantiator() for type %s returned value of type %s", new Object[] {
/*      */                 
/* 1042 */                 ClassUtil.getClassDescription(instClass), 
/* 1043 */                 ClassUtil.getClassDescription(instance)
/*      */               }));
/*      */       } 
/* 1046 */       h = h.next();
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1052 */     if (valueInst != null && !valueInst.canInstantiate()) {
/* 1053 */       msg = String.format("Cannot construct instance of %s (no Creators, like default construct, exist): %s", new Object[] {
/* 1054 */             ClassUtil.nameOf(instClass), msg });
/* 1055 */       return reportBadDefinition(constructType(instClass), msg);
/*      */     } 
/* 1057 */     msg = String.format("Cannot construct instance of %s (although at least one Creator exists): %s", new Object[] {
/* 1058 */           ClassUtil.nameOf(instClass), msg });
/* 1059 */     return reportInputMismatch(instClass, msg, new Object[0]);
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
/*      */   public Object handleInstantiationProblem(Class<?> instClass, Object argument, Throwable t) throws IOException {
/* 1083 */     LinkedNode<DeserializationProblemHandler> h = this._config.getProblemHandlers();
/* 1084 */     while (h != null) {
/*      */       
/* 1086 */       Object instance = ((DeserializationProblemHandler)h.value()).handleInstantiationProblem(this, instClass, argument, t);
/* 1087 */       if (instance != DeserializationProblemHandler.NOT_HANDLED) {
/*      */         
/* 1089 */         if (_isCompatible(instClass, instance)) {
/* 1090 */           return instance;
/*      */         }
/* 1092 */         reportBadDefinition(constructType(instClass), String.format("DeserializationProblemHandler.handleInstantiationProblem() for type %s returned value of type %s", new Object[] {
/*      */                 
/* 1094 */                 ClassUtil.getClassDescription(instClass), 
/* 1095 */                 ClassUtil.classNameOf(instance)
/*      */               }));
/*      */       } 
/* 1098 */       h = h.next();
/*      */     } 
/*      */     
/* 1101 */     ClassUtil.throwIfIOE(t);
/*      */     
/* 1103 */     if (!isEnabled(DeserializationFeature.WRAP_EXCEPTIONS)) {
/* 1104 */       ClassUtil.throwIfRTE(t);
/*      */     }
/* 1106 */     throw instantiationException(instClass, t);
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
/*      */   public Object handleUnexpectedToken(Class<?> instClass, JsonParser p) throws IOException {
/* 1126 */     return handleUnexpectedToken(constructType(instClass), p.getCurrentToken(), p, (String)null, new Object[0]);
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
/*      */   public Object handleUnexpectedToken(Class<?> instClass, JsonToken t, JsonParser p, String msg, Object... msgArgs) throws IOException {
/* 1148 */     return handleUnexpectedToken(constructType(instClass), t, p, msg, msgArgs);
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
/*      */   public Object handleUnexpectedToken(JavaType targetType, JsonParser p) throws IOException {
/* 1168 */     return handleUnexpectedToken(targetType, p.getCurrentToken(), p, (String)null, new Object[0]);
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
/*      */   public Object handleUnexpectedToken(JavaType targetType, JsonToken t, JsonParser p, String msg, Object... msgArgs) throws IOException {
/* 1190 */     msg = _format(msg, msgArgs);
/* 1191 */     LinkedNode<DeserializationProblemHandler> h = this._config.getProblemHandlers();
/* 1192 */     while (h != null) {
/* 1193 */       Object instance = ((DeserializationProblemHandler)h.value()).handleUnexpectedToken(this, targetType, t, p, msg);
/*      */       
/* 1195 */       if (instance != DeserializationProblemHandler.NOT_HANDLED) {
/* 1196 */         if (_isCompatible(targetType.getRawClass(), instance)) {
/* 1197 */           return instance;
/*      */         }
/* 1199 */         reportBadDefinition(targetType, String.format("DeserializationProblemHandler.handleUnexpectedToken() for type %s returned value of type %s", new Object[] {
/*      */                 
/* 1201 */                 ClassUtil.getClassDescription(targetType), 
/* 1202 */                 ClassUtil.classNameOf(instance)
/*      */               }));
/*      */       } 
/* 1205 */       h = h.next();
/*      */     } 
/* 1207 */     if (msg == null)
/* 1208 */       if (t == null) {
/* 1209 */         msg = String.format("Unexpected end-of-input when binding data into %s", new Object[] {
/* 1210 */               ClassUtil.getTypeDescription(targetType) });
/*      */       } else {
/* 1212 */         msg = String.format("Cannot deserialize instance of %s out of %s token", new Object[] {
/* 1213 */               ClassUtil.getTypeDescription(targetType), t
/*      */             });
/*      */       }  
/* 1216 */     reportInputMismatch(targetType, msg, new Object[0]);
/* 1217 */     return null;
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
/*      */   public JavaType handleUnknownTypeId(JavaType baseType, String id, TypeIdResolver idResolver, String extraDesc) throws IOException {
/* 1243 */     LinkedNode<DeserializationProblemHandler> h = this._config.getProblemHandlers();
/* 1244 */     while (h != null) {
/*      */       
/* 1246 */       JavaType type = ((DeserializationProblemHandler)h.value()).handleUnknownTypeId(this, baseType, id, idResolver, extraDesc);
/* 1247 */       if (type != null) {
/* 1248 */         if (type.hasRawClass(Void.class)) {
/* 1249 */           return null;
/*      */         }
/*      */         
/* 1252 */         if (type.isTypeOrSubTypeOf(baseType.getRawClass())) {
/* 1253 */           return type;
/*      */         }
/* 1255 */         throw invalidTypeIdException(baseType, id, "problem handler tried to resolve into non-subtype: " + 
/*      */             
/* 1257 */             ClassUtil.getTypeDescription(type));
/*      */       } 
/* 1259 */       h = h.next();
/*      */     } 
/*      */     
/* 1262 */     if (!isEnabled(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE)) {
/* 1263 */       return null;
/*      */     }
/* 1265 */     throw invalidTypeIdException(baseType, id, extraDesc);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JavaType handleMissingTypeId(JavaType baseType, TypeIdResolver idResolver, String extraDesc) throws IOException {
/* 1274 */     LinkedNode<DeserializationProblemHandler> h = this._config.getProblemHandlers();
/* 1275 */     while (h != null) {
/*      */       
/* 1277 */       JavaType type = ((DeserializationProblemHandler)h.value()).handleMissingTypeId(this, baseType, idResolver, extraDesc);
/* 1278 */       if (type != null) {
/* 1279 */         if (type.hasRawClass(Void.class)) {
/* 1280 */           return null;
/*      */         }
/*      */         
/* 1283 */         if (type.isTypeOrSubTypeOf(baseType.getRawClass())) {
/* 1284 */           return type;
/*      */         }
/* 1286 */         throw invalidTypeIdException(baseType, null, "problem handler tried to resolve into non-subtype: " + 
/*      */             
/* 1288 */             ClassUtil.getTypeDescription(type));
/*      */       } 
/* 1290 */       h = h.next();
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1297 */     throw missingTypeIdException(baseType, extraDesc);
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
/*      */   public void handleBadMerge(JsonDeserializer<?> deser) throws JsonMappingException {
/* 1313 */     if (!isEnabled(MapperFeature.IGNORE_MERGE_FOR_UNMERGEABLE)) {
/* 1314 */       JavaType type = constructType(deser.handledType());
/* 1315 */       String msg = String.format("Invalid configuration: values of type %s cannot be merged", new Object[] {
/* 1316 */             ClassUtil.getTypeDescription(type) });
/* 1317 */       throw InvalidDefinitionException.from(getParser(), msg, type);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean _isCompatible(Class<?> target, Object value) {
/* 1326 */     if (value == null || target.isInstance(value)) {
/* 1327 */       return true;
/*      */     }
/*      */     
/* 1330 */     return (target.isPrimitive() && 
/* 1331 */       ClassUtil.wrapperType(target).isInstance(value));
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
/*      */   public void reportWrongTokenException(JsonDeserializer<?> deser, JsonToken expToken, String msg, Object... msgArgs) throws JsonMappingException {
/* 1355 */     msg = _format(msg, msgArgs);
/* 1356 */     throw wrongTokenException(getParser(), deser.handledType(), expToken, msg);
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
/*      */   public void reportWrongTokenException(JavaType targetType, JsonToken expToken, String msg, Object... msgArgs) throws JsonMappingException {
/* 1373 */     msg = _format(msg, msgArgs);
/* 1374 */     throw wrongTokenException(getParser(), targetType, expToken, msg);
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
/*      */   public void reportWrongTokenException(Class<?> targetType, JsonToken expToken, String msg, Object... msgArgs) throws JsonMappingException {
/* 1391 */     msg = _format(msg, msgArgs);
/* 1392 */     throw wrongTokenException(getParser(), targetType, expToken, msg);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> T reportUnresolvedObjectId(ObjectIdReader oidReader, Object bean) throws JsonMappingException {
/* 1401 */     String msg = String.format("No Object Id found for an instance of %s, to assign to property '%s'", new Object[] {
/* 1402 */           ClassUtil.classNameOf(bean), oidReader.propertyName });
/* 1403 */     return reportInputMismatch((BeanProperty)oidReader.idProperty, msg, new Object[0]);
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
/*      */   public <T> T reportInputMismatch(JsonDeserializer<?> src, String msg, Object... msgArgs) throws JsonMappingException {
/* 1415 */     msg = _format(msg, msgArgs);
/* 1416 */     throw MismatchedInputException.from(getParser(), src.handledType(), msg);
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
/*      */   public <T> T reportInputMismatch(Class<?> targetType, String msg, Object... msgArgs) throws JsonMappingException {
/* 1428 */     msg = _format(msg, msgArgs);
/* 1429 */     throw MismatchedInputException.from(getParser(), targetType, msg);
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
/*      */   public <T> T reportInputMismatch(JavaType targetType, String msg, Object... msgArgs) throws JsonMappingException {
/* 1441 */     msg = _format(msg, msgArgs);
/* 1442 */     throw MismatchedInputException.from(getParser(), targetType, msg);
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
/*      */   public <T> T reportInputMismatch(BeanProperty prop, String msg, Object... msgArgs) throws JsonMappingException {
/* 1454 */     msg = _format(msg, msgArgs);
/* 1455 */     JavaType type = (prop == null) ? null : prop.getType();
/* 1456 */     MismatchedInputException e = MismatchedInputException.from(getParser(), type, msg);
/*      */     
/* 1458 */     if (prop != null) {
/* 1459 */       AnnotatedMember member = prop.getMember();
/* 1460 */       if (member != null) {
/* 1461 */         e.prependPath(member.getDeclaringClass(), prop.getName());
/*      */       }
/*      */     } 
/* 1464 */     throw e;
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
/*      */   public <T> T reportPropertyInputMismatch(Class<?> targetType, String propertyName, String msg, Object... msgArgs) throws JsonMappingException {
/* 1476 */     msg = _format(msg, msgArgs);
/* 1477 */     MismatchedInputException e = MismatchedInputException.from(getParser(), targetType, msg);
/* 1478 */     if (propertyName != null) {
/* 1479 */       e.prependPath(targetType, propertyName);
/*      */     }
/* 1481 */     throw e;
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
/*      */   public <T> T reportPropertyInputMismatch(JavaType targetType, String propertyName, String msg, Object... msgArgs) throws JsonMappingException {
/* 1493 */     return reportPropertyInputMismatch(targetType.getRawClass(), propertyName, msg, msgArgs);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> T reportTrailingTokens(Class<?> targetType, JsonParser p, JsonToken trailingToken) throws JsonMappingException {
/* 1499 */     throw MismatchedInputException.from(p, targetType, String.format("Trailing token (of type %s) found after value (bound as %s): not allowed as per `DeserializationFeature.FAIL_ON_TRAILING_TOKENS`", new Object[] { trailingToken, 
/*      */             
/* 1501 */             ClassUtil.nameOf(targetType) }));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public void reportWrongTokenException(JsonParser p, JsonToken expToken, String msg, Object... msgArgs) throws JsonMappingException {
/* 1510 */     msg = _format(msg, msgArgs);
/* 1511 */     throw wrongTokenException(p, expToken, msg);
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
/*      */   @Deprecated
/*      */   public void reportUnknownProperty(Object instanceOrClass, String fieldName, JsonDeserializer<?> deser) throws JsonMappingException {
/* 1530 */     if (isEnabled(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)) {
/*      */       
/* 1532 */       Collection<Object> propIds = (deser == null) ? null : deser.getKnownPropertyNames();
/* 1533 */       throw UnrecognizedPropertyException.from(this._parser, instanceOrClass, fieldName, propIds);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public void reportMissingContent(String msg, Object... msgArgs) throws JsonMappingException {
/* 1545 */     throw MismatchedInputException.from(getParser(), (JavaType)null, "No content to map due to end-of-input");
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
/*      */   public <T> T reportBadTypeDefinition(BeanDescription bean, String msg, Object... msgArgs) throws JsonMappingException {
/* 1564 */     msg = _format(msg, msgArgs);
/* 1565 */     String beanDesc = ClassUtil.nameOf(bean.getBeanClass());
/* 1566 */     msg = String.format("Invalid type definition for type %s: %s", new Object[] { beanDesc, msg });
/* 1567 */     throw InvalidDefinitionException.from(this._parser, msg, bean, null);
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
/*      */   public <T> T reportBadPropertyDefinition(BeanDescription bean, BeanPropertyDefinition prop, String msg, Object... msgArgs) throws JsonMappingException {
/* 1579 */     msg = _format(msg, msgArgs);
/* 1580 */     String propName = ClassUtil.nameOf((Named)prop);
/* 1581 */     String beanDesc = ClassUtil.nameOf(bean.getBeanClass());
/* 1582 */     msg = String.format("Invalid definition for property %s (of type %s): %s", new Object[] { propName, beanDesc, msg });
/*      */     
/* 1584 */     throw InvalidDefinitionException.from(this._parser, msg, bean, prop);
/*      */   }
/*      */ 
/*      */   
/*      */   public <T> T reportBadDefinition(JavaType type, String msg) throws JsonMappingException {
/* 1589 */     throw InvalidDefinitionException.from(this._parser, msg, type);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public <T> T reportBadMerge(JsonDeserializer<?> deser) throws JsonMappingException {
/* 1597 */     handleBadMerge(deser);
/* 1598 */     return null;
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
/*      */   public JsonMappingException wrongTokenException(JsonParser p, JavaType targetType, JsonToken expToken, String extra) {
/* 1621 */     String msg = String.format("Unexpected token (%s), expected %s", new Object[] { p
/* 1622 */           .getCurrentToken(), expToken });
/* 1623 */     msg = _colonConcat(msg, extra);
/* 1624 */     return (JsonMappingException)MismatchedInputException.from(p, targetType, msg);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public JsonMappingException wrongTokenException(JsonParser p, Class<?> targetType, JsonToken expToken, String extra) {
/* 1630 */     String msg = String.format("Unexpected token (%s), expected %s", new Object[] { p
/* 1631 */           .getCurrentToken(), expToken });
/* 1632 */     msg = _colonConcat(msg, extra);
/* 1633 */     return (JsonMappingException)MismatchedInputException.from(p, targetType, msg);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public JsonMappingException wrongTokenException(JsonParser p, JsonToken expToken, String msg) {
/* 1640 */     return wrongTokenException(p, (JavaType)null, expToken, msg);
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
/*      */   public JsonMappingException weirdKeyException(Class<?> keyClass, String keyValue, String msg) {
/* 1653 */     return (JsonMappingException)InvalidFormatException.from(this._parser, 
/* 1654 */         String.format("Cannot deserialize Map key of type %s from String %s: %s", new Object[] {
/* 1655 */             ClassUtil.nameOf(keyClass), _quotedString(keyValue), msg
/*      */           }), keyValue, keyClass);
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
/*      */   public JsonMappingException weirdStringException(String value, Class<?> instClass, String msgBase) {
/* 1674 */     String msg = String.format("Cannot deserialize value of type %s from String %s: %s", new Object[] {
/* 1675 */           ClassUtil.nameOf(instClass), _quotedString(value), msgBase });
/* 1676 */     return (JsonMappingException)InvalidFormatException.from(this._parser, msg, value, instClass);
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
/*      */   public JsonMappingException weirdNumberException(Number value, Class<?> instClass, String msg) {
/* 1688 */     return (JsonMappingException)InvalidFormatException.from(this._parser, 
/* 1689 */         String.format("Cannot deserialize value of type %s from number %s: %s", new Object[] {
/* 1690 */             ClassUtil.nameOf(instClass), String.valueOf(value), msg
/*      */           }), value, instClass);
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
/*      */   public JsonMappingException weirdNativeValueException(Object value, Class<?> instClass) {
/* 1706 */     return (JsonMappingException)InvalidFormatException.from(this._parser, String.format("Cannot deserialize value of type %s from native value (`JsonToken.VALUE_EMBEDDED_OBJECT`) of type %s: incompatible types", new Object[] {
/*      */             
/* 1708 */             ClassUtil.nameOf(instClass), ClassUtil.classNameOf(value)
/*      */           }), value, instClass);
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
/*      */   public JsonMappingException instantiationException(Class<?> instClass, Throwable cause) {
/*      */     String excMsg;
/* 1723 */     if (cause == null) {
/* 1724 */       excMsg = "N/A";
/* 1725 */     } else if ((excMsg = ClassUtil.exceptionMessage(cause)) == null) {
/* 1726 */       excMsg = ClassUtil.nameOf(cause.getClass());
/*      */     } 
/* 1728 */     String msg = String.format("Cannot construct instance of %s, problem: %s", new Object[] {
/* 1729 */           ClassUtil.nameOf(instClass), excMsg
/*      */         });
/*      */     
/* 1732 */     return (JsonMappingException)ValueInstantiationException.from(this._parser, msg, constructType(instClass), cause);
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
/*      */   public JsonMappingException instantiationException(Class<?> instClass, String msg0) {
/* 1747 */     return (JsonMappingException)ValueInstantiationException.from(this._parser, 
/* 1748 */         String.format("Cannot construct instance of %s: %s", new Object[] {
/* 1749 */             ClassUtil.nameOf(instClass), msg0
/* 1750 */           }), constructType(instClass));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public JsonMappingException invalidTypeIdException(JavaType baseType, String typeId, String extraDesc) {
/* 1756 */     String msg = String.format("Could not resolve type id '%s' as a subtype of %s", new Object[] { typeId, baseType });
/*      */     
/* 1758 */     return (JsonMappingException)InvalidTypeIdException.from(this._parser, _colonConcat(msg, extraDesc), baseType, typeId);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JsonMappingException missingTypeIdException(JavaType baseType, String extraDesc) {
/* 1766 */     String msg = String.format("Missing type id when trying to resolve subtype of %s", new Object[] { baseType });
/*      */     
/* 1768 */     return (JsonMappingException)InvalidTypeIdException.from(this._parser, _colonConcat(msg, extraDesc), baseType, null);
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
/*      */   @Deprecated
/*      */   public JsonMappingException unknownTypeException(JavaType type, String id, String extraDesc) {
/* 1786 */     String msg = String.format("Could not resolve type id '%s' into a subtype of %s", new Object[] { id, type });
/*      */     
/* 1788 */     msg = _colonConcat(msg, extraDesc);
/* 1789 */     return (JsonMappingException)MismatchedInputException.from(this._parser, type, msg);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public JsonMappingException endOfInputException(Class<?> instClass) {
/* 1800 */     return (JsonMappingException)MismatchedInputException.from(this._parser, instClass, "Unexpected end-of-input when trying to deserialize a " + instClass
/* 1801 */         .getName());
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
/*      */   @Deprecated
/*      */   public void reportMappingException(String msg, Object... msgArgs) throws JsonMappingException {
/* 1825 */     throw JsonMappingException.from(getParser(), _format(msg, msgArgs));
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
/*      */   @Deprecated
/*      */   public JsonMappingException mappingException(String message) {
/* 1841 */     return JsonMappingException.from(getParser(), message);
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
/*      */   @Deprecated
/*      */   public JsonMappingException mappingException(String msg, Object... msgArgs) {
/* 1857 */     return JsonMappingException.from(getParser(), _format(msg, msgArgs));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public JsonMappingException mappingException(Class<?> targetClass) {
/* 1867 */     return mappingException(targetClass, this._parser.getCurrentToken());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public JsonMappingException mappingException(Class<?> targetClass, JsonToken token) {
/* 1875 */     return JsonMappingException.from(this._parser, 
/* 1876 */         String.format("Cannot deserialize instance of %s out of %s token", new Object[] {
/* 1877 */             ClassUtil.nameOf(targetClass), token
/*      */           }));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected DateFormat getDateFormat() {
/* 1888 */     if (this._dateFormat != null) {
/* 1889 */       return this._dateFormat;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1896 */     DateFormat df = this._config.getDateFormat();
/* 1897 */     this._dateFormat = df = (DateFormat)df.clone();
/* 1898 */     return df;
/*      */   }
/*      */   
/*      */   public abstract ReadableObjectId findObjectId(Object paramObject, ObjectIdGenerator<?> paramObjectIdGenerator, ObjectIdResolver paramObjectIdResolver);
/*      */   
/*      */   public abstract void checkUnresolvedObjectId() throws UnresolvedForwardReference;
/*      */   
/*      */   public abstract JsonDeserializer<Object> deserializerInstance(Annotated paramAnnotated, Object paramObject) throws JsonMappingException;
/*      */   
/*      */   public abstract KeyDeserializer keyDeserializerInstance(Annotated paramAnnotated, Object paramObject) throws JsonMappingException;
/*      */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\DeserializationContext.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */