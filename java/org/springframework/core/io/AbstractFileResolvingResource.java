/*     */ package org.springframework.core.io;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
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
/*     */ public abstract class AbstractFileResolvingResource
/*     */   extends AbstractResource
/*     */ {
/*     */   public File getFile() throws IOException {
/*  48 */     URL url = getURL();
/*  49 */     if (url.getProtocol().startsWith("vfs")) {
/*  50 */       return VfsResourceDelegate.getResource(url).getFile();
/*     */     }
/*  52 */     return ResourceUtils.getFile(url, getDescription());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected File getFileForLastModifiedCheck() throws IOException {
/*  61 */     URL url = getURL();
/*  62 */     if (ResourceUtils.isJarURL(url)) {
/*  63 */       URL actualUrl = ResourceUtils.extractArchiveURL(url);
/*  64 */       if (actualUrl.getProtocol().startsWith("vfs")) {
/*  65 */         return VfsResourceDelegate.getResource(actualUrl).getFile();
/*     */       }
/*  67 */       return ResourceUtils.getFile(actualUrl, "Jar URL");
/*     */     } 
/*     */     
/*  70 */     return getFile();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected File getFile(URI uri) throws IOException {
/*  80 */     if (uri.getScheme().startsWith("vfs")) {
/*  81 */       return VfsResourceDelegate.getResource(uri).getFile();
/*     */     }
/*  83 */     return ResourceUtils.getFile(uri, getDescription());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean exists() {
/*     */     try {
/*  90 */       URL url = getURL();
/*  91 */       if (ResourceUtils.isFileURL(url))
/*     */       {
/*  93 */         return getFile().exists();
/*     */       }
/*     */ 
/*     */       
/*  97 */       URLConnection con = url.openConnection();
/*  98 */       customizeConnection(con);
/*  99 */       HttpURLConnection httpCon = (con instanceof HttpURLConnection) ? (HttpURLConnection)con : null;
/*     */       
/* 101 */       if (httpCon != null) {
/* 102 */         int code = httpCon.getResponseCode();
/* 103 */         if (code == 200) {
/* 104 */           return true;
/*     */         }
/* 106 */         if (code == 404) {
/* 107 */           return false;
/*     */         }
/*     */       } 
/* 110 */       if (con.getContentLength() >= 0) {
/* 111 */         return true;
/*     */       }
/* 113 */       if (httpCon != null) {
/*     */         
/* 115 */         httpCon.disconnect();
/* 116 */         return false;
/*     */       } 
/*     */ 
/*     */       
/* 120 */       getInputStream().close();
/* 121 */       return true;
/*     */ 
/*     */     
/*     */     }
/* 125 */     catch (IOException ex) {
/* 126 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isReadable() {
/*     */     try {
/* 133 */       URL url = getURL();
/* 134 */       if (ResourceUtils.isFileURL(url)) {
/*     */         
/* 136 */         File file = getFile();
/* 137 */         return (file.canRead() && !file.isDirectory());
/*     */       } 
/*     */       
/* 140 */       return true;
/*     */     
/*     */     }
/* 143 */     catch (IOException ex) {
/* 144 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public long contentLength() throws IOException {
/* 150 */     URL url = getURL();
/* 151 */     if (ResourceUtils.isFileURL(url))
/*     */     {
/* 153 */       return getFile().length();
/*     */     }
/*     */ 
/*     */     
/* 157 */     URLConnection con = url.openConnection();
/* 158 */     customizeConnection(con);
/* 159 */     return con.getContentLength();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public long lastModified() throws IOException {
/* 165 */     URL url = getURL();
/* 166 */     if (ResourceUtils.isFileURL(url) || ResourceUtils.isJarURL(url)) {
/*     */       
/*     */       try {
/* 169 */         return super.lastModified();
/*     */       }
/* 171 */       catch (FileNotFoundException fileNotFoundException) {}
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 176 */     URLConnection con = url.openConnection();
/* 177 */     customizeConnection(con);
/* 178 */     return con.getLastModified();
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
/*     */   
/*     */   protected void customizeConnection(URLConnection con) throws IOException {
/* 191 */     ResourceUtils.useCachesIfNecessary(con);
/* 192 */     if (con instanceof HttpURLConnection) {
/* 193 */       customizeConnection((HttpURLConnection)con);
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
/*     */   protected void customizeConnection(HttpURLConnection con) throws IOException {
/* 205 */     con.setRequestMethod("HEAD");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class VfsResourceDelegate
/*     */   {
/*     */     public static Resource getResource(URL url) throws IOException {
/* 215 */       return new VfsResource(VfsUtils.getRoot(url));
/*     */     }
/*     */     
/*     */     public static Resource getResource(URI uri) throws IOException {
/* 219 */       return new VfsResource(VfsUtils.getRoot(uri));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\io\AbstractFileResolvingResource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */