/*    */ package com.fasterxml.jackson.databind.jsontype.impl;
/*    */ 
/*    */ import com.fasterxml.jackson.annotation.JsonTypeInfo;
/*    */ import com.fasterxml.jackson.core.JsonGenerator;
/*    */ import com.fasterxml.jackson.databind.BeanProperty;
/*    */ import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
/*    */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*    */ import com.fasterxml.jackson.databind.util.ClassUtil;
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
/*    */ public class AsWrapperTypeSerializer
/*    */   extends TypeSerializerBase
/*    */ {
/*    */   public AsWrapperTypeSerializer(TypeIdResolver idRes, BeanProperty property) {
/* 23 */     super(idRes, property);
/*    */   }
/*    */ 
/*    */   
/*    */   public AsWrapperTypeSerializer forProperty(BeanProperty prop) {
/* 28 */     return (this._property == prop) ? this : new AsWrapperTypeSerializer(this._idResolver, prop);
/*    */   }
/*    */   
/*    */   public JsonTypeInfo.As getTypeInclusion() {
/* 32 */     return JsonTypeInfo.As.WRAPPER_OBJECT;
/*    */   }
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
/*    */   protected String _validTypeId(String typeId) {
/* 47 */     return ClassUtil.nonNullString(typeId);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected final void _writeTypeId(JsonGenerator g, String typeId) throws IOException {
/* 53 */     if (typeId != null)
/* 54 */       g.writeTypeId(typeId); 
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\jsontype\impl\AsWrapperTypeSerializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */