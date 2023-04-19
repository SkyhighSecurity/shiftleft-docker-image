/*     */ package org.springframework.beans.factory.xml;
/*     */ 
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.core.io.ClassPathResource;
/*     */ import org.springframework.core.io.support.PropertiesLoaderUtils;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.InputSource;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PluggableSchemaResolver
/*     */   implements EntityResolver
/*     */ {
/*     */   public static final String DEFAULT_SCHEMA_MAPPINGS_LOCATION = "META-INF/spring.schemas";
/*  66 */   private static final Log logger = LogFactory.getLog(PluggableSchemaResolver.class);
/*     */ 
/*     */ 
/*     */   
/*     */   private final ClassLoader classLoader;
/*     */ 
/*     */ 
/*     */   
/*     */   private final String schemaMappingsLocation;
/*     */ 
/*     */ 
/*     */   
/*     */   private volatile Map<String, String> schemaMappings;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PluggableSchemaResolver(ClassLoader classLoader) {
/*  84 */     this.classLoader = classLoader;
/*  85 */     this.schemaMappingsLocation = "META-INF/spring.schemas";
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
/*     */   public PluggableSchemaResolver(ClassLoader classLoader, String schemaMappingsLocation) {
/*  98 */     Assert.hasText(schemaMappingsLocation, "'schemaMappingsLocation' must not be empty");
/*  99 */     this.classLoader = classLoader;
/* 100 */     this.schemaMappingsLocation = schemaMappingsLocation;
/*     */   }
/*     */ 
/*     */   
/*     */   public InputSource resolveEntity(String publicId, String systemId) throws IOException {
/* 105 */     if (logger.isTraceEnabled()) {
/* 106 */       logger.trace("Trying to resolve XML entity with public id [" + publicId + "] and system id [" + systemId + "]");
/*     */     }
/*     */ 
/*     */     
/* 110 */     if (systemId != null) {
/* 111 */       String resourceLocation = getSchemaMappings().get(systemId);
/* 112 */       if (resourceLocation != null) {
/* 113 */         ClassPathResource classPathResource = new ClassPathResource(resourceLocation, this.classLoader);
/*     */         try {
/* 115 */           InputSource source = new InputSource(classPathResource.getInputStream());
/* 116 */           source.setPublicId(publicId);
/* 117 */           source.setSystemId(systemId);
/* 118 */           if (logger.isDebugEnabled()) {
/* 119 */             logger.debug("Found XML schema [" + systemId + "] in classpath: " + resourceLocation);
/*     */           }
/* 121 */           return source;
/*     */         }
/* 123 */         catch (FileNotFoundException ex) {
/* 124 */           if (logger.isDebugEnabled()) {
/* 125 */             logger.debug("Could not find XML schema [" + systemId + "]: " + classPathResource, ex);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/* 130 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Map<String, String> getSchemaMappings() {
/* 137 */     Map<String, String> schemaMappings = this.schemaMappings;
/* 138 */     if (schemaMappings == null) {
/* 139 */       synchronized (this) {
/* 140 */         schemaMappings = this.schemaMappings;
/* 141 */         if (schemaMappings == null) {
/* 142 */           if (logger.isDebugEnabled()) {
/* 143 */             logger.debug("Loading schema mappings from [" + this.schemaMappingsLocation + "]");
/*     */           }
/*     */           
/*     */           try {
/* 147 */             Properties mappings = PropertiesLoaderUtils.loadAllProperties(this.schemaMappingsLocation, this.classLoader);
/* 148 */             if (logger.isDebugEnabled()) {
/* 149 */               logger.debug("Loaded schema mappings: " + mappings);
/*     */             }
/* 151 */             schemaMappings = new ConcurrentHashMap<String, String>(mappings.size());
/* 152 */             CollectionUtils.mergePropertiesIntoMap(mappings, schemaMappings);
/* 153 */             this.schemaMappings = schemaMappings;
/*     */           }
/* 155 */           catch (IOException ex) {
/* 156 */             throw new IllegalStateException("Unable to load schema mappings from location [" + this.schemaMappingsLocation + "]", ex);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/* 162 */     return schemaMappings;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 168 */     return "EntityResolver using mappings " + getSchemaMappings();
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\xml\PluggableSchemaResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */