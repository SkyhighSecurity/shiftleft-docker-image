/*     */ package org.springframework.remoting.httpinvoker;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.Locale;
/*     */ import java.util.zip.GZIPInputStream;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.NoHttpResponseException;
/*     */ import org.apache.http.StatusLine;
/*     */ import org.apache.http.client.HttpClient;
/*     */ import org.apache.http.client.config.RequestConfig;
/*     */ import org.apache.http.client.methods.Configurable;
/*     */ import org.apache.http.client.methods.HttpPost;
/*     */ import org.apache.http.client.methods.HttpUriRequest;
/*     */ import org.apache.http.config.Registry;
/*     */ import org.apache.http.config.RegistryBuilder;
/*     */ import org.apache.http.conn.HttpClientConnectionManager;
/*     */ import org.apache.http.conn.socket.ConnectionSocketFactory;
/*     */ import org.apache.http.conn.socket.PlainConnectionSocketFactory;
/*     */ import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
/*     */ import org.apache.http.entity.ByteArrayEntity;
/*     */ import org.apache.http.impl.client.HttpClientBuilder;
/*     */ import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
/*     */ import org.springframework.context.i18n.LocaleContext;
/*     */ import org.springframework.context.i18n.LocaleContextHolder;
/*     */ import org.springframework.remoting.support.RemoteInvocationResult;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HttpComponentsHttpInvokerRequestExecutor
/*     */   extends AbstractHttpInvokerRequestExecutor
/*     */ {
/*     */   private static final int DEFAULT_MAX_TOTAL_CONNECTIONS = 100;
/*     */   private static final int DEFAULT_MAX_CONNECTIONS_PER_ROUTE = 5;
/*     */   private static final int DEFAULT_READ_TIMEOUT_MILLISECONDS = 60000;
/*     */   private static Class<?> abstractHttpClientClass;
/*     */   private HttpClient httpClient;
/*     */   private RequestConfig requestConfig;
/*     */   
/*     */   static {
/*     */     try {
/*  79 */       abstractHttpClientClass = ClassUtils.forName("org.apache.http.impl.client.AbstractHttpClient", HttpComponentsHttpInvokerRequestExecutor.class
/*  80 */           .getClassLoader());
/*     */     }
/*  82 */     catch (ClassNotFoundException classNotFoundException) {}
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
/*     */   public HttpComponentsHttpInvokerRequestExecutor() {
/*  98 */     this(createDefaultHttpClient(), RequestConfig.custom()
/*  99 */         .setSocketTimeout(60000).build());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpComponentsHttpInvokerRequestExecutor(HttpClient httpClient) {
/* 108 */     this(httpClient, (RequestConfig)null);
/*     */   }
/*     */   
/*     */   private HttpComponentsHttpInvokerRequestExecutor(HttpClient httpClient, RequestConfig requestConfig) {
/* 112 */     this.httpClient = httpClient;
/* 113 */     this.requestConfig = requestConfig;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static HttpClient createDefaultHttpClient() {
/* 121 */     Registry<ConnectionSocketFactory> schemeRegistry = RegistryBuilder.create().register("http", PlainConnectionSocketFactory.getSocketFactory()).register("https", SSLConnectionSocketFactory.getSocketFactory()).build();
/*     */     
/* 123 */     PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(schemeRegistry);
/* 124 */     connectionManager.setMaxTotal(100);
/* 125 */     connectionManager.setDefaultMaxPerRoute(5);
/*     */     
/* 127 */     return (HttpClient)HttpClientBuilder.create().setConnectionManager((HttpClientConnectionManager)connectionManager).build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHttpClient(HttpClient httpClient) {
/* 135 */     this.httpClient = httpClient;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpClient getHttpClient() {
/* 142 */     return this.httpClient;
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
/* 154 */     Assert.isTrue((timeout >= 0), "Timeout must be a non-negative value");
/* 155 */     this.requestConfig = cloneRequestConfig().setConnectTimeout(timeout).build();
/* 156 */     setLegacyConnectionTimeout(getHttpClient(), timeout);
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
/* 175 */     if (abstractHttpClientClass != null && abstractHttpClientClass.isInstance(client)) {
/* 176 */       client.getParams().setIntParameter("http.connection.timeout", timeout);
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
/* 190 */     this.requestConfig = cloneRequestConfig().setConnectionRequestTimeout(connectionRequestTimeout).build();
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
/*     */   public void setReadTimeout(int timeout) {
/* 203 */     Assert.isTrue((timeout >= 0), "Timeout must be a non-negative value");
/* 204 */     this.requestConfig = cloneRequestConfig().setSocketTimeout(timeout).build();
/* 205 */     setLegacySocketTimeout(getHttpClient(), timeout);
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
/* 217 */     if (abstractHttpClientClass != null && abstractHttpClientClass.isInstance(client)) {
/* 218 */       client.getParams().setIntParameter("http.socket.timeout", timeout);
/*     */     }
/*     */   }
/*     */   
/*     */   private RequestConfig.Builder cloneRequestConfig() {
/* 223 */     return (this.requestConfig != null) ? RequestConfig.copy(this.requestConfig) : RequestConfig.custom();
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
/*     */   protected RemoteInvocationResult doExecuteRequest(HttpInvokerClientConfiguration config, ByteArrayOutputStream baos) throws IOException, ClassNotFoundException {
/* 242 */     HttpPost postMethod = createHttpPost(config);
/* 243 */     setRequestBody(config, postMethod, baos);
/*     */     try {
/* 245 */       HttpResponse response = executeHttpPost(config, getHttpClient(), postMethod);
/* 246 */       validateResponse(config, response);
/* 247 */       InputStream responseBody = getResponseBody(config, response);
/* 248 */       return readRemoteInvocationResult(responseBody, config.getCodebaseUrl());
/*     */     } finally {
/*     */       
/* 251 */       postMethod.releaseConnection();
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
/*     */   protected HttpPost createHttpPost(HttpInvokerClientConfiguration config) throws IOException {
/* 265 */     HttpPost httpPost = new HttpPost(config.getServiceUrl());
/*     */     
/* 267 */     RequestConfig requestConfig = createRequestConfig(config);
/* 268 */     if (requestConfig != null) {
/* 269 */       httpPost.setConfig(requestConfig);
/*     */     }
/*     */     
/* 272 */     LocaleContext localeContext = LocaleContextHolder.getLocaleContext();
/* 273 */     if (localeContext != null) {
/* 274 */       Locale locale = localeContext.getLocale();
/* 275 */       if (locale != null) {
/* 276 */         httpPost.addHeader("Accept-Language", StringUtils.toLanguageTag(locale));
/*     */       }
/*     */     } 
/*     */     
/* 280 */     if (isAcceptGzipEncoding()) {
/* 281 */       httpPost.addHeader("Accept-Encoding", "gzip");
/*     */     }
/*     */     
/* 284 */     return httpPost;
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
/*     */   protected RequestConfig createRequestConfig(HttpInvokerClientConfiguration config) {
/* 298 */     HttpClient client = getHttpClient();
/* 299 */     if (client instanceof Configurable) {
/* 300 */       RequestConfig clientRequestConfig = ((Configurable)client).getConfig();
/* 301 */       return mergeRequestConfig(clientRequestConfig);
/*     */     } 
/* 303 */     return this.requestConfig;
/*     */   }
/*     */   
/*     */   private RequestConfig mergeRequestConfig(RequestConfig defaultRequestConfig) {
/* 307 */     if (this.requestConfig == null) {
/* 308 */       return defaultRequestConfig;
/*     */     }
/*     */     
/* 311 */     RequestConfig.Builder builder = RequestConfig.copy(defaultRequestConfig);
/* 312 */     int connectTimeout = this.requestConfig.getConnectTimeout();
/* 313 */     if (connectTimeout >= 0) {
/* 314 */       builder.setConnectTimeout(connectTimeout);
/*     */     }
/* 316 */     int connectionRequestTimeout = this.requestConfig.getConnectionRequestTimeout();
/* 317 */     if (connectionRequestTimeout >= 0) {
/* 318 */       builder.setConnectionRequestTimeout(connectionRequestTimeout);
/*     */     }
/* 320 */     int socketTimeout = this.requestConfig.getSocketTimeout();
/* 321 */     if (socketTimeout >= 0) {
/* 322 */       builder.setSocketTimeout(socketTimeout);
/*     */     }
/* 324 */     return builder.build();
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
/*     */   protected void setRequestBody(HttpInvokerClientConfiguration config, HttpPost httpPost, ByteArrayOutputStream baos) throws IOException {
/* 342 */     ByteArrayEntity entity = new ByteArrayEntity(baos.toByteArray());
/* 343 */     entity.setContentType(getContentType());
/* 344 */     httpPost.setEntity((HttpEntity)entity);
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
/*     */   protected HttpResponse executeHttpPost(HttpInvokerClientConfiguration config, HttpClient httpClient, HttpPost httpPost) throws IOException {
/* 359 */     return httpClient.execute((HttpUriRequest)httpPost);
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
/*     */   protected void validateResponse(HttpInvokerClientConfiguration config, HttpResponse response) throws IOException {
/* 374 */     StatusLine status = response.getStatusLine();
/* 375 */     if (status.getStatusCode() >= 300) {
/* 376 */       throw new NoHttpResponseException("Did not receive successful HTTP response: status code = " + status
/* 377 */           .getStatusCode() + ", status message = [" + status
/* 378 */           .getReasonPhrase() + "]");
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected InputStream getResponseBody(HttpInvokerClientConfiguration config, HttpResponse httpResponse) throws IOException {
/* 397 */     if (isGzipResponse(httpResponse)) {
/* 398 */       return new GZIPInputStream(httpResponse.getEntity().getContent());
/*     */     }
/*     */     
/* 401 */     return httpResponse.getEntity().getContent();
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
/*     */   protected boolean isGzipResponse(HttpResponse httpResponse) {
/* 413 */     Header encodingHeader = httpResponse.getFirstHeader("Content-Encoding");
/* 414 */     return (encodingHeader != null && encodingHeader.getValue() != null && encodingHeader
/* 415 */       .getValue().toLowerCase().contains("gzip"));
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\remoting\httpinvoker\HttpComponentsHttpInvokerRequestExecutor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */