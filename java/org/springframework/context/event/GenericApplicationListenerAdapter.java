/*     */ package org.springframework.context.event;
/*     */ 
/*     */ import org.springframework.aop.support.AopUtils;
/*     */ import org.springframework.context.ApplicationEvent;
/*     */ import org.springframework.context.ApplicationListener;
/*     */ import org.springframework.core.Ordered;
/*     */ import org.springframework.core.ResolvableType;
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
/*     */ public class GenericApplicationListenerAdapter
/*     */   implements GenericApplicationListener, SmartApplicationListener
/*     */ {
/*     */   private final ApplicationListener<ApplicationEvent> delegate;
/*     */   private final ResolvableType declaredEventType;
/*     */   
/*     */   public GenericApplicationListenerAdapter(ApplicationListener<?> delegate) {
/*  48 */     Assert.notNull(delegate, "Delegate listener must not be null");
/*  49 */     this.delegate = (ApplicationListener)delegate;
/*  50 */     this.declaredEventType = resolveDeclaredEventType(this.delegate);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onApplicationEvent(ApplicationEvent event) {
/*  56 */     this.delegate.onApplicationEvent(event);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean supportsEventType(ResolvableType eventType) {
/*  62 */     if (this.delegate instanceof SmartApplicationListener) {
/*  63 */       Class<? extends ApplicationEvent> eventClass = eventType.resolve();
/*  64 */       return (eventClass != null && ((SmartApplicationListener)this.delegate).supportsEventType(eventClass));
/*     */     } 
/*     */     
/*  67 */     return (this.declaredEventType == null || this.declaredEventType.isAssignableFrom(eventType));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean supportsEventType(Class<? extends ApplicationEvent> eventType) {
/*  73 */     return supportsEventType(ResolvableType.forClass(eventType));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsSourceType(Class<?> sourceType) {
/*  78 */     return (!(this.delegate instanceof SmartApplicationListener) || ((SmartApplicationListener)this.delegate)
/*  79 */       .supportsSourceType(sourceType));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getOrder() {
/*  84 */     return (this.delegate instanceof Ordered) ? ((Ordered)this.delegate).getOrder() : Integer.MAX_VALUE;
/*     */   }
/*     */ 
/*     */   
/*     */   static ResolvableType resolveDeclaredEventType(Class<?> listenerType) {
/*  89 */     ResolvableType resolvableType = ResolvableType.forClass(listenerType).as(ApplicationListener.class);
/*  90 */     return resolvableType.hasGenerics() ? resolvableType.getGeneric(new int[0]) : null;
/*     */   }
/*     */   
/*     */   private static ResolvableType resolveDeclaredEventType(ApplicationListener<ApplicationEvent> listener) {
/*  94 */     ResolvableType declaredEventType = resolveDeclaredEventType(listener.getClass());
/*  95 */     if (declaredEventType == null || declaredEventType.isAssignableFrom(
/*  96 */         ResolvableType.forClass(ApplicationEvent.class))) {
/*  97 */       Class<?> targetClass = AopUtils.getTargetClass(listener);
/*  98 */       if (targetClass != listener.getClass()) {
/*  99 */         declaredEventType = resolveDeclaredEventType(targetClass);
/*     */       }
/*     */     } 
/* 102 */     return declaredEventType;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\event\GenericApplicationListenerAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */