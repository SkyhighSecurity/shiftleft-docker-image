/*     */ package org.springframework.util.xml;
/*     */ 
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.Location;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamReader;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.ext.Locator2;
/*     */ import org.xml.sax.helpers.AttributesImpl;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class StaxStreamXMLReader
/*     */   extends AbstractStaxXMLReader
/*     */ {
/*     */   private static final String DEFAULT_XML_VERSION = "1.0";
/*     */   private final XMLStreamReader reader;
/*  50 */   private String xmlVersion = "1.0";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String encoding;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   StaxStreamXMLReader(XMLStreamReader reader) {
/*  63 */     int event = reader.getEventType();
/*  64 */     if (event != 7 && event != 1) {
/*  65 */       throw new IllegalStateException("XMLEventReader not at start of document or element");
/*     */     }
/*  67 */     this.reader = reader;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void parseInternal() throws SAXException, XMLStreamException {
/*  73 */     boolean documentStarted = false;
/*  74 */     boolean documentEnded = false;
/*  75 */     int elementDepth = 0;
/*  76 */     int eventType = this.reader.getEventType();
/*     */     while (true) {
/*  78 */       if (eventType != 7 && eventType != 8 && !documentStarted) {
/*     */         
/*  80 */         handleStartDocument();
/*  81 */         documentStarted = true;
/*     */       } 
/*  83 */       switch (eventType) {
/*     */         case 1:
/*  85 */           elementDepth++;
/*  86 */           handleStartElement();
/*     */           break;
/*     */         case 2:
/*  89 */           elementDepth--;
/*  90 */           if (elementDepth >= 0) {
/*  91 */             handleEndElement();
/*     */           }
/*     */           break;
/*     */         case 3:
/*  95 */           handleProcessingInstruction();
/*     */           break;
/*     */         case 4:
/*     */         case 6:
/*     */         case 12:
/* 100 */           handleCharacters();
/*     */           break;
/*     */         case 7:
/* 103 */           handleStartDocument();
/* 104 */           documentStarted = true;
/*     */           break;
/*     */         case 8:
/* 107 */           handleEndDocument();
/* 108 */           documentEnded = true;
/*     */           break;
/*     */         case 5:
/* 111 */           handleComment();
/*     */           break;
/*     */         case 11:
/* 114 */           handleDtd();
/*     */           break;
/*     */         case 9:
/* 117 */           handleEntityReference();
/*     */           break;
/*     */       } 
/* 120 */       if (this.reader.hasNext() && elementDepth >= 0) {
/* 121 */         eventType = this.reader.next();
/*     */         
/*     */         continue;
/*     */       } 
/*     */       break;
/*     */     } 
/* 127 */     if (!documentEnded) {
/* 128 */       handleEndDocument();
/*     */     }
/*     */   }
/*     */   
/*     */   private void handleStartDocument() throws SAXException {
/* 133 */     if (7 == this.reader.getEventType()) {
/* 134 */       String xmlVersion = this.reader.getVersion();
/* 135 */       if (StringUtils.hasLength(xmlVersion)) {
/* 136 */         this.xmlVersion = xmlVersion;
/*     */       }
/* 138 */       this.encoding = this.reader.getCharacterEncodingScheme();
/*     */     } 
/* 140 */     if (getContentHandler() != null) {
/* 141 */       final Location location = this.reader.getLocation();
/* 142 */       getContentHandler().setDocumentLocator(new Locator2()
/*     */           {
/*     */             public int getColumnNumber() {
/* 145 */               return (location != null) ? location.getColumnNumber() : -1;
/*     */             }
/*     */             
/*     */             public int getLineNumber() {
/* 149 */               return (location != null) ? location.getLineNumber() : -1;
/*     */             }
/*     */             
/*     */             public String getPublicId() {
/* 153 */               return (location != null) ? location.getPublicId() : null;
/*     */             }
/*     */             
/*     */             public String getSystemId() {
/* 157 */               return (location != null) ? location.getSystemId() : null;
/*     */             }
/*     */             
/*     */             public String getXMLVersion() {
/* 161 */               return StaxStreamXMLReader.this.xmlVersion;
/*     */             }
/*     */             
/*     */             public String getEncoding() {
/* 165 */               return StaxStreamXMLReader.this.encoding;
/*     */             }
/*     */           });
/* 168 */       getContentHandler().startDocument();
/* 169 */       if (this.reader.standaloneSet()) {
/* 170 */         setStandalone(this.reader.isStandalone());
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void handleStartElement() throws SAXException {
/* 176 */     if (getContentHandler() != null) {
/* 177 */       QName qName = this.reader.getName();
/* 178 */       if (hasNamespacesFeature()) {
/* 179 */         int i; for (i = 0; i < this.reader.getNamespaceCount(); i++) {
/* 180 */           startPrefixMapping(this.reader.getNamespacePrefix(i), this.reader.getNamespaceURI(i));
/*     */         }
/* 182 */         for (i = 0; i < this.reader.getAttributeCount(); i++) {
/* 183 */           String prefix = this.reader.getAttributePrefix(i);
/* 184 */           String namespace = this.reader.getAttributeNamespace(i);
/* 185 */           if (StringUtils.hasLength(namespace)) {
/* 186 */             startPrefixMapping(prefix, namespace);
/*     */           }
/*     */         } 
/* 189 */         getContentHandler().startElement(qName.getNamespaceURI(), qName.getLocalPart(), 
/* 190 */             toQualifiedName(qName), getAttributes());
/*     */       } else {
/*     */         
/* 193 */         getContentHandler().startElement("", "", toQualifiedName(qName), getAttributes());
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void handleEndElement() throws SAXException {
/* 199 */     if (getContentHandler() != null) {
/* 200 */       QName qName = this.reader.getName();
/* 201 */       if (hasNamespacesFeature()) {
/* 202 */         getContentHandler().endElement(qName.getNamespaceURI(), qName.getLocalPart(), toQualifiedName(qName));
/* 203 */         for (int i = 0; i < this.reader.getNamespaceCount(); i++) {
/* 204 */           String prefix = this.reader.getNamespacePrefix(i);
/* 205 */           if (prefix == null) {
/* 206 */             prefix = "";
/*     */           }
/* 208 */           endPrefixMapping(prefix);
/*     */         } 
/*     */       } else {
/*     */         
/* 212 */         getContentHandler().endElement("", "", toQualifiedName(qName));
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void handleCharacters() throws SAXException {
/* 218 */     if (12 == this.reader.getEventType() && getLexicalHandler() != null) {
/* 219 */       getLexicalHandler().startCDATA();
/*     */     }
/* 221 */     if (getContentHandler() != null) {
/* 222 */       getContentHandler().characters(this.reader.getTextCharacters(), this.reader
/* 223 */           .getTextStart(), this.reader.getTextLength());
/*     */     }
/* 225 */     if (12 == this.reader.getEventType() && getLexicalHandler() != null) {
/* 226 */       getLexicalHandler().endCDATA();
/*     */     }
/*     */   }
/*     */   
/*     */   private void handleComment() throws SAXException {
/* 231 */     if (getLexicalHandler() != null) {
/* 232 */       getLexicalHandler().comment(this.reader.getTextCharacters(), this.reader
/* 233 */           .getTextStart(), this.reader.getTextLength());
/*     */     }
/*     */   }
/*     */   
/*     */   private void handleDtd() throws SAXException {
/* 238 */     if (getLexicalHandler() != null) {
/* 239 */       Location location = this.reader.getLocation();
/* 240 */       getLexicalHandler().startDTD(null, location.getPublicId(), location.getSystemId());
/*     */     } 
/* 242 */     if (getLexicalHandler() != null) {
/* 243 */       getLexicalHandler().endDTD();
/*     */     }
/*     */   }
/*     */   
/*     */   private void handleEntityReference() throws SAXException {
/* 248 */     if (getLexicalHandler() != null) {
/* 249 */       getLexicalHandler().startEntity(this.reader.getLocalName());
/*     */     }
/* 251 */     if (getLexicalHandler() != null) {
/* 252 */       getLexicalHandler().endEntity(this.reader.getLocalName());
/*     */     }
/*     */   }
/*     */   
/*     */   private void handleEndDocument() throws SAXException {
/* 257 */     if (getContentHandler() != null) {
/* 258 */       getContentHandler().endDocument();
/*     */     }
/*     */   }
/*     */   
/*     */   private void handleProcessingInstruction() throws SAXException {
/* 263 */     if (getContentHandler() != null) {
/* 264 */       getContentHandler().processingInstruction(this.reader.getPITarget(), this.reader.getPIData());
/*     */     }
/*     */   }
/*     */   
/*     */   private Attributes getAttributes() {
/* 269 */     AttributesImpl attributes = new AttributesImpl(); int i;
/* 270 */     for (i = 0; i < this.reader.getAttributeCount(); i++) {
/* 271 */       String namespace = this.reader.getAttributeNamespace(i);
/* 272 */       if (namespace == null || !hasNamespacesFeature()) {
/* 273 */         namespace = "";
/*     */       }
/* 275 */       String type = this.reader.getAttributeType(i);
/* 276 */       if (type == null) {
/* 277 */         type = "CDATA";
/*     */       }
/* 279 */       attributes.addAttribute(namespace, this.reader.getAttributeLocalName(i), 
/* 280 */           toQualifiedName(this.reader.getAttributeName(i)), type, this.reader.getAttributeValue(i));
/*     */     } 
/* 282 */     if (hasNamespacePrefixesFeature()) {
/* 283 */       for (i = 0; i < this.reader.getNamespaceCount(); i++) {
/* 284 */         String qName, prefix = this.reader.getNamespacePrefix(i);
/* 285 */         String namespaceUri = this.reader.getNamespaceURI(i);
/*     */         
/* 287 */         if (StringUtils.hasLength(prefix)) {
/* 288 */           qName = "xmlns:" + prefix;
/*     */         } else {
/*     */           
/* 291 */           qName = "xmlns";
/*     */         } 
/* 293 */         attributes.addAttribute("", "", qName, "CDATA", namespaceUri);
/*     */       } 
/*     */     }
/*     */     
/* 297 */     return attributes;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframewor\\util\xml\StaxStreamXMLReader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */