/*     */ package org.springframework.http.client;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.net.URI;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.HttpEntityEnclosingRequest;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.client.HttpClient;
/*     */ import org.apache.http.client.methods.HttpUriRequest;
/*     */ import org.apache.http.message.BasicHeader;
/*     */ import org.apache.http.protocol.HttpContext;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpMethod;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.StreamingHttpOutputMessage;
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
/*     */ final class HttpComponentsStreamingClientHttpRequest
/*     */   extends AbstractClientHttpRequest
/*     */   implements StreamingHttpOutputMessage
/*     */ {
/*     */   private final HttpClient httpClient;
/*     */   private final HttpUriRequest httpRequest;
/*     */   private final HttpContext httpContext;
/*     */   private StreamingHttpOutputMessage.Body body;
/*     */   
/*     */   HttpComponentsStreamingClientHttpRequest(HttpClient client, HttpUriRequest request, HttpContext context) {
/*  61 */     this.httpClient = client;
/*  62 */     this.httpRequest = request;
/*  63 */     this.httpContext = context;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpMethod getMethod() {
/*  69 */     return HttpMethod.resolve(this.httpRequest.getMethod());
/*     */   }
/*     */ 
/*     */   
/*     */   public URI getURI() {
/*  74 */     return this.httpRequest.getURI();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBody(StreamingHttpOutputMessage.Body body) {
/*  79 */     assertNotExecuted();
/*  80 */     this.body = body;
/*     */   }
/*     */ 
/*     */   
/*     */   protected OutputStream getBodyInternal(HttpHeaders headers) throws IOException {
/*  85 */     throw new UnsupportedOperationException("getBody not supported");
/*     */   }
/*     */ 
/*     */   
/*     */   protected ClientHttpResponse executeInternal(HttpHeaders headers) throws IOException {
/*  90 */     HttpComponentsClientHttpRequest.addHeaders(this.httpRequest, headers);
/*     */     
/*  92 */     if (this.httpRequest instanceof HttpEntityEnclosingRequest && this.body != null) {
/*  93 */       HttpEntityEnclosingRequest entityEnclosingRequest = (HttpEntityEnclosingRequest)this.httpRequest;
/*  94 */       HttpEntity requestEntity = new StreamingHttpEntity(getHeaders(), this.body);
/*  95 */       entityEnclosingRequest.setEntity(requestEntity);
/*     */     } 
/*     */     
/*  98 */     HttpResponse httpResponse = this.httpClient.execute(this.httpRequest, this.httpContext);
/*  99 */     return new HttpComponentsClientHttpResponse(httpResponse);
/*     */   }
/*     */ 
/*     */   
/*     */   private static class StreamingHttpEntity
/*     */     implements HttpEntity
/*     */   {
/*     */     private final HttpHeaders headers;
/*     */     private final StreamingHttpOutputMessage.Body body;
/*     */     
/*     */     public StreamingHttpEntity(HttpHeaders headers, StreamingHttpOutputMessage.Body body) {
/* 110 */       this.headers = headers;
/* 111 */       this.body = body;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isRepeatable() {
/* 116 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isChunked() {
/* 121 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public long getContentLength() {
/* 126 */       return this.headers.getContentLength();
/*     */     }
/*     */ 
/*     */     
/*     */     public Header getContentType() {
/* 131 */       MediaType contentType = this.headers.getContentType();
/* 132 */       return (contentType != null) ? (Header)new BasicHeader("Content-Type", contentType.toString()) : null;
/*     */     }
/*     */ 
/*     */     
/*     */     public Header getContentEncoding() {
/* 137 */       String contentEncoding = this.headers.getFirst("Content-Encoding");
/* 138 */       return (contentEncoding != null) ? (Header)new BasicHeader("Content-Encoding", contentEncoding) : null;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public InputStream getContent() throws IOException, IllegalStateException {
/* 144 */       throw new IllegalStateException("No content available");
/*     */     }
/*     */ 
/*     */     
/*     */     public void writeTo(OutputStream outputStream) throws IOException {
/* 149 */       this.body.writeTo(outputStream);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isStreaming() {
/* 154 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public void consumeContent() throws IOException {
/* 160 */       throw new UnsupportedOperationException();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\http\client\HttpComponentsStreamingClientHttpRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */