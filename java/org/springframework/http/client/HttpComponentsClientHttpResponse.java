/*    */ package org.springframework.http.client;
/*    */ 
/*    */ import java.io.Closeable;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import org.apache.http.Header;
/*    */ import org.apache.http.HttpEntity;
/*    */ import org.apache.http.HttpResponse;
/*    */ import org.apache.http.util.EntityUtils;
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
/*    */ final class HttpComponentsClientHttpResponse
/*    */   extends AbstractClientHttpResponse
/*    */ {
/*    */   private final HttpResponse httpResponse;
/*    */   private HttpHeaders headers;
/*    */   
/*    */   HttpComponentsClientHttpResponse(HttpResponse httpResponse) {
/* 50 */     this.httpResponse = httpResponse;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int getRawStatusCode() throws IOException {
/* 56 */     return this.httpResponse.getStatusLine().getStatusCode();
/*    */   }
/*    */ 
/*    */   
/*    */   public String getStatusText() throws IOException {
/* 61 */     return this.httpResponse.getStatusLine().getReasonPhrase();
/*    */   }
/*    */ 
/*    */   
/*    */   public HttpHeaders getHeaders() {
/* 66 */     if (this.headers == null) {
/* 67 */       this.headers = new HttpHeaders();
/* 68 */       for (Header header : this.httpResponse.getAllHeaders()) {
/* 69 */         this.headers.add(header.getName(), header.getValue());
/*    */       }
/*    */     } 
/* 72 */     return this.headers;
/*    */   }
/*    */ 
/*    */   
/*    */   public InputStream getBody() throws IOException {
/* 77 */     HttpEntity entity = this.httpResponse.getEntity();
/* 78 */     return (entity != null) ? entity.getContent() : StreamUtils.emptyInput();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void close() {
/*    */     try {
/*    */       try {
/* 87 */         EntityUtils.consume(this.httpResponse.getEntity());
/*    */       } finally {
/*    */         
/* 90 */         if (this.httpResponse instanceof Closeable) {
/* 91 */           ((Closeable)this.httpResponse).close();
/*    */         }
/*    */       }
/*    */     
/* 95 */     } catch (IOException iOException) {}
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\http\client\HttpComponentsClientHttpResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */