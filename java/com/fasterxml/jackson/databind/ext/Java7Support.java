/*    */ package com.fasterxml.jackson.databind.ext;
/*    */ 
/*    */ import com.fasterxml.jackson.databind.PropertyName;
/*    */ import com.fasterxml.jackson.databind.introspect.Annotated;
/*    */ import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
/*    */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class Java7Support
/*    */ {
/*    */   private static final Java7Support IMPL;
/*    */   
/*    */   static {
/* 20 */     Java7Support impl = null;
/*    */     try {
/* 22 */       Class<?> cls = Class.forName("com.fasterxml.jackson.databind.ext.Java7SupportImpl");
/* 23 */       impl = (Java7Support)ClassUtil.createInstance(cls, false);
/* 24 */     } catch (Throwable throwable) {}
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 29 */     IMPL = impl;
/*    */   }
/*    */   
/*    */   public static Java7Support instance() {
/* 33 */     return IMPL;
/*    */   }
/*    */   
/*    */   public abstract PropertyName findConstructorName(AnnotatedParameter paramAnnotatedParameter);
/*    */   
/*    */   public abstract Boolean hasCreatorAnnotation(Annotated paramAnnotated);
/*    */   
/*    */   public abstract Boolean findTransient(Annotated paramAnnotated);
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\ext\Java7Support.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */