/*     */ package org.springframework.core.io;
/*     */ 
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.util.Collection;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
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
/*     */ public class DefaultResourceLoader
/*     */   implements ResourceLoader
/*     */ {
/*     */   private ClassLoader classLoader;
/*  48 */   private final Set<ProtocolResolver> protocolResolvers = new LinkedHashSet<ProtocolResolver>(4);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultResourceLoader() {
/*  58 */     this.classLoader = ClassUtils.getDefaultClassLoader();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultResourceLoader(ClassLoader classLoader) {
/*  67 */     this.classLoader = classLoader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClassLoader(ClassLoader classLoader) {
/*  78 */     this.classLoader = classLoader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassLoader getClassLoader() {
/*  89 */     return (this.classLoader != null) ? this.classLoader : ClassUtils.getDefaultClassLoader();
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
/*     */   public void addProtocolResolver(ProtocolResolver resolver) {
/* 101 */     Assert.notNull(resolver, "ProtocolResolver must not be null");
/* 102 */     this.protocolResolvers.add(resolver);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<ProtocolResolver> getProtocolResolvers() {
/* 111 */     return this.protocolResolvers;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Resource getResource(String location) {
/* 117 */     Assert.notNull(location, "Location must not be null");
/*     */     
/* 119 */     for (ProtocolResolver protocolResolver : this.protocolResolvers) {
/* 120 */       Resource resource = protocolResolver.resolve(location, this);
/* 121 */       if (resource != null) {
/* 122 */         return resource;
/*     */       }
/*     */     } 
/*     */     
/* 126 */     if (location.startsWith("/")) {
/* 127 */       return getResourceByPath(location);
/*     */     }
/* 129 */     if (location.startsWith("classpath:")) {
/* 130 */       return new ClassPathResource(location.substring("classpath:".length()), getClassLoader());
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/* 135 */       URL url = new URL(location);
/* 136 */       return new UrlResource(url);
/*     */     }
/* 138 */     catch (MalformedURLException ex) {
/*     */       
/* 140 */       return getResourceByPath(location);
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
/*     */   protected Resource getResourceByPath(String path) {
/* 157 */     return new ClassPathContextResource(path, getClassLoader());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static class ClassPathContextResource
/*     */     extends ClassPathResource
/*     */     implements ContextResource
/*     */   {
/*     */     public ClassPathContextResource(String path, ClassLoader classLoader) {
/* 168 */       super(path, classLoader);
/*     */     }
/*     */ 
/*     */     
/*     */     public String getPathWithinContext() {
/* 173 */       return getPath();
/*     */     }
/*     */ 
/*     */     
/*     */     public Resource createRelative(String relativePath) {
/* 178 */       String pathToUse = StringUtils.applyRelativePath(getPath(), relativePath);
/* 179 */       return new ClassPathContextResource(pathToUse, getClassLoader());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\io\DefaultResourceLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */