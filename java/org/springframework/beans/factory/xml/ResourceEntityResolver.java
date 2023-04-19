/*     */ package org.springframework.beans.factory.xml;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.net.URL;
/*     */ import java.net.URLDecoder;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.ResourceLoader;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ResourceEntityResolver
/*     */   extends DelegatingEntityResolver
/*     */ {
/*  55 */   private static final Log logger = LogFactory.getLog(ResourceEntityResolver.class);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final ResourceLoader resourceLoader;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResourceEntityResolver(ResourceLoader resourceLoader) {
/*  67 */     super(resourceLoader.getClassLoader());
/*  68 */     this.resourceLoader = resourceLoader;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
/*  74 */     InputSource source = super.resolveEntity(publicId, systemId);
/*  75 */     if (source == null && systemId != null) {
/*  76 */       String resourcePath = null;
/*     */       try {
/*  78 */         String decodedSystemId = URLDecoder.decode(systemId, "UTF-8");
/*  79 */         String givenUrl = (new URL(decodedSystemId)).toString();
/*  80 */         String systemRootUrl = (new File("")).toURI().toURL().toString();
/*     */         
/*  82 */         if (givenUrl.startsWith(systemRootUrl)) {
/*  83 */           resourcePath = givenUrl.substring(systemRootUrl.length());
/*     */         }
/*     */       }
/*  86 */       catch (Exception ex) {
/*     */         
/*  88 */         if (logger.isDebugEnabled()) {
/*  89 */           logger.debug("Could not resolve XML entity [" + systemId + "] against system root URL", ex);
/*     */         }
/*     */         
/*  92 */         resourcePath = systemId;
/*     */       } 
/*  94 */       if (resourcePath != null) {
/*  95 */         if (logger.isTraceEnabled()) {
/*  96 */           logger.trace("Trying to locate XML entity [" + systemId + "] as resource [" + resourcePath + "]");
/*     */         }
/*  98 */         Resource resource = this.resourceLoader.getResource(resourcePath);
/*  99 */         source = new InputSource(resource.getInputStream());
/* 100 */         source.setPublicId(publicId);
/* 101 */         source.setSystemId(systemId);
/* 102 */         if (logger.isDebugEnabled()) {
/* 103 */           logger.debug("Found XML entity [" + systemId + "]: " + resource);
/*     */         }
/*     */       } 
/*     */     } 
/* 107 */     return source;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\xml\ResourceEntityResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */