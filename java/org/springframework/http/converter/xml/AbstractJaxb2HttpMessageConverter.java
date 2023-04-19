/*     */ package org.springframework.http.converter.xml;
/*     */ 
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import javax.xml.bind.JAXBContext;
/*     */ import javax.xml.bind.JAXBException;
/*     */ import javax.xml.bind.Marshaller;
/*     */ import javax.xml.bind.Unmarshaller;
/*     */ import org.springframework.http.converter.HttpMessageConversionException;
/*     */ import org.springframework.util.Assert;
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
/*     */ public abstract class AbstractJaxb2HttpMessageConverter<T>
/*     */   extends AbstractXmlHttpMessageConverter<T>
/*     */ {
/*  39 */   private final ConcurrentMap<Class<?>, JAXBContext> jaxbContexts = new ConcurrentHashMap<Class<?>, JAXBContext>(64);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final Marshaller createMarshaller(Class<?> clazz) {
/*     */     try {
/*  50 */       JAXBContext jaxbContext = getJaxbContext(clazz);
/*  51 */       Marshaller marshaller = jaxbContext.createMarshaller();
/*  52 */       customizeMarshaller(marshaller);
/*  53 */       return marshaller;
/*     */     }
/*  55 */     catch (JAXBException ex) {
/*  56 */       throw new HttpMessageConversionException("Could not create Marshaller for class [" + clazz + "]: " + ex
/*  57 */           .getMessage(), ex);
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
/*     */   protected void customizeMarshaller(Marshaller marshaller) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final Unmarshaller createUnmarshaller(Class<?> clazz) {
/*     */     try {
/*  79 */       JAXBContext jaxbContext = getJaxbContext(clazz);
/*  80 */       Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
/*  81 */       customizeUnmarshaller(unmarshaller);
/*  82 */       return unmarshaller;
/*     */     }
/*  84 */     catch (JAXBException ex) {
/*  85 */       throw new HttpMessageConversionException("Could not create Unmarshaller for class [" + clazz + "]: " + ex
/*  86 */           .getMessage(), ex);
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
/*     */   protected void customizeUnmarshaller(Unmarshaller unmarshaller) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final JAXBContext getJaxbContext(Class<?> clazz) {
/* 107 */     Assert.notNull(clazz, "Class must not be null");
/* 108 */     JAXBContext jaxbContext = this.jaxbContexts.get(clazz);
/* 109 */     if (jaxbContext == null) {
/*     */       try {
/* 111 */         jaxbContext = JAXBContext.newInstance(new Class[] { clazz });
/* 112 */         this.jaxbContexts.putIfAbsent(clazz, jaxbContext);
/*     */       }
/* 114 */       catch (JAXBException ex) {
/* 115 */         throw new HttpMessageConversionException("Could not instantiate JAXBContext for class [" + clazz + "]: " + ex
/* 116 */             .getMessage(), ex);
/*     */       } 
/*     */     }
/* 119 */     return jaxbContext;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\http\converter\xml\AbstractJaxb2HttpMessageConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */