/*     */ package org.springframework.context.support;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import org.springframework.context.ResourceLoaderAware;
/*     */ import org.springframework.core.io.DefaultResourceLoader;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.ResourceLoader;
/*     */ import org.springframework.util.DefaultPropertiesPersister;
/*     */ import org.springframework.util.PropertiesPersister;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ReloadableResourceBundleMessageSource
/*     */   extends AbstractResourceBasedMessageSource
/*     */   implements ResourceLoaderAware
/*     */ {
/*     */   private static final String PROPERTIES_SUFFIX = ".properties";
/*     */   private static final String XML_SUFFIX = ".xml";
/*     */   private Properties fileEncodings;
/*     */   private boolean concurrentRefresh = true;
/*  99 */   private PropertiesPersister propertiesPersister = (PropertiesPersister)new DefaultPropertiesPersister();
/*     */   
/* 101 */   private ResourceLoader resourceLoader = (ResourceLoader)new DefaultResourceLoader();
/*     */ 
/*     */   
/* 104 */   private final ConcurrentMap<String, Map<Locale, List<String>>> cachedFilenames = new ConcurrentHashMap<String, Map<Locale, List<String>>>();
/*     */ 
/*     */ 
/*     */   
/* 108 */   private final ConcurrentMap<String, PropertiesHolder> cachedProperties = new ConcurrentHashMap<String, PropertiesHolder>();
/*     */ 
/*     */ 
/*     */   
/* 112 */   private final ConcurrentMap<Locale, PropertiesHolder> cachedMergedProperties = new ConcurrentHashMap<Locale, PropertiesHolder>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFileEncodings(Properties fileEncodings) {
/* 127 */     this.fileEncodings = fileEncodings;
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
/*     */   public void setConcurrentRefresh(boolean concurrentRefresh) {
/* 142 */     this.concurrentRefresh = concurrentRefresh;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPropertiesPersister(PropertiesPersister propertiesPersister) {
/* 151 */     this.propertiesPersister = (propertiesPersister != null) ? propertiesPersister : (PropertiesPersister)new DefaultPropertiesPersister();
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
/*     */   public void setResourceLoader(ResourceLoader resourceLoader) {
/* 166 */     this.resourceLoader = (resourceLoader != null) ? resourceLoader : (ResourceLoader)new DefaultResourceLoader();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String resolveCodeWithoutArguments(String code, Locale locale) {
/* 176 */     if (getCacheMillis() < 0L) {
/* 177 */       PropertiesHolder propHolder = getMergedProperties(locale);
/* 178 */       String result = propHolder.getProperty(code);
/* 179 */       if (result != null) {
/* 180 */         return result;
/*     */       }
/*     */     } else {
/*     */       
/* 184 */       for (String basename : getBasenameSet()) {
/* 185 */         List<String> filenames = calculateAllFilenames(basename, locale);
/* 186 */         for (String filename : filenames) {
/* 187 */           PropertiesHolder propHolder = getProperties(filename);
/* 188 */           String result = propHolder.getProperty(code);
/* 189 */           if (result != null) {
/* 190 */             return result;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/* 195 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected MessageFormat resolveCode(String code, Locale locale) {
/* 204 */     if (getCacheMillis() < 0L) {
/* 205 */       PropertiesHolder propHolder = getMergedProperties(locale);
/* 206 */       MessageFormat result = propHolder.getMessageFormat(code, locale);
/* 207 */       if (result != null) {
/* 208 */         return result;
/*     */       }
/*     */     } else {
/*     */       
/* 212 */       for (String basename : getBasenameSet()) {
/* 213 */         List<String> filenames = calculateAllFilenames(basename, locale);
/* 214 */         for (String filename : filenames) {
/* 215 */           PropertiesHolder propHolder = getProperties(filename);
/* 216 */           MessageFormat result = propHolder.getMessageFormat(code, locale);
/* 217 */           if (result != null) {
/* 218 */             return result;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/* 223 */     return null;
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
/*     */   protected PropertiesHolder getMergedProperties(Locale locale) {
/* 236 */     PropertiesHolder mergedHolder = this.cachedMergedProperties.get(locale);
/* 237 */     if (mergedHolder != null) {
/* 238 */       return mergedHolder;
/*     */     }
/* 240 */     Properties mergedProps = newProperties();
/* 241 */     long latestTimestamp = -1L;
/* 242 */     String[] basenames = StringUtils.toStringArray(getBasenameSet());
/* 243 */     for (int i = basenames.length - 1; i >= 0; i--) {
/* 244 */       List<String> filenames = calculateAllFilenames(basenames[i], locale);
/* 245 */       for (int j = filenames.size() - 1; j >= 0; j--) {
/* 246 */         String filename = filenames.get(j);
/* 247 */         PropertiesHolder propHolder = getProperties(filename);
/* 248 */         if (propHolder.getProperties() != null) {
/* 249 */           mergedProps.putAll(propHolder.getProperties());
/* 250 */           if (propHolder.getFileTimestamp() > latestTimestamp) {
/* 251 */             latestTimestamp = propHolder.getFileTimestamp();
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/* 256 */     mergedHolder = new PropertiesHolder(mergedProps, latestTimestamp);
/* 257 */     PropertiesHolder existing = this.cachedMergedProperties.putIfAbsent(locale, mergedHolder);
/* 258 */     if (existing != null) {
/* 259 */       mergedHolder = existing;
/*     */     }
/* 261 */     return mergedHolder;
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
/*     */   protected List<String> calculateAllFilenames(String basename, Locale locale) {
/* 275 */     Map<Locale, List<String>> localeMap = this.cachedFilenames.get(basename);
/* 276 */     if (localeMap != null) {
/* 277 */       List<String> list = localeMap.get(locale);
/* 278 */       if (list != null) {
/* 279 */         return list;
/*     */       }
/*     */     } 
/* 282 */     List<String> filenames = new ArrayList<String>(7);
/* 283 */     filenames.addAll(calculateFilenamesForLocale(basename, locale));
/* 284 */     if (isFallbackToSystemLocale() && !locale.equals(Locale.getDefault())) {
/* 285 */       List<String> fallbackFilenames = calculateFilenamesForLocale(basename, Locale.getDefault());
/* 286 */       for (String fallbackFilename : fallbackFilenames) {
/* 287 */         if (!filenames.contains(fallbackFilename))
/*     */         {
/* 289 */           filenames.add(fallbackFilename);
/*     */         }
/*     */       } 
/*     */     } 
/* 293 */     filenames.add(basename);
/* 294 */     if (localeMap == null) {
/* 295 */       localeMap = new ConcurrentHashMap<Locale, List<String>>();
/* 296 */       Map<Locale, List<String>> existing = this.cachedFilenames.putIfAbsent(basename, localeMap);
/* 297 */       if (existing != null) {
/* 298 */         localeMap = existing;
/*     */       }
/*     */     } 
/* 301 */     localeMap.put(locale, filenames);
/* 302 */     return filenames;
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
/*     */   protected List<String> calculateFilenamesForLocale(String basename, Locale locale) {
/* 316 */     List<String> result = new ArrayList<String>(3);
/* 317 */     String language = locale.getLanguage();
/* 318 */     String country = locale.getCountry();
/* 319 */     String variant = locale.getVariant();
/* 320 */     StringBuilder temp = new StringBuilder(basename);
/*     */     
/* 322 */     temp.append('_');
/* 323 */     if (language.length() > 0) {
/* 324 */       temp.append(language);
/* 325 */       result.add(0, temp.toString());
/*     */     } 
/*     */     
/* 328 */     temp.append('_');
/* 329 */     if (country.length() > 0) {
/* 330 */       temp.append(country);
/* 331 */       result.add(0, temp.toString());
/*     */     } 
/*     */     
/* 334 */     if (variant.length() > 0 && (language.length() > 0 || country.length() > 0)) {
/* 335 */       temp.append('_').append(variant);
/* 336 */       result.add(0, temp.toString());
/*     */     } 
/*     */     
/* 339 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected PropertiesHolder getProperties(String filename) {
/* 350 */     PropertiesHolder propHolder = this.cachedProperties.get(filename);
/* 351 */     long originalTimestamp = -2L;
/*     */     
/* 353 */     if (propHolder != null) {
/* 354 */       originalTimestamp = propHolder.getRefreshTimestamp();
/* 355 */       if (originalTimestamp == -1L || originalTimestamp > System.currentTimeMillis() - getCacheMillis())
/*     */       {
/* 357 */         return propHolder;
/*     */       }
/*     */     } else {
/*     */       
/* 361 */       propHolder = new PropertiesHolder();
/* 362 */       PropertiesHolder existingHolder = this.cachedProperties.putIfAbsent(filename, propHolder);
/* 363 */       if (existingHolder != null) {
/* 364 */         propHolder = existingHolder;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 369 */     if (this.concurrentRefresh && propHolder.getRefreshTimestamp() >= 0L) {
/*     */       
/* 371 */       if (!propHolder.refreshLock.tryLock())
/*     */       {
/*     */         
/* 374 */         return propHolder;
/*     */       }
/*     */     } else {
/*     */       
/* 378 */       propHolder.refreshLock.lock();
/*     */     } 
/*     */     try {
/* 381 */       PropertiesHolder existingHolder = this.cachedProperties.get(filename);
/* 382 */       if (existingHolder != null && existingHolder.getRefreshTimestamp() > originalTimestamp) {
/* 383 */         return existingHolder;
/*     */       }
/* 385 */       return refreshProperties(filename, propHolder);
/*     */     } finally {
/*     */       
/* 388 */       propHolder.refreshLock.unlock();
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
/*     */   protected PropertiesHolder refreshProperties(String filename, PropertiesHolder propHolder) {
/* 400 */     long refreshTimestamp = (getCacheMillis() < 0L) ? -1L : System.currentTimeMillis();
/*     */     
/* 402 */     Resource resource = this.resourceLoader.getResource(filename + ".properties");
/* 403 */     if (!resource.exists()) {
/* 404 */       resource = this.resourceLoader.getResource(filename + ".xml");
/*     */     }
/*     */     
/* 407 */     if (resource.exists()) {
/* 408 */       long fileTimestamp = -1L;
/* 409 */       if (getCacheMillis() >= 0L) {
/*     */         
/*     */         try {
/* 412 */           fileTimestamp = resource.lastModified();
/* 413 */           if (propHolder != null && propHolder.getFileTimestamp() == fileTimestamp) {
/* 414 */             if (this.logger.isDebugEnabled()) {
/* 415 */               this.logger.debug("Re-caching properties for filename [" + filename + "] - file hasn't been modified");
/*     */             }
/* 417 */             propHolder.setRefreshTimestamp(refreshTimestamp);
/* 418 */             return propHolder;
/*     */           }
/*     */         
/* 421 */         } catch (IOException ex) {
/*     */           
/* 423 */           if (this.logger.isDebugEnabled()) {
/* 424 */             this.logger.debug(resource + " could not be resolved in the file system - assuming that it hasn't changed", ex);
/*     */           }
/* 426 */           fileTimestamp = -1L;
/*     */         } 
/*     */       }
/*     */       try {
/* 430 */         Properties props = loadProperties(resource, filename);
/* 431 */         propHolder = new PropertiesHolder(props, fileTimestamp);
/*     */       }
/* 433 */       catch (IOException ex) {
/* 434 */         if (this.logger.isWarnEnabled()) {
/* 435 */           this.logger.warn("Could not parse properties file [" + resource.getFilename() + "]", ex);
/*     */         }
/*     */         
/* 438 */         propHolder = new PropertiesHolder();
/*     */       }
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 444 */       if (this.logger.isDebugEnabled()) {
/* 445 */         this.logger.debug("No properties file found for [" + filename + "] - neither plain properties nor XML");
/*     */       }
/*     */       
/* 448 */       propHolder = new PropertiesHolder();
/*     */     } 
/*     */     
/* 451 */     propHolder.setRefreshTimestamp(refreshTimestamp);
/* 452 */     this.cachedProperties.put(filename, propHolder);
/* 453 */     return propHolder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Properties loadProperties(Resource resource, String filename) throws IOException {
/* 464 */     InputStream is = resource.getInputStream();
/* 465 */     Properties props = newProperties();
/*     */     try {
/* 467 */       if (resource.getFilename().endsWith(".xml")) {
/* 468 */         if (this.logger.isDebugEnabled()) {
/* 469 */           this.logger.debug("Loading properties [" + resource.getFilename() + "]");
/*     */         }
/* 471 */         this.propertiesPersister.loadFromXml(props, is);
/*     */       } else {
/*     */         
/* 474 */         String encoding = null;
/* 475 */         if (this.fileEncodings != null) {
/* 476 */           encoding = this.fileEncodings.getProperty(filename);
/*     */         }
/* 478 */         if (encoding == null) {
/* 479 */           encoding = getDefaultEncoding();
/*     */         }
/* 481 */         if (encoding != null) {
/* 482 */           if (this.logger.isDebugEnabled()) {
/* 483 */             this.logger.debug("Loading properties [" + resource.getFilename() + "] with encoding '" + encoding + "'");
/*     */           }
/* 485 */           this.propertiesPersister.load(props, new InputStreamReader(is, encoding));
/*     */         } else {
/*     */           
/* 488 */           if (this.logger.isDebugEnabled()) {
/* 489 */             this.logger.debug("Loading properties [" + resource.getFilename() + "]");
/*     */           }
/* 491 */           this.propertiesPersister.load(props, is);
/*     */         } 
/*     */       } 
/* 494 */       return props;
/*     */     } finally {
/*     */       
/* 497 */       is.close();
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
/*     */   protected Properties newProperties() {
/* 511 */     return new Properties();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearCache() {
/* 520 */     this.logger.debug("Clearing entire resource bundle cache");
/* 521 */     this.cachedProperties.clear();
/* 522 */     this.cachedMergedProperties.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearCacheIncludingAncestors() {
/* 530 */     clearCache();
/* 531 */     if (getParentMessageSource() instanceof ReloadableResourceBundleMessageSource) {
/* 532 */       ((ReloadableResourceBundleMessageSource)getParentMessageSource()).clearCacheIncludingAncestors();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 539 */     return getClass().getName() + ": basenames=" + getBasenameSet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected class PropertiesHolder
/*     */   {
/*     */     private final Properties properties;
/*     */ 
/*     */ 
/*     */     
/*     */     private final long fileTimestamp;
/*     */ 
/*     */ 
/*     */     
/* 555 */     private volatile long refreshTimestamp = -2L;
/*     */     
/* 557 */     private final ReentrantLock refreshLock = new ReentrantLock();
/*     */ 
/*     */     
/* 560 */     private final ConcurrentMap<String, Map<Locale, MessageFormat>> cachedMessageFormats = new ConcurrentHashMap<String, Map<Locale, MessageFormat>>();
/*     */ 
/*     */     
/*     */     public PropertiesHolder() {
/* 564 */       this.properties = null;
/* 565 */       this.fileTimestamp = -1L;
/*     */     }
/*     */     
/*     */     public PropertiesHolder(Properties properties, long fileTimestamp) {
/* 569 */       this.properties = properties;
/* 570 */       this.fileTimestamp = fileTimestamp;
/*     */     }
/*     */     
/*     */     public Properties getProperties() {
/* 574 */       return this.properties;
/*     */     }
/*     */     
/*     */     public long getFileTimestamp() {
/* 578 */       return this.fileTimestamp;
/*     */     }
/*     */     
/*     */     public void setRefreshTimestamp(long refreshTimestamp) {
/* 582 */       this.refreshTimestamp = refreshTimestamp;
/*     */     }
/*     */     
/*     */     public long getRefreshTimestamp() {
/* 586 */       return this.refreshTimestamp;
/*     */     }
/*     */     
/*     */     public String getProperty(String code) {
/* 590 */       if (this.properties == null) {
/* 591 */         return null;
/*     */       }
/* 593 */       return this.properties.getProperty(code);
/*     */     }
/*     */     
/*     */     public MessageFormat getMessageFormat(String code, Locale locale) {
/* 597 */       if (this.properties == null) {
/* 598 */         return null;
/*     */       }
/* 600 */       Map<Locale, MessageFormat> localeMap = this.cachedMessageFormats.get(code);
/* 601 */       if (localeMap != null) {
/* 602 */         MessageFormat result = localeMap.get(locale);
/* 603 */         if (result != null) {
/* 604 */           return result;
/*     */         }
/*     */       } 
/* 607 */       String msg = this.properties.getProperty(code);
/* 608 */       if (msg != null) {
/* 609 */         if (localeMap == null) {
/* 610 */           localeMap = new ConcurrentHashMap<Locale, MessageFormat>();
/* 611 */           Map<Locale, MessageFormat> existing = this.cachedMessageFormats.putIfAbsent(code, localeMap);
/* 612 */           if (existing != null) {
/* 613 */             localeMap = existing;
/*     */           }
/*     */         } 
/* 616 */         MessageFormat result = ReloadableResourceBundleMessageSource.this.createMessageFormat(msg, locale);
/* 617 */         localeMap.put(locale, result);
/* 618 */         return result;
/*     */       } 
/* 620 */       return null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\support\ReloadableResourceBundleMessageSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */