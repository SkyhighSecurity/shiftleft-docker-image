/*    */ package com.fasterxml.jackson.databind.jsontype.impl;
/*    */ 
/*    */ import com.fasterxml.jackson.databind.DatabindContext;
/*    */ import com.fasterxml.jackson.databind.JavaType;
/*    */ import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
/*    */ import com.fasterxml.jackson.databind.type.TypeFactory;
/*    */ import java.io.IOException;
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
/*    */ public abstract class TypeIdResolverBase
/*    */   implements TypeIdResolver
/*    */ {
/*    */   protected final TypeFactory _typeFactory;
/*    */   protected final JavaType _baseType;
/*    */   
/*    */   protected TypeIdResolverBase() {
/* 34 */     this(null, null);
/*    */   }
/*    */   
/*    */   protected TypeIdResolverBase(JavaType baseType, TypeFactory typeFactory) {
/* 38 */     this._baseType = baseType;
/* 39 */     this._typeFactory = typeFactory;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void init(JavaType bt) {}
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String idFromBaseType() {
/* 53 */     return idFromValueAndType(null, this._baseType.getRawClass());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public JavaType typeFromId(DatabindContext context, String id) throws IOException {
/* 60 */     throw new IllegalStateException("Sub-class " + getClass().getName() + " MUST implement `typeFromId(DatabindContext,String)");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getDescForKnownTypeIds() {
/* 70 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\jsontype\impl\TypeIdResolverBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */