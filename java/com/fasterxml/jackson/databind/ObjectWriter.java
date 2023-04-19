/*      */ package com.fasterxml.jackson.databind;
/*      */ import com.fasterxml.jackson.core.Base64Variant;
/*      */ import com.fasterxml.jackson.core.FormatFeature;
/*      */ import com.fasterxml.jackson.core.FormatSchema;
/*      */ import com.fasterxml.jackson.core.JsonEncoding;
/*      */ import com.fasterxml.jackson.core.JsonFactory;
/*      */ import com.fasterxml.jackson.core.JsonGenerationException;
/*      */ import com.fasterxml.jackson.core.JsonGenerator;
/*      */ import com.fasterxml.jackson.core.JsonParser;
/*      */ import com.fasterxml.jackson.core.JsonProcessingException;
/*      */ import com.fasterxml.jackson.core.PrettyPrinter;
/*      */ import com.fasterxml.jackson.core.SerializableString;
/*      */ import com.fasterxml.jackson.core.io.CharacterEscapes;
/*      */ import com.fasterxml.jackson.core.io.SegmentedStringWriter;
/*      */ import com.fasterxml.jackson.core.io.SerializedString;
/*      */ import com.fasterxml.jackson.core.type.TypeReference;
/*      */ import com.fasterxml.jackson.core.util.ByteArrayBuilder;
/*      */ import com.fasterxml.jackson.core.util.Instantiatable;
/*      */ import com.fasterxml.jackson.core.util.MinimalPrettyPrinter;
/*      */ import com.fasterxml.jackson.databind.cfg.ContextAttributes;
/*      */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
/*      */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*      */ import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
/*      */ import com.fasterxml.jackson.databind.ser.FilterProvider;
/*      */ import com.fasterxml.jackson.databind.ser.SerializerFactory;
/*      */ import com.fasterxml.jackson.databind.type.TypeFactory;
/*      */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*      */ import java.io.Closeable;
/*      */ import java.io.DataOutput;
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.io.OutputStream;
/*      */ import java.io.Serializable;
/*      */ import java.io.Writer;
/*      */ import java.text.DateFormat;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.TimeZone;
/*      */ import java.util.concurrent.atomic.AtomicReference;
/*      */ 
/*      */ public class ObjectWriter implements Versioned, Serializable {
/*   42 */   protected static final PrettyPrinter NULL_PRETTY_PRINTER = (PrettyPrinter)new MinimalPrettyPrinter();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final long serialVersionUID = 1L;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final SerializationConfig _config;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final DefaultSerializerProvider _serializerProvider;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final SerializerFactory _serializerFactory;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final JsonFactory _generatorFactory;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final GeneratorSettings _generatorSettings;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final Prefetch _prefetch;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected ObjectWriter(ObjectMapper mapper, SerializationConfig config, JavaType rootType, PrettyPrinter pp) {
/*  100 */     this._config = config;
/*  101 */     this._serializerProvider = mapper._serializerProvider;
/*  102 */     this._serializerFactory = mapper._serializerFactory;
/*  103 */     this._generatorFactory = mapper._jsonFactory;
/*  104 */     this._generatorSettings = (pp == null) ? GeneratorSettings.empty : new GeneratorSettings(pp, null, null, null);
/*      */ 
/*      */     
/*  107 */     if (rootType == null) {
/*  108 */       this._prefetch = Prefetch.empty;
/*  109 */     } else if (rootType.hasRawClass(Object.class)) {
/*      */ 
/*      */       
/*  112 */       this._prefetch = Prefetch.empty.forRootType(this, rootType);
/*      */     } else {
/*  114 */       this._prefetch = Prefetch.empty.forRootType(this, rootType.withStaticTyping());
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected ObjectWriter(ObjectMapper mapper, SerializationConfig config) {
/*  123 */     this._config = config;
/*  124 */     this._serializerProvider = mapper._serializerProvider;
/*  125 */     this._serializerFactory = mapper._serializerFactory;
/*  126 */     this._generatorFactory = mapper._jsonFactory;
/*      */     
/*  128 */     this._generatorSettings = GeneratorSettings.empty;
/*  129 */     this._prefetch = Prefetch.empty;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected ObjectWriter(ObjectMapper mapper, SerializationConfig config, FormatSchema s) {
/*  138 */     this._config = config;
/*      */     
/*  140 */     this._serializerProvider = mapper._serializerProvider;
/*  141 */     this._serializerFactory = mapper._serializerFactory;
/*  142 */     this._generatorFactory = mapper._jsonFactory;
/*      */     
/*  144 */     this._generatorSettings = (s == null) ? GeneratorSettings.empty : new GeneratorSettings(null, s, null, null);
/*      */     
/*  146 */     this._prefetch = Prefetch.empty;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected ObjectWriter(ObjectWriter base, SerializationConfig config, GeneratorSettings genSettings, Prefetch prefetch) {
/*  155 */     this._config = config;
/*      */     
/*  157 */     this._serializerProvider = base._serializerProvider;
/*  158 */     this._serializerFactory = base._serializerFactory;
/*  159 */     this._generatorFactory = base._generatorFactory;
/*      */     
/*  161 */     this._generatorSettings = genSettings;
/*  162 */     this._prefetch = prefetch;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected ObjectWriter(ObjectWriter base, SerializationConfig config) {
/*  170 */     this._config = config;
/*      */     
/*  172 */     this._serializerProvider = base._serializerProvider;
/*  173 */     this._serializerFactory = base._serializerFactory;
/*  174 */     this._generatorFactory = base._generatorFactory;
/*      */     
/*  176 */     this._generatorSettings = base._generatorSettings;
/*  177 */     this._prefetch = base._prefetch;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected ObjectWriter(ObjectWriter base, JsonFactory f) {
/*  186 */     this
/*  187 */       ._config = (SerializationConfig)base._config.with(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, f.requiresPropertyOrdering());
/*      */     
/*  189 */     this._serializerProvider = base._serializerProvider;
/*  190 */     this._serializerFactory = base._serializerFactory;
/*  191 */     this._generatorFactory = f;
/*      */     
/*  193 */     this._generatorSettings = base._generatorSettings;
/*  194 */     this._prefetch = base._prefetch;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Version version() {
/*  203 */     return PackageVersion.VERSION;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected ObjectWriter _new(ObjectWriter base, JsonFactory f) {
/*  218 */     return new ObjectWriter(base, f);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected ObjectWriter _new(ObjectWriter base, SerializationConfig config) {
/*  227 */     if (config == this._config) {
/*  228 */       return this;
/*      */     }
/*  230 */     return new ObjectWriter(base, config);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected ObjectWriter _new(GeneratorSettings genSettings, Prefetch prefetch) {
/*  241 */     if (this._generatorSettings == genSettings && this._prefetch == prefetch) {
/*  242 */       return this;
/*      */     }
/*  244 */     return new ObjectWriter(this, this._config, genSettings, prefetch);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected SequenceWriter _newSequenceWriter(boolean wrapInArray, JsonGenerator gen, boolean managedInput) throws IOException {
/*  258 */     _configureGenerator(gen);
/*  259 */     return (new SequenceWriter(_serializerProvider(), gen, managedInput, this._prefetch))
/*      */       
/*  261 */       .init(wrapInArray);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectWriter with(SerializationFeature feature) {
/*  275 */     return _new(this, this._config.with(feature));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectWriter with(SerializationFeature first, SerializationFeature... other) {
/*  283 */     return _new(this, this._config.with(first, other));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectWriter withFeatures(SerializationFeature... features) {
/*  291 */     return _new(this, this._config.withFeatures(features));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectWriter without(SerializationFeature feature) {
/*  299 */     return _new(this, this._config.without(feature));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectWriter without(SerializationFeature first, SerializationFeature... other) {
/*  307 */     return _new(this, this._config.without(first, other));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectWriter withoutFeatures(SerializationFeature... features) {
/*  315 */     return _new(this, this._config.withoutFeatures(features));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectWriter with(JsonGenerator.Feature feature) {
/*  328 */     return _new(this, this._config.with(feature));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectWriter withFeatures(JsonGenerator.Feature... features) {
/*  335 */     return _new(this, this._config.withFeatures(features));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectWriter without(JsonGenerator.Feature feature) {
/*  342 */     return _new(this, this._config.without(feature));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectWriter withoutFeatures(JsonGenerator.Feature... features) {
/*  349 */     return _new(this, this._config.withoutFeatures(features));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectWriter with(FormatFeature feature) {
/*  362 */     return _new(this, this._config.with(feature));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectWriter withFeatures(FormatFeature... features) {
/*  369 */     return _new(this, this._config.withFeatures(features));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectWriter without(FormatFeature feature) {
/*  376 */     return _new(this, this._config.without(feature));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectWriter withoutFeatures(FormatFeature... features) {
/*  383 */     return _new(this, this._config.withoutFeatures(features));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectWriter forType(JavaType rootType) {
/*  403 */     return _new(this._generatorSettings, this._prefetch.forRootType(this, rootType));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectWriter forType(Class<?> rootType) {
/*  414 */     return forType(this._config.constructType(rootType));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectWriter forType(TypeReference<?> rootType) {
/*  425 */     return forType(this._config.getTypeFactory().constructType(rootType.getType()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public ObjectWriter withType(JavaType rootType) {
/*  433 */     return forType(rootType);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public ObjectWriter withType(Class<?> rootType) {
/*  441 */     return forType(rootType);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public ObjectWriter withType(TypeReference<?> rootType) {
/*  449 */     return forType(rootType);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectWriter with(DateFormat df) {
/*  467 */     return _new(this, this._config.with(df));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectWriter withDefaultPrettyPrinter() {
/*  475 */     return with(this._config.getDefaultPrettyPrinter());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectWriter with(FilterProvider filterProvider) {
/*  483 */     if (filterProvider == this._config.getFilterProvider()) {
/*  484 */       return this;
/*      */     }
/*  486 */     return _new(this, this._config.withFilters(filterProvider));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectWriter with(PrettyPrinter pp) {
/*  494 */     return _new(this._generatorSettings.with(pp), this._prefetch);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectWriter withRootName(String rootName) {
/*  509 */     return _new(this, (SerializationConfig)this._config.withRootName(rootName));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectWriter withRootName(PropertyName rootName) {
/*  516 */     return _new(this, this._config.withRootName(rootName));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectWriter withoutRootName() {
/*  530 */     return _new(this, this._config.withRootName(PropertyName.NO_NAME));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectWriter with(FormatSchema schema) {
/*  541 */     _verifySchemaType(schema);
/*  542 */     return _new(this._generatorSettings.with(schema), this._prefetch);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public ObjectWriter withSchema(FormatSchema schema) {
/*  550 */     return with(schema);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectWriter withView(Class<?> view) {
/*  562 */     return _new(this, this._config.withView(view));
/*      */   }
/*      */   
/*      */   public ObjectWriter with(Locale l) {
/*  566 */     return _new(this, (SerializationConfig)this._config.with(l));
/*      */   }
/*      */   
/*      */   public ObjectWriter with(TimeZone tz) {
/*  570 */     return _new(this, (SerializationConfig)this._config.with(tz));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectWriter with(Base64Variant b64variant) {
/*  580 */     return _new(this, (SerializationConfig)this._config.with(b64variant));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectWriter with(CharacterEscapes escapes) {
/*  587 */     return _new(this._generatorSettings.with(escapes), this._prefetch);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectWriter with(JsonFactory f) {
/*  594 */     return (f == this._generatorFactory) ? this : _new(this, f);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectWriter with(ContextAttributes attrs) {
/*  601 */     return _new(this, this._config.with(attrs));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectWriter withAttributes(Map<?, ?> attrs) {
/*  611 */     return _new(this, (SerializationConfig)this._config.withAttributes(attrs));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectWriter withAttribute(Object key, Object value) {
/*  618 */     return _new(this, (SerializationConfig)this._config.withAttribute(key, value));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectWriter withoutAttribute(Object key) {
/*  625 */     return _new(this, (SerializationConfig)this._config.withoutAttribute(key));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectWriter withRootValueSeparator(String sep) {
/*  632 */     return _new(this._generatorSettings.withRootValueSeparator(sep), this._prefetch);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectWriter withRootValueSeparator(SerializableString sep) {
/*  639 */     return _new(this._generatorSettings.withRootValueSeparator(sep), this._prefetch);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public SequenceWriter writeValues(File out) throws IOException {
/*  662 */     _assertNotNull("out", out);
/*  663 */     return _newSequenceWriter(false, this._generatorFactory
/*  664 */         .createGenerator(out, JsonEncoding.UTF8), true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public SequenceWriter writeValues(JsonGenerator g) throws IOException {
/*  683 */     _assertNotNull("g", g);
/*  684 */     _configureGenerator(g);
/*  685 */     return _newSequenceWriter(false, g, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public SequenceWriter writeValues(Writer out) throws IOException {
/*  702 */     _assertNotNull("out", out);
/*  703 */     return _newSequenceWriter(false, this._generatorFactory
/*  704 */         .createGenerator(out), true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public SequenceWriter writeValues(OutputStream out) throws IOException {
/*  721 */     _assertNotNull("out", out);
/*  722 */     return _newSequenceWriter(false, this._generatorFactory
/*  723 */         .createGenerator(out, JsonEncoding.UTF8), true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public SequenceWriter writeValues(DataOutput out) throws IOException {
/*  730 */     _assertNotNull("out", out);
/*  731 */     return _newSequenceWriter(false, this._generatorFactory
/*  732 */         .createGenerator(out), true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public SequenceWriter writeValuesAsArray(File out) throws IOException {
/*  751 */     _assertNotNull("out", out);
/*  752 */     return _newSequenceWriter(true, this._generatorFactory
/*  753 */         .createGenerator(out, JsonEncoding.UTF8), true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public SequenceWriter writeValuesAsArray(JsonGenerator gen) throws IOException {
/*  773 */     _assertNotNull("gen", gen);
/*  774 */     return _newSequenceWriter(true, gen, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public SequenceWriter writeValuesAsArray(Writer out) throws IOException {
/*  793 */     _assertNotNull("out", out);
/*  794 */     return _newSequenceWriter(true, this._generatorFactory.createGenerator(out), true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public SequenceWriter writeValuesAsArray(OutputStream out) throws IOException {
/*  813 */     _assertNotNull("out", out);
/*  814 */     return _newSequenceWriter(true, this._generatorFactory
/*  815 */         .createGenerator(out, JsonEncoding.UTF8), true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public SequenceWriter writeValuesAsArray(DataOutput out) throws IOException {
/*  822 */     _assertNotNull("out", out);
/*  823 */     return _newSequenceWriter(true, this._generatorFactory.createGenerator(out), true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isEnabled(SerializationFeature f) {
/*  833 */     return this._config.isEnabled(f);
/*      */   }
/*      */   
/*      */   public boolean isEnabled(MapperFeature f) {
/*  837 */     return this._config.isEnabled(f);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public boolean isEnabled(JsonParser.Feature f) {
/*  845 */     return this._generatorFactory.isEnabled(f);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isEnabled(JsonGenerator.Feature f) {
/*  852 */     return this._generatorFactory.isEnabled(f);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public SerializationConfig getConfig() {
/*  859 */     return this._config;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JsonFactory getFactory() {
/*  866 */     return this._generatorFactory;
/*      */   }
/*      */   
/*      */   public TypeFactory getTypeFactory() {
/*  870 */     return this._config.getTypeFactory();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasPrefetchedSerializer() {
/*  882 */     return this._prefetch.hasSerializer();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ContextAttributes getAttributes() {
/*  889 */     return this._config.getAttributes();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void writeValue(JsonGenerator g, Object value) throws IOException {
/*  904 */     _assertNotNull("g", g);
/*  905 */     _configureGenerator(g);
/*  906 */     if (this._config.isEnabled(SerializationFeature.CLOSE_CLOSEABLE) && value instanceof Closeable) {
/*      */ 
/*      */       
/*  909 */       Closeable toClose = (Closeable)value;
/*      */       try {
/*  911 */         this._prefetch.serialize(g, value, _serializerProvider());
/*  912 */         if (this._config.isEnabled(SerializationFeature.FLUSH_AFTER_WRITE_VALUE)) {
/*  913 */           g.flush();
/*      */         }
/*  915 */       } catch (Exception e) {
/*  916 */         ClassUtil.closeOnFailAndThrowAsIOE(null, toClose, e);
/*      */         return;
/*      */       } 
/*  919 */       toClose.close();
/*      */     } else {
/*  921 */       this._prefetch.serialize(g, value, _serializerProvider());
/*  922 */       if (this._config.isEnabled(SerializationFeature.FLUSH_AFTER_WRITE_VALUE)) {
/*  923 */         g.flush();
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
/*      */   public void writeValue(File resultFile, Object value) throws IOException, JsonGenerationException, JsonMappingException {
/*  941 */     _assertNotNull("resultFile", resultFile);
/*  942 */     _configAndWriteValue(this._generatorFactory.createGenerator(resultFile, JsonEncoding.UTF8), value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*  959 */     _assertNotNull("out", out);
/*  960 */     _configAndWriteValue(this._generatorFactory.createGenerator(out, JsonEncoding.UTF8), value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*  976 */     _assertNotNull("w", w);
/*  977 */     _configAndWriteValue(this._generatorFactory.createGenerator(w), value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void writeValue(DataOutput out, Object value) throws IOException {
/*  986 */     _assertNotNull("out", out);
/*  987 */     _configAndWriteValue(this._generatorFactory.createGenerator(out), value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/* 1003 */     SegmentedStringWriter sw = new SegmentedStringWriter(this._generatorFactory._getBufferRecycler());
/*      */     try {
/* 1005 */       _configAndWriteValue(this._generatorFactory.createGenerator((Writer)sw), value);
/* 1006 */     } catch (JsonProcessingException e) {
/* 1007 */       throw e;
/* 1008 */     } catch (IOException e) {
/* 1009 */       throw JsonMappingException.fromUnexpectedIOE(e);
/*      */     } 
/* 1011 */     return sw.getAndClear();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/* 1027 */     ByteArrayBuilder bb = new ByteArrayBuilder(this._generatorFactory._getBufferRecycler());
/*      */     try {
/* 1029 */       _configAndWriteValue(this._generatorFactory.createGenerator((OutputStream)bb, JsonEncoding.UTF8), value);
/* 1030 */     } catch (JsonProcessingException e) {
/* 1031 */       throw e;
/* 1032 */     } catch (IOException e) {
/* 1033 */       throw JsonMappingException.fromUnexpectedIOE(e);
/*      */     } 
/* 1035 */     byte[] result = bb.toByteArray();
/* 1036 */     bb.release();
/* 1037 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/* 1060 */     _assertNotNull("type", type);
/* 1061 */     _assertNotNull("visitor", visitor);
/* 1062 */     _serializerProvider().acceptJsonFormatVisitor(type, visitor);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void acceptJsonFormatVisitor(Class<?> type, JsonFormatVisitorWrapper visitor) throws JsonMappingException {
/* 1069 */     _assertNotNull("type", type);
/* 1070 */     _assertNotNull("visitor", visitor);
/* 1071 */     acceptJsonFormatVisitor(this._config.constructType(type), visitor);
/*      */   }
/*      */   
/*      */   public boolean canSerialize(Class<?> type) {
/* 1075 */     _assertNotNull("type", type);
/* 1076 */     return _serializerProvider().hasSerializerFor(type, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean canSerialize(Class<?> type, AtomicReference<Throwable> cause) {
/* 1086 */     _assertNotNull("type", type);
/* 1087 */     return _serializerProvider().hasSerializerFor(type, cause);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected DefaultSerializerProvider _serializerProvider() {
/* 1101 */     return this._serializerProvider.createInstance(this._config, this._serializerFactory);
/*      */   }
/*      */ 
/*      */ 
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
/* 1115 */     if (schema != null && 
/* 1116 */       !this._generatorFactory.canUseSchema(schema)) {
/* 1117 */       throw new IllegalArgumentException("Cannot use FormatSchema of type " + schema.getClass().getName() + " for format " + this._generatorFactory
/* 1118 */           .getFormatName());
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final void _configAndWriteValue(JsonGenerator gen, Object value) throws IOException {
/* 1129 */     _configureGenerator(gen);
/* 1130 */     if (this._config.isEnabled(SerializationFeature.CLOSE_CLOSEABLE) && value instanceof Closeable) {
/* 1131 */       _writeCloseable(gen, value);
/*      */       return;
/*      */     } 
/*      */     try {
/* 1135 */       this._prefetch.serialize(gen, value, _serializerProvider());
/* 1136 */     } catch (Exception e) {
/* 1137 */       ClassUtil.closeOnFailAndThrowAsIOE(gen, e);
/*      */       return;
/*      */     } 
/* 1140 */     gen.close();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final void _writeCloseable(JsonGenerator gen, Object value) throws IOException {
/* 1150 */     Closeable toClose = (Closeable)value;
/*      */     try {
/* 1152 */       this._prefetch.serialize(gen, value, _serializerProvider());
/* 1153 */       Closeable tmpToClose = toClose;
/* 1154 */       toClose = null;
/* 1155 */       tmpToClose.close();
/* 1156 */     } catch (Exception e) {
/* 1157 */       ClassUtil.closeOnFailAndThrowAsIOE(gen, toClose, e);
/*      */       return;
/*      */     } 
/* 1160 */     gen.close();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final void _configureGenerator(JsonGenerator gen) {
/* 1173 */     this._config.initialize(gen);
/* 1174 */     this._generatorSettings.initialize(gen);
/*      */   }
/*      */   
/*      */   protected final void _assertNotNull(String paramName, Object src) {
/* 1178 */     if (src == null) {
/* 1179 */       throw new IllegalArgumentException(String.format("argument \"%s\" is null", new Object[] { paramName }));
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
/*      */   public static final class GeneratorSettings
/*      */     implements Serializable
/*      */   {
/*      */     private static final long serialVersionUID = 1L;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1201 */     public static final GeneratorSettings empty = new GeneratorSettings(null, null, null, null);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public final PrettyPrinter prettyPrinter;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public final FormatSchema schema;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public final CharacterEscapes characterEscapes;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public final SerializableString rootValueSeparator;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public GeneratorSettings(PrettyPrinter pp, FormatSchema sch, CharacterEscapes esc, SerializableString rootSep) {
/* 1232 */       this.prettyPrinter = pp;
/* 1233 */       this.schema = sch;
/* 1234 */       this.characterEscapes = esc;
/* 1235 */       this.rootValueSeparator = rootSep;
/*      */     }
/*      */ 
/*      */     
/*      */     public GeneratorSettings with(PrettyPrinter pp) {
/* 1240 */       if (pp == null) {
/* 1241 */         pp = ObjectWriter.NULL_PRETTY_PRINTER;
/*      */       }
/* 1243 */       return (pp == this.prettyPrinter) ? this : new GeneratorSettings(pp, this.schema, this.characterEscapes, this.rootValueSeparator);
/*      */     }
/*      */ 
/*      */     
/*      */     public GeneratorSettings with(FormatSchema sch) {
/* 1248 */       return (this.schema == sch) ? this : new GeneratorSettings(this.prettyPrinter, sch, this.characterEscapes, this.rootValueSeparator);
/*      */     }
/*      */ 
/*      */     
/*      */     public GeneratorSettings with(CharacterEscapes esc) {
/* 1253 */       return (this.characterEscapes == esc) ? this : new GeneratorSettings(this.prettyPrinter, this.schema, esc, this.rootValueSeparator);
/*      */     }
/*      */ 
/*      */     
/*      */     public GeneratorSettings withRootValueSeparator(String sep) {
/* 1258 */       if (sep == null) {
/* 1259 */         if (this.rootValueSeparator == null) {
/* 1260 */           return this;
/*      */         }
/* 1262 */         return new GeneratorSettings(this.prettyPrinter, this.schema, this.characterEscapes, null);
/*      */       } 
/* 1264 */       if (sep.equals(_rootValueSeparatorAsString())) {
/* 1265 */         return this;
/*      */       }
/* 1267 */       return new GeneratorSettings(this.prettyPrinter, this.schema, this.characterEscapes, (SerializableString)new SerializedString(sep));
/*      */     }
/*      */ 
/*      */     
/*      */     public GeneratorSettings withRootValueSeparator(SerializableString sep) {
/* 1272 */       if (sep == null) {
/* 1273 */         if (this.rootValueSeparator == null) {
/* 1274 */           return this;
/*      */         }
/* 1276 */         return new GeneratorSettings(this.prettyPrinter, this.schema, this.characterEscapes, null);
/*      */       } 
/* 1278 */       if (sep.equals(this.rootValueSeparator)) {
/* 1279 */         return this;
/*      */       }
/* 1281 */       return new GeneratorSettings(this.prettyPrinter, this.schema, this.characterEscapes, sep);
/*      */     }
/*      */     
/*      */     private final String _rootValueSeparatorAsString() {
/* 1285 */       return (this.rootValueSeparator == null) ? null : this.rootValueSeparator.getValue();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void initialize(JsonGenerator gen) {
/* 1293 */       PrettyPrinter pp = this.prettyPrinter;
/* 1294 */       if (this.prettyPrinter != null) {
/* 1295 */         if (pp == ObjectWriter.NULL_PRETTY_PRINTER) {
/* 1296 */           gen.setPrettyPrinter(null);
/*      */         } else {
/* 1298 */           if (pp instanceof Instantiatable) {
/* 1299 */             pp = (PrettyPrinter)((Instantiatable)pp).createInstance();
/*      */           }
/* 1301 */           gen.setPrettyPrinter(pp);
/*      */         } 
/*      */       }
/* 1304 */       if (this.characterEscapes != null) {
/* 1305 */         gen.setCharacterEscapes(this.characterEscapes);
/*      */       }
/* 1307 */       if (this.schema != null) {
/* 1308 */         gen.setSchema(this.schema);
/*      */       }
/* 1310 */       if (this.rootValueSeparator != null) {
/* 1311 */         gen.setRootValueSeparator(this.rootValueSeparator);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final class Prefetch
/*      */     implements Serializable
/*      */   {
/*      */     private static final long serialVersionUID = 1L;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1328 */     public static final Prefetch empty = new Prefetch(null, null, null);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final JavaType rootType;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final JsonSerializer<Object> valueSerializer;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final TypeSerializer typeSerializer;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private Prefetch(JavaType rootT, JsonSerializer<Object> ser, TypeSerializer typeSer) {
/* 1354 */       this.rootType = rootT;
/* 1355 */       this.valueSerializer = ser;
/* 1356 */       this.typeSerializer = typeSer;
/*      */     }
/*      */ 
/*      */     
/*      */     public Prefetch forRootType(ObjectWriter parent, JavaType newType) {
/* 1361 */       if (newType == null) {
/* 1362 */         if (this.rootType == null || this.valueSerializer == null) {
/* 1363 */           return this;
/*      */         }
/* 1365 */         return new Prefetch(null, null, null);
/*      */       } 
/*      */ 
/*      */       
/* 1369 */       if (newType.equals(this.rootType)) {
/* 1370 */         return this;
/*      */       }
/*      */ 
/*      */ 
/*      */       
/* 1375 */       if (newType.isJavaLangObject()) {
/* 1376 */         TypeSerializer typeSer; DefaultSerializerProvider prov = parent._serializerProvider();
/*      */ 
/*      */         
/*      */         try {
/* 1380 */           typeSer = prov.findTypeSerializer(newType);
/* 1381 */         } catch (JsonMappingException e) {
/*      */ 
/*      */           
/* 1384 */           throw new RuntimeJsonMappingException(e);
/*      */         } 
/* 1386 */         return new Prefetch(null, null, typeSer);
/*      */       } 
/*      */       
/* 1389 */       if (parent.isEnabled(SerializationFeature.EAGER_SERIALIZER_FETCH)) {
/* 1390 */         DefaultSerializerProvider prov = parent._serializerProvider();
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         try {
/* 1396 */           JsonSerializer<Object> ser = prov.findTypedValueSerializer(newType, true, null);
/*      */           
/* 1398 */           if (ser instanceof TypeWrappedSerializer) {
/* 1399 */             return new Prefetch(newType, null, ((TypeWrappedSerializer)ser)
/* 1400 */                 .typeSerializer());
/*      */           }
/* 1402 */           return new Prefetch(newType, ser, null);
/* 1403 */         } catch (JsonMappingException typeSer) {}
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 1408 */       return new Prefetch(newType, null, this.typeSerializer);
/*      */     }
/*      */     
/*      */     public final JsonSerializer<Object> getValueSerializer() {
/* 1412 */       return this.valueSerializer;
/*      */     }
/*      */     
/*      */     public final TypeSerializer getTypeSerializer() {
/* 1416 */       return this.typeSerializer;
/*      */     }
/*      */     
/*      */     public boolean hasSerializer() {
/* 1420 */       return (this.valueSerializer != null || this.typeSerializer != null);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void serialize(JsonGenerator gen, Object value, DefaultSerializerProvider prov) throws IOException {
/* 1426 */       if (this.typeSerializer != null) {
/* 1427 */         prov.serializePolymorphic(gen, value, this.rootType, this.valueSerializer, this.typeSerializer);
/* 1428 */       } else if (this.valueSerializer != null) {
/* 1429 */         prov.serializeValue(gen, value, this.rootType, this.valueSerializer);
/* 1430 */       } else if (this.rootType != null) {
/* 1431 */         prov.serializeValue(gen, value, this.rootType);
/*      */       } else {
/* 1433 */         prov.serializeValue(gen, value);
/*      */       } 
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\ObjectWriter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */