/*     */ package org.springframework.util;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
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
/*     */ public abstract class ResourceUtils
/*     */ {
/*     */   public static final String CLASSPATH_URL_PREFIX = "classpath:";
/*     */   public static final String FILE_URL_PREFIX = "file:";
/*     */   public static final String JAR_URL_PREFIX = "jar:";
/*     */   public static final String WAR_URL_PREFIX = "war:";
/*     */   public static final String URL_PROTOCOL_FILE = "file";
/*     */   public static final String URL_PROTOCOL_JAR = "jar";
/*     */   public static final String URL_PROTOCOL_WAR = "war";
/*     */   public static final String URL_PROTOCOL_ZIP = "zip";
/*     */   public static final String URL_PROTOCOL_WSJAR = "wsjar";
/*     */   public static final String URL_PROTOCOL_VFSZIP = "vfszip";
/*     */   public static final String URL_PROTOCOL_VFSFILE = "vfsfile";
/*     */   public static final String URL_PROTOCOL_VFS = "vfs";
/*     */   public static final String JAR_FILE_EXTENSION = ".jar";
/*     */   public static final String JAR_URL_SEPARATOR = "!/";
/*     */   public static final String WAR_URL_SEPARATOR = "*/";
/*     */   
/*     */   public static boolean isUrl(String resourceLocation) {
/* 103 */     if (resourceLocation == null) {
/* 104 */       return false;
/*     */     }
/* 106 */     if (resourceLocation.startsWith("classpath:")) {
/* 107 */       return true;
/*     */     }
/*     */     try {
/* 110 */       new URL(resourceLocation);
/* 111 */       return true;
/*     */     }
/* 113 */     catch (MalformedURLException ex) {
/* 114 */       return false;
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
/*     */   public static URL getURL(String resourceLocation) throws FileNotFoundException {
/* 128 */     Assert.notNull(resourceLocation, "Resource location must not be null");
/* 129 */     if (resourceLocation.startsWith("classpath:")) {
/* 130 */       String path = resourceLocation.substring("classpath:".length());
/* 131 */       ClassLoader cl = ClassUtils.getDefaultClassLoader();
/* 132 */       URL url = (cl != null) ? cl.getResource(path) : ClassLoader.getSystemResource(path);
/* 133 */       if (url == null) {
/* 134 */         String description = "class path resource [" + path + "]";
/* 135 */         throw new FileNotFoundException(description + " cannot be resolved to URL because it does not exist");
/*     */       } 
/*     */       
/* 138 */       return url;
/*     */     } 
/*     */     
/*     */     try {
/* 142 */       return new URL(resourceLocation);
/*     */     }
/* 144 */     catch (MalformedURLException ex) {
/*     */       
/*     */       try {
/* 147 */         return (new File(resourceLocation)).toURI().toURL();
/*     */       }
/* 149 */       catch (MalformedURLException ex2) {
/* 150 */         throw new FileNotFoundException("Resource location [" + resourceLocation + "] is neither a URL not a well-formed file path");
/*     */       } 
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
/*     */ 
/*     */ 
/*     */   
/*     */   public static File getFile(String resourceLocation) throws FileNotFoundException {
/* 168 */     Assert.notNull(resourceLocation, "Resource location must not be null");
/* 169 */     if (resourceLocation.startsWith("classpath:")) {
/* 170 */       String path = resourceLocation.substring("classpath:".length());
/* 171 */       String description = "class path resource [" + path + "]";
/* 172 */       ClassLoader cl = ClassUtils.getDefaultClassLoader();
/* 173 */       URL url = (cl != null) ? cl.getResource(path) : ClassLoader.getSystemResource(path);
/* 174 */       if (url == null) {
/* 175 */         throw new FileNotFoundException(description + " cannot be resolved to absolute file path because it does not exist");
/*     */       }
/*     */       
/* 178 */       return getFile(url, description);
/*     */     } 
/*     */     
/*     */     try {
/* 182 */       return getFile(new URL(resourceLocation));
/*     */     }
/* 184 */     catch (MalformedURLException ex) {
/*     */       
/* 186 */       return new File(resourceLocation);
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
/*     */   public static File getFile(URL resourceUrl) throws FileNotFoundException {
/* 199 */     return getFile(resourceUrl, "URL");
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
/*     */   public static File getFile(URL resourceUrl, String description) throws FileNotFoundException {
/* 213 */     Assert.notNull(resourceUrl, "Resource URL must not be null");
/* 214 */     if (!"file".equals(resourceUrl.getProtocol())) {
/* 215 */       throw new FileNotFoundException(description + " cannot be resolved to absolute file path because it does not reside in the file system: " + resourceUrl);
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/* 220 */       return new File(toURI(resourceUrl).getSchemeSpecificPart());
/*     */     }
/* 222 */     catch (URISyntaxException ex) {
/*     */       
/* 224 */       return new File(resourceUrl.getFile());
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
/*     */   public static File getFile(URI resourceUri) throws FileNotFoundException {
/* 238 */     return getFile(resourceUri, "URI");
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
/*     */   public static File getFile(URI resourceUri, String description) throws FileNotFoundException {
/* 253 */     Assert.notNull(resourceUri, "Resource URI must not be null");
/* 254 */     if (!"file".equals(resourceUri.getScheme())) {
/* 255 */       throw new FileNotFoundException(description + " cannot be resolved to absolute file path because it does not reside in the file system: " + resourceUri);
/*     */     }
/*     */ 
/*     */     
/* 259 */     return new File(resourceUri.getSchemeSpecificPart());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isFileURL(URL url) {
/* 269 */     String protocol = url.getProtocol();
/* 270 */     return ("file".equals(protocol) || "vfsfile".equals(protocol) || "vfs"
/* 271 */       .equals(protocol));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isJarURL(URL url) {
/* 281 */     String protocol = url.getProtocol();
/* 282 */     return ("jar".equals(protocol) || "war".equals(protocol) || "zip"
/* 283 */       .equals(protocol) || "vfszip".equals(protocol) || "wsjar"
/* 284 */       .equals(protocol));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isJarFileURL(URL url) {
/* 295 */     return ("file".equals(url.getProtocol()) && url
/* 296 */       .getPath().toLowerCase().endsWith(".jar"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static URL extractJarFileURL(URL jarUrl) throws MalformedURLException {
/* 307 */     String urlFile = jarUrl.getFile();
/* 308 */     int separatorIndex = urlFile.indexOf("!/");
/* 309 */     if (separatorIndex != -1) {
/* 310 */       String jarFile = urlFile.substring(0, separatorIndex);
/*     */       try {
/* 312 */         return new URL(jarFile);
/*     */       }
/* 314 */       catch (MalformedURLException ex) {
/*     */ 
/*     */         
/* 317 */         if (!jarFile.startsWith("/")) {
/* 318 */           jarFile = "/" + jarFile;
/*     */         }
/* 320 */         return new URL("file:" + jarFile);
/*     */       } 
/*     */     } 
/*     */     
/* 324 */     return jarUrl;
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
/*     */   public static URL extractArchiveURL(URL jarUrl) throws MalformedURLException {
/* 340 */     String urlFile = jarUrl.getFile();
/*     */     
/* 342 */     int endIndex = urlFile.indexOf("*/");
/* 343 */     if (endIndex != -1) {
/*     */       
/* 345 */       String warFile = urlFile.substring(0, endIndex);
/* 346 */       if ("war".equals(jarUrl.getProtocol())) {
/* 347 */         return new URL(warFile);
/*     */       }
/* 349 */       int startIndex = warFile.indexOf("war:");
/* 350 */       if (startIndex != -1) {
/* 351 */         return new URL(warFile.substring(startIndex + "war:".length()));
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 356 */     return extractJarFileURL(jarUrl);
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
/*     */   public static URI toURI(URL url) throws URISyntaxException {
/* 368 */     return toURI(url.toString());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static URI toURI(String location) throws URISyntaxException {
/* 379 */     return new URI(StringUtils.replace(location, " ", "%20"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void useCachesIfNecessary(URLConnection con) {
/* 389 */     con.setUseCaches(con.getClass().getSimpleName().startsWith("JNLP"));
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframewor\\util\ResourceUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */