/*     */ package org.springframework.http.converter;
/*     */ 
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import org.springframework.core.io.ByteArrayResource;
/*     */ import org.springframework.core.io.InputStreamResource;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.http.HttpInputMessage;
/*     */ import org.springframework.http.HttpOutputMessage;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.StreamUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ResourceHttpMessageConverter
/*     */   extends AbstractHttpMessageConverter<Resource>
/*     */ {
/*  47 */   private static final boolean jafPresent = ClassUtils.isPresent("javax.activation.FileTypeMap", ResourceHttpMessageConverter.class
/*  48 */       .getClassLoader());
/*     */ 
/*     */   
/*     */   public ResourceHttpMessageConverter() {
/*  52 */     super(MediaType.ALL);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean supports(Class<?> clazz) {
/*  58 */     return Resource.class.isAssignableFrom(clazz);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Resource readInternal(Class<? extends Resource> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
/*  65 */     if (InputStreamResource.class == clazz) {
/*  66 */       return (Resource)new InputStreamResource(inputMessage.getBody());
/*     */     }
/*  68 */     if (clazz.isAssignableFrom(ByteArrayResource.class)) {
/*  69 */       byte[] body = StreamUtils.copyToByteArray(inputMessage.getBody());
/*  70 */       return (Resource)new ByteArrayResource(body);
/*     */     } 
/*     */     
/*  73 */     throw new IllegalStateException("Unsupported resource class: " + clazz);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected MediaType getDefaultContentType(Resource resource) {
/*  79 */     if (jafPresent) {
/*  80 */       return ActivationMediaTypeFactory.getMediaType(resource);
/*     */     }
/*     */     
/*  83 */     return MediaType.APPLICATION_OCTET_STREAM;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Long getContentLength(Resource resource, MediaType contentType) throws IOException {
/*  91 */     if (InputStreamResource.class == resource.getClass()) {
/*  92 */       return null;
/*     */     }
/*  94 */     long contentLength = resource.contentLength();
/*  95 */     return (contentLength < 0L) ? null : Long.valueOf(contentLength);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeInternal(Resource resource, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
/* 102 */     writeContent(resource, outputMessage);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void writeContent(Resource resource, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
/*     */     try {
/* 108 */       InputStream in = resource.getInputStream();
/*     */       
/* 110 */       try { StreamUtils.copy(in, outputMessage.getBody()); }
/*     */       
/* 112 */       catch (NullPointerException nullPointerException)
/*     */       
/*     */       { 
/*     */         try {
/*     */           
/* 117 */           in.close();
/*     */         }
/* 119 */         catch (Throwable throwable) {} } finally { try { in.close(); } catch (Throwable throwable) {}
/*     */          }
/*     */ 
/*     */     
/*     */     }
/* 124 */     catch (FileNotFoundException fileNotFoundException) {}
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\http\converter\ResourceHttpMessageConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */