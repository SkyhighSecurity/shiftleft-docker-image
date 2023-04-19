/*     */ package org.springframework.core.io;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ResourceUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class UrlResource
/*     */   extends AbstractFileResolvingResource
/*     */ {
/*     */   private final URI uri;
/*     */   private final URL url;
/*     */   private final URL cleanedUrl;
/*     */   
/*     */   public UrlResource(URI uri) throws MalformedURLException {
/*  67 */     Assert.notNull(uri, "URI must not be null");
/*  68 */     this.uri = uri;
/*  69 */     this.url = uri.toURL();
/*  70 */     this.cleanedUrl = getCleanedUrl(this.url, uri.toString());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UrlResource(URL url) {
/*  78 */     Assert.notNull(url, "URL must not be null");
/*  79 */     this.url = url;
/*  80 */     this.cleanedUrl = getCleanedUrl(this.url, url.toString());
/*  81 */     this.uri = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UrlResource(String path) throws MalformedURLException {
/*  92 */     Assert.notNull(path, "Path must not be null");
/*  93 */     this.uri = null;
/*  94 */     this.url = new URL(path);
/*  95 */     this.cleanedUrl = getCleanedUrl(this.url, path);
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
/*     */   
/*     */   public UrlResource(String protocol, String location) throws MalformedURLException {
/* 109 */     this(protocol, location, null);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public UrlResource(String protocol, String location, String fragment) throws MalformedURLException {
/*     */     try {
/* 126 */       this.uri = new URI(protocol, location, fragment);
/* 127 */       this.url = this.uri.toURL();
/* 128 */       this.cleanedUrl = getCleanedUrl(this.url, this.uri.toString());
/*     */     }
/* 130 */     catch (URISyntaxException ex) {
/* 131 */       MalformedURLException exToThrow = new MalformedURLException(ex.getMessage());
/* 132 */       exToThrow.initCause(ex);
/* 133 */       throw exToThrow;
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
/*     */   
/*     */   private URL getCleanedUrl(URL originalUrl, String originalPath) {
/*     */     try {
/* 147 */       return new URL(StringUtils.cleanPath(originalPath));
/*     */     }
/* 149 */     catch (MalformedURLException ex) {
/*     */ 
/*     */       
/* 152 */       return originalUrl;
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
/*     */ 
/*     */   
/*     */   public InputStream getInputStream() throws IOException {
/* 166 */     URLConnection con = this.url.openConnection();
/* 167 */     ResourceUtils.useCachesIfNecessary(con);
/*     */     try {
/* 169 */       return con.getInputStream();
/*     */     }
/* 171 */     catch (IOException ex) {
/*     */       
/* 173 */       if (con instanceof HttpURLConnection) {
/* 174 */         ((HttpURLConnection)con).disconnect();
/*     */       }
/* 176 */       throw ex;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URL getURL() throws IOException {
/* 185 */     return this.url;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URI getURI() throws IOException {
/* 194 */     if (this.uri != null) {
/* 195 */       return this.uri;
/*     */     }
/*     */     
/* 198 */     return super.getURI();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File getFile() throws IOException {
/* 209 */     if (this.uri != null) {
/* 210 */       return getFile(this.uri);
/*     */     }
/*     */     
/* 213 */     return super.getFile();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Resource createRelative(String relativePath) throws MalformedURLException {
/* 224 */     if (relativePath.startsWith("/")) {
/* 225 */       relativePath = relativePath.substring(1);
/*     */     }
/* 227 */     return new UrlResource(new URL(this.url, relativePath));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFilename() {
/* 236 */     return StringUtils.getFilename(this.cleanedUrl.getPath());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDescription() {
/* 244 */     return "URL [" + this.url + "]";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 253 */     return (obj == this || (obj instanceof UrlResource && this.cleanedUrl
/* 254 */       .equals(((UrlResource)obj).cleanedUrl)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 262 */     return this.cleanedUrl.hashCode();
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\io\UrlResource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */