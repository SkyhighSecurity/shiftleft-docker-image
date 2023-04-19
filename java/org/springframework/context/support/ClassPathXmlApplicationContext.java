/*     */ package org.springframework.context.support;
/*     */ 
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.core.io.ClassPathResource;
/*     */ import org.springframework.core.io.Resource;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ClassPathXmlApplicationContext
/*     */   extends AbstractXmlApplicationContext
/*     */ {
/*     */   private Resource[] configResources;
/*     */   
/*     */   public ClassPathXmlApplicationContext() {}
/*     */   
/*     */   public ClassPathXmlApplicationContext(ApplicationContext parent) {
/*  73 */     super(parent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassPathXmlApplicationContext(String configLocation) throws BeansException {
/*  83 */     this(new String[] { configLocation }, true, (ApplicationContext)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassPathXmlApplicationContext(String... configLocations) throws BeansException {
/*  93 */     this(configLocations, true, (ApplicationContext)null);
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
/*     */   public ClassPathXmlApplicationContext(String[] configLocations, ApplicationContext parent) throws BeansException {
/* 105 */     this(configLocations, true, parent);
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
/*     */   public ClassPathXmlApplicationContext(String[] configLocations, boolean refresh) throws BeansException {
/* 119 */     this(configLocations, refresh, (ApplicationContext)null);
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
/*     */   public ClassPathXmlApplicationContext(String[] configLocations, boolean refresh, ApplicationContext parent) throws BeansException {
/* 136 */     super(parent);
/* 137 */     setConfigLocations(configLocations);
/* 138 */     if (refresh) {
/* 139 */       refresh();
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
/*     */ 
/*     */   
/*     */   public ClassPathXmlApplicationContext(String path, Class<?> clazz) throws BeansException {
/* 158 */     this(new String[] { path }, clazz);
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
/*     */   public ClassPathXmlApplicationContext(String[] paths, Class<?> clazz) throws BeansException {
/* 172 */     this(paths, clazz, (ApplicationContext)null);
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
/*     */   
/*     */   public ClassPathXmlApplicationContext(String[] paths, Class<?> clazz, ApplicationContext parent) throws BeansException {
/* 190 */     super(parent);
/* 191 */     Assert.notNull(paths, "Path array must not be null");
/* 192 */     Assert.notNull(clazz, "Class argument must not be null");
/* 193 */     this.configResources = new Resource[paths.length];
/* 194 */     for (int i = 0; i < paths.length; i++) {
/* 195 */       this.configResources[i] = (Resource)new ClassPathResource(paths[i], clazz);
/*     */     }
/* 197 */     refresh();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Resource[] getConfigResources() {
/* 203 */     return this.configResources;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\support\ClassPathXmlApplicationContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */