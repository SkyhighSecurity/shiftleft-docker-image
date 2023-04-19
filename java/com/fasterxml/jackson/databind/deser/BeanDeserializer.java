/*      */ package com.fasterxml.jackson.databind.deser;
/*      */ 
/*      */ import com.fasterxml.jackson.core.JsonParser;
/*      */ import com.fasterxml.jackson.core.JsonToken;
/*      */ import com.fasterxml.jackson.databind.BeanDescription;
/*      */ import com.fasterxml.jackson.databind.BeanProperty;
/*      */ import com.fasterxml.jackson.databind.DeserializationContext;
/*      */ import com.fasterxml.jackson.databind.JavaType;
/*      */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*      */ import com.fasterxml.jackson.databind.JsonMappingException;
/*      */ import com.fasterxml.jackson.databind.deser.impl.BeanAsArrayDeserializer;
/*      */ import com.fasterxml.jackson.databind.deser.impl.BeanPropertyMap;
/*      */ import com.fasterxml.jackson.databind.deser.impl.ExternalTypeHandler;
/*      */ import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader;
/*      */ import com.fasterxml.jackson.databind.deser.impl.PropertyBasedCreator;
/*      */ import com.fasterxml.jackson.databind.deser.impl.PropertyValueBuffer;
/*      */ import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
/*      */ import com.fasterxml.jackson.databind.util.NameTransformer;
/*      */ import com.fasterxml.jackson.databind.util.TokenBuffer;
/*      */ import java.io.IOException;
/*      */ import java.io.Serializable;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashSet;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class BeanDeserializer
/*      */   extends BeanDeserializerBase
/*      */   implements Serializable
/*      */ {
/*      */   private static final long serialVersionUID = 1L;
/*      */   protected transient Exception _nullFromCreator;
/*      */   private volatile transient NameTransformer _currentlyTransforming;
/*      */   
/*      */   public BeanDeserializer(BeanDeserializerBuilder builder, BeanDescription beanDesc, BeanPropertyMap properties, Map<String, SettableBeanProperty> backRefs, HashSet<String> ignorableProps, boolean ignoreAllUnknown, boolean hasViews) {
/*   64 */     super(builder, beanDesc, properties, backRefs, ignorableProps, ignoreAllUnknown, hasViews);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected BeanDeserializer(BeanDeserializerBase src) {
/*   73 */     super(src, src._ignoreAllUnknown);
/*      */   }
/*      */   
/*      */   protected BeanDeserializer(BeanDeserializerBase src, boolean ignoreAllUnknown) {
/*   77 */     super(src, ignoreAllUnknown);
/*      */   }
/*      */   
/*      */   protected BeanDeserializer(BeanDeserializerBase src, NameTransformer unwrapper) {
/*   81 */     super(src, unwrapper);
/*      */   }
/*      */   
/*      */   public BeanDeserializer(BeanDeserializerBase src, ObjectIdReader oir) {
/*   85 */     super(src, oir);
/*      */   }
/*      */   
/*      */   public BeanDeserializer(BeanDeserializerBase src, Set<String> ignorableProps) {
/*   89 */     super(src, ignorableProps);
/*      */   }
/*      */   
/*      */   public BeanDeserializer(BeanDeserializerBase src, BeanPropertyMap props) {
/*   93 */     super(src, props);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JsonDeserializer<Object> unwrappingDeserializer(NameTransformer transformer) {
/*  101 */     if (getClass() != BeanDeserializer.class) {
/*  102 */       return (JsonDeserializer<Object>)this;
/*      */     }
/*      */ 
/*      */     
/*  106 */     if (this._currentlyTransforming == transformer) {
/*  107 */       return (JsonDeserializer<Object>)this;
/*      */     }
/*  109 */     this._currentlyTransforming = transformer;
/*      */     
/*  111 */     try { return (JsonDeserializer<Object>)new BeanDeserializer(this, transformer); }
/*  112 */     finally { this._currentlyTransforming = null; }
/*      */   
/*      */   }
/*      */   
/*      */   public BeanDeserializer withObjectIdReader(ObjectIdReader oir) {
/*  117 */     return new BeanDeserializer(this, oir);
/*      */   }
/*      */ 
/*      */   
/*      */   public BeanDeserializer withIgnorableProperties(Set<String> ignorableProps) {
/*  122 */     return new BeanDeserializer(this, ignorableProps);
/*      */   }
/*      */ 
/*      */   
/*      */   public BeanDeserializerBase withBeanProperties(BeanPropertyMap props) {
/*  127 */     return new BeanDeserializer(this, props);
/*      */   }
/*      */ 
/*      */   
/*      */   protected BeanDeserializerBase asArrayDeserializer() {
/*  132 */     SettableBeanProperty[] props = this._beanProperties.getPropertiesInInsertionOrder();
/*  133 */     return (BeanDeserializerBase)new BeanAsArrayDeserializer(this, props);
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
/*      */   public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
/*  149 */     if (p.isExpectedStartObjectToken()) {
/*  150 */       if (this._vanillaProcessing) {
/*  151 */         return vanillaDeserialize(p, ctxt, p.nextToken());
/*      */       }
/*      */ 
/*      */       
/*  155 */       p.nextToken();
/*  156 */       if (this._objectIdReader != null) {
/*  157 */         return deserializeWithObjectId(p, ctxt);
/*      */       }
/*  159 */       return deserializeFromObject(p, ctxt);
/*      */     } 
/*  161 */     return _deserializeOther(p, ctxt, p.getCurrentToken());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final Object _deserializeOther(JsonParser p, DeserializationContext ctxt, JsonToken t) throws IOException {
/*  168 */     if (t != null) {
/*  169 */       switch (t) {
/*      */         case VALUE_STRING:
/*  171 */           return deserializeFromString(p, ctxt);
/*      */         case VALUE_NUMBER_INT:
/*  173 */           return deserializeFromNumber(p, ctxt);
/*      */         case VALUE_NUMBER_FLOAT:
/*  175 */           return deserializeFromDouble(p, ctxt);
/*      */         case VALUE_EMBEDDED_OBJECT:
/*  177 */           return deserializeFromEmbedded(p, ctxt);
/*      */         case VALUE_TRUE:
/*      */         case VALUE_FALSE:
/*  180 */           return deserializeFromBoolean(p, ctxt);
/*      */         case VALUE_NULL:
/*  182 */           return deserializeFromNull(p, ctxt);
/*      */         
/*      */         case START_ARRAY:
/*  185 */           return deserializeFromArray(p, ctxt);
/*      */         case FIELD_NAME:
/*      */         case END_OBJECT:
/*  188 */           if (this._vanillaProcessing) {
/*  189 */             return vanillaDeserialize(p, ctxt, t);
/*      */           }
/*  191 */           if (this._objectIdReader != null) {
/*  192 */             return deserializeWithObjectId(p, ctxt);
/*      */           }
/*  194 */           return deserializeFromObject(p, ctxt);
/*      */       } 
/*      */     
/*      */     }
/*  198 */     return ctxt.handleUnexpectedToken(getValueType(ctxt), p);
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   protected Object _missingToken(JsonParser p, DeserializationContext ctxt) throws IOException {
/*  203 */     throw ctxt.endOfInputException(handledType());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object deserialize(JsonParser p, DeserializationContext ctxt, Object bean) throws IOException {
/*      */     String propName;
/*  215 */     p.setCurrentValue(bean);
/*  216 */     if (this._injectables != null) {
/*  217 */       injectValues(ctxt, bean);
/*      */     }
/*  219 */     if (this._unwrappedPropertyHandler != null) {
/*  220 */       return deserializeWithUnwrapped(p, ctxt, bean);
/*      */     }
/*  222 */     if (this._externalTypeIdHandler != null) {
/*  223 */       return deserializeWithExternalTypeId(p, ctxt, bean);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  228 */     if (p.isExpectedStartObjectToken()) {
/*  229 */       propName = p.nextFieldName();
/*  230 */       if (propName == null) {
/*  231 */         return bean;
/*      */       }
/*      */     }
/*  234 */     else if (p.hasTokenId(5)) {
/*  235 */       propName = p.getCurrentName();
/*      */     } else {
/*  237 */       return bean;
/*      */     } 
/*      */     
/*  240 */     if (this._needViewProcesing) {
/*  241 */       Class<?> view = ctxt.getActiveView();
/*  242 */       if (view != null) {
/*  243 */         return deserializeWithView(p, ctxt, bean, view);
/*      */       }
/*      */     } 
/*      */     while (true) {
/*  247 */       p.nextToken();
/*  248 */       SettableBeanProperty prop = this._beanProperties.find(propName);
/*      */       
/*  250 */       if (prop != null) {
/*      */         try {
/*  252 */           prop.deserializeAndSet(p, ctxt, bean);
/*  253 */         } catch (Exception e) {
/*  254 */           wrapAndThrow(e, bean, propName, ctxt);
/*      */         } 
/*      */       } else {
/*      */         
/*  258 */         handleUnknownVanilla(p, ctxt, bean, propName);
/*  259 */       }  if ((propName = p.nextFieldName()) == null) {
/*  260 */         return bean;
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
/*      */   private final Object vanillaDeserialize(JsonParser p, DeserializationContext ctxt, JsonToken t) throws IOException {
/*  277 */     Object bean = this._valueInstantiator.createUsingDefault(ctxt);
/*      */     
/*  279 */     p.setCurrentValue(bean);
/*  280 */     if (p.hasTokenId(5)) {
/*  281 */       String propName = p.getCurrentName();
/*      */       do {
/*  283 */         p.nextToken();
/*  284 */         SettableBeanProperty prop = this._beanProperties.find(propName);
/*      */         
/*  286 */         if (prop != null)
/*      */         { try {
/*  288 */             prop.deserializeAndSet(p, ctxt, bean);
/*  289 */           } catch (Exception e) {
/*  290 */             wrapAndThrow(e, bean, propName, ctxt);
/*      */           }  }
/*      */         else
/*      */         
/*  294 */         { handleUnknownVanilla(p, ctxt, bean, propName); } 
/*  295 */       } while ((propName = p.nextFieldName()) != null);
/*      */     } 
/*  297 */     return bean;
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
/*      */   public Object deserializeFromObject(JsonParser p, DeserializationContext ctxt) throws IOException {
/*  313 */     if (this._objectIdReader != null && this._objectIdReader.maySerializeAsObject() && 
/*  314 */       p.hasTokenId(5) && this._objectIdReader
/*  315 */       .isValidReferencePropertyName(p.getCurrentName(), p)) {
/*  316 */       return deserializeFromObjectId(p, ctxt);
/*      */     }
/*      */     
/*  319 */     if (this._nonStandardCreation) {
/*  320 */       if (this._unwrappedPropertyHandler != null) {
/*  321 */         return deserializeWithUnwrapped(p, ctxt);
/*      */       }
/*  323 */       if (this._externalTypeIdHandler != null) {
/*  324 */         return deserializeWithExternalTypeId(p, ctxt);
/*      */       }
/*  326 */       Object object = deserializeFromObjectUsingNonDefault(p, ctxt);
/*  327 */       if (this._injectables != null) {
/*  328 */         injectValues(ctxt, object);
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
/*  342 */       return object;
/*      */     } 
/*  344 */     Object bean = this._valueInstantiator.createUsingDefault(ctxt);
/*      */     
/*  346 */     p.setCurrentValue(bean);
/*  347 */     if (p.canReadObjectId()) {
/*  348 */       Object id = p.getObjectId();
/*  349 */       if (id != null) {
/*  350 */         _handleTypedObjectId(p, ctxt, bean, id);
/*      */       }
/*      */     } 
/*  353 */     if (this._injectables != null) {
/*  354 */       injectValues(ctxt, bean);
/*      */     }
/*  356 */     if (this._needViewProcesing) {
/*  357 */       Class<?> view = ctxt.getActiveView();
/*  358 */       if (view != null) {
/*  359 */         return deserializeWithView(p, ctxt, bean, view);
/*      */       }
/*      */     } 
/*  362 */     if (p.hasTokenId(5)) {
/*  363 */       String propName = p.getCurrentName();
/*      */       do {
/*  365 */         p.nextToken();
/*  366 */         SettableBeanProperty prop = this._beanProperties.find(propName);
/*  367 */         if (prop != null)
/*      */         { try {
/*  369 */             prop.deserializeAndSet(p, ctxt, bean);
/*  370 */           } catch (Exception e) {
/*  371 */             wrapAndThrow(e, bean, propName, ctxt);
/*      */           }  }
/*      */         else
/*      */         
/*  375 */         { handleUnknownVanilla(p, ctxt, bean, propName); } 
/*  376 */       } while ((propName = p.nextFieldName()) != null);
/*      */     } 
/*  378 */     return bean;
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
/*      */   protected Object _deserializeUsingPropertyBased(JsonParser p, DeserializationContext ctxt) throws IOException {
/*      */     Object bean;
/*  394 */     PropertyBasedCreator creator = this._propertyBasedCreator;
/*  395 */     PropertyValueBuffer buffer = creator.startBuilding(p, ctxt, this._objectIdReader);
/*  396 */     TokenBuffer unknown = null;
/*  397 */     Class<?> activeView = this._needViewProcesing ? ctxt.getActiveView() : null;
/*      */     
/*  399 */     JsonToken t = p.getCurrentToken();
/*  400 */     List<BeanReferring> referrings = null;
/*  401 */     for (; t == JsonToken.FIELD_NAME; t = p.nextToken()) {
/*  402 */       String propName = p.getCurrentName();
/*  403 */       p.nextToken();
/*      */       
/*  405 */       if (!buffer.readIdProperty(propName)) {
/*      */ 
/*      */ 
/*      */         
/*  409 */         SettableBeanProperty creatorProp = creator.findCreatorProperty(propName);
/*  410 */         if (creatorProp != null) {
/*      */ 
/*      */           
/*  413 */           if (activeView != null && !creatorProp.visibleInView(activeView)) {
/*  414 */             p.skipChildren();
/*      */           } else {
/*      */             
/*  417 */             Object value = _deserializeWithErrorWrapping(p, ctxt, creatorProp);
/*  418 */             if (buffer.assignParameter(creatorProp, value)) {
/*  419 */               Object object; p.nextToken();
/*      */               
/*      */               try {
/*  422 */                 object = creator.build(ctxt, buffer);
/*  423 */               } catch (Exception e) {
/*  424 */                 object = wrapInstantiationProblem(e, ctxt);
/*      */               } 
/*  426 */               if (object == null) {
/*  427 */                 return ctxt.handleInstantiationProblem(handledType(), null, 
/*  428 */                     _creatorReturnedNullException());
/*      */               }
/*      */               
/*  431 */               p.setCurrentValue(object);
/*      */ 
/*      */               
/*  434 */               if (object.getClass() != this._beanType.getRawClass()) {
/*  435 */                 return handlePolymorphic(p, ctxt, object, unknown);
/*      */               }
/*  437 */               if (unknown != null) {
/*  438 */                 object = handleUnknownProperties(ctxt, object, unknown);
/*      */               }
/*      */               
/*  441 */               return deserialize(p, ctxt, object);
/*      */             } 
/*      */           } 
/*      */         } else {
/*      */           
/*  446 */           SettableBeanProperty prop = this._beanProperties.find(propName);
/*  447 */           if (prop != null) {
/*      */             try {
/*  449 */               buffer.bufferProperty(prop, _deserializeWithErrorWrapping(p, ctxt, prop));
/*  450 */             } catch (UnresolvedForwardReference reference) {
/*      */ 
/*      */ 
/*      */               
/*  454 */               BeanReferring referring = handleUnresolvedReference(ctxt, prop, buffer, reference);
/*      */               
/*  456 */               if (referrings == null) {
/*  457 */                 referrings = new ArrayList<>();
/*      */               }
/*  459 */               referrings.add(referring);
/*      */             
/*      */             }
/*      */           
/*      */           }
/*  464 */           else if (this._ignorableProps != null && this._ignorableProps.contains(propName)) {
/*  465 */             handleIgnoredProperty(p, ctxt, handledType(), propName);
/*      */ 
/*      */           
/*      */           }
/*  469 */           else if (this._anySetter != null) {
/*      */             try {
/*  471 */               buffer.bufferAnyProperty(this._anySetter, propName, this._anySetter.deserialize(p, ctxt));
/*  472 */             } catch (Exception e) {
/*  473 */               wrapAndThrow(e, this._beanType.getRawClass(), propName, ctxt);
/*      */             }
/*      */           
/*      */           } else {
/*      */             
/*  478 */             if (unknown == null) {
/*  479 */               unknown = new TokenBuffer(p, ctxt);
/*      */             }
/*  481 */             unknown.writeFieldName(propName);
/*  482 */             unknown.copyCurrentStructure(p);
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     try {
/*  488 */       bean = creator.build(ctxt, buffer);
/*  489 */     } catch (Exception e) {
/*  490 */       wrapInstantiationProblem(e, ctxt);
/*  491 */       bean = null;
/*      */     } 
/*  493 */     if (referrings != null) {
/*  494 */       for (BeanReferring referring : referrings) {
/*  495 */         referring.setBean(bean);
/*      */       }
/*      */     }
/*  498 */     if (unknown != null) {
/*      */       
/*  500 */       if (bean.getClass() != this._beanType.getRawClass()) {
/*  501 */         return handlePolymorphic((JsonParser)null, ctxt, bean, unknown);
/*      */       }
/*      */       
/*  504 */       return handleUnknownProperties(ctxt, bean, unknown);
/*      */     } 
/*  506 */     return bean;
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
/*      */   private BeanReferring handleUnresolvedReference(DeserializationContext ctxt, SettableBeanProperty prop, PropertyValueBuffer buffer, UnresolvedForwardReference reference) throws JsonMappingException {
/*  518 */     BeanReferring referring = new BeanReferring(ctxt, reference, prop.getType(), buffer, prop);
/*  519 */     reference.getRoid().appendReferring(referring);
/*  520 */     return referring;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final Object _deserializeWithErrorWrapping(JsonParser p, DeserializationContext ctxt, SettableBeanProperty prop) throws IOException {
/*      */     try {
/*  528 */       return prop.deserialize(p, ctxt);
/*  529 */     } catch (Exception e) {
/*  530 */       wrapAndThrow(e, this._beanType.getRawClass(), prop.getName(), ctxt);
/*      */       
/*  532 */       return null;
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
/*      */   protected Object deserializeFromNull(JsonParser p, DeserializationContext ctxt) throws IOException {
/*  550 */     if (p.requiresCustomCodec()) {
/*      */       
/*  552 */       TokenBuffer tb = new TokenBuffer(p, ctxt);
/*  553 */       tb.writeEndObject();
/*  554 */       JsonParser p2 = tb.asParser(p);
/*  555 */       p2.nextToken();
/*      */ 
/*      */       
/*  558 */       Object ob = this._vanillaProcessing ? vanillaDeserialize(p2, ctxt, JsonToken.END_OBJECT) : deserializeFromObject(p2, ctxt);
/*  559 */       p2.close();
/*  560 */       return ob;
/*      */     } 
/*  562 */     return ctxt.handleUnexpectedToken(getValueType(ctxt), p);
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
/*      */   protected final Object deserializeWithView(JsonParser p, DeserializationContext ctxt, Object bean, Class<?> activeView) throws IOException {
/*  575 */     if (p.hasTokenId(5)) {
/*  576 */       String propName = p.getCurrentName();
/*      */       do {
/*  578 */         p.nextToken();
/*      */         
/*  580 */         SettableBeanProperty prop = this._beanProperties.find(propName);
/*  581 */         if (prop != null)
/*  582 */         { if (!prop.visibleInView(activeView)) {
/*  583 */             p.skipChildren();
/*      */           } else {
/*      */             
/*      */             try {
/*  587 */               prop.deserializeAndSet(p, ctxt, bean);
/*  588 */             } catch (Exception e) {
/*  589 */               wrapAndThrow(e, bean, propName, ctxt);
/*      */             } 
/*      */           }  }
/*      */         else
/*  593 */         { handleUnknownVanilla(p, ctxt, bean, propName); } 
/*  594 */       } while ((propName = p.nextFieldName()) != null);
/*      */     } 
/*  596 */     return bean;
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
/*      */   protected Object deserializeWithUnwrapped(JsonParser p, DeserializationContext ctxt) throws IOException {
/*  613 */     if (this._delegateDeserializer != null) {
/*  614 */       return this._valueInstantiator.createUsingDelegate(ctxt, this._delegateDeserializer.deserialize(p, ctxt));
/*      */     }
/*  616 */     if (this._propertyBasedCreator != null) {
/*  617 */       return deserializeUsingPropertyBasedWithUnwrapped(p, ctxt);
/*      */     }
/*  619 */     TokenBuffer tokens = new TokenBuffer(p, ctxt);
/*  620 */     tokens.writeStartObject();
/*  621 */     Object bean = this._valueInstantiator.createUsingDefault(ctxt);
/*      */ 
/*      */     
/*  624 */     p.setCurrentValue(bean);
/*      */     
/*  626 */     if (this._injectables != null) {
/*  627 */       injectValues(ctxt, bean);
/*      */     }
/*  629 */     Class<?> activeView = this._needViewProcesing ? ctxt.getActiveView() : null;
/*  630 */     String propName = p.hasTokenId(5) ? p.getCurrentName() : null;
/*      */     
/*  632 */     for (; propName != null; propName = p.nextFieldName()) {
/*  633 */       p.nextToken();
/*  634 */       SettableBeanProperty prop = this._beanProperties.find(propName);
/*  635 */       if (prop != null) {
/*  636 */         if (activeView != null && !prop.visibleInView(activeView)) {
/*  637 */           p.skipChildren();
/*      */         } else {
/*      */           
/*      */           try {
/*  641 */             prop.deserializeAndSet(p, ctxt, bean);
/*  642 */           } catch (Exception e) {
/*  643 */             wrapAndThrow(e, bean, propName, ctxt);
/*      */           }
/*      */         
/*      */         }
/*      */       
/*  648 */       } else if (this._ignorableProps != null && this._ignorableProps.contains(propName)) {
/*  649 */         handleIgnoredProperty(p, ctxt, bean, propName);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       }
/*  656 */       else if (this._anySetter == null) {
/*      */         
/*  658 */         tokens.writeFieldName(propName);
/*  659 */         tokens.copyCurrentStructure(p);
/*      */       }
/*      */       else {
/*      */         
/*  663 */         TokenBuffer b2 = TokenBuffer.asCopyOfValue(p);
/*  664 */         tokens.writeFieldName(propName);
/*  665 */         tokens.append(b2);
/*      */         try {
/*  667 */           this._anySetter.deserializeAndSet(b2.asParserOnFirstToken(), ctxt, bean, propName);
/*  668 */         } catch (Exception e) {
/*  669 */           wrapAndThrow(e, bean, propName, ctxt);
/*      */         } 
/*      */       } 
/*  672 */     }  tokens.writeEndObject();
/*  673 */     this._unwrappedPropertyHandler.processUnwrapped(p, ctxt, bean, tokens);
/*  674 */     return bean;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Object deserializeWithUnwrapped(JsonParser p, DeserializationContext ctxt, Object bean) throws IOException {
/*  682 */     JsonToken t = p.getCurrentToken();
/*  683 */     if (t == JsonToken.START_OBJECT) {
/*  684 */       t = p.nextToken();
/*      */     }
/*  686 */     TokenBuffer tokens = new TokenBuffer(p, ctxt);
/*  687 */     tokens.writeStartObject();
/*  688 */     Class<?> activeView = this._needViewProcesing ? ctxt.getActiveView() : null;
/*  689 */     for (; t == JsonToken.FIELD_NAME; t = p.nextToken()) {
/*  690 */       String propName = p.getCurrentName();
/*  691 */       SettableBeanProperty prop = this._beanProperties.find(propName);
/*  692 */       p.nextToken();
/*  693 */       if (prop != null) {
/*  694 */         if (activeView != null && !prop.visibleInView(activeView)) {
/*  695 */           p.skipChildren();
/*      */         } else {
/*      */           
/*      */           try {
/*  699 */             prop.deserializeAndSet(p, ctxt, bean);
/*  700 */           } catch (Exception e) {
/*  701 */             wrapAndThrow(e, bean, propName, ctxt);
/*      */           }
/*      */         
/*      */         } 
/*  705 */       } else if (this._ignorableProps != null && this._ignorableProps.contains(propName)) {
/*  706 */         handleIgnoredProperty(p, ctxt, bean, propName);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       }
/*  713 */       else if (this._anySetter == null) {
/*      */         
/*  715 */         tokens.writeFieldName(propName);
/*  716 */         tokens.copyCurrentStructure(p);
/*      */       } else {
/*      */         
/*  719 */         TokenBuffer b2 = TokenBuffer.asCopyOfValue(p);
/*  720 */         tokens.writeFieldName(propName);
/*  721 */         tokens.append(b2);
/*      */         try {
/*  723 */           this._anySetter.deserializeAndSet(b2.asParserOnFirstToken(), ctxt, bean, propName);
/*  724 */         } catch (Exception e) {
/*  725 */           wrapAndThrow(e, bean, propName, ctxt);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/*  730 */     tokens.writeEndObject();
/*  731 */     this._unwrappedPropertyHandler.processUnwrapped(p, ctxt, bean, tokens);
/*  732 */     return bean;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Object deserializeUsingPropertyBasedWithUnwrapped(JsonParser p, DeserializationContext ctxt) throws IOException {
/*      */     Object bean;
/*  743 */     PropertyBasedCreator creator = this._propertyBasedCreator;
/*  744 */     PropertyValueBuffer buffer = creator.startBuilding(p, ctxt, this._objectIdReader);
/*      */     
/*  746 */     TokenBuffer tokens = new TokenBuffer(p, ctxt);
/*  747 */     tokens.writeStartObject();
/*      */     
/*  749 */     JsonToken t = p.getCurrentToken();
/*  750 */     for (; t == JsonToken.FIELD_NAME; t = p.nextToken()) {
/*  751 */       String propName = p.getCurrentName();
/*  752 */       p.nextToken();
/*      */       
/*  754 */       SettableBeanProperty creatorProp = creator.findCreatorProperty(propName);
/*  755 */       if (creatorProp != null) {
/*      */         
/*  757 */         if (buffer.assignParameter(creatorProp, 
/*  758 */             _deserializeWithErrorWrapping(p, ctxt, creatorProp))) {
/*  759 */           Object object; t = p.nextToken();
/*      */           
/*      */           try {
/*  762 */             object = creator.build(ctxt, buffer);
/*  763 */           } catch (Exception e) {
/*  764 */             object = wrapInstantiationProblem(e, ctxt);
/*      */           } 
/*      */           
/*  767 */           p.setCurrentValue(object);
/*      */           
/*  769 */           while (t == JsonToken.FIELD_NAME) {
/*      */             
/*  771 */             tokens.copyCurrentStructure(p);
/*  772 */             t = p.nextToken();
/*      */           } 
/*      */ 
/*      */           
/*  776 */           if (t != JsonToken.END_OBJECT)
/*  777 */             ctxt.reportWrongTokenException((JsonDeserializer)this, JsonToken.END_OBJECT, "Attempted to unwrap '%s' value", new Object[] {
/*      */                   
/*  779 */                   handledType().getName()
/*      */                 }); 
/*  781 */           tokens.writeEndObject();
/*  782 */           if (object.getClass() != this._beanType.getRawClass()) {
/*      */ 
/*      */             
/*  785 */             ctxt.reportInputMismatch((BeanProperty)creatorProp, "Cannot create polymorphic instances with unwrapped values", new Object[0]);
/*      */             
/*  787 */             return null;
/*      */           } 
/*  789 */           return this._unwrappedPropertyHandler.processUnwrapped(p, ctxt, object, tokens);
/*      */         
/*      */         }
/*      */       
/*      */       }
/*  794 */       else if (!buffer.readIdProperty(propName)) {
/*      */ 
/*      */ 
/*      */         
/*  798 */         SettableBeanProperty prop = this._beanProperties.find(propName);
/*  799 */         if (prop != null) {
/*  800 */           buffer.bufferProperty(prop, _deserializeWithErrorWrapping(p, ctxt, prop));
/*      */ 
/*      */         
/*      */         }
/*  804 */         else if (this._ignorableProps != null && this._ignorableProps.contains(propName)) {
/*  805 */           handleIgnoredProperty(p, ctxt, handledType(), propName);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         }
/*  812 */         else if (this._anySetter == null) {
/*      */           
/*  814 */           tokens.writeFieldName(propName);
/*  815 */           tokens.copyCurrentStructure(p);
/*      */         } else {
/*      */           
/*  818 */           TokenBuffer b2 = TokenBuffer.asCopyOfValue(p);
/*  819 */           tokens.writeFieldName(propName);
/*  820 */           tokens.append(b2);
/*      */           try {
/*  822 */             buffer.bufferAnyProperty(this._anySetter, propName, this._anySetter
/*  823 */                 .deserialize(b2.asParserOnFirstToken(), ctxt));
/*  824 */           } catch (Exception e) {
/*  825 */             wrapAndThrow(e, this._beanType.getRawClass(), propName, ctxt);
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/*  834 */       bean = creator.build(ctxt, buffer);
/*  835 */     } catch (Exception e) {
/*  836 */       wrapInstantiationProblem(e, ctxt);
/*  837 */       return null;
/*      */     } 
/*  839 */     return this._unwrappedPropertyHandler.processUnwrapped(p, ctxt, bean, tokens);
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
/*      */   protected Object deserializeWithExternalTypeId(JsonParser p, DeserializationContext ctxt) throws IOException {
/*  852 */     if (this._propertyBasedCreator != null) {
/*  853 */       return deserializeUsingPropertyBasedWithExternalTypeId(p, ctxt);
/*      */     }
/*  855 */     if (this._delegateDeserializer != null)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  861 */       return this._valueInstantiator.createUsingDelegate(ctxt, this._delegateDeserializer
/*  862 */           .deserialize(p, ctxt));
/*      */     }
/*      */     
/*  865 */     return deserializeWithExternalTypeId(p, ctxt, this._valueInstantiator.createUsingDefault(ctxt));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Object deserializeWithExternalTypeId(JsonParser p, DeserializationContext ctxt, Object bean) throws IOException {
/*  872 */     Class<?> activeView = this._needViewProcesing ? ctxt.getActiveView() : null;
/*  873 */     ExternalTypeHandler ext = this._externalTypeIdHandler.start();
/*      */     
/*  875 */     for (JsonToken t = p.getCurrentToken(); t == JsonToken.FIELD_NAME; t = p.nextToken()) {
/*  876 */       String propName = p.getCurrentName();
/*  877 */       t = p.nextToken();
/*  878 */       SettableBeanProperty prop = this._beanProperties.find(propName);
/*  879 */       if (prop != null) {
/*      */         
/*  881 */         if (t.isScalarValue()) {
/*  882 */           ext.handleTypePropertyValue(p, ctxt, propName, bean);
/*      */         }
/*  884 */         if (activeView != null && !prop.visibleInView(activeView)) {
/*  885 */           p.skipChildren();
/*      */         } else {
/*      */           
/*      */           try {
/*  889 */             prop.deserializeAndSet(p, ctxt, bean);
/*  890 */           } catch (Exception e) {
/*  891 */             wrapAndThrow(e, bean, propName, ctxt);
/*      */           }
/*      */         
/*      */         }
/*      */       
/*  896 */       } else if (this._ignorableProps != null && this._ignorableProps.contains(propName)) {
/*  897 */         handleIgnoredProperty(p, ctxt, bean, propName);
/*      */ 
/*      */       
/*      */       }
/*  901 */       else if (!ext.handlePropertyValue(p, ctxt, propName, bean)) {
/*      */ 
/*      */ 
/*      */         
/*  905 */         if (this._anySetter != null) {
/*      */           try {
/*  907 */             this._anySetter.deserializeAndSet(p, ctxt, bean, propName);
/*  908 */           } catch (Exception e) {
/*  909 */             wrapAndThrow(e, bean, propName, ctxt);
/*      */           }
/*      */         
/*      */         } else {
/*      */           
/*  914 */           handleUnknownProperty(p, ctxt, bean, propName);
/*      */         } 
/*      */       } 
/*  917 */     }  return ext.complete(p, ctxt, bean);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Object deserializeUsingPropertyBasedWithExternalTypeId(JsonParser p, DeserializationContext ctxt) throws IOException {
/*  924 */     ExternalTypeHandler ext = this._externalTypeIdHandler.start();
/*  925 */     PropertyBasedCreator creator = this._propertyBasedCreator;
/*  926 */     PropertyValueBuffer buffer = creator.startBuilding(p, ctxt, this._objectIdReader);
/*      */     
/*  928 */     TokenBuffer tokens = new TokenBuffer(p, ctxt);
/*  929 */     tokens.writeStartObject();
/*      */     
/*  931 */     JsonToken t = p.getCurrentToken();
/*  932 */     for (; t == JsonToken.FIELD_NAME; t = p.nextToken()) {
/*  933 */       String propName = p.getCurrentName();
/*  934 */       p.nextToken();
/*      */       
/*  936 */       SettableBeanProperty creatorProp = creator.findCreatorProperty(propName);
/*  937 */       if (creatorProp != null) {
/*      */ 
/*      */ 
/*      */         
/*  941 */         if (!ext.handlePropertyValue(p, ctxt, propName, null))
/*      */         {
/*      */ 
/*      */           
/*  945 */           if (buffer.assignParameter(creatorProp, _deserializeWithErrorWrapping(p, ctxt, creatorProp))) {
/*  946 */             Object bean; t = p.nextToken();
/*      */             
/*      */             try {
/*  949 */               bean = creator.build(ctxt, buffer);
/*  950 */             } catch (Exception e) {
/*  951 */               wrapAndThrow(e, this._beanType.getRawClass(), propName, ctxt);
/*      */             } 
/*      */ 
/*      */             
/*  955 */             while (t == JsonToken.FIELD_NAME) {
/*  956 */               p.nextToken();
/*  957 */               tokens.copyCurrentStructure(p);
/*  958 */               t = p.nextToken();
/*      */             } 
/*  960 */             if (bean.getClass() != this._beanType.getRawClass())
/*      */             {
/*      */               
/*  963 */               return ctxt.reportBadDefinition(this._beanType, String.format("Cannot create polymorphic instances with external type ids (%s -> %s)", new Object[] { this._beanType, bean
/*      */                       
/*  965 */                       .getClass() }));
/*      */             }
/*  967 */             return ext.complete(p, ctxt, bean);
/*      */           
/*      */           }
/*      */         
/*      */         }
/*      */       }
/*  973 */       else if (!buffer.readIdProperty(propName)) {
/*      */ 
/*      */ 
/*      */         
/*  977 */         SettableBeanProperty prop = this._beanProperties.find(propName);
/*  978 */         if (prop != null) {
/*  979 */           buffer.bufferProperty(prop, prop.deserialize(p, ctxt));
/*      */ 
/*      */         
/*      */         }
/*  983 */         else if (!ext.handlePropertyValue(p, ctxt, propName, null)) {
/*      */ 
/*      */ 
/*      */           
/*  987 */           if (this._ignorableProps != null && this._ignorableProps.contains(propName))
/*  988 */           { handleIgnoredProperty(p, ctxt, handledType(), propName);
/*      */             
/*      */              }
/*      */           
/*  992 */           else if (this._anySetter != null)
/*  993 */           { buffer.bufferAnyProperty(this._anySetter, propName, this._anySetter
/*  994 */                 .deserialize(p, ctxt)); } 
/*      */         } 
/*      */       } 
/*  997 */     }  tokens.writeEndObject();
/*      */ 
/*      */     
/*      */     try {
/* 1001 */       return ext.complete(p, ctxt, buffer, creator);
/* 1002 */     } catch (Exception e) {
/* 1003 */       return wrapInstantiationProblem(e, ctxt);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Exception _creatorReturnedNullException() {
/* 1014 */     if (this._nullFromCreator == null) {
/* 1015 */       this._nullFromCreator = new NullPointerException("JSON Creator returned null");
/*      */     }
/* 1017 */     return this._nullFromCreator;
/*      */   }
/*      */ 
/*      */   
/*      */   static class BeanReferring
/*      */     extends ReadableObjectId.Referring
/*      */   {
/*      */     private final DeserializationContext _context;
/*      */     
/*      */     private final SettableBeanProperty _prop;
/*      */     
/*      */     private Object _bean;
/*      */ 
/*      */     
/*      */     BeanReferring(DeserializationContext ctxt, UnresolvedForwardReference ref, JavaType valueType, PropertyValueBuffer buffer, SettableBeanProperty prop) {
/* 1032 */       super(ref, valueType);
/* 1033 */       this._context = ctxt;
/* 1034 */       this._prop = prop;
/*      */     }
/*      */     
/*      */     public void setBean(Object bean) {
/* 1038 */       this._bean = bean;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void handleResolvedForwardReference(Object id, Object value) throws IOException {
/* 1044 */       if (this._bean == null) {
/* 1045 */         this._context.reportInputMismatch((BeanProperty)this._prop, "Cannot resolve ObjectId forward reference using property '%s' (of type %s): Bean not yet resolved", new Object[] { this._prop
/*      */               
/* 1047 */               .getName(), this._prop.getDeclaringClass().getName() });
/*      */       }
/* 1049 */       this._prop.set(this._bean, value);
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\deser\BeanDeserializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */