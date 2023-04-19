/*    */ package com.fasterxml.jackson.databind.ser.impl;
/*    */ 
/*    */ import com.fasterxml.jackson.annotation.ObjectIdGenerator;
/*    */ import com.fasterxml.jackson.core.JsonGenerator;
/*    */ import com.fasterxml.jackson.core.SerializableString;
/*    */ import com.fasterxml.jackson.databind.SerializerProvider;
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
/*    */ public final class WritableObjectId
/*    */ {
/*    */   public final ObjectIdGenerator<?> generator;
/*    */   public Object id;
/*    */   protected boolean idWritten = false;
/*    */   
/*    */   public WritableObjectId(ObjectIdGenerator<?> generator) {
/* 29 */     this.generator = generator;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean writeAsId(JsonGenerator gen, SerializerProvider provider, ObjectIdWriter w) throws IOException {
/* 34 */     if (this.id != null && (this.idWritten || w.alwaysAsId)) {
/*    */       
/* 36 */       if (gen.canWriteObjectId()) {
/* 37 */         gen.writeObjectRef(String.valueOf(this.id));
/*    */       } else {
/* 39 */         w.serializer.serialize(this.id, gen, provider);
/*    */       } 
/* 41 */       return true;
/*    */     } 
/* 43 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Object generateId(Object forPojo) {
/* 50 */     if (this.id == null) {
/* 51 */       this.id = this.generator.generateId(forPojo);
/*    */     }
/* 53 */     return this.id;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void writeAsField(JsonGenerator gen, SerializerProvider provider, ObjectIdWriter w) throws IOException {
/* 62 */     this.idWritten = true;
/*    */ 
/*    */     
/* 65 */     if (gen.canWriteObjectId()) {
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 70 */       String idStr = (this.id == null) ? null : String.valueOf(this.id);
/* 71 */       gen.writeObjectId(idStr);
/*    */       
/*    */       return;
/*    */     } 
/* 75 */     SerializableString name = w.propertyName;
/* 76 */     if (name != null) {
/*    */       
/* 78 */       gen.writeFieldName(name);
/* 79 */       w.serializer.serialize(this.id, gen, provider);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\ser\impl\WritableObjectId.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */