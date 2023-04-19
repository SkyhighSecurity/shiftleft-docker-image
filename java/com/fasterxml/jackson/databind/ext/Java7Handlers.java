/*    */ package com.fasterxml.jackson.databind.ext;
/*    */ 
/*    */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*    */ import com.fasterxml.jackson.databind.JsonSerializer;
/*    */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*    */ import java.util.logging.Logger;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class Java7Handlers
/*    */ {
/*    */   private static final Java7Handlers IMPL;
/*    */   
/*    */   static {
/* 21 */     Java7Handlers impl = null;
/*    */     try {
/* 23 */       Class<?> cls = Class.forName("com.fasterxml.jackson.databind.ext.Java7HandlersImpl");
/* 24 */       impl = (Java7Handlers)ClassUtil.createInstance(cls, false);
/* 25 */     } catch (Throwable t) {
/*    */ 
/*    */       
/* 28 */       Logger.getLogger(Java7Handlers.class.getName())
/* 29 */         .warning("Unable to load JDK7 types (java.nio.file.Path): no Java7 type support added");
/*    */     } 
/* 31 */     IMPL = impl;
/*    */   }
/*    */   
/*    */   public static Java7Handlers instance() {
/* 35 */     return IMPL;
/*    */   }
/*    */   
/*    */   public abstract JsonSerializer<?> getSerializerForJavaNioFilePath(Class<?> paramClass);
/*    */   
/*    */   public abstract JsonDeserializer<?> getDeserializerForJavaNioFilePath(Class<?> paramClass);
/*    */   
/*    */   public abstract Class<?> getClassJavaNioFilePath();
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\ext\Java7Handlers.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */