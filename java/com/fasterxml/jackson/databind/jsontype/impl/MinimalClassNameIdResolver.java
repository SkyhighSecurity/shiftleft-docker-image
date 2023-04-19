/*    */ package com.fasterxml.jackson.databind.jsontype.impl;
/*    */ 
/*    */ import com.fasterxml.jackson.annotation.JsonTypeInfo;
/*    */ import com.fasterxml.jackson.databind.DatabindContext;
/*    */ import com.fasterxml.jackson.databind.JavaType;
/*    */ import com.fasterxml.jackson.databind.cfg.MapperConfig;
/*    */ import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
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
/*    */ public class MinimalClassNameIdResolver
/*    */   extends ClassNameIdResolver
/*    */ {
/*    */   protected final String _basePackageName;
/*    */   protected final String _basePackagePrefix;
/*    */   
/*    */   protected MinimalClassNameIdResolver(JavaType baseType, TypeFactory typeFactory, PolymorphicTypeValidator ptv) {
/* 30 */     super(baseType, typeFactory, ptv);
/* 31 */     String base = baseType.getRawClass().getName();
/* 32 */     int ix = base.lastIndexOf('.');
/* 33 */     if (ix < 0) {
/* 34 */       this._basePackageName = "";
/* 35 */       this._basePackagePrefix = ".";
/*    */     } else {
/* 37 */       this._basePackagePrefix = base.substring(0, ix + 1);
/* 38 */       this._basePackageName = base.substring(0, ix);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public static MinimalClassNameIdResolver construct(JavaType baseType, MapperConfig<?> config, PolymorphicTypeValidator ptv) {
/* 44 */     return new MinimalClassNameIdResolver(baseType, config.getTypeFactory(), ptv);
/*    */   }
/*    */   
/*    */   public JsonTypeInfo.Id getMechanism() {
/* 48 */     return JsonTypeInfo.Id.MINIMAL_CLASS;
/*    */   }
/*    */ 
/*    */   
/*    */   public String idFromValue(Object value) {
/* 53 */     String n = value.getClass().getName();
/* 54 */     if (n.startsWith(this._basePackagePrefix))
/*    */     {
/* 56 */       return n.substring(this._basePackagePrefix.length() - 1);
/*    */     }
/* 58 */     return n;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected JavaType _typeFromId(String id, DatabindContext ctxt) throws IOException {
/* 64 */     if (id.startsWith(".")) {
/* 65 */       StringBuilder sb = new StringBuilder(id.length() + this._basePackageName.length());
/* 66 */       if (this._basePackageName.length() == 0) {
/*    */         
/* 68 */         sb.append(id.substring(1));
/*    */       } else {
/*    */         
/* 71 */         sb.append(this._basePackageName).append(id);
/*    */       } 
/* 73 */       id = sb.toString();
/*    */     } 
/* 75 */     return super._typeFromId(id, ctxt);
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\jsontype\impl\MinimalClassNameIdResolver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */