/*     */ package org.springframework.http.converter;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.lang.reflect.Type;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpOutputMessage;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.StreamingHttpOutputMessage;
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
/*     */ public abstract class AbstractGenericHttpMessageConverter<T>
/*     */   extends AbstractHttpMessageConverter<T>
/*     */   implements GenericHttpMessageConverter<T>
/*     */ {
/*     */   protected AbstractGenericHttpMessageConverter() {}
/*     */   
/*     */   protected AbstractGenericHttpMessageConverter(MediaType supportedMediaType) {
/*  50 */     super(supportedMediaType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractGenericHttpMessageConverter(MediaType... supportedMediaTypes) {
/*  58 */     super(supportedMediaTypes);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean supports(Class<?> clazz) {
/*  64 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canRead(Type type, Class<?> contextClass, MediaType mediaType) {
/*  69 */     return (type instanceof Class) ? canRead((Class)type, mediaType) : canRead(mediaType);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canWrite(Type type, Class<?> clazz, MediaType mediaType) {
/*  74 */     return canWrite(clazz, mediaType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void write(final T t, final Type type, MediaType contentType, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
/*  84 */     final HttpHeaders headers = outputMessage.getHeaders();
/*  85 */     addDefaultHeaders(headers, t, contentType);
/*     */     
/*  87 */     if (outputMessage instanceof StreamingHttpOutputMessage) {
/*  88 */       StreamingHttpOutputMessage streamingOutputMessage = (StreamingHttpOutputMessage)outputMessage;
/*  89 */       streamingOutputMessage.setBody(new StreamingHttpOutputMessage.Body()
/*     */           {
/*     */             public void writeTo(final OutputStream outputStream) throws IOException {
/*  92 */               AbstractGenericHttpMessageConverter.this.writeInternal(t, type, new HttpOutputMessage()
/*     */                   {
/*     */                     public OutputStream getBody() throws IOException {
/*  95 */                       return outputStream;
/*     */                     }
/*     */                     
/*     */                     public HttpHeaders getHeaders() {
/*  99 */                       return headers;
/*     */                     }
/*     */                   });
/*     */             }
/*     */           });
/*     */     } else {
/*     */       
/* 106 */       writeInternal(t, type, outputMessage);
/* 107 */       outputMessage.getBody().flush();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeInternal(T t, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
/* 115 */     writeInternal(t, (Type)null, outputMessage);
/*     */   }
/*     */   
/*     */   protected abstract void writeInternal(T paramT, Type paramType, HttpOutputMessage paramHttpOutputMessage) throws IOException, HttpMessageNotWritableException;
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\http\converter\AbstractGenericHttpMessageConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */