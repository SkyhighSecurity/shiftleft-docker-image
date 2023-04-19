/*     */ package com.fasterxml.jackson.annotation;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.lang.annotation.ElementType;
/*     */ import java.lang.annotation.Retention;
/*     */ import java.lang.annotation.RetentionPolicy;
/*     */ import java.lang.annotation.Target;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
/*     */ @Retention(RetentionPolicy.RUNTIME)
/*     */ @JacksonAnnotation
/*     */ public @interface JacksonInject
/*     */ {
/*     */   String value() default "";
/*     */   
/*     */   OptBoolean useInput() default OptBoolean.DEFAULT;
/*     */   
/*     */   public static class Value
/*     */     implements JacksonAnnotationValue<JacksonInject>, Serializable
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*  68 */     protected static final Value EMPTY = new Value(null, null);
/*     */ 
/*     */     
/*     */     protected final Object _id;
/*     */ 
/*     */     
/*     */     protected final Boolean _useInput;
/*     */ 
/*     */ 
/*     */     
/*     */     protected Value(Object id, Boolean useInput) {
/*  79 */       this._id = id;
/*  80 */       this._useInput = useInput;
/*     */     }
/*     */ 
/*     */     
/*     */     public Class<JacksonInject> valueFor() {
/*  85 */       return JacksonInject.class;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static Value empty() {
/*  95 */       return EMPTY;
/*     */     }
/*     */     
/*     */     public static Value construct(Object id, Boolean useInput) {
/*  99 */       if ("".equals(id)) {
/* 100 */         id = null;
/*     */       }
/* 102 */       if (_empty(id, useInput)) {
/* 103 */         return EMPTY;
/*     */       }
/* 105 */       return new Value(id, useInput);
/*     */     }
/*     */     
/*     */     public static Value from(JacksonInject src) {
/* 109 */       if (src == null) {
/* 110 */         return EMPTY;
/*     */       }
/* 112 */       return construct(src.value(), src.useInput().asBoolean());
/*     */     }
/*     */     
/*     */     public static Value forId(Object id) {
/* 116 */       return construct(id, null);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Value withId(Object id) {
/* 126 */       if (id == null) {
/* 127 */         if (this._id == null) {
/* 128 */           return this;
/*     */         }
/* 130 */       } else if (id.equals(this._id)) {
/* 131 */         return this;
/*     */       } 
/* 133 */       return new Value(id, this._useInput);
/*     */     }
/*     */     
/*     */     public Value withUseInput(Boolean useInput) {
/* 137 */       if (useInput == null) {
/* 138 */         if (this._useInput == null) {
/* 139 */           return this;
/*     */         }
/* 141 */       } else if (useInput.equals(this._useInput)) {
/* 142 */         return this;
/*     */       } 
/* 144 */       return new Value(this._id, useInput);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Object getId() {
/* 153 */       return this._id; } public Boolean getUseInput() {
/* 154 */       return this._useInput;
/*     */     }
/*     */     public boolean hasId() {
/* 157 */       return (this._id != null);
/*     */     }
/*     */     
/*     */     public boolean willUseInput(boolean defaultSetting) {
/* 161 */       return (this._useInput == null) ? defaultSetting : this._useInput.booleanValue();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 172 */       return String.format("JacksonInject.Value(id=%s,useInput=%s)", new Object[] { this._id, this._useInput });
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 178 */       int h = 1;
/* 179 */       if (this._id != null) {
/* 180 */         h += this._id.hashCode();
/*     */       }
/* 182 */       if (this._useInput != null) {
/* 183 */         h += this._useInput.hashCode();
/*     */       }
/* 185 */       return h;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object o) {
/* 190 */       if (o == this) return true; 
/* 191 */       if (o == null) return false; 
/* 192 */       if (o.getClass() == getClass()) {
/* 193 */         Value other = (Value)o;
/* 194 */         if (OptBoolean.equals(this._useInput, other._useInput)) {
/* 195 */           if (this._id == null) {
/* 196 */             return (other._id == null);
/*     */           }
/* 198 */           return this._id.equals(other._id);
/*     */         } 
/*     */       } 
/* 201 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private static boolean _empty(Object id, Boolean useInput) {
/* 211 */       return (id == null && useInput == null);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\annotation\JacksonInject.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */