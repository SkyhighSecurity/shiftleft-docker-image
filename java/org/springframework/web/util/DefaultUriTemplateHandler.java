/*     */ package org.springframework.web.util;
/*     */ 
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultUriTemplateHandler
/*     */   extends AbstractUriTemplateHandler
/*     */ {
/*     */   private boolean parsePath;
/*     */   private boolean strictEncoding;
/*     */   
/*     */   public void setParsePath(boolean parsePath) {
/*  57 */     this.parsePath = parsePath;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean shouldParsePath() {
/*  64 */     return this.parsePath;
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
/*     */   public void setStrictEncoding(boolean strictEncoding) {
/*  83 */     this.strictEncoding = strictEncoding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isStrictEncoding() {
/*  90 */     return this.strictEncoding;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected URI expandInternal(String uriTemplate, Map<String, ?> uriVariables) {
/*  96 */     UriComponentsBuilder uriComponentsBuilder = initUriComponentsBuilder(uriTemplate);
/*  97 */     UriComponents uriComponents = expandAndEncode(uriComponentsBuilder, uriVariables);
/*  98 */     return createUri(uriComponents);
/*     */   }
/*     */ 
/*     */   
/*     */   protected URI expandInternal(String uriTemplate, Object... uriVariables) {
/* 103 */     UriComponentsBuilder uriComponentsBuilder = initUriComponentsBuilder(uriTemplate);
/* 104 */     UriComponents uriComponents = expandAndEncode(uriComponentsBuilder, uriVariables);
/* 105 */     return createUri(uriComponents);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected UriComponentsBuilder initUriComponentsBuilder(String uriTemplate) {
/* 114 */     UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(uriTemplate);
/* 115 */     if (shouldParsePath() && !isStrictEncoding()) {
/* 116 */       List<String> pathSegments = builder.build().getPathSegments();
/* 117 */       builder.replacePath(null);
/* 118 */       for (String pathSegment : pathSegments) {
/* 119 */         builder.pathSegment(new String[] { pathSegment });
/*     */       } 
/*     */     } 
/* 122 */     return builder;
/*     */   }
/*     */   
/*     */   protected UriComponents expandAndEncode(UriComponentsBuilder builder, Map<String, ?> uriVariables) {
/* 126 */     if (!isStrictEncoding()) {
/* 127 */       return builder.buildAndExpand(uriVariables).encode();
/*     */     }
/*     */     
/* 130 */     Map<String, Object> encodedUriVars = new HashMap<String, Object>(uriVariables.size());
/* 131 */     for (Map.Entry<String, ?> entry : uriVariables.entrySet()) {
/* 132 */       encodedUriVars.put(entry.getKey(), applyStrictEncoding(entry.getValue()));
/*     */     }
/* 134 */     return builder.buildAndExpand(encodedUriVars);
/*     */   }
/*     */ 
/*     */   
/*     */   protected UriComponents expandAndEncode(UriComponentsBuilder builder, Object[] uriVariables) {
/* 139 */     if (!isStrictEncoding()) {
/* 140 */       return builder.buildAndExpand(uriVariables).encode();
/*     */     }
/*     */     
/* 143 */     Object[] encodedUriVars = new Object[uriVariables.length];
/* 144 */     for (int i = 0; i < uriVariables.length; i++) {
/* 145 */       encodedUriVars[i] = applyStrictEncoding(uriVariables[i]);
/*     */     }
/* 147 */     return builder.buildAndExpand(encodedUriVars);
/*     */   }
/*     */ 
/*     */   
/*     */   private String applyStrictEncoding(Object value) {
/* 152 */     String stringValue = (value != null) ? value.toString() : "";
/*     */     try {
/* 154 */       return UriUtils.encode(stringValue, "UTF-8");
/*     */     }
/* 156 */     catch (UnsupportedEncodingException ex) {
/*     */       
/* 158 */       throw new IllegalStateException("Failed to encode URI variable", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private URI createUri(UriComponents uriComponents) {
/*     */     try {
/* 165 */       return new URI(uriComponents.toUriString());
/*     */     }
/* 167 */     catch (URISyntaxException ex) {
/* 168 */       throw new IllegalStateException("Could not create URI object: " + ex.getMessage(), ex);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\we\\util\DefaultUriTemplateHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */