/*     */ package org.springframework.ui.context.support;
/*     */ 
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*     */ import org.springframework.context.HierarchicalMessageSource;
/*     */ import org.springframework.context.MessageSource;
/*     */ import org.springframework.context.support.ResourceBundleMessageSource;
/*     */ import org.springframework.ui.context.HierarchicalThemeSource;
/*     */ import org.springframework.ui.context.Theme;
/*     */ import org.springframework.ui.context.ThemeSource;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ResourceBundleThemeSource
/*     */   implements HierarchicalThemeSource, BeanClassLoaderAware
/*     */ {
/*  47 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*     */   private ThemeSource parentThemeSource;
/*     */   
/*  51 */   private String basenamePrefix = "";
/*     */ 
/*     */   
/*     */   private String defaultEncoding;
/*     */   
/*     */   private Boolean fallbackToSystemLocale;
/*     */   
/*     */   private ClassLoader beanClassLoader;
/*     */   
/*  60 */   private final Map<String, Theme> themeCache = new ConcurrentHashMap<String, Theme>();
/*     */ 
/*     */ 
/*     */   
/*     */   public void setParentThemeSource(ThemeSource parent) {
/*  65 */     this.parentThemeSource = parent;
/*     */ 
/*     */ 
/*     */     
/*  69 */     synchronized (this.themeCache) {
/*  70 */       for (Theme theme : this.themeCache.values()) {
/*  71 */         initParent(theme);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ThemeSource getParentThemeSource() {
/*  78 */     return this.parentThemeSource;
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
/*     */   public void setBasenamePrefix(String basenamePrefix) {
/*  92 */     this.basenamePrefix = (basenamePrefix != null) ? basenamePrefix : "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDefaultEncoding(String defaultEncoding) {
/* 103 */     this.defaultEncoding = defaultEncoding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFallbackToSystemLocale(boolean fallbackToSystemLocale) {
/* 114 */     this.fallbackToSystemLocale = Boolean.valueOf(fallbackToSystemLocale);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBeanClassLoader(ClassLoader beanClassLoader) {
/* 119 */     this.beanClassLoader = beanClassLoader;
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
/*     */   public Theme getTheme(String themeName) {
/* 134 */     if (themeName == null) {
/* 135 */       return null;
/*     */     }
/* 137 */     Theme theme = this.themeCache.get(themeName);
/* 138 */     if (theme == null) {
/* 139 */       synchronized (this.themeCache) {
/* 140 */         theme = this.themeCache.get(themeName);
/* 141 */         if (theme == null) {
/* 142 */           String basename = this.basenamePrefix + themeName;
/* 143 */           MessageSource messageSource = createMessageSource(basename);
/* 144 */           theme = new SimpleTheme(themeName, messageSource);
/* 145 */           initParent(theme);
/* 146 */           this.themeCache.put(themeName, theme);
/* 147 */           if (this.logger.isDebugEnabled()) {
/* 148 */             this.logger.debug("Theme created: name '" + themeName + "', basename [" + basename + "]");
/*     */           }
/*     */         } 
/*     */       } 
/*     */     }
/* 153 */     return theme;
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
/*     */   protected MessageSource createMessageSource(String basename) {
/* 168 */     ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
/* 169 */     messageSource.setBasename(basename);
/* 170 */     if (this.defaultEncoding != null) {
/* 171 */       messageSource.setDefaultEncoding(this.defaultEncoding);
/*     */     }
/* 173 */     if (this.fallbackToSystemLocale != null) {
/* 174 */       messageSource.setFallbackToSystemLocale(this.fallbackToSystemLocale.booleanValue());
/*     */     }
/* 176 */     if (this.beanClassLoader != null) {
/* 177 */       messageSource.setBeanClassLoader(this.beanClassLoader);
/*     */     }
/* 179 */     return (MessageSource)messageSource;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void initParent(Theme theme) {
/* 188 */     if (theme.getMessageSource() instanceof HierarchicalMessageSource) {
/* 189 */       HierarchicalMessageSource messageSource = (HierarchicalMessageSource)theme.getMessageSource();
/* 190 */       if (getParentThemeSource() != null && messageSource.getParentMessageSource() == null) {
/* 191 */         Theme parentTheme = getParentThemeSource().getTheme(theme.getName());
/* 192 */         if (parentTheme != null)
/* 193 */           messageSource.setParentMessageSource(parentTheme.getMessageSource()); 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframewor\\ui\context\support\ResourceBundleThemeSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */