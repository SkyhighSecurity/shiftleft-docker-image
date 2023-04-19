/*     */ package org.springframework.core.io.support;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import org.springframework.core.io.DefaultResourceLoader;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.ResourceLoader;
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
/*     */ 
/*     */ public class LocalizedResourceHelper
/*     */ {
/*     */   public static final String DEFAULT_SEPARATOR = "_";
/*     */   private final ResourceLoader resourceLoader;
/*  41 */   private String separator = "_";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LocalizedResourceHelper() {
/*  49 */     this.resourceLoader = (ResourceLoader)new DefaultResourceLoader();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LocalizedResourceHelper(ResourceLoader resourceLoader) {
/*  57 */     Assert.notNull(resourceLoader, "ResourceLoader must not be null");
/*  58 */     this.resourceLoader = resourceLoader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSeparator(String separator) {
/*  66 */     this.separator = (separator != null) ? separator : "_";
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
/*     */   
/*     */   public Resource findLocalizedResource(String name, String extension, Locale locale) {
/*  90 */     Assert.notNull(name, "Name must not be null");
/*  91 */     Assert.notNull(extension, "Extension must not be null");
/*     */     
/*  93 */     Resource resource = null;
/*     */     
/*  95 */     if (locale != null) {
/*  96 */       String lang = locale.getLanguage();
/*  97 */       String country = locale.getCountry();
/*  98 */       String variant = locale.getVariant();
/*     */ 
/*     */       
/* 101 */       if (variant.length() > 0) {
/* 102 */         String location = name + this.separator + lang + this.separator + country + this.separator + variant + extension;
/*     */         
/* 104 */         resource = this.resourceLoader.getResource(location);
/*     */       } 
/*     */ 
/*     */       
/* 108 */       if ((resource == null || !resource.exists()) && country.length() > 0) {
/* 109 */         String location = name + this.separator + lang + this.separator + country + extension;
/* 110 */         resource = this.resourceLoader.getResource(location);
/*     */       } 
/*     */ 
/*     */       
/* 114 */       if ((resource == null || !resource.exists()) && lang.length() > 0) {
/* 115 */         String location = name + this.separator + lang + extension;
/* 116 */         resource = this.resourceLoader.getResource(location);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 121 */     if (resource == null || !resource.exists()) {
/* 122 */       String location = name + extension;
/* 123 */       resource = this.resourceLoader.getResource(location);
/*     */     } 
/*     */     
/* 126 */     return resource;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\io\support\LocalizedResourceHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */