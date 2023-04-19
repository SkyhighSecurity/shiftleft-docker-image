/*     */ package org.springframework.util.xml;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import javax.xml.namespace.NamespaceContext;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.XMLEventFactory;
/*     */ import javax.xml.stream.XMLEventWriter;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamWriter;
/*     */ import javax.xml.stream.events.EndElement;
/*     */ import javax.xml.stream.events.Namespace;
/*     */ import javax.xml.stream.events.StartElement;
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
/*     */ class XMLEventStreamWriter
/*     */   implements XMLStreamWriter
/*     */ {
/*     */   private static final String DEFAULT_ENCODING = "UTF-8";
/*     */   private final XMLEventWriter eventWriter;
/*     */   private final XMLEventFactory eventFactory;
/*  48 */   private final List<EndElement> endElements = new ArrayList<EndElement>();
/*     */   
/*     */   private boolean emptyElement = false;
/*     */ 
/*     */   
/*     */   public XMLEventStreamWriter(XMLEventWriter eventWriter, XMLEventFactory eventFactory) {
/*  54 */     this.eventWriter = eventWriter;
/*  55 */     this.eventFactory = eventFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNamespaceContext(NamespaceContext context) throws XMLStreamException {
/*  61 */     this.eventWriter.setNamespaceContext(context);
/*     */   }
/*     */ 
/*     */   
/*     */   public NamespaceContext getNamespaceContext() {
/*  66 */     return this.eventWriter.getNamespaceContext();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPrefix(String prefix, String uri) throws XMLStreamException {
/*  71 */     this.eventWriter.setPrefix(prefix, uri);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getPrefix(String uri) throws XMLStreamException {
/*  76 */     return this.eventWriter.getPrefix(uri);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDefaultNamespace(String uri) throws XMLStreamException {
/*  81 */     this.eventWriter.setDefaultNamespace(uri);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getProperty(String name) throws IllegalArgumentException {
/*  86 */     throw new IllegalArgumentException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeStartDocument() throws XMLStreamException {
/*  92 */     closeEmptyElementIfNecessary();
/*  93 */     this.eventWriter.add(this.eventFactory.createStartDocument());
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeStartDocument(String version) throws XMLStreamException {
/*  98 */     closeEmptyElementIfNecessary();
/*  99 */     this.eventWriter.add(this.eventFactory.createStartDocument("UTF-8", version));
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeStartDocument(String encoding, String version) throws XMLStreamException {
/* 104 */     closeEmptyElementIfNecessary();
/* 105 */     this.eventWriter.add(this.eventFactory.createStartDocument(encoding, version));
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeStartElement(String localName) throws XMLStreamException {
/* 110 */     closeEmptyElementIfNecessary();
/* 111 */     doWriteStartElement(this.eventFactory.createStartElement(new QName(localName), (Iterator)null, (Iterator)null));
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeStartElement(String namespaceURI, String localName) throws XMLStreamException {
/* 116 */     closeEmptyElementIfNecessary();
/* 117 */     doWriteStartElement(this.eventFactory.createStartElement(new QName(namespaceURI, localName), (Iterator)null, (Iterator)null));
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeStartElement(String prefix, String localName, String namespaceURI) throws XMLStreamException {
/* 122 */     closeEmptyElementIfNecessary();
/* 123 */     doWriteStartElement(this.eventFactory.createStartElement(new QName(namespaceURI, localName, prefix), (Iterator)null, (Iterator)null));
/*     */   }
/*     */   
/*     */   private void doWriteStartElement(StartElement startElement) throws XMLStreamException {
/* 127 */     this.eventWriter.add(startElement);
/* 128 */     this.endElements.add(this.eventFactory.createEndElement(startElement.getName(), startElement.getNamespaces()));
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeEmptyElement(String localName) throws XMLStreamException {
/* 133 */     closeEmptyElementIfNecessary();
/* 134 */     writeStartElement(localName);
/* 135 */     this.emptyElement = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeEmptyElement(String namespaceURI, String localName) throws XMLStreamException {
/* 140 */     closeEmptyElementIfNecessary();
/* 141 */     writeStartElement(namespaceURI, localName);
/* 142 */     this.emptyElement = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeEmptyElement(String prefix, String localName, String namespaceURI) throws XMLStreamException {
/* 147 */     closeEmptyElementIfNecessary();
/* 148 */     writeStartElement(prefix, localName, namespaceURI);
/* 149 */     this.emptyElement = true;
/*     */   }
/*     */   
/*     */   private void closeEmptyElementIfNecessary() throws XMLStreamException {
/* 153 */     if (this.emptyElement) {
/* 154 */       this.emptyElement = false;
/* 155 */       writeEndElement();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeEndElement() throws XMLStreamException {
/* 161 */     closeEmptyElementIfNecessary();
/* 162 */     int last = this.endElements.size() - 1;
/* 163 */     EndElement lastEndElement = this.endElements.get(last);
/* 164 */     this.eventWriter.add(lastEndElement);
/* 165 */     this.endElements.remove(last);
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeAttribute(String localName, String value) throws XMLStreamException {
/* 170 */     this.eventWriter.add(this.eventFactory.createAttribute(localName, value));
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeAttribute(String namespaceURI, String localName, String value) throws XMLStreamException {
/* 175 */     this.eventWriter.add(this.eventFactory.createAttribute(new QName(namespaceURI, localName), value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeAttribute(String prefix, String namespaceURI, String localName, String value) throws XMLStreamException {
/* 182 */     this.eventWriter.add(this.eventFactory.createAttribute(prefix, namespaceURI, localName, value));
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeNamespace(String prefix, String namespaceURI) throws XMLStreamException {
/* 187 */     doWriteNamespace(this.eventFactory.createNamespace(prefix, namespaceURI));
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeDefaultNamespace(String namespaceURI) throws XMLStreamException {
/* 192 */     doWriteNamespace(this.eventFactory.createNamespace(namespaceURI));
/*     */   }
/*     */ 
/*     */   
/*     */   private void doWriteNamespace(Namespace namespace) throws XMLStreamException {
/* 197 */     int last = this.endElements.size() - 1;
/* 198 */     EndElement oldEndElement = this.endElements.get(last);
/* 199 */     Iterator<Namespace> oldNamespaces = oldEndElement.getNamespaces();
/* 200 */     List<Namespace> newNamespaces = new ArrayList<Namespace>();
/* 201 */     while (oldNamespaces.hasNext()) {
/* 202 */       Namespace oldNamespace = oldNamespaces.next();
/* 203 */       newNamespaces.add(oldNamespace);
/*     */     } 
/* 205 */     newNamespaces.add(namespace);
/* 206 */     EndElement newEndElement = this.eventFactory.createEndElement(oldEndElement.getName(), newNamespaces.iterator());
/* 207 */     this.eventWriter.add(namespace);
/* 208 */     this.endElements.set(last, newEndElement);
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeCharacters(String text) throws XMLStreamException {
/* 213 */     closeEmptyElementIfNecessary();
/* 214 */     this.eventWriter.add(this.eventFactory.createCharacters(text));
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeCharacters(char[] text, int start, int len) throws XMLStreamException {
/* 219 */     closeEmptyElementIfNecessary();
/* 220 */     this.eventWriter.add(this.eventFactory.createCharacters(new String(text, start, len)));
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeCData(String data) throws XMLStreamException {
/* 225 */     closeEmptyElementIfNecessary();
/* 226 */     this.eventWriter.add(this.eventFactory.createCData(data));
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeComment(String data) throws XMLStreamException {
/* 231 */     closeEmptyElementIfNecessary();
/* 232 */     this.eventWriter.add(this.eventFactory.createComment(data));
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeProcessingInstruction(String target) throws XMLStreamException {
/* 237 */     closeEmptyElementIfNecessary();
/* 238 */     this.eventWriter.add(this.eventFactory.createProcessingInstruction(target, ""));
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeProcessingInstruction(String target, String data) throws XMLStreamException {
/* 243 */     closeEmptyElementIfNecessary();
/* 244 */     this.eventWriter.add(this.eventFactory.createProcessingInstruction(target, data));
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeDTD(String dtd) throws XMLStreamException {
/* 249 */     closeEmptyElementIfNecessary();
/* 250 */     this.eventWriter.add(this.eventFactory.createDTD(dtd));
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeEntityRef(String name) throws XMLStreamException {
/* 255 */     closeEmptyElementIfNecessary();
/* 256 */     this.eventWriter.add(this.eventFactory.createEntityReference(name, null));
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeEndDocument() throws XMLStreamException {
/* 261 */     closeEmptyElementIfNecessary();
/* 262 */     this.eventWriter.add(this.eventFactory.createEndDocument());
/*     */   }
/*     */ 
/*     */   
/*     */   public void flush() throws XMLStreamException {
/* 267 */     this.eventWriter.flush();
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws XMLStreamException {
/* 272 */     closeEmptyElementIfNecessary();
/* 273 */     this.eventWriter.close();
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframewor\\util\xml\XMLEventStreamWriter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */