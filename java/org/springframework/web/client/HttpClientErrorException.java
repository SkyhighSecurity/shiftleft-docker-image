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
/*    */ public class HttpClientErrorException
/*    */   extends HttpStatusCodeException
/*    */ {
/*    */   private static final long serialVersionUID = 5177019431887513952L;
/*    */   
/*    */   public HttpClientErrorException(HttpStatus statusCode) {
/* 42 */     super(statusCode);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public HttpClientErrorException(HttpStatus statusCode, String statusText) {
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
/*    */   public HttpClientErrorException(HttpStatus statusCode, String statusText, byte[] responseBody, Charset responseCharset) {
/* 66 */     super(statusCode, statusText, responseBody, responseCharset);
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
/*    */   public HttpClientErrorException(HttpStatus statusCode, String statusText, HttpHeaders responseHeaders, byte[] responseBody, Charset responseCharset) {
/* 82 */     super(statusCode, statusText, responseHeaders, responseBody, responseCharset);
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\client\HttpClientErrorException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */