/*     */ package org.springframework.core.convert.support;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Collector;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import org.springframework.core.convert.ConversionService;
/*     */ import org.springframework.core.convert.TypeDescriptor;
/*     */ import org.springframework.core.convert.converter.ConditionalGenericConverter;
/*     */ import org.springframework.core.convert.converter.GenericConverter;
/*     */ import org.springframework.lang.UsesJava8;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @UsesJava8
/*     */ class StreamConverter
/*     */   implements ConditionalGenericConverter
/*     */ {
/*  41 */   private static final TypeDescriptor STREAM_TYPE = TypeDescriptor.valueOf(Stream.class);
/*     */   
/*  43 */   private static final Set<GenericConverter.ConvertiblePair> CONVERTIBLE_TYPES = createConvertibleTypes();
/*     */   
/*     */   private final ConversionService conversionService;
/*     */ 
/*     */   
/*     */   public StreamConverter(ConversionService conversionService) {
/*  49 */     this.conversionService = conversionService;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<GenericConverter.ConvertiblePair> getConvertibleTypes() {
/*  55 */     return CONVERTIBLE_TYPES;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
/*  60 */     if (sourceType.isAssignableTo(STREAM_TYPE)) {
/*  61 */       return matchesFromStream(sourceType.getElementTypeDescriptor(), targetType);
/*     */     }
/*  63 */     if (targetType.isAssignableTo(STREAM_TYPE)) {
/*  64 */       return matchesToStream(targetType.getElementTypeDescriptor(), sourceType);
/*     */     }
/*  66 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean matchesFromStream(TypeDescriptor elementType, TypeDescriptor targetType) {
/*  76 */     TypeDescriptor collectionOfElement = TypeDescriptor.collection(Collection.class, elementType);
/*  77 */     return this.conversionService.canConvert(collectionOfElement, targetType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean matchesToStream(TypeDescriptor elementType, TypeDescriptor sourceType) {
/*  87 */     TypeDescriptor collectionOfElement = TypeDescriptor.collection(Collection.class, elementType);
/*  88 */     return this.conversionService.canConvert(sourceType, collectionOfElement);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
/*  93 */     if (sourceType.isAssignableTo(STREAM_TYPE)) {
/*  94 */       return convertFromStream((Stream)source, sourceType, targetType);
/*     */     }
/*  96 */     if (targetType.isAssignableTo(STREAM_TYPE)) {
/*  97 */       return convertToStream(source, sourceType, targetType);
/*     */     }
/*     */     
/* 100 */     throw new IllegalStateException("Unexpected source/target types");
/*     */   }
/*     */   
/*     */   private Object convertFromStream(Stream<?> source, TypeDescriptor streamType, TypeDescriptor targetType) {
/* 104 */     List<Object> content = source.collect((Collector)Collectors.toList());
/* 105 */     TypeDescriptor listType = TypeDescriptor.collection(List.class, streamType.getElementTypeDescriptor());
/* 106 */     return this.conversionService.convert(content, listType, targetType);
/*     */   }
/*     */   
/*     */   private Object convertToStream(Object source, TypeDescriptor sourceType, TypeDescriptor streamType) {
/* 110 */     TypeDescriptor targetCollection = TypeDescriptor.collection(List.class, streamType.getElementTypeDescriptor());
/* 111 */     List<?> target = (List)this.conversionService.convert(source, sourceType, targetCollection);
/* 112 */     return target.stream();
/*     */   }
/*     */ 
/*     */   
/*     */   private static Set<GenericConverter.ConvertiblePair> createConvertibleTypes() {
/* 117 */     Set<GenericConverter.ConvertiblePair> convertiblePairs = new HashSet<GenericConverter.ConvertiblePair>();
/* 118 */     convertiblePairs.add(new GenericConverter.ConvertiblePair(Stream.class, Collection.class));
/* 119 */     convertiblePairs.add(new GenericConverter.ConvertiblePair(Stream.class, Object[].class));
/* 120 */     convertiblePairs.add(new GenericConverter.ConvertiblePair(Collection.class, Stream.class));
/* 121 */     convertiblePairs.add(new GenericConverter.ConvertiblePair(Object[].class, Stream.class));
/* 122 */     return convertiblePairs;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\convert\support\StreamConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */