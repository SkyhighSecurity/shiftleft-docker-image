/*     */ package org.springframework.core.convert.support;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import org.springframework.core.convert.ConversionService;
/*     */ import org.springframework.core.convert.TypeDescriptor;
/*     */ import org.springframework.core.convert.converter.ConditionalGenericConverter;
/*     */ import org.springframework.core.convert.converter.GenericConverter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class ByteBufferConverter
/*     */   implements ConditionalGenericConverter
/*     */ {
/*  39 */   private static final TypeDescriptor BYTE_BUFFER_TYPE = TypeDescriptor.valueOf(ByteBuffer.class);
/*     */   
/*  41 */   private static final TypeDescriptor BYTE_ARRAY_TYPE = TypeDescriptor.valueOf(byte[].class);
/*     */   private static final Set<GenericConverter.ConvertiblePair> CONVERTIBLE_PAIRS;
/*     */   private final ConversionService conversionService;
/*     */   
/*     */   static {
/*  46 */     Set<GenericConverter.ConvertiblePair> convertiblePairs = new HashSet<GenericConverter.ConvertiblePair>(4);
/*  47 */     convertiblePairs.add(new GenericConverter.ConvertiblePair(ByteBuffer.class, byte[].class));
/*  48 */     convertiblePairs.add(new GenericConverter.ConvertiblePair(byte[].class, ByteBuffer.class));
/*  49 */     convertiblePairs.add(new GenericConverter.ConvertiblePair(ByteBuffer.class, Object.class));
/*  50 */     convertiblePairs.add(new GenericConverter.ConvertiblePair(Object.class, ByteBuffer.class));
/*  51 */     CONVERTIBLE_PAIRS = Collections.unmodifiableSet(convertiblePairs);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ByteBufferConverter(ConversionService conversionService) {
/*  59 */     this.conversionService = conversionService;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<GenericConverter.ConvertiblePair> getConvertibleTypes() {
/*  65 */     return CONVERTIBLE_PAIRS;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
/*  70 */     boolean byteBufferTarget = targetType.isAssignableTo(BYTE_BUFFER_TYPE);
/*  71 */     if (sourceType.isAssignableTo(BYTE_BUFFER_TYPE)) {
/*  72 */       return (byteBufferTarget || matchesFromByteBuffer(targetType));
/*     */     }
/*  74 */     return (byteBufferTarget && matchesToByteBuffer(sourceType));
/*     */   }
/*     */   
/*     */   private boolean matchesFromByteBuffer(TypeDescriptor targetType) {
/*  78 */     return (targetType.isAssignableTo(BYTE_ARRAY_TYPE) || this.conversionService
/*  79 */       .canConvert(BYTE_ARRAY_TYPE, targetType));
/*     */   }
/*     */   
/*     */   private boolean matchesToByteBuffer(TypeDescriptor sourceType) {
/*  83 */     return (sourceType.isAssignableTo(BYTE_ARRAY_TYPE) || this.conversionService
/*  84 */       .canConvert(sourceType, BYTE_ARRAY_TYPE));
/*     */   }
/*     */ 
/*     */   
/*     */   public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
/*  89 */     boolean byteBufferTarget = targetType.isAssignableTo(BYTE_BUFFER_TYPE);
/*  90 */     if (source instanceof ByteBuffer) {
/*  91 */       ByteBuffer buffer = (ByteBuffer)source;
/*  92 */       return byteBufferTarget ? buffer.duplicate() : convertFromByteBuffer(buffer, targetType);
/*     */     } 
/*  94 */     if (byteBufferTarget) {
/*  95 */       return convertToByteBuffer(source, sourceType);
/*     */     }
/*     */     
/*  98 */     throw new IllegalStateException("Unexpected source/target types");
/*     */   }
/*     */   
/*     */   private Object convertFromByteBuffer(ByteBuffer source, TypeDescriptor targetType) {
/* 102 */     byte[] bytes = new byte[source.remaining()];
/* 103 */     source.get(bytes);
/*     */     
/* 105 */     if (targetType.isAssignableTo(BYTE_ARRAY_TYPE)) {
/* 106 */       return bytes;
/*     */     }
/* 108 */     return this.conversionService.convert(bytes, BYTE_ARRAY_TYPE, targetType);
/*     */   }
/*     */ 
/*     */   
/*     */   private Object convertToByteBuffer(Object source, TypeDescriptor sourceType) {
/* 113 */     byte[] bytes = (source instanceof byte[]) ? (byte[])source : (byte[])this.conversionService.convert(source, sourceType, BYTE_ARRAY_TYPE);
/*     */     
/* 115 */     if (bytes == null) {
/* 116 */       return ByteBuffer.wrap(new byte[0]);
/*     */     }
/*     */     
/* 119 */     ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length);
/* 120 */     byteBuffer.put(bytes);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 125 */     return byteBuffer.rewind();
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\convert\support\ByteBufferConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */