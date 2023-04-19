/*    */ package com.fasterxml.jackson.databind.introspect;
/*    */ 
/*    */ import com.fasterxml.jackson.databind.JavaType;
/*    */ import com.fasterxml.jackson.databind.type.TypeBindings;
/*    */ import com.fasterxml.jackson.databind.type.TypeFactory;
/*    */ import java.lang.reflect.Type;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface TypeResolutionContext
/*    */ {
/*    */   JavaType resolveType(Type paramType);
/*    */   
/*    */   public static class Basic
/*    */     implements TypeResolutionContext
/*    */   {
/*    */     private final TypeFactory _typeFactory;
/*    */     private final TypeBindings _bindings;
/*    */     
/*    */     public Basic(TypeFactory tf, TypeBindings b) {
/* 25 */       this._typeFactory = tf;
/* 26 */       this._bindings = b;
/*    */     }
/*    */ 
/*    */     
/*    */     public JavaType resolveType(Type type) {
/* 31 */       return this._typeFactory.constructType(type, this._bindings);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\introspect\TypeResolutionContext.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */