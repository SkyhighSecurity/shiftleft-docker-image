/*     */ package org.springframework.core.io;
/*     */ 
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URL;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ObjectUtils;
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
/*     */ public class ClassPathResource
/*     */   extends AbstractFileResolvingResource
/*     */ {
/*     */   private final String path;
/*     */   private ClassLoader classLoader;
/*     */   private Class<?> clazz;
/*     */   
/*     */   public ClassPathResource(String path) {
/*  63 */     this(path, (ClassLoader)null);
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
/*     */   public ClassPathResource(String path, ClassLoader classLoader) {
/*  76 */     Assert.notNull(path, "Path must not be null");
/*  77 */     String pathToUse = StringUtils.cleanPath(path);
/*  78 */     if (pathToUse.startsWith("/")) {
/*  79 */       pathToUse = pathToUse.substring(1);
/*     */     }
/*  81 */     this.path = pathToUse;
/*  82 */     this.classLoader = (classLoader != null) ? classLoader : ClassUtils.getDefaultClassLoader();
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
/*     */   public ClassPathResource(String path, Class<?> clazz) {
/*  94 */     Assert.notNull(path, "Path must not be null");
/*  95 */     this.path = StringUtils.cleanPath(path);
/*  96 */     this.clazz = clazz;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ClassPathResource(String path, ClassLoader classLoader, Class<?> clazz) {
/* 107 */     this.path = StringUtils.cleanPath(path);
/* 108 */     this.classLoader = classLoader;
/* 109 */     this.clazz = clazz;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getPath() {
/* 117 */     return this.path;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ClassLoader getClassLoader() {
/* 124 */     return (this.clazz != null) ? this.clazz.getClassLoader() : this.classLoader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean exists() {
/* 135 */     return (resolveURL() != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected URL resolveURL() {
/* 143 */     if (this.clazz != null) {
/* 144 */       return this.clazz.getResource(this.path);
/*     */     }
/* 146 */     if (this.classLoader != null) {
/* 147 */       return this.classLoader.getResource(this.path);
/*     */     }
/*     */     
/* 150 */     return ClassLoader.getSystemResource(this.path);
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
/*     */     InputStream is;
/* 162 */     if (this.clazz != null) {
/* 163 */       is = this.clazz.getResourceAsStream(this.path);
/*     */     }
/* 165 */     else if (this.classLoader != null) {
/* 166 */       is = this.classLoader.getResourceAsStream(this.path);
/*     */     } else {
/*     */       
/* 169 */       is = ClassLoader.getSystemResourceAsStream(this.path);
/*     */     } 
/* 171 */     if (is == null) {
/* 172 */       throw new FileNotFoundException(getDescription() + " cannot be opened because it does not exist");
/*     */     }
/* 174 */     return is;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URL getURL() throws IOException {
/* 185 */     URL url = resolveURL();
/* 186 */     if (url == null) {
/* 187 */       throw new FileNotFoundException(getDescription() + " cannot be resolved to URL because it does not exist");
/*     */     }
/* 189 */     return url;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Resource createRelative(String relativePath) {
/* 199 */     String pathToUse = StringUtils.applyRelativePath(this.path, relativePath);
/* 200 */     return (this.clazz != null) ? new ClassPathResource(pathToUse, this.clazz) : new ClassPathResource(pathToUse, this.classLoader);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFilename() {
/* 211 */     return StringUtils.getFilename(this.path);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDescription() {
/* 219 */     StringBuilder builder = new StringBuilder("class path resource [");
/* 220 */     String pathToUse = this.path;
/* 221 */     if (this.clazz != null && !pathToUse.startsWith("/")) {
/* 222 */       builder.append(ClassUtils.classPackageAsResourcePath(this.clazz));
/* 223 */       builder.append('/');
/*     */     } 
/* 225 */     if (pathToUse.startsWith("/")) {
/* 226 */       pathToUse = pathToUse.substring(1);
/*     */     }
/* 228 */     builder.append(pathToUse);
/* 229 */     builder.append(']');
/* 230 */     return builder.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 239 */     if (obj == this) {
/* 240 */       return true;
/*     */     }
/* 242 */     if (obj instanceof ClassPathResource) {
/* 243 */       ClassPathResource otherRes = (ClassPathResource)obj;
/* 244 */       return (this.path.equals(otherRes.path) && 
/* 245 */         ObjectUtils.nullSafeEquals(this.classLoader, otherRes.classLoader) && 
/* 246 */         ObjectUtils.nullSafeEquals(this.clazz, otherRes.clazz));
/*     */     } 
/* 248 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 257 */     return this.path.hashCode();
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\io\ClassPathResource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */