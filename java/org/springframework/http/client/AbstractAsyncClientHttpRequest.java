/*    */ package org.springframework.http.client;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ import org.springframework.http.HttpHeaders;
/*    */ import org.springframework.util.Assert;
/*    */ import org.springframework.util.concurrent.ListenableFuture;
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
/*    */ abstract class AbstractAsyncClientHttpRequest
/*    */   implements AsyncClientHttpRequest
/*    */ {
/* 35 */   private final HttpHeaders headers = new HttpHeaders();
/*    */ 
/*    */   
/*    */   private boolean executed = false;
/*    */ 
/*    */   
/*    */   public final HttpHeaders getHeaders() {
/* 42 */     return this.executed ? HttpHeaders.readOnlyHttpHeaders(this.headers) : this.headers;
/*    */   }
/*    */ 
/*    */   
/*    */   public final OutputStream getBody() throws IOException {
/* 47 */     assertNotExecuted();
/* 48 */     return getBodyInternal(this.headers);
/*    */   }
/*    */ 
/*    */   
/*    */   public ListenableFuture<ClientHttpResponse> executeAsync() throws IOException {
/* 53 */     assertNotExecuted();
/* 54 */     ListenableFuture<ClientHttpResponse> result = executeInternal(this.headers);
/* 55 */     this.executed = true;
/* 56 */     return result;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void assertNotExecuted() {
/* 64 */     Assert.state(!this.executed, "ClientHttpRequest already executed");
/*    */   }
/*    */   
/*    */   protected abstract OutputStream getBodyInternal(HttpHeaders paramHttpHeaders) throws IOException;
/*    */   
/*    */   protected abstract ListenableFuture<ClientHttpResponse> executeInternal(HttpHeaders paramHttpHeaders) throws IOException;
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\http\client\AbstractAsyncClientHttpRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */