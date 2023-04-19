/*     */ package org.springframework.web.client;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.lang.reflect.Type;
/*     */ import java.net.URI;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import org.springframework.core.ParameterizedTypeReference;
/*     */ import org.springframework.core.task.AsyncListenableTaskExecutor;
/*     */ import org.springframework.core.task.SimpleAsyncTaskExecutor;
/*     */ import org.springframework.http.HttpEntity;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpMethod;
/*     */ import org.springframework.http.ResponseEntity;
/*     */ import org.springframework.http.client.AsyncClientHttpRequest;
/*     */ import org.springframework.http.client.AsyncClientHttpRequestFactory;
/*     */ import org.springframework.http.client.ClientHttpRequest;
/*     */ import org.springframework.http.client.ClientHttpRequestFactory;
/*     */ import org.springframework.http.client.ClientHttpResponse;
/*     */ import org.springframework.http.client.SimpleClientHttpRequestFactory;
/*     */ import org.springframework.http.client.support.InterceptingAsyncHttpAccessor;
/*     */ import org.springframework.http.converter.HttpMessageConverter;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.concurrent.ListenableFuture;
/*     */ import org.springframework.util.concurrent.ListenableFutureAdapter;
/*     */ import org.springframework.web.util.AbstractUriTemplateHandler;
/*     */ import org.springframework.web.util.UriTemplateHandler;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AsyncRestTemplate
/*     */   extends InterceptingAsyncHttpAccessor
/*     */   implements AsyncRestOperations
/*     */ {
/*     */   private final RestTemplate syncTemplate;
/*     */   
/*     */   public AsyncRestTemplate() {
/*  81 */     this((AsyncListenableTaskExecutor)new SimpleAsyncTaskExecutor());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AsyncRestTemplate(AsyncListenableTaskExecutor taskExecutor) {
/*  91 */     Assert.notNull(taskExecutor, "AsyncTaskExecutor must not be null");
/*  92 */     SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
/*  93 */     requestFactory.setTaskExecutor(taskExecutor);
/*  94 */     this.syncTemplate = new RestTemplate((ClientHttpRequestFactory)requestFactory);
/*  95 */     setAsyncRequestFactory((AsyncClientHttpRequestFactory)requestFactory);
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
/*     */   public AsyncRestTemplate(AsyncClientHttpRequestFactory asyncRequestFactory) {
/* 108 */     this(asyncRequestFactory, (ClientHttpRequestFactory)asyncRequestFactory);
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
/*     */   public AsyncRestTemplate(AsyncClientHttpRequestFactory asyncRequestFactory, ClientHttpRequestFactory syncRequestFactory) {
/* 120 */     this(asyncRequestFactory, new RestTemplate(syncRequestFactory));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AsyncRestTemplate(AsyncClientHttpRequestFactory requestFactory, RestTemplate restTemplate) {
/* 130 */     Assert.notNull(restTemplate, "RestTemplate must not be null");
/* 131 */     this.syncTemplate = restTemplate;
/* 132 */     setAsyncRequestFactory(requestFactory);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setErrorHandler(ResponseErrorHandler errorHandler) {
/* 142 */     this.syncTemplate.setErrorHandler(errorHandler);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResponseErrorHandler getErrorHandler() {
/* 149 */     return this.syncTemplate.getErrorHandler();
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
/*     */   public void setDefaultUriVariables(Map<String, ?> defaultUriVariables) {
/* 165 */     UriTemplateHandler handler = this.syncTemplate.getUriTemplateHandler();
/* 166 */     Assert.isInstanceOf(AbstractUriTemplateHandler.class, handler, "Can only use this property in conjunction with a DefaultUriTemplateHandler");
/*     */     
/* 168 */     ((AbstractUriTemplateHandler)handler).setDefaultUriVariables(defaultUriVariables);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUriTemplateHandler(UriTemplateHandler handler) {
/* 178 */     this.syncTemplate.setUriTemplateHandler(handler);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UriTemplateHandler getUriTemplateHandler() {
/* 185 */     return this.syncTemplate.getUriTemplateHandler();
/*     */   }
/*     */ 
/*     */   
/*     */   public RestOperations getRestOperations() {
/* 190 */     return this.syncTemplate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMessageConverters(List<HttpMessageConverter<?>> messageConverters) {
/* 198 */     this.syncTemplate.setMessageConverters(messageConverters);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<HttpMessageConverter<?>> getMessageConverters() {
/* 205 */     return this.syncTemplate.getMessageConverters();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> ListenableFuture<ResponseEntity<T>> getForEntity(String url, Class<T> responseType, Object... uriVariables) throws RestClientException {
/* 215 */     AsyncRequestCallback requestCallback = acceptHeaderRequestCallback(responseType);
/* 216 */     ResponseExtractor<ResponseEntity<T>> responseExtractor = responseEntityExtractor(responseType);
/* 217 */     return execute(url, HttpMethod.GET, requestCallback, responseExtractor, uriVariables);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> ListenableFuture<ResponseEntity<T>> getForEntity(String url, Class<T> responseType, Map<String, ?> uriVariables) throws RestClientException {
/* 224 */     AsyncRequestCallback requestCallback = acceptHeaderRequestCallback(responseType);
/* 225 */     ResponseExtractor<ResponseEntity<T>> responseExtractor = responseEntityExtractor(responseType);
/* 226 */     return execute(url, HttpMethod.GET, requestCallback, responseExtractor, uriVariables);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> ListenableFuture<ResponseEntity<T>> getForEntity(URI url, Class<T> responseType) throws RestClientException {
/* 231 */     AsyncRequestCallback requestCallback = acceptHeaderRequestCallback(responseType);
/* 232 */     ResponseExtractor<ResponseEntity<T>> responseExtractor = responseEntityExtractor(responseType);
/* 233 */     return execute(url, HttpMethod.GET, requestCallback, responseExtractor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ListenableFuture<HttpHeaders> headForHeaders(String url, Object... uriVariables) throws RestClientException {
/* 241 */     ResponseExtractor<HttpHeaders> headersExtractor = headersExtractor();
/* 242 */     return execute(url, HttpMethod.HEAD, (AsyncRequestCallback)null, headersExtractor, uriVariables);
/*     */   }
/*     */ 
/*     */   
/*     */   public ListenableFuture<HttpHeaders> headForHeaders(String url, Map<String, ?> uriVariables) throws RestClientException {
/* 247 */     ResponseExtractor<HttpHeaders> headersExtractor = headersExtractor();
/* 248 */     return execute(url, HttpMethod.HEAD, (AsyncRequestCallback)null, headersExtractor, uriVariables);
/*     */   }
/*     */ 
/*     */   
/*     */   public ListenableFuture<HttpHeaders> headForHeaders(URI url) throws RestClientException {
/* 253 */     ResponseExtractor<HttpHeaders> headersExtractor = headersExtractor();
/* 254 */     return execute(url, HttpMethod.HEAD, (AsyncRequestCallback)null, headersExtractor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ListenableFuture<URI> postForLocation(String url, HttpEntity<?> request, Object... uriVars) throws RestClientException {
/* 264 */     AsyncRequestCallback callback = httpEntityCallback(request);
/* 265 */     ResponseExtractor<HttpHeaders> extractor = headersExtractor();
/* 266 */     ListenableFuture<HttpHeaders> future = execute(url, HttpMethod.POST, callback, extractor, uriVars);
/* 267 */     return adaptToLocationHeader(future);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ListenableFuture<URI> postForLocation(String url, HttpEntity<?> request, Map<String, ?> uriVars) throws RestClientException {
/* 274 */     AsyncRequestCallback callback = httpEntityCallback(request);
/* 275 */     ResponseExtractor<HttpHeaders> extractor = headersExtractor();
/* 276 */     ListenableFuture<HttpHeaders> future = execute(url, HttpMethod.POST, callback, extractor, uriVars);
/* 277 */     return adaptToLocationHeader(future);
/*     */   }
/*     */ 
/*     */   
/*     */   public ListenableFuture<URI> postForLocation(URI url, HttpEntity<?> request) throws RestClientException {
/* 282 */     AsyncRequestCallback callback = httpEntityCallback(request);
/* 283 */     ResponseExtractor<HttpHeaders> extractor = headersExtractor();
/* 284 */     ListenableFuture<HttpHeaders> future = execute(url, HttpMethod.POST, callback, extractor);
/* 285 */     return adaptToLocationHeader(future);
/*     */   }
/*     */   
/*     */   private static ListenableFuture<URI> adaptToLocationHeader(ListenableFuture<HttpHeaders> future) {
/* 289 */     return (ListenableFuture)new ListenableFutureAdapter<URI, HttpHeaders>(future)
/*     */       {
/*     */         protected URI adapt(HttpHeaders headers) throws ExecutionException {
/* 292 */           return headers.getLocation();
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> ListenableFuture<ResponseEntity<T>> postForEntity(String url, HttpEntity<?> request, Class<T> responseType, Object... uriVariables) throws RestClientException {
/* 301 */     AsyncRequestCallback requestCallback = httpEntityCallback(request, responseType);
/* 302 */     ResponseExtractor<ResponseEntity<T>> responseExtractor = responseEntityExtractor(responseType);
/* 303 */     return execute(url, HttpMethod.POST, requestCallback, responseExtractor, uriVariables);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> ListenableFuture<ResponseEntity<T>> postForEntity(String url, HttpEntity<?> request, Class<T> responseType, Map<String, ?> uriVariables) throws RestClientException {
/* 310 */     AsyncRequestCallback requestCallback = httpEntityCallback(request, responseType);
/* 311 */     ResponseExtractor<ResponseEntity<T>> responseExtractor = responseEntityExtractor(responseType);
/* 312 */     return execute(url, HttpMethod.POST, requestCallback, responseExtractor, uriVariables);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> ListenableFuture<ResponseEntity<T>> postForEntity(URI url, HttpEntity<?> request, Class<T> responseType) throws RestClientException {
/* 319 */     AsyncRequestCallback requestCallback = httpEntityCallback(request, responseType);
/* 320 */     ResponseExtractor<ResponseEntity<T>> responseExtractor = responseEntityExtractor(responseType);
/* 321 */     return execute(url, HttpMethod.POST, requestCallback, responseExtractor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ListenableFuture<?> put(String url, HttpEntity<?> request, Object... uriVariables) throws RestClientException {
/* 329 */     AsyncRequestCallback requestCallback = httpEntityCallback(request);
/* 330 */     return execute(url, HttpMethod.PUT, requestCallback, (ResponseExtractor<?>)null, uriVariables);
/*     */   }
/*     */ 
/*     */   
/*     */   public ListenableFuture<?> put(String url, HttpEntity<?> request, Map<String, ?> uriVariables) throws RestClientException {
/* 335 */     AsyncRequestCallback requestCallback = httpEntityCallback(request);
/* 336 */     return execute(url, HttpMethod.PUT, requestCallback, (ResponseExtractor<?>)null, uriVariables);
/*     */   }
/*     */ 
/*     */   
/*     */   public ListenableFuture<?> put(URI url, HttpEntity<?> request) throws RestClientException {
/* 341 */     AsyncRequestCallback requestCallback = httpEntityCallback(request);
/* 342 */     return execute(url, HttpMethod.PUT, requestCallback, (ResponseExtractor<?>)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ListenableFuture<?> delete(String url, Object... uriVariables) throws RestClientException {
/* 350 */     return execute(url, HttpMethod.DELETE, (AsyncRequestCallback)null, (ResponseExtractor<?>)null, uriVariables);
/*     */   }
/*     */ 
/*     */   
/*     */   public ListenableFuture<?> delete(String url, Map<String, ?> uriVariables) throws RestClientException {
/* 355 */     return execute(url, HttpMethod.DELETE, (AsyncRequestCallback)null, (ResponseExtractor<?>)null, uriVariables);
/*     */   }
/*     */ 
/*     */   
/*     */   public ListenableFuture<?> delete(URI url) throws RestClientException {
/* 360 */     return execute(url, HttpMethod.DELETE, (AsyncRequestCallback)null, (ResponseExtractor<?>)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ListenableFuture<Set<HttpMethod>> optionsForAllow(String url, Object... uriVars) throws RestClientException {
/* 368 */     ResponseExtractor<HttpHeaders> extractor = headersExtractor();
/* 369 */     ListenableFuture<HttpHeaders> future = execute(url, HttpMethod.OPTIONS, (AsyncRequestCallback)null, extractor, uriVars);
/* 370 */     return adaptToAllowHeader(future);
/*     */   }
/*     */ 
/*     */   
/*     */   public ListenableFuture<Set<HttpMethod>> optionsForAllow(String url, Map<String, ?> uriVars) throws RestClientException {
/* 375 */     ResponseExtractor<HttpHeaders> extractor = headersExtractor();
/* 376 */     ListenableFuture<HttpHeaders> future = execute(url, HttpMethod.OPTIONS, (AsyncRequestCallback)null, extractor, uriVars);
/* 377 */     return adaptToAllowHeader(future);
/*     */   }
/*     */ 
/*     */   
/*     */   public ListenableFuture<Set<HttpMethod>> optionsForAllow(URI url) throws RestClientException {
/* 382 */     ResponseExtractor<HttpHeaders> extractor = headersExtractor();
/* 383 */     ListenableFuture<HttpHeaders> future = execute(url, HttpMethod.OPTIONS, (AsyncRequestCallback)null, extractor);
/* 384 */     return adaptToAllowHeader(future);
/*     */   }
/*     */   
/*     */   private static ListenableFuture<Set<HttpMethod>> adaptToAllowHeader(ListenableFuture<HttpHeaders> future) {
/* 388 */     return (ListenableFuture)new ListenableFutureAdapter<Set<HttpMethod>, HttpHeaders>(future)
/*     */       {
/*     */         protected Set<HttpMethod> adapt(HttpHeaders headers) throws ExecutionException {
/* 391 */           return headers.getAllow();
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> ListenableFuture<ResponseEntity<T>> exchange(String url, HttpMethod method, HttpEntity<?> requestEntity, Class<T> responseType, Object... uriVariables) throws RestClientException {
/* 402 */     AsyncRequestCallback requestCallback = httpEntityCallback(requestEntity, responseType);
/* 403 */     ResponseExtractor<ResponseEntity<T>> responseExtractor = responseEntityExtractor(responseType);
/* 404 */     return execute(url, method, requestCallback, responseExtractor, uriVariables);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> ListenableFuture<ResponseEntity<T>> exchange(String url, HttpMethod method, HttpEntity<?> requestEntity, Class<T> responseType, Map<String, ?> uriVariables) throws RestClientException {
/* 411 */     AsyncRequestCallback requestCallback = httpEntityCallback(requestEntity, responseType);
/* 412 */     ResponseExtractor<ResponseEntity<T>> responseExtractor = responseEntityExtractor(responseType);
/* 413 */     return execute(url, method, requestCallback, responseExtractor, uriVariables);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> ListenableFuture<ResponseEntity<T>> exchange(URI url, HttpMethod method, HttpEntity<?> requestEntity, Class<T> responseType) throws RestClientException {
/* 420 */     AsyncRequestCallback requestCallback = httpEntityCallback(requestEntity, responseType);
/* 421 */     ResponseExtractor<ResponseEntity<T>> responseExtractor = responseEntityExtractor(responseType);
/* 422 */     return execute(url, method, requestCallback, responseExtractor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> ListenableFuture<ResponseEntity<T>> exchange(String url, HttpMethod method, HttpEntity<?> requestEntity, ParameterizedTypeReference<T> responseType, Object... uriVariables) throws RestClientException {
/* 429 */     Type type = responseType.getType();
/* 430 */     AsyncRequestCallback requestCallback = httpEntityCallback(requestEntity, type);
/* 431 */     ResponseExtractor<ResponseEntity<T>> responseExtractor = responseEntityExtractor(type);
/* 432 */     return execute(url, method, requestCallback, responseExtractor, uriVariables);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> ListenableFuture<ResponseEntity<T>> exchange(String url, HttpMethod method, HttpEntity<?> requestEntity, ParameterizedTypeReference<T> responseType, Map<String, ?> uriVariables) throws RestClientException {
/* 439 */     Type type = responseType.getType();
/* 440 */     AsyncRequestCallback requestCallback = httpEntityCallback(requestEntity, type);
/* 441 */     ResponseExtractor<ResponseEntity<T>> responseExtractor = responseEntityExtractor(type);
/* 442 */     return execute(url, method, requestCallback, responseExtractor, uriVariables);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> ListenableFuture<ResponseEntity<T>> exchange(URI url, HttpMethod method, HttpEntity<?> requestEntity, ParameterizedTypeReference<T> responseType) throws RestClientException {
/* 449 */     Type type = responseType.getType();
/* 450 */     AsyncRequestCallback requestCallback = httpEntityCallback(requestEntity, type);
/* 451 */     ResponseExtractor<ResponseEntity<T>> responseExtractor = responseEntityExtractor(type);
/* 452 */     return execute(url, method, requestCallback, responseExtractor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> ListenableFuture<T> execute(String url, HttpMethod method, AsyncRequestCallback requestCallback, ResponseExtractor<T> responseExtractor, Object... uriVariables) throws RestClientException {
/* 462 */     URI expanded = getUriTemplateHandler().expand(url, uriVariables);
/* 463 */     return doExecute(expanded, method, requestCallback, responseExtractor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> ListenableFuture<T> execute(String url, HttpMethod method, AsyncRequestCallback requestCallback, ResponseExtractor<T> responseExtractor, Map<String, ?> uriVariables) throws RestClientException {
/* 470 */     URI expanded = getUriTemplateHandler().expand(url, uriVariables);
/* 471 */     return doExecute(expanded, method, requestCallback, responseExtractor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> ListenableFuture<T> execute(URI url, HttpMethod method, AsyncRequestCallback requestCallback, ResponseExtractor<T> responseExtractor) throws RestClientException {
/* 478 */     return doExecute(url, method, requestCallback, responseExtractor);
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
/*     */   protected <T> ListenableFuture<T> doExecute(URI url, HttpMethod method, AsyncRequestCallback requestCallback, ResponseExtractor<T> responseExtractor) throws RestClientException {
/* 496 */     Assert.notNull(url, "'url' must not be null");
/* 497 */     Assert.notNull(method, "'method' must not be null");
/*     */     try {
/* 499 */       AsyncClientHttpRequest request = createAsyncRequest(url, method);
/* 500 */       if (requestCallback != null) {
/* 501 */         requestCallback.doWithRequest(request);
/*     */       }
/* 503 */       ListenableFuture<ClientHttpResponse> responseFuture = request.executeAsync();
/* 504 */       return (ListenableFuture<T>)new ResponseExtractorFuture<T>(method, url, responseFuture, responseExtractor);
/*     */     }
/* 506 */     catch (IOException ex) {
/* 507 */       throw new ResourceAccessException("I/O error on " + method.name() + " request for \"" + url + "\":" + ex
/* 508 */           .getMessage(), ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void logResponseStatus(HttpMethod method, URI url, ClientHttpResponse response) {
/* 513 */     if (this.logger.isDebugEnabled()) {
/*     */       try {
/* 515 */         this.logger.debug("Async " + method.name() + " request for \"" + url + "\" resulted in " + response
/* 516 */             .getRawStatusCode() + " (" + response.getStatusText() + ")");
/*     */       }
/* 518 */       catch (IOException iOException) {}
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void handleResponseError(HttpMethod method, URI url, ClientHttpResponse response) throws IOException {
/* 525 */     if (this.logger.isWarnEnabled()) {
/*     */       try {
/* 527 */         this.logger.warn("Async " + method.name() + " request for \"" + url + "\" resulted in " + response
/* 528 */             .getRawStatusCode() + " (" + response.getStatusText() + "); invoking error handler");
/*     */       }
/* 530 */       catch (IOException iOException) {}
/*     */     }
/*     */ 
/*     */     
/* 534 */     getErrorHandler().handleError(response);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected <T> AsyncRequestCallback acceptHeaderRequestCallback(Class<T> responseType) {
/* 543 */     return new AsyncRequestCallbackAdapter(this.syncTemplate.acceptHeaderRequestCallback(responseType));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected <T> AsyncRequestCallback httpEntityCallback(HttpEntity<T> requestBody) {
/* 551 */     return new AsyncRequestCallbackAdapter(this.syncTemplate.httpEntityCallback(requestBody));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected <T> AsyncRequestCallback httpEntityCallback(HttpEntity<T> request, Type responseType) {
/* 559 */     return new AsyncRequestCallbackAdapter(this.syncTemplate.httpEntityCallback(request, responseType));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected <T> ResponseExtractor<ResponseEntity<T>> responseEntityExtractor(Type responseType) {
/* 566 */     return this.syncTemplate.responseEntityExtractor(responseType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ResponseExtractor<HttpHeaders> headersExtractor() {
/* 573 */     return this.syncTemplate.headersExtractor();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class ResponseExtractorFuture<T>
/*     */     extends ListenableFutureAdapter<T, ClientHttpResponse>
/*     */   {
/*     */     private final HttpMethod method;
/*     */ 
/*     */     
/*     */     private final URI url;
/*     */ 
/*     */     
/*     */     private final ResponseExtractor<T> responseExtractor;
/*     */ 
/*     */     
/*     */     public ResponseExtractorFuture(HttpMethod method, URI url, ListenableFuture<ClientHttpResponse> clientHttpResponseFuture, ResponseExtractor<T> responseExtractor) {
/* 591 */       super(clientHttpResponseFuture);
/* 592 */       this.method = method;
/* 593 */       this.url = url;
/* 594 */       this.responseExtractor = responseExtractor;
/*     */     }
/*     */ 
/*     */     
/*     */     protected final T adapt(ClientHttpResponse response) throws ExecutionException {
/*     */       try {
/* 600 */         if (!AsyncRestTemplate.this.getErrorHandler().hasError(response)) {
/* 601 */           AsyncRestTemplate.this.logResponseStatus(this.method, this.url, response);
/*     */         } else {
/*     */           
/* 604 */           AsyncRestTemplate.this.handleResponseError(this.method, this.url, response);
/*     */         } 
/* 606 */         return convertResponse(response);
/*     */       }
/* 608 */       catch (Throwable ex) {
/* 609 */         throw new ExecutionException(ex);
/*     */       } finally {
/*     */         
/* 612 */         if (response != null) {
/* 613 */           response.close();
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/*     */     protected T convertResponse(ClientHttpResponse response) throws IOException {
/* 619 */       return (this.responseExtractor != null) ? this.responseExtractor.extractData(response) : null;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class AsyncRequestCallbackAdapter
/*     */     implements AsyncRequestCallback
/*     */   {
/*     */     private final RequestCallback adaptee;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public AsyncRequestCallbackAdapter(RequestCallback requestCallback) {
/* 637 */       this.adaptee = requestCallback;
/*     */     }
/*     */ 
/*     */     
/*     */     public void doWithRequest(final AsyncClientHttpRequest request) throws IOException {
/* 642 */       if (this.adaptee != null)
/* 643 */         this.adaptee.doWithRequest(new ClientHttpRequest()
/*     */             {
/*     */               public ClientHttpResponse execute() throws IOException {
/* 646 */                 throw new UnsupportedOperationException("execute not supported");
/*     */               }
/*     */               
/*     */               public OutputStream getBody() throws IOException {
/* 650 */                 return request.getBody();
/*     */               }
/*     */               
/*     */               public HttpMethod getMethod() {
/* 654 */                 return request.getMethod();
/*     */               }
/*     */               
/*     */               public URI getURI() {
/* 658 */                 return request.getURI();
/*     */               }
/*     */               
/*     */               public HttpHeaders getHeaders() {
/* 662 */                 return request.getHeaders();
/*     */               }
/*     */             }); 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\client\AsyncRestTemplate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */