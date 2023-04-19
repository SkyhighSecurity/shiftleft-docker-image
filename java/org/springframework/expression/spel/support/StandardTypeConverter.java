/*    */ package org.springframework.expression.spel.support;
/*    */ 
/*    */ import org.springframework.core.convert.ConversionException;
/*    */ import org.springframework.core.convert.ConversionService;
/*    */ import org.springframework.core.convert.TypeDescriptor;
/*    */ import org.springframework.core.convert.support.DefaultConversionService;
/*    */ import org.springframework.expression.TypeConverter;
/*    */ import org.springframework.expression.spel.SpelEvaluationException;
/*    */ import org.springframework.expression.spel.SpelMessage;
/*    */ import org.springframework.util.Assert;
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
/*    */ public class StandardTypeConverter
/*    */   implements TypeConverter
/*    */ {
/*    */   private final ConversionService conversionService;
/*    */   
/*    */   public StandardTypeConverter() {
/* 46 */     this.conversionService = DefaultConversionService.getSharedInstance();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public StandardTypeConverter(ConversionService conversionService) {
/* 54 */     Assert.notNull(conversionService, "ConversionService must not be null");
/* 55 */     this.conversionService = conversionService;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean canConvert(TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 61 */     return this.conversionService.canConvert(sourceType, targetType);
/*    */   }
/*    */ 
/*    */   
/*    */   public Object convertValue(Object value, TypeDescriptor sourceType, TypeDescriptor targetType) {
/*    */     try {
/* 67 */       return this.conversionService.convert(value, sourceType, targetType);
/*    */     }
/* 69 */     catch (ConversionException ex) {
/* 70 */       throw new SpelEvaluationException(ex, SpelMessage.TYPE_CONVERSION_ERROR, new Object[] { (sourceType != null) ? sourceType
/* 71 */             .toString() : ((value != null) ? value.getClass().getName() : "null"), targetType
/* 72 */             .toString() });
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\expression\spel\support\StandardTypeConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */