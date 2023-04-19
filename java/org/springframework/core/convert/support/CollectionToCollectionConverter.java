/*    */ package org.springframework.core.convert.support;
/*    */ 
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
/*    */ final class CollectionToCollectionConverter
/*    */   implements ConditionalGenericConverter
/*    */ {
/*    */   private final ConversionService conversionService;
/*    */   
/*    */   public CollectionToCollectionConverter(ConversionService conversionService) {
/* 46 */     this.conversionService = conversionService;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Set<GenericConverter.ConvertiblePair> getConvertibleTypes() {
/* 52 */     return Collections.singleton(new GenericConverter.ConvertiblePair(Collection.class, Collection.class));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 57 */     return ConversionUtils.canConvertElements(sourceType
/* 58 */         .getElementTypeDescriptor(), targetType.getElementTypeDescriptor(), this.conversionService);
/*    */   }
/*    */ 
/*    */   
/*    */   public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 63 */     if (source == null) {
/* 64 */       return null;
/*    */     }
/* 66 */     Collection<?> sourceCollection = (Collection)source;
/*    */ 
/*    */     
/* 69 */     boolean copyRequired = !targetType.getType().isInstance(source);
/* 70 */     if (!copyRequired && sourceCollection.isEmpty()) {
/* 71 */       return source;
/*    */     }
/* 73 */     TypeDescriptor elementDesc = targetType.getElementTypeDescriptor();
/* 74 */     if (elementDesc == null && !copyRequired) {
/* 75 */       return source;
/*    */     }
/*    */ 
/*    */     
/* 79 */     Collection<Object> target = CollectionFactory.createCollection(targetType.getType(), (elementDesc != null) ? elementDesc
/* 80 */         .getType() : null, sourceCollection.size());
/*    */     
/* 82 */     if (elementDesc == null) {
/* 83 */       target.addAll(sourceCollection);
/*    */     } else {
/*    */       
/* 86 */       for (Object sourceElement : sourceCollection) {
/* 87 */         Object targetElement = this.conversionService.convert(sourceElement, sourceType
/* 88 */             .elementTypeDescriptor(sourceElement), elementDesc);
/* 89 */         target.add(targetElement);
/* 90 */         if (sourceElement != targetElement) {
/* 91 */           copyRequired = true;
/*    */         }
/*    */       } 
/*    */     } 
/*    */     
/* 96 */     return copyRequired ? target : source;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\convert\support\CollectionToCollectionConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */