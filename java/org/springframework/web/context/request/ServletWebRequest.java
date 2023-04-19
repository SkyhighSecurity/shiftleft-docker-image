/*     */ package org.springframework.web.context.request;
/*     */ 
/*     */ import java.security.Principal;
/*     */ import java.text.ParseException;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Arrays;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.TimeZone;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.servlet.ServletRequest;
/*     */ import javax.servlet.ServletResponse;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import javax.servlet.http.HttpSession;
/*     */ import org.springframework.http.HttpMethod;
/*     */ import org.springframework.http.HttpStatus;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.util.StringUtils;
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
/*     */ public class ServletWebRequest
/*     */   extends ServletRequestAttributes
/*     */   implements NativeWebRequest
/*     */ {
/*     */   private static final String ETAG = "ETag";
/*     */   private static final String IF_MODIFIED_SINCE = "If-Modified-Since";
/*     */   private static final String IF_UNMODIFIED_SINCE = "If-Unmodified-Since";
/*     */   private static final String IF_NONE_MATCH = "If-None-Match";
/*     */   private static final String LAST_MODIFIED = "Last-Modified";
/*  63 */   private static final List<String> SAFE_METHODS = Arrays.asList(new String[] { "GET", "HEAD" });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  69 */   private static final Pattern ETAG_HEADER_VALUE_PATTERN = Pattern.compile("\\*|\\s*((W\\/)?(\"[^\"]*\"))\\s*,?");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  75 */   private static final String[] DATE_FORMATS = new String[] { "EEE, dd MMM yyyy HH:mm:ss zzz", "EEE, dd-MMM-yy HH:mm:ss zzz", "EEE MMM dd HH:mm:ss yyyy" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  81 */   private static final TimeZone GMT = TimeZone.getTimeZone("GMT");
/*     */ 
/*     */ 
/*     */   
/*  85 */   private static final boolean servlet3Present = ClassUtils.hasMethod(HttpServletResponse.class, "getHeader", new Class[] { String.class });
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean notModified = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ServletWebRequest(HttpServletRequest request) {
/*  95 */     super(request);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ServletWebRequest(HttpServletRequest request, HttpServletResponse response) {
/* 104 */     super(request, response);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getNativeRequest() {
/* 110 */     return getRequest();
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getNativeResponse() {
/* 115 */     return getResponse();
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T getNativeRequest(Class<T> requiredType) {
/* 120 */     return (T)WebUtils.getNativeRequest((ServletRequest)getRequest(), requiredType);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T getNativeResponse(Class<T> requiredType) {
/* 125 */     return (T)WebUtils.getNativeResponse((ServletResponse)getResponse(), requiredType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpMethod getHttpMethod() {
/* 133 */     return HttpMethod.resolve(getRequest().getMethod());
/*     */   }
/*     */ 
/*     */   
/*     */   public String getHeader(String headerName) {
/* 138 */     return getRequest().getHeader(headerName);
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] getHeaderValues(String headerName) {
/* 143 */     String[] headerValues = StringUtils.toStringArray(getRequest().getHeaders(headerName));
/* 144 */     return !ObjectUtils.isEmpty((Object[])headerValues) ? headerValues : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<String> getHeaderNames() {
/* 149 */     return CollectionUtils.toIterator(getRequest().getHeaderNames());
/*     */   }
/*     */ 
/*     */   
/*     */   public String getParameter(String paramName) {
/* 154 */     return getRequest().getParameter(paramName);
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] getParameterValues(String paramName) {
/* 159 */     return getRequest().getParameterValues(paramName);
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<String> getParameterNames() {
/* 164 */     return CollectionUtils.toIterator(getRequest().getParameterNames());
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, String[]> getParameterMap() {
/* 169 */     return getRequest().getParameterMap();
/*     */   }
/*     */ 
/*     */   
/*     */   public Locale getLocale() {
/* 174 */     return getRequest().getLocale();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getContextPath() {
/* 179 */     return getRequest().getContextPath();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getRemoteUser() {
/* 184 */     return getRequest().getRemoteUser();
/*     */   }
/*     */ 
/*     */   
/*     */   public Principal getUserPrincipal() {
/* 189 */     return getRequest().getUserPrincipal();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isUserInRole(String role) {
/* 194 */     return getRequest().isUserInRole(role);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSecure() {
/* 199 */     return getRequest().isSecure();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean checkNotModified(long lastModifiedTimestamp) {
/* 205 */     return checkNotModified((String)null, lastModifiedTimestamp);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean checkNotModified(String etag) {
/* 210 */     return checkNotModified(etag, -1L);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean checkNotModified(String etag, long lastModifiedTimestamp) {
/* 215 */     HttpServletResponse response = getResponse();
/* 216 */     if (this.notModified || !isStatusOK(response)) {
/* 217 */       return this.notModified;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 223 */     if (validateIfUnmodifiedSince(lastModifiedTimestamp)) {
/* 224 */       if (this.notModified) {
/* 225 */         response.setStatus(HttpStatus.PRECONDITION_FAILED.value());
/*     */       }
/* 227 */       return this.notModified;
/*     */     } 
/*     */     
/* 230 */     boolean validated = validateIfNoneMatch(etag);
/* 231 */     if (!validated) {
/* 232 */       validateIfModifiedSince(lastModifiedTimestamp);
/*     */     }
/*     */ 
/*     */     
/* 236 */     boolean isHttpGetOrHead = SAFE_METHODS.contains(getRequest().getMethod());
/* 237 */     if (this.notModified) {
/* 238 */       response.setStatus(isHttpGetOrHead ? HttpStatus.NOT_MODIFIED
/* 239 */           .value() : HttpStatus.PRECONDITION_FAILED.value());
/*     */     }
/* 241 */     if (isHttpGetOrHead) {
/* 242 */       if (lastModifiedTimestamp > 0L && isHeaderAbsent(response, "Last-Modified")) {
/* 243 */         response.setDateHeader("Last-Modified", lastModifiedTimestamp);
/*     */       }
/* 245 */       if (StringUtils.hasLength(etag) && isHeaderAbsent(response, "ETag")) {
/* 246 */         response.setHeader("ETag", padEtagIfNecessary(etag));
/*     */       }
/*     */     } 
/*     */     
/* 250 */     return this.notModified;
/*     */   }
/*     */   
/*     */   private boolean isStatusOK(HttpServletResponse response) {
/* 254 */     if (response == null || !servlet3Present)
/*     */     {
/* 256 */       return true;
/*     */     }
/* 258 */     return (response.getStatus() == 200);
/*     */   }
/*     */   
/*     */   private boolean isHeaderAbsent(HttpServletResponse response, String header) {
/* 262 */     if (response == null || !servlet3Present)
/*     */     {
/* 264 */       return true;
/*     */     }
/* 266 */     return (response.getHeader(header) == null);
/*     */   }
/*     */   
/*     */   private boolean validateIfUnmodifiedSince(long lastModifiedTimestamp) {
/* 270 */     if (lastModifiedTimestamp < 0L) {
/* 271 */       return false;
/*     */     }
/* 273 */     long ifUnmodifiedSince = parseDateHeader("If-Unmodified-Since");
/* 274 */     if (ifUnmodifiedSince == -1L) {
/* 275 */       return false;
/*     */     }
/*     */     
/* 278 */     this.notModified = (ifUnmodifiedSince < lastModifiedTimestamp / 1000L * 1000L);
/* 279 */     return true;
/*     */   }
/*     */   private boolean validateIfNoneMatch(String etag) {
/*     */     Enumeration<String> ifNoneMatch;
/* 283 */     if (!StringUtils.hasLength(etag)) {
/* 284 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/* 289 */       ifNoneMatch = getRequest().getHeaders("If-None-Match");
/*     */     }
/* 291 */     catch (IllegalArgumentException ex) {
/* 292 */       return false;
/*     */     } 
/* 294 */     if (!ifNoneMatch.hasMoreElements()) {
/* 295 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 299 */     etag = padEtagIfNecessary(etag);
/* 300 */     while (ifNoneMatch.hasMoreElements()) {
/* 301 */       String clientETags = ifNoneMatch.nextElement();
/* 302 */       Matcher etagMatcher = ETAG_HEADER_VALUE_PATTERN.matcher(clientETags);
/*     */       
/* 304 */       while (etagMatcher.find()) {
/* 305 */         if (StringUtils.hasLength(etagMatcher.group()) && etag
/* 306 */           .replaceFirst("^W/", "").equals(etagMatcher.group(3))) {
/* 307 */           this.notModified = true;
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 313 */     return true;
/*     */   }
/*     */   
/*     */   private String padEtagIfNecessary(String etag) {
/* 317 */     if (!StringUtils.hasLength(etag)) {
/* 318 */       return etag;
/*     */     }
/* 320 */     if ((etag.startsWith("\"") || etag.startsWith("W/\"")) && etag.endsWith("\"")) {
/* 321 */       return etag;
/*     */     }
/* 323 */     return "\"" + etag + "\"";
/*     */   }
/*     */   
/*     */   private boolean validateIfModifiedSince(long lastModifiedTimestamp) {
/* 327 */     if (lastModifiedTimestamp < 0L) {
/* 328 */       return false;
/*     */     }
/* 330 */     long ifModifiedSince = parseDateHeader("If-Modified-Since");
/* 331 */     if (ifModifiedSince == -1L) {
/* 332 */       return false;
/*     */     }
/*     */     
/* 335 */     this.notModified = (ifModifiedSince >= lastModifiedTimestamp / 1000L * 1000L);
/* 336 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isNotModified() {
/* 340 */     return this.notModified;
/*     */   }
/*     */   
/*     */   private long parseDateHeader(String headerName) {
/* 344 */     long dateValue = -1L;
/*     */     try {
/* 346 */       dateValue = getRequest().getDateHeader(headerName);
/*     */     }
/* 348 */     catch (IllegalArgumentException ex) {
/* 349 */       String headerValue = getHeader(headerName);
/*     */       
/* 351 */       int separatorIndex = headerValue.indexOf(';');
/* 352 */       if (separatorIndex != -1) {
/* 353 */         String datePart = headerValue.substring(0, separatorIndex);
/* 354 */         dateValue = parseDateValue(datePart);
/*     */       } 
/*     */     } 
/* 357 */     return dateValue;
/*     */   }
/*     */   
/*     */   private long parseDateValue(String headerValue) {
/* 361 */     if (headerValue == null)
/*     */     {
/* 363 */       return -1L;
/*     */     }
/* 365 */     if (headerValue.length() >= 3)
/*     */     {
/*     */       
/* 368 */       for (String dateFormat : DATE_FORMATS) {
/* 369 */         SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.US);
/* 370 */         simpleDateFormat.setTimeZone(GMT);
/*     */         try {
/* 372 */           return simpleDateFormat.parse(headerValue).getTime();
/*     */         }
/* 374 */         catch (ParseException parseException) {}
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 379 */     return -1L;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDescription(boolean includeClientInfo) {
/* 384 */     HttpServletRequest request = getRequest();
/* 385 */     StringBuilder sb = new StringBuilder();
/* 386 */     sb.append("uri=").append(request.getRequestURI());
/* 387 */     if (includeClientInfo) {
/* 388 */       String client = request.getRemoteAddr();
/* 389 */       if (StringUtils.hasLength(client)) {
/* 390 */         sb.append(";client=").append(client);
/*     */       }
/* 392 */       HttpSession session = request.getSession(false);
/* 393 */       if (session != null) {
/* 394 */         sb.append(";session=").append(session.getId());
/*     */       }
/* 396 */       String user = request.getRemoteUser();
/* 397 */       if (StringUtils.hasLength(user)) {
/* 398 */         sb.append(";user=").append(user);
/*     */       }
/*     */     } 
/* 401 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 407 */     return "ServletWebRequest: " + getDescription(true);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\context\request\ServletWebRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */