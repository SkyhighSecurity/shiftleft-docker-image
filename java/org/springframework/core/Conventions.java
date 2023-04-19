/*     */ package org.springframework.core;
/*     */ 
/*     */ import java.io.Externalizable;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
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
/*     */ public abstract class Conventions
/*     */ {
/*     */   private static final String PLURAL_SUFFIX = "List";
/*  55 */   private static final Set<Class<?>> IGNORED_INTERFACES = Collections.unmodifiableSet(new HashSet<Class<?>>(
/*  56 */         Arrays.asList(new Class[] { Serializable.class, Externalizable.class, Cloneable.class, Comparable.class })));
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
/*     */   public static String getVariableName(Object value) {
/*     */     Class<?> valueClass;
/*  76 */     Assert.notNull(value, "Value must not be null");
/*     */     
/*  78 */     boolean pluralize = false;
/*     */     
/*  80 */     if (value.getClass().isArray()) {
/*  81 */       valueClass = value.getClass().getComponentType();
/*  82 */       pluralize = true;
/*     */     }
/*  84 */     else if (value instanceof Collection) {
/*  85 */       Collection<?> collection = (Collection)value;
/*  86 */       if (collection.isEmpty()) {
/*  87 */         throw new IllegalArgumentException("Cannot generate variable name for an empty Collection");
/*     */       }
/*  89 */       Object valueToCheck = peekAhead(collection);
/*  90 */       valueClass = getClassForValue(valueToCheck);
/*  91 */       pluralize = true;
/*     */     } else {
/*     */       
/*  94 */       valueClass = getClassForValue(value);
/*     */     } 
/*     */     
/*  97 */     String name = ClassUtils.getShortNameAsProperty(valueClass);
/*  98 */     return pluralize ? pluralize(name) : name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getVariableNameForParameter(MethodParameter parameter) {
/*     */     Class<?> valueClass;
/* 108 */     Assert.notNull(parameter, "MethodParameter must not be null");
/*     */     
/* 110 */     boolean pluralize = false;
/*     */     
/* 112 */     if (parameter.getParameterType().isArray()) {
/* 113 */       valueClass = parameter.getParameterType().getComponentType();
/* 114 */       pluralize = true;
/*     */     }
/* 116 */     else if (Collection.class.isAssignableFrom(parameter.getParameterType())) {
/* 117 */       valueClass = ResolvableType.forMethodParameter(parameter).asCollection().resolveGeneric(new int[0]);
/* 118 */       if (valueClass == null) {
/* 119 */         throw new IllegalArgumentException("Cannot generate variable name for non-typed Collection parameter type");
/*     */       }
/*     */       
/* 122 */       pluralize = true;
/*     */     } else {
/*     */       
/* 125 */       valueClass = parameter.getParameterType();
/*     */     } 
/*     */     
/* 128 */     String name = ClassUtils.getShortNameAsProperty(valueClass);
/* 129 */     return pluralize ? pluralize(name) : name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getVariableNameForReturnType(Method method) {
/* 139 */     return getVariableNameForReturnType(method, method.getReturnType(), null);
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
/*     */   public static String getVariableNameForReturnType(Method method, Object value) {
/* 152 */     return getVariableNameForReturnType(method, method.getReturnType(), value);
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
/*     */   public static String getVariableNameForReturnType(Method method, Class<?> resolvedType, Object value) {
/*     */     Class<?> valueClass;
/* 166 */     Assert.notNull(method, "Method must not be null");
/*     */     
/* 168 */     if (Object.class == resolvedType) {
/* 169 */       if (value == null) {
/* 170 */         throw new IllegalArgumentException("Cannot generate variable name for an Object return type with null value");
/*     */       }
/* 172 */       return getVariableName(value);
/*     */     } 
/*     */ 
/*     */     
/* 176 */     boolean pluralize = false;
/*     */     
/* 178 */     if (resolvedType.isArray()) {
/* 179 */       valueClass = resolvedType.getComponentType();
/* 180 */       pluralize = true;
/*     */     }
/* 182 */     else if (Collection.class.isAssignableFrom(resolvedType)) {
/* 183 */       valueClass = ResolvableType.forMethodReturnType(method).asCollection().resolveGeneric(new int[0]);
/* 184 */       if (valueClass == null) {
/* 185 */         if (!(value instanceof Collection)) {
/* 186 */           throw new IllegalArgumentException("Cannot generate variable name for non-typed Collection return type and a non-Collection value");
/*     */         }
/*     */         
/* 189 */         Collection<?> collection = (Collection)value;
/* 190 */         if (collection.isEmpty()) {
/* 191 */           throw new IllegalArgumentException("Cannot generate variable name for non-typed Collection return type and an empty Collection value");
/*     */         }
/*     */         
/* 194 */         Object valueToCheck = peekAhead(collection);
/* 195 */         valueClass = getClassForValue(valueToCheck);
/*     */       } 
/* 197 */       pluralize = true;
/*     */     } else {
/*     */       
/* 200 */       valueClass = resolvedType;
/*     */     } 
/*     */     
/* 203 */     String name = ClassUtils.getShortNameAsProperty(valueClass);
/* 204 */     return pluralize ? pluralize(name) : name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String attributeNameToPropertyName(String attributeName) {
/* 213 */     Assert.notNull(attributeName, "'attributeName' must not be null");
/* 214 */     if (!attributeName.contains("-")) {
/* 215 */       return attributeName;
/*     */     }
/* 217 */     char[] chars = attributeName.toCharArray();
/* 218 */     char[] result = new char[chars.length - 1];
/* 219 */     int currPos = 0;
/* 220 */     boolean upperCaseNext = false;
/* 221 */     for (char c : chars) {
/* 222 */       if (c == '-') {
/* 223 */         upperCaseNext = true;
/*     */       }
/* 225 */       else if (upperCaseNext) {
/* 226 */         result[currPos++] = Character.toUpperCase(c);
/* 227 */         upperCaseNext = false;
/*     */       } else {
/*     */         
/* 230 */         result[currPos++] = c;
/*     */       } 
/*     */     } 
/* 233 */     return new String(result, 0, currPos);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getQualifiedAttributeName(Class<?> enclosingClass, String attributeName) {
/* 242 */     Assert.notNull(enclosingClass, "'enclosingClass' must not be null");
/* 243 */     Assert.notNull(attributeName, "'attributeName' must not be null");
/* 244 */     return enclosingClass.getName() + '.' + attributeName;
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
/*     */   private static Class<?> getClassForValue(Object value) {
/* 258 */     Class<?> valueClass = value.getClass();
/* 259 */     if (Proxy.isProxyClass(valueClass)) {
/* 260 */       Class<?>[] ifcs = valueClass.getInterfaces();
/* 261 */       for (Class<?> ifc : ifcs) {
/* 262 */         if (!IGNORED_INTERFACES.contains(ifc)) {
/* 263 */           return ifc;
/*     */         }
/*     */       }
/*     */     
/* 267 */     } else if (valueClass.getName().lastIndexOf('$') != -1 && valueClass.getDeclaringClass() == null) {
/*     */ 
/*     */       
/* 270 */       valueClass = valueClass.getSuperclass();
/*     */     } 
/* 272 */     return valueClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String pluralize(String name) {
/* 279 */     return name + "List";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <E> E peekAhead(Collection<E> collection) {
/* 288 */     Iterator<E> it = collection.iterator();
/* 289 */     if (!it.hasNext()) {
/* 290 */       throw new IllegalStateException("Unable to peek ahead in non-empty collection - no element found");
/*     */     }
/*     */     
/* 293 */     E value = it.next();
/* 294 */     if (value == null) {
/* 295 */       throw new IllegalStateException("Unable to peek ahead in non-empty collection - only null element found");
/*     */     }
/*     */     
/* 298 */     return value;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\Conventions.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */