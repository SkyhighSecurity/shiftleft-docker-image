/*    */ package com.fasterxml.jackson.databind.deser.std;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonParser;
/*    */ import com.fasterxml.jackson.core.JsonProcessingException;
/*    */ import com.fasterxml.jackson.databind.DeserializationContext;
/*    */ import com.fasterxml.jackson.databind.util.ByteBufferBackedOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ import java.nio.ByteBuffer;
/*    */ 
/*    */ public class ByteBufferDeserializer
/*    */   extends StdScalarDeserializer<ByteBuffer> {
/*    */   protected ByteBufferDeserializer() {
/* 14 */     super(ByteBuffer.class);
/*    */   }
/*    */   private static final long serialVersionUID = 1L;
/*    */   public ByteBuffer deserialize(JsonParser parser, DeserializationContext cx) throws IOException {
/* 18 */     byte[] b = parser.getBinaryValue();
/* 19 */     return ByteBuffer.wrap(b);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public ByteBuffer deserialize(JsonParser jp, DeserializationContext ctxt, ByteBuffer intoValue) throws IOException {
/* 25 */     ByteBufferBackedOutputStream byteBufferBackedOutputStream = new ByteBufferBackedOutputStream(intoValue);
/* 26 */     jp.readBinaryValue(ctxt.getBase64Variant(), (OutputStream)byteBufferBackedOutputStream);
/* 27 */     byteBufferBackedOutputStream.close();
/* 28 */     return intoValue;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\deser\std\ByteBufferDeserializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */