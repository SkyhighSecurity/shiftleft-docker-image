/*     */ package org.springframework.core.io;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import org.springframework.core.NestedIOException;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ResourceUtils;
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
/*     */ public abstract class AbstractResource
/*     */   implements Resource
/*     */ {
/*     */   public boolean exists() {
/*     */     try {
/*  53 */       return getFile().exists();
/*     */     }
/*  55 */     catch (IOException ex) {
/*     */       
/*     */       try {
/*  58 */         getInputStream().close();
/*  59 */         return true;
/*     */       }
/*  61 */       catch (Throwable isEx) {
/*  62 */         return false;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isReadable() {
/*  72 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isOpen() {
/*  80 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URL getURL() throws IOException {
/*  89 */     throw new FileNotFoundException(getDescription() + " cannot be resolved to URL");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URI getURI() throws IOException {
/*  98 */     URL url = getURL();
/*     */     try {
/* 100 */       return ResourceUtils.toURI(url);
/*     */     }
/* 102 */     catch (URISyntaxException ex) {
/* 103 */       throw new NestedIOException("Invalid URI [" + url + "]", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File getFile() throws IOException {
/* 113 */     throw new FileNotFoundException(getDescription() + " cannot be resolved to absolute file path");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long contentLength() throws IOException {
/* 124 */     InputStream is = getInputStream();
/* 125 */     Assert.state((is != null), "Resource InputStream must not be null");
/*     */     try {
/* 127 */       long size = 0L;
/* 128 */       byte[] buf = new byte[256];
/*     */       int read;
/* 130 */       while ((read = is.read(buf)) != -1) {
/* 131 */         size += read;
/*     */       }
/* 133 */       return size;
/*     */     } finally {
/*     */       
/*     */       try {
/* 137 */         is.close();
/*     */       }
/* 139 */       catch (IOException iOException) {}
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
/*     */   public long lastModified() throws IOException {
/* 151 */     File fileToCheck = getFileForLastModifiedCheck();
/* 152 */     long lastModified = fileToCheck.lastModified();
/* 153 */     if (lastModified == 0L && !fileToCheck.exists()) {
/* 154 */       throw new FileNotFoundException(getDescription() + " cannot be resolved in the file system for checking its last-modified timestamp");
/*     */     }
/*     */     
/* 157 */     return lastModified;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected File getFileForLastModifiedCheck() throws IOException {
/* 169 */     return getFile();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Resource createRelative(String relativePath) throws IOException {
/* 178 */     throw new FileNotFoundException("Cannot create a relative resource for " + getDescription());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFilename() {
/* 187 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 197 */     return getDescription();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 206 */     return (obj == this || (obj instanceof Resource && ((Resource)obj)
/* 207 */       .getDescription().equals(getDescription())));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 216 */     return getDescription().hashCode();
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\io\AbstractResource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */