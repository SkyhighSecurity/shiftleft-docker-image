/*     */ package org.springframework.context.event;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.aop.framework.autoproxy.AutoProxyUtils;
/*     */ import org.springframework.aop.scope.ScopedObject;
/*     */ import org.springframework.aop.scope.ScopedProxyUtils;
/*     */ import org.springframework.aop.support.AopUtils;
/*     */ import org.springframework.beans.factory.BeanInitializationException;
/*     */ import org.springframework.beans.factory.SmartInitializingSingleton;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.context.ApplicationContextAware;
/*     */ import org.springframework.context.ApplicationListener;
/*     */ import org.springframework.context.ConfigurableApplicationContext;
/*     */ import org.springframework.core.MethodIntrospector;
/*     */ import org.springframework.core.annotation.AnnotatedElementUtils;
/*     */ import org.springframework.core.annotation.AnnotationAwareOrderComparator;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EventListenerMethodProcessor
/*     */   implements SmartInitializingSingleton, ApplicationContextAware
/*     */ {
/*  55 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*     */   private ConfigurableApplicationContext applicationContext;
/*     */   
/*  59 */   private final EventExpressionEvaluator evaluator = new EventExpressionEvaluator();
/*     */ 
/*     */   
/*  62 */   private final Set<Class<?>> nonAnnotatedClasses = Collections.newSetFromMap(new ConcurrentHashMap<Class<?>, Boolean>(64));
/*     */ 
/*     */ 
/*     */   
/*     */   public void setApplicationContext(ApplicationContext applicationContext) {
/*  67 */     Assert.isTrue(applicationContext instanceof ConfigurableApplicationContext, "ApplicationContext does not implement ConfigurableApplicationContext");
/*     */     
/*  69 */     this.applicationContext = (ConfigurableApplicationContext)applicationContext;
/*     */   }
/*     */ 
/*     */   
/*     */   public void afterSingletonsInstantiated() {
/*  74 */     List<EventListenerFactory> factories = getEventListenerFactories();
/*  75 */     String[] beanNames = this.applicationContext.getBeanNamesForType(Object.class);
/*  76 */     for (String beanName : beanNames) {
/*  77 */       if (!ScopedProxyUtils.isScopedTarget(beanName)) {
/*  78 */         Class<?> type = null;
/*     */         try {
/*  80 */           type = AutoProxyUtils.determineTargetClass(this.applicationContext.getBeanFactory(), beanName);
/*     */         }
/*  82 */         catch (Throwable ex) {
/*     */           
/*  84 */           if (this.logger.isDebugEnabled()) {
/*  85 */             this.logger.debug("Could not resolve target class for bean with name '" + beanName + "'", ex);
/*     */           }
/*     */         } 
/*  88 */         if (type != null) {
/*  89 */           if (ScopedObject.class.isAssignableFrom(type)) {
/*     */             try {
/*  91 */               type = AutoProxyUtils.determineTargetClass(this.applicationContext.getBeanFactory(), 
/*  92 */                   ScopedProxyUtils.getTargetBeanName(beanName));
/*     */             }
/*  94 */             catch (Throwable ex) {
/*     */               
/*  96 */               if (this.logger.isDebugEnabled()) {
/*  97 */                 this.logger.debug("Could not resolve target bean for scoped proxy '" + beanName + "'", ex);
/*     */               }
/*     */             } 
/*     */           }
/*     */           try {
/* 102 */             processBean(factories, beanName, type);
/*     */           }
/* 104 */           catch (Throwable ex) {
/* 105 */             throw new BeanInitializationException("Failed to process @EventListener annotation on bean with name '" + beanName + "'", ex);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected List<EventListenerFactory> getEventListenerFactories() {
/* 119 */     Map<String, EventListenerFactory> beans = this.applicationContext.getBeansOfType(EventListenerFactory.class);
/* 120 */     List<EventListenerFactory> factories = new ArrayList<EventListenerFactory>(beans.values());
/* 121 */     AnnotationAwareOrderComparator.sort(factories);
/* 122 */     return factories;
/*     */   }
/*     */   
/*     */   protected void processBean(List<EventListenerFactory> factories, String beanName, Class<?> targetType) {
/* 126 */     if (!this.nonAnnotatedClasses.contains(targetType)) {
/* 127 */       Map<Method, EventListener> annotatedMethods = null;
/*     */       try {
/* 129 */         annotatedMethods = MethodIntrospector.selectMethods(targetType, new MethodIntrospector.MetadataLookup<EventListener>()
/*     */             {
/*     */               public EventListener inspect(Method method)
/*     */               {
/* 133 */                 return (EventListener)AnnotatedElementUtils.findMergedAnnotation(method, EventListener.class);
/*     */               }
/*     */             });
/*     */       }
/* 137 */       catch (Throwable ex) {
/*     */         
/* 139 */         if (this.logger.isDebugEnabled()) {
/* 140 */           this.logger.debug("Could not resolve methods for bean with name '" + beanName + "'", ex);
/*     */         }
/*     */       } 
/* 143 */       if (CollectionUtils.isEmpty(annotatedMethods)) {
/* 144 */         this.nonAnnotatedClasses.add(targetType);
/* 145 */         if (this.logger.isTraceEnabled()) {
/* 146 */           this.logger.trace("No @EventListener annotations found on bean class: " + targetType.getName());
/*     */         }
/*     */       }
/*     */       else {
/*     */         
/* 151 */         for (Method method : annotatedMethods.keySet()) {
/* 152 */           label31: for (EventListenerFactory factory : factories) {
/* 153 */             if (factory.supportsMethod(method)) {
/* 154 */               Method methodToUse = AopUtils.selectInvocableMethod(method, this.applicationContext
/* 155 */                   .getType(beanName));
/*     */               
/* 157 */               ApplicationListener<?> applicationListener = factory.createApplicationListener(beanName, targetType, methodToUse);
/* 158 */               if (applicationListener instanceof ApplicationListenerMethodAdapter) {
/* 159 */                 ((ApplicationListenerMethodAdapter)applicationListener)
/* 160 */                   .init((ApplicationContext)this.applicationContext, this.evaluator); break label31;
/*     */               } 
/* 162 */               this.applicationContext.addApplicationListener(applicationListener);
/*     */             } 
/*     */           } 
/*     */         } 
/*     */         
/* 167 */         if (this.logger.isDebugEnabled())
/* 168 */           this.logger.debug(annotatedMethods.size() + " @EventListener methods processed on bean '" + beanName + "': " + annotatedMethods); 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\event\EventListenerMethodProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */