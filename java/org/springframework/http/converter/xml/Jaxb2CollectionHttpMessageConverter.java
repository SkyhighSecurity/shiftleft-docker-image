/*     */ package org.springframework.http.converter.xml;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.SortedSet;
/*     */ import java.util.TreeSet;
/*     */ import javax.xml.bind.JAXBException;
/*     */ import javax.xml.bind.UnmarshalException;
/*     */ import javax.xml.bind.Unmarshaller;
/*     */ import javax.xml.bind.annotation.XmlRootElement;
/*     */ import javax.xml.bind.annotation.XmlType;
/*     */ import javax.xml.stream.XMLInputFactory;
/*     */ import javax.xml.stream.XMLResolver;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamReader;
/*     */ import javax.xml.transform.Result;
/*     */ import javax.xml.transform.Source;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpInputMessage;
/*     */ import org.springframework.http.HttpOutputMessage;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.converter.GenericHttpMessageConverter;
/*     */ import org.springframework.http.converter.HttpMessageConversionException;
/*     */ import org.springframework.http.converter.HttpMessageNotReadableException;
/*     */ import org.springframework.http.converter.HttpMessageNotWritableException;
/*     */ import org.springframework.util.StreamUtils;
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
/*     */ public class Jaxb2CollectionHttpMessageConverter<T extends Collection>
/*     */   extends AbstractJaxb2HttpMessageConverter<T>
/*     */   implements GenericHttpMessageConverter<T>
/*     */ {
/*  65 */   private final XMLInputFactory inputFactory = createXmlInputFactory();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canRead(Class<?> clazz, MediaType mediaType) {
/*  74 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canRead(Type type, Class<?> contextClass, MediaType mediaType) {
/*  85 */     if (!(type instanceof ParameterizedType)) {
/*  86 */       return false;
/*     */     }
/*  88 */     ParameterizedType parameterizedType = (ParameterizedType)type;
/*  89 */     if (!(parameterizedType.getRawType() instanceof Class)) {
/*  90 */       return false;
/*     */     }
/*  92 */     Class<?> rawType = (Class)parameterizedType.getRawType();
/*  93 */     if (!Collection.class.isAssignableFrom(rawType)) {
/*  94 */       return false;
/*     */     }
/*  96 */     if ((parameterizedType.getActualTypeArguments()).length != 1) {
/*  97 */       return false;
/*     */     }
/*  99 */     Type typeArgument = parameterizedType.getActualTypeArguments()[0];
/* 100 */     if (!(typeArgument instanceof Class)) {
/* 101 */       return false;
/*     */     }
/* 103 */     Class<?> typeArgumentClass = (Class)typeArgument;
/* 104 */     return ((typeArgumentClass.isAnnotationPresent((Class)XmlRootElement.class) || typeArgumentClass
/* 105 */       .isAnnotationPresent((Class)XmlType.class)) && canRead(mediaType));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canWrite(Class<?> clazz, MediaType mediaType) {
/* 114 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canWrite(Type type, Class<?> clazz, MediaType mediaType) {
/* 123 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean supports(Class<?> clazz) {
/* 129 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected T readFromSource(Class<? extends T> clazz, HttpHeaders headers, Source source) throws IOException {
/* 135 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public T read(Type type, Class<?> contextClass, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
/* 143 */     ParameterizedType parameterizedType = (ParameterizedType)type;
/* 144 */     T result = createCollection((Class)parameterizedType.getRawType());
/* 145 */     Class<?> elementClass = (Class)parameterizedType.getActualTypeArguments()[0];
/*     */     
/*     */     try {
/* 148 */       Unmarshaller unmarshaller = createUnmarshaller(elementClass);
/* 149 */       XMLStreamReader streamReader = this.inputFactory.createXMLStreamReader(inputMessage.getBody());
/* 150 */       int event = moveToFirstChildOfRootElement(streamReader);
/*     */       
/* 152 */       while (event != 8) {
/* 153 */         if (elementClass.isAnnotationPresent((Class)XmlRootElement.class)) {
/* 154 */           result.add(unmarshaller.unmarshal(streamReader));
/*     */         }
/* 156 */         else if (elementClass.isAnnotationPresent((Class)XmlType.class)) {
/* 157 */           result.add(unmarshaller.unmarshal(streamReader, elementClass).getValue());
/*     */         }
/*     */         else {
/*     */           
/* 161 */           throw new HttpMessageConversionException("Could not unmarshal to [" + elementClass + "]");
/*     */         } 
/* 163 */         event = moveToNextElement(streamReader);
/*     */       } 
/* 165 */       return result;
/*     */     }
/* 167 */     catch (UnmarshalException ex) {
/* 168 */       throw new HttpMessageNotReadableException("Could not unmarshal to [" + elementClass + "]: " + ex
/* 169 */           .getMessage(), ex);
/*     */     }
/* 171 */     catch (JAXBException ex) {
/* 172 */       throw new HttpMessageConversionException("Invalid JAXB setup: " + ex.getMessage(), ex);
/*     */     }
/* 174 */     catch (XMLStreamException ex) {
/* 175 */       throw new HttpMessageConversionException(ex.getMessage(), ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected T createCollection(Class<?> collectionClass) {
/* 187 */     if (!collectionClass.isInterface()) {
/*     */       try {
/* 189 */         return (T)collectionClass.newInstance();
/*     */       }
/* 191 */       catch (Throwable ex) {
/* 192 */         throw new IllegalArgumentException("Could not instantiate collection class: " + collectionClass
/* 193 */             .getName(), ex);
/*     */       } 
/*     */     }
/* 196 */     if (List.class == collectionClass) {
/* 197 */       return (T)new ArrayList();
/*     */     }
/* 199 */     if (SortedSet.class == collectionClass) {
/* 200 */       return (T)new TreeSet();
/*     */     }
/*     */     
/* 203 */     return (T)new LinkedHashSet();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private int moveToFirstChildOfRootElement(XMLStreamReader streamReader) throws XMLStreamException {
/* 209 */     int event = streamReader.next();
/* 210 */     while (event != 1) {
/* 211 */       event = streamReader.next();
/*     */     }
/*     */ 
/*     */     
/* 215 */     event = streamReader.next();
/* 216 */     while (event != 1 && event != 8) {
/* 217 */       event = streamReader.next();
/*     */     }
/* 219 */     return event;
/*     */   }
/*     */   
/*     */   private int moveToNextElement(XMLStreamReader streamReader) throws XMLStreamException {
/* 223 */     int event = streamReader.getEventType();
/* 224 */     while (event != 1 && event != 8) {
/* 225 */       event = streamReader.next();
/*     */     }
/* 227 */     return event;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(T t, Type type, MediaType contentType, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
/* 234 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void writeToResult(T t, HttpHeaders headers, Result result) throws IOException {
/* 239 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected XMLInputFactory createXmlInputFactory() {
/* 249 */     XMLInputFactory inputFactory = XMLInputFactory.newInstance();
/* 250 */     inputFactory.setProperty("javax.xml.stream.supportDTD", Boolean.valueOf(false));
/* 251 */     inputFactory.setProperty("javax.xml.stream.isSupportingExternalEntities", Boolean.valueOf(false));
/* 252 */     inputFactory.setXMLResolver(NO_OP_XML_RESOLVER);
/* 253 */     return inputFactory;
/*     */   }
/*     */ 
/*     */   
/* 257 */   private static final XMLResolver NO_OP_XML_RESOLVER = new XMLResolver()
/*     */     {
/*     */       public Object resolveEntity(String publicID, String systemID, String base, String ns) {
/* 260 */         return StreamUtils.emptyInput();
/*     */       }
/*     */     };
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\http\converter\xml\Jaxb2CollectionHttpMessageConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */