/*     */ package com.fasterxml.jackson.databind.introspect;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*     */ import java.io.Serializable;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Member;
/*     */ import java.util.Collections;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AnnotatedMember
/*     */   extends Annotated
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final transient TypeResolutionContext _typeContext;
/*     */   protected final transient AnnotationMap _annotations;
/*     */   
/*     */   protected AnnotatedMember(TypeResolutionContext ctxt, AnnotationMap annotations) {
/*  37 */     this._typeContext = ctxt;
/*  38 */     this._annotations = annotations;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AnnotatedMember(AnnotatedMember base) {
/*  47 */     this._typeContext = base._typeContext;
/*  48 */     this._annotations = base._annotations;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract Annotated withAnnotations(AnnotationMap paramAnnotationMap);
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract Class<?> getDeclaringClass();
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract Member getMember();
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFullName() {
/*  67 */     return getDeclaringClass().getName() + "#" + getName();
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
/*     */   public TypeResolutionContext getTypeContext() {
/*  80 */     return this._typeContext;
/*     */   }
/*     */ 
/*     */   
/*     */   public final <A extends Annotation> A getAnnotation(Class<A> acls) {
/*  85 */     if (this._annotations == null) {
/*  86 */       return null;
/*     */     }
/*  88 */     return this._annotations.get(acls);
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean hasAnnotation(Class<?> acls) {
/*  93 */     if (this._annotations == null) {
/*  94 */       return false;
/*     */     }
/*  96 */     return this._annotations.has(acls);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasOneOf(Class<? extends Annotation>[] annoClasses) {
/* 101 */     if (this._annotations == null) {
/* 102 */       return false;
/*     */     }
/* 104 */     return this._annotations.hasOneOf(annoClasses);
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public Iterable<Annotation> annotations() {
/* 110 */     if (this._annotations == null) {
/* 111 */       return Collections.emptyList();
/*     */     }
/* 113 */     return this._annotations.annotations();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationMap getAllAnnotations() {
/* 121 */     return this._annotations;
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
/*     */   public final void fixAccess(boolean force) {
/* 137 */     Member m = getMember();
/* 138 */     if (m != null)
/* 139 */       ClassUtil.checkAndFixAccess(m, force); 
/*     */   }
/*     */   
/*     */   public abstract void setValue(Object paramObject1, Object paramObject2) throws UnsupportedOperationException, IllegalArgumentException;
/*     */   
/*     */   public abstract Object getValue(Object paramObject) throws UnsupportedOperationException, IllegalArgumentException;
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\introspect\AnnotatedMember.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */