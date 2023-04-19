/*     */ package com.fasterxml.jackson.databind;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonInclude;
/*     */ import com.fasterxml.jackson.core.FormatFeature;
/*     */ import com.fasterxml.jackson.core.JsonFactory;
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.core.PrettyPrinter;
/*     */ import com.fasterxml.jackson.core.json.JsonWriteFeature;
/*     */ import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
/*     */ import com.fasterxml.jackson.core.util.Instantiatable;
/*     */ import com.fasterxml.jackson.databind.cfg.BaseSettings;
/*     */ import com.fasterxml.jackson.databind.cfg.ConfigOverrides;
/*     */ import com.fasterxml.jackson.databind.cfg.ContextAttributes;
/*     */ import com.fasterxml.jackson.databind.cfg.MapperConfigBase;
/*     */ import com.fasterxml.jackson.databind.introspect.ClassIntrospector;
/*     */ import com.fasterxml.jackson.databind.introspect.SimpleMixInResolver;
/*     */ import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
/*     */ import com.fasterxml.jackson.databind.ser.FilterProvider;
/*     */ import com.fasterxml.jackson.databind.util.RootNameLookup;
/*     */ import java.io.Serializable;
/*     */ import java.text.DateFormat;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class SerializationConfig
/*     */   extends MapperConfigBase<SerializationFeature, SerializationConfig>
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  38 */   protected static final PrettyPrinter DEFAULT_PRETTY_PRINTER = (PrettyPrinter)new DefaultPrettyPrinter();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final FilterProvider _filterProvider;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final PrettyPrinter _defaultPrettyPrinter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final int _serFeatures;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final int _generatorFeatures;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final int _generatorFeaturesToChange;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final int _formatWriteFeatures;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final int _formatWriteFeaturesToChange;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SerializationConfig(BaseSettings base, SubtypeResolver str, SimpleMixInResolver mixins, RootNameLookup rootNames, ConfigOverrides configOverrides) {
/* 115 */     super(base, str, mixins, rootNames, configOverrides);
/* 116 */     this._serFeatures = collectFeatureDefaults(SerializationFeature.class);
/* 117 */     this._filterProvider = null;
/* 118 */     this._defaultPrettyPrinter = DEFAULT_PRETTY_PRINTER;
/* 119 */     this._generatorFeatures = 0;
/* 120 */     this._generatorFeaturesToChange = 0;
/* 121 */     this._formatWriteFeatures = 0;
/* 122 */     this._formatWriteFeaturesToChange = 0;
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
/*     */   protected SerializationConfig(SerializationConfig src, SimpleMixInResolver mixins, RootNameLookup rootNames, ConfigOverrides configOverrides) {
/* 134 */     super(src, mixins, rootNames, configOverrides);
/* 135 */     this._serFeatures = src._serFeatures;
/* 136 */     this._filterProvider = src._filterProvider;
/* 137 */     this._defaultPrettyPrinter = src._defaultPrettyPrinter;
/* 138 */     this._generatorFeatures = src._generatorFeatures;
/* 139 */     this._generatorFeaturesToChange = src._generatorFeaturesToChange;
/* 140 */     this._formatWriteFeatures = src._formatWriteFeatures;
/* 141 */     this._formatWriteFeaturesToChange = src._formatWriteFeaturesToChange;
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
/*     */   private SerializationConfig(SerializationConfig src, SubtypeResolver str) {
/* 153 */     super(src, str);
/* 154 */     this._serFeatures = src._serFeatures;
/* 155 */     this._filterProvider = src._filterProvider;
/* 156 */     this._defaultPrettyPrinter = src._defaultPrettyPrinter;
/* 157 */     this._generatorFeatures = src._generatorFeatures;
/* 158 */     this._generatorFeaturesToChange = src._generatorFeaturesToChange;
/* 159 */     this._formatWriteFeatures = src._formatWriteFeatures;
/* 160 */     this._formatWriteFeaturesToChange = src._formatWriteFeaturesToChange;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private SerializationConfig(SerializationConfig src, int mapperFeatures, int serFeatures, int generatorFeatures, int generatorFeatureMask, int formatFeatures, int formatFeaturesMask) {
/* 168 */     super(src, mapperFeatures);
/* 169 */     this._serFeatures = serFeatures;
/* 170 */     this._filterProvider = src._filterProvider;
/* 171 */     this._defaultPrettyPrinter = src._defaultPrettyPrinter;
/* 172 */     this._generatorFeatures = generatorFeatures;
/* 173 */     this._generatorFeaturesToChange = generatorFeatureMask;
/* 174 */     this._formatWriteFeatures = formatFeatures;
/* 175 */     this._formatWriteFeaturesToChange = formatFeaturesMask;
/*     */   }
/*     */ 
/*     */   
/*     */   private SerializationConfig(SerializationConfig src, BaseSettings base) {
/* 180 */     super(src, base);
/* 181 */     this._serFeatures = src._serFeatures;
/* 182 */     this._filterProvider = src._filterProvider;
/* 183 */     this._defaultPrettyPrinter = src._defaultPrettyPrinter;
/* 184 */     this._generatorFeatures = src._generatorFeatures;
/* 185 */     this._generatorFeaturesToChange = src._generatorFeaturesToChange;
/* 186 */     this._formatWriteFeatures = src._formatWriteFeatures;
/* 187 */     this._formatWriteFeaturesToChange = src._formatWriteFeaturesToChange;
/*     */   }
/*     */ 
/*     */   
/*     */   private SerializationConfig(SerializationConfig src, FilterProvider filters) {
/* 192 */     super(src);
/* 193 */     this._serFeatures = src._serFeatures;
/* 194 */     this._filterProvider = filters;
/* 195 */     this._defaultPrettyPrinter = src._defaultPrettyPrinter;
/* 196 */     this._generatorFeatures = src._generatorFeatures;
/* 197 */     this._generatorFeaturesToChange = src._generatorFeaturesToChange;
/* 198 */     this._formatWriteFeatures = src._formatWriteFeatures;
/* 199 */     this._formatWriteFeaturesToChange = src._formatWriteFeaturesToChange;
/*     */   }
/*     */ 
/*     */   
/*     */   private SerializationConfig(SerializationConfig src, Class<?> view) {
/* 204 */     super(src, view);
/* 205 */     this._serFeatures = src._serFeatures;
/* 206 */     this._filterProvider = src._filterProvider;
/* 207 */     this._defaultPrettyPrinter = src._defaultPrettyPrinter;
/* 208 */     this._generatorFeatures = src._generatorFeatures;
/* 209 */     this._generatorFeaturesToChange = src._generatorFeaturesToChange;
/* 210 */     this._formatWriteFeatures = src._formatWriteFeatures;
/* 211 */     this._formatWriteFeaturesToChange = src._formatWriteFeaturesToChange;
/*     */   }
/*     */ 
/*     */   
/*     */   private SerializationConfig(SerializationConfig src, PropertyName rootName) {
/* 216 */     super(src, rootName);
/* 217 */     this._serFeatures = src._serFeatures;
/* 218 */     this._filterProvider = src._filterProvider;
/* 219 */     this._defaultPrettyPrinter = src._defaultPrettyPrinter;
/* 220 */     this._generatorFeatures = src._generatorFeatures;
/* 221 */     this._generatorFeaturesToChange = src._generatorFeaturesToChange;
/* 222 */     this._formatWriteFeatures = src._formatWriteFeatures;
/* 223 */     this._formatWriteFeaturesToChange = src._formatWriteFeaturesToChange;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SerializationConfig(SerializationConfig src, ContextAttributes attrs) {
/* 231 */     super(src, attrs);
/* 232 */     this._serFeatures = src._serFeatures;
/* 233 */     this._filterProvider = src._filterProvider;
/* 234 */     this._defaultPrettyPrinter = src._defaultPrettyPrinter;
/* 235 */     this._generatorFeatures = src._generatorFeatures;
/* 236 */     this._generatorFeaturesToChange = src._generatorFeaturesToChange;
/* 237 */     this._formatWriteFeatures = src._formatWriteFeatures;
/* 238 */     this._formatWriteFeaturesToChange = src._formatWriteFeaturesToChange;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SerializationConfig(SerializationConfig src, SimpleMixInResolver mixins) {
/* 246 */     super(src, mixins);
/* 247 */     this._serFeatures = src._serFeatures;
/* 248 */     this._filterProvider = src._filterProvider;
/* 249 */     this._defaultPrettyPrinter = src._defaultPrettyPrinter;
/* 250 */     this._generatorFeatures = src._generatorFeatures;
/* 251 */     this._generatorFeaturesToChange = src._generatorFeaturesToChange;
/* 252 */     this._formatWriteFeatures = src._formatWriteFeatures;
/* 253 */     this._formatWriteFeaturesToChange = src._formatWriteFeaturesToChange;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SerializationConfig(SerializationConfig src, PrettyPrinter defaultPP) {
/* 261 */     super(src);
/* 262 */     this._serFeatures = src._serFeatures;
/* 263 */     this._filterProvider = src._filterProvider;
/* 264 */     this._defaultPrettyPrinter = defaultPP;
/* 265 */     this._generatorFeatures = src._generatorFeatures;
/* 266 */     this._generatorFeaturesToChange = src._generatorFeaturesToChange;
/* 267 */     this._formatWriteFeatures = src._formatWriteFeatures;
/* 268 */     this._formatWriteFeaturesToChange = src._formatWriteFeaturesToChange;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final SerializationConfig _withBase(BaseSettings newBase) {
/* 279 */     return (this._base == newBase) ? this : new SerializationConfig(this, newBase);
/*     */   }
/*     */ 
/*     */   
/*     */   protected final SerializationConfig _withMapperFeatures(int mapperFeatures) {
/* 284 */     return new SerializationConfig(this, mapperFeatures, this._serFeatures, this._generatorFeatures, this._generatorFeaturesToChange, this._formatWriteFeatures, this._formatWriteFeaturesToChange);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SerializationConfig withRootName(PropertyName rootName) {
/* 291 */     if (rootName == null) {
/* 292 */       if (this._rootName == null) {
/* 293 */         return this;
/*     */       }
/* 295 */     } else if (rootName.equals(this._rootName)) {
/* 296 */       return this;
/*     */     } 
/* 298 */     return new SerializationConfig(this, rootName);
/*     */   }
/*     */ 
/*     */   
/*     */   public SerializationConfig with(SubtypeResolver str) {
/* 303 */     return (str == this._subtypeResolver) ? this : new SerializationConfig(this, str);
/*     */   }
/*     */ 
/*     */   
/*     */   public SerializationConfig withView(Class<?> view) {
/* 308 */     return (this._view == view) ? this : new SerializationConfig(this, view);
/*     */   }
/*     */ 
/*     */   
/*     */   public SerializationConfig with(ContextAttributes attrs) {
/* 313 */     return (attrs == this._attributes) ? this : new SerializationConfig(this, attrs);
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
/*     */   public SerializationConfig with(DateFormat df) {
/* 329 */     SerializationConfig cfg = (SerializationConfig)super.with(df);
/*     */     
/* 331 */     if (df == null) {
/* 332 */       return cfg.with(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
/*     */     }
/* 334 */     return cfg.without(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
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
/*     */   public SerializationConfig with(SerializationFeature feature) {
/* 349 */     int newSerFeatures = this._serFeatures | feature.getMask();
/* 350 */     return (newSerFeatures == this._serFeatures) ? this : new SerializationConfig(this, this._mapperFeatures, newSerFeatures, this._generatorFeatures, this._generatorFeaturesToChange, this._formatWriteFeatures, this._formatWriteFeaturesToChange);
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
/*     */   public SerializationConfig with(SerializationFeature first, SerializationFeature... features) {
/* 362 */     int newSerFeatures = this._serFeatures | first.getMask();
/* 363 */     for (SerializationFeature f : features) {
/* 364 */       newSerFeatures |= f.getMask();
/*     */     }
/* 366 */     return (newSerFeatures == this._serFeatures) ? this : new SerializationConfig(this, this._mapperFeatures, newSerFeatures, this._generatorFeatures, this._generatorFeaturesToChange, this._formatWriteFeatures, this._formatWriteFeaturesToChange);
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
/*     */   public SerializationConfig withFeatures(SerializationFeature... features) {
/* 378 */     int newSerFeatures = this._serFeatures;
/* 379 */     for (SerializationFeature f : features) {
/* 380 */       newSerFeatures |= f.getMask();
/*     */     }
/* 382 */     return (newSerFeatures == this._serFeatures) ? this : new SerializationConfig(this, this._mapperFeatures, newSerFeatures, this._generatorFeatures, this._generatorFeaturesToChange, this._formatWriteFeatures, this._formatWriteFeaturesToChange);
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
/*     */   public SerializationConfig without(SerializationFeature feature) {
/* 394 */     int newSerFeatures = this._serFeatures & (feature.getMask() ^ 0xFFFFFFFF);
/* 395 */     return (newSerFeatures == this._serFeatures) ? this : new SerializationConfig(this, this._mapperFeatures, newSerFeatures, this._generatorFeatures, this._generatorFeaturesToChange, this._formatWriteFeatures, this._formatWriteFeaturesToChange);
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
/*     */   public SerializationConfig without(SerializationFeature first, SerializationFeature... features) {
/* 407 */     int newSerFeatures = this._serFeatures & (first.getMask() ^ 0xFFFFFFFF);
/* 408 */     for (SerializationFeature f : features) {
/* 409 */       newSerFeatures &= f.getMask() ^ 0xFFFFFFFF;
/*     */     }
/* 411 */     return (newSerFeatures == this._serFeatures) ? this : new SerializationConfig(this, this._mapperFeatures, newSerFeatures, this._generatorFeatures, this._generatorFeaturesToChange, this._formatWriteFeatures, this._formatWriteFeaturesToChange);
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
/*     */   public SerializationConfig withoutFeatures(SerializationFeature... features) {
/* 423 */     int newSerFeatures = this._serFeatures;
/* 424 */     for (SerializationFeature f : features) {
/* 425 */       newSerFeatures &= f.getMask() ^ 0xFFFFFFFF;
/*     */     }
/* 427 */     return (newSerFeatures == this._serFeatures) ? this : new SerializationConfig(this, this._mapperFeatures, newSerFeatures, this._generatorFeatures, this._generatorFeaturesToChange, this._formatWriteFeatures, this._formatWriteFeaturesToChange);
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
/*     */   public SerializationConfig with(JsonGenerator.Feature feature) {
/* 446 */     int newSet = this._generatorFeatures | feature.getMask();
/* 447 */     int newMask = this._generatorFeaturesToChange | feature.getMask();
/* 448 */     return (this._generatorFeatures == newSet && this._generatorFeaturesToChange == newMask) ? this : new SerializationConfig(this, this._mapperFeatures, this._serFeatures, newSet, newMask, this._formatWriteFeatures, this._formatWriteFeaturesToChange);
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
/*     */   public SerializationConfig withFeatures(JsonGenerator.Feature... features) {
/* 462 */     int newSet = this._generatorFeatures;
/* 463 */     int newMask = this._generatorFeaturesToChange;
/* 464 */     for (JsonGenerator.Feature f : features) {
/* 465 */       int mask = f.getMask();
/* 466 */       newSet |= mask;
/* 467 */       newMask |= mask;
/*     */     } 
/* 469 */     return (this._generatorFeatures == newSet && this._generatorFeaturesToChange == newMask) ? this : new SerializationConfig(this, this._mapperFeatures, this._serFeatures, newSet, newMask, this._formatWriteFeatures, this._formatWriteFeaturesToChange);
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
/*     */   public SerializationConfig without(JsonGenerator.Feature feature) {
/* 483 */     int newSet = this._generatorFeatures & (feature.getMask() ^ 0xFFFFFFFF);
/* 484 */     int newMask = this._generatorFeaturesToChange | feature.getMask();
/* 485 */     return (this._generatorFeatures == newSet && this._generatorFeaturesToChange == newMask) ? this : new SerializationConfig(this, this._mapperFeatures, this._serFeatures, newSet, newMask, this._formatWriteFeatures, this._formatWriteFeaturesToChange);
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
/*     */   public SerializationConfig withoutFeatures(JsonGenerator.Feature... features) {
/* 499 */     int newSet = this._generatorFeatures;
/* 500 */     int newMask = this._generatorFeaturesToChange;
/* 501 */     for (JsonGenerator.Feature f : features) {
/* 502 */       int mask = f.getMask();
/* 503 */       newSet &= mask ^ 0xFFFFFFFF;
/* 504 */       newMask |= mask;
/*     */     } 
/* 506 */     return (this._generatorFeatures == newSet && this._generatorFeaturesToChange == newMask) ? this : new SerializationConfig(this, this._mapperFeatures, this._serFeatures, newSet, newMask, this._formatWriteFeatures, this._formatWriteFeaturesToChange);
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
/*     */   public SerializationConfig with(FormatFeature feature) {
/* 526 */     if (feature instanceof JsonWriteFeature) {
/* 527 */       return _withJsonWriteFeatures(new FormatFeature[] { feature });
/*     */     }
/* 529 */     int newSet = this._formatWriteFeatures | feature.getMask();
/* 530 */     int newMask = this._formatWriteFeaturesToChange | feature.getMask();
/* 531 */     return (this._formatWriteFeatures == newSet && this._formatWriteFeaturesToChange == newMask) ? this : new SerializationConfig(this, this._mapperFeatures, this._serFeatures, this._generatorFeatures, this._generatorFeaturesToChange, newSet, newMask);
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
/*     */   public SerializationConfig withFeatures(FormatFeature... features) {
/* 546 */     if (features.length > 0 && features[0] instanceof JsonWriteFeature) {
/* 547 */       return _withJsonWriteFeatures(features);
/*     */     }
/* 549 */     int newSet = this._formatWriteFeatures;
/* 550 */     int newMask = this._formatWriteFeaturesToChange;
/* 551 */     for (FormatFeature f : features) {
/* 552 */       int mask = f.getMask();
/* 553 */       newSet |= mask;
/* 554 */       newMask |= mask;
/*     */     } 
/* 556 */     return (this._formatWriteFeatures == newSet && this._formatWriteFeaturesToChange == newMask) ? this : new SerializationConfig(this, this._mapperFeatures, this._serFeatures, this._generatorFeatures, this._generatorFeaturesToChange, newSet, newMask);
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
/*     */   public SerializationConfig without(FormatFeature feature) {
/* 571 */     if (feature instanceof JsonWriteFeature) {
/* 572 */       return _withoutJsonWriteFeatures(new FormatFeature[] { feature });
/*     */     }
/* 574 */     int newSet = this._formatWriteFeatures & (feature.getMask() ^ 0xFFFFFFFF);
/* 575 */     int newMask = this._formatWriteFeaturesToChange | feature.getMask();
/* 576 */     return (this._formatWriteFeatures == newSet && this._formatWriteFeaturesToChange == newMask) ? this : new SerializationConfig(this, this._mapperFeatures, this._serFeatures, this._generatorFeatures, this._generatorFeaturesToChange, newSet, newMask);
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
/*     */   public SerializationConfig withoutFeatures(FormatFeature... features) {
/* 590 */     if (features.length > 0 && features[0] instanceof JsonWriteFeature) {
/* 591 */       return _withoutJsonWriteFeatures(features);
/*     */     }
/* 593 */     int newSet = this._formatWriteFeatures;
/* 594 */     int newMask = this._formatWriteFeaturesToChange;
/* 595 */     for (FormatFeature f : features) {
/* 596 */       int mask = f.getMask();
/* 597 */       newSet &= mask ^ 0xFFFFFFFF;
/* 598 */       newMask |= mask;
/*     */     } 
/* 600 */     return (this._formatWriteFeatures == newSet && this._formatWriteFeaturesToChange == newMask) ? this : new SerializationConfig(this, this._mapperFeatures, this._serFeatures, this._generatorFeatures, this._generatorFeaturesToChange, newSet, newMask);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private SerializationConfig _withJsonWriteFeatures(FormatFeature... features) {
/* 608 */     int parserSet = this._generatorFeatures;
/* 609 */     int parserMask = this._generatorFeaturesToChange;
/* 610 */     int newSet = this._formatWriteFeatures;
/* 611 */     int newMask = this._formatWriteFeaturesToChange;
/* 612 */     for (FormatFeature f : features) {
/* 613 */       int mask = f.getMask();
/* 614 */       newSet |= mask;
/* 615 */       newMask |= mask;
/*     */       
/* 617 */       if (f instanceof JsonWriteFeature) {
/* 618 */         JsonGenerator.Feature oldF = ((JsonWriteFeature)f).mappedFeature();
/* 619 */         if (oldF != null) {
/* 620 */           int pmask = oldF.getMask();
/* 621 */           parserSet |= pmask;
/* 622 */           parserMask |= pmask;
/*     */         } 
/*     */       } 
/*     */     } 
/* 626 */     return (this._formatWriteFeatures == newSet && this._formatWriteFeaturesToChange == newMask && this._generatorFeatures == parserSet && this._generatorFeaturesToChange == parserMask) ? this : new SerializationConfig(this, this._mapperFeatures, this._serFeatures, parserSet, parserMask, newSet, newMask);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private SerializationConfig _withoutJsonWriteFeatures(FormatFeature... features) {
/* 635 */     int parserSet = this._generatorFeatures;
/* 636 */     int parserMask = this._generatorFeaturesToChange;
/* 637 */     int newSet = this._formatWriteFeatures;
/* 638 */     int newMask = this._formatWriteFeaturesToChange;
/* 639 */     for (FormatFeature f : features) {
/* 640 */       int mask = f.getMask();
/* 641 */       newSet &= mask ^ 0xFFFFFFFF;
/* 642 */       newMask |= mask;
/*     */       
/* 644 */       if (f instanceof JsonWriteFeature) {
/* 645 */         JsonGenerator.Feature oldF = ((JsonWriteFeature)f).mappedFeature();
/* 646 */         if (oldF != null) {
/* 647 */           int pmask = oldF.getMask();
/* 648 */           parserSet &= pmask ^ 0xFFFFFFFF;
/* 649 */           parserMask |= pmask;
/*     */         } 
/*     */       } 
/*     */     } 
/* 653 */     return (this._formatWriteFeatures == newSet && this._formatWriteFeaturesToChange == newMask && this._generatorFeatures == parserSet && this._generatorFeaturesToChange == parserMask) ? this : new SerializationConfig(this, this._mapperFeatures, this._serFeatures, parserSet, parserMask, newSet, newMask);
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
/*     */   public SerializationConfig withFilters(FilterProvider filterProvider) {
/* 667 */     return (filterProvider == this._filterProvider) ? this : new SerializationConfig(this, filterProvider);
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
/*     */   @Deprecated
/*     */   public SerializationConfig withPropertyInclusion(JsonInclude.Value incl) {
/* 680 */     this._configOverrides.setDefaultInclusion(incl);
/* 681 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SerializationConfig withDefaultPrettyPrinter(PrettyPrinter pp) {
/* 688 */     return (this._defaultPrettyPrinter == pp) ? this : new SerializationConfig(this, pp);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PrettyPrinter constructDefaultPrettyPrinter() {
/* 698 */     PrettyPrinter pp = this._defaultPrettyPrinter;
/* 699 */     if (pp instanceof Instantiatable) {
/* 700 */       pp = (PrettyPrinter)((Instantiatable)pp).createInstance();
/*     */     }
/* 702 */     return pp;
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
/*     */   public void initialize(JsonGenerator g) {
/* 720 */     if (SerializationFeature.INDENT_OUTPUT.enabledIn(this._serFeatures))
/*     */     {
/* 722 */       if (g.getPrettyPrinter() == null) {
/* 723 */         PrettyPrinter pp = constructDefaultPrettyPrinter();
/* 724 */         if (pp != null) {
/* 725 */           g.setPrettyPrinter(pp);
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/* 730 */     boolean useBigDec = SerializationFeature.WRITE_BIGDECIMAL_AS_PLAIN.enabledIn(this._serFeatures);
/*     */     
/* 732 */     int mask = this._generatorFeaturesToChange;
/* 733 */     if (mask != 0 || useBigDec) {
/* 734 */       int newFlags = this._generatorFeatures;
/*     */       
/* 736 */       if (useBigDec) {
/* 737 */         int f = JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN.getMask();
/* 738 */         newFlags |= f;
/* 739 */         mask |= f;
/*     */       } 
/* 741 */       g.overrideStdFeatures(newFlags, mask);
/*     */     } 
/* 743 */     if (this._formatWriteFeaturesToChange != 0) {
/* 744 */       g.overrideFormatFeatures(this._formatWriteFeatures, this._formatWriteFeaturesToChange);
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
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public JsonInclude.Include getSerializationInclusion() {
/* 760 */     JsonInclude.Include incl = getDefaultPropertyInclusion().getValueInclusion();
/* 761 */     return (incl == JsonInclude.Include.USE_DEFAULTS) ? JsonInclude.Include.ALWAYS : incl;
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
/*     */   public boolean useRootWrapping() {
/* 773 */     if (this._rootName != null) {
/* 774 */       return !this._rootName.isEmpty();
/*     */     }
/* 776 */     return isEnabled(SerializationFeature.WRAP_ROOT_VALUE);
/*     */   }
/*     */   
/*     */   public final boolean isEnabled(SerializationFeature f) {
/* 780 */     return ((this._serFeatures & f.getMask()) != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isEnabled(JsonGenerator.Feature f, JsonFactory factory) {
/* 791 */     int mask = f.getMask();
/* 792 */     if ((this._generatorFeaturesToChange & mask) != 0) {
/* 793 */       return ((this._generatorFeatures & f.getMask()) != 0);
/*     */     }
/* 795 */     return factory.isEnabled(f);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean hasSerializationFeatures(int featureMask) {
/* 805 */     return ((this._serFeatures & featureMask) == featureMask);
/*     */   }
/*     */   
/*     */   public final int getSerializationFeatures() {
/* 809 */     return this._serFeatures;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FilterProvider getFilterProvider() {
/* 819 */     return this._filterProvider;
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
/*     */   public PrettyPrinter getDefaultPrettyPrinter() {
/* 833 */     return this._defaultPrettyPrinter;
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
/*     */   public <T extends BeanDescription> T introspect(JavaType type) {
/* 848 */     return (T)getClassIntrospector().forSerialization(this, type, (ClassIntrospector.MixInResolver)this);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\SerializationConfig.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */