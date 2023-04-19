/*     */ package com.fasterxml.jackson.databind.cfg;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonFormat;
/*     */ import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/*     */ import com.fasterxml.jackson.annotation.JsonInclude;
/*     */ import com.fasterxml.jackson.annotation.JsonSetter;
/*     */ import com.fasterxml.jackson.core.Base64Variant;
/*     */ import com.fasterxml.jackson.core.SerializableString;
/*     */ import com.fasterxml.jackson.core.io.SerializedString;
/*     */ import com.fasterxml.jackson.core.type.TypeReference;
/*     */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*     */ import com.fasterxml.jackson.databind.BeanDescription;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.MapperFeature;
/*     */ import com.fasterxml.jackson.databind.PropertyName;
/*     */ import com.fasterxml.jackson.databind.PropertyNamingStrategy;
/*     */ import com.fasterxml.jackson.databind.introspect.Annotated;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
/*     */ import com.fasterxml.jackson.databind.introspect.ClassIntrospector;
/*     */ import com.fasterxml.jackson.databind.introspect.NopAnnotationIntrospector;
/*     */ import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
/*     */ import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
/*     */ import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
/*     */ import com.fasterxml.jackson.databind.type.TypeFactory;
/*     */ import com.fasterxml.jackson.databind.util.ClassUtil;
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
/*     */ public abstract class MapperConfig<T extends MapperConfig<T>>
/*     */   implements ClassIntrospector.MixInResolver, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 2L;
/*  46 */   protected static final JsonInclude.Value EMPTY_INCLUDE = JsonInclude.Value.empty();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  51 */   protected static final JsonFormat.Value EMPTY_FORMAT = JsonFormat.Value.empty();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final int _mapperFeatures;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final BaseSettings _base;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected MapperConfig(BaseSettings base, int mapperFeatures) {
/*  71 */     this._base = base;
/*  72 */     this._mapperFeatures = mapperFeatures;
/*     */   }
/*     */ 
/*     */   
/*     */   protected MapperConfig(MapperConfig<T> src, int mapperFeatures) {
/*  77 */     this._base = src._base;
/*  78 */     this._mapperFeatures = mapperFeatures;
/*     */   }
/*     */ 
/*     */   
/*     */   protected MapperConfig(MapperConfig<T> src, BaseSettings base) {
/*  83 */     this._base = base;
/*  84 */     this._mapperFeatures = src._mapperFeatures;
/*     */   }
/*     */ 
/*     */   
/*     */   protected MapperConfig(MapperConfig<T> src) {
/*  89 */     this._base = src._base;
/*  90 */     this._mapperFeatures = src._mapperFeatures;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <F extends Enum<F> & ConfigFeature> int collectFeatureDefaults(Class<F> enumClass) {
/*  99 */     int flags = 0;
/* 100 */     for (Enum enum_ : (Enum[])enumClass.getEnumConstants()) {
/* 101 */       if (((ConfigFeature)enum_).enabledByDefault()) {
/* 102 */         flags |= ((ConfigFeature)enum_).getMask();
/*     */       }
/*     */     } 
/* 105 */     return flags;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract T with(MapperFeature... paramVarArgs);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract T without(MapperFeature... paramVarArgs);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract T with(MapperFeature paramMapperFeature, boolean paramBoolean);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isEnabled(MapperFeature f) {
/* 142 */     return ((this._mapperFeatures & f.getMask()) != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean hasMapperFeatures(int featureMask) {
/* 152 */     return ((this._mapperFeatures & featureMask) == featureMask);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isAnnotationProcessingEnabled() {
/* 162 */     return isEnabled(MapperFeature.USE_ANNOTATIONS);
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
/*     */   public final boolean canOverrideAccessModifiers() {
/* 177 */     return isEnabled(MapperFeature.CAN_OVERRIDE_ACCESS_MODIFIERS);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean shouldSortPropertiesAlphabetically() {
/* 185 */     return isEnabled(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY);
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
/*     */   public abstract boolean useRootWrapping();
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
/*     */   public SerializableString compileString(String src) {
/* 217 */     return (SerializableString)new SerializedString(src);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassIntrospector getClassIntrospector() {
/* 227 */     return this._base.getClassIntrospector();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationIntrospector getAnnotationIntrospector() {
/* 237 */     if (isEnabled(MapperFeature.USE_ANNOTATIONS)) {
/* 238 */       return this._base.getAnnotationIntrospector();
/*     */     }
/* 240 */     return (AnnotationIntrospector)NopAnnotationIntrospector.instance;
/*     */   }
/*     */   
/*     */   public final PropertyNamingStrategy getPropertyNamingStrategy() {
/* 244 */     return this._base.getPropertyNamingStrategy();
/*     */   }
/*     */   
/*     */   public final HandlerInstantiator getHandlerInstantiator() {
/* 248 */     return this._base.getHandlerInstantiator();
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
/*     */   public final TypeResolverBuilder<?> getDefaultTyper(JavaType baseType) {
/* 264 */     return this._base.getTypeResolverBuilder();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract SubtypeResolver getSubtypeResolver();
/*     */ 
/*     */   
/*     */   public PolymorphicTypeValidator getPolymorphicTypeValidator() {
/* 273 */     return this._base.getPolymorphicTypeValidator();
/*     */   }
/*     */   
/*     */   public final TypeFactory getTypeFactory() {
/* 277 */     return this._base.getTypeFactory();
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
/*     */   public final JavaType constructType(Class<?> cls) {
/* 289 */     return getTypeFactory().constructType(cls);
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
/*     */   public final JavaType constructType(TypeReference<?> valueTypeRef) {
/* 301 */     return getTypeFactory().constructType(valueTypeRef.getType());
/*     */   }
/*     */   
/*     */   public JavaType constructSpecializedType(JavaType baseType, Class<?> subclass) {
/* 305 */     return getTypeFactory().constructSpecializedType(baseType, subclass);
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
/*     */   public BeanDescription introspectClassAnnotations(Class<?> cls) {
/* 319 */     return introspectClassAnnotations(constructType(cls));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanDescription introspectClassAnnotations(JavaType type) {
/* 327 */     return getClassIntrospector().forClassAnnotations(this, type, this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanDescription introspectDirectClassAnnotations(Class<?> cls) {
/* 336 */     return introspectDirectClassAnnotations(constructType(cls));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final BeanDescription introspectDirectClassAnnotations(JavaType type) {
/* 345 */     return getClassIntrospector().forDirectClassAnnotations(this, type, this);
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
/*     */   public abstract ConfigOverride findConfigOverride(Class<?> paramClass);
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
/*     */   public abstract ConfigOverride getConfigOverride(Class<?> paramClass);
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
/*     */   public abstract JsonInclude.Value getDefaultPropertyInclusion();
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
/*     */   public abstract JsonInclude.Value getDefaultPropertyInclusion(Class<?> paramClass);
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
/*     */   public JsonInclude.Value getDefaultPropertyInclusion(Class<?> baseType, JsonInclude.Value defaultIncl) {
/* 411 */     JsonInclude.Value v = getConfigOverride(baseType).getInclude();
/* 412 */     if (v != null) {
/* 413 */       return v;
/*     */     }
/* 415 */     return defaultIncl;
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
/*     */   public abstract JsonInclude.Value getDefaultInclusion(Class<?> paramClass1, Class<?> paramClass2);
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
/*     */   public JsonInclude.Value getDefaultInclusion(Class<?> baseType, Class<?> propertyType, JsonInclude.Value defaultIncl) {
/* 448 */     JsonInclude.Value baseOverride = getConfigOverride(baseType).getInclude();
/* 449 */     JsonInclude.Value propOverride = getConfigOverride(propertyType).getIncludeAsProperty();
/*     */     
/* 451 */     JsonInclude.Value result = JsonInclude.Value.mergeAll(new JsonInclude.Value[] { defaultIncl, baseOverride, propOverride });
/* 452 */     return result;
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
/*     */   public abstract JsonFormat.Value getDefaultPropertyFormat(Class<?> paramClass);
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
/*     */   public abstract JsonIgnoreProperties.Value getDefaultPropertyIgnorals(Class<?> paramClass);
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
/*     */   public abstract JsonIgnoreProperties.Value getDefaultPropertyIgnorals(Class<?> paramClass, AnnotatedClass paramAnnotatedClass);
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
/*     */   public abstract VisibilityChecker<?> getDefaultVisibilityChecker();
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
/*     */   public abstract VisibilityChecker<?> getDefaultVisibilityChecker(Class<?> paramClass, AnnotatedClass paramAnnotatedClass);
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
/*     */   public abstract JsonSetter.Value getDefaultSetterInfo();
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
/*     */   public abstract Boolean getDefaultMergeable();
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
/*     */   public abstract Boolean getDefaultMergeable(Class<?> paramClass);
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
/*     */   public final DateFormat getDateFormat() {
/* 561 */     return this._base.getDateFormat();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Locale getLocale() {
/* 568 */     return this._base.getLocale();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final TimeZone getTimeZone() {
/* 575 */     return this._base.getTimeZone();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract Class<?> getActiveView();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Base64Variant getBase64Variant() {
/* 590 */     return this._base.getBase64Variant();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract ContextAttributes getAttributes();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract PropertyName findRootName(JavaType paramJavaType);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract PropertyName findRootName(Class<?> paramClass);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypeResolverBuilder<?> typeResolverBuilderInstance(Annotated annotated, Class<? extends TypeResolverBuilder<?>> builderClass) {
/* 625 */     HandlerInstantiator hi = getHandlerInstantiator();
/* 626 */     if (hi != null) {
/* 627 */       TypeResolverBuilder<?> builder = hi.typeResolverBuilderInstance(this, annotated, builderClass);
/* 628 */       if (builder != null) {
/* 629 */         return builder;
/*     */       }
/*     */     } 
/* 632 */     return (TypeResolverBuilder)ClassUtil.createInstance(builderClass, canOverrideAccessModifiers());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypeIdResolver typeIdResolverInstance(Annotated annotated, Class<? extends TypeIdResolver> resolverClass) {
/* 642 */     HandlerInstantiator hi = getHandlerInstantiator();
/* 643 */     if (hi != null) {
/* 644 */       TypeIdResolver builder = hi.typeIdResolverInstance(this, annotated, resolverClass);
/* 645 */       if (builder != null) {
/* 646 */         return builder;
/*     */       }
/*     */     } 
/* 649 */     return (TypeIdResolver)ClassUtil.createInstance(resolverClass, canOverrideAccessModifiers());
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\cfg\MapperConfig.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */