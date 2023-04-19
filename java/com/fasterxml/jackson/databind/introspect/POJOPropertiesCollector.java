/*      */ package com.fasterxml.jackson.databind.introspect;
/*      */ 
/*      */ import com.fasterxml.jackson.annotation.JacksonInject;
/*      */ import com.fasterxml.jackson.annotation.JsonCreator;
/*      */ import com.fasterxml.jackson.annotation.JsonProperty;
/*      */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*      */ import com.fasterxml.jackson.databind.JavaType;
/*      */ import com.fasterxml.jackson.databind.MapperFeature;
/*      */ import com.fasterxml.jackson.databind.PropertyName;
/*      */ import com.fasterxml.jackson.databind.PropertyNamingStrategy;
/*      */ import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
/*      */ import com.fasterxml.jackson.databind.cfg.MapperConfig;
/*      */ import com.fasterxml.jackson.databind.util.BeanUtil;
/*      */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*      */ import java.lang.reflect.Modifier;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.TreeMap;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class POJOPropertiesCollector
/*      */ {
/*      */   protected final MapperConfig<?> _config;
/*      */   protected final boolean _forSerialization;
/*      */   protected final boolean _stdBeanNaming;
/*      */   protected final JavaType _type;
/*      */   protected final AnnotatedClass _classDef;
/*      */   protected final VisibilityChecker<?> _visibilityChecker;
/*      */   protected final AnnotationIntrospector _annotationIntrospector;
/*      */   protected final boolean _useAnnotations;
/*      */   protected final String _mutatorPrefix;
/*      */   protected boolean _collected;
/*      */   protected LinkedHashMap<String, POJOPropertyBuilder> _properties;
/*      */   protected LinkedList<POJOPropertyBuilder> _creatorProperties;
/*      */   protected LinkedList<AnnotatedMember> _anyGetters;
/*      */   protected LinkedList<AnnotatedMethod> _anySetters;
/*      */   protected LinkedList<AnnotatedMember> _anySetterField;
/*      */   protected LinkedList<AnnotatedMember> _jsonValueAccessors;
/*      */   protected HashSet<String> _ignoredPropertyNames;
/*      */   protected LinkedHashMap<Object, AnnotatedMember> _injectables;
/*      */   
/*      */   protected POJOPropertiesCollector(MapperConfig<?> config, boolean forSerialization, JavaType type, AnnotatedClass classDef, String mutatorPrefix) {
/*  129 */     this._config = config;
/*  130 */     this._stdBeanNaming = config.isEnabled(MapperFeature.USE_STD_BEAN_NAMING);
/*  131 */     this._forSerialization = forSerialization;
/*  132 */     this._type = type;
/*  133 */     this._classDef = classDef;
/*  134 */     this._mutatorPrefix = (mutatorPrefix == null) ? "set" : mutatorPrefix;
/*  135 */     if (config.isAnnotationProcessingEnabled()) {
/*  136 */       this._useAnnotations = true;
/*  137 */       this._annotationIntrospector = this._config.getAnnotationIntrospector();
/*      */     } else {
/*  139 */       this._useAnnotations = false;
/*  140 */       this._annotationIntrospector = AnnotationIntrospector.nopInstance();
/*      */     } 
/*  142 */     this._visibilityChecker = this._config.getDefaultVisibilityChecker(type.getRawClass(), classDef);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public MapperConfig<?> getConfig() {
/*  153 */     return this._config;
/*      */   }
/*      */   
/*      */   public JavaType getType() {
/*  157 */     return this._type;
/*      */   }
/*      */   
/*      */   public AnnotatedClass getClassDef() {
/*  161 */     return this._classDef;
/*      */   }
/*      */   
/*      */   public AnnotationIntrospector getAnnotationIntrospector() {
/*  165 */     return this._annotationIntrospector;
/*      */   }
/*      */ 
/*      */   
/*      */   public List<BeanPropertyDefinition> getProperties() {
/*  170 */     Map<String, POJOPropertyBuilder> props = getPropertyMap();
/*  171 */     return new ArrayList<>(props.values());
/*      */   }
/*      */   
/*      */   public Map<Object, AnnotatedMember> getInjectables() {
/*  175 */     if (!this._collected) {
/*  176 */       collectAll();
/*      */     }
/*  178 */     return this._injectables;
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   public AnnotatedMethod getJsonValueMethod() {
/*  183 */     AnnotatedMember m = getJsonValueAccessor();
/*  184 */     if (m instanceof AnnotatedMethod) {
/*  185 */       return (AnnotatedMethod)m;
/*      */     }
/*  187 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public AnnotatedMember getJsonValueAccessor() {
/*  195 */     if (!this._collected) {
/*  196 */       collectAll();
/*      */     }
/*      */     
/*  199 */     if (this._jsonValueAccessors != null) {
/*  200 */       if (this._jsonValueAccessors.size() > 1) {
/*  201 */         reportProblem("Multiple 'as-value' properties defined (%s vs %s)", new Object[] { this._jsonValueAccessors
/*  202 */               .get(0), this._jsonValueAccessors
/*  203 */               .get(1) });
/*      */       }
/*      */       
/*  206 */       return this._jsonValueAccessors.get(0);
/*      */     } 
/*  208 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public AnnotatedMember getAnyGetter() {
/*  213 */     if (!this._collected) {
/*  214 */       collectAll();
/*      */     }
/*  216 */     if (this._anyGetters != null) {
/*  217 */       if (this._anyGetters.size() > 1) {
/*  218 */         reportProblem("Multiple 'any-getters' defined (%s vs %s)", new Object[] { this._anyGetters
/*  219 */               .get(0), this._anyGetters.get(1) });
/*      */       }
/*  221 */       return this._anyGetters.getFirst();
/*      */     } 
/*  223 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public AnnotatedMember getAnySetterField() {
/*  228 */     if (!this._collected) {
/*  229 */       collectAll();
/*      */     }
/*  231 */     if (this._anySetterField != null) {
/*  232 */       if (this._anySetterField.size() > 1) {
/*  233 */         reportProblem("Multiple 'any-setter' fields defined (%s vs %s)", new Object[] { this._anySetterField
/*  234 */               .get(0), this._anySetterField.get(1) });
/*      */       }
/*  236 */       return this._anySetterField.getFirst();
/*      */     } 
/*  238 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public AnnotatedMethod getAnySetterMethod() {
/*  243 */     if (!this._collected) {
/*  244 */       collectAll();
/*      */     }
/*  246 */     if (this._anySetters != null) {
/*  247 */       if (this._anySetters.size() > 1) {
/*  248 */         reportProblem("Multiple 'any-setter' methods defined (%s vs %s)", new Object[] { this._anySetters
/*  249 */               .get(0), this._anySetters.get(1) });
/*      */       }
/*  251 */       return this._anySetters.getFirst();
/*      */     } 
/*  253 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Set<String> getIgnoredPropertyNames() {
/*  261 */     return this._ignoredPropertyNames;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectIdInfo getObjectIdInfo() {
/*  270 */     ObjectIdInfo info = this._annotationIntrospector.findObjectIdInfo(this._classDef);
/*  271 */     if (info != null) {
/*  272 */       info = this._annotationIntrospector.findObjectReferenceInfo(this._classDef, info);
/*      */     }
/*  274 */     return info;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Class<?> findPOJOBuilderClass() {
/*  281 */     return this._annotationIntrospector.findPOJOBuilder(this._classDef);
/*      */   }
/*      */ 
/*      */   
/*      */   protected Map<String, POJOPropertyBuilder> getPropertyMap() {
/*  286 */     if (!this._collected) {
/*  287 */       collectAll();
/*      */     }
/*  289 */     return this._properties;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void collectAll() {
/*  305 */     LinkedHashMap<String, POJOPropertyBuilder> props = new LinkedHashMap<>();
/*      */ 
/*      */     
/*  308 */     _addFields(props);
/*  309 */     _addMethods(props);
/*      */ 
/*      */     
/*  312 */     if (!this._classDef.isNonStaticInnerClass()) {
/*  313 */       _addCreators(props);
/*      */     }
/*  315 */     _addInjectables(props);
/*      */ 
/*      */ 
/*      */     
/*  319 */     _removeUnwantedProperties(props);
/*      */     
/*  321 */     _removeUnwantedAccessor(props);
/*      */ 
/*      */     
/*  324 */     _renameProperties(props);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  329 */     for (POJOPropertyBuilder property : props.values()) {
/*  330 */       property.mergeAnnotations(this._forSerialization);
/*      */     }
/*      */ 
/*      */     
/*  334 */     PropertyNamingStrategy naming = _findNamingStrategy();
/*  335 */     if (naming != null) {
/*  336 */       _renameUsing(props, naming);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  343 */     for (POJOPropertyBuilder property : props.values()) {
/*  344 */       property.trimByVisibility();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  350 */     if (this._config.isEnabled(MapperFeature.USE_WRAPPER_NAME_AS_PROPERTY_NAME)) {
/*  351 */       _renameWithWrappers(props);
/*      */     }
/*      */ 
/*      */     
/*  355 */     _sortProperties(props);
/*  356 */     this._properties = props;
/*  357 */     this._collected = true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void _addFields(Map<String, POJOPropertyBuilder> props) {
/*  371 */     AnnotationIntrospector ai = this._annotationIntrospector;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  376 */     boolean pruneFinalFields = (!this._forSerialization && !this._config.isEnabled(MapperFeature.ALLOW_FINAL_FIELDS_AS_MUTATORS));
/*  377 */     boolean transientAsIgnoral = this._config.isEnabled(MapperFeature.PROPAGATE_TRANSIENT_MARKER);
/*      */     
/*  379 */     for (AnnotatedField f : this._classDef.fields()) {
/*  380 */       PropertyName pn; String implName = ai.findImplicitPropertyName(f);
/*      */       
/*  382 */       if (Boolean.TRUE.equals(ai.hasAsValue(f))) {
/*  383 */         if (this._jsonValueAccessors == null) {
/*  384 */           this._jsonValueAccessors = new LinkedList<>();
/*      */         }
/*  386 */         this._jsonValueAccessors.add(f);
/*      */         
/*      */         continue;
/*      */       } 
/*  390 */       if (Boolean.TRUE.equals(ai.hasAnySetter(f))) {
/*  391 */         if (this._anySetterField == null) {
/*  392 */           this._anySetterField = new LinkedList<>();
/*      */         }
/*  394 */         this._anySetterField.add(f);
/*      */         continue;
/*      */       } 
/*  397 */       if (implName == null) {
/*  398 */         implName = f.getName();
/*      */       }
/*      */ 
/*      */       
/*  402 */       if (this._forSerialization) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  408 */         pn = ai.findNameForSerialization(f);
/*      */       } else {
/*  410 */         pn = ai.findNameForDeserialization(f);
/*      */       } 
/*  412 */       boolean hasName = (pn != null);
/*  413 */       boolean nameExplicit = hasName;
/*      */       
/*  415 */       if (nameExplicit && pn.isEmpty()) {
/*  416 */         pn = _propNameFromSimple(implName);
/*  417 */         nameExplicit = false;
/*      */       } 
/*      */       
/*  420 */       boolean visible = (pn != null);
/*  421 */       if (!visible) {
/*  422 */         visible = this._visibilityChecker.isFieldVisible(f);
/*      */       }
/*      */       
/*  425 */       boolean ignored = ai.hasIgnoreMarker(f);
/*      */ 
/*      */       
/*  428 */       if (f.isTransient())
/*      */       {
/*      */         
/*  431 */         if (!hasName) {
/*  432 */           visible = false;
/*  433 */           if (transientAsIgnoral) {
/*  434 */             ignored = true;
/*      */           }
/*      */         } 
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  443 */       if (pruneFinalFields && pn == null && !ignored && 
/*  444 */         Modifier.isFinal(f.getModifiers())) {
/*      */         continue;
/*      */       }
/*  447 */       _property(props, implName).addField(f, pn, nameExplicit, visible, ignored);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void _addCreators(Map<String, POJOPropertyBuilder> props) {
/*  457 */     if (!this._useAnnotations) {
/*      */       return;
/*      */     }
/*  460 */     for (AnnotatedConstructor ctor : this._classDef.getConstructors()) {
/*  461 */       if (this._creatorProperties == null) {
/*  462 */         this._creatorProperties = new LinkedList<>();
/*      */       }
/*  464 */       for (int i = 0, len = ctor.getParameterCount(); i < len; i++) {
/*  465 */         _addCreatorParam(props, ctor.getParameter(i));
/*      */       }
/*      */     } 
/*  468 */     for (AnnotatedMethod factory : this._classDef.getFactoryMethods()) {
/*  469 */       if (this._creatorProperties == null) {
/*  470 */         this._creatorProperties = new LinkedList<>();
/*      */       }
/*  472 */       for (int i = 0, len = factory.getParameterCount(); i < len; i++) {
/*  473 */         _addCreatorParam(props, factory.getParameter(i));
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
/*      */   protected void _addCreatorParam(Map<String, POJOPropertyBuilder> props, AnnotatedParameter param) {
/*  485 */     String impl = this._annotationIntrospector.findImplicitPropertyName(param);
/*  486 */     if (impl == null) {
/*  487 */       impl = "";
/*      */     }
/*  489 */     PropertyName pn = this._annotationIntrospector.findNameForDeserialization(param);
/*  490 */     boolean expl = (pn != null && !pn.isEmpty());
/*  491 */     if (!expl) {
/*  492 */       if (impl.isEmpty()) {
/*      */         return;
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*  498 */       JsonCreator.Mode creatorMode = this._annotationIntrospector.findCreatorAnnotation(this._config, param
/*  499 */           .getOwner());
/*  500 */       if (creatorMode == null || creatorMode == JsonCreator.Mode.DISABLED) {
/*      */         return;
/*      */       }
/*  503 */       pn = PropertyName.construct(impl);
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
/*  514 */     POJOPropertyBuilder prop = (expl && impl.isEmpty()) ? _property(props, pn) : _property(props, impl);
/*  515 */     prop.addCtor(param, pn, expl, true, false);
/*  516 */     this._creatorProperties.add(prop);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void _addMethods(Map<String, POJOPropertyBuilder> props) {
/*  524 */     AnnotationIntrospector ai = this._annotationIntrospector;
/*  525 */     for (AnnotatedMethod m : this._classDef.memberMethods()) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  531 */       int argCount = m.getParameterCount();
/*  532 */       if (argCount == 0) {
/*  533 */         _addGetterMethod(props, m, ai); continue;
/*  534 */       }  if (argCount == 1) {
/*  535 */         _addSetterMethod(props, m, ai); continue;
/*  536 */       }  if (argCount == 2 && 
/*  537 */         ai != null && 
/*  538 */         Boolean.TRUE.equals(ai.hasAnySetter(m))) {
/*  539 */         if (this._anySetters == null) {
/*  540 */           this._anySetters = new LinkedList<>();
/*      */         }
/*  542 */         this._anySetters.add(m);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void _addGetterMethod(Map<String, POJOPropertyBuilder> props, AnnotatedMethod m, AnnotationIntrospector ai) {
/*      */     String implName;
/*      */     boolean visible;
/*  553 */     if (!m.hasReturnType()) {
/*      */       return;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  559 */     if (Boolean.TRUE.equals(ai.hasAnyGetter(m))) {
/*  560 */       if (this._anyGetters == null) {
/*  561 */         this._anyGetters = new LinkedList<>();
/*      */       }
/*  563 */       this._anyGetters.add(m);
/*      */       
/*      */       return;
/*      */     } 
/*  567 */     if (Boolean.TRUE.equals(ai.hasAsValue(m))) {
/*  568 */       if (this._jsonValueAccessors == null) {
/*  569 */         this._jsonValueAccessors = new LinkedList<>();
/*      */       }
/*  571 */       this._jsonValueAccessors.add(m);
/*      */ 
/*      */       
/*      */       return;
/*      */     } 
/*      */     
/*  577 */     PropertyName pn = ai.findNameForSerialization(m);
/*  578 */     boolean nameExplicit = (pn != null);
/*      */     
/*  580 */     if (!nameExplicit) {
/*  581 */       implName = ai.findImplicitPropertyName(m);
/*  582 */       if (implName == null) {
/*  583 */         implName = BeanUtil.okNameForRegularGetter(m, m.getName(), this._stdBeanNaming);
/*      */       }
/*  585 */       if (implName == null) {
/*  586 */         implName = BeanUtil.okNameForIsGetter(m, m.getName(), this._stdBeanNaming);
/*  587 */         if (implName == null) {
/*      */           return;
/*      */         }
/*  590 */         visible = this._visibilityChecker.isIsGetterVisible(m);
/*      */       } else {
/*  592 */         visible = this._visibilityChecker.isGetterVisible(m);
/*      */       } 
/*      */     } else {
/*      */       
/*  596 */       implName = ai.findImplicitPropertyName(m);
/*  597 */       if (implName == null) {
/*  598 */         implName = BeanUtil.okNameForGetter(m, this._stdBeanNaming);
/*      */       }
/*      */       
/*  601 */       if (implName == null) {
/*  602 */         implName = m.getName();
/*      */       }
/*  604 */       if (pn.isEmpty()) {
/*      */         
/*  606 */         pn = _propNameFromSimple(implName);
/*  607 */         nameExplicit = false;
/*      */       } 
/*  609 */       visible = true;
/*      */     } 
/*  611 */     boolean ignore = ai.hasIgnoreMarker(m);
/*  612 */     _property(props, implName).addGetter(m, pn, nameExplicit, visible, ignore);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void _addSetterMethod(Map<String, POJOPropertyBuilder> props, AnnotatedMethod m, AnnotationIntrospector ai) {
/*      */     String implName;
/*      */     boolean visible;
/*  620 */     PropertyName pn = (ai == null) ? null : ai.findNameForDeserialization(m);
/*  621 */     boolean nameExplicit = (pn != null);
/*  622 */     if (!nameExplicit) {
/*  623 */       implName = (ai == null) ? null : ai.findImplicitPropertyName(m);
/*  624 */       if (implName == null) {
/*  625 */         implName = BeanUtil.okNameForMutator(m, this._mutatorPrefix, this._stdBeanNaming);
/*      */       }
/*  627 */       if (implName == null) {
/*      */         return;
/*      */       }
/*  630 */       visible = this._visibilityChecker.isSetterVisible(m);
/*      */     } else {
/*      */       
/*  633 */       implName = (ai == null) ? null : ai.findImplicitPropertyName(m);
/*  634 */       if (implName == null) {
/*  635 */         implName = BeanUtil.okNameForMutator(m, this._mutatorPrefix, this._stdBeanNaming);
/*      */       }
/*      */       
/*  638 */       if (implName == null) {
/*  639 */         implName = m.getName();
/*      */       }
/*  641 */       if (pn.isEmpty()) {
/*      */         
/*  643 */         pn = _propNameFromSimple(implName);
/*  644 */         nameExplicit = false;
/*      */       } 
/*  646 */       visible = true;
/*      */     } 
/*  648 */     boolean ignore = (ai == null) ? false : ai.hasIgnoreMarker(m);
/*  649 */     _property(props, implName).addSetter(m, pn, nameExplicit, visible, ignore);
/*      */   }
/*      */ 
/*      */   
/*      */   protected void _addInjectables(Map<String, POJOPropertyBuilder> props) {
/*  654 */     AnnotationIntrospector ai = this._annotationIntrospector;
/*      */     
/*  656 */     for (AnnotatedField f : this._classDef.fields()) {
/*  657 */       _doAddInjectable(ai.findInjectableValue(f), f);
/*      */     }
/*      */     
/*  660 */     for (AnnotatedMethod m : this._classDef.memberMethods()) {
/*      */       
/*  662 */       if (m.getParameterCount() != 1) {
/*      */         continue;
/*      */       }
/*  665 */       _doAddInjectable(ai.findInjectableValue(m), m);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected void _doAddInjectable(JacksonInject.Value injectable, AnnotatedMember m) {
/*  671 */     if (injectable == null) {
/*      */       return;
/*      */     }
/*  674 */     Object id = injectable.getId();
/*  675 */     if (this._injectables == null) {
/*  676 */       this._injectables = new LinkedHashMap<>();
/*      */     }
/*  678 */     AnnotatedMember prev = this._injectables.put(id, m);
/*  679 */     if (prev != null)
/*      */     {
/*  681 */       if (prev.getClass() == m.getClass()) {
/*  682 */         String type = id.getClass().getName();
/*  683 */         throw new IllegalArgumentException("Duplicate injectable value with id '" + 
/*  684 */             String.valueOf(id) + "' (of type " + type + ")");
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   private PropertyName _propNameFromSimple(String simpleName) {
/*  690 */     return PropertyName.construct(simpleName, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void _removeUnwantedProperties(Map<String, POJOPropertyBuilder> props) {
/*  705 */     Iterator<POJOPropertyBuilder> it = props.values().iterator();
/*  706 */     while (it.hasNext()) {
/*  707 */       POJOPropertyBuilder prop = it.next();
/*      */ 
/*      */       
/*  710 */       if (!prop.anyVisible()) {
/*  711 */         it.remove();
/*      */         
/*      */         continue;
/*      */       } 
/*  715 */       if (prop.anyIgnorals()) {
/*      */         
/*  717 */         if (!prop.isExplicitlyIncluded()) {
/*  718 */           it.remove();
/*  719 */           _collectIgnorals(prop.getName());
/*      */           
/*      */           continue;
/*      */         } 
/*  723 */         prop.removeIgnored();
/*  724 */         if (!prop.couldDeserialize()) {
/*  725 */           _collectIgnorals(prop.getName());
/*      */         }
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
/*      */   protected void _removeUnwantedAccessor(Map<String, POJOPropertyBuilder> props) {
/*  738 */     boolean inferMutators = this._config.isEnabled(MapperFeature.INFER_PROPERTY_MUTATORS);
/*  739 */     Iterator<POJOPropertyBuilder> it = props.values().iterator();
/*      */     
/*  741 */     while (it.hasNext()) {
/*  742 */       POJOPropertyBuilder prop = it.next();
/*      */       
/*  744 */       JsonProperty.Access acc = prop.removeNonVisible(inferMutators);
/*  745 */       if (acc == JsonProperty.Access.READ_ONLY) {
/*  746 */         _collectIgnorals(prop.getName());
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
/*      */   private void _collectIgnorals(String name) {
/*  758 */     if (!this._forSerialization) {
/*  759 */       if (this._ignoredPropertyNames == null) {
/*  760 */         this._ignoredPropertyNames = new HashSet<>();
/*      */       }
/*  762 */       this._ignoredPropertyNames.add(name);
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
/*      */   protected void _renameProperties(Map<String, POJOPropertyBuilder> props) {
/*  775 */     Iterator<Map.Entry<String, POJOPropertyBuilder>> it = props.entrySet().iterator();
/*  776 */     LinkedList<POJOPropertyBuilder> renamed = null;
/*  777 */     while (it.hasNext()) {
/*  778 */       Map.Entry<String, POJOPropertyBuilder> entry = it.next();
/*  779 */       POJOPropertyBuilder prop = entry.getValue();
/*      */       
/*  781 */       Collection<PropertyName> l = prop.findExplicitNames();
/*      */ 
/*      */       
/*  784 */       if (l.isEmpty()) {
/*      */         continue;
/*      */       }
/*  787 */       it.remove();
/*  788 */       if (renamed == null) {
/*  789 */         renamed = new LinkedList<>();
/*      */       }
/*      */       
/*  792 */       if (l.size() == 1) {
/*  793 */         PropertyName n = l.iterator().next();
/*  794 */         renamed.add(prop.withName(n));
/*      */         
/*      */         continue;
/*      */       } 
/*  798 */       renamed.addAll(prop.explode(l));
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
/*      */ 
/*      */     
/*  814 */     if (renamed != null) {
/*  815 */       for (POJOPropertyBuilder prop : renamed) {
/*  816 */         String name = prop.getName();
/*  817 */         POJOPropertyBuilder old = props.get(name);
/*  818 */         if (old == null) {
/*  819 */           props.put(name, prop);
/*      */         } else {
/*  821 */           old.addAll(prop);
/*      */         } 
/*      */         
/*  824 */         _updateCreatorProperty(prop, this._creatorProperties);
/*      */ 
/*      */ 
/*      */         
/*  828 */         if (this._ignoredPropertyNames != null) {
/*  829 */           this._ignoredPropertyNames.remove(name);
/*      */         }
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void _renameUsing(Map<String, POJOPropertyBuilder> propMap, PropertyNamingStrategy naming) {
/*  838 */     POJOPropertyBuilder[] props = (POJOPropertyBuilder[])propMap.values().toArray((Object[])new POJOPropertyBuilder[propMap.size()]);
/*  839 */     propMap.clear();
/*  840 */     for (POJOPropertyBuilder prop : props) {
/*  841 */       String simpleName; PropertyName fullName = prop.getFullName();
/*  842 */       String rename = null;
/*      */ 
/*      */       
/*  845 */       if (!prop.isExplicitlyNamed() || this._config.isEnabled(MapperFeature.ALLOW_EXPLICIT_PROPERTY_RENAMING)) {
/*  846 */         if (this._forSerialization) {
/*  847 */           if (prop.hasGetter()) {
/*  848 */             rename = naming.nameForGetterMethod(this._config, prop.getGetter(), fullName.getSimpleName());
/*  849 */           } else if (prop.hasField()) {
/*  850 */             rename = naming.nameForField(this._config, prop.getField(), fullName.getSimpleName());
/*      */           }
/*      */         
/*  853 */         } else if (prop.hasSetter()) {
/*  854 */           rename = naming.nameForSetterMethod(this._config, prop.getSetter(), fullName.getSimpleName());
/*  855 */         } else if (prop.hasConstructorParameter()) {
/*  856 */           rename = naming.nameForConstructorParameter(this._config, prop.getConstructorParameter(), fullName.getSimpleName());
/*  857 */         } else if (prop.hasField()) {
/*  858 */           rename = naming.nameForField(this._config, prop.getField(), fullName.getSimpleName());
/*  859 */         } else if (prop.hasGetter()) {
/*      */ 
/*      */ 
/*      */           
/*  863 */           rename = naming.nameForGetterMethod(this._config, prop.getGetter(), fullName.getSimpleName());
/*      */         } 
/*      */       }
/*      */ 
/*      */       
/*  868 */       if (rename != null && !fullName.hasSimpleName(rename)) {
/*  869 */         prop = prop.withSimpleName(rename);
/*  870 */         simpleName = rename;
/*      */       } else {
/*  872 */         simpleName = fullName.getSimpleName();
/*      */       } 
/*      */       
/*  875 */       POJOPropertyBuilder old = propMap.get(simpleName);
/*  876 */       if (old == null) {
/*  877 */         propMap.put(simpleName, prop);
/*      */       } else {
/*  879 */         old.addAll(prop);
/*      */       } 
/*      */ 
/*      */       
/*  883 */       _updateCreatorProperty(prop, this._creatorProperties);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void _renameWithWrappers(Map<String, POJOPropertyBuilder> props) {
/*  891 */     Iterator<Map.Entry<String, POJOPropertyBuilder>> it = props.entrySet().iterator();
/*  892 */     LinkedList<POJOPropertyBuilder> renamed = null;
/*  893 */     while (it.hasNext()) {
/*  894 */       Map.Entry<String, POJOPropertyBuilder> entry = it.next();
/*  895 */       POJOPropertyBuilder prop = entry.getValue();
/*  896 */       AnnotatedMember member = prop.getPrimaryMember();
/*  897 */       if (member == null) {
/*      */         continue;
/*      */       }
/*  900 */       PropertyName wrapperName = this._annotationIntrospector.findWrapperName(member);
/*      */ 
/*      */ 
/*      */       
/*  904 */       if (wrapperName == null || !wrapperName.hasSimpleName()) {
/*      */         continue;
/*      */       }
/*  907 */       if (!wrapperName.equals(prop.getFullName())) {
/*  908 */         if (renamed == null) {
/*  909 */           renamed = new LinkedList<>();
/*      */         }
/*  911 */         prop = prop.withName(wrapperName);
/*  912 */         renamed.add(prop);
/*  913 */         it.remove();
/*      */       } 
/*      */     } 
/*      */     
/*  917 */     if (renamed != null) {
/*  918 */       for (POJOPropertyBuilder prop : renamed) {
/*  919 */         String name = prop.getName();
/*  920 */         POJOPropertyBuilder old = props.get(name);
/*  921 */         if (old == null) {
/*  922 */           props.put(name, prop); continue;
/*      */         } 
/*  924 */         old.addAll(prop);
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
/*      */   protected void _sortProperties(Map<String, POJOPropertyBuilder> props) {
/*      */     boolean sort;
/*      */     Map<String, POJOPropertyBuilder> all;
/*  942 */     AnnotationIntrospector intr = this._annotationIntrospector;
/*  943 */     Boolean alpha = intr.findSerializationSortAlphabetically(this._classDef);
/*      */ 
/*      */     
/*  946 */     if (alpha == null) {
/*  947 */       sort = this._config.shouldSortPropertiesAlphabetically();
/*      */     } else {
/*  949 */       sort = alpha.booleanValue();
/*      */     } 
/*  951 */     String[] propertyOrder = intr.findSerializationPropertyOrder(this._classDef);
/*      */ 
/*      */     
/*  954 */     if (!sort && this._creatorProperties == null && propertyOrder == null) {
/*      */       return;
/*      */     }
/*  957 */     int size = props.size();
/*      */ 
/*      */     
/*  960 */     if (sort) {
/*  961 */       all = new TreeMap<>();
/*      */     } else {
/*  963 */       all = new LinkedHashMap<>(size + size);
/*      */     } 
/*      */     
/*  966 */     for (POJOPropertyBuilder prop : props.values()) {
/*  967 */       all.put(prop.getName(), prop);
/*      */     }
/*  969 */     Map<String, POJOPropertyBuilder> ordered = new LinkedHashMap<>(size + size);
/*      */     
/*  971 */     if (propertyOrder != null) {
/*  972 */       for (String name : propertyOrder) {
/*  973 */         POJOPropertyBuilder w = all.get(name);
/*  974 */         if (w == null) {
/*  975 */           for (POJOPropertyBuilder prop : props.values()) {
/*  976 */             if (name.equals(prop.getInternalName())) {
/*  977 */               w = prop;
/*      */               
/*  979 */               name = prop.getName();
/*      */               break;
/*      */             } 
/*      */           } 
/*      */         }
/*  984 */         if (w != null) {
/*  985 */           ordered.put(name, w);
/*      */         }
/*      */       } 
/*      */     }
/*      */     
/*  990 */     if (this._creatorProperties != null) {
/*      */       Collection<POJOPropertyBuilder> cr;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  997 */       if (sort) {
/*  998 */         TreeMap<String, POJOPropertyBuilder> sorted = new TreeMap<>();
/*      */         
/* 1000 */         for (POJOPropertyBuilder prop : this._creatorProperties) {
/* 1001 */           sorted.put(prop.getName(), prop);
/*      */         }
/* 1003 */         cr = sorted.values();
/*      */       } else {
/* 1005 */         cr = this._creatorProperties;
/*      */       } 
/* 1007 */       for (POJOPropertyBuilder prop : cr) {
/*      */ 
/*      */         
/* 1010 */         String name = prop.getName();
/* 1011 */         if (all.containsKey(name)) {
/* 1012 */           ordered.put(name, prop);
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/* 1017 */     ordered.putAll(all);
/* 1018 */     props.clear();
/* 1019 */     props.putAll(ordered);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void reportProblem(String msg, Object... args) {
/* 1029 */     if (args.length > 0) {
/* 1030 */       msg = String.format(msg, args);
/*      */     }
/* 1032 */     throw new IllegalArgumentException("Problem with definition of " + this._classDef + ": " + msg);
/*      */   }
/*      */ 
/*      */   
/*      */   protected POJOPropertyBuilder _property(Map<String, POJOPropertyBuilder> props, PropertyName name) {
/* 1037 */     String simpleName = name.getSimpleName();
/* 1038 */     POJOPropertyBuilder prop = props.get(simpleName);
/* 1039 */     if (prop == null) {
/* 1040 */       prop = new POJOPropertyBuilder(this._config, this._annotationIntrospector, this._forSerialization, name);
/*      */       
/* 1042 */       props.put(simpleName, prop);
/*      */     } 
/* 1044 */     return prop;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected POJOPropertyBuilder _property(Map<String, POJOPropertyBuilder> props, String implName) {
/* 1051 */     POJOPropertyBuilder prop = props.get(implName);
/* 1052 */     if (prop == null) {
/*      */       
/* 1054 */       prop = new POJOPropertyBuilder(this._config, this._annotationIntrospector, this._forSerialization, PropertyName.construct(implName));
/* 1055 */       props.put(implName, prop);
/*      */     } 
/* 1057 */     return prop;
/*      */   }
/*      */ 
/*      */   
/*      */   private PropertyNamingStrategy _findNamingStrategy() {
/* 1062 */     Object namingDef = this._annotationIntrospector.findNamingStrategy(this._classDef);
/* 1063 */     if (namingDef == null) {
/* 1064 */       return this._config.getPropertyNamingStrategy();
/*      */     }
/* 1066 */     if (namingDef instanceof PropertyNamingStrategy) {
/* 1067 */       return (PropertyNamingStrategy)namingDef;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1072 */     if (!(namingDef instanceof Class)) {
/* 1073 */       throw new IllegalStateException("AnnotationIntrospector returned PropertyNamingStrategy definition of type " + namingDef
/* 1074 */           .getClass().getName() + "; expected type PropertyNamingStrategy or Class<PropertyNamingStrategy> instead");
/*      */     }
/* 1076 */     Class<?> namingClass = (Class)namingDef;
/*      */     
/* 1078 */     if (namingClass == PropertyNamingStrategy.class) {
/* 1079 */       return null;
/*      */     }
/*      */     
/* 1082 */     if (!PropertyNamingStrategy.class.isAssignableFrom(namingClass)) {
/* 1083 */       throw new IllegalStateException("AnnotationIntrospector returned Class " + namingClass
/* 1084 */           .getName() + "; expected Class<PropertyNamingStrategy>");
/*      */     }
/* 1086 */     HandlerInstantiator hi = this._config.getHandlerInstantiator();
/* 1087 */     if (hi != null) {
/* 1088 */       PropertyNamingStrategy pns = hi.namingStrategyInstance(this._config, this._classDef, namingClass);
/* 1089 */       if (pns != null) {
/* 1090 */         return pns;
/*      */       }
/*      */     } 
/* 1093 */     return (PropertyNamingStrategy)ClassUtil.createInstance(namingClass, this._config
/* 1094 */         .canOverrideAccessModifiers());
/*      */   }
/*      */ 
/*      */   
/*      */   protected void _updateCreatorProperty(POJOPropertyBuilder prop, List<POJOPropertyBuilder> creatorProperties) {
/* 1099 */     if (creatorProperties != null) {
/* 1100 */       String intName = prop.getInternalName();
/* 1101 */       for (int i = 0, len = creatorProperties.size(); i < len; i++) {
/* 1102 */         if (((POJOPropertyBuilder)creatorProperties.get(i)).getInternalName().equals(intName)) {
/* 1103 */           creatorProperties.set(i, prop);
/*      */           break;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\introspect\POJOPropertiesCollector.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */