/*    */ package com.fasterxml.jackson.databind.annotation;
/*    */ 
/*    */ import com.fasterxml.jackson.annotation.JacksonAnnotation;
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
/*    */ @Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE})
/*    */ @Retention(RetentionPolicy.RUNTIME)
/*    */ @JacksonAnnotation
/*    */ public @interface JsonPOJOBuilder
/*    */ {
/*    */   public static final String DEFAULT_BUILD_METHOD = "build";
/*    */   public static final String DEFAULT_WITH_PREFIX = "with";
/*    */   
/*    */   String buildMethodName() default "build";
/*    */   
/*    */   String withPrefix() default "with";
/*    */   
/*    */   public static class Value
/*    */   {
/*    */     public final String buildMethodName;
/*    */     public final String withPrefix;
/*    */     
/*    */     public Value(JsonPOJOBuilder ann) {
/* 87 */       this(ann.buildMethodName(), ann.withPrefix());
/*    */     }
/*    */ 
/*    */     
/*    */     public Value(String buildMethodName, String withPrefix) {
/* 92 */       this.buildMethodName = buildMethodName;
/* 93 */       this.withPrefix = withPrefix;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\annotation\JsonPOJOBuilder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */