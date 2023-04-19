/*    */ package org.springframework.core.convert.support;
/*    */ 
/*    */ import org.springframework.core.convert.ConversionService;
/*    */ import org.springframework.core.convert.converter.Converter;
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
/*    */ final class EnumToStringConverter
/*    */   extends AbstractConditionalEnumConverter
/*    */   implements Converter<Enum<?>, String>
/*    */ {
/*    */   public EnumToStringConverter(ConversionService conversionService) {
/* 33 */     super(conversionService);
/*    */   }
/*    */ 
/*    */   
/*    */   public String convert(Enum<?> source) {
/* 38 */     return source.name();
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\convert\support\EnumToStringConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */