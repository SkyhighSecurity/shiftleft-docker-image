/*     */ package com.fasterxml.jackson.databind.deser;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.databind.BeanDescription;
/*     */ import com.fasterxml.jackson.databind.DeserializationConfig;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.deser.impl.BeanAsArrayBuilderDeserializer;
/*     */ import com.fasterxml.jackson.databind.deser.impl.BeanPropertyMap;
/*     */ import com.fasterxml.jackson.databind.deser.impl.ExternalTypeHandler;
/*     */ import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader;
/*     */ import com.fasterxml.jackson.databind.deser.impl.PropertyBasedCreator;
/*     */ import com.fasterxml.jackson.databind.deser.impl.PropertyValueBuffer;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
/*     */ import com.fasterxml.jackson.databind.util.NameTransformer;
/*     */ import com.fasterxml.jackson.databind.util.TokenBuffer;
/*     */ import java.io.IOException;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
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
/*     */ public class BuilderBasedDeserializer
/*     */   extends BeanDeserializerBase
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final AnnotatedMethod _buildMethod;
/*     */   protected final JavaType _targetType;
/*     */   
/*     */   public BuilderBasedDeserializer(BeanDeserializerBuilder builder, BeanDescription beanDesc, JavaType targetType, BeanPropertyMap properties, Map<String, SettableBeanProperty> backRefs, Set<String> ignorableProps, boolean ignoreAllUnknown, boolean hasViews) {
/*  53 */     super(builder, beanDesc, properties, backRefs, ignorableProps, ignoreAllUnknown, hasViews);
/*     */     
/*  55 */     this._targetType = targetType;
/*  56 */     this._buildMethod = builder.getBuildMethod();
/*     */     
/*  58 */     if (this._objectIdReader != null) {
/*  59 */       throw new IllegalArgumentException("Cannot use Object Id with Builder-based deserialization (type " + beanDesc
/*  60 */           .getType() + ")");
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
/*     */   @Deprecated
/*     */   public BuilderBasedDeserializer(BeanDeserializerBuilder builder, BeanDescription beanDesc, BeanPropertyMap properties, Map<String, SettableBeanProperty> backRefs, Set<String> ignorableProps, boolean ignoreAllUnknown, boolean hasViews) {
/*  74 */     this(builder, beanDesc, beanDesc
/*  75 */         .getType(), properties, backRefs, ignorableProps, ignoreAllUnknown, hasViews);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected BuilderBasedDeserializer(BuilderBasedDeserializer src) {
/*  85 */     this(src, src._ignoreAllUnknown);
/*     */   }
/*     */ 
/*     */   
/*     */   protected BuilderBasedDeserializer(BuilderBasedDeserializer src, boolean ignoreAllUnknown) {
/*  90 */     super(src, ignoreAllUnknown);
/*  91 */     this._buildMethod = src._buildMethod;
/*  92 */     this._targetType = src._targetType;
/*     */   }
/*     */   
/*     */   protected BuilderBasedDeserializer(BuilderBasedDeserializer src, NameTransformer unwrapper) {
/*  96 */     super(src, unwrapper);
/*  97 */     this._buildMethod = src._buildMethod;
/*  98 */     this._targetType = src._targetType;
/*     */   }
/*     */   
/*     */   public BuilderBasedDeserializer(BuilderBasedDeserializer src, ObjectIdReader oir) {
/* 102 */     super(src, oir);
/* 103 */     this._buildMethod = src._buildMethod;
/* 104 */     this._targetType = src._targetType;
/*     */   }
/*     */   
/*     */   public BuilderBasedDeserializer(BuilderBasedDeserializer src, Set<String> ignorableProps) {
/* 108 */     super(src, ignorableProps);
/* 109 */     this._buildMethod = src._buildMethod;
/* 110 */     this._targetType = src._targetType;
/*     */   }
/*     */   
/*     */   public BuilderBasedDeserializer(BuilderBasedDeserializer src, BeanPropertyMap props) {
/* 114 */     super(src, props);
/* 115 */     this._buildMethod = src._buildMethod;
/* 116 */     this._targetType = src._targetType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonDeserializer<Object> unwrappingDeserializer(NameTransformer unwrapper) {
/* 126 */     return (JsonDeserializer<Object>)new BuilderBasedDeserializer(this, unwrapper);
/*     */   }
/*     */ 
/*     */   
/*     */   public BeanDeserializerBase withObjectIdReader(ObjectIdReader oir) {
/* 131 */     return new BuilderBasedDeserializer(this, oir);
/*     */   }
/*     */ 
/*     */   
/*     */   public BeanDeserializerBase withIgnorableProperties(Set<String> ignorableProps) {
/* 136 */     return new BuilderBasedDeserializer(this, ignorableProps);
/*     */   }
/*     */ 
/*     */   
/*     */   public BeanDeserializerBase withBeanProperties(BeanPropertyMap props) {
/* 141 */     return new BuilderBasedDeserializer(this, props);
/*     */   }
/*     */ 
/*     */   
/*     */   protected BeanDeserializerBase asArrayDeserializer() {
/* 146 */     SettableBeanProperty[] props = this._beanProperties.getPropertiesInInsertionOrder();
/* 147 */     return (BeanDeserializerBase)new BeanAsArrayBuilderDeserializer(this, this._targetType, props, this._buildMethod);
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
/*     */   public Boolean supportsUpdate(DeserializationConfig config) {
/* 159 */     return Boolean.FALSE;
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
/*     */   protected Object finishBuild(DeserializationContext ctxt, Object builder) throws IOException {
/* 172 */     if (null == this._buildMethod) {
/* 173 */       return builder;
/*     */     }
/*     */     try {
/* 176 */       return this._buildMethod.getMember().invoke(builder, (Object[])null);
/* 177 */     } catch (Exception e) {
/* 178 */       return wrapInstantiationProblem(e, ctxt);
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
/*     */   public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
/* 190 */     if (p.isExpectedStartObjectToken()) {
/* 191 */       JsonToken t = p.nextToken();
/* 192 */       if (this._vanillaProcessing) {
/* 193 */         return finishBuild(ctxt, vanillaDeserialize(p, ctxt, t));
/*     */       }
/* 195 */       Object builder = deserializeFromObject(p, ctxt);
/* 196 */       return finishBuild(ctxt, builder);
/*     */     } 
/*     */     
/* 199 */     switch (p.getCurrentTokenId()) {
/*     */       case 6:
/* 201 */         return finishBuild(ctxt, deserializeFromString(p, ctxt));
/*     */       case 7:
/* 203 */         return finishBuild(ctxt, deserializeFromNumber(p, ctxt));
/*     */       case 8:
/* 205 */         return finishBuild(ctxt, deserializeFromDouble(p, ctxt));
/*     */       case 12:
/* 207 */         return p.getEmbeddedObject();
/*     */       case 9:
/*     */       case 10:
/* 210 */         return finishBuild(ctxt, deserializeFromBoolean(p, ctxt));
/*     */       
/*     */       case 3:
/* 213 */         return finishBuild(ctxt, deserializeFromArray(p, ctxt));
/*     */       case 2:
/*     */       case 5:
/* 216 */         return finishBuild(ctxt, deserializeFromObject(p, ctxt));
/*     */     } 
/*     */     
/* 219 */     return ctxt.handleUnexpectedToken(getValueType(ctxt), p);
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
/*     */   public Object deserialize(JsonParser p, DeserializationContext ctxt, Object value) throws IOException {
/* 233 */     JavaType valueType = this._targetType;
/*     */     
/* 235 */     Class<?> builderRawType = handledType();
/* 236 */     Class<?> instRawType = value.getClass();
/* 237 */     if (builderRawType.isAssignableFrom(instRawType)) {
/* 238 */       return ctxt.reportBadDefinition(valueType, String.format("Deserialization of %s by passing existing Builder (%s) instance not supported", new Object[] { valueType, builderRawType
/*     */               
/* 240 */               .getName() }));
/*     */     }
/* 242 */     return ctxt.reportBadDefinition(valueType, String.format("Deserialization of %s by passing existing instance (of %s) not supported", new Object[] { valueType, instRawType
/*     */             
/* 244 */             .getName() }));
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
/*     */   private final Object vanillaDeserialize(JsonParser p, DeserializationContext ctxt, JsonToken t) throws IOException {
/* 261 */     Object bean = this._valueInstantiator.createUsingDefault(ctxt);
/* 262 */     for (; p.getCurrentToken() == JsonToken.FIELD_NAME; p.nextToken()) {
/* 263 */       String propName = p.getCurrentName();
/*     */       
/* 265 */       p.nextToken();
/* 266 */       SettableBeanProperty prop = this._beanProperties.find(propName);
/* 267 */       if (prop != null) {
/*     */         try {
/* 269 */           bean = prop.deserializeSetAndReturn(p, ctxt, bean);
/* 270 */         } catch (Exception e) {
/* 271 */           wrapAndThrow(e, bean, propName, ctxt);
/*     */         } 
/*     */       } else {
/* 274 */         handleUnknownVanilla(p, ctxt, bean, propName);
/*     */       } 
/*     */     } 
/* 277 */     return bean;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object deserializeFromObject(JsonParser p, DeserializationContext ctxt) throws IOException {
/* 288 */     if (this._nonStandardCreation) {
/* 289 */       if (this._unwrappedPropertyHandler != null) {
/* 290 */         return deserializeWithUnwrapped(p, ctxt);
/*     */       }
/* 292 */       if (this._externalTypeIdHandler != null) {
/* 293 */         return deserializeWithExternalTypeId(p, ctxt);
/*     */       }
/* 295 */       return deserializeFromObjectUsingNonDefault(p, ctxt);
/*     */     } 
/* 297 */     Object bean = this._valueInstantiator.createUsingDefault(ctxt);
/* 298 */     if (this._injectables != null) {
/* 299 */       injectValues(ctxt, bean);
/*     */     }
/* 301 */     if (this._needViewProcesing) {
/* 302 */       Class<?> view = ctxt.getActiveView();
/* 303 */       if (view != null) {
/* 304 */         return deserializeWithView(p, ctxt, bean, view);
/*     */       }
/*     */     } 
/* 307 */     for (; p.getCurrentToken() == JsonToken.FIELD_NAME; p.nextToken()) {
/* 308 */       String propName = p.getCurrentName();
/*     */       
/* 310 */       p.nextToken();
/* 311 */       SettableBeanProperty prop = this._beanProperties.find(propName);
/* 312 */       if (prop != null) {
/*     */         try {
/* 314 */           bean = prop.deserializeSetAndReturn(p, ctxt, bean);
/* 315 */         } catch (Exception e) {
/* 316 */           wrapAndThrow(e, bean, propName, ctxt);
/*     */         } 
/*     */       } else {
/*     */         
/* 320 */         handleUnknownVanilla(p, ctxt, bean, propName);
/*     */       } 
/* 322 */     }  return bean;
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
/*     */   protected Object _deserializeUsingPropertyBased(JsonParser p, DeserializationContext ctxt) throws IOException {
/*     */     Object builder;
/* 341 */     PropertyBasedCreator creator = this._propertyBasedCreator;
/* 342 */     PropertyValueBuffer buffer = creator.startBuilding(p, ctxt, this._objectIdReader);
/* 343 */     Class<?> activeView = this._needViewProcesing ? ctxt.getActiveView() : null;
/*     */ 
/*     */     
/* 346 */     TokenBuffer unknown = null;
/*     */     
/* 348 */     JsonToken t = p.getCurrentToken();
/* 349 */     for (; t == JsonToken.FIELD_NAME; t = p.nextToken()) {
/* 350 */       String propName = p.getCurrentName();
/* 351 */       p.nextToken();
/*     */       
/* 353 */       SettableBeanProperty creatorProp = creator.findCreatorProperty(propName);
/* 354 */       if (creatorProp != null) {
/* 355 */         if (activeView != null && !creatorProp.visibleInView(activeView)) {
/* 356 */           p.skipChildren();
/*     */ 
/*     */         
/*     */         }
/* 360 */         else if (buffer.assignParameter(creatorProp, creatorProp.deserialize(p, ctxt))) {
/* 361 */           Object object; p.nextToken();
/*     */           
/*     */           try {
/* 364 */             object = creator.build(ctxt, buffer);
/* 365 */           } catch (Exception e) {
/* 366 */             wrapAndThrow(e, this._beanType.getRawClass(), propName, ctxt);
/*     */           } 
/*     */ 
/*     */           
/* 370 */           if (object.getClass() != this._beanType.getRawClass()) {
/* 371 */             return handlePolymorphic(p, ctxt, object, unknown);
/*     */           }
/* 373 */           if (unknown != null) {
/* 374 */             object = handleUnknownProperties(ctxt, object, unknown);
/*     */           }
/*     */           
/* 377 */           return _deserialize(p, ctxt, object);
/*     */         
/*     */         }
/*     */       
/*     */       }
/* 382 */       else if (!buffer.readIdProperty(propName)) {
/*     */ 
/*     */ 
/*     */         
/* 386 */         SettableBeanProperty prop = this._beanProperties.find(propName);
/* 387 */         if (prop != null) {
/* 388 */           buffer.bufferProperty(prop, prop.deserialize(p, ctxt));
/*     */ 
/*     */ 
/*     */         
/*     */         }
/* 393 */         else if (this._ignorableProps != null && this._ignorableProps.contains(propName)) {
/* 394 */           handleIgnoredProperty(p, ctxt, handledType(), propName);
/*     */ 
/*     */         
/*     */         }
/* 398 */         else if (this._anySetter != null) {
/* 399 */           buffer.bufferAnyProperty(this._anySetter, propName, this._anySetter.deserialize(p, ctxt));
/*     */         }
/*     */         else {
/*     */           
/* 403 */           if (unknown == null) {
/* 404 */             unknown = new TokenBuffer(p, ctxt);
/*     */           }
/* 406 */           unknown.writeFieldName(propName);
/* 407 */           unknown.copyCurrentStructure(p);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/*     */     try {
/* 413 */       builder = creator.build(ctxt, buffer);
/* 414 */     } catch (Exception e) {
/* 415 */       builder = wrapInstantiationProblem(e, ctxt);
/*     */     } 
/* 417 */     if (unknown != null) {
/*     */       
/* 419 */       if (builder.getClass() != this._beanType.getRawClass()) {
/* 420 */         return handlePolymorphic((JsonParser)null, ctxt, builder, unknown);
/*     */       }
/*     */       
/* 423 */       return handleUnknownProperties(ctxt, builder, unknown);
/*     */     } 
/* 425 */     return builder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final Object _deserialize(JsonParser p, DeserializationContext ctxt, Object builder) throws IOException {
/* 432 */     if (this._injectables != null) {
/* 433 */       injectValues(ctxt, builder);
/*     */     }
/* 435 */     if (this._unwrappedPropertyHandler != null) {
/* 436 */       if (p.hasToken(JsonToken.START_OBJECT)) {
/* 437 */         p.nextToken();
/*     */       }
/* 439 */       TokenBuffer tokens = new TokenBuffer(p, ctxt);
/* 440 */       tokens.writeStartObject();
/* 441 */       return deserializeWithUnwrapped(p, ctxt, builder, tokens);
/*     */     } 
/* 443 */     if (this._externalTypeIdHandler != null) {
/* 444 */       return deserializeWithExternalTypeId(p, ctxt, builder);
/*     */     }
/* 446 */     if (this._needViewProcesing) {
/* 447 */       Class<?> view = ctxt.getActiveView();
/* 448 */       if (view != null) {
/* 449 */         return deserializeWithView(p, ctxt, builder, view);
/*     */       }
/*     */     } 
/* 452 */     JsonToken t = p.getCurrentToken();
/*     */     
/* 454 */     if (t == JsonToken.START_OBJECT) {
/* 455 */       t = p.nextToken();
/*     */     }
/* 457 */     for (; t == JsonToken.FIELD_NAME; t = p.nextToken()) {
/* 458 */       String propName = p.getCurrentName();
/*     */       
/* 460 */       p.nextToken();
/* 461 */       SettableBeanProperty prop = this._beanProperties.find(propName);
/*     */       
/* 463 */       if (prop != null) {
/*     */         try {
/* 465 */           builder = prop.deserializeSetAndReturn(p, ctxt, builder);
/* 466 */         } catch (Exception e) {
/* 467 */           wrapAndThrow(e, builder, propName, ctxt);
/*     */         } 
/*     */       } else {
/*     */         
/* 471 */         handleUnknownVanilla(p, ctxt, builder, propName);
/*     */       } 
/* 473 */     }  return builder;
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
/*     */   protected final Object deserializeWithView(JsonParser p, DeserializationContext ctxt, Object bean, Class<?> activeView) throws IOException {
/* 486 */     JsonToken t = p.getCurrentToken();
/* 487 */     for (; t == JsonToken.FIELD_NAME; t = p.nextToken()) {
/* 488 */       String propName = p.getCurrentName();
/*     */       
/* 490 */       p.nextToken();
/* 491 */       SettableBeanProperty prop = this._beanProperties.find(propName);
/* 492 */       if (prop != null) {
/* 493 */         if (!prop.visibleInView(activeView)) {
/* 494 */           p.skipChildren();
/*     */         } else {
/*     */           
/*     */           try {
/* 498 */             bean = prop.deserializeSetAndReturn(p, ctxt, bean);
/* 499 */           } catch (Exception e) {
/* 500 */             wrapAndThrow(e, bean, propName, ctxt);
/*     */           } 
/*     */         } 
/*     */       } else {
/* 504 */         handleUnknownVanilla(p, ctxt, bean, propName);
/*     */       } 
/* 506 */     }  return bean;
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
/*     */   protected Object deserializeWithUnwrapped(JsonParser p, DeserializationContext ctxt) throws IOException {
/* 523 */     if (this._delegateDeserializer != null) {
/* 524 */       return this._valueInstantiator.createUsingDelegate(ctxt, this._delegateDeserializer.deserialize(p, ctxt));
/*     */     }
/* 526 */     if (this._propertyBasedCreator != null) {
/* 527 */       return deserializeUsingPropertyBasedWithUnwrapped(p, ctxt);
/*     */     }
/* 529 */     TokenBuffer tokens = new TokenBuffer(p, ctxt);
/* 530 */     tokens.writeStartObject();
/* 531 */     Object bean = this._valueInstantiator.createUsingDefault(ctxt);
/*     */     
/* 533 */     if (this._injectables != null) {
/* 534 */       injectValues(ctxt, bean);
/*     */     }
/*     */     
/* 537 */     Class<?> activeView = this._needViewProcesing ? ctxt.getActiveView() : null;
/* 538 */     for (; p.getCurrentToken() == JsonToken.FIELD_NAME; p.nextToken()) {
/* 539 */       String propName = p.getCurrentName();
/* 540 */       p.nextToken();
/* 541 */       SettableBeanProperty prop = this._beanProperties.find(propName);
/* 542 */       if (prop != null) {
/* 543 */         if (activeView != null && !prop.visibleInView(activeView)) {
/* 544 */           p.skipChildren();
/*     */         } else {
/*     */           
/*     */           try {
/* 548 */             bean = prop.deserializeSetAndReturn(p, ctxt, bean);
/* 549 */           } catch (Exception e) {
/* 550 */             wrapAndThrow(e, bean, propName, ctxt);
/*     */           }
/*     */         
/*     */         }
/*     */       
/* 555 */       } else if (this._ignorableProps != null && this._ignorableProps.contains(propName)) {
/* 556 */         handleIgnoredProperty(p, ctxt, bean, propName);
/*     */       }
/*     */       else {
/*     */         
/* 560 */         tokens.writeFieldName(propName);
/* 561 */         tokens.copyCurrentStructure(p);
/*     */         
/* 563 */         if (this._anySetter != null) {
/*     */           try {
/* 565 */             this._anySetter.deserializeAndSet(p, ctxt, bean, propName);
/* 566 */           } catch (Exception e) {
/* 567 */             wrapAndThrow(e, bean, propName, ctxt);
/*     */           } 
/*     */         }
/*     */       } 
/*     */     } 
/* 572 */     tokens.writeEndObject();
/* 573 */     return this._unwrappedPropertyHandler.processUnwrapped(p, ctxt, bean, tokens);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object deserializeUsingPropertyBasedWithUnwrapped(JsonParser p, DeserializationContext ctxt) throws IOException {
/* 581 */     PropertyBasedCreator creator = this._propertyBasedCreator;
/* 582 */     PropertyValueBuffer buffer = creator.startBuilding(p, ctxt, this._objectIdReader);
/*     */     
/* 584 */     TokenBuffer tokens = new TokenBuffer(p, ctxt);
/* 585 */     tokens.writeStartObject();
/* 586 */     Object builder = null;
/*     */     
/* 588 */     JsonToken t = p.getCurrentToken();
/* 589 */     for (; t == JsonToken.FIELD_NAME; t = p.nextToken()) {
/* 590 */       String propName = p.getCurrentName();
/* 591 */       p.nextToken();
/*     */       
/* 593 */       SettableBeanProperty creatorProp = creator.findCreatorProperty(propName);
/* 594 */       if (creatorProp != null) {
/*     */         
/* 596 */         if (buffer.assignParameter(creatorProp, creatorProp.deserialize(p, ctxt))) {
/* 597 */           t = p.nextToken();
/*     */           try {
/* 599 */             builder = creator.build(ctxt, buffer);
/* 600 */           } catch (Exception e) {
/* 601 */             wrapAndThrow(e, this._beanType.getRawClass(), propName, ctxt);
/*     */           } 
/*     */           
/* 604 */           if (builder.getClass() != this._beanType.getRawClass()) {
/* 605 */             return handlePolymorphic(p, ctxt, builder, tokens);
/*     */           }
/* 607 */           return deserializeWithUnwrapped(p, ctxt, builder, tokens);
/*     */         
/*     */         }
/*     */       
/*     */       }
/* 612 */       else if (!buffer.readIdProperty(propName)) {
/*     */ 
/*     */ 
/*     */         
/* 616 */         SettableBeanProperty prop = this._beanProperties.find(propName);
/* 617 */         if (prop != null) {
/* 618 */           buffer.bufferProperty(prop, prop.deserialize(p, ctxt));
/*     */         
/*     */         }
/* 621 */         else if (this._ignorableProps != null && this._ignorableProps.contains(propName)) {
/* 622 */           handleIgnoredProperty(p, ctxt, handledType(), propName);
/*     */         } else {
/*     */           
/* 625 */           tokens.writeFieldName(propName);
/* 626 */           tokens.copyCurrentStructure(p);
/*     */           
/* 628 */           if (this._anySetter != null)
/* 629 */             buffer.bufferAnyProperty(this._anySetter, propName, this._anySetter.deserialize(p, ctxt)); 
/*     */         } 
/*     */       } 
/* 632 */     }  tokens.writeEndObject();
/*     */ 
/*     */     
/* 635 */     if (builder == null) {
/*     */       try {
/* 637 */         builder = creator.build(ctxt, buffer);
/* 638 */       } catch (Exception e) {
/* 639 */         return wrapInstantiationProblem(e, ctxt);
/*     */       } 
/*     */     }
/* 642 */     return this._unwrappedPropertyHandler.processUnwrapped(p, ctxt, builder, tokens);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object deserializeWithUnwrapped(JsonParser p, DeserializationContext ctxt, Object builder, TokenBuffer tokens) throws IOException {
/* 649 */     Class<?> activeView = this._needViewProcesing ? ctxt.getActiveView() : null;
/* 650 */     for (JsonToken t = p.getCurrentToken(); t == JsonToken.FIELD_NAME; t = p.nextToken()) {
/* 651 */       String propName = p.getCurrentName();
/* 652 */       SettableBeanProperty prop = this._beanProperties.find(propName);
/* 653 */       p.nextToken();
/* 654 */       if (prop != null) {
/* 655 */         if (activeView != null && !prop.visibleInView(activeView)) {
/* 656 */           p.skipChildren();
/*     */         } else {
/*     */           
/*     */           try {
/* 660 */             builder = prop.deserializeSetAndReturn(p, ctxt, builder);
/* 661 */           } catch (Exception e) {
/* 662 */             wrapAndThrow(e, builder, propName, ctxt);
/*     */           }
/*     */         
/*     */         } 
/* 666 */       } else if (this._ignorableProps != null && this._ignorableProps.contains(propName)) {
/* 667 */         handleIgnoredProperty(p, ctxt, builder, propName);
/*     */       }
/*     */       else {
/*     */         
/* 671 */         tokens.writeFieldName(propName);
/* 672 */         tokens.copyCurrentStructure(p);
/*     */         
/* 674 */         if (this._anySetter != null)
/* 675 */           this._anySetter.deserializeAndSet(p, ctxt, builder, propName); 
/*     */       } 
/*     */     } 
/* 678 */     tokens.writeEndObject();
/* 679 */     return this._unwrappedPropertyHandler.processUnwrapped(p, ctxt, builder, tokens);
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
/*     */   protected Object deserializeWithExternalTypeId(JsonParser p, DeserializationContext ctxt) throws IOException {
/* 692 */     if (this._propertyBasedCreator != null) {
/* 693 */       return deserializeUsingPropertyBasedWithExternalTypeId(p, ctxt);
/*     */     }
/* 695 */     return deserializeWithExternalTypeId(p, ctxt, this._valueInstantiator.createUsingDefault(ctxt));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object deserializeWithExternalTypeId(JsonParser p, DeserializationContext ctxt, Object bean) throws IOException {
/* 702 */     Class<?> activeView = this._needViewProcesing ? ctxt.getActiveView() : null;
/* 703 */     ExternalTypeHandler ext = this._externalTypeIdHandler.start();
/*     */     
/* 705 */     for (JsonToken t = p.getCurrentToken(); t == JsonToken.FIELD_NAME; t = p.nextToken()) {
/* 706 */       String propName = p.getCurrentName();
/* 707 */       t = p.nextToken();
/* 708 */       SettableBeanProperty prop = this._beanProperties.find(propName);
/* 709 */       if (prop != null) {
/*     */         
/* 711 */         if (t.isScalarValue()) {
/* 712 */           ext.handleTypePropertyValue(p, ctxt, propName, bean);
/*     */         }
/* 714 */         if (activeView != null && !prop.visibleInView(activeView)) {
/* 715 */           p.skipChildren();
/*     */         } else {
/*     */           
/*     */           try {
/* 719 */             bean = prop.deserializeSetAndReturn(p, ctxt, bean);
/* 720 */           } catch (Exception e) {
/* 721 */             wrapAndThrow(e, bean, propName, ctxt);
/*     */           }
/*     */         
/*     */         }
/*     */       
/* 726 */       } else if (this._ignorableProps != null && this._ignorableProps.contains(propName)) {
/* 727 */         handleIgnoredProperty(p, ctxt, bean, propName);
/*     */ 
/*     */       
/*     */       }
/* 731 */       else if (!ext.handlePropertyValue(p, ctxt, propName, bean)) {
/*     */ 
/*     */ 
/*     */         
/* 735 */         if (this._anySetter != null) {
/*     */           try {
/* 737 */             this._anySetter.deserializeAndSet(p, ctxt, bean, propName);
/* 738 */           } catch (Exception e) {
/* 739 */             wrapAndThrow(e, bean, propName, ctxt);
/*     */           }
/*     */         
/*     */         } else {
/*     */           
/* 744 */           handleUnknownProperty(p, ctxt, bean, propName);
/*     */         } 
/*     */       } 
/*     */     } 
/* 748 */     return ext.complete(p, ctxt, bean);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object deserializeUsingPropertyBasedWithExternalTypeId(JsonParser p, DeserializationContext ctxt) throws IOException {
/* 756 */     JavaType t = this._targetType;
/* 757 */     return ctxt.reportBadDefinition(t, String.format("Deserialization (of %s) with Builder, External type id, @JsonCreator not yet implemented", new Object[] { t }));
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\deser\BuilderBasedDeserializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */