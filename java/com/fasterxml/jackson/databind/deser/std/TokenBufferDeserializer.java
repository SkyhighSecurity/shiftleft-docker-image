/*    */ package com.fasterxml.jackson.databind.deser.std;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonParser;
/*    */ import com.fasterxml.jackson.core.JsonProcessingException;
/*    */ import com.fasterxml.jackson.databind.DeserializationContext;
/*    */ import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
/*    */ import com.fasterxml.jackson.databind.util.TokenBuffer;
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
/*    */ @JacksonStdImpl
/*    */ public class TokenBufferDeserializer
/*    */   extends StdScalarDeserializer<TokenBuffer>
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public TokenBufferDeserializer() {
/* 27 */     super(TokenBuffer.class);
/*    */   }
/*    */   
/*    */   public TokenBuffer deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
/* 31 */     return createBufferInstance(p).deserialize(p, ctxt);
/*    */   }
/*    */   
/*    */   protected TokenBuffer createBufferInstance(JsonParser p) {
/* 35 */     return new TokenBuffer(p);
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\deser\std\TokenBufferDeserializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */