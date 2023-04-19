/*     */ package com.fasterxml.jackson.databind.introspect;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*     */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*     */ import java.lang.annotation.Annotation;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class CollectorBase
/*     */ {
/*  13 */   protected static final AnnotationMap[] NO_ANNOTATION_MAPS = new AnnotationMap[0];
/*  14 */   protected static final Annotation[] NO_ANNOTATIONS = new Annotation[0];
/*     */   
/*     */   protected final AnnotationIntrospector _intr;
/*     */   
/*     */   protected CollectorBase(AnnotationIntrospector intr) {
/*  19 */     this._intr = intr;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected final AnnotationCollector collectAnnotations(Annotation[] anns) {
/*  25 */     AnnotationCollector c = AnnotationCollector.emptyCollector();
/*  26 */     for (int i = 0, end = anns.length; i < end; i++) {
/*  27 */       Annotation ann = anns[i];
/*  28 */       c = c.addOrOverride(ann);
/*  29 */       if (this._intr.isAnnotationBundle(ann)) {
/*  30 */         c = collectFromBundle(c, ann);
/*     */       }
/*     */     } 
/*  33 */     return c;
/*     */   }
/*     */   
/*     */   protected final AnnotationCollector collectAnnotations(AnnotationCollector c, Annotation[] anns) {
/*  37 */     for (int i = 0, end = anns.length; i < end; i++) {
/*  38 */       Annotation ann = anns[i];
/*  39 */       c = c.addOrOverride(ann);
/*  40 */       if (this._intr.isAnnotationBundle(ann)) {
/*  41 */         c = collectFromBundle(c, ann);
/*     */       }
/*     */     } 
/*  44 */     return c;
/*     */   }
/*     */   
/*     */   protected final AnnotationCollector collectFromBundle(AnnotationCollector c, Annotation bundle) {
/*  48 */     Annotation[] anns = ClassUtil.findClassAnnotations(bundle.annotationType());
/*  49 */     for (int i = 0, end = anns.length; i < end; i++) {
/*  50 */       Annotation ann = anns[i];
/*     */       
/*  52 */       if (!_ignorableAnnotation(ann))
/*     */       {
/*     */         
/*  55 */         if (this._intr.isAnnotationBundle(ann)) {
/*     */           
/*  57 */           if (!c.isPresent(ann)) {
/*  58 */             c = c.addOrOverride(ann);
/*  59 */             c = collectFromBundle(c, ann);
/*     */           } 
/*     */         } else {
/*  62 */           c = c.addOrOverride(ann);
/*     */         }  } 
/*     */     } 
/*  65 */     return c;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final AnnotationCollector collectDefaultAnnotations(AnnotationCollector c, Annotation[] anns) {
/*  73 */     for (int i = 0, end = anns.length; i < end; i++) {
/*  74 */       Annotation ann = anns[i];
/*  75 */       if (!c.isPresent(ann)) {
/*  76 */         c = c.addOrOverride(ann);
/*  77 */         if (this._intr.isAnnotationBundle(ann)) {
/*  78 */           c = collectDefaultFromBundle(c, ann);
/*     */         }
/*     */       } 
/*     */     } 
/*  82 */     return c;
/*     */   }
/*     */ 
/*     */   
/*     */   protected final AnnotationCollector collectDefaultFromBundle(AnnotationCollector c, Annotation bundle) {
/*  87 */     Annotation[] anns = ClassUtil.findClassAnnotations(bundle.annotationType());
/*  88 */     for (int i = 0, end = anns.length; i < end; i++) {
/*  89 */       Annotation ann = anns[i];
/*     */       
/*  91 */       if (!_ignorableAnnotation(ann))
/*     */       {
/*     */ 
/*     */         
/*  95 */         if (!c.isPresent(ann)) {
/*  96 */           c = c.addOrOverride(ann);
/*  97 */           if (this._intr.isAnnotationBundle(ann))
/*  98 */             c = collectFromBundle(c, ann); 
/*     */         } 
/*     */       }
/*     */     } 
/* 102 */     return c;
/*     */   }
/*     */   
/*     */   protected static final boolean _ignorableAnnotation(Annotation a) {
/* 106 */     return (a instanceof java.lang.annotation.Target || a instanceof java.lang.annotation.Retention);
/*     */   }
/*     */   
/*     */   static AnnotationMap _emptyAnnotationMap() {
/* 110 */     return new AnnotationMap();
/*     */   }
/*     */   
/*     */   static AnnotationMap[] _emptyAnnotationMaps(int count) {
/* 114 */     if (count == 0) {
/* 115 */       return NO_ANNOTATION_MAPS;
/*     */     }
/* 117 */     AnnotationMap[] maps = new AnnotationMap[count];
/* 118 */     for (int i = 0; i < count; i++) {
/* 119 */       maps[i] = _emptyAnnotationMap();
/*     */     }
/* 121 */     return maps;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\introspect\CollectorBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */