/*     */ package org.springframework.beans.propertyeditors;
/*     */ 
/*     */ import java.beans.PropertyEditorSupport;
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import org.springframework.core.io.ClassPathResource;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class URIEditor
/*     */   extends PropertyEditorSupport
/*     */ {
/*     */   private final ClassLoader classLoader;
/*     */   private final boolean encode;
/*     */   
/*     */   public URIEditor() {
/*  63 */     this(true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URIEditor(boolean encode) {
/*  73 */     this.classLoader = null;
/*  74 */     this.encode = encode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URIEditor(ClassLoader classLoader) {
/*  84 */     this(classLoader, true);
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
/*     */   public URIEditor(ClassLoader classLoader, boolean encode) {
/*  96 */     this.classLoader = (classLoader != null) ? classLoader : ClassUtils.getDefaultClassLoader();
/*  97 */     this.encode = encode;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAsText(String text) throws IllegalArgumentException {
/* 103 */     if (StringUtils.hasText(text)) {
/* 104 */       String uri = text.trim();
/* 105 */       if (this.classLoader != null && uri.startsWith("classpath:")) {
/*     */         
/* 107 */         ClassPathResource resource = new ClassPathResource(uri.substring("classpath:".length()), this.classLoader);
/*     */         try {
/* 109 */           setValue(resource.getURI());
/*     */         }
/* 111 */         catch (IOException ex) {
/* 112 */           throw new IllegalArgumentException("Could not retrieve URI for " + resource + ": " + ex.getMessage());
/*     */         } 
/*     */       } else {
/*     */         
/*     */         try {
/* 117 */           setValue(createURI(uri));
/*     */         }
/* 119 */         catch (URISyntaxException ex) {
/* 120 */           throw new IllegalArgumentException("Invalid URI syntax: " + ex);
/*     */         } 
/*     */       } 
/*     */     } else {
/*     */       
/* 125 */       setValue(null);
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
/*     */   protected URI createURI(String value) throws URISyntaxException {
/* 137 */     int colonIndex = value.indexOf(':');
/* 138 */     if (this.encode && colonIndex != -1) {
/* 139 */       int fragmentIndex = value.indexOf('#', colonIndex + 1);
/* 140 */       String scheme = value.substring(0, colonIndex);
/* 141 */       String ssp = value.substring(colonIndex + 1, (fragmentIndex > 0) ? fragmentIndex : value.length());
/* 142 */       String fragment = (fragmentIndex > 0) ? value.substring(fragmentIndex + 1) : null;
/* 143 */       return new URI(scheme, ssp, fragment);
/*     */     } 
/*     */ 
/*     */     
/* 147 */     return new URI(value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getAsText() {
/* 154 */     URI value = (URI)getValue();
/* 155 */     return (value != null) ? value.toString() : "";
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\propertyeditors\URIEditor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */