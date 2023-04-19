/*     */ package org.springframework.http.converter;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpInputMessage;
/*     */ import org.springframework.http.HttpOutputMessage;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.StreamingHttpOutputMessage;
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
/*     */ public abstract class AbstractHttpMessageConverter<T>
/*     */   implements HttpMessageConverter<T>
/*     */ {
/*  52 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*  54 */   private List<MediaType> supportedMediaTypes = Collections.emptyList();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Charset defaultCharset;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractHttpMessageConverter() {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractHttpMessageConverter(MediaType supportedMediaType) {
/*  71 */     setSupportedMediaTypes(Collections.singletonList(supportedMediaType));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractHttpMessageConverter(MediaType... supportedMediaTypes) {
/*  79 */     setSupportedMediaTypes(Arrays.asList(supportedMediaTypes));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractHttpMessageConverter(Charset defaultCharset, MediaType... supportedMediaTypes) {
/*  90 */     this.defaultCharset = defaultCharset;
/*  91 */     setSupportedMediaTypes(Arrays.asList(supportedMediaTypes));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSupportedMediaTypes(List<MediaType> supportedMediaTypes) {
/*  99 */     Assert.notEmpty(supportedMediaTypes, "MediaType List must not be empty");
/* 100 */     this.supportedMediaTypes = new ArrayList<MediaType>(supportedMediaTypes);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<MediaType> getSupportedMediaTypes() {
/* 105 */     return Collections.unmodifiableList(this.supportedMediaTypes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDefaultCharset(Charset defaultCharset) {
/* 113 */     this.defaultCharset = defaultCharset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Charset getDefaultCharset() {
/* 121 */     return this.defaultCharset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canRead(Class<?> clazz, MediaType mediaType) {
/* 132 */     return (supports(clazz) && canRead(mediaType));
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
/*     */   protected boolean canRead(MediaType mediaType) {
/* 145 */     if (mediaType == null) {
/* 146 */       return true;
/*     */     }
/* 148 */     for (MediaType supportedMediaType : getSupportedMediaTypes()) {
/* 149 */       if (supportedMediaType.includes(mediaType)) {
/* 150 */         return true;
/*     */       }
/*     */     } 
/* 153 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canWrite(Class<?> clazz, MediaType mediaType) {
/* 164 */     return (supports(clazz) && canWrite(mediaType));
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
/*     */   protected boolean canWrite(MediaType mediaType) {
/* 176 */     if (mediaType == null || MediaType.ALL.equals(mediaType)) {
/* 177 */       return true;
/*     */     }
/* 179 */     for (MediaType supportedMediaType : getSupportedMediaTypes()) {
/* 180 */       if (supportedMediaType.isCompatibleWith(mediaType)) {
/* 181 */         return true;
/*     */       }
/*     */     } 
/* 184 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final T read(Class<? extends T> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
/* 195 */     return readInternal(clazz, inputMessage);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void write(final T t, MediaType contentType, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
/* 206 */     final HttpHeaders headers = outputMessage.getHeaders();
/* 207 */     addDefaultHeaders(headers, t, contentType);
/*     */     
/* 209 */     if (outputMessage instanceof StreamingHttpOutputMessage) {
/* 210 */       StreamingHttpOutputMessage streamingOutputMessage = (StreamingHttpOutputMessage)outputMessage;
/* 211 */       streamingOutputMessage.setBody(new StreamingHttpOutputMessage.Body()
/*     */           {
/*     */             public void writeTo(final OutputStream outputStream) throws IOException {
/* 214 */               AbstractHttpMessageConverter.this.writeInternal(t, new HttpOutputMessage()
/*     */                   {
/*     */                     public OutputStream getBody() throws IOException {
/* 217 */                       return outputStream;
/*     */                     }
/*     */                     
/*     */                     public HttpHeaders getHeaders() {
/* 221 */                       return headers;
/*     */                     }
/*     */                   });
/*     */             }
/*     */           });
/*     */     } else {
/*     */       
/* 228 */       writeInternal(t, outputMessage);
/* 229 */       outputMessage.getBody().flush();
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
/*     */   protected void addDefaultHeaders(HttpHeaders headers, T t, MediaType contentType) throws IOException {
/* 241 */     if (headers.getContentType() == null) {
/* 242 */       MediaType contentTypeToUse = contentType;
/* 243 */       if (contentType == null || contentType.isWildcardType() || contentType.isWildcardSubtype()) {
/* 244 */         contentTypeToUse = getDefaultContentType(t);
/*     */       }
/* 246 */       else if (MediaType.APPLICATION_OCTET_STREAM.equals(contentType)) {
/* 247 */         MediaType mediaType = getDefaultContentType(t);
/* 248 */         contentTypeToUse = (mediaType != null) ? mediaType : contentTypeToUse;
/*     */       } 
/* 250 */       if (contentTypeToUse != null) {
/* 251 */         if (contentTypeToUse.getCharset() == null) {
/* 252 */           Charset defaultCharset = getDefaultCharset();
/* 253 */           if (defaultCharset != null) {
/* 254 */             contentTypeToUse = new MediaType(contentTypeToUse, defaultCharset);
/*     */           }
/*     */         } 
/* 257 */         headers.setContentType(contentTypeToUse);
/*     */       } 
/*     */     } 
/* 260 */     if (headers.getContentLength() < 0L && !headers.containsKey("Transfer-Encoding")) {
/* 261 */       Long contentLength = getContentLength(t, headers.getContentType());
/* 262 */       if (contentLength != null) {
/* 263 */         headers.setContentLength(contentLength.longValue());
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
/*     */   protected MediaType getDefaultContentType(T t) throws IOException {
/* 278 */     List<MediaType> mediaTypes = getSupportedMediaTypes();
/* 279 */     return !mediaTypes.isEmpty() ? mediaTypes.get(0) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Long getContentLength(T t, MediaType contentType) throws IOException {
/* 290 */     return null;
/*     */   }
/*     */   
/*     */   protected abstract boolean supports(Class<?> paramClass);
/*     */   
/*     */   protected abstract T readInternal(Class<? extends T> paramClass, HttpInputMessage paramHttpInputMessage) throws IOException, HttpMessageNotReadableException;
/*     */   
/*     */   protected abstract void writeInternal(T paramT, HttpOutputMessage paramHttpOutputMessage) throws IOException, HttpMessageNotWritableException;
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\http\converter\AbstractHttpMessageConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */