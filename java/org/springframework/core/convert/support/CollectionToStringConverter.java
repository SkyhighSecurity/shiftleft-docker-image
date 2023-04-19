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
/*    */ 
/*    */ 
/*    */ final class CollectionToStringConverter
/*    */   implements ConditionalGenericConverter
/*    */ {
/*    */   private static final String DELIMITER = ",";
/*    */   private final ConversionService conversionService;
/*    */   
/*    */   public CollectionToStringConverter(ConversionService conversionService) {
/* 41 */     this.conversionService = conversionService;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Set<GenericConverter.ConvertiblePair> getConvertibleTypes() {
/* 47 */     return Collections.singleton(new GenericConverter.ConvertiblePair(Collection.class, String.class));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 52 */     return ConversionUtils.canConvertElements(sourceType
/* 53 */         .getElementTypeDescriptor(), targetType, this.conversionService);
/*    */   }
/*    */ 
/*    */   
/*    */   public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 58 */     if (source == null) {
/* 59 */       return null;
/*    */     }
/* 61 */     Collection<?> sourceCollection = (Collection)source;
/* 62 */     if (sourceCollection.isEmpty()) {
/* 63 */       return "";
/*    */     }
/* 65 */     StringBuilder sb = new StringBuilder();
/* 66 */     int i = 0;
/* 67 */     for (Object sourceElement : sourceCollection) {
/* 68 */       if (i > 0) {
/* 69 */         sb.append(",");
/*    */       }
/* 71 */       Object targetElement = this.conversionService.convert(sourceElement, sourceType
/* 72 */           .elementTypeDescriptor(sourceElement), targetType);
/* 73 */       sb.append(targetElement);
/* 74 */       i++;
/*    */     } 
/* 76 */     return sb.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\convert\support\CollectionToStringConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */