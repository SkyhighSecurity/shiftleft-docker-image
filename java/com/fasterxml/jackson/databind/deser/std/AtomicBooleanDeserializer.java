/*    */ package com.fasterxml.jackson.databind.deser.std;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonParser;
/*    */ import com.fasterxml.jackson.core.JsonProcessingException;
/*    */ import com.fasterxml.jackson.databind.DeserializationContext;
/*    */ import java.io.IOException;
/*    */ import java.util.concurrent.atomic.AtomicBoolean;
/*    */ 
/*    */ public class AtomicBooleanDeserializer extends StdScalarDeserializer<AtomicBoolean> {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public AtomicBooleanDeserializer() {
/* 13 */     super(AtomicBoolean.class);
/*    */   }
/*    */   
/*    */   public AtomicBoolean deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
/* 17 */     return new AtomicBoolean(_parseBooleanPrimitive(jp, ctxt));
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\deser\std\AtomicBooleanDeserializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */