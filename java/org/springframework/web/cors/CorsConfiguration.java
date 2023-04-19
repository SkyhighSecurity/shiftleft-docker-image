/*     */ package org.springframework.web.cors;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.springframework.http.HttpMethod;
/*     */ import org.springframework.util.CollectionUtils;
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
/*     */ public class CorsConfiguration
/*     */ {
/*     */   public static final String ALL = "*";
/*     */   private static final List<HttpMethod> DEFAULT_METHODS;
/*     */   private List<String> allowedOrigins;
/*     */   private List<String> allowedMethods;
/*     */   
/*     */   static {
/*  60 */     List<HttpMethod> rawMethods = new ArrayList<HttpMethod>(2);
/*  61 */     rawMethods.add(HttpMethod.GET);
/*  62 */     rawMethods.add(HttpMethod.HEAD);
/*  63 */     DEFAULT_METHODS = Collections.unmodifiableList(rawMethods);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  71 */   private List<HttpMethod> resolvedMethods = DEFAULT_METHODS;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<String> allowedHeaders;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<String> exposedHeaders;
/*     */ 
/*     */ 
/*     */   
/*     */   private Boolean allowCredentials;
/*     */ 
/*     */ 
/*     */   
/*     */   private Long maxAge;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CorsConfiguration(CorsConfiguration other) {
/*  95 */     this.allowedOrigins = other.allowedOrigins;
/*  96 */     this.allowedMethods = other.allowedMethods;
/*  97 */     this.resolvedMethods = other.resolvedMethods;
/*  98 */     this.allowedHeaders = other.allowedHeaders;
/*  99 */     this.exposedHeaders = other.exposedHeaders;
/* 100 */     this.allowCredentials = other.allowCredentials;
/* 101 */     this.maxAge = other.maxAge;
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
/*     */   public void setAllowedOrigins(List<String> allowedOrigins) {
/* 118 */     this.allowedOrigins = (allowedOrigins != null) ? new ArrayList<String>(allowedOrigins) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<String> getAllowedOrigins() {
/* 127 */     return this.allowedOrigins;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAllowedOrigin(String origin) {
/* 134 */     if (this.allowedOrigins == null) {
/* 135 */       this.allowedOrigins = new ArrayList<String>(4);
/*     */     }
/* 137 */     this.allowedOrigins.add(origin);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAllowedMethods(List<String> allowedMethods) {
/* 148 */     this.allowedMethods = (allowedMethods != null) ? new ArrayList<String>(allowedMethods) : null;
/* 149 */     if (!CollectionUtils.isEmpty(allowedMethods)) {
/* 150 */       this.resolvedMethods = new ArrayList<HttpMethod>(allowedMethods.size());
/* 151 */       for (String method : allowedMethods) {
/* 152 */         if ("*".equals(method)) {
/* 153 */           this.resolvedMethods = null;
/*     */           break;
/*     */         } 
/* 156 */         this.resolvedMethods.add(HttpMethod.resolve(method));
/*     */       } 
/*     */     } else {
/*     */       
/* 160 */       this.resolvedMethods = DEFAULT_METHODS;
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
/*     */   public List<String> getAllowedMethods() {
/* 172 */     return this.allowedMethods;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAllowedMethod(HttpMethod method) {
/* 179 */     if (method != null) {
/* 180 */       addAllowedMethod(method.name());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAllowedMethod(String method) {
/* 188 */     if (StringUtils.hasText(method)) {
/* 189 */       if (this.allowedMethods == null) {
/* 190 */         this.allowedMethods = new ArrayList<String>(4);
/* 191 */         this.resolvedMethods = new ArrayList<HttpMethod>(4);
/*     */       } 
/* 193 */       this.allowedMethods.add(method);
/* 194 */       if ("*".equals(method)) {
/* 195 */         this.resolvedMethods = null;
/*     */       }
/* 197 */       else if (this.resolvedMethods != null) {
/* 198 */         this.resolvedMethods.add(HttpMethod.resolve(method));
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
/*     */   public void setAllowedHeaders(List<String> allowedHeaders) {
/* 214 */     this.allowedHeaders = (allowedHeaders != null) ? new ArrayList<String>(allowedHeaders) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<String> getAllowedHeaders() {
/* 223 */     return this.allowedHeaders;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAllowedHeader(String allowedHeader) {
/* 230 */     if (this.allowedHeaders == null) {
/* 231 */       this.allowedHeaders = new ArrayList<String>(4);
/*     */     }
/* 233 */     this.allowedHeaders.add(allowedHeader);
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
/*     */   public void setExposedHeaders(List<String> exposedHeaders) {
/* 245 */     if (exposedHeaders != null && exposedHeaders.contains("*")) {
/* 246 */       throw new IllegalArgumentException("'*' is not a valid exposed header value");
/*     */     }
/* 248 */     this.exposedHeaders = (exposedHeaders != null) ? new ArrayList<String>(exposedHeaders) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<String> getExposedHeaders() {
/* 257 */     return this.exposedHeaders;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addExposedHeader(String exposedHeader) {
/* 265 */     if ("*".equals(exposedHeader)) {
/* 266 */       throw new IllegalArgumentException("'*' is not a valid exposed header value");
/*     */     }
/* 268 */     if (this.exposedHeaders == null) {
/* 269 */       this.exposedHeaders = new ArrayList<String>(4);
/*     */     }
/* 271 */     this.exposedHeaders.add(exposedHeader);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAllowCredentials(Boolean allowCredentials) {
/* 279 */     this.allowCredentials = allowCredentials;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Boolean getAllowCredentials() {
/* 287 */     return this.allowCredentials;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxAge(Long maxAge) {
/* 296 */     this.maxAge = maxAge;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Long getMaxAge() {
/* 304 */     return this.maxAge;
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
/*     */   public CorsConfiguration applyPermitDefaultValues() {
/* 327 */     if (this.allowedOrigins == null) {
/* 328 */       addAllowedOrigin("*");
/*     */     }
/* 330 */     if (this.allowedMethods == null) {
/* 331 */       setAllowedMethods(Arrays.asList(new String[] { HttpMethod.GET
/* 332 */               .name(), HttpMethod.HEAD.name(), HttpMethod.POST.name() }));
/*     */     }
/* 334 */     if (this.allowedHeaders == null) {
/* 335 */       addAllowedHeader("*");
/*     */     }
/* 337 */     if (this.allowCredentials == null) {
/* 338 */       setAllowCredentials(Boolean.valueOf(true));
/*     */     }
/* 340 */     if (this.maxAge == null) {
/* 341 */       setMaxAge(Long.valueOf(1800L));
/*     */     }
/* 343 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CorsConfiguration combine(CorsConfiguration other) {
/* 354 */     if (other == null) {
/* 355 */       return this;
/*     */     }
/* 357 */     CorsConfiguration config = new CorsConfiguration(this);
/* 358 */     config.setAllowedOrigins(combine(getAllowedOrigins(), other.getAllowedOrigins()));
/* 359 */     config.setAllowedMethods(combine(getAllowedMethods(), other.getAllowedMethods()));
/* 360 */     config.setAllowedHeaders(combine(getAllowedHeaders(), other.getAllowedHeaders()));
/* 361 */     config.setExposedHeaders(combine(getExposedHeaders(), other.getExposedHeaders()));
/* 362 */     Boolean allowCredentials = other.getAllowCredentials();
/* 363 */     if (allowCredentials != null) {
/* 364 */       config.setAllowCredentials(allowCredentials);
/*     */     }
/* 366 */     Long maxAge = other.getMaxAge();
/* 367 */     if (maxAge != null) {
/* 368 */       config.setMaxAge(maxAge);
/*     */     }
/* 370 */     return config;
/*     */   }
/*     */   
/*     */   private List<String> combine(List<String> source, List<String> other) {
/* 374 */     if (other == null || other.contains("*")) {
/* 375 */       return source;
/*     */     }
/* 377 */     if (source == null || source.contains("*")) {
/* 378 */       return other;
/*     */     }
/* 380 */     Set<String> combined = new LinkedHashSet<String>(source);
/* 381 */     combined.addAll(other);
/* 382 */     return new ArrayList<String>(combined);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String checkOrigin(String requestOrigin) {
/* 392 */     if (!StringUtils.hasText(requestOrigin)) {
/* 393 */       return null;
/*     */     }
/* 395 */     if (ObjectUtils.isEmpty(this.allowedOrigins)) {
/* 396 */       return null;
/*     */     }
/*     */     
/* 399 */     if (this.allowedOrigins.contains("*")) {
/* 400 */       if (this.allowCredentials != Boolean.TRUE) {
/* 401 */         return "*";
/*     */       }
/*     */       
/* 404 */       return requestOrigin;
/*     */     } 
/*     */     
/* 407 */     for (String allowedOrigin : this.allowedOrigins) {
/* 408 */       if (requestOrigin.equalsIgnoreCase(allowedOrigin)) {
/* 409 */         return requestOrigin;
/*     */       }
/*     */     } 
/*     */     
/* 413 */     return null;
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
/*     */   public List<HttpMethod> checkHttpMethod(HttpMethod requestMethod) {
/* 425 */     if (requestMethod == null) {
/* 426 */       return null;
/*     */     }
/* 428 */     if (this.resolvedMethods == null) {
/* 429 */       return Collections.singletonList(requestMethod);
/*     */     }
/* 431 */     return this.resolvedMethods.contains(requestMethod) ? this.resolvedMethods : null;
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
/*     */   public List<String> checkHeaders(List<String> requestHeaders) {
/* 443 */     if (requestHeaders == null) {
/* 444 */       return null;
/*     */     }
/* 446 */     if (requestHeaders.isEmpty()) {
/* 447 */       return Collections.emptyList();
/*     */     }
/* 449 */     if (ObjectUtils.isEmpty(this.allowedHeaders)) {
/* 450 */       return null;
/*     */     }
/*     */     
/* 453 */     boolean allowAnyHeader = this.allowedHeaders.contains("*");
/* 454 */     List<String> result = new ArrayList<String>(requestHeaders.size());
/* 455 */     for (String requestHeader : requestHeaders) {
/* 456 */       if (StringUtils.hasText(requestHeader)) {
/* 457 */         requestHeader = requestHeader.trim();
/* 458 */         if (allowAnyHeader) {
/* 459 */           result.add(requestHeader);
/*     */           continue;
/*     */         } 
/* 462 */         for (String allowedHeader : this.allowedHeaders) {
/* 463 */           if (requestHeader.equalsIgnoreCase(allowedHeader)) {
/* 464 */             result.add(requestHeader);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 471 */     return result.isEmpty() ? null : result;
/*     */   }
/*     */   
/*     */   public CorsConfiguration() {}
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\cors\CorsConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */