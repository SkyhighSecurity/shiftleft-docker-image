/*    */ package org.springframework.web.client;
/*    */ 
/*    */ import java.nio.charset.Charset;
/*    */ import org.springframework.http.HttpHeaders;
/*    */ import org.springframework.http.HttpStatus;
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
/*    */ public class HttpServerErrorException
/*    */   extends HttpStatusCodeException
/*    */ {
/*    */   private static final long serialVersionUID = -2915754006618138282L;
/*    */   
/*    */   public HttpServerErrorException(HttpStatus statusCode) {
/* 42 */     super(statusCode);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public HttpServerErrorException(HttpStatus statusCode, String statusText) {
/* 52 */     super(statusCode, statusText);
/*    */   }
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
/*    */   public HttpServerErrorException(HttpStatus statusCode, String statusText, byte[] responseBody, Charset responseCharset) {
/* 67 */     super(statusCode, statusText, responseBody, responseCharset);
/*    */   }
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
/*    */   public HttpServerErrorException(HttpStatus statusCode, String statusText, HttpHeaders responseHeaders, byte[] responseBody, Charset responseCharset) {
/* 83 */     super(statusCode, statusText, responseHeaders, responseBody, responseCharset);
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\client\HttpServerErrorException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */