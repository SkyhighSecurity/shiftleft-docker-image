/*    */ package org.springframework.http.client;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.net.HttpURLConnection;
/*    */ import org.springframework.http.HttpHeaders;
/*    */ import org.springframework.util.StreamUtils;
/*    */ import org.springframework.util.StringUtils;
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
/*    */ final class SimpleClientHttpResponse
/*    */   extends AbstractClientHttpResponse
/*    */ {
/*    */   private final HttpURLConnection connection;
/*    */   private HttpHeaders headers;
/*    */   private InputStream responseStream;
/*    */   
/*    */   SimpleClientHttpResponse(HttpURLConnection connection) {
/* 46 */     this.connection = connection;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int getRawStatusCode() throws IOException {
/* 52 */     return this.connection.getResponseCode();
/*    */   }
/*    */ 
/*    */   
/*    */   public String getStatusText() throws IOException {
/* 57 */     return this.connection.getResponseMessage();
/*    */   }
/*    */ 
/*    */   
/*    */   public HttpHeaders getHeaders() {
/* 62 */     if (this.headers == null) {
/* 63 */       this.headers = new HttpHeaders();
/*    */       
/* 65 */       String name = this.connection.getHeaderFieldKey(0);
/* 66 */       if (StringUtils.hasLength(name)) {
/* 67 */         this.headers.add(name, this.connection.getHeaderField(0));
/*    */       }
/* 69 */       int i = 1;
/*    */       while (true) {
/* 71 */         name = this.connection.getHeaderFieldKey(i);
/* 72 */         if (!StringUtils.hasLength(name)) {
/*    */           break;
/*    */         }
/* 75 */         this.headers.add(name, this.connection.getHeaderField(i));
/* 76 */         i++;
/*    */       } 
/*    */     } 
/* 79 */     return this.headers;
/*    */   }
/*    */ 
/*    */   
/*    */   public InputStream getBody() throws IOException {
/* 84 */     InputStream errorStream = this.connection.getErrorStream();
/* 85 */     this.responseStream = (errorStream != null) ? errorStream : this.connection.getInputStream();
/* 86 */     return this.responseStream;
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() {
/* 91 */     if (this.responseStream != null)
/*    */       try {
/* 93 */         StreamUtils.drain(this.responseStream);
/* 94 */         this.responseStream.close();
/*    */       }
/* 96 */       catch (Exception exception) {} 
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\http\client\SimpleClientHttpResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */