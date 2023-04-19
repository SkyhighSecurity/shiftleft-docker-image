/*     */ package org.springframework.http.converter.xml;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import javax.xml.transform.Result;
/*     */ import javax.xml.transform.Source;
/*     */ import org.springframework.beans.TypeMismatchException;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.converter.HttpMessageNotReadableException;
/*     */ import org.springframework.http.converter.HttpMessageNotWritableException;
/*     */ import org.springframework.oxm.Marshaller;
/*     */ import org.springframework.oxm.MarshallingFailureException;
/*     */ import org.springframework.oxm.Unmarshaller;
/*     */ import org.springframework.oxm.UnmarshallingFailureException;
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
/*     */ public class MarshallingHttpMessageConverter
/*     */   extends AbstractXmlHttpMessageConverter<Object>
/*     */ {
/*     */   private Marshaller marshaller;
/*     */   private Unmarshaller unmarshaller;
/*     */   
/*     */   public MarshallingHttpMessageConverter() {}
/*     */   
/*     */   public MarshallingHttpMessageConverter(Marshaller marshaller) {
/*  72 */     Assert.notNull(marshaller, "Marshaller must not be null");
/*  73 */     this.marshaller = marshaller;
/*  74 */     if (marshaller instanceof Unmarshaller) {
/*  75 */       this.unmarshaller = (Unmarshaller)marshaller;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MarshallingHttpMessageConverter(Marshaller marshaller, Unmarshaller unmarshaller) {
/*  86 */     Assert.notNull(marshaller, "Marshaller must not be null");
/*  87 */     Assert.notNull(unmarshaller, "Unmarshaller must not be null");
/*  88 */     this.marshaller = marshaller;
/*  89 */     this.unmarshaller = unmarshaller;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMarshaller(Marshaller marshaller) {
/*  97 */     this.marshaller = marshaller;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUnmarshaller(Unmarshaller unmarshaller) {
/* 104 */     this.unmarshaller = unmarshaller;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canRead(Class<?> clazz, MediaType mediaType) {
/* 110 */     return (canRead(mediaType) && this.unmarshaller != null && this.unmarshaller.supports(clazz));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canWrite(Class<?> clazz, MediaType mediaType) {
/* 115 */     return (canWrite(mediaType) && this.marshaller != null && this.marshaller.supports(clazz));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean supports(Class<?> clazz) {
/* 121 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   protected Object readFromSource(Class<?> clazz, HttpHeaders headers, Source source) throws IOException {
/* 126 */     Assert.notNull(this.unmarshaller, "Property 'unmarshaller' is required");
/*     */     try {
/* 128 */       Object result = this.unmarshaller.unmarshal(source);
/* 129 */       if (!clazz.isInstance(result)) {
/* 130 */         throw new TypeMismatchException(result, clazz);
/*     */       }
/* 132 */       return result;
/*     */     }
/* 134 */     catch (UnmarshallingFailureException ex) {
/* 135 */       throw new HttpMessageNotReadableException("Could not read [" + clazz + "]", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void writeToResult(Object o, HttpHeaders headers, Result result) throws IOException {
/* 141 */     Assert.notNull(this.marshaller, "Property 'marshaller' is required");
/*     */     try {
/* 143 */       this.marshaller.marshal(o, result);
/*     */     }
/* 145 */     catch (MarshallingFailureException ex) {
/* 146 */       throw new HttpMessageNotWritableException("Could not write [" + o + "]", ex);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\http\converter\xml\MarshallingHttpMessageConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */