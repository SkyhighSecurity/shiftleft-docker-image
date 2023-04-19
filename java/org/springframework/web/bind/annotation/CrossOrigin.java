/*    */ package org.springframework.web.bind.annotation;
/*    */ 
/*    */ import java.lang.annotation.Documented;
/*    */ import java.lang.annotation.ElementType;
/*    */ import java.lang.annotation.Retention;
/*    */ import java.lang.annotation.RetentionPolicy;
/*    */ import java.lang.annotation.Target;
/*    */ import org.springframework.core.annotation.AliasFor;
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
/*    */ @Target({ElementType.METHOD, ElementType.TYPE})
/*    */ @Retention(RetentionPolicy.RUNTIME)
/*    */ @Documented
/*    */ public @interface CrossOrigin
/*    */ {
/*    */   @Deprecated
/* 58 */   public static final String[] DEFAULT_ORIGINS = new String[] { "*" };
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Deprecated
/* 64 */   public static final String[] DEFAULT_ALLOWED_HEADERS = new String[] { "*" };
/*    */   @Deprecated
/*    */   public static final boolean DEFAULT_ALLOW_CREDENTIALS = true;
/*    */   @Deprecated
/*    */   public static final long DEFAULT_MAX_AGE = 1800L;
/*    */   
/*    */   @AliasFor("origins")
/*    */   String[] value() default {};
/*    */   
/*    */   @AliasFor("value")
/*    */   String[] origins() default {};
/*    */   
/*    */   String[] allowedHeaders() default {};
/*    */   
/*    */   String[] exposedHeaders() default {};
/*    */   
/*    */   RequestMethod[] methods() default {};
/*    */   
/*    */   String allowCredentials() default "";
/*    */   
/*    */   long maxAge() default -1L;
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\bind\annotation\CrossOrigin.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */