/*     */ package org.springframework.web.multipart.support;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.servlet.ServletRequest;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletRequestWrapper;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpMethod;
/*     */ import org.springframework.util.LinkedMultiValueMap;
/*     */ import org.springframework.util.MultiValueMap;
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
/*     */ public abstract class AbstractMultipartHttpServletRequest
/*     */   extends HttpServletRequestWrapper
/*     */   implements MultipartHttpServletRequest
/*     */ {
/*     */   private MultiValueMap<String, MultipartFile> multipartFiles;
/*     */   
/*     */   protected AbstractMultipartHttpServletRequest(HttpServletRequest request) {
/*  53 */     super(request);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpServletRequest getRequest() {
/*  59 */     return (HttpServletRequest)super.getRequest();
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpMethod getRequestMethod() {
/*  64 */     return HttpMethod.resolve(getRequest().getMethod());
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpHeaders getRequestHeaders() {
/*  69 */     HttpHeaders headers = new HttpHeaders();
/*  70 */     Enumeration<String> headerNames = getHeaderNames();
/*  71 */     while (headerNames.hasMoreElements()) {
/*  72 */       String headerName = headerNames.nextElement();
/*  73 */       headers.put(headerName, Collections.list(getHeaders(headerName)));
/*     */     } 
/*  75 */     return headers;
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<String> getFileNames() {
/*  80 */     return getMultipartFiles().keySet().iterator();
/*     */   }
/*     */ 
/*     */   
/*     */   public MultipartFile getFile(String name) {
/*  85 */     return (MultipartFile)getMultipartFiles().getFirst(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<MultipartFile> getFiles(String name) {
/*  90 */     List<MultipartFile> multipartFiles = (List<MultipartFile>)getMultipartFiles().get(name);
/*  91 */     if (multipartFiles != null) {
/*  92 */       return multipartFiles;
/*     */     }
/*     */     
/*  95 */     return Collections.emptyList();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, MultipartFile> getFileMap() {
/* 101 */     return getMultipartFiles().toSingleValueMap();
/*     */   }
/*     */ 
/*     */   
/*     */   public MultiValueMap<String, MultipartFile> getMultiFileMap() {
/* 106 */     return getMultipartFiles();
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
/*     */   public boolean isResolved() {
/* 118 */     return (this.multipartFiles != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void setMultipartFiles(MultiValueMap<String, MultipartFile> multipartFiles) {
/* 127 */     this
/* 128 */       .multipartFiles = (MultiValueMap<String, MultipartFile>)new LinkedMultiValueMap(Collections.unmodifiableMap((Map<? extends String, ? extends MultipartFile>)multipartFiles));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected MultiValueMap<String, MultipartFile> getMultipartFiles() {
/* 137 */     if (this.multipartFiles == null) {
/* 138 */       initializeMultipart();
/*     */     }
/* 140 */     return this.multipartFiles;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void initializeMultipart() {
/* 148 */     throw new IllegalStateException("Multipart request not initialized");
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\multipart\support\AbstractMultipartHttpServletRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */