/*     */ package org.springframework.http.converter;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.Collection;
/*     */ import org.springframework.core.io.support.ResourceRegion;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpInputMessage;
/*     */ import org.springframework.http.HttpOutputMessage;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.MimeTypeUtils;
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
/*     */ public class ResourceRegionHttpMessageConverter
/*     */   extends AbstractGenericHttpMessageConverter<Object>
/*     */ {
/*  46 */   private static final boolean jafPresent = ClassUtils.isPresent("javax.activation.FileTypeMap", ResourceHttpMessageConverter.class
/*  47 */       .getClassLoader());
/*     */   
/*     */   public ResourceRegionHttpMessageConverter() {
/*  50 */     super(MediaType.ALL);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected MediaType getDefaultContentType(Object object) {
/*  57 */     if (jafPresent) {
/*  58 */       if (object instanceof ResourceRegion) {
/*  59 */         return ActivationMediaTypeFactory.getMediaType(((ResourceRegion)object).getResource());
/*     */       }
/*     */       
/*  62 */       Collection<ResourceRegion> regions = (Collection<ResourceRegion>)object;
/*  63 */       if (!regions.isEmpty()) {
/*  64 */         return ActivationMediaTypeFactory.getMediaType(((ResourceRegion)regions.iterator().next()).getResource());
/*     */       }
/*     */     } 
/*     */     
/*  68 */     return MediaType.APPLICATION_OCTET_STREAM;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canRead(Class<?> clazz, MediaType mediaType) {
/*  73 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canRead(Type type, Class<?> contextClass, MediaType mediaType) {
/*  78 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
/*  85 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ResourceRegion readInternal(Class<?> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
/*  92 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canWrite(Class<?> clazz, MediaType mediaType) {
/*  97 */     return canWrite(clazz, null, mediaType);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canWrite(Type type, Class<?> clazz, MediaType mediaType) {
/* 102 */     if (!(type instanceof ParameterizedType)) {
/* 103 */       return ResourceRegion.class.isAssignableFrom((Class)type);
/*     */     }
/* 105 */     ParameterizedType parameterizedType = (ParameterizedType)type;
/* 106 */     if (!(parameterizedType.getRawType() instanceof Class)) {
/* 107 */       return false;
/*     */     }
/* 109 */     Class<?> rawType = (Class)parameterizedType.getRawType();
/* 110 */     if (!Collection.class.isAssignableFrom(rawType)) {
/* 111 */       return false;
/*     */     }
/* 113 */     if ((parameterizedType.getActualTypeArguments()).length != 1) {
/* 114 */       return false;
/*     */     }
/* 116 */     Type typeArgument = parameterizedType.getActualTypeArguments()[0];
/* 117 */     if (!(typeArgument instanceof Class)) {
/* 118 */       return false;
/*     */     }
/* 120 */     Class<?> typeArgumentClass = (Class)typeArgument;
/* 121 */     return typeArgumentClass.isAssignableFrom(ResourceRegion.class);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeInternal(Object object, Type type, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
/* 129 */     if (object instanceof ResourceRegion) {
/* 130 */       writeResourceRegion((ResourceRegion)object, outputMessage);
/*     */     } else {
/*     */       
/* 133 */       Collection<ResourceRegion> regions = (Collection<ResourceRegion>)object;
/* 134 */       if (regions.size() == 1) {
/* 135 */         writeResourceRegion(regions.iterator().next(), outputMessage);
/*     */       } else {
/*     */         
/* 138 */         writeResourceRegionCollection((Collection<ResourceRegion>)object, outputMessage);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void writeResourceRegion(ResourceRegion region, HttpOutputMessage outputMessage) throws IOException {
/* 145 */     Assert.notNull(region, "ResourceRegion must not be null");
/* 146 */     HttpHeaders responseHeaders = outputMessage.getHeaders();
/*     */     
/* 148 */     long start = region.getPosition();
/* 149 */     long end = start + region.getCount() - 1L;
/* 150 */     Long resourceLength = Long.valueOf(region.getResource().contentLength());
/* 151 */     end = Math.min(end, resourceLength.longValue() - 1L);
/* 152 */     long rangeLength = end - start + 1L;
/* 153 */     responseHeaders.add("Content-Range", "bytes " + start + '-' + end + '/' + resourceLength);
/* 154 */     responseHeaders.setContentLength(rangeLength);
/*     */     
/* 156 */     InputStream in = region.getResource().getInputStream();
/*     */     try {
/* 158 */       StreamUtils.copyRange(in, outputMessage.getBody(), start, end);
/*     */     } finally {
/*     */       
/*     */       try {
/* 162 */         in.close();
/*     */       }
/* 164 */       catch (IOException iOException) {}
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeResourceRegionCollection(Collection<ResourceRegion> resourceRegions, HttpOutputMessage outputMessage) throws IOException {
/* 173 */     Assert.notNull(resourceRegions, "Collection of ResourceRegion should not be null");
/* 174 */     HttpHeaders responseHeaders = outputMessage.getHeaders();
/*     */     
/* 176 */     MediaType contentType = responseHeaders.getContentType();
/* 177 */     String boundaryString = MimeTypeUtils.generateMultipartBoundaryString();
/* 178 */     responseHeaders.set("Content-Type", "multipart/byteranges; boundary=" + boundaryString);
/* 179 */     OutputStream out = outputMessage.getBody();
/*     */     
/* 181 */     for (ResourceRegion region : resourceRegions) {
/* 182 */       long start = region.getPosition();
/* 183 */       long end = start + region.getCount() - 1L;
/* 184 */       InputStream in = region.getResource().getInputStream();
/*     */       
/*     */       try {
/* 187 */         println(out);
/* 188 */         print(out, "--" + boundaryString);
/* 189 */         println(out);
/* 190 */         if (contentType != null) {
/* 191 */           print(out, "Content-Type: " + contentType.toString());
/* 192 */           println(out);
/*     */         } 
/* 194 */         Long resourceLength = Long.valueOf(region.getResource().contentLength());
/* 195 */         end = Math.min(end, resourceLength.longValue() - 1L);
/* 196 */         print(out, "Content-Range: bytes " + start + '-' + end + '/' + resourceLength);
/* 197 */         println(out);
/* 198 */         println(out);
/*     */         
/* 200 */         StreamUtils.copyRange(in, out, start, end);
/*     */       } finally {
/*     */         
/*     */         try {
/* 204 */           in.close();
/*     */         }
/* 206 */         catch (IOException iOException) {}
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 212 */     println(out);
/* 213 */     print(out, "--" + boundaryString + "--");
/*     */   }
/*     */   
/*     */   private static void println(OutputStream os) throws IOException {
/* 217 */     os.write(13);
/* 218 */     os.write(10);
/*     */   }
/*     */   
/*     */   private static void print(OutputStream os, String buf) throws IOException {
/* 222 */     os.write(buf.getBytes("US-ASCII"));
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\http\converter\ResourceRegionHttpMessageConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */