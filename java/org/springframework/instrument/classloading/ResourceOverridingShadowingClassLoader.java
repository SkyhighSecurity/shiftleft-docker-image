/*     */ package org.springframework.instrument.classloading;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URL;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
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
/*     */ public class ResourceOverridingShadowingClassLoader
/*     */   extends ShadowingClassLoader
/*     */ {
/*  38 */   private static final Enumeration<URL> EMPTY_URL_ENUMERATION = new Enumeration<URL>()
/*     */     {
/*     */       public boolean hasMoreElements() {
/*  41 */         return false;
/*     */       }
/*     */       
/*     */       public URL nextElement() {
/*  45 */         throw new UnsupportedOperationException("Should not be called. I am empty.");
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  53 */   private Map<String, String> overrides = new HashMap<String, String>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResourceOverridingShadowingClassLoader(ClassLoader enclosingClassLoader) {
/*  62 */     super(enclosingClassLoader);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void override(String oldPath, String newPath) {
/*  73 */     this.overrides.put(oldPath, newPath);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void suppress(String oldPath) {
/*  82 */     this.overrides.put(oldPath, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void copyOverrides(ResourceOverridingShadowingClassLoader other) {
/*  90 */     Assert.notNull(other, "Other ClassLoader must not be null");
/*  91 */     this.overrides.putAll(other.overrides);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public URL getResource(String requestedPath) {
/*  97 */     if (this.overrides.containsKey(requestedPath)) {
/*  98 */       String overriddenPath = this.overrides.get(requestedPath);
/*  99 */       return (overriddenPath != null) ? super.getResource(overriddenPath) : null;
/*     */     } 
/*     */     
/* 102 */     return super.getResource(requestedPath);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public InputStream getResourceAsStream(String requestedPath) {
/* 108 */     if (this.overrides.containsKey(requestedPath)) {
/* 109 */       String overriddenPath = this.overrides.get(requestedPath);
/* 110 */       return (overriddenPath != null) ? super.getResourceAsStream(overriddenPath) : null;
/*     */     } 
/*     */     
/* 113 */     return super.getResourceAsStream(requestedPath);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Enumeration<URL> getResources(String requestedPath) throws IOException {
/* 119 */     if (this.overrides.containsKey(requestedPath)) {
/* 120 */       String overriddenLocation = this.overrides.get(requestedPath);
/* 121 */       return (overriddenLocation != null) ? super
/* 122 */         .getResources(overriddenLocation) : EMPTY_URL_ENUMERATION;
/*     */     } 
/*     */     
/* 125 */     return super.getResources(requestedPath);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\instrument\classloading\ResourceOverridingShadowingClassLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */