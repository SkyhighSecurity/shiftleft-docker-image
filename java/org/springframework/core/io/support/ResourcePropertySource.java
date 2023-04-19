/*     */ package org.springframework.core.io.support;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Map;
/*     */ import org.springframework.core.env.PropertiesPropertySource;
/*     */ import org.springframework.core.io.DefaultResourceLoader;
/*     */ import org.springframework.core.io.Resource;
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
/*     */ public class ResourcePropertySource
/*     */   extends PropertiesPropertySource
/*     */ {
/*     */   private final String resourceName;
/*     */   
/*     */   public ResourcePropertySource(String name, EncodedResource resource) throws IOException {
/*  55 */     super(name, PropertiesLoaderUtils.loadProperties(resource));
/*  56 */     this.resourceName = getNameForResource(resource.getResource());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResourcePropertySource(EncodedResource resource) throws IOException {
/*  65 */     super(getNameForResource(resource.getResource()), PropertiesLoaderUtils.loadProperties(resource));
/*  66 */     this.resourceName = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResourcePropertySource(String name, Resource resource) throws IOException {
/*  74 */     super(name, PropertiesLoaderUtils.loadProperties(new EncodedResource(resource)));
/*  75 */     this.resourceName = getNameForResource(resource);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResourcePropertySource(Resource resource) throws IOException {
/*  84 */     super(getNameForResource(resource), PropertiesLoaderUtils.loadProperties(new EncodedResource(resource)));
/*  85 */     this.resourceName = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResourcePropertySource(String name, String location, ClassLoader classLoader) throws IOException {
/*  94 */     this(name, (new DefaultResourceLoader(classLoader)).getResource(location));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResourcePropertySource(String location, ClassLoader classLoader) throws IOException {
/* 105 */     this((new DefaultResourceLoader(classLoader)).getResource(location));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResourcePropertySource(String name, String location) throws IOException {
/* 115 */     this(name, (new DefaultResourceLoader()).getResource(location));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResourcePropertySource(String location) throws IOException {
/* 124 */     this((new DefaultResourceLoader()).getResource(location));
/*     */   }
/*     */   
/*     */   private ResourcePropertySource(String name, String resourceName, Map<String, Object> source) {
/* 128 */     super(name, source);
/* 129 */     this.resourceName = resourceName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResourcePropertySource withName(String name) {
/* 139 */     if (this.name.equals(name)) {
/* 140 */       return this;
/*     */     }
/*     */     
/* 143 */     if (this.resourceName != null) {
/* 144 */       if (this.resourceName.equals(name)) {
/* 145 */         return new ResourcePropertySource(this.resourceName, null, (Map<String, Object>)this.source);
/*     */       }
/*     */       
/* 148 */       return new ResourcePropertySource(name, this.resourceName, (Map<String, Object>)this.source);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 153 */     return new ResourcePropertySource(name, this.name, (Map<String, Object>)this.source);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResourcePropertySource withResourceName() {
/* 164 */     if (this.resourceName == null) {
/* 165 */       return this;
/*     */     }
/* 167 */     return new ResourcePropertySource(this.resourceName, null, (Map<String, Object>)this.source);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String getNameForResource(Resource resource) {
/* 177 */     String name = resource.getDescription();
/* 178 */     if (!StringUtils.hasText(name)) {
/* 179 */       name = resource.getClass().getSimpleName() + "@" + System.identityHashCode(resource);
/*     */     }
/* 181 */     return name;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\io\support\ResourcePropertySource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */