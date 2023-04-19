/*     */ package org.springframework.http.client;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.Proxy;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import org.springframework.core.task.AsyncListenableTaskExecutor;
/*     */ import org.springframework.http.HttpMethod;
/*     */ import org.springframework.util.Assert;
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
/*     */ public class SimpleClientHttpRequestFactory
/*     */   implements ClientHttpRequestFactory, AsyncClientHttpRequestFactory
/*     */ {
/*     */   private static final int DEFAULT_CHUNK_SIZE = 4096;
/*     */   private Proxy proxy;
/*     */   private boolean bufferRequestBody = true;
/*  48 */   private int chunkSize = 4096;
/*     */   
/*  50 */   private int connectTimeout = -1;
/*     */   
/*  52 */   private int readTimeout = -1;
/*     */ 
/*     */   
/*     */   private boolean outputStreaming = true;
/*     */ 
/*     */   
/*     */   private AsyncListenableTaskExecutor taskExecutor;
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProxy(Proxy proxy) {
/*  63 */     this.proxy = proxy;
/*     */   }
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
/*     */   public void setBufferRequestBody(boolean bufferRequestBody) {
/*  80 */     this.bufferRequestBody = bufferRequestBody;
/*     */   }
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
/*     */   public void setChunkSize(int chunkSize) {
/*  93 */     this.chunkSize = chunkSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setConnectTimeout(int connectTimeout) {
/* 103 */     this.connectTimeout = connectTimeout;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setReadTimeout(int readTimeout) {
/* 113 */     this.readTimeout = readTimeout;
/*     */   }
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
/*     */   public void setOutputStreaming(boolean outputStreaming) {
/* 126 */     this.outputStreaming = outputStreaming;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTaskExecutor(AsyncListenableTaskExecutor taskExecutor) {
/* 135 */     this.taskExecutor = taskExecutor;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ClientHttpRequest createRequest(URI uri, HttpMethod httpMethod) throws IOException {
/* 141 */     HttpURLConnection connection = openConnection(uri.toURL(), this.proxy);
/* 142 */     prepareConnection(connection, httpMethod.name());
/*     */     
/* 144 */     if (this.bufferRequestBody) {
/* 145 */       return new SimpleBufferingClientHttpRequest(connection, this.outputStreaming);
/*     */     }
/*     */     
/* 148 */     return new SimpleStreamingClientHttpRequest(connection, this.chunkSize, this.outputStreaming);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AsyncClientHttpRequest createAsyncRequest(URI uri, HttpMethod httpMethod) throws IOException {
/* 158 */     Assert.state((this.taskExecutor != null), "Asynchronous execution requires TaskExecutor to be set");
/*     */     
/* 160 */     HttpURLConnection connection = openConnection(uri.toURL(), this.proxy);
/* 161 */     prepareConnection(connection, httpMethod.name());
/*     */     
/* 163 */     if (this.bufferRequestBody) {
/* 164 */       return new SimpleBufferingAsyncClientHttpRequest(connection, this.outputStreaming, this.taskExecutor);
/*     */     }
/*     */ 
/*     */     
/* 168 */     return new SimpleStreamingAsyncClientHttpRequest(connection, this.chunkSize, this.outputStreaming, this.taskExecutor);
/*     */   }
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
/*     */   protected HttpURLConnection openConnection(URL url, Proxy proxy) throws IOException {
/* 183 */     URLConnection urlConnection = (proxy != null) ? url.openConnection(proxy) : url.openConnection();
/* 184 */     if (!HttpURLConnection.class.isInstance(urlConnection)) {
/* 185 */       throw new IllegalStateException("HttpURLConnection required for [" + url + "] but got: " + urlConnection);
/*     */     }
/* 187 */     return (HttpURLConnection)urlConnection;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void prepareConnection(HttpURLConnection connection, String httpMethod) throws IOException {
/* 198 */     if (this.connectTimeout >= 0) {
/* 199 */       connection.setConnectTimeout(this.connectTimeout);
/*     */     }
/* 201 */     if (this.readTimeout >= 0) {
/* 202 */       connection.setReadTimeout(this.readTimeout);
/*     */     }
/*     */     
/* 205 */     connection.setDoInput(true);
/*     */     
/* 207 */     if ("GET".equals(httpMethod)) {
/* 208 */       connection.setInstanceFollowRedirects(true);
/*     */     } else {
/*     */       
/* 211 */       connection.setInstanceFollowRedirects(false);
/*     */     } 
/*     */     
/* 214 */     if ("POST".equals(httpMethod) || "PUT".equals(httpMethod) || "PATCH"
/* 215 */       .equals(httpMethod) || "DELETE".equals(httpMethod)) {
/* 216 */       connection.setDoOutput(true);
/*     */     } else {
/*     */       
/* 219 */       connection.setDoOutput(false);
/*     */     } 
/*     */     
/* 222 */     connection.setRequestMethod(httpMethod);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\http\client\SimpleClientHttpRequestFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */