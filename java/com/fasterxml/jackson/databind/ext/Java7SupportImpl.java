/*    */ package com.fasterxml.jackson.databind.ext;
/*    */ 
/*    */ import com.fasterxml.jackson.databind.PropertyName;
/*    */ import com.fasterxml.jackson.databind.introspect.Annotated;
/*    */ import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
/*    */ import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
/*    */ import java.beans.ConstructorProperties;
/*    */ import java.beans.Transient;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Java7SupportImpl
/*    */   extends Java7Support
/*    */ {
/*    */   private final Class<?> _bogus;
/*    */   
/*    */   public Java7SupportImpl() {
/* 22 */     Class<?> cls = Transient.class;
/* 23 */     cls = ConstructorProperties.class;
/* 24 */     this._bogus = cls;
/*    */   }
/*    */ 
/*    */   
/*    */   public Boolean findTransient(Annotated a) {
/* 29 */     Transient t = (Transient)a.getAnnotation(Transient.class);
/* 30 */     if (t != null) {
/* 31 */       return Boolean.valueOf(t.value());
/*    */     }
/* 33 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public Boolean hasCreatorAnnotation(Annotated a) {
/* 38 */     ConstructorProperties props = (ConstructorProperties)a.getAnnotation(ConstructorProperties.class);
/*    */ 
/*    */     
/* 41 */     if (props != null) {
/* 42 */       return Boolean.TRUE;
/*    */     }
/* 44 */     return null;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public PropertyName findConstructorName(AnnotatedParameter p) {
/* 50 */     AnnotatedWithParams ctor = p.getOwner();
/* 51 */     if (ctor != null) {
/* 52 */       ConstructorProperties props = (ConstructorProperties)ctor.getAnnotation(ConstructorProperties.class);
/* 53 */       if (props != null) {
/* 54 */         String[] names = props.value();
/* 55 */         int ix = p.getIndex();
/* 56 */         if (ix < names.length) {
/* 57 */           return PropertyName.construct(names[ix]);
/*    */         }
/*    */       } 
/*    */     } 
/* 61 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\ext\Java7SupportImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */