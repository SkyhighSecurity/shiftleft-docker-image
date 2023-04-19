/*    */ package org.springframework.http.converter.xml;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import javax.xml.transform.Result;
/*    */ import javax.xml.transform.Source;
/*    */ import javax.xml.transform.TransformerException;
/*    */ import javax.xml.transform.TransformerFactory;
/*    */ import javax.xml.transform.stream.StreamResult;
/*    */ import javax.xml.transform.stream.StreamSource;
/*    */ import org.springframework.http.HttpHeaders;
/*    */ import org.springframework.http.HttpInputMessage;
/*    */ import org.springframework.http.HttpOutputMessage;
/*    */ import org.springframework.http.MediaType;
/*    */ import org.springframework.http.converter.AbstractHttpMessageConverter;
/*    */ import org.springframework.http.converter.HttpMessageNotReadableException;
/*    */ import org.springframework.http.converter.HttpMessageNotWritableException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class AbstractXmlHttpMessageConverter<T>
/*    */   extends AbstractHttpMessageConverter<T>
/*    */ {
/* 48 */   private final TransformerFactory transformerFactory = TransformerFactory.newInstance();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected AbstractXmlHttpMessageConverter() {
/* 56 */     super(new MediaType[] { MediaType.APPLICATION_XML, MediaType.TEXT_XML, new MediaType("application", "*+xml") });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final T readInternal(Class<? extends T> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
/* 64 */     return readFromSource(clazz, inputMessage.getHeaders(), new StreamSource(inputMessage.getBody()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected final void writeInternal(T t, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
/* 71 */     writeToResult(t, outputMessage.getHeaders(), new StreamResult(outputMessage.getBody()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void transform(Source source, Result result) throws TransformerException {
/* 81 */     this.transformerFactory.newTransformer().transform(source, result);
/*    */   }
/*    */   
/*    */   protected abstract T readFromSource(Class<? extends T> paramClass, HttpHeaders paramHttpHeaders, Source paramSource) throws IOException, HttpMessageNotReadableException;
/*    */   
/*    */   protected abstract void writeToResult(T paramT, HttpHeaders paramHttpHeaders, Result paramResult) throws IOException, HttpMessageNotWritableException;
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\http\converter\xml\AbstractXmlHttpMessageConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */