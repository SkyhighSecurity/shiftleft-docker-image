/*     */ package org.springframework.http.client;
/*     */ 
/*     */ import com.squareup.okhttp.MediaType;
/*     */ import com.squareup.okhttp.OkHttpClient;
/*     */ import com.squareup.okhttp.Request;
/*     */ import com.squareup.okhttp.RequestBody;
/*     */ import com.squareup.okhttp.internal.http.HttpMethod;
/*     */ import java.io.IOException;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URI;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.springframework.beans.factory.DisposableBean;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpMethod;
/*     */ import org.springframework.util.Assert;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class OkHttpClientHttpRequestFactory
/*     */   implements ClientHttpRequestFactory, AsyncClientHttpRequestFactory, DisposableBean
/*     */ {
/*     */   private final OkHttpClient client;
/*     */   private final boolean defaultClient;
/*     */   
/*     */   public OkHttpClientHttpRequestFactory() {
/*  57 */     this.client = new OkHttpClient();
/*  58 */     this.defaultClient = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public OkHttpClientHttpRequestFactory(OkHttpClient client) {
/*  66 */     Assert.notNull(client, "OkHttpClient must not be null");
/*  67 */     this.client = client;
/*  68 */     this.defaultClient = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setReadTimeout(int readTimeout) {
/*  78 */     this.client.setReadTimeout(readTimeout, TimeUnit.MILLISECONDS);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setWriteTimeout(int writeTimeout) {
/*  87 */     this.client.setWriteTimeout(writeTimeout, TimeUnit.MILLISECONDS);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setConnectTimeout(int connectTimeout) {
/*  96 */     this.client.setConnectTimeout(connectTimeout, TimeUnit.MILLISECONDS);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ClientHttpRequest createRequest(URI uri, HttpMethod httpMethod) {
/* 102 */     return new OkHttpClientHttpRequest(this.client, uri, httpMethod);
/*     */   }
/*     */ 
/*     */   
/*     */   public AsyncClientHttpRequest createAsyncRequest(URI uri, HttpMethod httpMethod) {
/* 107 */     return new OkHttpAsyncClientHttpRequest(this.client, uri, httpMethod);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void destroy() throws IOException {
/* 113 */     if (this.defaultClient) {
/*     */       
/* 115 */       if (this.client.getCache() != null) {
/* 116 */         this.client.getCache().close();
/*     */       }
/* 118 */       this.client.getDispatcher().getExecutorService().shutdown();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static Request buildRequest(HttpHeaders headers, byte[] content, URI uri, HttpMethod method) throws MalformedURLException {
/* 126 */     MediaType contentType = getContentType(headers);
/*     */ 
/*     */     
/* 129 */     RequestBody body = (content.length > 0 || HttpMethod.requiresRequestBody(method.name())) ? RequestBody.create(contentType, content) : null;
/*     */     
/* 131 */     Request.Builder builder = (new Request.Builder()).url(uri.toURL()).method(method.name(), body);
/* 132 */     for (Map.Entry<String, List<String>> entry : (Iterable<Map.Entry<String, List<String>>>)headers.entrySet()) {
/* 133 */       String headerName = entry.getKey();
/* 134 */       for (String headerValue : entry.getValue()) {
/* 135 */         builder.addHeader(headerName, headerValue);
/*     */       }
/*     */     } 
/* 138 */     return builder.build();
/*     */   }
/*     */   
/*     */   private static MediaType getContentType(HttpHeaders headers) {
/* 142 */     String rawContentType = headers.getFirst("Content-Type");
/* 143 */     return StringUtils.hasText(rawContentType) ? 
/* 144 */       MediaType.parse(rawContentType) : null;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\http\client\OkHttpClientHttpRequestFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */