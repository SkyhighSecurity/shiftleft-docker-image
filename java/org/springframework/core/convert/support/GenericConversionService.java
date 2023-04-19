/*     */ package org.springframework.core.convert.support;
/*     */ 
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.springframework.core.DecoratingProxy;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.core.convert.ConversionFailedException;
/*     */ import org.springframework.core.convert.ConverterNotFoundException;
/*     */ import org.springframework.core.convert.TypeDescriptor;
/*     */ import org.springframework.core.convert.converter.ConditionalConverter;
/*     */ import org.springframework.core.convert.converter.ConditionalGenericConverter;
/*     */ import org.springframework.core.convert.converter.Converter;
/*     */ import org.springframework.core.convert.converter.ConverterFactory;
/*     */ import org.springframework.core.convert.converter.GenericConverter;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ConcurrentReferenceHashMap;
/*     */ import org.springframework.util.ObjectUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GenericConversionService
/*     */   implements ConfigurableConversionService
/*     */ {
/*  67 */   private static final GenericConverter NO_OP_CONVERTER = new NoOpConverter("NO_OP");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  73 */   private static final GenericConverter NO_MATCH = new NoOpConverter("NO_MATCH");
/*     */ 
/*     */ 
/*     */   
/*  77 */   private static Object javaUtilOptionalEmpty = null;
/*     */   
/*     */   static {
/*     */     try {
/*  81 */       Class<?> clazz = ClassUtils.forName("java.util.Optional", GenericConversionService.class.getClassLoader());
/*  82 */       javaUtilOptionalEmpty = ClassUtils.getMethod(clazz, "empty", new Class[0]).invoke(null, new Object[0]);
/*     */     }
/*  84 */     catch (Exception exception) {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  90 */   private final Converters converters = new Converters();
/*     */   
/*  92 */   private final Map<ConverterCacheKey, GenericConverter> converterCache = (Map<ConverterCacheKey, GenericConverter>)new ConcurrentReferenceHashMap(64);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addConverter(Converter<?, ?> converter) {
/* 100 */     ResolvableType[] typeInfo = getRequiredTypeInfo(converter.getClass(), Converter.class);
/* 101 */     if (typeInfo == null && converter instanceof DecoratingProxy) {
/* 102 */       typeInfo = getRequiredTypeInfo(((DecoratingProxy)converter).getDecoratedClass(), Converter.class);
/*     */     }
/* 104 */     if (typeInfo == null) {
/* 105 */       throw new IllegalArgumentException("Unable to determine source type <S> and target type <T> for your Converter [" + converter
/* 106 */           .getClass().getName() + "]; does the class parameterize those types?");
/*     */     }
/* 108 */     addConverter((GenericConverter)new ConverterAdapter(converter, typeInfo[0], typeInfo[1]));
/*     */   }
/*     */ 
/*     */   
/*     */   public <S, T> void addConverter(Class<S> sourceType, Class<T> targetType, Converter<? super S, ? extends T> converter) {
/* 113 */     addConverter((GenericConverter)new ConverterAdapter(converter, 
/* 114 */           ResolvableType.forClass(sourceType), ResolvableType.forClass(targetType)));
/*     */   }
/*     */ 
/*     */   
/*     */   public void addConverter(GenericConverter converter) {
/* 119 */     this.converters.add(converter);
/* 120 */     invalidateCache();
/*     */   }
/*     */ 
/*     */   
/*     */   public void addConverterFactory(ConverterFactory<?, ?> factory) {
/* 125 */     ResolvableType[] typeInfo = getRequiredTypeInfo(factory.getClass(), ConverterFactory.class);
/* 126 */     if (typeInfo == null && factory instanceof DecoratingProxy) {
/* 127 */       typeInfo = getRequiredTypeInfo(((DecoratingProxy)factory).getDecoratedClass(), ConverterFactory.class);
/*     */     }
/* 129 */     if (typeInfo == null) {
/* 130 */       throw new IllegalArgumentException("Unable to determine source type <S> and target type <T> for your ConverterFactory [" + factory
/* 131 */           .getClass().getName() + "]; does the class parameterize those types?");
/*     */     }
/* 133 */     addConverter((GenericConverter)new ConverterFactoryAdapter(factory, new GenericConverter.ConvertiblePair(typeInfo[0]
/* 134 */             .resolve(), typeInfo[1].resolve())));
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeConvertible(Class<?> sourceType, Class<?> targetType) {
/* 139 */     this.converters.remove(sourceType, targetType);
/* 140 */     invalidateCache();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canConvert(Class<?> sourceType, Class<?> targetType) {
/* 148 */     Assert.notNull(targetType, "Target type to convert to cannot be null");
/* 149 */     return canConvert((sourceType != null) ? TypeDescriptor.valueOf(sourceType) : null, 
/* 150 */         TypeDescriptor.valueOf(targetType));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canConvert(TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 155 */     Assert.notNull(targetType, "Target type to convert to cannot be null");
/* 156 */     if (sourceType == null) {
/* 157 */       return true;
/*     */     }
/* 159 */     GenericConverter converter = getConverter(sourceType, targetType);
/* 160 */     return (converter != null);
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
/*     */   public boolean canBypassConvert(TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 175 */     Assert.notNull(targetType, "Target type to convert to cannot be null");
/* 176 */     if (sourceType == null) {
/* 177 */       return true;
/*     */     }
/* 179 */     GenericConverter converter = getConverter(sourceType, targetType);
/* 180 */     return (converter == NO_OP_CONVERTER);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T convert(Object source, Class<T> targetType) {
/* 186 */     Assert.notNull(targetType, "Target type to convert to cannot be null");
/* 187 */     return (T)convert(source, TypeDescriptor.forObject(source), TypeDescriptor.valueOf(targetType));
/*     */   }
/*     */ 
/*     */   
/*     */   public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 192 */     Assert.notNull(targetType, "Target type to convert to cannot be null");
/* 193 */     if (sourceType == null) {
/* 194 */       Assert.isTrue((source == null), "Source must be [null] if source type == [null]");
/* 195 */       return handleResult(null, targetType, convertNullSource(null, targetType));
/*     */     } 
/* 197 */     if (source != null && !sourceType.getObjectType().isInstance(source)) {
/* 198 */       throw new IllegalArgumentException("Source to convert from must be an instance of [" + sourceType + "]; instead it was a [" + source
/* 199 */           .getClass().getName() + "]");
/*     */     }
/* 201 */     GenericConverter converter = getConverter(sourceType, targetType);
/* 202 */     if (converter != null) {
/* 203 */       Object result = ConversionUtils.invokeConverter(converter, source, sourceType, targetType);
/* 204 */       return handleResult(sourceType, targetType, result);
/*     */     } 
/* 206 */     return handleConverterNotFound(source, sourceType, targetType);
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
/*     */   public Object convert(Object source, TypeDescriptor targetType) {
/* 223 */     return convert(source, TypeDescriptor.forObject(source), targetType);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 228 */     return this.converters.toString();
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
/*     */   protected Object convertNullSource(TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 245 */     if (javaUtilOptionalEmpty != null && targetType.getObjectType() == javaUtilOptionalEmpty.getClass()) {
/* 246 */       return javaUtilOptionalEmpty;
/*     */     }
/* 248 */     return null;
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
/*     */   protected GenericConverter getConverter(TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 263 */     ConverterCacheKey key = new ConverterCacheKey(sourceType, targetType);
/* 264 */     GenericConverter converter = this.converterCache.get(key);
/* 265 */     if (converter != null) {
/* 266 */       return (converter != NO_MATCH) ? converter : null;
/*     */     }
/*     */     
/* 269 */     converter = this.converters.find(sourceType, targetType);
/* 270 */     if (converter == null) {
/* 271 */       converter = getDefaultConverter(sourceType, targetType);
/*     */     }
/*     */     
/* 274 */     if (converter != null) {
/* 275 */       this.converterCache.put(key, converter);
/* 276 */       return converter;
/*     */     } 
/*     */     
/* 279 */     this.converterCache.put(key, NO_MATCH);
/* 280 */     return null;
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
/*     */   protected GenericConverter getDefaultConverter(TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 292 */     return sourceType.isAssignableTo(targetType) ? NO_OP_CONVERTER : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ResolvableType[] getRequiredTypeInfo(Class<?> converterClass, Class<?> genericIfc) {
/* 299 */     ResolvableType resolvableType = ResolvableType.forClass(converterClass).as(genericIfc);
/* 300 */     ResolvableType[] generics = resolvableType.getGenerics();
/* 301 */     if (generics.length < 2) {
/* 302 */       return null;
/*     */     }
/* 304 */     Class<?> sourceType = generics[0].resolve();
/* 305 */     Class<?> targetType = generics[1].resolve();
/* 306 */     if (sourceType == null || targetType == null) {
/* 307 */       return null;
/*     */     }
/* 309 */     return generics;
/*     */   }
/*     */   
/*     */   private void invalidateCache() {
/* 313 */     this.converterCache.clear();
/*     */   }
/*     */   
/*     */   private Object handleConverterNotFound(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 317 */     if (source == null) {
/* 318 */       assertNotPrimitiveTargetType(sourceType, targetType);
/* 319 */       return null;
/*     */     } 
/* 321 */     if (sourceType.isAssignableTo(targetType) && targetType.getObjectType().isInstance(source)) {
/* 322 */       return source;
/*     */     }
/* 324 */     throw new ConverterNotFoundException(sourceType, targetType);
/*     */   }
/*     */   
/*     */   private Object handleResult(TypeDescriptor sourceType, TypeDescriptor targetType, Object result) {
/* 328 */     if (result == null) {
/* 329 */       assertNotPrimitiveTargetType(sourceType, targetType);
/*     */     }
/* 331 */     return result;
/*     */   }
/*     */   
/*     */   private void assertNotPrimitiveTargetType(TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 335 */     if (targetType.isPrimitive()) {
/* 336 */       throw new ConversionFailedException(sourceType, targetType, null, new IllegalArgumentException("A null value cannot be assigned to a primitive type"));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private final class ConverterAdapter
/*     */     implements ConditionalGenericConverter
/*     */   {
/*     */     private final Converter<Object, Object> converter;
/*     */ 
/*     */     
/*     */     private final GenericConverter.ConvertiblePair typeInfo;
/*     */ 
/*     */     
/*     */     private final ResolvableType targetType;
/*     */ 
/*     */     
/*     */     public ConverterAdapter(Converter<?, ?> converter, ResolvableType sourceType, ResolvableType targetType) {
/* 355 */       this.converter = (Converter)converter;
/* 356 */       this.typeInfo = new GenericConverter.ConvertiblePair(sourceType.resolve(Object.class), targetType.resolve(Object.class));
/* 357 */       this.targetType = targetType;
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<GenericConverter.ConvertiblePair> getConvertibleTypes() {
/* 362 */       return Collections.singleton(this.typeInfo);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 368 */       if (this.typeInfo.getTargetType() != targetType.getObjectType()) {
/* 369 */         return false;
/*     */       }
/*     */       
/* 372 */       ResolvableType rt = targetType.getResolvableType();
/* 373 */       if (!(rt.getType() instanceof Class) && !rt.isAssignableFrom(this.targetType) && 
/* 374 */         !this.targetType.hasUnresolvableGenerics()) {
/* 375 */         return false;
/*     */       }
/* 377 */       return (!(this.converter instanceof ConditionalConverter) || ((ConditionalConverter)this.converter)
/* 378 */         .matches(sourceType, targetType));
/*     */     }
/*     */ 
/*     */     
/*     */     public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 383 */       if (source == null) {
/* 384 */         return GenericConversionService.this.convertNullSource(sourceType, targetType);
/*     */       }
/* 386 */       return this.converter.convert(source);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 391 */       return this.typeInfo + " : " + this.converter;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private final class ConverterFactoryAdapter
/*     */     implements ConditionalGenericConverter
/*     */   {
/*     */     private final ConverterFactory<Object, Object> converterFactory;
/*     */ 
/*     */     
/*     */     private final GenericConverter.ConvertiblePair typeInfo;
/*     */ 
/*     */     
/*     */     public ConverterFactoryAdapter(ConverterFactory<?, ?> converterFactory, GenericConverter.ConvertiblePair typeInfo) {
/* 407 */       this.converterFactory = (ConverterFactory)converterFactory;
/* 408 */       this.typeInfo = typeInfo;
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<GenericConverter.ConvertiblePair> getConvertibleTypes() {
/* 413 */       return Collections.singleton(this.typeInfo);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 418 */       boolean matches = true;
/* 419 */       if (this.converterFactory instanceof ConditionalConverter) {
/* 420 */         matches = ((ConditionalConverter)this.converterFactory).matches(sourceType, targetType);
/*     */       }
/* 422 */       if (matches) {
/* 423 */         Converter<?, ?> converter = this.converterFactory.getConverter(targetType.getType());
/* 424 */         if (converter instanceof ConditionalConverter) {
/* 425 */           matches = ((ConditionalConverter)converter).matches(sourceType, targetType);
/*     */         }
/*     */       } 
/* 428 */       return matches;
/*     */     }
/*     */ 
/*     */     
/*     */     public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 433 */       if (source == null) {
/* 434 */         return GenericConversionService.this.convertNullSource(sourceType, targetType);
/*     */       }
/* 436 */       return this.converterFactory.getConverter(targetType.getObjectType()).convert(source);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 441 */       return this.typeInfo + " : " + this.converterFactory;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class ConverterCacheKey
/*     */     implements Comparable<ConverterCacheKey>
/*     */   {
/*     */     private final TypeDescriptor sourceType;
/*     */     
/*     */     private final TypeDescriptor targetType;
/*     */ 
/*     */     
/*     */     public ConverterCacheKey(TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 456 */       this.sourceType = sourceType;
/* 457 */       this.targetType = targetType;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object other) {
/* 462 */       if (this == other) {
/* 463 */         return true;
/*     */       }
/* 465 */       if (!(other instanceof ConverterCacheKey)) {
/* 466 */         return false;
/*     */       }
/* 468 */       ConverterCacheKey otherKey = (ConverterCacheKey)other;
/* 469 */       return (ObjectUtils.nullSafeEquals(this.sourceType, otherKey.sourceType) && 
/* 470 */         ObjectUtils.nullSafeEquals(this.targetType, otherKey.targetType));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 475 */       return ObjectUtils.nullSafeHashCode(this.sourceType) * 29 + 
/* 476 */         ObjectUtils.nullSafeHashCode(this.targetType);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 481 */       return "ConverterCacheKey [sourceType = " + this.sourceType + ", targetType = " + this.targetType + "]";
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int compareTo(ConverterCacheKey other) {
/* 487 */       int result = this.sourceType.getResolvableType().toString().compareTo(other.sourceType
/* 488 */           .getResolvableType().toString());
/* 489 */       if (result == 0) {
/* 490 */         result = this.targetType.getResolvableType().toString().compareTo(other.targetType
/* 491 */             .getResolvableType().toString());
/*     */       }
/* 493 */       return result;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class Converters
/*     */   {
/* 503 */     private final Set<GenericConverter> globalConverters = new LinkedHashSet<GenericConverter>();
/*     */     
/* 505 */     private final Map<GenericConverter.ConvertiblePair, GenericConversionService.ConvertersForPair> converters = new LinkedHashMap<GenericConverter.ConvertiblePair, GenericConversionService.ConvertersForPair>(36);
/*     */ 
/*     */     
/*     */     public void add(GenericConverter converter) {
/* 509 */       Set<GenericConverter.ConvertiblePair> convertibleTypes = converter.getConvertibleTypes();
/* 510 */       if (convertibleTypes == null) {
/* 511 */         Assert.state(converter instanceof ConditionalConverter, "Only conditional converters may return null convertible types");
/*     */         
/* 513 */         this.globalConverters.add(converter);
/*     */       } else {
/*     */         
/* 516 */         for (GenericConverter.ConvertiblePair convertiblePair : convertibleTypes) {
/* 517 */           GenericConversionService.ConvertersForPair convertersForPair = getMatchableConverters(convertiblePair);
/* 518 */           convertersForPair.add(converter);
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     private GenericConversionService.ConvertersForPair getMatchableConverters(GenericConverter.ConvertiblePair convertiblePair) {
/* 524 */       GenericConversionService.ConvertersForPair convertersForPair = this.converters.get(convertiblePair);
/* 525 */       if (convertersForPair == null) {
/* 526 */         convertersForPair = new GenericConversionService.ConvertersForPair();
/* 527 */         this.converters.put(convertiblePair, convertersForPair);
/*     */       } 
/* 529 */       return convertersForPair;
/*     */     }
/*     */     
/*     */     public void remove(Class<?> sourceType, Class<?> targetType) {
/* 533 */       this.converters.remove(new GenericConverter.ConvertiblePair(sourceType, targetType));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public GenericConverter find(TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 546 */       List<Class<?>> sourceCandidates = getClassHierarchy(sourceType.getType());
/* 547 */       List<Class<?>> targetCandidates = getClassHierarchy(targetType.getType());
/* 548 */       for (Class<?> sourceCandidate : sourceCandidates) {
/* 549 */         for (Class<?> targetCandidate : targetCandidates) {
/* 550 */           GenericConverter.ConvertiblePair convertiblePair = new GenericConverter.ConvertiblePair(sourceCandidate, targetCandidate);
/* 551 */           GenericConverter converter = getRegisteredConverter(sourceType, targetType, convertiblePair);
/* 552 */           if (converter != null) {
/* 553 */             return converter;
/*     */           }
/*     */         } 
/*     */       } 
/* 557 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private GenericConverter getRegisteredConverter(TypeDescriptor sourceType, TypeDescriptor targetType, GenericConverter.ConvertiblePair convertiblePair) {
/* 564 */       GenericConversionService.ConvertersForPair convertersForPair = this.converters.get(convertiblePair);
/* 565 */       if (convertersForPair != null) {
/* 566 */         GenericConverter converter = convertersForPair.getConverter(sourceType, targetType);
/* 567 */         if (converter != null) {
/* 568 */           return converter;
/*     */         }
/*     */       } 
/*     */       
/* 572 */       for (GenericConverter globalConverter : this.globalConverters) {
/* 573 */         if (((ConditionalConverter)globalConverter).matches(sourceType, targetType)) {
/* 574 */           return globalConverter;
/*     */         }
/*     */       } 
/* 577 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private List<Class<?>> getClassHierarchy(Class<?> type) {
/* 586 */       List<Class<?>> hierarchy = new ArrayList<Class<?>>(20);
/* 587 */       Set<Class<?>> visited = new HashSet<Class<?>>(20);
/* 588 */       addToClassHierarchy(0, ClassUtils.resolvePrimitiveIfNecessary(type), false, hierarchy, visited);
/* 589 */       boolean array = type.isArray();
/*     */       
/* 591 */       int i = 0;
/* 592 */       while (i < hierarchy.size()) {
/* 593 */         Class<?> candidate = hierarchy.get(i);
/* 594 */         candidate = array ? candidate.getComponentType() : ClassUtils.resolvePrimitiveIfNecessary(candidate);
/* 595 */         Class<?> superclass = candidate.getSuperclass();
/* 596 */         if (superclass != null && superclass != Object.class && superclass != Enum.class) {
/* 597 */           addToClassHierarchy(i + 1, candidate.getSuperclass(), array, hierarchy, visited);
/*     */         }
/* 599 */         addInterfacesToClassHierarchy(candidate, array, hierarchy, visited);
/* 600 */         i++;
/*     */       } 
/*     */       
/* 603 */       if (Enum.class.isAssignableFrom(type)) {
/* 604 */         addToClassHierarchy(hierarchy.size(), Enum.class, array, hierarchy, visited);
/* 605 */         addToClassHierarchy(hierarchy.size(), Enum.class, false, hierarchy, visited);
/* 606 */         addInterfacesToClassHierarchy(Enum.class, array, hierarchy, visited);
/*     */       } 
/*     */       
/* 609 */       addToClassHierarchy(hierarchy.size(), Object.class, array, hierarchy, visited);
/* 610 */       addToClassHierarchy(hierarchy.size(), Object.class, false, hierarchy, visited);
/* 611 */       return hierarchy;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private void addInterfacesToClassHierarchy(Class<?> type, boolean asArray, List<Class<?>> hierarchy, Set<Class<?>> visited) {
/* 617 */       for (Class<?> implementedInterface : type.getInterfaces()) {
/* 618 */         addToClassHierarchy(hierarchy.size(), implementedInterface, asArray, hierarchy, visited);
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private void addToClassHierarchy(int index, Class<?> type, boolean asArray, List<Class<?>> hierarchy, Set<Class<?>> visited) {
/* 625 */       if (asArray) {
/* 626 */         type = Array.newInstance(type, 0).getClass();
/*     */       }
/* 628 */       if (visited.add(type)) {
/* 629 */         hierarchy.add(index, type);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 635 */       StringBuilder builder = new StringBuilder();
/* 636 */       builder.append("ConversionService converters =\n");
/* 637 */       for (String converterString : getConverterStrings()) {
/* 638 */         builder.append('\t').append(converterString).append('\n');
/*     */       }
/* 640 */       return builder.toString();
/*     */     }
/*     */     
/*     */     private List<String> getConverterStrings() {
/* 644 */       List<String> converterStrings = new ArrayList<String>();
/* 645 */       for (GenericConversionService.ConvertersForPair convertersForPair : this.converters.values()) {
/* 646 */         converterStrings.add(convertersForPair.toString());
/*     */       }
/* 648 */       Collections.sort(converterStrings);
/* 649 */       return converterStrings;
/*     */     }
/*     */ 
/*     */     
/*     */     private Converters() {}
/*     */   }
/*     */ 
/*     */   
/*     */   private static class ConvertersForPair
/*     */   {
/* 659 */     private final LinkedList<GenericConverter> converters = new LinkedList<GenericConverter>();
/*     */     
/*     */     public void add(GenericConverter converter) {
/* 662 */       this.converters.addFirst(converter);
/*     */     }
/*     */     
/*     */     public GenericConverter getConverter(TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 666 */       for (GenericConverter converter : this.converters) {
/* 667 */         if (!(converter instanceof ConditionalGenericConverter) || ((ConditionalGenericConverter)converter)
/* 668 */           .matches(sourceType, targetType)) {
/* 669 */           return converter;
/*     */         }
/*     */       } 
/* 672 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 677 */       return StringUtils.collectionToCommaDelimitedString(this.converters);
/*     */     }
/*     */ 
/*     */     
/*     */     private ConvertersForPair() {}
/*     */   }
/*     */   
/*     */   private static class NoOpConverter
/*     */     implements GenericConverter
/*     */   {
/*     */     private final String name;
/*     */     
/*     */     public NoOpConverter(String name) {
/* 690 */       this.name = name;
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<GenericConverter.ConvertiblePair> getConvertibleTypes() {
/* 695 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 700 */       return source;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 705 */       return this.name;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\convert\support\GenericConversionService.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */