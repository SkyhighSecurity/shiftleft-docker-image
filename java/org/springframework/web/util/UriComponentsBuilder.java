/*     */ package org.springframework.web.util;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.util.ArrayList;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpRequest;
/*     */ import org.springframework.util.Assert;
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
/*     */ public class UriComponentsBuilder
/*     */   implements Cloneable
/*     */ {
/*  63 */   private static final Pattern QUERY_PARAM_PATTERN = Pattern.compile("([^&=]+)(=?)([^&]+)?");
/*     */ 
/*     */   
/*     */   private static final String SCHEME_PATTERN = "([^:/?#]+):";
/*     */   
/*     */   private static final String HTTP_PATTERN = "(?i)(http|https):";
/*     */   
/*     */   private static final String USERINFO_PATTERN = "([^@\\[/?#]*)";
/*     */   
/*     */   private static final String HOST_IPV4_PATTERN = "[^\\[/?#:]*";
/*     */   
/*     */   private static final String HOST_IPV6_PATTERN = "\\[[\\p{XDigit}\\:\\.]*[%\\p{Alnum}]*\\]";
/*     */   
/*     */   private static final String HOST_PATTERN = "(\\[[\\p{XDigit}\\:\\.]*[%\\p{Alnum}]*\\]|[^\\[/?#:]*)";
/*     */   
/*     */   private static final String PORT_PATTERN = "(\\d*(?:\\{[^/]+?\\})?)";
/*     */   
/*     */   private static final String PATH_PATTERN = "([^?#]*)";
/*     */   
/*     */   private static final String QUERY_PATTERN = "([^#]*)";
/*     */   
/*     */   private static final String LAST_PATTERN = "(.*)";
/*     */   
/*  86 */   private static final Pattern URI_PATTERN = Pattern.compile("^(([^:/?#]+):)?(//(([^@\\[/?#]*)@)?(\\[[\\p{XDigit}\\:\\.]*[%\\p{Alnum}]*\\]|[^\\[/?#:]*)(:(\\d*(?:\\{[^/]+?\\})?))?)?([^?#]*)(\\?([^#]*))?(#(.*))?");
/*     */ 
/*     */ 
/*     */   
/*  90 */   private static final Pattern HTTP_URL_PATTERN = Pattern.compile("^(?i)(http|https):(//(([^@\\[/?#]*)@)?(\\[[\\p{XDigit}\\:\\.]*[%\\p{Alnum}]*\\]|[^\\[/?#:]*)(:(\\d*(?:\\{[^/]+?\\})?))?)?([^?#]*)(\\?(.*))?");
/*     */ 
/*     */ 
/*     */   
/*  94 */   private static final Pattern FORWARDED_HOST_PATTERN = Pattern.compile("host=\"?([^;,\"]+)\"?");
/*     */   
/*  96 */   private static final Pattern FORWARDED_PROTO_PATTERN = Pattern.compile("proto=\"?([^;,\"]+)\"?");
/*     */ 
/*     */   
/*     */   private String scheme;
/*     */   
/*     */   private String ssp;
/*     */   
/*     */   private String userInfo;
/*     */   
/*     */   private String host;
/*     */   
/*     */   private String port;
/*     */   
/*     */   private CompositePathComponentBuilder pathBuilder;
/*     */   
/* 111 */   private final MultiValueMap<String, String> queryParams = (MultiValueMap<String, String>)new LinkedMultiValueMap();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String fragment;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected UriComponentsBuilder() {
/* 123 */     this.pathBuilder = new CompositePathComponentBuilder();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected UriComponentsBuilder(UriComponentsBuilder other) {
/* 132 */     this.scheme = other.scheme;
/* 133 */     this.ssp = other.ssp;
/* 134 */     this.userInfo = other.userInfo;
/* 135 */     this.host = other.host;
/* 136 */     this.port = other.port;
/* 137 */     this.pathBuilder = other.pathBuilder.cloneBuilder();
/* 138 */     this.queryParams.putAll((Map)other.queryParams);
/* 139 */     this.fragment = other.fragment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static UriComponentsBuilder newInstance() {
/* 150 */     return new UriComponentsBuilder();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static UriComponentsBuilder fromPath(String path) {
/* 159 */     UriComponentsBuilder builder = new UriComponentsBuilder();
/* 160 */     builder.path(path);
/* 161 */     return builder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static UriComponentsBuilder fromUri(URI uri) {
/* 170 */     UriComponentsBuilder builder = new UriComponentsBuilder();
/* 171 */     builder.uri(uri);
/* 172 */     return builder;
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
/*     */   public static UriComponentsBuilder fromUriString(String uri) {
/* 190 */     Assert.notNull(uri, "URI must not be null");
/* 191 */     Matcher matcher = URI_PATTERN.matcher(uri);
/* 192 */     if (matcher.matches()) {
/* 193 */       UriComponentsBuilder builder = new UriComponentsBuilder();
/* 194 */       String scheme = matcher.group(2);
/* 195 */       String userInfo = matcher.group(5);
/* 196 */       String host = matcher.group(6);
/* 197 */       String port = matcher.group(8);
/* 198 */       String path = matcher.group(9);
/* 199 */       String query = matcher.group(11);
/* 200 */       String fragment = matcher.group(13);
/* 201 */       boolean opaque = false;
/* 202 */       if (StringUtils.hasLength(scheme)) {
/* 203 */         String rest = uri.substring(scheme.length());
/* 204 */         if (!rest.startsWith(":/")) {
/* 205 */           opaque = true;
/*     */         }
/*     */       } 
/* 208 */       builder.scheme(scheme);
/* 209 */       if (opaque) {
/* 210 */         String ssp = uri.substring(scheme.length()).substring(1);
/* 211 */         if (StringUtils.hasLength(fragment)) {
/* 212 */           ssp = ssp.substring(0, ssp.length() - fragment.length() + 1);
/*     */         }
/* 214 */         builder.schemeSpecificPart(ssp);
/*     */       } else {
/*     */         
/* 217 */         builder.userInfo(userInfo);
/* 218 */         builder.host(host);
/* 219 */         if (StringUtils.hasLength(port)) {
/* 220 */           builder.port(port);
/*     */         }
/* 222 */         builder.path(path);
/* 223 */         builder.query(query);
/*     */       } 
/* 225 */       if (StringUtils.hasText(fragment)) {
/* 226 */         builder.fragment(fragment);
/*     */       }
/* 228 */       return builder;
/*     */     } 
/*     */     
/* 231 */     throw new IllegalArgumentException("[" + uri + "] is not a valid URI");
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
/*     */   public static UriComponentsBuilder fromHttpUrl(String httpUrl) {
/* 250 */     Assert.notNull(httpUrl, "HTTP URL must not be null");
/* 251 */     Matcher matcher = HTTP_URL_PATTERN.matcher(httpUrl);
/* 252 */     if (matcher.matches()) {
/* 253 */       UriComponentsBuilder builder = new UriComponentsBuilder();
/* 254 */       String scheme = matcher.group(1);
/* 255 */       builder.scheme((scheme != null) ? scheme.toLowerCase() : null);
/* 256 */       builder.userInfo(matcher.group(4));
/* 257 */       String host = matcher.group(5);
/* 258 */       if (StringUtils.hasLength(scheme) && !StringUtils.hasLength(host)) {
/* 259 */         throw new IllegalArgumentException("[" + httpUrl + "] is not a valid HTTP URL");
/*     */       }
/* 261 */       builder.host(host);
/* 262 */       String port = matcher.group(7);
/* 263 */       if (StringUtils.hasLength(port)) {
/* 264 */         builder.port(port);
/*     */       }
/* 266 */       builder.path(matcher.group(8));
/* 267 */       builder.query(matcher.group(10));
/* 268 */       return builder;
/*     */     } 
/*     */     
/* 271 */     throw new IllegalArgumentException("[" + httpUrl + "] is not a valid HTTP URL");
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
/*     */   public static UriComponentsBuilder fromHttpRequest(HttpRequest request) {
/* 291 */     return fromUri(request.getURI()).adaptFromForwardedHeaders(request.getHeaders());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static UriComponentsBuilder fromOriginHeader(String origin) {
/* 299 */     Matcher matcher = URI_PATTERN.matcher(origin);
/* 300 */     if (matcher.matches()) {
/* 301 */       UriComponentsBuilder builder = new UriComponentsBuilder();
/* 302 */       String scheme = matcher.group(2);
/* 303 */       String host = matcher.group(6);
/* 304 */       String port = matcher.group(8);
/* 305 */       if (StringUtils.hasLength(scheme)) {
/* 306 */         builder.scheme(scheme);
/*     */       }
/* 308 */       builder.host(host);
/* 309 */       if (StringUtils.hasLength(port)) {
/* 310 */         builder.port(port);
/*     */       }
/* 312 */       return builder;
/*     */     } 
/*     */     
/* 315 */     throw new IllegalArgumentException("[" + origin + "] is not a valid \"Origin\" header value");
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
/*     */   public UriComponents build() {
/* 327 */     return build(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UriComponents build(boolean encoded) {
/* 338 */     if (this.ssp != null) {
/* 339 */       return new OpaqueUriComponents(this.scheme, this.ssp, this.fragment);
/*     */     }
/*     */     
/* 342 */     return new HierarchicalUriComponents(this.scheme, this.userInfo, this.host, this.port, this.pathBuilder
/* 343 */         .build(), this.queryParams, this.fragment, encoded, true);
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
/*     */   public UriComponents buildAndExpand(Map<String, ?> uriVariables) {
/* 355 */     return build(false).expand(uriVariables);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UriComponents buildAndExpand(Object... uriVariableValues) {
/* 366 */     return build(false).expand(uriVariableValues);
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
/* 377 */     return build(false).encode().toUriString();
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
/*     */   public UriComponentsBuilder uri(URI uri) {
/* 389 */     Assert.notNull(uri, "URI must not be null");
/* 390 */     this.scheme = uri.getScheme();
/* 391 */     if (uri.isOpaque()) {
/* 392 */       this.ssp = uri.getRawSchemeSpecificPart();
/* 393 */       resetHierarchicalComponents();
/*     */     } else {
/*     */       
/* 396 */       if (uri.getRawUserInfo() != null) {
/* 397 */         this.userInfo = uri.getRawUserInfo();
/*     */       }
/* 399 */       if (uri.getHost() != null) {
/* 400 */         this.host = uri.getHost();
/*     */       }
/* 402 */       if (uri.getPort() != -1) {
/* 403 */         this.port = String.valueOf(uri.getPort());
/*     */       }
/* 405 */       if (StringUtils.hasLength(uri.getRawPath())) {
/* 406 */         this.pathBuilder = new CompositePathComponentBuilder(uri.getRawPath());
/*     */       }
/* 408 */       if (StringUtils.hasLength(uri.getRawQuery())) {
/* 409 */         this.queryParams.clear();
/* 410 */         query(uri.getRawQuery());
/*     */       } 
/* 412 */       resetSchemeSpecificPart();
/*     */     } 
/* 414 */     if (uri.getRawFragment() != null) {
/* 415 */       this.fragment = uri.getRawFragment();
/*     */     }
/* 417 */     return this;
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
/*     */   public UriComponentsBuilder uriComponents(UriComponents uriComponents) {
/* 430 */     Assert.notNull(uriComponents, "UriComponents must not be null");
/* 431 */     uriComponents.copyToUriComponentsBuilder(this);
/* 432 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UriComponentsBuilder scheme(String scheme) {
/* 442 */     this.scheme = scheme;
/* 443 */     return this;
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
/*     */   public UriComponentsBuilder schemeSpecificPart(String ssp) {
/* 455 */     this.ssp = ssp;
/* 456 */     resetHierarchicalComponents();
/* 457 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UriComponentsBuilder userInfo(String userInfo) {
/* 467 */     this.userInfo = userInfo;
/* 468 */     resetSchemeSpecificPart();
/* 469 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UriComponentsBuilder host(String host) {
/* 479 */     this.host = host;
/* 480 */     resetSchemeSpecificPart();
/* 481 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UriComponentsBuilder port(int port) {
/* 490 */     Assert.isTrue((port >= -1), "Port must be >= -1");
/* 491 */     this.port = String.valueOf(port);
/* 492 */     resetSchemeSpecificPart();
/* 493 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UriComponentsBuilder port(String port) {
/* 504 */     this.port = port;
/* 505 */     resetSchemeSpecificPart();
/* 506 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UriComponentsBuilder path(String path) {
/* 516 */     this.pathBuilder.addPath(path);
/* 517 */     resetSchemeSpecificPart();
/* 518 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UriComponentsBuilder pathSegment(String... pathSegments) throws IllegalArgumentException {
/* 529 */     this.pathBuilder.addPathSegments(pathSegments);
/* 530 */     resetSchemeSpecificPart();
/* 531 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UriComponentsBuilder replacePath(String path) {
/* 540 */     this.pathBuilder = new CompositePathComponentBuilder(path);
/* 541 */     resetSchemeSpecificPart();
/* 542 */     return this;
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
/*     */   public UriComponentsBuilder query(String query) {
/* 562 */     if (query != null) {
/* 563 */       Matcher matcher = QUERY_PARAM_PATTERN.matcher(query);
/* 564 */       while (matcher.find()) {
/* 565 */         String name = matcher.group(1);
/* 566 */         String eq = matcher.group(2);
/* 567 */         String value = matcher.group(3);
/* 568 */         queryParam(name, new Object[] { (value != null) ? value : (StringUtils.hasLength(eq) ? "" : null) });
/*     */       } 
/*     */     } else {
/*     */       
/* 572 */       this.queryParams.clear();
/*     */     } 
/* 574 */     resetSchemeSpecificPart();
/* 575 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UriComponentsBuilder replaceQuery(String query) {
/* 584 */     this.queryParams.clear();
/* 585 */     query(query);
/* 586 */     resetSchemeSpecificPart();
/* 587 */     return this;
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
/*     */   public UriComponentsBuilder queryParam(String name, Object... values) {
/* 600 */     Assert.notNull(name, "Name must not be null");
/* 601 */     if (!ObjectUtils.isEmpty(values)) {
/* 602 */       for (Object value : values) {
/* 603 */         String valueAsString = (value != null) ? value.toString() : null;
/* 604 */         this.queryParams.add(name, valueAsString);
/*     */       } 
/*     */     } else {
/*     */       
/* 608 */       this.queryParams.add(name, null);
/*     */     } 
/* 610 */     resetSchemeSpecificPart();
/* 611 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UriComponentsBuilder queryParams(MultiValueMap<String, String> params) {
/* 621 */     if (params != null) {
/* 622 */       this.queryParams.putAll((Map)params);
/*     */     }
/* 624 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UriComponentsBuilder replaceQueryParam(String name, Object... values) {
/* 635 */     Assert.notNull(name, "Name must not be null");
/* 636 */     this.queryParams.remove(name);
/* 637 */     if (!ObjectUtils.isEmpty(values)) {
/* 638 */       queryParam(name, values);
/*     */     }
/* 640 */     resetSchemeSpecificPart();
/* 641 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UriComponentsBuilder replaceQueryParams(MultiValueMap<String, String> params) {
/* 651 */     this.queryParams.clear();
/* 652 */     if (params != null) {
/* 653 */       this.queryParams.putAll((Map)params);
/*     */     }
/* 655 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UriComponentsBuilder fragment(String fragment) {
/* 665 */     if (fragment != null) {
/* 666 */       Assert.hasLength(fragment, "Fragment must not be empty");
/* 667 */       this.fragment = fragment;
/*     */     } else {
/*     */       
/* 670 */       this.fragment = null;
/*     */     } 
/* 672 */     return this;
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
/*     */   UriComponentsBuilder adaptFromForwardedHeaders(HttpHeaders headers) {
/*     */     try {
/* 686 */       String forwardedHeader = headers.getFirst("Forwarded");
/* 687 */       if (StringUtils.hasText(forwardedHeader)) {
/* 688 */         String forwardedToUse = StringUtils.tokenizeToStringArray(forwardedHeader, ",")[0];
/* 689 */         Matcher matcher = FORWARDED_PROTO_PATTERN.matcher(forwardedToUse);
/* 690 */         if (matcher.find()) {
/* 691 */           scheme(matcher.group(1).trim());
/* 692 */           port((String)null);
/*     */         } 
/* 694 */         matcher = FORWARDED_HOST_PATTERN.matcher(forwardedToUse);
/* 695 */         if (matcher.find()) {
/* 696 */           adaptForwardedHost(matcher.group(1).trim());
/*     */         }
/*     */       } else {
/*     */         
/* 700 */         String protocolHeader = headers.getFirst("X-Forwarded-Proto");
/* 701 */         if (StringUtils.hasText(protocolHeader)) {
/* 702 */           scheme(StringUtils.tokenizeToStringArray(protocolHeader, ",")[0]);
/* 703 */           port((String)null);
/*     */         } 
/*     */         
/* 706 */         String hostHeader = headers.getFirst("X-Forwarded-Host");
/* 707 */         if (StringUtils.hasText(hostHeader)) {
/* 708 */           adaptForwardedHost(StringUtils.tokenizeToStringArray(hostHeader, ",")[0]);
/*     */         }
/*     */         
/* 711 */         String portHeader = headers.getFirst("X-Forwarded-Port");
/* 712 */         if (StringUtils.hasText(portHeader)) {
/* 713 */           port(Integer.parseInt(StringUtils.tokenizeToStringArray(portHeader, ",")[0]));
/*     */         }
/*     */       }
/*     */     
/* 717 */     } catch (NumberFormatException ex) {
/* 718 */       throw new IllegalArgumentException("Failed to parse a port from \"forwarded\"-type headers. If not behind a trusted proxy, consider using ForwardedHeaderFilter with the removeOnly=true. Request headers: " + headers);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 723 */     if (this.scheme != null && ((this.scheme.equals("http") && "80".equals(this.port)) || (this.scheme
/* 724 */       .equals("https") && "443".equals(this.port)))) {
/* 725 */       port((String)null);
/*     */     }
/*     */     
/* 728 */     return this;
/*     */   }
/*     */   
/*     */   private void adaptForwardedHost(String hostToUse) {
/* 732 */     int portSeparatorIdx = hostToUse.lastIndexOf(':');
/* 733 */     if (portSeparatorIdx > hostToUse.lastIndexOf(']')) {
/* 734 */       host(hostToUse.substring(0, portSeparatorIdx));
/* 735 */       port(Integer.parseInt(hostToUse.substring(portSeparatorIdx + 1)));
/*     */     } else {
/*     */       
/* 738 */       host(hostToUse);
/* 739 */       port((String)null);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void resetHierarchicalComponents() {
/* 744 */     this.userInfo = null;
/* 745 */     this.host = null;
/* 746 */     this.port = null;
/* 747 */     this.pathBuilder = new CompositePathComponentBuilder();
/* 748 */     this.queryParams.clear();
/*     */   }
/*     */   
/*     */   private void resetSchemeSpecificPart() {
/* 752 */     this.ssp = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object clone() {
/* 762 */     return cloneBuilder();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UriComponentsBuilder cloneBuilder() {
/* 771 */     return new UriComponentsBuilder(this);
/*     */   }
/*     */ 
/*     */   
/*     */   private static interface PathComponentBuilder
/*     */   {
/*     */     HierarchicalUriComponents.PathComponent build();
/*     */     
/*     */     PathComponentBuilder cloneBuilder();
/*     */   }
/*     */   
/*     */   private static class CompositePathComponentBuilder
/*     */     implements PathComponentBuilder
/*     */   {
/* 785 */     private final LinkedList<UriComponentsBuilder.PathComponentBuilder> builders = new LinkedList<UriComponentsBuilder.PathComponentBuilder>();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public CompositePathComponentBuilder(String path) {
/* 791 */       addPath(path);
/*     */     }
/*     */     
/*     */     public void addPathSegments(String... pathSegments) {
/* 795 */       if (!ObjectUtils.isEmpty((Object[])pathSegments)) {
/* 796 */         UriComponentsBuilder.PathSegmentComponentBuilder psBuilder = getLastBuilder(UriComponentsBuilder.PathSegmentComponentBuilder.class);
/* 797 */         UriComponentsBuilder.FullPathComponentBuilder fpBuilder = getLastBuilder(UriComponentsBuilder.FullPathComponentBuilder.class);
/* 798 */         if (psBuilder == null) {
/* 799 */           psBuilder = new UriComponentsBuilder.PathSegmentComponentBuilder();
/* 800 */           this.builders.add(psBuilder);
/* 801 */           if (fpBuilder != null) {
/* 802 */             fpBuilder.removeTrailingSlash();
/*     */           }
/*     */         } 
/* 805 */         psBuilder.append(pathSegments);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void addPath(String path) {
/* 810 */       if (StringUtils.hasText(path)) {
/* 811 */         UriComponentsBuilder.PathSegmentComponentBuilder psBuilder = getLastBuilder(UriComponentsBuilder.PathSegmentComponentBuilder.class);
/* 812 */         UriComponentsBuilder.FullPathComponentBuilder fpBuilder = getLastBuilder(UriComponentsBuilder.FullPathComponentBuilder.class);
/* 813 */         if (psBuilder != null) {
/* 814 */           path = path.startsWith("/") ? path : ("/" + path);
/*     */         }
/* 816 */         if (fpBuilder == null) {
/* 817 */           fpBuilder = new UriComponentsBuilder.FullPathComponentBuilder();
/* 818 */           this.builders.add(fpBuilder);
/*     */         } 
/* 820 */         fpBuilder.append(path);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     private <T> T getLastBuilder(Class<T> builderClass) {
/* 826 */       if (!this.builders.isEmpty()) {
/* 827 */         UriComponentsBuilder.PathComponentBuilder last = this.builders.getLast();
/* 828 */         if (builderClass.isInstance(last)) {
/* 829 */           return (T)last;
/*     */         }
/*     */       } 
/* 832 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public HierarchicalUriComponents.PathComponent build() {
/* 837 */       int size = this.builders.size();
/* 838 */       List<HierarchicalUriComponents.PathComponent> components = new ArrayList<HierarchicalUriComponents.PathComponent>(size);
/* 839 */       for (UriComponentsBuilder.PathComponentBuilder componentBuilder : this.builders) {
/* 840 */         HierarchicalUriComponents.PathComponent pathComponent = componentBuilder.build();
/* 841 */         if (pathComponent != null) {
/* 842 */           components.add(pathComponent);
/*     */         }
/*     */       } 
/* 845 */       if (components.isEmpty()) {
/* 846 */         return HierarchicalUriComponents.NULL_PATH_COMPONENT;
/*     */       }
/* 848 */       if (components.size() == 1) {
/* 849 */         return components.get(0);
/*     */       }
/* 851 */       return new HierarchicalUriComponents.PathComponentComposite(components);
/*     */     }
/*     */ 
/*     */     
/*     */     public CompositePathComponentBuilder cloneBuilder() {
/* 856 */       CompositePathComponentBuilder compositeBuilder = new CompositePathComponentBuilder();
/* 857 */       for (UriComponentsBuilder.PathComponentBuilder builder : this.builders) {
/* 858 */         compositeBuilder.builders.add(builder.cloneBuilder());
/*     */       }
/* 860 */       return compositeBuilder;
/*     */     }
/*     */     
/*     */     public CompositePathComponentBuilder() {}
/*     */   }
/*     */   
/*     */   private static class FullPathComponentBuilder implements PathComponentBuilder {
/* 867 */     private final StringBuilder path = new StringBuilder();
/*     */     
/*     */     public void append(String path) {
/* 870 */       this.path.append(path);
/*     */     }
/*     */ 
/*     */     
/*     */     public HierarchicalUriComponents.PathComponent build() {
/* 875 */       if (this.path.length() == 0) {
/* 876 */         return null;
/*     */       }
/* 878 */       String path = this.path.toString();
/*     */       while (true) {
/* 880 */         int index = path.indexOf("//");
/* 881 */         if (index == -1) {
/*     */           break;
/*     */         }
/* 884 */         path = path.substring(0, index) + path.substring(index + 1);
/*     */       } 
/* 886 */       return new HierarchicalUriComponents.FullPathComponent(path);
/*     */     }
/*     */     
/*     */     public void removeTrailingSlash() {
/* 890 */       int index = this.path.length() - 1;
/* 891 */       if (this.path.charAt(index) == '/') {
/* 892 */         this.path.deleteCharAt(index);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public FullPathComponentBuilder cloneBuilder() {
/* 898 */       FullPathComponentBuilder builder = new FullPathComponentBuilder();
/* 899 */       builder.append(this.path.toString());
/* 900 */       return builder;
/*     */     }
/*     */     
/*     */     private FullPathComponentBuilder() {}
/*     */   }
/*     */   
/*     */   private static class PathSegmentComponentBuilder implements PathComponentBuilder {
/* 907 */     private final List<String> pathSegments = new LinkedList<String>();
/*     */     
/*     */     public void append(String... pathSegments) {
/* 910 */       for (String pathSegment : pathSegments) {
/* 911 */         if (StringUtils.hasText(pathSegment)) {
/* 912 */           this.pathSegments.add(pathSegment);
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public HierarchicalUriComponents.PathComponent build() {
/* 919 */       return this.pathSegments.isEmpty() ? null : new HierarchicalUriComponents.PathSegmentComponent(this.pathSegments);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public PathSegmentComponentBuilder cloneBuilder() {
/* 925 */       PathSegmentComponentBuilder builder = new PathSegmentComponentBuilder();
/* 926 */       builder.pathSegments.addAll(this.pathSegments);
/* 927 */       return builder;
/*     */     }
/*     */     
/*     */     private PathSegmentComponentBuilder() {}
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\we\\util\UriComponentsBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */