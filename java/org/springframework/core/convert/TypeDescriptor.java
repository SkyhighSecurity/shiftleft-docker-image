/*     */ package org.springframework.core.convert;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.AnnotatedElement;
/*     */ import java.lang.reflect.Field;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.stream.Stream;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.core.annotation.AnnotatedElementUtils;
/*     */ import org.springframework.lang.UsesJava8;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ObjectUtils;
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
/*     */ public class TypeDescriptor
/*     */   implements Serializable
/*     */ {
/*  52 */   static final Annotation[] EMPTY_ANNOTATION_ARRAY = new Annotation[0];
/*     */   
/*  54 */   private static final boolean streamAvailable = ClassUtils.isPresent("java.util.stream.Stream", TypeDescriptor.class
/*  55 */       .getClassLoader());
/*     */   
/*  57 */   private static final Map<Class<?>, TypeDescriptor> commonTypesCache = new HashMap<Class<?>, TypeDescriptor>(32);
/*     */   
/*  59 */   private static final Class<?>[] CACHED_COMMON_TYPES = new Class[] { boolean.class, Boolean.class, byte.class, Byte.class, char.class, Character.class, double.class, Double.class, float.class, Float.class, int.class, Integer.class, long.class, Long.class, short.class, Short.class, String.class, Object.class };
/*     */   private final Class<?> type;
/*     */   private final ResolvableType resolvableType;
/*     */   private final AnnotatedElementAdapter annotatedElement;
/*     */   
/*     */   static {
/*  65 */     for (Class<?> preCachedClass : CACHED_COMMON_TYPES) {
/*  66 */       commonTypesCache.put(preCachedClass, valueOf(preCachedClass));
/*     */     }
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
/*     */ 
/*     */   
/*     */   public TypeDescriptor(MethodParameter methodParameter) {
/*  85 */     this.resolvableType = ResolvableType.forMethodParameter(methodParameter);
/*  86 */     this.type = this.resolvableType.resolve(methodParameter.getParameterType());
/*  87 */     this
/*  88 */       .annotatedElement = new AnnotatedElementAdapter((methodParameter.getParameterIndex() == -1) ? methodParameter.getMethodAnnotations() : methodParameter.getParameterAnnotations());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypeDescriptor(Field field) {
/*  97 */     this.resolvableType = ResolvableType.forField(field);
/*  98 */     this.type = this.resolvableType.resolve(field.getType());
/*  99 */     this.annotatedElement = new AnnotatedElementAdapter(field.getAnnotations());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypeDescriptor(Property property) {
/* 109 */     Assert.notNull(property, "Property must not be null");
/* 110 */     this.resolvableType = ResolvableType.forMethodParameter(property.getMethodParameter());
/* 111 */     this.type = this.resolvableType.resolve(property.getType());
/* 112 */     this.annotatedElement = new AnnotatedElementAdapter(property.getAnnotations());
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
/*     */   protected TypeDescriptor(ResolvableType resolvableType, Class<?> type, Annotation[] annotations) {
/* 125 */     this.resolvableType = resolvableType;
/* 126 */     this.type = (type != null) ? type : resolvableType.resolve(Object.class);
/* 127 */     this.annotatedElement = new AnnotatedElementAdapter(annotations);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?> getObjectType() {
/* 138 */     return ClassUtils.resolvePrimitiveIfNecessary(getType());
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
/*     */   public Class<?> getType() {
/* 150 */     return this.type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResolvableType getResolvableType() {
/* 158 */     return this.resolvableType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getSource() {
/* 169 */     return (this.resolvableType != null) ? this.resolvableType.getSource() : null;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypeDescriptor narrow(Object value) {
/* 189 */     if (value == null) {
/* 190 */       return this;
/*     */     }
/* 192 */     ResolvableType narrowed = ResolvableType.forType(value.getClass(), getResolvableType());
/* 193 */     return new TypeDescriptor(narrowed, value.getClass(), getAnnotations());
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
/*     */   public TypeDescriptor upcast(Class<?> superType) {
/* 205 */     if (superType == null) {
/* 206 */       return null;
/*     */     }
/* 208 */     Assert.isAssignable(superType, getType());
/* 209 */     return new TypeDescriptor(getResolvableType().as(superType), superType, getAnnotations());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 216 */     return ClassUtils.getQualifiedName(getType());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isPrimitive() {
/* 223 */     return getType().isPrimitive();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Annotation[] getAnnotations() {
/* 231 */     return this.annotatedElement.getAnnotations();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasAnnotation(Class<? extends Annotation> annotationType) {
/* 242 */     if (this.annotatedElement.isEmpty())
/*     */     {
/*     */       
/* 245 */       return false;
/*     */     }
/* 247 */     return AnnotatedElementUtils.isAnnotated(this.annotatedElement, annotationType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T extends Annotation> T getAnnotation(Class<T> annotationType) {
/* 258 */     if (this.annotatedElement.isEmpty())
/*     */     {
/*     */       
/* 261 */       return null;
/*     */     }
/* 263 */     return (T)AnnotatedElementUtils.getMergedAnnotation(this.annotatedElement, annotationType);
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
/*     */ 
/*     */   
/*     */   public boolean isAssignableTo(TypeDescriptor typeDescriptor) {
/* 281 */     boolean typesAssignable = typeDescriptor.getObjectType().isAssignableFrom(getObjectType());
/* 282 */     if (!typesAssignable) {
/* 283 */       return false;
/*     */     }
/* 285 */     if (isArray() && typeDescriptor.isArray()) {
/* 286 */       return getElementTypeDescriptor().isAssignableTo(typeDescriptor.getElementTypeDescriptor());
/*     */     }
/* 288 */     if (isCollection() && typeDescriptor.isCollection()) {
/* 289 */       return isNestedAssignable(getElementTypeDescriptor(), typeDescriptor.getElementTypeDescriptor());
/*     */     }
/* 291 */     if (isMap() && typeDescriptor.isMap()) {
/* 292 */       return (isNestedAssignable(getMapKeyTypeDescriptor(), typeDescriptor.getMapKeyTypeDescriptor()) && 
/* 293 */         isNestedAssignable(getMapValueTypeDescriptor(), typeDescriptor.getMapValueTypeDescriptor()));
/*     */     }
/*     */     
/* 296 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isNestedAssignable(TypeDescriptor nestedTypeDescriptor, TypeDescriptor otherNestedTypeDescriptor) {
/* 301 */     if (nestedTypeDescriptor == null || otherNestedTypeDescriptor == null) {
/* 302 */       return true;
/*     */     }
/* 304 */     return nestedTypeDescriptor.isAssignableTo(otherNestedTypeDescriptor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isCollection() {
/* 311 */     return Collection.class.isAssignableFrom(getType());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isArray() {
/* 318 */     return getType().isArray();
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
/*     */   public TypeDescriptor getElementTypeDescriptor() {
/* 331 */     if (getResolvableType().isArray()) {
/* 332 */       return new TypeDescriptor(getResolvableType().getComponentType(), null, getAnnotations());
/*     */     }
/* 334 */     if (streamAvailable && StreamDelegate.isStream(getType())) {
/* 335 */       return StreamDelegate.getStreamElementType(this);
/*     */     }
/* 337 */     return getRelatedIfResolvable(this, getResolvableType().asCollection().getGeneric(new int[] { 0 }));
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypeDescriptor elementTypeDescriptor(Object element) {
/* 359 */     return narrow(element, getElementTypeDescriptor());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isMap() {
/* 366 */     return Map.class.isAssignableFrom(getType());
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
/*     */   public TypeDescriptor getMapKeyTypeDescriptor() {
/* 378 */     Assert.state(isMap(), "Not a [java.util.Map]");
/* 379 */     return getRelatedIfResolvable(this, getResolvableType().asMap().getGeneric(new int[] { 0 }));
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypeDescriptor getMapKeyTypeDescriptor(Object mapKey) {
/* 400 */     return narrow(mapKey, getMapKeyTypeDescriptor());
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
/*     */   public TypeDescriptor getMapValueTypeDescriptor() {
/* 413 */     Assert.state(isMap(), "Not a [java.util.Map]");
/* 414 */     return getRelatedIfResolvable(this, getResolvableType().asMap().getGeneric(new int[] { 1 }));
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypeDescriptor getMapValueTypeDescriptor(Object mapValue) {
/* 435 */     return narrow(mapValue, getMapValueTypeDescriptor());
/*     */   }
/*     */   
/*     */   private TypeDescriptor narrow(Object value, TypeDescriptor typeDescriptor) {
/* 439 */     if (typeDescriptor != null) {
/* 440 */       return typeDescriptor.narrow(value);
/*     */     }
/* 442 */     if (value != null) {
/* 443 */       return narrow(value);
/*     */     }
/* 445 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 450 */     if (this == other) {
/* 451 */       return true;
/*     */     }
/* 453 */     if (!(other instanceof TypeDescriptor)) {
/* 454 */       return false;
/*     */     }
/* 456 */     TypeDescriptor otherDesc = (TypeDescriptor)other;
/* 457 */     if (getType() != otherDesc.getType()) {
/* 458 */       return false;
/*     */     }
/* 460 */     if (!annotationsMatch(otherDesc)) {
/* 461 */       return false;
/*     */     }
/* 463 */     if (isCollection() || isArray()) {
/* 464 */       return ObjectUtils.nullSafeEquals(getElementTypeDescriptor(), otherDesc.getElementTypeDescriptor());
/*     */     }
/* 466 */     if (isMap()) {
/* 467 */       return (ObjectUtils.nullSafeEquals(getMapKeyTypeDescriptor(), otherDesc.getMapKeyTypeDescriptor()) && 
/* 468 */         ObjectUtils.nullSafeEquals(getMapValueTypeDescriptor(), otherDesc.getMapValueTypeDescriptor()));
/*     */     }
/*     */     
/* 471 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean annotationsMatch(TypeDescriptor otherDesc) {
/* 476 */     Annotation[] anns = getAnnotations();
/* 477 */     Annotation[] otherAnns = otherDesc.getAnnotations();
/* 478 */     if (anns == otherAnns) {
/* 479 */       return true;
/*     */     }
/* 481 */     if (anns.length != otherAnns.length) {
/* 482 */       return false;
/*     */     }
/* 484 */     if (anns.length > 0) {
/* 485 */       for (int i = 0; i < anns.length; i++) {
/* 486 */         if (!annotationEquals(anns[i], otherAnns[i])) {
/* 487 */           return false;
/*     */         }
/*     */       } 
/*     */     }
/* 491 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean annotationEquals(Annotation ann, Annotation otherAnn) {
/* 496 */     return (ann == otherAnn || (ann.getClass() == otherAnn.getClass() && ann.equals(otherAnn)));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 501 */     return getType().hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 506 */     StringBuilder builder = new StringBuilder();
/* 507 */     for (Annotation ann : getAnnotations()) {
/* 508 */       builder.append("@").append(ann.annotationType().getName()).append(' ');
/*     */     }
/* 510 */     builder.append(getResolvableType().toString());
/* 511 */     return builder.toString();
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
/*     */   public static TypeDescriptor forObject(Object source) {
/* 525 */     return (source != null) ? valueOf(source.getClass()) : null;
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
/*     */   public static TypeDescriptor valueOf(Class<?> type) {
/* 539 */     if (type == null) {
/* 540 */       type = Object.class;
/*     */     }
/* 542 */     TypeDescriptor desc = commonTypesCache.get(type);
/* 543 */     return (desc != null) ? desc : new TypeDescriptor(ResolvableType.forClass(type), null, null);
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
/*     */   public static TypeDescriptor collection(Class<?> collectionType, TypeDescriptor elementTypeDescriptor) {
/* 559 */     Assert.notNull(collectionType, "Collection type must not be null");
/* 560 */     if (!Collection.class.isAssignableFrom(collectionType)) {
/* 561 */       throw new IllegalArgumentException("Collection type must be a [java.util.Collection]");
/*     */     }
/* 563 */     ResolvableType element = (elementTypeDescriptor != null) ? elementTypeDescriptor.resolvableType : null;
/* 564 */     return new TypeDescriptor(ResolvableType.forClassWithGenerics(collectionType, new ResolvableType[] { element }), null, null);
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
/*     */ 
/*     */   
/*     */   public static TypeDescriptor map(Class<?> mapType, TypeDescriptor keyTypeDescriptor, TypeDescriptor valueTypeDescriptor) {
/* 582 */     Assert.notNull(mapType, "Map type must not be null");
/* 583 */     if (!Map.class.isAssignableFrom(mapType)) {
/* 584 */       throw new IllegalArgumentException("Map type must be a [java.util.Map]");
/*     */     }
/* 586 */     ResolvableType key = (keyTypeDescriptor != null) ? keyTypeDescriptor.resolvableType : null;
/* 587 */     ResolvableType value = (valueTypeDescriptor != null) ? valueTypeDescriptor.resolvableType : null;
/* 588 */     return new TypeDescriptor(ResolvableType.forClassWithGenerics(mapType, new ResolvableType[] { key, value }), null, null);
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
/*     */   public static TypeDescriptor array(TypeDescriptor elementTypeDescriptor) {
/* 602 */     if (elementTypeDescriptor == null) {
/* 603 */       return null;
/*     */     }
/* 605 */     return new TypeDescriptor(ResolvableType.forArrayComponent(elementTypeDescriptor.resolvableType), null, elementTypeDescriptor
/* 606 */         .getAnnotations());
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
/*     */   public static TypeDescriptor nested(MethodParameter methodParameter, int nestingLevel) {
/* 632 */     if (methodParameter.getNestingLevel() != 1) {
/* 633 */       throw new IllegalArgumentException("MethodParameter nesting level must be 1: use the nestingLevel parameter to specify the desired nestingLevel for nested type traversal");
/*     */     }
/*     */     
/* 636 */     return nested(new TypeDescriptor(methodParameter), nestingLevel);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static TypeDescriptor nested(Field field, int nestingLevel) {
/* 660 */     return nested(new TypeDescriptor(field), nestingLevel);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static TypeDescriptor nested(Property property, int nestingLevel) {
/* 685 */     return nested(new TypeDescriptor(property), nestingLevel);
/*     */   }
/*     */   
/*     */   private static TypeDescriptor nested(TypeDescriptor typeDescriptor, int nestingLevel) {
/* 689 */     ResolvableType nested = typeDescriptor.resolvableType;
/* 690 */     for (int i = 0; i < nestingLevel; i++) {
/* 691 */       if (Object.class != nested.getType())
/*     */       {
/*     */ 
/*     */ 
/*     */         
/* 696 */         nested = nested.getNested(2);
/*     */       }
/*     */     } 
/* 699 */     if (nested == ResolvableType.NONE) {
/* 700 */       return null;
/*     */     }
/* 702 */     return getRelatedIfResolvable(typeDescriptor, nested);
/*     */   }
/*     */   
/*     */   private static TypeDescriptor getRelatedIfResolvable(TypeDescriptor source, ResolvableType type) {
/* 706 */     if (type.resolve() == null) {
/* 707 */       return null;
/*     */     }
/* 709 */     return new TypeDescriptor(type, null, source.getAnnotations());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class AnnotatedElementAdapter
/*     */     implements AnnotatedElement, Serializable
/*     */   {
/*     */     private final Annotation[] annotations;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public AnnotatedElementAdapter(Annotation[] annotations) {
/* 724 */       this.annotations = annotations;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
/* 729 */       for (Annotation annotation : getAnnotations()) {
/* 730 */         if (annotation.annotationType() == annotationClass) {
/* 731 */           return true;
/*     */         }
/*     */       } 
/* 734 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
/* 740 */       for (Annotation annotation : getAnnotations()) {
/* 741 */         if (annotation.annotationType() == annotationClass) {
/* 742 */           return (T)annotation;
/*     */         }
/*     */       } 
/* 745 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public Annotation[] getAnnotations() {
/* 750 */       return (this.annotations != null) ? this.annotations : TypeDescriptor.EMPTY_ANNOTATION_ARRAY;
/*     */     }
/*     */ 
/*     */     
/*     */     public Annotation[] getDeclaredAnnotations() {
/* 755 */       return getAnnotations();
/*     */     }
/*     */     
/*     */     public boolean isEmpty() {
/* 759 */       return ObjectUtils.isEmpty((Object[])this.annotations);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object other) {
/* 764 */       return (this == other || (other instanceof AnnotatedElementAdapter && 
/* 765 */         Arrays.equals((Object[])this.annotations, (Object[])((AnnotatedElementAdapter)other).annotations)));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 770 */       return Arrays.hashCode((Object[])this.annotations);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 775 */       return TypeDescriptor.this.toString();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @UsesJava8
/*     */   private static class StreamDelegate
/*     */   {
/*     */     public static boolean isStream(Class<?> type) {
/* 787 */       return Stream.class.isAssignableFrom(type);
/*     */     }
/*     */     
/*     */     public static TypeDescriptor getStreamElementType(TypeDescriptor source) {
/* 791 */       return TypeDescriptor.getRelatedIfResolvable(source, source.getResolvableType().as(Stream.class).getGeneric(new int[] { 0 }));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\convert\TypeDescriptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */