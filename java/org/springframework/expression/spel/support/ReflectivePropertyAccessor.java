/*     */ package org.springframework.expression.spel.support;
/*     */ 
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.springframework.asm.MethodVisitor;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.core.convert.Property;
/*     */ import org.springframework.core.convert.TypeDescriptor;
/*     */ import org.springframework.expression.AccessException;
/*     */ import org.springframework.expression.EvaluationContext;
/*     */ import org.springframework.expression.EvaluationException;
/*     */ import org.springframework.expression.PropertyAccessor;
/*     */ import org.springframework.expression.TypedValue;
/*     */ import org.springframework.expression.spel.CodeFlow;
/*     */ import org.springframework.expression.spel.CompilablePropertyAccessor;
/*     */ import org.springframework.util.ReflectionUtils;
/*     */ import org.springframework.util.StringUtils;
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
/*     */ public class ReflectivePropertyAccessor
/*     */   implements PropertyAccessor
/*     */ {
/*  63 */   private static final Set<Class<?>> ANY_TYPES = Collections.emptySet();
/*     */   private static final Set<Class<?>> BOOLEAN_TYPES;
/*     */   private final boolean allowWrite;
/*     */   
/*     */   static {
/*  68 */     Set<Class<?>> booleanTypes = new HashSet<Class<?>>(4);
/*  69 */     booleanTypes.add(Boolean.class);
/*  70 */     booleanTypes.add(boolean.class);
/*  71 */     BOOLEAN_TYPES = Collections.unmodifiableSet(booleanTypes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  77 */   private final Map<PropertyCacheKey, InvokerPair> readerCache = new ConcurrentHashMap<PropertyCacheKey, InvokerPair>(64);
/*     */ 
/*     */   
/*  80 */   private final Map<PropertyCacheKey, Member> writerCache = new ConcurrentHashMap<PropertyCacheKey, Member>(64);
/*     */ 
/*     */   
/*  83 */   private final Map<PropertyCacheKey, TypeDescriptor> typeDescriptorCache = new ConcurrentHashMap<PropertyCacheKey, TypeDescriptor>(64);
/*     */ 
/*     */   
/*  86 */   private final Map<Class<?>, Method[]> sortedMethodsCache = (Map)new ConcurrentHashMap<Class<?>, Method>(64);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private volatile InvokerPair lastReadInvokerPair;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ReflectivePropertyAccessor() {
/*  97 */     this.allowWrite = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ReflectivePropertyAccessor(boolean allowWrite) {
/* 107 */     this.allowWrite = allowWrite;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?>[] getSpecificTargetClasses() {
/* 116 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canRead(EvaluationContext context, Object target, String name) throws AccessException {
/* 121 */     if (target == null) {
/* 122 */       return false;
/*     */     }
/*     */     
/* 125 */     Class<?> type = (target instanceof Class) ? (Class)target : target.getClass();
/* 126 */     if (type.isArray() && name.equals("length")) {
/* 127 */       return true;
/*     */     }
/*     */     
/* 130 */     PropertyCacheKey cacheKey = new PropertyCacheKey(type, name, target instanceof Class);
/* 131 */     if (this.readerCache.containsKey(cacheKey)) {
/* 132 */       return true;
/*     */     }
/*     */     
/* 135 */     Method method = findGetterForProperty(name, type, target);
/* 136 */     if (method != null) {
/*     */ 
/*     */       
/* 139 */       Property property = new Property(type, method, null);
/* 140 */       TypeDescriptor typeDescriptor = new TypeDescriptor(property);
/* 141 */       this.readerCache.put(cacheKey, new InvokerPair(method, typeDescriptor));
/* 142 */       this.typeDescriptorCache.put(cacheKey, typeDescriptor);
/* 143 */       return true;
/*     */     } 
/*     */     
/* 146 */     Field field = findField(name, type, target);
/* 147 */     if (field != null) {
/* 148 */       TypeDescriptor typeDescriptor = new TypeDescriptor(field);
/* 149 */       this.readerCache.put(cacheKey, new InvokerPair(field, typeDescriptor));
/* 150 */       this.typeDescriptorCache.put(cacheKey, typeDescriptor);
/* 151 */       return true;
/*     */     } 
/*     */ 
/*     */     
/* 155 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public TypedValue read(EvaluationContext context, Object target, String name) throws AccessException {
/* 160 */     if (target == null) {
/* 161 */       throw new AccessException("Cannot read property of null target");
/*     */     }
/* 163 */     Class<?> type = (target instanceof Class) ? (Class)target : target.getClass();
/*     */     
/* 165 */     if (type.isArray() && name.equals("length")) {
/* 166 */       if (target instanceof Class) {
/* 167 */         throw new AccessException("Cannot access length on array class itself");
/*     */       }
/* 169 */       return new TypedValue(Integer.valueOf(Array.getLength(target)));
/*     */     } 
/*     */     
/* 172 */     PropertyCacheKey cacheKey = new PropertyCacheKey(type, name, target instanceof Class);
/* 173 */     InvokerPair invoker = this.readerCache.get(cacheKey);
/* 174 */     this.lastReadInvokerPair = invoker;
/*     */     
/* 176 */     if (invoker == null || invoker.member instanceof Method) {
/* 177 */       Method method = (invoker != null) ? (Method)invoker.member : null;
/* 178 */       if (method == null) {
/* 179 */         method = findGetterForProperty(name, type, target);
/* 180 */         if (method != null) {
/*     */ 
/*     */           
/* 183 */           Property property = new Property(type, method, null);
/* 184 */           TypeDescriptor typeDescriptor = new TypeDescriptor(property);
/* 185 */           invoker = new InvokerPair(method, typeDescriptor);
/* 186 */           this.lastReadInvokerPair = invoker;
/* 187 */           this.readerCache.put(cacheKey, invoker);
/*     */         } 
/*     */       } 
/* 190 */       if (method != null) {
/*     */         try {
/* 192 */           ReflectionUtils.makeAccessible(method);
/* 193 */           Object value = method.invoke(target, new Object[0]);
/* 194 */           return new TypedValue(value, invoker.typeDescriptor.narrow(value));
/*     */         }
/* 196 */         catch (Exception ex) {
/* 197 */           throw new AccessException("Unable to access property '" + name + "' through getter method", ex);
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 202 */     if (invoker == null || invoker.member instanceof Field) {
/* 203 */       Field field = (invoker == null) ? null : (Field)invoker.member;
/* 204 */       if (field == null) {
/* 205 */         field = findField(name, type, target);
/* 206 */         if (field != null) {
/* 207 */           invoker = new InvokerPair(field, new TypeDescriptor(field));
/* 208 */           this.lastReadInvokerPair = invoker;
/* 209 */           this.readerCache.put(cacheKey, invoker);
/*     */         } 
/*     */       } 
/* 212 */       if (field != null) {
/*     */         try {
/* 214 */           ReflectionUtils.makeAccessible(field);
/* 215 */           Object value = field.get(target);
/* 216 */           return new TypedValue(value, invoker.typeDescriptor.narrow(value));
/*     */         }
/* 218 */         catch (Exception ex) {
/* 219 */           throw new AccessException("Unable to access field '" + name + "'", ex);
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 224 */     throw new AccessException("Neither getter method nor field found for property '" + name + "'");
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canWrite(EvaluationContext context, Object target, String name) throws AccessException {
/* 229 */     if (!this.allowWrite || target == null) {
/* 230 */       return false;
/*     */     }
/*     */     
/* 233 */     Class<?> type = (target instanceof Class) ? (Class)target : target.getClass();
/* 234 */     PropertyCacheKey cacheKey = new PropertyCacheKey(type, name, target instanceof Class);
/* 235 */     if (this.writerCache.containsKey(cacheKey)) {
/* 236 */       return true;
/*     */     }
/*     */     
/* 239 */     Method method = findSetterForProperty(name, type, target);
/* 240 */     if (method != null) {
/*     */       
/* 242 */       Property property = new Property(type, null, method);
/* 243 */       TypeDescriptor typeDescriptor = new TypeDescriptor(property);
/* 244 */       this.writerCache.put(cacheKey, method);
/* 245 */       this.typeDescriptorCache.put(cacheKey, typeDescriptor);
/* 246 */       return true;
/*     */     } 
/*     */     
/* 249 */     Field field = findField(name, type, target);
/* 250 */     if (field != null) {
/* 251 */       this.writerCache.put(cacheKey, field);
/* 252 */       this.typeDescriptorCache.put(cacheKey, new TypeDescriptor(field));
/* 253 */       return true;
/*     */     } 
/*     */ 
/*     */     
/* 257 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(EvaluationContext context, Object target, String name, Object newValue) throws AccessException {
/* 262 */     if (!this.allowWrite) {
/* 263 */       throw new AccessException("PropertyAccessor for property '" + name + "' on target [" + target + "] does not allow write operations");
/*     */     }
/*     */ 
/*     */     
/* 267 */     if (target == null) {
/* 268 */       throw new AccessException("Cannot write property on null target");
/*     */     }
/* 270 */     Class<?> type = (target instanceof Class) ? (Class)target : target.getClass();
/*     */     
/* 272 */     Object possiblyConvertedNewValue = newValue;
/* 273 */     TypeDescriptor typeDescriptor = getTypeDescriptor(context, target, name);
/* 274 */     if (typeDescriptor != null) {
/*     */       try {
/* 276 */         possiblyConvertedNewValue = context.getTypeConverter().convertValue(newValue, 
/* 277 */             TypeDescriptor.forObject(newValue), typeDescriptor);
/*     */       }
/* 279 */       catch (EvaluationException evaluationException) {
/* 280 */         throw new AccessException("Type conversion failure", evaluationException);
/*     */       } 
/*     */     }
/*     */     
/* 284 */     PropertyCacheKey cacheKey = new PropertyCacheKey(type, name, target instanceof Class);
/* 285 */     Member cachedMember = this.writerCache.get(cacheKey);
/*     */     
/* 287 */     if (cachedMember == null || cachedMember instanceof Method) {
/* 288 */       Method method = (Method)cachedMember;
/* 289 */       if (method == null) {
/* 290 */         method = findSetterForProperty(name, type, target);
/* 291 */         if (method != null) {
/* 292 */           cachedMember = method;
/* 293 */           this.writerCache.put(cacheKey, cachedMember);
/*     */         } 
/*     */       } 
/* 296 */       if (method != null) {
/*     */         try {
/* 298 */           ReflectionUtils.makeAccessible(method);
/* 299 */           method.invoke(target, new Object[] { possiblyConvertedNewValue });
/*     */           
/*     */           return;
/* 302 */         } catch (Exception ex) {
/* 303 */           throw new AccessException("Unable to access property '" + name + "' through setter method", ex);
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 308 */     if (cachedMember == null || cachedMember instanceof Field) {
/* 309 */       Field field = (Field)cachedMember;
/* 310 */       if (field == null) {
/* 311 */         field = findField(name, type, target);
/* 312 */         if (field != null) {
/* 313 */           cachedMember = field;
/* 314 */           this.writerCache.put(cacheKey, cachedMember);
/*     */         } 
/*     */       } 
/* 317 */       if (field != null) {
/*     */         try {
/* 319 */           ReflectionUtils.makeAccessible(field);
/* 320 */           field.set(target, possiblyConvertedNewValue);
/*     */           
/*     */           return;
/* 323 */         } catch (Exception ex) {
/* 324 */           throw new AccessException("Unable to access field '" + name + "'", ex);
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 329 */     throw new AccessException("Neither setter method nor field found for property '" + name + "'");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public Member getLastReadInvokerPair() {
/* 337 */     InvokerPair lastReadInvoker = this.lastReadInvokerPair;
/* 338 */     return (lastReadInvoker != null) ? lastReadInvoker.member : null;
/*     */   }
/*     */ 
/*     */   
/*     */   private TypeDescriptor getTypeDescriptor(EvaluationContext context, Object target, String name) {
/* 343 */     if (target == null) {
/* 344 */       return null;
/*     */     }
/* 346 */     Class<?> type = (target instanceof Class) ? (Class)target : target.getClass();
/*     */     
/* 348 */     if (type.isArray() && name.equals("length")) {
/* 349 */       return TypeDescriptor.valueOf(int.class);
/*     */     }
/* 351 */     PropertyCacheKey cacheKey = new PropertyCacheKey(type, name, target instanceof Class);
/* 352 */     TypeDescriptor typeDescriptor = this.typeDescriptorCache.get(cacheKey);
/* 353 */     if (typeDescriptor == null) {
/*     */       
/*     */       try {
/* 356 */         if (canRead(context, target, name) || canWrite(context, target, name)) {
/* 357 */           typeDescriptor = this.typeDescriptorCache.get(cacheKey);
/*     */         }
/*     */       }
/* 360 */       catch (AccessException accessException) {}
/*     */     }
/*     */ 
/*     */     
/* 364 */     return typeDescriptor;
/*     */   }
/*     */   
/*     */   private Method findGetterForProperty(String propertyName, Class<?> clazz, Object target) {
/* 368 */     Method method = findGetterForProperty(propertyName, clazz, target instanceof Class);
/* 369 */     if (method == null && target instanceof Class) {
/* 370 */       method = findGetterForProperty(propertyName, target.getClass(), false);
/*     */     }
/* 372 */     return method;
/*     */   }
/*     */   
/*     */   private Method findSetterForProperty(String propertyName, Class<?> clazz, Object target) {
/* 376 */     Method method = findSetterForProperty(propertyName, clazz, target instanceof Class);
/* 377 */     if (method == null && target instanceof Class) {
/* 378 */       method = findSetterForProperty(propertyName, target.getClass(), false);
/*     */     }
/* 380 */     return method;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Method findGetterForProperty(String propertyName, Class<?> clazz, boolean mustBeStatic) {
/* 387 */     Method method = findMethodForProperty(getPropertyMethodSuffixes(propertyName), "get", clazz, mustBeStatic, 0, ANY_TYPES);
/*     */     
/* 389 */     if (method == null) {
/* 390 */       method = findMethodForProperty(getPropertyMethodSuffixes(propertyName), "is", clazz, mustBeStatic, 0, BOOLEAN_TYPES);
/*     */     }
/*     */     
/* 393 */     return method;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Method findSetterForProperty(String propertyName, Class<?> clazz, boolean mustBeStatic) {
/* 400 */     return findMethodForProperty(getPropertyMethodSuffixes(propertyName), "set", clazz, mustBeStatic, 1, ANY_TYPES);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Method findMethodForProperty(String[] methodSuffixes, String prefix, Class<?> clazz, boolean mustBeStatic, int numberOfParams, Set<Class<?>> requiredReturnTypes) {
/* 407 */     Method[] methods = getSortedMethods(clazz);
/* 408 */     for (String methodSuffix : methodSuffixes) {
/* 409 */       for (Method method : methods) {
/* 410 */         if (isCandidateForProperty(method, clazz) && method.getName().equals(prefix + methodSuffix) && (method
/* 411 */           .getParameterTypes()).length == numberOfParams && (!mustBeStatic || 
/* 412 */           Modifier.isStatic(method.getModifiers())) && (requiredReturnTypes
/* 413 */           .isEmpty() || requiredReturnTypes.contains(method.getReturnType()))) {
/* 414 */           return method;
/*     */         }
/*     */       } 
/*     */     } 
/* 418 */     return null;
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
/*     */   protected boolean isCandidateForProperty(Method method, Class<?> targetClass) {
/* 431 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Method[] getSortedMethods(Class<?> clazz) {
/* 438 */     Method[] methods = this.sortedMethodsCache.get(clazz);
/* 439 */     if (methods == null) {
/* 440 */       methods = clazz.getMethods();
/* 441 */       Arrays.sort(methods, new Comparator<Method>()
/*     */           {
/*     */             public int compare(Method o1, Method o2) {
/* 444 */               return (o1.isBridge() == o2.isBridge()) ? 0 : (o1.isBridge() ? 1 : -1);
/*     */             }
/*     */           });
/* 447 */       this.sortedMethodsCache.put(clazz, methods);
/*     */     } 
/* 449 */     return methods;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String[] getPropertyMethodSuffixes(String propertyName) {
/* 459 */     String suffix = getPropertyMethodSuffix(propertyName);
/* 460 */     if (suffix.length() > 0 && Character.isUpperCase(suffix.charAt(0))) {
/* 461 */       return new String[] { suffix };
/*     */     }
/* 463 */     return new String[] { suffix, StringUtils.capitalize(suffix) };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getPropertyMethodSuffix(String propertyName) {
/* 471 */     if (propertyName.length() > 1 && Character.isUpperCase(propertyName.charAt(1))) {
/* 472 */       return propertyName;
/*     */     }
/* 474 */     return StringUtils.capitalize(propertyName);
/*     */   }
/*     */   
/*     */   private Field findField(String name, Class<?> clazz, Object target) {
/* 478 */     Field field = findField(name, clazz, target instanceof Class);
/* 479 */     if (field == null && target instanceof Class) {
/* 480 */       field = findField(name, target.getClass(), false);
/*     */     }
/* 482 */     return field;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Field findField(String name, Class<?> clazz, boolean mustBeStatic) {
/* 489 */     Field[] fields = clazz.getFields();
/* 490 */     for (Field field : fields) {
/* 491 */       if (field.getName().equals(name) && (!mustBeStatic || Modifier.isStatic(field.getModifiers()))) {
/* 492 */         return field;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 497 */     if (clazz.getSuperclass() != null) {
/* 498 */       Field field = findField(name, clazz.getSuperclass(), mustBeStatic);
/* 499 */       if (field != null) {
/* 500 */         return field;
/*     */       }
/*     */     } 
/* 503 */     for (Class<?> implementedInterface : clazz.getInterfaces()) {
/* 504 */       Field field = findField(name, implementedInterface, mustBeStatic);
/* 505 */       if (field != null) {
/* 506 */         return field;
/*     */       }
/*     */     } 
/* 509 */     return null;
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
/*     */   public PropertyAccessor createOptimalAccessor(EvaluationContext context, Object target, String name) {
/* 525 */     if (target == null) {
/* 526 */       return this;
/*     */     }
/* 528 */     Class<?> clazz = (target instanceof Class) ? (Class)target : target.getClass();
/* 529 */     if (clazz.isArray()) {
/* 530 */       return this;
/*     */     }
/*     */     
/* 533 */     PropertyCacheKey cacheKey = new PropertyCacheKey(clazz, name, target instanceof Class);
/* 534 */     InvokerPair invocationTarget = this.readerCache.get(cacheKey);
/*     */     
/* 536 */     if (invocationTarget == null || invocationTarget.member instanceof Method) {
/* 537 */       Method method = (invocationTarget != null) ? (Method)invocationTarget.member : null;
/* 538 */       if (method == null) {
/* 539 */         method = findGetterForProperty(name, clazz, target);
/* 540 */         if (method != null) {
/* 541 */           invocationTarget = new InvokerPair(method, new TypeDescriptor(new MethodParameter(method, -1)));
/* 542 */           ReflectionUtils.makeAccessible(method);
/* 543 */           this.readerCache.put(cacheKey, invocationTarget);
/*     */         } 
/*     */       } 
/* 546 */       if (method != null) {
/* 547 */         return (PropertyAccessor)new OptimalPropertyAccessor(invocationTarget);
/*     */       }
/*     */     } 
/*     */     
/* 551 */     if (invocationTarget == null || invocationTarget.member instanceof Field) {
/* 552 */       Field field = (invocationTarget != null) ? (Field)invocationTarget.member : null;
/* 553 */       if (field == null) {
/* 554 */         field = findField(name, clazz, target instanceof Class);
/* 555 */         if (field != null) {
/* 556 */           invocationTarget = new InvokerPair(field, new TypeDescriptor(field));
/* 557 */           ReflectionUtils.makeAccessible(field);
/* 558 */           this.readerCache.put(cacheKey, invocationTarget);
/*     */         } 
/*     */       } 
/* 561 */       if (field != null) {
/* 562 */         return (PropertyAccessor)new OptimalPropertyAccessor(invocationTarget);
/*     */       }
/*     */     } 
/*     */     
/* 566 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class InvokerPair
/*     */   {
/*     */     final Member member;
/*     */ 
/*     */     
/*     */     final TypeDescriptor typeDescriptor;
/*     */ 
/*     */ 
/*     */     
/*     */     public InvokerPair(Member member, TypeDescriptor typeDescriptor) {
/* 581 */       this.member = member;
/* 582 */       this.typeDescriptor = typeDescriptor;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class PropertyCacheKey
/*     */     implements Comparable<PropertyCacheKey>
/*     */   {
/*     */     private final Class<?> clazz;
/*     */     
/*     */     private final String property;
/*     */     private boolean targetIsClass;
/*     */     
/*     */     public PropertyCacheKey(Class<?> clazz, String name, boolean targetIsClass) {
/* 596 */       this.clazz = clazz;
/* 597 */       this.property = name;
/* 598 */       this.targetIsClass = targetIsClass;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object other) {
/* 603 */       if (this == other) {
/* 604 */         return true;
/*     */       }
/* 606 */       if (!(other instanceof PropertyCacheKey)) {
/* 607 */         return false;
/*     */       }
/* 609 */       PropertyCacheKey otherKey = (PropertyCacheKey)other;
/* 610 */       return (this.clazz == otherKey.clazz && this.property.equals(otherKey.property) && this.targetIsClass == otherKey.targetIsClass);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 616 */       return this.clazz.hashCode() * 29 + this.property.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 621 */       return "CacheKey [clazz=" + this.clazz.getName() + ", property=" + this.property + ", " + this.property + ", targetIsClass=" + this.targetIsClass + "]";
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int compareTo(PropertyCacheKey other) {
/* 627 */       int result = this.clazz.getName().compareTo(other.clazz.getName());
/* 628 */       if (result == 0) {
/* 629 */         result = this.property.compareTo(other.property);
/*     */       }
/* 631 */       return result;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class OptimalPropertyAccessor
/*     */     implements CompilablePropertyAccessor
/*     */   {
/*     */     public final Member member;
/*     */ 
/*     */ 
/*     */     
/*     */     private final TypeDescriptor typeDescriptor;
/*     */ 
/*     */     
/*     */     private final boolean needsToBeMadeAccessible;
/*     */ 
/*     */ 
/*     */     
/*     */     OptimalPropertyAccessor(ReflectivePropertyAccessor.InvokerPair target) {
/* 653 */       this.member = target.member;
/* 654 */       this.typeDescriptor = target.typeDescriptor;
/* 655 */       this
/* 656 */         .needsToBeMadeAccessible = (!Modifier.isPublic(this.member.getModifiers()) || !Modifier.isPublic(this.member.getDeclaringClass().getModifiers()));
/*     */     }
/*     */ 
/*     */     
/*     */     public Class<?>[] getSpecificTargetClasses() {
/* 661 */       throw new UnsupportedOperationException("Should not be called on an OptimalPropertyAccessor");
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canRead(EvaluationContext context, Object target, String name) throws AccessException {
/* 666 */       if (target == null) {
/* 667 */         return false;
/*     */       }
/* 669 */       Class<?> type = (target instanceof Class) ? (Class)target : target.getClass();
/* 670 */       if (type.isArray()) {
/* 671 */         return false;
/*     */       }
/*     */       
/* 674 */       if (this.member instanceof Method) {
/* 675 */         Method method = (Method)this.member;
/* 676 */         String getterName = "get" + StringUtils.capitalize(name);
/* 677 */         if (getterName.equals(method.getName())) {
/* 678 */           return true;
/*     */         }
/* 680 */         getterName = "is" + StringUtils.capitalize(name);
/* 681 */         return getterName.equals(method.getName());
/*     */       } 
/*     */       
/* 684 */       Field field = (Field)this.member;
/* 685 */       return field.getName().equals(name);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public TypedValue read(EvaluationContext context, Object target, String name) throws AccessException {
/* 691 */       if (this.member instanceof Method) {
/* 692 */         Method method = (Method)this.member;
/*     */         try {
/* 694 */           if (this.needsToBeMadeAccessible && !method.isAccessible()) {
/* 695 */             method.setAccessible(true);
/*     */           }
/* 697 */           Object value = method.invoke(target, new Object[0]);
/* 698 */           return new TypedValue(value, this.typeDescriptor.narrow(value));
/*     */         }
/* 700 */         catch (Exception ex) {
/* 701 */           throw new AccessException("Unable to access property '" + name + "' through getter method", ex);
/*     */         } 
/*     */       } 
/*     */       
/* 705 */       Field field = (Field)this.member;
/*     */       try {
/* 707 */         if (this.needsToBeMadeAccessible && !field.isAccessible()) {
/* 708 */           field.setAccessible(true);
/*     */         }
/* 710 */         Object value = field.get(target);
/* 711 */         return new TypedValue(value, this.typeDescriptor.narrow(value));
/*     */       }
/* 713 */       catch (Exception ex) {
/* 714 */         throw new AccessException("Unable to access field '" + name + "'", ex);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean canWrite(EvaluationContext context, Object target, String name) {
/* 721 */       throw new UnsupportedOperationException("Should not be called on an OptimalPropertyAccessor");
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(EvaluationContext context, Object target, String name, Object newValue) {
/* 726 */       throw new UnsupportedOperationException("Should not be called on an OptimalPropertyAccessor");
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isCompilable() {
/* 731 */       return (Modifier.isPublic(this.member.getModifiers()) && 
/* 732 */         Modifier.isPublic(this.member.getDeclaringClass().getModifiers()));
/*     */     }
/*     */ 
/*     */     
/*     */     public Class<?> getPropertyType() {
/* 737 */       if (this.member instanceof Method) {
/* 738 */         return ((Method)this.member).getReturnType();
/*     */       }
/*     */       
/* 741 */       return ((Field)this.member).getType();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void generateCode(String propertyName, MethodVisitor mv, CodeFlow cf) {
/* 747 */       boolean isStatic = Modifier.isStatic(this.member.getModifiers());
/* 748 */       String descriptor = cf.lastDescriptor();
/* 749 */       String classDesc = this.member.getDeclaringClass().getName().replace('.', '/');
/*     */       
/* 751 */       if (!isStatic) {
/* 752 */         if (descriptor == null) {
/* 753 */           cf.loadTarget(mv);
/*     */         }
/* 755 */         if (descriptor == null || !classDesc.equals(descriptor.substring(1))) {
/* 756 */           mv.visitTypeInsn(192, classDesc);
/*     */         
/*     */         }
/*     */       }
/* 760 */       else if (descriptor != null) {
/*     */ 
/*     */         
/* 763 */         mv.visitInsn(87);
/*     */       } 
/*     */ 
/*     */       
/* 767 */       if (this.member instanceof Method) {
/* 768 */         mv.visitMethodInsn(isStatic ? 184 : 182, classDesc, this.member.getName(), 
/* 769 */             CodeFlow.createSignatureDescriptor((Method)this.member), false);
/*     */       } else {
/*     */         
/* 772 */         mv.visitFieldInsn(isStatic ? 178 : 180, classDesc, this.member.getName(), 
/* 773 */             CodeFlow.toJvmDescriptor(((Field)this.member).getType()));
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\expression\spel\support\ReflectivePropertyAccessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */