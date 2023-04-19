/*     */ package com.fasterxml.jackson.databind.deser.impl;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.DeserializationFeature;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*     */ import com.fasterxml.jackson.databind.util.TokenBuffer;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
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
/*     */ public class ExternalTypeHandler
/*     */ {
/*     */   private final JavaType _beanType;
/*     */   private final ExtTypedProperty[] _properties;
/*     */   private final Map<String, Object> _nameToPropertyIndex;
/*     */   private final String[] _typeIds;
/*     */   private final TokenBuffer[] _tokens;
/*     */   
/*     */   protected ExternalTypeHandler(JavaType beanType, ExtTypedProperty[] properties, Map<String, Object> nameToPropertyIndex, String[] typeIds, TokenBuffer[] tokens) {
/*  41 */     this._beanType = beanType;
/*  42 */     this._properties = properties;
/*  43 */     this._nameToPropertyIndex = nameToPropertyIndex;
/*  44 */     this._typeIds = typeIds;
/*  45 */     this._tokens = tokens;
/*     */   }
/*     */ 
/*     */   
/*     */   protected ExternalTypeHandler(ExternalTypeHandler h) {
/*  50 */     this._beanType = h._beanType;
/*  51 */     this._properties = h._properties;
/*  52 */     this._nameToPropertyIndex = h._nameToPropertyIndex;
/*  53 */     int len = this._properties.length;
/*  54 */     this._typeIds = new String[len];
/*  55 */     this._tokens = new TokenBuffer[len];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Builder builder(JavaType beanType) {
/*  62 */     return new Builder(beanType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ExternalTypeHandler start() {
/*  70 */     return new ExternalTypeHandler(this);
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
/*     */   public boolean handleTypePropertyValue(JsonParser p, DeserializationContext ctxt, String propName, Object bean) throws IOException {
/*  85 */     Object ob = this._nameToPropertyIndex.get(propName);
/*  86 */     if (ob == null) {
/*  87 */       return false;
/*     */     }
/*  89 */     String typeId = p.getText();
/*     */     
/*  91 */     if (ob instanceof List) {
/*  92 */       boolean result = false;
/*  93 */       for (Integer index : ob) {
/*  94 */         if (_handleTypePropertyValue(p, ctxt, propName, bean, typeId, index
/*  95 */             .intValue())) {
/*  96 */           result = true;
/*     */         }
/*     */       } 
/*  99 */       return result;
/*     */     } 
/* 101 */     return _handleTypePropertyValue(p, ctxt, propName, bean, typeId, ((Integer)ob)
/* 102 */         .intValue());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final boolean _handleTypePropertyValue(JsonParser p, DeserializationContext ctxt, String propName, Object bean, String typeId, int index) throws IOException {
/* 109 */     ExtTypedProperty prop = this._properties[index];
/* 110 */     if (!prop.hasTypePropertyName(propName)) {
/* 111 */       return false;
/*     */     }
/*     */     
/* 114 */     boolean canDeserialize = (bean != null && this._tokens[index] != null);
/*     */     
/* 116 */     if (canDeserialize) {
/* 117 */       _deserializeAndSet(p, ctxt, bean, index, typeId);
/*     */       
/* 119 */       this._tokens[index] = null;
/*     */     } else {
/* 121 */       this._typeIds[index] = typeId;
/*     */     } 
/* 123 */     return true;
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
/*     */   public boolean handlePropertyValue(JsonParser p, DeserializationContext ctxt, String propName, Object bean) throws IOException {
/*     */     boolean canDeserialize;
/* 138 */     Object ob = this._nameToPropertyIndex.get(propName);
/* 139 */     if (ob == null) {
/* 140 */       return false;
/*     */     }
/*     */     
/* 143 */     if (ob instanceof List) {
/* 144 */       Iterator<Integer> it = ((List<Integer>)ob).iterator();
/* 145 */       Integer integer = it.next();
/*     */       
/* 147 */       ExtTypedProperty extTypedProperty = this._properties[integer.intValue()];
/*     */ 
/*     */       
/* 150 */       if (extTypedProperty.hasTypePropertyName(propName)) {
/* 151 */         String typeId = p.getText();
/* 152 */         p.skipChildren();
/* 153 */         this._typeIds[integer.intValue()] = typeId;
/* 154 */         while (it.hasNext()) {
/* 155 */           this._typeIds[((Integer)it.next()).intValue()] = typeId;
/*     */         }
/*     */       } else {
/*     */         
/* 159 */         TokenBuffer tokens = new TokenBuffer(p, ctxt);
/* 160 */         tokens.copyCurrentStructure(p);
/* 161 */         this._tokens[integer.intValue()] = tokens;
/* 162 */         while (it.hasNext()) {
/* 163 */           this._tokens[((Integer)it.next()).intValue()] = tokens;
/*     */         }
/*     */       } 
/* 166 */       return true;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 171 */     int index = ((Integer)ob).intValue();
/* 172 */     ExtTypedProperty prop = this._properties[index];
/*     */     
/* 174 */     if (prop.hasTypePropertyName(propName)) {
/* 175 */       this._typeIds[index] = p.getText();
/* 176 */       p.skipChildren();
/* 177 */       canDeserialize = (bean != null && this._tokens[index] != null);
/*     */     } else {
/*     */       
/* 180 */       TokenBuffer tokens = new TokenBuffer(p, ctxt);
/* 181 */       tokens.copyCurrentStructure(p);
/* 182 */       this._tokens[index] = tokens;
/* 183 */       canDeserialize = (bean != null && this._typeIds[index] != null);
/*     */     } 
/*     */ 
/*     */     
/* 187 */     if (canDeserialize) {
/* 188 */       String typeId = this._typeIds[index];
/*     */       
/* 190 */       this._typeIds[index] = null;
/* 191 */       _deserializeAndSet(p, ctxt, bean, index, typeId);
/* 192 */       this._tokens[index] = null;
/*     */     } 
/* 194 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object complete(JsonParser p, DeserializationContext ctxt, Object bean) throws IOException {
/* 205 */     int i = 0, len = this._properties.length; while (true) { String typeId; if (i < len)
/* 206 */       { typeId = this._typeIds[i];
/* 207 */         if (typeId == null)
/* 208 */         { TokenBuffer tokens = this._tokens[i];
/*     */ 
/*     */           
/* 211 */           if (tokens == null) {
/*     */             continue;
/*     */           }
/*     */ 
/*     */           
/* 216 */           JsonToken t = tokens.firstToken();
/* 217 */           if (t.isScalarValue())
/* 218 */           { JsonParser buffered = tokens.asParser(p);
/* 219 */             buffered.nextToken();
/* 220 */             SettableBeanProperty extProp = this._properties[i].getProperty();
/* 221 */             Object result = TypeDeserializer.deserializeIfNatural(buffered, ctxt, extProp.getType());
/* 222 */             if (result != null)
/* 223 */             { extProp.set(bean, result); }
/*     */             
/*     */             else
/*     */             
/* 227 */             { if (!this._properties[i].hasDefaultType()) {
/* 228 */                 ctxt.reportPropertyInputMismatch(bean.getClass(), extProp.getName(), "Missing external type id property '%s'", new Object[] { this._properties[i]
/*     */                       
/* 230 */                       .getTypePropertyName() });
/*     */               } else {
/* 232 */                 typeId = this._properties[i].getDefaultTypeId();
/*     */               } 
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
/* 246 */               _deserializeAndSet(p, ctxt, bean, i, typeId); }  continue; }  } else if (this._tokens[i] == null) { SettableBeanProperty prop = this._properties[i].getProperty(); if (prop.isRequired() || ctxt.isEnabled(DeserializationFeature.FAIL_ON_MISSING_EXTERNAL_TYPE_ID_PROPERTY)) ctxt.reportPropertyInputMismatch(bean.getClass(), prop.getName(), "Missing property '%s' for external type id '%s'", new Object[] { prop.getName(), this._properties[i].getTypePropertyName() });  return bean; }  } else { break; }  _deserializeAndSet(p, ctxt, bean, i, typeId); i++; }
/*     */     
/* 248 */     return bean;
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
/*     */   public Object complete(JsonParser p, DeserializationContext ctxt, PropertyValueBuffer buffer, PropertyBasedCreator creator) throws IOException {
/* 260 */     int len = this._properties.length;
/* 261 */     Object[] values = new Object[len];
/* 262 */     for (int i = 0; i < len; i++) {
/* 263 */       String typeId = this._typeIds[i];
/* 264 */       ExtTypedProperty extProp = this._properties[i];
/* 265 */       if (typeId == null) {
/*     */         
/* 267 */         if (this._tokens[i] == null) {
/*     */           continue;
/*     */         }
/*     */ 
/*     */         
/* 272 */         if (!extProp.hasDefaultType()) {
/* 273 */           ctxt.reportPropertyInputMismatch(this._beanType, extProp.getProperty().getName(), "Missing external type id property '%s'", new Object[] { extProp
/*     */                 
/* 275 */                 .getTypePropertyName() });
/*     */         } else {
/* 277 */           typeId = extProp.getDefaultTypeId();
/*     */         } 
/* 279 */       } else if (this._tokens[i] == null) {
/* 280 */         SettableBeanProperty settableBeanProperty = extProp.getProperty();
/* 281 */         if (settableBeanProperty.isRequired() || ctxt
/* 282 */           .isEnabled(DeserializationFeature.FAIL_ON_MISSING_EXTERNAL_TYPE_ID_PROPERTY)) {
/* 283 */           ctxt.reportPropertyInputMismatch(this._beanType, settableBeanProperty.getName(), "Missing property '%s' for external type id '%s'", new Object[] { settableBeanProperty
/*     */                 
/* 285 */                 .getName(), this._properties[i].getTypePropertyName() });
/*     */         }
/*     */       } 
/* 288 */       if (this._tokens[i] != null) {
/* 289 */         values[i] = _deserialize(p, ctxt, i, typeId);
/*     */       }
/*     */       
/* 292 */       SettableBeanProperty prop = extProp.getProperty();
/*     */       
/* 294 */       if (prop.getCreatorIndex() >= 0) {
/* 295 */         buffer.assignParameter(prop, values[i]);
/*     */ 
/*     */         
/* 298 */         SettableBeanProperty typeProp = extProp.getTypeProperty();
/*     */         
/* 300 */         if (typeProp != null && typeProp.getCreatorIndex() >= 0) {
/*     */           Object v;
/*     */ 
/*     */           
/* 304 */           if (typeProp.getType().hasRawClass(String.class)) {
/* 305 */             v = typeId;
/*     */           } else {
/* 307 */             TokenBuffer tb = new TokenBuffer(p, ctxt);
/* 308 */             tb.writeString(typeId);
/* 309 */             v = typeProp.getValueDeserializer().deserialize(tb.asParserOnFirstToken(), ctxt);
/* 310 */             tb.close();
/*     */           } 
/* 312 */           buffer.assignParameter(typeProp, v);
/*     */         } 
/*     */       }  continue;
/*     */     } 
/* 316 */     Object bean = creator.build(ctxt, buffer);
/*     */     
/* 318 */     for (int j = 0; j < len; j++) {
/* 319 */       SettableBeanProperty prop = this._properties[j].getProperty();
/* 320 */       if (prop.getCreatorIndex() < 0) {
/* 321 */         prop.set(bean, values[j]);
/*     */       }
/*     */     } 
/* 324 */     return bean;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final Object _deserialize(JsonParser p, DeserializationContext ctxt, int index, String typeId) throws IOException {
/* 331 */     JsonParser p2 = this._tokens[index].asParser(p);
/* 332 */     JsonToken t = p2.nextToken();
/*     */     
/* 334 */     if (t == JsonToken.VALUE_NULL) {
/* 335 */       return null;
/*     */     }
/* 337 */     TokenBuffer merged = new TokenBuffer(p, ctxt);
/* 338 */     merged.writeStartArray();
/* 339 */     merged.writeString(typeId);
/* 340 */     merged.copyCurrentStructure(p2);
/* 341 */     merged.writeEndArray();
/*     */ 
/*     */     
/* 344 */     JsonParser mp = merged.asParser(p);
/* 345 */     mp.nextToken();
/* 346 */     return this._properties[index].getProperty().deserialize(mp, ctxt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void _deserializeAndSet(JsonParser p, DeserializationContext ctxt, Object bean, int index, String typeId) throws IOException {
/* 356 */     JsonParser p2 = this._tokens[index].asParser(p);
/* 357 */     JsonToken t = p2.nextToken();
/*     */     
/* 359 */     if (t == JsonToken.VALUE_NULL) {
/* 360 */       this._properties[index].getProperty().set(bean, null);
/*     */       return;
/*     */     } 
/* 363 */     TokenBuffer merged = new TokenBuffer(p, ctxt);
/* 364 */     merged.writeStartArray();
/* 365 */     merged.writeString(typeId);
/*     */     
/* 367 */     merged.copyCurrentStructure(p2);
/* 368 */     merged.writeEndArray();
/*     */     
/* 370 */     JsonParser mp = merged.asParser(p);
/* 371 */     mp.nextToken();
/* 372 */     this._properties[index].getProperty().deserializeAndSet(mp, ctxt, bean);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Builder
/*     */   {
/*     */     private final JavaType _beanType;
/*     */ 
/*     */ 
/*     */     
/* 385 */     private final List<ExternalTypeHandler.ExtTypedProperty> _properties = new ArrayList<>();
/* 386 */     private final Map<String, Object> _nameToPropertyIndex = new HashMap<>();
/*     */     
/*     */     protected Builder(JavaType t) {
/* 389 */       this._beanType = t;
/*     */     }
/*     */ 
/*     */     
/*     */     public void addExternal(SettableBeanProperty property, TypeDeserializer typeDeser) {
/* 394 */       Integer index = Integer.valueOf(this._properties.size());
/* 395 */       this._properties.add(new ExternalTypeHandler.ExtTypedProperty(property, typeDeser));
/* 396 */       _addPropertyIndex(property.getName(), index);
/* 397 */       _addPropertyIndex(typeDeser.getPropertyName(), index);
/*     */     }
/*     */     
/*     */     private void _addPropertyIndex(String name, Integer index) {
/* 401 */       Object ob = this._nameToPropertyIndex.get(name);
/* 402 */       if (ob == null) {
/* 403 */         this._nameToPropertyIndex.put(name, index);
/* 404 */       } else if (ob instanceof List) {
/*     */         
/* 406 */         List<Object> list = (List<Object>)ob;
/* 407 */         list.add(index);
/*     */       } else {
/* 409 */         List<Object> list = new LinkedList();
/* 410 */         list.add(ob);
/* 411 */         list.add(index);
/* 412 */         this._nameToPropertyIndex.put(name, list);
/*     */       } 
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
/*     */     public ExternalTypeHandler build(BeanPropertyMap otherProps) {
/* 425 */       int len = this._properties.size();
/* 426 */       ExternalTypeHandler.ExtTypedProperty[] extProps = new ExternalTypeHandler.ExtTypedProperty[len];
/* 427 */       for (int i = 0; i < len; i++) {
/* 428 */         ExternalTypeHandler.ExtTypedProperty extProp = this._properties.get(i);
/* 429 */         String typePropId = extProp.getTypePropertyName();
/* 430 */         SettableBeanProperty typeProp = otherProps.find(typePropId);
/* 431 */         if (typeProp != null) {
/* 432 */           extProp.linkTypeProperty(typeProp);
/*     */         }
/* 434 */         extProps[i] = extProp;
/*     */       } 
/* 436 */       return new ExternalTypeHandler(this._beanType, extProps, this._nameToPropertyIndex, null, null);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class ExtTypedProperty
/*     */   {
/*     */     private final SettableBeanProperty _property;
/*     */     
/*     */     private final TypeDeserializer _typeDeserializer;
/*     */     
/*     */     private final String _typePropertyName;
/*     */     
/*     */     private SettableBeanProperty _typeProperty;
/*     */ 
/*     */     
/*     */     public ExtTypedProperty(SettableBeanProperty property, TypeDeserializer typeDeser) {
/* 454 */       this._property = property;
/* 455 */       this._typeDeserializer = typeDeser;
/* 456 */       this._typePropertyName = typeDeser.getPropertyName();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void linkTypeProperty(SettableBeanProperty p) {
/* 463 */       this._typeProperty = p;
/*     */     }
/*     */     
/*     */     public boolean hasTypePropertyName(String n) {
/* 467 */       return n.equals(this._typePropertyName);
/*     */     }
/*     */     
/*     */     public boolean hasDefaultType() {
/* 471 */       return (this._typeDeserializer.getDefaultImpl() != null);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getDefaultTypeId() {
/* 480 */       Class<?> defaultType = this._typeDeserializer.getDefaultImpl();
/* 481 */       if (defaultType == null) {
/* 482 */         return null;
/*     */       }
/* 484 */       return this._typeDeserializer.getTypeIdResolver().idFromValueAndType(null, defaultType);
/*     */     }
/*     */     public String getTypePropertyName() {
/* 487 */       return this._typePropertyName;
/*     */     }
/*     */     public SettableBeanProperty getProperty() {
/* 490 */       return this._property;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public SettableBeanProperty getTypeProperty() {
/* 497 */       return this._typeProperty;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\deser\impl\ExternalTypeHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */