/*    */ package org.springframework.http.client;
/*    */ 
/*    */ import java.io.ByteArrayInputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import org.springframework.http.HttpHeaders;
/*    */ import org.springframework.http.HttpStatus;
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
/*    */ final class BufferingClientHttpResponseWrapper
/*    */   implements ClientHttpResponse
/*    */ {
/*    */   private final ClientHttpResponse response;
/*    */   private byte[] body;
/*    */   
/*    */   BufferingClientHttpResponseWrapper(ClientHttpResponse response) {
/* 42 */     this.response = response;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public HttpStatus getStatusCode() throws IOException {
/* 48 */     return this.response.getStatusCode();
/*    */   }
/*    */ 
/*    */   
/*    */   public int getRawStatusCode() throws IOException {
/* 53 */     return this.response.getRawStatusCode();
/*    */   }
/*    */ 
/*    */   
/*    */   public String getStatusText() throws IOException {
/* 58 */     return this.response.getStatusText();
/*    */   }
/*    */ 
/*    */   
/*    */   public HttpHeaders getHeaders() {
/* 63 */     return this.response.getHeaders();
/*    */   }
/*    */ 
/*    */   
/*    */   public InputStream getBody() throws IOException {
/* 68 */     if (this.body == null) {
/* 69 */       this.body = StreamUtils.copyToByteArray(this.response.getBody());
/*    */     }
/* 71 */     return new ByteArrayInputStream(this.body);
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() {
/* 76 */     this.response.close();
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\http\client\BufferingClientHttpResponseWrapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */