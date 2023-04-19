/*    */ package org.springframework.http.converter;
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
/*    */ public class HttpMessageNotReadableException
/*    */   extends HttpMessageConversionException
/*    */ {
/*    */   public HttpMessageNotReadableException(String msg) {
/* 34 */     super(msg);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public HttpMessageNotReadableException(String msg, Throwable cause) {
/* 43 */     super(msg, cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\http\converter\HttpMessageNotReadableException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */