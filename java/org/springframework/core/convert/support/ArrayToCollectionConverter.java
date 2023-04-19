/*    */ package org.springframework.core.convert.support;
/*    */ 
/*    */ import java.lang.reflect.Array;
/*    */ import java.util.Collection;
/*    */ import java.util.Collections;
/*    */ import java.util.Set;
/*    */ import org.springframework.core.CollectionFactory;
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
/*    */ 
/*    */ final class ArrayToCollectionConverter
/*    */   implements ConditionalGenericConverter
/*    */ {
/*    */   private final ConversionService conversionService;
/*    */   
/*    */   public ArrayToCollectionConverter(ConversionService conversionService) {
/* 47 */     this.conversionService = conversionService;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Set<GenericConverter.ConvertiblePair> getConvertibleTypes() {
/* 53 */     return Collections.singleton(new GenericConverter.ConvertiblePair(Object[].class, Collection.class));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 58 */     return ConversionUtils.canConvertElements(sourceType
/* 59 */         .getElementTypeDescriptor(), targetType.getElementTypeDescriptor(), this.conversionService);
/*    */   }
/*    */ 
/*    */   
/*    */   public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 64 */     if (source == null) {
/* 65 */       return null;
/*    */     }
/*    */     
/* 68 */     int length = Array.getLength(source);
/* 69 */     TypeDescriptor elementDesc = targetType.getElementTypeDescriptor();
/* 70 */     Collection<Object> target = CollectionFactory.createCollection(targetType.getType(), (elementDesc != null) ? elementDesc
/* 71 */         .getType() : null, length);
/*    */     
/* 73 */     if (elementDesc == null) {
/* 74 */       for (int i = 0; i < length; i++) {
/* 75 */         Object sourceElement = Array.get(source, i);
/* 76 */         target.add(sourceElement);
/*    */       } 
/*    */     } else {
/*    */       
/* 80 */       for (int i = 0; i < length; i++) {
/* 81 */         Object sourceElement = Array.get(source, i);
/* 82 */         Object targetElement = this.conversionService.convert(sourceElement, sourceType
/* 83 */             .elementTypeDescriptor(sourceElement), elementDesc);
/* 84 */         target.add(targetElement);
/*    */       } 
/*    */     } 
/* 87 */     return target;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\convert\support\ArrayToCollectionConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */