/*     */ package org.springframework.util.xml;
/*     */ 
/*     */ import javax.xml.stream.XMLEventReader;
/*     */ import javax.xml.stream.XMLStreamReader;
/*     */ import javax.xml.transform.sax.SAXSource;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.XMLReader;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ class StaxSource
/*     */   extends SAXSource
/*     */ {
/*     */   private XMLEventReader eventReader;
/*     */   private XMLStreamReader streamReader;
/*     */   
/*     */   StaxSource(XMLEventReader eventReader) {
/*  61 */     super(new StaxEventXMLReader(eventReader), new InputSource());
/*  62 */     this.eventReader = eventReader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   StaxSource(XMLStreamReader streamReader) {
/*  73 */     super(new StaxStreamXMLReader(streamReader), new InputSource());
/*  74 */     this.streamReader = streamReader;
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
/*     */   XMLEventReader getXMLEventReader() {
/*  86 */     return this.eventReader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   XMLStreamReader getXMLStreamReader() {
/*  97 */     return this.streamReader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInputSource(InputSource inputSource) {
/* 107 */     throw new UnsupportedOperationException("setInputSource is not supported");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setXMLReader(XMLReader reader) {
/* 116 */     throw new UnsupportedOperationException("setXMLReader is not supported");
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframewor\\util\xml\StaxSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */