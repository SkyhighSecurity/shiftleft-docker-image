/*    */ package org.springframework.cache.annotation;
/*    */ 
/*    */ import org.aopalliance.aop.Advice;
/*    */ import org.springframework.cache.interceptor.BeanFactoryCacheOperationSourceAdvisor;
/*    */ import org.springframework.cache.interceptor.CacheInterceptor;
/*    */ import org.springframework.cache.interceptor.CacheOperationSource;
/*    */ import org.springframework.context.annotation.Bean;
/*    */ import org.springframework.context.annotation.Configuration;
/*    */ import org.springframework.context.annotation.Role;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Configuration
/*    */ @Role(2)
/*    */ public class ProxyCachingConfiguration
/*    */   extends AbstractCachingConfiguration
/*    */ {
/*    */   @Bean(name = {"org.springframework.cache.config.internalCacheAdvisor"})
/*    */   @Role(2)
/*    */   public BeanFactoryCacheOperationSourceAdvisor cacheAdvisor() {
/* 45 */     BeanFactoryCacheOperationSourceAdvisor advisor = new BeanFactoryCacheOperationSourceAdvisor();
/* 46 */     advisor.setCacheOperationSource(cacheOperationSource());
/* 47 */     advisor.setAdvice((Advice)cacheInterceptor());
/* 48 */     advisor.setOrder(((Integer)this.enableCaching.getNumber("order")).intValue());
/* 49 */     return advisor;
/*    */   }
/*    */   
/*    */   @Bean
/*    */   @Role(2)
/*    */   public CacheOperationSource cacheOperationSource() {
/* 55 */     return (CacheOperationSource)new AnnotationCacheOperationSource();
/*    */   }
/*    */   
/*    */   @Bean
/*    */   @Role(2)
/*    */   public CacheInterceptor cacheInterceptor() {
/* 61 */     CacheInterceptor interceptor = new CacheInterceptor();
/* 62 */     interceptor.setCacheOperationSources(new CacheOperationSource[] { cacheOperationSource() });
/* 63 */     if (this.cacheResolver != null) {
/* 64 */       interceptor.setCacheResolver(this.cacheResolver);
/*    */     }
/* 66 */     else if (this.cacheManager != null) {
/* 67 */       interceptor.setCacheManager(this.cacheManager);
/*    */     } 
/* 69 */     if (this.keyGenerator != null) {
/* 70 */       interceptor.setKeyGenerator(this.keyGenerator);
/*    */     }
/* 72 */     if (this.errorHandler != null) {
/* 73 */       interceptor.setErrorHandler(this.errorHandler);
/*    */     }
/* 75 */     return interceptor;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\cache\annotation\ProxyCachingConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */