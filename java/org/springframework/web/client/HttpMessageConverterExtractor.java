/*     */ package org.springframework.web.client;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.List;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.http.HttpInputMessage;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.client.ClientHttpResponse;
/*     */ import org.springframework.http.converter.GenericHttpMessageConverter;
/*     */ import org.springframework.http.converter.HttpMessageConverter;
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
/*     */ public class HttpMessageConverterExtractor<T>
/*     */   implements ResponseExtractor<T>
/*     */ {
/*     */   private final Type responseType;
/*     */   private final Class<T> responseClass;
/*     */   private final List<HttpMessageConverter<?>> messageConverters;
/*     */   private final Log logger;
/*     */   
/*     */   public HttpMessageConverterExtractor(Class<T> responseType, List<HttpMessageConverter<?>> messageConverters) {
/*  56 */     this(responseType, messageConverters);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpMessageConverterExtractor(Type responseType, List<HttpMessageConverter<?>> messageConverters) {
/*  64 */     this(responseType, messageConverters, LogFactory.getLog(HttpMessageConverterExtractor.class));
/*     */   }
/*     */ 
/*     */   
/*     */   HttpMessageConverterExtractor(Type responseType, List<HttpMessageConverter<?>> messageConverters, Log logger) {
/*  69 */     Assert.notNull(responseType, "'responseType' must not be null");
/*  70 */     Assert.notEmpty(messageConverters, "'messageConverters' must not be empty");
/*  71 */     this.responseType = responseType;
/*  72 */     this.responseClass = (responseType instanceof Class) ? (Class<T>)responseType : null;
/*  73 */     this.messageConverters = messageConverters;
/*  74 */     this.logger = logger;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public T extractData(ClientHttpResponse response) throws IOException {
/*  81 */     MessageBodyClientHttpResponseWrapper responseWrapper = new MessageBodyClientHttpResponseWrapper(response);
/*  82 */     if (!responseWrapper.hasMessageBody() || responseWrapper.hasEmptyMessageBody()) {
/*  83 */       return null;
/*     */     }
/*  85 */     MediaType contentType = getContentType(responseWrapper);
/*     */     
/*  87 */     for (HttpMessageConverter<?> messageConverter : this.messageConverters) {
/*  88 */       if (messageConverter instanceof GenericHttpMessageConverter) {
/*  89 */         GenericHttpMessageConverter<?> genericMessageConverter = (GenericHttpMessageConverter)messageConverter;
/*     */         
/*  91 */         if (genericMessageConverter.canRead(this.responseType, null, contentType)) {
/*  92 */           if (this.logger.isDebugEnabled()) {
/*  93 */             this.logger.debug("Reading [" + this.responseType + "] as \"" + contentType + "\" using [" + messageConverter + "]");
/*     */           }
/*     */           
/*  96 */           return (T)genericMessageConverter.read(this.responseType, null, (HttpInputMessage)responseWrapper);
/*     */         } 
/*     */       } 
/*  99 */       if (this.responseClass != null && 
/* 100 */         messageConverter.canRead(this.responseClass, contentType)) {
/* 101 */         if (this.logger.isDebugEnabled()) {
/* 102 */           this.logger.debug("Reading [" + this.responseClass.getName() + "] as \"" + contentType + "\" using [" + messageConverter + "]");
/*     */         }
/*     */         
/* 105 */         return (T)messageConverter.read(this.responseClass, (HttpInputMessage)responseWrapper);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 110 */     throw new RestClientException("Could not extract response: no suitable HttpMessageConverter found for response type [" + this.responseType + "] and content type [" + contentType + "]");
/*     */   }
/*     */ 
/*     */   
/*     */   private MediaType getContentType(ClientHttpResponse response) {
/* 115 */     MediaType contentType = response.getHeaders().getContentType();
/* 116 */     if (contentType == null) {
/* 117 */       if (this.logger.isTraceEnabled()) {
/* 118 */         this.logger.trace("No Content-Type header found, defaulting to application/octet-stream");
/*     */       }
/* 120 */       contentType = MediaType.APPLICATION_OCTET_STREAM;
/*     */     } 
/* 122 */     return contentType;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\client\HttpMessageConverterExtractor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */