/*    */ package org.springframework.core.convert.support;
/*    */ 
/*    */ import java.lang.reflect.Array;
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
/*    */ final class ObjectToArrayConverter
/*    */   implements ConditionalGenericConverter
/*    */ {
/*    */   private final ConversionService conversionService;
/*    */   
/*    */   public ObjectToArrayConverter(ConversionService conversionService) {
/* 40 */     this.conversionService = conversionService;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Set<GenericConverter.ConvertiblePair> getConvertibleTypes() {
/* 46 */     return Collections.singleton(new GenericConverter.ConvertiblePair(Object.class, Object[].class));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 51 */     return ConversionUtils.canConvertElements(sourceType, targetType.getElementTypeDescriptor(), this.conversionService);
/*    */   }
/*    */ 
/*    */   
/*    */   public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 56 */     if (source == null) {
/* 57 */       return null;
/*    */     }
/* 59 */     Object target = Array.newInstance(targetType.getElementTypeDescriptor().getType(), 1);
/* 60 */     Object targetElement = this.conversionService.convert(source, sourceType, targetType.getElementTypeDescriptor());
/* 61 */     Array.set(target, 0, targetElement);
/* 62 */     return target;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\convert\support\ObjectToArrayConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */