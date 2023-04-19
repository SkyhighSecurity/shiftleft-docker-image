/*     */ package com.fasterxml.jackson.databind.deser;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.DeserializationConfig;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.DeserializationFeature;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.deser.impl.PropertyValueBuffer;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
/*     */ import java.io.IOException;
/*     */ import java.io.Serializable;
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
/*     */ public abstract class ValueInstantiator
/*     */ {
/*     */   public Class<?> getValueClass() {
/*  50 */     return Object.class;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getValueTypeDesc() {
/*  58 */     Class<?> cls = getValueClass();
/*  59 */     if (cls == null) {
/*  60 */       return "UNKNOWN";
/*     */     }
/*  62 */     return cls.getName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canInstantiate() {
/*  71 */     return (canCreateUsingDefault() || 
/*  72 */       canCreateUsingDelegate() || canCreateUsingArrayDelegate() || 
/*  73 */       canCreateFromObjectWith() || canCreateFromString() || 
/*  74 */       canCreateFromInt() || canCreateFromLong() || 
/*  75 */       canCreateFromDouble() || canCreateFromBoolean());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canCreateFromString() {
/*  82 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canCreateFromInt() {
/*  88 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canCreateFromLong() {
/*  94 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canCreateFromDouble() {
/* 100 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canCreateFromBoolean() {
/* 106 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canCreateUsingDefault() {
/* 113 */     return (getDefaultCreator() != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canCreateUsingDelegate() {
/* 120 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canCreateUsingArrayDelegate() {
/* 129 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canCreateFromObjectWith() {
/* 136 */     return false;
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
/*     */   public SettableBeanProperty[] getFromObjectArguments(DeserializationConfig config) {
/* 149 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JavaType getDelegateType(DeserializationConfig config) {
/* 159 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JavaType getArrayDelegateType(DeserializationConfig config) {
/* 170 */     return null;
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
/*     */   public Object createUsingDefault(DeserializationContext ctxt) throws IOException {
/* 189 */     return ctxt.handleMissingInstantiator(getValueClass(), this, null, "no default no-arguments constructor found", new Object[0]);
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
/*     */   public Object createFromObjectWith(DeserializationContext ctxt, Object[] args) throws IOException {
/* 203 */     return ctxt.handleMissingInstantiator(getValueClass(), this, null, "no creator with arguments specified", new Object[0]);
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
/*     */   
/*     */   public Object createFromObjectWith(DeserializationContext ctxt, SettableBeanProperty[] props, PropertyValueBuffer buffer) throws IOException {
/* 229 */     return createFromObjectWith(ctxt, buffer.getParameters(props));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object createUsingDelegate(DeserializationContext ctxt, Object delegate) throws IOException {
/* 237 */     return ctxt.handleMissingInstantiator(getValueClass(), this, null, "no delegate creator specified", new Object[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object createUsingArrayDelegate(DeserializationContext ctxt, Object delegate) throws IOException {
/* 246 */     return ctxt.handleMissingInstantiator(getValueClass(), this, null, "no array delegate creator specified", new Object[0]);
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
/*     */   public Object createFromString(DeserializationContext ctxt, String value) throws IOException {
/* 258 */     return _createFromStringFallbacks(ctxt, value);
/*     */   }
/*     */   
/*     */   public Object createFromInt(DeserializationContext ctxt, int value) throws IOException {
/* 262 */     return ctxt.handleMissingInstantiator(getValueClass(), this, null, "no int/Int-argument constructor/factory method to deserialize from Number value (%s)", new Object[] {
/*     */           
/* 264 */           Integer.valueOf(value) });
/*     */   }
/*     */   
/*     */   public Object createFromLong(DeserializationContext ctxt, long value) throws IOException {
/* 268 */     return ctxt.handleMissingInstantiator(getValueClass(), this, null, "no long/Long-argument constructor/factory method to deserialize from Number value (%s)", new Object[] {
/*     */           
/* 270 */           Long.valueOf(value) });
/*     */   }
/*     */   
/*     */   public Object createFromDouble(DeserializationContext ctxt, double value) throws IOException {
/* 274 */     return ctxt.handleMissingInstantiator(getValueClass(), this, null, "no double/Double-argument constructor/factory method to deserialize from Number value (%s)", new Object[] {
/*     */           
/* 276 */           Double.valueOf(value) });
/*     */   }
/*     */   
/*     */   public Object createFromBoolean(DeserializationContext ctxt, boolean value) throws IOException {
/* 280 */     return ctxt.handleMissingInstantiator(getValueClass(), this, null, "no boolean/Boolean-argument constructor/factory method to deserialize from boolean value (%s)", new Object[] {
/*     */           
/* 282 */           Boolean.valueOf(value)
/*     */         });
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
/*     */   public AnnotatedWithParams getDefaultCreator() {
/* 301 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotatedWithParams getDelegateCreator() {
/* 311 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotatedWithParams getArrayDelegateCreator() {
/* 321 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotatedWithParams getWithArgsCreator() {
/* 332 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotatedParameter getIncompleteParameter() {
/* 338 */     return null;
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
/*     */   protected Object _createFromStringFallbacks(DeserializationContext ctxt, String value) throws IOException {
/* 356 */     if (canCreateFromBoolean()) {
/* 357 */       String str = value.trim();
/* 358 */       if ("true".equals(str)) {
/* 359 */         return createFromBoolean(ctxt, true);
/*     */       }
/* 361 */       if ("false".equals(str)) {
/* 362 */         return createFromBoolean(ctxt, false);
/*     */       }
/*     */     } 
/*     */     
/* 366 */     if (value.length() == 0 && 
/* 367 */       ctxt.isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)) {
/* 368 */       return null;
/*     */     }
/*     */     
/* 371 */     return ctxt.handleMissingInstantiator(getValueClass(), this, ctxt.getParser(), "no String-argument constructor/factory method to deserialize from String value ('%s')", new Object[] { value });
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
/*     */   public static class Base
/*     */     extends ValueInstantiator
/*     */     implements Serializable
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected final Class<?> _valueType;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Base(Class<?> type) {
/* 407 */       this._valueType = type;
/*     */     }
/*     */     
/*     */     public Base(JavaType type) {
/* 411 */       this._valueType = type.getRawClass();
/*     */     }
/*     */ 
/*     */     
/*     */     public String getValueTypeDesc() {
/* 416 */       return this._valueType.getName();
/*     */     }
/*     */ 
/*     */     
/*     */     public Class<?> getValueClass() {
/* 421 */       return this._valueType;
/*     */     }
/*     */   }
/*     */   
/*     */   public static interface Gettable {
/*     */     ValueInstantiator getValueInstantiator();
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\deser\ValueInstantiator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */