/*    */ package com.fasterxml.jackson.databind.ser.std;
/*    */ import com.fasterxml.jackson.core.JsonGenerator;
/*    */ import com.fasterxml.jackson.databind.JavaType;
/*    */ import com.fasterxml.jackson.databind.JsonMappingException;
/*    */ import com.fasterxml.jackson.databind.SerializerProvider;
/*    */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonArrayFormatVisitor;
/*    */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatTypes;
/*    */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
/*    */ import com.fasterxml.jackson.databind.util.ByteBufferBackedInputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.nio.ByteBuffer;
/*    */ 
/*    */ public class ByteBufferSerializer extends StdScalarSerializer<ByteBuffer> {
/*    */   public ByteBufferSerializer() {
/* 16 */     super(ByteBuffer.class);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void serialize(ByteBuffer bbuf, JsonGenerator gen, SerializerProvider provider) throws IOException {
/* 22 */     if (bbuf.hasArray()) {
/* 23 */       gen.writeBinary(bbuf.array(), bbuf.arrayOffset(), bbuf.limit());
/*    */       
/*    */       return;
/*    */     } 
/*    */     
/* 28 */     ByteBuffer copy = bbuf.asReadOnlyBuffer();
/* 29 */     if (copy.position() > 0) {
/* 30 */       copy.rewind();
/*    */     }
/* 32 */     ByteBufferBackedInputStream byteBufferBackedInputStream = new ByteBufferBackedInputStream(copy);
/* 33 */     gen.writeBinary((InputStream)byteBufferBackedInputStream, copy.remaining());
/* 34 */     byteBufferBackedInputStream.close();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
/* 42 */     JsonArrayFormatVisitor v2 = visitor.expectArrayFormat(typeHint);
/* 43 */     if (v2 != null)
/* 44 */       v2.itemsFormat(JsonFormatTypes.INTEGER); 
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\ser\std\ByteBufferSerializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */