/*     */ package org.springframework.http.client;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URI;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import okhttp3.Cache;
/*     */ import okhttp3.MediaType;
/*     */ import okhttp3.OkHttpClient;
/*     */ import okhttp3.Request;
/*     */ import okhttp3.RequestBody;
/*     */ import okhttp3.internal.http.HttpMethod;
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
/*     */ public class OkHttp3ClientHttpRequestFactory
/*     */   implements ClientHttpRequestFactory, AsyncClientHttpRequestFactory, DisposableBean
/*     */ {
/*     */   private OkHttpClient client;
/*     */   private final boolean defaultClient;
/*     */   
/*     */   public OkHttp3ClientHttpRequestFactory() {
/*  58 */     this.client = new OkHttpClient();
/*  59 */     this.defaultClient = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public OkHttp3ClientHttpRequestFactory(OkHttpClient client) {
/*  67 */     Assert.notNull(client, "OkHttpClient must not be null");
/*  68 */     this.client = client;
/*  69 */     this.defaultClient = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setReadTimeout(int readTimeout) {
/*  79 */     this
/*     */       
/*  81 */       .client = this.client.newBuilder().readTimeout(readTimeout, TimeUnit.MILLISECONDS).build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setWriteTimeout(int writeTimeout) {
/*  90 */     this
/*     */       
/*  92 */       .client = this.client.newBuilder().writeTimeout(writeTimeout, TimeUnit.MILLISECONDS).build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setConnectTimeout(int connectTimeout) {
/* 101 */     this
/*     */       
/* 103 */       .client = this.client.newBuilder().connectTimeout(connectTimeout, TimeUnit.MILLISECONDS).build();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ClientHttpRequest createRequest(URI uri, HttpMethod httpMethod) {
/* 109 */     return new OkHttp3ClientHttpRequest(this.client, uri, httpMethod);
/*     */   }
/*     */ 
/*     */   
/*     */   public AsyncClientHttpRequest createAsyncRequest(URI uri, HttpMethod httpMethod) {
/* 114 */     return new OkHttp3AsyncClientHttpRequest(this.client, uri, httpMethod);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void destroy() throws IOException {
/* 120 */     if (this.defaultClient) {
/*     */       
/* 122 */       Cache cache = this.client.cache();
/* 123 */       if (cache != null) {
/* 124 */         cache.close();
/*     */       }
/* 126 */       this.client.dispatcher().executorService().shutdown();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static Request buildRequest(HttpHeaders headers, byte[] content, URI uri, HttpMethod method) throws MalformedURLException {
/* 134 */     MediaType contentType = getContentType(headers);
/*     */ 
/*     */     
/* 137 */     RequestBody body = (content.length > 0 || HttpMethod.requiresRequestBody(method.name())) ? RequestBody.create(contentType, content) : null;
/*     */     
/* 139 */     Request.Builder builder = (new Request.Builder()).url(uri.toURL()).method(method.name(), body);
/* 140 */     for (Map.Entry<String, List<String>> entry : (Iterable<Map.Entry<String, List<String>>>)headers.entrySet()) {
/* 141 */       String headerName = entry.getKey();
/* 142 */       for (String headerValue : entry.getValue()) {
/* 143 */         builder.addHeader(headerName, headerValue);
/*     */       }
/*     */     } 
/* 146 */     return builder.build();
/*     */   }
/*     */   
/*     */   private static MediaType getContentType(HttpHeaders headers) {
/* 150 */     String rawContentType = headers.getFirst("Content-Type");
/* 151 */     return StringUtils.hasText(rawContentType) ? MediaType.parse(rawContentType) : null;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\http\client\OkHttp3ClientHttpRequestFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */