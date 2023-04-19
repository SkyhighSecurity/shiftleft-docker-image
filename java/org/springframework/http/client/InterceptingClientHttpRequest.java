/*     */ package org.springframework.http.client;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.net.URI;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpMethod;
/*     */ import org.springframework.http.HttpRequest;
/*     */ import org.springframework.http.StreamingHttpOutputMessage;
/*     */ import org.springframework.util.StreamUtils;
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
/*     */ class InterceptingClientHttpRequest
/*     */   extends AbstractBufferingClientHttpRequest
/*     */ {
/*     */   private final ClientHttpRequestFactory requestFactory;
/*     */   private final List<ClientHttpRequestInterceptor> interceptors;
/*     */   private HttpMethod method;
/*     */   private URI uri;
/*     */   
/*     */   protected InterceptingClientHttpRequest(ClientHttpRequestFactory requestFactory, List<ClientHttpRequestInterceptor> interceptors, URI uri, HttpMethod method) {
/*  52 */     this.requestFactory = requestFactory;
/*  53 */     this.interceptors = interceptors;
/*  54 */     this.method = method;
/*  55 */     this.uri = uri;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpMethod getMethod() {
/*  61 */     return this.method;
/*     */   }
/*     */ 
/*     */   
/*     */   public URI getURI() {
/*  66 */     return this.uri;
/*     */   }
/*     */ 
/*     */   
/*     */   protected final ClientHttpResponse executeInternal(HttpHeaders headers, byte[] bufferedOutput) throws IOException {
/*  71 */     InterceptingRequestExecution requestExecution = new InterceptingRequestExecution();
/*  72 */     return requestExecution.execute(this, bufferedOutput);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class InterceptingRequestExecution
/*     */     implements ClientHttpRequestExecution
/*     */   {
/*  81 */     private final Iterator<ClientHttpRequestInterceptor> iterator = InterceptingClientHttpRequest.this.interceptors.iterator();
/*     */ 
/*     */ 
/*     */     
/*     */     public ClientHttpResponse execute(HttpRequest request, final byte[] body) throws IOException {
/*  86 */       if (this.iterator.hasNext()) {
/*  87 */         ClientHttpRequestInterceptor nextInterceptor = this.iterator.next();
/*  88 */         return nextInterceptor.intercept(request, body, this);
/*     */       } 
/*     */       
/*  91 */       ClientHttpRequest delegate = InterceptingClientHttpRequest.this.requestFactory.createRequest(request.getURI(), request.getMethod());
/*  92 */       for (Map.Entry<String, List<String>> entry : (Iterable<Map.Entry<String, List<String>>>)request.getHeaders().entrySet()) {
/*  93 */         List<String> values = entry.getValue();
/*  94 */         for (String value : values) {
/*  95 */           delegate.getHeaders().add(entry.getKey(), value);
/*     */         }
/*     */       } 
/*  98 */       if (body.length > 0) {
/*  99 */         if (delegate instanceof StreamingHttpOutputMessage) {
/* 100 */           StreamingHttpOutputMessage streamingOutputMessage = (StreamingHttpOutputMessage)delegate;
/* 101 */           streamingOutputMessage.setBody(new StreamingHttpOutputMessage.Body()
/*     */               {
/*     */                 public void writeTo(OutputStream outputStream) throws IOException {
/* 104 */                   StreamUtils.copy(body, outputStream);
/*     */                 }
/*     */               });
/*     */         } else {
/*     */           
/* 109 */           StreamUtils.copy(body, delegate.getBody());
/*     */         } 
/*     */       }
/* 112 */       return delegate.execute();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\http\client\InterceptingClientHttpRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */