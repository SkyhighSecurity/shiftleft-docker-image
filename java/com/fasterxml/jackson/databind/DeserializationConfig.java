/*     */ package com.fasterxml.jackson.databind;
/*     */ 
/*     */ import com.fasterxml.jackson.core.FormatFeature;
/*     */ import com.fasterxml.jackson.core.JsonFactory;
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.json.JsonReadFeature;
/*     */ import com.fasterxml.jackson.databind.cfg.BaseSettings;
/*     */ import com.fasterxml.jackson.databind.cfg.ConfigOverrides;
/*     */ import com.fasterxml.jackson.databind.cfg.ContextAttributes;
/*     */ import com.fasterxml.jackson.databind.cfg.MapperConfig;
/*     */ import com.fasterxml.jackson.databind.cfg.MapperConfigBase;
/*     */ import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
/*     */ import com.fasterxml.jackson.databind.introspect.ClassIntrospector;
/*     */ import com.fasterxml.jackson.databind.introspect.SimpleMixInResolver;
/*     */ import com.fasterxml.jackson.databind.jsontype.NamedType;
/*     */ import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
/*     */ import com.fasterxml.jackson.databind.node.JsonNodeFactory;
/*     */ import com.fasterxml.jackson.databind.util.LinkedNode;
/*     */ import com.fasterxml.jackson.databind.util.RootNameLookup;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class DeserializationConfig
/*     */   extends MapperConfigBase<DeserializationFeature, DeserializationConfig>
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 2L;
/*     */   protected final LinkedNode<DeserializationProblemHandler> _problemHandlers;
/*     */   protected final JsonNodeFactory _nodeFactory;
/*     */   protected final int _deserFeatures;
/*     */   protected final int _parserFeatures;
/*     */   protected final int _parserFeaturesToChange;
/*     */   protected final int _formatReadFeatures;
/*     */   protected final int _formatReadFeaturesToChange;
/*     */   
/*     */   public DeserializationConfig(BaseSettings base, SubtypeResolver str, SimpleMixInResolver mixins, RootNameLookup rootNames, ConfigOverrides configOverrides) {
/* 103 */     super(base, str, mixins, rootNames, configOverrides);
/* 104 */     this._deserFeatures = collectFeatureDefaults(DeserializationFeature.class);
/* 105 */     this._nodeFactory = JsonNodeFactory.instance;
/* 106 */     this._problemHandlers = null;
/* 107 */     this._parserFeatures = 0;
/* 108 */     this._parserFeaturesToChange = 0;
/* 109 */     this._formatReadFeatures = 0;
/* 110 */     this._formatReadFeaturesToChange = 0;
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
/*     */   protected DeserializationConfig(DeserializationConfig src, SimpleMixInResolver mixins, RootNameLookup rootNames, ConfigOverrides configOverrides) {
/* 122 */     super(src, mixins, rootNames, configOverrides);
/* 123 */     this._deserFeatures = src._deserFeatures;
/* 124 */     this._problemHandlers = src._problemHandlers;
/* 125 */     this._nodeFactory = src._nodeFactory;
/* 126 */     this._parserFeatures = src._parserFeatures;
/* 127 */     this._parserFeaturesToChange = src._parserFeaturesToChange;
/* 128 */     this._formatReadFeatures = src._formatReadFeatures;
/* 129 */     this._formatReadFeaturesToChange = src._formatReadFeaturesToChange;
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
/*     */   private DeserializationConfig(DeserializationConfig src, int mapperFeatures, int deserFeatures, int parserFeatures, int parserFeatureMask, int formatFeatures, int formatFeatureMask) {
/* 144 */     super(src, mapperFeatures);
/* 145 */     this._deserFeatures = deserFeatures;
/* 146 */     this._nodeFactory = src._nodeFactory;
/* 147 */     this._problemHandlers = src._problemHandlers;
/* 148 */     this._parserFeatures = parserFeatures;
/* 149 */     this._parserFeaturesToChange = parserFeatureMask;
/* 150 */     this._formatReadFeatures = formatFeatures;
/* 151 */     this._formatReadFeaturesToChange = formatFeatureMask;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private DeserializationConfig(DeserializationConfig src, SubtypeResolver str) {
/* 160 */     super(src, str);
/* 161 */     this._deserFeatures = src._deserFeatures;
/* 162 */     this._nodeFactory = src._nodeFactory;
/* 163 */     this._problemHandlers = src._problemHandlers;
/* 164 */     this._parserFeatures = src._parserFeatures;
/* 165 */     this._parserFeaturesToChange = src._parserFeaturesToChange;
/* 166 */     this._formatReadFeatures = src._formatReadFeatures;
/* 167 */     this._formatReadFeaturesToChange = src._formatReadFeaturesToChange;
/*     */   }
/*     */ 
/*     */   
/*     */   private DeserializationConfig(DeserializationConfig src, BaseSettings base) {
/* 172 */     super(src, base);
/* 173 */     this._deserFeatures = src._deserFeatures;
/* 174 */     this._nodeFactory = src._nodeFactory;
/* 175 */     this._problemHandlers = src._problemHandlers;
/* 176 */     this._parserFeatures = src._parserFeatures;
/* 177 */     this._parserFeaturesToChange = src._parserFeaturesToChange;
/* 178 */     this._formatReadFeatures = src._formatReadFeatures;
/* 179 */     this._formatReadFeaturesToChange = src._formatReadFeaturesToChange;
/*     */   }
/*     */ 
/*     */   
/*     */   private DeserializationConfig(DeserializationConfig src, JsonNodeFactory f) {
/* 184 */     super(src);
/* 185 */     this._deserFeatures = src._deserFeatures;
/* 186 */     this._problemHandlers = src._problemHandlers;
/* 187 */     this._nodeFactory = f;
/* 188 */     this._parserFeatures = src._parserFeatures;
/* 189 */     this._parserFeaturesToChange = src._parserFeaturesToChange;
/* 190 */     this._formatReadFeatures = src._formatReadFeatures;
/* 191 */     this._formatReadFeaturesToChange = src._formatReadFeaturesToChange;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private DeserializationConfig(DeserializationConfig src, LinkedNode<DeserializationProblemHandler> problemHandlers) {
/* 197 */     super(src);
/* 198 */     this._deserFeatures = src._deserFeatures;
/* 199 */     this._problemHandlers = problemHandlers;
/* 200 */     this._nodeFactory = src._nodeFactory;
/* 201 */     this._parserFeatures = src._parserFeatures;
/* 202 */     this._parserFeaturesToChange = src._parserFeaturesToChange;
/* 203 */     this._formatReadFeatures = src._formatReadFeatures;
/* 204 */     this._formatReadFeaturesToChange = src._formatReadFeaturesToChange;
/*     */   }
/*     */ 
/*     */   
/*     */   private DeserializationConfig(DeserializationConfig src, PropertyName rootName) {
/* 209 */     super(src, rootName);
/* 210 */     this._deserFeatures = src._deserFeatures;
/* 211 */     this._problemHandlers = src._problemHandlers;
/* 212 */     this._nodeFactory = src._nodeFactory;
/* 213 */     this._parserFeatures = src._parserFeatures;
/* 214 */     this._parserFeaturesToChange = src._parserFeaturesToChange;
/* 215 */     this._formatReadFeatures = src._formatReadFeatures;
/* 216 */     this._formatReadFeaturesToChange = src._formatReadFeaturesToChange;
/*     */   }
/*     */ 
/*     */   
/*     */   private DeserializationConfig(DeserializationConfig src, Class<?> view) {
/* 221 */     super(src, view);
/* 222 */     this._deserFeatures = src._deserFeatures;
/* 223 */     this._problemHandlers = src._problemHandlers;
/* 224 */     this._nodeFactory = src._nodeFactory;
/* 225 */     this._parserFeatures = src._parserFeatures;
/* 226 */     this._parserFeaturesToChange = src._parserFeaturesToChange;
/* 227 */     this._formatReadFeatures = src._formatReadFeatures;
/* 228 */     this._formatReadFeaturesToChange = src._formatReadFeaturesToChange;
/*     */   }
/*     */ 
/*     */   
/*     */   protected DeserializationConfig(DeserializationConfig src, ContextAttributes attrs) {
/* 233 */     super(src, attrs);
/* 234 */     this._deserFeatures = src._deserFeatures;
/* 235 */     this._problemHandlers = src._problemHandlers;
/* 236 */     this._nodeFactory = src._nodeFactory;
/* 237 */     this._parserFeatures = src._parserFeatures;
/* 238 */     this._parserFeaturesToChange = src._parserFeaturesToChange;
/* 239 */     this._formatReadFeatures = src._formatReadFeatures;
/* 240 */     this._formatReadFeaturesToChange = src._formatReadFeaturesToChange;
/*     */   }
/*     */ 
/*     */   
/*     */   protected DeserializationConfig(DeserializationConfig src, SimpleMixInResolver mixins) {
/* 245 */     super(src, mixins);
/* 246 */     this._deserFeatures = src._deserFeatures;
/* 247 */     this._problemHandlers = src._problemHandlers;
/* 248 */     this._nodeFactory = src._nodeFactory;
/* 249 */     this._parserFeatures = src._parserFeatures;
/* 250 */     this._parserFeaturesToChange = src._parserFeaturesToChange;
/* 251 */     this._formatReadFeatures = src._formatReadFeatures;
/* 252 */     this._formatReadFeaturesToChange = src._formatReadFeaturesToChange;
/*     */   }
/*     */   
/*     */   protected BaseSettings getBaseSettings() {
/* 256 */     return this._base;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final DeserializationConfig _withBase(BaseSettings newBase) {
/* 266 */     return (this._base == newBase) ? this : new DeserializationConfig(this, newBase);
/*     */   }
/*     */ 
/*     */   
/*     */   protected final DeserializationConfig _withMapperFeatures(int mapperFeatures) {
/* 271 */     return new DeserializationConfig(this, mapperFeatures, this._deserFeatures, this._parserFeatures, this._parserFeaturesToChange, this._formatReadFeatures, this._formatReadFeaturesToChange);
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
/*     */   public DeserializationConfig with(SubtypeResolver str) {
/* 284 */     return (this._subtypeResolver == str) ? this : new DeserializationConfig(this, str);
/*     */   }
/*     */ 
/*     */   
/*     */   public DeserializationConfig withRootName(PropertyName rootName) {
/* 289 */     if (rootName == null) {
/* 290 */       if (this._rootName == null) {
/* 291 */         return this;
/*     */       }
/* 293 */     } else if (rootName.equals(this._rootName)) {
/* 294 */       return this;
/*     */     } 
/* 296 */     return new DeserializationConfig(this, rootName);
/*     */   }
/*     */ 
/*     */   
/*     */   public DeserializationConfig withView(Class<?> view) {
/* 301 */     return (this._view == view) ? this : new DeserializationConfig(this, view);
/*     */   }
/*     */ 
/*     */   
/*     */   public DeserializationConfig with(ContextAttributes attrs) {
/* 306 */     return (attrs == this._attributes) ? this : new DeserializationConfig(this, attrs);
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
/*     */   public DeserializationConfig with(DeserializationFeature feature) {
/* 321 */     int newDeserFeatures = this._deserFeatures | feature.getMask();
/* 322 */     return (newDeserFeatures == this._deserFeatures) ? this : new DeserializationConfig(this, this._mapperFeatures, newDeserFeatures, this._parserFeatures, this._parserFeaturesToChange, this._formatReadFeatures, this._formatReadFeaturesToChange);
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
/*     */   public DeserializationConfig with(DeserializationFeature first, DeserializationFeature... features) {
/* 335 */     int newDeserFeatures = this._deserFeatures | first.getMask();
/* 336 */     for (DeserializationFeature f : features) {
/* 337 */       newDeserFeatures |= f.getMask();
/*     */     }
/* 339 */     return (newDeserFeatures == this._deserFeatures) ? this : new DeserializationConfig(this, this._mapperFeatures, newDeserFeatures, this._parserFeatures, this._parserFeaturesToChange, this._formatReadFeatures, this._formatReadFeaturesToChange);
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
/*     */   public DeserializationConfig withFeatures(DeserializationFeature... features) {
/* 351 */     int newDeserFeatures = this._deserFeatures;
/* 352 */     for (DeserializationFeature f : features) {
/* 353 */       newDeserFeatures |= f.getMask();
/*     */     }
/* 355 */     return (newDeserFeatures == this._deserFeatures) ? this : new DeserializationConfig(this, this._mapperFeatures, newDeserFeatures, this._parserFeatures, this._parserFeaturesToChange, this._formatReadFeatures, this._formatReadFeaturesToChange);
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
/*     */   public DeserializationConfig without(DeserializationFeature feature) {
/* 367 */     int newDeserFeatures = this._deserFeatures & (feature.getMask() ^ 0xFFFFFFFF);
/* 368 */     return (newDeserFeatures == this._deserFeatures) ? this : new DeserializationConfig(this, this._mapperFeatures, newDeserFeatures, this._parserFeatures, this._parserFeaturesToChange, this._formatReadFeatures, this._formatReadFeaturesToChange);
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
/*     */   public DeserializationConfig without(DeserializationFeature first, DeserializationFeature... features) {
/* 381 */     int newDeserFeatures = this._deserFeatures & (first.getMask() ^ 0xFFFFFFFF);
/* 382 */     for (DeserializationFeature f : features) {
/* 383 */       newDeserFeatures &= f.getMask() ^ 0xFFFFFFFF;
/*     */     }
/* 385 */     return (newDeserFeatures == this._deserFeatures) ? this : new DeserializationConfig(this, this._mapperFeatures, newDeserFeatures, this._parserFeatures, this._parserFeaturesToChange, this._formatReadFeatures, this._formatReadFeaturesToChange);
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
/*     */   public DeserializationConfig withoutFeatures(DeserializationFeature... features) {
/* 397 */     int newDeserFeatures = this._deserFeatures;
/* 398 */     for (DeserializationFeature f : features) {
/* 399 */       newDeserFeatures &= f.getMask() ^ 0xFFFFFFFF;
/*     */     }
/* 401 */     return (newDeserFeatures == this._deserFeatures) ? this : new DeserializationConfig(this, this._mapperFeatures, newDeserFeatures, this._parserFeatures, this._parserFeaturesToChange, this._formatReadFeatures, this._formatReadFeaturesToChange);
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
/*     */   public DeserializationConfig with(JsonParser.Feature feature) {
/* 421 */     int newSet = this._parserFeatures | feature.getMask();
/* 422 */     int newMask = this._parserFeaturesToChange | feature.getMask();
/* 423 */     return (this._parserFeatures == newSet && this._parserFeaturesToChange == newMask) ? this : new DeserializationConfig(this, this._mapperFeatures, this._deserFeatures, newSet, newMask, this._formatReadFeatures, this._formatReadFeaturesToChange);
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
/*     */   public DeserializationConfig withFeatures(JsonParser.Feature... features) {
/* 437 */     int newSet = this._parserFeatures;
/* 438 */     int newMask = this._parserFeaturesToChange;
/* 439 */     for (JsonParser.Feature f : features) {
/* 440 */       int mask = f.getMask();
/* 441 */       newSet |= mask;
/* 442 */       newMask |= mask;
/*     */     } 
/* 444 */     return (this._parserFeatures == newSet && this._parserFeaturesToChange == newMask) ? this : new DeserializationConfig(this, this._mapperFeatures, this._deserFeatures, newSet, newMask, this._formatReadFeatures, this._formatReadFeaturesToChange);
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
/*     */   public DeserializationConfig without(JsonParser.Feature feature) {
/* 458 */     int newSet = this._parserFeatures & (feature.getMask() ^ 0xFFFFFFFF);
/* 459 */     int newMask = this._parserFeaturesToChange | feature.getMask();
/* 460 */     return (this._parserFeatures == newSet && this._parserFeaturesToChange == newMask) ? this : new DeserializationConfig(this, this._mapperFeatures, this._deserFeatures, newSet, newMask, this._formatReadFeatures, this._formatReadFeaturesToChange);
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
/*     */   public DeserializationConfig withoutFeatures(JsonParser.Feature... features) {
/* 474 */     int newSet = this._parserFeatures;
/* 475 */     int newMask = this._parserFeaturesToChange;
/* 476 */     for (JsonParser.Feature f : features) {
/* 477 */       int mask = f.getMask();
/* 478 */       newSet &= mask ^ 0xFFFFFFFF;
/* 479 */       newMask |= mask;
/*     */     } 
/* 481 */     return (this._parserFeatures == newSet && this._parserFeaturesToChange == newMask) ? this : new DeserializationConfig(this, this._mapperFeatures, this._deserFeatures, newSet, newMask, this._formatReadFeatures, this._formatReadFeaturesToChange);
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
/*     */   public DeserializationConfig with(FormatFeature feature) {
/* 502 */     if (feature instanceof JsonReadFeature) {
/* 503 */       return _withJsonReadFeatures(new FormatFeature[] { feature });
/*     */     }
/* 505 */     int newSet = this._formatReadFeatures | feature.getMask();
/* 506 */     int newMask = this._formatReadFeaturesToChange | feature.getMask();
/* 507 */     return (this._formatReadFeatures == newSet && this._formatReadFeaturesToChange == newMask) ? this : new DeserializationConfig(this, this._mapperFeatures, this._deserFeatures, this._parserFeatures, this._parserFeaturesToChange, newSet, newMask);
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
/*     */   public DeserializationConfig withFeatures(FormatFeature... features) {
/* 522 */     if (features.length > 0 && features[0] instanceof JsonReadFeature) {
/* 523 */       return _withJsonReadFeatures(features);
/*     */     }
/* 525 */     int newSet = this._formatReadFeatures;
/* 526 */     int newMask = this._formatReadFeaturesToChange;
/* 527 */     for (FormatFeature f : features) {
/* 528 */       int mask = f.getMask();
/* 529 */       newSet |= mask;
/* 530 */       newMask |= mask;
/*     */     } 
/* 532 */     return (this._formatReadFeatures == newSet && this._formatReadFeaturesToChange == newMask) ? this : new DeserializationConfig(this, this._mapperFeatures, this._deserFeatures, this._parserFeatures, this._parserFeaturesToChange, newSet, newMask);
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
/*     */   public DeserializationConfig without(FormatFeature feature) {
/* 547 */     if (feature instanceof JsonReadFeature) {
/* 548 */       return _withoutJsonReadFeatures(new FormatFeature[] { feature });
/*     */     }
/* 550 */     int newSet = this._formatReadFeatures & (feature.getMask() ^ 0xFFFFFFFF);
/* 551 */     int newMask = this._formatReadFeaturesToChange | feature.getMask();
/* 552 */     return (this._formatReadFeatures == newSet && this._formatReadFeaturesToChange == newMask) ? this : new DeserializationConfig(this, this._mapperFeatures, this._deserFeatures, this._parserFeatures, this._parserFeaturesToChange, newSet, newMask);
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
/*     */   public DeserializationConfig withoutFeatures(FormatFeature... features) {
/* 567 */     if (features.length > 0 && features[0] instanceof JsonReadFeature) {
/* 568 */       return _withoutJsonReadFeatures(features);
/*     */     }
/* 570 */     int newSet = this._formatReadFeatures;
/* 571 */     int newMask = this._formatReadFeaturesToChange;
/* 572 */     for (FormatFeature f : features) {
/* 573 */       int mask = f.getMask();
/* 574 */       newSet &= mask ^ 0xFFFFFFFF;
/* 575 */       newMask |= mask;
/*     */     } 
/* 577 */     return (this._formatReadFeatures == newSet && this._formatReadFeaturesToChange == newMask) ? this : new DeserializationConfig(this, this._mapperFeatures, this._deserFeatures, this._parserFeatures, this._parserFeaturesToChange, newSet, newMask);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private DeserializationConfig _withJsonReadFeatures(FormatFeature... features) {
/* 585 */     int parserSet = this._parserFeatures;
/* 586 */     int parserMask = this._parserFeaturesToChange;
/* 587 */     int newSet = this._formatReadFeatures;
/* 588 */     int newMask = this._formatReadFeaturesToChange;
/* 589 */     for (FormatFeature f : features) {
/* 590 */       int mask = f.getMask();
/* 591 */       newSet |= mask;
/* 592 */       newMask |= mask;
/*     */       
/* 594 */       if (f instanceof JsonReadFeature) {
/* 595 */         JsonParser.Feature oldF = ((JsonReadFeature)f).mappedFeature();
/* 596 */         if (oldF != null) {
/* 597 */           int pmask = oldF.getMask();
/* 598 */           parserSet |= pmask;
/* 599 */           parserMask |= pmask;
/*     */         } 
/*     */       } 
/*     */     } 
/* 603 */     return (this._formatReadFeatures == newSet && this._formatReadFeaturesToChange == newMask && this._parserFeatures == parserSet && this._parserFeaturesToChange == parserMask) ? this : new DeserializationConfig(this, this._mapperFeatures, this._deserFeatures, parserSet, parserMask, newSet, newMask);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private DeserializationConfig _withoutJsonReadFeatures(FormatFeature... features) {
/* 612 */     int parserSet = this._parserFeatures;
/* 613 */     int parserMask = this._parserFeaturesToChange;
/* 614 */     int newSet = this._formatReadFeatures;
/* 615 */     int newMask = this._formatReadFeaturesToChange;
/* 616 */     for (FormatFeature f : features) {
/* 617 */       int mask = f.getMask();
/* 618 */       newSet &= mask ^ 0xFFFFFFFF;
/* 619 */       newMask |= mask;
/*     */       
/* 621 */       if (f instanceof JsonReadFeature) {
/* 622 */         JsonParser.Feature oldF = ((JsonReadFeature)f).mappedFeature();
/* 623 */         if (oldF != null) {
/* 624 */           int pmask = oldF.getMask();
/* 625 */           parserSet &= pmask ^ 0xFFFFFFFF;
/* 626 */           parserMask |= pmask;
/*     */         } 
/*     */       } 
/*     */     } 
/* 630 */     return (this._formatReadFeatures == newSet && this._formatReadFeaturesToChange == newMask && this._parserFeatures == parserSet && this._parserFeaturesToChange == parserMask) ? this : new DeserializationConfig(this, this._mapperFeatures, this._deserFeatures, parserSet, parserMask, newSet, newMask);
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
/*     */   public DeserializationConfig with(JsonNodeFactory f) {
/* 648 */     if (this._nodeFactory == f) {
/* 649 */       return this;
/*     */     }
/* 651 */     return new DeserializationConfig(this, f);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DeserializationConfig withHandler(DeserializationProblemHandler h) {
/* 661 */     if (LinkedNode.contains(this._problemHandlers, h)) {
/* 662 */       return this;
/*     */     }
/* 664 */     return new DeserializationConfig(this, new LinkedNode(h, this._problemHandlers));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DeserializationConfig withNoProblemHandlers() {
/* 673 */     if (this._problemHandlers == null) {
/* 674 */       return this;
/*     */     }
/* 676 */     return new DeserializationConfig(this, (LinkedNode<DeserializationProblemHandler>)null);
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
/*     */   public void initialize(JsonParser p) {
/* 694 */     if (this._parserFeaturesToChange != 0) {
/* 695 */       p.overrideStdFeatures(this._parserFeatures, this._parserFeaturesToChange);
/*     */     }
/* 697 */     if (this._formatReadFeaturesToChange != 0) {
/* 698 */       p.overrideFormatFeatures(this._formatReadFeatures, this._formatReadFeaturesToChange);
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
/*     */   public boolean useRootWrapping() {
/* 711 */     if (this._rootName != null) {
/* 712 */       return !this._rootName.isEmpty();
/*     */     }
/* 714 */     return isEnabled(DeserializationFeature.UNWRAP_ROOT_VALUE);
/*     */   }
/*     */   
/*     */   public final boolean isEnabled(DeserializationFeature f) {
/* 718 */     return ((this._deserFeatures & f.getMask()) != 0);
/*     */   }
/*     */   
/*     */   public final boolean isEnabled(JsonParser.Feature f, JsonFactory factory) {
/* 722 */     int mask = f.getMask();
/* 723 */     if ((this._parserFeaturesToChange & mask) != 0) {
/* 724 */       return ((this._parserFeatures & f.getMask()) != 0);
/*     */     }
/* 726 */     return factory.isEnabled(f);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean hasDeserializationFeatures(int featureMask) {
/* 736 */     return ((this._deserFeatures & featureMask) == featureMask);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean hasSomeOfFeatures(int featureMask) {
/* 746 */     return ((this._deserFeatures & featureMask) != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getDeserializationFeatures() {
/* 754 */     return this._deserFeatures;
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
/*     */   public final boolean requiresFullValue() {
/* 766 */     return DeserializationFeature.FAIL_ON_TRAILING_TOKENS.enabledIn(this._deserFeatures);
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
/*     */   public LinkedNode<DeserializationProblemHandler> getProblemHandlers() {
/* 780 */     return this._problemHandlers;
/*     */   }
/*     */   
/*     */   public final JsonNodeFactory getNodeFactory() {
/* 784 */     return this._nodeFactory;
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
/*     */   public <T extends BeanDescription> T introspect(JavaType type) {
/* 801 */     return (T)getClassIntrospector().forDeserialization(this, type, (ClassIntrospector.MixInResolver)this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T extends BeanDescription> T introspectForCreation(JavaType type) {
/* 810 */     return (T)getClassIntrospector().forCreation(this, type, (ClassIntrospector.MixInResolver)this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T extends BeanDescription> T introspectForBuilder(JavaType type) {
/* 818 */     return (T)getClassIntrospector().forDeserializationWithBuilder(this, type, (ClassIntrospector.MixInResolver)this);
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
/*     */   public TypeDeserializer findTypeDeserializer(JavaType baseType) throws JsonMappingException {
/* 837 */     BeanDescription bean = introspectClassAnnotations(baseType.getRawClass());
/* 838 */     AnnotatedClass ac = bean.getClassInfo();
/* 839 */     TypeResolverBuilder<?> b = getAnnotationIntrospector().findTypeResolver((MapperConfig<?>)this, ac, baseType);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 844 */     Collection<NamedType> subtypes = null;
/* 845 */     if (b == null) {
/* 846 */       b = getDefaultTyper(baseType);
/* 847 */       if (b == null) {
/* 848 */         return null;
/*     */       }
/*     */     } else {
/* 851 */       subtypes = getSubtypeResolver().collectAndResolveSubtypesByTypeId((MapperConfig)this, ac);
/*     */     } 
/* 853 */     return b.buildTypeDeserializer(this, baseType, subtypes);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\DeserializationConfig.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */