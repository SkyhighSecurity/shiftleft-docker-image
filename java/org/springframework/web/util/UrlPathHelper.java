/*     */ package org.springframework.web.util;
/*     */ 
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.URLDecoder;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.util.LinkedMultiValueMap;
/*     */ import org.springframework.util.MultiValueMap;
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
/*     */ public class UrlPathHelper
/*     */ {
/*     */   private static final String WEBSPHERE_URI_ATTRIBUTE = "com.ibm.websphere.servlet.uri_non_decoded";
/*  56 */   private static final Log logger = LogFactory.getLog(UrlPathHelper.class);
/*     */ 
/*     */   
/*     */   static volatile Boolean websphereComplianceFlag;
/*     */   
/*     */   private boolean alwaysUseFullPath = false;
/*     */   
/*     */   private boolean urlDecode = true;
/*     */   
/*     */   private boolean removeSemicolonContent = true;
/*     */   
/*  67 */   private String defaultEncoding = "ISO-8859-1";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAlwaysUseFullPath(boolean alwaysUseFullPath) {
/*  77 */     this.alwaysUseFullPath = alwaysUseFullPath;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUrlDecode(boolean urlDecode) {
/*  95 */     this.urlDecode = urlDecode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isUrlDecode() {
/* 103 */     return this.urlDecode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRemoveSemicolonContent(boolean removeSemicolonContent) {
/* 111 */     this.removeSemicolonContent = removeSemicolonContent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean shouldRemoveSemicolonContent() {
/* 118 */     return this.removeSemicolonContent;
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
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDefaultEncoding(String defaultEncoding) {
/* 135 */     this.defaultEncoding = defaultEncoding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getDefaultEncoding() {
/* 142 */     return this.defaultEncoding;
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
/*     */   
/*     */   public String getLookupPathForRequest(HttpServletRequest request) {
/* 157 */     if (this.alwaysUseFullPath) {
/* 158 */       return getPathWithinApplication(request);
/*     */     }
/*     */     
/* 161 */     String rest = getPathWithinServletMapping(request);
/* 162 */     if (!"".equals(rest)) {
/* 163 */       return rest;
/*     */     }
/*     */     
/* 166 */     return getPathWithinApplication(request);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPathWithinServletMapping(HttpServletRequest request) {
/* 184 */     String path, pathWithinApp = getPathWithinApplication(request);
/* 185 */     String servletPath = getServletPath(request);
/* 186 */     String sanitizedPathWithinApp = getSanitizedPath(pathWithinApp);
/*     */ 
/*     */ 
/*     */     
/* 190 */     if (servletPath.contains(sanitizedPathWithinApp)) {
/* 191 */       path = getRemainingPath(sanitizedPathWithinApp, servletPath, false);
/*     */     } else {
/*     */       
/* 194 */       path = getRemainingPath(pathWithinApp, servletPath, false);
/*     */     } 
/*     */     
/* 197 */     if (path != null)
/*     */     {
/* 199 */       return path;
/*     */     }
/*     */ 
/*     */     
/* 203 */     String pathInfo = request.getPathInfo();
/* 204 */     if (pathInfo != null)
/*     */     {
/*     */       
/* 207 */       return pathInfo;
/*     */     }
/* 209 */     if (!this.urlDecode) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 214 */       path = getRemainingPath(decodeInternal(request, pathWithinApp), servletPath, false);
/* 215 */       if (path != null) {
/* 216 */         return pathWithinApp;
/*     */       }
/*     */     } 
/*     */     
/* 220 */     return servletPath;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPathWithinApplication(HttpServletRequest request) {
/* 231 */     String contextPath = getContextPath(request);
/* 232 */     String requestUri = getRequestUri(request);
/* 233 */     String path = getRemainingPath(requestUri, contextPath, true);
/* 234 */     if (path != null)
/*     */     {
/* 236 */       return StringUtils.hasText(path) ? path : "/";
/*     */     }
/*     */     
/* 239 */     return requestUri;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String getRemainingPath(String requestUri, String mapping, boolean ignoreCase) {
/* 250 */     int index1 = 0;
/* 251 */     int index2 = 0;
/* 252 */     while (index1 < requestUri.length() && index2 < mapping.length()) {
/* 253 */       char c1 = requestUri.charAt(index1);
/* 254 */       char c2 = mapping.charAt(index2);
/* 255 */       if (c1 == ';') {
/* 256 */         index1 = requestUri.indexOf('/', index1);
/* 257 */         if (index1 == -1) {
/* 258 */           return null;
/*     */         }
/* 260 */         c1 = requestUri.charAt(index1);
/*     */       } 
/* 262 */       if (c1 == c2 || (ignoreCase && Character.toLowerCase(c1) == Character.toLowerCase(c2))) {
/*     */         index1++; index2++; continue;
/*     */       } 
/* 265 */       return null;
/*     */     } 
/* 267 */     if (index2 != mapping.length()) {
/* 268 */       return null;
/*     */     }
/* 270 */     if (index1 == requestUri.length()) {
/* 271 */       return "";
/*     */     }
/* 273 */     if (requestUri.charAt(index1) == ';') {
/* 274 */       index1 = requestUri.indexOf('/', index1);
/*     */     }
/* 276 */     return (index1 != -1) ? requestUri.substring(index1) : "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String getSanitizedPath(String path) {
/* 286 */     String sanitized = path;
/*     */     while (true) {
/* 288 */       int index = sanitized.indexOf("//");
/* 289 */       if (index < 0) {
/*     */         break;
/*     */       }
/*     */       
/* 293 */       sanitized = sanitized.substring(0, index) + sanitized.substring(index + 1);
/*     */     } 
/*     */     
/* 296 */     return sanitized;
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
/*     */   
/*     */   public String getRequestUri(HttpServletRequest request) {
/* 311 */     String uri = (String)request.getAttribute("javax.servlet.include.request_uri");
/* 312 */     if (uri == null) {
/* 313 */       uri = request.getRequestURI();
/*     */     }
/* 315 */     return decodeAndCleanUriString(request, uri);
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
/*     */   public String getContextPath(HttpServletRequest request) {
/* 327 */     String contextPath = (String)request.getAttribute("javax.servlet.include.context_path");
/* 328 */     if (contextPath == null) {
/* 329 */       contextPath = request.getContextPath();
/*     */     }
/* 331 */     if ("/".equals(contextPath))
/*     */     {
/* 333 */       contextPath = "";
/*     */     }
/* 335 */     return decodeRequestString(request, contextPath);
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
/*     */   public String getServletPath(HttpServletRequest request) {
/* 347 */     String servletPath = (String)request.getAttribute("javax.servlet.include.servlet_path");
/* 348 */     if (servletPath == null) {
/* 349 */       servletPath = request.getServletPath();
/*     */     }
/* 351 */     if (servletPath.length() > 1 && servletPath.endsWith("/") && shouldRemoveTrailingServletPathSlash(request))
/*     */     {
/*     */ 
/*     */       
/* 355 */       servletPath = servletPath.substring(0, servletPath.length() - 1);
/*     */     }
/* 357 */     return servletPath;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getOriginatingRequestUri(HttpServletRequest request) {
/* 366 */     String uri = (String)request.getAttribute("com.ibm.websphere.servlet.uri_non_decoded");
/* 367 */     if (uri == null) {
/* 368 */       uri = (String)request.getAttribute("javax.servlet.forward.request_uri");
/* 369 */       if (uri == null) {
/* 370 */         uri = request.getRequestURI();
/*     */       }
/*     */     } 
/* 373 */     return decodeAndCleanUriString(request, uri);
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
/*     */   public String getOriginatingContextPath(HttpServletRequest request) {
/* 385 */     String contextPath = (String)request.getAttribute("javax.servlet.forward.context_path");
/* 386 */     if (contextPath == null) {
/* 387 */       contextPath = request.getContextPath();
/*     */     }
/* 389 */     return decodeRequestString(request, contextPath);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getOriginatingServletPath(HttpServletRequest request) {
/* 399 */     String servletPath = (String)request.getAttribute("javax.servlet.forward.servlet_path");
/* 400 */     if (servletPath == null) {
/* 401 */       servletPath = request.getServletPath();
/*     */     }
/* 403 */     return servletPath;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getOriginatingQueryString(HttpServletRequest request) {
/* 413 */     if (request.getAttribute("javax.servlet.forward.request_uri") != null || request
/* 414 */       .getAttribute("javax.servlet.error.request_uri") != null) {
/* 415 */       return (String)request.getAttribute("javax.servlet.forward.query_string");
/*     */     }
/*     */     
/* 418 */     return request.getQueryString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String decodeAndCleanUriString(HttpServletRequest request, String uri) {
/* 426 */     uri = removeSemicolonContent(uri);
/* 427 */     uri = decodeRequestString(request, uri);
/* 428 */     uri = getSanitizedPath(uri);
/* 429 */     return uri;
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
/*     */ 
/*     */   
/*     */   public String decodeRequestString(HttpServletRequest request, String source) {
/* 445 */     if (this.urlDecode && source != null) {
/* 446 */       return decodeInternal(request, source);
/*     */     }
/* 448 */     return source;
/*     */   }
/*     */ 
/*     */   
/*     */   private String decodeInternal(HttpServletRequest request, String source) {
/* 453 */     String enc = determineEncoding(request);
/*     */     try {
/* 455 */       return UriUtils.decode(source, enc);
/*     */     }
/* 457 */     catch (UnsupportedEncodingException ex) {
/* 458 */       if (logger.isWarnEnabled()) {
/* 459 */         logger.warn("Could not decode request string [" + source + "] with encoding '" + enc + "': falling back to platform default encoding; exception message: " + ex
/* 460 */             .getMessage());
/*     */       }
/* 462 */       return URLDecoder.decode(source);
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
/* 477 */     String enc = request.getCharacterEncoding();
/* 478 */     if (enc == null) {
/* 479 */       enc = getDefaultEncoding();
/*     */     }
/* 481 */     return enc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String removeSemicolonContent(String requestUri) {
/* 492 */     return this.removeSemicolonContent ? 
/* 493 */       removeSemicolonContentInternal(requestUri) : removeJsessionid(requestUri);
/*     */   }
/*     */   
/*     */   private String removeSemicolonContentInternal(String requestUri) {
/* 497 */     int semicolonIndex = requestUri.indexOf(';');
/* 498 */     while (semicolonIndex != -1) {
/* 499 */       int slashIndex = requestUri.indexOf('/', semicolonIndex);
/* 500 */       String start = requestUri.substring(0, semicolonIndex);
/* 501 */       requestUri = (slashIndex != -1) ? (start + requestUri.substring(slashIndex)) : start;
/* 502 */       semicolonIndex = requestUri.indexOf(';', semicolonIndex);
/*     */     } 
/* 504 */     return requestUri;
/*     */   }
/*     */   
/*     */   private String removeJsessionid(String requestUri) {
/* 508 */     int startIndex = requestUri.toLowerCase().indexOf(";jsessionid=");
/* 509 */     if (startIndex != -1) {
/* 510 */       int endIndex = requestUri.indexOf(';', startIndex + 12);
/* 511 */       String start = requestUri.substring(0, startIndex);
/* 512 */       requestUri = (endIndex != -1) ? (start + requestUri.substring(endIndex)) : start;
/*     */     } 
/* 514 */     return requestUri;
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
/*     */   
/*     */   public Map<String, String> decodePathVariables(HttpServletRequest request, Map<String, String> vars) {
/* 529 */     if (this.urlDecode) {
/* 530 */       return vars;
/*     */     }
/*     */     
/* 533 */     Map<String, String> decodedVars = new LinkedHashMap<String, String>(vars.size());
/* 534 */     for (Map.Entry<String, String> entry : vars.entrySet()) {
/* 535 */       decodedVars.put(entry.getKey(), decodeInternal(request, entry.getValue()));
/*     */     }
/* 537 */     return decodedVars;
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
/*     */ 
/*     */   
/*     */   public MultiValueMap<String, String> decodeMatrixVariables(HttpServletRequest request, MultiValueMap<String, String> vars) {
/* 553 */     if (this.urlDecode) {
/* 554 */       return vars;
/*     */     }
/*     */     
/* 557 */     LinkedMultiValueMap linkedMultiValueMap = new LinkedMultiValueMap(vars.size());
/* 558 */     for (String key : vars.keySet()) {
/* 559 */       for (String value : vars.get(key)) {
/* 560 */         linkedMultiValueMap.add(key, decodeInternal(request, value));
/*     */       }
/*     */     } 
/* 563 */     return (MultiValueMap<String, String>)linkedMultiValueMap;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean shouldRemoveTrailingServletPathSlash(HttpServletRequest request) {
/* 568 */     if (request.getAttribute("com.ibm.websphere.servlet.uri_non_decoded") == null)
/*     */     {
/*     */ 
/*     */       
/* 572 */       return false;
/*     */     }
/* 574 */     if (websphereComplianceFlag == null) {
/* 575 */       ClassLoader classLoader = UrlPathHelper.class.getClassLoader();
/* 576 */       String className = "com.ibm.ws.webcontainer.WebContainer";
/* 577 */       String methodName = "getWebContainerProperties";
/* 578 */       String propName = "com.ibm.ws.webcontainer.removetrailingservletpathslash";
/* 579 */       boolean flag = false;
/*     */       try {
/* 581 */         Class<?> cl = classLoader.loadClass(className);
/* 582 */         Properties prop = (Properties)cl.getMethod(methodName, new Class[0]).invoke(null, new Object[0]);
/* 583 */         flag = Boolean.parseBoolean(prop.getProperty(propName));
/*     */       }
/* 585 */       catch (Throwable ex) {
/* 586 */         if (logger.isDebugEnabled()) {
/* 587 */           logger.debug("Could not introspect WebSphere web container properties: " + ex);
/*     */         }
/*     */       } 
/* 590 */       websphereComplianceFlag = Boolean.valueOf(flag);
/*     */     } 
/*     */ 
/*     */     
/* 594 */     return !websphereComplianceFlag.booleanValue();
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\we\\util\UrlPathHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */