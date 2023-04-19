/*     */ package org.springframework.http.client;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpMethod;
/*     */ import org.springframework.util.FileCopyUtils;
/*     */ import org.springframework.util.StringUtils;
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
/*     */ final class SimpleBufferingClientHttpRequest
/*     */   extends AbstractBufferingClientHttpRequest
/*     */ {
/*     */   private final HttpURLConnection connection;
/*     */   private final boolean outputStreaming;
/*     */   
/*     */   SimpleBufferingClientHttpRequest(HttpURLConnection connection, boolean outputStreaming) {
/*  48 */     this.connection = connection;
/*  49 */     this.outputStreaming = outputStreaming;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpMethod getMethod() {
/*  55 */     return HttpMethod.resolve(this.connection.getRequestMethod());
/*     */   }
/*     */ 
/*     */   
/*     */   public URI getURI() {
/*     */     try {
/*  61 */       return this.connection.getURL().toURI();
/*     */     }
/*  63 */     catch (URISyntaxException ex) {
/*  64 */       throw new IllegalStateException("Could not get HttpURLConnection URI: " + ex.getMessage(), ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected ClientHttpResponse executeInternal(HttpHeaders headers, byte[] bufferedOutput) throws IOException {
/*  70 */     addHeaders(this.connection, headers);
/*     */     
/*  72 */     if (getMethod() == HttpMethod.DELETE && bufferedOutput.length == 0) {
/*  73 */       this.connection.setDoOutput(false);
/*     */     }
/*  75 */     if (this.connection.getDoOutput() && this.outputStreaming) {
/*  76 */       this.connection.setFixedLengthStreamingMode(bufferedOutput.length);
/*     */     }
/*  78 */     this.connection.connect();
/*  79 */     if (this.connection.getDoOutput()) {
/*  80 */       FileCopyUtils.copy(bufferedOutput, this.connection.getOutputStream());
/*     */     }
/*     */     else {
/*     */       
/*  84 */       this.connection.getResponseCode();
/*     */     } 
/*  86 */     return new SimpleClientHttpResponse(this.connection);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void addHeaders(HttpURLConnection connection, HttpHeaders headers) {
/*  96 */     for (Map.Entry<String, List<String>> entry : (Iterable<Map.Entry<String, List<String>>>)headers.entrySet()) {
/*  97 */       String headerName = entry.getKey();
/*  98 */       if ("Cookie".equalsIgnoreCase(headerName)) {
/*  99 */         String headerValue = StringUtils.collectionToDelimitedString(entry.getValue(), "; ");
/* 100 */         connection.setRequestProperty(headerName, headerValue);
/*     */         continue;
/*     */       } 
/* 103 */       for (String headerValue : entry.getValue()) {
/* 104 */         String actualHeaderValue = (headerValue != null) ? headerValue : "";
/* 105 */         connection.addRequestProperty(headerName, actualHeaderValue);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\http\client\SimpleBufferingClientHttpRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */