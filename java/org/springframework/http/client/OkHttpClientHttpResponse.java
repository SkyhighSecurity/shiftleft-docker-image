/*    */ package org.springframework.http.client;
/*    */ 
/*    */ import com.squareup.okhttp.Response;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import org.springframework.http.HttpHeaders;
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
/*    */ class OkHttpClientHttpResponse
/*    */   extends AbstractClientHttpResponse
/*    */ {
/*    */   private final Response response;
/*    */   private HttpHeaders headers;
/*    */   
/*    */   public OkHttpClientHttpResponse(Response response) {
/* 43 */     Assert.notNull(response, "Response must not be null");
/* 44 */     this.response = response;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int getRawStatusCode() {
/* 50 */     return this.response.code();
/*    */   }
/*    */ 
/*    */   
/*    */   public String getStatusText() {
/* 55 */     return this.response.message();
/*    */   }
/*    */ 
/*    */   
/*    */   public InputStream getBody() throws IOException {
/* 60 */     return this.response.body().byteStream();
/*    */   }
/*    */ 
/*    */   
/*    */   public HttpHeaders getHeaders() {
/* 65 */     if (this.headers == null) {
/* 66 */       HttpHeaders headers = new HttpHeaders();
/* 67 */       for (String headerName : this.response.headers().names()) {
/* 68 */         for (String headerValue : this.response.headers(headerName)) {
/* 69 */           headers.add(headerName, headerValue);
/*    */         }
/*    */       } 
/* 72 */       this.headers = headers;
/*    */     } 
/* 74 */     return this.headers;
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() {
/*    */     try {
/* 80 */       this.response.body().close();
/*    */     }
/* 82 */     catch (IOException iOException) {}
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\http\client\OkHttpClientHttpResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */