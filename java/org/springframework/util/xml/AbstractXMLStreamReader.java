/*     */ package org.springframework.util.xml;
/*     */ 
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamReader;
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
/*     */ abstract class AbstractXMLStreamReader
/*     */   implements XMLStreamReader
/*     */ {
/*     */   public String getElementText() throws XMLStreamException {
/*  34 */     if (getEventType() != 1) {
/*  35 */       throw new XMLStreamException("Parser must be on START_ELEMENT to read next text", getLocation());
/*     */     }
/*  37 */     int eventType = next();
/*  38 */     StringBuilder builder = new StringBuilder();
/*  39 */     while (eventType != 2) {
/*  40 */       if (eventType == 4 || eventType == 12 || eventType == 6 || eventType == 9) {
/*     */         
/*  42 */         builder.append(getText());
/*     */       }
/*  44 */       else if (eventType != 3 && eventType != 5) {
/*     */ 
/*     */ 
/*     */         
/*  48 */         if (eventType == 8) {
/*  49 */           throw new XMLStreamException("Unexpected end of document when reading element text content", 
/*  50 */               getLocation());
/*     */         }
/*  52 */         if (eventType == 1) {
/*  53 */           throw new XMLStreamException("Element text content may not contain START_ELEMENT", getLocation());
/*     */         }
/*     */         
/*  56 */         throw new XMLStreamException("Unexpected event type " + eventType, getLocation());
/*     */       } 
/*  58 */       eventType = next();
/*     */     } 
/*  60 */     return builder.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getAttributeLocalName(int index) {
/*  65 */     return getAttributeName(index).getLocalPart();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getAttributeNamespace(int index) {
/*  70 */     return getAttributeName(index).getNamespaceURI();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getAttributePrefix(int index) {
/*  75 */     return getAttributeName(index).getPrefix();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getNamespaceURI() {
/*  80 */     int eventType = getEventType();
/*  81 */     if (eventType == 1 || eventType == 2) {
/*  82 */       return getName().getNamespaceURI();
/*     */     }
/*     */     
/*  85 */     throw new IllegalStateException("Parser must be on START_ELEMENT or END_ELEMENT state");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getNamespaceURI(String prefix) {
/*  91 */     return getNamespaceContext().getNamespaceURI(prefix);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasText() {
/*  96 */     int eventType = getEventType();
/*  97 */     return (eventType == 6 || eventType == 4 || eventType == 5 || eventType == 12 || eventType == 9);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPrefix() {
/* 104 */     int eventType = getEventType();
/* 105 */     if (eventType == 1 || eventType == 2) {
/* 106 */       return getName().getPrefix();
/*     */     }
/*     */     
/* 109 */     throw new IllegalStateException("Parser must be on START_ELEMENT or END_ELEMENT state");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasName() {
/* 115 */     int eventType = getEventType();
/* 116 */     return (eventType == 1 || eventType == 2);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isWhiteSpace() {
/* 121 */     return (getEventType() == 6);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isStartElement() {
/* 126 */     return (getEventType() == 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEndElement() {
/* 131 */     return (getEventType() == 2);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCharacters() {
/* 136 */     return (getEventType() == 4);
/*     */   }
/*     */ 
/*     */   
/*     */   public int nextTag() throws XMLStreamException {
/* 141 */     int eventType = next();
/* 142 */     while ((eventType == 4 && isWhiteSpace()) || (eventType == 12 && 
/* 143 */       isWhiteSpace()) || eventType == 6 || eventType == 3 || eventType == 5)
/*     */     {
/* 145 */       eventType = next();
/*     */     }
/* 147 */     if (eventType != 1 && eventType != 2) {
/* 148 */       throw new XMLStreamException("expected start or end tag", getLocation());
/*     */     }
/* 150 */     return eventType;
/*     */   }
/*     */ 
/*     */   
/*     */   public void require(int expectedType, String namespaceURI, String localName) throws XMLStreamException {
/* 155 */     int eventType = getEventType();
/* 156 */     if (eventType != expectedType) {
/* 157 */       throw new XMLStreamException("Expected [" + expectedType + "] but read [" + eventType + "]");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public String getAttributeValue(String namespaceURI, String localName) {
/* 163 */     for (int i = 0; i < getAttributeCount(); i++) {
/* 164 */       QName name = getAttributeName(i);
/* 165 */       if (name.getLocalPart().equals(localName) && (namespaceURI == null || name
/* 166 */         .getNamespaceURI().equals(namespaceURI))) {
/* 167 */         return getAttributeValue(i);
/*     */       }
/*     */     } 
/* 170 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasNext() {
/* 175 */     return (getEventType() != 8);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getLocalName() {
/* 180 */     return getName().getLocalPart();
/*     */   }
/*     */ 
/*     */   
/*     */   public char[] getTextCharacters() {
/* 185 */     return getText().toCharArray();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getTextCharacters(int sourceStart, char[] target, int targetStart, int length) {
/* 190 */     char[] source = getTextCharacters();
/* 191 */     length = Math.min(length, source.length);
/* 192 */     System.arraycopy(source, sourceStart, target, targetStart, length);
/* 193 */     return length;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getTextLength() {
/* 198 */     return getText().length();
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframewor\\util\xml\AbstractXMLStreamReader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */