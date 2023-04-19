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
/*    */ final class ObjectToCollectionConverter
/*    */   implements ConditionalGenericConverter
/*    */ {
/*    */   private final ConversionService conversionService;
/*    */   
/*    */   public ObjectToCollectionConverter(ConversionService conversionService) {
/* 42 */     this.conversionService = conversionService;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Set<GenericConverter.ConvertiblePair> getConvertibleTypes() {
/* 48 */     return Collections.singleton(new GenericConverter.ConvertiblePair(Object.class, Collection.class));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 53 */     return ConversionUtils.canConvertElements(sourceType, targetType.getElementTypeDescriptor(), this.conversionService);
/*    */   }
/*    */ 
/*    */   
/*    */   public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 58 */     if (source == null) {
/* 59 */       return null;
/*    */     }
/*    */     
/* 62 */     TypeDescriptor elementDesc = targetType.getElementTypeDescriptor();
/* 63 */     Collection<Object> target = CollectionFactory.createCollection(targetType.getType(), (elementDesc != null) ? elementDesc
/* 64 */         .getType() : null, 1);
/*    */     
/* 66 */     if (elementDesc == null || elementDesc.isCollection()) {
/* 67 */       target.add(source);
/*    */     } else {
/*    */       
/* 70 */       Object singleElement = this.conversionService.convert(source, sourceType, elementDesc);
/* 71 */       target.add(singleElement);
/*    */     } 
/* 73 */     return target;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\convert\support\ObjectToCollectionConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */