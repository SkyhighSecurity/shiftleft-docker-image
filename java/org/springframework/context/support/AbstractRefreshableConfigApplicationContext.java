/*     */ package org.springframework.context.support;
/*     */ 
/*     */ import org.springframework.beans.factory.BeanNameAware;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.util.Assert;
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
/*     */ public abstract class AbstractRefreshableConfigApplicationContext
/*     */   extends AbstractRefreshableApplicationContext
/*     */   implements BeanNameAware, InitializingBean
/*     */ {
/*     */   private String[] configLocations;
/*     */   private boolean setIdCalled = false;
/*     */   
/*     */   public AbstractRefreshableConfigApplicationContext() {}
/*     */   
/*     */   public AbstractRefreshableConfigApplicationContext(ApplicationContext parent) {
/*  58 */     super(parent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setConfigLocation(String location) {
/*  68 */     setConfigLocations(StringUtils.tokenizeToStringArray(location, ",; \t\n"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setConfigLocations(String... locations) {
/*  76 */     if (locations != null) {
/*  77 */       Assert.noNullElements((Object[])locations, "Config locations must not be null");
/*  78 */       this.configLocations = new String[locations.length];
/*  79 */       for (int i = 0; i < locations.length; i++) {
/*  80 */         this.configLocations[i] = resolvePath(locations[i]).trim();
/*     */       }
/*     */     } else {
/*     */       
/*  84 */       this.configLocations = null;
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
/*     */   protected String[] getConfigLocations() {
/*  99 */     return (this.configLocations != null) ? this.configLocations : getDefaultConfigLocations();
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
/*     */   protected String[] getDefaultConfigLocations() {
/* 111 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String resolvePath(String path) {
/* 122 */     return getEnvironment().resolveRequiredPlaceholders(path);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setId(String id) {
/* 128 */     super.setId(id);
/* 129 */     this.setIdCalled = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBeanName(String name) {
/* 138 */     if (!this.setIdCalled) {
/* 139 */       super.setId(name);
/* 140 */       setDisplayName("ApplicationContext '" + name + "'");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() {
/* 150 */     if (!isActive())
/* 151 */       refresh(); 
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\support\AbstractRefreshableConfigApplicationContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */