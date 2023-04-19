/*     */ package org.springframework.http.converter;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.charset.Charset;
/*     */ import org.springframework.core.convert.ConversionService;
/*     */ import org.springframework.http.HttpInputMessage;
/*     */ import org.springframework.http.HttpOutputMessage;
/*     */ import org.springframework.http.MediaType;
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
/*     */ 
/*     */ public class ObjectToStringHttpMessageConverter
/*     */   extends AbstractHttpMessageConverter<Object>
/*     */ {
/*     */   private final ConversionService conversionService;
/*     */   private final StringHttpMessageConverter stringHttpMessageConverter;
/*     */   
/*     */   public ObjectToStringHttpMessageConverter(ConversionService conversionService) {
/*  65 */     this(conversionService, StringHttpMessageConverter.DEFAULT_CHARSET);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjectToStringHttpMessageConverter(ConversionService conversionService, Charset defaultCharset) {
/*  74 */     super(defaultCharset, new MediaType[] { MediaType.TEXT_PLAIN });
/*     */     
/*  76 */     Assert.notNull(conversionService, "ConversionService is required");
/*  77 */     this.conversionService = conversionService;
/*  78 */     this.stringHttpMessageConverter = new StringHttpMessageConverter(defaultCharset);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setWriteAcceptCharset(boolean writeAcceptCharset) {
/*  87 */     this.stringHttpMessageConverter.setWriteAcceptCharset(writeAcceptCharset);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canRead(Class<?> clazz, MediaType mediaType) {
/*  93 */     return (this.conversionService.canConvert(String.class, clazz) && canRead(mediaType));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canWrite(Class<?> clazz, MediaType mediaType) {
/*  98 */     return (this.conversionService.canConvert(clazz, String.class) && canWrite(mediaType));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean supports(Class<?> clazz) {
/* 104 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage) throws IOException {
/* 109 */     String value = this.stringHttpMessageConverter.readInternal(String.class, inputMessage);
/* 110 */     return this.conversionService.convert(value, clazz);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void writeInternal(Object obj, HttpOutputMessage outputMessage) throws IOException {
/* 115 */     String value = (String)this.conversionService.convert(obj, String.class);
/* 116 */     this.stringHttpMessageConverter.writeInternal(value, outputMessage);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Long getContentLength(Object obj, MediaType contentType) {
/* 121 */     String value = (String)this.conversionService.convert(obj, String.class);
/* 122 */     return this.stringHttpMessageConverter.getContentLength(value, contentType);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\http\converter\ObjectToStringHttpMessageConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */