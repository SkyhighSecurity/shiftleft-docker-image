/*     */ package com.fasterxml.jackson.databind.deser;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*     */ import com.fasterxml.jackson.databind.BeanDescription;
/*     */ import com.fasterxml.jackson.databind.DeserializationConfig;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.MapperFeature;
/*     */ import com.fasterxml.jackson.databind.PropertyMetadata;
/*     */ import com.fasterxml.jackson.databind.PropertyName;
/*     */ import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
/*     */ import com.fasterxml.jackson.databind.deser.impl.BeanPropertyMap;
/*     */ import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader;
/*     */ import com.fasterxml.jackson.databind.deser.impl.ObjectIdValueProperty;
/*     */ import com.fasterxml.jackson.databind.deser.impl.ValueInjector;
/*     */ import com.fasterxml.jackson.databind.introspect.Annotated;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
/*     */ import com.fasterxml.jackson.databind.util.Annotations;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ public class BeanDeserializerBuilder
/*     */ {
/*     */   protected final DeserializationConfig _config;
/*     */   protected final DeserializationContext _context;
/*     */   protected final BeanDescription _beanDesc;
/*  54 */   protected final Map<String, SettableBeanProperty> _properties = new LinkedHashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected List<ValueInjector> _injectables;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected HashMap<String, SettableBeanProperty> _backRefProperties;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected HashSet<String> _ignorableProps;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ValueInstantiator _valueInstantiator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ObjectIdReader _objectIdReader;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SettableAnyProperty _anySetter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean _ignoreAllUnknown;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AnnotatedMethod _buildMethod;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected JsonPOJOBuilder.Value _builderConfig;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanDeserializerBuilder(BeanDescription beanDesc, DeserializationContext ctxt) {
/* 117 */     this._beanDesc = beanDesc;
/* 118 */     this._context = ctxt;
/* 119 */     this._config = ctxt.getConfig();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected BeanDeserializerBuilder(BeanDeserializerBuilder src) {
/* 128 */     this._beanDesc = src._beanDesc;
/* 129 */     this._context = src._context;
/* 130 */     this._config = src._config;
/*     */ 
/*     */     
/* 133 */     this._properties.putAll(src._properties);
/* 134 */     this._injectables = _copy(src._injectables);
/* 135 */     this._backRefProperties = _copy(src._backRefProperties);
/*     */     
/* 137 */     this._ignorableProps = src._ignorableProps;
/* 138 */     this._valueInstantiator = src._valueInstantiator;
/* 139 */     this._objectIdReader = src._objectIdReader;
/*     */     
/* 141 */     this._anySetter = src._anySetter;
/* 142 */     this._ignoreAllUnknown = src._ignoreAllUnknown;
/*     */     
/* 144 */     this._buildMethod = src._buildMethod;
/* 145 */     this._builderConfig = src._builderConfig;
/*     */   }
/*     */   
/*     */   private static HashMap<String, SettableBeanProperty> _copy(HashMap<String, SettableBeanProperty> src) {
/* 149 */     return (src == null) ? null : new HashMap<>(src);
/*     */   }
/*     */ 
/*     */   
/*     */   private static <T> List<T> _copy(List<T> src) {
/* 154 */     return (src == null) ? null : new ArrayList<>(src);
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
/*     */   public void addOrReplaceProperty(SettableBeanProperty prop, boolean allowOverride) {
/* 167 */     this._properties.put(prop.getName(), prop);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addProperty(SettableBeanProperty prop) {
/* 177 */     SettableBeanProperty old = this._properties.put(prop.getName(), prop);
/* 178 */     if (old != null && old != prop) {
/* 179 */       throw new IllegalArgumentException("Duplicate property '" + prop.getName() + "' for " + this._beanDesc.getType());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addBackReferenceProperty(String referenceName, SettableBeanProperty prop) {
/* 190 */     if (this._backRefProperties == null) {
/* 191 */       this._backRefProperties = new HashMap<>(4);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 196 */     prop.fixAccess(this._config);
/* 197 */     this._backRefProperties.put(referenceName, prop);
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
/*     */   public void addInjectable(PropertyName propName, JavaType propType, Annotations contextAnnotations, AnnotatedMember member, Object valueId) {
/* 213 */     if (this._injectables == null) {
/* 214 */       this._injectables = new ArrayList<>();
/*     */     }
/* 216 */     boolean fixAccess = this._config.canOverrideAccessModifiers();
/* 217 */     boolean forceAccess = (fixAccess && this._config.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS));
/* 218 */     if (fixAccess) {
/* 219 */       member.fixAccess(forceAccess);
/*     */     }
/* 221 */     this._injectables.add(new ValueInjector(propName, propType, member, valueId));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addIgnorable(String propName) {
/* 230 */     if (this._ignorableProps == null) {
/* 231 */       this._ignorableProps = new HashSet<>();
/*     */     }
/* 233 */     this._ignorableProps.add(propName);
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
/*     */   public void addCreatorProperty(SettableBeanProperty prop) {
/* 248 */     addProperty(prop);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAnySetter(SettableAnyProperty s) {
/* 253 */     if (this._anySetter != null && s != null) {
/* 254 */       throw new IllegalStateException("_anySetter already set to non-null");
/*     */     }
/* 256 */     this._anySetter = s;
/*     */   }
/*     */   
/*     */   public void setIgnoreUnknownProperties(boolean ignore) {
/* 260 */     this._ignoreAllUnknown = ignore;
/*     */   }
/*     */   
/*     */   public void setValueInstantiator(ValueInstantiator inst) {
/* 264 */     this._valueInstantiator = inst;
/*     */   }
/*     */   
/*     */   public void setObjectIdReader(ObjectIdReader r) {
/* 268 */     this._objectIdReader = r;
/*     */   }
/*     */   
/*     */   public void setPOJOBuilder(AnnotatedMethod buildMethod, JsonPOJOBuilder.Value config) {
/* 272 */     this._buildMethod = buildMethod;
/* 273 */     this._builderConfig = config;
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
/*     */   public Iterator<SettableBeanProperty> getProperties() {
/* 291 */     return this._properties.values().iterator();
/*     */   }
/*     */   
/*     */   public SettableBeanProperty findProperty(PropertyName propertyName) {
/* 295 */     return this._properties.get(propertyName.getSimpleName());
/*     */   }
/*     */   
/*     */   public boolean hasProperty(PropertyName propertyName) {
/* 299 */     return (findProperty(propertyName) != null);
/*     */   }
/*     */   
/*     */   public SettableBeanProperty removeProperty(PropertyName name) {
/* 303 */     return this._properties.remove(name.getSimpleName());
/*     */   }
/*     */   
/*     */   public SettableAnyProperty getAnySetter() {
/* 307 */     return this._anySetter;
/*     */   }
/*     */   
/*     */   public ValueInstantiator getValueInstantiator() {
/* 311 */     return this._valueInstantiator;
/*     */   }
/*     */   
/*     */   public List<ValueInjector> getInjectables() {
/* 315 */     return this._injectables;
/*     */   }
/*     */   
/*     */   public ObjectIdReader getObjectIdReader() {
/* 319 */     return this._objectIdReader;
/*     */   }
/*     */   
/*     */   public AnnotatedMethod getBuildMethod() {
/* 323 */     return this._buildMethod;
/*     */   }
/*     */   
/*     */   public JsonPOJOBuilder.Value getBuilderConfig() {
/* 327 */     return this._builderConfig;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasIgnorable(String name) {
/* 334 */     return (this._ignorableProps != null && this._ignorableProps.contains(name));
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
/*     */   public JsonDeserializer<?> build() {
/* 349 */     Collection<SettableBeanProperty> props = this._properties.values();
/* 350 */     _fixAccess(props);
/* 351 */     BeanPropertyMap propertyMap = BeanPropertyMap.construct(props, this._config
/* 352 */         .isEnabled(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES), 
/* 353 */         _collectAliases(props));
/* 354 */     propertyMap.assignIndexes();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 359 */     boolean anyViews = !this._config.isEnabled(MapperFeature.DEFAULT_VIEW_INCLUSION);
/* 360 */     if (!anyViews) {
/* 361 */       for (SettableBeanProperty prop : props) {
/* 362 */         if (prop.hasViews()) {
/* 363 */           anyViews = true;
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/* 370 */     if (this._objectIdReader != null) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 375 */       ObjectIdValueProperty prop = new ObjectIdValueProperty(this._objectIdReader, PropertyMetadata.STD_REQUIRED);
/* 376 */       propertyMap = propertyMap.withProperty((SettableBeanProperty)prop);
/*     */     } 
/*     */     
/* 379 */     return (JsonDeserializer<?>)new BeanDeserializer(this, this._beanDesc, propertyMap, this._backRefProperties, this._ignorableProps, this._ignoreAllUnknown, anyViews);
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
/*     */   public AbstractDeserializer buildAbstract() {
/* 392 */     return new AbstractDeserializer(this, this._beanDesc, this._backRefProperties, this._properties);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonDeserializer<?> buildBuilderBased(JavaType valueType, String expBuildMethodName) throws JsonMappingException {
/* 403 */     if (this._buildMethod == null) {
/*     */       
/* 405 */       if (!expBuildMethodName.isEmpty()) {
/* 406 */         this._context.reportBadDefinition(this._beanDesc.getType(), 
/* 407 */             String.format("Builder class %s does not have build method (name: '%s')", new Object[] {
/* 408 */                 this._beanDesc.getBeanClass().getName(), expBuildMethodName
/*     */               }));
/*     */       }
/*     */     } else {
/*     */       
/* 413 */       Class<?> rawBuildType = this._buildMethod.getRawReturnType();
/* 414 */       Class<?> rawValueType = valueType.getRawClass();
/* 415 */       if (rawBuildType != rawValueType && 
/* 416 */         !rawBuildType.isAssignableFrom(rawValueType) && 
/* 417 */         !rawValueType.isAssignableFrom(rawBuildType)) {
/* 418 */         this._context.reportBadDefinition(this._beanDesc.getType(), 
/* 419 */             String.format("Build method '%s' has wrong return type (%s), not compatible with POJO type (%s)", new Object[] {
/* 420 */                 this._buildMethod.getFullName(), rawBuildType
/* 421 */                 .getName(), valueType
/* 422 */                 .getRawClass().getName()
/*     */               }));
/*     */       }
/*     */     } 
/* 426 */     Collection<SettableBeanProperty> props = this._properties.values();
/* 427 */     _fixAccess(props);
/* 428 */     BeanPropertyMap propertyMap = BeanPropertyMap.construct(props, this._config
/* 429 */         .isEnabled(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES), 
/* 430 */         _collectAliases(props));
/* 431 */     propertyMap.assignIndexes();
/*     */     
/* 433 */     boolean anyViews = !this._config.isEnabled(MapperFeature.DEFAULT_VIEW_INCLUSION);
/*     */     
/* 435 */     if (!anyViews) {
/* 436 */       for (SettableBeanProperty prop : props) {
/* 437 */         if (prop.hasViews()) {
/* 438 */           anyViews = true;
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     }
/* 444 */     if (this._objectIdReader != null) {
/*     */ 
/*     */       
/* 447 */       ObjectIdValueProperty prop = new ObjectIdValueProperty(this._objectIdReader, PropertyMetadata.STD_REQUIRED);
/*     */       
/* 449 */       propertyMap = propertyMap.withProperty((SettableBeanProperty)prop);
/*     */     } 
/*     */     
/* 452 */     return (JsonDeserializer<?>)new BuilderBasedDeserializer(this, this._beanDesc, valueType, propertyMap, this._backRefProperties, this._ignorableProps, this._ignoreAllUnknown, anyViews);
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
/*     */   protected void _fixAccess(Collection<SettableBeanProperty> mainProps) {
/* 477 */     for (SettableBeanProperty prop : mainProps)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 484 */       prop.fixAccess(this._config);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 495 */     if (this._anySetter != null) {
/* 496 */       this._anySetter.fixAccess(this._config);
/*     */     }
/* 498 */     if (this._buildMethod != null) {
/* 499 */       this._buildMethod.fixAccess(this._config.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected Map<String, List<PropertyName>> _collectAliases(Collection<SettableBeanProperty> props) {
/* 505 */     Map<String, List<PropertyName>> mapping = null;
/* 506 */     AnnotationIntrospector intr = this._config.getAnnotationIntrospector();
/* 507 */     if (intr != null) {
/* 508 */       for (SettableBeanProperty prop : props) {
/* 509 */         List<PropertyName> aliases = intr.findPropertyAliases((Annotated)prop.getMember());
/* 510 */         if (aliases == null || aliases.isEmpty()) {
/*     */           continue;
/*     */         }
/* 513 */         if (mapping == null) {
/* 514 */           mapping = new HashMap<>();
/*     */         }
/* 516 */         mapping.put(prop.getName(), aliases);
/*     */       } 
/*     */     }
/* 519 */     if (mapping == null) {
/* 520 */       return Collections.emptyMap();
/*     */     }
/* 522 */     return mapping;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\deser\BeanDeserializerBuilder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */