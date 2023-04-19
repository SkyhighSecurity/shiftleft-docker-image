/*     */ package org.springframework.beans.factory.xml;
/*     */ 
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.w3c.dom.Document;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.ErrorHandler;
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
/*     */ public class DefaultDocumentLoader
/*     */   implements DocumentLoader
/*     */ {
/*     */   private static final String SCHEMA_LANGUAGE_ATTRIBUTE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
/*     */   private static final String XSD_SCHEMA_LANGUAGE = "http://www.w3.org/2001/XMLSchema";
/*  60 */   private static final Log logger = LogFactory.getLog(DefaultDocumentLoader.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Document loadDocument(InputSource inputSource, EntityResolver entityResolver, ErrorHandler errorHandler, int validationMode, boolean namespaceAware) throws Exception {
/*  71 */     DocumentBuilderFactory factory = createDocumentBuilderFactory(validationMode, namespaceAware);
/*  72 */     if (logger.isDebugEnabled()) {
/*  73 */       logger.debug("Using JAXP provider [" + factory.getClass().getName() + "]");
/*     */     }
/*  75 */     DocumentBuilder builder = createDocumentBuilder(factory, entityResolver, errorHandler);
/*  76 */     return builder.parse(inputSource);
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
/*     */   protected DocumentBuilderFactory createDocumentBuilderFactory(int validationMode, boolean namespaceAware) throws ParserConfigurationException {
/*  90 */     DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
/*  91 */     factory.setNamespaceAware(namespaceAware);
/*     */     
/*  93 */     if (validationMode != 0) {
/*  94 */       factory.setValidating(true);
/*  95 */       if (validationMode == 3) {
/*     */         
/*  97 */         factory.setNamespaceAware(true);
/*     */         try {
/*  99 */           factory.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema");
/*     */         }
/* 101 */         catch (IllegalArgumentException ex) {
/* 102 */           ParserConfigurationException pcex = new ParserConfigurationException("Unable to validate using XSD: Your JAXP provider [" + factory + "] does not support XML Schema. Are you running on Java 1.4 with Apache Crimson? Upgrade to Apache Xerces (or Java 1.5) for full XSD support.");
/*     */ 
/*     */ 
/*     */           
/* 106 */           pcex.initCause(ex);
/* 107 */           throw pcex;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 112 */     return factory;
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
/*     */   protected DocumentBuilder createDocumentBuilder(DocumentBuilderFactory factory, EntityResolver entityResolver, ErrorHandler errorHandler) throws ParserConfigurationException {
/* 130 */     DocumentBuilder docBuilder = factory.newDocumentBuilder();
/* 131 */     if (entityResolver != null) {
/* 132 */       docBuilder.setEntityResolver(entityResolver);
/*     */     }
/* 134 */     if (errorHandler != null) {
/* 135 */       docBuilder.setErrorHandler(errorHandler);
/*     */     }
/* 137 */     return docBuilder;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\xml\DefaultDocumentLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */