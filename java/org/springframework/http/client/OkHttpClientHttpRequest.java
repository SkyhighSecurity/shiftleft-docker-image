/*    */ package org.springframework.http.client;
/*    */ 
/*    */ import com.squareup.okhttp.OkHttpClient;
/*    */ import com.squareup.okhttp.Request;
/*    */ import java.io.IOException;
/*    */ import java.net.URI;
/*    */ import org.springframework.http.HttpHeaders;
/*    */ import org.springframework.http.HttpMethod;
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
/*    */ class OkHttpClientHttpRequest
/*    */   extends AbstractBufferingClientHttpRequest
/*    */ {
/*    */   private final OkHttpClient client;
/*    */   private final URI uri;
/*    */   private final HttpMethod method;
/*    */   
/*    */   public OkHttpClientHttpRequest(OkHttpClient client, URI uri, HttpMethod method) {
/* 48 */     this.client = client;
/* 49 */     this.uri = uri;
/* 50 */     this.method = method;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public HttpMethod getMethod() {
/* 56 */     return this.method;
/*    */   }
/*    */ 
/*    */   
/*    */   public URI getURI() {
/* 61 */     return this.uri;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected ClientHttpResponse executeInternal(HttpHeaders headers, byte[] content) throws IOException {
/* 67 */     Request request = OkHttpClientHttpRequestFactory.buildRequest(headers, content, this.uri, this.method);
/* 68 */     return new OkHttpClientHttpResponse(this.client.newCall(request).execute());
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\http\client\OkHttpClientHttpRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */