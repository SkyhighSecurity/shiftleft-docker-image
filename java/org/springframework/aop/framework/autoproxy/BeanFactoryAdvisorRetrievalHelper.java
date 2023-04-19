/*     */ package org.springframework.aop.framework.autoproxy;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.aop.Advisor;
/*     */ import org.springframework.beans.factory.BeanCreationException;
/*     */ import org.springframework.beans.factory.BeanFactoryUtils;
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
/*     */ public class BeanFactoryAdvisorRetrievalHelper
/*     */ {
/*  42 */   private static final Log logger = LogFactory.getLog(BeanFactoryAdvisorRetrievalHelper.class);
/*     */ 
/*     */ 
/*     */   
/*     */   private final ConfigurableListableBeanFactory beanFactory;
/*     */ 
/*     */   
/*     */   private volatile String[] cachedAdvisorBeanNames;
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanFactoryAdvisorRetrievalHelper(ConfigurableListableBeanFactory beanFactory) {
/*  54 */     Assert.notNull(beanFactory, "ListableBeanFactory must not be null");
/*  55 */     this.beanFactory = beanFactory;
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
/*     */   public List<Advisor> findAdvisorBeans() {
/*  67 */     String[] advisorNames = this.cachedAdvisorBeanNames;
/*  68 */     if (advisorNames == null) {
/*     */ 
/*     */       
/*  71 */       advisorNames = BeanFactoryUtils.beanNamesForTypeIncludingAncestors((ListableBeanFactory)this.beanFactory, Advisor.class, true, false);
/*     */       
/*  73 */       this.cachedAdvisorBeanNames = advisorNames;
/*     */     } 
/*  75 */     if (advisorNames.length == 0) {
/*  76 */       return new ArrayList<Advisor>();
/*     */     }
/*     */     
/*  79 */     List<Advisor> advisors = new ArrayList<Advisor>();
/*  80 */     for (String name : advisorNames) {
/*  81 */       if (isEligibleBean(name))
/*  82 */         if (this.beanFactory.isCurrentlyInCreation(name)) {
/*  83 */           if (logger.isDebugEnabled()) {
/*  84 */             logger.debug("Skipping currently created advisor '" + name + "'");
/*     */           }
/*     */         } else {
/*     */           
/*     */           try {
/*  89 */             advisors.add(this.beanFactory.getBean(name, Advisor.class));
/*     */           }
/*  91 */           catch (BeanCreationException ex) {
/*  92 */             Throwable rootCause = ex.getMostSpecificCause();
/*  93 */             if (rootCause instanceof org.springframework.beans.factory.BeanCurrentlyInCreationException)
/*  94 */             { BeanCreationException bce = (BeanCreationException)rootCause;
/*  95 */               if (this.beanFactory.isCurrentlyInCreation(bce.getBeanName()))
/*  96 */               { if (logger.isDebugEnabled()) {
/*  97 */                   logger.debug("Skipping advisor '" + name + "' with dependency on currently created bean: " + ex
/*  98 */                       .getMessage());
/*     */                 
/*     */                 }
/*     */                  }
/*     */               
/*     */               else
/*     */               
/* 105 */               { throw ex; }  } else { throw ex; }
/*     */           
/*     */           } 
/*     */         }  
/*     */     } 
/* 110 */     return advisors;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isEligibleBean(String beanName) {
/* 120 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\framework\autoproxy\BeanFactoryAdvisorRetrievalHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */