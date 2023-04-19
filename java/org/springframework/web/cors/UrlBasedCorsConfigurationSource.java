/*     */ package org.springframework.web.cors;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.springframework.util.AntPathMatcher;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.PathMatcher;
/*     */ import org.springframework.web.util.UrlPathHelper;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class UrlBasedCorsConfigurationSource
/*     */   implements CorsConfigurationSource
/*     */ {
/*  41 */   private final Map<String, CorsConfiguration> corsConfigurations = new LinkedHashMap<String, CorsConfiguration>();
/*     */   
/*  43 */   private PathMatcher pathMatcher = (PathMatcher)new AntPathMatcher();
/*     */   
/*  45 */   private UrlPathHelper urlPathHelper = new UrlPathHelper();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPathMatcher(PathMatcher pathMatcher) {
/*  54 */     Assert.notNull(pathMatcher, "PathMatcher must not be null");
/*  55 */     this.pathMatcher = pathMatcher;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAlwaysUseFullPath(boolean alwaysUseFullPath) {
/*  66 */     this.urlPathHelper.setAlwaysUseFullPath(alwaysUseFullPath);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUrlDecode(boolean urlDecode) {
/*  77 */     this.urlPathHelper.setUrlDecode(urlDecode);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRemoveSemicolonContent(boolean removeSemicolonContent) {
/*  86 */     this.urlPathHelper.setRemoveSemicolonContent(removeSemicolonContent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUrlPathHelper(UrlPathHelper urlPathHelper) {
/*  94 */     Assert.notNull(urlPathHelper, "UrlPathHelper must not be null");
/*  95 */     this.urlPathHelper = urlPathHelper;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCorsConfigurations(Map<String, CorsConfiguration> corsConfigurations) {
/* 102 */     this.corsConfigurations.clear();
/* 103 */     if (corsConfigurations != null) {
/* 104 */       this.corsConfigurations.putAll(corsConfigurations);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, CorsConfiguration> getCorsConfigurations() {
/* 112 */     return Collections.unmodifiableMap(this.corsConfigurations);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerCorsConfiguration(String path, CorsConfiguration config) {
/* 119 */     this.corsConfigurations.put(path, config);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
/* 125 */     String lookupPath = this.urlPathHelper.getLookupPathForRequest(request);
/* 126 */     for (Map.Entry<String, CorsConfiguration> entry : this.corsConfigurations.entrySet()) {
/* 127 */       if (this.pathMatcher.match(entry.getKey(), lookupPath)) {
/* 128 */         return entry.getValue();
/*     */       }
/*     */     } 
/* 131 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\cors\UrlBasedCorsConfigurationSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */