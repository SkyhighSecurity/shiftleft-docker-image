/*     */ package com.fasterxml.jackson.databind.type;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PlaceholderForType
/*     */   extends TypeBase
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final int _ordinal;
/*     */   protected JavaType _actualType;
/*     */   
/*     */   public PlaceholderForType(int ordinal) {
/*  25 */     super(Object.class, TypeBindings.emptyBindings(), 
/*  26 */         TypeFactory.unknownType(), null, 1, null, null, false);
/*     */     
/*  28 */     this._ordinal = ordinal;
/*     */   }
/*     */   
/*  31 */   public JavaType actualType() { return this._actualType; } public void actualType(JavaType t) {
/*  32 */     this._actualType = t;
/*     */   }
/*     */ 
/*     */   
/*     */   protected String buildCanonicalName() {
/*  37 */     return toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public StringBuilder getGenericSignature(StringBuilder sb) {
/*  42 */     return getErasedSignature(sb);
/*     */   }
/*     */ 
/*     */   
/*     */   public StringBuilder getErasedSignature(StringBuilder sb) {
/*  47 */     sb.append('$').append(this._ordinal + 1);
/*  48 */     return sb;
/*     */   }
/*     */ 
/*     */   
/*     */   public JavaType withTypeHandler(Object h) {
/*  53 */     return _unsupported();
/*     */   }
/*     */ 
/*     */   
/*     */   public JavaType withContentTypeHandler(Object h) {
/*  58 */     return _unsupported();
/*     */   }
/*     */ 
/*     */   
/*     */   public JavaType withValueHandler(Object h) {
/*  63 */     return _unsupported();
/*     */   }
/*     */ 
/*     */   
/*     */   public JavaType withContentValueHandler(Object h) {
/*  68 */     return _unsupported();
/*     */   }
/*     */ 
/*     */   
/*     */   public JavaType withContentType(JavaType contentType) {
/*  73 */     return _unsupported();
/*     */   }
/*     */ 
/*     */   
/*     */   public JavaType withStaticTyping() {
/*  78 */     return _unsupported();
/*     */   }
/*     */ 
/*     */   
/*     */   public JavaType refine(Class<?> rawType, TypeBindings bindings, JavaType superClass, JavaType[] superInterfaces) {
/*  83 */     return _unsupported();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected JavaType _narrow(Class<?> subclass) {
/*  89 */     return _unsupported();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isContainerType() {
/*  94 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  99 */     return getErasedSignature(new StringBuilder()).toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 104 */     return (o == this);
/*     */   }
/*     */   
/*     */   private <T> T _unsupported() {
/* 108 */     throw new UnsupportedOperationException("Operation should not be attempted on " + getClass().getName());
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\type\PlaceholderForType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */