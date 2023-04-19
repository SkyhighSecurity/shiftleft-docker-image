/*    */ package org.springframework.core.convert.support;
/*    */ 
/*    */ import java.lang.reflect.Array;
/*    */ import java.util.Collection;
/*    */ import java.util.LinkedHashSet;
/*    */ import java.util.Optional;
/*    */ import java.util.Set;
/*    */ import org.springframework.core.convert.ConversionService;
/*    */ import org.springframework.core.convert.TypeDescriptor;
/*    */ import org.springframework.core.convert.converter.ConditionalGenericConverter;
/*    */ import org.springframework.core.convert.converter.GenericConverter;
/*    */ import org.springframework.lang.UsesJava8;
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
/*    */ @UsesJava8
/*    */ final class ObjectToOptionalConverter
/*    */   implements ConditionalGenericConverter
/*    */ {
/*    */   private final ConversionService conversionService;
/*    */   
/*    */   public ObjectToOptionalConverter(ConversionService conversionService) {
/* 46 */     this.conversionService = conversionService;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Set<GenericConverter.ConvertiblePair> getConvertibleTypes() {
/* 52 */     Set<GenericConverter.ConvertiblePair> convertibleTypes = new LinkedHashSet<GenericConverter.ConvertiblePair>(4);
/* 53 */     convertibleTypes.add(new GenericConverter.ConvertiblePair(Collection.class, Optional.class));
/* 54 */     convertibleTypes.add(new GenericConverter.ConvertiblePair(Object[].class, Optional.class));
/* 55 */     convertibleTypes.add(new GenericConverter.ConvertiblePair(Object.class, Optional.class));
/* 56 */     return convertibleTypes;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 61 */     if (targetType.getResolvableType() != null) {
/* 62 */       return this.conversionService.canConvert(sourceType, new GenericTypeDescriptor(targetType));
/*    */     }
/*    */     
/* 65 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 71 */     if (source == null) {
/* 72 */       return Optional.empty();
/*    */     }
/* 74 */     if (source instanceof Optional) {
/* 75 */       return source;
/*    */     }
/* 77 */     if (targetType.getResolvableType() != null) {
/* 78 */       Object target = this.conversionService.convert(source, sourceType, new GenericTypeDescriptor(targetType));
/* 79 */       if (target == null || (target.getClass().isArray() && Array.getLength(target) == 0) || (target instanceof Collection && ((Collection)target)
/* 80 */         .isEmpty())) {
/* 81 */         return Optional.empty();
/*    */       }
/* 83 */       return Optional.of(target);
/*    */     } 
/*    */     
/* 86 */     return Optional.of(source);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private static class GenericTypeDescriptor
/*    */     extends TypeDescriptor
/*    */   {
/*    */     public GenericTypeDescriptor(TypeDescriptor typeDescriptor) {
/* 95 */       super(typeDescriptor.getResolvableType().getGeneric(new int[0]), null, typeDescriptor.getAnnotations());
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\convert\support\ObjectToOptionalConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */