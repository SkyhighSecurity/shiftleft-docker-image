/*    */ package org.springframework.core.convert.support;
/*    */ 
/*    */ import java.lang.reflect.Array;
/*    */ import java.util.Collection;
/*    */ import java.util.Collections;
/*    */ import java.util.Set;
/*    */ import org.springframework.core.convert.ConversionService;
/*    */ import org.springframework.core.convert.TypeDescriptor;
/*    */ import org.springframework.core.convert.converter.ConditionalGenericConverter;
/*    */ import org.springframework.core.convert.converter.GenericConverter;
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
/*    */ 
/*    */ final class CollectionToArrayConverter
/*    */   implements ConditionalGenericConverter
/*    */ {
/*    */   private final ConversionService conversionService;
/*    */   
/*    */   public CollectionToArrayConverter(ConversionService conversionService) {
/* 45 */     this.conversionService = conversionService;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Set<GenericConverter.ConvertiblePair> getConvertibleTypes() {
/* 51 */     return Collections.singleton(new GenericConverter.ConvertiblePair(Collection.class, Object[].class));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 56 */     return ConversionUtils.canConvertElements(sourceType.getElementTypeDescriptor(), targetType
/* 57 */         .getElementTypeDescriptor(), this.conversionService);
/*    */   }
/*    */ 
/*    */   
/*    */   public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 62 */     if (source == null) {
/* 63 */       return null;
/*    */     }
/* 65 */     Collection<?> sourceCollection = (Collection)source;
/* 66 */     Object array = Array.newInstance(targetType.getElementTypeDescriptor().getType(), sourceCollection.size());
/* 67 */     int i = 0;
/* 68 */     for (Object sourceElement : sourceCollection) {
/* 69 */       Object targetElement = this.conversionService.convert(sourceElement, sourceType
/* 70 */           .elementTypeDescriptor(sourceElement), targetType.getElementTypeDescriptor());
/* 71 */       Array.set(array, i++, targetElement);
/*    */     } 
/* 73 */     return array;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\convert\support\CollectionToArrayConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */