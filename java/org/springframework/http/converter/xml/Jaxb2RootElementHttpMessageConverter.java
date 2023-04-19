/*     */ package org.springframework.http.converter.xml;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.StringReader;
/*     */ import javax.xml.bind.JAXBElement;
/*     */ import javax.xml.bind.JAXBException;
/*     */ import javax.xml.bind.MarshalException;
/*     */ import javax.xml.bind.Marshaller;
/*     */ import javax.xml.bind.PropertyException;
/*     */ import javax.xml.bind.UnmarshalException;
/*     */ import javax.xml.bind.Unmarshaller;
/*     */ import javax.xml.bind.annotation.XmlRootElement;
/*     */ import javax.xml.bind.annotation.XmlType;
/*     */ import javax.xml.transform.Result;
/*     */ import javax.xml.transform.Source;
/*     */ import javax.xml.transform.sax.SAXSource;
/*     */ import javax.xml.transform.stream.StreamSource;
/*     */ import org.springframework.core.annotation.AnnotationUtils;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.converter.HttpMessageConversionException;
/*     */ import org.springframework.http.converter.HttpMessageNotReadableException;
/*     */ import org.springframework.http.converter.HttpMessageNotWritableException;
/*     */ import org.springframework.util.ClassUtils;
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
/*     */ public class Jaxb2RootElementHttpMessageConverter
/*     */   extends AbstractJaxb2HttpMessageConverter<Object>
/*     */ {
/*     */   private boolean supportDtd = false;
/*     */   private boolean processExternalEntities = false;
/*     */   
/*     */   public void setSupportDtd(boolean supportDtd) {
/*  79 */     this.supportDtd = supportDtd;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSupportDtd() {
/*  86 */     return this.supportDtd;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProcessExternalEntities(boolean processExternalEntities) {
/*  96 */     this.processExternalEntities = processExternalEntities;
/*  97 */     if (processExternalEntities) {
/*  98 */       setSupportDtd(true);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isProcessExternalEntities() {
/* 106 */     return this.processExternalEntities;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canRead(Class<?> clazz, MediaType mediaType) {
/* 112 */     return ((clazz.isAnnotationPresent((Class)XmlRootElement.class) || clazz.isAnnotationPresent((Class)XmlType.class)) && 
/* 113 */       canRead(mediaType));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canWrite(Class<?> clazz, MediaType mediaType) {
/* 118 */     return (AnnotationUtils.findAnnotation(clazz, XmlRootElement.class) != null && canWrite(mediaType));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean supports(Class<?> clazz) {
/* 124 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   protected Object readFromSource(Class<?> clazz, HttpHeaders headers, Source source) throws IOException {
/*     */     try {
/* 130 */       source = processSource(source);
/* 131 */       Unmarshaller unmarshaller = createUnmarshaller(clazz);
/* 132 */       if (clazz.isAnnotationPresent((Class)XmlRootElement.class)) {
/* 133 */         return unmarshaller.unmarshal(source);
/*     */       }
/*     */       
/* 136 */       JAXBElement<?> jaxbElement = unmarshaller.unmarshal(source, clazz);
/* 137 */       return jaxbElement.getValue();
/*     */     
/*     */     }
/* 140 */     catch (NullPointerException ex) {
/* 141 */       if (!isSupportDtd()) {
/* 142 */         throw new HttpMessageNotReadableException("NPE while unmarshalling. This can happen due to the presence of DTD declarations which are disabled.", ex);
/*     */       }
/*     */       
/* 145 */       throw ex;
/*     */     }
/* 147 */     catch (UnmarshalException ex) {
/* 148 */       throw new HttpMessageNotReadableException("Could not unmarshal to [" + clazz + "]: " + ex.getMessage(), ex);
/*     */     }
/* 150 */     catch (JAXBException ex) {
/* 151 */       throw new HttpMessageConversionException("Invalid JAXB setup: " + ex.getMessage(), ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected Source processSource(Source source) {
/* 156 */     if (source instanceof StreamSource) {
/* 157 */       StreamSource streamSource = (StreamSource)source;
/* 158 */       InputSource inputSource = new InputSource(streamSource.getInputStream());
/*     */       try {
/* 160 */         XMLReader xmlReader = XMLReaderFactory.createXMLReader();
/* 161 */         xmlReader.setFeature("http://apache.org/xml/features/disallow-doctype-decl", !isSupportDtd());
/* 162 */         String featureName = "http://xml.org/sax/features/external-general-entities";
/* 163 */         xmlReader.setFeature(featureName, isProcessExternalEntities());
/* 164 */         if (!isProcessExternalEntities()) {
/* 165 */           xmlReader.setEntityResolver(NO_OP_ENTITY_RESOLVER);
/*     */         }
/* 167 */         return new SAXSource(xmlReader, inputSource);
/*     */       }
/* 169 */       catch (SAXException ex) {
/* 170 */         this.logger.warn("Processing of external entities could not be disabled", ex);
/* 171 */         return source;
/*     */       } 
/*     */     } 
/*     */     
/* 175 */     return source;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeToResult(Object o, HttpHeaders headers, Result result) throws IOException {
/*     */     try {
/* 182 */       Class<?> clazz = ClassUtils.getUserClass(o);
/* 183 */       Marshaller marshaller = createMarshaller(clazz);
/* 184 */       setCharset(headers.getContentType(), marshaller);
/* 185 */       marshaller.marshal(o, result);
/*     */     }
/* 187 */     catch (MarshalException ex) {
/* 188 */       throw new HttpMessageNotWritableException("Could not marshal [" + o + "]: " + ex.getMessage(), ex);
/*     */     }
/* 190 */     catch (JAXBException ex) {
/* 191 */       throw new HttpMessageConversionException("Invalid JAXB setup: " + ex.getMessage(), ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void setCharset(MediaType contentType, Marshaller marshaller) throws PropertyException {
/* 196 */     if (contentType != null && contentType.getCharset() != null) {
/* 197 */       marshaller.setProperty("jaxb.encoding", contentType.getCharset().name());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 202 */   private static final EntityResolver NO_OP_ENTITY_RESOLVER = new EntityResolver()
/*     */     {
/*     */       public InputSource resolveEntity(String publicId, String systemId) {
/* 205 */         return new InputSource(new StringReader(""));
/*     */       }
/*     */     };
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\http\converter\xml\Jaxb2RootElementHttpMessageConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */