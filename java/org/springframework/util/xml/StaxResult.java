/*     */ package org.springframework.util.xml;
/*     */ 
/*     */ import javax.xml.stream.XMLEventWriter;
/*     */ import javax.xml.stream.XMLStreamWriter;
/*     */ import javax.xml.transform.sax.SAXResult;
/*     */ import org.xml.sax.ContentHandler;
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
/*     */ class StaxResult
/*     */   extends SAXResult
/*     */ {
/*     */   private XMLEventWriter eventWriter;
/*     */   private XMLStreamWriter streamWriter;
/*     */   
/*     */   public StaxResult(XMLEventWriter eventWriter) {
/*  59 */     StaxEventHandler handler = new StaxEventHandler(eventWriter);
/*  60 */     super.setHandler(handler);
/*  61 */     super.setLexicalHandler(handler);
/*  62 */     this.eventWriter = eventWriter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StaxResult(XMLStreamWriter streamWriter) {
/*  70 */     StaxStreamHandler handler = new StaxStreamHandler(streamWriter);
/*  71 */     super.setHandler(handler);
/*  72 */     super.setLexicalHandler(handler);
/*  73 */     this.streamWriter = streamWriter;
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
/*     */   public XMLEventWriter getXMLEventWriter() {
/*  85 */     return this.eventWriter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public XMLStreamWriter getXMLStreamWriter() {
/*  96 */     return this.streamWriter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHandler(ContentHandler handler) {
/* 106 */     throw new UnsupportedOperationException("setHandler is not supported");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLexicalHandler(LexicalHandler handler) {
/* 115 */     throw new UnsupportedOperationException("setLexicalHandler is not supported");
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframewor\\util\xml\StaxResult.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */