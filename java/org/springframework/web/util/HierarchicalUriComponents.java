/*     */ package org.springframework.web.util;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ final class HierarchicalUriComponents
/*     */   extends UriComponents
/*     */ {
/*     */   private static final char PATH_DELIMITER = '/';
/*     */   private static final String PATH_DELIMITER_STRING = "/";
/*     */   private final String userInfo;
/*     */   private final String host;
/*     */   private final String port;
/*     */   private final PathComponent path;
/*     */   private final MultiValueMap<String, String> queryParams;
/*     */   private final boolean encoded;
/*     */   
/*     */   HierarchicalUriComponents(String scheme, String userInfo, String host, String port, PathComponent path, MultiValueMap<String, String> queryParams, String fragment, boolean encoded, boolean verify) {
/*  85 */     super(scheme, fragment);
/*  86 */     this.userInfo = userInfo;
/*  87 */     this.host = host;
/*  88 */     this.port = port;
/*  89 */     this.path = (path != null) ? path : NULL_PATH_COMPONENT;
/*  90 */     this.queryParams = CollectionUtils.unmodifiableMultiValueMap((queryParams != null) ? queryParams : (MultiValueMap)new LinkedMultiValueMap(0));
/*     */     
/*  92 */     this.encoded = encoded;
/*     */     
/*  94 */     if (verify) {
/*  95 */       verify();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSchemeSpecificPart() {
/* 104 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getUserInfo() {
/* 109 */     return this.userInfo;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getHost() {
/* 114 */     return this.host;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getPort() {
/* 119 */     if (this.port == null) {
/* 120 */       return -1;
/*     */     }
/* 122 */     if (this.port.contains("{")) {
/* 123 */       throw new IllegalStateException("The port contains a URI variable but has not been expanded yet: " + this.port);
/*     */     }
/*     */     
/* 126 */     return Integer.parseInt(this.port);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getPath() {
/* 131 */     return this.path.getPath();
/*     */   }
/*     */ 
/*     */   
/*     */   public List<String> getPathSegments() {
/* 136 */     return this.path.getPathSegments();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getQuery() {
/* 141 */     if (!this.queryParams.isEmpty()) {
/* 142 */       StringBuilder queryBuilder = new StringBuilder();
/* 143 */       for (Map.Entry<String, List<String>> entry : (Iterable<Map.Entry<String, List<String>>>)this.queryParams.entrySet()) {
/* 144 */         String name = entry.getKey();
/* 145 */         List<String> values = entry.getValue();
/* 146 */         if (CollectionUtils.isEmpty(values)) {
/* 147 */           if (queryBuilder.length() != 0) {
/* 148 */             queryBuilder.append('&');
/*     */           }
/* 150 */           queryBuilder.append(name);
/*     */           continue;
/*     */         } 
/* 153 */         for (String value : values) {
/* 154 */           if (queryBuilder.length() != 0) {
/* 155 */             queryBuilder.append('&');
/*     */           }
/* 157 */           queryBuilder.append(name);
/* 158 */           if (value != null) {
/* 159 */             queryBuilder.append('=').append(value.toString());
/*     */           }
/*     */         } 
/*     */       } 
/*     */       
/* 164 */       return queryBuilder.toString();
/*     */     } 
/*     */     
/* 167 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MultiValueMap<String, String> getQueryParams() {
/* 176 */     return this.queryParams;
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
/*     */   public HierarchicalUriComponents encode(String encoding) throws UnsupportedEncodingException {
/* 191 */     if (this.encoded) {
/* 192 */       return this;
/*     */     }
/* 194 */     Assert.hasLength(encoding, "Encoding must not be empty");
/* 195 */     String schemeTo = encodeUriComponent(getScheme(), encoding, Type.SCHEME);
/* 196 */     String userInfoTo = encodeUriComponent(this.userInfo, encoding, Type.USER_INFO);
/* 197 */     String hostTo = encodeUriComponent(this.host, encoding, getHostType());
/* 198 */     PathComponent pathTo = this.path.encode(encoding);
/* 199 */     MultiValueMap<String, String> paramsTo = encodeQueryParams(encoding);
/* 200 */     String fragmentTo = encodeUriComponent(getFragment(), encoding, Type.FRAGMENT);
/* 201 */     return new HierarchicalUriComponents(schemeTo, userInfoTo, hostTo, this.port, pathTo, paramsTo, fragmentTo, true, false);
/*     */   }
/*     */ 
/*     */   
/*     */   private MultiValueMap<String, String> encodeQueryParams(String encoding) throws UnsupportedEncodingException {
/* 206 */     int size = this.queryParams.size();
/* 207 */     LinkedMultiValueMap linkedMultiValueMap = new LinkedMultiValueMap(size);
/* 208 */     for (Map.Entry<String, List<String>> entry : (Iterable<Map.Entry<String, List<String>>>)this.queryParams.entrySet()) {
/* 209 */       String name = encodeUriComponent(entry.getKey(), encoding, Type.QUERY_PARAM);
/* 210 */       List<String> values = new ArrayList<String>(((List)entry.getValue()).size());
/* 211 */       for (String value : entry.getValue()) {
/* 212 */         values.add(encodeUriComponent(value, encoding, Type.QUERY_PARAM));
/*     */       }
/* 214 */       linkedMultiValueMap.put(name, values);
/*     */     } 
/* 216 */     return (MultiValueMap<String, String>)linkedMultiValueMap;
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
/*     */   static String encodeUriComponent(String source, String encoding, Type type) throws UnsupportedEncodingException {
/* 229 */     if (source == null) {
/* 230 */       return null;
/*     */     }
/* 232 */     Assert.hasLength(encoding, "Encoding must not be empty");
/* 233 */     byte[] bytes = encodeBytes(source.getBytes(encoding), type);
/* 234 */     return new String(bytes, "US-ASCII");
/*     */   }
/*     */   
/*     */   private static byte[] encodeBytes(byte[] source, Type type) {
/* 238 */     Assert.notNull(source, "Source must not be null");
/* 239 */     Assert.notNull(type, "Type must not be null");
/* 240 */     ByteArrayOutputStream bos = new ByteArrayOutputStream(source.length);
/* 241 */     for (byte b : source) {
/* 242 */       if (b < 0) {
/* 243 */         b = (byte)(b + 256);
/*     */       }
/* 245 */       if (type.isAllowed(b)) {
/* 246 */         bos.write(b);
/*     */       } else {
/*     */         
/* 249 */         bos.write(37);
/* 250 */         char hex1 = Character.toUpperCase(Character.forDigit(b >> 4 & 0xF, 16));
/* 251 */         char hex2 = Character.toUpperCase(Character.forDigit(b & 0xF, 16));
/* 252 */         bos.write(hex1);
/* 253 */         bos.write(hex2);
/*     */       } 
/*     */     } 
/* 256 */     return bos.toByteArray();
/*     */   }
/*     */   
/*     */   private Type getHostType() {
/* 260 */     return (this.host != null && this.host.startsWith("[")) ? Type.HOST_IPV6 : Type.HOST_IPV4;
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
/*     */   private void verify() {
/* 272 */     if (!this.encoded) {
/*     */       return;
/*     */     }
/* 275 */     verifyUriComponent(getScheme(), Type.SCHEME);
/* 276 */     verifyUriComponent(this.userInfo, Type.USER_INFO);
/* 277 */     verifyUriComponent(this.host, getHostType());
/* 278 */     this.path.verify();
/* 279 */     for (Map.Entry<String, List<String>> entry : (Iterable<Map.Entry<String, List<String>>>)this.queryParams.entrySet()) {
/* 280 */       verifyUriComponent(entry.getKey(), Type.QUERY_PARAM);
/* 281 */       for (String value : entry.getValue()) {
/* 282 */         verifyUriComponent(value, Type.QUERY_PARAM);
/*     */       }
/*     */     } 
/* 285 */     verifyUriComponent(getFragment(), Type.FRAGMENT);
/*     */   }
/*     */   
/*     */   private static void verifyUriComponent(String source, Type type) {
/* 289 */     if (source == null) {
/*     */       return;
/*     */     }
/* 292 */     int length = source.length();
/* 293 */     for (int i = 0; i < length; i++) {
/* 294 */       char ch = source.charAt(i);
/* 295 */       if (ch == '%') {
/* 296 */         if (i + 2 < length) {
/* 297 */           char hex1 = source.charAt(i + 1);
/* 298 */           char hex2 = source.charAt(i + 2);
/* 299 */           int u = Character.digit(hex1, 16);
/* 300 */           int l = Character.digit(hex2, 16);
/* 301 */           if (u == -1 || l == -1) {
/* 302 */             throw new IllegalArgumentException("Invalid encoded sequence \"" + source
/* 303 */                 .substring(i) + "\"");
/*     */           }
/* 305 */           i += 2;
/*     */         } else {
/*     */           
/* 308 */           throw new IllegalArgumentException("Invalid encoded sequence \"" + source
/* 309 */               .substring(i) + "\"");
/*     */         }
/*     */       
/* 312 */       } else if (!type.isAllowed(ch)) {
/* 313 */         throw new IllegalArgumentException("Invalid character '" + ch + "' for " + type
/* 314 */             .name() + " in \"" + source + "\"");
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected HierarchicalUriComponents expandInternal(UriComponents.UriTemplateVariables uriVariables) {
/* 324 */     Assert.state(!this.encoded, "Cannot expand an already encoded UriComponents object");
/*     */     
/* 326 */     String schemeTo = expandUriComponent(getScheme(), uriVariables);
/* 327 */     String userInfoTo = expandUriComponent(this.userInfo, uriVariables);
/* 328 */     String hostTo = expandUriComponent(this.host, uriVariables);
/* 329 */     String portTo = expandUriComponent(this.port, uriVariables);
/* 330 */     PathComponent pathTo = this.path.expand(uriVariables);
/* 331 */     MultiValueMap<String, String> paramsTo = expandQueryParams(uriVariables);
/* 332 */     String fragmentTo = expandUriComponent(getFragment(), uriVariables);
/*     */     
/* 334 */     return new HierarchicalUriComponents(schemeTo, userInfoTo, hostTo, portTo, pathTo, paramsTo, fragmentTo, false, false);
/*     */   }
/*     */ 
/*     */   
/*     */   private MultiValueMap<String, String> expandQueryParams(UriComponents.UriTemplateVariables variables) {
/* 339 */     int size = this.queryParams.size();
/* 340 */     LinkedMultiValueMap linkedMultiValueMap = new LinkedMultiValueMap(size);
/* 341 */     variables = new QueryUriTemplateVariables(variables);
/* 342 */     for (Map.Entry<String, List<String>> entry : (Iterable<Map.Entry<String, List<String>>>)this.queryParams.entrySet()) {
/* 343 */       String name = expandUriComponent(entry.getKey(), variables);
/* 344 */       List<String> values = new ArrayList<String>(((List)entry.getValue()).size());
/* 345 */       for (String value : entry.getValue()) {
/* 346 */         values.add(expandUriComponent(value, variables));
/*     */       }
/* 348 */       linkedMultiValueMap.put(name, values);
/*     */     } 
/* 350 */     return (MultiValueMap<String, String>)linkedMultiValueMap;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UriComponents normalize() {
/* 359 */     String normalizedPath = StringUtils.cleanPath(getPath());
/* 360 */     return new HierarchicalUriComponents(getScheme(), this.userInfo, this.host, this.port, new FullPathComponent(normalizedPath), this.queryParams, 
/*     */         
/* 362 */         getFragment(), this.encoded, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toUriString() {
/* 373 */     StringBuilder uriBuilder = new StringBuilder();
/* 374 */     if (getScheme() != null) {
/* 375 */       uriBuilder.append(getScheme());
/* 376 */       uriBuilder.append(':');
/*     */     } 
/* 378 */     if (this.userInfo != null || this.host != null) {
/* 379 */       uriBuilder.append("//");
/* 380 */       if (this.userInfo != null) {
/* 381 */         uriBuilder.append(this.userInfo);
/* 382 */         uriBuilder.append('@');
/*     */       } 
/* 384 */       if (this.host != null) {
/* 385 */         uriBuilder.append(this.host);
/*     */       }
/* 387 */       if (getPort() != -1) {
/* 388 */         uriBuilder.append(':');
/* 389 */         uriBuilder.append(this.port);
/*     */       } 
/*     */     } 
/* 392 */     String path = getPath();
/* 393 */     if (StringUtils.hasLength(path)) {
/* 394 */       if (uriBuilder.length() != 0 && path.charAt(0) != '/') {
/* 395 */         uriBuilder.append('/');
/*     */       }
/* 397 */       uriBuilder.append(path);
/*     */     } 
/* 399 */     String query = getQuery();
/* 400 */     if (query != null) {
/* 401 */       uriBuilder.append('?');
/* 402 */       uriBuilder.append(query);
/*     */     } 
/* 404 */     if (getFragment() != null) {
/* 405 */       uriBuilder.append('#');
/* 406 */       uriBuilder.append(getFragment());
/*     */     } 
/* 408 */     return uriBuilder.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URI toUri() {
/*     */     try {
/* 417 */       if (this.encoded) {
/* 418 */         return new URI(toString());
/*     */       }
/*     */       
/* 421 */       String path = getPath();
/* 422 */       if (StringUtils.hasLength(path) && path.charAt(0) != '/')
/*     */       {
/* 424 */         if (getScheme() != null || getUserInfo() != null || getHost() != null || getPort() != -1) {
/* 425 */           path = '/' + path;
/*     */         }
/*     */       }
/* 428 */       return new URI(getScheme(), getUserInfo(), getHost(), getPort(), path, getQuery(), 
/* 429 */           getFragment());
/*     */     
/*     */     }
/* 432 */     catch (URISyntaxException ex) {
/* 433 */       throw new IllegalStateException("Could not create URI object: " + ex.getMessage(), ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void copyToUriComponentsBuilder(UriComponentsBuilder builder) {
/* 439 */     builder.scheme(getScheme());
/* 440 */     builder.userInfo(getUserInfo());
/* 441 */     builder.host(getHost());
/* 442 */     builder.port(getPort());
/* 443 */     builder.replacePath("");
/* 444 */     this.path.copyToUriComponentsBuilder(builder);
/* 445 */     builder.replaceQueryParams(getQueryParams());
/* 446 */     builder.fragment(getFragment());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 452 */     if (this == obj) {
/* 453 */       return true;
/*     */     }
/* 455 */     if (!(obj instanceof HierarchicalUriComponents)) {
/* 456 */       return false;
/*     */     }
/* 458 */     HierarchicalUriComponents other = (HierarchicalUriComponents)obj;
/* 459 */     return (ObjectUtils.nullSafeEquals(getScheme(), other.getScheme()) && 
/* 460 */       ObjectUtils.nullSafeEquals(getUserInfo(), other.getUserInfo()) && 
/* 461 */       ObjectUtils.nullSafeEquals(getHost(), other.getHost()) && 
/* 462 */       getPort() == other.getPort() && this.path
/* 463 */       .equals(other.path) && this.queryParams
/* 464 */       .equals(other.queryParams) && 
/* 465 */       ObjectUtils.nullSafeEquals(getFragment(), other.getFragment()));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 470 */     int result = ObjectUtils.nullSafeHashCode(getScheme());
/* 471 */     result = 31 * result + ObjectUtils.nullSafeHashCode(this.userInfo);
/* 472 */     result = 31 * result + ObjectUtils.nullSafeHashCode(this.host);
/* 473 */     result = 31 * result + ObjectUtils.nullSafeHashCode(this.port);
/* 474 */     result = 31 * result + this.path.hashCode();
/* 475 */     result = 31 * result + this.queryParams.hashCode();
/* 476 */     result = 31 * result + ObjectUtils.nullSafeHashCode(getFragment());
/* 477 */     return result;
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
/*     */   enum Type
/*     */   {
/* 490 */     SCHEME
/*     */     {
/*     */       public boolean isAllowed(int c) {
/* 493 */         return (isAlpha(c) || isDigit(c) || 43 == c || 45 == c || 46 == c);
/*     */       }
/*     */     },
/* 496 */     AUTHORITY
/*     */     {
/*     */       public boolean isAllowed(int c) {
/* 499 */         return (isUnreserved(c) || isSubDelimiter(c) || 58 == c || 64 == c);
/*     */       }
/*     */     },
/* 502 */     USER_INFO
/*     */     {
/*     */       public boolean isAllowed(int c) {
/* 505 */         return (isUnreserved(c) || isSubDelimiter(c) || 58 == c);
/*     */       }
/*     */     },
/* 508 */     HOST_IPV4
/*     */     {
/*     */       public boolean isAllowed(int c) {
/* 511 */         return (isUnreserved(c) || isSubDelimiter(c));
/*     */       }
/*     */     },
/* 514 */     HOST_IPV6
/*     */     {
/*     */       public boolean isAllowed(int c) {
/* 517 */         return (isUnreserved(c) || isSubDelimiter(c) || 91 == c || 93 == c || 58 == c);
/*     */       }
/*     */     },
/* 520 */     PORT
/*     */     {
/*     */       public boolean isAllowed(int c) {
/* 523 */         return isDigit(c);
/*     */       }
/*     */     },
/* 526 */     PATH
/*     */     {
/*     */       public boolean isAllowed(int c) {
/* 529 */         return (isPchar(c) || 47 == c);
/*     */       }
/*     */     },
/* 532 */     PATH_SEGMENT
/*     */     {
/*     */       public boolean isAllowed(int c) {
/* 535 */         return isPchar(c);
/*     */       }
/*     */     },
/* 538 */     QUERY
/*     */     {
/*     */       public boolean isAllowed(int c) {
/* 541 */         return (isPchar(c) || 47 == c || 63 == c);
/*     */       }
/*     */     },
/* 544 */     QUERY_PARAM
/*     */     {
/*     */       public boolean isAllowed(int c) {
/* 547 */         if (61 == c || 43 == c || 38 == c) {
/* 548 */           return false;
/*     */         }
/*     */         
/* 551 */         return (isPchar(c) || 47 == c || 63 == c);
/*     */       }
/*     */     },
/*     */     
/* 555 */     FRAGMENT
/*     */     {
/*     */       public boolean isAllowed(int c) {
/* 558 */         return (isPchar(c) || 47 == c || 63 == c);
/*     */       }
/*     */     },
/* 561 */     URI
/*     */     {
/*     */       public boolean isAllowed(int c) {
/* 564 */         return isUnreserved(c);
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected boolean isAlpha(int c) {
/* 579 */       return ((c >= 97 && c <= 122) || (c >= 65 && c <= 90));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected boolean isDigit(int c) {
/* 587 */       return (c >= 48 && c <= 57);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected boolean isGenericDelimiter(int c) {
/* 595 */       return (58 == c || 47 == c || 63 == c || 35 == c || 91 == c || 93 == c || 64 == c);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected boolean isSubDelimiter(int c) {
/* 603 */       return (33 == c || 36 == c || 38 == c || 39 == c || 40 == c || 41 == c || 42 == c || 43 == c || 44 == c || 59 == c || 61 == c);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected boolean isReserved(int c) {
/* 612 */       return (isGenericDelimiter(c) || isSubDelimiter(c));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected boolean isUnreserved(int c) {
/* 620 */       return (isAlpha(c) || isDigit(c) || 45 == c || 46 == c || 95 == c || 126 == c);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected boolean isPchar(int c) {
/* 628 */       return (isUnreserved(c) || isSubDelimiter(c) || 58 == c || 64 == c);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public abstract boolean isAllowed(int param1Int);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final class FullPathComponent
/*     */     implements PathComponent
/*     */   {
/*     */     private final String path;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public FullPathComponent(String path) {
/* 660 */       this.path = path;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getPath() {
/* 665 */       return this.path;
/*     */     }
/*     */ 
/*     */     
/*     */     public List<String> getPathSegments() {
/* 670 */       String[] segments = StringUtils.tokenizeToStringArray(getPath(), "/");
/* 671 */       if (segments == null) {
/* 672 */         return Collections.emptyList();
/*     */       }
/* 674 */       return Collections.unmodifiableList(Arrays.asList(segments));
/*     */     }
/*     */ 
/*     */     
/*     */     public HierarchicalUriComponents.PathComponent encode(String encoding) throws UnsupportedEncodingException {
/* 679 */       String encodedPath = HierarchicalUriComponents.encodeUriComponent(getPath(), encoding, HierarchicalUriComponents.Type.PATH);
/* 680 */       return new FullPathComponent(encodedPath);
/*     */     }
/*     */ 
/*     */     
/*     */     public void verify() {
/* 685 */       HierarchicalUriComponents.verifyUriComponent(getPath(), HierarchicalUriComponents.Type.PATH);
/*     */     }
/*     */ 
/*     */     
/*     */     public HierarchicalUriComponents.PathComponent expand(UriComponents.UriTemplateVariables uriVariables) {
/* 690 */       String expandedPath = UriComponents.expandUriComponent(getPath(), uriVariables);
/* 691 */       return new FullPathComponent(expandedPath);
/*     */     }
/*     */ 
/*     */     
/*     */     public void copyToUriComponentsBuilder(UriComponentsBuilder builder) {
/* 696 */       builder.path(getPath());
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 701 */       return (this == obj || (obj instanceof FullPathComponent && 
/* 702 */         ObjectUtils.nullSafeEquals(getPath(), ((FullPathComponent)obj).getPath())));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 707 */       return ObjectUtils.nullSafeHashCode(getPath());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static final class PathSegmentComponent
/*     */     implements PathComponent
/*     */   {
/*     */     private final List<String> pathSegments;
/*     */ 
/*     */     
/*     */     public PathSegmentComponent(List<String> pathSegments) {
/* 720 */       Assert.notNull(pathSegments, "List must not be null");
/* 721 */       this.pathSegments = Collections.unmodifiableList(new ArrayList<String>(pathSegments));
/*     */     }
/*     */ 
/*     */     
/*     */     public String getPath() {
/* 726 */       StringBuilder pathBuilder = new StringBuilder();
/* 727 */       pathBuilder.append('/');
/* 728 */       for (Iterator<String> iterator = this.pathSegments.iterator(); iterator.hasNext(); ) {
/* 729 */         String pathSegment = iterator.next();
/* 730 */         pathBuilder.append(pathSegment);
/* 731 */         if (iterator.hasNext()) {
/* 732 */           pathBuilder.append('/');
/*     */         }
/*     */       } 
/* 735 */       return pathBuilder.toString();
/*     */     }
/*     */ 
/*     */     
/*     */     public List<String> getPathSegments() {
/* 740 */       return this.pathSegments;
/*     */     }
/*     */ 
/*     */     
/*     */     public HierarchicalUriComponents.PathComponent encode(String encoding) throws UnsupportedEncodingException {
/* 745 */       List<String> pathSegments = getPathSegments();
/* 746 */       List<String> encodedPathSegments = new ArrayList<String>(pathSegments.size());
/* 747 */       for (String pathSegment : pathSegments) {
/* 748 */         String encodedPathSegment = HierarchicalUriComponents.encodeUriComponent(pathSegment, encoding, HierarchicalUriComponents.Type.PATH_SEGMENT);
/* 749 */         encodedPathSegments.add(encodedPathSegment);
/*     */       } 
/* 751 */       return new PathSegmentComponent(encodedPathSegments);
/*     */     }
/*     */ 
/*     */     
/*     */     public void verify() {
/* 756 */       for (String pathSegment : getPathSegments()) {
/* 757 */         HierarchicalUriComponents.verifyUriComponent(pathSegment, HierarchicalUriComponents.Type.PATH_SEGMENT);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public HierarchicalUriComponents.PathComponent expand(UriComponents.UriTemplateVariables uriVariables) {
/* 763 */       List<String> pathSegments = getPathSegments();
/* 764 */       List<String> expandedPathSegments = new ArrayList<String>(pathSegments.size());
/* 765 */       for (String pathSegment : pathSegments) {
/* 766 */         String expandedPathSegment = UriComponents.expandUriComponent(pathSegment, uriVariables);
/* 767 */         expandedPathSegments.add(expandedPathSegment);
/*     */       } 
/* 769 */       return new PathSegmentComponent(expandedPathSegments);
/*     */     }
/*     */ 
/*     */     
/*     */     public void copyToUriComponentsBuilder(UriComponentsBuilder builder) {
/* 774 */       builder.pathSegment(StringUtils.toStringArray(getPathSegments()));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 779 */       return (this == obj || (obj instanceof PathSegmentComponent && 
/* 780 */         getPathSegments().equals(((PathSegmentComponent)obj).getPathSegments())));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 785 */       return getPathSegments().hashCode();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static final class PathComponentComposite
/*     */     implements PathComponent
/*     */   {
/*     */     private final List<HierarchicalUriComponents.PathComponent> pathComponents;
/*     */ 
/*     */     
/*     */     public PathComponentComposite(List<HierarchicalUriComponents.PathComponent> pathComponents) {
/* 798 */       Assert.notNull(pathComponents, "PathComponent List must not be null");
/* 799 */       this.pathComponents = pathComponents;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getPath() {
/* 804 */       StringBuilder pathBuilder = new StringBuilder();
/* 805 */       for (HierarchicalUriComponents.PathComponent pathComponent : this.pathComponents) {
/* 806 */         pathBuilder.append(pathComponent.getPath());
/*     */       }
/* 808 */       return pathBuilder.toString();
/*     */     }
/*     */ 
/*     */     
/*     */     public List<String> getPathSegments() {
/* 813 */       List<String> result = new ArrayList<String>();
/* 814 */       for (HierarchicalUriComponents.PathComponent pathComponent : this.pathComponents) {
/* 815 */         result.addAll(pathComponent.getPathSegments());
/*     */       }
/* 817 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public HierarchicalUriComponents.PathComponent encode(String encoding) throws UnsupportedEncodingException {
/* 822 */       List<HierarchicalUriComponents.PathComponent> encodedComponents = new ArrayList<HierarchicalUriComponents.PathComponent>(this.pathComponents.size());
/* 823 */       for (HierarchicalUriComponents.PathComponent pathComponent : this.pathComponents) {
/* 824 */         encodedComponents.add(pathComponent.encode(encoding));
/*     */       }
/* 826 */       return new PathComponentComposite(encodedComponents);
/*     */     }
/*     */ 
/*     */     
/*     */     public void verify() {
/* 831 */       for (HierarchicalUriComponents.PathComponent pathComponent : this.pathComponents) {
/* 832 */         pathComponent.verify();
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public HierarchicalUriComponents.PathComponent expand(UriComponents.UriTemplateVariables uriVariables) {
/* 838 */       List<HierarchicalUriComponents.PathComponent> expandedComponents = new ArrayList<HierarchicalUriComponents.PathComponent>(this.pathComponents.size());
/* 839 */       for (HierarchicalUriComponents.PathComponent pathComponent : this.pathComponents) {
/* 840 */         expandedComponents.add(pathComponent.expand(uriVariables));
/*     */       }
/* 842 */       return new PathComponentComposite(expandedComponents);
/*     */     }
/*     */ 
/*     */     
/*     */     public void copyToUriComponentsBuilder(UriComponentsBuilder builder) {
/* 847 */       for (HierarchicalUriComponents.PathComponent pathComponent : this.pathComponents) {
/* 848 */         pathComponent.copyToUriComponentsBuilder(builder);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 857 */   static final PathComponent NULL_PATH_COMPONENT = new PathComponent()
/*     */     {
/*     */       public String getPath() {
/* 860 */         return null;
/*     */       }
/*     */       
/*     */       public List<String> getPathSegments() {
/* 864 */         return Collections.emptyList();
/*     */       }
/*     */       
/*     */       public HierarchicalUriComponents.PathComponent encode(String encoding) throws UnsupportedEncodingException {
/* 868 */         return this;
/*     */       }
/*     */ 
/*     */       
/*     */       public void verify() {}
/*     */       
/*     */       public HierarchicalUriComponents.PathComponent expand(UriComponents.UriTemplateVariables uriVariables) {
/* 875 */         return this;
/*     */       }
/*     */ 
/*     */       
/*     */       public void copyToUriComponentsBuilder(UriComponentsBuilder builder) {}
/*     */       
/*     */       public boolean equals(Object obj) {
/* 882 */         return (this == obj);
/*     */       }
/*     */       
/*     */       public int hashCode() {
/* 886 */         return getClass().hashCode();
/*     */       }
/*     */     };
/*     */   
/*     */   private static class QueryUriTemplateVariables
/*     */     implements UriComponents.UriTemplateVariables
/*     */   {
/*     */     private final UriComponents.UriTemplateVariables delegate;
/*     */     
/*     */     public QueryUriTemplateVariables(UriComponents.UriTemplateVariables delegate) {
/* 896 */       this.delegate = delegate;
/*     */     }
/*     */ 
/*     */     
/*     */     public Object getValue(String name) {
/* 901 */       Object value = this.delegate.getValue(name);
/* 902 */       if (ObjectUtils.isArray(value)) {
/* 903 */         value = StringUtils.arrayToCommaDelimitedString(ObjectUtils.toObjectArray(value));
/*     */       }
/* 905 */       return value;
/*     */     }
/*     */   }
/*     */   
/*     */   static interface PathComponent extends Serializable {
/*     */     String getPath();
/*     */     
/*     */     List<String> getPathSegments();
/*     */     
/*     */     PathComponent encode(String param1String) throws UnsupportedEncodingException;
/*     */     
/*     */     void verify();
/*     */     
/*     */     PathComponent expand(UriComponents.UriTemplateVariables param1UriTemplateVariables);
/*     */     
/*     */     void copyToUriComponentsBuilder(UriComponentsBuilder param1UriComponentsBuilder);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\we\\util\HierarchicalUriComponents.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */