/*     */ package org.springframework.core.env;
/*     */ 
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.core.convert.ConversionService;
/*     */ import org.springframework.core.convert.support.ConfigurableConversionService;
/*     */ import org.springframework.core.convert.support.DefaultConversionService;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.PropertyPlaceholderHelper;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractPropertyResolver
/*     */   implements ConfigurablePropertyResolver
/*     */ {
/*  42 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*     */   private volatile ConfigurableConversionService conversionService;
/*     */   
/*     */   private PropertyPlaceholderHelper nonStrictHelper;
/*     */   
/*     */   private PropertyPlaceholderHelper strictHelper;
/*     */   
/*     */   private boolean ignoreUnresolvableNestedPlaceholders = false;
/*     */   
/*  52 */   private String placeholderPrefix = "${";
/*     */   
/*  54 */   private String placeholderSuffix = "}";
/*     */   
/*  56 */   private String valueSeparator = ":";
/*     */   
/*  58 */   private final Set<String> requiredProperties = new LinkedHashSet<String>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConfigurableConversionService getConversionService() {
/*  65 */     if (this.conversionService == null) {
/*  66 */       synchronized (this) {
/*  67 */         if (this.conversionService == null) {
/*  68 */           this.conversionService = (ConfigurableConversionService)new DefaultConversionService();
/*     */         }
/*     */       } 
/*     */     }
/*  72 */     return this.conversionService;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setConversionService(ConfigurableConversionService conversionService) {
/*  77 */     Assert.notNull(conversionService, "ConversionService must not be null");
/*  78 */     this.conversionService = conversionService;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPlaceholderPrefix(String placeholderPrefix) {
/*  88 */     Assert.notNull(placeholderPrefix, "'placeholderPrefix' must not be null");
/*  89 */     this.placeholderPrefix = placeholderPrefix;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPlaceholderSuffix(String placeholderSuffix) {
/*  99 */     Assert.notNull(placeholderSuffix, "'placeholderSuffix' must not be null");
/* 100 */     this.placeholderSuffix = placeholderSuffix;
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
/*     */   public void setValueSeparator(String valueSeparator) {
/* 112 */     this.valueSeparator = valueSeparator;
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
/*     */   public void setIgnoreUnresolvableNestedPlaceholders(boolean ignoreUnresolvableNestedPlaceholders) {
/* 126 */     this.ignoreUnresolvableNestedPlaceholders = ignoreUnresolvableNestedPlaceholders;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRequiredProperties(String... requiredProperties) {
/* 131 */     if (requiredProperties != null) {
/* 132 */       for (String key : requiredProperties) {
/* 133 */         this.requiredProperties.add(key);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void validateRequiredProperties() {
/* 140 */     MissingRequiredPropertiesException ex = new MissingRequiredPropertiesException();
/* 141 */     for (String key : this.requiredProperties) {
/* 142 */       if (getProperty(key) == null) {
/* 143 */         ex.addMissingRequiredProperty(key);
/*     */       }
/*     */     } 
/* 146 */     if (!ex.getMissingRequiredProperties().isEmpty()) {
/* 147 */       throw ex;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsProperty(String key) {
/* 153 */     return (getProperty(key) != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getProperty(String key) {
/* 158 */     return getProperty(key, String.class);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getProperty(String key, String defaultValue) {
/* 163 */     String value = getProperty(key);
/* 164 */     return (value != null) ? value : defaultValue;
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T getProperty(String key, Class<T> targetType, T defaultValue) {
/* 169 */     T value = getProperty(key, targetType);
/* 170 */     return (value != null) ? value : defaultValue;
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public <T> Class<T> getPropertyAsClass(String key, Class<T> targetValueType) {
/* 176 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getRequiredProperty(String key) throws IllegalStateException {
/* 181 */     String value = getProperty(key);
/* 182 */     if (value == null) {
/* 183 */       throw new IllegalStateException("Required key '" + key + "' not found");
/*     */     }
/* 185 */     return value;
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T getRequiredProperty(String key, Class<T> valueType) throws IllegalStateException {
/* 190 */     T value = getProperty(key, valueType);
/* 191 */     if (value == null) {
/* 192 */       throw new IllegalStateException("Required key '" + key + "' not found");
/*     */     }
/* 194 */     return value;
/*     */   }
/*     */ 
/*     */   
/*     */   public String resolvePlaceholders(String text) {
/* 199 */     if (this.nonStrictHelper == null) {
/* 200 */       this.nonStrictHelper = createPlaceholderHelper(true);
/*     */     }
/* 202 */     return doResolvePlaceholders(text, this.nonStrictHelper);
/*     */   }
/*     */ 
/*     */   
/*     */   public String resolveRequiredPlaceholders(String text) throws IllegalArgumentException {
/* 207 */     if (this.strictHelper == null) {
/* 208 */       this.strictHelper = createPlaceholderHelper(false);
/*     */     }
/* 210 */     return doResolvePlaceholders(text, this.strictHelper);
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
/*     */   protected String resolveNestedPlaceholders(String value) {
/* 226 */     return this.ignoreUnresolvableNestedPlaceholders ? 
/* 227 */       resolvePlaceholders(value) : resolveRequiredPlaceholders(value);
/*     */   }
/*     */   
/*     */   private PropertyPlaceholderHelper createPlaceholderHelper(boolean ignoreUnresolvablePlaceholders) {
/* 231 */     return new PropertyPlaceholderHelper(this.placeholderPrefix, this.placeholderSuffix, this.valueSeparator, ignoreUnresolvablePlaceholders);
/*     */   }
/*     */ 
/*     */   
/*     */   private String doResolvePlaceholders(String text, PropertyPlaceholderHelper helper) {
/* 236 */     return helper.replacePlaceholders(text, new PropertyPlaceholderHelper.PlaceholderResolver()
/*     */         {
/*     */           public String resolvePlaceholder(String placeholderName) {
/* 239 */             return AbstractPropertyResolver.this.getPropertyAsRawString(placeholderName);
/*     */           }
/*     */         });
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
/*     */   protected <T> T convertValueIfNecessary(Object value, Class<T> targetType) {
/*     */     ConversionService conversionService;
/* 254 */     if (targetType == null) {
/* 255 */       return (T)value;
/*     */     }
/* 257 */     ConfigurableConversionService configurableConversionService = this.conversionService;
/* 258 */     if (configurableConversionService == null) {
/*     */ 
/*     */       
/* 261 */       if (ClassUtils.isAssignableValue(targetType, value)) {
/* 262 */         return (T)value;
/*     */       }
/* 264 */       conversionService = DefaultConversionService.getSharedInstance();
/*     */     } 
/* 266 */     return (T)conversionService.convert(value, targetType);
/*     */   }
/*     */   
/*     */   protected abstract String getPropertyAsRawString(String paramString);
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\env\AbstractPropertyResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */