/*    */ package org.springframework.core.convert.support;
/*    */ 
/*    */ import java.lang.reflect.Array;
/*    */ import java.util.Collections;
/*    */ import java.util.Set;
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
/*    */ final class StringToArrayConverter
/*    */   implements ConditionalGenericConverter
/*    */ {
/*    */   private final ConversionService conversionService;
/*    */   
/*    */   public StringToArrayConverter(ConversionService conversionService) {
/* 40 */     this.conversionService = conversionService;
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<GenericConverter.ConvertiblePair> getConvertibleTypes() {
/* 45 */     return Collections.singleton(new GenericConverter.ConvertiblePair(String.class, Object[].class));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 50 */     return this.conversionService.canConvert(sourceType, targetType.getElementTypeDescriptor());
/*    */   }
/*    */ 
/*    */   
/*    */   public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 55 */     if (source == null) {
/* 56 */       return null;
/*    */     }
/* 58 */     String string = (String)source;
/* 59 */     String[] fields = StringUtils.commaDelimitedListToStringArray(string);
/* 60 */     Object target = Array.newInstance(targetType.getElementTypeDescriptor().getType(), fields.length);
/* 61 */     for (int i = 0; i < fields.length; i++) {
/* 62 */       String sourceElement = fields[i];
/* 63 */       Object targetElement = this.conversionService.convert(sourceElement.trim(), sourceType, targetType.getElementTypeDescriptor());
/* 64 */       Array.set(target, i, targetElement);
/*    */     } 
/* 66 */     return target;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\convert\support\StringToArrayConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */