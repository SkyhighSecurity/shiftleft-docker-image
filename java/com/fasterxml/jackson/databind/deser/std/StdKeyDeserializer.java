/*     */ package com.fasterxml.jackson.databind.deser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonProcessingException;
/*     */ import com.fasterxml.jackson.core.io.NumberInput;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.DeserializationFeature;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.KeyDeserializer;
/*     */ import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
/*     */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*     */ import com.fasterxml.jackson.databind.util.EnumResolver;
/*     */ import com.fasterxml.jackson.databind.util.TokenBuffer;
/*     */ import java.io.IOException;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.util.Calendar;
/*     */ import java.util.Currency;
/*     */ import java.util.Date;
/*     */ import java.util.Locale;
/*     */ import java.util.UUID;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @JacksonStdImpl
/*     */ public class StdKeyDeserializer
/*     */   extends KeyDeserializer
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   public static final int TYPE_BOOLEAN = 1;
/*     */   public static final int TYPE_BYTE = 2;
/*     */   public static final int TYPE_SHORT = 3;
/*     */   public static final int TYPE_CHAR = 4;
/*     */   public static final int TYPE_INT = 5;
/*     */   public static final int TYPE_LONG = 6;
/*     */   public static final int TYPE_FLOAT = 7;
/*     */   public static final int TYPE_DOUBLE = 8;
/*     */   public static final int TYPE_LOCALE = 9;
/*     */   public static final int TYPE_DATE = 10;
/*     */   public static final int TYPE_CALENDAR = 11;
/*     */   public static final int TYPE_UUID = 12;
/*     */   public static final int TYPE_URI = 13;
/*     */   public static final int TYPE_URL = 14;
/*     */   public static final int TYPE_CLASS = 15;
/*     */   public static final int TYPE_CURRENCY = 16;
/*     */   public static final int TYPE_BYTE_ARRAY = 17;
/*     */   protected final int _kind;
/*     */   protected final Class<?> _keyClass;
/*     */   protected final FromStringDeserializer<?> _deser;
/*     */   
/*     */   protected StdKeyDeserializer(int kind, Class<?> cls) {
/*  62 */     this(kind, cls, null);
/*     */   }
/*     */   
/*     */   protected StdKeyDeserializer(int kind, Class<?> cls, FromStringDeserializer<?> deser) {
/*  66 */     this._kind = kind;
/*  67 */     this._keyClass = cls;
/*  68 */     this._deser = deser;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static StdKeyDeserializer forType(Class<?> raw) {
/*     */     int kind;
/*  76 */     if (raw == String.class || raw == Object.class || raw == CharSequence.class || raw == Serializable.class)
/*     */     {
/*     */ 
/*     */       
/*  80 */       return StringKD.forType(raw);
/*     */     }
/*  82 */     if (raw == UUID.class)
/*  83 */     { kind = 12; }
/*  84 */     else if (raw == Integer.class)
/*  85 */     { kind = 5; }
/*  86 */     else if (raw == Long.class)
/*  87 */     { kind = 6; }
/*  88 */     else if (raw == Date.class)
/*  89 */     { kind = 10; }
/*  90 */     else if (raw == Calendar.class)
/*  91 */     { kind = 11; }
/*     */     
/*  93 */     else if (raw == Boolean.class)
/*  94 */     { kind = 1; }
/*  95 */     else if (raw == Byte.class)
/*  96 */     { kind = 2; }
/*  97 */     else if (raw == Character.class)
/*  98 */     { kind = 4; }
/*  99 */     else if (raw == Short.class)
/* 100 */     { kind = 3; }
/* 101 */     else if (raw == Float.class)
/* 102 */     { kind = 7; }
/* 103 */     else if (raw == Double.class)
/* 104 */     { kind = 8; }
/* 105 */     else if (raw == URI.class)
/* 106 */     { kind = 13; }
/* 107 */     else if (raw == URL.class)
/* 108 */     { kind = 14; }
/* 109 */     else if (raw == Class.class)
/* 110 */     { kind = 15; }
/* 111 */     else { if (raw == Locale.class) {
/* 112 */         FromStringDeserializer<?> deser = FromStringDeserializer.findDeserializer(Locale.class);
/* 113 */         return new StdKeyDeserializer(9, raw, deser);
/* 114 */       }  if (raw == Currency.class) {
/* 115 */         FromStringDeserializer<?> deser = FromStringDeserializer.findDeserializer(Currency.class);
/* 116 */         return new StdKeyDeserializer(16, raw, deser);
/* 117 */       }  if (raw == byte[].class) {
/* 118 */         kind = 17;
/*     */       } else {
/* 120 */         return null;
/*     */       }  }
/* 122 */      return new StdKeyDeserializer(kind, raw);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException {
/* 129 */     if (key == null) {
/* 130 */       return null;
/*     */     }
/*     */     try {
/* 133 */       Object result = _parse(key, ctxt);
/* 134 */       if (result != null) {
/* 135 */         return result;
/*     */       }
/* 137 */     } catch (Exception re) {
/* 138 */       return ctxt.handleWeirdKey(this._keyClass, key, "not a valid representation, problem: (%s) %s", new Object[] { re
/* 139 */             .getClass().getName(), 
/* 140 */             ClassUtil.exceptionMessage(re) });
/*     */     } 
/* 142 */     if (this._keyClass.isEnum() && ctxt.getConfig().isEnabled(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL)) {
/* 143 */       return null;
/*     */     }
/* 145 */     return ctxt.handleWeirdKey(this._keyClass, key, "not a valid representation", new Object[0]);
/*     */   }
/*     */   public Class<?> getKeyClass() {
/* 148 */     return this._keyClass;
/*     */   }
/*     */   protected Object _parse(String key, DeserializationContext ctxt) throws Exception {
/*     */     int value;
/* 152 */     switch (this._kind) {
/*     */       case 1:
/* 154 */         if ("true".equals(key)) {
/* 155 */           return Boolean.TRUE;
/*     */         }
/* 157 */         if ("false".equals(key)) {
/* 158 */           return Boolean.FALSE;
/*     */         }
/* 160 */         return ctxt.handleWeirdKey(this._keyClass, key, "value not 'true' or 'false'", new Object[0]);
/*     */       
/*     */       case 2:
/* 163 */         value = _parseInt(key);
/*     */         
/* 165 */         if (value < -128 || value > 255) {
/* 166 */           return ctxt.handleWeirdKey(this._keyClass, key, "overflow, value cannot be represented as 8-bit value", new Object[0]);
/*     */         }
/* 168 */         return Byte.valueOf((byte)value);
/*     */ 
/*     */       
/*     */       case 3:
/* 172 */         value = _parseInt(key);
/* 173 */         if (value < -32768 || value > 32767) {
/* 174 */           return ctxt.handleWeirdKey(this._keyClass, key, "overflow, value cannot be represented as 16-bit value", new Object[0]);
/*     */         }
/*     */         
/* 177 */         return Short.valueOf((short)value);
/*     */       
/*     */       case 4:
/* 180 */         if (key.length() == 1) {
/* 181 */           return Character.valueOf(key.charAt(0));
/*     */         }
/* 183 */         return ctxt.handleWeirdKey(this._keyClass, key, "can only convert 1-character Strings", new Object[0]);
/*     */       case 5:
/* 185 */         return Integer.valueOf(_parseInt(key));
/*     */       
/*     */       case 6:
/* 188 */         return Long.valueOf(_parseLong(key));
/*     */ 
/*     */       
/*     */       case 7:
/* 192 */         return Float.valueOf((float)_parseDouble(key));
/*     */       case 8:
/* 194 */         return Double.valueOf(_parseDouble(key));
/*     */       case 9:
/*     */         try {
/* 197 */           return this._deser._deserialize(key, ctxt);
/* 198 */         } catch (IllegalArgumentException e) {
/* 199 */           return _weirdKey(ctxt, key, e);
/*     */         } 
/*     */       case 16:
/*     */         try {
/* 203 */           return this._deser._deserialize(key, ctxt);
/* 204 */         } catch (IllegalArgumentException e) {
/* 205 */           return _weirdKey(ctxt, key, e);
/*     */         } 
/*     */       case 10:
/* 208 */         return ctxt.parseDate(key);
/*     */       case 11:
/* 210 */         return ctxt.constructCalendar(ctxt.parseDate(key));
/*     */       case 12:
/*     */         try {
/* 213 */           return UUID.fromString(key);
/* 214 */         } catch (Exception e) {
/* 215 */           return _weirdKey(ctxt, key, e);
/*     */         } 
/*     */       case 13:
/*     */         try {
/* 219 */           return URI.create(key);
/* 220 */         } catch (Exception e) {
/* 221 */           return _weirdKey(ctxt, key, e);
/*     */         } 
/*     */       case 14:
/*     */         try {
/* 225 */           return new URL(key);
/* 226 */         } catch (MalformedURLException e) {
/* 227 */           return _weirdKey(ctxt, key, e);
/*     */         } 
/*     */       case 15:
/*     */         try {
/* 231 */           return ctxt.findClass(key);
/* 232 */         } catch (Exception e) {
/* 233 */           return ctxt.handleWeirdKey(this._keyClass, key, "unable to parse key as Class", new Object[0]);
/*     */         } 
/*     */       case 17:
/*     */         try {
/* 237 */           return ctxt.getConfig().getBase64Variant().decode(key);
/* 238 */         } catch (IllegalArgumentException e) {
/* 239 */           return _weirdKey(ctxt, key, e);
/*     */         } 
/*     */     } 
/* 242 */     throw new IllegalStateException("Internal error: unknown key type " + this._keyClass);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int _parseInt(String key) throws IllegalArgumentException {
/* 253 */     return Integer.parseInt(key);
/*     */   }
/*     */   
/*     */   protected long _parseLong(String key) throws IllegalArgumentException {
/* 257 */     return Long.parseLong(key);
/*     */   }
/*     */   
/*     */   protected double _parseDouble(String key) throws IllegalArgumentException {
/* 261 */     return NumberInput.parseDouble(key);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Object _weirdKey(DeserializationContext ctxt, String key, Exception e) throws IOException {
/* 266 */     return ctxt.handleWeirdKey(this._keyClass, key, "problem: %s", new Object[] {
/* 267 */           ClassUtil.exceptionMessage(e)
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @JacksonStdImpl
/*     */   static final class StringKD
/*     */     extends StdKeyDeserializer
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */ 
/*     */     
/* 280 */     private static final StringKD sString = new StringKD(String.class);
/* 281 */     private static final StringKD sObject = new StringKD(Object.class);
/*     */     private StringKD(Class<?> nominalType) {
/* 283 */       super(-1, nominalType);
/*     */     }
/*     */     
/*     */     public static StringKD forType(Class<?> nominalType) {
/* 287 */       if (nominalType == String.class) {
/* 288 */         return sString;
/*     */       }
/* 290 */       if (nominalType == Object.class) {
/* 291 */         return sObject;
/*     */       }
/* 293 */       return new StringKD(nominalType);
/*     */     }
/*     */ 
/*     */     
/*     */     public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException, JsonProcessingException {
/* 298 */       return key;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final class DelegatingKD
/*     */     extends KeyDeserializer
/*     */     implements Serializable
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */ 
/*     */ 
/*     */     
/*     */     protected final Class<?> _keyClass;
/*     */ 
/*     */ 
/*     */     
/*     */     protected final JsonDeserializer<?> _delegate;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected DelegatingKD(Class<?> cls, JsonDeserializer<?> deser) {
/* 324 */       this._keyClass = cls;
/* 325 */       this._delegate = deser;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public final Object deserializeKey(String key, DeserializationContext ctxt) throws IOException {
/* 333 */       if (key == null) {
/* 334 */         return null;
/*     */       }
/* 336 */       TokenBuffer tb = new TokenBuffer(ctxt.getParser(), ctxt);
/* 337 */       tb.writeString(key);
/*     */       
/*     */       try {
/* 340 */         JsonParser p = tb.asParser();
/* 341 */         p.nextToken();
/* 342 */         Object result = this._delegate.deserialize(p, ctxt);
/* 343 */         if (result != null) {
/* 344 */           return result;
/*     */         }
/* 346 */         return ctxt.handleWeirdKey(this._keyClass, key, "not a valid representation", new Object[0]);
/* 347 */       } catch (Exception re) {
/* 348 */         return ctxt.handleWeirdKey(this._keyClass, key, "not a valid representation: %s", new Object[] { re.getMessage() });
/*     */       } 
/*     */     }
/*     */     public Class<?> getKeyClass() {
/* 352 */       return this._keyClass;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @JacksonStdImpl
/*     */   static final class EnumKD
/*     */     extends StdKeyDeserializer
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */ 
/*     */     
/*     */     protected final EnumResolver _byNameResolver;
/*     */     
/*     */     protected final AnnotatedMethod _factory;
/*     */     
/*     */     protected EnumResolver _byToStringResolver;
/*     */     
/*     */     protected final Enum<?> _enumDefaultValue;
/*     */ 
/*     */     
/*     */     protected EnumKD(EnumResolver er, AnnotatedMethod factory) {
/* 375 */       super(-1, er.getEnumClass());
/* 376 */       this._byNameResolver = er;
/* 377 */       this._factory = factory;
/* 378 */       this._enumDefaultValue = er.getDefaultValue();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Object _parse(String key, DeserializationContext ctxt) throws IOException {
/* 384 */       if (this._factory != null) {
/*     */         try {
/* 386 */           return this._factory.call1(key);
/* 387 */         } catch (Exception exception) {
/* 388 */           ClassUtil.unwrapAndThrowAsIAE(exception);
/*     */         } 
/*     */       }
/*     */       
/* 392 */       EnumResolver res = ctxt.isEnabled(DeserializationFeature.READ_ENUMS_USING_TO_STRING) ? _getToStringResolver(ctxt) : this._byNameResolver;
/* 393 */       Enum<?> e = res.findEnum(key);
/* 394 */       if (e == null) {
/* 395 */         if (this._enumDefaultValue != null && ctxt
/* 396 */           .isEnabled(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE)) {
/* 397 */           e = this._enumDefaultValue;
/* 398 */         } else if (!ctxt.isEnabled(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL)) {
/* 399 */           return ctxt.handleWeirdKey(this._keyClass, key, "not one of the values accepted for Enum class: %s", new Object[] { res
/* 400 */                 .getEnumIds() });
/*     */         } 
/*     */       }
/*     */       
/* 404 */       return e;
/*     */     }
/*     */ 
/*     */     
/*     */     private EnumResolver _getToStringResolver(DeserializationContext ctxt) {
/* 409 */       EnumResolver res = this._byToStringResolver;
/* 410 */       if (res == null) {
/* 411 */         synchronized (this) {
/* 412 */           res = EnumResolver.constructUnsafeUsingToString(this._byNameResolver.getEnumClass(), ctxt
/* 413 */               .getAnnotationIntrospector());
/* 414 */           this._byToStringResolver = res;
/*     */         } 
/*     */       }
/* 417 */       return res;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static final class StringCtorKeyDeserializer
/*     */     extends StdKeyDeserializer
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */     protected final Constructor<?> _ctor;
/*     */ 
/*     */     
/*     */     public StringCtorKeyDeserializer(Constructor<?> ctor) {
/* 432 */       super(-1, ctor.getDeclaringClass());
/* 433 */       this._ctor = ctor;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Object _parse(String key, DeserializationContext ctxt) throws Exception {
/* 439 */       return this._ctor.newInstance(new Object[] { key });
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static final class StringFactoryKeyDeserializer
/*     */     extends StdKeyDeserializer
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */     final Method _factoryMethod;
/*     */ 
/*     */     
/*     */     public StringFactoryKeyDeserializer(Method fm) {
/* 454 */       super(-1, fm.getDeclaringClass());
/* 455 */       this._factoryMethod = fm;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Object _parse(String key, DeserializationContext ctxt) throws Exception {
/* 461 */       return this._factoryMethod.invoke(null, new Object[] { key });
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\deser\std\StdKeyDeserializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */