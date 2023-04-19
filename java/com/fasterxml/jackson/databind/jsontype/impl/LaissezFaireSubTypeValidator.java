/*    */ package com.fasterxml.jackson.databind.jsontype.impl;
/*    */ 
/*    */ import com.fasterxml.jackson.databind.JavaType;
/*    */ import com.fasterxml.jackson.databind.cfg.MapperConfig;
/*    */ import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
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
/*    */ public final class LaissezFaireSubTypeValidator
/*    */   extends PolymorphicTypeValidator.Base
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/* 20 */   public static final LaissezFaireSubTypeValidator instance = new LaissezFaireSubTypeValidator();
/*    */ 
/*    */   
/*    */   public PolymorphicTypeValidator.Validity validateBaseType(MapperConfig<?> ctxt, JavaType baseType) {
/* 24 */     return PolymorphicTypeValidator.Validity.INDETERMINATE;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public PolymorphicTypeValidator.Validity validateSubClassName(MapperConfig<?> ctxt, JavaType baseType, String subClassName) {
/* 30 */     return PolymorphicTypeValidator.Validity.ALLOWED;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public PolymorphicTypeValidator.Validity validateSubType(MapperConfig<?> ctxt, JavaType baseType, JavaType subType) {
/* 36 */     return PolymorphicTypeValidator.Validity.ALLOWED;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\jsontype\impl\LaissezFaireSubTypeValidator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */