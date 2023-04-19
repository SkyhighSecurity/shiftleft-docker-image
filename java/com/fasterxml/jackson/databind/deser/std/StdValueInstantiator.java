/*     */ package com.fasterxml.jackson.databind.deser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.DeserializationConfig;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
/*     */ import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
/*     */ import com.fasterxml.jackson.databind.deser.ValueInstantiator;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
/*     */ import com.fasterxml.jackson.databind.util.ClassUtil;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @JacksonStdImpl
/*     */ public class StdValueInstantiator
/*     */   extends ValueInstantiator
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final String _valueTypeDesc;
/*     */   protected final Class<?> _valueClass;
/*     */   protected AnnotatedWithParams _defaultCreator;
/*     */   protected AnnotatedWithParams _withArgsCreator;
/*     */   protected SettableBeanProperty[] _constructorArguments;
/*     */   protected JavaType _delegateType;
/*     */   protected AnnotatedWithParams _delegateCreator;
/*     */   protected SettableBeanProperty[] _delegateArguments;
/*     */   protected JavaType _arrayDelegateType;
/*     */   protected AnnotatedWithParams _arrayDelegateCreator;
/*     */   protected SettableBeanProperty[] _arrayDelegateArguments;
/*     */   protected AnnotatedWithParams _fromStringCreator;
/*     */   protected AnnotatedWithParams _fromIntCreator;
/*     */   protected AnnotatedWithParams _fromLongCreator;
/*     */   protected AnnotatedWithParams _fromDoubleCreator;
/*     */   protected AnnotatedWithParams _fromBooleanCreator;
/*     */   protected AnnotatedParameter _incompleteParameter;
/*     */   
/*     */   @Deprecated
/*     */   public StdValueInstantiator(DeserializationConfig config, Class<?> valueType) {
/*  83 */     this._valueTypeDesc = ClassUtil.nameOf(valueType);
/*  84 */     this._valueClass = (valueType == null) ? Object.class : valueType;
/*     */   }
/*     */   
/*     */   public StdValueInstantiator(DeserializationConfig config, JavaType valueType) {
/*  88 */     this._valueTypeDesc = (valueType == null) ? "UNKNOWN TYPE" : valueType.toString();
/*  89 */     this._valueClass = (valueType == null) ? Object.class : valueType.getRawClass();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected StdValueInstantiator(StdValueInstantiator src) {
/*  98 */     this._valueTypeDesc = src._valueTypeDesc;
/*  99 */     this._valueClass = src._valueClass;
/*     */     
/* 101 */     this._defaultCreator = src._defaultCreator;
/*     */     
/* 103 */     this._constructorArguments = src._constructorArguments;
/* 104 */     this._withArgsCreator = src._withArgsCreator;
/*     */     
/* 106 */     this._delegateType = src._delegateType;
/* 107 */     this._delegateCreator = src._delegateCreator;
/* 108 */     this._delegateArguments = src._delegateArguments;
/*     */     
/* 110 */     this._arrayDelegateType = src._arrayDelegateType;
/* 111 */     this._arrayDelegateCreator = src._arrayDelegateCreator;
/* 112 */     this._arrayDelegateArguments = src._arrayDelegateArguments;
/*     */     
/* 114 */     this._fromStringCreator = src._fromStringCreator;
/* 115 */     this._fromIntCreator = src._fromIntCreator;
/* 116 */     this._fromLongCreator = src._fromLongCreator;
/* 117 */     this._fromDoubleCreator = src._fromDoubleCreator;
/* 118 */     this._fromBooleanCreator = src._fromBooleanCreator;
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
/*     */   public void configureFromObjectSettings(AnnotatedWithParams defaultCreator, AnnotatedWithParams delegateCreator, JavaType delegateType, SettableBeanProperty[] delegateArgs, AnnotatedWithParams withArgsCreator, SettableBeanProperty[] constructorArgs) {
/* 130 */     this._defaultCreator = defaultCreator;
/* 131 */     this._delegateCreator = delegateCreator;
/* 132 */     this._delegateType = delegateType;
/* 133 */     this._delegateArguments = delegateArgs;
/* 134 */     this._withArgsCreator = withArgsCreator;
/* 135 */     this._constructorArguments = constructorArgs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void configureFromArraySettings(AnnotatedWithParams arrayDelegateCreator, JavaType arrayDelegateType, SettableBeanProperty[] arrayDelegateArgs) {
/* 143 */     this._arrayDelegateCreator = arrayDelegateCreator;
/* 144 */     this._arrayDelegateType = arrayDelegateType;
/* 145 */     this._arrayDelegateArguments = arrayDelegateArgs;
/*     */   }
/*     */   
/*     */   public void configureFromStringCreator(AnnotatedWithParams creator) {
/* 149 */     this._fromStringCreator = creator;
/*     */   }
/*     */   
/*     */   public void configureFromIntCreator(AnnotatedWithParams creator) {
/* 153 */     this._fromIntCreator = creator;
/*     */   }
/*     */   
/*     */   public void configureFromLongCreator(AnnotatedWithParams creator) {
/* 157 */     this._fromLongCreator = creator;
/*     */   }
/*     */   
/*     */   public void configureFromDoubleCreator(AnnotatedWithParams creator) {
/* 161 */     this._fromDoubleCreator = creator;
/*     */   }
/*     */   
/*     */   public void configureFromBooleanCreator(AnnotatedWithParams creator) {
/* 165 */     this._fromBooleanCreator = creator;
/*     */   }
/*     */   
/*     */   public void configureIncompleteParameter(AnnotatedParameter parameter) {
/* 169 */     this._incompleteParameter = parameter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getValueTypeDesc() {
/* 180 */     return this._valueTypeDesc;
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?> getValueClass() {
/* 185 */     return this._valueClass;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canCreateFromString() {
/* 190 */     return (this._fromStringCreator != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canCreateFromInt() {
/* 195 */     return (this._fromIntCreator != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canCreateFromLong() {
/* 200 */     return (this._fromLongCreator != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canCreateFromDouble() {
/* 205 */     return (this._fromDoubleCreator != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canCreateFromBoolean() {
/* 210 */     return (this._fromBooleanCreator != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canCreateUsingDefault() {
/* 215 */     return (this._defaultCreator != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canCreateUsingDelegate() {
/* 220 */     return (this._delegateType != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canCreateUsingArrayDelegate() {
/* 225 */     return (this._arrayDelegateType != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canCreateFromObjectWith() {
/* 230 */     return (this._withArgsCreator != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canInstantiate() {
/* 235 */     return (canCreateUsingDefault() || 
/* 236 */       canCreateUsingDelegate() || canCreateUsingArrayDelegate() || 
/* 237 */       canCreateFromObjectWith() || canCreateFromString() || 
/* 238 */       canCreateFromInt() || canCreateFromLong() || 
/* 239 */       canCreateFromDouble() || canCreateFromBoolean());
/*     */   }
/*     */ 
/*     */   
/*     */   public JavaType getDelegateType(DeserializationConfig config) {
/* 244 */     return this._delegateType;
/*     */   }
/*     */ 
/*     */   
/*     */   public JavaType getArrayDelegateType(DeserializationConfig config) {
/* 249 */     return this._arrayDelegateType;
/*     */   }
/*     */ 
/*     */   
/*     */   public SettableBeanProperty[] getFromObjectArguments(DeserializationConfig config) {
/* 254 */     return this._constructorArguments;
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
/*     */   public Object createUsingDefault(DeserializationContext ctxt) throws IOException {
/* 266 */     if (this._defaultCreator == null) {
/* 267 */       return super.createUsingDefault(ctxt);
/*     */     }
/*     */     try {
/* 270 */       return this._defaultCreator.call();
/* 271 */     } catch (Exception e) {
/* 272 */       return ctxt.handleInstantiationProblem(this._valueClass, null, (Throwable)rewrapCtorProblem(ctxt, e));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object createFromObjectWith(DeserializationContext ctxt, Object[] args) throws IOException {
/* 279 */     if (this._withArgsCreator == null) {
/* 280 */       return super.createFromObjectWith(ctxt, args);
/*     */     }
/*     */     try {
/* 283 */       return this._withArgsCreator.call(args);
/* 284 */     } catch (Exception e) {
/* 285 */       return ctxt.handleInstantiationProblem(this._valueClass, args, (Throwable)rewrapCtorProblem(ctxt, e));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object createUsingDelegate(DeserializationContext ctxt, Object delegate) throws IOException {
/* 293 */     if (this._delegateCreator == null && 
/* 294 */       this._arrayDelegateCreator != null) {
/* 295 */       return _createUsingDelegate(this._arrayDelegateCreator, this._arrayDelegateArguments, ctxt, delegate);
/*     */     }
/*     */     
/* 298 */     return _createUsingDelegate(this._delegateCreator, this._delegateArguments, ctxt, delegate);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object createUsingArrayDelegate(DeserializationContext ctxt, Object delegate) throws IOException {
/* 304 */     if (this._arrayDelegateCreator == null && 
/* 305 */       this._delegateCreator != null)
/*     */     {
/* 307 */       return createUsingDelegate(ctxt, delegate);
/*     */     }
/*     */     
/* 310 */     return _createUsingDelegate(this._arrayDelegateCreator, this._arrayDelegateArguments, ctxt, delegate);
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
/* 322 */     if (this._fromStringCreator == null) {
/* 323 */       return _createFromStringFallbacks(ctxt, value);
/*     */     }
/*     */     try {
/* 326 */       return this._fromStringCreator.call1(value);
/* 327 */     } catch (Throwable t) {
/* 328 */       return ctxt.handleInstantiationProblem(this._fromStringCreator.getDeclaringClass(), value, (Throwable)
/* 329 */           rewrapCtorProblem(ctxt, t));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object createFromInt(DeserializationContext ctxt, int value) throws IOException {
/* 337 */     if (this._fromIntCreator != null) {
/* 338 */       Object arg = Integer.valueOf(value);
/*     */       try {
/* 340 */         return this._fromIntCreator.call1(arg);
/* 341 */       } catch (Throwable t0) {
/* 342 */         return ctxt.handleInstantiationProblem(this._fromIntCreator.getDeclaringClass(), arg, (Throwable)
/* 343 */             rewrapCtorProblem(ctxt, t0));
/*     */       } 
/*     */     } 
/*     */     
/* 347 */     if (this._fromLongCreator != null) {
/* 348 */       Object arg = Long.valueOf(value);
/*     */       try {
/* 350 */         return this._fromLongCreator.call1(arg);
/* 351 */       } catch (Throwable t0) {
/* 352 */         return ctxt.handleInstantiationProblem(this._fromLongCreator.getDeclaringClass(), arg, (Throwable)
/* 353 */             rewrapCtorProblem(ctxt, t0));
/*     */       } 
/*     */     } 
/* 356 */     return super.createFromInt(ctxt, value);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object createFromLong(DeserializationContext ctxt, long value) throws IOException {
/* 362 */     if (this._fromLongCreator == null) {
/* 363 */       return super.createFromLong(ctxt, value);
/*     */     }
/* 365 */     Object arg = Long.valueOf(value);
/*     */     try {
/* 367 */       return this._fromLongCreator.call1(arg);
/* 368 */     } catch (Throwable t0) {
/* 369 */       return ctxt.handleInstantiationProblem(this._fromLongCreator.getDeclaringClass(), arg, (Throwable)
/* 370 */           rewrapCtorProblem(ctxt, t0));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object createFromDouble(DeserializationContext ctxt, double value) throws IOException {
/* 377 */     if (this._fromDoubleCreator == null) {
/* 378 */       return super.createFromDouble(ctxt, value);
/*     */     }
/* 380 */     Object arg = Double.valueOf(value);
/*     */     try {
/* 382 */       return this._fromDoubleCreator.call1(arg);
/* 383 */     } catch (Throwable t0) {
/* 384 */       return ctxt.handleInstantiationProblem(this._fromDoubleCreator.getDeclaringClass(), arg, (Throwable)
/* 385 */           rewrapCtorProblem(ctxt, t0));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object createFromBoolean(DeserializationContext ctxt, boolean value) throws IOException {
/* 392 */     if (this._fromBooleanCreator == null) {
/* 393 */       return super.createFromBoolean(ctxt, value);
/*     */     }
/* 395 */     Boolean arg = Boolean.valueOf(value);
/*     */     try {
/* 397 */       return this._fromBooleanCreator.call1(arg);
/* 398 */     } catch (Throwable t0) {
/* 399 */       return ctxt.handleInstantiationProblem(this._fromBooleanCreator.getDeclaringClass(), arg, (Throwable)
/* 400 */           rewrapCtorProblem(ctxt, t0));
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
/*     */   public AnnotatedWithParams getDelegateCreator() {
/* 412 */     return this._delegateCreator;
/*     */   }
/*     */ 
/*     */   
/*     */   public AnnotatedWithParams getArrayDelegateCreator() {
/* 417 */     return this._arrayDelegateCreator;
/*     */   }
/*     */ 
/*     */   
/*     */   public AnnotatedWithParams getDefaultCreator() {
/* 422 */     return this._defaultCreator;
/*     */   }
/*     */ 
/*     */   
/*     */   public AnnotatedWithParams getWithArgsCreator() {
/* 427 */     return this._withArgsCreator;
/*     */   }
/*     */ 
/*     */   
/*     */   public AnnotatedParameter getIncompleteParameter() {
/* 432 */     return this._incompleteParameter;
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
/*     */   @Deprecated
/*     */   protected JsonMappingException wrapException(Throwable t) {
/* 450 */     for (Throwable curr = t; curr != null; curr = curr.getCause()) {
/* 451 */       if (curr instanceof JsonMappingException) {
/* 452 */         return (JsonMappingException)curr;
/*     */       }
/*     */     } 
/* 455 */     return new JsonMappingException(null, "Instantiation of " + 
/* 456 */         getValueTypeDesc() + " value failed: " + ClassUtil.exceptionMessage(t), t);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected JsonMappingException unwrapAndWrapException(DeserializationContext ctxt, Throwable t) {
/* 468 */     for (Throwable curr = t; curr != null; curr = curr.getCause()) {
/* 469 */       if (curr instanceof JsonMappingException) {
/* 470 */         return (JsonMappingException)curr;
/*     */       }
/*     */     } 
/* 473 */     return ctxt.instantiationException(getValueClass(), t);
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
/*     */   protected JsonMappingException wrapAsJsonMappingException(DeserializationContext ctxt, Throwable t) {
/* 488 */     if (t instanceof JsonMappingException) {
/* 489 */       return (JsonMappingException)t;
/*     */     }
/* 491 */     return ctxt.instantiationException(getValueClass(), t);
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
/*     */   protected JsonMappingException rewrapCtorProblem(DeserializationContext ctxt, Throwable t) {
/* 506 */     if (t instanceof ExceptionInInitializerError || t instanceof java.lang.reflect.InvocationTargetException) {
/*     */ 
/*     */       
/* 509 */       Throwable cause = t.getCause();
/* 510 */       if (cause != null) {
/* 511 */         t = cause;
/*     */       }
/*     */     } 
/* 514 */     return wrapAsJsonMappingException(ctxt, t);
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
/*     */   private Object _createUsingDelegate(AnnotatedWithParams delegateCreator, SettableBeanProperty[] delegateArguments, DeserializationContext ctxt, Object delegate) throws IOException {
/* 529 */     if (delegateCreator == null) {
/* 530 */       throw new IllegalStateException("No delegate constructor for " + getValueTypeDesc());
/*     */     }
/*     */     
/*     */     try {
/* 534 */       if (delegateArguments == null) {
/* 535 */         return delegateCreator.call1(delegate);
/*     */       }
/*     */       
/* 538 */       int len = delegateArguments.length;
/* 539 */       Object[] args = new Object[len];
/* 540 */       for (int i = 0; i < len; i++) {
/* 541 */         SettableBeanProperty prop = delegateArguments[i];
/* 542 */         if (prop == null) {
/* 543 */           args[i] = delegate;
/*     */         } else {
/* 545 */           args[i] = ctxt.findInjectableValue(prop.getInjectableValueId(), (BeanProperty)prop, null);
/*     */         } 
/*     */       } 
/*     */       
/* 549 */       return delegateCreator.call(args);
/* 550 */     } catch (Throwable t) {
/* 551 */       throw rewrapCtorProblem(ctxt, t);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\deser\std\StdValueInstantiator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */