/*     */ package com.fasterxml.jackson.databind.introspect;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.type.TypeFactory;
/*     */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AnnotatedMethodCollector
/*     */   extends CollectorBase
/*     */ {
/*     */   private final ClassIntrospector.MixInResolver _mixInResolver;
/*     */   
/*     */   AnnotatedMethodCollector(AnnotationIntrospector intr, ClassIntrospector.MixInResolver mixins) {
/*  22 */     super(intr);
/*  23 */     this._mixInResolver = (intr == null) ? null : mixins;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static AnnotatedMethodMap collectMethods(AnnotationIntrospector intr, TypeResolutionContext tc, ClassIntrospector.MixInResolver mixins, TypeFactory types, JavaType type, List<JavaType> superTypes, Class<?> primaryMixIn) {
/*  32 */     return (new AnnotatedMethodCollector(intr, mixins))
/*  33 */       .collect(types, tc, type, superTypes, primaryMixIn);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   AnnotatedMethodMap collect(TypeFactory typeFactory, TypeResolutionContext tc, JavaType mainType, List<JavaType> superTypes, Class<?> primaryMixIn) {
/*  39 */     Map<MemberKey, MethodBuilder> methods = new LinkedHashMap<>();
/*     */ 
/*     */     
/*  42 */     _addMemberMethods(tc, mainType.getRawClass(), methods, primaryMixIn);
/*     */ 
/*     */     
/*  45 */     for (JavaType type : superTypes) {
/*  46 */       Class<?> mixin = (this._mixInResolver == null) ? null : this._mixInResolver.findMixInClassFor(type.getRawClass());
/*  47 */       _addMemberMethods(new TypeResolutionContext.Basic(typeFactory, type
/*  48 */             .getBindings()), type
/*  49 */           .getRawClass(), methods, mixin);
/*     */     } 
/*     */     
/*  52 */     boolean checkJavaLangObject = false;
/*  53 */     if (this._mixInResolver != null) {
/*  54 */       Class<?> mixin = this._mixInResolver.findMixInClassFor(Object.class);
/*  55 */       if (mixin != null) {
/*  56 */         _addMethodMixIns(tc, mainType.getRawClass(), methods, mixin);
/*  57 */         checkJavaLangObject = true;
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  65 */     if (checkJavaLangObject && this._intr != null && !methods.isEmpty())
/*     */     {
/*  67 */       for (Map.Entry<MemberKey, MethodBuilder> entry : methods.entrySet()) {
/*  68 */         MemberKey k = entry.getKey();
/*  69 */         if (!"hashCode".equals(k.getName()) || 0 != k.argCount()) {
/*     */           continue;
/*     */         }
/*     */         
/*     */         try {
/*  74 */           Method m = Object.class.getDeclaredMethod(k.getName(), new Class[0]);
/*  75 */           if (m != null) {
/*  76 */             MethodBuilder b = entry.getValue();
/*  77 */             b.annotations = collectDefaultAnnotations(b.annotations, m
/*  78 */                 .getDeclaredAnnotations());
/*  79 */             b.method = m;
/*     */           } 
/*  81 */         } catch (Exception exception) {}
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*  86 */     if (methods.isEmpty()) {
/*  87 */       return new AnnotatedMethodMap();
/*     */     }
/*  89 */     Map<MemberKey, AnnotatedMethod> actual = new LinkedHashMap<>(methods.size());
/*  90 */     for (Map.Entry<MemberKey, MethodBuilder> entry : methods.entrySet()) {
/*  91 */       AnnotatedMethod am = ((MethodBuilder)entry.getValue()).build();
/*  92 */       if (am != null) {
/*  93 */         actual.put(entry.getKey(), am);
/*     */       }
/*     */     } 
/*  96 */     return new AnnotatedMethodMap(actual);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void _addMemberMethods(TypeResolutionContext tc, Class<?> cls, Map<MemberKey, MethodBuilder> methods, Class<?> mixInCls) {
/* 103 */     if (mixInCls != null) {
/* 104 */       _addMethodMixIns(tc, cls, methods, mixInCls);
/*     */     }
/* 106 */     if (cls == null) {
/*     */       return;
/*     */     }
/*     */     
/* 110 */     for (Method m : ClassUtil.getClassMethods(cls)) {
/* 111 */       if (_isIncludableMemberMethod(m)) {
/*     */ 
/*     */         
/* 114 */         MemberKey key = new MemberKey(m);
/* 115 */         MethodBuilder b = methods.get(key);
/* 116 */         if (b == null) {
/*     */           
/* 118 */           AnnotationCollector c = (this._intr == null) ? AnnotationCollector.emptyCollector() : collectAnnotations(m.getDeclaredAnnotations());
/* 119 */           methods.put(key, new MethodBuilder(tc, m, c));
/*     */         } else {
/* 121 */           if (this._intr != null) {
/* 122 */             b.annotations = collectDefaultAnnotations(b.annotations, m.getDeclaredAnnotations());
/*     */           }
/* 124 */           Method old = b.method;
/* 125 */           if (old == null) {
/* 126 */             b.method = m;
/*     */           }
/* 128 */           else if (Modifier.isAbstract(old.getModifiers()) && 
/* 129 */             !Modifier.isAbstract(m.getModifiers())) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 136 */             b.method = m;
/*     */ 
/*     */             
/* 139 */             b.typeContext = tc;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _addMethodMixIns(TypeResolutionContext tc, Class<?> targetClass, Map<MemberKey, MethodBuilder> methods, Class<?> mixInCls) {
/* 148 */     if (this._intr == null) {
/*     */       return;
/*     */     }
/* 151 */     for (Class<?> mixin : (Iterable<Class<?>>)ClassUtil.findRawSuperTypes(mixInCls, targetClass, true)) {
/* 152 */       for (Method m : ClassUtil.getDeclaredMethods(mixin)) {
/* 153 */         if (_isIncludableMemberMethod(m)) {
/*     */ 
/*     */           
/* 156 */           MemberKey key = new MemberKey(m);
/* 157 */           MethodBuilder b = methods.get(key);
/* 158 */           Annotation[] anns = m.getDeclaredAnnotations();
/* 159 */           if (b == null) {
/*     */ 
/*     */             
/* 162 */             methods.put(key, new MethodBuilder(tc, null, collectAnnotations(anns)));
/*     */           } else {
/* 164 */             b.annotations = collectDefaultAnnotations(b.annotations, anns);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean _isIncludableMemberMethod(Method m) {
/* 172 */     if (Modifier.isStatic(m.getModifiers()) || m
/*     */ 
/*     */       
/* 175 */       .isSynthetic() || m.isBridge()) {
/* 176 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 180 */     int pcount = (m.getParameterTypes()).length;
/* 181 */     return (pcount <= 2);
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class MethodBuilder
/*     */   {
/*     */     public TypeResolutionContext typeContext;
/*     */     
/*     */     public Method method;
/*     */     public AnnotationCollector annotations;
/*     */     
/*     */     public MethodBuilder(TypeResolutionContext tc, Method m, AnnotationCollector ann) {
/* 193 */       this.typeContext = tc;
/* 194 */       this.method = m;
/* 195 */       this.annotations = ann;
/*     */     }
/*     */     
/*     */     public AnnotatedMethod build() {
/* 199 */       if (this.method == null) {
/* 200 */         return null;
/*     */       }
/*     */ 
/*     */       
/* 204 */       return new AnnotatedMethod(this.typeContext, this.method, this.annotations.asAnnotationMap(), null);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\introspect\AnnotatedMethodCollector.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */