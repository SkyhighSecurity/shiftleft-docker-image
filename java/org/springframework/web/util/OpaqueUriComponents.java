/*     */ package org.springframework.web.util;
/*     */ 
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import org.springframework.util.LinkedMultiValueMap;
/*     */ import org.springframework.util.MultiValueMap;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class OpaqueUriComponents
/*     */   extends UriComponents
/*     */ {
/*  40 */   private static final MultiValueMap<String, String> QUERY_PARAMS_NONE = (MultiValueMap<String, String>)new LinkedMultiValueMap(0);
/*     */   
/*     */   private final String ssp;
/*     */ 
/*     */   
/*     */   OpaqueUriComponents(String scheme, String schemeSpecificPart, String fragment) {
/*  46 */     super(scheme, fragment);
/*  47 */     this.ssp = schemeSpecificPart;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSchemeSpecificPart() {
/*  53 */     return this.ssp;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getUserInfo() {
/*  58 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getHost() {
/*  63 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getPort() {
/*  68 */     return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getPath() {
/*  73 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<String> getPathSegments() {
/*  78 */     return Collections.emptyList();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getQuery() {
/*  83 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public MultiValueMap<String, String> getQueryParams() {
/*  88 */     return QUERY_PARAMS_NONE;
/*     */   }
/*     */ 
/*     */   
/*     */   public UriComponents encode(String encoding) throws UnsupportedEncodingException {
/*  93 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   protected UriComponents expandInternal(UriComponents.UriTemplateVariables uriVariables) {
/*  98 */     String expandedScheme = expandUriComponent(getScheme(), uriVariables);
/*  99 */     String expandedSsp = expandUriComponent(getSchemeSpecificPart(), uriVariables);
/* 100 */     String expandedFragment = expandUriComponent(getFragment(), uriVariables);
/* 101 */     return new OpaqueUriComponents(expandedScheme, expandedSsp, expandedFragment);
/*     */   }
/*     */ 
/*     */   
/*     */   public UriComponents normalize() {
/* 106 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toUriString() {
/* 111 */     StringBuilder uriBuilder = new StringBuilder();
/*     */     
/* 113 */     if (getScheme() != null) {
/* 114 */       uriBuilder.append(getScheme());
/* 115 */       uriBuilder.append(':');
/*     */     } 
/* 117 */     if (this.ssp != null) {
/* 118 */       uriBuilder.append(this.ssp);
/*     */     }
/* 120 */     if (getFragment() != null) {
/* 121 */       uriBuilder.append('#');
/* 122 */       uriBuilder.append(getFragment());
/*     */     } 
/*     */     
/* 125 */     return uriBuilder.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public URI toUri() {
/*     */     try {
/* 131 */       return new URI(getScheme(), this.ssp, getFragment());
/*     */     }
/* 133 */     catch (URISyntaxException ex) {
/* 134 */       throw new IllegalStateException("Could not create URI object: " + ex.getMessage(), ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void copyToUriComponentsBuilder(UriComponentsBuilder builder) {
/* 140 */     builder.scheme(getScheme());
/* 141 */     builder.schemeSpecificPart(getSchemeSpecificPart());
/* 142 */     builder.fragment(getFragment());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 148 */     if (this == obj) {
/* 149 */       return true;
/*     */     }
/* 151 */     if (!(obj instanceof OpaqueUriComponents)) {
/* 152 */       return false;
/*     */     }
/*     */     
/* 155 */     OpaqueUriComponents other = (OpaqueUriComponents)obj;
/* 156 */     return (ObjectUtils.nullSafeEquals(getScheme(), other.getScheme()) && 
/* 157 */       ObjectUtils.nullSafeEquals(this.ssp, other.ssp) && 
/* 158 */       ObjectUtils.nullSafeEquals(getFragment(), other.getFragment()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 164 */     int result = ObjectUtils.nullSafeHashCode(getScheme());
/* 165 */     result = 31 * result + ObjectUtils.nullSafeHashCode(this.ssp);
/* 166 */     result = 31 * result + ObjectUtils.nullSafeHashCode(getFragment());
/* 167 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\we\\util\OpaqueUriComponents.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */