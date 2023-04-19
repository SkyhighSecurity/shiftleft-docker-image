/*     */ package org.springframework.web.accept;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import javax.servlet.ServletContext;
/*     */ import org.springframework.beans.factory.FactoryBean;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.web.context.ServletContextAware;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ContentNegotiationManagerFactoryBean
/*     */   implements FactoryBean<ContentNegotiationManager>, ServletContextAware, InitializingBean
/*     */ {
/*     */   private boolean favorPathExtension = true;
/*     */   private boolean favorParameter = false;
/*     */   private boolean ignoreAcceptHeader = false;
/*  99 */   private Map<String, MediaType> mediaTypes = new HashMap<String, MediaType>();
/*     */   
/*     */   private boolean ignoreUnknownPathExtensions = true;
/*     */   
/*     */   private Boolean useJaf;
/*     */   
/* 105 */   private String parameterName = "format";
/*     */ 
/*     */ 
/*     */   
/*     */   private ContentNegotiationStrategy defaultNegotiationStrategy;
/*     */ 
/*     */ 
/*     */   
/*     */   private ContentNegotiationManager contentNegotiationManager;
/*     */ 
/*     */ 
/*     */   
/*     */   private ServletContext servletContext;
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFavorPathExtension(boolean favorPathExtension) {
/* 122 */     this.favorPathExtension = favorPathExtension;
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
/*     */   public void setMediaTypes(Properties mediaTypes) {
/* 140 */     if (!CollectionUtils.isEmpty(mediaTypes)) {
/* 141 */       for (Map.Entry<Object, Object> entry : mediaTypes.entrySet()) {
/* 142 */         String extension = ((String)entry.getKey()).toLowerCase(Locale.ENGLISH);
/* 143 */         MediaType mediaType = MediaType.valueOf((String)entry.getValue());
/* 144 */         this.mediaTypes.put(extension, mediaType);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addMediaType(String fileExtension, MediaType mediaType) {
/* 155 */     this.mediaTypes.put(fileExtension, mediaType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addMediaTypes(Map<String, MediaType> mediaTypes) {
/* 164 */     if (mediaTypes != null) {
/* 165 */       this.mediaTypes.putAll(mediaTypes);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIgnoreUnknownPathExtensions(boolean ignore) {
/* 176 */     this.ignoreUnknownPathExtensions = ignore;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUseJaf(boolean useJaf) {
/* 187 */     this.useJaf = Boolean.valueOf(useJaf);
/*     */   }
/*     */   
/*     */   private boolean isUseJafTurnedOff() {
/* 191 */     return (this.useJaf != null && !this.useJaf.booleanValue());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFavorParameter(boolean favorParameter) {
/* 202 */     this.favorParameter = favorParameter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setParameterName(String parameterName) {
/* 210 */     Assert.notNull(parameterName, "parameterName is required");
/* 211 */     this.parameterName = parameterName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIgnoreAcceptHeader(boolean ignoreAcceptHeader) {
/* 219 */     this.ignoreAcceptHeader = ignoreAcceptHeader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDefaultContentType(MediaType contentType) {
/* 228 */     this.defaultNegotiationStrategy = new FixedContentNegotiationStrategy(contentType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDefaultContentTypeStrategy(ContentNegotiationStrategy strategy) {
/* 239 */     this.defaultNegotiationStrategy = strategy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setServletContext(ServletContext servletContext) {
/* 247 */     this.servletContext = servletContext;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() {
/* 253 */     List<ContentNegotiationStrategy> strategies = new ArrayList<ContentNegotiationStrategy>();
/*     */     
/* 255 */     if (this.favorPathExtension) {
/*     */       PathExtensionContentNegotiationStrategy strategy;
/* 257 */       if (this.servletContext != null && !isUseJafTurnedOff()) {
/* 258 */         strategy = new ServletPathExtensionContentNegotiationStrategy(this.servletContext, this.mediaTypes);
/*     */       } else {
/*     */         
/* 261 */         strategy = new PathExtensionContentNegotiationStrategy(this.mediaTypes);
/*     */       } 
/* 263 */       strategy.setIgnoreUnknownExtensions(this.ignoreUnknownPathExtensions);
/* 264 */       if (this.useJaf != null) {
/* 265 */         strategy.setUseJaf(this.useJaf.booleanValue());
/*     */       }
/* 267 */       strategies.add(strategy);
/*     */     } 
/*     */     
/* 270 */     if (this.favorParameter) {
/* 271 */       ParameterContentNegotiationStrategy strategy = new ParameterContentNegotiationStrategy(this.mediaTypes);
/* 272 */       strategy.setParameterName(this.parameterName);
/* 273 */       strategies.add(strategy);
/*     */     } 
/*     */     
/* 276 */     if (!this.ignoreAcceptHeader) {
/* 277 */       strategies.add(new HeaderContentNegotiationStrategy());
/*     */     }
/*     */     
/* 280 */     if (this.defaultNegotiationStrategy != null) {
/* 281 */       strategies.add(this.defaultNegotiationStrategy);
/*     */     }
/*     */     
/* 284 */     this.contentNegotiationManager = new ContentNegotiationManager(strategies);
/*     */   }
/*     */ 
/*     */   
/*     */   public ContentNegotiationManager getObject() {
/* 289 */     return this.contentNegotiationManager;
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?> getObjectType() {
/* 294 */     return ContentNegotiationManager.class;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSingleton() {
/* 299 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\accept\ContentNegotiationManagerFactoryBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */