/*     */ package com.fasterxml.jackson.databind.type;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ResolvedRecursiveType
/*     */   extends TypeBase
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected JavaType _referencedType;
/*     */   
/*     */   public ResolvedRecursiveType(Class<?> erasedType, TypeBindings bindings) {
/*  17 */     super(erasedType, bindings, null, null, 0, null, null, false);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setReference(JavaType ref) {
/*  23 */     if (this._referencedType != null) {
/*  24 */       throw new IllegalStateException("Trying to re-set self reference; old value = " + this._referencedType + ", new = " + ref);
/*     */     }
/*  26 */     this._referencedType = ref;
/*     */   }
/*     */ 
/*     */   
/*     */   public JavaType getSuperClass() {
/*  31 */     if (this._referencedType != null) {
/*  32 */       return this._referencedType.getSuperClass();
/*     */     }
/*  34 */     return super.getSuperClass();
/*     */   }
/*     */   public JavaType getSelfReferencedType() {
/*  37 */     return this._referencedType;
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeBindings getBindings() {
/*  42 */     if (this._referencedType != null) {
/*  43 */       return this._referencedType.getBindings();
/*     */     }
/*  45 */     return super.getBindings();
/*     */   }
/*     */ 
/*     */   
/*     */   public StringBuilder getGenericSignature(StringBuilder sb) {
/*  50 */     if (this._referencedType != null) {
/*  51 */       return this._referencedType.getGenericSignature(sb);
/*     */     }
/*  53 */     return sb.append("?");
/*     */   }
/*     */ 
/*     */   
/*     */   public StringBuilder getErasedSignature(StringBuilder sb) {
/*  58 */     if (this._referencedType != null) {
/*  59 */       return this._referencedType.getErasedSignature(sb);
/*     */     }
/*  61 */     return sb;
/*     */   }
/*     */ 
/*     */   
/*     */   public JavaType withContentType(JavaType contentType) {
/*  66 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public JavaType withTypeHandler(Object h) {
/*  71 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public JavaType withContentTypeHandler(Object h) {
/*  76 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public JavaType withValueHandler(Object h) {
/*  81 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public JavaType withContentValueHandler(Object h) {
/*  86 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public JavaType withStaticTyping() {
/*  91 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected JavaType _narrow(Class<?> subclass) {
/*  97 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JavaType refine(Class<?> rawType, TypeBindings bindings, JavaType superClass, JavaType[] superInterfaces) {
/* 103 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isContainerType() {
/* 108 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 114 */     StringBuilder sb = (new StringBuilder(40)).append("[recursive type; ");
/* 115 */     if (this._referencedType == null) {
/* 116 */       sb.append("UNRESOLVED");
/*     */     }
/*     */     else {
/*     */       
/* 120 */       sb.append(this._referencedType.getRawClass().getName());
/*     */     } 
/* 122 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 127 */     if (o == this) return true; 
/* 128 */     if (o == null) return false; 
/* 129 */     if (o.getClass() == getClass())
/*     */     {
/*     */ 
/*     */ 
/*     */       
/* 134 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 145 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\type\ResolvedRecursiveType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */