/*     */ package org.springframework.core.io.support;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.Reader;
/*     */ import java.nio.charset.Charset;
/*     */ import org.springframework.core.io.InputStreamSource;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EncodedResource
/*     */   implements InputStreamSource
/*     */ {
/*     */   private final Resource resource;
/*     */   private final String encoding;
/*     */   private final Charset charset;
/*     */   
/*     */   public EncodedResource(Resource resource) {
/*  59 */     this(resource, null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EncodedResource(Resource resource, String encoding) {
/*  69 */     this(resource, encoding, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EncodedResource(Resource resource, Charset charset) {
/*  79 */     this(resource, null, charset);
/*     */   }
/*     */ 
/*     */   
/*     */   private EncodedResource(Resource resource, String encoding, Charset charset) {
/*  84 */     Assert.notNull(resource, "Resource must not be null");
/*  85 */     this.resource = resource;
/*  86 */     this.encoding = encoding;
/*  87 */     this.charset = charset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Resource getResource() {
/*  95 */     return this.resource;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getEncoding() {
/* 103 */     return this.encoding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Charset getCharset() {
/* 111 */     return this.charset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean requiresReader() {
/* 122 */     return (this.encoding != null || this.charset != null);
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
/*     */   public Reader getReader() throws IOException {
/* 134 */     if (this.charset != null) {
/* 135 */       return new InputStreamReader(this.resource.getInputStream(), this.charset);
/*     */     }
/* 137 */     if (this.encoding != null) {
/* 138 */       return new InputStreamReader(this.resource.getInputStream(), this.encoding);
/*     */     }
/*     */     
/* 141 */     return new InputStreamReader(this.resource.getInputStream());
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
/* 154 */     return this.resource.getInputStream();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 160 */     if (this == other) {
/* 161 */       return true;
/*     */     }
/* 163 */     if (!(other instanceof EncodedResource)) {
/* 164 */       return false;
/*     */     }
/* 166 */     EncodedResource otherResource = (EncodedResource)other;
/* 167 */     return (this.resource.equals(otherResource.resource) && 
/* 168 */       ObjectUtils.nullSafeEquals(this.charset, otherResource.charset) && 
/* 169 */       ObjectUtils.nullSafeEquals(this.encoding, otherResource.encoding));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 174 */     return this.resource.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 179 */     return this.resource.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\io\support\EncodedResource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */