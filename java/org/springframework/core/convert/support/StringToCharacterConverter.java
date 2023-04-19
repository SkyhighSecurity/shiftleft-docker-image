/*    */ package org.springframework.core.convert.support;
/*    */ 
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
/*    */ final class StringToCharacterConverter
/*    */   implements Converter<String, Character>
/*    */ {
/*    */   public Character convert(String source) {
/* 31 */     if (source.isEmpty()) {
/* 32 */       return null;
/*    */     }
/* 34 */     if (source.length() > 1) {
/* 35 */       throw new IllegalArgumentException("Can only convert a [String] with length of 1 to a [Character]; string value '" + source + "'  has length of " + source
/* 36 */           .length());
/*    */     }
/* 38 */     return Character.valueOf(source.charAt(0));
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\convert\support\StringToCharacterConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */