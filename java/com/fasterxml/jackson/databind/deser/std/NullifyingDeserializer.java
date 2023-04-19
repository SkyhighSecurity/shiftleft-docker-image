/*    */ package com.fasterxml.jackson.databind.deser.std;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonParser;
/*    */ import com.fasterxml.jackson.core.JsonToken;
/*    */ import com.fasterxml.jackson.databind.DeserializationConfig;
/*    */ import com.fasterxml.jackson.databind.DeserializationContext;
/*    */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NullifyingDeserializer
/*    */   extends StdDeserializer<Object>
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/* 20 */   public static final NullifyingDeserializer instance = new NullifyingDeserializer();
/*    */   public NullifyingDeserializer() {
/* 22 */     super(Object.class);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Boolean supportsUpdate(DeserializationConfig config) {
/* 32 */     return Boolean.FALSE;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
/* 40 */     if (p.hasToken(JsonToken.FIELD_NAME)) {
/*    */       while (true) {
/* 42 */         JsonToken t = p.nextToken();
/* 43 */         if (t == null || t == JsonToken.END_OBJECT) {
/*    */           break;
/*    */         }
/* 46 */         p.skipChildren();
/*    */       } 
/*    */     } else {
/* 49 */       p.skipChildren();
/*    */     } 
/* 51 */     return null;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Object deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException {
/* 60 */     switch (p.getCurrentTokenId()) {
/*    */       case 1:
/*    */       case 3:
/*    */       case 5:
/* 64 */         return typeDeserializer.deserializeTypedFromAny(p, ctxt);
/*    */     } 
/* 66 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\deser\std\NullifyingDeserializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */