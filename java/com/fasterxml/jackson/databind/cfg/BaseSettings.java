/*     */ package com.fasterxml.jackson.databind.cfg;
/*     */ 
/*     */ import com.fasterxml.jackson.core.Base64Variant;
/*     */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*     */ import com.fasterxml.jackson.databind.PropertyNamingStrategy;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotationIntrospectorPair;
/*     */ import com.fasterxml.jackson.databind.introspect.ClassIntrospector;
/*     */ import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
/*     */ import com.fasterxml.jackson.databind.type.TypeFactory;
/*     */ import com.fasterxml.jackson.databind.util.StdDateFormat;
/*     */ import java.io.Serializable;
/*     */ import java.text.DateFormat;
/*     */ import java.util.Locale;
/*     */ import java.util.TimeZone;
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
/*     */ public final class BaseSettings
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  35 */   private static final TimeZone DEFAULT_TIMEZONE = TimeZone.getTimeZone("UTC");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final ClassIntrospector _classIntrospector;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final AnnotationIntrospector _annotationIntrospector;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final PropertyNamingStrategy _propertyNamingStrategy;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final TypeFactory _typeFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final TypeResolverBuilder<?> _typeResolverBuilder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final PolymorphicTypeValidator _typeValidator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final DateFormat _dateFormat;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final HandlerInstantiator _handlerInstantiator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final Locale _locale;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final TimeZone _timeZone;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final Base64Variant _defaultBase64;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BaseSettings(ClassIntrospector ci, AnnotationIntrospector ai, PropertyNamingStrategy pns, TypeFactory tf, TypeResolverBuilder<?> typer, DateFormat dateFormat, HandlerInstantiator hi, Locale locale, TimeZone tz, Base64Variant defaultBase64, PolymorphicTypeValidator ptv) {
/* 151 */     this._classIntrospector = ci;
/* 152 */     this._annotationIntrospector = ai;
/* 153 */     this._propertyNamingStrategy = pns;
/* 154 */     this._typeFactory = tf;
/* 155 */     this._typeResolverBuilder = typer;
/* 156 */     this._dateFormat = dateFormat;
/* 157 */     this._handlerInstantiator = hi;
/* 158 */     this._locale = locale;
/* 159 */     this._timeZone = tz;
/* 160 */     this._defaultBase64 = defaultBase64;
/* 161 */     this._typeValidator = ptv;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public BaseSettings(ClassIntrospector ci, AnnotationIntrospector ai, PropertyNamingStrategy pns, TypeFactory tf, TypeResolverBuilder<?> typer, DateFormat dateFormat, HandlerInstantiator hi, Locale locale, TimeZone tz, Base64Variant defaultBase64) {
/* 170 */     this(ci, ai, pns, tf, typer, dateFormat, hi, locale, tz, defaultBase64, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BaseSettings copy() {
/* 180 */     return new BaseSettings(this._classIntrospector.copy(), this._annotationIntrospector, this._propertyNamingStrategy, this._typeFactory, this._typeResolverBuilder, this._dateFormat, this._handlerInstantiator, this._locale, this._timeZone, this._defaultBase64, this._typeValidator);
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
/*     */   public BaseSettings withClassIntrospector(ClassIntrospector ci) {
/* 201 */     if (this._classIntrospector == ci) {
/* 202 */       return this;
/*     */     }
/* 204 */     return new BaseSettings(ci, this._annotationIntrospector, this._propertyNamingStrategy, this._typeFactory, this._typeResolverBuilder, this._dateFormat, this._handlerInstantiator, this._locale, this._timeZone, this._defaultBase64, this._typeValidator);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BaseSettings withAnnotationIntrospector(AnnotationIntrospector ai) {
/* 210 */     if (this._annotationIntrospector == ai) {
/* 211 */       return this;
/*     */     }
/* 213 */     return new BaseSettings(this._classIntrospector, ai, this._propertyNamingStrategy, this._typeFactory, this._typeResolverBuilder, this._dateFormat, this._handlerInstantiator, this._locale, this._timeZone, this._defaultBase64, this._typeValidator);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BaseSettings withInsertedAnnotationIntrospector(AnnotationIntrospector ai) {
/* 219 */     return withAnnotationIntrospector(AnnotationIntrospectorPair.create(ai, this._annotationIntrospector));
/*     */   }
/*     */   
/*     */   public BaseSettings withAppendedAnnotationIntrospector(AnnotationIntrospector ai) {
/* 223 */     return withAnnotationIntrospector(AnnotationIntrospectorPair.create(this._annotationIntrospector, ai));
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
/*     */   public BaseSettings withPropertyNamingStrategy(PropertyNamingStrategy pns) {
/* 237 */     if (this._propertyNamingStrategy == pns) {
/* 238 */       return this;
/*     */     }
/* 240 */     return new BaseSettings(this._classIntrospector, this._annotationIntrospector, pns, this._typeFactory, this._typeResolverBuilder, this._dateFormat, this._handlerInstantiator, this._locale, this._timeZone, this._defaultBase64, this._typeValidator);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BaseSettings withTypeFactory(TypeFactory tf) {
/* 246 */     if (this._typeFactory == tf) {
/* 247 */       return this;
/*     */     }
/* 249 */     return new BaseSettings(this._classIntrospector, this._annotationIntrospector, this._propertyNamingStrategy, tf, this._typeResolverBuilder, this._dateFormat, this._handlerInstantiator, this._locale, this._timeZone, this._defaultBase64, this._typeValidator);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BaseSettings withTypeResolverBuilder(TypeResolverBuilder<?> typer) {
/* 255 */     if (this._typeResolverBuilder == typer) {
/* 256 */       return this;
/*     */     }
/* 258 */     return new BaseSettings(this._classIntrospector, this._annotationIntrospector, this._propertyNamingStrategy, this._typeFactory, typer, this._dateFormat, this._handlerInstantiator, this._locale, this._timeZone, this._defaultBase64, this._typeValidator);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BaseSettings withDateFormat(DateFormat df) {
/* 264 */     if (this._dateFormat == df) {
/* 265 */       return this;
/*     */     }
/*     */ 
/*     */     
/* 269 */     if (df != null && hasExplicitTimeZone()) {
/* 270 */       df = _force(df, this._timeZone);
/*     */     }
/* 272 */     return new BaseSettings(this._classIntrospector, this._annotationIntrospector, this._propertyNamingStrategy, this._typeFactory, this._typeResolverBuilder, df, this._handlerInstantiator, this._locale, this._timeZone, this._defaultBase64, this._typeValidator);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BaseSettings withHandlerInstantiator(HandlerInstantiator hi) {
/* 278 */     if (this._handlerInstantiator == hi) {
/* 279 */       return this;
/*     */     }
/* 281 */     return new BaseSettings(this._classIntrospector, this._annotationIntrospector, this._propertyNamingStrategy, this._typeFactory, this._typeResolverBuilder, this._dateFormat, hi, this._locale, this._timeZone, this._defaultBase64, this._typeValidator);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BaseSettings with(Locale l) {
/* 287 */     if (this._locale == l) {
/* 288 */       return this;
/*     */     }
/* 290 */     return new BaseSettings(this._classIntrospector, this._annotationIntrospector, this._propertyNamingStrategy, this._typeFactory, this._typeResolverBuilder, this._dateFormat, this._handlerInstantiator, l, this._timeZone, this._defaultBase64, this._typeValidator);
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
/*     */   public BaseSettings with(TimeZone tz) {
/* 302 */     if (tz == null) {
/* 303 */       throw new IllegalArgumentException();
/*     */     }
/* 305 */     if (tz == this._timeZone) {
/* 306 */       return this;
/*     */     }
/*     */     
/* 309 */     DateFormat df = _force(this._dateFormat, tz);
/* 310 */     return new BaseSettings(this._classIntrospector, this._annotationIntrospector, this._propertyNamingStrategy, this._typeFactory, this._typeResolverBuilder, df, this._handlerInstantiator, this._locale, tz, this._defaultBase64, this._typeValidator);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BaseSettings with(Base64Variant base64) {
/* 320 */     if (base64 == this._defaultBase64) {
/* 321 */       return this;
/*     */     }
/* 323 */     return new BaseSettings(this._classIntrospector, this._annotationIntrospector, this._propertyNamingStrategy, this._typeFactory, this._typeResolverBuilder, this._dateFormat, this._handlerInstantiator, this._locale, this._timeZone, base64, this._typeValidator);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BaseSettings with(PolymorphicTypeValidator v) {
/* 333 */     if (v == this._typeValidator) {
/* 334 */       return this;
/*     */     }
/* 336 */     return new BaseSettings(this._classIntrospector, this._annotationIntrospector, this._propertyNamingStrategy, this._typeFactory, this._typeResolverBuilder, this._dateFormat, this._handlerInstantiator, this._locale, this._timeZone, this._defaultBase64, v);
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
/*     */   public ClassIntrospector getClassIntrospector() {
/* 349 */     return this._classIntrospector;
/*     */   }
/*     */   
/*     */   public AnnotationIntrospector getAnnotationIntrospector() {
/* 353 */     return this._annotationIntrospector;
/*     */   }
/*     */   
/*     */   public PropertyNamingStrategy getPropertyNamingStrategy() {
/* 357 */     return this._propertyNamingStrategy;
/*     */   }
/*     */   
/*     */   public TypeFactory getTypeFactory() {
/* 361 */     return this._typeFactory;
/*     */   }
/*     */   
/*     */   public TypeResolverBuilder<?> getTypeResolverBuilder() {
/* 365 */     return this._typeResolverBuilder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PolymorphicTypeValidator getPolymorphicTypeValidator() {
/* 372 */     return this._typeValidator;
/*     */   }
/*     */   
/*     */   public DateFormat getDateFormat() {
/* 376 */     return this._dateFormat;
/*     */   }
/*     */   
/*     */   public HandlerInstantiator getHandlerInstantiator() {
/* 380 */     return this._handlerInstantiator;
/*     */   }
/*     */   
/*     */   public Locale getLocale() {
/* 384 */     return this._locale;
/*     */   }
/*     */   
/*     */   public TimeZone getTimeZone() {
/* 388 */     TimeZone tz = this._timeZone;
/* 389 */     return (tz == null) ? DEFAULT_TIMEZONE : tz;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasExplicitTimeZone() {
/* 400 */     return (this._timeZone != null);
/*     */   }
/*     */   
/*     */   public Base64Variant getBase64Variant() {
/* 404 */     return this._defaultBase64;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private DateFormat _force(DateFormat df, TimeZone tz) {
/* 415 */     if (df instanceof StdDateFormat) {
/* 416 */       return (DateFormat)((StdDateFormat)df).withTimeZone(tz);
/*     */     }
/*     */     
/* 419 */     df = (DateFormat)df.clone();
/* 420 */     df.setTimeZone(tz);
/* 421 */     return df;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\cfg\BaseSettings.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */