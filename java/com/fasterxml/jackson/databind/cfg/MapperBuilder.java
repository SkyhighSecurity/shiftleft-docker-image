/*     */ package com.fasterxml.jackson.databind.cfg;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonAutoDetect;
/*     */ import com.fasterxml.jackson.annotation.JsonInclude;
/*     */ import com.fasterxml.jackson.annotation.JsonSetter;
/*     */ import com.fasterxml.jackson.annotation.JsonTypeInfo;
/*     */ import com.fasterxml.jackson.annotation.PropertyAccessor;
/*     */ import com.fasterxml.jackson.core.Base64Variant;
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.PrettyPrinter;
/*     */ import com.fasterxml.jackson.core.StreamReadFeature;
/*     */ import com.fasterxml.jackson.core.StreamWriteFeature;
/*     */ import com.fasterxml.jackson.core.TokenStreamFactory;
/*     */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*     */ import com.fasterxml.jackson.databind.DeserializationFeature;
/*     */ import com.fasterxml.jackson.databind.InjectableValues;
/*     */ import com.fasterxml.jackson.databind.MapperFeature;
/*     */ import com.fasterxml.jackson.databind.Module;
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import com.fasterxml.jackson.databind.PropertyNamingStrategy;
/*     */ import com.fasterxml.jackson.databind.SerializationFeature;
/*     */ import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;
/*     */ import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
/*     */ import com.fasterxml.jackson.databind.jsontype.NamedType;
/*     */ import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
/*     */ import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
/*     */ import com.fasterxml.jackson.databind.node.JsonNodeFactory;
/*     */ import com.fasterxml.jackson.databind.ser.FilterProvider;
/*     */ import com.fasterxml.jackson.databind.ser.SerializerFactory;
/*     */ import com.fasterxml.jackson.databind.type.TypeFactory;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.text.DateFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.ServiceLoader;
/*     */ import java.util.TimeZone;
/*     */ 
/*     */ public abstract class MapperBuilder<M extends ObjectMapper, B extends MapperBuilder<M, B>> {
/*     */   protected final M _mapper;
/*     */   
/*     */   protected MapperBuilder(M mapper) {
/*  46 */     this._mapper = mapper;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public M build() {
/*  57 */     return this._mapper;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEnabled(MapperFeature f) {
/*  67 */     return this._mapper.isEnabled(f);
/*     */   }
/*     */   public boolean isEnabled(DeserializationFeature f) {
/*  70 */     return this._mapper.isEnabled(f);
/*     */   }
/*     */   public boolean isEnabled(SerializationFeature f) {
/*  73 */     return this._mapper.isEnabled(f);
/*     */   }
/*     */   
/*     */   public boolean isEnabled(JsonParser.Feature f) {
/*  77 */     return this._mapper.isEnabled(f);
/*     */   }
/*     */   public boolean isEnabled(JsonGenerator.Feature f) {
/*  80 */     return this._mapper.isEnabled(f);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TokenStreamFactory streamFactory() {
/*  90 */     return (TokenStreamFactory)this._mapper.tokenStreamFactory();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public B enable(MapperFeature... features) {
/* 101 */     this._mapper.enable(features);
/* 102 */     return _this();
/*     */   }
/*     */ 
/*     */   
/*     */   public B disable(MapperFeature... features) {
/* 107 */     this._mapper.disable(features);
/* 108 */     return _this();
/*     */   }
/*     */ 
/*     */   
/*     */   public B configure(MapperFeature feature, boolean state) {
/* 113 */     this._mapper.configure(feature, state);
/* 114 */     return _this();
/*     */   }
/*     */   
/*     */   public B enable(SerializationFeature... features) {
/* 118 */     for (SerializationFeature f : features) {
/* 119 */       this._mapper.enable(f);
/*     */     }
/* 121 */     return _this();
/*     */   }
/*     */   
/*     */   public B disable(SerializationFeature... features) {
/* 125 */     for (SerializationFeature f : features) {
/* 126 */       this._mapper.disable(f);
/*     */     }
/* 128 */     return _this();
/*     */   }
/*     */   
/*     */   public B configure(SerializationFeature feature, boolean state) {
/* 132 */     this._mapper.configure(feature, state);
/* 133 */     return _this();
/*     */   }
/*     */   
/*     */   public B enable(DeserializationFeature... features) {
/* 137 */     for (DeserializationFeature f : features) {
/* 138 */       this._mapper.enable(f);
/*     */     }
/* 140 */     return _this();
/*     */   }
/*     */   
/*     */   public B disable(DeserializationFeature... features) {
/* 144 */     for (DeserializationFeature f : features) {
/* 145 */       this._mapper.disable(f);
/*     */     }
/* 147 */     return _this();
/*     */   }
/*     */   
/*     */   public B configure(DeserializationFeature feature, boolean state) {
/* 151 */     this._mapper.configure(feature, state);
/* 152 */     return _this();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public B enable(JsonParser.Feature... features) {
/* 162 */     this._mapper.enable(features);
/* 163 */     return _this();
/*     */   }
/*     */   
/*     */   public B disable(JsonParser.Feature... features) {
/* 167 */     this._mapper.disable(features);
/* 168 */     return _this();
/*     */   }
/*     */   
/*     */   public B configure(JsonParser.Feature feature, boolean state) {
/* 172 */     this._mapper.configure(feature, state);
/* 173 */     return _this();
/*     */   }
/*     */   
/*     */   public B enable(JsonGenerator.Feature... features) {
/* 177 */     this._mapper.enable(features);
/* 178 */     return _this();
/*     */   }
/*     */   
/*     */   public B disable(JsonGenerator.Feature... features) {
/* 182 */     this._mapper.disable(features);
/* 183 */     return _this();
/*     */   }
/*     */   
/*     */   public B configure(JsonGenerator.Feature feature, boolean state) {
/* 187 */     this._mapper.configure(feature, state);
/* 188 */     return _this();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public B enable(StreamReadFeature... features) {
/* 198 */     for (StreamReadFeature f : features) {
/* 199 */       this._mapper.enable(new JsonParser.Feature[] { f.mappedFeature() });
/*     */     } 
/* 201 */     return _this();
/*     */   }
/*     */   
/*     */   public B disable(StreamReadFeature... features) {
/* 205 */     for (StreamReadFeature f : features) {
/* 206 */       this._mapper.disable(new JsonParser.Feature[] { f.mappedFeature() });
/*     */     } 
/* 208 */     return _this();
/*     */   }
/*     */   
/*     */   public B configure(StreamReadFeature feature, boolean state) {
/* 212 */     this._mapper.configure(feature.mappedFeature(), state);
/* 213 */     return _this();
/*     */   }
/*     */   
/*     */   public B enable(StreamWriteFeature... features) {
/* 217 */     for (StreamWriteFeature f : features) {
/* 218 */       this._mapper.enable(new JsonGenerator.Feature[] { f.mappedFeature() });
/*     */     } 
/* 220 */     return _this();
/*     */   }
/*     */   
/*     */   public B disable(StreamWriteFeature... features) {
/* 224 */     for (StreamWriteFeature f : features) {
/* 225 */       this._mapper.disable(new JsonGenerator.Feature[] { f.mappedFeature() });
/*     */     } 
/* 227 */     return _this();
/*     */   }
/*     */   
/*     */   public B configure(StreamWriteFeature feature, boolean state) {
/* 231 */     this._mapper.configure(feature.mappedFeature(), state);
/* 232 */     return _this();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public B addModule(Module module) {
/* 243 */     this._mapper.registerModule(module);
/* 244 */     return _this();
/*     */   }
/*     */ 
/*     */   
/*     */   public B addModules(Module... modules) {
/* 249 */     for (Module module : modules) {
/* 250 */       addModule(module);
/*     */     }
/* 252 */     return _this();
/*     */   }
/*     */ 
/*     */   
/*     */   public B addModules(Iterable<? extends Module> modules) {
/* 257 */     for (Module module : modules) {
/* 258 */       addModule(module);
/*     */     }
/* 260 */     return _this();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static List<Module> findModules() {
/* 271 */     return findModules(null);
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
/*     */   public static List<Module> findModules(ClassLoader classLoader) {
/* 283 */     ArrayList<Module> modules = new ArrayList<>();
/* 284 */     ServiceLoader<Module> loader = secureGetServiceLoader(Module.class, classLoader);
/* 285 */     for (Module module : loader) {
/* 286 */       modules.add(module);
/*     */     }
/* 288 */     return modules;
/*     */   }
/*     */   
/*     */   private static <T> ServiceLoader<T> secureGetServiceLoader(final Class<T> clazz, final ClassLoader classLoader) {
/* 292 */     SecurityManager sm = System.getSecurityManager();
/* 293 */     if (sm == null) {
/* 294 */       return (classLoader == null) ? 
/* 295 */         ServiceLoader.<T>load(clazz) : ServiceLoader.<T>load(clazz, classLoader);
/*     */     }
/* 297 */     return AccessController.<ServiceLoader<T>>doPrivileged((PrivilegedAction)new PrivilegedAction<ServiceLoader<ServiceLoader<T>>>()
/*     */         {
/*     */           public ServiceLoader<T> run() {
/* 300 */             return (classLoader == null) ? 
/* 301 */               ServiceLoader.<T>load(clazz) : ServiceLoader.<T>load(clazz, classLoader);
/*     */           }
/*     */         });
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
/*     */   public B findAndAddModules() {
/* 317 */     return addModules(findModules());
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public B annotationIntrospector(AnnotationIntrospector intr) {
/* 337 */     this._mapper.setAnnotationIntrospector(intr);
/* 338 */     return _this();
/*     */   }
/*     */   
/*     */   public B nodeFactory(JsonNodeFactory f) {
/* 342 */     this._mapper.setNodeFactory(f);
/* 343 */     return _this();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public B typeFactory(TypeFactory f) {
/* 353 */     this._mapper.setTypeFactory(f);
/* 354 */     return _this();
/*     */   }
/*     */   
/*     */   public B subtypeResolver(SubtypeResolver r) {
/* 358 */     this._mapper.setSubtypeResolver(r);
/* 359 */     return _this();
/*     */   }
/*     */   
/*     */   public B visibility(VisibilityChecker<?> vc) {
/* 363 */     this._mapper.setVisibility(vc);
/* 364 */     return _this();
/*     */   }
/*     */   
/*     */   public B visibility(PropertyAccessor forMethod, JsonAutoDetect.Visibility visibility) {
/* 368 */     this._mapper.setVisibility(forMethod, visibility);
/* 369 */     return _this();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public B handlerInstantiator(HandlerInstantiator hi) {
/* 380 */     this._mapper.setHandlerInstantiator(hi);
/* 381 */     return _this();
/*     */   }
/*     */   
/*     */   public B propertyNamingStrategy(PropertyNamingStrategy s) {
/* 385 */     this._mapper.setPropertyNamingStrategy(s);
/* 386 */     return _this();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public B serializerFactory(SerializerFactory f) {
/* 396 */     this._mapper.setSerializerFactory(f);
/* 397 */     return _this();
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
/*     */   public B filterProvider(FilterProvider prov) {
/* 409 */     this._mapper.setFilterProvider(prov);
/* 410 */     return _this();
/*     */   }
/*     */   
/*     */   public B defaultPrettyPrinter(PrettyPrinter pp) {
/* 414 */     this._mapper.setDefaultPrettyPrinter(pp);
/* 415 */     return _this();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public B injectableValues(InjectableValues v) {
/* 425 */     this._mapper.setInjectableValues(v);
/* 426 */     return _this();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public B addHandler(DeserializationProblemHandler h) {
/* 435 */     this._mapper.addHandler(h);
/* 436 */     return _this();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public B clearProblemHandlers() {
/* 444 */     this._mapper.clearProblemHandlers();
/* 445 */     return _this();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public B defaultSetterInfo(JsonSetter.Value v) {
/* 455 */     this._mapper.setDefaultSetterInfo(v);
/* 456 */     return _this();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public B defaultMergeable(Boolean b) {
/* 465 */     this._mapper.setDefaultMergeable(b);
/* 466 */     return _this();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public B defaultLeniency(Boolean b) {
/* 475 */     this._mapper.setDefaultLeniency(b);
/* 476 */     return _this();
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
/*     */ 
/*     */   
/*     */   public B defaultDateFormat(DateFormat df) {
/* 492 */     this._mapper.setDateFormat(df);
/* 493 */     return _this();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public B defaultTimeZone(TimeZone tz) {
/* 501 */     this._mapper.setTimeZone(tz);
/* 502 */     return _this();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public B defaultLocale(Locale locale) {
/* 510 */     this._mapper.setLocale(locale);
/* 511 */     return _this();
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public B defaultBase64Variant(Base64Variant v) {
/* 529 */     this._mapper.setBase64Variant(v);
/* 530 */     return _this();
/*     */   }
/*     */   
/*     */   public B serializationInclusion(JsonInclude.Include incl) {
/* 534 */     this._mapper.setSerializationInclusion(incl);
/* 535 */     return _this();
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
/*     */   public B addMixIn(Class<?> target, Class<?> mixinSource) {
/* 560 */     this._mapper.addMixIn(target, mixinSource);
/* 561 */     return _this();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public B registerSubtypes(Class<?>... subtypes) {
/* 571 */     this._mapper.registerSubtypes(subtypes);
/* 572 */     return _this();
/*     */   }
/*     */   
/*     */   public B registerSubtypes(NamedType... subtypes) {
/* 576 */     this._mapper.registerSubtypes(subtypes);
/* 577 */     return _this();
/*     */   }
/*     */   
/*     */   public B registerSubtypes(Collection<Class<?>> subtypes) {
/* 581 */     this._mapper.registerSubtypes(subtypes);
/* 582 */     return _this();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public B polymorphicTypeValidator(PolymorphicTypeValidator ptv) {
/* 589 */     this._mapper.setPolymorphicTypeValidator(ptv);
/* 590 */     return _this();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public B activateDefaultTyping(PolymorphicTypeValidator subtypeValidator) {
/* 609 */     this._mapper.activateDefaultTyping(subtypeValidator);
/* 610 */     return _this();
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
/*     */   public B activateDefaultTyping(PolymorphicTypeValidator subtypeValidator, ObjectMapper.DefaultTyping dti) {
/* 624 */     this._mapper.activateDefaultTyping(subtypeValidator, dti);
/* 625 */     return _this();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public B activateDefaultTyping(PolymorphicTypeValidator subtypeValidator, ObjectMapper.DefaultTyping applicability, JsonTypeInfo.As includeAs) {
/* 646 */     this._mapper.activateDefaultTyping(subtypeValidator, applicability, includeAs);
/* 647 */     return _this();
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
/*     */ 
/*     */ 
/*     */   
/*     */   public B activateDefaultTypingAsProperty(PolymorphicTypeValidator subtypeValidator, ObjectMapper.DefaultTyping applicability, String propertyName) {
/* 664 */     this._mapper.activateDefaultTypingAsProperty(subtypeValidator, applicability, propertyName);
/* 665 */     return _this();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public B deactivateDefaultTyping() {
/* 675 */     this._mapper.deactivateDefaultTyping();
/* 676 */     return _this();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final B _this() {
/* 687 */     return (B)this;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\cfg\MapperBuilder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */