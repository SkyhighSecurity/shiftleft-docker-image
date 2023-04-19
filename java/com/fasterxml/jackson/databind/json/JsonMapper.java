/*     */ package com.fasterxml.jackson.databind.json;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonFactory;
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.Version;
/*     */ import com.fasterxml.jackson.core.json.JsonReadFeature;
/*     */ import com.fasterxml.jackson.core.json.JsonWriteFeature;
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import com.fasterxml.jackson.databind.cfg.MapperBuilder;
/*     */ import com.fasterxml.jackson.databind.cfg.PackageVersion;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JsonMapper
/*     */   extends ObjectMapper
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   
/*     */   public static class Builder
/*     */     extends MapperBuilder<JsonMapper, Builder>
/*     */   {
/*     */     public Builder(JsonMapper m) {
/*  30 */       super(m);
/*     */     }
/*     */     
/*     */     public Builder enable(JsonReadFeature... features) {
/*  34 */       for (JsonReadFeature f : features) {
/*  35 */         ((JsonMapper)this._mapper).enable(new JsonParser.Feature[] { f.mappedFeature() });
/*     */       } 
/*  37 */       return this;
/*     */     }
/*     */     
/*     */     public Builder disable(JsonReadFeature... features) {
/*  41 */       for (JsonReadFeature f : features) {
/*  42 */         ((JsonMapper)this._mapper).disable(new JsonParser.Feature[] { f.mappedFeature() });
/*     */       } 
/*  44 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Builder configure(JsonReadFeature f, boolean state) {
/*  49 */       if (state) {
/*  50 */         ((JsonMapper)this._mapper).enable(new JsonParser.Feature[] { f.mappedFeature() });
/*     */       } else {
/*  52 */         ((JsonMapper)this._mapper).disable(new JsonParser.Feature[] { f.mappedFeature() });
/*     */       } 
/*  54 */       return this;
/*     */     }
/*     */     
/*     */     public Builder enable(JsonWriteFeature... features) {
/*  58 */       for (JsonWriteFeature f : features) {
/*  59 */         ((JsonMapper)this._mapper).enable(new JsonGenerator.Feature[] { f.mappedFeature() });
/*     */       } 
/*  61 */       return this;
/*     */     }
/*     */     
/*     */     public Builder disable(JsonWriteFeature... features) {
/*  65 */       for (JsonWriteFeature f : features) {
/*  66 */         ((JsonMapper)this._mapper).disable(new JsonGenerator.Feature[] { f.mappedFeature() });
/*     */       } 
/*  68 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Builder configure(JsonWriteFeature f, boolean state) {
/*  73 */       if (state) {
/*  74 */         ((JsonMapper)this._mapper).enable(new JsonGenerator.Feature[] { f.mappedFeature() });
/*     */       } else {
/*  76 */         ((JsonMapper)this._mapper).disable(new JsonGenerator.Feature[] { f.mappedFeature() });
/*     */       } 
/*  78 */       return this;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonMapper() {
/*  89 */     this(new JsonFactory());
/*     */   }
/*     */   
/*     */   public JsonMapper(JsonFactory f) {
/*  93 */     super(f);
/*     */   }
/*     */   
/*     */   protected JsonMapper(JsonMapper src) {
/*  97 */     super(src);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonMapper copy() {
/* 103 */     _checkInvalidCopy(JsonMapper.class);
/* 104 */     return new JsonMapper(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Builder builder() {
/* 114 */     return new Builder(new JsonMapper());
/*     */   }
/*     */   
/*     */   public static Builder builder(JsonFactory streamFactory) {
/* 118 */     return new Builder(new JsonMapper(streamFactory));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Builder rebuild() {
/* 124 */     return new Builder(copy());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Version version() {
/* 135 */     return PackageVersion.VERSION;
/*     */   }
/*     */ 
/*     */   
/*     */   public JsonFactory getFactory() {
/* 140 */     return this._jsonFactory;
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
/*     */   public boolean isEnabled(JsonReadFeature f) {
/* 153 */     return isEnabled(f.mappedFeature());
/*     */   }
/*     */   
/*     */   public boolean isEnabled(JsonWriteFeature f) {
/* 157 */     return isEnabled(f.mappedFeature());
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\json\JsonMapper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */