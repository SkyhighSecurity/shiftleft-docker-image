/*     */ package org.springframework.web.client;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.charset.Charset;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpStatus;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.client.ClientHttpResponse;
/*     */ import org.springframework.util.FileCopyUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultResponseErrorHandler
/*     */   implements ResponseErrorHandler
/*     */ {
/*     */   public boolean hasError(ClientHttpResponse response) throws IOException {
/*  50 */     int rawStatusCode = response.getRawStatusCode();
/*  51 */     for (HttpStatus statusCode : HttpStatus.values()) {
/*  52 */       if (statusCode.value() == rawStatusCode) {
/*  53 */         return hasError(statusCode);
/*     */       }
/*     */     } 
/*  56 */     return false;
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
/*     */   protected boolean hasError(HttpStatus statusCode) {
/*  70 */     return (statusCode.series() == HttpStatus.Series.CLIENT_ERROR || statusCode
/*  71 */       .series() == HttpStatus.Series.SERVER_ERROR);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void handleError(ClientHttpResponse response) throws IOException {
/*  82 */     HttpStatus statusCode = getHttpStatusCode(response);
/*  83 */     switch (statusCode.series()) {
/*     */       case CLIENT_ERROR:
/*  85 */         throw new HttpClientErrorException(statusCode, response.getStatusText(), response
/*  86 */             .getHeaders(), getResponseBody(response), getCharset(response));
/*     */       case SERVER_ERROR:
/*  88 */         throw new HttpServerErrorException(statusCode, response.getStatusText(), response
/*  89 */             .getHeaders(), getResponseBody(response), getCharset(response));
/*     */     } 
/*  91 */     throw new UnknownHttpStatusCodeException(statusCode.value(), response.getStatusText(), response
/*  92 */         .getHeaders(), getResponseBody(response), getCharset(response));
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
/*     */   protected HttpStatus getHttpStatusCode(ClientHttpResponse response) throws IOException {
/*     */     try {
/* 109 */       return response.getStatusCode();
/*     */     }
/* 111 */     catch (IllegalArgumentException ex) {
/* 112 */       throw new UnknownHttpStatusCodeException(response.getRawStatusCode(), response.getStatusText(), response
/* 113 */           .getHeaders(), getResponseBody(response), getCharset(response));
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
/*     */   protected byte[] getResponseBody(ClientHttpResponse response) {
/*     */     try {
/* 126 */       return FileCopyUtils.copyToByteArray(response.getBody());
/*     */     }
/* 128 */     catch (IOException iOException) {
/*     */ 
/*     */       
/* 131 */       return new byte[0];
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Charset getCharset(ClientHttpResponse response) {
/* 141 */     HttpHeaders headers = response.getHeaders();
/* 142 */     MediaType contentType = headers.getContentType();
/* 143 */     return (contentType != null) ? contentType.getCharset() : null;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\client\DefaultResponseErrorHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */