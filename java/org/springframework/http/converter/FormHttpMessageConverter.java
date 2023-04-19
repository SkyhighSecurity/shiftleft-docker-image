/*     */ package org.springframework.http.converter;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.URLDecoder;
/*     */ import java.net.URLEncoder;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.mail.internet.MimeUtility;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.http.HttpEntity;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpInputMessage;
/*     */ import org.springframework.http.HttpOutputMessage;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.StreamingHttpOutputMessage;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.LinkedMultiValueMap;
/*     */ import org.springframework.util.MimeTypeUtils;
/*     */ import org.springframework.util.MultiValueMap;
/*     */ import org.springframework.util.StreamUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FormHttpMessageConverter
/*     */   implements HttpMessageConverter<MultiValueMap<String, ?>>
/*     */ {
/*  94 */   public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
/*     */ 
/*     */   
/*  97 */   private List<MediaType> supportedMediaTypes = new ArrayList<MediaType>();
/*     */   
/*  99 */   private List<HttpMessageConverter<?>> partConverters = new ArrayList<HttpMessageConverter<?>>();
/*     */   
/* 101 */   private Charset charset = DEFAULT_CHARSET;
/*     */   
/*     */   private Charset multipartCharset;
/*     */ 
/*     */   
/*     */   public FormHttpMessageConverter() {
/* 107 */     this.supportedMediaTypes.add(MediaType.APPLICATION_FORM_URLENCODED);
/* 108 */     this.supportedMediaTypes.add(MediaType.MULTIPART_FORM_DATA);
/*     */     
/* 110 */     StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter();
/* 111 */     stringHttpMessageConverter.setWriteAcceptCharset(false);
/*     */     
/* 113 */     this.partConverters.add(new ByteArrayHttpMessageConverter());
/* 114 */     this.partConverters.add(stringHttpMessageConverter);
/* 115 */     this.partConverters.add(new ResourceHttpMessageConverter());
/*     */     
/* 117 */     applyDefaultCharset();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSupportedMediaTypes(List<MediaType> supportedMediaTypes) {
/* 125 */     this.supportedMediaTypes = supportedMediaTypes;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<MediaType> getSupportedMediaTypes() {
/* 130 */     return Collections.unmodifiableList(this.supportedMediaTypes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPartConverters(List<HttpMessageConverter<?>> partConverters) {
/* 138 */     Assert.notEmpty(partConverters, "'partConverters' must not be empty");
/* 139 */     this.partConverters = partConverters;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addPartConverter(HttpMessageConverter<?> partConverter) {
/* 147 */     Assert.notNull(partConverter, "'partConverter' must not be null");
/* 148 */     this.partConverters.add(partConverter);
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
/*     */   public void setCharset(Charset charset) {
/* 160 */     if (charset != this.charset) {
/* 161 */       this.charset = (charset != null) ? charset : DEFAULT_CHARSET;
/* 162 */       applyDefaultCharset();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void applyDefaultCharset() {
/* 170 */     for (HttpMessageConverter<?> candidate : this.partConverters) {
/* 171 */       if (candidate instanceof AbstractHttpMessageConverter) {
/* 172 */         AbstractHttpMessageConverter<?> converter = (AbstractHttpMessageConverter)candidate;
/*     */         
/* 174 */         if (converter.getDefaultCharset() != null) {
/* 175 */           converter.setDefaultCharset(this.charset);
/*     */         }
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
/*     */   public void setMultipartCharset(Charset charset) {
/* 190 */     this.multipartCharset = charset;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canRead(Class<?> clazz, MediaType mediaType) {
/* 196 */     if (!MultiValueMap.class.isAssignableFrom(clazz)) {
/* 197 */       return false;
/*     */     }
/* 199 */     if (mediaType == null) {
/* 200 */       return true;
/*     */     }
/* 202 */     for (MediaType supportedMediaType : getSupportedMediaTypes()) {
/*     */       
/* 204 */       if (!supportedMediaType.equals(MediaType.MULTIPART_FORM_DATA) && supportedMediaType.includes(mediaType)) {
/* 205 */         return true;
/*     */       }
/*     */     } 
/* 208 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canWrite(Class<?> clazz, MediaType mediaType) {
/* 213 */     if (!MultiValueMap.class.isAssignableFrom(clazz)) {
/* 214 */       return false;
/*     */     }
/* 216 */     if (mediaType == null || MediaType.ALL.equals(mediaType)) {
/* 217 */       return true;
/*     */     }
/* 219 */     for (MediaType supportedMediaType : getSupportedMediaTypes()) {
/* 220 */       if (supportedMediaType.isCompatibleWith(mediaType)) {
/* 221 */         return true;
/*     */       }
/*     */     } 
/* 224 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MultiValueMap<String, String> read(Class<? extends MultiValueMap<String, ?>> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
/* 231 */     MediaType contentType = inputMessage.getHeaders().getContentType();
/* 232 */     Charset charset = (contentType.getCharset() != null) ? contentType.getCharset() : this.charset;
/* 233 */     String body = StreamUtils.copyToString(inputMessage.getBody(), charset);
/*     */     
/* 235 */     String[] pairs = StringUtils.tokenizeToStringArray(body, "&");
/* 236 */     LinkedMultiValueMap linkedMultiValueMap = new LinkedMultiValueMap(pairs.length);
/* 237 */     for (String pair : pairs) {
/* 238 */       int idx = pair.indexOf('=');
/* 239 */       if (idx == -1) {
/* 240 */         linkedMultiValueMap.add(URLDecoder.decode(pair, charset.name()), null);
/*     */       } else {
/*     */         
/* 243 */         String name = URLDecoder.decode(pair.substring(0, idx), charset.name());
/* 244 */         String value = URLDecoder.decode(pair.substring(idx + 1), charset.name());
/* 245 */         linkedMultiValueMap.add(name, value);
/*     */       } 
/*     */     } 
/* 248 */     return (MultiValueMap<String, String>)linkedMultiValueMap;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(MultiValueMap<String, ?> map, MediaType contentType, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
/* 256 */     if (!isMultipart(map, contentType)) {
/* 257 */       writeForm((MultiValueMap)map, contentType, outputMessage);
/*     */     } else {
/*     */       
/* 260 */       writeMultipart((MultiValueMap)map, outputMessage);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isMultipart(MultiValueMap<String, ?> map, MediaType contentType) {
/* 266 */     if (contentType != null) {
/* 267 */       return MediaType.MULTIPART_FORM_DATA.includes(contentType);
/*     */     }
/* 269 */     for (String name : map.keySet()) {
/* 270 */       for (Object value : map.get(name)) {
/* 271 */         if (value != null && !(value instanceof String)) {
/* 272 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/* 276 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeForm(MultiValueMap<String, String> form, MediaType contentType, HttpOutputMessage outputMessage) throws IOException {
/*     */     Charset charset;
/* 283 */     if (contentType != null) {
/* 284 */       outputMessage.getHeaders().setContentType(contentType);
/* 285 */       charset = (contentType.getCharset() != null) ? contentType.getCharset() : this.charset;
/*     */     } else {
/*     */       
/* 288 */       outputMessage.getHeaders().setContentType(MediaType.APPLICATION_FORM_URLENCODED);
/* 289 */       charset = this.charset;
/*     */     } 
/* 291 */     StringBuilder builder = new StringBuilder();
/* 292 */     for (Iterator<String> nameIterator = form.keySet().iterator(); nameIterator.hasNext(); ) {
/* 293 */       String name = nameIterator.next();
/* 294 */       for (Iterator<String> valueIterator = ((List<String>)form.get(name)).iterator(); valueIterator.hasNext(); ) {
/* 295 */         String value = valueIterator.next();
/* 296 */         builder.append(URLEncoder.encode(name, charset.name()));
/* 297 */         if (value != null) {
/* 298 */           builder.append('=');
/* 299 */           builder.append(URLEncoder.encode(value, charset.name()));
/* 300 */           if (valueIterator.hasNext()) {
/* 301 */             builder.append('&');
/*     */           }
/*     */         } 
/*     */       } 
/* 305 */       if (nameIterator.hasNext()) {
/* 306 */         builder.append('&');
/*     */       }
/*     */     } 
/* 309 */     final byte[] bytes = builder.toString().getBytes(charset.name());
/* 310 */     outputMessage.getHeaders().setContentLength(bytes.length);
/*     */     
/* 312 */     if (outputMessage instanceof StreamingHttpOutputMessage) {
/* 313 */       StreamingHttpOutputMessage streamingOutputMessage = (StreamingHttpOutputMessage)outputMessage;
/* 314 */       streamingOutputMessage.setBody(new StreamingHttpOutputMessage.Body()
/*     */           {
/*     */             public void writeTo(OutputStream outputStream) throws IOException {
/* 317 */               StreamUtils.copy(bytes, outputStream);
/*     */             }
/*     */           });
/*     */     } else {
/*     */       
/* 322 */       StreamUtils.copy(bytes, outputMessage.getBody());
/*     */     } 
/*     */   }
/*     */   
/*     */   private void writeMultipart(final MultiValueMap<String, Object> parts, HttpOutputMessage outputMessage) throws IOException {
/* 327 */     final byte[] boundary = generateMultipartBoundary();
/* 328 */     Map<String, String> parameters = Collections.singletonMap("boundary", new String(boundary, "US-ASCII"));
/*     */     
/* 330 */     MediaType contentType = new MediaType(MediaType.MULTIPART_FORM_DATA, parameters);
/* 331 */     HttpHeaders headers = outputMessage.getHeaders();
/* 332 */     headers.setContentType(contentType);
/*     */     
/* 334 */     if (outputMessage instanceof StreamingHttpOutputMessage) {
/* 335 */       StreamingHttpOutputMessage streamingOutputMessage = (StreamingHttpOutputMessage)outputMessage;
/* 336 */       streamingOutputMessage.setBody(new StreamingHttpOutputMessage.Body()
/*     */           {
/*     */             public void writeTo(OutputStream outputStream) throws IOException {
/* 339 */               FormHttpMessageConverter.this.writeParts(outputStream, parts, boundary);
/* 340 */               FormHttpMessageConverter.writeEnd(outputStream, boundary);
/*     */             }
/*     */           });
/*     */     } else {
/*     */       
/* 345 */       writeParts(outputMessage.getBody(), parts, boundary);
/* 346 */       writeEnd(outputMessage.getBody(), boundary);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void writeParts(OutputStream os, MultiValueMap<String, Object> parts, byte[] boundary) throws IOException {
/* 351 */     for (Map.Entry<String, List<Object>> entry : (Iterable<Map.Entry<String, List<Object>>>)parts.entrySet()) {
/* 352 */       String name = entry.getKey();
/* 353 */       for (Object part : entry.getValue()) {
/* 354 */         if (part != null) {
/* 355 */           writeBoundary(os, boundary);
/* 356 */           writePart(name, getHttpEntity(part), os);
/* 357 */           writeNewLine(os);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void writePart(String name, HttpEntity<?> partEntity, OutputStream os) throws IOException {
/* 365 */     Object partBody = partEntity.getBody();
/* 366 */     Class<?> partType = partBody.getClass();
/* 367 */     HttpHeaders partHeaders = partEntity.getHeaders();
/* 368 */     MediaType partContentType = partHeaders.getContentType();
/* 369 */     for (HttpMessageConverter<?> messageConverter : this.partConverters) {
/* 370 */       if (messageConverter.canWrite(partType, partContentType)) {
/* 371 */         HttpOutputMessage multipartMessage = new MultipartHttpOutputMessage(os);
/* 372 */         multipartMessage.getHeaders().setContentDispositionFormData(name, getFilename(partBody));
/* 373 */         if (!partHeaders.isEmpty()) {
/* 374 */           multipartMessage.getHeaders().putAll((Map)partHeaders);
/*     */         }
/* 376 */         messageConverter.write(partBody, partContentType, multipartMessage);
/*     */         return;
/*     */       } 
/*     */     } 
/* 380 */     throw new HttpMessageNotWritableException("Could not write request: no suitable HttpMessageConverter found for request type [" + partType
/* 381 */         .getName() + "]");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected byte[] generateMultipartBoundary() {
/* 391 */     return MimeTypeUtils.generateMultipartBoundary();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected HttpEntity<?> getHttpEntity(Object part) {
/* 401 */     return (part instanceof HttpEntity) ? (HttpEntity)part : new HttpEntity(part);
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
/*     */   protected String getFilename(Object part) {
/* 413 */     if (part instanceof Resource) {
/* 414 */       Resource resource = (Resource)part;
/* 415 */       String filename = resource.getFilename();
/* 416 */       if (filename != null && this.multipartCharset != null) {
/* 417 */         filename = MimeDelegate.encode(filename, this.multipartCharset.name());
/*     */       }
/* 419 */       return filename;
/*     */     } 
/*     */     
/* 422 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeBoundary(OutputStream os, byte[] boundary) throws IOException {
/* 428 */     os.write(45);
/* 429 */     os.write(45);
/* 430 */     os.write(boundary);
/* 431 */     writeNewLine(os);
/*     */   }
/*     */   
/*     */   private static void writeEnd(OutputStream os, byte[] boundary) throws IOException {
/* 435 */     os.write(45);
/* 436 */     os.write(45);
/* 437 */     os.write(boundary);
/* 438 */     os.write(45);
/* 439 */     os.write(45);
/* 440 */     writeNewLine(os);
/*     */   }
/*     */   
/*     */   private static void writeNewLine(OutputStream os) throws IOException {
/* 444 */     os.write(13);
/* 445 */     os.write(10);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class MultipartHttpOutputMessage
/*     */     implements HttpOutputMessage
/*     */   {
/*     */     private final OutputStream outputStream;
/*     */ 
/*     */     
/* 457 */     private final HttpHeaders headers = new HttpHeaders();
/*     */     
/*     */     private boolean headersWritten = false;
/*     */     
/*     */     public MultipartHttpOutputMessage(OutputStream outputStream) {
/* 462 */       this.outputStream = outputStream;
/*     */     }
/*     */ 
/*     */     
/*     */     public HttpHeaders getHeaders() {
/* 467 */       return this.headersWritten ? HttpHeaders.readOnlyHttpHeaders(this.headers) : this.headers;
/*     */     }
/*     */ 
/*     */     
/*     */     public OutputStream getBody() throws IOException {
/* 472 */       writeHeaders();
/* 473 */       return this.outputStream;
/*     */     }
/*     */     
/*     */     private void writeHeaders() throws IOException {
/* 477 */       if (!this.headersWritten) {
/* 478 */         for (Map.Entry<String, List<String>> entry : (Iterable<Map.Entry<String, List<String>>>)this.headers.entrySet()) {
/* 479 */           byte[] headerName = getAsciiBytes(entry.getKey());
/* 480 */           for (String headerValueString : entry.getValue()) {
/* 481 */             byte[] headerValue = getAsciiBytes(headerValueString);
/* 482 */             this.outputStream.write(headerName);
/* 483 */             this.outputStream.write(58);
/* 484 */             this.outputStream.write(32);
/* 485 */             this.outputStream.write(headerValue);
/* 486 */             FormHttpMessageConverter.writeNewLine(this.outputStream);
/*     */           } 
/*     */         } 
/* 489 */         FormHttpMessageConverter.writeNewLine(this.outputStream);
/* 490 */         this.headersWritten = true;
/*     */       } 
/*     */     }
/*     */     
/*     */     private byte[] getAsciiBytes(String name) {
/*     */       try {
/* 496 */         return name.getBytes("US-ASCII");
/*     */       }
/* 498 */       catch (UnsupportedEncodingException ex) {
/*     */         
/* 500 */         throw new IllegalStateException(ex);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class MimeDelegate
/*     */   {
/*     */     public static String encode(String value, String charset) {
/*     */       try {
/* 513 */         return MimeUtility.encodeText(value, charset, null);
/*     */       }
/* 515 */       catch (UnsupportedEncodingException ex) {
/* 516 */         throw new IllegalStateException(ex);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\http\converter\FormHttpMessageConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */