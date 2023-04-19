/*     */ package org.springframework.web.accept;
/*     */ 
/*     */ import java.util.Map;
/*     */ import javax.servlet.ServletContext;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.HttpMediaTypeNotAcceptableException;
/*     */ import org.springframework.web.context.request.NativeWebRequest;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ServletPathExtensionContentNegotiationStrategy
/*     */   extends PathExtensionContentNegotiationStrategy
/*     */ {
/*     */   private final ServletContext servletContext;
/*     */   
/*     */   public ServletPathExtensionContentNegotiationStrategy(ServletContext context) {
/*  47 */     this(context, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ServletPathExtensionContentNegotiationStrategy(ServletContext servletContext, Map<String, MediaType> mediaTypes) {
/*  56 */     super(mediaTypes);
/*  57 */     Assert.notNull(servletContext, "ServletContext is required");
/*  58 */     this.servletContext = servletContext;
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
/*     */   protected MediaType handleNoMatch(NativeWebRequest webRequest, String extension) throws HttpMediaTypeNotAcceptableException {
/*  70 */     MediaType mediaType = null;
/*  71 */     if (this.servletContext != null) {
/*  72 */       String mimeType = this.servletContext.getMimeType("file." + extension);
/*  73 */       if (StringUtils.hasText(mimeType)) {
/*  74 */         mediaType = MediaType.parseMediaType(mimeType);
/*     */       }
/*     */     } 
/*  77 */     if (mediaType == null || MediaType.APPLICATION_OCTET_STREAM.equals(mediaType)) {
/*  78 */       MediaType superMediaType = super.handleNoMatch(webRequest, extension);
/*  79 */       if (superMediaType != null) {
/*  80 */         mediaType = superMediaType;
/*     */       }
/*     */     } 
/*  83 */     return mediaType;
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
/*     */   public MediaType getMediaTypeForResource(Resource resource) {
/*  95 */     MediaType mediaType = null;
/*  96 */     if (this.servletContext != null) {
/*  97 */       String mimeType = this.servletContext.getMimeType(resource.getFilename());
/*  98 */       if (StringUtils.hasText(mimeType)) {
/*  99 */         mediaType = MediaType.parseMediaType(mimeType);
/*     */       }
/*     */     } 
/* 102 */     if (mediaType == null || MediaType.APPLICATION_OCTET_STREAM.equals(mediaType)) {
/* 103 */       MediaType superMediaType = super.getMediaTypeForResource(resource);
/* 104 */       if (superMediaType != null) {
/* 105 */         mediaType = superMediaType;
/*     */       }
/*     */     } 
/* 108 */     return mediaType;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\accept\ServletPathExtensionContentNegotiationStrategy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */