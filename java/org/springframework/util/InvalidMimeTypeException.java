/*    */ package org.springframework.util;
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
/*    */ public class InvalidMimeTypeException
/*    */   extends IllegalArgumentException
/*    */ {
/*    */   private final String mimeType;
/*    */   
/*    */   public InvalidMimeTypeException(String mimeType, String message) {
/* 39 */     super("Invalid mime type \"" + mimeType + "\": " + message);
/* 40 */     this.mimeType = mimeType;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getMimeType() {
/* 48 */     return this.mimeType;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframewor\\util\InvalidMimeTypeException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */