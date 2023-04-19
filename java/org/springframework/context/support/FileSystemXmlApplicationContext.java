/*     */ package org.springframework.context.support;
/*     */ 
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.core.io.FileSystemResource;
/*     */ import org.springframework.core.io.Resource;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FileSystemXmlApplicationContext
/*     */   extends AbstractXmlApplicationContext
/*     */ {
/*     */   public FileSystemXmlApplicationContext() {}
/*     */   
/*     */   public FileSystemXmlApplicationContext(ApplicationContext parent) {
/*  74 */     super(parent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FileSystemXmlApplicationContext(String configLocation) throws BeansException {
/*  84 */     this(new String[] { configLocation }, true, (ApplicationContext)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FileSystemXmlApplicationContext(String... configLocations) throws BeansException {
/*  94 */     this(configLocations, true, (ApplicationContext)null);
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
/*     */   public FileSystemXmlApplicationContext(String[] configLocations, ApplicationContext parent) throws BeansException {
/* 106 */     this(configLocations, true, parent);
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
/*     */   public FileSystemXmlApplicationContext(String[] configLocations, boolean refresh) throws BeansException {
/* 120 */     this(configLocations, refresh, (ApplicationContext)null);
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
/*     */   
/*     */   public FileSystemXmlApplicationContext(String[] configLocations, boolean refresh, ApplicationContext parent) throws BeansException {
/* 137 */     super(parent);
/* 138 */     setConfigLocations(configLocations);
/* 139 */     if (refresh) {
/* 140 */       refresh();
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
/*     */   protected Resource getResourceByPath(String path) {
/* 156 */     if (path != null && path.startsWith("/")) {
/* 157 */       path = path.substring(1);
/*     */     }
/* 159 */     return (Resource)new FileSystemResource(path);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\support\FileSystemXmlApplicationContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */