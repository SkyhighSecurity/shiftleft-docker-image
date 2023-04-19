/*     */ package org.springframework.web.multipart.support;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.util.MultiValueMap;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultMultipartHttpServletRequest
/*     */   extends AbstractMultipartHttpServletRequest
/*     */ {
/*     */   private static final String CONTENT_TYPE = "Content-Type";
/*     */   private Map<String, String[]> multipartParameters;
/*     */   private Map<String, String> multipartParameterContentTypes;
/*     */   
/*     */   public DefaultMultipartHttpServletRequest(HttpServletRequest request, MultiValueMap<String, MultipartFile> mpFiles, Map<String, String[]> mpParams, Map<String, String> mpParamContentTypes) {
/*  63 */     super(request);
/*  64 */     setMultipartFiles(mpFiles);
/*  65 */     setMultipartParameters(mpParams);
/*  66 */     setMultipartParameterContentTypes(mpParamContentTypes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultMultipartHttpServletRequest(HttpServletRequest request) {
/*  74 */     super(request);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getParameter(String name) {
/*  80 */     String[] values = getMultipartParameters().get(name);
/*  81 */     if (values != null) {
/*  82 */       return (values.length > 0) ? values[0] : null;
/*     */     }
/*  84 */     return super.getParameter(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] getParameterValues(String name) {
/*  89 */     String[] values = getMultipartParameters().get(name);
/*  90 */     if (values != null) {
/*  91 */       return values;
/*     */     }
/*  93 */     return super.getParameterValues(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public Enumeration<String> getParameterNames() {
/*  98 */     Map<String, String[]> multipartParameters = getMultipartParameters();
/*  99 */     if (multipartParameters.isEmpty()) {
/* 100 */       return super.getParameterNames();
/*     */     }
/*     */     
/* 103 */     Set<String> paramNames = new LinkedHashSet<String>();
/* 104 */     Enumeration<String> paramEnum = super.getParameterNames();
/* 105 */     while (paramEnum.hasMoreElements()) {
/* 106 */       paramNames.add(paramEnum.nextElement());
/*     */     }
/* 108 */     paramNames.addAll(multipartParameters.keySet());
/* 109 */     return Collections.enumeration(paramNames);
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, String[]> getParameterMap() {
/* 114 */     Map<String, String[]> multipartParameters = getMultipartParameters();
/* 115 */     if (multipartParameters.isEmpty()) {
/* 116 */       return super.getParameterMap();
/*     */     }
/*     */     
/* 119 */     Map<String, String[]> paramMap = (Map)new LinkedHashMap<String, String>();
/* 120 */     paramMap.putAll(super.getParameterMap());
/* 121 */     paramMap.putAll((Map)multipartParameters);
/* 122 */     return paramMap;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getMultipartContentType(String paramOrFileName) {
/* 127 */     MultipartFile file = getFile(paramOrFileName);
/* 128 */     if (file != null) {
/* 129 */       return file.getContentType();
/*     */     }
/*     */     
/* 132 */     return getMultipartParameterContentTypes().get(paramOrFileName);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpHeaders getMultipartHeaders(String paramOrFileName) {
/* 138 */     String contentType = getMultipartContentType(paramOrFileName);
/* 139 */     if (contentType != null) {
/* 140 */       HttpHeaders headers = new HttpHeaders();
/* 141 */       headers.add("Content-Type", contentType);
/* 142 */       return headers;
/*     */     } 
/*     */     
/* 145 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void setMultipartParameters(Map<String, String[]> multipartParameters) {
/* 155 */     this.multipartParameters = multipartParameters;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Map<String, String[]> getMultipartParameters() {
/* 164 */     if (this.multipartParameters == null) {
/* 165 */       initializeMultipart();
/*     */     }
/* 167 */     return this.multipartParameters;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void setMultipartParameterContentTypes(Map<String, String> multipartParameterContentTypes) {
/* 175 */     this.multipartParameterContentTypes = multipartParameterContentTypes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Map<String, String> getMultipartParameterContentTypes() {
/* 184 */     if (this.multipartParameterContentTypes == null) {
/* 185 */       initializeMultipart();
/*     */     }
/* 187 */     return this.multipartParameterContentTypes;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\multipart\support\DefaultMultipartHttpServletRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */