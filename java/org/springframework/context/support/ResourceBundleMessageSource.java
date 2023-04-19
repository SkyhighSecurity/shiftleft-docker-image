/*     */ package org.springframework.context.support;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.Reader;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.HashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.MissingResourceException;
/*     */ import java.util.PropertyResourceBundle;
/*     */ import java.util.ResourceBundle;
/*     */ import java.util.Set;
/*     */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*     */ import org.springframework.util.ClassUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ResourceBundleMessageSource
/*     */   extends AbstractResourceBasedMessageSource
/*     */   implements BeanClassLoaderAware
/*     */ {
/*     */   private ClassLoader bundleClassLoader;
/*  70 */   private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  79 */   private final Map<String, Map<Locale, ResourceBundle>> cachedResourceBundles = new HashMap<String, Map<Locale, ResourceBundle>>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  90 */   private final Map<ResourceBundle, Map<String, Map<Locale, MessageFormat>>> cachedBundleMessageFormats = new HashMap<ResourceBundle, Map<String, Map<Locale, MessageFormat>>>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBundleClassLoader(ClassLoader classLoader) {
/* 103 */     this.bundleClassLoader = classLoader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ClassLoader getBundleClassLoader() {
/* 112 */     return (this.bundleClassLoader != null) ? this.bundleClassLoader : this.beanClassLoader;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBeanClassLoader(ClassLoader classLoader) {
/* 117 */     this.beanClassLoader = (classLoader != null) ? classLoader : ClassUtils.getDefaultClassLoader();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String resolveCodeWithoutArguments(String code, Locale locale) {
/* 127 */     Set<String> basenames = getBasenameSet();
/* 128 */     for (String basename : basenames) {
/* 129 */       ResourceBundle bundle = getResourceBundle(basename, locale);
/* 130 */       if (bundle != null) {
/* 131 */         String result = getStringOrNull(bundle, code);
/* 132 */         if (result != null) {
/* 133 */           return result;
/*     */         }
/*     */       } 
/*     */     } 
/* 137 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected MessageFormat resolveCode(String code, Locale locale) {
/* 146 */     Set<String> basenames = getBasenameSet();
/* 147 */     for (String basename : basenames) {
/* 148 */       ResourceBundle bundle = getResourceBundle(basename, locale);
/* 149 */       if (bundle != null) {
/* 150 */         MessageFormat messageFormat = getMessageFormat(bundle, code, locale);
/* 151 */         if (messageFormat != null) {
/* 152 */           return messageFormat;
/*     */         }
/*     */       } 
/*     */     } 
/* 156 */     return null;
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
/*     */   protected ResourceBundle getResourceBundle(String basename, Locale locale) {
/* 169 */     if (getCacheMillis() >= 0L)
/*     */     {
/*     */       
/* 172 */       return doGetBundle(basename, locale);
/*     */     }
/*     */ 
/*     */     
/* 176 */     synchronized (this.cachedResourceBundles) {
/* 177 */       Map<Locale, ResourceBundle> localeMap = this.cachedResourceBundles.get(basename);
/* 178 */       if (localeMap != null) {
/* 179 */         ResourceBundle bundle = localeMap.get(locale);
/* 180 */         if (bundle != null) {
/* 181 */           return bundle;
/*     */         }
/*     */       } 
/*     */       try {
/* 185 */         ResourceBundle bundle = doGetBundle(basename, locale);
/* 186 */         if (localeMap == null) {
/* 187 */           localeMap = new HashMap<Locale, ResourceBundle>();
/* 188 */           this.cachedResourceBundles.put(basename, localeMap);
/*     */         } 
/* 190 */         localeMap.put(locale, bundle);
/* 191 */         return bundle;
/*     */       }
/* 193 */       catch (MissingResourceException ex) {
/* 194 */         if (this.logger.isWarnEnabled()) {
/* 195 */           this.logger.warn("ResourceBundle [" + basename + "] not found for MessageSource: " + ex.getMessage());
/*     */         }
/*     */ 
/*     */         
/* 199 */         return null;
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
/*     */   protected ResourceBundle doGetBundle(String basename, Locale locale) throws MissingResourceException {
/* 215 */     return ResourceBundle.getBundle(basename, locale, getBundleClassLoader(), new MessageSourceControl());
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
/*     */   protected ResourceBundle loadBundle(Reader reader) throws IOException {
/* 228 */     return new PropertyResourceBundle(reader);
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
/*     */   protected MessageFormat getMessageFormat(ResourceBundle bundle, String code, Locale locale) throws MissingResourceException {
/* 244 */     synchronized (this.cachedBundleMessageFormats) {
/* 245 */       Map<String, Map<Locale, MessageFormat>> codeMap = this.cachedBundleMessageFormats.get(bundle);
/* 246 */       Map<Locale, MessageFormat> localeMap = null;
/* 247 */       if (codeMap != null) {
/* 248 */         localeMap = codeMap.get(code);
/* 249 */         if (localeMap != null) {
/* 250 */           MessageFormat result = localeMap.get(locale);
/* 251 */           if (result != null) {
/* 252 */             return result;
/*     */           }
/*     */         } 
/*     */       } 
/*     */       
/* 257 */       String msg = getStringOrNull(bundle, code);
/* 258 */       if (msg != null) {
/* 259 */         if (codeMap == null) {
/* 260 */           codeMap = new HashMap<String, Map<Locale, MessageFormat>>();
/* 261 */           this.cachedBundleMessageFormats.put(bundle, codeMap);
/*     */         } 
/* 263 */         if (localeMap == null) {
/* 264 */           localeMap = new HashMap<Locale, MessageFormat>();
/* 265 */           codeMap.put(code, localeMap);
/*     */         } 
/* 267 */         MessageFormat result = createMessageFormat(msg, locale);
/* 268 */         localeMap.put(locale, result);
/* 269 */         return result;
/*     */       } 
/*     */       
/* 272 */       return null;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getStringOrNull(ResourceBundle bundle, String key) {
/* 291 */     if (bundle.containsKey(key)) {
/*     */       try {
/* 293 */         return bundle.getString(key);
/*     */       }
/* 295 */       catch (MissingResourceException missingResourceException) {}
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 300 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 308 */     return getClass().getName() + ": basenames=" + getBasenameSet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class MessageSourceControl
/*     */     extends ResourceBundle.Control
/*     */   {
/*     */     private MessageSourceControl() {}
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload) throws IllegalAccessException, InstantiationException, IOException {
/* 324 */       if (format.equals("java.properties")) {
/* 325 */         InputStream stream; String bundleName = toBundleName(baseName, locale);
/* 326 */         final String resourceName = toResourceName(bundleName, "properties");
/* 327 */         final ClassLoader classLoader = loader;
/* 328 */         final boolean reloadFlag = reload;
/*     */         
/*     */         try {
/* 331 */           stream = AccessController.<InputStream>doPrivileged(new PrivilegedExceptionAction<InputStream>()
/*     */               {
/*     */                 public InputStream run() throws IOException
/*     */                 {
/* 335 */                   InputStream is = null;
/* 336 */                   if (reloadFlag) {
/* 337 */                     URL url = classLoader.getResource(resourceName);
/* 338 */                     if (url != null) {
/* 339 */                       URLConnection connection = url.openConnection();
/* 340 */                       if (connection != null) {
/* 341 */                         connection.setUseCaches(false);
/* 342 */                         is = connection.getInputStream();
/*     */                       } 
/*     */                     } 
/*     */                   } else {
/*     */                     
/* 347 */                     is = classLoader.getResourceAsStream(resourceName);
/*     */                   } 
/* 349 */                   return is;
/*     */                 }
/*     */               });
/*     */         }
/* 353 */         catch (PrivilegedActionException ex) {
/* 354 */           throw (IOException)ex.getException();
/*     */         } 
/* 356 */         if (stream != null) {
/* 357 */           String encoding = ResourceBundleMessageSource.this.getDefaultEncoding();
/* 358 */           if (encoding == null) {
/* 359 */             encoding = "ISO-8859-1";
/*     */           }
/*     */           try {
/* 362 */             return ResourceBundleMessageSource.this.loadBundle(new InputStreamReader(stream, encoding));
/*     */           } finally {
/*     */             
/* 365 */             stream.close();
/*     */           } 
/*     */         } 
/*     */         
/* 369 */         return null;
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 374 */       return super.newBundle(baseName, locale, format, loader, reload);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Locale getFallbackLocale(String baseName, Locale locale) {
/* 380 */       return ResourceBundleMessageSource.this.isFallbackToSystemLocale() ? super.getFallbackLocale(baseName, locale) : null;
/*     */     }
/*     */ 
/*     */     
/*     */     public long getTimeToLive(String baseName, Locale locale) {
/* 385 */       long cacheMillis = ResourceBundleMessageSource.this.getCacheMillis();
/* 386 */       return (cacheMillis >= 0L) ? cacheMillis : super.getTimeToLive(baseName, locale);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean needsReload(String baseName, Locale locale, String format, ClassLoader loader, ResourceBundle bundle, long loadTime) {
/* 393 */       if (super.needsReload(baseName, locale, format, loader, bundle, loadTime)) {
/* 394 */         synchronized (ResourceBundleMessageSource.this.cachedBundleMessageFormats) {
/* 395 */           ResourceBundleMessageSource.this.cachedBundleMessageFormats.remove(bundle);
/*     */         } 
/* 397 */         return true;
/*     */       } 
/*     */       
/* 400 */       return false;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\support\ResourceBundleMessageSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */