/*     */ package com.fasterxml.jackson.databind.cfg;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonAutoDetect;
/*     */ import com.fasterxml.jackson.annotation.JsonFormat;
/*     */ import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/*     */ import com.fasterxml.jackson.annotation.JsonInclude;
/*     */ import com.fasterxml.jackson.annotation.JsonSetter;
/*     */ import com.fasterxml.jackson.core.Base64Variant;
/*     */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.MapperFeature;
/*     */ import com.fasterxml.jackson.databind.PropertyName;
/*     */ import com.fasterxml.jackson.databind.PropertyNamingStrategy;
/*     */ import com.fasterxml.jackson.databind.introspect.Annotated;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
/*     */ import com.fasterxml.jackson.databind.introspect.ClassIntrospector;
/*     */ import com.fasterxml.jackson.databind.introspect.SimpleMixInResolver;
/*     */ import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
/*     */ import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
/*     */ import com.fasterxml.jackson.databind.type.TypeFactory;
/*     */ import com.fasterxml.jackson.databind.util.RootNameLookup;
/*     */ import java.io.Serializable;
/*     */ import java.text.DateFormat;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.TimeZone;
/*     */ 
/*     */ public abstract class MapperConfigBase<CFG extends ConfigFeature, T extends MapperConfigBase<CFG, T>> extends MapperConfig<T> implements Serializable {
/*  30 */   protected static final ConfigOverride EMPTY_OVERRIDE = ConfigOverride.empty();
/*     */   
/*  32 */   private static final int DEFAULT_MAPPER_FEATURES = collectFeatureDefaults(MapperFeature.class);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  37 */   private static final int AUTO_DETECT_MASK = MapperFeature.AUTO_DETECT_FIELDS
/*  38 */     .getMask() | MapperFeature.AUTO_DETECT_GETTERS
/*  39 */     .getMask() | MapperFeature.AUTO_DETECT_IS_GETTERS
/*  40 */     .getMask() | MapperFeature.AUTO_DETECT_SETTERS
/*  41 */     .getMask() | MapperFeature.AUTO_DETECT_CREATORS
/*  42 */     .getMask();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final SimpleMixInResolver _mixIns;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final SubtypeResolver _subtypeResolver;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final PropertyName _rootName;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final Class<?> _view;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final ContextAttributes _attributes;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final RootNameLookup _rootNames;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final ConfigOverrides _configOverrides;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected MapperConfigBase(BaseSettings base, SubtypeResolver str, SimpleMixInResolver mixins, RootNameLookup rootNames, ConfigOverrides configOverrides) {
/* 125 */     super(base, DEFAULT_MAPPER_FEATURES);
/* 126 */     this._mixIns = mixins;
/* 127 */     this._subtypeResolver = str;
/* 128 */     this._rootNames = rootNames;
/* 129 */     this._rootName = null;
/* 130 */     this._view = null;
/*     */     
/* 132 */     this._attributes = ContextAttributes.getEmpty();
/* 133 */     this._configOverrides = configOverrides;
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
/*     */   protected MapperConfigBase(MapperConfigBase<CFG, T> src, SimpleMixInResolver mixins, RootNameLookup rootNames, ConfigOverrides configOverrides) {
/* 148 */     super(src, src._base.copy());
/* 149 */     this._mixIns = mixins;
/* 150 */     this._subtypeResolver = src._subtypeResolver;
/* 151 */     this._rootNames = rootNames;
/* 152 */     this._rootName = src._rootName;
/* 153 */     this._view = src._view;
/* 154 */     this._attributes = src._attributes;
/* 155 */     this._configOverrides = configOverrides;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected MapperConfigBase(MapperConfigBase<CFG, T> src) {
/* 164 */     super(src);
/* 165 */     this._mixIns = src._mixIns;
/* 166 */     this._subtypeResolver = src._subtypeResolver;
/* 167 */     this._rootNames = src._rootNames;
/* 168 */     this._rootName = src._rootName;
/* 169 */     this._view = src._view;
/* 170 */     this._attributes = src._attributes;
/* 171 */     this._configOverrides = src._configOverrides;
/*     */   }
/*     */ 
/*     */   
/*     */   protected MapperConfigBase(MapperConfigBase<CFG, T> src, BaseSettings base) {
/* 176 */     super(src, base);
/* 177 */     this._mixIns = src._mixIns;
/* 178 */     this._subtypeResolver = src._subtypeResolver;
/* 179 */     this._rootNames = src._rootNames;
/* 180 */     this._rootName = src._rootName;
/* 181 */     this._view = src._view;
/* 182 */     this._attributes = src._attributes;
/* 183 */     this._configOverrides = src._configOverrides;
/*     */   }
/*     */ 
/*     */   
/*     */   protected MapperConfigBase(MapperConfigBase<CFG, T> src, int mapperFeatures) {
/* 188 */     super(src, mapperFeatures);
/* 189 */     this._mixIns = src._mixIns;
/* 190 */     this._subtypeResolver = src._subtypeResolver;
/* 191 */     this._rootNames = src._rootNames;
/* 192 */     this._rootName = src._rootName;
/* 193 */     this._view = src._view;
/* 194 */     this._attributes = src._attributes;
/* 195 */     this._configOverrides = src._configOverrides;
/*     */   }
/*     */   
/*     */   protected MapperConfigBase(MapperConfigBase<CFG, T> src, SubtypeResolver str) {
/* 199 */     super(src);
/* 200 */     this._mixIns = src._mixIns;
/* 201 */     this._subtypeResolver = str;
/* 202 */     this._rootNames = src._rootNames;
/* 203 */     this._rootName = src._rootName;
/* 204 */     this._view = src._view;
/* 205 */     this._attributes = src._attributes;
/* 206 */     this._configOverrides = src._configOverrides;
/*     */   }
/*     */   
/*     */   protected MapperConfigBase(MapperConfigBase<CFG, T> src, PropertyName rootName) {
/* 210 */     super(src);
/* 211 */     this._mixIns = src._mixIns;
/* 212 */     this._subtypeResolver = src._subtypeResolver;
/* 213 */     this._rootNames = src._rootNames;
/* 214 */     this._rootName = rootName;
/* 215 */     this._view = src._view;
/* 216 */     this._attributes = src._attributes;
/* 217 */     this._configOverrides = src._configOverrides;
/*     */   }
/*     */ 
/*     */   
/*     */   protected MapperConfigBase(MapperConfigBase<CFG, T> src, Class<?> view) {
/* 222 */     super(src);
/* 223 */     this._mixIns = src._mixIns;
/* 224 */     this._subtypeResolver = src._subtypeResolver;
/* 225 */     this._rootNames = src._rootNames;
/* 226 */     this._rootName = src._rootName;
/* 227 */     this._view = view;
/* 228 */     this._attributes = src._attributes;
/* 229 */     this._configOverrides = src._configOverrides;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected MapperConfigBase(MapperConfigBase<CFG, T> src, SimpleMixInResolver mixins) {
/* 237 */     super(src);
/* 238 */     this._mixIns = mixins;
/* 239 */     this._subtypeResolver = src._subtypeResolver;
/* 240 */     this._rootNames = src._rootNames;
/* 241 */     this._rootName = src._rootName;
/* 242 */     this._view = src._view;
/* 243 */     this._attributes = src._attributes;
/* 244 */     this._configOverrides = src._configOverrides;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected MapperConfigBase(MapperConfigBase<CFG, T> src, ContextAttributes attr) {
/* 252 */     super(src);
/* 253 */     this._mixIns = src._mixIns;
/* 254 */     this._subtypeResolver = src._subtypeResolver;
/* 255 */     this._rootNames = src._rootNames;
/* 256 */     this._rootName = src._rootName;
/* 257 */     this._view = src._view;
/* 258 */     this._attributes = attr;
/* 259 */     this._configOverrides = src._configOverrides;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final T with(MapperFeature... features) {
/* 292 */     int newMapperFlags = this._mapperFeatures;
/* 293 */     for (MapperFeature f : features) {
/* 294 */       newMapperFlags |= f.getMask();
/*     */     }
/* 296 */     if (newMapperFlags == this._mapperFeatures) {
/* 297 */       return (T)this;
/*     */     }
/* 299 */     return _withMapperFeatures(newMapperFlags);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final T without(MapperFeature... features) {
/* 310 */     int newMapperFlags = this._mapperFeatures;
/* 311 */     for (MapperFeature f : features) {
/* 312 */       newMapperFlags &= f.getMask() ^ 0xFFFFFFFF;
/*     */     }
/* 314 */     if (newMapperFlags == this._mapperFeatures) {
/* 315 */       return (T)this;
/*     */     }
/* 317 */     return _withMapperFeatures(newMapperFlags);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final T with(MapperFeature feature, boolean state) {
/*     */     int newMapperFlags;
/* 325 */     if (state) {
/* 326 */       newMapperFlags = this._mapperFeatures | feature.getMask();
/*     */     } else {
/* 328 */       newMapperFlags = this._mapperFeatures & (feature.getMask() ^ 0xFFFFFFFF);
/*     */     } 
/* 330 */     if (newMapperFlags == this._mapperFeatures) {
/* 331 */       return (T)this;
/*     */     }
/* 333 */     return _withMapperFeatures(newMapperFlags);
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
/*     */   public final T with(AnnotationIntrospector ai) {
/* 350 */     return _withBase(this._base.withAnnotationIntrospector(ai));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final T withAppendedAnnotationIntrospector(AnnotationIntrospector ai) {
/* 358 */     return _withBase(this._base.withAppendedAnnotationIntrospector(ai));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final T withInsertedAnnotationIntrospector(AnnotationIntrospector ai) {
/* 366 */     return _withBase(this._base.withInsertedAnnotationIntrospector(ai));
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
/*     */   public final T with(ClassIntrospector ci) {
/* 378 */     return _withBase(this._base.withClassIntrospector(ci));
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
/*     */   public T withAttributes(Map<?, ?> attributes) {
/* 402 */     return with(getAttributes().withSharedAttributes(attributes));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public T withAttribute(Object key, Object value) {
/* 412 */     return with(getAttributes().withSharedAttribute(key, value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public T withoutAttribute(Object key) {
/* 422 */     return with(getAttributes().withoutSharedAttribute(key));
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
/*     */   public final T with(TypeFactory tf) {
/* 437 */     return _withBase(this._base.withTypeFactory(tf));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final T with(TypeResolverBuilder<?> trb) {
/* 445 */     return _withBase(this._base.withTypeResolverBuilder(trb));
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
/*     */   public final T with(PropertyNamingStrategy pns) {
/* 457 */     return _withBase(this._base.withPropertyNamingStrategy(pns));
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
/*     */   public final T with(HandlerInstantiator hi) {
/* 469 */     return _withBase(this._base.withHandlerInstantiator(hi));
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
/*     */   public final T with(Base64Variant base64) {
/* 483 */     return _withBase(this._base.with(base64));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public T with(DateFormat df) {
/* 494 */     return _withBase(this._base.withDateFormat(df));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final T with(Locale l) {
/* 502 */     return _withBase(this._base.with(l));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final T with(TimeZone tz) {
/* 510 */     return _withBase(this._base.with(tz));
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
/*     */   public T withRootName(String rootName) {
/* 532 */     if (rootName == null) {
/* 533 */       return withRootName((PropertyName)null);
/*     */     }
/* 535 */     return withRootName(PropertyName.construct(rootName));
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final SubtypeResolver getSubtypeResolver() {
/* 567 */     return this._subtypeResolver;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public final String getRootName() {
/* 575 */     return (this._rootName == null) ? null : this._rootName.getSimpleName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final PropertyName getFullRootName() {
/* 582 */     return this._rootName;
/*     */   }
/*     */ 
/*     */   
/*     */   public final Class<?> getActiveView() {
/* 587 */     return this._view;
/*     */   }
/*     */ 
/*     */   
/*     */   public final ContextAttributes getAttributes() {
/* 592 */     return this._attributes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ConfigOverride getConfigOverride(Class<?> type) {
/* 603 */     ConfigOverride override = this._configOverrides.findOverride(type);
/* 604 */     return (override == null) ? EMPTY_OVERRIDE : override;
/*     */   }
/*     */ 
/*     */   
/*     */   public final ConfigOverride findConfigOverride(Class<?> type) {
/* 609 */     return this._configOverrides.findOverride(type);
/*     */   }
/*     */ 
/*     */   
/*     */   public final JsonInclude.Value getDefaultPropertyInclusion() {
/* 614 */     return this._configOverrides.getDefaultInclusion();
/*     */   }
/*     */ 
/*     */   
/*     */   public final JsonInclude.Value getDefaultPropertyInclusion(Class<?> baseType) {
/* 619 */     JsonInclude.Value v = getConfigOverride(baseType).getInclude();
/* 620 */     JsonInclude.Value def = getDefaultPropertyInclusion();
/* 621 */     if (def == null) {
/* 622 */       return v;
/*     */     }
/* 624 */     return def.withOverrides(v);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final JsonInclude.Value getDefaultInclusion(Class<?> baseType, Class<?> propertyType) {
/* 630 */     JsonInclude.Value v = getConfigOverride(propertyType).getIncludeAsProperty();
/* 631 */     JsonInclude.Value def = getDefaultPropertyInclusion(baseType);
/* 632 */     if (def == null) {
/* 633 */       return v;
/*     */     }
/* 635 */     return def.withOverrides(v);
/*     */   }
/*     */ 
/*     */   
/*     */   public final JsonFormat.Value getDefaultPropertyFormat(Class<?> type) {
/* 640 */     return this._configOverrides.findFormatDefaults(type);
/*     */   }
/*     */ 
/*     */   
/*     */   public final JsonIgnoreProperties.Value getDefaultPropertyIgnorals(Class<?> type) {
/* 645 */     ConfigOverride overrides = this._configOverrides.findOverride(type);
/* 646 */     if (overrides != null) {
/* 647 */       JsonIgnoreProperties.Value v = overrides.getIgnorals();
/* 648 */       if (v != null) {
/* 649 */         return v;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 654 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final JsonIgnoreProperties.Value getDefaultPropertyIgnorals(Class<?> baseType, AnnotatedClass actualClass) {
/* 661 */     AnnotationIntrospector intr = getAnnotationIntrospector();
/*     */     
/* 663 */     JsonIgnoreProperties.Value base = (intr == null) ? null : intr.findPropertyIgnorals((Annotated)actualClass);
/* 664 */     JsonIgnoreProperties.Value overrides = getDefaultPropertyIgnorals(baseType);
/* 665 */     return JsonIgnoreProperties.Value.merge(base, overrides);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final VisibilityChecker<?> getDefaultVisibilityChecker() {
/* 671 */     VisibilityChecker<?> vchecker = this._configOverrides.getDefaultVisibility();
/*     */ 
/*     */     
/* 674 */     if ((this._mapperFeatures & AUTO_DETECT_MASK) != AUTO_DETECT_MASK) {
/* 675 */       if (!isEnabled(MapperFeature.AUTO_DETECT_FIELDS)) {
/* 676 */         vchecker = vchecker.withFieldVisibility(JsonAutoDetect.Visibility.NONE);
/*     */       }
/* 678 */       if (!isEnabled(MapperFeature.AUTO_DETECT_GETTERS)) {
/* 679 */         vchecker = vchecker.withGetterVisibility(JsonAutoDetect.Visibility.NONE);
/*     */       }
/* 681 */       if (!isEnabled(MapperFeature.AUTO_DETECT_IS_GETTERS)) {
/* 682 */         vchecker = vchecker.withIsGetterVisibility(JsonAutoDetect.Visibility.NONE);
/*     */       }
/* 684 */       if (!isEnabled(MapperFeature.AUTO_DETECT_SETTERS)) {
/* 685 */         vchecker = vchecker.withSetterVisibility(JsonAutoDetect.Visibility.NONE);
/*     */       }
/* 687 */       if (!isEnabled(MapperFeature.AUTO_DETECT_CREATORS)) {
/* 688 */         vchecker = vchecker.withCreatorVisibility(JsonAutoDetect.Visibility.NONE);
/*     */       }
/*     */     } 
/* 691 */     return vchecker;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final VisibilityChecker<?> getDefaultVisibilityChecker(Class<?> baseType, AnnotatedClass actualClass) {
/* 697 */     VisibilityChecker<?> vc = getDefaultVisibilityChecker();
/* 698 */     AnnotationIntrospector intr = getAnnotationIntrospector();
/* 699 */     if (intr != null) {
/* 700 */       vc = intr.findAutoDetectVisibility(actualClass, vc);
/*     */     }
/* 702 */     ConfigOverride overrides = this._configOverrides.findOverride(baseType);
/* 703 */     if (overrides != null) {
/* 704 */       vc = vc.withOverrides(overrides.getVisibility());
/*     */     }
/* 706 */     return vc;
/*     */   }
/*     */ 
/*     */   
/*     */   public final JsonSetter.Value getDefaultSetterInfo() {
/* 711 */     return this._configOverrides.getDefaultSetterInfo();
/*     */   }
/*     */ 
/*     */   
/*     */   public Boolean getDefaultMergeable() {
/* 716 */     return this._configOverrides.getDefaultMergeable();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Boolean getDefaultMergeable(Class<?> baseType) {
/* 722 */     ConfigOverride cfg = this._configOverrides.findOverride(baseType);
/* 723 */     if (cfg != null) {
/* 724 */       Boolean b = cfg.getMergeable();
/* 725 */       if (b != null) {
/* 726 */         return b;
/*     */       }
/*     */     } 
/* 729 */     return this._configOverrides.getDefaultMergeable();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PropertyName findRootName(JavaType rootType) {
/* 740 */     if (this._rootName != null) {
/* 741 */       return this._rootName;
/*     */     }
/* 743 */     return this._rootNames.findRootName(rootType, this);
/*     */   }
/*     */ 
/*     */   
/*     */   public PropertyName findRootName(Class<?> rawRootType) {
/* 748 */     if (this._rootName != null) {
/* 749 */       return this._rootName;
/*     */     }
/* 751 */     return this._rootNames.findRootName(rawRootType, this);
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
/*     */   public final Class<?> findMixInClassFor(Class<?> cls) {
/* 766 */     return this._mixIns.findMixInClassFor(cls);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassIntrospector.MixInResolver copy() {
/* 772 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int mixInCount() {
/* 780 */     return this._mixIns.localSize();
/*     */   }
/*     */   
/*     */   protected abstract T _withBase(BaseSettings paramBaseSettings);
/*     */   
/*     */   protected abstract T _withMapperFeatures(int paramInt);
/*     */   
/*     */   public abstract T with(ContextAttributes paramContextAttributes);
/*     */   
/*     */   public abstract T withRootName(PropertyName paramPropertyName);
/*     */   
/*     */   public abstract T with(SubtypeResolver paramSubtypeResolver);
/*     */   
/*     */   public abstract T withView(Class<?> paramClass);
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\cfg\MapperConfigBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */