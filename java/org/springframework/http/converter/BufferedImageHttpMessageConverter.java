/*     */ package org.springframework.http.converter;
/*     */ 
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import javax.imageio.IIOImage;
/*     */ import javax.imageio.ImageIO;
/*     */ import javax.imageio.ImageReadParam;
/*     */ import javax.imageio.ImageReader;
/*     */ import javax.imageio.ImageWriteParam;
/*     */ import javax.imageio.ImageWriter;
/*     */ import javax.imageio.stream.FileCacheImageInputStream;
/*     */ import javax.imageio.stream.FileCacheImageOutputStream;
/*     */ import javax.imageio.stream.ImageInputStream;
/*     */ import javax.imageio.stream.ImageOutputStream;
/*     */ import javax.imageio.stream.MemoryCacheImageInputStream;
/*     */ import javax.imageio.stream.MemoryCacheImageOutputStream;
/*     */ import org.springframework.http.HttpInputMessage;
/*     */ import org.springframework.http.HttpOutputMessage;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.StreamingHttpOutputMessage;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.StringUtils;
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
/*     */ public class BufferedImageHttpMessageConverter
/*     */   implements HttpMessageConverter<BufferedImage>
/*     */ {
/*  70 */   private final List<MediaType> readableMediaTypes = new ArrayList<MediaType>();
/*     */   
/*     */   private MediaType defaultContentType;
/*     */   
/*     */   private File cacheDir;
/*     */ 
/*     */   
/*     */   public BufferedImageHttpMessageConverter() {
/*  78 */     String[] readerMediaTypes = ImageIO.getReaderMIMETypes();
/*  79 */     for (String mediaType : readerMediaTypes) {
/*  80 */       if (StringUtils.hasText(mediaType)) {
/*  81 */         this.readableMediaTypes.add(MediaType.parseMediaType(mediaType));
/*     */       }
/*     */     } 
/*     */     
/*  85 */     String[] writerMediaTypes = ImageIO.getWriterMIMETypes();
/*  86 */     for (String mediaType : writerMediaTypes) {
/*  87 */       if (StringUtils.hasText(mediaType)) {
/*  88 */         this.defaultContentType = MediaType.parseMediaType(mediaType);
/*     */         break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDefaultContentType(MediaType defaultContentType) {
/* 100 */     Assert.notNull(defaultContentType, "'contentType' must not be null");
/* 101 */     Iterator<ImageWriter> imageWriters = ImageIO.getImageWritersByMIMEType(defaultContentType.toString());
/* 102 */     if (!imageWriters.hasNext()) {
/* 103 */       throw new IllegalArgumentException("Content-Type [" + defaultContentType + "] is not supported by the Java Image I/O API");
/*     */     }
/*     */ 
/*     */     
/* 107 */     this.defaultContentType = defaultContentType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MediaType getDefaultContentType() {
/* 115 */     return this.defaultContentType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCacheDir(File cacheDir) {
/* 123 */     Assert.notNull(cacheDir, "'cacheDir' must not be null");
/* 124 */     Assert.isTrue(cacheDir.isDirectory(), "'cacheDir' is not a directory");
/* 125 */     this.cacheDir = cacheDir;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canRead(Class<?> clazz, MediaType mediaType) {
/* 131 */     return (BufferedImage.class == clazz && isReadable(mediaType));
/*     */   }
/*     */   
/*     */   private boolean isReadable(MediaType mediaType) {
/* 135 */     if (mediaType == null) {
/* 136 */       return true;
/*     */     }
/* 138 */     Iterator<ImageReader> imageReaders = ImageIO.getImageReadersByMIMEType(mediaType.toString());
/* 139 */     return imageReaders.hasNext();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canWrite(Class<?> clazz, MediaType mediaType) {
/* 144 */     return (BufferedImage.class == clazz && isWritable(mediaType));
/*     */   }
/*     */   
/*     */   private boolean isWritable(MediaType mediaType) {
/* 148 */     if (mediaType == null || MediaType.ALL.equals(mediaType)) {
/* 149 */       return true;
/*     */     }
/* 151 */     Iterator<ImageWriter> imageWriters = ImageIO.getImageWritersByMIMEType(mediaType.toString());
/* 152 */     return imageWriters.hasNext();
/*     */   }
/*     */ 
/*     */   
/*     */   public List<MediaType> getSupportedMediaTypes() {
/* 157 */     return Collections.unmodifiableList(this.readableMediaTypes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BufferedImage read(Class<? extends BufferedImage> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
/* 164 */     ImageInputStream imageInputStream = null;
/* 165 */     ImageReader imageReader = null;
/*     */     try {
/* 167 */       imageInputStream = createImageInputStream(inputMessage.getBody());
/* 168 */       MediaType contentType = inputMessage.getHeaders().getContentType();
/* 169 */       Iterator<ImageReader> imageReaders = ImageIO.getImageReadersByMIMEType(contentType.toString());
/* 170 */       if (imageReaders.hasNext()) {
/* 171 */         imageReader = imageReaders.next();
/* 172 */         ImageReadParam irp = imageReader.getDefaultReadParam();
/* 173 */         process(irp);
/* 174 */         imageReader.setInput(imageInputStream, true);
/* 175 */         return imageReader.read(0, irp);
/*     */       } 
/*     */       
/* 178 */       throw new HttpMessageNotReadableException("Could not find javax.imageio.ImageReader for Content-Type [" + contentType + "]");
/*     */     
/*     */     }
/*     */     finally {
/*     */       
/* 183 */       if (imageReader != null) {
/* 184 */         imageReader.dispose();
/*     */       }
/* 186 */       if (imageInputStream != null) {
/*     */         try {
/* 188 */           imageInputStream.close();
/*     */         }
/* 190 */         catch (IOException iOException) {}
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private ImageInputStream createImageInputStream(InputStream is) throws IOException {
/* 198 */     if (this.cacheDir != null) {
/* 199 */       return new FileCacheImageInputStream(is, this.cacheDir);
/*     */     }
/*     */     
/* 202 */     return new MemoryCacheImageInputStream(is);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(final BufferedImage image, MediaType contentType, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
/* 211 */     final MediaType selectedContentType = getContentType(contentType);
/* 212 */     outputMessage.getHeaders().setContentType(selectedContentType);
/*     */     
/* 214 */     if (outputMessage instanceof StreamingHttpOutputMessage) {
/* 215 */       StreamingHttpOutputMessage streamingOutputMessage = (StreamingHttpOutputMessage)outputMessage;
/* 216 */       streamingOutputMessage.setBody(new StreamingHttpOutputMessage.Body()
/*     */           {
/*     */             public void writeTo(OutputStream outputStream) throws IOException {
/* 219 */               BufferedImageHttpMessageConverter.this.writeInternal(image, selectedContentType, outputStream);
/*     */             }
/*     */           });
/*     */     } else {
/*     */       
/* 224 */       writeInternal(image, selectedContentType, outputMessage.getBody());
/*     */     } 
/*     */   }
/*     */   
/*     */   private MediaType getContentType(MediaType contentType) {
/* 229 */     if (contentType == null || contentType.isWildcardType() || contentType.isWildcardSubtype()) {
/* 230 */       contentType = getDefaultContentType();
/*     */     }
/* 232 */     Assert.notNull(contentType, "Could not select Content-Type. Please specify one through the 'defaultContentType' property.");
/*     */     
/* 234 */     return contentType;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeInternal(BufferedImage image, MediaType contentType, OutputStream body) throws IOException, HttpMessageNotWritableException {
/* 240 */     ImageOutputStream imageOutputStream = null;
/* 241 */     ImageWriter imageWriter = null;
/*     */     try {
/* 243 */       Iterator<ImageWriter> imageWriters = ImageIO.getImageWritersByMIMEType(contentType.toString());
/* 244 */       if (imageWriters.hasNext()) {
/* 245 */         imageWriter = imageWriters.next();
/* 246 */         ImageWriteParam iwp = imageWriter.getDefaultWriteParam();
/* 247 */         process(iwp);
/* 248 */         imageOutputStream = createImageOutputStream(body);
/* 249 */         imageWriter.setOutput(imageOutputStream);
/* 250 */         imageWriter.write(null, new IIOImage(image, null, null), iwp);
/*     */       } else {
/*     */         
/* 253 */         throw new HttpMessageNotWritableException("Could not find javax.imageio.ImageWriter for Content-Type [" + contentType + "]");
/*     */       }
/*     */     
/*     */     } finally {
/*     */       
/* 258 */       if (imageWriter != null) {
/* 259 */         imageWriter.dispose();
/*     */       }
/* 261 */       if (imageOutputStream != null) {
/*     */         try {
/* 263 */           imageOutputStream.close();
/*     */         }
/* 265 */         catch (IOException iOException) {}
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private ImageOutputStream createImageOutputStream(OutputStream os) throws IOException {
/* 273 */     if (this.cacheDir != null) {
/* 274 */       return new FileCacheImageOutputStream(os, this.cacheDir);
/*     */     }
/*     */     
/* 277 */     return new MemoryCacheImageOutputStream(os);
/*     */   }
/*     */   
/*     */   protected void process(ImageReadParam irp) {}
/*     */   
/*     */   protected void process(ImageWriteParam iwp) {}
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\http\converter\BufferedImageHttpMessageConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */