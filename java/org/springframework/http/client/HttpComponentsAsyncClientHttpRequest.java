/*     */ package org.springframework.http.client;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.Future;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.HttpEntityEnclosingRequest;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.client.methods.HttpUriRequest;
/*     */ import org.apache.http.concurrent.FutureCallback;
/*     */ import org.apache.http.nio.client.HttpAsyncClient;
/*     */ import org.apache.http.nio.entity.NByteArrayEntity;
/*     */ import org.apache.http.protocol.HttpContext;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpMethod;
/*     */ import org.springframework.util.concurrent.FailureCallback;
/*     */ import org.springframework.util.concurrent.FutureAdapter;
/*     */ import org.springframework.util.concurrent.ListenableFuture;
/*     */ import org.springframework.util.concurrent.ListenableFutureCallback;
/*     */ import org.springframework.util.concurrent.ListenableFutureCallbackRegistry;
/*     */ import org.springframework.util.concurrent.SuccessCallback;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class HttpComponentsAsyncClientHttpRequest
/*     */   extends AbstractBufferingAsyncClientHttpRequest
/*     */ {
/*     */   private final HttpAsyncClient httpClient;
/*     */   private final HttpUriRequest httpRequest;
/*     */   private final HttpContext httpContext;
/*     */   
/*     */   HttpComponentsAsyncClientHttpRequest(HttpAsyncClient client, HttpUriRequest request, HttpContext context) {
/*  63 */     this.httpClient = client;
/*  64 */     this.httpRequest = request;
/*  65 */     this.httpContext = context;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpMethod getMethod() {
/*  71 */     return HttpMethod.resolve(this.httpRequest.getMethod());
/*     */   }
/*     */ 
/*     */   
/*     */   public URI getURI() {
/*  76 */     return this.httpRequest.getURI();
/*     */   }
/*     */   
/*     */   HttpContext getHttpContext() {
/*  80 */     return this.httpContext;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ListenableFuture<ClientHttpResponse> executeInternal(HttpHeaders headers, byte[] bufferedOutput) throws IOException {
/*  87 */     HttpComponentsClientHttpRequest.addHeaders(this.httpRequest, headers);
/*     */     
/*  89 */     if (this.httpRequest instanceof HttpEntityEnclosingRequest) {
/*  90 */       HttpEntityEnclosingRequest entityEnclosingRequest = (HttpEntityEnclosingRequest)this.httpRequest;
/*  91 */       NByteArrayEntity nByteArrayEntity = new NByteArrayEntity(bufferedOutput);
/*  92 */       entityEnclosingRequest.setEntity((HttpEntity)nByteArrayEntity);
/*     */     } 
/*     */     
/*  95 */     HttpResponseFutureCallback callback = new HttpResponseFutureCallback(this.httpRequest);
/*  96 */     Future<HttpResponse> futureResponse = this.httpClient.execute(this.httpRequest, this.httpContext, callback);
/*  97 */     return new ClientHttpResponseFuture(futureResponse, callback);
/*     */   }
/*     */ 
/*     */   
/*     */   private static class HttpResponseFutureCallback
/*     */     implements FutureCallback<HttpResponse>
/*     */   {
/*     */     private final HttpUriRequest request;
/* 105 */     private final ListenableFutureCallbackRegistry<ClientHttpResponse> callbacks = new ListenableFutureCallbackRegistry();
/*     */ 
/*     */     
/*     */     public HttpResponseFutureCallback(HttpUriRequest request) {
/* 109 */       this.request = request;
/*     */     }
/*     */     
/*     */     public void addCallback(ListenableFutureCallback<? super ClientHttpResponse> callback) {
/* 113 */       this.callbacks.addCallback(callback);
/*     */     }
/*     */     
/*     */     public void addSuccessCallback(SuccessCallback<? super ClientHttpResponse> callback) {
/* 117 */       this.callbacks.addSuccessCallback(callback);
/*     */     }
/*     */     
/*     */     public void addFailureCallback(FailureCallback callback) {
/* 121 */       this.callbacks.addFailureCallback(callback);
/*     */     }
/*     */ 
/*     */     
/*     */     public void completed(HttpResponse result) {
/* 126 */       this.callbacks.success(new HttpComponentsAsyncClientHttpResponse(result));
/*     */     }
/*     */ 
/*     */     
/*     */     public void failed(Exception ex) {
/* 131 */       this.callbacks.failure(ex);
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancelled() {
/* 136 */       this.request.abort();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class ClientHttpResponseFuture
/*     */     extends FutureAdapter<ClientHttpResponse, HttpResponse>
/*     */     implements ListenableFuture<ClientHttpResponse>
/*     */   {
/*     */     private final HttpComponentsAsyncClientHttpRequest.HttpResponseFutureCallback callback;
/*     */     
/*     */     public ClientHttpResponseFuture(Future<HttpResponse> response, HttpComponentsAsyncClientHttpRequest.HttpResponseFutureCallback callback) {
/* 147 */       super(response);
/* 148 */       this.callback = callback;
/*     */     }
/*     */ 
/*     */     
/*     */     protected ClientHttpResponse adapt(HttpResponse response) {
/* 153 */       return new HttpComponentsAsyncClientHttpResponse(response);
/*     */     }
/*     */ 
/*     */     
/*     */     public void addCallback(ListenableFutureCallback<? super ClientHttpResponse> callback) {
/* 158 */       this.callback.addCallback(callback);
/*     */     }
/*     */ 
/*     */     
/*     */     public void addCallback(SuccessCallback<? super ClientHttpResponse> successCallback, FailureCallback failureCallback) {
/* 163 */       this.callback.addSuccessCallback(successCallback);
/* 164 */       this.callback.addFailureCallback(failureCallback);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\http\client\HttpComponentsAsyncClientHttpRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */