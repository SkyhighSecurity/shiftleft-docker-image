/*    */ package com.shn.shiftleft;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ShiftLeftInlineException
/*    */   extends RuntimeException
/*    */ {
/*    */   public ShiftLeftInlineException(String message) {
/*  9 */     super(message);
/*    */   }
/*    */   
/*    */   public ShiftLeftInlineException(String message, Throwable cause) {
/* 13 */     super(message, cause);
/*    */   }
/*    */   
/*    */   public ShiftLeftInlineException(Throwable cause) {
/* 17 */     this(cause.getMessage(), cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\shn\shiftleft\ShiftLeftInlineException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */