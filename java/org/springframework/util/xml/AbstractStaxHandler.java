/*     */ package org.springframework.util.xml;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.SAXException;
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
/*     */ abstract class AbstractStaxHandler
/*     */   implements ContentHandler, LexicalHandler
/*     */ {
/*  43 */   private final List<Map<String, String>> namespaceMappings = new ArrayList<Map<String, String>>();
/*     */ 
/*     */   
/*     */   private boolean inCData;
/*     */ 
/*     */   
/*     */   public final void startDocument() throws SAXException {
/*  50 */     removeAllNamespaceMappings();
/*  51 */     newNamespaceMapping();
/*     */     try {
/*  53 */       startDocumentInternal();
/*     */     }
/*  55 */     catch (XMLStreamException ex) {
/*  56 */       throw new SAXException("Could not handle startDocument: " + ex.getMessage(), ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final void endDocument() throws SAXException {
/*  62 */     removeAllNamespaceMappings();
/*     */     try {
/*  64 */       endDocumentInternal();
/*     */     }
/*  66 */     catch (XMLStreamException ex) {
/*  67 */       throw new SAXException("Could not handle endDocument: " + ex.getMessage(), ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final void startPrefixMapping(String prefix, String uri) {
/*  73 */     currentNamespaceMapping().put(prefix, uri);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void endPrefixMapping(String prefix) {}
/*     */ 
/*     */   
/*     */   public final void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
/*     */     try {
/*  83 */       startElementInternal(toQName(uri, qName), atts, currentNamespaceMapping());
/*  84 */       newNamespaceMapping();
/*     */     }
/*  86 */     catch (XMLStreamException ex) {
/*  87 */       throw new SAXException("Could not handle startElement: " + ex.getMessage(), ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final void endElement(String uri, String localName, String qName) throws SAXException {
/*     */     try {
/*  94 */       endElementInternal(toQName(uri, qName), currentNamespaceMapping());
/*  95 */       removeNamespaceMapping();
/*     */     }
/*  97 */     catch (XMLStreamException ex) {
/*  98 */       throw new SAXException("Could not handle endElement: " + ex.getMessage(), ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final void characters(char[] ch, int start, int length) throws SAXException {
/*     */     try {
/* 105 */       String data = new String(ch, start, length);
/* 106 */       if (!this.inCData) {
/* 107 */         charactersInternal(data);
/*     */       } else {
/*     */         
/* 110 */         cDataInternal(data);
/*     */       }
/*     */     
/* 113 */     } catch (XMLStreamException ex) {
/* 114 */       throw new SAXException("Could not handle characters: " + ex.getMessage(), ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
/*     */     try {
/* 121 */       ignorableWhitespaceInternal(new String(ch, start, length));
/*     */     }
/* 123 */     catch (XMLStreamException ex) {
/* 124 */       throw new SAXException("Could not handle ignorableWhitespace:" + ex
/* 125 */           .getMessage(), ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final void processingInstruction(String target, String data) throws SAXException {
/*     */     try {
/* 132 */       processingInstructionInternal(target, data);
/*     */     }
/* 134 */     catch (XMLStreamException ex) {
/* 135 */       throw new SAXException("Could not handle processingInstruction: " + ex.getMessage(), ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final void skippedEntity(String name) throws SAXException {
/*     */     try {
/* 142 */       skippedEntityInternal(name);
/*     */     }
/* 144 */     catch (XMLStreamException ex) {
/* 145 */       throw new SAXException("Could not handle skippedEntity: " + ex.getMessage(), ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final void startDTD(String name, String publicId, String systemId) throws SAXException {
/*     */     try {
/* 152 */       StringBuilder builder = new StringBuilder("<!DOCTYPE ");
/* 153 */       builder.append(name);
/* 154 */       if (publicId != null) {
/* 155 */         builder.append(" PUBLIC \"");
/* 156 */         builder.append(publicId);
/* 157 */         builder.append("\" \"");
/*     */       } else {
/*     */         
/* 160 */         builder.append(" SYSTEM \"");
/*     */       } 
/* 162 */       builder.append(systemId);
/* 163 */       builder.append("\">");
/*     */       
/* 165 */       dtdInternal(builder.toString());
/*     */     }
/* 167 */     catch (XMLStreamException ex) {
/* 168 */       throw new SAXException("Could not handle startDTD: " + ex.getMessage(), ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void endDTD() throws SAXException {}
/*     */ 
/*     */   
/*     */   public final void startCDATA() throws SAXException {
/* 178 */     this.inCData = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void endCDATA() throws SAXException {
/* 183 */     this.inCData = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void comment(char[] ch, int start, int length) throws SAXException {
/*     */     try {
/* 189 */       commentInternal(new String(ch, start, length));
/*     */     }
/* 191 */     catch (XMLStreamException ex) {
/* 192 */       throw new SAXException("Could not handle comment: " + ex.getMessage(), ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void startEntity(String name) throws SAXException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void endEntity(String name) throws SAXException {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected QName toQName(String namespaceUri, String qualifiedName) {
/* 212 */     int idx = qualifiedName.indexOf(':');
/* 213 */     if (idx == -1) {
/* 214 */       return new QName(namespaceUri, qualifiedName);
/*     */     }
/*     */     
/* 217 */     String prefix = qualifiedName.substring(0, idx);
/* 218 */     String localPart = qualifiedName.substring(idx + 1);
/* 219 */     return new QName(namespaceUri, localPart, prefix);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isNamespaceDeclaration(QName qName) {
/* 224 */     String prefix = qName.getPrefix();
/* 225 */     String localPart = qName.getLocalPart();
/* 226 */     return (("xmlns".equals(localPart) && prefix.isEmpty()) || ("xmlns"
/* 227 */       .equals(prefix) && !localPart.isEmpty()));
/*     */   }
/*     */ 
/*     */   
/*     */   private Map<String, String> currentNamespaceMapping() {
/* 232 */     return this.namespaceMappings.get(this.namespaceMappings.size() - 1);
/*     */   }
/*     */   
/*     */   private void newNamespaceMapping() {
/* 236 */     this.namespaceMappings.add(new HashMap<String, String>());
/*     */   }
/*     */   
/*     */   private void removeNamespaceMapping() {
/* 240 */     this.namespaceMappings.remove(this.namespaceMappings.size() - 1);
/*     */   }
/*     */   
/*     */   private void removeAllNamespaceMappings() {
/* 244 */     this.namespaceMappings.clear();
/*     */   }
/*     */   
/*     */   protected abstract void startDocumentInternal() throws XMLStreamException;
/*     */   
/*     */   protected abstract void endDocumentInternal() throws XMLStreamException;
/*     */   
/*     */   protected abstract void startElementInternal(QName paramQName, Attributes paramAttributes, Map<String, String> paramMap) throws XMLStreamException;
/*     */   
/*     */   protected abstract void endElementInternal(QName paramQName, Map<String, String> paramMap) throws XMLStreamException;
/*     */   
/*     */   protected abstract void charactersInternal(String paramString) throws XMLStreamException;
/*     */   
/*     */   protected abstract void cDataInternal(String paramString) throws XMLStreamException;
/*     */   
/*     */   protected abstract void ignorableWhitespaceInternal(String paramString) throws XMLStreamException;
/*     */   
/*     */   protected abstract void processingInstructionInternal(String paramString1, String paramString2) throws XMLStreamException;
/*     */   
/*     */   protected abstract void skippedEntityInternal(String paramString) throws XMLStreamException;
/*     */   
/*     */   protected abstract void dtdInternal(String paramString) throws XMLStreamException;
/*     */   
/*     */   protected abstract void commentInternal(String paramString) throws XMLStreamException;
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframewor\\util\xml\AbstractStaxHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */