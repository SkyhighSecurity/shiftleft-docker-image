/*      */ package com.fasterxml.jackson.databind.deser;
/*      */ import com.fasterxml.jackson.annotation.JacksonInject;
/*      */ import com.fasterxml.jackson.annotation.JsonCreator;
/*      */ import com.fasterxml.jackson.annotation.JsonSetter;
/*      */ import com.fasterxml.jackson.annotation.Nulls;
/*      */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*      */ import com.fasterxml.jackson.databind.BeanDescription;
/*      */ import com.fasterxml.jackson.databind.BeanProperty;
/*      */ import com.fasterxml.jackson.databind.DeserializationConfig;
/*      */ import com.fasterxml.jackson.databind.DeserializationContext;
/*      */ import com.fasterxml.jackson.databind.JavaType;
/*      */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*      */ import com.fasterxml.jackson.databind.JsonMappingException;
/*      */ import com.fasterxml.jackson.databind.KeyDeserializer;
/*      */ import com.fasterxml.jackson.databind.PropertyMetadata;
/*      */ import com.fasterxml.jackson.databind.PropertyName;
/*      */ import com.fasterxml.jackson.databind.cfg.MapperConfig;
/*      */ import com.fasterxml.jackson.databind.deser.impl.CreatorCandidate;
/*      */ import com.fasterxml.jackson.databind.deser.impl.CreatorCollector;
/*      */ import com.fasterxml.jackson.databind.deser.std.EnumDeserializer;
/*      */ import com.fasterxml.jackson.databind.deser.std.MapDeserializer;
/*      */ import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
/*      */ import com.fasterxml.jackson.databind.introspect.Annotated;
/*      */ import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
/*      */ import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
/*      */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*      */ import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
/*      */ import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
/*      */ import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
/*      */ import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
/*      */ import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
/*      */ import com.fasterxml.jackson.databind.jsontype.NamedType;
/*      */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*      */ import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
/*      */ import com.fasterxml.jackson.databind.type.ArrayType;
/*      */ import com.fasterxml.jackson.databind.type.CollectionLikeType;
/*      */ import com.fasterxml.jackson.databind.type.CollectionType;
/*      */ import com.fasterxml.jackson.databind.type.MapLikeType;
/*      */ import com.fasterxml.jackson.databind.type.MapType;
/*      */ import com.fasterxml.jackson.databind.type.ReferenceType;
/*      */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*      */ import com.fasterxml.jackson.databind.util.EnumResolver;
/*      */ import com.fasterxml.jackson.databind.util.NameTransformer;
/*      */ import java.util.Collection;
/*      */ import java.util.HashMap;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ 
/*      */ public abstract class BasicDeserializerFactory extends DeserializerFactory implements Serializable {
/*   50 */   private static final Class<?> CLASS_OBJECT = Object.class;
/*   51 */   private static final Class<?> CLASS_STRING = String.class;
/*   52 */   private static final Class<?> CLASS_CHAR_SEQUENCE = CharSequence.class;
/*   53 */   private static final Class<?> CLASS_ITERABLE = Iterable.class;
/*   54 */   private static final Class<?> CLASS_MAP_ENTRY = Map.Entry.class;
/*   55 */   private static final Class<?> CLASS_SERIALIZABLE = Serializable.class;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   61 */   protected static final PropertyName UNWRAPPED_CREATOR_PARAM_NAME = new PropertyName("@JsonUnwrapped");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final DeserializerFactoryConfig _factoryConfig;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected BasicDeserializerFactory(DeserializerFactoryConfig config) {
/*   82 */     this._factoryConfig = config;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public DeserializerFactoryConfig getFactoryConfig() {
/*   93 */     return this._factoryConfig;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected abstract DeserializerFactory withConfig(DeserializerFactoryConfig paramDeserializerFactoryConfig);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final DeserializerFactory withAdditionalDeserializers(Deserializers additional) {
/*  110 */     return withConfig(this._factoryConfig.withAdditionalDeserializers(additional));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final DeserializerFactory withAdditionalKeyDeserializers(KeyDeserializers additional) {
/*  119 */     return withConfig(this._factoryConfig.withAdditionalKeyDeserializers(additional));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final DeserializerFactory withDeserializerModifier(BeanDeserializerModifier modifier) {
/*  128 */     return withConfig(this._factoryConfig.withDeserializerModifier(modifier));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final DeserializerFactory withAbstractTypeResolver(AbstractTypeResolver resolver) {
/*  137 */     return withConfig(this._factoryConfig.withAbstractTypeResolver(resolver));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final DeserializerFactory withValueInstantiators(ValueInstantiators instantiators) {
/*  146 */     return withConfig(this._factoryConfig.withValueInstantiators(instantiators));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JavaType mapAbstractType(DeserializationConfig config, JavaType type) throws JsonMappingException {
/*      */     while (true) {
/*  160 */       JavaType next = _mapAbstractType2(config, type);
/*  161 */       if (next == null) {
/*  162 */         return type;
/*      */       }
/*      */ 
/*      */       
/*  166 */       Class<?> prevCls = type.getRawClass();
/*  167 */       Class<?> nextCls = next.getRawClass();
/*  168 */       if (prevCls == nextCls || !prevCls.isAssignableFrom(nextCls)) {
/*  169 */         throw new IllegalArgumentException("Invalid abstract type resolution from " + type + " to " + next + ": latter is not a subtype of former");
/*      */       }
/*  171 */       type = next;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private JavaType _mapAbstractType2(DeserializationConfig config, JavaType type) throws JsonMappingException {
/*  182 */     Class<?> currClass = type.getRawClass();
/*  183 */     if (this._factoryConfig.hasAbstractTypeResolvers()) {
/*  184 */       for (AbstractTypeResolver resolver : this._factoryConfig.abstractTypeResolvers()) {
/*  185 */         JavaType concrete = resolver.findTypeMapping(config, type);
/*  186 */         if (concrete != null && !concrete.hasRawClass(currClass)) {
/*  187 */           return concrete;
/*      */         }
/*      */       } 
/*      */     }
/*  191 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ValueInstantiator findValueInstantiator(DeserializationContext ctxt, BeanDescription beanDesc) throws JsonMappingException {
/*  210 */     DeserializationConfig config = ctxt.getConfig();
/*      */     
/*  212 */     ValueInstantiator instantiator = null;
/*      */     
/*  214 */     AnnotatedClass ac = beanDesc.getClassInfo();
/*  215 */     Object instDef = ctxt.getAnnotationIntrospector().findValueInstantiator(ac);
/*  216 */     if (instDef != null) {
/*  217 */       instantiator = _valueInstantiatorInstance(config, (Annotated)ac, instDef);
/*      */     }
/*  219 */     if (instantiator == null) {
/*      */ 
/*      */       
/*  222 */       instantiator = JDKValueInstantiators.findStdValueInstantiator(config, beanDesc.getBeanClass());
/*  223 */       if (instantiator == null) {
/*  224 */         instantiator = _constructDefaultValueInstantiator(ctxt, beanDesc);
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/*  229 */     if (this._factoryConfig.hasValueInstantiators()) {
/*  230 */       for (ValueInstantiators insts : this._factoryConfig.valueInstantiators()) {
/*  231 */         instantiator = insts.findValueInstantiator(config, beanDesc, instantiator);
/*      */         
/*  233 */         if (instantiator == null) {
/*  234 */           ctxt.reportBadTypeDefinition(beanDesc, "Broken registered ValueInstantiators (of type %s): returned null ValueInstantiator", new Object[] { insts
/*      */                 
/*  236 */                 .getClass().getName() });
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*  242 */     if (instantiator.getIncompleteParameter() != null) {
/*  243 */       AnnotatedParameter nonAnnotatedParam = instantiator.getIncompleteParameter();
/*  244 */       AnnotatedWithParams ctor = nonAnnotatedParam.getOwner();
/*  245 */       throw new IllegalArgumentException("Argument #" + nonAnnotatedParam.getIndex() + " of constructor " + ctor + " has no property name annotation; must have name when multiple-parameter constructor annotated as Creator");
/*      */     } 
/*      */ 
/*      */     
/*  249 */     return instantiator;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected ValueInstantiator _constructDefaultValueInstantiator(DeserializationContext ctxt, BeanDescription beanDesc) throws JsonMappingException {
/*  260 */     CreatorCollector creators = new CreatorCollector(beanDesc, (MapperConfig)ctxt.getConfig());
/*  261 */     AnnotationIntrospector intr = ctxt.getAnnotationIntrospector();
/*      */ 
/*      */     
/*  264 */     DeserializationConfig config = ctxt.getConfig();
/*  265 */     VisibilityChecker<?> vchecker = config.getDefaultVisibilityChecker(beanDesc.getBeanClass(), beanDesc
/*  266 */         .getClassInfo());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  276 */     Map<AnnotatedWithParams, BeanPropertyDefinition[]> creatorDefs = _findCreatorsFromProperties(ctxt, beanDesc);
/*      */ 
/*      */ 
/*      */     
/*  280 */     _addDeserializerFactoryMethods(ctxt, beanDesc, vchecker, intr, creators, creatorDefs);
/*      */     
/*  282 */     if (beanDesc.getType().isConcrete()) {
/*  283 */       _addDeserializerConstructors(ctxt, beanDesc, vchecker, intr, creators, creatorDefs);
/*      */     }
/*  285 */     return creators.constructValueInstantiator(ctxt);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected Map<AnnotatedWithParams, BeanPropertyDefinition[]> _findCreatorsFromProperties(DeserializationContext ctxt, BeanDescription beanDesc) throws JsonMappingException {
/*  291 */     Map<AnnotatedWithParams, BeanPropertyDefinition[]> result = (Map)Collections.emptyMap();
/*  292 */     for (BeanPropertyDefinition propDef : beanDesc.findProperties()) {
/*  293 */       Iterator<AnnotatedParameter> it = propDef.getConstructorParameters();
/*  294 */       while (it.hasNext()) {
/*  295 */         AnnotatedParameter param = it.next();
/*  296 */         AnnotatedWithParams owner = param.getOwner();
/*  297 */         BeanPropertyDefinition[] defs = result.get(owner);
/*  298 */         int index = param.getIndex();
/*      */         
/*  300 */         if (defs == null) {
/*  301 */           if (result.isEmpty()) {
/*  302 */             result = (Map)new LinkedHashMap<>();
/*      */           }
/*  304 */           defs = new BeanPropertyDefinition[owner.getParameterCount()];
/*  305 */           result.put(owner, defs);
/*      */         }
/*  307 */         else if (defs[index] != null) {
/*  308 */           ctxt.reportBadTypeDefinition(beanDesc, "Conflict: parameter #%d of %s bound to more than one property; %s vs %s", new Object[] {
/*      */                 
/*  310 */                 Integer.valueOf(index), owner, defs[index], propDef
/*      */               });
/*      */         } 
/*  313 */         defs[index] = propDef;
/*      */       } 
/*      */     } 
/*  316 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ValueInstantiator _valueInstantiatorInstance(DeserializationConfig config, Annotated annotated, Object instDef) throws JsonMappingException {
/*  323 */     if (instDef == null) {
/*  324 */       return null;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  329 */     if (instDef instanceof ValueInstantiator) {
/*  330 */       return (ValueInstantiator)instDef;
/*      */     }
/*  332 */     if (!(instDef instanceof Class)) {
/*  333 */       throw new IllegalStateException("AnnotationIntrospector returned key deserializer definition of type " + instDef
/*  334 */           .getClass().getName() + "; expected type KeyDeserializer or Class<KeyDeserializer> instead");
/*      */     }
/*      */     
/*  337 */     Class<?> instClass = (Class)instDef;
/*  338 */     if (ClassUtil.isBogusClass(instClass)) {
/*  339 */       return null;
/*      */     }
/*  341 */     if (!ValueInstantiator.class.isAssignableFrom(instClass)) {
/*  342 */       throw new IllegalStateException("AnnotationIntrospector returned Class " + instClass.getName() + "; expected Class<ValueInstantiator>");
/*      */     }
/*      */     
/*  345 */     HandlerInstantiator hi = config.getHandlerInstantiator();
/*  346 */     if (hi != null) {
/*  347 */       ValueInstantiator inst = hi.valueInstantiatorInstance((MapperConfig)config, annotated, instClass);
/*  348 */       if (inst != null) {
/*  349 */         return inst;
/*      */       }
/*      */     } 
/*  352 */     return (ValueInstantiator)ClassUtil.createInstance(instClass, config
/*  353 */         .canOverrideAccessModifiers());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void _addDeserializerConstructors(DeserializationContext ctxt, BeanDescription beanDesc, VisibilityChecker<?> vchecker, AnnotationIntrospector intr, CreatorCollector creators, Map<AnnotatedWithParams, BeanPropertyDefinition[]> creatorParams) throws JsonMappingException {
/*  371 */     boolean isNonStaticInnerClass = beanDesc.isNonStaticInnerClass();
/*  372 */     if (isNonStaticInnerClass) {
/*      */       return;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  380 */     AnnotatedConstructor defaultCtor = beanDesc.findDefaultConstructor();
/*  381 */     if (defaultCtor != null && (
/*  382 */       !creators.hasDefaultCreator() || _hasCreatorAnnotation(ctxt, (Annotated)defaultCtor))) {
/*  383 */       creators.setDefaultCreator((AnnotatedWithParams)defaultCtor);
/*      */     }
/*      */ 
/*      */     
/*  387 */     List<CreatorCandidate> nonAnnotated = new LinkedList<>();
/*  388 */     int explCount = 0;
/*  389 */     for (AnnotatedConstructor ctor : beanDesc.getConstructors()) {
/*  390 */       JsonCreator.Mode creatorMode = intr.findCreatorAnnotation((MapperConfig)ctxt.getConfig(), (Annotated)ctor);
/*  391 */       if (JsonCreator.Mode.DISABLED == creatorMode) {
/*      */         continue;
/*      */       }
/*  394 */       if (creatorMode == null) {
/*      */         
/*  396 */         if (vchecker.isCreatorVisible((AnnotatedMember)ctor)) {
/*  397 */           nonAnnotated.add(CreatorCandidate.construct(intr, (AnnotatedWithParams)ctor, creatorParams.get(ctor)));
/*      */         }
/*      */         continue;
/*      */       } 
/*  401 */       switch (creatorMode) {
/*      */         case DELEGATING:
/*  403 */           _addExplicitDelegatingCreator(ctxt, beanDesc, creators, 
/*  404 */               CreatorCandidate.construct(intr, (AnnotatedWithParams)ctor, null));
/*      */           break;
/*      */         case PROPERTIES:
/*  407 */           _addExplicitPropertyCreator(ctxt, beanDesc, creators, 
/*  408 */               CreatorCandidate.construct(intr, (AnnotatedWithParams)ctor, creatorParams.get(ctor)));
/*      */           break;
/*      */         default:
/*  411 */           _addExplicitAnyCreator(ctxt, beanDesc, creators, 
/*  412 */               CreatorCandidate.construct(intr, (AnnotatedWithParams)ctor, creatorParams.get(ctor)));
/*      */           break;
/*      */       } 
/*  415 */       explCount++;
/*      */     } 
/*      */     
/*  418 */     if (explCount > 0) {
/*      */       return;
/*      */     }
/*  421 */     List<AnnotatedWithParams> implicitCtors = null;
/*  422 */     for (CreatorCandidate candidate : nonAnnotated) {
/*  423 */       int argCount = candidate.paramCount();
/*  424 */       AnnotatedWithParams ctor = candidate.creator();
/*      */ 
/*      */       
/*  427 */       if (argCount == 1) {
/*  428 */         BeanPropertyDefinition propDef = candidate.propertyDef(0);
/*  429 */         boolean useProps = _checkIfCreatorPropertyBased(intr, ctor, propDef);
/*      */         
/*  431 */         if (useProps) {
/*  432 */           SettableBeanProperty[] arrayOfSettableBeanProperty = new SettableBeanProperty[1];
/*  433 */           PropertyName name = candidate.paramName(0);
/*  434 */           arrayOfSettableBeanProperty[0] = constructCreatorProperty(ctxt, beanDesc, name, 0, candidate
/*  435 */               .parameter(0), candidate.injection(0));
/*  436 */           creators.addPropertyCreator(ctor, false, arrayOfSettableBeanProperty); continue;
/*      */         } 
/*  438 */         _handleSingleArgumentCreator(creators, ctor, false, vchecker
/*      */             
/*  440 */             .isCreatorVisible((AnnotatedMember)ctor));
/*      */ 
/*      */         
/*  443 */         if (propDef != null) {
/*  444 */           ((POJOPropertyBuilder)propDef).removeConstructors();
/*      */         }
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         continue;
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  455 */       int nonAnnotatedParamIndex = -1;
/*  456 */       SettableBeanProperty[] properties = new SettableBeanProperty[argCount];
/*  457 */       int explicitNameCount = 0;
/*  458 */       int implicitWithCreatorCount = 0;
/*  459 */       int injectCount = 0;
/*      */       
/*  461 */       for (int i = 0; i < argCount; i++) {
/*  462 */         AnnotatedParameter param = ctor.getParameter(i);
/*  463 */         BeanPropertyDefinition propDef = candidate.propertyDef(i);
/*  464 */         JacksonInject.Value injectId = intr.findInjectableValue((AnnotatedMember)param);
/*  465 */         PropertyName name = (propDef == null) ? null : propDef.getFullName();
/*      */         
/*  467 */         if (propDef != null && propDef.isExplicitlyNamed()) {
/*  468 */           explicitNameCount++;
/*  469 */           properties[i] = constructCreatorProperty(ctxt, beanDesc, name, i, param, injectId);
/*      */         
/*      */         }
/*  472 */         else if (injectId != null) {
/*  473 */           injectCount++;
/*  474 */           properties[i] = constructCreatorProperty(ctxt, beanDesc, name, i, param, injectId);
/*      */         } else {
/*      */           
/*  477 */           NameTransformer unwrapper = intr.findUnwrappingNameTransformer((AnnotatedMember)param);
/*  478 */           if (unwrapper != null) {
/*  479 */             _reportUnwrappedCreatorProperty(ctxt, beanDesc, param);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           }
/*  494 */           else if (nonAnnotatedParamIndex < 0) {
/*  495 */             nonAnnotatedParamIndex = i;
/*      */           } 
/*      */         } 
/*      */       } 
/*  499 */       int namedCount = explicitNameCount + implicitWithCreatorCount;
/*      */       
/*  501 */       if (explicitNameCount > 0 || injectCount > 0) {
/*      */         
/*  503 */         if (namedCount + injectCount == argCount) {
/*  504 */           creators.addPropertyCreator(ctor, false, properties);
/*      */           continue;
/*      */         } 
/*  507 */         if (explicitNameCount == 0 && injectCount + 1 == argCount) {
/*      */           
/*  509 */           creators.addDelegatingCreator(ctor, false, properties, 0);
/*      */ 
/*      */           
/*      */           continue;
/*      */         } 
/*      */         
/*  515 */         PropertyName impl = candidate.findImplicitParamName(nonAnnotatedParamIndex);
/*  516 */         if (impl == null || impl.isEmpty())
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  525 */           ctxt.reportBadTypeDefinition(beanDesc, "Argument #%d of constructor %s has no property name annotation; must have name when multiple-parameter constructor annotated as Creator", new Object[] {
/*      */                 
/*  527 */                 Integer.valueOf(nonAnnotatedParamIndex), ctor
/*      */               });
/*      */         }
/*      */       } 
/*  531 */       if (!creators.hasDefaultCreator()) {
/*  532 */         if (implicitCtors == null) {
/*  533 */           implicitCtors = new LinkedList<>();
/*      */         }
/*  535 */         implicitCtors.add(ctor);
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  540 */     if (implicitCtors != null && !creators.hasDelegatingCreator() && 
/*  541 */       !creators.hasPropertyBasedCreator()) {
/*  542 */       _checkImplicitlyNamedConstructors(ctxt, beanDesc, vchecker, intr, creators, implicitCtors);
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
/*      */   protected void _addExplicitDelegatingCreator(DeserializationContext ctxt, BeanDescription beanDesc, CreatorCollector creators, CreatorCandidate candidate) throws JsonMappingException {
/*  560 */     int ix = -1;
/*  561 */     int argCount = candidate.paramCount();
/*  562 */     SettableBeanProperty[] properties = new SettableBeanProperty[argCount];
/*  563 */     for (int i = 0; i < argCount; i++) {
/*  564 */       AnnotatedParameter param = candidate.parameter(i);
/*  565 */       JacksonInject.Value injectId = candidate.injection(i);
/*  566 */       if (injectId != null) {
/*  567 */         properties[i] = constructCreatorProperty(ctxt, beanDesc, null, i, param, injectId);
/*      */       
/*      */       }
/*  570 */       else if (ix < 0) {
/*  571 */         ix = i;
/*      */       }
/*      */       else {
/*      */         
/*  575 */         ctxt.reportBadTypeDefinition(beanDesc, "More than one argument (#%d and #%d) left as delegating for Creator %s: only one allowed", new Object[] {
/*      */               
/*  577 */               Integer.valueOf(ix), Integer.valueOf(i), candidate });
/*      */       } 
/*      */     } 
/*  580 */     if (ix < 0) {
/*  581 */       ctxt.reportBadTypeDefinition(beanDesc, "No argument left as delegating for Creator %s: exactly one required", new Object[] { candidate });
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  586 */     if (argCount == 1) {
/*  587 */       _handleSingleArgumentCreator(creators, candidate.creator(), true, true);
/*      */ 
/*      */       
/*  590 */       BeanPropertyDefinition paramDef = candidate.propertyDef(0);
/*  591 */       if (paramDef != null) {
/*  592 */         ((POJOPropertyBuilder)paramDef).removeConstructors();
/*      */       }
/*      */       return;
/*      */     } 
/*  596 */     creators.addDelegatingCreator(candidate.creator(), true, properties, ix);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void _addExplicitPropertyCreator(DeserializationContext ctxt, BeanDescription beanDesc, CreatorCollector creators, CreatorCandidate candidate) throws JsonMappingException {
/*  609 */     int paramCount = candidate.paramCount();
/*  610 */     SettableBeanProperty[] properties = new SettableBeanProperty[paramCount];
/*      */     
/*  612 */     for (int i = 0; i < paramCount; i++) {
/*  613 */       JacksonInject.Value injectId = candidate.injection(i);
/*  614 */       AnnotatedParameter param = candidate.parameter(i);
/*  615 */       PropertyName name = candidate.paramName(i);
/*  616 */       if (name == null) {
/*      */ 
/*      */         
/*  619 */         NameTransformer unwrapper = ctxt.getAnnotationIntrospector().findUnwrappingNameTransformer((AnnotatedMember)param);
/*  620 */         if (unwrapper != null) {
/*  621 */           _reportUnwrappedCreatorProperty(ctxt, beanDesc, param);
/*      */         }
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  627 */         name = candidate.findImplicitParamName(i);
/*      */         
/*  629 */         if (name == null && injectId == null)
/*  630 */           ctxt.reportBadTypeDefinition(beanDesc, "Argument #%d has no property name, is not Injectable: can not use as Creator %s", new Object[] {
/*  631 */                 Integer.valueOf(i), candidate
/*      */               }); 
/*      */       } 
/*  634 */       properties[i] = constructCreatorProperty(ctxt, beanDesc, name, i, param, injectId);
/*      */     } 
/*  636 */     creators.addPropertyCreator(candidate.creator(), true, properties);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void _addExplicitAnyCreator(DeserializationContext ctxt, BeanDescription beanDesc, CreatorCollector creators, CreatorCandidate candidate) throws JsonMappingException {
/*  650 */     if (1 != candidate.paramCount()) {
/*      */ 
/*      */       
/*  653 */       int oneNotInjected = candidate.findOnlyParamWithoutInjection();
/*  654 */       if (oneNotInjected >= 0)
/*      */       {
/*  656 */         if (candidate.paramName(oneNotInjected) == null) {
/*  657 */           _addExplicitDelegatingCreator(ctxt, beanDesc, creators, candidate);
/*      */           return;
/*      */         } 
/*      */       }
/*  661 */       _addExplicitPropertyCreator(ctxt, beanDesc, creators, candidate);
/*      */       return;
/*      */     } 
/*  664 */     AnnotatedParameter param = candidate.parameter(0);
/*  665 */     JacksonInject.Value injectId = candidate.injection(0);
/*  666 */     PropertyName paramName = candidate.explicitParamName(0);
/*  667 */     BeanPropertyDefinition paramDef = candidate.propertyDef(0);
/*      */ 
/*      */     
/*  670 */     boolean useProps = (paramName != null || injectId != null);
/*  671 */     if (!useProps && paramDef != null) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  678 */       paramName = candidate.paramName(0);
/*  679 */       useProps = (paramName != null && paramDef.couldSerialize());
/*      */     } 
/*  681 */     if (useProps) {
/*      */       
/*  683 */       SettableBeanProperty[] properties = { constructCreatorProperty(ctxt, beanDesc, paramName, 0, param, injectId) };
/*      */       
/*  685 */       creators.addPropertyCreator(candidate.creator(), true, properties);
/*      */       return;
/*      */     } 
/*  688 */     _handleSingleArgumentCreator(creators, candidate.creator(), true, true);
/*      */ 
/*      */ 
/*      */     
/*  692 */     if (paramDef != null) {
/*  693 */       ((POJOPropertyBuilder)paramDef).removeConstructors();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean _checkIfCreatorPropertyBased(AnnotationIntrospector intr, AnnotatedWithParams creator, BeanPropertyDefinition propDef) {
/*  701 */     if ((propDef != null && propDef.isExplicitlyNamed()) || intr
/*  702 */       .findInjectableValue((AnnotatedMember)creator.getParameter(0)) != null) {
/*  703 */       return true;
/*      */     }
/*  705 */     if (propDef != null) {
/*      */ 
/*      */       
/*  708 */       String implName = propDef.getName();
/*  709 */       if (implName != null && !implName.isEmpty() && 
/*  710 */         propDef.couldSerialize()) {
/*  711 */         return true;
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/*  716 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void _checkImplicitlyNamedConstructors(DeserializationContext ctxt, BeanDescription beanDesc, VisibilityChecker<?> vchecker, AnnotationIntrospector intr, CreatorCollector creators, List<AnnotatedWithParams> implicitCtors) throws JsonMappingException {
/*  724 */     AnnotatedWithParams found = null;
/*  725 */     SettableBeanProperty[] foundProps = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  732 */     label34: for (AnnotatedWithParams ctor : implicitCtors) {
/*  733 */       if (!vchecker.isCreatorVisible((AnnotatedMember)ctor)) {
/*      */         continue;
/*      */       }
/*      */       
/*  737 */       int argCount = ctor.getParameterCount();
/*  738 */       SettableBeanProperty[] properties = new SettableBeanProperty[argCount];
/*  739 */       for (int i = 0; i < argCount; ) {
/*  740 */         AnnotatedParameter param = ctor.getParameter(i);
/*  741 */         PropertyName name = _findParamName(param, intr);
/*      */ 
/*      */         
/*  744 */         if (name != null) { if (name.isEmpty()) {
/*      */             continue label34;
/*      */           }
/*  747 */           properties[i] = constructCreatorProperty(ctxt, beanDesc, name, param.getIndex(), param, null); i++; }
/*      */          continue label34;
/*      */       } 
/*  750 */       if (found != null) {
/*  751 */         found = null;
/*      */         break;
/*      */       } 
/*  754 */       found = ctor;
/*  755 */       foundProps = properties;
/*      */     } 
/*      */     
/*  758 */     if (found != null) {
/*  759 */       creators.addPropertyCreator(found, false, foundProps);
/*  760 */       BasicBeanDescription bbd = (BasicBeanDescription)beanDesc;
/*      */       
/*  762 */       for (SettableBeanProperty prop : foundProps) {
/*  763 */         PropertyName pn = prop.getFullName();
/*  764 */         if (!bbd.hasProperty(pn)) {
/*  765 */           SimpleBeanPropertyDefinition simpleBeanPropertyDefinition = SimpleBeanPropertyDefinition.construct((MapperConfig)ctxt
/*  766 */               .getConfig(), prop.getMember(), pn);
/*  767 */           bbd.addProperty((BeanPropertyDefinition)simpleBeanPropertyDefinition);
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
/*      */   protected void _addDeserializerFactoryMethods(DeserializationContext ctxt, BeanDescription beanDesc, VisibilityChecker<?> vchecker, AnnotationIntrospector intr, CreatorCollector creators, Map<AnnotatedWithParams, BeanPropertyDefinition[]> creatorParams) throws JsonMappingException {
/*  779 */     List<CreatorCandidate> nonAnnotated = new LinkedList<>();
/*  780 */     int explCount = 0;
/*      */ 
/*      */     
/*  783 */     for (AnnotatedMethod factory : beanDesc.getFactoryMethods()) {
/*  784 */       JsonCreator.Mode creatorMode = intr.findCreatorAnnotation((MapperConfig)ctxt.getConfig(), (Annotated)factory);
/*  785 */       int argCount = factory.getParameterCount();
/*  786 */       if (creatorMode == null) {
/*      */         
/*  788 */         if (argCount == 1 && vchecker.isCreatorVisible((AnnotatedMember)factory)) {
/*  789 */           nonAnnotated.add(CreatorCandidate.construct(intr, (AnnotatedWithParams)factory, null));
/*      */         }
/*      */         continue;
/*      */       } 
/*  793 */       if (creatorMode == JsonCreator.Mode.DISABLED) {
/*      */         continue;
/*      */       }
/*      */ 
/*      */       
/*  798 */       if (argCount == 0) {
/*  799 */         creators.setDefaultCreator((AnnotatedWithParams)factory);
/*      */         
/*      */         continue;
/*      */       } 
/*  803 */       switch (creatorMode) {
/*      */         case DELEGATING:
/*  805 */           _addExplicitDelegatingCreator(ctxt, beanDesc, creators, 
/*  806 */               CreatorCandidate.construct(intr, (AnnotatedWithParams)factory, null));
/*      */           break;
/*      */         case PROPERTIES:
/*  809 */           _addExplicitPropertyCreator(ctxt, beanDesc, creators, 
/*  810 */               CreatorCandidate.construct(intr, (AnnotatedWithParams)factory, creatorParams.get(factory)));
/*      */           break;
/*      */         
/*      */         default:
/*  814 */           _addExplicitAnyCreator(ctxt, beanDesc, creators, 
/*  815 */               CreatorCandidate.construct(intr, (AnnotatedWithParams)factory, creatorParams.get(factory)));
/*      */           break;
/*      */       } 
/*  818 */       explCount++;
/*      */     } 
/*      */     
/*  821 */     if (explCount > 0) {
/*      */       return;
/*      */     }
/*      */     
/*  825 */     for (CreatorCandidate candidate : nonAnnotated) {
/*  826 */       int argCount = candidate.paramCount();
/*  827 */       AnnotatedWithParams factory = candidate.creator();
/*  828 */       BeanPropertyDefinition[] propDefs = creatorParams.get(factory);
/*      */       
/*  830 */       if (argCount != 1) {
/*      */         continue;
/*      */       }
/*  833 */       BeanPropertyDefinition argDef = candidate.propertyDef(0);
/*  834 */       boolean useProps = _checkIfCreatorPropertyBased(intr, factory, argDef);
/*  835 */       if (!useProps) {
/*  836 */         _handleSingleArgumentCreator(creators, factory, false, vchecker
/*  837 */             .isCreatorVisible((AnnotatedMember)factory));
/*      */ 
/*      */         
/*  840 */         if (argDef != null) {
/*  841 */           ((POJOPropertyBuilder)argDef).removeConstructors();
/*      */         }
/*      */         continue;
/*      */       } 
/*  845 */       AnnotatedParameter nonAnnotatedParam = null;
/*  846 */       SettableBeanProperty[] properties = new SettableBeanProperty[argCount];
/*  847 */       int implicitNameCount = 0;
/*  848 */       int explicitNameCount = 0;
/*  849 */       int injectCount = 0;
/*      */       
/*  851 */       for (int i = 0; i < argCount; i++) {
/*  852 */         AnnotatedParameter param = factory.getParameter(i);
/*  853 */         BeanPropertyDefinition propDef = (propDefs == null) ? null : propDefs[i];
/*  854 */         JacksonInject.Value injectable = intr.findInjectableValue((AnnotatedMember)param);
/*  855 */         PropertyName name = (propDef == null) ? null : propDef.getFullName();
/*      */         
/*  857 */         if (propDef != null && propDef.isExplicitlyNamed()) {
/*  858 */           explicitNameCount++;
/*  859 */           properties[i] = constructCreatorProperty(ctxt, beanDesc, name, i, param, injectable);
/*      */         
/*      */         }
/*  862 */         else if (injectable != null) {
/*  863 */           injectCount++;
/*  864 */           properties[i] = constructCreatorProperty(ctxt, beanDesc, name, i, param, injectable);
/*      */         } else {
/*      */           
/*  867 */           NameTransformer unwrapper = intr.findUnwrappingNameTransformer((AnnotatedMember)param);
/*  868 */           if (unwrapper != null) {
/*  869 */             _reportUnwrappedCreatorProperty(ctxt, beanDesc, param);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           }
/*  898 */           else if (nonAnnotatedParam == null) {
/*  899 */             nonAnnotatedParam = param;
/*      */           } 
/*      */         } 
/*  902 */       }  int namedCount = explicitNameCount + implicitNameCount;
/*      */ 
/*      */       
/*  905 */       if (explicitNameCount > 0 || injectCount > 0) {
/*      */         
/*  907 */         if (namedCount + injectCount == argCount) {
/*  908 */           creators.addPropertyCreator(factory, false, properties); continue;
/*  909 */         }  if (explicitNameCount == 0 && injectCount + 1 == argCount) {
/*      */           
/*  911 */           creators.addDelegatingCreator(factory, false, properties, 0); continue;
/*      */         } 
/*  913 */         ctxt.reportBadTypeDefinition(beanDesc, "Argument #%d of factory method %s has no property name annotation; must have name when multiple-parameter constructor annotated as Creator", new Object[] {
/*      */               
/*  915 */               Integer.valueOf(nonAnnotatedParam.getIndex()), factory
/*      */             });
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean _handleSingleArgumentCreator(CreatorCollector creators, AnnotatedWithParams ctor, boolean isCreator, boolean isVisible) {
/*  925 */     Class<?> type = ctor.getRawParameterType(0);
/*  926 */     if (type == String.class || type == CLASS_CHAR_SEQUENCE) {
/*  927 */       if (isCreator || isVisible) {
/*  928 */         creators.addStringCreator(ctor, isCreator);
/*      */       }
/*  930 */       return true;
/*      */     } 
/*  932 */     if (type == int.class || type == Integer.class) {
/*  933 */       if (isCreator || isVisible) {
/*  934 */         creators.addIntCreator(ctor, isCreator);
/*      */       }
/*  936 */       return true;
/*      */     } 
/*  938 */     if (type == long.class || type == Long.class) {
/*  939 */       if (isCreator || isVisible) {
/*  940 */         creators.addLongCreator(ctor, isCreator);
/*      */       }
/*  942 */       return true;
/*      */     } 
/*  944 */     if (type == double.class || type == Double.class) {
/*  945 */       if (isCreator || isVisible) {
/*  946 */         creators.addDoubleCreator(ctor, isCreator);
/*      */       }
/*  948 */       return true;
/*      */     } 
/*  950 */     if (type == boolean.class || type == Boolean.class) {
/*  951 */       if (isCreator || isVisible) {
/*  952 */         creators.addBooleanCreator(ctor, isCreator);
/*      */       }
/*  954 */       return true;
/*      */     } 
/*      */     
/*  957 */     if (isCreator) {
/*  958 */       creators.addDelegatingCreator(ctor, isCreator, null, 0);
/*  959 */       return true;
/*      */     } 
/*  961 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void _reportUnwrappedCreatorProperty(DeserializationContext ctxt, BeanDescription beanDesc, AnnotatedParameter param) throws JsonMappingException {
/*  970 */     ctxt.reportBadDefinition(beanDesc.getType(), String.format("Cannot define Creator parameter %d as `@JsonUnwrapped`: combination not yet supported", new Object[] {
/*      */             
/*  972 */             Integer.valueOf(param.getIndex())
/*      */           }));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected SettableBeanProperty constructCreatorProperty(DeserializationContext ctxt, BeanDescription beanDesc, PropertyName name, int index, AnnotatedParameter param, JacksonInject.Value injectable) throws JsonMappingException {
/*  986 */     DeserializationConfig config = ctxt.getConfig();
/*  987 */     AnnotationIntrospector intr = ctxt.getAnnotationIntrospector();
/*      */ 
/*      */     
/*  990 */     if (intr == null) {
/*  991 */       metadata = PropertyMetadata.STD_REQUIRED_OR_OPTIONAL;
/*      */     } else {
/*  993 */       Boolean b = intr.hasRequiredMarker((AnnotatedMember)param);
/*  994 */       String desc = intr.findPropertyDescription((Annotated)param);
/*  995 */       Integer idx = intr.findPropertyIndex((Annotated)param);
/*  996 */       String def = intr.findPropertyDefaultValue((Annotated)param);
/*  997 */       metadata = PropertyMetadata.construct(b, desc, idx, def);
/*      */     } 
/*      */     
/* 1000 */     JavaType type = resolveMemberAndTypeAnnotations(ctxt, (AnnotatedMember)param, param.getType());
/*      */     
/* 1002 */     BeanProperty.Std property = new BeanProperty.Std(name, type, intr.findWrapperName((Annotated)param), (AnnotatedMember)param, metadata);
/*      */     
/* 1004 */     TypeDeserializer typeDeser = (TypeDeserializer)type.getTypeHandler();
/*      */     
/* 1006 */     if (typeDeser == null) {
/* 1007 */       typeDeser = findTypeDeserializer(config, type);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1012 */     PropertyMetadata metadata = _getSetterInfo(ctxt, (BeanProperty)property, metadata);
/*      */ 
/*      */ 
/*      */     
/* 1016 */     Object injectableValueId = (injectable == null) ? null : injectable.getId();
/*      */     
/* 1018 */     SettableBeanProperty prop = new CreatorProperty(name, type, property.getWrapperName(), typeDeser, beanDesc.getClassAnnotations(), param, index, injectableValueId, metadata);
/*      */     
/* 1020 */     JsonDeserializer<?> deser = findDeserializerFromAnnotation(ctxt, (Annotated)param);
/* 1021 */     if (deser == null) {
/* 1022 */       deser = (JsonDeserializer)type.getValueHandler();
/*      */     }
/* 1024 */     if (deser != null) {
/*      */       
/* 1026 */       deser = ctxt.handlePrimaryContextualization(deser, (BeanProperty)prop, type);
/* 1027 */       prop = prop.withValueDeserializer(deser);
/*      */     } 
/* 1029 */     return prop;
/*      */   }
/*      */ 
/*      */   
/*      */   private PropertyName _findParamName(AnnotatedParameter param, AnnotationIntrospector intr) {
/* 1034 */     if (param != null && intr != null) {
/* 1035 */       PropertyName name = intr.findNameForDeserialization((Annotated)param);
/* 1036 */       if (name != null) {
/* 1037 */         return name;
/*      */       }
/*      */ 
/*      */ 
/*      */       
/* 1042 */       String str = intr.findImplicitPropertyName((AnnotatedMember)param);
/* 1043 */       if (str != null && !str.isEmpty()) {
/* 1044 */         return PropertyName.construct(str);
/*      */       }
/*      */     } 
/* 1047 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected PropertyMetadata _getSetterInfo(DeserializationContext ctxt, BeanProperty prop, PropertyMetadata metadata) {
/* 1059 */     AnnotationIntrospector intr = ctxt.getAnnotationIntrospector();
/* 1060 */     DeserializationConfig config = ctxt.getConfig();
/*      */     
/* 1062 */     boolean needMerge = true;
/* 1063 */     Nulls valueNulls = null;
/* 1064 */     Nulls contentNulls = null;
/*      */ 
/*      */ 
/*      */     
/* 1068 */     AnnotatedMember prim = prop.getMember();
/*      */     
/* 1070 */     if (prim != null) {
/*      */       
/* 1072 */       if (intr != null) {
/* 1073 */         JsonSetter.Value setterInfo = intr.findSetterInfo((Annotated)prim);
/* 1074 */         if (setterInfo != null) {
/* 1075 */           valueNulls = setterInfo.nonDefaultValueNulls();
/* 1076 */           contentNulls = setterInfo.nonDefaultContentNulls();
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/* 1081 */       if (needMerge || valueNulls == null || contentNulls == null) {
/* 1082 */         ConfigOverride co = config.getConfigOverride(prop.getType().getRawClass());
/* 1083 */         JsonSetter.Value setterInfo = co.getSetterInfo();
/* 1084 */         if (setterInfo != null) {
/* 1085 */           if (valueNulls == null) {
/* 1086 */             valueNulls = setterInfo.nonDefaultValueNulls();
/*      */           }
/* 1088 */           if (contentNulls == null) {
/* 1089 */             contentNulls = setterInfo.nonDefaultContentNulls();
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/* 1094 */     if (needMerge || valueNulls == null || contentNulls == null) {
/* 1095 */       JsonSetter.Value setterInfo = config.getDefaultSetterInfo();
/* 1096 */       if (valueNulls == null) {
/* 1097 */         valueNulls = setterInfo.nonDefaultValueNulls();
/*      */       }
/* 1099 */       if (contentNulls == null) {
/* 1100 */         contentNulls = setterInfo.nonDefaultContentNulls();
/*      */       }
/*      */     } 
/* 1103 */     if (valueNulls != null || contentNulls != null) {
/* 1104 */       metadata = metadata.withNulls(valueNulls, contentNulls);
/*      */     }
/* 1106 */     return metadata;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JsonDeserializer<?> createArrayDeserializer(DeserializationContext ctxt, ArrayType type, BeanDescription beanDesc) throws JsonMappingException {
/*      */     ObjectArrayDeserializer objectArrayDeserializer;
/*      */     JsonDeserializer<?> jsonDeserializer1;
/* 1120 */     DeserializationConfig config = ctxt.getConfig();
/* 1121 */     JavaType elemType = type.getContentType();
/*      */ 
/*      */     
/* 1124 */     JsonDeserializer<Object> contentDeser = (JsonDeserializer<Object>)elemType.getValueHandler();
/*      */     
/* 1126 */     TypeDeserializer elemTypeDeser = (TypeDeserializer)elemType.getTypeHandler();
/*      */     
/* 1128 */     if (elemTypeDeser == null) {
/* 1129 */       elemTypeDeser = findTypeDeserializer(config, elemType);
/*      */     }
/*      */     
/* 1132 */     JsonDeserializer<?> deser = _findCustomArrayDeserializer(type, config, beanDesc, elemTypeDeser, contentDeser);
/*      */     
/* 1134 */     if (deser == null) {
/* 1135 */       if (contentDeser == null) {
/* 1136 */         Class<?> raw = elemType.getRawClass();
/* 1137 */         if (elemType.isPrimitive()) {
/* 1138 */           return PrimitiveArrayDeserializers.forType(raw);
/*      */         }
/* 1140 */         if (raw == String.class) {
/* 1141 */           return (JsonDeserializer<?>)StringArrayDeserializer.instance;
/*      */         }
/*      */       } 
/* 1144 */       objectArrayDeserializer = new ObjectArrayDeserializer((JavaType)type, contentDeser, elemTypeDeser);
/*      */     } 
/*      */     
/* 1147 */     if (this._factoryConfig.hasDeserializerModifiers()) {
/* 1148 */       for (BeanDeserializerModifier mod : this._factoryConfig.deserializerModifiers()) {
/* 1149 */         jsonDeserializer1 = mod.modifyArrayDeserializer(config, type, beanDesc, (JsonDeserializer<?>)objectArrayDeserializer);
/*      */       }
/*      */     }
/* 1152 */     return jsonDeserializer1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JsonDeserializer<?> createCollectionDeserializer(DeserializationContext ctxt, CollectionType type, BeanDescription beanDesc) throws JsonMappingException {
/*      */     EnumSetDeserializer enumSetDeserializer;
/*      */     CollectionDeserializer collectionDeserializer;
/*      */     JsonDeserializer<?> jsonDeserializer1;
/* 1166 */     JavaType contentType = type.getContentType();
/*      */     
/* 1168 */     JsonDeserializer<Object> contentDeser = (JsonDeserializer<Object>)contentType.getValueHandler();
/* 1169 */     DeserializationConfig config = ctxt.getConfig();
/*      */ 
/*      */     
/* 1172 */     TypeDeserializer contentTypeDeser = (TypeDeserializer)contentType.getTypeHandler();
/*      */     
/* 1174 */     if (contentTypeDeser == null) {
/* 1175 */       contentTypeDeser = findTypeDeserializer(config, contentType);
/*      */     }
/*      */     
/* 1178 */     JsonDeserializer<?> deser = _findCustomCollectionDeserializer(type, config, beanDesc, contentTypeDeser, contentDeser);
/*      */     
/* 1180 */     if (deser == null) {
/* 1181 */       Class<?> collectionClass = type.getRawClass();
/* 1182 */       if (contentDeser == null)
/*      */       {
/* 1184 */         if (EnumSet.class.isAssignableFrom(collectionClass)) {
/* 1185 */           enumSetDeserializer = new EnumSetDeserializer(contentType, null);
/*      */         }
/*      */       }
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
/* 1199 */     if (enumSetDeserializer == null) {
/* 1200 */       if (type.isInterface() || type.isAbstract()) {
/* 1201 */         CollectionType implType = _mapAbstractCollectionType((JavaType)type, config);
/* 1202 */         if (implType == null) {
/*      */           
/* 1204 */           if (type.getTypeHandler() == null) {
/* 1205 */             throw new IllegalArgumentException("Cannot find a deserializer for non-concrete Collection type " + type);
/*      */           }
/* 1207 */           jsonDeserializer1 = AbstractDeserializer.constructForNonPOJO(beanDesc);
/*      */         } else {
/* 1209 */           type = implType;
/*      */           
/* 1211 */           beanDesc = config.introspectForCreation((JavaType)type);
/*      */         } 
/*      */       } 
/* 1214 */       if (jsonDeserializer1 == null) {
/* 1215 */         ValueInstantiator inst = findValueInstantiator(ctxt, beanDesc);
/* 1216 */         if (!inst.canCreateUsingDefault()) {
/*      */           
/* 1218 */           if (type.hasRawClass(ArrayBlockingQueue.class)) {
/* 1219 */             return (JsonDeserializer<?>)new ArrayBlockingQueueDeserializer((JavaType)type, contentDeser, contentTypeDeser, inst);
/*      */           }
/*      */           
/* 1222 */           JsonDeserializer<?> jsonDeserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, (JavaType)type);
/* 1223 */           if (jsonDeserializer != null) {
/* 1224 */             return jsonDeserializer;
/*      */           }
/*      */         } 
/*      */         
/* 1228 */         if (contentType.hasRawClass(String.class)) {
/*      */           
/* 1230 */           StringCollectionDeserializer stringCollectionDeserializer = new StringCollectionDeserializer((JavaType)type, contentDeser, inst);
/*      */         } else {
/* 1232 */           collectionDeserializer = new CollectionDeserializer((JavaType)type, contentDeser, contentTypeDeser, inst);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 1237 */     if (this._factoryConfig.hasDeserializerModifiers()) {
/* 1238 */       for (BeanDeserializerModifier mod : this._factoryConfig.deserializerModifiers()) {
/* 1239 */         jsonDeserializer1 = mod.modifyCollectionDeserializer(config, type, beanDesc, (JsonDeserializer<?>)collectionDeserializer);
/*      */       }
/*      */     }
/* 1242 */     return jsonDeserializer1;
/*      */   }
/*      */ 
/*      */   
/*      */   protected CollectionType _mapAbstractCollectionType(JavaType type, DeserializationConfig config) {
/* 1247 */     Class<?> collectionClass = ContainerDefaultMappings.findCollectionFallback(type);
/* 1248 */     if (collectionClass != null) {
/* 1249 */       return (CollectionType)config.constructSpecializedType(type, collectionClass);
/*      */     }
/* 1251 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JsonDeserializer<?> createCollectionLikeDeserializer(DeserializationContext ctxt, CollectionLikeType type, BeanDescription beanDesc) throws JsonMappingException {
/* 1260 */     JavaType contentType = type.getContentType();
/*      */     
/* 1262 */     JsonDeserializer<Object> contentDeser = (JsonDeserializer<Object>)contentType.getValueHandler();
/* 1263 */     DeserializationConfig config = ctxt.getConfig();
/*      */ 
/*      */     
/* 1266 */     TypeDeserializer contentTypeDeser = (TypeDeserializer)contentType.getTypeHandler();
/*      */     
/* 1268 */     if (contentTypeDeser == null) {
/* 1269 */       contentTypeDeser = findTypeDeserializer(config, contentType);
/*      */     }
/* 1271 */     JsonDeserializer<?> deser = _findCustomCollectionLikeDeserializer(type, config, beanDesc, contentTypeDeser, contentDeser);
/*      */     
/* 1273 */     if (deser != null)
/*      */     {
/* 1275 */       if (this._factoryConfig.hasDeserializerModifiers()) {
/* 1276 */         for (BeanDeserializerModifier mod : this._factoryConfig.deserializerModifiers()) {
/* 1277 */           deser = mod.modifyCollectionLikeDeserializer(config, type, beanDesc, deser);
/*      */         }
/*      */       }
/*      */     }
/* 1281 */     return deser;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JsonDeserializer<?> createMapDeserializer(DeserializationContext ctxt, MapType type, BeanDescription beanDesc) throws JsonMappingException {
/*      */     MapDeserializer mapDeserializer;
/*      */     JsonDeserializer<?> jsonDeserializer1;
/* 1295 */     DeserializationConfig config = ctxt.getConfig();
/* 1296 */     JavaType keyType = type.getKeyType();
/* 1297 */     JavaType contentType = type.getContentType();
/*      */ 
/*      */ 
/*      */     
/* 1301 */     JsonDeserializer<Object> contentDeser = (JsonDeserializer<Object>)contentType.getValueHandler();
/*      */ 
/*      */     
/* 1304 */     KeyDeserializer keyDes = (KeyDeserializer)keyType.getValueHandler();
/*      */     
/* 1306 */     TypeDeserializer contentTypeDeser = (TypeDeserializer)contentType.getTypeHandler();
/*      */     
/* 1308 */     if (contentTypeDeser == null) {
/* 1309 */       contentTypeDeser = findTypeDeserializer(config, contentType);
/*      */     }
/*      */ 
/*      */     
/* 1313 */     JsonDeserializer<?> deser = _findCustomMapDeserializer(type, config, beanDesc, keyDes, contentTypeDeser, contentDeser);
/*      */ 
/*      */     
/* 1316 */     if (deser == null) {
/*      */       EnumMapDeserializer enumMapDeserializer;
/* 1318 */       Class<?> mapClass = type.getRawClass();
/* 1319 */       if (EnumMap.class.isAssignableFrom(mapClass)) {
/*      */         ValueInstantiator inst;
/*      */ 
/*      */ 
/*      */         
/* 1324 */         if (mapClass == EnumMap.class) {
/* 1325 */           inst = null;
/*      */         } else {
/* 1327 */           inst = findValueInstantiator(ctxt, beanDesc);
/*      */         } 
/* 1329 */         Class<?> kt = keyType.getRawClass();
/* 1330 */         if (kt == null || !kt.isEnum()) {
/* 1331 */           throw new IllegalArgumentException("Cannot construct EnumMap; generic (key) type not available");
/*      */         }
/* 1333 */         enumMapDeserializer = new EnumMapDeserializer((JavaType)type, inst, null, contentDeser, contentTypeDeser, null);
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1348 */       if (enumMapDeserializer == null) {
/* 1349 */         if (type.isInterface() || type.isAbstract()) {
/* 1350 */           MapType fallback = _mapAbstractMapType((JavaType)type, config);
/* 1351 */           if (fallback != null) {
/* 1352 */             type = fallback;
/* 1353 */             mapClass = type.getRawClass();
/*      */             
/* 1355 */             beanDesc = config.introspectForCreation((JavaType)type);
/*      */           } else {
/*      */             
/* 1358 */             if (type.getTypeHandler() == null) {
/* 1359 */               throw new IllegalArgumentException("Cannot find a deserializer for non-concrete Map type " + type);
/*      */             }
/* 1361 */             jsonDeserializer1 = AbstractDeserializer.constructForNonPOJO(beanDesc);
/*      */           } 
/*      */         } else {
/*      */           
/* 1365 */           jsonDeserializer1 = JavaUtilCollectionsDeserializers.findForMap(ctxt, (JavaType)type);
/* 1366 */           if (jsonDeserializer1 != null) {
/* 1367 */             return jsonDeserializer1;
/*      */           }
/*      */         } 
/* 1370 */         if (jsonDeserializer1 == null) {
/* 1371 */           ValueInstantiator inst = findValueInstantiator(ctxt, beanDesc);
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1376 */           MapDeserializer md = new MapDeserializer((JavaType)type, inst, keyDes, contentDeser, contentTypeDeser);
/* 1377 */           JsonIgnoreProperties.Value ignorals = config.getDefaultPropertyIgnorals(Map.class, beanDesc
/* 1378 */               .getClassInfo());
/*      */           
/* 1380 */           Set<String> ignored = (ignorals == null) ? null : ignorals.findIgnoredForDeserialization();
/* 1381 */           md.setIgnorableProperties(ignored);
/* 1382 */           mapDeserializer = md;
/*      */         } 
/*      */       } 
/*      */     } 
/* 1386 */     if (this._factoryConfig.hasDeserializerModifiers()) {
/* 1387 */       for (BeanDeserializerModifier mod : this._factoryConfig.deserializerModifiers()) {
/* 1388 */         jsonDeserializer1 = mod.modifyMapDeserializer(config, type, beanDesc, (JsonDeserializer<?>)mapDeserializer);
/*      */       }
/*      */     }
/* 1391 */     return jsonDeserializer1;
/*      */   }
/*      */ 
/*      */   
/*      */   protected MapType _mapAbstractMapType(JavaType type, DeserializationConfig config) {
/* 1396 */     Class<?> mapClass = ContainerDefaultMappings.findMapFallback(type);
/* 1397 */     if (mapClass != null) {
/* 1398 */       return (MapType)config.constructSpecializedType(type, mapClass);
/*      */     }
/* 1400 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JsonDeserializer<?> createMapLikeDeserializer(DeserializationContext ctxt, MapLikeType type, BeanDescription beanDesc) throws JsonMappingException {
/* 1410 */     JavaType keyType = type.getKeyType();
/* 1411 */     JavaType contentType = type.getContentType();
/* 1412 */     DeserializationConfig config = ctxt.getConfig();
/*      */ 
/*      */ 
/*      */     
/* 1416 */     JsonDeserializer<Object> contentDeser = (JsonDeserializer<Object>)contentType.getValueHandler();
/*      */ 
/*      */     
/* 1419 */     KeyDeserializer keyDes = (KeyDeserializer)keyType.getValueHandler();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1426 */     TypeDeserializer contentTypeDeser = (TypeDeserializer)contentType.getTypeHandler();
/*      */     
/* 1428 */     if (contentTypeDeser == null) {
/* 1429 */       contentTypeDeser = findTypeDeserializer(config, contentType);
/*      */     }
/* 1431 */     JsonDeserializer<?> deser = _findCustomMapLikeDeserializer(type, config, beanDesc, keyDes, contentTypeDeser, contentDeser);
/*      */     
/* 1433 */     if (deser != null)
/*      */     {
/* 1435 */       if (this._factoryConfig.hasDeserializerModifiers()) {
/* 1436 */         for (BeanDeserializerModifier mod : this._factoryConfig.deserializerModifiers()) {
/* 1437 */           deser = mod.modifyMapLikeDeserializer(config, type, beanDesc, deser);
/*      */         }
/*      */       }
/*      */     }
/* 1441 */     return deser;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JsonDeserializer<?> createEnumDeserializer(DeserializationContext ctxt, JavaType type, BeanDescription beanDesc) throws JsonMappingException {
/*      */     EnumDeserializer enumDeserializer;
/*      */     JsonDeserializer<?> jsonDeserializer1;
/* 1458 */     DeserializationConfig config = ctxt.getConfig();
/* 1459 */     Class<?> enumClass = type.getRawClass();
/*      */     
/* 1461 */     JsonDeserializer<?> deser = _findCustomEnumDeserializer(enumClass, config, beanDesc);
/*      */     
/* 1463 */     if (deser == null) {
/* 1464 */       ValueInstantiator valueInstantiator = _constructDefaultValueInstantiator(ctxt, beanDesc);
/*      */       
/* 1466 */       SettableBeanProperty[] creatorProps = (valueInstantiator == null) ? null : valueInstantiator.getFromObjectArguments(ctxt.getConfig());
/*      */       
/* 1468 */       for (AnnotatedMethod factory : beanDesc.getFactoryMethods()) {
/* 1469 */         if (_hasCreatorAnnotation(ctxt, (Annotated)factory)) {
/* 1470 */           if (factory.getParameterCount() == 0) {
/* 1471 */             deser = EnumDeserializer.deserializerForNoArgsCreator(config, enumClass, factory);
/*      */             break;
/*      */           } 
/* 1474 */           Class<?> returnType = factory.getRawReturnType();
/*      */           
/* 1476 */           if (returnType.isAssignableFrom(enumClass)) {
/* 1477 */             deser = EnumDeserializer.deserializerForCreator(config, enumClass, factory, valueInstantiator, creatorProps);
/*      */             
/*      */             break;
/*      */           } 
/*      */         } 
/*      */       } 
/*      */       
/* 1484 */       if (deser == null)
/*      */       {
/*      */         
/* 1487 */         enumDeserializer = new EnumDeserializer(constructEnumResolver(enumClass, config, beanDesc.findJsonValueAccessor()), Boolean.valueOf(config.isEnabled(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)));
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 1492 */     if (this._factoryConfig.hasDeserializerModifiers()) {
/* 1493 */       for (BeanDeserializerModifier mod : this._factoryConfig.deserializerModifiers()) {
/* 1494 */         jsonDeserializer1 = mod.modifyEnumDeserializer(config, type, beanDesc, (JsonDeserializer<?>)enumDeserializer);
/*      */       }
/*      */     }
/* 1497 */     return jsonDeserializer1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JsonDeserializer<?> createTreeDeserializer(DeserializationConfig config, JavaType nodeType, BeanDescription beanDesc) throws JsonMappingException {
/* 1506 */     Class<? extends JsonNode> nodeClass = nodeType.getRawClass();
/*      */     
/* 1508 */     JsonDeserializer<?> custom = _findCustomTreeNodeDeserializer(nodeClass, config, beanDesc);
/*      */     
/* 1510 */     if (custom != null) {
/* 1511 */       return custom;
/*      */     }
/* 1513 */     return JsonNodeDeserializer.getDeserializer(nodeClass);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JsonDeserializer<?> createReferenceDeserializer(DeserializationContext ctxt, ReferenceType type, BeanDescription beanDesc) throws JsonMappingException {
/* 1521 */     JavaType contentType = type.getContentType();
/*      */     
/* 1523 */     JsonDeserializer<Object> contentDeser = (JsonDeserializer<Object>)contentType.getValueHandler();
/* 1524 */     DeserializationConfig config = ctxt.getConfig();
/*      */     
/* 1526 */     TypeDeserializer contentTypeDeser = (TypeDeserializer)contentType.getTypeHandler();
/* 1527 */     if (contentTypeDeser == null) {
/* 1528 */       contentTypeDeser = findTypeDeserializer(config, contentType);
/*      */     }
/* 1530 */     JsonDeserializer<?> deser = _findCustomReferenceDeserializer(type, config, beanDesc, contentTypeDeser, contentDeser);
/*      */ 
/*      */     
/* 1533 */     if (deser == null)
/*      */     {
/* 1535 */       if (type.isTypeOrSubTypeOf(AtomicReference.class)) {
/* 1536 */         ValueInstantiator inst; Class<?> rawType = type.getRawClass();
/*      */         
/* 1538 */         if (rawType == AtomicReference.class) {
/* 1539 */           inst = null;
/*      */         
/*      */         }
/*      */         else {
/*      */ 
/*      */           
/* 1545 */           inst = findValueInstantiator(ctxt, beanDesc);
/*      */         } 
/* 1547 */         return (JsonDeserializer<?>)new AtomicReferenceDeserializer((JavaType)type, inst, contentTypeDeser, contentDeser);
/*      */       } 
/*      */     }
/* 1550 */     if (deser != null)
/*      */     {
/* 1552 */       if (this._factoryConfig.hasDeserializerModifiers()) {
/* 1553 */         for (BeanDeserializerModifier mod : this._factoryConfig.deserializerModifiers()) {
/* 1554 */           deser = mod.modifyReferenceDeserializer(config, type, beanDesc, deser);
/*      */         }
/*      */       }
/*      */     }
/* 1558 */     return deser;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public TypeDeserializer findTypeDeserializer(DeserializationConfig config, JavaType baseType) throws JsonMappingException {
/* 1572 */     BeanDescription bean = config.introspectClassAnnotations(baseType.getRawClass());
/* 1573 */     AnnotatedClass ac = bean.getClassInfo();
/* 1574 */     AnnotationIntrospector ai = config.getAnnotationIntrospector();
/* 1575 */     TypeResolverBuilder<?> b = ai.findTypeResolver((MapperConfig)config, ac, baseType);
/*      */ 
/*      */ 
/*      */     
/* 1579 */     Collection<NamedType> subtypes = null;
/* 1580 */     if (b == null) {
/* 1581 */       b = config.getDefaultTyper(baseType);
/* 1582 */       if (b == null) {
/* 1583 */         return null;
/*      */       }
/*      */     } else {
/* 1586 */       subtypes = config.getSubtypeResolver().collectAndResolveSubtypesByTypeId((MapperConfig)config, ac);
/*      */     } 
/*      */ 
/*      */     
/* 1590 */     if (b.getDefaultImpl() == null && baseType.isAbstract()) {
/* 1591 */       JavaType defaultType = mapAbstractType(config, baseType);
/* 1592 */       if (defaultType != null && !defaultType.hasRawClass(baseType.getRawClass())) {
/* 1593 */         b = b.defaultImpl(defaultType.getRawClass());
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/*      */     try {
/* 1599 */       return b.buildTypeDeserializer(config, baseType, subtypes);
/* 1600 */     } catch (IllegalArgumentException e0) {
/* 1601 */       InvalidDefinitionException e = InvalidDefinitionException.from((JsonParser)null, 
/* 1602 */           ClassUtil.exceptionMessage(e0), baseType);
/* 1603 */       e.initCause(e0);
/* 1604 */       throw e;
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
/*      */   protected JsonDeserializer<?> findOptionalStdDeserializer(DeserializationContext ctxt, JavaType type, BeanDescription beanDesc) throws JsonMappingException {
/* 1617 */     return OptionalHandlerFactory.instance.findDeserializer(type, ctxt.getConfig(), beanDesc);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public KeyDeserializer createKeyDeserializer(DeserializationContext ctxt, JavaType type) throws JsonMappingException {
/* 1631 */     DeserializationConfig config = ctxt.getConfig();
/* 1632 */     KeyDeserializer deser = null;
/* 1633 */     if (this._factoryConfig.hasKeyDeserializers()) {
/* 1634 */       BeanDescription beanDesc = config.introspectClassAnnotations(type.getRawClass());
/* 1635 */       for (KeyDeserializers d : this._factoryConfig.keyDeserializers()) {
/* 1636 */         deser = d.findKeyDeserializer(type, config, beanDesc);
/* 1637 */         if (deser != null) {
/*      */           break;
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/* 1643 */     if (deser == null) {
/* 1644 */       if (type.isEnumType()) {
/* 1645 */         deser = _createEnumKeyDeserializer(ctxt, type);
/*      */       } else {
/* 1647 */         deser = StdKeyDeserializers.findStringBasedKeyDeserializer(config, type);
/*      */       } 
/*      */     }
/*      */     
/* 1651 */     if (deser != null && 
/* 1652 */       this._factoryConfig.hasDeserializerModifiers()) {
/* 1653 */       for (BeanDeserializerModifier mod : this._factoryConfig.deserializerModifiers()) {
/* 1654 */         deser = mod.modifyKeyDeserializer(config, type, deser);
/*      */       }
/*      */     }
/*      */     
/* 1658 */     return deser;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private KeyDeserializer _createEnumKeyDeserializer(DeserializationContext ctxt, JavaType type) throws JsonMappingException {
/* 1665 */     DeserializationConfig config = ctxt.getConfig();
/* 1666 */     Class<?> enumClass = type.getRawClass();
/*      */     
/* 1668 */     BeanDescription beanDesc = config.introspect(type);
/*      */     
/* 1670 */     KeyDeserializer des = findKeyDeserializerFromAnnotation(ctxt, (Annotated)beanDesc.getClassInfo());
/* 1671 */     if (des != null) {
/* 1672 */       return des;
/*      */     }
/*      */     
/* 1675 */     JsonDeserializer<?> custom = _findCustomEnumDeserializer(enumClass, config, beanDesc);
/* 1676 */     if (custom != null) {
/* 1677 */       return StdKeyDeserializers.constructDelegatingKeyDeserializer(config, type, custom);
/*      */     }
/* 1679 */     JsonDeserializer<?> valueDesForKey = findDeserializerFromAnnotation(ctxt, (Annotated)beanDesc.getClassInfo());
/* 1680 */     if (valueDesForKey != null) {
/* 1681 */       return StdKeyDeserializers.constructDelegatingKeyDeserializer(config, type, valueDesForKey);
/*      */     }
/*      */     
/* 1684 */     EnumResolver enumRes = constructEnumResolver(enumClass, config, beanDesc.findJsonValueAccessor());
/*      */     
/* 1686 */     for (AnnotatedMethod factory : beanDesc.getFactoryMethods()) {
/* 1687 */       if (_hasCreatorAnnotation(ctxt, (Annotated)factory)) {
/* 1688 */         int argCount = factory.getParameterCount();
/* 1689 */         if (argCount == 1) {
/* 1690 */           Class<?> returnType = factory.getRawReturnType();
/*      */           
/* 1692 */           if (returnType.isAssignableFrom(enumClass)) {
/*      */             
/* 1694 */             if (factory.getRawParameterType(0) != String.class) {
/* 1695 */               throw new IllegalArgumentException("Parameter #0 type for factory method (" + factory + ") not suitable, must be java.lang.String");
/*      */             }
/* 1697 */             if (config.canOverrideAccessModifiers()) {
/* 1698 */               ClassUtil.checkAndFixAccess(factory.getMember(), ctxt
/* 1699 */                   .isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS));
/*      */             }
/* 1701 */             return StdKeyDeserializers.constructEnumKeyDeserializer(enumRes, factory);
/*      */           } 
/*      */         } 
/* 1704 */         throw new IllegalArgumentException("Unsuitable method (" + factory + ") decorated with @JsonCreator (for Enum type " + enumClass
/* 1705 */             .getName() + ")");
/*      */       } 
/*      */     } 
/*      */     
/* 1709 */     return StdKeyDeserializers.constructEnumKeyDeserializer(enumRes);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public TypeDeserializer findPropertyTypeDeserializer(DeserializationConfig config, JavaType baseType, AnnotatedMember annotated) throws JsonMappingException {
/* 1735 */     AnnotationIntrospector ai = config.getAnnotationIntrospector();
/* 1736 */     TypeResolverBuilder<?> b = ai.findPropertyTypeResolver((MapperConfig)config, annotated, baseType);
/*      */     
/* 1738 */     if (b == null) {
/* 1739 */       return findTypeDeserializer(config, baseType);
/*      */     }
/*      */     
/* 1742 */     Collection<NamedType> subtypes = config.getSubtypeResolver().collectAndResolveSubtypesByTypeId((MapperConfig)config, annotated, baseType);
/*      */     
/*      */     try {
/* 1745 */       return b.buildTypeDeserializer(config, baseType, subtypes);
/* 1746 */     } catch (IllegalArgumentException e0) {
/* 1747 */       InvalidDefinitionException e = InvalidDefinitionException.from((JsonParser)null, 
/* 1748 */           ClassUtil.exceptionMessage(e0), baseType);
/* 1749 */       e.initCause(e0);
/* 1750 */       throw e;
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
/*      */   public TypeDeserializer findPropertyContentTypeDeserializer(DeserializationConfig config, JavaType containerType, AnnotatedMember propertyEntity) throws JsonMappingException {
/* 1769 */     AnnotationIntrospector ai = config.getAnnotationIntrospector();
/* 1770 */     TypeResolverBuilder<?> b = ai.findPropertyContentTypeResolver((MapperConfig)config, propertyEntity, containerType);
/* 1771 */     JavaType contentType = containerType.getContentType();
/*      */     
/* 1773 */     if (b == null) {
/* 1774 */       return findTypeDeserializer(config, contentType);
/*      */     }
/*      */     
/* 1777 */     Collection<NamedType> subtypes = config.getSubtypeResolver().collectAndResolveSubtypesByTypeId((MapperConfig)config, propertyEntity, contentType);
/*      */     
/* 1779 */     return b.buildTypeDeserializer(config, contentType, subtypes);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JsonDeserializer<?> findDefaultDeserializer(DeserializationContext ctxt, JavaType type, BeanDescription beanDesc) throws JsonMappingException {
/* 1793 */     Class<?> rawType = type.getRawClass();
/*      */     
/* 1795 */     if (rawType == CLASS_OBJECT || rawType == CLASS_SERIALIZABLE) {
/*      */       JavaType lt, mt;
/* 1797 */       DeserializationConfig config = ctxt.getConfig();
/*      */ 
/*      */       
/* 1800 */       if (this._factoryConfig.hasAbstractTypeResolvers()) {
/* 1801 */         lt = _findRemappedType(config, List.class);
/* 1802 */         mt = _findRemappedType(config, Map.class);
/*      */       } else {
/* 1804 */         lt = mt = null;
/*      */       } 
/* 1806 */       return (JsonDeserializer<?>)new UntypedObjectDeserializer(lt, mt);
/*      */     } 
/*      */     
/* 1809 */     if (rawType == CLASS_STRING || rawType == CLASS_CHAR_SEQUENCE) {
/* 1810 */       return (JsonDeserializer<?>)StringDeserializer.instance;
/*      */     }
/* 1812 */     if (rawType == CLASS_ITERABLE) {
/*      */       
/* 1814 */       TypeFactory tf = ctxt.getTypeFactory();
/* 1815 */       JavaType[] tps = tf.findTypeParameters(type, CLASS_ITERABLE);
/* 1816 */       JavaType elemType = (tps == null || tps.length != 1) ? TypeFactory.unknownType() : tps[0];
/* 1817 */       CollectionType ct = tf.constructCollectionType(Collection.class, elemType);
/*      */       
/* 1819 */       return createCollectionDeserializer(ctxt, ct, beanDesc);
/*      */     } 
/* 1821 */     if (rawType == CLASS_MAP_ENTRY) {
/*      */       
/* 1823 */       JavaType kt = type.containedTypeOrUnknown(0);
/* 1824 */       JavaType vt = type.containedTypeOrUnknown(1);
/* 1825 */       TypeDeserializer vts = (TypeDeserializer)vt.getTypeHandler();
/* 1826 */       if (vts == null) {
/* 1827 */         vts = findTypeDeserializer(ctxt.getConfig(), vt);
/*      */       }
/* 1829 */       JsonDeserializer<Object> valueDeser = (JsonDeserializer<Object>)vt.getValueHandler();
/* 1830 */       KeyDeserializer keyDes = (KeyDeserializer)kt.getValueHandler();
/* 1831 */       return (JsonDeserializer<?>)new MapEntryDeserializer(type, keyDes, valueDeser, vts);
/*      */     } 
/* 1833 */     String clsName = rawType.getName();
/* 1834 */     if (rawType.isPrimitive() || clsName.startsWith("java.")) {
/*      */       
/* 1836 */       JsonDeserializer<?> jsonDeserializer = NumberDeserializers.find(rawType, clsName);
/* 1837 */       if (jsonDeserializer == null) {
/* 1838 */         jsonDeserializer = DateDeserializers.find(rawType, clsName);
/*      */       }
/* 1840 */       if (jsonDeserializer != null) {
/* 1841 */         return jsonDeserializer;
/*      */       }
/*      */     } 
/*      */     
/* 1845 */     if (rawType == TokenBuffer.class) {
/* 1846 */       return (JsonDeserializer<?>)new TokenBufferDeserializer();
/*      */     }
/* 1848 */     JsonDeserializer<?> deser = findOptionalStdDeserializer(ctxt, type, beanDesc);
/* 1849 */     if (deser != null) {
/* 1850 */       return deser;
/*      */     }
/* 1852 */     return JdkDeserializers.find(rawType, clsName);
/*      */   }
/*      */   
/*      */   protected JavaType _findRemappedType(DeserializationConfig config, Class<?> rawType) throws JsonMappingException {
/* 1856 */     JavaType type = mapAbstractType(config, config.constructType(rawType));
/* 1857 */     return (type == null || type.hasRawClass(rawType)) ? null : type;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected JsonDeserializer<?> _findCustomTreeNodeDeserializer(Class<? extends JsonNode> type, DeserializationConfig config, BeanDescription beanDesc) throws JsonMappingException {
/* 1870 */     for (Deserializers d : this._factoryConfig.deserializers()) {
/* 1871 */       JsonDeserializer<?> deser = d.findTreeNodeDeserializer(type, config, beanDesc);
/* 1872 */       if (deser != null) {
/* 1873 */         return deser;
/*      */       }
/*      */     } 
/* 1876 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected JsonDeserializer<?> _findCustomReferenceDeserializer(ReferenceType type, DeserializationConfig config, BeanDescription beanDesc, TypeDeserializer contentTypeDeserializer, JsonDeserializer<?> contentDeserializer) throws JsonMappingException {
/* 1884 */     for (Deserializers d : this._factoryConfig.deserializers()) {
/* 1885 */       JsonDeserializer<?> deser = d.findReferenceDeserializer(type, config, beanDesc, contentTypeDeserializer, contentDeserializer);
/*      */       
/* 1887 */       if (deser != null) {
/* 1888 */         return deser;
/*      */       }
/*      */     } 
/* 1891 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected JsonDeserializer<Object> _findCustomBeanDeserializer(JavaType type, DeserializationConfig config, BeanDescription beanDesc) throws JsonMappingException {
/* 1899 */     for (Deserializers d : this._factoryConfig.deserializers()) {
/* 1900 */       JsonDeserializer<?> deser = d.findBeanDeserializer(type, config, beanDesc);
/* 1901 */       if (deser != null) {
/* 1902 */         return (JsonDeserializer)deser;
/*      */       }
/*      */     } 
/* 1905 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected JsonDeserializer<?> _findCustomArrayDeserializer(ArrayType type, DeserializationConfig config, BeanDescription beanDesc, TypeDeserializer elementTypeDeserializer, JsonDeserializer<?> elementDeserializer) throws JsonMappingException {
/* 1913 */     for (Deserializers d : this._factoryConfig.deserializers()) {
/* 1914 */       JsonDeserializer<?> deser = d.findArrayDeserializer(type, config, beanDesc, elementTypeDeserializer, elementDeserializer);
/*      */       
/* 1916 */       if (deser != null) {
/* 1917 */         return deser;
/*      */       }
/*      */     } 
/* 1920 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected JsonDeserializer<?> _findCustomCollectionDeserializer(CollectionType type, DeserializationConfig config, BeanDescription beanDesc, TypeDeserializer elementTypeDeserializer, JsonDeserializer<?> elementDeserializer) throws JsonMappingException {
/* 1928 */     for (Deserializers d : this._factoryConfig.deserializers()) {
/* 1929 */       JsonDeserializer<?> deser = d.findCollectionDeserializer(type, config, beanDesc, elementTypeDeserializer, elementDeserializer);
/*      */       
/* 1931 */       if (deser != null) {
/* 1932 */         return deser;
/*      */       }
/*      */     } 
/* 1935 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected JsonDeserializer<?> _findCustomCollectionLikeDeserializer(CollectionLikeType type, DeserializationConfig config, BeanDescription beanDesc, TypeDeserializer elementTypeDeserializer, JsonDeserializer<?> elementDeserializer) throws JsonMappingException {
/* 1943 */     for (Deserializers d : this._factoryConfig.deserializers()) {
/* 1944 */       JsonDeserializer<?> deser = d.findCollectionLikeDeserializer(type, config, beanDesc, elementTypeDeserializer, elementDeserializer);
/*      */       
/* 1946 */       if (deser != null) {
/* 1947 */         return deser;
/*      */       }
/*      */     } 
/* 1950 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected JsonDeserializer<?> _findCustomEnumDeserializer(Class<?> type, DeserializationConfig config, BeanDescription beanDesc) throws JsonMappingException {
/* 1957 */     for (Deserializers d : this._factoryConfig.deserializers()) {
/* 1958 */       JsonDeserializer<?> deser = d.findEnumDeserializer(type, config, beanDesc);
/* 1959 */       if (deser != null) {
/* 1960 */         return deser;
/*      */       }
/*      */     } 
/* 1963 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected JsonDeserializer<?> _findCustomMapDeserializer(MapType type, DeserializationConfig config, BeanDescription beanDesc, KeyDeserializer keyDeserializer, TypeDeserializer elementTypeDeserializer, JsonDeserializer<?> elementDeserializer) throws JsonMappingException {
/* 1972 */     for (Deserializers d : this._factoryConfig.deserializers()) {
/* 1973 */       JsonDeserializer<?> deser = d.findMapDeserializer(type, config, beanDesc, keyDeserializer, elementTypeDeserializer, elementDeserializer);
/*      */       
/* 1975 */       if (deser != null) {
/* 1976 */         return deser;
/*      */       }
/*      */     } 
/* 1979 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected JsonDeserializer<?> _findCustomMapLikeDeserializer(MapLikeType type, DeserializationConfig config, BeanDescription beanDesc, KeyDeserializer keyDeserializer, TypeDeserializer elementTypeDeserializer, JsonDeserializer<?> elementDeserializer) throws JsonMappingException {
/* 1988 */     for (Deserializers d : this._factoryConfig.deserializers()) {
/* 1989 */       JsonDeserializer<?> deser = d.findMapLikeDeserializer(type, config, beanDesc, keyDeserializer, elementTypeDeserializer, elementDeserializer);
/*      */       
/* 1991 */       if (deser != null) {
/* 1992 */         return deser;
/*      */       }
/*      */     } 
/* 1995 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected JsonDeserializer<Object> findDeserializerFromAnnotation(DeserializationContext ctxt, Annotated ann) throws JsonMappingException {
/* 2016 */     AnnotationIntrospector intr = ctxt.getAnnotationIntrospector();
/* 2017 */     if (intr != null) {
/* 2018 */       Object deserDef = intr.findDeserializer(ann);
/* 2019 */       if (deserDef != null) {
/* 2020 */         return ctxt.deserializerInstance(ann, deserDef);
/*      */       }
/*      */     } 
/* 2023 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected KeyDeserializer findKeyDeserializerFromAnnotation(DeserializationContext ctxt, Annotated ann) throws JsonMappingException {
/* 2035 */     AnnotationIntrospector intr = ctxt.getAnnotationIntrospector();
/* 2036 */     if (intr != null) {
/* 2037 */       Object deserDef = intr.findKeyDeserializer(ann);
/* 2038 */       if (deserDef != null) {
/* 2039 */         return ctxt.keyDeserializerInstance(ann, deserDef);
/*      */       }
/*      */     } 
/* 2042 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected JsonDeserializer<Object> findContentDeserializerFromAnnotation(DeserializationContext ctxt, Annotated ann) throws JsonMappingException {
/* 2052 */     AnnotationIntrospector intr = ctxt.getAnnotationIntrospector();
/* 2053 */     if (intr != null) {
/* 2054 */       Object deserDef = intr.findContentDeserializer(ann);
/* 2055 */       if (deserDef != null) {
/* 2056 */         return ctxt.deserializerInstance(ann, deserDef);
/*      */       }
/*      */     } 
/* 2059 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected JavaType resolveMemberAndTypeAnnotations(DeserializationContext ctxt, AnnotatedMember member, JavaType type) throws JsonMappingException {
/*      */     MapLikeType mapLikeType;
/* 2075 */     AnnotationIntrospector intr = ctxt.getAnnotationIntrospector();
/* 2076 */     if (intr == null) {
/* 2077 */       return type;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2083 */     if (type.isMapLikeType()) {
/* 2084 */       JavaType keyType = type.getKeyType();
/* 2085 */       if (keyType != null) {
/* 2086 */         Object kdDef = intr.findKeyDeserializer((Annotated)member);
/* 2087 */         KeyDeserializer kd = ctxt.keyDeserializerInstance((Annotated)member, kdDef);
/* 2088 */         if (kd != null) {
/* 2089 */           mapLikeType = ((MapLikeType)type).withKeyValueHandler(kd);
/* 2090 */           keyType = mapLikeType.getKeyType();
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 2095 */     if (mapLikeType.hasContentType()) {
/* 2096 */       Object cdDef = intr.findContentDeserializer((Annotated)member);
/* 2097 */       JsonDeserializer<?> cd = ctxt.deserializerInstance((Annotated)member, cdDef);
/* 2098 */       if (cd != null) {
/* 2099 */         javaType = mapLikeType.withContentValueHandler(cd);
/*      */       }
/* 2101 */       TypeDeserializer contentTypeDeser = findPropertyContentTypeDeserializer(ctxt
/* 2102 */           .getConfig(), javaType, member);
/* 2103 */       if (contentTypeDeser != null) {
/* 2104 */         javaType = javaType.withContentTypeHandler(contentTypeDeser);
/*      */       }
/*      */     } 
/* 2107 */     TypeDeserializer valueTypeDeser = findPropertyTypeDeserializer(ctxt.getConfig(), javaType, member);
/*      */     
/* 2109 */     if (valueTypeDeser != null) {
/* 2110 */       javaType = javaType.withTypeHandler(valueTypeDeser);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2118 */     JavaType javaType = intr.refineDeserializationType((MapperConfig)ctxt.getConfig(), (Annotated)member, javaType);
/* 2119 */     return javaType;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected EnumResolver constructEnumResolver(Class<?> enumClass, DeserializationConfig config, AnnotatedMember jsonValueAccessor) {
/* 2125 */     if (jsonValueAccessor != null) {
/* 2126 */       if (config.canOverrideAccessModifiers()) {
/* 2127 */         ClassUtil.checkAndFixAccess(jsonValueAccessor.getMember(), config
/* 2128 */             .isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS));
/*      */       }
/* 2130 */       return EnumResolver.constructUnsafeUsingMethod(enumClass, jsonValueAccessor, config
/* 2131 */           .getAnnotationIntrospector());
/*      */     } 
/*      */ 
/*      */     
/* 2135 */     return EnumResolver.constructUnsafe(enumClass, config.getAnnotationIntrospector());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean _hasCreatorAnnotation(DeserializationContext ctxt, Annotated ann) {
/* 2143 */     AnnotationIntrospector intr = ctxt.getAnnotationIntrospector();
/* 2144 */     if (intr != null) {
/* 2145 */       JsonCreator.Mode mode = intr.findCreatorAnnotation((MapperConfig)ctxt.getConfig(), ann);
/* 2146 */       return (mode != null && mode != JsonCreator.Mode.DISABLED);
/*      */     } 
/* 2148 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   protected JavaType modifyTypeByAnnotation(DeserializationContext ctxt, Annotated a, JavaType type) throws JsonMappingException {
/* 2168 */     AnnotationIntrospector intr = ctxt.getAnnotationIntrospector();
/* 2169 */     if (intr == null) {
/* 2170 */       return type;
/*      */     }
/* 2172 */     return intr.refineDeserializationType((MapperConfig)ctxt.getConfig(), a, type);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   protected JavaType resolveType(DeserializationContext ctxt, BeanDescription beanDesc, JavaType type, AnnotatedMember member) throws JsonMappingException {
/* 2183 */     return resolveMemberAndTypeAnnotations(ctxt, member, type);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   protected AnnotatedMethod _findJsonValueFor(DeserializationConfig config, JavaType enumType) {
/* 2192 */     if (enumType == null) {
/* 2193 */       return null;
/*      */     }
/* 2195 */     BeanDescription beanDesc = config.introspect(enumType);
/* 2196 */     return beanDesc.findJsonValueMethod();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static class ContainerDefaultMappings
/*      */   {
/*      */     static final HashMap<String, Class<? extends Collection>> _collectionFallbacks;
/*      */ 
/*      */ 
/*      */     
/*      */     static final HashMap<String, Class<? extends Map>> _mapFallbacks;
/*      */ 
/*      */ 
/*      */     
/*      */     static {
/* 2213 */       HashMap<String, Class<? extends Collection>> hashMap = new HashMap<>();
/*      */       
/* 2215 */       Class<ArrayList> clazz1 = ArrayList.class;
/* 2216 */       Class<HashSet> clazz2 = HashSet.class;
/*      */       
/* 2218 */       hashMap.put(Collection.class.getName(), clazz1);
/* 2219 */       hashMap.put(List.class.getName(), clazz1);
/* 2220 */       hashMap.put(Set.class.getName(), clazz2);
/* 2221 */       hashMap.put(SortedSet.class.getName(), TreeSet.class);
/* 2222 */       hashMap.put(Queue.class.getName(), LinkedList.class);
/*      */ 
/*      */       
/* 2225 */       hashMap.put(AbstractList.class.getName(), clazz1);
/* 2226 */       hashMap.put(AbstractSet.class.getName(), clazz2);
/*      */ 
/*      */       
/* 2229 */       hashMap.put(Deque.class.getName(), LinkedList.class);
/* 2230 */       hashMap.put(NavigableSet.class.getName(), TreeSet.class);
/*      */       
/* 2232 */       _collectionFallbacks = hashMap;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2240 */       HashMap<String, Class<? extends Map>> fallbacks = new HashMap<>();
/*      */       
/* 2242 */       Class<LinkedHashMap> clazz = LinkedHashMap.class;
/* 2243 */       fallbacks.put(Map.class.getName(), clazz);
/* 2244 */       fallbacks.put(AbstractMap.class.getName(), clazz);
/* 2245 */       fallbacks.put(ConcurrentMap.class.getName(), ConcurrentHashMap.class);
/* 2246 */       fallbacks.put(SortedMap.class.getName(), TreeMap.class);
/*      */       
/* 2248 */       fallbacks.put(NavigableMap.class.getName(), TreeMap.class);
/* 2249 */       fallbacks.put(ConcurrentNavigableMap.class.getName(), ConcurrentSkipListMap.class);
/*      */ 
/*      */       
/* 2252 */       _mapFallbacks = fallbacks;
/*      */     }
/*      */     
/*      */     public static Class<?> findCollectionFallback(JavaType type) {
/* 2256 */       return _collectionFallbacks.get(type.getRawClass().getName());
/*      */     }
/*      */     
/*      */     public static Class<?> findMapFallback(JavaType type) {
/* 2260 */       return _mapFallbacks.get(type.getRawClass().getName());
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\deser\BasicDeserializerFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */