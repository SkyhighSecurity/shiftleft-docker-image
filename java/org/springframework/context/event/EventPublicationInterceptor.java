/*     */ package org.springframework.context.event;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import org.aopalliance.intercept.MethodInterceptor;
/*     */ import org.aopalliance.intercept.MethodInvocation;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.context.ApplicationEvent;
/*     */ import org.springframework.context.ApplicationEventPublisher;
/*     */ import org.springframework.context.ApplicationEventPublisherAware;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EventPublicationInterceptor
/*     */   implements MethodInterceptor, ApplicationEventPublisherAware, InitializingBean
/*     */ {
/*     */   private Constructor<?> applicationEventClassConstructor;
/*     */   private ApplicationEventPublisher applicationEventPublisher;
/*     */   
/*     */   public void setApplicationEventClass(Class<?> applicationEventClass) {
/*  66 */     if (ApplicationEvent.class == applicationEventClass || 
/*  67 */       !ApplicationEvent.class.isAssignableFrom(applicationEventClass)) {
/*  68 */       throw new IllegalArgumentException("'applicationEventClass' needs to extend ApplicationEvent");
/*     */     }
/*     */     try {
/*  71 */       this.applicationEventClassConstructor = applicationEventClass.getConstructor(new Class[] { Object.class });
/*     */     }
/*  73 */     catch (NoSuchMethodException ex) {
/*  74 */       throw new IllegalArgumentException("ApplicationEvent class [" + applicationEventClass
/*  75 */           .getName() + "] does not have the required Object constructor: " + ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
/*  81 */     this.applicationEventPublisher = applicationEventPublisher;
/*     */   }
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() throws Exception {
/*  86 */     if (this.applicationEventClassConstructor == null) {
/*  87 */       throw new IllegalArgumentException("Property 'applicationEventClass' is required");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object invoke(MethodInvocation invocation) throws Throwable {
/*  94 */     Object retVal = invocation.proceed();
/*     */ 
/*     */     
/*  97 */     ApplicationEvent event = (ApplicationEvent)this.applicationEventClassConstructor.newInstance(new Object[] { invocation.getThis() });
/*  98 */     this.applicationEventPublisher.publishEvent(event);
/*     */     
/* 100 */     return retVal;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\event\EventPublicationInterceptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */