/*     */ package org.springframework.http.client;
/*     */ 
/*     */ import java.io.Closeable;
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import org.apache.http.client.HttpClient;
/*     */ import org.apache.http.client.config.RequestConfig;
/*     */ import org.apache.http.client.methods.Configurable;
/*     */ import org.apache.http.client.methods.HttpUriRequest;
/*     */ import org.apache.http.client.protocol.HttpClientContext;
/*     */ import org.apache.http.impl.client.CloseableHttpClient;
/*     */ import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
/*     */ import org.apache.http.impl.nio.client.HttpAsyncClients;
/*     */ import org.apache.http.nio.client.HttpAsyncClient;
/*     */ import org.apache.http.protocol.HttpContext;
/*     */ import org.springframework.beans.factory.InitializingBean;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HttpComponentsAsyncClientHttpRequestFactory
/*     */   extends HttpComponentsClientHttpRequestFactory
/*     */   implements AsyncClientHttpRequestFactory, InitializingBean
/*     */ {
/*     */   private HttpAsyncClient asyncClient;
/*     */   
/*     */   public HttpComponentsAsyncClientHttpRequestFactory() {
/*  60 */     this.asyncClient = (HttpAsyncClient)HttpAsyncClients.createSystem();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpComponentsAsyncClientHttpRequestFactory(HttpAsyncClient asyncClient) {
/*  71 */     setAsyncClient(asyncClient);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpComponentsAsyncClientHttpRequestFactory(CloseableHttpAsyncClient asyncClient) {
/*  81 */     setAsyncClient((HttpAsyncClient)asyncClient);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpComponentsAsyncClientHttpRequestFactory(HttpClient httpClient, HttpAsyncClient asyncClient) {
/*  92 */     super(httpClient);
/*  93 */     setAsyncClient(asyncClient);
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
/*     */   public HttpComponentsAsyncClientHttpRequestFactory(CloseableHttpClient httpClient, CloseableHttpAsyncClient asyncClient) {
/* 105 */     super((HttpClient)httpClient);
/* 106 */     setAsyncClient((HttpAsyncClient)asyncClient);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAsyncClient(HttpAsyncClient asyncClient) {
/* 117 */     Assert.notNull(asyncClient, "HttpAsyncClient must not be null");
/* 118 */     this.asyncClient = asyncClient;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpAsyncClient getAsyncClient() {
/* 128 */     return this.asyncClient;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void setHttpAsyncClient(CloseableHttpAsyncClient asyncClient) {
/* 138 */     this.asyncClient = (HttpAsyncClient)asyncClient;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public CloseableHttpAsyncClient getHttpAsyncClient() {
/* 148 */     Assert.state((this.asyncClient == null || this.asyncClient instanceof CloseableHttpAsyncClient), "No CloseableHttpAsyncClient - use getAsyncClient() instead");
/*     */     
/* 150 */     return (CloseableHttpAsyncClient)this.asyncClient;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() {
/* 156 */     startAsyncClient();
/*     */   }
/*     */   
/*     */   private void startAsyncClient() {
/* 160 */     HttpAsyncClient asyncClient = getAsyncClient();
/* 161 */     if (asyncClient instanceof CloseableHttpAsyncClient) {
/* 162 */       CloseableHttpAsyncClient closeableAsyncClient = (CloseableHttpAsyncClient)asyncClient;
/* 163 */       if (!closeableAsyncClient.isRunning()) {
/* 164 */         closeableAsyncClient.start();
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public AsyncClientHttpRequest createAsyncRequest(URI uri, HttpMethod httpMethod) throws IOException {
/*     */     HttpClientContext httpClientContext;
/* 171 */     startAsyncClient();
/*     */     
/* 173 */     HttpUriRequest httpRequest = createHttpUriRequest(httpMethod, uri);
/* 174 */     postProcessHttpRequest(httpRequest);
/* 175 */     HttpContext context = createHttpContext(httpMethod, uri);
/* 176 */     if (context == null) {
/* 177 */       httpClientContext = HttpClientContext.create();
/*     */     }
/*     */ 
/*     */     
/* 181 */     if (httpClientContext.getAttribute("http.request-config") == null) {
/*     */       
/* 183 */       RequestConfig config = null;
/* 184 */       if (httpRequest instanceof Configurable) {
/* 185 */         config = ((Configurable)httpRequest).getConfig();
/*     */       }
/* 187 */       if (config == null) {
/* 188 */         config = createRequestConfig(getAsyncClient());
/*     */       }
/* 190 */       if (config != null) {
/* 191 */         httpClientContext.setAttribute("http.request-config", config);
/*     */       }
/*     */     } 
/*     */     
/* 195 */     return new HttpComponentsAsyncClientHttpRequest(getAsyncClient(), httpRequest, (HttpContext)httpClientContext);
/*     */   }
/*     */ 
/*     */   
/*     */   public void destroy() throws Exception {
/*     */     try {
/* 201 */       super.destroy();
/*     */     } finally {
/*     */       
/* 204 */       HttpAsyncClient asyncClient = getAsyncClient();
/* 205 */       if (asyncClient instanceof Closeable)
/* 206 */         ((Closeable)asyncClient).close(); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\http\client\HttpComponentsAsyncClientHttpRequestFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */