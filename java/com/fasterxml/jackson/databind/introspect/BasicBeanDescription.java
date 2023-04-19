/*     */ package com.fasterxml.jackson.databind.introspect;
/*     */ import com.fasterxml.jackson.annotation.JsonCreator;
/*     */ import com.fasterxml.jackson.annotation.JsonFormat;
/*     */ import com.fasterxml.jackson.annotation.JsonInclude;
/*     */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*     */ import com.fasterxml.jackson.databind.BeanDescription;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.MapperFeature;
/*     */ import com.fasterxml.jackson.databind.PropertyName;
/*     */ import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
/*     */ import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
/*     */ import com.fasterxml.jackson.databind.cfg.MapperConfig;
/*     */ import com.fasterxml.jackson.databind.util.Annotations;
/*     */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*     */ import com.fasterxml.jackson.databind.util.Converter;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class BasicBeanDescription extends BeanDescription {
/*  31 */   private static final Class<?>[] NO_VIEWS = new Class[0];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final POJOPropertiesCollector _propCollector;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final MapperConfig<?> _config;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final AnnotationIntrospector _annotationIntrospector;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final AnnotatedClass _classInfo;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Class<?>[] _defaultViews;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean _defaultViewsResolved;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected List<BeanPropertyDefinition> _properties;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ObjectIdInfo _objectIdInfo;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected BasicBeanDescription(POJOPropertiesCollector coll, JavaType type, AnnotatedClass classDef) {
/*  96 */     super(type);
/*  97 */     this._propCollector = coll;
/*  98 */     this._config = coll.getConfig();
/*     */     
/* 100 */     if (this._config == null) {
/* 101 */       this._annotationIntrospector = null;
/*     */     } else {
/* 103 */       this._annotationIntrospector = this._config.getAnnotationIntrospector();
/*     */     } 
/* 105 */     this._classInfo = classDef;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected BasicBeanDescription(MapperConfig<?> config, JavaType type, AnnotatedClass classDef, List<BeanPropertyDefinition> props) {
/* 115 */     super(type);
/* 116 */     this._propCollector = null;
/* 117 */     this._config = config;
/*     */     
/* 119 */     if (this._config == null) {
/* 120 */       this._annotationIntrospector = null;
/*     */     } else {
/* 122 */       this._annotationIntrospector = this._config.getAnnotationIntrospector();
/*     */     } 
/* 124 */     this._classInfo = classDef;
/* 125 */     this._properties = props;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BasicBeanDescription(POJOPropertiesCollector coll) {
/* 130 */     this(coll, coll.getType(), coll.getClassDef());
/* 131 */     this._objectIdInfo = coll.getObjectIdInfo();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static BasicBeanDescription forDeserialization(POJOPropertiesCollector coll) {
/* 139 */     return new BasicBeanDescription(coll);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static BasicBeanDescription forSerialization(POJOPropertiesCollector coll) {
/* 147 */     return new BasicBeanDescription(coll);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static BasicBeanDescription forOtherUse(MapperConfig<?> config, JavaType type, AnnotatedClass ac) {
/* 158 */     return new BasicBeanDescription(config, type, ac, 
/* 159 */         Collections.emptyList());
/*     */   }
/*     */   
/*     */   protected List<BeanPropertyDefinition> _properties() {
/* 163 */     if (this._properties == null) {
/* 164 */       this._properties = this._propCollector.getProperties();
/*     */     }
/* 166 */     return this._properties;
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
/*     */   public boolean removeProperty(String propName) {
/* 184 */     Iterator<BeanPropertyDefinition> it = _properties().iterator();
/* 185 */     while (it.hasNext()) {
/* 186 */       BeanPropertyDefinition prop = it.next();
/* 187 */       if (prop.getName().equals(propName)) {
/* 188 */         it.remove();
/* 189 */         return true;
/*     */       } 
/*     */     } 
/* 192 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean addProperty(BeanPropertyDefinition def) {
/* 198 */     if (hasProperty(def.getFullName())) {
/* 199 */       return false;
/*     */     }
/* 201 */     _properties().add(def);
/* 202 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasProperty(PropertyName name) {
/* 209 */     return (findProperty(name) != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanPropertyDefinition findProperty(PropertyName name) {
/* 217 */     for (BeanPropertyDefinition prop : _properties()) {
/* 218 */       if (prop.hasName(name)) {
/* 219 */         return prop;
/*     */       }
/*     */     } 
/* 222 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotatedClass getClassInfo() {
/* 232 */     return this._classInfo;
/*     */   }
/*     */   public ObjectIdInfo getObjectIdInfo() {
/* 235 */     return this._objectIdInfo;
/*     */   }
/*     */   
/*     */   public List<BeanPropertyDefinition> findProperties() {
/* 239 */     return _properties();
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public AnnotatedMethod findJsonValueMethod() {
/* 245 */     return (this._propCollector == null) ? null : this._propCollector
/* 246 */       .getJsonValueMethod();
/*     */   }
/*     */ 
/*     */   
/*     */   public AnnotatedMember findJsonValueAccessor() {
/* 251 */     return (this._propCollector == null) ? null : this._propCollector
/* 252 */       .getJsonValueAccessor();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<String> getIgnoredPropertyNames() {
/* 258 */     Set<String> ign = (this._propCollector == null) ? null : this._propCollector.getIgnoredPropertyNames();
/* 259 */     if (ign == null) {
/* 260 */       return Collections.emptySet();
/*     */     }
/* 262 */     return ign;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasKnownClassAnnotations() {
/* 267 */     return this._classInfo.hasAnnotations();
/*     */   }
/*     */ 
/*     */   
/*     */   public Annotations getClassAnnotations() {
/* 272 */     return this._classInfo.getAnnotations();
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public TypeBindings bindingsForBeanType() {
/* 278 */     return this._type.getBindings();
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public JavaType resolveType(Type jdkType) {
/* 284 */     if (jdkType == null) {
/* 285 */       return null;
/*     */     }
/* 287 */     return this._config.getTypeFactory().constructType(jdkType, this._type.getBindings());
/*     */   }
/*     */ 
/*     */   
/*     */   public AnnotatedConstructor findDefaultConstructor() {
/* 292 */     return this._classInfo.getDefaultConstructor();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotatedMember findAnySetterAccessor() throws IllegalArgumentException {
/* 298 */     if (this._propCollector != null) {
/* 299 */       AnnotatedMethod anyMethod = this._propCollector.getAnySetterMethod();
/* 300 */       if (anyMethod != null) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 307 */         Class<?> type = anyMethod.getRawParameterType(0);
/* 308 */         if (type != String.class && type != Object.class) {
/* 309 */           throw new IllegalArgumentException(String.format("Invalid 'any-setter' annotation on method '%s()': first argument not of type String or Object, but %s", new Object[] { anyMethod
/*     */                   
/* 311 */                   .getName(), type.getName() }));
/*     */         }
/* 313 */         return anyMethod;
/*     */       } 
/* 315 */       AnnotatedMember anyField = this._propCollector.getAnySetterField();
/* 316 */       if (anyField != null) {
/*     */ 
/*     */         
/* 319 */         Class<?> type = anyField.getRawType();
/* 320 */         if (!Map.class.isAssignableFrom(type)) {
/* 321 */           throw new IllegalArgumentException(String.format("Invalid 'any-setter' annotation on field '%s': type is not instance of java.util.Map", new Object[] { anyField
/*     */                   
/* 323 */                   .getName() }));
/*     */         }
/* 325 */         return anyField;
/*     */       } 
/*     */     } 
/* 328 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<Object, AnnotatedMember> findInjectables() {
/* 333 */     if (this._propCollector != null) {
/* 334 */       return this._propCollector.getInjectables();
/*     */     }
/* 336 */     return Collections.emptyMap();
/*     */   }
/*     */ 
/*     */   
/*     */   public List<AnnotatedConstructor> getConstructors() {
/* 341 */     return this._classInfo.getConstructors();
/*     */   }
/*     */ 
/*     */   
/*     */   public Object instantiateBean(boolean fixAccess) {
/* 346 */     AnnotatedConstructor ac = this._classInfo.getDefaultConstructor();
/* 347 */     if (ac == null) {
/* 348 */       return null;
/*     */     }
/* 350 */     if (fixAccess) {
/* 351 */       ac.fixAccess(this._config.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS));
/*     */     }
/*     */     try {
/* 354 */       return ac.getAnnotated().newInstance(new Object[0]);
/* 355 */     } catch (Exception e) {
/* 356 */       Throwable t = e;
/* 357 */       while (t.getCause() != null) {
/* 358 */         t = t.getCause();
/*     */       }
/* 360 */       ClassUtil.throwIfError(t);
/* 361 */       ClassUtil.throwIfRTE(t);
/* 362 */       throw new IllegalArgumentException("Failed to instantiate bean of type " + this._classInfo
/* 363 */           .getAnnotated().getName() + ": (" + t.getClass().getName() + ") " + 
/* 364 */           ClassUtil.exceptionMessage(t), t);
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
/*     */   public AnnotatedMethod findMethod(String name, Class<?>[] paramTypes) {
/* 376 */     return this._classInfo.findMethod(name, paramTypes);
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
/*     */   public JsonFormat.Value findExpectedFormat(JsonFormat.Value defValue) {
/* 390 */     if (this._annotationIntrospector != null) {
/* 391 */       JsonFormat.Value value = this._annotationIntrospector.findFormat(this._classInfo);
/* 392 */       if (value != null) {
/* 393 */         if (defValue == null) {
/* 394 */           defValue = value;
/*     */         } else {
/* 396 */           defValue = defValue.withOverrides(value);
/*     */         } 
/*     */       }
/*     */     } 
/* 400 */     JsonFormat.Value v = this._config.getDefaultPropertyFormat(this._classInfo.getRawType());
/* 401 */     if (v != null) {
/* 402 */       if (defValue == null) {
/* 403 */         defValue = v;
/*     */       } else {
/* 405 */         defValue = defValue.withOverrides(v);
/*     */       } 
/*     */     }
/* 408 */     return defValue;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?>[] findDefaultViews() {
/* 414 */     if (!this._defaultViewsResolved) {
/* 415 */       this._defaultViewsResolved = true;
/*     */       
/* 417 */       Class<?>[] def = (this._annotationIntrospector == null) ? null : this._annotationIntrospector.findViews(this._classInfo);
/*     */       
/* 419 */       if (def == null && 
/* 420 */         !this._config.isEnabled(MapperFeature.DEFAULT_VIEW_INCLUSION)) {
/* 421 */         def = NO_VIEWS;
/*     */       }
/*     */       
/* 424 */       this._defaultViews = def;
/*     */     } 
/* 426 */     return this._defaultViews;
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
/*     */   public Converter<Object, Object> findSerializationConverter() {
/* 438 */     if (this._annotationIntrospector == null) {
/* 439 */       return null;
/*     */     }
/* 441 */     return _createConverter(this._annotationIntrospector.findSerializationConverter(this._classInfo));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonInclude.Value findPropertyInclusion(JsonInclude.Value defValue) {
/* 452 */     if (this._annotationIntrospector != null) {
/* 453 */       JsonInclude.Value incl = this._annotationIntrospector.findPropertyInclusion(this._classInfo);
/* 454 */       if (incl != null) {
/* 455 */         return (defValue == null) ? incl : defValue.withOverrides(incl);
/*     */       }
/*     */     } 
/* 458 */     return defValue;
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
/*     */   public AnnotatedMember findAnyGetter() throws IllegalArgumentException {
/* 471 */     AnnotatedMember anyGetter = (this._propCollector == null) ? null : this._propCollector.getAnyGetter();
/* 472 */     if (anyGetter != null) {
/*     */ 
/*     */ 
/*     */       
/* 476 */       Class<?> type = anyGetter.getRawType();
/* 477 */       if (!Map.class.isAssignableFrom(type)) {
/* 478 */         throw new IllegalArgumentException("Invalid 'any-getter' annotation on method " + anyGetter.getName() + "(): return type is not instance of java.util.Map");
/*     */       }
/*     */     } 
/* 481 */     return anyGetter;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<BeanPropertyDefinition> findBackReferences() {
/* 487 */     List<BeanPropertyDefinition> result = null;
/* 488 */     HashSet<String> names = null;
/* 489 */     for (BeanPropertyDefinition property : _properties()) {
/* 490 */       AnnotationIntrospector.ReferenceProperty refDef = property.findReferenceType();
/* 491 */       if (refDef == null || !refDef.isBackReference()) {
/*     */         continue;
/*     */       }
/* 494 */       String refName = refDef.getName();
/* 495 */       if (result == null) {
/* 496 */         result = new ArrayList<>();
/* 497 */         names = new HashSet<>();
/* 498 */         names.add(refName);
/*     */       }
/* 500 */       else if (!names.add(refName)) {
/* 501 */         throw new IllegalArgumentException("Multiple back-reference properties with name '" + refName + "'");
/*     */       } 
/*     */       
/* 504 */       result.add(property);
/*     */     } 
/* 506 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public Map<String, AnnotatedMember> findBackReferenceProperties() {
/* 513 */     List<BeanPropertyDefinition> props = findBackReferences();
/* 514 */     if (props == null) {
/* 515 */       return null;
/*     */     }
/* 517 */     Map<String, AnnotatedMember> result = new HashMap<>();
/* 518 */     for (BeanPropertyDefinition prop : props) {
/* 519 */       result.put(prop.getName(), prop.getMutator());
/*     */     }
/* 521 */     return result;
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
/*     */   public List<AnnotatedMethod> getFactoryMethods() {
/* 534 */     List<AnnotatedMethod> candidates = this._classInfo.getFactoryMethods();
/* 535 */     if (candidates.isEmpty()) {
/* 536 */       return candidates;
/*     */     }
/* 538 */     List<AnnotatedMethod> result = null;
/* 539 */     for (AnnotatedMethod am : candidates) {
/* 540 */       if (isFactoryMethod(am)) {
/* 541 */         if (result == null) {
/* 542 */           result = new ArrayList<>();
/*     */         }
/* 544 */         result.add(am);
/*     */       } 
/*     */     } 
/* 547 */     if (result == null) {
/* 548 */       return Collections.emptyList();
/*     */     }
/* 550 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Constructor<?> findSingleArgConstructor(Class<?>... argTypes) {
/* 556 */     for (AnnotatedConstructor ac : this._classInfo.getConstructors()) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 561 */       if (ac.getParameterCount() == 1) {
/* 562 */         Class<?> actArg = ac.getRawParameterType(0);
/* 563 */         for (Class<?> expArg : argTypes) {
/* 564 */           if (expArg == actArg) {
/* 565 */             return ac.getAnnotated();
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/* 570 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Method findFactoryMethod(Class<?>... expArgTypes) {
/* 577 */     for (AnnotatedMethod am : this._classInfo.getFactoryMethods()) {
/*     */       
/* 579 */       if (isFactoryMethod(am) && am.getParameterCount() == 1) {
/*     */         
/* 581 */         Class<?> actualArgType = am.getRawParameterType(0);
/* 582 */         for (Class<?> expArgType : expArgTypes) {
/*     */           
/* 584 */           if (actualArgType.isAssignableFrom(expArgType)) {
/* 585 */             return am.getAnnotated();
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/* 590 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isFactoryMethod(AnnotatedMethod am) {
/* 597 */     Class<?> rt = am.getRawReturnType();
/* 598 */     if (!getBeanClass().isAssignableFrom(rt)) {
/* 599 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 605 */     JsonCreator.Mode mode = this._annotationIntrospector.findCreatorAnnotation(this._config, am);
/* 606 */     if (mode != null && mode != JsonCreator.Mode.DISABLED) {
/* 607 */       return true;
/*     */     }
/* 609 */     String name = am.getName();
/*     */     
/* 611 */     if ("valueOf".equals(name) && 
/* 612 */       am.getParameterCount() == 1) {
/* 613 */       return true;
/*     */     }
/*     */ 
/*     */     
/* 617 */     if ("fromString".equals(name) && 
/* 618 */       am.getParameterCount() == 1) {
/* 619 */       Class<?> cls = am.getRawParameterType(0);
/* 620 */       if (cls == String.class || CharSequence.class.isAssignableFrom(cls)) {
/* 621 */         return true;
/*     */       }
/*     */     } 
/*     */     
/* 625 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected PropertyName _findCreatorPropertyName(AnnotatedParameter param) {
/* 634 */     PropertyName name = this._annotationIntrospector.findNameForDeserialization(param);
/* 635 */     if (name == null || name.isEmpty()) {
/* 636 */       String str = this._annotationIntrospector.findImplicitPropertyName(param);
/* 637 */       if (str != null && !str.isEmpty()) {
/* 638 */         name = PropertyName.construct(str);
/*     */       }
/*     */     } 
/* 641 */     return name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?> findPOJOBuilder() {
/* 652 */     return (this._annotationIntrospector == null) ? null : this._annotationIntrospector
/* 653 */       .findPOJOBuilder(this._classInfo);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonPOJOBuilder.Value findPOJOBuilderConfig() {
/* 659 */     return (this._annotationIntrospector == null) ? null : this._annotationIntrospector
/* 660 */       .findPOJOBuilderConfig(this._classInfo);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Converter<Object, Object> findDeserializationConverter() {
/* 666 */     if (this._annotationIntrospector == null) {
/* 667 */       return null;
/*     */     }
/* 669 */     return _createConverter(this._annotationIntrospector.findDeserializationConverter(this._classInfo));
/*     */   }
/*     */ 
/*     */   
/*     */   public String findClassDescription() {
/* 674 */     return (this._annotationIntrospector == null) ? null : this._annotationIntrospector
/* 675 */       .findClassDescription(this._classInfo);
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
/*     */   @Deprecated
/*     */   public LinkedHashMap<String, AnnotatedField> _findPropertyFields(Collection<String> ignoredProperties, boolean forSerialization) {
/* 700 */     LinkedHashMap<String, AnnotatedField> results = new LinkedHashMap<>();
/* 701 */     for (BeanPropertyDefinition property : _properties()) {
/* 702 */       AnnotatedField f = property.getField();
/* 703 */       if (f != null) {
/* 704 */         String name = property.getName();
/* 705 */         if (ignoredProperties != null && 
/* 706 */           ignoredProperties.contains(name)) {
/*     */           continue;
/*     */         }
/*     */         
/* 710 */         results.put(name, f);
/*     */       } 
/*     */     } 
/* 713 */     return results;
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
/*     */   protected Converter<Object, Object> _createConverter(Object converterDef) {
/* 725 */     if (converterDef == null) {
/* 726 */       return null;
/*     */     }
/* 728 */     if (converterDef instanceof Converter) {
/* 729 */       return (Converter<Object, Object>)converterDef;
/*     */     }
/* 731 */     if (!(converterDef instanceof Class)) {
/* 732 */       throw new IllegalStateException("AnnotationIntrospector returned Converter definition of type " + converterDef
/* 733 */           .getClass().getName() + "; expected type Converter or Class<Converter> instead");
/*     */     }
/* 735 */     Class<?> converterClass = (Class)converterDef;
/*     */     
/* 737 */     if (converterClass == Converter.None.class || ClassUtil.isBogusClass(converterClass)) {
/* 738 */       return null;
/*     */     }
/* 740 */     if (!Converter.class.isAssignableFrom(converterClass)) {
/* 741 */       throw new IllegalStateException("AnnotationIntrospector returned Class " + converterClass
/* 742 */           .getName() + "; expected Class<Converter>");
/*     */     }
/* 744 */     HandlerInstantiator hi = this._config.getHandlerInstantiator();
/* 745 */     Converter<?, ?> conv = (hi == null) ? null : hi.converterInstance(this._config, this._classInfo, converterClass);
/* 746 */     if (conv == null) {
/* 747 */       conv = (Converter<?, ?>)ClassUtil.createInstance(converterClass, this._config
/* 748 */           .canOverrideAccessModifiers());
/*     */     }
/* 750 */     return (Converter)conv;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\introspect\BasicBeanDescription.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */