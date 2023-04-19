/*     */ package com.fasterxml.jackson.databind;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.cfg.MapperConfig;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedField;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PropertyNamingStrategy
/*     */   implements Serializable
/*     */ {
/*  40 */   public static final PropertyNamingStrategy SNAKE_CASE = new SnakeCaseStrategy();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  49 */   public static final PropertyNamingStrategy UPPER_CAMEL_CASE = new UpperCamelCaseStrategy();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  59 */   public static final PropertyNamingStrategy LOWER_CAMEL_CASE = new PropertyNamingStrategy();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  68 */   public static final PropertyNamingStrategy LOWER_CASE = new LowerCaseStrategy();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  77 */   public static final PropertyNamingStrategy KEBAB_CASE = new KebabCaseStrategy();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  86 */   public static final PropertyNamingStrategy LOWER_DOT_CASE = new LowerDotCaseStrategy();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String nameForField(MapperConfig<?> config, AnnotatedField field, String defaultName) {
/* 110 */     return defaultName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String nameForGetterMethod(MapperConfig<?> config, AnnotatedMethod method, String defaultName) {
/* 131 */     return defaultName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String nameForSetterMethod(MapperConfig<?> config, AnnotatedMethod method, String defaultName) {
/* 151 */     return defaultName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String nameForConstructorParameter(MapperConfig<?> config, AnnotatedParameter ctorParam, String defaultName) {
/* 169 */     return defaultName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static abstract class PropertyNamingStrategyBase
/*     */     extends PropertyNamingStrategy
/*     */   {
/*     */     public String nameForField(MapperConfig<?> config, AnnotatedField field, String defaultName) {
/* 183 */       return translate(defaultName);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String nameForGetterMethod(MapperConfig<?> config, AnnotatedMethod method, String defaultName) {
/* 189 */       return translate(defaultName);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String nameForSetterMethod(MapperConfig<?> config, AnnotatedMethod method, String defaultName) {
/* 195 */       return translate(defaultName);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String nameForConstructorParameter(MapperConfig<?> config, AnnotatedParameter ctorParam, String defaultName) {
/* 202 */       return translate(defaultName);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public abstract String translate(String param1String);
/*     */ 
/*     */ 
/*     */     
/*     */     protected static String translateLowerCaseWithSeparator(String input, char separator) {
/* 212 */       if (input == null) {
/* 213 */         return input;
/*     */       }
/* 215 */       int length = input.length();
/* 216 */       if (length == 0) {
/* 217 */         return input;
/*     */       }
/*     */       
/* 220 */       StringBuilder result = new StringBuilder(length + (length >> 1));
/* 221 */       int upperCount = 0;
/* 222 */       for (int i = 0; i < length; i++) {
/* 223 */         char ch = input.charAt(i);
/* 224 */         char lc = Character.toLowerCase(ch);
/*     */         
/* 226 */         if (lc == ch) {
/*     */ 
/*     */           
/* 229 */           if (upperCount > 1)
/*     */           {
/* 231 */             result.insert(result.length() - 1, separator);
/*     */           }
/* 233 */           upperCount = 0;
/*     */         } else {
/*     */           
/* 236 */           if (upperCount == 0 && i > 0) {
/* 237 */             result.append(separator);
/*     */           }
/* 239 */           upperCount++;
/*     */         } 
/* 241 */         result.append(lc);
/*     */       } 
/* 243 */       return result.toString();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class SnakeCaseStrategy
/*     */     extends PropertyNamingStrategyBase
/*     */   {
/*     */     public String translate(String input) {
/* 309 */       if (input == null) return input; 
/* 310 */       int length = input.length();
/* 311 */       StringBuilder result = new StringBuilder(length * 2);
/* 312 */       int resultLength = 0;
/* 313 */       boolean wasPrevTranslated = false;
/* 314 */       for (int i = 0; i < length; i++) {
/*     */         
/* 316 */         char c = input.charAt(i);
/* 317 */         if (i > 0 || c != '_') {
/*     */           
/* 319 */           if (Character.isUpperCase(c)) {
/*     */             
/* 321 */             if (!wasPrevTranslated && resultLength > 0 && result.charAt(resultLength - 1) != '_') {
/*     */               
/* 323 */               result.append('_');
/* 324 */               resultLength++;
/*     */             } 
/* 326 */             c = Character.toLowerCase(c);
/* 327 */             wasPrevTranslated = true;
/*     */           }
/*     */           else {
/*     */             
/* 331 */             wasPrevTranslated = false;
/*     */           } 
/* 333 */           result.append(c);
/* 334 */           resultLength++;
/*     */         } 
/*     */       } 
/* 337 */       return (resultLength > 0) ? result.toString() : input;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class UpperCamelCaseStrategy
/*     */     extends PropertyNamingStrategyBase
/*     */   {
/*     */     public String translate(String input) {
/* 369 */       if (input == null || input.length() == 0) {
/* 370 */         return input;
/*     */       }
/*     */       
/* 373 */       char c = input.charAt(0);
/* 374 */       char uc = Character.toUpperCase(c);
/* 375 */       if (c == uc) {
/* 376 */         return input;
/*     */       }
/* 378 */       StringBuilder sb = new StringBuilder(input);
/* 379 */       sb.setCharAt(0, uc);
/* 380 */       return sb.toString();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class LowerCaseStrategy
/*     */     extends PropertyNamingStrategyBase
/*     */   {
/*     */     public String translate(String input) {
/* 396 */       return input.toLowerCase();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class KebabCaseStrategy
/*     */     extends PropertyNamingStrategyBase
/*     */   {
/*     */     public String translate(String input) {
/* 411 */       return translateLowerCaseWithSeparator(input, '-');
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class LowerDotCaseStrategy
/*     */     extends PropertyNamingStrategyBase
/*     */   {
/*     */     public String translate(String input) {
/* 424 */       return translateLowerCaseWithSeparator(input, '.');
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/* 438 */   public static final PropertyNamingStrategy CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES = SNAKE_CASE;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/* 444 */   public static final PropertyNamingStrategy PASCAL_CASE_TO_CAMEL_CASE = UPPER_CAMEL_CASE;
/*     */   
/*     */   @Deprecated
/*     */   public static class LowerCaseWithUnderscoresStrategy extends SnakeCaseStrategy {}
/*     */   
/*     */   @Deprecated
/*     */   public static class PascalCaseStrategy extends UpperCamelCaseStrategy {}
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\PropertyNamingStrategy.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */