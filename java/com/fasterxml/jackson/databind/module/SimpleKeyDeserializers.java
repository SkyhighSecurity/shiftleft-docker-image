/*    */ package com.fasterxml.jackson.databind.module;
/*    */ 
/*    */ import com.fasterxml.jackson.databind.BeanDescription;
/*    */ import com.fasterxml.jackson.databind.DeserializationConfig;
/*    */ import com.fasterxml.jackson.databind.JavaType;
/*    */ import com.fasterxml.jackson.databind.KeyDeserializer;
/*    */ import com.fasterxml.jackson.databind.deser.KeyDeserializers;
/*    */ import com.fasterxml.jackson.databind.type.ClassKey;
/*    */ import java.io.Serializable;
/*    */ import java.util.HashMap;
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
/*    */ public class SimpleKeyDeserializers
/*    */   implements KeyDeserializers, Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/* 27 */   protected HashMap<ClassKey, KeyDeserializer> _classMappings = null;
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
/*    */   public SimpleKeyDeserializers addDeserializer(Class<?> forClass, KeyDeserializer deser) {
/* 39 */     if (this._classMappings == null) {
/* 40 */       this._classMappings = new HashMap<>();
/*    */     }
/* 42 */     this._classMappings.put(new ClassKey(forClass), deser);
/* 43 */     return this;
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
/*    */   public KeyDeserializer findKeyDeserializer(JavaType type, DeserializationConfig config, BeanDescription beanDesc) {
/* 56 */     if (this._classMappings == null) {
/* 57 */       return null;
/*    */     }
/* 59 */     return this._classMappings.get(new ClassKey(type.getRawClass()));
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\module\SimpleKeyDeserializers.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */