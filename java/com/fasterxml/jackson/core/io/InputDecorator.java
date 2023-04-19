/*    */ package com.fasterxml.jackson.core.io;
/*    */ 
/*    */ import java.io.DataInput;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.Reader;
/*    */ import java.io.Serializable;
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
/*    */ public abstract class InputDecorator
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public abstract InputStream decorate(IOContext paramIOContext, InputStream paramInputStream) throws IOException;
/*    */   
/*    */   public abstract InputStream decorate(IOContext paramIOContext, byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws IOException;
/*    */   
/*    */   public DataInput decorate(IOContext ctxt, DataInput input) throws IOException {
/* 73 */     throw new UnsupportedOperationException();
/*    */   }
/*    */   
/*    */   public abstract Reader decorate(IOContext paramIOContext, Reader paramReader) throws IOException;
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\core\io\InputDecorator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */