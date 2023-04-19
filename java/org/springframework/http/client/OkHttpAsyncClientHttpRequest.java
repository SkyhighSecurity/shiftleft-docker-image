/*    */ package org.springframework.http.client;
/*    */ 
/*    */ import com.squareup.okhttp.Call;
/*    */ import com.squareup.okhttp.Callback;
/*    */ import com.squareup.okhttp.OkHttpClient;
/*    */ import com.squareup.okhttp.Request;
/*    */ import com.squareup.okhttp.Response;
/*    */ import java.io.IOException;
/*    */ import java.net.URI;
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
/*    */ class OkHttpAsyncClientHttpRequest
/*    */   extends AbstractBufferingAsyncClientHttpRequest
/*    */ {
/*    */   private final OkHttpClient client;
/*    */   private final URI uri;
/*    */   private final HttpMethod method;
/*    */   
/*    */   public OkHttpAsyncClientHttpRequest(OkHttpClient client, URI uri, HttpMethod method) {
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
/* 73 */     Request request = OkHttpClientHttpRequestFactory.buildRequest(headers, content, this.uri, this.method);
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
/*    */             public void onResponse(Response response) {
/* 87 */               OkHttpAsyncClientHttpRequest.OkHttpListenableFuture.this.set(new OkHttpClientHttpResponse(response));
/*    */             }
/*    */             
/*    */             public void onFailure(Request request, IOException ex) {
/* 91 */               OkHttpAsyncClientHttpRequest.OkHttpListenableFuture.this.setException(ex);
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


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\http\client\OkHttpAsyncClientHttpRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */