/*     */ package org.springframework.core.env;
/*     */ 
/*     */ import java.security.AccessControlException;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.core.SpringProperties;
/*     */ import org.springframework.core.convert.support.ConfigurableConversionService;
/*     */ import org.springframework.util.Assert;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractEnvironment
/*     */   implements ConfigurableEnvironment
/*     */ {
/*     */   public static final String IGNORE_GETENV_PROPERTY_NAME = "spring.getenv.ignore";
/*     */   public static final String ACTIVE_PROFILES_PROPERTY_NAME = "spring.profiles.active";
/*     */   public static final String DEFAULT_PROFILES_PROPERTY_NAME = "spring.profiles.default";
/*     */   protected static final String RESERVED_DEFAULT_PROFILE_NAME = "default";
/* 103 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/* 105 */   private final Set<String> activeProfiles = new LinkedHashSet<String>();
/*     */   
/* 107 */   private final Set<String> defaultProfiles = new LinkedHashSet<String>(getReservedDefaultProfiles());
/*     */   
/* 109 */   private final MutablePropertySources propertySources = new MutablePropertySources(this.logger);
/*     */   
/* 111 */   private final ConfigurablePropertyResolver propertyResolver = new PropertySourcesPropertyResolver(this.propertySources);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractEnvironment() {
/* 123 */     customizePropertySources(this.propertySources);
/* 124 */     if (this.logger.isDebugEnabled()) {
/* 125 */       this.logger.debug("Initialized " + getClass().getSimpleName() + " with PropertySources " + this.propertySources);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void customizePropertySources(MutablePropertySources propertySources) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Set<String> getReservedDefaultProfiles() {
/* 216 */     return Collections.singleton("default");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getActiveProfiles() {
/* 226 */     return StringUtils.toStringArray(doGetActiveProfiles());
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
/*     */   protected Set<String> doGetActiveProfiles() {
/* 238 */     synchronized (this.activeProfiles) {
/* 239 */       if (this.activeProfiles.isEmpty()) {
/* 240 */         String profiles = getProperty("spring.profiles.active");
/* 241 */         if (StringUtils.hasText(profiles)) {
/* 242 */           setActiveProfiles(StringUtils.commaDelimitedListToStringArray(
/* 243 */                 StringUtils.trimAllWhitespace(profiles)));
/*     */         }
/*     */       } 
/* 246 */       return this.activeProfiles;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setActiveProfiles(String... profiles) {
/* 252 */     Assert.notNull(profiles, "Profile array must not be null");
/* 253 */     if (this.logger.isDebugEnabled()) {
/* 254 */       this.logger.debug("Activating profiles " + Arrays.<String>asList(profiles));
/*     */     }
/* 256 */     synchronized (this.activeProfiles) {
/* 257 */       this.activeProfiles.clear();
/* 258 */       for (String profile : profiles) {
/* 259 */         validateProfile(profile);
/* 260 */         this.activeProfiles.add(profile);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void addActiveProfile(String profile) {
/* 267 */     if (this.logger.isDebugEnabled()) {
/* 268 */       this.logger.debug("Activating profile '" + profile + "'");
/*     */     }
/* 270 */     validateProfile(profile);
/* 271 */     doGetActiveProfiles();
/* 272 */     synchronized (this.activeProfiles) {
/* 273 */       this.activeProfiles.add(profile);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getDefaultProfiles() {
/* 280 */     return StringUtils.toStringArray(doGetDefaultProfiles());
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
/*     */   protected Set<String> doGetDefaultProfiles() {
/* 296 */     synchronized (this.defaultProfiles) {
/* 297 */       if (this.defaultProfiles.equals(getReservedDefaultProfiles())) {
/* 298 */         String profiles = getProperty("spring.profiles.default");
/* 299 */         if (StringUtils.hasText(profiles)) {
/* 300 */           setDefaultProfiles(StringUtils.commaDelimitedListToStringArray(
/* 301 */                 StringUtils.trimAllWhitespace(profiles)));
/*     */         }
/*     */       } 
/* 304 */       return this.defaultProfiles;
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
/*     */   public void setDefaultProfiles(String... profiles) {
/* 318 */     Assert.notNull(profiles, "Profile array must not be null");
/* 319 */     synchronized (this.defaultProfiles) {
/* 320 */       this.defaultProfiles.clear();
/* 321 */       for (String profile : profiles) {
/* 322 */         validateProfile(profile);
/* 323 */         this.defaultProfiles.add(profile);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean acceptsProfiles(String... profiles) {
/* 330 */     Assert.notEmpty((Object[])profiles, "Must specify at least one profile");
/* 331 */     for (String profile : profiles) {
/* 332 */       if (StringUtils.hasLength(profile) && profile.charAt(0) == '!') {
/* 333 */         if (!isProfileActive(profile.substring(1))) {
/* 334 */           return true;
/*     */         }
/*     */       }
/* 337 */       else if (isProfileActive(profile)) {
/* 338 */         return true;
/*     */       } 
/*     */     } 
/* 341 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isProfileActive(String profile) {
/* 350 */     validateProfile(profile);
/* 351 */     Set<String> currentActiveProfiles = doGetActiveProfiles();
/* 352 */     return (currentActiveProfiles.contains(profile) || (currentActiveProfiles
/* 353 */       .isEmpty() && doGetDefaultProfiles().contains(profile)));
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
/*     */   protected void validateProfile(String profile) {
/* 367 */     if (!StringUtils.hasText(profile)) {
/* 368 */       throw new IllegalArgumentException("Invalid profile [" + profile + "]: must contain text");
/*     */     }
/* 370 */     if (profile.charAt(0) == '!') {
/* 371 */       throw new IllegalArgumentException("Invalid profile [" + profile + "]: must not begin with ! operator");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public MutablePropertySources getPropertySources() {
/* 377 */     return this.propertySources;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, Object> getSystemProperties() {
/*     */     try {
/* 384 */       return System.getProperties();
/*     */     }
/* 386 */     catch (AccessControlException ex) {
/* 387 */       return new ReadOnlySystemAttributesMap()
/*     */         {
/*     */           protected String getSystemAttribute(String attributeName) {
/*     */             try {
/* 391 */               return System.getProperty(attributeName);
/*     */             }
/* 393 */             catch (AccessControlException ex) {
/* 394 */               if (AbstractEnvironment.this.logger.isInfoEnabled()) {
/* 395 */                 AbstractEnvironment.this.logger.info("Caught AccessControlException when accessing system property '" + attributeName + "'; its value will be returned [null]. Reason: " + ex
/* 396 */                     .getMessage());
/*     */               }
/* 398 */               return null;
/*     */             } 
/*     */           }
/*     */         };
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, Object> getSystemEnvironment() {
/* 408 */     if (suppressGetenvAccess()) {
/* 409 */       return Collections.emptyMap();
/*     */     }
/*     */     try {
/* 412 */       return (Map)System.getenv();
/*     */     }
/* 414 */     catch (AccessControlException ex) {
/* 415 */       return new ReadOnlySystemAttributesMap()
/*     */         {
/*     */           protected String getSystemAttribute(String attributeName) {
/*     */             try {
/* 419 */               return System.getenv(attributeName);
/*     */             }
/* 421 */             catch (AccessControlException ex) {
/* 422 */               if (AbstractEnvironment.this.logger.isInfoEnabled()) {
/* 423 */                 AbstractEnvironment.this.logger.info("Caught AccessControlException when accessing system environment variable '" + attributeName + "'; its value will be returned [null]. Reason: " + ex
/* 424 */                     .getMessage());
/*     */               }
/* 426 */               return null;
/*     */             } 
/*     */           }
/*     */         };
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
/*     */   protected boolean suppressGetenvAccess() {
/* 445 */     return SpringProperties.getFlag("spring.getenv.ignore");
/*     */   }
/*     */ 
/*     */   
/*     */   public void merge(ConfigurableEnvironment parent) {
/* 450 */     for (PropertySource<?> ps : (Iterable<PropertySource<?>>)parent.getPropertySources()) {
/* 451 */       if (!this.propertySources.contains(ps.getName())) {
/* 452 */         this.propertySources.addLast(ps);
/*     */       }
/*     */     } 
/* 455 */     String[] parentActiveProfiles = parent.getActiveProfiles();
/* 456 */     if (!ObjectUtils.isEmpty((Object[])parentActiveProfiles)) {
/* 457 */       synchronized (this.activeProfiles) {
/* 458 */         for (String profile : parentActiveProfiles) {
/* 459 */           this.activeProfiles.add(profile);
/*     */         }
/*     */       } 
/*     */     }
/* 463 */     String[] parentDefaultProfiles = parent.getDefaultProfiles();
/* 464 */     if (!ObjectUtils.isEmpty((Object[])parentDefaultProfiles)) {
/* 465 */       synchronized (this.defaultProfiles) {
/* 466 */         this.defaultProfiles.remove("default");
/* 467 */         for (String profile : parentDefaultProfiles) {
/* 468 */           this.defaultProfiles.add(profile);
/*     */         }
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
/*     */   public ConfigurableConversionService getConversionService() {
/* 481 */     return this.propertyResolver.getConversionService();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setConversionService(ConfigurableConversionService conversionService) {
/* 486 */     this.propertyResolver.setConversionService(conversionService);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPlaceholderPrefix(String placeholderPrefix) {
/* 491 */     this.propertyResolver.setPlaceholderPrefix(placeholderPrefix);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPlaceholderSuffix(String placeholderSuffix) {
/* 496 */     this.propertyResolver.setPlaceholderSuffix(placeholderSuffix);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setValueSeparator(String valueSeparator) {
/* 501 */     this.propertyResolver.setValueSeparator(valueSeparator);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setIgnoreUnresolvableNestedPlaceholders(boolean ignoreUnresolvableNestedPlaceholders) {
/* 506 */     this.propertyResolver.setIgnoreUnresolvableNestedPlaceholders(ignoreUnresolvableNestedPlaceholders);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRequiredProperties(String... requiredProperties) {
/* 511 */     this.propertyResolver.setRequiredProperties(requiredProperties);
/*     */   }
/*     */ 
/*     */   
/*     */   public void validateRequiredProperties() throws MissingRequiredPropertiesException {
/* 516 */     this.propertyResolver.validateRequiredProperties();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsProperty(String key) {
/* 526 */     return this.propertyResolver.containsProperty(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getProperty(String key) {
/* 531 */     return this.propertyResolver.getProperty(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getProperty(String key, String defaultValue) {
/* 536 */     return this.propertyResolver.getProperty(key, defaultValue);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T getProperty(String key, Class<T> targetType) {
/* 541 */     return this.propertyResolver.getProperty(key, targetType);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T getProperty(String key, Class<T> targetType, T defaultValue) {
/* 546 */     return this.propertyResolver.getProperty(key, targetType, defaultValue);
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public <T> Class<T> getPropertyAsClass(String key, Class<T> targetType) {
/* 552 */     return this.propertyResolver.getPropertyAsClass(key, targetType);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getRequiredProperty(String key) throws IllegalStateException {
/* 557 */     return this.propertyResolver.getRequiredProperty(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T getRequiredProperty(String key, Class<T> targetType) throws IllegalStateException {
/* 562 */     return this.propertyResolver.getRequiredProperty(key, targetType);
/*     */   }
/*     */ 
/*     */   
/*     */   public String resolvePlaceholders(String text) {
/* 567 */     return this.propertyResolver.resolvePlaceholders(text);
/*     */   }
/*     */ 
/*     */   
/*     */   public String resolveRequiredPlaceholders(String text) throws IllegalArgumentException {
/* 572 */     return this.propertyResolver.resolveRequiredPlaceholders(text);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 578 */     return getClass().getSimpleName() + " {activeProfiles=" + this.activeProfiles + ", defaultProfiles=" + this.defaultProfiles + ", propertySources=" + this.propertySources + "}";
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\env\AbstractEnvironment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */