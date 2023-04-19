/*     */ package org.springframework.http.client;
/*     */ 
/*     */ import java.io.Closeable;
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import org.apache.http.client.HttpClient;
/*     */ import org.apache.http.client.config.RequestConfig;
/*     */ import org.apache.http.client.methods.Configurable;
/*     */ import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
/*     */ import org.apache.http.client.methods.HttpGet;
/*     */ import org.apache.http.client.methods.HttpHead;
/*     */ import org.apache.http.client.methods.HttpOptions;
/*     */ import org.apache.http.client.methods.HttpPatch;
/*     */ import org.apache.http.client.methods.HttpPost;
/*     */ import org.apache.http.client.methods.HttpPut;
/*     */ import org.apache.http.client.methods.HttpTrace;
/*     */ import org.apache.http.client.methods.HttpUriRequest;
/*     */ import org.apache.http.client.protocol.HttpClientContext;
/*     */ import org.apache.http.impl.client.HttpClients;
/*     */ import org.apache.http.protocol.HttpContext;
/*     */ import org.springframework.beans.factory.DisposableBean;
/*     */ import org.springframework.http.HttpMethod;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
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
/*     */ public class HttpComponentsClientHttpRequestFactory
/*     */   implements ClientHttpRequestFactory, DisposableBean
/*     */ {
/*     */   private static Class<?> abstractHttpClientClass;
/*     */   private HttpClient httpClient;
/*     */   private RequestConfig requestConfig;
/*     */   
/*     */   static {
/*     */     try {
/*  67 */       abstractHttpClientClass = ClassUtils.forName("org.apache.http.impl.client.AbstractHttpClient", HttpComponentsClientHttpRequestFactory.class
/*  68 */           .getClassLoader());
/*     */     }
/*  70 */     catch (ClassNotFoundException classNotFoundException) {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean bufferRequestBody = true;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpComponentsClientHttpRequestFactory() {
/*  88 */     this.httpClient = (HttpClient)HttpClients.createSystem();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpComponentsClientHttpRequestFactory(HttpClient httpClient) {
/*  97 */     setHttpClient(httpClient);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHttpClient(HttpClient httpClient) {
/* 106 */     Assert.notNull(httpClient, "HttpClient must not be null");
/* 107 */     this.httpClient = httpClient;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpClient getHttpClient() {
/* 115 */     return this.httpClient;
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
/*     */   public void setConnectTimeout(int timeout) {
/* 127 */     Assert.isTrue((timeout >= 0), "Timeout must be a non-negative value");
/* 128 */     this.requestConfig = requestConfigBuilder().setConnectTimeout(timeout).build();
/* 129 */     setLegacyConnectionTimeout(getHttpClient(), timeout);
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
/*     */ 
/*     */   
/*     */   private void setLegacyConnectionTimeout(HttpClient client, int timeout) {
/* 148 */     if (abstractHttpClientClass != null && abstractHttpClientClass.isInstance(client)) {
/* 149 */       client.getParams().setIntParameter("http.connection.timeout", timeout);
/*     */     }
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
/*     */   public void setConnectionRequestTimeout(int connectionRequestTimeout) {
/* 163 */     this.requestConfig = requestConfigBuilder().setConnectionRequestTimeout(connectionRequestTimeout).build();
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
/*     */   public void setReadTimeout(int timeout) {
/* 175 */     Assert.isTrue((timeout >= 0), "Timeout must be a non-negative value");
/* 176 */     this.requestConfig = requestConfigBuilder().setSocketTimeout(timeout).build();
/* 177 */     setLegacySocketTimeout(getHttpClient(), timeout);
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
/*     */   private void setLegacySocketTimeout(HttpClient client, int timeout) {
/* 189 */     if (abstractHttpClientClass != null && abstractHttpClientClass.isInstance(client)) {
/* 190 */       client.getParams().setIntParameter("http.socket.timeout", timeout);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBufferRequestBody(boolean bufferRequestBody) {
/* 201 */     this.bufferRequestBody = bufferRequestBody;
/*     */   }
/*     */ 
/*     */   
/*     */   public ClientHttpRequest createRequest(URI uri, HttpMethod httpMethod) throws IOException {
/*     */     HttpClientContext httpClientContext;
/* 207 */     HttpUriRequest httpRequest = createHttpUriRequest(httpMethod, uri);
/* 208 */     postProcessHttpRequest(httpRequest);
/* 209 */     HttpContext context = createHttpContext(httpMethod, uri);
/* 210 */     if (context == null) {
/* 211 */       httpClientContext = HttpClientContext.create();
/*     */     }
/*     */ 
/*     */     
/* 215 */     if (httpClientContext.getAttribute("http.request-config") == null) {
/*     */       
/* 217 */       RequestConfig config = null;
/* 218 */       if (httpRequest instanceof Configurable) {
/* 219 */         config = ((Configurable)httpRequest).getConfig();
/*     */       }
/* 221 */       if (config == null) {
/* 222 */         config = createRequestConfig(getHttpClient());
/*     */       }
/* 224 */       if (config != null) {
/* 225 */         httpClientContext.setAttribute("http.request-config", config);
/*     */       }
/*     */     } 
/*     */     
/* 229 */     if (this.bufferRequestBody) {
/* 230 */       return new HttpComponentsClientHttpRequest(getHttpClient(), httpRequest, (HttpContext)httpClientContext);
/*     */     }
/*     */     
/* 233 */     return new HttpComponentsStreamingClientHttpRequest(getHttpClient(), httpRequest, (HttpContext)httpClientContext);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private RequestConfig.Builder requestConfigBuilder() {
/* 243 */     return (this.requestConfig != null) ? RequestConfig.copy(this.requestConfig) : RequestConfig.custom();
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
/*     */   protected RequestConfig createRequestConfig(Object client) {
/* 258 */     if (client instanceof Configurable) {
/* 259 */       RequestConfig clientRequestConfig = ((Configurable)client).getConfig();
/* 260 */       return mergeRequestConfig(clientRequestConfig);
/*     */     } 
/* 262 */     return this.requestConfig;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected RequestConfig mergeRequestConfig(RequestConfig clientConfig) {
/* 273 */     if (this.requestConfig == null) {
/* 274 */       return clientConfig;
/*     */     }
/*     */     
/* 277 */     RequestConfig.Builder builder = RequestConfig.copy(clientConfig);
/* 278 */     int connectTimeout = this.requestConfig.getConnectTimeout();
/* 279 */     if (connectTimeout >= 0) {
/* 280 */       builder.setConnectTimeout(connectTimeout);
/*     */     }
/* 282 */     int connectionRequestTimeout = this.requestConfig.getConnectionRequestTimeout();
/* 283 */     if (connectionRequestTimeout >= 0) {
/* 284 */       builder.setConnectionRequestTimeout(connectionRequestTimeout);
/*     */     }
/* 286 */     int socketTimeout = this.requestConfig.getSocketTimeout();
/* 287 */     if (socketTimeout >= 0) {
/* 288 */       builder.setSocketTimeout(socketTimeout);
/*     */     }
/* 290 */     return builder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected HttpUriRequest createHttpUriRequest(HttpMethod httpMethod, URI uri) {
/* 300 */     switch (httpMethod) {
/*     */       case GET:
/* 302 */         return (HttpUriRequest)new HttpGet(uri);
/*     */       case HEAD:
/* 304 */         return (HttpUriRequest)new HttpHead(uri);
/*     */       case POST:
/* 306 */         return (HttpUriRequest)new HttpPost(uri);
/*     */       case PUT:
/* 308 */         return (HttpUriRequest)new HttpPut(uri);
/*     */       case PATCH:
/* 310 */         return (HttpUriRequest)new HttpPatch(uri);
/*     */       case DELETE:
/* 312 */         return (HttpUriRequest)new HttpDelete(uri);
/*     */       case OPTIONS:
/* 314 */         return (HttpUriRequest)new HttpOptions(uri);
/*     */       case TRACE:
/* 316 */         return (HttpUriRequest)new HttpTrace(uri);
/*     */     } 
/* 318 */     throw new IllegalArgumentException("Invalid HTTP method: " + httpMethod);
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
/*     */   protected void postProcessHttpRequest(HttpUriRequest request) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected HttpContext createHttpContext(HttpMethod httpMethod, URI uri) {
/* 339 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void destroy() throws Exception {
/* 350 */     HttpClient httpClient = getHttpClient();
/* 351 */     if (httpClient instanceof Closeable) {
/* 352 */       ((Closeable)httpClient).close();
/*     */     }
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
/*     */   private static class HttpDelete
/*     */     extends HttpEntityEnclosingRequestBase
/*     */   {
/*     */     public HttpDelete(URI uri) {
/* 369 */       setURI(uri);
/*     */     }
/*     */ 
/*     */     
/*     */     public String getMethod() {
/* 374 */       return "DELETE";
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\http\client\HttpComponentsClientHttpRequestFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */