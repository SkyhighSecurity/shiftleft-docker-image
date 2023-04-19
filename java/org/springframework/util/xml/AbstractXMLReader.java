/*     */ package org.springframework.util.xml;
/*     */ 
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.DTDHandler;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.SAXNotRecognizedException;
/*     */ import org.xml.sax.SAXNotSupportedException;
/*     */ import org.xml.sax.XMLReader;
/*     */ import org.xml.sax.ext.LexicalHandler;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class AbstractXMLReader
/*     */   implements XMLReader
/*     */ {
/*     */   private DTDHandler dtdHandler;
/*     */   private ContentHandler contentHandler;
/*     */   private EntityResolver entityResolver;
/*     */   private ErrorHandler errorHandler;
/*     */   private LexicalHandler lexicalHandler;
/*     */   
/*     */   public void setContentHandler(ContentHandler contentHandler) {
/*  55 */     this.contentHandler = contentHandler;
/*     */   }
/*     */ 
/*     */   
/*     */   public ContentHandler getContentHandler() {
/*  60 */     return this.contentHandler;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDTDHandler(DTDHandler dtdHandler) {
/*  65 */     this.dtdHandler = dtdHandler;
/*     */   }
/*     */ 
/*     */   
/*     */   public DTDHandler getDTDHandler() {
/*  70 */     return this.dtdHandler;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setEntityResolver(EntityResolver entityResolver) {
/*  75 */     this.entityResolver = entityResolver;
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityResolver getEntityResolver() {
/*  80 */     return this.entityResolver;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setErrorHandler(ErrorHandler errorHandler) {
/*  85 */     this.errorHandler = errorHandler;
/*     */   }
/*     */ 
/*     */   
/*     */   public ErrorHandler getErrorHandler() {
/*  90 */     return this.errorHandler;
/*     */   }
/*     */   
/*     */   protected LexicalHandler getLexicalHandler() {
/*  94 */     return this.lexicalHandler;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getFeature(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
/* 105 */     if (name.startsWith("http://xml.org/sax/features/")) {
/* 106 */       return false;
/*     */     }
/*     */     
/* 109 */     throw new SAXNotRecognizedException(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFeature(String name, boolean value) throws SAXNotRecognizedException, SAXNotSupportedException {
/* 120 */     if (name.startsWith("http://xml.org/sax/features/")) {
/* 121 */       if (value) {
/* 122 */         throw new SAXNotSupportedException(name);
/*     */       }
/*     */     } else {
/*     */       
/* 126 */       throw new SAXNotRecognizedException(name);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getProperty(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
/* 136 */     if ("http://xml.org/sax/properties/lexical-handler".equals(name)) {
/* 137 */       return this.lexicalHandler;
/*     */     }
/*     */     
/* 140 */     throw new SAXNotRecognizedException(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProperty(String name, Object value) throws SAXNotRecognizedException, SAXNotSupportedException {
/* 150 */     if ("http://xml.org/sax/properties/lexical-handler".equals(name)) {
/* 151 */       this.lexicalHandler = (LexicalHandler)value;
/*     */     } else {
/*     */       
/* 154 */       throw new SAXNotRecognizedException(name);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframewor\\util\xml\AbstractXMLReader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */