/*     */ package org.springframework.util.xml;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.Location;
/*     */ import javax.xml.stream.XMLEventReader;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.events.Attribute;
/*     */ import javax.xml.stream.events.Characters;
/*     */ import javax.xml.stream.events.Comment;
/*     */ import javax.xml.stream.events.DTD;
/*     */ import javax.xml.stream.events.EndElement;
/*     */ import javax.xml.stream.events.EntityDeclaration;
/*     */ import javax.xml.stream.events.EntityReference;
/*     */ import javax.xml.stream.events.Namespace;
/*     */ import javax.xml.stream.events.NotationDeclaration;
/*     */ import javax.xml.stream.events.ProcessingInstruction;
/*     */ import javax.xml.stream.events.StartDocument;
/*     */ import javax.xml.stream.events.StartElement;
/*     */ import javax.xml.stream.events.XMLEvent;
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
/*     */ 
/*     */ class StaxEventXMLReader
/*     */   extends AbstractStaxXMLReader
/*     */ {
/*     */   private static final String DEFAULT_XML_VERSION = "1.0";
/*     */   private final XMLEventReader reader;
/*  65 */   private String xmlVersion = "1.0";
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
/*     */   StaxEventXMLReader(XMLEventReader reader) {
/*     */     try {
/*  79 */       XMLEvent event = reader.peek();
/*  80 */       if (event != null && !event.isStartDocument() && !event.isStartElement()) {
/*  81 */         throw new IllegalStateException("XMLEventReader not at start of document or element");
/*     */       }
/*     */     }
/*  84 */     catch (XMLStreamException ex) {
/*  85 */       throw new IllegalStateException("Could not read first element: " + ex.getMessage());
/*     */     } 
/*  87 */     this.reader = reader;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void parseInternal() throws SAXException, XMLStreamException {
/*  93 */     boolean documentStarted = false;
/*  94 */     boolean documentEnded = false;
/*  95 */     int elementDepth = 0;
/*  96 */     while (this.reader.hasNext() && elementDepth >= 0) {
/*  97 */       XMLEvent event = this.reader.nextEvent();
/*  98 */       if (!event.isStartDocument() && !event.isEndDocument() && !documentStarted) {
/*  99 */         handleStartDocument(event);
/* 100 */         documentStarted = true;
/*     */       } 
/* 102 */       switch (event.getEventType()) {
/*     */         case 7:
/* 104 */           handleStartDocument(event);
/* 105 */           documentStarted = true;
/*     */         
/*     */         case 1:
/* 108 */           elementDepth++;
/* 109 */           handleStartElement(event.asStartElement());
/*     */         
/*     */         case 2:
/* 112 */           elementDepth--;
/* 113 */           if (elementDepth >= 0) {
/* 114 */             handleEndElement(event.asEndElement());
/*     */           }
/*     */         
/*     */         case 3:
/* 118 */           handleProcessingInstruction((ProcessingInstruction)event);
/*     */         
/*     */         case 4:
/*     */         case 6:
/*     */         case 12:
/* 123 */           handleCharacters(event.asCharacters());
/*     */         
/*     */         case 8:
/* 126 */           handleEndDocument();
/* 127 */           documentEnded = true;
/*     */         
/*     */         case 14:
/* 130 */           handleNotationDeclaration((NotationDeclaration)event);
/*     */         
/*     */         case 15:
/* 133 */           handleEntityDeclaration((EntityDeclaration)event);
/*     */         
/*     */         case 5:
/* 136 */           handleComment((Comment)event);
/*     */         
/*     */         case 11:
/* 139 */           handleDtd((DTD)event);
/*     */         
/*     */         case 9:
/* 142 */           handleEntityReference((EntityReference)event);
/*     */       } 
/*     */     
/*     */     } 
/* 146 */     if (documentStarted && !documentEnded) {
/* 147 */       handleEndDocument();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void handleStartDocument(XMLEvent event) throws SAXException {
/* 153 */     if (event.isStartDocument()) {
/* 154 */       StartDocument startDocument = (StartDocument)event;
/* 155 */       String xmlVersion = startDocument.getVersion();
/* 156 */       if (StringUtils.hasLength(xmlVersion)) {
/* 157 */         this.xmlVersion = xmlVersion;
/*     */       }
/* 159 */       if (startDocument.encodingSet()) {
/* 160 */         this.encoding = startDocument.getCharacterEncodingScheme();
/*     */       }
/*     */     } 
/* 163 */     if (getContentHandler() != null) {
/* 164 */       final Location location = event.getLocation();
/* 165 */       getContentHandler().setDocumentLocator(new Locator2()
/*     */           {
/*     */             public int getColumnNumber() {
/* 168 */               return (location != null) ? location.getColumnNumber() : -1;
/*     */             }
/*     */             
/*     */             public int getLineNumber() {
/* 172 */               return (location != null) ? location.getLineNumber() : -1;
/*     */             }
/*     */             
/*     */             public String getPublicId() {
/* 176 */               return (location != null) ? location.getPublicId() : null;
/*     */             }
/*     */             
/*     */             public String getSystemId() {
/* 180 */               return (location != null) ? location.getSystemId() : null;
/*     */             }
/*     */             
/*     */             public String getXMLVersion() {
/* 184 */               return StaxEventXMLReader.this.xmlVersion;
/*     */             }
/*     */             
/*     */             public String getEncoding() {
/* 188 */               return StaxEventXMLReader.this.encoding;
/*     */             }
/*     */           });
/* 191 */       getContentHandler().startDocument();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void handleStartElement(StartElement startElement) throws SAXException {
/* 196 */     if (getContentHandler() != null) {
/* 197 */       QName qName = startElement.getName();
/* 198 */       if (hasNamespacesFeature()) {
/* 199 */         for (Iterator<Namespace> iterator = startElement.getNamespaces(); iterator.hasNext(); ) {
/* 200 */           Namespace namespace = iterator.next();
/* 201 */           startPrefixMapping(namespace.getPrefix(), namespace.getNamespaceURI());
/*     */         } 
/* 203 */         for (Iterator<Attribute> i = startElement.getAttributes(); i.hasNext(); ) {
/* 204 */           Attribute attribute = i.next();
/* 205 */           QName attributeName = attribute.getName();
/* 206 */           startPrefixMapping(attributeName.getPrefix(), attributeName.getNamespaceURI());
/*     */         } 
/*     */         
/* 209 */         getContentHandler().startElement(qName.getNamespaceURI(), qName.getLocalPart(), toQualifiedName(qName), 
/* 210 */             getAttributes(startElement));
/*     */       } else {
/*     */         
/* 213 */         getContentHandler().startElement("", "", toQualifiedName(qName), getAttributes(startElement));
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void handleCharacters(Characters characters) throws SAXException {
/* 219 */     char[] data = characters.getData().toCharArray();
/* 220 */     if (getContentHandler() != null && characters.isIgnorableWhiteSpace()) {
/* 221 */       getContentHandler().ignorableWhitespace(data, 0, data.length);
/*     */       return;
/*     */     } 
/* 224 */     if (characters.isCData() && getLexicalHandler() != null) {
/* 225 */       getLexicalHandler().startCDATA();
/*     */     }
/* 227 */     if (getContentHandler() != null) {
/* 228 */       getContentHandler().characters(data, 0, data.length);
/*     */     }
/* 230 */     if (characters.isCData() && getLexicalHandler() != null) {
/* 231 */       getLexicalHandler().endCDATA();
/*     */     }
/*     */   }
/*     */   
/*     */   private void handleEndElement(EndElement endElement) throws SAXException {
/* 236 */     if (getContentHandler() != null) {
/* 237 */       QName qName = endElement.getName();
/* 238 */       if (hasNamespacesFeature()) {
/* 239 */         getContentHandler().endElement(qName.getNamespaceURI(), qName.getLocalPart(), toQualifiedName(qName));
/* 240 */         for (Iterator<Namespace> i = endElement.getNamespaces(); i.hasNext(); ) {
/* 241 */           Namespace namespace = i.next();
/* 242 */           endPrefixMapping(namespace.getPrefix());
/*     */         } 
/*     */       } else {
/*     */         
/* 246 */         getContentHandler().endElement("", "", toQualifiedName(qName));
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void handleEndDocument() throws SAXException {
/* 253 */     if (getContentHandler() != null) {
/* 254 */       getContentHandler().endDocument();
/*     */     }
/*     */   }
/*     */   
/*     */   private void handleNotationDeclaration(NotationDeclaration declaration) throws SAXException {
/* 259 */     if (getDTDHandler() != null) {
/* 260 */       getDTDHandler().notationDecl(declaration.getName(), declaration.getPublicId(), declaration.getSystemId());
/*     */     }
/*     */   }
/*     */   
/*     */   private void handleEntityDeclaration(EntityDeclaration entityDeclaration) throws SAXException {
/* 265 */     if (getDTDHandler() != null) {
/* 266 */       getDTDHandler().unparsedEntityDecl(entityDeclaration.getName(), entityDeclaration.getPublicId(), entityDeclaration
/* 267 */           .getSystemId(), entityDeclaration.getNotationName());
/*     */     }
/*     */   }
/*     */   
/*     */   private void handleProcessingInstruction(ProcessingInstruction pi) throws SAXException {
/* 272 */     if (getContentHandler() != null) {
/* 273 */       getContentHandler().processingInstruction(pi.getTarget(), pi.getData());
/*     */     }
/*     */   }
/*     */   
/*     */   private void handleComment(Comment comment) throws SAXException {
/* 278 */     if (getLexicalHandler() != null) {
/* 279 */       char[] ch = comment.getText().toCharArray();
/* 280 */       getLexicalHandler().comment(ch, 0, ch.length);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void handleDtd(DTD dtd) throws SAXException {
/* 285 */     if (getLexicalHandler() != null) {
/* 286 */       Location location = dtd.getLocation();
/* 287 */       getLexicalHandler().startDTD(null, location.getPublicId(), location.getSystemId());
/*     */     } 
/* 289 */     if (getLexicalHandler() != null) {
/* 290 */       getLexicalHandler().endDTD();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void handleEntityReference(EntityReference reference) throws SAXException {
/* 296 */     if (getLexicalHandler() != null) {
/* 297 */       getLexicalHandler().startEntity(reference.getName());
/*     */     }
/* 299 */     if (getLexicalHandler() != null) {
/* 300 */       getLexicalHandler().endEntity(reference.getName());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private Attributes getAttributes(StartElement event) {
/* 306 */     AttributesImpl attributes = new AttributesImpl();
/* 307 */     for (Iterator<Attribute> i = event.getAttributes(); i.hasNext(); ) {
/* 308 */       Attribute attribute = i.next();
/* 309 */       QName qName = attribute.getName();
/* 310 */       String namespace = qName.getNamespaceURI();
/* 311 */       if (namespace == null || !hasNamespacesFeature()) {
/* 312 */         namespace = "";
/*     */       }
/* 314 */       String type = attribute.getDTDType();
/* 315 */       if (type == null) {
/* 316 */         type = "CDATA";
/*     */       }
/* 318 */       attributes.addAttribute(namespace, qName.getLocalPart(), toQualifiedName(qName), type, attribute.getValue());
/*     */     } 
/* 320 */     if (hasNamespacePrefixesFeature()) {
/* 321 */       for (Iterator<Namespace> iterator = event.getNamespaces(); iterator.hasNext(); ) {
/* 322 */         String qName; Namespace namespace = iterator.next();
/* 323 */         String prefix = namespace.getPrefix();
/* 324 */         String namespaceUri = namespace.getNamespaceURI();
/*     */         
/* 326 */         if (StringUtils.hasLength(prefix)) {
/* 327 */           qName = "xmlns:" + prefix;
/*     */         } else {
/*     */           
/* 330 */           qName = "xmlns";
/*     */         } 
/* 332 */         attributes.addAttribute("", "", qName, "CDATA", namespaceUri);
/*     */       } 
/*     */     }
/*     */     
/* 336 */     return attributes;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframewor\\util\xml\StaxEventXMLReader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */