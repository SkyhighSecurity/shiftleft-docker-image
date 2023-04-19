/*    */ package org.springframework.http.client;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.URI;
/*    */ import okhttp3.Call;
/*    */ import okhttp3.Callback;
/*    */ import okhttp3.OkHttpClient;
/*    */ import okhttp3.Request;
/*    */ import okhttp3.Response;
/*    */ import org.springframework.http.HttpHeaders;
/*    */ import org.springframework.http.HttpMethod;
/*    */ import org.springframework.util.concurrent.ListenableFuture;
/*    */ import org.springframework.util.concurrent.SettableListenableFuture;
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
/*    */ class OkHttp3AsyncClientHttpRequest
/*    */   extends AbstractBufferingAsyncClientHttpRequest
/*    */ {
/*    */   private final OkHttpClient client;
/*    */   private final URI uri;
/*    */   private final HttpMethod method;
/*    */   
/*    */   public OkHttp3AsyncClientHttpRequest(OkHttpClient client, URI uri, HttpMethod method) {
/* 53 */     this.client = client;
/* 54 */     this.uri = uri;
/* 55 */     this.method = method;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public HttpMethod getMethod() {
/* 61 */     return this.method;
/*    */   }
/*    */ 
/*    */   
/*    */   public URI getURI() {
/* 66 */     return this.uri;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected ListenableFuture<ClientHttpResponse> executeInternal(HttpHeaders headers, byte[] content) throws IOException {
/* 73 */     Request request = OkHttp3ClientHttpRequestFactory.buildRequest(headers, content, this.uri, this.method);
/* 74 */     return (ListenableFuture<ClientHttpResponse>)new OkHttpListenableFuture(this.client.newCall(request));
/*    */   }
/*    */   
/*    */   private static class OkHttpListenableFuture
/*    */     extends SettableListenableFuture<ClientHttpResponse>
/*    */   {
/*    */     private final Call call;
/*    */     
/*    */     public OkHttpListenableFuture(Call call) {
/* 83 */       this.call = call;
/* 84 */       this.call.enqueue(new Callback()
/*    */           {
/*    */             public void onResponse(Call call, Response response) {
/* 87 */               OkHttp3AsyncClientHttpRequest.OkHttpListenableFuture.this.set(new OkHttp3ClientHttpResponse(response));
/*    */             }
/*    */             
/*    */             public void onFailure(Call call, IOException ex) {
/* 91 */               OkHttp3AsyncClientHttpRequest.OkHttpListenableFuture.this.setException(ex);
/*    */             }
/*    */           });
/*    */     }
/*    */ 
/*    */     
/*    */     protected void interruptTask() {
/* 98 */       this.call.cancel();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\http\client\OkHttp3AsyncClientHttpRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */