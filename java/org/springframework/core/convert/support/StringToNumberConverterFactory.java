/*    */ package org.springframework.core.convert.support;
/*    */ 
/*    */ import org.springframework.core.convert.converter.Converter;
/*    */ import org.springframework.core.convert.converter.ConverterFactory;
/*    */ import org.springframework.util.NumberUtils;
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
/*    */ 
/*    */ final class StringToNumberConverterFactory
/*    */   implements ConverterFactory<String, Number>
/*    */ {
/*    */   public <T extends Number> Converter<String, T> getConverter(Class<T> targetType) {
/* 45 */     return new StringToNumber<T>(targetType);
/*    */   }
/*    */   
/*    */   private static final class StringToNumber<T extends Number>
/*    */     implements Converter<String, T>
/*    */   {
/*    */     private final Class<T> targetType;
/*    */     
/*    */     public StringToNumber(Class<T> targetType) {
/* 54 */       this.targetType = targetType;
/*    */     }
/*    */ 
/*    */     
/*    */     public T convert(String source) {
/* 59 */       if (source.isEmpty()) {
/* 60 */         return null;
/*    */       }
/* 62 */       return (T)NumberUtils.parseNumber(source, this.targetType);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\convert\support\StringToNumberConverterFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */