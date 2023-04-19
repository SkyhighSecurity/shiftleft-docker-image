/*      */ package org.springframework.http;
/*      */ 
/*      */ import java.io.Serializable;
/*      */ import java.net.URI;
/*      */ import java.nio.charset.Charset;
/*      */ import java.text.ParseException;
/*      */ import java.text.SimpleDateFormat;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Date;
/*      */ import java.util.EnumSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.TimeZone;
/*      */ import java.util.regex.Matcher;
/*      */ import java.util.regex.Pattern;
/*      */ import org.springframework.util.Assert;
/*      */ import org.springframework.util.LinkedCaseInsensitiveMap;
/*      */ import org.springframework.util.MultiValueMap;
/*      */ import org.springframework.util.StringUtils;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class HttpHeaders
/*      */   implements MultiValueMap<String, String>, Serializable
/*      */ {
/*      */   private static final long serialVersionUID = -8578554704772377436L;
/*      */   public static final String ACCEPT = "Accept";
/*      */   public static final String ACCEPT_CHARSET = "Accept-Charset";
/*      */   public static final String ACCEPT_ENCODING = "Accept-Encoding";
/*      */   public static final String ACCEPT_LANGUAGE = "Accept-Language";
/*      */   public static final String ACCEPT_RANGES = "Accept-Ranges";
/*      */   public static final String ACCESS_CONTROL_ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";
/*      */   public static final String ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";
/*      */   public static final String ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";
/*      */   public static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
/*      */   public static final String ACCESS_CONTROL_EXPOSE_HEADERS = "Access-Control-Expose-Headers";
/*      */   public static final String ACCESS_CONTROL_MAX_AGE = "Access-Control-Max-Age";
/*      */   public static final String ACCESS_CONTROL_REQUEST_HEADERS = "Access-Control-Request-Headers";
/*      */   public static final String ACCESS_CONTROL_REQUEST_METHOD = "Access-Control-Request-Method";
/*      */   public static final String AGE = "Age";
/*      */   public static final String ALLOW = "Allow";
/*      */   public static final String AUTHORIZATION = "Authorization";
/*      */   public static final String CACHE_CONTROL = "Cache-Control";
/*      */   public static final String CONNECTION = "Connection";
/*      */   public static final String CONTENT_ENCODING = "Content-Encoding";
/*      */   public static final String CONTENT_DISPOSITION = "Content-Disposition";
/*      */   public static final String CONTENT_LANGUAGE = "Content-Language";
/*      */   public static final String CONTENT_LENGTH = "Content-Length";
/*      */   public static final String CONTENT_LOCATION = "Content-Location";
/*      */   public static final String CONTENT_RANGE = "Content-Range";
/*      */   public static final String CONTENT_TYPE = "Content-Type";
/*      */   public static final String COOKIE = "Cookie";
/*      */   public static final String DATE = "Date";
/*      */   public static final String ETAG = "ETag";
/*      */   public static final String EXPECT = "Expect";
/*      */   public static final String EXPIRES = "Expires";
/*      */   public static final String FROM = "From";
/*      */   public static final String HOST = "Host";
/*      */   public static final String IF_MATCH = "If-Match";
/*      */   public static final String IF_MODIFIED_SINCE = "If-Modified-Since";
/*      */   public static final String IF_NONE_MATCH = "If-None-Match";
/*      */   public static final String IF_RANGE = "If-Range";
/*      */   public static final String IF_UNMODIFIED_SINCE = "If-Unmodified-Since";
/*      */   public static final String LAST_MODIFIED = "Last-Modified";
/*      */   public static final String LINK = "Link";
/*      */   public static final String LOCATION = "Location";
/*      */   public static final String MAX_FORWARDS = "Max-Forwards";
/*      */   public static final String ORIGIN = "Origin";
/*      */   public static final String PRAGMA = "Pragma";
/*      */   public static final String PROXY_AUTHENTICATE = "Proxy-Authenticate";
/*      */   public static final String PROXY_AUTHORIZATION = "Proxy-Authorization";
/*      */   public static final String RANGE = "Range";
/*      */   public static final String REFERER = "Referer";
/*      */   public static final String RETRY_AFTER = "Retry-After";
/*      */   public static final String SERVER = "Server";
/*      */   public static final String SET_COOKIE = "Set-Cookie";
/*      */   public static final String SET_COOKIE2 = "Set-Cookie2";
/*      */   public static final String TE = "TE";
/*      */   public static final String TRAILER = "Trailer";
/*      */   public static final String TRANSFER_ENCODING = "Transfer-Encoding";
/*      */   public static final String UPGRADE = "Upgrade";
/*      */   public static final String USER_AGENT = "User-Agent";
/*      */   public static final String VARY = "Vary";
/*      */   public static final String VIA = "Via";
/*      */   public static final String WARNING = "Warning";
/*      */   public static final String WWW_AUTHENTICATE = "WWW-Authenticate";
/*  374 */   private static final Pattern ETAG_HEADER_VALUE_PATTERN = Pattern.compile("\\*|\\s*((W\\/)?(\"[^\"]*\"))\\s*,?");
/*      */   
/*  376 */   private static final TimeZone GMT = TimeZone.getTimeZone("GMT");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  382 */   private static final String[] DATE_FORMATS = new String[] { "EEE, dd MMM yyyy HH:mm:ss zzz", "EEE, dd-MMM-yy HH:mm:ss zzz", "EEE MMM dd HH:mm:ss yyyy" };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final Map<String, List<String>> headers;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public HttpHeaders() {
/*  396 */     this((Map<String, List<String>>)new LinkedCaseInsensitiveMap(8, Locale.ENGLISH), false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private HttpHeaders(Map<String, List<String>> headers, boolean readOnly) {
/*  403 */     if (readOnly) {
/*      */       
/*  405 */       LinkedCaseInsensitiveMap linkedCaseInsensitiveMap = new LinkedCaseInsensitiveMap(headers.size(), Locale.ENGLISH);
/*  406 */       for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
/*  407 */         List<String> values = Collections.unmodifiableList(entry.getValue());
/*  408 */         linkedCaseInsensitiveMap.put(entry.getKey(), values);
/*      */       } 
/*  410 */       this.headers = Collections.unmodifiableMap((Map<? extends String, ? extends List<String>>)linkedCaseInsensitiveMap);
/*      */     } else {
/*      */       
/*  413 */       this.headers = headers;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAccept(List<MediaType> acceptableMediaTypes) {
/*  423 */     set("Accept", MediaType.toString(acceptableMediaTypes));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<MediaType> getAccept() {
/*  432 */     return MediaType.parseMediaTypes(get("Accept"));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAccessControlAllowCredentials(boolean allowCredentials) {
/*  439 */     set("Access-Control-Allow-Credentials", Boolean.toString(allowCredentials));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getAccessControlAllowCredentials() {
/*  446 */     return Boolean.parseBoolean(getFirst("Access-Control-Allow-Credentials"));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAccessControlAllowHeaders(List<String> allowedHeaders) {
/*  453 */     set("Access-Control-Allow-Headers", toCommaDelimitedString(allowedHeaders));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<String> getAccessControlAllowHeaders() {
/*  460 */     return getValuesAsList("Access-Control-Allow-Headers");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAccessControlAllowMethods(List<HttpMethod> allowedMethods) {
/*  467 */     set("Access-Control-Allow-Methods", StringUtils.collectionToCommaDelimitedString(allowedMethods));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<HttpMethod> getAccessControlAllowMethods() {
/*  474 */     List<HttpMethod> result = new ArrayList<HttpMethod>();
/*  475 */     String value = getFirst("Access-Control-Allow-Methods");
/*  476 */     if (value != null) {
/*  477 */       String[] tokens = StringUtils.tokenizeToStringArray(value, ",");
/*  478 */       for (String token : tokens) {
/*  479 */         HttpMethod resolved = HttpMethod.resolve(token);
/*  480 */         if (resolved != null) {
/*  481 */           result.add(resolved);
/*      */         }
/*      */       } 
/*      */     } 
/*  485 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAccessControlAllowOrigin(String allowedOrigin) {
/*  492 */     set("Access-Control-Allow-Origin", allowedOrigin);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getAccessControlAllowOrigin() {
/*  499 */     return getFieldValues("Access-Control-Allow-Origin");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAccessControlExposeHeaders(List<String> exposedHeaders) {
/*  506 */     set("Access-Control-Expose-Headers", toCommaDelimitedString(exposedHeaders));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<String> getAccessControlExposeHeaders() {
/*  513 */     return getValuesAsList("Access-Control-Expose-Headers");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAccessControlMaxAge(long maxAge) {
/*  520 */     set("Access-Control-Max-Age", Long.toString(maxAge));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getAccessControlMaxAge() {
/*  528 */     String value = getFirst("Access-Control-Max-Age");
/*  529 */     return (value != null) ? Long.parseLong(value) : -1L;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAccessControlRequestHeaders(List<String> requestHeaders) {
/*  536 */     set("Access-Control-Request-Headers", toCommaDelimitedString(requestHeaders));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<String> getAccessControlRequestHeaders() {
/*  543 */     return getValuesAsList("Access-Control-Request-Headers");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAccessControlRequestMethod(HttpMethod requestMethod) {
/*  550 */     set("Access-Control-Request-Method", requestMethod.name());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public HttpMethod getAccessControlRequestMethod() {
/*  557 */     return HttpMethod.resolve(getFirst("Access-Control-Request-Method"));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAcceptCharset(List<Charset> acceptableCharsets) {
/*  565 */     StringBuilder builder = new StringBuilder();
/*  566 */     for (Iterator<Charset> iterator = acceptableCharsets.iterator(); iterator.hasNext(); ) {
/*  567 */       Charset charset = iterator.next();
/*  568 */       builder.append(charset.name().toLowerCase(Locale.ENGLISH));
/*  569 */       if (iterator.hasNext()) {
/*  570 */         builder.append(", ");
/*      */       }
/*      */     } 
/*  573 */     set("Accept-Charset", builder.toString());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<Charset> getAcceptCharset() {
/*  581 */     String value = getFirst("Accept-Charset");
/*  582 */     if (value != null) {
/*  583 */       String[] tokens = StringUtils.tokenizeToStringArray(value, ",");
/*  584 */       List<Charset> result = new ArrayList<Charset>(tokens.length);
/*  585 */       for (String token : tokens) {
/*  586 */         String charsetName; int paramIdx = token.indexOf(';');
/*      */         
/*  588 */         if (paramIdx == -1) {
/*  589 */           charsetName = token;
/*      */         } else {
/*      */           
/*  592 */           charsetName = token.substring(0, paramIdx);
/*      */         } 
/*  594 */         if (!charsetName.equals("*")) {
/*  595 */           result.add(Charset.forName(charsetName));
/*      */         }
/*      */       } 
/*  598 */       return result;
/*      */     } 
/*      */     
/*  601 */     return Collections.emptyList();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAllow(Set<HttpMethod> allowedMethods) {
/*  610 */     set("Allow", StringUtils.collectionToCommaDelimitedString(allowedMethods));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Set<HttpMethod> getAllow() {
/*  619 */     String value = getFirst("Allow");
/*  620 */     if (!StringUtils.isEmpty(value)) {
/*  621 */       String[] tokens = StringUtils.tokenizeToStringArray(value, ",");
/*  622 */       List<HttpMethod> result = new ArrayList<HttpMethod>(tokens.length);
/*  623 */       for (String token : tokens) {
/*  624 */         HttpMethod resolved = HttpMethod.resolve(token);
/*  625 */         if (resolved != null) {
/*  626 */           result.add(resolved);
/*      */         }
/*      */       } 
/*  629 */       return EnumSet.copyOf(result);
/*      */     } 
/*      */     
/*  632 */     return EnumSet.noneOf(HttpMethod.class);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCacheControl(String cacheControl) {
/*  640 */     set("Cache-Control", cacheControl);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getCacheControl() {
/*  647 */     return getFieldValues("Cache-Control");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setConnection(String connection) {
/*  654 */     set("Connection", connection);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setConnection(List<String> connection) {
/*  661 */     set("Connection", toCommaDelimitedString(connection));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<String> getConnection() {
/*  668 */     return getValuesAsList("Connection");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setContentDispositionFormData(String name, String filename) {
/*  682 */     Assert.notNull(name, "'name' must not be null");
/*  683 */     StringBuilder builder = new StringBuilder("form-data; name=\"");
/*  684 */     builder.append(name).append('"');
/*  685 */     if (filename != null) {
/*  686 */       builder.append("; filename=\"");
/*  687 */       builder.append(filename).append('"');
/*      */     } 
/*  689 */     set("Content-Disposition", builder.toString());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public void setContentDispositionFormData(String name, String filename, Charset charset) {
/*  708 */     Assert.notNull(name, "'name' must not be null");
/*  709 */     StringBuilder builder = new StringBuilder("form-data; name=\"");
/*  710 */     builder.append(name).append('"');
/*  711 */     if (filename != null) {
/*  712 */       if (charset == null || charset.name().equals("US-ASCII")) {
/*  713 */         builder.append("; filename=\"");
/*  714 */         builder.append(filename).append('"');
/*      */       } else {
/*      */         
/*  717 */         builder.append("; filename*=");
/*  718 */         builder.append(encodeHeaderFieldParam(filename, charset));
/*      */       } 
/*      */     }
/*  721 */     set("Content-Disposition", builder.toString());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setContentLength(long contentLength) {
/*  729 */     set("Content-Length", Long.toString(contentLength));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getContentLength() {
/*  738 */     String value = getFirst("Content-Length");
/*  739 */     return (value != null) ? Long.parseLong(value) : -1L;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setContentType(MediaType mediaType) {
/*  747 */     Assert.isTrue(!mediaType.isWildcardType(), "'Content-Type' cannot contain wildcard type '*'");
/*  748 */     Assert.isTrue(!mediaType.isWildcardSubtype(), "'Content-Type' cannot contain wildcard subtype '*'");
/*  749 */     set("Content-Type", mediaType.toString());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public MediaType getContentType() {
/*  758 */     String value = getFirst("Content-Type");
/*  759 */     return StringUtils.hasLength(value) ? MediaType.parseMediaType(value) : null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDate(long date) {
/*  769 */     setDate("Date", date);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getDate() {
/*  780 */     return getFirstDate("Date");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setETag(String etag) {
/*  787 */     if (etag != null) {
/*  788 */       Assert.isTrue((etag.startsWith("\"") || etag.startsWith("W/")), "Invalid ETag: does not start with W/ or \"");
/*      */       
/*  790 */       Assert.isTrue(etag.endsWith("\""), "Invalid ETag: does not end with \"");
/*      */     } 
/*  792 */     set("ETag", etag);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getETag() {
/*  799 */     return getFirst("ETag");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setExpires(long expires) {
/*  809 */     setDate("Expires", expires);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getExpires() {
/*  819 */     return getFirstDate("Expires", false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setIfMatch(String ifMatch) {
/*  827 */     set("If-Match", ifMatch);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setIfMatch(List<String> ifMatchList) {
/*  835 */     set("If-Match", toCommaDelimitedString(ifMatchList));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<String> getIfMatch() {
/*  843 */     return getETagValuesAsList("If-Match");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setIfModifiedSince(long ifModifiedSince) {
/*  852 */     setDate("If-Modified-Since", ifModifiedSince);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getIfModifiedSince() {
/*  861 */     return getFirstDate("If-Modified-Since", false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setIfNoneMatch(String ifNoneMatch) {
/*  868 */     set("If-None-Match", ifNoneMatch);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setIfNoneMatch(List<String> ifNoneMatchList) {
/*  875 */     set("If-None-Match", toCommaDelimitedString(ifNoneMatchList));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<String> getIfNoneMatch() {
/*  882 */     return getETagValuesAsList("If-None-Match");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setIfUnmodifiedSince(long ifUnmodifiedSince) {
/*  892 */     setDate("If-Unmodified-Since", ifUnmodifiedSince);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getIfUnmodifiedSince() {
/*  902 */     return getFirstDate("If-Unmodified-Since", false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLastModified(long lastModified) {
/*  912 */     setDate("Last-Modified", lastModified);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getLastModified() {
/*  922 */     return getFirstDate("Last-Modified", false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLocation(URI location) {
/*  930 */     set("Location", location.toASCIIString());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public URI getLocation() {
/*  939 */     String value = getFirst("Location");
/*  940 */     return (value != null) ? URI.create(value) : null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setOrigin(String origin) {
/*  947 */     set("Origin", origin);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getOrigin() {
/*  954 */     return getFirst("Origin");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPragma(String pragma) {
/*  961 */     set("Pragma", pragma);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getPragma() {
/*  968 */     return getFirst("Pragma");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setRange(List<HttpRange> ranges) {
/*  975 */     String value = HttpRange.toString(ranges);
/*  976 */     set("Range", value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<HttpRange> getRange() {
/*  984 */     String value = getFirst("Range");
/*  985 */     return HttpRange.parseRanges(value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setUpgrade(String upgrade) {
/*  992 */     set("Upgrade", upgrade);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getUpgrade() {
/*  999 */     return getFirst("Upgrade");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setVary(List<String> requestHeaders) {
/* 1010 */     set("Vary", toCommaDelimitedString(requestHeaders));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<String> getVary() {
/* 1018 */     return getValuesAsList("Vary");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDate(String headerName, long date) {
/* 1028 */     SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMATS[0], Locale.US);
/* 1029 */     dateFormat.setTimeZone(GMT);
/* 1030 */     set(headerName, dateFormat.format(new Date(date)));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getFirstDate(String headerName) {
/* 1042 */     return getFirstDate(headerName, true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private long getFirstDate(String headerName, boolean rejectInvalid) {
/* 1057 */     String headerValue = getFirst(headerName);
/* 1058 */     if (headerValue == null)
/*      */     {
/* 1060 */       return -1L;
/*      */     }
/* 1062 */     if (headerValue.length() >= 3)
/*      */     {
/*      */       
/* 1065 */       for (String dateFormat : DATE_FORMATS) {
/* 1066 */         SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.US);
/* 1067 */         simpleDateFormat.setTimeZone(GMT);
/*      */         try {
/* 1069 */           return simpleDateFormat.parse(headerValue).getTime();
/*      */         }
/* 1071 */         catch (ParseException parseException) {}
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/* 1076 */     if (rejectInvalid) {
/* 1077 */       throw new IllegalArgumentException("Cannot parse date value \"" + headerValue + "\" for \"" + headerName + "\" header");
/*      */     }
/*      */     
/* 1080 */     return -1L;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<String> getValuesAsList(String headerName) {
/* 1091 */     List<String> values = get(headerName);
/* 1092 */     if (values != null) {
/* 1093 */       List<String> result = new ArrayList<String>();
/* 1094 */       for (String value : values) {
/* 1095 */         if (value != null) {
/* 1096 */           String[] tokens = StringUtils.tokenizeToStringArray(value, ",");
/* 1097 */           for (String token : tokens) {
/* 1098 */             result.add(token);
/*      */           }
/*      */         } 
/*      */       } 
/* 1102 */       return result;
/*      */     } 
/* 1104 */     return Collections.emptyList();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected List<String> getETagValuesAsList(String headerName) {
/* 1114 */     List<String> values = get(headerName);
/* 1115 */     if (values != null) {
/* 1116 */       List<String> result = new ArrayList<String>();
/* 1117 */       for (String value : values) {
/* 1118 */         if (value != null) {
/* 1119 */           Matcher matcher = ETAG_HEADER_VALUE_PATTERN.matcher(value);
/* 1120 */           while (matcher.find()) {
/* 1121 */             if ("*".equals(matcher.group())) {
/* 1122 */               result.add(matcher.group());
/*      */               continue;
/*      */             } 
/* 1125 */             result.add(matcher.group(1));
/*      */           } 
/*      */           
/* 1128 */           if (result.isEmpty()) {
/* 1129 */             throw new IllegalArgumentException("Could not parse header '" + headerName + "' with value '" + value + "'");
/*      */           }
/*      */         } 
/*      */       } 
/*      */       
/* 1134 */       return result;
/*      */     } 
/* 1136 */     return Collections.emptyList();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected String getFieldValues(String headerName) {
/* 1146 */     List<String> headerValues = get(headerName);
/* 1147 */     return (headerValues != null) ? toCommaDelimitedString(headerValues) : null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected String toCommaDelimitedString(List<String> headerValues) {
/* 1156 */     StringBuilder builder = new StringBuilder();
/* 1157 */     for (Iterator<String> it = headerValues.iterator(); it.hasNext(); ) {
/* 1158 */       String val = it.next();
/* 1159 */       builder.append(val);
/* 1160 */       if (it.hasNext()) {
/* 1161 */         builder.append(", ");
/*      */       }
/*      */     } 
/* 1164 */     return builder.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getFirst(String headerName) {
/* 1177 */     List<String> headerValues = this.headers.get(headerName);
/* 1178 */     return (headerValues != null) ? headerValues.get(0) : null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void add(String headerName, String headerValue) {
/* 1191 */     List<String> headerValues = this.headers.get(headerName);
/* 1192 */     if (headerValues == null) {
/* 1193 */       headerValues = new LinkedList<String>();
/* 1194 */       this.headers.put(headerName, headerValues);
/*      */     } 
/* 1196 */     headerValues.add(headerValue);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void set(String headerName, String headerValue) {
/* 1209 */     List<String> headerValues = new LinkedList<String>();
/* 1210 */     headerValues.add(headerValue);
/* 1211 */     this.headers.put(headerName, headerValues);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setAll(Map<String, String> values) {
/* 1216 */     for (Map.Entry<String, String> entry : values.entrySet()) {
/* 1217 */       set(entry.getKey(), entry.getValue());
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public Map<String, String> toSingleValueMap() {
/* 1223 */     LinkedHashMap<String, String> singleValueMap = new LinkedHashMap<String, String>(this.headers.size());
/* 1224 */     for (Map.Entry<String, List<String>> entry : this.headers.entrySet()) {
/* 1225 */       singleValueMap.put(entry.getKey(), ((List<String>)entry.getValue()).get(0));
/*      */     }
/* 1227 */     return singleValueMap;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int size() {
/* 1235 */     return this.headers.size();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isEmpty() {
/* 1240 */     return this.headers.isEmpty();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean containsKey(Object key) {
/* 1245 */     return this.headers.containsKey(key);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean containsValue(Object value) {
/* 1250 */     return this.headers.containsValue(value);
/*      */   }
/*      */ 
/*      */   
/*      */   public List<String> get(Object key) {
/* 1255 */     return this.headers.get(key);
/*      */   }
/*      */ 
/*      */   
/*      */   public List<String> put(String key, List<String> value) {
/* 1260 */     return this.headers.put(key, value);
/*      */   }
/*      */ 
/*      */   
/*      */   public List<String> remove(Object key) {
/* 1265 */     return this.headers.remove(key);
/*      */   }
/*      */ 
/*      */   
/*      */   public void putAll(Map<? extends String, ? extends List<String>> map) {
/* 1270 */     this.headers.putAll(map);
/*      */   }
/*      */ 
/*      */   
/*      */   public void clear() {
/* 1275 */     this.headers.clear();
/*      */   }
/*      */ 
/*      */   
/*      */   public Set<String> keySet() {
/* 1280 */     return this.headers.keySet();
/*      */   }
/*      */ 
/*      */   
/*      */   public Collection<List<String>> values() {
/* 1285 */     return this.headers.values();
/*      */   }
/*      */ 
/*      */   
/*      */   public Set<Map.Entry<String, List<String>>> entrySet() {
/* 1290 */     return this.headers.entrySet();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean equals(Object other) {
/* 1296 */     if (this == other) {
/* 1297 */       return true;
/*      */     }
/* 1299 */     if (!(other instanceof HttpHeaders)) {
/* 1300 */       return false;
/*      */     }
/* 1302 */     HttpHeaders otherHeaders = (HttpHeaders)other;
/* 1303 */     return this.headers.equals(otherHeaders.headers);
/*      */   }
/*      */ 
/*      */   
/*      */   public int hashCode() {
/* 1308 */     return this.headers.hashCode();
/*      */   }
/*      */ 
/*      */   
/*      */   public String toString() {
/* 1313 */     return this.headers.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static HttpHeaders readOnlyHttpHeaders(HttpHeaders headers) {
/* 1321 */     Assert.notNull(headers, "HttpHeaders must not be null");
/* 1322 */     return new HttpHeaders((Map<String, List<String>>)headers, true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static String encodeHeaderFieldParam(String input, Charset charset) {
/* 1333 */     Assert.notNull(input, "Input String should not be null");
/* 1334 */     Assert.notNull(charset, "Charset should not be null");
/* 1335 */     if (charset.name().equals("US-ASCII")) {
/* 1336 */       return input;
/*      */     }
/* 1338 */     Assert.isTrue((charset.name().equals("UTF-8") || charset.name().equals("ISO-8859-1")), "Charset should be UTF-8 or ISO-8859-1");
/*      */     
/* 1340 */     byte[] source = input.getBytes(charset);
/* 1341 */     int len = source.length;
/* 1342 */     StringBuilder sb = new StringBuilder(len << 1);
/* 1343 */     sb.append(charset.name());
/* 1344 */     sb.append("''");
/* 1345 */     for (byte b : source) {
/* 1346 */       if (isRFC5987AttrChar(b)) {
/* 1347 */         sb.append((char)b);
/*      */       } else {
/*      */         
/* 1350 */         sb.append('%');
/* 1351 */         char hex1 = Character.toUpperCase(Character.forDigit(b >> 4 & 0xF, 16));
/* 1352 */         char hex2 = Character.toUpperCase(Character.forDigit(b & 0xF, 16));
/* 1353 */         sb.append(hex1);
/* 1354 */         sb.append(hex2);
/*      */       } 
/*      */     } 
/* 1357 */     return sb.toString();
/*      */   }
/*      */   
/*      */   private static boolean isRFC5987AttrChar(byte c) {
/* 1361 */     return ((c >= 48 && c <= 57) || (c >= 97 && c <= 122) || (c >= 65 && c <= 90) || c == 33 || c == 35 || c == 36 || c == 38 || c == 43 || c == 45 || c == 46 || c == 94 || c == 95 || c == 96 || c == 124 || c == 126);
/*      */   }
/*      */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\http\HttpHeaders.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */