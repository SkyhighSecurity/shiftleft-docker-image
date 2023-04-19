/*    */ package org.springframework.cache.interceptor;
/*    */ 
/*    */ import org.springframework.aop.ClassFilter;
/*    */ import org.springframework.aop.Pointcut;
/*    */ import org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor;
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
/*    */ public class BeanFactoryCacheOperationSourceAdvisor
/*    */   extends AbstractBeanFactoryPointcutAdvisor
/*    */ {
/*    */   private CacheOperationSource cacheOperationSource;
/*    */   
/* 35 */   private final CacheOperationSourcePointcut pointcut = new CacheOperationSourcePointcut()
/*    */     {
/*    */       protected CacheOperationSource getCacheOperationSource() {
/* 38 */         return BeanFactoryCacheOperationSourceAdvisor.this.cacheOperationSource;
/*    */       }
/*    */     };
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setCacheOperationSource(CacheOperationSource cacheOperationSource) {
/* 49 */     this.cacheOperationSource = cacheOperationSource;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setClassFilter(ClassFilter classFilter) {
/* 57 */     this.pointcut.setClassFilter(classFilter);
/*    */   }
/*    */ 
/*    */   
/*    */   public Pointcut getPointcut() {
/* 62 */     return (Pointcut)this.pointcut;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\cache\interceptor\BeanFactoryCacheOperationSourceAdvisor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */