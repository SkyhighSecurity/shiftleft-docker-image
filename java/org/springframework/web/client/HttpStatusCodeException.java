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
/*    */ 
/*    */ 
/*    */ public abstract class HttpStatusCodeException
/*    */   extends RestClientResponseException
/*    */ {
/*    */   private static final long serialVersionUID = 5696801857651587810L;
/*    */   private final HttpStatus statusCode;
/*    */   
/*    */   protected HttpStatusCodeException(HttpStatus statusCode) {
/* 45 */     this(statusCode, statusCode.name(), null, null, null);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected HttpStatusCodeException(HttpStatus statusCode, String statusText) {
/* 54 */     this(statusCode, statusText, null, null, null);
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
/*    */   protected HttpStatusCodeException(HttpStatus statusCode, String statusText, byte[] responseBody, Charset responseCharset) {
/* 68 */     this(statusCode, statusText, null, responseBody, responseCharset);
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
/*    */   protected HttpStatusCodeException(HttpStatus statusCode, String statusText, HttpHeaders responseHeaders, byte[] responseBody, Charset responseCharset) {
/* 84 */     super(statusCode.value() + " " + statusText, statusCode.value(), statusText, responseHeaders, responseBody, responseCharset);
/*    */     
/* 86 */     this.statusCode = statusCode;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public HttpStatus getStatusCode() {
/* 94 */     return this.statusCode;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\client\HttpStatusCodeException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */