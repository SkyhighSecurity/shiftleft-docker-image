/*     */ package org.springframework.core.convert.support;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.Collections;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.springframework.core.convert.ConversionFailedException;
/*     */ import org.springframework.core.convert.TypeDescriptor;
/*     */ import org.springframework.core.convert.converter.ConditionalGenericConverter;
/*     */ import org.springframework.core.convert.converter.GenericConverter;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ConcurrentReferenceHashMap;
/*     */ import org.springframework.util.ReflectionUtils;
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
/*     */ final class ObjectToObjectConverter
/*     */   implements ConditionalGenericConverter
/*     */ {
/*  68 */   private static final Map<Class<?>, Member> conversionMemberCache = (Map<Class<?>, Member>)new ConcurrentReferenceHashMap(32);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<GenericConverter.ConvertiblePair> getConvertibleTypes() {
/*  74 */     return Collections.singleton(new GenericConverter.ConvertiblePair(Object.class, Object.class));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
/*  79 */     return (sourceType.getType() != targetType.getType() && 
/*  80 */       hasConversionMethodOrConstructor(targetType.getType(), sourceType.getType()));
/*     */   }
/*     */ 
/*     */   
/*     */   public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
/*  85 */     if (source == null) {
/*  86 */       return null;
/*     */     }
/*  88 */     Class<?> sourceClass = sourceType.getType();
/*  89 */     Class<?> targetClass = targetType.getType();
/*  90 */     Member member = getValidatedMember(targetClass, sourceClass);
/*     */     
/*     */     try {
/*  93 */       if (member instanceof Method) {
/*  94 */         Method method = (Method)member;
/*  95 */         ReflectionUtils.makeAccessible(method);
/*  96 */         if (!Modifier.isStatic(method.getModifiers())) {
/*  97 */           return method.invoke(source, new Object[0]);
/*     */         }
/*     */         
/* 100 */         return method.invoke(null, new Object[] { source });
/*     */       } 
/*     */       
/* 103 */       if (member instanceof Constructor) {
/* 104 */         Constructor<?> ctor = (Constructor)member;
/* 105 */         ReflectionUtils.makeAccessible(ctor);
/* 106 */         return ctor.newInstance(new Object[] { source });
/*     */       }
/*     */     
/* 109 */     } catch (InvocationTargetException ex) {
/* 110 */       throw new ConversionFailedException(sourceType, targetType, source, ex.getTargetException());
/*     */     }
/* 112 */     catch (Throwable ex) {
/* 113 */       throw new ConversionFailedException(sourceType, targetType, source, ex);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 119 */     throw new IllegalStateException(String.format("No to%3$s() method exists on %1$s, and no static valueOf/of/from(%1$s) method or %3$s(%1$s) constructor exists on %2$s.", new Object[] { sourceClass
/*     */             
/* 121 */             .getName(), targetClass.getName(), targetClass.getSimpleName() }));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean hasConversionMethodOrConstructor(Class<?> targetClass, Class<?> sourceClass) {
/* 127 */     return (getValidatedMember(targetClass, sourceClass) != null);
/*     */   }
/*     */   
/*     */   private static Member getValidatedMember(Class<?> targetClass, Class<?> sourceClass) {
/* 131 */     Member<?> member = conversionMemberCache.get(targetClass);
/* 132 */     if (isApplicable(member, sourceClass)) {
/* 133 */       return member;
/*     */     }
/*     */     
/* 136 */     member = determineToMethod(targetClass, sourceClass);
/* 137 */     if (member == null) {
/* 138 */       member = determineFactoryMethod(targetClass, sourceClass);
/* 139 */       if (member == null) {
/* 140 */         member = determineFactoryConstructor(targetClass, sourceClass);
/* 141 */         if (member == null) {
/* 142 */           return null;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 147 */     conversionMemberCache.put(targetClass, member);
/* 148 */     return member;
/*     */   }
/*     */   
/*     */   private static boolean isApplicable(Member member, Class<?> sourceClass) {
/* 152 */     if (member instanceof Method) {
/* 153 */       Method method = (Method)member;
/* 154 */       return !Modifier.isStatic(method.getModifiers()) ? 
/* 155 */         ClassUtils.isAssignable(method.getDeclaringClass(), sourceClass) : (
/* 156 */         (method.getParameterTypes()[0] == sourceClass));
/*     */     } 
/* 158 */     if (member instanceof Constructor) {
/* 159 */       Constructor<?> ctor = (Constructor)member;
/* 160 */       return (ctor.getParameterTypes()[0] == sourceClass);
/*     */     } 
/*     */     
/* 163 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private static Method determineToMethod(Class<?> targetClass, Class<?> sourceClass) {
/* 168 */     if (String.class == targetClass || String.class == sourceClass)
/*     */     {
/* 170 */       return null;
/*     */     }
/*     */     
/* 173 */     Method method = ClassUtils.getMethodIfAvailable(sourceClass, "to" + targetClass.getSimpleName(), new Class[0]);
/* 174 */     return (method != null && !Modifier.isStatic(method.getModifiers()) && 
/* 175 */       ClassUtils.isAssignable(targetClass, method.getReturnType())) ? method : null;
/*     */   }
/*     */   
/*     */   private static Method determineFactoryMethod(Class<?> targetClass, Class<?> sourceClass) {
/* 179 */     if (String.class == targetClass)
/*     */     {
/* 181 */       return null;
/*     */     }
/*     */     
/* 184 */     Method method = ClassUtils.getStaticMethod(targetClass, "valueOf", new Class[] { sourceClass });
/* 185 */     if (method == null) {
/* 186 */       method = ClassUtils.getStaticMethod(targetClass, "of", new Class[] { sourceClass });
/* 187 */       if (method == null) {
/* 188 */         method = ClassUtils.getStaticMethod(targetClass, "from", new Class[] { sourceClass });
/*     */       }
/*     */     } 
/* 191 */     return method;
/*     */   }
/*     */   
/*     */   private static Constructor<?> determineFactoryConstructor(Class<?> targetClass, Class<?> sourceClass) {
/* 195 */     return ClassUtils.getConstructorIfAvailable(targetClass, new Class[] { sourceClass });
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\convert\support\ObjectToObjectConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */