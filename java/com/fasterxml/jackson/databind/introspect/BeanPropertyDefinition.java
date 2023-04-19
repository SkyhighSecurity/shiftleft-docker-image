/*     */ package com.fasterxml.jackson.databind.introspect;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonInclude;
/*     */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.PropertyMetadata;
/*     */ import com.fasterxml.jackson.databind.PropertyName;
/*     */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*     */ import com.fasterxml.jackson.databind.util.Named;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class BeanPropertyDefinition
/*     */   implements Named
/*     */ {
/*  23 */   protected static final JsonInclude.Value EMPTY_INCLUDE = JsonInclude.Value.empty();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract BeanPropertyDefinition withName(PropertyName paramPropertyName);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract BeanPropertyDefinition withSimpleName(String paramString);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract String getName();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract PropertyName getFullName();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasName(PropertyName name) {
/*  67 */     return getFullName().equals(name);
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
/*     */   public abstract String getInternalName();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract PropertyName getWrapperName();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract boolean isExplicitlyIncluded();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isExplicitlyNamed() {
/* 108 */     return isExplicitlyIncluded();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract JavaType getPrimaryType();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract Class<?> getRawPrimaryType();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract PropertyMetadata getMetadata();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isRequired() {
/* 143 */     return getMetadata().isRequired();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean couldDeserialize() {
/* 152 */     return (getMutator() != null); } public boolean couldSerialize() {
/* 153 */     return (getAccessor() != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public abstract boolean hasGetter();
/*     */ 
/*     */   
/*     */   public abstract boolean hasSetter();
/*     */ 
/*     */   
/*     */   public abstract boolean hasField();
/*     */ 
/*     */   
/*     */   public abstract boolean hasConstructorParameter();
/*     */ 
/*     */   
/*     */   public abstract AnnotatedMethod getGetter();
/*     */   
/*     */   public abstract AnnotatedMethod getSetter();
/*     */   
/*     */   public abstract AnnotatedField getField();
/*     */   
/*     */   public abstract AnnotatedParameter getConstructorParameter();
/*     */   
/*     */   public Iterator<AnnotatedParameter> getConstructorParameters() {
/* 178 */     return ClassUtil.emptyIterator();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotatedMember getAccessor() {
/* 188 */     AnnotatedMember m = getGetter();
/* 189 */     if (m == null) {
/* 190 */       m = getField();
/*     */     }
/* 192 */     return m;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotatedMember getMutator() {
/* 201 */     AnnotatedMember acc = getConstructorParameter();
/* 202 */     if (acc == null) {
/* 203 */       acc = getSetter();
/* 204 */       if (acc == null) {
/* 205 */         acc = getField();
/*     */       }
/*     */     } 
/* 208 */     return acc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotatedMember getNonConstructorMutator() {
/* 215 */     AnnotatedMember m = getSetter();
/* 216 */     if (m == null) {
/* 217 */       m = getField();
/*     */     }
/* 219 */     return m;
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
/*     */   public abstract AnnotatedMember getPrimaryMember();
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
/*     */   public Class<?>[] findViews() {
/* 245 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationIntrospector.ReferenceProperty findReferenceType() {
/* 251 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String findReferenceName() {
/* 257 */     AnnotationIntrospector.ReferenceProperty ref = findReferenceType();
/* 258 */     return (ref == null) ? null : ref.getName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isTypeId() {
/* 266 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjectIdInfo findObjectIdInfo() {
/* 273 */     return null;
/*     */   }
/*     */   
/*     */   public abstract JsonInclude.Value findInclusion();
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\introspect\BeanPropertyDefinition.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */