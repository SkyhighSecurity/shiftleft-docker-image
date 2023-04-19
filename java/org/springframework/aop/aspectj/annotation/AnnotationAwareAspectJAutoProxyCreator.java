/*     */ package org.springframework.aop.aspectj.annotation;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.regex.Pattern;
/*     */ import org.springframework.aop.Advisor;
/*     */ import org.springframework.aop.aspectj.autoproxy.AspectJAwareAdvisorAutoProxyCreator;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.ListableBeanFactory;
/*     */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
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
/*     */ public class AnnotationAwareAspectJAutoProxyCreator
/*     */   extends AspectJAwareAdvisorAutoProxyCreator
/*     */ {
/*     */   private List<Pattern> includePatterns;
/*     */   private AspectJAdvisorFactory aspectJAdvisorFactory;
/*     */   private BeanFactoryAspectJAdvisorsBuilder aspectJAdvisorsBuilder;
/*     */   
/*     */   public void setIncludePatterns(List<String> patterns) {
/*  63 */     this.includePatterns = new ArrayList<Pattern>(patterns.size());
/*  64 */     for (String patternText : patterns) {
/*  65 */       this.includePatterns.add(Pattern.compile(patternText));
/*     */     }
/*     */   }
/*     */   
/*     */   public void setAspectJAdvisorFactory(AspectJAdvisorFactory aspectJAdvisorFactory) {
/*  70 */     Assert.notNull(aspectJAdvisorFactory, "AspectJAdvisorFactory must not be null");
/*  71 */     this.aspectJAdvisorFactory = aspectJAdvisorFactory;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void initBeanFactory(ConfigurableListableBeanFactory beanFactory) {
/*  76 */     super.initBeanFactory(beanFactory);
/*  77 */     if (this.aspectJAdvisorFactory == null) {
/*  78 */       this.aspectJAdvisorFactory = new ReflectiveAspectJAdvisorFactory((BeanFactory)beanFactory);
/*     */     }
/*  80 */     this.aspectJAdvisorsBuilder = new BeanFactoryAspectJAdvisorsBuilderAdapter((ListableBeanFactory)beanFactory, this.aspectJAdvisorFactory);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected List<Advisor> findCandidateAdvisors() {
/*  88 */     List<Advisor> advisors = super.findCandidateAdvisors();
/*     */     
/*  90 */     advisors.addAll(this.aspectJAdvisorsBuilder.buildAspectJAdvisors());
/*  91 */     return advisors;
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
/*     */   protected boolean isInfrastructureClass(Class<?> beanClass) {
/* 104 */     return (super.isInfrastructureClass(beanClass) || this.aspectJAdvisorFactory.isAspect(beanClass));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isEligibleAspectBean(String beanName) {
/* 114 */     if (this.includePatterns == null) {
/* 115 */       return true;
/*     */     }
/*     */     
/* 118 */     for (Pattern pattern : this.includePatterns) {
/* 119 */       if (pattern.matcher(beanName).matches()) {
/* 120 */         return true;
/*     */       }
/*     */     } 
/* 123 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class BeanFactoryAspectJAdvisorsBuilderAdapter
/*     */     extends BeanFactoryAspectJAdvisorsBuilder
/*     */   {
/*     */     public BeanFactoryAspectJAdvisorsBuilderAdapter(ListableBeanFactory beanFactory, AspectJAdvisorFactory advisorFactory) {
/* 137 */       super(beanFactory, advisorFactory);
/*     */     }
/*     */ 
/*     */     
/*     */     protected boolean isEligibleBean(String beanName) {
/* 142 */       return AnnotationAwareAspectJAutoProxyCreator.this.isEligibleAspectBean(beanName);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\aspectj\annotation\AnnotationAwareAspectJAutoProxyCreator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */