/*    */ package org.springframework.format.annotation;
/*    */ 
/*    */ import java.lang.annotation.Documented;
/*    */ import java.lang.annotation.ElementType;
/*    */ import java.lang.annotation.Retention;
/*    */ import java.lang.annotation.RetentionPolicy;
/*    */ import java.lang.annotation.Target;
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
/*    */ @Documented
/*    */ @Retention(RetentionPolicy.RUNTIME)
/*    */ @Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
/*    */ public @interface NumberFormat
/*    */ {
/*    */   Style style() default Style.DEFAULT;
/*    */   
/*    */   String pattern() default "";
/*    */   
/*    */   public enum Style
/*    */   {
/* 80 */     DEFAULT,
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 85 */     NUMBER,
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 90 */     PERCENT,
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 95 */     CURRENCY;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\format\annotation\NumberFormat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */