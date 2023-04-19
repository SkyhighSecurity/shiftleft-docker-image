/*     */ package org.springframework.web.util;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.springframework.util.Assert;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractUriTemplateHandler
/*     */   implements UriTemplateHandler
/*     */ {
/*     */   private String baseUrl;
/*  41 */   private final Map<String, Object> defaultUriVariables = new HashMap<String, Object>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBaseUrl(String baseUrl) {
/*  52 */     if (baseUrl != null) {
/*  53 */       UriComponents uriComponents = UriComponentsBuilder.fromUriString(baseUrl).build();
/*  54 */       Assert.hasText(uriComponents.getScheme(), "'baseUrl' must have a scheme");
/*  55 */       Assert.hasText(uriComponents.getHost(), "'baseUrl' must have a host");
/*  56 */       Assert.isNull(uriComponents.getQuery(), "'baseUrl' cannot have a query");
/*  57 */       Assert.isNull(uriComponents.getFragment(), "'baseUrl' cannot have a fragment");
/*     */     } 
/*  59 */     this.baseUrl = baseUrl;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getBaseUrl() {
/*  66 */     return this.baseUrl;
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
/*     */   public void setDefaultUriVariables(Map<String, ?> defaultUriVariables) {
/*  78 */     this.defaultUriVariables.clear();
/*  79 */     if (defaultUriVariables != null) {
/*  80 */       this.defaultUriVariables.putAll(defaultUriVariables);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, ?> getDefaultUriVariables() {
/*  88 */     return Collections.unmodifiableMap(this.defaultUriVariables);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public URI expand(String uriTemplate, Map<String, ?> uriVariables) {
/*  94 */     if (!getDefaultUriVariables().isEmpty()) {
/*  95 */       Map<String, Object> map = new HashMap<String, Object>();
/*  96 */       map.putAll(getDefaultUriVariables());
/*  97 */       map.putAll(uriVariables);
/*  98 */       uriVariables = map;
/*     */     } 
/* 100 */     URI url = expandInternal(uriTemplate, uriVariables);
/* 101 */     return insertBaseUrl(url);
/*     */   }
/*     */ 
/*     */   
/*     */   public URI expand(String uriTemplate, Object... uriVariables) {
/* 106 */     URI url = expandInternal(uriTemplate, uriVariables);
/* 107 */     return insertBaseUrl(url);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract URI expandInternal(String paramString, Map<String, ?> paramMap);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract URI expandInternal(String paramString, Object... paramVarArgs);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private URI insertBaseUrl(URI url) {
/*     */     try {
/* 127 */       String baseUrl = getBaseUrl();
/* 128 */       if (baseUrl != null && url.getHost() == null) {
/* 129 */         url = new URI(baseUrl + url.toString());
/*     */       }
/* 131 */       return url;
/*     */     }
/* 133 */     catch (URISyntaxException ex) {
/* 134 */       throw new IllegalArgumentException("Invalid URL after inserting base URL: " + url, ex);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\we\\util\AbstractUriTemplateHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */