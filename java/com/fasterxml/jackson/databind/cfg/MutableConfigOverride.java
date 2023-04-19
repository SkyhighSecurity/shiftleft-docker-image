/*    */ package com.fasterxml.jackson.databind.cfg;
/*    */ 
/*    */ import com.fasterxml.jackson.annotation.JsonAutoDetect;
/*    */ import com.fasterxml.jackson.annotation.JsonFormat;
/*    */ import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/*    */ import com.fasterxml.jackson.annotation.JsonInclude;
/*    */ import com.fasterxml.jackson.annotation.JsonSetter;
/*    */ import java.io.Serializable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MutableConfigOverride
/*    */   extends ConfigOverride
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public MutableConfigOverride() {}
/*    */   
/*    */   protected MutableConfigOverride(MutableConfigOverride src) {
/* 27 */     super(src);
/*    */   }
/*    */   
/*    */   public MutableConfigOverride copy() {
/* 31 */     return new MutableConfigOverride(this);
/*    */   }
/*    */   
/*    */   public MutableConfigOverride setFormat(JsonFormat.Value v) {
/* 35 */     this._format = v;
/* 36 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public MutableConfigOverride setInclude(JsonInclude.Value v) {
/* 46 */     this._include = v;
/* 47 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public MutableConfigOverride setIncludeAsProperty(JsonInclude.Value v) {
/* 59 */     this._includeAsProperty = v;
/* 60 */     return this;
/*    */   }
/*    */   
/*    */   public MutableConfigOverride setIgnorals(JsonIgnoreProperties.Value v) {
/* 64 */     this._ignorals = v;
/* 65 */     return this;
/*    */   }
/*    */   
/*    */   public MutableConfigOverride setIsIgnoredType(Boolean v) {
/* 69 */     this._isIgnoredType = v;
/* 70 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public MutableConfigOverride setSetterInfo(JsonSetter.Value v) {
/* 77 */     this._setterInfo = v;
/* 78 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public MutableConfigOverride setVisibility(JsonAutoDetect.Value v) {
/* 85 */     this._visibility = v;
/* 86 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public MutableConfigOverride setMergeable(Boolean v) {
/* 93 */     this._mergeable = v;
/* 94 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\cfg\MutableConfigOverride.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */