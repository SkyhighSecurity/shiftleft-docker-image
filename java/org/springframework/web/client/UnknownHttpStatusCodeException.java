/*    */ package org.springframework.web.client;
/*    */ 
/*    */ import java.nio.charset.Charset;
/*    */ import org.springframework.http.HttpHeaders;
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
/*    */ public class UnknownHttpStatusCodeException
/*    */   extends RestClientResponseException
/*    */ {
/*    */   private static final long serialVersionUID = 7103980251635005491L;
/*    */   
/*    */   public UnknownHttpStatusCodeException(int rawStatusCode, String statusText, HttpHeaders responseHeaders, byte[] responseBody, Charset responseCharset) {
/* 47 */     super("Unknown status code [" + rawStatusCode + "] " + statusText, rawStatusCode, statusText, responseHeaders, responseBody, responseCharset);
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\client\UnknownHttpStatusCodeException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */