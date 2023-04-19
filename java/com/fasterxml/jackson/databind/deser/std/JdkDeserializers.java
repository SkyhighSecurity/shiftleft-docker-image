/*    */ package com.fasterxml.jackson.databind.deser.std;
/*    */ 
/*    */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*    */ import java.nio.ByteBuffer;
/*    */ import java.util.HashSet;
/*    */ import java.util.UUID;
/*    */ import java.util.concurrent.atomic.AtomicBoolean;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JdkDeserializers
/*    */ {
/* 15 */   private static final HashSet<String> _classNames = new HashSet<>();
/*    */   
/*    */   static {
/* 18 */     Class<?>[] types = new Class[] { UUID.class, AtomicBoolean.class, StackTraceElement.class, ByteBuffer.class, Void.class };
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 25 */     for (Class<?> cls : types) _classNames.add(cls.getName()); 
/* 26 */     for (Class<?> cls : FromStringDeserializer.types()) _classNames.add(cls.getName());
/*    */   
/*    */   }
/*    */   
/*    */   public static JsonDeserializer<?> find(Class<?> rawType, String clsName) {
/* 31 */     if (_classNames.contains(clsName)) {
/* 32 */       JsonDeserializer<?> d = FromStringDeserializer.findDeserializer(rawType);
/* 33 */       if (d != null) {
/* 34 */         return d;
/*    */       }
/* 36 */       if (rawType == UUID.class) {
/* 37 */         return new UUIDDeserializer();
/*    */       }
/* 39 */       if (rawType == StackTraceElement.class) {
/* 40 */         return new StackTraceElementDeserializer();
/*    */       }
/* 42 */       if (rawType == AtomicBoolean.class)
/*    */       {
/* 44 */         return new AtomicBooleanDeserializer();
/*    */       }
/* 46 */       if (rawType == ByteBuffer.class) {
/* 47 */         return new ByteBufferDeserializer();
/*    */       }
/* 49 */       if (rawType == Void.class) {
/* 50 */         return NullifyingDeserializer.instance;
/*    */       }
/*    */     } 
/* 53 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\deser\std\JdkDeserializers.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */