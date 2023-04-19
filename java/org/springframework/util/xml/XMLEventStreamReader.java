/*     */ package org.springframework.util.xml;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import javax.xml.namespace.NamespaceContext;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.Location;
/*     */ import javax.xml.stream.XMLEventReader;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.events.Attribute;
/*     */ import javax.xml.stream.events.Comment;
/*     */ import javax.xml.stream.events.Namespace;
/*     */ import javax.xml.stream.events.ProcessingInstruction;
/*     */ import javax.xml.stream.events.StartDocument;
/*     */ import javax.xml.stream.events.XMLEvent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class XMLEventStreamReader
/*     */   extends AbstractXMLStreamReader
/*     */ {
/*     */   private XMLEvent event;
/*     */   private final XMLEventReader eventReader;
/*     */   
/*     */   public XMLEventStreamReader(XMLEventReader eventReader) throws XMLStreamException {
/*  49 */     this.eventReader = eventReader;
/*  50 */     this.event = eventReader.nextEvent();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public QName getName() {
/*  56 */     if (this.event.isStartElement()) {
/*  57 */       return this.event.asStartElement().getName();
/*     */     }
/*  59 */     if (this.event.isEndElement()) {
/*  60 */       return this.event.asEndElement().getName();
/*     */     }
/*     */     
/*  63 */     throw new IllegalStateException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Location getLocation() {
/*  69 */     return this.event.getLocation();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getEventType() {
/*  74 */     return this.event.getEventType();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getVersion() {
/*  79 */     if (this.event.isStartDocument()) {
/*  80 */       return ((StartDocument)this.event).getVersion();
/*     */     }
/*     */     
/*  83 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getProperty(String name) throws IllegalArgumentException {
/*  89 */     return this.eventReader.getProperty(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isStandalone() {
/*  94 */     if (this.event.isStartDocument()) {
/*  95 */       return ((StartDocument)this.event).isStandalone();
/*     */     }
/*     */     
/*  98 */     throw new IllegalStateException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean standaloneSet() {
/* 104 */     if (this.event.isStartDocument()) {
/* 105 */       return ((StartDocument)this.event).standaloneSet();
/*     */     }
/*     */     
/* 108 */     throw new IllegalStateException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getEncoding() {
/* 114 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getCharacterEncodingScheme() {
/* 119 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getPITarget() {
/* 124 */     if (this.event.isProcessingInstruction()) {
/* 125 */       return ((ProcessingInstruction)this.event).getTarget();
/*     */     }
/*     */     
/* 128 */     throw new IllegalStateException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPIData() {
/* 134 */     if (this.event.isProcessingInstruction()) {
/* 135 */       return ((ProcessingInstruction)this.event).getData();
/*     */     }
/*     */     
/* 138 */     throw new IllegalStateException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getTextStart() {
/* 144 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getText() {
/* 149 */     if (this.event.isCharacters()) {
/* 150 */       return this.event.asCharacters().getData();
/*     */     }
/* 152 */     if (this.event.getEventType() == 5) {
/* 153 */       return ((Comment)this.event).getText();
/*     */     }
/*     */     
/* 156 */     throw new IllegalStateException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getAttributeCount() {
/* 163 */     if (!this.event.isStartElement()) {
/* 164 */       throw new IllegalStateException();
/*     */     }
/* 166 */     Iterator attributes = this.event.asStartElement().getAttributes();
/* 167 */     return countIterator(attributes);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAttributeSpecified(int index) {
/* 172 */     return getAttribute(index).isSpecified();
/*     */   }
/*     */ 
/*     */   
/*     */   public QName getAttributeName(int index) {
/* 177 */     return getAttribute(index).getName();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getAttributeType(int index) {
/* 182 */     return getAttribute(index).getDTDType();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getAttributeValue(int index) {
/* 187 */     return getAttribute(index).getValue();
/*     */   }
/*     */ 
/*     */   
/*     */   private Attribute getAttribute(int index) {
/* 192 */     if (!this.event.isStartElement()) {
/* 193 */       throw new IllegalStateException();
/*     */     }
/* 195 */     int count = 0;
/* 196 */     Iterator<Attribute> attributes = this.event.asStartElement().getAttributes();
/* 197 */     while (attributes.hasNext()) {
/* 198 */       Attribute attribute = attributes.next();
/* 199 */       if (count == index) {
/* 200 */         return attribute;
/*     */       }
/*     */       
/* 203 */       count++;
/*     */     } 
/*     */     
/* 206 */     throw new IllegalArgumentException();
/*     */   }
/*     */ 
/*     */   
/*     */   public NamespaceContext getNamespaceContext() {
/* 211 */     if (this.event.isStartElement()) {
/* 212 */       return this.event.asStartElement().getNamespaceContext();
/*     */     }
/*     */     
/* 215 */     throw new IllegalStateException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getNamespaceCount() {
/*     */     Iterator namespaces;
/* 223 */     if (this.event.isStartElement()) {
/* 224 */       namespaces = this.event.asStartElement().getNamespaces();
/*     */     }
/* 226 */     else if (this.event.isEndElement()) {
/* 227 */       namespaces = this.event.asEndElement().getNamespaces();
/*     */     } else {
/*     */       
/* 230 */       throw new IllegalStateException();
/*     */     } 
/* 232 */     return countIterator(namespaces);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getNamespacePrefix(int index) {
/* 237 */     return getNamespace(index).getPrefix();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getNamespaceURI(int index) {
/* 242 */     return getNamespace(index).getNamespaceURI();
/*     */   }
/*     */ 
/*     */   
/*     */   private Namespace getNamespace(int index) {
/*     */     Iterator<Namespace> namespaces;
/* 248 */     if (this.event.isStartElement()) {
/* 249 */       namespaces = this.event.asStartElement().getNamespaces();
/*     */     }
/* 251 */     else if (this.event.isEndElement()) {
/* 252 */       namespaces = this.event.asEndElement().getNamespaces();
/*     */     } else {
/*     */       
/* 255 */       throw new IllegalStateException();
/*     */     } 
/* 257 */     int count = 0;
/* 258 */     while (namespaces.hasNext()) {
/* 259 */       Namespace namespace = namespaces.next();
/* 260 */       if (count == index) {
/* 261 */         return namespace;
/*     */       }
/*     */       
/* 264 */       count++;
/*     */     } 
/*     */     
/* 267 */     throw new IllegalArgumentException();
/*     */   }
/*     */ 
/*     */   
/*     */   public int next() throws XMLStreamException {
/* 272 */     this.event = this.eventReader.nextEvent();
/* 273 */     return this.event.getEventType();
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws XMLStreamException {
/* 278 */     this.eventReader.close();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static int countIterator(Iterator iterator) {
/* 284 */     int count = 0;
/* 285 */     while (iterator.hasNext()) {
/* 286 */       iterator.next();
/* 287 */       count++;
/*     */     } 
/* 289 */     return count;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframewor\\util\xml\XMLEventStreamReader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */