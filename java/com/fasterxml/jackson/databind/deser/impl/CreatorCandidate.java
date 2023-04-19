/*     */ package com.fasterxml.jackson.databind.deser.impl;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JacksonInject;
/*     */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*     */ import com.fasterxml.jackson.databind.PropertyName;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
/*     */ import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
/*     */ 
/*     */ public final class CreatorCandidate
/*     */ {
/*     */   protected final AnnotationIntrospector _intr;
/*     */   protected final AnnotatedWithParams _creator;
/*     */   protected final int _paramCount;
/*     */   protected final Param[] _params;
/*     */   
/*     */   protected CreatorCandidate(AnnotationIntrospector intr, AnnotatedWithParams ct, Param[] params, int count) {
/*  19 */     this._intr = intr;
/*  20 */     this._creator = ct;
/*  21 */     this._params = params;
/*  22 */     this._paramCount = count;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static CreatorCandidate construct(AnnotationIntrospector intr, AnnotatedWithParams creator, BeanPropertyDefinition[] propDefs) {
/*  28 */     int pcount = creator.getParameterCount();
/*  29 */     Param[] params = new Param[pcount];
/*  30 */     for (int i = 0; i < pcount; i++) {
/*  31 */       AnnotatedParameter annParam = creator.getParameter(i);
/*  32 */       JacksonInject.Value injectId = intr.findInjectableValue((AnnotatedMember)annParam);
/*  33 */       params[i] = new Param(annParam, (propDefs == null) ? null : propDefs[i], injectId);
/*     */     } 
/*  35 */     return new CreatorCandidate(intr, creator, params, pcount);
/*     */   }
/*     */   
/*  38 */   public AnnotatedWithParams creator() { return this._creator; }
/*  39 */   public int paramCount() { return this._paramCount; }
/*  40 */   public JacksonInject.Value injection(int i) { return (this._params[i]).injection; }
/*  41 */   public AnnotatedParameter parameter(int i) { return (this._params[i]).annotated; } public BeanPropertyDefinition propertyDef(int i) {
/*  42 */     return (this._params[i]).propDef;
/*     */   }
/*     */   public PropertyName paramName(int i) {
/*  45 */     BeanPropertyDefinition propDef = (this._params[i]).propDef;
/*  46 */     if (propDef != null) {
/*  47 */       return propDef.getFullName();
/*     */     }
/*  49 */     return null;
/*     */   }
/*     */   
/*     */   public PropertyName explicitParamName(int i) {
/*  53 */     BeanPropertyDefinition propDef = (this._params[i]).propDef;
/*  54 */     if (propDef != null && 
/*  55 */       propDef.isExplicitlyNamed()) {
/*  56 */       return propDef.getFullName();
/*     */     }
/*     */     
/*  59 */     return null;
/*     */   }
/*     */   
/*     */   public PropertyName findImplicitParamName(int i) {
/*  63 */     String str = this._intr.findImplicitPropertyName((AnnotatedMember)(this._params[i]).annotated);
/*  64 */     if (str != null && !str.isEmpty()) {
/*  65 */       return PropertyName.construct(str);
/*     */     }
/*  67 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int findOnlyParamWithoutInjection() {
/*  77 */     int missing = -1;
/*  78 */     for (int i = 0; i < this._paramCount; i++) {
/*  79 */       if ((this._params[i]).injection == null) {
/*  80 */         if (missing >= 0) {
/*  81 */           return -1;
/*     */         }
/*  83 */         missing = i;
/*     */       } 
/*     */     } 
/*  86 */     return missing;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  91 */     return this._creator.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public static final class Param
/*     */   {
/*     */     public final AnnotatedParameter annotated;
/*     */     public final BeanPropertyDefinition propDef;
/*     */     public final JacksonInject.Value injection;
/*     */     
/*     */     public Param(AnnotatedParameter p, BeanPropertyDefinition pd, JacksonInject.Value i) {
/* 102 */       this.annotated = p;
/* 103 */       this.propDef = pd;
/* 104 */       this.injection = i;
/*     */     }
/*     */     
/*     */     public PropertyName fullName() {
/* 108 */       if (this.propDef == null) {
/* 109 */         return null;
/*     */       }
/* 111 */       return this.propDef.getFullName();
/*     */     }
/*     */     
/*     */     public boolean hasFullName() {
/* 115 */       if (this.propDef == null) {
/* 116 */         return false;
/*     */       }
/* 118 */       PropertyName n = this.propDef.getFullName();
/* 119 */       return n.hasSimpleName();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\deser\impl\CreatorCandidate.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */