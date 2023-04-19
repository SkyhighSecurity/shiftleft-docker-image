/*     */ package com.fasterxml.jackson.databind.cfg;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonFormat;
/*     */ import com.fasterxml.jackson.annotation.JsonInclude;
/*     */ import com.fasterxml.jackson.annotation.JsonSetter;
/*     */ import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
/*     */ import java.io.Serializable;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ConfigOverrides
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected Map<Class<?>, MutableConfigOverride> _overrides;
/*     */   protected JsonInclude.Value _defaultInclusion;
/*     */   protected JsonSetter.Value _defaultSetterInfo;
/*     */   protected VisibilityChecker<?> _visibilityChecker;
/*     */   protected Boolean _defaultMergeable;
/*     */   protected Boolean _defaultLeniency;
/*     */   
/*     */   public ConfigOverrides() {
/*  64 */     this(null, 
/*     */         
/*  66 */         JsonInclude.Value.empty(), 
/*  67 */         JsonSetter.Value.empty(), 
/*  68 */         (VisibilityChecker<?>)VisibilityChecker.Std.defaultInstance(), null, null);
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
/*     */   protected ConfigOverrides(Map<Class<?>, MutableConfigOverride> overrides, JsonInclude.Value defIncl, JsonSetter.Value defSetter, VisibilityChecker<?> defVisibility, Boolean defMergeable, Boolean defLeniency) {
/*  80 */     this._overrides = overrides;
/*  81 */     this._defaultInclusion = defIncl;
/*  82 */     this._defaultSetterInfo = defSetter;
/*  83 */     this._visibilityChecker = defVisibility;
/*  84 */     this._defaultMergeable = defMergeable;
/*  85 */     this._defaultLeniency = defLeniency;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected ConfigOverrides(Map<Class<?>, MutableConfigOverride> overrides, JsonInclude.Value defIncl, JsonSetter.Value defSetter, VisibilityChecker<?> defVisibility, Boolean defMergeable) {
/*  95 */     this(overrides, defIncl, defSetter, defVisibility, defMergeable, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public ConfigOverrides copy() {
/*     */     Map<Class<?>, MutableConfigOverride> newOverrides;
/* 101 */     if (this._overrides == null) {
/* 102 */       newOverrides = null;
/*     */     } else {
/* 104 */       newOverrides = _newMap();
/* 105 */       for (Map.Entry<Class<?>, MutableConfigOverride> entry : this._overrides.entrySet()) {
/* 106 */         newOverrides.put(entry.getKey(), ((MutableConfigOverride)entry.getValue()).copy());
/*     */       }
/*     */     } 
/* 109 */     return new ConfigOverrides(newOverrides, this._defaultInclusion, this._defaultSetterInfo, this._visibilityChecker, this._defaultMergeable, this._defaultLeniency);
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
/*     */   public ConfigOverride findOverride(Class<?> type) {
/* 121 */     if (this._overrides == null) {
/* 122 */       return null;
/*     */     }
/* 124 */     return this._overrides.get(type);
/*     */   }
/*     */   
/*     */   public MutableConfigOverride findOrCreateOverride(Class<?> type) {
/* 128 */     if (this._overrides == null) {
/* 129 */       this._overrides = _newMap();
/*     */     }
/* 131 */     MutableConfigOverride override = this._overrides.get(type);
/* 132 */     if (override == null) {
/* 133 */       override = new MutableConfigOverride();
/* 134 */       this._overrides.put(type, override);
/*     */     } 
/* 136 */     return override;
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
/*     */   public JsonFormat.Value findFormatDefaults(Class<?> type) {
/* 149 */     if (this._overrides != null) {
/* 150 */       ConfigOverride override = this._overrides.get(type);
/* 151 */       if (override != null) {
/* 152 */         JsonFormat.Value format = override.getFormat();
/* 153 */         if (format != null) {
/* 154 */           if (!format.hasLenient()) {
/* 155 */             return format.withLenient(this._defaultLeniency);
/*     */           }
/* 157 */           return format;
/*     */         } 
/*     */       } 
/*     */     } 
/* 161 */     if (this._defaultLeniency == null) {
/* 162 */       return JsonFormat.Value.empty();
/*     */     }
/* 164 */     return JsonFormat.Value.forLeniency(this._defaultLeniency.booleanValue());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonInclude.Value getDefaultInclusion() {
/* 174 */     return this._defaultInclusion;
/*     */   }
/*     */   
/*     */   public JsonSetter.Value getDefaultSetterInfo() {
/* 178 */     return this._defaultSetterInfo;
/*     */   }
/*     */   
/*     */   public Boolean getDefaultMergeable() {
/* 182 */     return this._defaultMergeable;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Boolean getDefaultLeniency() {
/* 189 */     return this._defaultLeniency;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public VisibilityChecker<?> getDefaultVisibility() {
/* 196 */     return this._visibilityChecker;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDefaultInclusion(JsonInclude.Value v) {
/* 203 */     this._defaultInclusion = v;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDefaultSetterInfo(JsonSetter.Value v) {
/* 210 */     this._defaultSetterInfo = v;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDefaultMergeable(Boolean v) {
/* 217 */     this._defaultMergeable = v;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDefaultLeniency(Boolean v) {
/* 224 */     this._defaultLeniency = v;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDefaultVisibility(VisibilityChecker<?> v) {
/* 231 */     this._visibilityChecker = v;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Map<Class<?>, MutableConfigOverride> _newMap() {
/* 241 */     return new HashMap<>();
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\cfg\ConfigOverrides.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */