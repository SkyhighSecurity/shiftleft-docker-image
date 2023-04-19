/*      */ package com.fasterxml.jackson.databind.deser;
/*      */ import com.fasterxml.jackson.annotation.JsonFormat;
/*      */ import com.fasterxml.jackson.core.JsonParser;
/*      */ import com.fasterxml.jackson.core.JsonToken;
/*      */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*      */ import com.fasterxml.jackson.databind.BeanProperty;
/*      */ import com.fasterxml.jackson.databind.DeserializationContext;
/*      */ import com.fasterxml.jackson.databind.DeserializationFeature;
/*      */ import com.fasterxml.jackson.databind.JavaType;
/*      */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*      */ import com.fasterxml.jackson.databind.JsonMappingException;
/*      */ import com.fasterxml.jackson.databind.PropertyMetadata;
/*      */ import com.fasterxml.jackson.databind.PropertyName;
/*      */ import com.fasterxml.jackson.databind.deser.impl.BeanPropertyMap;
/*      */ import com.fasterxml.jackson.databind.deser.impl.ExternalTypeHandler;
/*      */ import com.fasterxml.jackson.databind.deser.impl.MergingSettableBeanProperty;
/*      */ import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader;
/*      */ import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
/*      */ import com.fasterxml.jackson.databind.deser.impl.UnwrappedPropertyHandler;
/*      */ import com.fasterxml.jackson.databind.deser.impl.ValueInjector;
/*      */ import com.fasterxml.jackson.databind.introspect.Annotated;
/*      */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*      */ import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
/*      */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*      */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*      */ import com.fasterxml.jackson.databind.util.NameTransformer;
/*      */ import com.fasterxml.jackson.databind.util.TokenBuffer;
/*      */ import java.io.IOException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.List;
/*      */ import java.util.Set;
/*      */ 
/*      */ public abstract class BeanDeserializerBase extends StdDeserializer<Object> implements ContextualDeserializer, ResolvableDeserializer, ValueInstantiator.Gettable, Serializable {
/*   34 */   protected static final PropertyName TEMP_PROPERTY_NAME = new PropertyName("#temporary-name");
/*      */ 
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
/*      */   
/*      */   protected final JavaType _beanType;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final JsonFormat.Shape _serializationShape;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final ValueInstantiator _valueInstantiator;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected JsonDeserializer<Object> _delegateDeserializer;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected JsonDeserializer<Object> _arrayDelegateDeserializer;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected PropertyBasedCreator _propertyBasedCreator;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean _nonStandardCreation;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean _vanillaProcessing;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final BeanPropertyMap _beanProperties;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final ValueInjector[] _injectables;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected SettableAnyProperty _anySetter;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final Set<String> _ignorableProps;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final boolean _ignoreAllUnknown;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final boolean _needViewProcesing;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final Map<String, SettableBeanProperty> _backRefs;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected transient HashMap<ClassKey, JsonDeserializer<Object>> _subDeserializers;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected UnwrappedPropertyHandler _unwrappedPropertyHandler;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected ExternalTypeHandler _externalTypeIdHandler;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final ObjectIdReader _objectIdReader;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected BeanDeserializerBase(BeanDeserializerBuilder builder, BeanDescription beanDesc, BeanPropertyMap properties, Map<String, SettableBeanProperty> backRefs, Set<String> ignorableProps, boolean ignoreAllUnknown, boolean hasViews) {
/*  201 */     super(beanDesc.getType());
/*  202 */     this._beanType = beanDesc.getType();
/*  203 */     this._valueInstantiator = builder.getValueInstantiator();
/*      */     
/*  205 */     this._beanProperties = properties;
/*  206 */     this._backRefs = backRefs;
/*  207 */     this._ignorableProps = ignorableProps;
/*  208 */     this._ignoreAllUnknown = ignoreAllUnknown;
/*      */     
/*  210 */     this._anySetter = builder.getAnySetter();
/*  211 */     List<ValueInjector> injectables = builder.getInjectables();
/*  212 */     this
/*  213 */       ._injectables = (injectables == null || injectables.isEmpty()) ? null : injectables.<ValueInjector>toArray(new ValueInjector[injectables.size()]);
/*  214 */     this._objectIdReader = builder.getObjectIdReader();
/*  215 */     this
/*      */ 
/*      */ 
/*      */       
/*  219 */       ._nonStandardCreation = (this._unwrappedPropertyHandler != null || this._valueInstantiator.canCreateUsingDelegate() || this._valueInstantiator.canCreateUsingArrayDelegate() || this._valueInstantiator.canCreateFromObjectWith() || !this._valueInstantiator.canCreateUsingDefault());
/*      */ 
/*      */ 
/*      */     
/*  223 */     JsonFormat.Value format = beanDesc.findExpectedFormat(null);
/*  224 */     this._serializationShape = (format == null) ? null : format.getShape();
/*      */     
/*  226 */     this._needViewProcesing = hasViews;
/*  227 */     this._vanillaProcessing = (!this._nonStandardCreation && this._injectables == null && !this._needViewProcesing && this._objectIdReader == null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected BeanDeserializerBase(BeanDeserializerBase src) {
/*  236 */     this(src, src._ignoreAllUnknown);
/*      */   }
/*      */ 
/*      */   
/*      */   protected BeanDeserializerBase(BeanDeserializerBase src, boolean ignoreAllUnknown) {
/*  241 */     super(src._beanType);
/*      */     
/*  243 */     this._beanType = src._beanType;
/*      */     
/*  245 */     this._valueInstantiator = src._valueInstantiator;
/*  246 */     this._delegateDeserializer = src._delegateDeserializer;
/*  247 */     this._propertyBasedCreator = src._propertyBasedCreator;
/*      */     
/*  249 */     this._beanProperties = src._beanProperties;
/*  250 */     this._backRefs = src._backRefs;
/*  251 */     this._ignorableProps = src._ignorableProps;
/*  252 */     this._ignoreAllUnknown = ignoreAllUnknown;
/*  253 */     this._anySetter = src._anySetter;
/*  254 */     this._injectables = src._injectables;
/*  255 */     this._objectIdReader = src._objectIdReader;
/*      */     
/*  257 */     this._nonStandardCreation = src._nonStandardCreation;
/*  258 */     this._unwrappedPropertyHandler = src._unwrappedPropertyHandler;
/*  259 */     this._needViewProcesing = src._needViewProcesing;
/*  260 */     this._serializationShape = src._serializationShape;
/*      */     
/*  262 */     this._vanillaProcessing = src._vanillaProcessing;
/*      */   }
/*      */ 
/*      */   
/*      */   protected BeanDeserializerBase(BeanDeserializerBase src, NameTransformer unwrapper) {
/*  267 */     super(src._beanType);
/*      */     
/*  269 */     this._beanType = src._beanType;
/*      */     
/*  271 */     this._valueInstantiator = src._valueInstantiator;
/*  272 */     this._delegateDeserializer = src._delegateDeserializer;
/*  273 */     this._propertyBasedCreator = src._propertyBasedCreator;
/*      */     
/*  275 */     this._backRefs = src._backRefs;
/*  276 */     this._ignorableProps = src._ignorableProps;
/*  277 */     this._ignoreAllUnknown = (unwrapper != null || src._ignoreAllUnknown);
/*  278 */     this._anySetter = src._anySetter;
/*  279 */     this._injectables = src._injectables;
/*  280 */     this._objectIdReader = src._objectIdReader;
/*      */     
/*  282 */     this._nonStandardCreation = src._nonStandardCreation;
/*  283 */     UnwrappedPropertyHandler uph = src._unwrappedPropertyHandler;
/*      */     
/*  285 */     if (unwrapper != null) {
/*      */       
/*  287 */       if (uph != null) {
/*  288 */         uph = uph.renameAll(unwrapper);
/*      */       }
/*      */       
/*  291 */       this._beanProperties = src._beanProperties.renameAll(unwrapper);
/*      */     } else {
/*  293 */       this._beanProperties = src._beanProperties;
/*      */     } 
/*  295 */     this._unwrappedPropertyHandler = uph;
/*  296 */     this._needViewProcesing = src._needViewProcesing;
/*  297 */     this._serializationShape = src._serializationShape;
/*      */ 
/*      */     
/*  300 */     this._vanillaProcessing = false;
/*      */   }
/*      */ 
/*      */   
/*      */   public BeanDeserializerBase(BeanDeserializerBase src, ObjectIdReader oir) {
/*  305 */     super(src._beanType);
/*  306 */     this._beanType = src._beanType;
/*      */     
/*  308 */     this._valueInstantiator = src._valueInstantiator;
/*  309 */     this._delegateDeserializer = src._delegateDeserializer;
/*  310 */     this._propertyBasedCreator = src._propertyBasedCreator;
/*      */     
/*  312 */     this._backRefs = src._backRefs;
/*  313 */     this._ignorableProps = src._ignorableProps;
/*  314 */     this._ignoreAllUnknown = src._ignoreAllUnknown;
/*  315 */     this._anySetter = src._anySetter;
/*  316 */     this._injectables = src._injectables;
/*      */     
/*  318 */     this._nonStandardCreation = src._nonStandardCreation;
/*  319 */     this._unwrappedPropertyHandler = src._unwrappedPropertyHandler;
/*  320 */     this._needViewProcesing = src._needViewProcesing;
/*  321 */     this._serializationShape = src._serializationShape;
/*      */ 
/*      */     
/*  324 */     this._objectIdReader = oir;
/*      */     
/*  326 */     if (oir == null) {
/*  327 */       this._beanProperties = src._beanProperties;
/*  328 */       this._vanillaProcessing = src._vanillaProcessing;
/*      */     
/*      */     }
/*      */     else {
/*      */ 
/*      */       
/*  334 */       ObjectIdValueProperty idProp = new ObjectIdValueProperty(oir, PropertyMetadata.STD_REQUIRED);
/*  335 */       this._beanProperties = src._beanProperties.withProperty((SettableBeanProperty)idProp);
/*  336 */       this._vanillaProcessing = false;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public BeanDeserializerBase(BeanDeserializerBase src, Set<String> ignorableProps) {
/*  342 */     super(src._beanType);
/*  343 */     this._beanType = src._beanType;
/*      */     
/*  345 */     this._valueInstantiator = src._valueInstantiator;
/*  346 */     this._delegateDeserializer = src._delegateDeserializer;
/*  347 */     this._propertyBasedCreator = src._propertyBasedCreator;
/*      */     
/*  349 */     this._backRefs = src._backRefs;
/*  350 */     this._ignorableProps = ignorableProps;
/*  351 */     this._ignoreAllUnknown = src._ignoreAllUnknown;
/*  352 */     this._anySetter = src._anySetter;
/*  353 */     this._injectables = src._injectables;
/*      */     
/*  355 */     this._nonStandardCreation = src._nonStandardCreation;
/*  356 */     this._unwrappedPropertyHandler = src._unwrappedPropertyHandler;
/*  357 */     this._needViewProcesing = src._needViewProcesing;
/*  358 */     this._serializationShape = src._serializationShape;
/*      */     
/*  360 */     this._vanillaProcessing = src._vanillaProcessing;
/*  361 */     this._objectIdReader = src._objectIdReader;
/*      */ 
/*      */ 
/*      */     
/*  365 */     this._beanProperties = src._beanProperties.withoutProperties(ignorableProps);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected BeanDeserializerBase(BeanDeserializerBase src, BeanPropertyMap beanProps) {
/*  373 */     super(src._beanType);
/*  374 */     this._beanType = src._beanType;
/*      */     
/*  376 */     this._valueInstantiator = src._valueInstantiator;
/*  377 */     this._delegateDeserializer = src._delegateDeserializer;
/*  378 */     this._propertyBasedCreator = src._propertyBasedCreator;
/*      */     
/*  380 */     this._beanProperties = beanProps;
/*  381 */     this._backRefs = src._backRefs;
/*  382 */     this._ignorableProps = src._ignorableProps;
/*  383 */     this._ignoreAllUnknown = src._ignoreAllUnknown;
/*  384 */     this._anySetter = src._anySetter;
/*  385 */     this._injectables = src._injectables;
/*  386 */     this._objectIdReader = src._objectIdReader;
/*      */     
/*  388 */     this._nonStandardCreation = src._nonStandardCreation;
/*  389 */     this._unwrappedPropertyHandler = src._unwrappedPropertyHandler;
/*  390 */     this._needViewProcesing = src._needViewProcesing;
/*  391 */     this._serializationShape = src._serializationShape;
/*      */     
/*  393 */     this._vanillaProcessing = src._vanillaProcessing;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract JsonDeserializer<Object> unwrappingDeserializer(NameTransformer paramNameTransformer);
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract BeanDeserializerBase withObjectIdReader(ObjectIdReader paramObjectIdReader);
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract BeanDeserializerBase withIgnorableProperties(Set<String> paramSet);
/*      */ 
/*      */   
/*      */   public BeanDeserializerBase withBeanProperties(BeanPropertyMap props) {
/*  410 */     throw new UnsupportedOperationException("Class " + getClass().getName() + " does not override `withBeanProperties()`, needs to");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected abstract BeanDeserializerBase asArrayDeserializer();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void resolve(DeserializationContext ctxt) throws JsonMappingException {
/*      */     SettableBeanProperty[] creatorProps;
/*  437 */     ExternalTypeHandler.Builder extTypes = null;
/*      */ 
/*      */ 
/*      */     
/*  441 */     if (this._valueInstantiator.canCreateFromObjectWith()) {
/*  442 */       creatorProps = this._valueInstantiator.getFromObjectArguments(ctxt.getConfig());
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  447 */       if (this._ignorableProps != null) {
/*  448 */         for (int i = 0, end = creatorProps.length; i < end; i++) {
/*  449 */           SettableBeanProperty prop = creatorProps[i];
/*  450 */           if (this._ignorableProps.contains(prop.getName())) {
/*  451 */             creatorProps[i].markAsIgnorable();
/*      */           }
/*      */         } 
/*      */       }
/*      */     } else {
/*  456 */       creatorProps = null;
/*      */     } 
/*  458 */     UnwrappedPropertyHandler unwrapped = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  468 */     for (SettableBeanProperty prop : this._beanProperties) {
/*  469 */       if (!prop.hasValueDeserializer()) {
/*      */         
/*  471 */         JsonDeserializer<?> deser = findConvertingDeserializer(ctxt, prop);
/*  472 */         if (deser == null) {
/*  473 */           deser = ctxt.findNonContextualValueDeserializer(prop.getType());
/*      */         }
/*  475 */         SettableBeanProperty newProp = prop.withValueDeserializer(deser);
/*  476 */         _replaceProperty(this._beanProperties, creatorProps, prop, newProp);
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  481 */     for (SettableBeanProperty origProp : this._beanProperties) {
/*  482 */       SettableBeanProperty prop = origProp;
/*  483 */       JsonDeserializer<?> deser = prop.getValueDeserializer();
/*  484 */       deser = ctxt.handlePrimaryContextualization(deser, (BeanProperty)prop, prop.getType());
/*  485 */       prop = prop.withValueDeserializer(deser);
/*      */       
/*  487 */       prop = _resolveManagedReferenceProperty(ctxt, prop);
/*      */ 
/*      */       
/*  490 */       if (!(prop instanceof ManagedReferenceProperty)) {
/*  491 */         prop = _resolvedObjectIdProperty(ctxt, prop);
/*      */       }
/*      */       
/*  494 */       NameTransformer xform = _findPropertyUnwrapper(ctxt, prop);
/*  495 */       if (xform != null) {
/*  496 */         JsonDeserializer<Object> orig = prop.getValueDeserializer();
/*  497 */         JsonDeserializer<Object> unwrapping = orig.unwrappingDeserializer(xform);
/*  498 */         if (unwrapping != orig && unwrapping != null) {
/*  499 */           prop = prop.withValueDeserializer(unwrapping);
/*  500 */           if (unwrapped == null) {
/*  501 */             unwrapped = new UnwrappedPropertyHandler();
/*      */           }
/*  503 */           unwrapped.addProperty(prop);
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  508 */           this._beanProperties.remove(prop);
/*      */ 
/*      */           
/*      */           continue;
/*      */         } 
/*      */       } 
/*      */       
/*  515 */       PropertyMetadata md = prop.getMetadata();
/*  516 */       prop = _resolveMergeAndNullSettings(ctxt, prop, md);
/*      */ 
/*      */       
/*  519 */       prop = _resolveInnerClassValuedProperty(ctxt, prop);
/*  520 */       if (prop != origProp) {
/*  521 */         _replaceProperty(this._beanProperties, creatorProps, origProp, prop);
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*  526 */       if (prop.hasValueTypeDeserializer()) {
/*  527 */         TypeDeserializer typeDeser = prop.getValueTypeDeserializer();
/*  528 */         if (typeDeser.getTypeInclusion() == JsonTypeInfo.As.EXTERNAL_PROPERTY) {
/*  529 */           if (extTypes == null) {
/*  530 */             extTypes = ExternalTypeHandler.builder(this._beanType);
/*      */           }
/*  532 */           extTypes.addExternal(prop, typeDeser);
/*      */           
/*  534 */           this._beanProperties.remove(prop);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  540 */     if (this._anySetter != null && !this._anySetter.hasValueDeserializer()) {
/*  541 */       this._anySetter = this._anySetter.withValueDeserializer(findDeserializer(ctxt, this._anySetter
/*  542 */             .getType(), this._anySetter.getProperty()));
/*      */     }
/*      */     
/*  545 */     if (this._valueInstantiator.canCreateUsingDelegate()) {
/*  546 */       JavaType delegateType = this._valueInstantiator.getDelegateType(ctxt.getConfig());
/*  547 */       if (delegateType == null) {
/*  548 */         ctxt.reportBadDefinition(this._beanType, String.format("Invalid delegate-creator definition for %s: value instantiator (%s) returned true for 'canCreateUsingDelegate()', but null for 'getDelegateType()'", new Object[] { this._beanType, this._valueInstantiator
/*      */                 
/*  550 */                 .getClass().getName() }));
/*      */       }
/*  552 */       this._delegateDeserializer = _findDelegateDeserializer(ctxt, delegateType, this._valueInstantiator
/*  553 */           .getDelegateCreator());
/*      */     } 
/*      */ 
/*      */     
/*  557 */     if (this._valueInstantiator.canCreateUsingArrayDelegate()) {
/*  558 */       JavaType delegateType = this._valueInstantiator.getArrayDelegateType(ctxt.getConfig());
/*  559 */       if (delegateType == null) {
/*  560 */         ctxt.reportBadDefinition(this._beanType, String.format("Invalid delegate-creator definition for %s: value instantiator (%s) returned true for 'canCreateUsingArrayDelegate()', but null for 'getArrayDelegateType()'", new Object[] { this._beanType, this._valueInstantiator
/*      */                 
/*  562 */                 .getClass().getName() }));
/*      */       }
/*  564 */       this._arrayDelegateDeserializer = _findDelegateDeserializer(ctxt, delegateType, this._valueInstantiator
/*  565 */           .getArrayDelegateCreator());
/*      */     } 
/*      */ 
/*      */     
/*  569 */     if (creatorProps != null) {
/*  570 */       this._propertyBasedCreator = PropertyBasedCreator.construct(ctxt, this._valueInstantiator, creatorProps, this._beanProperties);
/*      */     }
/*      */ 
/*      */     
/*  574 */     if (extTypes != null) {
/*      */ 
/*      */       
/*  577 */       this._externalTypeIdHandler = extTypes.build(this._beanProperties);
/*      */       
/*  579 */       this._nonStandardCreation = true;
/*      */     } 
/*      */     
/*  582 */     this._unwrappedPropertyHandler = unwrapped;
/*  583 */     if (unwrapped != null) {
/*  584 */       this._nonStandardCreation = true;
/*      */     }
/*      */     
/*  587 */     this._vanillaProcessing = (this._vanillaProcessing && !this._nonStandardCreation);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void _replaceProperty(BeanPropertyMap props, SettableBeanProperty[] creatorProps, SettableBeanProperty origProp, SettableBeanProperty newProp) {
/*  596 */     props.replace(origProp, newProp);
/*      */     
/*  598 */     if (creatorProps != null)
/*      */     {
/*      */       
/*  601 */       for (int i = 0, len = creatorProps.length; i < len; i++) {
/*  602 */         if (creatorProps[i] == origProp) {
/*  603 */           creatorProps[i] = newProp;
/*      */           return;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private JsonDeserializer<Object> _findDelegateDeserializer(DeserializationContext ctxt, JavaType delegateType, AnnotatedWithParams delegateCreator) throws JsonMappingException {
/*  625 */     BeanProperty.Std property = new BeanProperty.Std(TEMP_PROPERTY_NAME, delegateType, null, (AnnotatedMember)delegateCreator, PropertyMetadata.STD_OPTIONAL);
/*      */ 
/*      */     
/*  628 */     TypeDeserializer td = (TypeDeserializer)delegateType.getTypeHandler();
/*  629 */     if (td == null) {
/*  630 */       td = ctxt.getConfig().findTypeDeserializer(delegateType);
/*      */     }
/*      */ 
/*      */     
/*  634 */     JsonDeserializer<Object> dd = (JsonDeserializer<Object>)delegateType.getValueHandler();
/*  635 */     if (dd == null) {
/*  636 */       dd = findDeserializer(ctxt, delegateType, (BeanProperty)property);
/*      */     } else {
/*  638 */       dd = ctxt.handleSecondaryContextualization(dd, (BeanProperty)property, delegateType);
/*      */     } 
/*  640 */     if (td != null) {
/*  641 */       td = td.forProperty((BeanProperty)property);
/*  642 */       return (JsonDeserializer<Object>)new TypeWrappedDeserializer(td, dd);
/*      */     } 
/*  644 */     return dd;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected JsonDeserializer<Object> findConvertingDeserializer(DeserializationContext ctxt, SettableBeanProperty prop) throws JsonMappingException {
/*  661 */     AnnotationIntrospector intr = ctxt.getAnnotationIntrospector();
/*  662 */     if (intr != null) {
/*  663 */       Object convDef = intr.findDeserializationConverter((Annotated)prop.getMember());
/*  664 */       if (convDef != null) {
/*  665 */         Converter<Object, Object> conv = ctxt.converterInstance((Annotated)prop.getMember(), convDef);
/*  666 */         JavaType delegateType = conv.getInputType(ctxt.getTypeFactory());
/*      */ 
/*      */         
/*  669 */         JsonDeserializer<?> deser = ctxt.findNonContextualValueDeserializer(delegateType);
/*  670 */         return (JsonDeserializer<Object>)new StdDelegatingDeserializer(conv, delegateType, deser);
/*      */       } 
/*      */     } 
/*  673 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException {
/*  687 */     ObjectIdReader oir = this._objectIdReader;
/*      */ 
/*      */     
/*  690 */     AnnotationIntrospector intr = ctxt.getAnnotationIntrospector();
/*  691 */     AnnotatedMember accessor = _neitherNull(property, intr) ? property.getMember() : null;
/*  692 */     if (accessor != null) {
/*  693 */       ObjectIdInfo objectIdInfo = intr.findObjectIdInfo((Annotated)accessor);
/*  694 */       if (objectIdInfo != null) {
/*      */         JavaType idType; SettableBeanProperty idProp; ObjectIdGenerator<?> idGen;
/*  696 */         objectIdInfo = intr.findObjectReferenceInfo((Annotated)accessor, objectIdInfo);
/*      */         
/*  698 */         Class<?> implClass = objectIdInfo.getGeneratorType();
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  703 */         ObjectIdResolver resolver = ctxt.objectIdResolverInstance((Annotated)accessor, objectIdInfo);
/*  704 */         if (implClass == ObjectIdGenerators.PropertyGenerator.class) {
/*  705 */           PropertyName propName = objectIdInfo.getPropertyName();
/*  706 */           idProp = findProperty(propName);
/*  707 */           if (idProp == null)
/*  708 */             ctxt.reportBadDefinition(this._beanType, String.format("Invalid Object Id definition for %s: cannot find property with name '%s'", new Object[] {
/*      */                     
/*  710 */                     handledType().getName(), propName
/*      */                   })); 
/*  712 */           idType = idProp.getType();
/*  713 */           PropertyBasedObjectIdGenerator propertyBasedObjectIdGenerator = new PropertyBasedObjectIdGenerator(objectIdInfo.getScope());
/*      */         } else {
/*  715 */           JavaType type = ctxt.constructType(implClass);
/*  716 */           idType = ctxt.getTypeFactory().findTypeParameters(type, ObjectIdGenerator.class)[0];
/*  717 */           idProp = null;
/*  718 */           idGen = ctxt.objectIdGeneratorInstance((Annotated)accessor, objectIdInfo);
/*      */         } 
/*  720 */         JsonDeserializer<?> deser = ctxt.findRootValueDeserializer(idType);
/*  721 */         oir = ObjectIdReader.construct(idType, objectIdInfo.getPropertyName(), idGen, deser, idProp, resolver);
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  726 */     BeanDeserializerBase contextual = this;
/*  727 */     if (oir != null && oir != this._objectIdReader) {
/*  728 */       contextual = contextual.withObjectIdReader(oir);
/*      */     }
/*      */     
/*  731 */     if (accessor != null) {
/*  732 */       JsonIgnoreProperties.Value ignorals = intr.findPropertyIgnorals((Annotated)accessor);
/*  733 */       if (ignorals != null) {
/*  734 */         Set<String> ignored = ignorals.findIgnoredForDeserialization();
/*  735 */         if (!ignored.isEmpty()) {
/*  736 */           Set<String> prev = contextual._ignorableProps;
/*  737 */           if (prev != null && !prev.isEmpty()) {
/*  738 */             ignored = new HashSet<>(ignored);
/*  739 */             ignored.addAll(prev);
/*      */           } 
/*  741 */           contextual = contextual.withIgnorableProperties(ignored);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  747 */     JsonFormat.Value format = findFormatOverrides(ctxt, property, handledType());
/*  748 */     JsonFormat.Shape shape = null;
/*  749 */     if (format != null) {
/*  750 */       if (format.hasShape()) {
/*  751 */         shape = format.getShape();
/*      */       }
/*      */       
/*  754 */       Boolean B = format.getFeature(JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES);
/*  755 */       if (B != null) {
/*  756 */         BeanPropertyMap propsOrig = this._beanProperties;
/*  757 */         BeanPropertyMap props = propsOrig.withCaseInsensitivity(B.booleanValue());
/*  758 */         if (props != propsOrig) {
/*  759 */           contextual = contextual.withBeanProperties(props);
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/*  764 */     if (shape == null) {
/*  765 */       shape = this._serializationShape;
/*      */     }
/*  767 */     if (shape == JsonFormat.Shape.ARRAY) {
/*  768 */       contextual = contextual.asArrayDeserializer();
/*      */     }
/*  770 */     return (JsonDeserializer<?>)contextual;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected SettableBeanProperty _resolveManagedReferenceProperty(DeserializationContext ctxt, SettableBeanProperty prop) throws JsonMappingException {
/*  781 */     String refName = prop.getManagedReferenceName();
/*  782 */     if (refName == null) {
/*  783 */       return prop;
/*      */     }
/*  785 */     JsonDeserializer<?> valueDeser = prop.getValueDeserializer();
/*  786 */     SettableBeanProperty backProp = valueDeser.findBackReference(refName);
/*  787 */     if (backProp == null) {
/*  788 */       ctxt.reportBadDefinition(this._beanType, String.format("Cannot handle managed/back reference '%s': no back reference property found from type %s", new Object[] { refName, prop
/*      */               
/*  790 */               .getType() }));
/*      */     }
/*      */     
/*  793 */     JavaType referredType = this._beanType;
/*  794 */     JavaType backRefType = backProp.getType();
/*  795 */     boolean isContainer = prop.getType().isContainerType();
/*  796 */     if (!backRefType.getRawClass().isAssignableFrom(referredType.getRawClass())) {
/*  797 */       ctxt.reportBadDefinition(this._beanType, String.format("Cannot handle managed/back reference '%s': back reference type (%s) not compatible with managed type (%s)", new Object[] { refName, backRefType
/*      */               
/*  799 */               .getRawClass().getName(), referredType
/*  800 */               .getRawClass().getName() }));
/*      */     }
/*  802 */     return (SettableBeanProperty)new ManagedReferenceProperty(prop, refName, backProp, isContainer);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected SettableBeanProperty _resolvedObjectIdProperty(DeserializationContext ctxt, SettableBeanProperty prop) throws JsonMappingException {
/*  812 */     ObjectIdInfo objectIdInfo = prop.getObjectIdInfo();
/*  813 */     JsonDeserializer<Object> valueDeser = prop.getValueDeserializer();
/*  814 */     ObjectIdReader objectIdReader = (valueDeser == null) ? null : valueDeser.getObjectIdReader();
/*  815 */     if (objectIdInfo == null && objectIdReader == null) {
/*  816 */       return prop;
/*      */     }
/*  818 */     return (SettableBeanProperty)new ObjectIdReferenceProperty(prop, objectIdInfo);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected NameTransformer _findPropertyUnwrapper(DeserializationContext ctxt, SettableBeanProperty prop) throws JsonMappingException {
/*  829 */     AnnotatedMember am = prop.getMember();
/*  830 */     if (am != null) {
/*  831 */       NameTransformer unwrapper = ctxt.getAnnotationIntrospector().findUnwrappingNameTransformer(am);
/*  832 */       if (unwrapper != null) {
/*      */ 
/*      */         
/*  835 */         if (prop instanceof CreatorProperty) {
/*  836 */           ctxt.reportBadDefinition(getValueType(), String.format("Cannot define Creator property \"%s\" as `@JsonUnwrapped`: combination not yet supported", new Object[] { prop
/*      */                   
/*  838 */                   .getName() }));
/*      */         }
/*  840 */         return unwrapper;
/*      */       } 
/*      */     } 
/*  843 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected SettableBeanProperty _resolveInnerClassValuedProperty(DeserializationContext ctxt, SettableBeanProperty prop) {
/*  856 */     JsonDeserializer<Object> deser = prop.getValueDeserializer();
/*      */     
/*  858 */     if (deser instanceof BeanDeserializerBase) {
/*  859 */       BeanDeserializerBase bd = (BeanDeserializerBase)deser;
/*  860 */       ValueInstantiator vi = bd.getValueInstantiator();
/*  861 */       if (!vi.canCreateUsingDefault()) {
/*  862 */         Class<?> valueClass = prop.getType().getRawClass();
/*      */         
/*  864 */         Class<?> enclosing = ClassUtil.getOuterClass(valueClass);
/*      */         
/*  866 */         if (enclosing != null && enclosing == this._beanType.getRawClass()) {
/*  867 */           for (Constructor<?> ctor : valueClass.getConstructors()) {
/*  868 */             Class<?>[] paramTypes = ctor.getParameterTypes();
/*  869 */             if (paramTypes.length == 1 && 
/*  870 */               enclosing.equals(paramTypes[0])) {
/*  871 */               if (ctxt.canOverrideAccessModifiers()) {
/*  872 */                 ClassUtil.checkAndFixAccess(ctor, ctxt.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS));
/*      */               }
/*  874 */               return (SettableBeanProperty)new InnerClassProperty(prop, ctor);
/*      */             } 
/*      */           } 
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/*  881 */     return prop;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected SettableBeanProperty _resolveMergeAndNullSettings(DeserializationContext ctxt, SettableBeanProperty prop, PropertyMetadata propMetadata) throws JsonMappingException {
/*      */     MergingSettableBeanProperty mergingSettableBeanProperty;
/*      */     SettableBeanProperty settableBeanProperty;
/*  889 */     PropertyMetadata.MergeInfo merge = propMetadata.getMergeInfo();
/*      */     
/*  891 */     if (merge != null) {
/*  892 */       JsonDeserializer<?> valueDeser = prop.getValueDeserializer();
/*  893 */       Boolean mayMerge = valueDeser.supportsUpdate(ctxt.getConfig());
/*      */       
/*  895 */       if (mayMerge == null) {
/*      */         
/*  897 */         if (merge.fromDefaults) {
/*  898 */           return prop;
/*      */         }
/*  900 */       } else if (!mayMerge.booleanValue()) {
/*  901 */         if (!merge.fromDefaults)
/*      */         {
/*      */           
/*  904 */           ctxt.handleBadMerge(valueDeser);
/*      */         }
/*  906 */         return prop;
/*      */       } 
/*      */       
/*  909 */       AnnotatedMember accessor = merge.getter;
/*  910 */       accessor.fixAccess(ctxt.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS));
/*  911 */       if (!(prop instanceof com.fasterxml.jackson.databind.deser.impl.SetterlessProperty)) {
/*  912 */         mergingSettableBeanProperty = MergingSettableBeanProperty.construct(prop, accessor);
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/*  917 */     NullValueProvider nuller = findValueNullProvider(ctxt, (SettableBeanProperty)mergingSettableBeanProperty, propMetadata);
/*  918 */     if (nuller != null) {
/*  919 */       settableBeanProperty = mergingSettableBeanProperty.withNullProvider(nuller);
/*      */     }
/*  921 */     return settableBeanProperty;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public AccessPattern getNullAccessPattern() {
/*  933 */     return AccessPattern.ALWAYS_NULL;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public AccessPattern getEmptyAccessPattern() {
/*  939 */     return AccessPattern.DYNAMIC;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Object getEmptyValue(DeserializationContext ctxt) throws JsonMappingException {
/*      */     try {
/*  946 */       return this._valueInstantiator.createUsingDefault(ctxt);
/*  947 */     } catch (IOException e) {
/*  948 */       return ClassUtil.throwAsMappingException(ctxt, e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isCachable() {
/*  959 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Boolean supportsUpdate(DeserializationConfig config) {
/*  966 */     return Boolean.TRUE;
/*      */   }
/*      */ 
/*      */   
/*      */   public Class<?> handledType() {
/*  971 */     return this._beanType.getRawClass();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectIdReader getObjectIdReader() {
/*  981 */     return this._objectIdReader;
/*      */   }
/*      */   
/*      */   public boolean hasProperty(String propertyName) {
/*  985 */     return (this._beanProperties.find(propertyName) != null);
/*      */   }
/*      */   
/*      */   public boolean hasViews() {
/*  989 */     return this._needViewProcesing;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getPropertyCount() {
/*  996 */     return this._beanProperties.size();
/*      */   }
/*      */ 
/*      */   
/*      */   public Collection<Object> getKnownPropertyNames() {
/* 1001 */     ArrayList<Object> names = new ArrayList();
/* 1002 */     for (SettableBeanProperty prop : this._beanProperties) {
/* 1003 */       names.add(prop.getName());
/*      */     }
/* 1005 */     return names;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public final Class<?> getBeanClass() {
/* 1012 */     return this._beanType.getRawClass();
/*      */   }
/*      */   public JavaType getValueType() {
/* 1015 */     return this._beanType;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Iterator<SettableBeanProperty> properties() {
/* 1026 */     if (this._beanProperties == null) {
/* 1027 */       throw new IllegalStateException("Can only call after BeanDeserializer has been resolved");
/*      */     }
/* 1029 */     return this._beanProperties.iterator();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Iterator<SettableBeanProperty> creatorProperties() {
/* 1041 */     if (this._propertyBasedCreator == null) {
/* 1042 */       return Collections.<SettableBeanProperty>emptyList().iterator();
/*      */     }
/* 1044 */     return this._propertyBasedCreator.properties().iterator();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public SettableBeanProperty findProperty(PropertyName propertyName) {
/* 1050 */     return findProperty(propertyName.getSimpleName());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public SettableBeanProperty findProperty(String propertyName) {
/* 1063 */     SettableBeanProperty prop = (this._beanProperties == null) ? null : this._beanProperties.find(propertyName);
/* 1064 */     if (prop == null && this._propertyBasedCreator != null) {
/* 1065 */       prop = this._propertyBasedCreator.findCreatorProperty(propertyName);
/*      */     }
/* 1067 */     return prop;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public SettableBeanProperty findProperty(int propertyIndex) {
/* 1083 */     SettableBeanProperty prop = (this._beanProperties == null) ? null : this._beanProperties.find(propertyIndex);
/* 1084 */     if (prop == null && this._propertyBasedCreator != null) {
/* 1085 */       prop = this._propertyBasedCreator.findCreatorProperty(propertyIndex);
/*      */     }
/* 1087 */     return prop;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public SettableBeanProperty findBackReference(String logicalName) {
/* 1097 */     if (this._backRefs == null) {
/* 1098 */       return null;
/*      */     }
/* 1100 */     return this._backRefs.get(logicalName);
/*      */   }
/*      */ 
/*      */   
/*      */   public ValueInstantiator getValueInstantiator() {
/* 1105 */     return this._valueInstantiator;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void replaceProperty(SettableBeanProperty original, SettableBeanProperty replacement) {
/* 1129 */     this._beanProperties.replace(original, replacement);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract Object deserializeFromObject(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext) throws IOException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException {
/* 1151 */     if (this._objectIdReader != null) {
/*      */       
/* 1153 */       if (p.canReadObjectId()) {
/* 1154 */         Object id = p.getObjectId();
/* 1155 */         if (id != null) {
/* 1156 */           Object ob = typeDeserializer.deserializeTypedFromObject(p, ctxt);
/* 1157 */           return _handleTypedObjectId(p, ctxt, ob, id);
/*      */         } 
/*      */       } 
/*      */       
/* 1161 */       JsonToken t = p.getCurrentToken();
/* 1162 */       if (t != null) {
/*      */         
/* 1164 */         if (t.isScalarValue()) {
/* 1165 */           return deserializeFromObjectId(p, ctxt);
/*      */         }
/*      */         
/* 1168 */         if (t == JsonToken.START_OBJECT) {
/* 1169 */           t = p.nextToken();
/*      */         }
/* 1171 */         if (t == JsonToken.FIELD_NAME && this._objectIdReader.maySerializeAsObject() && this._objectIdReader
/* 1172 */           .isValidReferencePropertyName(p.getCurrentName(), p)) {
/* 1173 */           return deserializeFromObjectId(p, ctxt);
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/* 1178 */     return typeDeserializer.deserializeTypedFromObject(p, ctxt);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Object _handleTypedObjectId(JsonParser p, DeserializationContext ctxt, Object pojo, Object rawId) throws IOException {
/*      */     Object id;
/* 1193 */     JsonDeserializer<Object> idDeser = this._objectIdReader.getDeserializer();
/*      */ 
/*      */ 
/*      */     
/* 1197 */     if (idDeser.handledType() == rawId.getClass()) {
/*      */       
/* 1199 */       id = rawId;
/*      */     } else {
/* 1201 */       id = _convertObjectId(p, ctxt, rawId, idDeser);
/*      */     } 
/*      */     
/* 1204 */     ReadableObjectId roid = ctxt.findObjectId(id, this._objectIdReader.generator, this._objectIdReader.resolver);
/* 1205 */     roid.bindItem(pojo);
/*      */     
/* 1207 */     SettableBeanProperty idProp = this._objectIdReader.idProperty;
/* 1208 */     if (idProp != null) {
/* 1209 */       return idProp.setAndReturn(pojo, id);
/*      */     }
/* 1211 */     return pojo;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Object _convertObjectId(JsonParser p, DeserializationContext ctxt, Object rawId, JsonDeserializer<Object> idDeser) throws IOException {
/* 1227 */     TokenBuffer buf = new TokenBuffer(p, ctxt);
/* 1228 */     if (rawId instanceof String) {
/* 1229 */       buf.writeString((String)rawId);
/* 1230 */     } else if (rawId instanceof Long) {
/* 1231 */       buf.writeNumber(((Long)rawId).longValue());
/* 1232 */     } else if (rawId instanceof Integer) {
/* 1233 */       buf.writeNumber(((Integer)rawId).intValue());
/*      */ 
/*      */     
/*      */     }
/*      */     else {
/*      */ 
/*      */       
/* 1240 */       buf.writeObject(rawId);
/*      */     } 
/* 1242 */     JsonParser bufParser = buf.asParser();
/* 1243 */     bufParser.nextToken();
/* 1244 */     return idDeser.deserialize(bufParser, ctxt);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Object deserializeWithObjectId(JsonParser p, DeserializationContext ctxt) throws IOException {
/* 1257 */     return deserializeFromObject(p, ctxt);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Object deserializeFromObjectId(JsonParser p, DeserializationContext ctxt) throws IOException {
/* 1266 */     Object id = this._objectIdReader.readObjectReference(p, ctxt);
/* 1267 */     ReadableObjectId roid = ctxt.findObjectId(id, this._objectIdReader.generator, this._objectIdReader.resolver);
/*      */     
/* 1269 */     Object pojo = roid.resolve();
/* 1270 */     if (pojo == null) {
/* 1271 */       throw new UnresolvedForwardReference(p, "Could not resolve Object Id [" + id + "] (for " + this._beanType + ").", p
/*      */           
/* 1273 */           .getCurrentLocation(), roid);
/*      */     }
/* 1275 */     return pojo;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected Object deserializeFromObjectUsingNonDefault(JsonParser p, DeserializationContext ctxt) throws IOException {
/* 1281 */     JsonDeserializer<Object> delegateDeser = _delegateDeserializer();
/* 1282 */     if (delegateDeser != null) {
/* 1283 */       return this._valueInstantiator.createUsingDelegate(ctxt, delegateDeser
/* 1284 */           .deserialize(p, ctxt));
/*      */     }
/* 1286 */     if (this._propertyBasedCreator != null) {
/* 1287 */       return _deserializeUsingPropertyBased(p, ctxt);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1292 */     Class<?> raw = this._beanType.getRawClass();
/* 1293 */     if (ClassUtil.isNonStaticInnerClass(raw)) {
/* 1294 */       return ctxt.handleMissingInstantiator(raw, null, p, "can only instantiate non-static inner class by using default, no-argument constructor", new Object[0]);
/*      */     }
/*      */     
/* 1297 */     return ctxt.handleMissingInstantiator(raw, getValueInstantiator(), p, "cannot deserialize from Object value (no delegate- or property-based Creator)", new Object[0]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected abstract Object _deserializeUsingPropertyBased(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext) throws IOException;
/*      */ 
/*      */ 
/*      */   
/*      */   public Object deserializeFromNumber(JsonParser p, DeserializationContext ctxt) throws IOException {
/* 1308 */     if (this._objectIdReader != null) {
/* 1309 */       return deserializeFromObjectId(p, ctxt);
/*      */     }
/* 1311 */     JsonDeserializer<Object> delegateDeser = _delegateDeserializer();
/* 1312 */     JsonParser.NumberType nt = p.getNumberType();
/* 1313 */     if (nt == JsonParser.NumberType.INT) {
/* 1314 */       if (delegateDeser != null && 
/* 1315 */         !this._valueInstantiator.canCreateFromInt()) {
/* 1316 */         Object bean = this._valueInstantiator.createUsingDelegate(ctxt, delegateDeser
/* 1317 */             .deserialize(p, ctxt));
/* 1318 */         if (this._injectables != null) {
/* 1319 */           injectValues(ctxt, bean);
/*      */         }
/* 1321 */         return bean;
/*      */       } 
/*      */       
/* 1324 */       return this._valueInstantiator.createFromInt(ctxt, p.getIntValue());
/*      */     } 
/* 1326 */     if (nt == JsonParser.NumberType.LONG) {
/* 1327 */       if (delegateDeser != null && 
/* 1328 */         !this._valueInstantiator.canCreateFromInt()) {
/* 1329 */         Object bean = this._valueInstantiator.createUsingDelegate(ctxt, delegateDeser
/* 1330 */             .deserialize(p, ctxt));
/* 1331 */         if (this._injectables != null) {
/* 1332 */           injectValues(ctxt, bean);
/*      */         }
/* 1334 */         return bean;
/*      */       } 
/*      */       
/* 1337 */       return this._valueInstantiator.createFromLong(ctxt, p.getLongValue());
/*      */     } 
/*      */     
/* 1340 */     if (delegateDeser != null) {
/* 1341 */       Object bean = this._valueInstantiator.createUsingDelegate(ctxt, delegateDeser
/* 1342 */           .deserialize(p, ctxt));
/* 1343 */       if (this._injectables != null) {
/* 1344 */         injectValues(ctxt, bean);
/*      */       }
/* 1346 */       return bean;
/*      */     } 
/* 1348 */     return ctxt.handleMissingInstantiator(handledType(), getValueInstantiator(), p, "no suitable creator method found to deserialize from Number value (%s)", new Object[] { p
/*      */           
/* 1350 */           .getNumberValue() });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object deserializeFromString(JsonParser p, DeserializationContext ctxt) throws IOException {
/* 1357 */     if (this._objectIdReader != null) {
/* 1358 */       return deserializeFromObjectId(p, ctxt);
/*      */     }
/*      */ 
/*      */     
/* 1362 */     JsonDeserializer<Object> delegateDeser = _delegateDeserializer();
/* 1363 */     if (delegateDeser != null && 
/* 1364 */       !this._valueInstantiator.canCreateFromString()) {
/* 1365 */       Object bean = this._valueInstantiator.createUsingDelegate(ctxt, delegateDeser
/* 1366 */           .deserialize(p, ctxt));
/* 1367 */       if (this._injectables != null) {
/* 1368 */         injectValues(ctxt, bean);
/*      */       }
/* 1370 */       return bean;
/*      */     } 
/*      */     
/* 1373 */     return this._valueInstantiator.createFromString(ctxt, p.getText());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object deserializeFromDouble(JsonParser p, DeserializationContext ctxt) throws IOException {
/* 1382 */     JsonParser.NumberType t = p.getNumberType();
/*      */     
/* 1384 */     if (t == JsonParser.NumberType.DOUBLE || t == JsonParser.NumberType.FLOAT) {
/* 1385 */       JsonDeserializer<Object> jsonDeserializer = _delegateDeserializer();
/* 1386 */       if (jsonDeserializer != null && 
/* 1387 */         !this._valueInstantiator.canCreateFromDouble()) {
/* 1388 */         Object bean = this._valueInstantiator.createUsingDelegate(ctxt, jsonDeserializer
/* 1389 */             .deserialize(p, ctxt));
/* 1390 */         if (this._injectables != null) {
/* 1391 */           injectValues(ctxt, bean);
/*      */         }
/* 1393 */         return bean;
/*      */       } 
/*      */       
/* 1396 */       return this._valueInstantiator.createFromDouble(ctxt, p.getDoubleValue());
/*      */     } 
/*      */     
/* 1399 */     JsonDeserializer<Object> delegateDeser = _delegateDeserializer();
/* 1400 */     if (delegateDeser != null) {
/* 1401 */       return this._valueInstantiator.createUsingDelegate(ctxt, delegateDeser
/* 1402 */           .deserialize(p, ctxt));
/*      */     }
/* 1404 */     return ctxt.handleMissingInstantiator(handledType(), getValueInstantiator(), p, "no suitable creator method found to deserialize from Number value (%s)", new Object[] { p
/*      */           
/* 1406 */           .getNumberValue() });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object deserializeFromBoolean(JsonParser p, DeserializationContext ctxt) throws IOException {
/* 1414 */     JsonDeserializer<Object> delegateDeser = _delegateDeserializer();
/* 1415 */     if (delegateDeser != null && 
/* 1416 */       !this._valueInstantiator.canCreateFromBoolean()) {
/* 1417 */       Object bean = this._valueInstantiator.createUsingDelegate(ctxt, delegateDeser
/* 1418 */           .deserialize(p, ctxt));
/* 1419 */       if (this._injectables != null) {
/* 1420 */         injectValues(ctxt, bean);
/*      */       }
/* 1422 */       return bean;
/*      */     } 
/*      */     
/* 1425 */     boolean value = (p.getCurrentToken() == JsonToken.VALUE_TRUE);
/* 1426 */     return this._valueInstantiator.createFromBoolean(ctxt, value);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Object deserializeFromArray(JsonParser p, DeserializationContext ctxt) throws IOException {
/* 1432 */     JsonDeserializer<Object> delegateDeser = this._arrayDelegateDeserializer;
/*      */     
/* 1434 */     if (delegateDeser != null || (delegateDeser = this._delegateDeserializer) != null) {
/* 1435 */       Object bean = this._valueInstantiator.createUsingArrayDelegate(ctxt, delegateDeser
/* 1436 */           .deserialize(p, ctxt));
/* 1437 */       if (this._injectables != null) {
/* 1438 */         injectValues(ctxt, bean);
/*      */       }
/* 1440 */       return bean;
/*      */     } 
/* 1442 */     if (ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS)) {
/* 1443 */       JsonToken t = p.nextToken();
/* 1444 */       if (t == JsonToken.END_ARRAY && ctxt.isEnabled(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT)) {
/* 1445 */         return null;
/*      */       }
/* 1447 */       Object value = deserialize(p, ctxt);
/* 1448 */       if (p.nextToken() != JsonToken.END_ARRAY) {
/* 1449 */         handleMissingEndArrayForSingle(p, ctxt);
/*      */       }
/* 1451 */       return value;
/*      */     } 
/* 1453 */     if (ctxt.isEnabled(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT)) {
/* 1454 */       JsonToken t = p.nextToken();
/* 1455 */       if (t == JsonToken.END_ARRAY) {
/* 1456 */         return null;
/*      */       }
/* 1458 */       return ctxt.handleUnexpectedToken(getValueType(ctxt), JsonToken.START_ARRAY, p, null, new Object[0]);
/*      */     } 
/* 1460 */     return ctxt.handleUnexpectedToken(getValueType(ctxt), p);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object deserializeFromEmbedded(JsonParser p, DeserializationContext ctxt) throws IOException {
/* 1468 */     if (this._objectIdReader != null) {
/* 1469 */       return deserializeFromObjectId(p, ctxt);
/*      */     }
/*      */     
/* 1472 */     JsonDeserializer<Object> delegateDeser = _delegateDeserializer();
/* 1473 */     if (delegateDeser != null && 
/* 1474 */       !this._valueInstantiator.canCreateFromString()) {
/* 1475 */       Object bean = this._valueInstantiator.createUsingDelegate(ctxt, delegateDeser
/* 1476 */           .deserialize(p, ctxt));
/* 1477 */       if (this._injectables != null) {
/* 1478 */         injectValues(ctxt, bean);
/*      */       }
/* 1480 */       return bean;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1488 */     Object value = p.getEmbeddedObject();
/* 1489 */     if (value != null && 
/* 1490 */       !this._beanType.isTypeOrSuperTypeOf(value.getClass()))
/*      */     {
/* 1492 */       value = ctxt.handleWeirdNativeValue(this._beanType, value, p);
/*      */     }
/*      */     
/* 1495 */     return value;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final JsonDeserializer<Object> _delegateDeserializer() {
/* 1502 */     JsonDeserializer<Object> deser = this._delegateDeserializer;
/* 1503 */     if (deser == null) {
/* 1504 */       deser = this._arrayDelegateDeserializer;
/*      */     }
/* 1506 */     return deser;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void injectValues(DeserializationContext ctxt, Object bean) throws IOException {
/* 1518 */     for (ValueInjector injector : this._injectables) {
/* 1519 */       injector.inject(ctxt, bean);
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
/*      */   protected Object handleUnknownProperties(DeserializationContext ctxt, Object bean, TokenBuffer unknownTokens) throws IOException {
/* 1534 */     unknownTokens.writeEndObject();
/*      */ 
/*      */     
/* 1537 */     JsonParser bufferParser = unknownTokens.asParser();
/* 1538 */     while (bufferParser.nextToken() != JsonToken.END_OBJECT) {
/* 1539 */       String propName = bufferParser.getCurrentName();
/*      */       
/* 1541 */       bufferParser.nextToken();
/* 1542 */       handleUnknownProperty(bufferParser, ctxt, bean, propName);
/*      */     } 
/* 1544 */     return bean;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void handleUnknownVanilla(JsonParser p, DeserializationContext ctxt, Object beanOrBuilder, String propName) throws IOException {
/* 1559 */     if (this._ignorableProps != null && this._ignorableProps.contains(propName)) {
/* 1560 */       handleIgnoredProperty(p, ctxt, beanOrBuilder, propName);
/* 1561 */     } else if (this._anySetter != null) {
/*      */       
/*      */       try {
/* 1564 */         this._anySetter.deserializeAndSet(p, ctxt, beanOrBuilder, propName);
/* 1565 */       } catch (Exception e) {
/* 1566 */         wrapAndThrow(e, beanOrBuilder, propName, ctxt);
/*      */       } 
/*      */     } else {
/*      */       
/* 1570 */       handleUnknownProperty(p, ctxt, beanOrBuilder, propName);
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
/*      */   protected void handleUnknownProperty(JsonParser p, DeserializationContext ctxt, Object beanOrClass, String propName) throws IOException {
/* 1583 */     if (this._ignoreAllUnknown) {
/* 1584 */       p.skipChildren();
/*      */       return;
/*      */     } 
/* 1587 */     if (this._ignorableProps != null && this._ignorableProps.contains(propName)) {
/* 1588 */       handleIgnoredProperty(p, ctxt, beanOrClass, propName);
/*      */     }
/*      */ 
/*      */     
/* 1592 */     super.handleUnknownProperty(p, ctxt, beanOrClass, propName);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void handleIgnoredProperty(JsonParser p, DeserializationContext ctxt, Object beanOrClass, String propName) throws IOException {
/* 1605 */     if (ctxt.isEnabled(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES)) {
/* 1606 */       throw IgnoredPropertyException.from(p, beanOrClass, propName, getKnownPropertyNames());
/*      */     }
/* 1608 */     p.skipChildren();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Object handlePolymorphic(JsonParser p, DeserializationContext ctxt, Object bean, TokenBuffer unknownTokens) throws IOException {
/* 1628 */     JsonDeserializer<Object> subDeser = _findSubclassDeserializer(ctxt, bean, unknownTokens);
/* 1629 */     if (subDeser != null) {
/* 1630 */       if (unknownTokens != null) {
/*      */         
/* 1632 */         unknownTokens.writeEndObject();
/* 1633 */         JsonParser p2 = unknownTokens.asParser();
/* 1634 */         p2.nextToken();
/* 1635 */         bean = subDeser.deserialize(p2, ctxt, bean);
/*      */       } 
/*      */       
/* 1638 */       if (p != null) {
/* 1639 */         bean = subDeser.deserialize(p, ctxt, bean);
/*      */       }
/* 1641 */       return bean;
/*      */     } 
/*      */     
/* 1644 */     if (unknownTokens != null) {
/* 1645 */       bean = handleUnknownProperties(ctxt, bean, unknownTokens);
/*      */     }
/*      */     
/* 1648 */     if (p != null) {
/* 1649 */       bean = deserialize(p, ctxt, bean);
/*      */     }
/* 1651 */     return bean;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected JsonDeserializer<Object> _findSubclassDeserializer(DeserializationContext ctxt, Object bean, TokenBuffer unknownTokens) throws IOException {
/* 1665 */     synchronized (this) {
/* 1666 */       subDeser = (this._subDeserializers == null) ? null : this._subDeserializers.get(new ClassKey(bean.getClass()));
/*      */     } 
/* 1668 */     if (subDeser != null) {
/* 1669 */       return subDeser;
/*      */     }
/*      */     
/* 1672 */     JavaType type = ctxt.constructType(bean.getClass());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1679 */     JsonDeserializer<Object> subDeser = ctxt.findRootValueDeserializer(type);
/*      */     
/* 1681 */     if (subDeser != null) {
/* 1682 */       synchronized (this) {
/* 1683 */         if (this._subDeserializers == null) {
/* 1684 */           this._subDeserializers = new HashMap<>();
/*      */         }
/* 1686 */         this._subDeserializers.put(new ClassKey(bean.getClass()), subDeser);
/*      */       } 
/*      */     }
/* 1689 */     return subDeser;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void wrapAndThrow(Throwable t, Object bean, String fieldName, DeserializationContext ctxt) throws IOException {
/* 1714 */     throw JsonMappingException.wrapWithPath(throwOrReturnThrowable(t, ctxt), bean, fieldName);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Throwable throwOrReturnThrowable(Throwable t, DeserializationContext ctxt) throws IOException {
/* 1724 */     while (t instanceof java.lang.reflect.InvocationTargetException && t.getCause() != null) {
/* 1725 */       t = t.getCause();
/*      */     }
/*      */     
/* 1728 */     ClassUtil.throwIfError(t);
/* 1729 */     boolean wrap = (ctxt == null || ctxt.isEnabled(DeserializationFeature.WRAP_EXCEPTIONS));
/*      */     
/* 1731 */     if (t instanceof IOException) {
/* 1732 */       if (!wrap || !(t instanceof com.fasterxml.jackson.core.JsonProcessingException)) {
/* 1733 */         throw (IOException)t;
/*      */       }
/* 1735 */     } else if (!wrap) {
/* 1736 */       ClassUtil.throwIfRTE(t);
/*      */     } 
/* 1738 */     return t;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected Object wrapInstantiationProblem(Throwable t, DeserializationContext ctxt) throws IOException {
/* 1744 */     while (t instanceof java.lang.reflect.InvocationTargetException && t.getCause() != null) {
/* 1745 */       t = t.getCause();
/*      */     }
/*      */     
/* 1748 */     ClassUtil.throwIfError(t);
/* 1749 */     if (t instanceof IOException)
/*      */     {
/* 1751 */       throw (IOException)t;
/*      */     }
/* 1753 */     boolean wrap = (ctxt == null || ctxt.isEnabled(DeserializationFeature.WRAP_EXCEPTIONS));
/* 1754 */     if (!wrap) {
/* 1755 */       ClassUtil.throwIfRTE(t);
/*      */     }
/* 1757 */     return ctxt.handleInstantiationProblem(this._beanType.getRawClass(), null, t);
/*      */   }
/*      */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\deser\BeanDeserializerBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */