/*     */ package com.fasterxml.jackson.databind.cfg;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonAutoDetect;
/*     */ import com.fasterxml.jackson.annotation.JsonFormat;
/*     */ import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/*     */ import com.fasterxml.jackson.annotation.JsonInclude;
/*     */ import com.fasterxml.jackson.annotation.JsonSetter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class ConfigOverride
/*     */ {
/*     */   protected JsonFormat.Value _format;
/*     */   protected JsonInclude.Value _include;
/*     */   protected JsonInclude.Value _includeAsProperty;
/*     */   protected JsonIgnoreProperties.Value _ignorals;
/*     */   protected JsonSetter.Value _setterInfo;
/*     */   protected JsonAutoDetect.Value _visibility;
/*     */   protected Boolean _isIgnoredType;
/*     */   protected Boolean _mergeable;
/*     */   
/*     */   protected ConfigOverride() {}
/*     */   
/*     */   protected ConfigOverride(ConfigOverride src) {
/*  78 */     this._format = src._format;
/*  79 */     this._include = src._include;
/*  80 */     this._includeAsProperty = src._includeAsProperty;
/*  81 */     this._ignorals = src._ignorals;
/*  82 */     this._setterInfo = src._setterInfo;
/*  83 */     this._visibility = src._visibility;
/*  84 */     this._isIgnoredType = src._isIgnoredType;
/*  85 */     this._mergeable = src._mergeable;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ConfigOverride empty() {
/*  94 */     return Empty.INSTANCE;
/*     */   }
/*     */   
/*  97 */   public JsonFormat.Value getFormat() { return this._format; } public JsonInclude.Value getInclude() {
/*  98 */     return this._include;
/*     */   }
/*     */ 
/*     */   
/*     */   public JsonInclude.Value getIncludeAsProperty() {
/* 103 */     return this._includeAsProperty;
/*     */   } public JsonIgnoreProperties.Value getIgnorals() {
/* 105 */     return this._ignorals;
/*     */   }
/*     */   public Boolean getIsIgnoredType() {
/* 108 */     return this._isIgnoredType;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonSetter.Value getSetterInfo() {
/* 114 */     return this._setterInfo;
/*     */   }
/*     */ 
/*     */   
/*     */   public JsonAutoDetect.Value getVisibility() {
/* 119 */     return this._visibility;
/*     */   }
/*     */ 
/*     */   
/*     */   public Boolean getMergeable() {
/* 124 */     return this._mergeable;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final class Empty
/*     */     extends ConfigOverride
/*     */   {
/* 133 */     static final Empty INSTANCE = new Empty();
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\cfg\ConfigOverride.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */