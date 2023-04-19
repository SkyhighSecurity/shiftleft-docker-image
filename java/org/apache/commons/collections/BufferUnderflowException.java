/*    */ package org.apache.commons.collections;
/*    */ 
/*    */ import java.util.NoSuchElementException;
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
/*    */ public class BufferUnderflowException
/*    */   extends NoSuchElementException
/*    */ {
/*    */   private final Throwable throwable;
/*    */   
/*    */   public BufferUnderflowException() {
/* 45 */     this.throwable = null;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public BufferUnderflowException(String message) {
/* 54 */     this(message, null);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public BufferUnderflowException(String message, Throwable exception) {
/* 64 */     super(message);
/* 65 */     this.throwable = exception;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final Throwable getCause() {
/* 74 */     return this.throwable;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\apache\commons\collections\BufferUnderflowException.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */