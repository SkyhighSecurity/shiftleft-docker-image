/*     */ package com.fasterxml.jackson.databind.deser.impl;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.DeserializationFeature;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.deser.SettableAnyProperty;
/*     */ import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
/*     */ import java.io.IOException;
/*     */ import java.util.BitSet;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PropertyValueBuffer
/*     */ {
/*     */   protected final JsonParser _parser;
/*     */   protected final DeserializationContext _context;
/*     */   protected final ObjectIdReader _objectIdReader;
/*     */   protected final Object[] _creatorParameters;
/*     */   protected int _paramsNeeded;
/*     */   protected int _paramsSeen;
/*     */   protected final BitSet _paramsSeenBig;
/*     */   protected PropertyValue _buffered;
/*     */   protected Object _idValue;
/*     */   
/*     */   public PropertyValueBuffer(JsonParser p, DeserializationContext ctxt, int paramCount, ObjectIdReader oir) {
/*  88 */     this._parser = p;
/*  89 */     this._context = ctxt;
/*  90 */     this._paramsNeeded = paramCount;
/*  91 */     this._objectIdReader = oir;
/*  92 */     this._creatorParameters = new Object[paramCount];
/*  93 */     if (paramCount < 32) {
/*  94 */       this._paramsSeenBig = null;
/*     */     } else {
/*  96 */       this._paramsSeenBig = new BitSet();
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
/*     */   public final boolean hasParameter(SettableBeanProperty prop) {
/* 108 */     if (this._paramsSeenBig == null) {
/* 109 */       return ((this._paramsSeen >> prop.getCreatorIndex() & 0x1) == 1);
/*     */     }
/* 111 */     return this._paramsSeenBig.get(prop.getCreatorIndex());
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
/*     */   public Object getParameter(SettableBeanProperty prop) throws JsonMappingException {
/*     */     Object value;
/* 128 */     if (hasParameter(prop)) {
/* 129 */       value = this._creatorParameters[prop.getCreatorIndex()];
/*     */     } else {
/* 131 */       value = this._creatorParameters[prop.getCreatorIndex()] = _findMissing(prop);
/*     */     } 
/* 133 */     if (value == null && this._context.isEnabled(DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES)) {
/* 134 */       return this._context.reportInputMismatch((BeanProperty)prop, "Null value for creator property '%s' (index %d); `DeserializationFeature.FAIL_ON_NULL_FOR_CREATOR_PARAMETERS` enabled", new Object[] { prop
/*     */             
/* 136 */             .getName(), Integer.valueOf(prop.getCreatorIndex()) });
/*     */     }
/* 138 */     return value;
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
/*     */   public Object[] getParameters(SettableBeanProperty[] props) throws JsonMappingException {
/* 152 */     if (this._paramsNeeded > 0) {
/* 153 */       if (this._paramsSeenBig == null) {
/* 154 */         int mask = this._paramsSeen;
/*     */ 
/*     */         
/* 157 */         for (int ix = 0, len = this._creatorParameters.length; ix < len; ix++, mask >>= 1) {
/* 158 */           if ((mask & 0x1) == 0) {
/* 159 */             this._creatorParameters[ix] = _findMissing(props[ix]);
/*     */           }
/*     */         } 
/*     */       } else {
/* 163 */         int len = this._creatorParameters.length;
/* 164 */         for (int ix = 0; (ix = this._paramsSeenBig.nextClearBit(ix)) < len; ix++) {
/* 165 */           this._creatorParameters[ix] = _findMissing(props[ix]);
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/* 170 */     if (this._context.isEnabled(DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES)) {
/* 171 */       for (int ix = 0; ix < props.length; ix++) {
/* 172 */         if (this._creatorParameters[ix] == null) {
/* 173 */           SettableBeanProperty prop = props[ix];
/* 174 */           this._context.reportInputMismatch((BeanProperty)prop, "Null value for creator property '%s' (index %d); `DeserializationFeature.FAIL_ON_NULL_FOR_CREATOR_PARAMETERS` enabled", new Object[] { prop
/*     */                 
/* 176 */                 .getName(), Integer.valueOf(props[ix].getCreatorIndex()) });
/*     */         } 
/*     */       } 
/*     */     }
/* 180 */     return this._creatorParameters;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object _findMissing(SettableBeanProperty prop) throws JsonMappingException {
/* 186 */     Object injectableValueId = prop.getInjectableValueId();
/* 187 */     if (injectableValueId != null) {
/* 188 */       return this._context.findInjectableValue(prop.getInjectableValueId(), (BeanProperty)prop, null);
/*     */     }
/*     */ 
/*     */     
/* 192 */     if (prop.isRequired()) {
/* 193 */       this._context.reportInputMismatch((BeanProperty)prop, "Missing required creator property '%s' (index %d)", new Object[] { prop
/* 194 */             .getName(), Integer.valueOf(prop.getCreatorIndex()) });
/*     */     }
/* 196 */     if (this._context.isEnabled(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES)) {
/* 197 */       this._context.reportInputMismatch((BeanProperty)prop, "Missing creator property '%s' (index %d); `DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES` enabled", new Object[] { prop
/*     */             
/* 199 */             .getName(), Integer.valueOf(prop.getCreatorIndex()) });
/*     */     }
/*     */     
/* 202 */     Object nullValue = prop.getNullValueProvider().getNullValue(this._context);
/* 203 */     if (nullValue != null) {
/* 204 */       return nullValue;
/*     */     }
/*     */ 
/*     */     
/* 208 */     JsonDeserializer<Object> deser = prop.getValueDeserializer();
/* 209 */     return deser.getNullValue(this._context);
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
/*     */   public boolean readIdProperty(String propName) throws IOException {
/* 226 */     if (this._objectIdReader != null && propName.equals(this._objectIdReader.propertyName.getSimpleName())) {
/* 227 */       this._idValue = this._objectIdReader.readObjectReference(this._parser, this._context);
/* 228 */       return true;
/*     */     } 
/* 230 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object handleIdValue(DeserializationContext ctxt, Object bean) throws IOException {
/* 238 */     if (this._objectIdReader != null) {
/* 239 */       if (this._idValue != null) {
/* 240 */         ReadableObjectId roid = ctxt.findObjectId(this._idValue, this._objectIdReader.generator, this._objectIdReader.resolver);
/* 241 */         roid.bindItem(bean);
/*     */         
/* 243 */         SettableBeanProperty idProp = this._objectIdReader.idProperty;
/* 244 */         if (idProp != null) {
/* 245 */           return idProp.setAndReturn(bean, this._idValue);
/*     */         }
/*     */       } else {
/*     */         
/* 249 */         ctxt.reportUnresolvedObjectId(this._objectIdReader, bean);
/*     */       } 
/*     */     }
/* 252 */     return bean;
/*     */   }
/*     */   protected PropertyValue buffered() {
/* 255 */     return this._buffered;
/*     */   } public boolean isComplete() {
/* 257 */     return (this._paramsNeeded <= 0);
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
/*     */   public boolean assignParameter(SettableBeanProperty prop, Object value) {
/* 269 */     int ix = prop.getCreatorIndex();
/* 270 */     this._creatorParameters[ix] = value;
/* 271 */     if (this._paramsSeenBig == null) {
/* 272 */       int old = this._paramsSeen;
/* 273 */       int newValue = old | 1 << ix;
/*     */       
/* 275 */       this._paramsSeen = newValue;
/* 276 */       if (old != newValue && --this._paramsNeeded <= 0)
/*     */       {
/* 278 */         return (this._objectIdReader == null || this._idValue != null);
/*     */       }
/*     */     }
/*     */     else {
/*     */       
/* 283 */       this._paramsSeenBig.set(ix);
/* 284 */       if (this._paramsSeenBig.get(ix) || --this._paramsNeeded <= 0);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 289 */     return false;
/*     */   }
/*     */   
/*     */   public void bufferProperty(SettableBeanProperty prop, Object value) {
/* 293 */     this._buffered = new PropertyValue.Regular(this._buffered, value, prop);
/*     */   }
/*     */   
/*     */   public void bufferAnyProperty(SettableAnyProperty prop, String propName, Object value) {
/* 297 */     this._buffered = new PropertyValue.Any(this._buffered, value, prop, propName);
/*     */   }
/*     */   
/*     */   public void bufferMapProperty(Object key, Object value) {
/* 301 */     this._buffered = new PropertyValue.Map(this._buffered, value, key);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\deser\impl\PropertyValueBuffer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */