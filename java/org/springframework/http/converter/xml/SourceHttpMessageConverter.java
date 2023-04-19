/*     */ package org.springframework.http.converter.xml;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.StringReader;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import javax.xml.stream.XMLInputFactory;
/*     */ import javax.xml.stream.XMLResolver;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamReader;
/*     */ import javax.xml.transform.Result;
/*     */ import javax.xml.transform.Source;
/*     */ import javax.xml.transform.TransformerException;
/*     */ import javax.xml.transform.TransformerFactory;
/*     */ import javax.xml.transform.dom.DOMSource;
/*     */ import javax.xml.transform.sax.SAXSource;
/*     */ import javax.xml.transform.stax.StAXSource;
/*     */ import javax.xml.transform.stream.StreamResult;
/*     */ import javax.xml.transform.stream.StreamSource;
/*     */ import org.springframework.http.HttpInputMessage;
/*     */ import org.springframework.http.HttpOutputMessage;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.converter.AbstractHttpMessageConverter;
/*     */ import org.springframework.http.converter.HttpMessageConversionException;
/*     */ import org.springframework.http.converter.HttpMessageNotReadableException;
/*     */ import org.springframework.http.converter.HttpMessageNotWritableException;
/*     */ import org.springframework.util.StreamUtils;
/*     */ import org.w3c.dom.Document;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.XMLReader;
/*     */ import org.xml.sax.helpers.XMLReaderFactory;
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
/*     */ public class SourceHttpMessageConverter<T extends Source>
/*     */   extends AbstractHttpMessageConverter<T>
/*     */ {
/*  69 */   private static final Set<Class<?>> SUPPORTED_CLASSES = new HashSet<Class<?>>(5);
/*     */   
/*     */   static {
/*  72 */     SUPPORTED_CLASSES.add(DOMSource.class);
/*  73 */     SUPPORTED_CLASSES.add(SAXSource.class);
/*  74 */     SUPPORTED_CLASSES.add(StAXSource.class);
/*  75 */     SUPPORTED_CLASSES.add(StreamSource.class);
/*  76 */     SUPPORTED_CLASSES.add(Source.class);
/*     */   }
/*     */ 
/*     */   
/*  80 */   private final TransformerFactory transformerFactory = TransformerFactory.newInstance();
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean supportDtd = false;
/*     */ 
/*     */   
/*     */   private boolean processExternalEntities = false;
/*     */ 
/*     */ 
/*     */   
/*     */   public SourceHttpMessageConverter() {
/*  92 */     super(new MediaType[] { MediaType.APPLICATION_XML, MediaType.TEXT_XML, new MediaType("application", "*+xml") });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSupportDtd(boolean supportDtd) {
/* 101 */     this.supportDtd = supportDtd;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSupportDtd() {
/* 108 */     return this.supportDtd;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProcessExternalEntities(boolean processExternalEntities) {
/* 118 */     this.processExternalEntities = processExternalEntities;
/* 119 */     if (processExternalEntities) {
/* 120 */       setSupportDtd(true);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isProcessExternalEntities() {
/* 128 */     return this.processExternalEntities;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean supports(Class<?> clazz) {
/* 134 */     return SUPPORTED_CLASSES.contains(clazz);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected T readInternal(Class<? extends T> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
/* 142 */     InputStream body = inputMessage.getBody();
/* 143 */     if (DOMSource.class == clazz) {
/* 144 */       return (T)readDOMSource(body);
/*     */     }
/* 146 */     if (SAXSource.class == clazz) {
/* 147 */       return (T)readSAXSource(body);
/*     */     }
/* 149 */     if (StAXSource.class == clazz) {
/* 150 */       return (T)readStAXSource(body);
/*     */     }
/* 152 */     if (StreamSource.class == clazz || Source.class == clazz) {
/* 153 */       return (T)readStreamSource(body);
/*     */     }
/*     */     
/* 156 */     throw new HttpMessageConversionException("Could not read class [" + clazz + "]. Only DOMSource, SAXSource, StAXSource, and StreamSource are supported.");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private DOMSource readDOMSource(InputStream body) throws IOException {
/*     */     try {
/* 163 */       DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
/* 164 */       documentBuilderFactory.setNamespaceAware(true);
/* 165 */       documentBuilderFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", 
/* 166 */           !isSupportDtd());
/* 167 */       documentBuilderFactory.setFeature("http://xml.org/sax/features/external-general-entities", 
/* 168 */           isProcessExternalEntities());
/* 169 */       DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
/* 170 */       if (!isProcessExternalEntities()) {
/* 171 */         documentBuilder.setEntityResolver(NO_OP_ENTITY_RESOLVER);
/*     */       }
/* 173 */       Document document = documentBuilder.parse(body);
/* 174 */       return new DOMSource(document);
/*     */     }
/* 176 */     catch (NullPointerException ex) {
/* 177 */       if (!isSupportDtd()) {
/* 178 */         throw new HttpMessageNotReadableException("NPE while unmarshalling: This can happen due to the presence of DTD declarations which are disabled.", ex);
/*     */       }
/*     */       
/* 181 */       throw ex;
/*     */     }
/* 183 */     catch (ParserConfigurationException ex) {
/* 184 */       throw new HttpMessageNotReadableException("Could not set feature: " + ex.getMessage(), ex);
/*     */     }
/* 186 */     catch (SAXException ex) {
/* 187 */       throw new HttpMessageNotReadableException("Could not parse document: " + ex.getMessage(), ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   private SAXSource readSAXSource(InputStream body) throws IOException {
/*     */     try {
/* 193 */       XMLReader xmlReader = XMLReaderFactory.createXMLReader();
/* 194 */       xmlReader.setFeature("http://apache.org/xml/features/disallow-doctype-decl", !isSupportDtd());
/* 195 */       xmlReader.setFeature("http://xml.org/sax/features/external-general-entities", isProcessExternalEntities());
/* 196 */       if (!isProcessExternalEntities()) {
/* 197 */         xmlReader.setEntityResolver(NO_OP_ENTITY_RESOLVER);
/*     */       }
/* 199 */       byte[] bytes = StreamUtils.copyToByteArray(body);
/* 200 */       return new SAXSource(xmlReader, new InputSource(new ByteArrayInputStream(bytes)));
/*     */     }
/* 202 */     catch (SAXException ex) {
/* 203 */       throw new HttpMessageNotReadableException("Could not parse document: " + ex.getMessage(), ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   private Source readStAXSource(InputStream body) {
/*     */     try {
/* 209 */       XMLInputFactory inputFactory = XMLInputFactory.newInstance();
/* 210 */       inputFactory.setProperty("javax.xml.stream.supportDTD", Boolean.valueOf(isSupportDtd()));
/* 211 */       inputFactory.setProperty("javax.xml.stream.isSupportingExternalEntities", Boolean.valueOf(isProcessExternalEntities()));
/* 212 */       if (!isProcessExternalEntities()) {
/* 213 */         inputFactory.setXMLResolver(NO_OP_XML_RESOLVER);
/*     */       }
/* 215 */       XMLStreamReader streamReader = inputFactory.createXMLStreamReader(body);
/* 216 */       return new StAXSource(streamReader);
/*     */     }
/* 218 */     catch (XMLStreamException ex) {
/* 219 */       throw new HttpMessageNotReadableException("Could not parse document: " + ex.getMessage(), ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   private StreamSource readStreamSource(InputStream body) throws IOException {
/* 224 */     byte[] bytes = StreamUtils.copyToByteArray(body);
/* 225 */     return new StreamSource(new ByteArrayInputStream(bytes));
/*     */   }
/*     */ 
/*     */   
/*     */   protected Long getContentLength(T t, MediaType contentType) {
/* 230 */     if (t instanceof DOMSource) {
/*     */       try {
/* 232 */         CountingOutputStream os = new CountingOutputStream();
/* 233 */         transform((Source)t, new StreamResult(os));
/* 234 */         return Long.valueOf(os.count);
/*     */       }
/* 236 */       catch (TransformerException transformerException) {}
/*     */     }
/*     */ 
/*     */     
/* 240 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeInternal(T t, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
/*     */     try {
/* 247 */       Result result = new StreamResult(outputMessage.getBody());
/* 248 */       transform((Source)t, result);
/*     */     }
/* 250 */     catch (TransformerException ex) {
/* 251 */       throw new HttpMessageNotWritableException("Could not transform [" + t + "] to output message", ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void transform(Source source, Result result) throws TransformerException {
/* 256 */     this.transformerFactory.newTransformer().transform(source, result);
/*     */   }
/*     */   
/*     */   private static class CountingOutputStream
/*     */     extends OutputStream
/*     */   {
/* 262 */     long count = 0L;
/*     */ 
/*     */     
/*     */     public void write(int b) throws IOException {
/* 266 */       this.count++;
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(byte[] b) throws IOException {
/* 271 */       this.count += b.length;
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(byte[] b, int off, int len) throws IOException {
/* 276 */       this.count += len;
/*     */     }
/*     */     
/*     */     private CountingOutputStream() {} }
/*     */   
/* 281 */   private static final EntityResolver NO_OP_ENTITY_RESOLVER = new EntityResolver()
/*     */     {
/*     */       public InputSource resolveEntity(String publicId, String systemId) {
/* 284 */         return new InputSource(new StringReader(""));
/*     */       }
/*     */     };
/*     */   
/* 288 */   private static final XMLResolver NO_OP_XML_RESOLVER = new XMLResolver()
/*     */     {
/*     */       public Object resolveEntity(String publicID, String systemID, String base, String ns) {
/* 291 */         return StreamUtils.emptyInput();
/*     */       }
/*     */     };
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\http\converter\xml\SourceHttpMessageConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */