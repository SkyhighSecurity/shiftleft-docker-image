/*     */ package org.springframework.http.converter;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.springframework.http.HttpInputMessage;
/*     */ import org.springframework.http.HttpOutputMessage;
/*     */ import org.springframework.http.MediaType;
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
/*     */ public class StringHttpMessageConverter
/*     */   extends AbstractHttpMessageConverter<String>
/*     */ {
/*  43 */   public static final Charset DEFAULT_CHARSET = Charset.forName("ISO-8859-1");
/*     */ 
/*     */ 
/*     */   
/*     */   private volatile List<Charset> availableCharsets;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean writeAcceptCharset = true;
/*     */ 
/*     */ 
/*     */   
/*     */   public StringHttpMessageConverter() {
/*  56 */     this(DEFAULT_CHARSET);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StringHttpMessageConverter(Charset defaultCharset) {
/*  64 */     super(defaultCharset, new MediaType[] { MediaType.TEXT_PLAIN, MediaType.ALL });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setWriteAcceptCharset(boolean writeAcceptCharset) {
/*  73 */     this.writeAcceptCharset = writeAcceptCharset;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean supports(Class<?> clazz) {
/*  79 */     return (String.class == clazz);
/*     */   }
/*     */ 
/*     */   
/*     */   protected String readInternal(Class<? extends String> clazz, HttpInputMessage inputMessage) throws IOException {
/*  84 */     Charset charset = getContentTypeCharset(inputMessage.getHeaders().getContentType());
/*  85 */     return StreamUtils.copyToString(inputMessage.getBody(), charset);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Long getContentLength(String str, MediaType contentType) {
/*  90 */     Charset charset = getContentTypeCharset(contentType);
/*     */     try {
/*  92 */       return Long.valueOf((str.getBytes(charset.name())).length);
/*     */     }
/*  94 */     catch (UnsupportedEncodingException ex) {
/*     */       
/*  96 */       throw new IllegalStateException(ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void writeInternal(String str, HttpOutputMessage outputMessage) throws IOException {
/* 102 */     if (this.writeAcceptCharset) {
/* 103 */       outputMessage.getHeaders().setAcceptCharset(getAcceptedCharsets());
/*     */     }
/* 105 */     Charset charset = getContentTypeCharset(outputMessage.getHeaders().getContentType());
/* 106 */     StreamUtils.copy(str, charset, outputMessage.getBody());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected List<Charset> getAcceptedCharsets() {
/* 117 */     if (this.availableCharsets == null) {
/* 118 */       this
/* 119 */         .availableCharsets = new ArrayList<Charset>(Charset.availableCharsets().values());
/*     */     }
/* 121 */     return this.availableCharsets;
/*     */   }
/*     */   
/*     */   private Charset getContentTypeCharset(MediaType contentType) {
/* 125 */     if (contentType != null && contentType.getCharset() != null) {
/* 126 */       return contentType.getCharset();
/*     */     }
/*     */     
/* 129 */     return getDefaultCharset();
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\http\converter\StringHttpMessageConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */