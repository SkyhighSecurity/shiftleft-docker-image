/*     */ package org.springframework.core.io;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import org.springframework.core.NestedIOException;
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
/*     */ public class VfsResource
/*     */   extends AbstractResource
/*     */ {
/*     */   private final Object resource;
/*     */   
/*     */   public VfsResource(Object resource) {
/*  48 */     Assert.notNull(resource, "VirtualFile must not be null");
/*  49 */     this.resource = resource;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public InputStream getInputStream() throws IOException {
/*  55 */     return VfsUtils.getInputStream(this.resource);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean exists() {
/*  60 */     return VfsUtils.exists(this.resource);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isReadable() {
/*  65 */     return VfsUtils.isReadable(this.resource);
/*     */   }
/*     */ 
/*     */   
/*     */   public URL getURL() throws IOException {
/*     */     try {
/*  71 */       return VfsUtils.getURL(this.resource);
/*     */     }
/*  73 */     catch (Exception ex) {
/*  74 */       throw new NestedIOException("Failed to obtain URL for file " + this.resource, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public URI getURI() throws IOException {
/*     */     try {
/*  81 */       return VfsUtils.getURI(this.resource);
/*     */     }
/*  83 */     catch (Exception ex) {
/*  84 */       throw new NestedIOException("Failed to obtain URI for " + this.resource, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public File getFile() throws IOException {
/*  90 */     return VfsUtils.getFile(this.resource);
/*     */   }
/*     */ 
/*     */   
/*     */   public long contentLength() throws IOException {
/*  95 */     return VfsUtils.getSize(this.resource);
/*     */   }
/*     */ 
/*     */   
/*     */   public long lastModified() throws IOException {
/* 100 */     return VfsUtils.getLastModified(this.resource);
/*     */   }
/*     */ 
/*     */   
/*     */   public Resource createRelative(String relativePath) throws IOException {
/* 105 */     if (!relativePath.startsWith(".") && relativePath.contains("/")) {
/*     */       try {
/* 107 */         return new VfsResource(VfsUtils.getChild(this.resource, relativePath));
/*     */       }
/* 109 */       catch (IOException iOException) {}
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 114 */     return new VfsResource(VfsUtils.getRelative(new URL(getURL(), relativePath)));
/*     */   }
/*     */ 
/*     */   
/*     */   public String getFilename() {
/* 119 */     return VfsUtils.getName(this.resource);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDescription() {
/* 124 */     return "VFS resource [" + this.resource + "]";
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 129 */     return (obj == this || (obj instanceof VfsResource && this.resource.equals(((VfsResource)obj).resource)));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 134 */     return this.resource.hashCode();
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\io\VfsResource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */