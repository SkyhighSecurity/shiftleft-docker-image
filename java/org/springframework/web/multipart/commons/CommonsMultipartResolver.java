/*     */ package org.springframework.web.multipart.commons;
/*     */ 
/*     */ import java.util.List;
/*     */ import javax.servlet.ServletContext;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.apache.commons.fileupload.FileItem;
/*     */ import org.apache.commons.fileupload.FileItemFactory;
/*     */ import org.apache.commons.fileupload.FileUpload;
/*     */ import org.apache.commons.fileupload.FileUploadBase;
/*     */ import org.apache.commons.fileupload.FileUploadException;
/*     */ import org.apache.commons.fileupload.servlet.ServletFileUpload;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.web.context.ServletContextAware;
/*     */ import org.springframework.web.multipart.MaxUploadSizeExceededException;
/*     */ import org.springframework.web.multipart.MultipartException;
/*     */ import org.springframework.web.multipart.MultipartHttpServletRequest;
/*     */ import org.springframework.web.multipart.MultipartResolver;
/*     */ import org.springframework.web.multipart.support.AbstractMultipartHttpServletRequest;
/*     */ import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;
/*     */ import org.springframework.web.util.WebUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CommonsMultipartResolver
/*     */   extends CommonsFileUploadSupport
/*     */   implements MultipartResolver, ServletContextAware
/*     */ {
/*     */   private boolean resolveLazily = false;
/*     */   
/*     */   public CommonsMultipartResolver() {}
/*     */   
/*     */   public CommonsMultipartResolver(ServletContext servletContext) {
/*  87 */     this();
/*  88 */     setServletContext(servletContext);
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
/*     */   public void setResolveLazily(boolean resolveLazily) {
/* 101 */     this.resolveLazily = resolveLazily;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected FileUpload newFileUpload(FileItemFactory fileItemFactory) {
/* 112 */     return (FileUpload)new ServletFileUpload(fileItemFactory);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setServletContext(ServletContext servletContext) {
/* 117 */     if (!isUploadTempDirSpecified()) {
/* 118 */       getFileItemFactory().setRepository(WebUtils.getTempDir(servletContext));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isMultipart(HttpServletRequest request) {
/* 125 */     return (request != null && ServletFileUpload.isMultipartContent(request));
/*     */   }
/*     */ 
/*     */   
/*     */   public MultipartHttpServletRequest resolveMultipart(final HttpServletRequest request) throws MultipartException {
/* 130 */     Assert.notNull(request, "Request must not be null");
/* 131 */     if (this.resolveLazily) {
/* 132 */       return (MultipartHttpServletRequest)new DefaultMultipartHttpServletRequest(request)
/*     */         {
/*     */           protected void initializeMultipart() {
/* 135 */             CommonsFileUploadSupport.MultipartParsingResult parsingResult = CommonsMultipartResolver.this.parseRequest(request);
/* 136 */             setMultipartFiles(parsingResult.getMultipartFiles());
/* 137 */             setMultipartParameters(parsingResult.getMultipartParameters());
/* 138 */             setMultipartParameterContentTypes(parsingResult.getMultipartParameterContentTypes());
/*     */           }
/*     */         };
/*     */     }
/*     */     
/* 143 */     CommonsFileUploadSupport.MultipartParsingResult parsingResult = parseRequest(request);
/* 144 */     return (MultipartHttpServletRequest)new DefaultMultipartHttpServletRequest(request, parsingResult.getMultipartFiles(), parsingResult
/* 145 */         .getMultipartParameters(), parsingResult.getMultipartParameterContentTypes());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected CommonsFileUploadSupport.MultipartParsingResult parseRequest(HttpServletRequest request) throws MultipartException {
/* 156 */     String encoding = determineEncoding(request);
/* 157 */     FileUpload fileUpload = prepareFileUpload(encoding);
/*     */     try {
/* 159 */       List<FileItem> fileItems = ((ServletFileUpload)fileUpload).parseRequest(request);
/* 160 */       return parseFileItems(fileItems, encoding);
/*     */     }
/* 162 */     catch (org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException ex) {
/* 163 */       throw new MaxUploadSizeExceededException(fileUpload.getSizeMax(), ex);
/*     */     }
/* 165 */     catch (org.apache.commons.fileupload.FileUploadBase.FileSizeLimitExceededException ex) {
/* 166 */       throw new MaxUploadSizeExceededException(fileUpload.getFileSizeMax(), ex);
/*     */     }
/* 168 */     catch (FileUploadException ex) {
/* 169 */       throw new MultipartException("Failed to parse multipart servlet request", ex);
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
/*     */   protected String determineEncoding(HttpServletRequest request) {
/* 184 */     String encoding = request.getCharacterEncoding();
/* 185 */     if (encoding == null) {
/* 186 */       encoding = getDefaultEncoding();
/*     */     }
/* 188 */     return encoding;
/*     */   }
/*     */ 
/*     */   
/*     */   public void cleanupMultipart(MultipartHttpServletRequest request) {
/* 193 */     if (!(request instanceof AbstractMultipartHttpServletRequest) || ((AbstractMultipartHttpServletRequest)request)
/* 194 */       .isResolved())
/*     */       try {
/* 196 */         cleanupFileItems(request.getMultiFileMap());
/*     */       }
/* 198 */       catch (Throwable ex) {
/* 199 */         this.logger.warn("Failed to perform multipart cleanup for servlet request", ex);
/*     */       }  
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\multipart\commons\CommonsMultipartResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */