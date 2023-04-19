/*     */ package org.springframework.http.client;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpMethod;
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
/*     */ final class SimpleStreamingClientHttpRequest
/*     */   extends AbstractClientHttpRequest
/*     */ {
/*     */   private final HttpURLConnection connection;
/*     */   private final int chunkSize;
/*     */   private OutputStream body;
/*     */   private final boolean outputStreaming;
/*     */   
/*     */   SimpleStreamingClientHttpRequest(HttpURLConnection connection, int chunkSize, boolean outputStreaming) {
/*  49 */     this.connection = connection;
/*  50 */     this.chunkSize = chunkSize;
/*  51 */     this.outputStreaming = outputStreaming;
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpMethod getMethod() {
/*  56 */     return HttpMethod.resolve(this.connection.getRequestMethod());
/*     */   }
/*     */ 
/*     */   
/*     */   public URI getURI() {
/*     */     try {
/*  62 */       return this.connection.getURL().toURI();
/*     */     }
/*  64 */     catch (URISyntaxException ex) {
/*  65 */       throw new IllegalStateException("Could not get HttpURLConnection URI: " + ex.getMessage(), ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected OutputStream getBodyInternal(HttpHeaders headers) throws IOException {
/*  71 */     if (this.body == null) {
/*  72 */       if (this.outputStreaming) {
/*  73 */         int contentLength = (int)headers.getContentLength();
/*  74 */         if (contentLength >= 0) {
/*  75 */           this.connection.setFixedLengthStreamingMode(contentLength);
/*     */         } else {
/*     */           
/*  78 */           this.connection.setChunkedStreamingMode(this.chunkSize);
/*     */         } 
/*     */       } 
/*  81 */       SimpleBufferingClientHttpRequest.addHeaders(this.connection, headers);
/*  82 */       this.connection.connect();
/*  83 */       this.body = this.connection.getOutputStream();
/*     */     } 
/*  85 */     return StreamUtils.nonClosing(this.body);
/*     */   }
/*     */ 
/*     */   
/*     */   protected ClientHttpResponse executeInternal(HttpHeaders headers) throws IOException {
/*     */     try {
/*  91 */       if (this.body != null) {
/*  92 */         this.body.close();
/*     */       } else {
/*     */         
/*  95 */         SimpleBufferingClientHttpRequest.addHeaders(this.connection, headers);
/*  96 */         this.connection.connect();
/*     */         
/*  98 */         this.connection.getResponseCode();
/*     */       }
/*     */     
/* 101 */     } catch (IOException iOException) {}
/*     */ 
/*     */     
/* 104 */     return new SimpleClientHttpResponse(this.connection);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\http\client\SimpleStreamingClientHttpRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */