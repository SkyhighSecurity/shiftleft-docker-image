/*     */ package org.springframework.ejb.interceptor;
/*     */ 
/*     */ import java.util.Map;
/*     */ import java.util.WeakHashMap;
/*     */ import javax.annotation.PostConstruct;
/*     */ import javax.annotation.PreDestroy;
/*     */ import javax.ejb.EJBException;
/*     */ import javax.ejb.PostActivate;
/*     */ import javax.ejb.PrePassivate;
/*     */ import javax.interceptor.InvocationContext;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.access.BeanFactoryLocator;
/*     */ import org.springframework.beans.factory.access.BeanFactoryReference;
/*     */ import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
/*     */ import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.context.access.ContextSingletonBeanFactoryLocator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SpringBeanAutowiringInterceptor
/*     */ {
/*  84 */   private final Map<Object, BeanFactoryReference> beanFactoryReferences = new WeakHashMap<Object, BeanFactoryReference>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PostConstruct
/*     */   @PostActivate
/*     */   public void autowireBean(InvocationContext invocationContext) {
/*  95 */     doAutowireBean(invocationContext.getTarget());
/*     */     try {
/*  97 */       invocationContext.proceed();
/*     */     }
/*  99 */     catch (RuntimeException ex) {
/* 100 */       doReleaseBean(invocationContext.getTarget());
/* 101 */       throw ex;
/*     */     }
/* 103 */     catch (Error err) {
/* 104 */       doReleaseBean(invocationContext.getTarget());
/* 105 */       throw err;
/*     */     }
/* 107 */     catch (Exception ex) {
/* 108 */       doReleaseBean(invocationContext.getTarget());
/*     */       
/* 110 */       throw new EJBException(ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doAutowireBean(Object target) {
/* 119 */     AutowiredAnnotationBeanPostProcessor bpp = new AutowiredAnnotationBeanPostProcessor();
/* 120 */     configureBeanPostProcessor(bpp, target);
/* 121 */     bpp.setBeanFactory(getBeanFactory(target));
/* 122 */     bpp.processInjection(target);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void configureBeanPostProcessor(AutowiredAnnotationBeanPostProcessor processor, Object target) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected BeanFactory getBeanFactory(Object target) {
/*     */     AutowireCapableBeanFactory autowireCapableBeanFactory;
/* 141 */     BeanFactory factory = getBeanFactoryReference(target).getFactory();
/* 142 */     if (factory instanceof ApplicationContext) {
/* 143 */       autowireCapableBeanFactory = ((ApplicationContext)factory).getAutowireCapableBeanFactory();
/*     */     }
/* 145 */     return (BeanFactory)autowireCapableBeanFactory;
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
/*     */   protected BeanFactoryReference getBeanFactoryReference(Object target) {
/* 159 */     String key = getBeanFactoryLocatorKey(target);
/* 160 */     BeanFactoryReference ref = getBeanFactoryLocator(target).useBeanFactory(key);
/* 161 */     this.beanFactoryReferences.put(target, ref);
/* 162 */     return ref;
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
/*     */   protected BeanFactoryLocator getBeanFactoryLocator(Object target) {
/* 174 */     return ContextSingletonBeanFactoryLocator.getInstance();
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
/*     */   protected String getBeanFactoryLocatorKey(Object target) {
/* 189 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PreDestroy
/*     */   @PrePassivate
/*     */   public void releaseBean(InvocationContext invocationContext) {
/* 200 */     doReleaseBean(invocationContext.getTarget());
/*     */     try {
/* 202 */       invocationContext.proceed();
/*     */     }
/* 204 */     catch (RuntimeException ex) {
/* 205 */       throw ex;
/*     */     }
/* 207 */     catch (Exception ex) {
/*     */       
/* 209 */       throw new EJBException(ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doReleaseBean(Object target) {
/* 218 */     BeanFactoryReference ref = this.beanFactoryReferences.remove(target);
/* 219 */     if (ref != null)
/* 220 */       ref.release(); 
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\ejb\interceptor\SpringBeanAutowiringInterceptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */