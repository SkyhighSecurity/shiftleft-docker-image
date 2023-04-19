/*     */ package org.springframework.jmx.export.naming;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Properties;
/*     */ import javax.management.MalformedObjectNameException;
/*     */ import javax.management.ObjectName;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.support.PropertiesLoaderUtils;
/*     */ import org.springframework.jmx.support.ObjectNameManager;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class KeyNamingStrategy
/*     */   implements ObjectNamingStrategy, InitializingBean
/*     */ {
/*  57 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Properties mappings;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Resource[] mappingLocations;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Properties mergedMappings;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMappings(Properties mappings) {
/*  84 */     this.mappings = mappings;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMappingLocation(Resource location) {
/*  92 */     this.mappingLocations = new Resource[] { location };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMappingLocations(Resource... mappingLocations) {
/* 100 */     this.mappingLocations = mappingLocations;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() throws IOException {
/* 111 */     this.mergedMappings = new Properties();
/* 112 */     CollectionUtils.mergePropertiesIntoMap(this.mappings, this.mergedMappings);
/*     */     
/* 114 */     if (this.mappingLocations != null) {
/* 115 */       for (Resource location : this.mappingLocations) {
/* 116 */         if (this.logger.isInfoEnabled()) {
/* 117 */           this.logger.info("Loading JMX object name mappings file from " + location);
/*     */         }
/* 119 */         PropertiesLoaderUtils.fillProperties(this.mergedMappings, location);
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
/*     */   public ObjectName getObjectName(Object managedBean, String beanKey) throws MalformedObjectNameException {
/* 131 */     String objectName = null;
/* 132 */     if (this.mergedMappings != null) {
/* 133 */       objectName = this.mergedMappings.getProperty(beanKey);
/*     */     }
/* 135 */     if (objectName == null) {
/* 136 */       objectName = beanKey;
/*     */     }
/* 138 */     return ObjectNameManager.getInstance(objectName);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\jmx\export\naming\KeyNamingStrategy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */