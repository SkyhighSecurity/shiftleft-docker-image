/*     */ package org.springframework.core.io;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import org.springframework.lang.UsesJava7;
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
/*     */ @UsesJava7
/*     */ public class PathResource
/*     */   extends AbstractResource
/*     */   implements WritableResource
/*     */ {
/*     */   private final Path path;
/*     */   
/*     */   public PathResource(Path path) {
/*  60 */     Assert.notNull(path, "Path must not be null");
/*  61 */     this.path = path.normalize();
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
/*     */   public PathResource(String path) {
/*  73 */     Assert.notNull(path, "Path must not be null");
/*  74 */     this.path = Paths.get(path, new String[0]).normalize();
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
/*     */   public PathResource(URI uri) {
/*  86 */     Assert.notNull(uri, "URI must not be null");
/*  87 */     this.path = Paths.get(uri).normalize();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getPath() {
/*  95 */     return this.path.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean exists() {
/* 104 */     return Files.exists(this.path, new java.nio.file.LinkOption[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isReadable() {
/* 115 */     return (Files.isReadable(this.path) && !Files.isDirectory(this.path, new java.nio.file.LinkOption[0]));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InputStream getInputStream() throws IOException {
/* 124 */     if (!exists()) {
/* 125 */       throw new FileNotFoundException(getPath() + " (no such file or directory)");
/*     */     }
/* 127 */     if (Files.isDirectory(this.path, new java.nio.file.LinkOption[0])) {
/* 128 */       throw new FileNotFoundException(getPath() + " (is a directory)");
/*     */     }
/* 130 */     return Files.newInputStream(this.path, new java.nio.file.OpenOption[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isWritable() {
/* 141 */     return (Files.isWritable(this.path) && !Files.isDirectory(this.path, new java.nio.file.LinkOption[0]));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public OutputStream getOutputStream() throws IOException {
/* 150 */     if (Files.isDirectory(this.path, new java.nio.file.LinkOption[0])) {
/* 151 */       throw new FileNotFoundException(getPath() + " (is a directory)");
/*     */     }
/* 153 */     return Files.newOutputStream(this.path, new java.nio.file.OpenOption[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URL getURL() throws IOException {
/* 163 */     return this.path.toUri().toURL();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URI getURI() throws IOException {
/* 172 */     return this.path.toUri();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File getFile() throws IOException {
/*     */     try {
/* 181 */       return this.path.toFile();
/*     */     }
/* 183 */     catch (UnsupportedOperationException ex) {
/*     */ 
/*     */       
/* 186 */       throw new FileNotFoundException(this.path + " cannot be resolved to absolute file path");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long contentLength() throws IOException {
/* 195 */     return Files.size(this.path);
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
/* 206 */     return Files.getLastModifiedTime(this.path, new java.nio.file.LinkOption[0]).toMillis();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Resource createRelative(String relativePath) throws IOException {
/* 216 */     return new PathResource(this.path.resolve(relativePath));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFilename() {
/* 225 */     return this.path.getFileName().toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDescription() {
/* 230 */     return "path [" + this.path.toAbsolutePath() + "]";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 239 */     return (this == obj || (obj instanceof PathResource && this.path
/* 240 */       .equals(((PathResource)obj).path)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 248 */     return this.path.hashCode();
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\io\PathResource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */