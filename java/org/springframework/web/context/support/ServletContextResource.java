/*     */ package org.springframework.web.context.support;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import javax.servlet.ServletContext;
/*     */ import org.springframework.core.io.AbstractFileResolvingResource;
/*     */ import org.springframework.core.io.ContextResource;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ResourceUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.util.WebUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ServletContextResource
/*     */   extends AbstractFileResolvingResource
/*     */   implements ContextResource
/*     */ {
/*     */   private final ServletContext servletContext;
/*     */   private final String path;
/*     */   
/*     */   public ServletContextResource(ServletContext servletContext, String path) {
/*  68 */     Assert.notNull(servletContext, "Cannot resolve ServletContextResource without ServletContext");
/*  69 */     this.servletContext = servletContext;
/*     */ 
/*     */     
/*  72 */     Assert.notNull(path, "Path is required");
/*  73 */     String pathToUse = StringUtils.cleanPath(path);
/*  74 */     if (!pathToUse.startsWith("/")) {
/*  75 */       pathToUse = "/" + pathToUse;
/*     */     }
/*  77 */     this.path = pathToUse;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ServletContext getServletContext() {
/*  85 */     return this.servletContext;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getPath() {
/*  92 */     return this.path;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean exists() {
/*     */     try {
/* 102 */       URL url = this.servletContext.getResource(this.path);
/* 103 */       return (url != null);
/*     */     }
/* 105 */     catch (MalformedURLException ex) {
/* 106 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isReadable() {
/* 117 */     InputStream is = this.servletContext.getResourceAsStream(this.path);
/* 118 */     if (is != null) {
/*     */       try {
/* 120 */         is.close();
/*     */       }
/* 122 */       catch (IOException iOException) {}
/*     */ 
/*     */       
/* 125 */       return true;
/*     */     } 
/*     */     
/* 128 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InputStream getInputStream() throws IOException {
/* 139 */     InputStream is = this.servletContext.getResourceAsStream(this.path);
/* 140 */     if (is == null) {
/* 141 */       throw new FileNotFoundException("Could not open " + getDescription());
/*     */     }
/* 143 */     return is;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URL getURL() throws IOException {
/* 153 */     URL url = this.servletContext.getResource(this.path);
/* 154 */     if (url == null) {
/* 155 */       throw new FileNotFoundException(
/* 156 */           getDescription() + " cannot be resolved to URL because it does not exist");
/*     */     }
/* 158 */     return url;
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
/*     */   public File getFile() throws IOException {
/* 170 */     URL url = this.servletContext.getResource(this.path);
/* 171 */     if (url != null && ResourceUtils.isFileURL(url))
/*     */     {
/* 173 */       return super.getFile();
/*     */     }
/*     */     
/* 176 */     String realPath = WebUtils.getRealPath(this.servletContext, this.path);
/* 177 */     return new File(realPath);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Resource createRelative(String relativePath) {
/* 188 */     String pathToUse = StringUtils.applyRelativePath(this.path, relativePath);
/* 189 */     return (Resource)new ServletContextResource(this.servletContext, pathToUse);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFilename() {
/* 199 */     return StringUtils.getFilename(this.path);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDescription() {
/* 208 */     return "ServletContext resource [" + this.path + "]";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getPathWithinContext() {
/* 213 */     return this.path;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 222 */     if (obj == this) {
/* 223 */       return true;
/*     */     }
/* 225 */     if (obj instanceof ServletContextResource) {
/* 226 */       ServletContextResource otherRes = (ServletContextResource)obj;
/* 227 */       return (this.servletContext.equals(otherRes.servletContext) && this.path.equals(otherRes.path));
/*     */     } 
/* 229 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 238 */     return this.path.hashCode();
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\context\support\ServletContextResource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */