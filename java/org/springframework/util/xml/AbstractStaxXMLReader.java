/*     */ package org.springframework.util.xml;
/*     */ 
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.Location;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXNotRecognizedException;
/*     */ import org.xml.sax.SAXNotSupportedException;
/*     */ import org.xml.sax.SAXParseException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class AbstractStaxXMLReader
/*     */   extends AbstractXMLReader
/*     */ {
/*     */   private static final String NAMESPACES_FEATURE_NAME = "http://xml.org/sax/features/namespaces";
/*     */   private static final String NAMESPACE_PREFIXES_FEATURE_NAME = "http://xml.org/sax/features/namespace-prefixes";
/*     */   private static final String IS_STANDALONE_FEATURE_NAME = "http://xml.org/sax/features/is-standalone";
/*     */   private boolean namespacesFeature = true;
/*     */   private boolean namespacePrefixesFeature = false;
/*     */   private Boolean isStandalone;
/*  60 */   private final Map<String, String> namespaces = new LinkedHashMap<String, String>();
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getFeature(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
/*  65 */     if ("http://xml.org/sax/features/namespaces".equals(name)) {
/*  66 */       return this.namespacesFeature;
/*     */     }
/*  68 */     if ("http://xml.org/sax/features/namespace-prefixes".equals(name)) {
/*  69 */       return this.namespacePrefixesFeature;
/*     */     }
/*  71 */     if ("http://xml.org/sax/features/is-standalone".equals(name)) {
/*  72 */       if (this.isStandalone != null) {
/*  73 */         return this.isStandalone.booleanValue();
/*     */       }
/*     */       
/*  76 */       throw new SAXNotSupportedException("startDocument() callback not completed yet");
/*     */     } 
/*     */ 
/*     */     
/*  80 */     return super.getFeature(name);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFeature(String name, boolean value) throws SAXNotRecognizedException, SAXNotSupportedException {
/*  86 */     if ("http://xml.org/sax/features/namespaces".equals(name)) {
/*  87 */       this.namespacesFeature = value;
/*     */     }
/*  89 */     else if ("http://xml.org/sax/features/namespace-prefixes".equals(name)) {
/*  90 */       this.namespacePrefixesFeature = value;
/*     */     } else {
/*     */       
/*  93 */       super.setFeature(name, value);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void setStandalone(boolean standalone) {
/*  98 */     this.isStandalone = Boolean.valueOf(standalone);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean hasNamespacesFeature() {
/* 105 */     return this.namespacesFeature;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean hasNamespacePrefixesFeature() {
/* 112 */     return this.namespacePrefixesFeature;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String toQualifiedName(QName qName) {
/* 123 */     String prefix = qName.getPrefix();
/* 124 */     if (!StringUtils.hasLength(prefix)) {
/* 125 */       return qName.getLocalPart();
/*     */     }
/*     */     
/* 128 */     return prefix + ":" + qName.getLocalPart();
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
/*     */   public final void parse(InputSource ignored) throws SAXException {
/* 141 */     parse();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void parse(String ignored) throws SAXException {
/* 152 */     parse();
/*     */   }
/*     */   
/*     */   private void parse() throws SAXException {
/*     */     try {
/* 157 */       parseInternal();
/*     */     }
/* 159 */     catch (XMLStreamException ex) {
/* 160 */       Locator locator = null;
/* 161 */       if (ex.getLocation() != null) {
/* 162 */         locator = new StaxLocator(ex.getLocation());
/*     */       }
/* 164 */       SAXParseException saxException = new SAXParseException(ex.getMessage(), locator, ex);
/* 165 */       if (getErrorHandler() != null) {
/* 166 */         getErrorHandler().fatalError(saxException);
/*     */       } else {
/*     */         
/* 169 */         throw saxException;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void parseInternal() throws SAXException, XMLStreamException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void startPrefixMapping(String prefix, String namespace) throws SAXException {
/* 185 */     if (getContentHandler() != null && StringUtils.hasLength(namespace)) {
/* 186 */       if (prefix == null) {
/* 187 */         prefix = "";
/*     */       }
/* 189 */       if (!namespace.equals(this.namespaces.get(prefix))) {
/* 190 */         getContentHandler().startPrefixMapping(prefix, namespace);
/* 191 */         this.namespaces.put(prefix, namespace);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void endPrefixMapping(String prefix) throws SAXException {
/* 201 */     if (getContentHandler() != null && this.namespaces.containsKey(prefix)) {
/* 202 */       getContentHandler().endPrefixMapping(prefix);
/* 203 */       this.namespaces.remove(prefix);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class StaxLocator
/*     */     implements Locator
/*     */   {
/*     */     private final Location location;
/*     */ 
/*     */ 
/*     */     
/*     */     public StaxLocator(Location location) {
/* 218 */       this.location = location;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getPublicId() {
/* 223 */       return this.location.getPublicId();
/*     */     }
/*     */ 
/*     */     
/*     */     public String getSystemId() {
/* 228 */       return this.location.getSystemId();
/*     */     }
/*     */ 
/*     */     
/*     */     public int getLineNumber() {
/* 233 */       return this.location.getLineNumber();
/*     */     }
/*     */ 
/*     */     
/*     */     public int getColumnNumber() {
/* 238 */       return this.location.getColumnNumber();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframewor\\util\xml\AbstractStaxXMLReader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */