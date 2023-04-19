/*     */ package org.springframework.web.multipart.support;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.Serializable;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.Part;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.util.FileCopyUtils;
/*     */ import org.springframework.util.LinkedMultiValueMap;
/*     */ import org.springframework.util.MultiValueMap;
/*     */ import org.springframework.web.multipart.MultipartException;
/*     */ import org.springframework.web.multipart.MultipartFile;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StandardMultipartHttpServletRequest
/*     */   extends AbstractMultipartHttpServletRequest
/*     */ {
/*     */   private static final String CONTENT_DISPOSITION = "content-disposition";
/*     */   private static final String FILENAME_KEY = "filename=";
/*     */   private static final String FILENAME_WITH_CHARSET_KEY = "filename*=";
/*  61 */   private static final Charset US_ASCII = Charset.forName("us-ascii");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Set<String> multipartParameterNames;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StandardMultipartHttpServletRequest(HttpServletRequest request) throws MultipartException {
/*  74 */     this(request, false);
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
/*     */   public StandardMultipartHttpServletRequest(HttpServletRequest request, boolean lazyParsing) throws MultipartException {
/*  88 */     super(request);
/*  89 */     if (!lazyParsing) {
/*  90 */       parseRequest(request);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void parseRequest(HttpServletRequest request) {
/*     */     try {
/*  97 */       Collection<Part> parts = request.getParts();
/*  98 */       this.multipartParameterNames = new LinkedHashSet<String>(parts.size());
/*  99 */       LinkedMultiValueMap linkedMultiValueMap = new LinkedMultiValueMap(parts.size());
/* 100 */       for (Part part : parts) {
/* 101 */         String disposition = part.getHeader("content-disposition");
/* 102 */         String filename = extractFilename(disposition);
/* 103 */         if (filename == null) {
/* 104 */           filename = extractFilenameWithCharset(disposition);
/*     */         }
/* 106 */         if (filename != null) {
/* 107 */           linkedMultiValueMap.add(part.getName(), new StandardMultipartFile(part, filename));
/*     */           continue;
/*     */         } 
/* 110 */         this.multipartParameterNames.add(part.getName());
/*     */       } 
/*     */       
/* 113 */       setMultipartFiles((MultiValueMap<String, MultipartFile>)linkedMultiValueMap);
/*     */     }
/* 115 */     catch (Throwable ex) {
/* 116 */       throw new MultipartException("Could not parse multipart servlet request", ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   private String extractFilename(String contentDisposition, String key) {
/* 121 */     if (contentDisposition == null) {
/* 122 */       return null;
/*     */     }
/* 124 */     int startIndex = contentDisposition.indexOf(key);
/* 125 */     if (startIndex == -1) {
/* 126 */       return null;
/*     */     }
/* 128 */     String filename = contentDisposition.substring(startIndex + key.length());
/* 129 */     if (filename.startsWith("\"")) {
/* 130 */       int endIndex = filename.indexOf("\"", 1);
/* 131 */       if (endIndex != -1) {
/* 132 */         return filename.substring(1, endIndex);
/*     */       }
/*     */     } else {
/*     */       
/* 136 */       int endIndex = filename.indexOf(";");
/* 137 */       if (endIndex != -1) {
/* 138 */         return filename.substring(0, endIndex);
/*     */       }
/*     */     } 
/* 141 */     return filename;
/*     */   }
/*     */   
/*     */   private String extractFilename(String contentDisposition) {
/* 145 */     return extractFilename(contentDisposition, "filename=");
/*     */   }
/*     */   
/*     */   private String extractFilenameWithCharset(String contentDisposition) {
/* 149 */     String filename = extractFilename(contentDisposition, "filename*=");
/* 150 */     if (filename == null) {
/* 151 */       return null;
/*     */     }
/* 153 */     int index = filename.indexOf("'");
/* 154 */     if (index != -1) {
/* 155 */       Charset charset = null;
/*     */       try {
/* 157 */         charset = Charset.forName(filename.substring(0, index));
/*     */       }
/* 159 */       catch (IllegalArgumentException illegalArgumentException) {}
/*     */ 
/*     */       
/* 162 */       filename = filename.substring(index + 1);
/*     */       
/* 164 */       index = filename.indexOf("'");
/* 165 */       if (index != -1) {
/* 166 */         filename = filename.substring(index + 1);
/*     */       }
/* 168 */       if (charset != null) {
/* 169 */         filename = new String(filename.getBytes(US_ASCII), charset);
/*     */       }
/*     */     } 
/* 172 */     return filename;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void initializeMultipart() {
/* 178 */     parseRequest(getRequest());
/*     */   }
/*     */ 
/*     */   
/*     */   public Enumeration<String> getParameterNames() {
/* 183 */     if (this.multipartParameterNames == null) {
/* 184 */       initializeMultipart();
/*     */     }
/* 186 */     if (this.multipartParameterNames.isEmpty()) {
/* 187 */       return super.getParameterNames();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 192 */     Set<String> paramNames = new LinkedHashSet<String>();
/* 193 */     Enumeration<String> paramEnum = super.getParameterNames();
/* 194 */     while (paramEnum.hasMoreElements()) {
/* 195 */       paramNames.add(paramEnum.nextElement());
/*     */     }
/* 197 */     paramNames.addAll(this.multipartParameterNames);
/* 198 */     return Collections.enumeration(paramNames);
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, String[]> getParameterMap() {
/* 203 */     if (this.multipartParameterNames == null) {
/* 204 */       initializeMultipart();
/*     */     }
/* 206 */     if (this.multipartParameterNames.isEmpty()) {
/* 207 */       return super.getParameterMap();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 212 */     Map<String, String[]> paramMap = (Map)new LinkedHashMap<String, String>();
/* 213 */     paramMap.putAll(super.getParameterMap());
/* 214 */     for (String paramName : this.multipartParameterNames) {
/* 215 */       if (!paramMap.containsKey(paramName)) {
/* 216 */         paramMap.put(paramName, getParameterValues(paramName));
/*     */       }
/*     */     } 
/* 219 */     return paramMap;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getMultipartContentType(String paramOrFileName) {
/*     */     try {
/* 225 */       Part part = getPart(paramOrFileName);
/* 226 */       return (part != null) ? part.getContentType() : null;
/*     */     }
/* 228 */     catch (Throwable ex) {
/* 229 */       throw new MultipartException("Could not access multipart servlet request", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpHeaders getMultipartHeaders(String paramOrFileName) {
/*     */     try {
/* 236 */       Part part = getPart(paramOrFileName);
/* 237 */       if (part != null) {
/* 238 */         HttpHeaders headers = new HttpHeaders();
/* 239 */         for (String headerName : part.getHeaderNames()) {
/* 240 */           headers.put(headerName, new ArrayList(part.getHeaders(headerName)));
/*     */         }
/* 242 */         return headers;
/*     */       } 
/*     */       
/* 245 */       return null;
/*     */     
/*     */     }
/* 248 */     catch (Throwable ex) {
/* 249 */       throw new MultipartException("Could not access multipart servlet request", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class StandardMultipartFile
/*     */     implements MultipartFile, Serializable
/*     */   {
/*     */     private final Part part;
/*     */ 
/*     */     
/*     */     private final String filename;
/*     */ 
/*     */     
/*     */     public StandardMultipartFile(Part part, String filename) {
/* 265 */       this.part = part;
/* 266 */       this.filename = filename;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getName() {
/* 271 */       return this.part.getName();
/*     */     }
/*     */ 
/*     */     
/*     */     public String getOriginalFilename() {
/* 276 */       return this.filename;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getContentType() {
/* 281 */       return this.part.getContentType();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 286 */       return (this.part.getSize() == 0L);
/*     */     }
/*     */ 
/*     */     
/*     */     public long getSize() {
/* 291 */       return this.part.getSize();
/*     */     }
/*     */ 
/*     */     
/*     */     public byte[] getBytes() throws IOException {
/* 296 */       return FileCopyUtils.copyToByteArray(this.part.getInputStream());
/*     */     }
/*     */ 
/*     */     
/*     */     public InputStream getInputStream() throws IOException {
/* 301 */       return this.part.getInputStream();
/*     */     }
/*     */ 
/*     */     
/*     */     public void transferTo(File dest) throws IOException, IllegalStateException {
/* 306 */       this.part.write(dest.getPath());
/* 307 */       if (dest.isAbsolute() && !dest.exists())
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 314 */         FileCopyUtils.copy(this.part.getInputStream(), new FileOutputStream(dest));
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\multipart\support\StandardMultipartHttpServletRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */