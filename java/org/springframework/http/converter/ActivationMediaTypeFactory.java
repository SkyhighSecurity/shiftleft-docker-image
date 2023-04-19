/*    */ package org.springframework.http.converter;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import javax.activation.FileTypeMap;
/*    */ import javax.activation.MimetypesFileTypeMap;
/*    */ import org.springframework.core.io.ClassPathResource;
/*    */ import org.springframework.core.io.Resource;
/*    */ import org.springframework.http.MediaType;
/*    */ import org.springframework.util.StringUtils;
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
/*    */ class ActivationMediaTypeFactory
/*    */ {
/* 37 */   private static final FileTypeMap fileTypeMap = loadFileTypeMapFromContextSupportModule();
/*    */ 
/*    */ 
/*    */   
/*    */   private static FileTypeMap loadFileTypeMapFromContextSupportModule() {
/* 42 */     ClassPathResource classPathResource = new ClassPathResource("org/springframework/mail/javamail/mime.types");
/* 43 */     if (classPathResource.exists()) {
/* 44 */       InputStream inputStream = null;
/*    */       try {
/* 46 */         inputStream = classPathResource.getInputStream();
/* 47 */         return new MimetypesFileTypeMap(inputStream);
/*    */       }
/* 49 */       catch (IOException iOException) {
/*    */ 
/*    */       
/*    */       } finally {
/* 53 */         if (inputStream != null) {
/*    */           try {
/* 55 */             inputStream.close();
/*    */           }
/* 57 */           catch (IOException iOException) {}
/*    */         }
/*    */       } 
/*    */     } 
/*    */ 
/*    */     
/* 63 */     return FileTypeMap.getDefaultFileTypeMap();
/*    */   }
/*    */   
/*    */   public static MediaType getMediaType(Resource resource) {
/* 67 */     String filename = resource.getFilename();
/* 68 */     if (filename != null) {
/* 69 */       String mediaType = fileTypeMap.getContentType(filename);
/* 70 */       if (StringUtils.hasText(mediaType)) {
/* 71 */         return MediaType.parseMediaType(mediaType);
/*    */       }
/*    */     } 
/* 74 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\http\converter\ActivationMediaTypeFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */