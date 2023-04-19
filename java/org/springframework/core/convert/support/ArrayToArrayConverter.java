/*    */ package org.springframework.core.convert.support;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import org.springframework.core.convert.ConversionService;
/*    */ import org.springframework.core.convert.TypeDescriptor;
/*    */ import org.springframework.core.convert.converter.ConditionalGenericConverter;
/*    */ import org.springframework.core.convert.converter.GenericConverter;
/*    */ import org.springframework.util.ObjectUtils;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class ArrayToArrayConverter
/*    */   implements ConditionalGenericConverter
/*    */ {
/*    */   private final CollectionToArrayConverter helperConverter;
/*    */   private final ConversionService conversionService;
/*    */   
/*    */   public ArrayToArrayConverter(ConversionService conversionService) {
/* 46 */     this.helperConverter = new CollectionToArrayConverter(conversionService);
/* 47 */     this.conversionService = conversionService;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Set<GenericConverter.ConvertiblePair> getConvertibleTypes() {
/* 53 */     return Collections.singleton(new GenericConverter.ConvertiblePair(Object[].class, Object[].class));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 58 */     return this.helperConverter.matches(sourceType, targetType);
/*    */   }
/*    */ 
/*    */   
/*    */   public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 63 */     if (this.conversionService instanceof GenericConversionService && ((GenericConversionService)this.conversionService)
/* 64 */       .canBypassConvert(sourceType
/* 65 */         .getElementTypeDescriptor(), targetType.getElementTypeDescriptor())) {
/* 66 */       return source;
/*    */     }
/* 68 */     List<Object> sourceList = Arrays.asList(ObjectUtils.toObjectArray(source));
/* 69 */     return this.helperConverter.convert(sourceList, sourceType, targetType);
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\convert\support\ArrayToArrayConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */