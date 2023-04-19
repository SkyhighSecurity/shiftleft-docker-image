/*    */ package org.springframework.core.convert.support;
/*    */ 
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
/*    */ final class CollectionToObjectConverter
/*    */   implements ConditionalGenericConverter
/*    */ {
/*    */   private final ConversionService conversionService;
/*    */   
/*    */   public CollectionToObjectConverter(ConversionService conversionService) {
/* 38 */     this.conversionService = conversionService;
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<GenericConverter.ConvertiblePair> getConvertibleTypes() {
/* 43 */     return Collections.singleton(new GenericConverter.ConvertiblePair(Collection.class, Object.class));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 48 */     return ConversionUtils.canConvertElements(sourceType.getElementTypeDescriptor(), targetType, this.conversionService);
/*    */   }
/*    */ 
/*    */   
/*    */   public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 53 */     if (source == null) {
/* 54 */       return null;
/*    */     }
/* 56 */     if (sourceType.isAssignableTo(targetType)) {
/* 57 */       return source;
/*    */     }
/* 59 */     Collection<?> sourceCollection = (Collection)source;
/* 60 */     if (sourceCollection.isEmpty()) {
/* 61 */       return null;
/*    */     }
/* 63 */     Object firstElement = sourceCollection.iterator().next();
/* 64 */     return this.conversionService.convert(firstElement, sourceType.elementTypeDescriptor(firstElement), targetType);
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\convert\support\CollectionToObjectConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */