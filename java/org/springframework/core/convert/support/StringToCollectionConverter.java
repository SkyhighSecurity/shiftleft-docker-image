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
/*    */ import org.springframework.util.StringUtils;
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
/*    */ final class StringToCollectionConverter
/*    */   implements ConditionalGenericConverter
/*    */ {
/*    */   private final ConversionService conversionService;
/*    */   
/*    */   public StringToCollectionConverter(ConversionService conversionService) {
/* 44 */     this.conversionService = conversionService;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Set<GenericConverter.ConvertiblePair> getConvertibleTypes() {
/* 50 */     return Collections.singleton(new GenericConverter.ConvertiblePair(String.class, Collection.class));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 55 */     return (targetType.getElementTypeDescriptor() == null || this.conversionService
/* 56 */       .canConvert(sourceType, targetType.getElementTypeDescriptor()));
/*    */   }
/*    */ 
/*    */   
/*    */   public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 61 */     if (source == null) {
/* 62 */       return null;
/*    */     }
/* 64 */     String string = (String)source;
/*    */     
/* 66 */     String[] fields = StringUtils.commaDelimitedListToStringArray(string);
/* 67 */     TypeDescriptor elementDesc = targetType.getElementTypeDescriptor();
/* 68 */     Collection<Object> target = CollectionFactory.createCollection(targetType.getType(), (elementDesc != null) ? elementDesc
/* 69 */         .getType() : null, fields.length);
/*    */     
/* 71 */     if (elementDesc == null) {
/* 72 */       for (String field : fields) {
/* 73 */         target.add(field.trim());
/*    */       }
/*    */     } else {
/*    */       
/* 77 */       for (String field : fields) {
/* 78 */         Object targetElement = this.conversionService.convert(field.trim(), sourceType, elementDesc);
/* 79 */         target.add(targetElement);
/*    */       } 
/*    */     } 
/* 82 */     return target;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\convert\support\StringToCollectionConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */