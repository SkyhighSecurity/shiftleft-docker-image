/*     */ package org.springframework.scheduling.annotation;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.util.concurrent.Executor;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.aop.Advisor;
/*     */ import org.springframework.aop.framework.autoproxy.AbstractBeanFactoryAwareAdvisingPostProcessor;
/*     */ import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
/*     */ import org.springframework.beans.factory.BeanFactory;
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
/*     */ 
/*     */ 
/*     */ public class AsyncAnnotationBeanPostProcessor
/*     */   extends AbstractBeanFactoryAwareAdvisingPostProcessor
/*     */ {
/*     */   public static final String DEFAULT_TASK_EXECUTOR_BEAN_NAME = "taskExecutor";
/*  75 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*     */   private Class<? extends Annotation> asyncAnnotationType;
/*     */   
/*     */   private Executor executor;
/*     */   
/*     */   private AsyncUncaughtExceptionHandler exceptionHandler;
/*     */ 
/*     */   
/*     */   public AsyncAnnotationBeanPostProcessor() {
/*  85 */     setBeforeExistingAdvisors(true);
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
/*     */   public void setAsyncAnnotationType(Class<? extends Annotation> asyncAnnotationType) {
/*  99 */     Assert.notNull(asyncAnnotationType, "'asyncAnnotationType' must not be null");
/* 100 */     this.asyncAnnotationType = asyncAnnotationType;
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
/*     */   public void setExecutor(Executor executor) {
/* 114 */     this.executor = executor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExceptionHandler(AsyncUncaughtExceptionHandler exceptionHandler) {
/* 123 */     this.exceptionHandler = exceptionHandler;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBeanFactory(BeanFactory beanFactory) {
/* 129 */     super.setBeanFactory(beanFactory);
/*     */     
/* 131 */     AsyncAnnotationAdvisor advisor = new AsyncAnnotationAdvisor(this.executor, this.exceptionHandler);
/* 132 */     if (this.asyncAnnotationType != null) {
/* 133 */       advisor.setAsyncAnnotationType(this.asyncAnnotationType);
/*     */     }
/* 135 */     advisor.setBeanFactory(beanFactory);
/* 136 */     this.advisor = (Advisor)advisor;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\scheduling\annotation\AsyncAnnotationBeanPostProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */