/*    */ package org.springframework.core.convert.support;
/*    */ 
/*    */ import org.springframework.core.convert.ConversionFailedException;
/*    */ import org.springframework.core.convert.ConversionService;
/*    */ import org.springframework.core.convert.TypeDescriptor;
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
/*    */ abstract class ConversionUtils
/*    */ {
/*    */   public static Object invokeConverter(GenericConverter converter, Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
/*    */     try {
/* 37 */       return converter.convert(source, sourceType, targetType);
/*    */     }
/* 39 */     catch (ConversionFailedException ex) {
/* 40 */       throw ex;
/*    */     }
/* 42 */     catch (Throwable ex) {
/* 43 */       throw new ConversionFailedException(sourceType, targetType, source, ex);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static boolean canConvertElements(TypeDescriptor sourceElementType, TypeDescriptor targetElementType, ConversionService conversionService) {
/* 50 */     if (targetElementType == null)
/*    */     {
/* 52 */       return true;
/*    */     }
/* 54 */     if (sourceElementType == null)
/*    */     {
/* 56 */       return true;
/*    */     }
/* 58 */     if (conversionService.canConvert(sourceElementType, targetElementType))
/*    */     {
/* 60 */       return true;
/*    */     }
/* 62 */     if (sourceElementType.getType().isAssignableFrom(targetElementType.getType()))
/*    */     {
/* 64 */       return true;
/*    */     }
/*    */     
/* 67 */     return false;
/*    */   }
/*    */   
/*    */   public static Class<?> getEnumType(Class<?> targetType) {
/* 71 */     Class<?> enumType = targetType;
/* 72 */     while (enumType != null && !enumType.isEnum()) {
/* 73 */       enumType = enumType.getSuperclass();
/*    */     }
/* 75 */     if (enumType == null) {
/* 76 */       throw new IllegalArgumentException("The target type " + targetType
/* 77 */           .getName() + " does not refer to an enum");
/*    */     }
/* 79 */     return enumType;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\convert\support\ConversionUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */