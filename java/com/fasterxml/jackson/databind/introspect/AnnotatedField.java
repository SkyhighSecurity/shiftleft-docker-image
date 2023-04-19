/*     */ package com.fasterxml.jackson.databind.introspect;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.AnnotatedElement;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.lang.reflect.Type;
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
/*     */ public final class AnnotatedField
/*     */   extends AnnotatedMember
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final transient Field _field;
/*     */   protected Serialization _serialization;
/*     */   
/*     */   public AnnotatedField(TypeResolutionContext contextClass, Field field, AnnotationMap annMap) {
/*  39 */     super(contextClass, annMap);
/*  40 */     this._field = field;
/*     */   }
/*     */ 
/*     */   
/*     */   public AnnotatedField withAnnotations(AnnotationMap ann) {
/*  45 */     return new AnnotatedField(this._typeContext, this._field, ann);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AnnotatedField(Serialization ser) {
/*  53 */     super(null, null);
/*  54 */     this._field = null;
/*  55 */     this._serialization = ser;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Field getAnnotated() {
/*  65 */     return this._field;
/*     */   }
/*     */   public int getModifiers() {
/*  68 */     return this._field.getModifiers();
/*     */   }
/*     */   public String getName() {
/*  71 */     return this._field.getName();
/*     */   }
/*     */   
/*     */   public Class<?> getRawType() {
/*  75 */     return this._field.getType();
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public Type getGenericType() {
/*  81 */     return this._field.getGenericType();
/*     */   }
/*     */ 
/*     */   
/*     */   public JavaType getType() {
/*  86 */     return this._typeContext.resolveType(this._field.getGenericType());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?> getDeclaringClass() {
/*  96 */     return this._field.getDeclaringClass();
/*     */   }
/*     */   public Member getMember() {
/*  99 */     return this._field;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setValue(Object pojo, Object value) throws IllegalArgumentException {
/*     */     try {
/* 105 */       this._field.set(pojo, value);
/* 106 */     } catch (IllegalAccessException e) {
/* 107 */       throw new IllegalArgumentException("Failed to setValue() for field " + 
/* 108 */           getFullName() + ": " + e.getMessage(), e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getValue(Object pojo) throws IllegalArgumentException {
/*     */     try {
/* 116 */       return this._field.get(pojo);
/* 117 */     } catch (IllegalAccessException e) {
/* 118 */       throw new IllegalArgumentException("Failed to getValue() for field " + 
/* 119 */           getFullName() + ": " + e.getMessage(), e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getAnnotationCount() {
/* 129 */     return this._annotations.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isTransient() {
/* 134 */     return Modifier.isTransient(getModifiers());
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 138 */     return this._field.getName().hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 143 */     if (o == this) return true; 
/* 144 */     return (ClassUtil.hasClass(o, getClass()) && ((AnnotatedField)o)._field == this._field);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 150 */     return "[field " + getFullName() + "]";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Object writeReplace() {
/* 160 */     return new AnnotatedField(new Serialization(this._field));
/*     */   }
/*     */   
/*     */   Object readResolve() {
/* 164 */     Class<?> clazz = this._serialization.clazz;
/*     */     try {
/* 166 */       Field f = clazz.getDeclaredField(this._serialization.name);
/*     */       
/* 168 */       if (!f.isAccessible()) {
/* 169 */         ClassUtil.checkAndFixAccess(f, false);
/*     */       }
/* 171 */       return new AnnotatedField(null, f, null);
/* 172 */     } catch (Exception e) {
/* 173 */       throw new IllegalArgumentException("Could not find method '" + this._serialization.name + "' from Class '" + clazz
/* 174 */           .getName());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class Serialization
/*     */     implements Serializable
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */     protected Class<?> clazz;
/*     */     
/*     */     protected String name;
/*     */ 
/*     */     
/*     */     public Serialization(Field f) {
/* 191 */       this.clazz = f.getDeclaringClass();
/* 192 */       this.name = f.getName();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\introspect\AnnotatedField.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */