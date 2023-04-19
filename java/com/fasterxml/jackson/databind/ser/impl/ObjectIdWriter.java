/*    */ package com.fasterxml.jackson.databind.ser.impl;
/*    */ 
/*    */ import com.fasterxml.jackson.annotation.ObjectIdGenerator;
/*    */ import com.fasterxml.jackson.core.SerializableString;
/*    */ import com.fasterxml.jackson.core.io.SerializedString;
/*    */ import com.fasterxml.jackson.databind.JavaType;
/*    */ import com.fasterxml.jackson.databind.JsonSerializer;
/*    */ import com.fasterxml.jackson.databind.PropertyName;
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
/*    */ public final class ObjectIdWriter
/*    */ {
/*    */   public final JavaType idType;
/*    */   public final SerializableString propertyName;
/*    */   public final ObjectIdGenerator<?> generator;
/*    */   public final JsonSerializer<Object> serializer;
/*    */   public final boolean alwaysAsId;
/*    */   
/*    */   protected ObjectIdWriter(JavaType t, SerializableString propName, ObjectIdGenerator<?> gen, JsonSerializer<?> ser, boolean alwaysAsId) {
/* 53 */     this.idType = t;
/* 54 */     this.propertyName = propName;
/* 55 */     this.generator = gen;
/* 56 */     this.serializer = (JsonSerializer)ser;
/* 57 */     this.alwaysAsId = alwaysAsId;
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
/*    */   public static ObjectIdWriter construct(JavaType idType, PropertyName propName, ObjectIdGenerator<?> generator, boolean alwaysAsId) {
/* 70 */     String simpleName = (propName == null) ? null : propName.getSimpleName();
/* 71 */     SerializedString serializedString = (simpleName == null) ? null : new SerializedString(simpleName);
/* 72 */     return new ObjectIdWriter(idType, (SerializableString)serializedString, generator, null, alwaysAsId);
/*    */   }
/*    */   
/*    */   public ObjectIdWriter withSerializer(JsonSerializer<?> ser) {
/* 76 */     return new ObjectIdWriter(this.idType, this.propertyName, this.generator, ser, this.alwaysAsId);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ObjectIdWriter withAlwaysAsId(boolean newState) {
/* 83 */     if (newState == this.alwaysAsId) {
/* 84 */       return this;
/*    */     }
/* 86 */     return new ObjectIdWriter(this.idType, this.propertyName, this.generator, this.serializer, newState);
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\ser\impl\ObjectIdWriter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */