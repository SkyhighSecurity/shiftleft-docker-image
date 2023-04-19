/*     */ package org.springframework.core;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.AnnotatedElement;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
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
/*     */ public class MethodParameter
/*     */ {
/*     */   static {
/*     */     Class<?> clazz;
/*     */   }
/*     */   
/*  51 */   private static final Annotation[] EMPTY_ANNOTATION_ARRAY = new Annotation[0];
/*     */   
/*     */   private static final Class<?> javaUtilOptionalClass;
/*     */   private final Method method;
/*     */   
/*     */   static {
/*     */     try {
/*  58 */       clazz = ClassUtils.forName("java.util.Optional", MethodParameter.class.getClassLoader());
/*     */     }
/*  60 */     catch (ClassNotFoundException ex) {
/*     */       
/*  62 */       clazz = null;
/*     */     } 
/*  64 */     javaUtilOptionalClass = clazz;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private final Constructor<?> constructor;
/*     */ 
/*     */   
/*     */   private final int parameterIndex;
/*     */   
/*  74 */   private int nestingLevel = 1;
/*     */ 
/*     */   
/*     */   Map<Integer, Integer> typeIndexesPerLevel;
/*     */ 
/*     */   
/*     */   private volatile Class<?> containingClass;
/*     */ 
/*     */   
/*     */   private volatile Class<?> parameterType;
/*     */ 
/*     */   
/*     */   private volatile Type genericParameterType;
/*     */ 
/*     */   
/*     */   private volatile Annotation[] parameterAnnotations;
/*     */ 
/*     */   
/*     */   private volatile ParameterNameDiscoverer parameterNameDiscoverer;
/*     */ 
/*     */   
/*     */   private volatile String parameterName;
/*     */ 
/*     */   
/*     */   private volatile MethodParameter nestedMethodParameter;
/*     */ 
/*     */   
/*     */   public MethodParameter(Method method, int parameterIndex) {
/* 102 */     this(method, parameterIndex, 1);
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
/*     */   public MethodParameter(Method method, int parameterIndex, int nestingLevel) {
/* 116 */     Assert.notNull(method, "Method must not be null");
/* 117 */     this.method = method;
/* 118 */     this.parameterIndex = parameterIndex;
/* 119 */     this.nestingLevel = nestingLevel;
/* 120 */     this.constructor = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MethodParameter(Constructor<?> constructor, int parameterIndex) {
/* 129 */     this(constructor, parameterIndex, 1);
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
/*     */   public MethodParameter(Constructor<?> constructor, int parameterIndex, int nestingLevel) {
/* 141 */     Assert.notNull(constructor, "Constructor must not be null");
/* 142 */     this.constructor = constructor;
/* 143 */     this.parameterIndex = parameterIndex;
/* 144 */     this.nestingLevel = nestingLevel;
/* 145 */     this.method = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MethodParameter(MethodParameter original) {
/* 154 */     Assert.notNull(original, "Original must not be null");
/* 155 */     this.method = original.method;
/* 156 */     this.constructor = original.constructor;
/* 157 */     this.parameterIndex = original.parameterIndex;
/* 158 */     this.nestingLevel = original.nestingLevel;
/* 159 */     this.typeIndexesPerLevel = original.typeIndexesPerLevel;
/* 160 */     this.containingClass = original.containingClass;
/* 161 */     this.parameterType = original.parameterType;
/* 162 */     this.genericParameterType = original.genericParameterType;
/* 163 */     this.parameterAnnotations = original.parameterAnnotations;
/* 164 */     this.parameterNameDiscoverer = original.parameterNameDiscoverer;
/* 165 */     this.parameterName = original.parameterName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Method getMethod() {
/* 175 */     return this.method;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Constructor<?> getConstructor() {
/* 184 */     return this.constructor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?> getDeclaringClass() {
/* 191 */     return getMember().getDeclaringClass();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Member getMember() {
/* 202 */     if (this.method != null) {
/* 203 */       return this.method;
/*     */     }
/*     */     
/* 206 */     return this.constructor;
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
/*     */   public AnnotatedElement getAnnotatedElement() {
/* 220 */     if (this.method != null) {
/* 221 */       return this.method;
/*     */     }
/*     */     
/* 224 */     return this.constructor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getParameterIndex() {
/* 233 */     return this.parameterIndex;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void increaseNestingLevel() {
/* 241 */     this.nestingLevel++;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void decreaseNestingLevel() {
/* 249 */     getTypeIndexesPerLevel().remove(Integer.valueOf(this.nestingLevel));
/* 250 */     this.nestingLevel--;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getNestingLevel() {
/* 259 */     return this.nestingLevel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTypeIndexForCurrentLevel(int typeIndex) {
/* 269 */     getTypeIndexesPerLevel().put(Integer.valueOf(this.nestingLevel), Integer.valueOf(typeIndex));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Integer getTypeIndexForCurrentLevel() {
/* 279 */     return getTypeIndexForLevel(this.nestingLevel);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Integer getTypeIndexForLevel(int nestingLevel) {
/* 289 */     return getTypeIndexesPerLevel().get(Integer.valueOf(nestingLevel));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Map<Integer, Integer> getTypeIndexesPerLevel() {
/* 296 */     if (this.typeIndexesPerLevel == null) {
/* 297 */       this.typeIndexesPerLevel = new HashMap<Integer, Integer>(4);
/*     */     }
/* 299 */     return this.typeIndexesPerLevel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MethodParameter nested() {
/* 310 */     if (this.nestedMethodParameter != null) {
/* 311 */       return this.nestedMethodParameter;
/*     */     }
/* 313 */     MethodParameter nestedParam = clone();
/* 314 */     this.nestingLevel++;
/* 315 */     this.nestedMethodParameter = nestedParam;
/* 316 */     return nestedParam;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isOptional() {
/* 325 */     return (getParameterType() == javaUtilOptionalClass);
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
/*     */   public MethodParameter nestedIfOptional() {
/* 337 */     return isOptional() ? nested() : this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setContainingClass(Class<?> containingClass) {
/* 344 */     this.containingClass = containingClass;
/*     */   }
/*     */   
/*     */   public Class<?> getContainingClass() {
/* 348 */     return (this.containingClass != null) ? this.containingClass : getDeclaringClass();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setParameterType(Class<?> parameterType) {
/* 355 */     this.parameterType = parameterType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?> getParameterType() {
/* 363 */     Class<?> paramType = this.parameterType;
/* 364 */     if (paramType == null) {
/* 365 */       if (this.parameterIndex < 0) {
/* 366 */         Method method = getMethod();
/* 367 */         paramType = (method != null) ? method.getReturnType() : void.class;
/*     */       
/*     */       }
/*     */       else {
/*     */         
/* 372 */         paramType = (this.method != null) ? this.method.getParameterTypes()[this.parameterIndex] : this.constructor.getParameterTypes()[this.parameterIndex];
/*     */       } 
/* 374 */       this.parameterType = paramType;
/*     */     } 
/* 376 */     return paramType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Type getGenericParameterType() {
/* 385 */     Type paramType = this.genericParameterType;
/* 386 */     if (paramType == null) {
/* 387 */       if (this.parameterIndex < 0) {
/* 388 */         Method method = getMethod();
/* 389 */         paramType = (method != null) ? method.getGenericReturnType() : void.class;
/*     */       }
/*     */       else {
/*     */         
/* 393 */         Type[] genericParameterTypes = (this.method != null) ? this.method.getGenericParameterTypes() : this.constructor.getGenericParameterTypes();
/* 394 */         int index = this.parameterIndex;
/* 395 */         if (this.constructor != null && this.constructor.getDeclaringClass().isMemberClass() && 
/* 396 */           !Modifier.isStatic(this.constructor.getDeclaringClass().getModifiers()) && genericParameterTypes.length == (this.constructor
/* 397 */           .getParameterTypes()).length - 1)
/*     */         {
/*     */ 
/*     */           
/* 401 */           index = this.parameterIndex - 1;
/*     */         }
/*     */         
/* 404 */         paramType = (index >= 0 && index < genericParameterTypes.length) ? genericParameterTypes[index] : getParameterType();
/*     */       } 
/* 406 */       this.genericParameterType = paramType;
/*     */     } 
/* 408 */     return paramType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?> getNestedParameterType() {
/* 418 */     if (this.nestingLevel > 1) {
/* 419 */       Type type = getGenericParameterType();
/* 420 */       for (int i = 2; i <= this.nestingLevel; i++) {
/* 421 */         if (type instanceof ParameterizedType) {
/* 422 */           Type[] args = ((ParameterizedType)type).getActualTypeArguments();
/* 423 */           Integer index = getTypeIndexForLevel(i);
/* 424 */           type = args[(index != null) ? index.intValue() : (args.length - 1)];
/*     */         } 
/*     */       } 
/*     */       
/* 428 */       if (type instanceof Class) {
/* 429 */         return (Class)type;
/*     */       }
/* 431 */       if (type instanceof ParameterizedType) {
/* 432 */         Type arg = ((ParameterizedType)type).getRawType();
/* 433 */         if (arg instanceof Class) {
/* 434 */           return (Class)arg;
/*     */         }
/*     */       } 
/* 437 */       return Object.class;
/*     */     } 
/*     */     
/* 440 */     return getParameterType();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Type getNestedGenericParameterType() {
/* 451 */     if (this.nestingLevel > 1) {
/* 452 */       Type type = getGenericParameterType();
/* 453 */       for (int i = 2; i <= this.nestingLevel; i++) {
/* 454 */         if (type instanceof ParameterizedType) {
/* 455 */           Type[] args = ((ParameterizedType)type).getActualTypeArguments();
/* 456 */           Integer index = getTypeIndexForLevel(i);
/* 457 */           type = args[(index != null) ? index.intValue() : (args.length - 1)];
/*     */         } 
/*     */       } 
/* 460 */       return type;
/*     */     } 
/*     */     
/* 463 */     return getGenericParameterType();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Annotation[] getMethodAnnotations() {
/* 471 */     return adaptAnnotationArray(getAnnotatedElement().getAnnotations());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <A extends Annotation> A getMethodAnnotation(Class<A> annotationType) {
/* 480 */     return adaptAnnotation(getAnnotatedElement().getAnnotation(annotationType));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <A extends Annotation> boolean hasMethodAnnotation(Class<A> annotationType) {
/* 490 */     return getAnnotatedElement().isAnnotationPresent(annotationType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Annotation[] getParameterAnnotations() {
/* 497 */     Annotation[] paramAnns = this.parameterAnnotations;
/* 498 */     if (paramAnns == null) {
/*     */       
/* 500 */       Annotation[][] annotationArray = (this.method != null) ? this.method.getParameterAnnotations() : this.constructor.getParameterAnnotations();
/* 501 */       int index = this.parameterIndex;
/* 502 */       if (this.constructor != null && this.constructor.getDeclaringClass().isMemberClass() && 
/* 503 */         !Modifier.isStatic(this.constructor.getDeclaringClass().getModifiers()) && annotationArray.length == (this.constructor
/* 504 */         .getParameterTypes()).length - 1)
/*     */       {
/*     */         
/* 507 */         index = this.parameterIndex - 1;
/*     */       }
/*     */       
/* 510 */       paramAnns = (index >= 0 && index < annotationArray.length) ? adaptAnnotationArray(annotationArray[index]) : EMPTY_ANNOTATION_ARRAY;
/* 511 */       this.parameterAnnotations = paramAnns;
/*     */     } 
/* 513 */     return paramAnns;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasParameterAnnotations() {
/* 522 */     return ((getParameterAnnotations()).length != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <A extends Annotation> A getParameterAnnotation(Class<A> annotationType) {
/* 532 */     Annotation[] anns = getParameterAnnotations();
/* 533 */     for (Annotation ann : anns) {
/* 534 */       if (annotationType.isInstance(ann)) {
/* 535 */         return (A)ann;
/*     */       }
/*     */     } 
/* 538 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <A extends Annotation> boolean hasParameterAnnotation(Class<A> annotationType) {
/* 547 */     return (getParameterAnnotation(annotationType) != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void initParameterNameDiscovery(ParameterNameDiscoverer parameterNameDiscoverer) {
/* 557 */     this.parameterNameDiscoverer = parameterNameDiscoverer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getParameterName() {
/* 568 */     ParameterNameDiscoverer discoverer = this.parameterNameDiscoverer;
/* 569 */     if (discoverer != null) {
/*     */       
/* 571 */       String[] parameterNames = (this.method != null) ? discoverer.getParameterNames(this.method) : discoverer.getParameterNames(this.constructor);
/* 572 */       if (parameterNames != null) {
/* 573 */         this.parameterName = parameterNames[this.parameterIndex];
/*     */       }
/* 575 */       this.parameterNameDiscoverer = null;
/*     */     } 
/* 577 */     return this.parameterName;
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
/*     */   protected <A extends Annotation> A adaptAnnotation(A annotation) {
/* 590 */     return annotation;
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
/*     */   protected Annotation[] adaptAnnotationArray(Annotation[] annotations) {
/* 602 */     return annotations;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 608 */     if (this == other) {
/* 609 */       return true;
/*     */     }
/* 611 */     if (!(other instanceof MethodParameter)) {
/* 612 */       return false;
/*     */     }
/* 614 */     MethodParameter otherParam = (MethodParameter)other;
/* 615 */     return (this.parameterIndex == otherParam.parameterIndex && getMember().equals(otherParam.getMember()));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 620 */     return getMember().hashCode() * 31 + this.parameterIndex;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 625 */     return ((this.method != null) ? ("method '" + this.method.getName() + "'") : "constructor") + " parameter " + this.parameterIndex;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public MethodParameter clone() {
/* 631 */     return new MethodParameter(this);
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
/*     */   public static MethodParameter forMethodOrConstructor(Object methodOrConstructor, int parameterIndex) {
/* 644 */     if (methodOrConstructor instanceof Method) {
/* 645 */       return new MethodParameter((Method)methodOrConstructor, parameterIndex);
/*     */     }
/* 647 */     if (methodOrConstructor instanceof Constructor) {
/* 648 */       return new MethodParameter((Constructor)methodOrConstructor, parameterIndex);
/*     */     }
/*     */     
/* 651 */     throw new IllegalArgumentException("Given object [" + methodOrConstructor + "] is neither a Method nor a Constructor");
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\MethodParameter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */