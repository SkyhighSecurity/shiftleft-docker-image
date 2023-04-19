/*     */ package com.fasterxml.jackson.databind.deser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.deser.BeanDeserializer;
/*     */ import com.fasterxml.jackson.databind.deser.BeanDeserializerBase;
/*     */ import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
/*     */ import com.fasterxml.jackson.databind.util.NameTransformer;
/*     */ import java.io.IOException;
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
/*     */ public class ThrowableDeserializer
/*     */   extends BeanDeserializer
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected static final String PROP_NAME_MESSAGE = "message";
/*     */   
/*     */   public ThrowableDeserializer(BeanDeserializer baseDeserializer) {
/*  30 */     super((BeanDeserializerBase)baseDeserializer);
/*     */     
/*  32 */     this._vanillaProcessing = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ThrowableDeserializer(BeanDeserializer src, NameTransformer unwrapper) {
/*  39 */     super((BeanDeserializerBase)src, unwrapper);
/*     */   }
/*     */ 
/*     */   
/*     */   public JsonDeserializer<Object> unwrappingDeserializer(NameTransformer unwrapper) {
/*  44 */     if (getClass() != ThrowableDeserializer.class) {
/*  45 */       return (JsonDeserializer<Object>)this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  51 */     return (JsonDeserializer<Object>)new ThrowableDeserializer(this, unwrapper);
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
/*     */   public Object deserializeFromObject(JsonParser p, DeserializationContext ctxt) throws IOException {
/*  64 */     if (this._propertyBasedCreator != null) {
/*  65 */       return _deserializeUsingPropertyBased(p, ctxt);
/*     */     }
/*  67 */     if (this._delegateDeserializer != null) {
/*  68 */       return this._valueInstantiator.createUsingDelegate(ctxt, this._delegateDeserializer
/*  69 */           .deserialize(p, ctxt));
/*     */     }
/*  71 */     if (this._beanType.isAbstract()) {
/*  72 */       return ctxt.handleMissingInstantiator(handledType(), getValueInstantiator(), p, "abstract type (need to add/enable type information?)", new Object[0]);
/*     */     }
/*     */     
/*  75 */     boolean hasStringCreator = this._valueInstantiator.canCreateFromString();
/*  76 */     boolean hasDefaultCtor = this._valueInstantiator.canCreateUsingDefault();
/*     */     
/*  78 */     if (!hasStringCreator && !hasDefaultCtor) {
/*  79 */       return ctxt.handleMissingInstantiator(handledType(), getValueInstantiator(), p, "Throwable needs a default constructor, a single-String-arg constructor; or explicit @JsonCreator", new Object[0]);
/*     */     }
/*     */ 
/*     */     
/*  83 */     Object throwable = null;
/*  84 */     Object[] pending = null;
/*  85 */     int pendingIx = 0;
/*     */     
/*  87 */     for (; p.getCurrentToken() != JsonToken.END_OBJECT; p.nextToken()) {
/*  88 */       String propName = p.getCurrentName();
/*  89 */       SettableBeanProperty prop = this._beanProperties.find(propName);
/*  90 */       p.nextToken();
/*     */       
/*  92 */       if (prop != null) {
/*  93 */         if (throwable != null) {
/*  94 */           prop.deserializeAndSet(p, ctxt, throwable);
/*     */         }
/*     */         else {
/*     */           
/*  98 */           if (pending == null) {
/*  99 */             int len = this._beanProperties.size();
/* 100 */             pending = new Object[len + len];
/*     */           } 
/* 102 */           pending[pendingIx++] = prop;
/* 103 */           pending[pendingIx++] = prop.deserialize(p, ctxt);
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/* 108 */         boolean isMessage = "message".equals(propName);
/* 109 */         if (isMessage && 
/* 110 */           hasStringCreator) {
/* 111 */           throwable = this._valueInstantiator.createFromString(ctxt, p.getValueAsString());
/*     */           
/* 113 */           if (pending != null) {
/* 114 */             for (int i = 0, len = pendingIx; i < len; i += 2) {
/* 115 */               prop = (SettableBeanProperty)pending[i];
/* 116 */               prop.set(throwable, pending[i + 1]);
/*     */             } 
/* 118 */             pending = null;
/*     */           
/*     */           }
/*     */ 
/*     */         
/*     */         }
/* 124 */         else if (this._ignorableProps != null && this._ignorableProps.contains(propName)) {
/* 125 */           p.skipChildren();
/*     */         
/*     */         }
/* 128 */         else if (this._anySetter != null) {
/* 129 */           this._anySetter.deserializeAndSet(p, ctxt, throwable, propName);
/*     */ 
/*     */         
/*     */         }
/*     */         else {
/*     */ 
/*     */           
/* 136 */           handleUnknownProperty(p, ctxt, throwable, propName);
/*     */         } 
/*     */       } 
/* 139 */     }  if (throwable == null) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 146 */       if (hasStringCreator) {
/* 147 */         throwable = this._valueInstantiator.createFromString(ctxt, null);
/*     */       } else {
/* 149 */         throwable = this._valueInstantiator.createUsingDefault(ctxt);
/*     */       } 
/*     */       
/* 152 */       if (pending != null) {
/* 153 */         for (int i = 0, len = pendingIx; i < len; i += 2) {
/* 154 */           SettableBeanProperty prop = (SettableBeanProperty)pending[i];
/* 155 */           prop.set(throwable, pending[i + 1]);
/*     */         } 
/*     */       }
/*     */     } 
/* 159 */     return throwable;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\deser\std\ThrowableDeserializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */