/*     */ package org.springframework.format.annotation;
/*     */ 
/*     */ import java.lang.annotation.Documented;
/*     */ import java.lang.annotation.ElementType;
/*     */ import java.lang.annotation.Retention;
/*     */ import java.lang.annotation.RetentionPolicy;
/*     */ import java.lang.annotation.Target;
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
/*     */ @Documented
/*     */ @Retention(RetentionPolicy.RUNTIME)
/*     */ @Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
/*     */ public @interface DateTimeFormat
/*     */ {
/*     */   String style() default "SS";
/*     */   
/*     */   ISO iso() default ISO.NONE;
/*     */   
/*     */   String pattern() default "";
/*     */   
/*     */   public enum ISO
/*     */   {
/*  97 */     DATE,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 103 */     TIME,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 110 */     DATE_TIME,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 115 */     NONE;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\format\annotation\DateTimeFormat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */