/*     */ package org.springframework.web.client;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Type;
/*     */ import java.net.URI;
/*     */ import java.util.ArrayList;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.springframework.core.ParameterizedTypeReference;
/*     */ import org.springframework.http.HttpEntity;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpMethod;
/*     */ import org.springframework.http.HttpOutputMessage;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.RequestEntity;
/*     */ import org.springframework.http.ResponseEntity;
/*     */ import org.springframework.http.client.ClientHttpRequest;
/*     */ import org.springframework.http.client.ClientHttpRequestFactory;
/*     */ import org.springframework.http.client.ClientHttpResponse;
/*     */ import org.springframework.http.client.support.InterceptingHttpAccessor;
/*     */ import org.springframework.http.converter.ByteArrayHttpMessageConverter;
/*     */ import org.springframework.http.converter.GenericHttpMessageConverter;
/*     */ import org.springframework.http.converter.HttpMessageConverter;
/*     */ import org.springframework.http.converter.ResourceHttpMessageConverter;
/*     */ import org.springframework.http.converter.StringHttpMessageConverter;
/*     */ import org.springframework.http.converter.feed.AtomFeedHttpMessageConverter;
/*     */ import org.springframework.http.converter.feed.RssChannelHttpMessageConverter;
/*     */ import org.springframework.http.converter.json.GsonHttpMessageConverter;
/*     */ import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
/*     */ import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
/*     */ import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
/*     */ import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
/*     */ import org.springframework.http.converter.xml.SourceHttpMessageConverter;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.web.util.AbstractUriTemplateHandler;
/*     */ import org.springframework.web.util.DefaultUriTemplateHandler;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RestTemplate
/*     */   extends InterceptingHttpAccessor
/*     */   implements RestOperations
/*     */ {
/* 126 */   private static boolean romePresent = ClassUtils.isPresent("com.rometools.rome.feed.WireFeed", RestTemplate.class
/* 127 */       .getClassLoader());
/*     */ 
/*     */   
/* 130 */   private static final boolean jaxb2Present = ClassUtils.isPresent("javax.xml.bind.Binder", RestTemplate.class
/* 131 */       .getClassLoader());
/*     */   
/* 133 */   private static final boolean jackson2Present = (
/* 134 */     ClassUtils.isPresent("com.fasterxml.jackson.databind.ObjectMapper", RestTemplate.class
/* 135 */       .getClassLoader()) && 
/* 136 */     ClassUtils.isPresent("com.fasterxml.jackson.core.JsonGenerator", RestTemplate.class
/* 137 */       .getClassLoader()));
/*     */ 
/*     */   
/* 140 */   private static final boolean jackson2XmlPresent = ClassUtils.isPresent("com.fasterxml.jackson.dataformat.xml.XmlMapper", RestTemplate.class
/* 141 */       .getClassLoader());
/*     */ 
/*     */   
/* 144 */   private static final boolean gsonPresent = ClassUtils.isPresent("com.google.gson.Gson", RestTemplate.class
/* 145 */       .getClassLoader());
/*     */ 
/*     */   
/* 148 */   private final List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
/*     */   
/* 150 */   private ResponseErrorHandler errorHandler = new DefaultResponseErrorHandler();
/*     */   
/* 152 */   private UriTemplateHandler uriTemplateHandler = (UriTemplateHandler)new DefaultUriTemplateHandler();
/*     */   
/* 154 */   private final ResponseExtractor<HttpHeaders> headersExtractor = new HeadersExtractor();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RestTemplate() {
/* 162 */     this.messageConverters.add(new ByteArrayHttpMessageConverter());
/* 163 */     this.messageConverters.add(new StringHttpMessageConverter());
/* 164 */     this.messageConverters.add(new ResourceHttpMessageConverter());
/* 165 */     this.messageConverters.add(new SourceHttpMessageConverter());
/* 166 */     this.messageConverters.add(new AllEncompassingFormHttpMessageConverter());
/*     */     
/* 168 */     if (romePresent) {
/* 169 */       this.messageConverters.add(new AtomFeedHttpMessageConverter());
/* 170 */       this.messageConverters.add(new RssChannelHttpMessageConverter());
/*     */     } 
/*     */     
/* 173 */     if (jackson2XmlPresent) {
/* 174 */       this.messageConverters.add(new MappingJackson2XmlHttpMessageConverter());
/*     */     }
/* 176 */     else if (jaxb2Present) {
/* 177 */       this.messageConverters.add(new Jaxb2RootElementHttpMessageConverter());
/*     */     } 
/*     */     
/* 180 */     if (jackson2Present) {
/* 181 */       this.messageConverters.add(new MappingJackson2HttpMessageConverter());
/*     */     }
/* 183 */     else if (gsonPresent) {
/* 184 */       this.messageConverters.add(new GsonHttpMessageConverter());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RestTemplate(ClientHttpRequestFactory requestFactory) {
/* 195 */     this();
/* 196 */     setRequestFactory(requestFactory);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RestTemplate(List<HttpMessageConverter<?>> messageConverters) {
/* 206 */     Assert.notEmpty(messageConverters, "At least one HttpMessageConverter required");
/* 207 */     this.messageConverters.addAll(messageConverters);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMessageConverters(List<HttpMessageConverter<?>> messageConverters) {
/* 216 */     Assert.notEmpty(messageConverters, "At least one HttpMessageConverter required");
/*     */     
/* 218 */     if (this.messageConverters != messageConverters) {
/* 219 */       this.messageConverters.clear();
/* 220 */       this.messageConverters.addAll(messageConverters);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<HttpMessageConverter<?>> getMessageConverters() {
/* 229 */     return this.messageConverters;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setErrorHandler(ResponseErrorHandler errorHandler) {
/* 237 */     Assert.notNull(errorHandler, "ResponseErrorHandler must not be null");
/* 238 */     this.errorHandler = errorHandler;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResponseErrorHandler getErrorHandler() {
/* 245 */     return this.errorHandler;
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
/* 261 */     Assert.isInstanceOf(AbstractUriTemplateHandler.class, this.uriTemplateHandler, "Can only use this property in conjunction with an AbstractUriTemplateHandler");
/*     */     
/* 263 */     ((AbstractUriTemplateHandler)this.uriTemplateHandler).setDefaultUriVariables(defaultUriVariables);
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
/*     */   public void setUriTemplateHandler(UriTemplateHandler handler) {
/* 276 */     Assert.notNull(handler, "UriTemplateHandler must not be null");
/* 277 */     this.uriTemplateHandler = handler;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UriTemplateHandler getUriTemplateHandler() {
/* 284 */     return this.uriTemplateHandler;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T getForObject(String url, Class<T> responseType, Object... uriVariables) throws RestClientException {
/* 292 */     RequestCallback requestCallback = acceptHeaderRequestCallback(responseType);
/*     */     
/* 294 */     HttpMessageConverterExtractor<T> responseExtractor = new HttpMessageConverterExtractor<T>(responseType, getMessageConverters(), this.logger);
/* 295 */     return execute(url, HttpMethod.GET, requestCallback, responseExtractor, uriVariables);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T getForObject(String url, Class<T> responseType, Map<String, ?> uriVariables) throws RestClientException {
/* 300 */     RequestCallback requestCallback = acceptHeaderRequestCallback(responseType);
/*     */     
/* 302 */     HttpMessageConverterExtractor<T> responseExtractor = new HttpMessageConverterExtractor<T>(responseType, getMessageConverters(), this.logger);
/* 303 */     return execute(url, HttpMethod.GET, requestCallback, responseExtractor, uriVariables);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T getForObject(URI url, Class<T> responseType) throws RestClientException {
/* 308 */     RequestCallback requestCallback = acceptHeaderRequestCallback(responseType);
/*     */     
/* 310 */     HttpMessageConverterExtractor<T> responseExtractor = new HttpMessageConverterExtractor<T>(responseType, getMessageConverters(), this.logger);
/* 311 */     return execute(url, HttpMethod.GET, requestCallback, responseExtractor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> ResponseEntity<T> getForEntity(String url, Class<T> responseType, Object... uriVariables) throws RestClientException {
/* 318 */     RequestCallback requestCallback = acceptHeaderRequestCallback(responseType);
/* 319 */     ResponseExtractor<ResponseEntity<T>> responseExtractor = responseEntityExtractor(responseType);
/* 320 */     return execute(url, HttpMethod.GET, requestCallback, responseExtractor, uriVariables);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> ResponseEntity<T> getForEntity(String url, Class<T> responseType, Map<String, ?> uriVariables) throws RestClientException {
/* 327 */     RequestCallback requestCallback = acceptHeaderRequestCallback(responseType);
/* 328 */     ResponseExtractor<ResponseEntity<T>> responseExtractor = responseEntityExtractor(responseType);
/* 329 */     return execute(url, HttpMethod.GET, requestCallback, responseExtractor, uriVariables);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> ResponseEntity<T> getForEntity(URI url, Class<T> responseType) throws RestClientException {
/* 334 */     RequestCallback requestCallback = acceptHeaderRequestCallback(responseType);
/* 335 */     ResponseExtractor<ResponseEntity<T>> responseExtractor = responseEntityExtractor(responseType);
/* 336 */     return execute(url, HttpMethod.GET, requestCallback, responseExtractor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpHeaders headForHeaders(String url, Object... uriVariables) throws RestClientException {
/* 344 */     return execute(url, HttpMethod.HEAD, (RequestCallback)null, headersExtractor(), uriVariables);
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpHeaders headForHeaders(String url, Map<String, ?> uriVariables) throws RestClientException {
/* 349 */     return execute(url, HttpMethod.HEAD, (RequestCallback)null, headersExtractor(), uriVariables);
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpHeaders headForHeaders(URI url) throws RestClientException {
/* 354 */     return execute(url, HttpMethod.HEAD, (RequestCallback)null, headersExtractor());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URI postForLocation(String url, Object request, Object... uriVariables) throws RestClientException {
/* 362 */     RequestCallback requestCallback = httpEntityCallback(request);
/* 363 */     HttpHeaders headers = execute(url, HttpMethod.POST, requestCallback, headersExtractor(), uriVariables);
/* 364 */     return headers.getLocation();
/*     */   }
/*     */ 
/*     */   
/*     */   public URI postForLocation(String url, Object request, Map<String, ?> uriVariables) throws RestClientException {
/* 369 */     RequestCallback requestCallback = httpEntityCallback(request);
/* 370 */     HttpHeaders headers = execute(url, HttpMethod.POST, requestCallback, headersExtractor(), uriVariables);
/* 371 */     return headers.getLocation();
/*     */   }
/*     */ 
/*     */   
/*     */   public URI postForLocation(URI url, Object request) throws RestClientException {
/* 376 */     RequestCallback requestCallback = httpEntityCallback(request);
/* 377 */     HttpHeaders headers = execute(url, HttpMethod.POST, requestCallback, headersExtractor());
/* 378 */     return headers.getLocation();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T postForObject(String url, Object request, Class<T> responseType, Object... uriVariables) throws RestClientException {
/* 385 */     RequestCallback requestCallback = httpEntityCallback(request, responseType);
/*     */     
/* 387 */     HttpMessageConverterExtractor<T> responseExtractor = new HttpMessageConverterExtractor<T>(responseType, getMessageConverters(), this.logger);
/* 388 */     return execute(url, HttpMethod.POST, requestCallback, responseExtractor, uriVariables);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T postForObject(String url, Object request, Class<T> responseType, Map<String, ?> uriVariables) throws RestClientException {
/* 395 */     RequestCallback requestCallback = httpEntityCallback(request, responseType);
/*     */     
/* 397 */     HttpMessageConverterExtractor<T> responseExtractor = new HttpMessageConverterExtractor<T>(responseType, getMessageConverters(), this.logger);
/* 398 */     return execute(url, HttpMethod.POST, requestCallback, responseExtractor, uriVariables);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T postForObject(URI url, Object request, Class<T> responseType) throws RestClientException {
/* 403 */     RequestCallback requestCallback = httpEntityCallback(request, responseType);
/*     */     
/* 405 */     HttpMessageConverterExtractor<T> responseExtractor = new HttpMessageConverterExtractor<T>(responseType, getMessageConverters());
/* 406 */     return execute(url, HttpMethod.POST, requestCallback, responseExtractor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> ResponseEntity<T> postForEntity(String url, Object request, Class<T> responseType, Object... uriVariables) throws RestClientException {
/* 413 */     RequestCallback requestCallback = httpEntityCallback(request, responseType);
/* 414 */     ResponseExtractor<ResponseEntity<T>> responseExtractor = responseEntityExtractor(responseType);
/* 415 */     return execute(url, HttpMethod.POST, requestCallback, responseExtractor, uriVariables);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> ResponseEntity<T> postForEntity(String url, Object request, Class<T> responseType, Map<String, ?> uriVariables) throws RestClientException {
/* 422 */     RequestCallback requestCallback = httpEntityCallback(request, responseType);
/* 423 */     ResponseExtractor<ResponseEntity<T>> responseExtractor = responseEntityExtractor(responseType);
/* 424 */     return execute(url, HttpMethod.POST, requestCallback, responseExtractor, uriVariables);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> ResponseEntity<T> postForEntity(URI url, Object request, Class<T> responseType) throws RestClientException {
/* 429 */     RequestCallback requestCallback = httpEntityCallback(request, responseType);
/* 430 */     ResponseExtractor<ResponseEntity<T>> responseExtractor = responseEntityExtractor(responseType);
/* 431 */     return execute(url, HttpMethod.POST, requestCallback, responseExtractor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void put(String url, Object request, Object... uriVariables) throws RestClientException {
/* 439 */     RequestCallback requestCallback = httpEntityCallback(request);
/* 440 */     execute(url, HttpMethod.PUT, requestCallback, (ResponseExtractor<?>)null, uriVariables);
/*     */   }
/*     */ 
/*     */   
/*     */   public void put(String url, Object request, Map<String, ?> uriVariables) throws RestClientException {
/* 445 */     RequestCallback requestCallback = httpEntityCallback(request);
/* 446 */     execute(url, HttpMethod.PUT, requestCallback, (ResponseExtractor<?>)null, uriVariables);
/*     */   }
/*     */ 
/*     */   
/*     */   public void put(URI url, Object request) throws RestClientException {
/* 451 */     RequestCallback requestCallback = httpEntityCallback(request);
/* 452 */     execute(url, HttpMethod.PUT, requestCallback, (ResponseExtractor<?>)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T patchForObject(String url, Object request, Class<T> responseType, Object... uriVariables) throws RestClientException {
/* 462 */     RequestCallback requestCallback = httpEntityCallback(request, responseType);
/*     */     
/* 464 */     HttpMessageConverterExtractor<T> responseExtractor = new HttpMessageConverterExtractor<T>(responseType, getMessageConverters(), this.logger);
/* 465 */     return execute(url, HttpMethod.PATCH, requestCallback, responseExtractor, uriVariables);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T patchForObject(String url, Object request, Class<T> responseType, Map<String, ?> uriVariables) throws RestClientException {
/* 472 */     RequestCallback requestCallback = httpEntityCallback(request, responseType);
/*     */     
/* 474 */     HttpMessageConverterExtractor<T> responseExtractor = new HttpMessageConverterExtractor<T>(responseType, getMessageConverters(), this.logger);
/* 475 */     return execute(url, HttpMethod.PATCH, requestCallback, responseExtractor, uriVariables);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T patchForObject(URI url, Object request, Class<T> responseType) throws RestClientException {
/* 482 */     RequestCallback requestCallback = httpEntityCallback(request, responseType);
/*     */     
/* 484 */     HttpMessageConverterExtractor<T> responseExtractor = new HttpMessageConverterExtractor<T>(responseType, getMessageConverters());
/* 485 */     return execute(url, HttpMethod.PATCH, requestCallback, responseExtractor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void delete(String url, Object... uriVariables) throws RestClientException {
/* 493 */     execute(url, HttpMethod.DELETE, (RequestCallback)null, (ResponseExtractor<?>)null, uriVariables);
/*     */   }
/*     */ 
/*     */   
/*     */   public void delete(String url, Map<String, ?> uriVariables) throws RestClientException {
/* 498 */     execute(url, HttpMethod.DELETE, (RequestCallback)null, (ResponseExtractor<?>)null, uriVariables);
/*     */   }
/*     */ 
/*     */   
/*     */   public void delete(URI url) throws RestClientException {
/* 503 */     execute(url, HttpMethod.DELETE, (RequestCallback)null, (ResponseExtractor<?>)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<HttpMethod> optionsForAllow(String url, Object... uriVariables) throws RestClientException {
/* 511 */     ResponseExtractor<HttpHeaders> headersExtractor = headersExtractor();
/* 512 */     HttpHeaders headers = execute(url, HttpMethod.OPTIONS, (RequestCallback)null, headersExtractor, uriVariables);
/* 513 */     return headers.getAllow();
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<HttpMethod> optionsForAllow(String url, Map<String, ?> uriVariables) throws RestClientException {
/* 518 */     ResponseExtractor<HttpHeaders> headersExtractor = headersExtractor();
/* 519 */     HttpHeaders headers = execute(url, HttpMethod.OPTIONS, (RequestCallback)null, headersExtractor, uriVariables);
/* 520 */     return headers.getAllow();
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<HttpMethod> optionsForAllow(URI url) throws RestClientException {
/* 525 */     ResponseExtractor<HttpHeaders> headersExtractor = headersExtractor();
/* 526 */     HttpHeaders headers = execute(url, HttpMethod.OPTIONS, (RequestCallback)null, headersExtractor);
/* 527 */     return headers.getAllow();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> ResponseEntity<T> exchange(String url, HttpMethod method, HttpEntity<?> requestEntity, Class<T> responseType, Object... uriVariables) throws RestClientException {
/* 537 */     RequestCallback requestCallback = httpEntityCallback(requestEntity, responseType);
/* 538 */     ResponseExtractor<ResponseEntity<T>> responseExtractor = responseEntityExtractor(responseType);
/* 539 */     return execute(url, method, requestCallback, responseExtractor, uriVariables);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> ResponseEntity<T> exchange(String url, HttpMethod method, HttpEntity<?> requestEntity, Class<T> responseType, Map<String, ?> uriVariables) throws RestClientException {
/* 546 */     RequestCallback requestCallback = httpEntityCallback(requestEntity, responseType);
/* 547 */     ResponseExtractor<ResponseEntity<T>> responseExtractor = responseEntityExtractor(responseType);
/* 548 */     return execute(url, method, requestCallback, responseExtractor, uriVariables);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> ResponseEntity<T> exchange(URI url, HttpMethod method, HttpEntity<?> requestEntity, Class<T> responseType) throws RestClientException {
/* 555 */     RequestCallback requestCallback = httpEntityCallback(requestEntity, responseType);
/* 556 */     ResponseExtractor<ResponseEntity<T>> responseExtractor = responseEntityExtractor(responseType);
/* 557 */     return execute(url, method, requestCallback, responseExtractor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> ResponseEntity<T> exchange(String url, HttpMethod method, HttpEntity<?> requestEntity, ParameterizedTypeReference<T> responseType, Object... uriVariables) throws RestClientException {
/* 564 */     Type type = responseType.getType();
/* 565 */     RequestCallback requestCallback = httpEntityCallback(requestEntity, type);
/* 566 */     ResponseExtractor<ResponseEntity<T>> responseExtractor = responseEntityExtractor(type);
/* 567 */     return execute(url, method, requestCallback, responseExtractor, uriVariables);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> ResponseEntity<T> exchange(String url, HttpMethod method, HttpEntity<?> requestEntity, ParameterizedTypeReference<T> responseType, Map<String, ?> uriVariables) throws RestClientException {
/* 574 */     Type type = responseType.getType();
/* 575 */     RequestCallback requestCallback = httpEntityCallback(requestEntity, type);
/* 576 */     ResponseExtractor<ResponseEntity<T>> responseExtractor = responseEntityExtractor(type);
/* 577 */     return execute(url, method, requestCallback, responseExtractor, uriVariables);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> ResponseEntity<T> exchange(URI url, HttpMethod method, HttpEntity<?> requestEntity, ParameterizedTypeReference<T> responseType) throws RestClientException {
/* 584 */     Type type = responseType.getType();
/* 585 */     RequestCallback requestCallback = httpEntityCallback(requestEntity, type);
/* 586 */     ResponseExtractor<ResponseEntity<T>> responseExtractor = responseEntityExtractor(type);
/* 587 */     return execute(url, method, requestCallback, responseExtractor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> ResponseEntity<T> exchange(RequestEntity<?> requestEntity, Class<T> responseType) throws RestClientException {
/* 594 */     Assert.notNull(requestEntity, "RequestEntity must not be null");
/*     */     
/* 596 */     RequestCallback requestCallback = httpEntityCallback(requestEntity, responseType);
/* 597 */     ResponseExtractor<ResponseEntity<T>> responseExtractor = responseEntityExtractor(responseType);
/* 598 */     return execute(requestEntity.getUrl(), requestEntity.getMethod(), requestCallback, responseExtractor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> ResponseEntity<T> exchange(RequestEntity<?> requestEntity, ParameterizedTypeReference<T> responseType) throws RestClientException {
/* 605 */     Assert.notNull(requestEntity, "RequestEntity must not be null");
/*     */     
/* 607 */     Type type = responseType.getType();
/* 608 */     RequestCallback requestCallback = httpEntityCallback(requestEntity, type);
/* 609 */     ResponseExtractor<ResponseEntity<T>> responseExtractor = responseEntityExtractor(type);
/* 610 */     return execute(requestEntity.getUrl(), requestEntity.getMethod(), requestCallback, responseExtractor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T execute(String url, HttpMethod method, RequestCallback requestCallback, ResponseExtractor<T> responseExtractor, Object... uriVariables) throws RestClientException {
/* 620 */     URI expanded = getUriTemplateHandler().expand(url, uriVariables);
/* 621 */     return doExecute(expanded, method, requestCallback, responseExtractor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T execute(String url, HttpMethod method, RequestCallback requestCallback, ResponseExtractor<T> responseExtractor, Map<String, ?> uriVariables) throws RestClientException {
/* 628 */     URI expanded = getUriTemplateHandler().expand(url, uriVariables);
/* 629 */     return doExecute(expanded, method, requestCallback, responseExtractor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T execute(URI url, HttpMethod method, RequestCallback requestCallback, ResponseExtractor<T> responseExtractor) throws RestClientException {
/* 636 */     return doExecute(url, method, requestCallback, responseExtractor);
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
/*     */   protected <T> T doExecute(URI url, HttpMethod method, RequestCallback requestCallback, ResponseExtractor<T> responseExtractor) throws RestClientException {
/* 652 */     Assert.notNull(url, "'url' must not be null");
/* 653 */     Assert.notNull(method, "'method' must not be null");
/* 654 */     ClientHttpResponse response = null;
/*     */     try {
/* 656 */       ClientHttpRequest request = createRequest(url, method);
/* 657 */       if (requestCallback != null) {
/* 658 */         requestCallback.doWithRequest(request);
/*     */       }
/* 660 */       response = request.execute();
/* 661 */       handleResponse(url, method, response);
/* 662 */       if (responseExtractor != null) {
/* 663 */         return responseExtractor.extractData(response);
/*     */       }
/*     */       
/* 666 */       return null;
/*     */     
/*     */     }
/* 669 */     catch (IOException ex) {
/* 670 */       String resource = url.toString();
/* 671 */       String query = url.getRawQuery();
/* 672 */       resource = (query != null) ? resource.substring(0, resource.indexOf('?')) : resource;
/* 673 */       throw new ResourceAccessException("I/O error on " + method.name() + " request for \"" + resource + "\": " + ex
/* 674 */           .getMessage(), ex);
/*     */     } finally {
/*     */       
/* 677 */       if (response != null) {
/* 678 */         response.close();
/*     */       }
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
/*     */   protected void handleResponse(URI url, HttpMethod method, ClientHttpResponse response) throws IOException {
/* 695 */     ResponseErrorHandler errorHandler = getErrorHandler();
/* 696 */     boolean hasError = errorHandler.hasError(response);
/* 697 */     if (this.logger.isDebugEnabled()) {
/*     */       try {
/* 699 */         this.logger.debug(method.name() + " request for \"" + url + "\" resulted in " + response
/* 700 */             .getRawStatusCode() + " (" + response.getStatusText() + ")" + (hasError ? "; invoking error handler" : ""));
/*     */       
/*     */       }
/* 703 */       catch (IOException iOException) {}
/*     */     }
/*     */ 
/*     */     
/* 707 */     if (hasError) {
/* 708 */       errorHandler.handleError(response);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected <T> RequestCallback acceptHeaderRequestCallback(Class<T> responseType) {
/* 718 */     return new AcceptHeaderRequestCallback(responseType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected <T> RequestCallback httpEntityCallback(Object requestBody) {
/* 726 */     return new HttpEntityRequestCallback(requestBody);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected <T> RequestCallback httpEntityCallback(Object requestBody, Type responseType) {
/* 734 */     return new HttpEntityRequestCallback(requestBody, responseType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected <T> ResponseExtractor<ResponseEntity<T>> responseEntityExtractor(Type responseType) {
/* 741 */     return new ResponseEntityResponseExtractor<T>(responseType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ResponseExtractor<HttpHeaders> headersExtractor() {
/* 748 */     return this.headersExtractor;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class AcceptHeaderRequestCallback
/*     */     implements RequestCallback
/*     */   {
/*     */     private final Type responseType;
/*     */ 
/*     */     
/*     */     private AcceptHeaderRequestCallback(Type responseType) {
/* 760 */       this.responseType = responseType;
/*     */     }
/*     */ 
/*     */     
/*     */     public void doWithRequest(ClientHttpRequest request) throws IOException {
/* 765 */       if (this.responseType != null) {
/* 766 */         Class<?> responseClass = null;
/* 767 */         if (this.responseType instanceof Class) {
/* 768 */           responseClass = (Class)this.responseType;
/*     */         }
/* 770 */         List<MediaType> allSupportedMediaTypes = new ArrayList<MediaType>();
/* 771 */         for (HttpMessageConverter<?> converter : RestTemplate.this.getMessageConverters()) {
/* 772 */           if (responseClass != null) {
/* 773 */             if (converter.canRead(responseClass, null))
/* 774 */               allSupportedMediaTypes.addAll(getSupportedMediaTypes(converter)); 
/*     */             continue;
/*     */           } 
/* 777 */           if (converter instanceof GenericHttpMessageConverter) {
/* 778 */             GenericHttpMessageConverter<?> genericConverter = (GenericHttpMessageConverter)converter;
/* 779 */             if (genericConverter.canRead(this.responseType, null, null)) {
/* 780 */               allSupportedMediaTypes.addAll(getSupportedMediaTypes(converter));
/*     */             }
/*     */           } 
/*     */         } 
/* 784 */         if (!allSupportedMediaTypes.isEmpty()) {
/* 785 */           MediaType.sortBySpecificity(allSupportedMediaTypes);
/* 786 */           if (RestTemplate.this.logger.isDebugEnabled()) {
/* 787 */             RestTemplate.this.logger.debug("Setting request Accept header to " + allSupportedMediaTypes);
/*     */           }
/* 789 */           request.getHeaders().setAccept(allSupportedMediaTypes);
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     private List<MediaType> getSupportedMediaTypes(HttpMessageConverter<?> messageConverter) {
/* 795 */       List<MediaType> supportedMediaTypes = messageConverter.getSupportedMediaTypes();
/* 796 */       List<MediaType> result = new ArrayList<MediaType>(supportedMediaTypes.size());
/* 797 */       for (MediaType supportedMediaType : supportedMediaTypes) {
/* 798 */         if (supportedMediaType.getCharset() != null)
/*     */         {
/* 800 */           supportedMediaType = new MediaType(supportedMediaType.getType(), supportedMediaType.getSubtype());
/*     */         }
/* 802 */         result.add(supportedMediaType);
/*     */       } 
/* 804 */       return result;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class HttpEntityRequestCallback
/*     */     extends AcceptHeaderRequestCallback
/*     */   {
/*     */     private final HttpEntity<?> requestEntity;
/*     */ 
/*     */     
/*     */     private HttpEntityRequestCallback(Object requestBody) {
/* 817 */       this(requestBody, null);
/*     */     }
/*     */     
/*     */     private HttpEntityRequestCallback(Object requestBody, Type responseType) {
/* 821 */       super(responseType);
/* 822 */       if (requestBody instanceof HttpEntity) {
/* 823 */         this.requestEntity = (HttpEntity)requestBody;
/*     */       }
/* 825 */       else if (requestBody != null) {
/* 826 */         this.requestEntity = new HttpEntity(requestBody);
/*     */       } else {
/*     */         
/* 829 */         this.requestEntity = HttpEntity.EMPTY;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void doWithRequest(ClientHttpRequest httpRequest) throws IOException {
/* 836 */       super.doWithRequest(httpRequest);
/* 837 */       if (!this.requestEntity.hasBody()) {
/* 838 */         HttpHeaders httpHeaders = httpRequest.getHeaders();
/* 839 */         HttpHeaders requestHeaders = this.requestEntity.getHeaders();
/* 840 */         if (!requestHeaders.isEmpty()) {
/* 841 */           for (Map.Entry<String, List<String>> entry : (Iterable<Map.Entry<String, List<String>>>)requestHeaders.entrySet()) {
/* 842 */             httpHeaders.put(entry.getKey(), new LinkedList(entry.getValue()));
/*     */           }
/*     */         }
/* 845 */         if (httpHeaders.getContentLength() < 0L) {
/* 846 */           httpHeaders.setContentLength(0L);
/*     */         }
/*     */       } else {
/*     */         
/* 850 */         Object requestBody = this.requestEntity.getBody();
/* 851 */         Class<?> requestBodyClass = requestBody.getClass();
/*     */         
/* 853 */         Type requestBodyType = (this.requestEntity instanceof RequestEntity) ? ((RequestEntity)this.requestEntity).getType() : requestBodyClass;
/* 854 */         HttpHeaders httpHeaders = httpRequest.getHeaders();
/* 855 */         HttpHeaders requestHeaders = this.requestEntity.getHeaders();
/* 856 */         MediaType requestContentType = requestHeaders.getContentType();
/* 857 */         for (HttpMessageConverter<?> messageConverter : RestTemplate.this.getMessageConverters()) {
/* 858 */           if (messageConverter instanceof GenericHttpMessageConverter) {
/* 859 */             GenericHttpMessageConverter<Object> genericMessageConverter = (GenericHttpMessageConverter)messageConverter;
/* 860 */             if (genericMessageConverter.canWrite(requestBodyType, requestBodyClass, requestContentType)) {
/* 861 */               if (!requestHeaders.isEmpty()) {
/* 862 */                 for (Map.Entry<String, List<String>> entry : (Iterable<Map.Entry<String, List<String>>>)requestHeaders.entrySet()) {
/* 863 */                   httpHeaders.put(entry.getKey(), new LinkedList(entry.getValue()));
/*     */                 }
/*     */               }
/* 866 */               if (RestTemplate.this.logger.isDebugEnabled()) {
/* 867 */                 if (requestContentType != null) {
/* 868 */                   RestTemplate.this.logger.debug("Writing [" + requestBody + "] as \"" + requestContentType + "\" using [" + messageConverter + "]");
/*     */                 }
/*     */                 else {
/*     */                   
/* 872 */                   RestTemplate.this.logger.debug("Writing [" + requestBody + "] using [" + messageConverter + "]");
/*     */                 } 
/*     */               }
/*     */               
/* 876 */               genericMessageConverter.write(requestBody, requestBodyType, requestContentType, (HttpOutputMessage)httpRequest);
/*     */               return;
/*     */             } 
/*     */             continue;
/*     */           } 
/* 881 */           if (messageConverter.canWrite(requestBodyClass, requestContentType)) {
/* 882 */             if (!requestHeaders.isEmpty()) {
/* 883 */               for (Map.Entry<String, List<String>> entry : (Iterable<Map.Entry<String, List<String>>>)requestHeaders.entrySet()) {
/* 884 */                 httpHeaders.put(entry.getKey(), new LinkedList(entry.getValue()));
/*     */               }
/*     */             }
/* 887 */             if (RestTemplate.this.logger.isDebugEnabled()) {
/* 888 */               if (requestContentType != null) {
/* 889 */                 RestTemplate.this.logger.debug("Writing [" + requestBody + "] as \"" + requestContentType + "\" using [" + messageConverter + "]");
/*     */               }
/*     */               else {
/*     */                 
/* 893 */                 RestTemplate.this.logger.debug("Writing [" + requestBody + "] using [" + messageConverter + "]");
/*     */               } 
/*     */             }
/*     */             
/* 897 */             messageConverter.write(requestBody, requestContentType, (HttpOutputMessage)httpRequest);
/*     */             
/*     */             return;
/*     */           } 
/*     */         } 
/*     */         
/* 903 */         String message = "Could not write request: no suitable HttpMessageConverter found for request type [" + requestBodyClass.getName() + "]";
/* 904 */         if (requestContentType != null) {
/* 905 */           message = message + " and content type [" + requestContentType + "]";
/*     */         }
/* 907 */         throw new RestClientException(message);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class ResponseEntityResponseExtractor<T>
/*     */     implements ResponseExtractor<ResponseEntity<T>>
/*     */   {
/*     */     private final HttpMessageConverterExtractor<T> delegate;
/*     */ 
/*     */     
/*     */     public ResponseEntityResponseExtractor(Type responseType) {
/* 921 */       if (responseType != null && Void.class != responseType) {
/* 922 */         this.delegate = new HttpMessageConverterExtractor<T>(responseType, RestTemplate.this.getMessageConverters(), RestTemplate.this.logger);
/*     */       } else {
/*     */         
/* 925 */         this.delegate = null;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public ResponseEntity<T> extractData(ClientHttpResponse response) throws IOException {
/* 931 */       if (this.delegate != null) {
/* 932 */         T body = this.delegate.extractData(response);
/* 933 */         return ((ResponseEntity.BodyBuilder)ResponseEntity.status(response.getRawStatusCode()).headers(response.getHeaders())).body(body);
/*     */       } 
/*     */       
/* 936 */       return ((ResponseEntity.BodyBuilder)ResponseEntity.status(response.getRawStatusCode()).headers(response.getHeaders())).build();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class HeadersExtractor
/*     */     implements ResponseExtractor<HttpHeaders>
/*     */   {
/*     */     private HeadersExtractor() {}
/*     */ 
/*     */     
/*     */     public HttpHeaders extractData(ClientHttpResponse response) throws IOException {
/* 949 */       return response.getHeaders();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\client\RestTemplate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */