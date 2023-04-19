/*     */ package org.springframework.core.io.support;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Properties;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.util.DefaultPropertiesPersister;
/*     */ import org.springframework.util.PropertiesPersister;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class PropertiesLoaderSupport
/*     */ {
/*  43 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*     */   protected Properties[] localProperties;
/*     */   
/*     */   protected boolean localOverride = false;
/*     */   
/*     */   private Resource[] locations;
/*     */   
/*     */   private boolean ignoreResourceNotFound = false;
/*     */   
/*     */   private String fileEncoding;
/*     */   
/*  55 */   private PropertiesPersister propertiesPersister = (PropertiesPersister)new DefaultPropertiesPersister();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProperties(Properties properties) {
/*  64 */     this.localProperties = new Properties[] { properties };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPropertiesArray(Properties... propertiesArray) {
/*  72 */     this.localProperties = propertiesArray;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLocation(Resource location) {
/*  81 */     this.locations = new Resource[] { location };
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
/*     */   public void setLocations(Resource... locations) {
/*  94 */     this.locations = locations;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLocalOverride(boolean localOverride) {
/* 104 */     this.localOverride = localOverride;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIgnoreResourceNotFound(boolean ignoreResourceNotFound) {
/* 113 */     this.ignoreResourceNotFound = ignoreResourceNotFound;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFileEncoding(String encoding) {
/* 124 */     this.fileEncoding = encoding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPropertiesPersister(PropertiesPersister propertiesPersister) {
/* 133 */     this.propertiesPersister = (propertiesPersister != null) ? propertiesPersister : (PropertiesPersister)new DefaultPropertiesPersister();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Properties mergeProperties() throws IOException {
/* 143 */     Properties result = new Properties();
/*     */     
/* 145 */     if (this.localOverride)
/*     */     {
/* 147 */       loadProperties(result);
/*     */     }
/*     */     
/* 150 */     if (this.localProperties != null) {
/* 151 */       for (Properties localProp : this.localProperties) {
/* 152 */         CollectionUtils.mergePropertiesIntoMap(localProp, result);
/*     */       }
/*     */     }
/*     */     
/* 156 */     if (!this.localOverride)
/*     */     {
/* 158 */       loadProperties(result);
/*     */     }
/*     */     
/* 161 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void loadProperties(Properties props) throws IOException {
/* 171 */     if (this.locations != null)
/* 172 */       for (Resource location : this.locations) {
/* 173 */         if (this.logger.isDebugEnabled()) {
/* 174 */           this.logger.debug("Loading properties file from " + location);
/*     */         }
/*     */         try {
/* 177 */           PropertiesLoaderUtils.fillProperties(props, new EncodedResource(location, this.fileEncoding), this.propertiesPersister);
/*     */         
/*     */         }
/* 180 */         catch (IOException ex) {
/*     */           
/* 182 */           if (this.ignoreResourceNotFound && (ex instanceof java.io.FileNotFoundException || ex instanceof java.net.UnknownHostException)) {
/*     */             
/* 184 */             if (this.logger.isInfoEnabled()) {
/* 185 */               this.logger.info("Properties resource not found: " + ex.getMessage());
/*     */             }
/*     */           } else {
/*     */             
/* 189 */             throw ex;
/*     */           } 
/*     */         } 
/*     */       }  
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\io\support\PropertiesLoaderSupport.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */