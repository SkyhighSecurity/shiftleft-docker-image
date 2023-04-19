/*    */ package org.springframework.http.client.support;
/*    */ 
/*    */ import java.net.URI;
/*    */ import org.springframework.http.HttpHeaders;
/*    */ import org.springframework.http.HttpMethod;
/*    */ import org.springframework.http.HttpRequest;
/*    */ import org.springframework.util.Assert;
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
/*    */ public class HttpRequestWrapper
/*    */   implements HttpRequest
/*    */ {
/*    */   private final HttpRequest request;
/*    */   
/*    */   public HttpRequestWrapper(HttpRequest request) {
/* 45 */     Assert.notNull(request, "HttpRequest must not be null");
/* 46 */     this.request = request;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public HttpRequest getRequest() {
/* 54 */     return this.request;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public HttpMethod getMethod() {
/* 62 */     return this.request.getMethod();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public URI getURI() {
/* 70 */     return this.request.getURI();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public HttpHeaders getHeaders() {
/* 78 */     return this.request.getHeaders();
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\http\client\support\HttpRequestWrapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */