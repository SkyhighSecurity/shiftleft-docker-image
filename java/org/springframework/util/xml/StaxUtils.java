/*     */ package org.springframework.util.xml;
/*     */ 
/*     */ import javax.xml.stream.XMLEventFactory;
/*     */ import javax.xml.stream.XMLEventReader;
/*     */ import javax.xml.stream.XMLEventWriter;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamReader;
/*     */ import javax.xml.stream.XMLStreamWriter;
/*     */ import javax.xml.transform.Result;
/*     */ import javax.xml.transform.Source;
/*     */ import javax.xml.transform.stax.StAXResult;
/*     */ import javax.xml.transform.stax.StAXSource;
/*     */ import org.xml.sax.ContentHandler;
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
/*     */ public abstract class StaxUtils
/*     */ {
/*     */   public static Source createStaxSource(XMLStreamReader streamReader) {
/*  52 */     return new StAXSource(streamReader);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Source createStaxSource(XMLEventReader eventReader) throws XMLStreamException {
/*  61 */     return new StAXSource(eventReader);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Source createCustomStaxSource(XMLStreamReader streamReader) {
/*  70 */     return new StaxSource(streamReader);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Source createCustomStaxSource(XMLEventReader eventReader) {
/*  79 */     return new StaxSource(eventReader);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isStaxSource(Source source) {
/*  89 */     return (source instanceof StAXSource || source instanceof StaxSource);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static XMLStreamReader getXMLStreamReader(Source source) {
/* 100 */     if (source instanceof StAXSource) {
/* 101 */       return ((StAXSource)source).getXMLStreamReader();
/*     */     }
/* 103 */     if (source instanceof StaxSource) {
/* 104 */       return ((StaxSource)source).getXMLStreamReader();
/*     */     }
/*     */     
/* 107 */     throw new IllegalArgumentException("Source '" + source + "' is neither StaxSource nor StAXSource");
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
/*     */   public static XMLEventReader getXMLEventReader(Source source) {
/* 119 */     if (source instanceof StAXSource) {
/* 120 */       return ((StAXSource)source).getXMLEventReader();
/*     */     }
/* 122 */     if (source instanceof StaxSource) {
/* 123 */       return ((StaxSource)source).getXMLEventReader();
/*     */     }
/*     */     
/* 126 */     throw new IllegalArgumentException("Source '" + source + "' is neither StaxSource nor StAXSource");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Result createStaxResult(XMLStreamWriter streamWriter) {
/* 136 */     return new StAXResult(streamWriter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Result createStaxResult(XMLEventWriter eventWriter) {
/* 145 */     return new StAXResult(eventWriter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Result createCustomStaxResult(XMLStreamWriter streamWriter) {
/* 154 */     return new StaxResult(streamWriter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Result createCustomStaxResult(XMLEventWriter eventWriter) {
/* 163 */     return new StaxResult(eventWriter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isStaxResult(Result result) {
/* 173 */     return (result instanceof StAXResult || result instanceof StaxResult);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static XMLStreamWriter getXMLStreamWriter(Result result) {
/* 184 */     if (result instanceof StAXResult) {
/* 185 */       return ((StAXResult)result).getXMLStreamWriter();
/*     */     }
/* 187 */     if (result instanceof StaxResult) {
/* 188 */       return ((StaxResult)result).getXMLStreamWriter();
/*     */     }
/*     */     
/* 191 */     throw new IllegalArgumentException("Result '" + result + "' is neither StaxResult nor StAXResult");
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
/*     */   public static XMLEventWriter getXMLEventWriter(Result result) {
/* 203 */     if (result instanceof StAXResult) {
/* 204 */       return ((StAXResult)result).getXMLEventWriter();
/*     */     }
/* 206 */     if (result instanceof StaxResult) {
/* 207 */       return ((StaxResult)result).getXMLEventWriter();
/*     */     }
/*     */     
/* 210 */     throw new IllegalArgumentException("Result '" + result + "' is neither StaxResult nor StAXResult");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ContentHandler createContentHandler(XMLStreamWriter streamWriter) {
/* 220 */     return new StaxStreamHandler(streamWriter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ContentHandler createContentHandler(XMLEventWriter eventWriter) {
/* 229 */     return new StaxEventHandler(eventWriter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static XMLReader createXMLReader(XMLStreamReader streamReader) {
/* 238 */     return new StaxStreamXMLReader(streamReader);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static XMLReader createXMLReader(XMLEventReader eventReader) {
/* 247 */     return new StaxEventXMLReader(eventReader);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static XMLStreamReader createEventStreamReader(XMLEventReader eventReader) throws XMLStreamException {
/* 257 */     return new XMLEventStreamReader(eventReader);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static XMLStreamWriter createEventStreamWriter(XMLEventWriter eventWriter) {
/* 266 */     return new XMLEventStreamWriter(eventWriter, XMLEventFactory.newFactory());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static XMLStreamWriter createEventStreamWriter(XMLEventWriter eventWriter, XMLEventFactory eventFactory) {
/* 275 */     return new XMLEventStreamWriter(eventWriter, eventFactory);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframewor\\util\xml\StaxUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */