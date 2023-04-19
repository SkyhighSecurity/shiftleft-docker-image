/*     */ package org.springframework.cache.concurrent;
/*     */ 
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import org.springframework.beans.factory.BeanNameAware;
/*     */ import org.springframework.beans.factory.FactoryBean;
/*     */ import org.springframework.beans.factory.InitializingBean;
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
/*     */ public class ConcurrentMapCacheFactoryBean
/*     */   implements FactoryBean<ConcurrentMapCache>, BeanNameAware, InitializingBean
/*     */ {
/*  42 */   private String name = "";
/*     */ 
/*     */   
/*     */   private ConcurrentMap<Object, Object> store;
/*     */ 
/*     */   
/*     */   private boolean allowNullValues = true;
/*     */ 
/*     */   
/*     */   private ConcurrentMapCache cache;
/*     */ 
/*     */ 
/*     */   
/*     */   public void setName(String name) {
/*  56 */     this.name = name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStore(ConcurrentMap<Object, Object> store) {
/*  65 */     this.store = store;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAllowNullValues(boolean allowNullValues) {
/*  74 */     this.allowNullValues = allowNullValues;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBeanName(String beanName) {
/*  79 */     if (!StringUtils.hasLength(this.name)) {
/*  80 */       setName(beanName);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() {
/*  86 */     this.cache = (this.store != null) ? new ConcurrentMapCache(this.name, this.store, this.allowNullValues) : new ConcurrentMapCache(this.name, this.allowNullValues);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConcurrentMapCache getObject() {
/*  93 */     return this.cache;
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?> getObjectType() {
/*  98 */     return ConcurrentMapCache.class;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSingleton() {
/* 103 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\cache\concurrent\ConcurrentMapCacheFactoryBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */