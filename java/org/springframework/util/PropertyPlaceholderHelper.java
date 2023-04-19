/*     */ package org.springframework.util;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PropertyPlaceholderHelper
/*     */ {
/*  40 */   private static final Log logger = LogFactory.getLog(PropertyPlaceholderHelper.class);
/*     */   
/*  42 */   private static final Map<String, String> wellKnownSimplePrefixes = new HashMap<String, String>(4);
/*     */   
/*     */   static {
/*  45 */     wellKnownSimplePrefixes.put("}", "{");
/*  46 */     wellKnownSimplePrefixes.put("]", "[");
/*  47 */     wellKnownSimplePrefixes.put(")", "(");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private final String placeholderPrefix;
/*     */ 
/*     */   
/*     */   private final String placeholderSuffix;
/*     */ 
/*     */   
/*     */   private final String simplePrefix;
/*     */ 
/*     */   
/*     */   private final String valueSeparator;
/*     */ 
/*     */   
/*     */   private final boolean ignoreUnresolvablePlaceholders;
/*     */ 
/*     */ 
/*     */   
/*     */   public PropertyPlaceholderHelper(String placeholderPrefix, String placeholderSuffix) {
/*  69 */     this(placeholderPrefix, placeholderSuffix, null, true);
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
/*     */   public PropertyPlaceholderHelper(String placeholderPrefix, String placeholderSuffix, String valueSeparator, boolean ignoreUnresolvablePlaceholders) {
/*  84 */     Assert.notNull(placeholderPrefix, "'placeholderPrefix' must not be null");
/*  85 */     Assert.notNull(placeholderSuffix, "'placeholderSuffix' must not be null");
/*  86 */     this.placeholderPrefix = placeholderPrefix;
/*  87 */     this.placeholderSuffix = placeholderSuffix;
/*  88 */     String simplePrefixForSuffix = wellKnownSimplePrefixes.get(this.placeholderSuffix);
/*  89 */     if (simplePrefixForSuffix != null && this.placeholderPrefix.endsWith(simplePrefixForSuffix)) {
/*  90 */       this.simplePrefix = simplePrefixForSuffix;
/*     */     } else {
/*     */       
/*  93 */       this.simplePrefix = this.placeholderPrefix;
/*     */     } 
/*  95 */     this.valueSeparator = valueSeparator;
/*  96 */     this.ignoreUnresolvablePlaceholders = ignoreUnresolvablePlaceholders;
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
/*     */   public String replacePlaceholders(String value, final Properties properties) {
/* 108 */     Assert.notNull(properties, "'properties' must not be null");
/* 109 */     return replacePlaceholders(value, new PlaceholderResolver()
/*     */         {
/*     */           public String resolvePlaceholder(String placeholderName) {
/* 112 */             return properties.getProperty(placeholderName);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public static interface PlaceholderResolver
/*     */   {
/*     */     String resolvePlaceholder(String param1String);
/*     */   }
/*     */ 
/*     */   
/*     */   public String replacePlaceholders(String value, PlaceholderResolver placeholderResolver) {
/* 125 */     Assert.notNull(value, "'value' must not be null");
/* 126 */     return parseStringValue(value, placeholderResolver, new HashSet<String>());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected String parseStringValue(String value, PlaceholderResolver placeholderResolver, Set<String> visitedPlaceholders) {
/* 132 */     StringBuilder result = new StringBuilder(value);
/*     */     
/* 134 */     int startIndex = value.indexOf(this.placeholderPrefix);
/* 135 */     while (startIndex != -1) {
/* 136 */       int endIndex = findPlaceholderEndIndex(result, startIndex);
/* 137 */       if (endIndex != -1) {
/* 138 */         String placeholder = result.substring(startIndex + this.placeholderPrefix.length(), endIndex);
/* 139 */         String originalPlaceholder = placeholder;
/* 140 */         if (!visitedPlaceholders.add(originalPlaceholder)) {
/* 141 */           throw new IllegalArgumentException("Circular placeholder reference '" + originalPlaceholder + "' in property definitions");
/*     */         }
/*     */ 
/*     */         
/* 145 */         placeholder = parseStringValue(placeholder, placeholderResolver, visitedPlaceholders);
/*     */         
/* 147 */         String propVal = placeholderResolver.resolvePlaceholder(placeholder);
/* 148 */         if (propVal == null && this.valueSeparator != null) {
/* 149 */           int separatorIndex = placeholder.indexOf(this.valueSeparator);
/* 150 */           if (separatorIndex != -1) {
/* 151 */             String actualPlaceholder = placeholder.substring(0, separatorIndex);
/* 152 */             String defaultValue = placeholder.substring(separatorIndex + this.valueSeparator.length());
/* 153 */             propVal = placeholderResolver.resolvePlaceholder(actualPlaceholder);
/* 154 */             if (propVal == null) {
/* 155 */               propVal = defaultValue;
/*     */             }
/*     */           } 
/*     */         } 
/* 159 */         if (propVal != null) {
/*     */ 
/*     */           
/* 162 */           propVal = parseStringValue(propVal, placeholderResolver, visitedPlaceholders);
/* 163 */           result.replace(startIndex, endIndex + this.placeholderSuffix.length(), propVal);
/* 164 */           if (logger.isTraceEnabled()) {
/* 165 */             logger.trace("Resolved placeholder '" + placeholder + "'");
/*     */           }
/* 167 */           startIndex = result.indexOf(this.placeholderPrefix, startIndex + propVal.length());
/*     */         }
/* 169 */         else if (this.ignoreUnresolvablePlaceholders) {
/*     */           
/* 171 */           startIndex = result.indexOf(this.placeholderPrefix, endIndex + this.placeholderSuffix.length());
/*     */         } else {
/*     */           
/* 174 */           throw new IllegalArgumentException("Could not resolve placeholder '" + placeholder + "' in value \"" + value + "\"");
/*     */         } 
/*     */         
/* 177 */         visitedPlaceholders.remove(originalPlaceholder);
/*     */         continue;
/*     */       } 
/* 180 */       startIndex = -1;
/*     */     } 
/*     */ 
/*     */     
/* 184 */     return result.toString();
/*     */   }
/*     */   
/*     */   private int findPlaceholderEndIndex(CharSequence buf, int startIndex) {
/* 188 */     int index = startIndex + this.placeholderPrefix.length();
/* 189 */     int withinNestedPlaceholder = 0;
/* 190 */     while (index < buf.length()) {
/* 191 */       if (StringUtils.substringMatch(buf, index, this.placeholderSuffix)) {
/* 192 */         if (withinNestedPlaceholder > 0) {
/* 193 */           withinNestedPlaceholder--;
/* 194 */           index += this.placeholderSuffix.length();
/*     */           continue;
/*     */         } 
/* 197 */         return index;
/*     */       } 
/*     */       
/* 200 */       if (StringUtils.substringMatch(buf, index, this.simplePrefix)) {
/* 201 */         withinNestedPlaceholder++;
/* 202 */         index += this.simplePrefix.length();
/*     */         continue;
/*     */       } 
/* 205 */       index++;
/*     */     } 
/*     */     
/* 208 */     return -1;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframewor\\util\PropertyPlaceholderHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */