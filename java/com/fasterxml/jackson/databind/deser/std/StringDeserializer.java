/*    */ package com.fasterxml.jackson.databind.deser.std;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonParser;
/*    */ import com.fasterxml.jackson.core.JsonProcessingException;
/*    */ import com.fasterxml.jackson.core.JsonToken;
/*    */ import com.fasterxml.jackson.databind.DeserializationContext;
/*    */ import com.fasterxml.jackson.databind.JsonMappingException;
/*    */ import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
/*    */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ @JacksonStdImpl
/*    */ public class StringDeserializer
/*    */   extends StdScalarDeserializer<String>
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/* 18 */   public static final StringDeserializer instance = new StringDeserializer();
/*    */   public StringDeserializer() {
/* 20 */     super(String.class);
/*    */   }
/*    */   
/*    */   public boolean isCachable() {
/* 24 */     return true;
/*    */   }
/*    */   
/*    */   public Object getEmptyValue(DeserializationContext ctxt) throws JsonMappingException {
/* 28 */     return "";
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
/* 34 */     if (p.hasToken(JsonToken.VALUE_STRING)) {
/* 35 */       return p.getText();
/*    */     }
/* 37 */     JsonToken t = p.getCurrentToken();
/*    */     
/* 39 */     if (t == JsonToken.START_ARRAY) {
/* 40 */       return _deserializeFromArray(p, ctxt);
/*    */     }
/*    */     
/* 43 */     if (t == JsonToken.VALUE_EMBEDDED_OBJECT) {
/* 44 */       Object ob = p.getEmbeddedObject();
/* 45 */       if (ob == null) {
/* 46 */         return null;
/*    */       }
/* 48 */       if (ob instanceof byte[]) {
/* 49 */         return ctxt.getBase64Variant().encode((byte[])ob, false);
/*    */       }
/*    */       
/* 52 */       return ob.toString();
/*    */     } 
/*    */ 
/*    */ 
/*    */     
/* 57 */     if (t.isScalarValue()) {
/* 58 */       String text = p.getValueAsString();
/* 59 */       if (text != null) {
/* 60 */         return text;
/*    */       }
/*    */     } 
/* 63 */     return (String)ctxt.handleUnexpectedToken(this._valueClass, p);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException {
/* 71 */     return deserialize(p, ctxt);
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\deser\std\StringDeserializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */