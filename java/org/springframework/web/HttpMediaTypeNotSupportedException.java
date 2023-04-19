/*    */ package org.springframework.web;
/*    */ 
/*    */ import java.util.List;
/*    */ import org.springframework.http.MediaType;
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
/*    */ public class HttpMediaTypeNotSupportedException
/*    */   extends HttpMediaTypeException
/*    */ {
/*    */   private final MediaType contentType;
/*    */   
/*    */   public HttpMediaTypeNotSupportedException(String message) {
/* 41 */     super(message);
/* 42 */     this.contentType = null;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public HttpMediaTypeNotSupportedException(MediaType contentType, List<MediaType> supportedMediaTypes) {
/* 51 */     this(contentType, supportedMediaTypes, "Content type '" + contentType + "' not supported");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public HttpMediaTypeNotSupportedException(MediaType contentType, List<MediaType> supportedMediaTypes, String msg) {
/* 61 */     super(msg, supportedMediaTypes);
/* 62 */     this.contentType = contentType;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public MediaType getContentType() {
/* 70 */     return this.contentType;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\HttpMediaTypeNotSupportedException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */