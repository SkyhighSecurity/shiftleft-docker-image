/*    */ package org.springframework.http.client;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.URI;
/*    */ import okhttp3.OkHttpClient;
/*    */ import okhttp3.Request;
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
/*    */ class OkHttp3ClientHttpRequest
/*    */   extends AbstractBufferingClientHttpRequest
/*    */ {
/*    */   private final OkHttpClient client;
/*    */   private final URI uri;
/*    */   private final HttpMethod method;
/*    */   
/*    */   public OkHttp3ClientHttpRequest(OkHttpClient client, URI uri, HttpMethod method) {
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
/* 67 */     Request request = OkHttp3ClientHttpRequestFactory.buildRequest(headers, content, this.uri, this.method);
/* 68 */     return new OkHttp3ClientHttpResponse(this.client.newCall(request).execute());
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\http\client\OkHttp3ClientHttpRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */