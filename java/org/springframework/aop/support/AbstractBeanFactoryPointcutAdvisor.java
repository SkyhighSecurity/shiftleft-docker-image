/*     */ package org.springframework.aop.support;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import org.aopalliance.aop.Advice;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryAware;
/*     */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
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
/*     */ public abstract class AbstractBeanFactoryPointcutAdvisor
/*     */   extends AbstractPointcutAdvisor
/*     */   implements BeanFactoryAware
/*     */ {
/*     */   private String adviceBeanName;
/*     */   private BeanFactory beanFactory;
/*     */   private volatile transient Advice advice;
/*  51 */   private volatile transient Object adviceMonitor = new Object();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAdviceBeanName(String adviceBeanName) {
/*  63 */     this.adviceBeanName = adviceBeanName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getAdviceBeanName() {
/*  70 */     return this.adviceBeanName;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBeanFactory(BeanFactory beanFactory) {
/*  75 */     this.beanFactory = beanFactory;
/*  76 */     resetAdviceMonitor();
/*     */   }
/*     */   
/*     */   private void resetAdviceMonitor() {
/*  80 */     if (this.beanFactory instanceof ConfigurableBeanFactory) {
/*  81 */       this.adviceMonitor = ((ConfigurableBeanFactory)this.beanFactory).getSingletonMutex();
/*     */     } else {
/*     */       
/*  84 */       this.adviceMonitor = new Object();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAdvice(Advice advice) {
/*  94 */     synchronized (this.adviceMonitor) {
/*  95 */       this.advice = advice;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Advice getAdvice() {
/* 101 */     Advice advice = this.advice;
/* 102 */     if (advice != null || this.adviceBeanName == null) {
/* 103 */       return advice;
/*     */     }
/*     */     
/* 106 */     Assert.state((this.beanFactory != null), "BeanFactory must be set to resolve 'adviceBeanName'");
/* 107 */     if (this.beanFactory.isSingleton(this.adviceBeanName)) {
/*     */       
/* 109 */       advice = (Advice)this.beanFactory.getBean(this.adviceBeanName, Advice.class);
/* 110 */       this.advice = advice;
/* 111 */       return advice;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 117 */     synchronized (this.adviceMonitor) {
/* 118 */       if (this.advice == null) {
/* 119 */         this.advice = (Advice)this.beanFactory.getBean(this.adviceBeanName, Advice.class);
/*     */       }
/* 121 */       return this.advice;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 128 */     StringBuilder sb = new StringBuilder(getClass().getName());
/* 129 */     sb.append(": advice ");
/* 130 */     if (this.adviceBeanName != null) {
/* 131 */       sb.append("bean '").append(this.adviceBeanName).append("'");
/*     */     } else {
/*     */       
/* 134 */       sb.append(this.advice);
/*     */     } 
/* 136 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
/* 146 */     ois.defaultReadObject();
/*     */ 
/*     */     
/* 149 */     resetAdviceMonitor();
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\support\AbstractBeanFactoryPointcutAdvisor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */