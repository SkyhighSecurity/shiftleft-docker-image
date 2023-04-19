/*    */ package org.springframework.http.client;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import org.apache.http.Header;
/*    */ import org.apache.http.HttpEntity;
/*    */ import org.apache.http.HttpResponse;
/*    */ import org.springframework.http.HttpHeaders;
/*    */ import org.springframework.util.StreamUtils;
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
/*    */ final class HttpComponentsAsyncClientHttpResponse
/*    */   extends AbstractClientHttpResponse
/*    */ {
/*    */   private final HttpResponse httpResponse;
/*    */   private HttpHeaders headers;
/*    */   
/*    */   HttpComponentsAsyncClientHttpResponse(HttpResponse httpResponse) {
/* 48 */     this.httpResponse = httpResponse;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int getRawStatusCode() throws IOException {
/* 54 */     return this.httpResponse.getStatusLine().getStatusCode();
/*    */   }
/*    */ 
/*    */   
/*    */   public String getStatusText() throws IOException {
/* 59 */     return this.httpResponse.getStatusLine().getReasonPhrase();
/*    */   }
/*    */ 
/*    */   
/*    */   public HttpHeaders getHeaders() {
/* 64 */     if (this.headers == null) {
/* 65 */       this.headers = new HttpHeaders();
/* 66 */       for (Header header : this.httpResponse.getAllHeaders()) {
/* 67 */         this.headers.add(header.getName(), header.getValue());
/*    */       }
/*    */     } 
/* 70 */     return this.headers;
/*    */   }
/*    */ 
/*    */   
/*    */   public InputStream getBody() throws IOException {
/* 75 */     HttpEntity entity = this.httpResponse.getEntity();
/* 76 */     return (entity != null) ? entity.getContent() : StreamUtils.emptyInput();
/*    */   }
/*    */   
/*    */   public void close() {}
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\http\client\HttpComponentsAsyncClientHttpResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */