/*     */ package org.springframework.context.event;
/*     */ 
/*     */ import org.springframework.context.ApplicationEvent;
/*     */ import org.springframework.context.ApplicationListener;
/*     */ import org.springframework.core.ResolvableType;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SourceFilteringListener
/*     */   implements GenericApplicationListener, SmartApplicationListener
/*     */ {
/*     */   private final Object source;
/*     */   private GenericApplicationListener delegate;
/*     */   
/*     */   public SourceFilteringListener(Object source, ApplicationListener<?> delegate) {
/*  51 */     this.source = source;
/*  52 */     this.delegate = (delegate instanceof GenericApplicationListener) ? (GenericApplicationListener)delegate : new GenericApplicationListenerAdapter(delegate);
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
/*     */   protected SourceFilteringListener(Object source) {
/*  64 */     this.source = source;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onApplicationEvent(ApplicationEvent event) {
/*  70 */     if (event.getSource() == this.source) {
/*  71 */       onApplicationEventInternal(event);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsEventType(ResolvableType eventType) {
/*  77 */     return (this.delegate == null || this.delegate.supportsEventType(eventType));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsEventType(Class<? extends ApplicationEvent> eventType) {
/*  82 */     return supportsEventType(ResolvableType.forType(eventType));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsSourceType(Class<?> sourceType) {
/*  87 */     return (sourceType != null && sourceType.isInstance(this.source));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getOrder() {
/*  92 */     return (this.delegate != null) ? this.delegate.getOrder() : Integer.MAX_VALUE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void onApplicationEventInternal(ApplicationEvent event) {
/* 103 */     if (this.delegate == null) {
/* 104 */       throw new IllegalStateException("Must specify a delegate object or override the onApplicationEventInternal method");
/*     */     }
/*     */     
/* 107 */     this.delegate.onApplicationEvent(event);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\event\SourceFilteringListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */