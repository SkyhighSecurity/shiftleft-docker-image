/*     */ package org.springframework.aop.framework.autoproxy;
/*     */ 
/*     */ import java.util.List;
/*     */ import org.springframework.aop.Advisor;
/*     */ import org.springframework.aop.TargetSource;
/*     */ import org.springframework.aop.support.AopUtils;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*     */ import org.springframework.core.annotation.AnnotationAwareOrderComparator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractAdvisorAutoProxyCreator
/*     */   extends AbstractAutoProxyCreator
/*     */ {
/*     */   private BeanFactoryAdvisorRetrievalHelper advisorRetrievalHelper;
/*     */   
/*     */   public void setBeanFactory(BeanFactory beanFactory) {
/*  55 */     super.setBeanFactory(beanFactory);
/*  56 */     if (!(beanFactory instanceof ConfigurableListableBeanFactory)) {
/*  57 */       throw new IllegalArgumentException("AdvisorAutoProxyCreator requires a ConfigurableListableBeanFactory: " + beanFactory);
/*     */     }
/*     */     
/*  60 */     initBeanFactory((ConfigurableListableBeanFactory)beanFactory);
/*     */   }
/*     */   
/*     */   protected void initBeanFactory(ConfigurableListableBeanFactory beanFactory) {
/*  64 */     this.advisorRetrievalHelper = new BeanFactoryAdvisorRetrievalHelperAdapter(beanFactory);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object[] getAdvicesAndAdvisorsForBean(Class<?> beanClass, String beanName, TargetSource targetSource) {
/*  70 */     List<Advisor> advisors = findEligibleAdvisors(beanClass, beanName);
/*  71 */     if (advisors.isEmpty()) {
/*  72 */       return DO_NOT_PROXY;
/*     */     }
/*  74 */     return advisors.toArray();
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
/*     */   protected List<Advisor> findEligibleAdvisors(Class<?> beanClass, String beanName) {
/*  88 */     List<Advisor> candidateAdvisors = findCandidateAdvisors();
/*  89 */     List<Advisor> eligibleAdvisors = findAdvisorsThatCanApply(candidateAdvisors, beanClass, beanName);
/*  90 */     extendAdvisors(eligibleAdvisors);
/*  91 */     if (!eligibleAdvisors.isEmpty()) {
/*  92 */       eligibleAdvisors = sortAdvisors(eligibleAdvisors);
/*     */     }
/*  94 */     return eligibleAdvisors;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected List<Advisor> findCandidateAdvisors() {
/* 102 */     return this.advisorRetrievalHelper.findAdvisorBeans();
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
/*     */   protected List<Advisor> findAdvisorsThatCanApply(List<Advisor> candidateAdvisors, Class<?> beanClass, String beanName) {
/* 117 */     ProxyCreationContext.setCurrentProxiedBeanName(beanName);
/*     */     try {
/* 119 */       return AopUtils.findAdvisorsThatCanApply(candidateAdvisors, beanClass);
/*     */     } finally {
/*     */       
/* 122 */       ProxyCreationContext.setCurrentProxiedBeanName(null);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isEligibleAdvisorBean(String beanName) {
/* 133 */     return true;
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
/*     */   protected List<Advisor> sortAdvisors(List<Advisor> advisors) {
/* 146 */     AnnotationAwareOrderComparator.sort(advisors);
/* 147 */     return advisors;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void extendAdvisors(List<Advisor> candidateAdvisors) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean advisorsPreFiltered() {
/* 167 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class BeanFactoryAdvisorRetrievalHelperAdapter
/*     */     extends BeanFactoryAdvisorRetrievalHelper
/*     */   {
/*     */     public BeanFactoryAdvisorRetrievalHelperAdapter(ConfigurableListableBeanFactory beanFactory) {
/* 178 */       super(beanFactory);
/*     */     }
/*     */ 
/*     */     
/*     */     protected boolean isEligibleBean(String beanName) {
/* 183 */       return AbstractAdvisorAutoProxyCreator.this.isEligibleAdvisorBean(beanName);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\framework\autoproxy\AbstractAdvisorAutoProxyCreator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */