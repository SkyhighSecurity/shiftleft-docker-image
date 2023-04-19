/*     */ package org.springframework.scripting.support;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.support.EncodedResource;
/*     */ import org.springframework.scripting.ScriptSource;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.FileCopyUtils;
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
/*     */ public class ResourceScriptSource
/*     */   implements ScriptSource
/*     */ {
/*  50 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*     */   private EncodedResource resource;
/*     */   
/*  54 */   private long lastModified = -1L;
/*     */   
/*  56 */   private final Object lastModifiedMonitor = new Object();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResourceScriptSource(EncodedResource resource) {
/*  64 */     Assert.notNull(resource, "Resource must not be null");
/*  65 */     this.resource = resource;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResourceScriptSource(Resource resource) {
/*  73 */     Assert.notNull(resource, "Resource must not be null");
/*  74 */     this.resource = new EncodedResource(resource, "UTF-8");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Resource getResource() {
/*  83 */     return this.resource.getResource();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEncoding(String encoding) {
/*  92 */     this.resource = new EncodedResource(this.resource.getResource(), encoding);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getScriptAsString() throws IOException {
/*  98 */     synchronized (this.lastModifiedMonitor) {
/*  99 */       this.lastModified = retrieveLastModifiedTime();
/*     */     } 
/* 101 */     Reader reader = this.resource.getReader();
/* 102 */     return FileCopyUtils.copyToString(reader);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isModified() {
/* 107 */     synchronized (this.lastModifiedMonitor) {
/* 108 */       return (this.lastModified < 0L || retrieveLastModifiedTime() > this.lastModified);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected long retrieveLastModifiedTime() {
/*     */     try {
/* 118 */       return getResource().lastModified();
/*     */     }
/* 120 */     catch (IOException ex) {
/* 121 */       if (this.logger.isDebugEnabled()) {
/* 122 */         this.logger.debug(getResource() + " could not be resolved in the file system - current timestamp not available for script modification check", ex);
/*     */       }
/*     */       
/* 125 */       return 0L;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String suggestedClassName() {
/* 131 */     return StringUtils.stripFilenameExtension(getResource().getFilename());
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 136 */     return this.resource.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\scripting\support\ResourceScriptSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */