/*     */ package org.springframework.web.multipart.support;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.nio.charset.Charset;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.server.ServletServerHttpRequest;
/*     */ import org.springframework.web.multipart.MultipartException;
/*     */ import org.springframework.web.multipart.MultipartFile;
/*     */ import org.springframework.web.multipart.MultipartHttpServletRequest;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RequestPartServletServerHttpRequest
/*     */   extends ServletServerHttpRequest
/*     */ {
/*     */   private final MultipartHttpServletRequest multipartRequest;
/*     */   private final String partName;
/*     */   private final HttpHeaders headers;
/*     */   
/*     */   public RequestPartServletServerHttpRequest(HttpServletRequest request, String partName) throws MissingServletRequestPartException {
/*  63 */     super(request);
/*     */     
/*  65 */     this.multipartRequest = MultipartResolutionDelegate.asMultipartHttpServletRequest(request);
/*  66 */     this.partName = partName;
/*     */     
/*  68 */     this.headers = this.multipartRequest.getMultipartHeaders(this.partName);
/*  69 */     if (this.headers == null) {
/*  70 */       throw new MissingServletRequestPartException(partName);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpHeaders getHeaders() {
/*  77 */     return this.headers;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public InputStream getBody() throws IOException {
/*  83 */     if (this.multipartRequest instanceof StandardMultipartHttpServletRequest) {
/*     */       try {
/*  85 */         return this.multipartRequest.getPart(this.partName).getInputStream();
/*     */       }
/*  87 */       catch (Exception ex) {
/*  88 */         throw new MultipartException("Could not parse multipart servlet request", ex);
/*     */       } 
/*     */     }
/*     */     
/*  92 */     MultipartFile file = this.multipartRequest.getFile(this.partName);
/*  93 */     if (file != null) {
/*  94 */       return file.getInputStream();
/*     */     }
/*     */     
/*  97 */     String paramValue = this.multipartRequest.getParameter(this.partName);
/*  98 */     return new ByteArrayInputStream(paramValue.getBytes(determineEncoding()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private String determineEncoding() {
/* 104 */     MediaType contentType = getHeaders().getContentType();
/* 105 */     if (contentType != null) {
/* 106 */       Charset charset = contentType.getCharset();
/* 107 */       if (charset != null) {
/* 108 */         return charset.name();
/*     */       }
/*     */     } 
/* 111 */     String encoding = this.multipartRequest.getCharacterEncoding();
/* 112 */     return (encoding != null) ? encoding : "UTF-8";
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\multipart\support\RequestPartServletServerHttpRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */