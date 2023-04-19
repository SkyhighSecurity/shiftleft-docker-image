/*    */ package org.springframework.http;
/*    */ 
/*    */ import org.springframework.util.InvalidMimeTypeException;
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
/*    */ public class InvalidMediaTypeException
/*    */   extends IllegalArgumentException
/*    */ {
/*    */   private String mediaType;
/*    */   
/*    */   public InvalidMediaTypeException(String mediaType, String message) {
/* 40 */     super("Invalid media type \"" + mediaType + "\": " + message);
/* 41 */     this.mediaType = mediaType;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   InvalidMediaTypeException(InvalidMimeTypeException ex) {
/* 48 */     super(ex.getMessage(), (Throwable)ex);
/* 49 */     this.mediaType = ex.getMimeType();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getMediaType() {
/* 57 */     return this.mediaType;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\http\InvalidMediaTypeException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */