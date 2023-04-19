/*     */ package org.springframework.http.converter.json;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonInclude;
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*     */ import com.fasterxml.jackson.databind.DeserializationFeature;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.MapperFeature;
/*     */ import com.fasterxml.jackson.databind.Module;
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import com.fasterxml.jackson.databind.PropertyNamingStrategy;
/*     */ import com.fasterxml.jackson.databind.SerializationFeature;
/*     */ import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
/*     */ import com.fasterxml.jackson.databind.module.SimpleModule;
/*     */ import com.fasterxml.jackson.databind.ser.FilterProvider;
/*     */ import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
/*     */ import com.fasterxml.jackson.dataformat.xml.XmlFactory;
/*     */ import com.fasterxml.jackson.dataformat.xml.XmlMapper;
/*     */ import java.text.DateFormat;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.TimeZone;
/*     */ import javax.xml.stream.XMLInputFactory;
/*     */ import javax.xml.stream.XMLResolver;
/*     */ import org.springframework.beans.BeanUtils;
/*     */ import org.springframework.beans.FatalBeanException;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.StreamUtils;
/*     */ import org.springframework.util.StringUtils;
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
/*     */ public class Jackson2ObjectMapperBuilder
/*     */ {
/*     */   private boolean createXmlMapper = false;
/*     */   private DateFormat dateFormat;
/*     */   private Locale locale;
/*     */   private TimeZone timeZone;
/*     */   private AnnotationIntrospector annotationIntrospector;
/*     */   private PropertyNamingStrategy propertyNamingStrategy;
/*     */   private TypeResolverBuilder<?> defaultTyping;
/*     */   private JsonInclude.Include serializationInclusion;
/*     */   private FilterProvider filters;
/* 111 */   private final Map<Class<?>, Class<?>> mixIns = new HashMap<Class<?>, Class<?>>();
/*     */   
/* 113 */   private final Map<Class<?>, JsonSerializer<?>> serializers = new LinkedHashMap<Class<?>, JsonSerializer<?>>();
/*     */   
/* 115 */   private final Map<Class<?>, JsonDeserializer<?>> deserializers = new LinkedHashMap<Class<?>, JsonDeserializer<?>>();
/*     */   
/* 117 */   private final Map<Object, Boolean> features = new HashMap<Object, Boolean>();
/*     */   
/*     */   private List<Module> modules;
/*     */   
/*     */   private Class<? extends Module>[] moduleClasses;
/*     */   
/*     */   private boolean findModulesViaServiceLoader = false;
/*     */   
/*     */   private boolean findWellKnownModules = true;
/*     */   
/* 127 */   private ClassLoader moduleClassLoader = getClass().getClassLoader();
/*     */ 
/*     */ 
/*     */   
/*     */   private HandlerInstantiator handlerInstantiator;
/*     */ 
/*     */   
/*     */   private ApplicationContext applicationContext;
/*     */ 
/*     */   
/*     */   private Boolean defaultUseWrapper;
/*     */ 
/*     */ 
/*     */   
/*     */   public Jackson2ObjectMapperBuilder createXmlMapper(boolean createXmlMapper) {
/* 142 */     this.createXmlMapper = createXmlMapper;
/* 143 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Jackson2ObjectMapperBuilder dateFormat(DateFormat dateFormat) {
/* 153 */     this.dateFormat = dateFormat;
/* 154 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Jackson2ObjectMapperBuilder simpleDateFormat(String format) {
/* 164 */     this.dateFormat = new SimpleDateFormat(format);
/* 165 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Jackson2ObjectMapperBuilder locale(Locale locale) {
/* 174 */     this.locale = locale;
/* 175 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Jackson2ObjectMapperBuilder locale(String localeString) {
/* 185 */     this.locale = StringUtils.parseLocaleString(localeString);
/* 186 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Jackson2ObjectMapperBuilder timeZone(TimeZone timeZone) {
/* 195 */     this.timeZone = timeZone;
/* 196 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Jackson2ObjectMapperBuilder timeZone(String timeZoneString) {
/* 206 */     this.timeZone = StringUtils.parseTimeZoneString(timeZoneString);
/* 207 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Jackson2ObjectMapperBuilder annotationIntrospector(AnnotationIntrospector annotationIntrospector) {
/* 214 */     this.annotationIntrospector = annotationIntrospector;
/* 215 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Jackson2ObjectMapperBuilder propertyNamingStrategy(PropertyNamingStrategy propertyNamingStrategy) {
/* 223 */     this.propertyNamingStrategy = propertyNamingStrategy;
/* 224 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Jackson2ObjectMapperBuilder defaultTyping(TypeResolverBuilder<?> typeResolverBuilder) {
/* 232 */     this.defaultTyping = typeResolverBuilder;
/* 233 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Jackson2ObjectMapperBuilder serializationInclusion(JsonInclude.Include serializationInclusion) {
/* 241 */     this.serializationInclusion = serializationInclusion;
/* 242 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Jackson2ObjectMapperBuilder filters(FilterProvider filters) {
/* 251 */     this.filters = filters;
/* 252 */     return this;
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
/*     */   public Jackson2ObjectMapperBuilder mixIn(Class<?> target, Class<?> mixinSource) {
/* 264 */     if (mixinSource != null) {
/* 265 */       this.mixIns.put(target, mixinSource);
/*     */     }
/* 267 */     return this;
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
/*     */   public Jackson2ObjectMapperBuilder mixIns(Map<Class<?>, Class<?>> mixIns) {
/* 279 */     if (mixIns != null) {
/* 280 */       this.mixIns.putAll(mixIns);
/*     */     }
/* 282 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Jackson2ObjectMapperBuilder serializers(JsonSerializer<?>... serializers) {
/* 291 */     if (serializers != null) {
/* 292 */       for (JsonSerializer<?> serializer : serializers) {
/* 293 */         Class<?> handledType = serializer.handledType();
/* 294 */         if (handledType == null || handledType == Object.class) {
/* 295 */           throw new IllegalArgumentException("Unknown handled type in " + serializer.getClass().getName());
/*     */         }
/* 297 */         this.serializers.put(serializer.handledType(), serializer);
/*     */       } 
/*     */     }
/* 300 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Jackson2ObjectMapperBuilder serializerByType(Class<?> type, JsonSerializer<?> serializer) {
/* 309 */     if (serializer != null) {
/* 310 */       this.serializers.put(type, serializer);
/*     */     }
/* 312 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Jackson2ObjectMapperBuilder serializersByType(Map<Class<?>, JsonSerializer<?>> serializers) {
/* 320 */     if (serializers != null) {
/* 321 */       this.serializers.putAll(serializers);
/*     */     }
/* 323 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Jackson2ObjectMapperBuilder deserializers(JsonDeserializer<?>... deserializers) {
/* 333 */     if (deserializers != null) {
/* 334 */       for (JsonDeserializer<?> deserializer : deserializers) {
/* 335 */         Class<?> handledType = deserializer.handledType();
/* 336 */         if (handledType == null || handledType == Object.class) {
/* 337 */           throw new IllegalArgumentException("Unknown handled type in " + deserializer.getClass().getName());
/*     */         }
/* 339 */         this.deserializers.put(deserializer.handledType(), deserializer);
/*     */       } 
/*     */     }
/* 342 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Jackson2ObjectMapperBuilder deserializerByType(Class<?> type, JsonDeserializer<?> deserializer) {
/* 350 */     if (deserializer != null) {
/* 351 */       this.deserializers.put(type, deserializer);
/*     */     }
/* 353 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Jackson2ObjectMapperBuilder deserializersByType(Map<Class<?>, JsonDeserializer<?>> deserializers) {
/* 360 */     if (deserializers != null) {
/* 361 */       this.deserializers.putAll(deserializers);
/*     */     }
/* 363 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Jackson2ObjectMapperBuilder autoDetectFields(boolean autoDetectFields) {
/* 370 */     this.features.put(MapperFeature.AUTO_DETECT_FIELDS, Boolean.valueOf(autoDetectFields));
/* 371 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Jackson2ObjectMapperBuilder autoDetectGettersSetters(boolean autoDetectGettersSetters) {
/* 380 */     this.features.put(MapperFeature.AUTO_DETECT_GETTERS, Boolean.valueOf(autoDetectGettersSetters));
/* 381 */     this.features.put(MapperFeature.AUTO_DETECT_SETTERS, Boolean.valueOf(autoDetectGettersSetters));
/* 382 */     this.features.put(MapperFeature.AUTO_DETECT_IS_GETTERS, Boolean.valueOf(autoDetectGettersSetters));
/* 383 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Jackson2ObjectMapperBuilder defaultViewInclusion(boolean defaultViewInclusion) {
/* 390 */     this.features.put(MapperFeature.DEFAULT_VIEW_INCLUSION, Boolean.valueOf(defaultViewInclusion));
/* 391 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Jackson2ObjectMapperBuilder failOnUnknownProperties(boolean failOnUnknownProperties) {
/* 398 */     this.features.put(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, Boolean.valueOf(failOnUnknownProperties));
/* 399 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Jackson2ObjectMapperBuilder failOnEmptyBeans(boolean failOnEmptyBeans) {
/* 406 */     this.features.put(SerializationFeature.FAIL_ON_EMPTY_BEANS, Boolean.valueOf(failOnEmptyBeans));
/* 407 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Jackson2ObjectMapperBuilder indentOutput(boolean indentOutput) {
/* 414 */     this.features.put(SerializationFeature.INDENT_OUTPUT, Boolean.valueOf(indentOutput));
/* 415 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Jackson2ObjectMapperBuilder defaultUseWrapper(boolean defaultUseWrapper) {
/* 424 */     this.defaultUseWrapper = Boolean.valueOf(defaultUseWrapper);
/* 425 */     return this;
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
/*     */   public Jackson2ObjectMapperBuilder featuresToEnable(Object... featuresToEnable) {
/* 437 */     if (featuresToEnable != null) {
/* 438 */       for (Object feature : featuresToEnable) {
/* 439 */         this.features.put(feature, Boolean.TRUE);
/*     */       }
/*     */     }
/* 442 */     return this;
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
/*     */   public Jackson2ObjectMapperBuilder featuresToDisable(Object... featuresToDisable) {
/* 454 */     if (featuresToDisable != null) {
/* 455 */       for (Object feature : featuresToDisable) {
/* 456 */         this.features.put(feature, Boolean.FALSE);
/*     */       }
/*     */     }
/* 459 */     return this;
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
/*     */   public Jackson2ObjectMapperBuilder modules(Module... modules) {
/* 474 */     return modules(Arrays.asList(modules));
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
/*     */   public Jackson2ObjectMapperBuilder modules(List<Module> modules) {
/* 488 */     this.modules = new LinkedList<Module>(modules);
/* 489 */     this.findModulesViaServiceLoader = false;
/* 490 */     this.findWellKnownModules = false;
/* 491 */     return this;
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
/*     */   public Jackson2ObjectMapperBuilder modulesToInstall(Module... modules) {
/* 505 */     this.modules = Arrays.asList(modules);
/* 506 */     this.findWellKnownModules = true;
/* 507 */     return this;
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
/*     */   public Jackson2ObjectMapperBuilder modulesToInstall(Class<? extends Module>... modules) {
/* 523 */     this.moduleClasses = modules;
/* 524 */     this.findWellKnownModules = true;
/* 525 */     return this;
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
/*     */   public Jackson2ObjectMapperBuilder findModulesViaServiceLoader(boolean findModules) {
/* 537 */     this.findModulesViaServiceLoader = findModules;
/* 538 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Jackson2ObjectMapperBuilder moduleClassLoader(ClassLoader moduleClassLoader) {
/* 545 */     this.moduleClassLoader = moduleClassLoader;
/* 546 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Jackson2ObjectMapperBuilder handlerInstantiator(HandlerInstantiator handlerInstantiator) {
/* 556 */     this.handlerInstantiator = handlerInstantiator;
/* 557 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Jackson2ObjectMapperBuilder applicationContext(ApplicationContext applicationContext) {
/* 567 */     this.applicationContext = applicationContext;
/* 568 */     return this;
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
/*     */   public <T extends ObjectMapper> T build() {
/*     */     ObjectMapper mapper;
/* 582 */     if (this.createXmlMapper) {
/*     */ 
/*     */       
/* 585 */       mapper = (this.defaultUseWrapper != null) ? (new XmlObjectMapperInitializer()).create(this.defaultUseWrapper.booleanValue()) : (new XmlObjectMapperInitializer()).create();
/*     */     } else {
/*     */       
/* 588 */       mapper = new ObjectMapper();
/*     */     } 
/* 590 */     configure(mapper);
/* 591 */     return (T)mapper;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void configure(ObjectMapper objectMapper) {
/* 600 */     Assert.notNull(objectMapper, "ObjectMapper must not be null");
/*     */     
/* 602 */     if (this.findModulesViaServiceLoader) {
/*     */       
/* 604 */       objectMapper.registerModules(ObjectMapper.findModules(this.moduleClassLoader));
/*     */     }
/* 606 */     else if (this.findWellKnownModules) {
/* 607 */       registerWellKnownModulesIfAvailable(objectMapper);
/*     */     } 
/*     */     
/* 610 */     if (this.modules != null) {
/* 611 */       for (Module module : this.modules)
/*     */       {
/* 613 */         objectMapper.registerModule(module);
/*     */       }
/*     */     }
/* 616 */     if (this.moduleClasses != null) {
/* 617 */       for (Class<? extends Module> module : this.moduleClasses) {
/* 618 */         objectMapper.registerModule((Module)BeanUtils.instantiate(module));
/*     */       }
/*     */     }
/*     */     
/* 622 */     if (this.dateFormat != null) {
/* 623 */       objectMapper.setDateFormat(this.dateFormat);
/*     */     }
/* 625 */     if (this.locale != null) {
/* 626 */       objectMapper.setLocale(this.locale);
/*     */     }
/* 628 */     if (this.timeZone != null) {
/* 629 */       objectMapper.setTimeZone(this.timeZone);
/*     */     }
/*     */     
/* 632 */     if (this.annotationIntrospector != null) {
/* 633 */       objectMapper.setAnnotationIntrospector(this.annotationIntrospector);
/*     */     }
/* 635 */     if (this.propertyNamingStrategy != null) {
/* 636 */       objectMapper.setPropertyNamingStrategy(this.propertyNamingStrategy);
/*     */     }
/* 638 */     if (this.defaultTyping != null) {
/* 639 */       objectMapper.setDefaultTyping(this.defaultTyping);
/*     */     }
/* 641 */     if (this.serializationInclusion != null) {
/* 642 */       objectMapper.setSerializationInclusion(this.serializationInclusion);
/*     */     }
/*     */     
/* 645 */     if (this.filters != null) {
/* 646 */       objectMapper.setFilterProvider(this.filters);
/*     */     }
/*     */     
/* 649 */     for (Class<?> target : this.mixIns.keySet()) {
/* 650 */       objectMapper.addMixIn(target, this.mixIns.get(target));
/*     */     }
/*     */     
/* 653 */     if (!this.serializers.isEmpty() || !this.deserializers.isEmpty()) {
/* 654 */       SimpleModule module = new SimpleModule();
/* 655 */       addSerializers(module);
/* 656 */       addDeserializers(module);
/* 657 */       objectMapper.registerModule((Module)module);
/*     */     } 
/*     */     
/* 660 */     customizeDefaultFeatures(objectMapper);
/* 661 */     for (Object feature : this.features.keySet()) {
/* 662 */       configureFeature(objectMapper, feature, ((Boolean)this.features.get(feature)).booleanValue());
/*     */     }
/*     */     
/* 665 */     if (this.handlerInstantiator != null) {
/* 666 */       objectMapper.setHandlerInstantiator(this.handlerInstantiator);
/*     */     }
/* 668 */     else if (this.applicationContext != null) {
/* 669 */       objectMapper.setHandlerInstantiator(new SpringHandlerInstantiator(this.applicationContext
/* 670 */             .getAutowireCapableBeanFactory()));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void customizeDefaultFeatures(ObjectMapper objectMapper) {
/* 678 */     if (!this.features.containsKey(MapperFeature.DEFAULT_VIEW_INCLUSION)) {
/* 679 */       configureFeature(objectMapper, MapperFeature.DEFAULT_VIEW_INCLUSION, false);
/*     */     }
/* 681 */     if (!this.features.containsKey(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)) {
/* 682 */       configureFeature(objectMapper, DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private <T> void addSerializers(SimpleModule module) {
/* 688 */     for (Class<?> type : this.serializers.keySet()) {
/* 689 */       module.addSerializer(type, this.serializers.get(type));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private <T> void addDeserializers(SimpleModule module) {
/* 695 */     for (Class<?> type : this.deserializers.keySet()) {
/* 696 */       module.addDeserializer(type, this.deserializers.get(type));
/*     */     }
/*     */   }
/*     */   
/*     */   private void configureFeature(ObjectMapper objectMapper, Object feature, boolean enabled) {
/* 701 */     if (feature instanceof JsonParser.Feature) {
/* 702 */       objectMapper.configure((JsonParser.Feature)feature, enabled);
/*     */     }
/* 704 */     else if (feature instanceof JsonGenerator.Feature) {
/* 705 */       objectMapper.configure((JsonGenerator.Feature)feature, enabled);
/*     */     }
/* 707 */     else if (feature instanceof SerializationFeature) {
/* 708 */       objectMapper.configure((SerializationFeature)feature, enabled);
/*     */     }
/* 710 */     else if (feature instanceof DeserializationFeature) {
/* 711 */       objectMapper.configure((DeserializationFeature)feature, enabled);
/*     */     }
/* 713 */     else if (feature instanceof MapperFeature) {
/* 714 */       objectMapper.configure((MapperFeature)feature, enabled);
/*     */     } else {
/*     */       
/* 717 */       throw new FatalBeanException("Unknown feature class: " + feature.getClass().getName());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void registerWellKnownModulesIfAvailable(ObjectMapper objectMapper) {
/* 724 */     if (ClassUtils.isPresent("java.nio.file.Path", this.moduleClassLoader)) {
/*     */       
/*     */       try {
/* 727 */         Class<? extends Module> jdk7Module = ClassUtils.forName("com.fasterxml.jackson.datatype.jdk7.Jdk7Module", this.moduleClassLoader);
/* 728 */         objectMapper.registerModule((Module)BeanUtils.instantiateClass(jdk7Module));
/*     */       }
/* 730 */       catch (ClassNotFoundException classNotFoundException) {}
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 736 */     if (ClassUtils.isPresent("java.util.Optional", this.moduleClassLoader)) {
/*     */       
/*     */       try {
/* 739 */         Class<? extends Module> jdk8Module = ClassUtils.forName("com.fasterxml.jackson.datatype.jdk8.Jdk8Module", this.moduleClassLoader);
/* 740 */         objectMapper.registerModule((Module)BeanUtils.instantiateClass(jdk8Module));
/*     */       }
/* 742 */       catch (ClassNotFoundException classNotFoundException) {}
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 748 */     if (ClassUtils.isPresent("java.time.LocalDate", this.moduleClassLoader)) {
/*     */       
/*     */       try {
/* 751 */         Class<? extends Module> javaTimeModule = ClassUtils.forName("com.fasterxml.jackson.datatype.jsr310.JavaTimeModule", this.moduleClassLoader);
/* 752 */         objectMapper.registerModule((Module)BeanUtils.instantiateClass(javaTimeModule));
/*     */       }
/* 754 */       catch (ClassNotFoundException classNotFoundException) {}
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 760 */     if (ClassUtils.isPresent("org.joda.time.LocalDate", this.moduleClassLoader)) {
/*     */       
/*     */       try {
/* 763 */         Class<? extends Module> jodaModule = ClassUtils.forName("com.fasterxml.jackson.datatype.joda.JodaModule", this.moduleClassLoader);
/* 764 */         objectMapper.registerModule((Module)BeanUtils.instantiateClass(jodaModule));
/*     */       }
/* 766 */       catch (ClassNotFoundException classNotFoundException) {}
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 772 */     if (ClassUtils.isPresent("kotlin.Unit", this.moduleClassLoader)) {
/*     */       
/*     */       try {
/* 775 */         Class<? extends Module> kotlinModule = ClassUtils.forName("com.fasterxml.jackson.module.kotlin.KotlinModule", this.moduleClassLoader);
/* 776 */         objectMapper.registerModule((Module)BeanUtils.instantiateClass(kotlinModule));
/*     */       }
/* 778 */       catch (ClassNotFoundException classNotFoundException) {}
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
/*     */   
/*     */   public static Jackson2ObjectMapperBuilder json() {
/* 792 */     return new Jackson2ObjectMapperBuilder();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Jackson2ObjectMapperBuilder xml() {
/* 800 */     return (new Jackson2ObjectMapperBuilder()).createXmlMapper(true);
/*     */   }
/*     */   
/*     */   private static class XmlObjectMapperInitializer {
/*     */     private XmlObjectMapperInitializer() {}
/*     */     
/*     */     public ObjectMapper create() {
/* 807 */       return (ObjectMapper)new XmlMapper(xmlInputFactory());
/*     */     }
/*     */     
/*     */     public ObjectMapper create(boolean defaultUseWrapper) {
/* 811 */       JacksonXmlModule module = new JacksonXmlModule();
/* 812 */       module.setDefaultUseWrapper(defaultUseWrapper);
/* 813 */       return (ObjectMapper)new XmlMapper(new XmlFactory(xmlInputFactory()), module);
/*     */     }
/*     */     
/*     */     private static XMLInputFactory xmlInputFactory() {
/* 817 */       XMLInputFactory inputFactory = XMLInputFactory.newInstance();
/* 818 */       inputFactory.setProperty("javax.xml.stream.supportDTD", Boolean.valueOf(false));
/* 819 */       inputFactory.setProperty("javax.xml.stream.isSupportingExternalEntities", Boolean.valueOf(false));
/* 820 */       inputFactory.setXMLResolver(NO_OP_XML_RESOLVER);
/* 821 */       return inputFactory;
/*     */     }
/*     */     
/* 824 */     private static final XMLResolver NO_OP_XML_RESOLVER = new XMLResolver()
/*     */       {
/*     */         public Object resolveEntity(String publicID, String systemID, String base, String ns) {
/* 827 */           return StreamUtils.emptyInput();
/*     */         }
/*     */       };
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\http\converter\json\Jackson2ObjectMapperBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */