/*     */ package org.springframework.web.accept;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import javax.activation.FileTypeMap;
/*     */ import javax.activation.MimetypesFileTypeMap;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.core.io.ClassPathResource;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.HttpMediaTypeNotAcceptableException;
/*     */ import org.springframework.web.context.request.NativeWebRequest;
/*     */ import org.springframework.web.util.UriUtils;
/*     */ import org.springframework.web.util.UrlPathHelper;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PathExtensionContentNegotiationStrategy
/*     */   extends AbstractMappingContentNegotiationStrategy
/*     */ {
/*  57 */   private static final boolean JAF_PRESENT = ClassUtils.isPresent("javax.activation.FileTypeMap", PathExtensionContentNegotiationStrategy.class
/*  58 */       .getClassLoader());
/*     */   
/*  60 */   private static final Log logger = LogFactory.getLog(PathExtensionContentNegotiationStrategy.class);
/*     */   
/*  62 */   private UrlPathHelper urlPathHelper = new UrlPathHelper();
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean useJaf = true;
/*     */ 
/*     */   
/*     */   private boolean ignoreUnknownExtensions = true;
/*     */ 
/*     */ 
/*     */   
/*     */   public PathExtensionContentNegotiationStrategy() {
/*  74 */     this((Map<String, MediaType>)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PathExtensionContentNegotiationStrategy(Map<String, MediaType> mediaTypes) {
/*  81 */     super(mediaTypes);
/*  82 */     this.urlPathHelper.setUrlDecode(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUrlPathHelper(UrlPathHelper urlPathHelper) {
/*  92 */     this.urlPathHelper = urlPathHelper;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUseJaf(boolean useJaf) {
/* 100 */     this.useJaf = useJaf;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIgnoreUnknownExtensions(boolean ignoreUnknownExtensions) {
/* 109 */     this.ignoreUnknownExtensions = ignoreUnknownExtensions;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getMediaTypeKey(NativeWebRequest webRequest) {
/* 115 */     HttpServletRequest request = (HttpServletRequest)webRequest.getNativeRequest(HttpServletRequest.class);
/* 116 */     if (request == null) {
/* 117 */       logger.warn("An HttpServletRequest is required to determine the media type key");
/* 118 */       return null;
/*     */     } 
/* 120 */     String path = this.urlPathHelper.getLookupPathForRequest(request);
/* 121 */     String extension = UriUtils.extractFileExtension(path);
/* 122 */     return StringUtils.hasText(extension) ? extension.toLowerCase(Locale.ENGLISH) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected MediaType handleNoMatch(NativeWebRequest webRequest, String extension) throws HttpMediaTypeNotAcceptableException {
/* 129 */     if (this.useJaf && JAF_PRESENT) {
/* 130 */       MediaType mediaType = ActivationMediaTypeFactory.getMediaType("file." + extension);
/* 131 */       if (mediaType != null && !MediaType.APPLICATION_OCTET_STREAM.equals(mediaType)) {
/* 132 */         return mediaType;
/*     */       }
/*     */     } 
/* 135 */     if (this.ignoreUnknownExtensions) {
/* 136 */       return null;
/*     */     }
/* 138 */     throw new HttpMediaTypeNotAcceptableException(getAllMediaTypes());
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
/*     */   public MediaType getMediaTypeForResource(Resource resource) {
/* 151 */     Assert.notNull(resource, "Resource must not be null");
/* 152 */     MediaType mediaType = null;
/* 153 */     String filename = resource.getFilename();
/* 154 */     String extension = StringUtils.getFilenameExtension(filename);
/* 155 */     if (extension != null) {
/* 156 */       mediaType = lookupMediaType(extension);
/*     */     }
/* 158 */     if (mediaType == null && JAF_PRESENT) {
/* 159 */       mediaType = ActivationMediaTypeFactory.getMediaType(filename);
/*     */     }
/* 161 */     if (MediaType.APPLICATION_OCTET_STREAM.equals(mediaType)) {
/* 162 */       mediaType = null;
/*     */     }
/* 164 */     return mediaType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class ActivationMediaTypeFactory
/*     */   {
/* 176 */     private static final FileTypeMap fileTypeMap = initFileTypeMap();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private static FileTypeMap initFileTypeMap() {
/* 183 */       ClassPathResource classPathResource = new ClassPathResource("org/springframework/mail/javamail/mime.types");
/* 184 */       if (classPathResource.exists()) {
/* 185 */         if (PathExtensionContentNegotiationStrategy.logger.isTraceEnabled()) {
/* 186 */           PathExtensionContentNegotiationStrategy.logger.trace("Loading JAF FileTypeMap from " + classPathResource);
/*     */         }
/* 188 */         InputStream inputStream = null;
/*     */         try {
/* 190 */           inputStream = classPathResource.getInputStream();
/* 191 */           return new MimetypesFileTypeMap(inputStream);
/*     */         }
/* 193 */         catch (IOException iOException) {
/*     */ 
/*     */         
/*     */         } finally {
/* 197 */           if (inputStream != null) {
/*     */             try {
/* 199 */               inputStream.close();
/*     */             }
/* 201 */             catch (IOException iOException) {}
/*     */           }
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 207 */       if (PathExtensionContentNegotiationStrategy.logger.isTraceEnabled()) {
/* 208 */         PathExtensionContentNegotiationStrategy.logger.trace("Loading default Java Activation Framework FileTypeMap");
/*     */       }
/* 210 */       return FileTypeMap.getDefaultFileTypeMap();
/*     */     }
/*     */     
/*     */     public static MediaType getMediaType(String filename) {
/* 214 */       String mediaType = fileTypeMap.getContentType(filename);
/* 215 */       return StringUtils.hasText(mediaType) ? MediaType.parseMediaType(mediaType) : null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\accept\PathExtensionContentNegotiationStrategy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */