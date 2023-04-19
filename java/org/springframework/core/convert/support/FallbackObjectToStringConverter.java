/*    */ package org.springframework.core.convert.support;
/*    */ 
/*    */ import java.io.StringWriter;
/*    */ import java.util.Collections;
/*    */ import java.util.Set;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class FallbackObjectToStringConverter
/*    */   implements ConditionalGenericConverter
/*    */ {
/*    */   public Set<GenericConverter.ConvertiblePair> getConvertibleTypes() {
/* 47 */     return Collections.singleton(new GenericConverter.ConvertiblePair(Object.class, String.class));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 52 */     Class<?> sourceClass = sourceType.getObjectType();
/* 53 */     if (String.class == sourceClass)
/*    */     {
/* 55 */       return false;
/*    */     }
/* 57 */     return (CharSequence.class.isAssignableFrom(sourceClass) || StringWriter.class
/* 58 */       .isAssignableFrom(sourceClass) || 
/* 59 */       ObjectToObjectConverter.hasConversionMethodOrConstructor(sourceClass, String.class));
/*    */   }
/*    */ 
/*    */   
/*    */   public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 64 */     return (source != null) ? source.toString() : null;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\convert\support\FallbackObjectToStringConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */