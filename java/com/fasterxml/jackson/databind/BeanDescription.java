/*     */ package com.fasterxml.jackson.databind;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonFormat;
/*     */ import com.fasterxml.jackson.annotation.JsonInclude;
/*     */ import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
/*     */ import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
/*     */ import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
/*     */ import com.fasterxml.jackson.databind.type.TypeBindings;
/*     */ import com.fasterxml.jackson.databind.util.Annotations;
/*     */ import com.fasterxml.jackson.databind.util.Converter;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
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
/*     */ public abstract class BeanDescription
/*     */ {
/*     */   protected final JavaType _type;
/*     */   
/*     */   protected BeanDescription(JavaType type) {
/*  37 */     this._type = type;
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
/*     */   public JavaType getType() {
/*  50 */     return this._type;
/*     */   } public Class<?> getBeanClass() {
/*  52 */     return this._type.getRawClass();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isNonStaticInnerClass() {
/*  58 */     return getClassInfo().isNonStaticInnerClass();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract AnnotatedClass getClassInfo();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract ObjectIdInfo getObjectIdInfo();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract boolean hasKnownClassAnnotations();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public abstract TypeBindings bindingsForBeanType();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public abstract JavaType resolveType(Type paramType);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract Annotations getClassAnnotations();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract List<BeanPropertyDefinition> findProperties();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract Set<String> getIgnoredPropertyNames();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract List<BeanPropertyDefinition> findBackReferences();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public abstract Map<String, AnnotatedMember> findBackReferenceProperties();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract List<AnnotatedConstructor> getConstructors();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract List<AnnotatedMethod> getFactoryMethods();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract AnnotatedConstructor findDefaultConstructor();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract Constructor<?> findSingleArgConstructor(Class<?>... paramVarArgs);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract Method findFactoryMethod(Class<?>... paramVarArgs);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract AnnotatedMember findJsonValueAccessor();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract AnnotatedMember findAnyGetter();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract AnnotatedMember findAnySetterAccessor();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract AnnotatedMethod findMethod(String paramString, Class<?>[] paramArrayOfClass);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public abstract AnnotatedMethod findJsonValueMethod();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public AnnotatedMethod findAnySetter() {
/* 213 */     AnnotatedMember m = findAnySetterAccessor();
/* 214 */     if (m instanceof AnnotatedMethod) {
/* 215 */       return (AnnotatedMethod)m;
/*     */     }
/* 217 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public AnnotatedMember findAnySetterField() {
/* 225 */     AnnotatedMember m = findAnySetterAccessor();
/* 226 */     if (m instanceof com.fasterxml.jackson.databind.introspect.AnnotatedField) {
/* 227 */       return m;
/*     */     }
/* 229 */     return null;
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
/*     */   public abstract JsonInclude.Value findPropertyInclusion(JsonInclude.Value paramValue);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract JsonFormat.Value findExpectedFormat(JsonFormat.Value paramValue);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract Converter<Object, Object> findSerializationConverter();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract Converter<Object, Object> findDeserializationConverter();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String findClassDescription() {
/* 280 */     return null;
/*     */   }
/*     */   
/*     */   public abstract Map<Object, AnnotatedMember> findInjectables();
/*     */   
/*     */   public abstract Class<?> findPOJOBuilder();
/*     */   
/*     */   public abstract JsonPOJOBuilder.Value findPOJOBuilderConfig();
/*     */   
/*     */   public abstract Object instantiateBean(boolean paramBoolean);
/*     */   
/*     */   public abstract Class<?>[] findDefaultViews();
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\BeanDescription.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */