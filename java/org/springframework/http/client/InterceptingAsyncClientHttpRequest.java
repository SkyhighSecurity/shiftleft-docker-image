/*     */ package org.springframework.http.client;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpMethod;
/*     */ import org.springframework.http.HttpRequest;
/*     */ import org.springframework.util.StreamUtils;
/*     */ import org.springframework.util.concurrent.ListenableFuture;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class InterceptingAsyncClientHttpRequest
/*     */   extends AbstractBufferingAsyncClientHttpRequest
/*     */ {
/*     */   private AsyncClientHttpRequestFactory requestFactory;
/*     */   private List<AsyncClientHttpRequestInterceptor> interceptors;
/*     */   private URI uri;
/*     */   private HttpMethod httpMethod;
/*     */   
/*     */   public InterceptingAsyncClientHttpRequest(AsyncClientHttpRequestFactory requestFactory, List<AsyncClientHttpRequestInterceptor> interceptors, URI uri, HttpMethod httpMethod) {
/*  59 */     this.requestFactory = requestFactory;
/*  60 */     this.interceptors = interceptors;
/*  61 */     this.uri = uri;
/*  62 */     this.httpMethod = httpMethod;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ListenableFuture<ClientHttpResponse> executeInternal(HttpHeaders headers, byte[] body) throws IOException {
/*  70 */     return (new AsyncRequestExecution()).executeAsync(this, body);
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpMethod getMethod() {
/*  75 */     return this.httpMethod;
/*     */   }
/*     */ 
/*     */   
/*     */   public URI getURI() {
/*  80 */     return this.uri;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class AsyncRequestExecution
/*     */     implements AsyncClientHttpRequestExecution
/*     */   {
/*  89 */     private Iterator<AsyncClientHttpRequestInterceptor> iterator = InterceptingAsyncClientHttpRequest.this.interceptors.iterator();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ListenableFuture<ClientHttpResponse> executeAsync(HttpRequest request, byte[] body) throws IOException {
/*  96 */       if (this.iterator.hasNext()) {
/*  97 */         AsyncClientHttpRequestInterceptor interceptor = this.iterator.next();
/*  98 */         return interceptor.intercept(request, body, this);
/*     */       } 
/*     */       
/* 101 */       URI uri = request.getURI();
/* 102 */       HttpMethod method = request.getMethod();
/* 103 */       HttpHeaders headers = request.getHeaders();
/*     */       
/* 105 */       AsyncClientHttpRequest delegate = InterceptingAsyncClientHttpRequest.this.requestFactory.createAsyncRequest(uri, method);
/* 106 */       delegate.getHeaders().putAll((Map)headers);
/* 107 */       if (body.length > 0) {
/* 108 */         StreamUtils.copy(body, delegate.getBody());
/*     */       }
/*     */       
/* 111 */       return delegate.executeAsync();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\http\client\InterceptingAsyncClientHttpRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */