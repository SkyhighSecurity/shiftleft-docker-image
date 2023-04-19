/*    */ package org.springframework.beans.factory.xml;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ import org.springframework.core.io.ClassPathResource;
/*    */ import org.xml.sax.EntityResolver;
/*    */ import org.xml.sax.InputSource;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BeansDtdResolver
/*    */   implements EntityResolver
/*    */ {
/*    */   private static final String DTD_EXTENSION = ".dtd";
/*    */   private static final String DTD_FILENAME = "spring-beans-2.0";
/*    */   private static final String DTD_NAME = "spring-beans";
/* 51 */   private static final Log logger = LogFactory.getLog(BeansDtdResolver.class);
/*    */ 
/*    */ 
/*    */   
/*    */   public InputSource resolveEntity(String publicId, String systemId) throws IOException {
/* 56 */     if (logger.isTraceEnabled()) {
/* 57 */       logger.trace("Trying to resolve XML entity with public ID [" + publicId + "] and system ID [" + systemId + "]");
/*    */     }
/*    */     
/* 60 */     if (systemId != null && systemId.endsWith(".dtd")) {
/* 61 */       int lastPathSeparator = systemId.lastIndexOf('/');
/* 62 */       int dtdNameStart = systemId.indexOf("spring-beans", lastPathSeparator);
/* 63 */       if (dtdNameStart != -1) {
/* 64 */         String dtdFile = "spring-beans-2.0.dtd";
/* 65 */         if (logger.isTraceEnabled()) {
/* 66 */           logger.trace("Trying to locate [" + dtdFile + "] in Spring jar on classpath");
/*    */         }
/*    */         try {
/* 69 */           ClassPathResource classPathResource = new ClassPathResource(dtdFile, getClass());
/* 70 */           InputSource source = new InputSource(classPathResource.getInputStream());
/* 71 */           source.setPublicId(publicId);
/* 72 */           source.setSystemId(systemId);
/* 73 */           if (logger.isDebugEnabled()) {
/* 74 */             logger.debug("Found beans DTD [" + systemId + "] in classpath: " + dtdFile);
/*    */           }
/* 76 */           return source;
/*    */         }
/* 78 */         catch (IOException ex) {
/* 79 */           if (logger.isDebugEnabled()) {
/* 80 */             logger.debug("Could not resolve beans DTD [" + systemId + "]: not found in classpath", ex);
/*    */           }
/*    */         } 
/*    */       } 
/*    */     } 
/*    */ 
/*    */ 
/*    */     
/* 88 */     return null;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 94 */     return "EntityResolver for spring-beans DTD";
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\xml\BeansDtdResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */