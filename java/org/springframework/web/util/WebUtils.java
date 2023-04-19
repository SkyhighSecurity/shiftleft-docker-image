/*     */ package org.springframework.web.util;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.util.Collection;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Map;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.TreeMap;
/*     */ import javax.servlet.ServletContext;
/*     */ import javax.servlet.ServletRequest;
/*     */ import javax.servlet.ServletRequestWrapper;
/*     */ import javax.servlet.ServletResponse;
/*     */ import javax.servlet.ServletResponseWrapper;
/*     */ import javax.servlet.http.Cookie;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpSession;
/*     */ import org.springframework.http.HttpRequest;
/*     */ import org.springframework.http.server.ServletServerHttpRequest;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.util.LinkedMultiValueMap;
/*     */ import org.springframework.util.MultiValueMap;
/*     */ import org.springframework.util.ObjectUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class WebUtils
/*     */ {
/*     */   public static final String INCLUDE_REQUEST_URI_ATTRIBUTE = "javax.servlet.include.request_uri";
/*     */   public static final String INCLUDE_CONTEXT_PATH_ATTRIBUTE = "javax.servlet.include.context_path";
/*     */   public static final String INCLUDE_SERVLET_PATH_ATTRIBUTE = "javax.servlet.include.servlet_path";
/*     */   public static final String INCLUDE_PATH_INFO_ATTRIBUTE = "javax.servlet.include.path_info";
/*     */   public static final String INCLUDE_QUERY_STRING_ATTRIBUTE = "javax.servlet.include.query_string";
/*     */   public static final String FORWARD_REQUEST_URI_ATTRIBUTE = "javax.servlet.forward.request_uri";
/*     */   public static final String FORWARD_CONTEXT_PATH_ATTRIBUTE = "javax.servlet.forward.context_path";
/*     */   public static final String FORWARD_SERVLET_PATH_ATTRIBUTE = "javax.servlet.forward.servlet_path";
/*     */   public static final String FORWARD_PATH_INFO_ATTRIBUTE = "javax.servlet.forward.path_info";
/*     */   public static final String FORWARD_QUERY_STRING_ATTRIBUTE = "javax.servlet.forward.query_string";
/*     */   public static final String ERROR_STATUS_CODE_ATTRIBUTE = "javax.servlet.error.status_code";
/*     */   public static final String ERROR_EXCEPTION_TYPE_ATTRIBUTE = "javax.servlet.error.exception_type";
/*     */   public static final String ERROR_MESSAGE_ATTRIBUTE = "javax.servlet.error.message";
/*     */   public static final String ERROR_EXCEPTION_ATTRIBUTE = "javax.servlet.error.exception";
/*     */   public static final String ERROR_REQUEST_URI_ATTRIBUTE = "javax.servlet.error.request_uri";
/*     */   public static final String ERROR_SERVLET_NAME_ATTRIBUTE = "javax.servlet.error.servlet_name";
/*     */   public static final String CONTENT_TYPE_CHARSET_PREFIX = ";charset=";
/*     */   public static final String DEFAULT_CHARACTER_ENCODING = "ISO-8859-1";
/*     */   public static final String TEMP_DIR_CONTEXT_ATTRIBUTE = "javax.servlet.context.tempdir";
/*     */   public static final String HTML_ESCAPE_CONTEXT_PARAM = "defaultHtmlEscape";
/*     */   public static final String RESPONSE_ENCODED_HTML_ESCAPE_CONTEXT_PARAM = "responseEncodedHtmlEscape";
/*     */   public static final String WEB_APP_ROOT_KEY_PARAM = "webAppRootKey";
/*     */   public static final String DEFAULT_WEB_APP_ROOT_KEY = "webapp.root";
/* 132 */   public static final String[] SUBMIT_IMAGE_SUFFIXES = new String[] { ".x", ".y" };
/*     */ 
/*     */   
/* 135 */   public static final String SESSION_MUTEX_ATTRIBUTE = WebUtils.class.getName() + ".MUTEX";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setWebAppRootSystemProperty(ServletContext servletContext) throws IllegalStateException {
/* 153 */     Assert.notNull(servletContext, "ServletContext must not be null");
/* 154 */     String root = servletContext.getRealPath("/");
/* 155 */     if (root == null) {
/* 156 */       throw new IllegalStateException("Cannot set web app root system property when WAR file is not expanded");
/*     */     }
/*     */     
/* 159 */     String param = servletContext.getInitParameter("webAppRootKey");
/* 160 */     String key = (param != null) ? param : "webapp.root";
/* 161 */     String oldValue = System.getProperty(key);
/* 162 */     if (oldValue != null && !StringUtils.pathEquals(oldValue, root)) {
/* 163 */       throw new IllegalStateException("Web app root system property already set to different value: '" + key + "' = [" + oldValue + "] instead of [" + root + "] - Choose unique values for the 'webAppRootKey' context-param in your web.xml files!");
/*     */     }
/*     */ 
/*     */     
/* 167 */     System.setProperty(key, root);
/* 168 */     servletContext.log("Set web app root system property: '" + key + "' = [" + root + "]");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void removeWebAppRootSystemProperty(ServletContext servletContext) {
/* 178 */     Assert.notNull(servletContext, "ServletContext must not be null");
/* 179 */     String param = servletContext.getInitParameter("webAppRootKey");
/* 180 */     String key = (param != null) ? param : "webapp.root";
/* 181 */     System.getProperties().remove(key);
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
/*     */   @Deprecated
/*     */   public static boolean isDefaultHtmlEscape(ServletContext servletContext) {
/* 194 */     if (servletContext == null) {
/* 195 */       return false;
/*     */     }
/* 197 */     String param = servletContext.getInitParameter("defaultHtmlEscape");
/* 198 */     return Boolean.valueOf(param).booleanValue();
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
/*     */   public static Boolean getDefaultHtmlEscape(ServletContext servletContext) {
/* 213 */     if (servletContext == null) {
/* 214 */       return null;
/*     */     }
/* 216 */     String param = servletContext.getInitParameter("defaultHtmlEscape");
/* 217 */     return StringUtils.hasText(param) ? Boolean.valueOf(param) : null;
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
/*     */   public static Boolean getResponseEncodedHtmlEscape(ServletContext servletContext) {
/* 235 */     if (servletContext == null) {
/* 236 */       return null;
/*     */     }
/* 238 */     String param = servletContext.getInitParameter("responseEncodedHtmlEscape");
/* 239 */     return StringUtils.hasText(param) ? Boolean.valueOf(param) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static File getTempDir(ServletContext servletContext) {
/* 249 */     Assert.notNull(servletContext, "ServletContext must not be null");
/* 250 */     return (File)servletContext.getAttribute("javax.servlet.context.tempdir");
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
/*     */   public static String getRealPath(ServletContext servletContext, String path) throws FileNotFoundException {
/* 267 */     Assert.notNull(servletContext, "ServletContext must not be null");
/*     */     
/* 269 */     if (!path.startsWith("/")) {
/* 270 */       path = "/" + path;
/*     */     }
/* 272 */     String realPath = servletContext.getRealPath(path);
/* 273 */     if (realPath == null) {
/* 274 */       throw new FileNotFoundException("ServletContext resource [" + path + "] cannot be resolved to absolute file path - web application archive not expanded?");
/*     */     }
/*     */ 
/*     */     
/* 278 */     return realPath;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getSessionId(HttpServletRequest request) {
/* 287 */     Assert.notNull(request, "Request must not be null");
/* 288 */     HttpSession session = request.getSession(false);
/* 289 */     return (session != null) ? session.getId() : null;
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
/*     */   public static Object getSessionAttribute(HttpServletRequest request, String name) {
/* 301 */     Assert.notNull(request, "Request must not be null");
/* 302 */     HttpSession session = request.getSession(false);
/* 303 */     return (session != null) ? session.getAttribute(name) : null;
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
/*     */   public static Object getRequiredSessionAttribute(HttpServletRequest request, String name) throws IllegalStateException {
/* 318 */     Object attr = getSessionAttribute(request, name);
/* 319 */     if (attr == null) {
/* 320 */       throw new IllegalStateException("No session attribute '" + name + "' found");
/*     */     }
/* 322 */     return attr;
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
/*     */   public static void setSessionAttribute(HttpServletRequest request, String name, Object value) {
/* 334 */     Assert.notNull(request, "Request must not be null");
/* 335 */     if (value != null) {
/* 336 */       request.getSession().setAttribute(name, value);
/*     */     } else {
/*     */       
/* 339 */       HttpSession session = request.getSession(false);
/* 340 */       if (session != null) {
/* 341 */         session.removeAttribute(name);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static Object getOrCreateSessionAttribute(HttpSession session, String name, Class<?> clazz) throws IllegalArgumentException {
/* 361 */     Assert.notNull(session, "Session must not be null");
/* 362 */     Object sessionObject = session.getAttribute(name);
/* 363 */     if (sessionObject == null) {
/*     */       try {
/* 365 */         sessionObject = clazz.newInstance();
/*     */       }
/* 367 */       catch (InstantiationException ex) {
/* 368 */         throw new IllegalArgumentException("Could not instantiate class [" + clazz
/* 369 */             .getName() + "] for session attribute '" + name + "': " + ex
/* 370 */             .getMessage());
/*     */       }
/* 372 */       catch (IllegalAccessException ex) {
/* 373 */         throw new IllegalArgumentException("Could not access default constructor of class [" + clazz
/* 374 */             .getName() + "] for session attribute '" + name + "': " + ex
/* 375 */             .getMessage());
/*     */       } 
/* 377 */       session.setAttribute(name, sessionObject);
/*     */     } 
/* 379 */     return sessionObject;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object getSessionMutex(HttpSession session) {
/* 403 */     Assert.notNull(session, "Session must not be null");
/* 404 */     Object mutex = session.getAttribute(SESSION_MUTEX_ATTRIBUTE);
/* 405 */     if (mutex == null) {
/* 406 */       mutex = session;
/*     */     }
/* 408 */     return mutex;
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
/*     */   public static <T> T getNativeRequest(ServletRequest request, Class<T> requiredType) {
/* 422 */     if (requiredType != null) {
/* 423 */       if (requiredType.isInstance(request)) {
/* 424 */         return (T)request;
/*     */       }
/* 426 */       if (request instanceof ServletRequestWrapper) {
/* 427 */         return getNativeRequest(((ServletRequestWrapper)request).getRequest(), requiredType);
/*     */       }
/*     */     } 
/* 430 */     return null;
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
/*     */   public static <T> T getNativeResponse(ServletResponse response, Class<T> requiredType) {
/* 443 */     if (requiredType != null) {
/* 444 */       if (requiredType.isInstance(response)) {
/* 445 */         return (T)response;
/*     */       }
/* 447 */       if (response instanceof ServletResponseWrapper) {
/* 448 */         return getNativeResponse(((ServletResponseWrapper)response).getResponse(), requiredType);
/*     */       }
/*     */     } 
/* 451 */     return null;
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
/*     */   public static boolean isIncludeRequest(ServletRequest request) {
/* 464 */     return (request.getAttribute("javax.servlet.include.request_uri") != null);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void exposeErrorRequestAttributes(HttpServletRequest request, Throwable ex, String servletName) {
/* 486 */     exposeRequestAttributeIfNotPresent((ServletRequest)request, "javax.servlet.error.status_code", Integer.valueOf(200));
/* 487 */     exposeRequestAttributeIfNotPresent((ServletRequest)request, "javax.servlet.error.exception_type", ex.getClass());
/* 488 */     exposeRequestAttributeIfNotPresent((ServletRequest)request, "javax.servlet.error.message", ex.getMessage());
/* 489 */     exposeRequestAttributeIfNotPresent((ServletRequest)request, "javax.servlet.error.exception", ex);
/* 490 */     exposeRequestAttributeIfNotPresent((ServletRequest)request, "javax.servlet.error.request_uri", request.getRequestURI());
/* 491 */     exposeRequestAttributeIfNotPresent((ServletRequest)request, "javax.servlet.error.servlet_name", servletName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void exposeRequestAttributeIfNotPresent(ServletRequest request, String name, Object value) {
/* 501 */     if (request.getAttribute(name) == null) {
/* 502 */       request.setAttribute(name, value);
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
/*     */   
/*     */   public static void clearErrorRequestAttributes(HttpServletRequest request) {
/* 518 */     request.removeAttribute("javax.servlet.error.status_code");
/* 519 */     request.removeAttribute("javax.servlet.error.exception_type");
/* 520 */     request.removeAttribute("javax.servlet.error.message");
/* 521 */     request.removeAttribute("javax.servlet.error.exception");
/* 522 */     request.removeAttribute("javax.servlet.error.request_uri");
/* 523 */     request.removeAttribute("javax.servlet.error.servlet_name");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static void exposeRequestAttributes(ServletRequest request, Map<String, ?> attributes) {
/* 535 */     Assert.notNull(request, "Request must not be null");
/* 536 */     Assert.notNull(attributes, "Attributes Map must not be null");
/* 537 */     for (Map.Entry<String, ?> entry : attributes.entrySet()) {
/* 538 */       request.setAttribute(entry.getKey(), entry.getValue());
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
/*     */   public static Cookie getCookie(HttpServletRequest request, String name) {
/* 550 */     Assert.notNull(request, "Request must not be null");
/* 551 */     Cookie[] cookies = request.getCookies();
/* 552 */     if (cookies != null) {
/* 553 */       for (Cookie cookie : cookies) {
/* 554 */         if (name.equals(cookie.getName())) {
/* 555 */           return cookie;
/*     */         }
/*     */       } 
/*     */     }
/* 559 */     return null;
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
/*     */   public static boolean hasSubmitParameter(ServletRequest request, String name) {
/* 572 */     Assert.notNull(request, "Request must not be null");
/* 573 */     if (request.getParameter(name) != null) {
/* 574 */       return true;
/*     */     }
/* 576 */     for (String suffix : SUBMIT_IMAGE_SUFFIXES) {
/* 577 */       if (request.getParameter(name + suffix) != null) {
/* 578 */         return true;
/*     */       }
/*     */     } 
/* 581 */     return false;
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
/*     */   public static String findParameterValue(ServletRequest request, String name) {
/* 594 */     return findParameterValue(request.getParameterMap(), name);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String findParameterValue(Map<String, ?> parameters, String name) {
/* 622 */     Object value = parameters.get(name);
/* 623 */     if (value instanceof String[]) {
/* 624 */       String[] values = (String[])value;
/* 625 */       return (values.length > 0) ? values[0] : null;
/*     */     } 
/* 627 */     if (value != null) {
/* 628 */       return value.toString();
/*     */     }
/*     */     
/* 631 */     String prefix = name + "_";
/* 632 */     for (String paramName : parameters.keySet()) {
/* 633 */       if (paramName.startsWith(prefix)) {
/*     */         
/* 635 */         for (String suffix : SUBMIT_IMAGE_SUFFIXES) {
/* 636 */           if (paramName.endsWith(suffix)) {
/* 637 */             return paramName.substring(prefix.length(), paramName.length() - suffix.length());
/*     */           }
/*     */         } 
/* 640 */         return paramName.substring(prefix.length());
/*     */       } 
/*     */     } 
/*     */     
/* 644 */     return null;
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
/*     */   public static Map<String, Object> getParametersStartingWith(ServletRequest request, String prefix) {
/* 662 */     Assert.notNull(request, "Request must not be null");
/* 663 */     Enumeration<String> paramNames = request.getParameterNames();
/* 664 */     Map<String, Object> params = new TreeMap<String, Object>();
/* 665 */     if (prefix == null) {
/* 666 */       prefix = "";
/*     */     }
/* 668 */     while (paramNames != null && paramNames.hasMoreElements()) {
/* 669 */       String paramName = paramNames.nextElement();
/* 670 */       if ("".equals(prefix) || paramName.startsWith(prefix)) {
/* 671 */         String unprefixed = paramName.substring(prefix.length());
/* 672 */         String[] values = request.getParameterValues(paramName);
/* 673 */         if (values == null || values.length == 0) {
/*     */           continue;
/*     */         }
/* 676 */         if (values.length > 1) {
/* 677 */           params.put(unprefixed, values);
/*     */           continue;
/*     */         } 
/* 680 */         params.put(unprefixed, values[0]);
/*     */       } 
/*     */     } 
/*     */     
/* 684 */     return params;
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
/*     */   @Deprecated
/*     */   public static int getTargetPage(ServletRequest request, String paramPrefix, int currentPage) {
/* 699 */     Enumeration<String> paramNames = request.getParameterNames();
/* 700 */     while (paramNames.hasMoreElements()) {
/* 701 */       String paramName = paramNames.nextElement();
/* 702 */       if (paramName.startsWith(paramPrefix)) {
/* 703 */         for (int i = 0; i < SUBMIT_IMAGE_SUFFIXES.length; i++) {
/* 704 */           String suffix = SUBMIT_IMAGE_SUFFIXES[i];
/* 705 */           if (paramName.endsWith(suffix)) {
/* 706 */             paramName = paramName.substring(0, paramName.length() - suffix.length());
/*     */           }
/*     */         } 
/* 709 */         return Integer.parseInt(paramName.substring(paramPrefix.length()));
/*     */       } 
/*     */     } 
/* 712 */     return currentPage;
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
/*     */   @Deprecated
/*     */   public static String extractFilenameFromUrlPath(String urlPath) {
/* 725 */     String filename = extractFullFilenameFromUrlPath(urlPath);
/* 726 */     int dotIndex = filename.lastIndexOf('.');
/* 727 */     if (dotIndex != -1) {
/* 728 */       filename = filename.substring(0, dotIndex);
/*     */     }
/* 730 */     return filename;
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
/*     */   @Deprecated
/*     */   public static String extractFullFilenameFromUrlPath(String urlPath) {
/* 744 */     int end = urlPath.indexOf('?');
/* 745 */     if (end == -1) {
/* 746 */       end = urlPath.indexOf('#');
/* 747 */       if (end == -1) {
/* 748 */         end = urlPath.length();
/*     */       }
/*     */     } 
/* 751 */     int begin = urlPath.lastIndexOf('/', end) + 1;
/* 752 */     int paramIndex = urlPath.indexOf(';', begin);
/* 753 */     end = (paramIndex != -1 && paramIndex < end) ? paramIndex : end;
/* 754 */     return urlPath.substring(begin, end);
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
/*     */   public static MultiValueMap<String, String> parseMatrixVariables(String matrixVariables) {
/* 767 */     LinkedMultiValueMap linkedMultiValueMap = new LinkedMultiValueMap();
/* 768 */     if (!StringUtils.hasText(matrixVariables)) {
/* 769 */       return (MultiValueMap<String, String>)linkedMultiValueMap;
/*     */     }
/* 771 */     StringTokenizer pairs = new StringTokenizer(matrixVariables, ";");
/* 772 */     while (pairs.hasMoreTokens()) {
/* 773 */       String pair = pairs.nextToken();
/* 774 */       int index = pair.indexOf('=');
/* 775 */       if (index != -1) {
/* 776 */         String name = pair.substring(0, index);
/* 777 */         String rawValue = pair.substring(index + 1);
/* 778 */         for (String value : StringUtils.commaDelimitedListToStringArray(rawValue)) {
/* 779 */           linkedMultiValueMap.add(name, value);
/*     */         }
/*     */         continue;
/*     */       } 
/* 783 */       linkedMultiValueMap.add(pair, "");
/*     */     } 
/*     */     
/* 786 */     return (MultiValueMap<String, String>)linkedMultiValueMap;
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
/*     */   
/*     */   public static boolean isValidOrigin(HttpRequest request, Collection<String> allowedOrigins) {
/* 805 */     Assert.notNull(request, "Request must not be null");
/* 806 */     Assert.notNull(allowedOrigins, "Allowed origins must not be null");
/*     */     
/* 808 */     String origin = request.getHeaders().getOrigin();
/* 809 */     if (origin == null || allowedOrigins.contains("*")) {
/* 810 */       return true;
/*     */     }
/* 812 */     if (CollectionUtils.isEmpty(allowedOrigins)) {
/* 813 */       return isSameOrigin(request);
/*     */     }
/*     */     
/* 816 */     return allowedOrigins.contains(origin);
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
/*     */   
/*     */   public static boolean isSameOrigin(HttpRequest request) {
/*     */     UriComponentsBuilder urlBuilder;
/* 836 */     String origin = request.getHeaders().getOrigin();
/* 837 */     if (origin == null) {
/* 838 */       return true;
/*     */     }
/*     */     
/* 841 */     if (request instanceof ServletServerHttpRequest) {
/*     */       
/* 843 */       HttpServletRequest servletRequest = ((ServletServerHttpRequest)request).getServletRequest();
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 848 */       urlBuilder = (new UriComponentsBuilder()).scheme(servletRequest.getScheme()).host(servletRequest.getServerName()).port(servletRequest.getServerPort()).adaptFromForwardedHeaders(request.getHeaders());
/*     */     } else {
/*     */       
/* 851 */       urlBuilder = UriComponentsBuilder.fromHttpRequest(request);
/*     */     } 
/* 853 */     UriComponents actualUrl = urlBuilder.build();
/* 854 */     UriComponents originUrl = UriComponentsBuilder.fromOriginHeader(origin).build();
/* 855 */     return (ObjectUtils.nullSafeEquals(actualUrl.getHost(), originUrl.getHost()) && 
/* 856 */       getPort(actualUrl) == getPort(originUrl));
/*     */   }
/*     */   
/*     */   private static int getPort(UriComponents uri) {
/* 860 */     int port = uri.getPort();
/* 861 */     if (port == -1) {
/* 862 */       if ("http".equals(uri.getScheme()) || "ws".equals(uri.getScheme())) {
/* 863 */         port = 80;
/*     */       }
/* 865 */       else if ("https".equals(uri.getScheme()) || "wss".equals(uri.getScheme())) {
/* 866 */         port = 443;
/*     */       } 
/*     */     }
/* 869 */     return port;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\we\\util\WebUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */