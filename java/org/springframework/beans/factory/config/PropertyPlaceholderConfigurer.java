/*     */ package org.springframework.beans.factory.config;
/*     */ 
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.core.Constants;
/*     */ import org.springframework.core.SpringProperties;
/*     */ import org.springframework.util.PropertyPlaceholderHelper;
/*     */ import org.springframework.util.StringValueResolver;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PropertyPlaceholderConfigurer
/*     */   extends PlaceholderConfigurerSupport
/*     */ {
/*     */   public static final int SYSTEM_PROPERTIES_MODE_NEVER = 0;
/*     */   public static final int SYSTEM_PROPERTIES_MODE_FALLBACK = 1;
/*     */   public static final int SYSTEM_PROPERTIES_MODE_OVERRIDE = 2;
/*  83 */   private static final Constants constants = new Constants(PropertyPlaceholderConfigurer.class);
/*     */   
/*  85 */   private int systemPropertiesMode = 1;
/*     */ 
/*     */   
/*  88 */   private boolean searchSystemEnvironment = !SpringProperties.getFlag("spring.getenv.ignore");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSystemPropertiesModeName(String constantName) throws IllegalArgumentException {
/*  99 */     this.systemPropertiesMode = constants.asNumber(constantName).intValue();
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
/*     */   public void setSystemPropertiesMode(int systemPropertiesMode) {
/* 115 */     this.systemPropertiesMode = systemPropertiesMode;
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
/*     */   public void setSearchSystemEnvironment(boolean searchSystemEnvironment) {
/* 137 */     this.searchSystemEnvironment = searchSystemEnvironment;
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
/*     */   protected String resolvePlaceholder(String placeholder, Properties props, int systemPropertiesMode) {
/* 157 */     String propVal = null;
/* 158 */     if (systemPropertiesMode == 2) {
/* 159 */       propVal = resolveSystemProperty(placeholder);
/*     */     }
/* 161 */     if (propVal == null) {
/* 162 */       propVal = resolvePlaceholder(placeholder, props);
/*     */     }
/* 164 */     if (propVal == null && systemPropertiesMode == 1) {
/* 165 */       propVal = resolveSystemProperty(placeholder);
/*     */     }
/* 167 */     return propVal;
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
/*     */   protected String resolvePlaceholder(String placeholder, Properties props) {
/* 184 */     return props.getProperty(placeholder);
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
/*     */   protected String resolveSystemProperty(String key) {
/*     */     try {
/* 198 */       String value = System.getProperty(key);
/* 199 */       if (value == null && this.searchSystemEnvironment) {
/* 200 */         value = System.getenv(key);
/*     */       }
/* 202 */       return value;
/*     */     }
/* 204 */     catch (Throwable ex) {
/* 205 */       if (this.logger.isDebugEnabled()) {
/* 206 */         this.logger.debug("Could not access system property '" + key + "': " + ex);
/*     */       }
/* 208 */       return null;
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
/*     */   protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) throws BeansException {
/* 221 */     StringValueResolver valueResolver = new PlaceholderResolvingStringValueResolver(props);
/* 222 */     doProcessProperties(beanFactoryToProcess, valueResolver);
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
/*     */   @Deprecated
/*     */   protected String parseStringValue(String strVal, Properties props, Set<?> visitedPlaceholders) {
/* 237 */     PropertyPlaceholderHelper helper = new PropertyPlaceholderHelper(this.placeholderPrefix, this.placeholderSuffix, this.valueSeparator, this.ignoreUnresolvablePlaceholders);
/*     */     
/* 239 */     PropertyPlaceholderHelper.PlaceholderResolver resolver = new PropertyPlaceholderConfigurerResolver(props);
/* 240 */     return helper.replacePlaceholders(strVal, resolver);
/*     */   }
/*     */ 
/*     */   
/*     */   private class PlaceholderResolvingStringValueResolver
/*     */     implements StringValueResolver
/*     */   {
/*     */     private final PropertyPlaceholderHelper helper;
/*     */     private final PropertyPlaceholderHelper.PlaceholderResolver resolver;
/*     */     
/*     */     public PlaceholderResolvingStringValueResolver(Properties props) {
/* 251 */       this.helper = new PropertyPlaceholderHelper(PropertyPlaceholderConfigurer.this.placeholderPrefix, PropertyPlaceholderConfigurer.this.placeholderSuffix, PropertyPlaceholderConfigurer.this.valueSeparator, PropertyPlaceholderConfigurer.this.ignoreUnresolvablePlaceholders);
/*     */       
/* 253 */       this.resolver = new PropertyPlaceholderConfigurer.PropertyPlaceholderConfigurerResolver(props);
/*     */     }
/*     */ 
/*     */     
/*     */     public String resolveStringValue(String strVal) throws BeansException {
/* 258 */       String resolved = this.helper.replacePlaceholders(strVal, this.resolver);
/* 259 */       if (PropertyPlaceholderConfigurer.this.trimValues) {
/* 260 */         resolved = resolved.trim();
/*     */       }
/* 262 */       return resolved.equals(PropertyPlaceholderConfigurer.this.nullValue) ? null : resolved;
/*     */     }
/*     */   }
/*     */   
/*     */   private class PropertyPlaceholderConfigurerResolver
/*     */     implements PropertyPlaceholderHelper.PlaceholderResolver
/*     */   {
/*     */     private final Properties props;
/*     */     
/*     */     private PropertyPlaceholderConfigurerResolver(Properties props) {
/* 272 */       this.props = props;
/*     */     }
/*     */ 
/*     */     
/*     */     public String resolvePlaceholder(String placeholderName) {
/* 277 */       return PropertyPlaceholderConfigurer.this.resolvePlaceholder(placeholderName, this.props, PropertyPlaceholderConfigurer.this.systemPropertiesMode);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\config\PropertyPlaceholderConfigurer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */