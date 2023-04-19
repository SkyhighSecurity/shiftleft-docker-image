/*     */ package org.springframework.aop.target.dynamic;
/*     */ 
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.aop.TargetSource;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractRefreshableTargetSource
/*     */   implements TargetSource, Refreshable
/*     */ {
/*  42 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*     */   protected Object targetObject;
/*     */   
/*  46 */   private long refreshCheckDelay = -1L;
/*     */   
/*  48 */   private long lastRefreshCheck = -1L;
/*     */   
/*  50 */   private long lastRefreshTime = -1L;
/*     */   
/*  52 */   private long refreshCount = 0L;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRefreshCheckDelay(long refreshCheckDelay) {
/*  62 */     this.refreshCheckDelay = refreshCheckDelay;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized Class<?> getTargetClass() {
/*  68 */     if (this.targetObject == null) {
/*  69 */       refresh();
/*     */     }
/*  71 */     return this.targetObject.getClass();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isStatic() {
/*  79 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public final synchronized Object getTarget() {
/*  84 */     if ((refreshCheckDelayElapsed() && requiresRefresh()) || this.targetObject == null) {
/*  85 */       refresh();
/*     */     }
/*  87 */     return this.targetObject;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void releaseTarget(Object object) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final synchronized void refresh() {
/* 100 */     this.logger.debug("Attempting to refresh target");
/*     */     
/* 102 */     this.targetObject = freshTarget();
/* 103 */     this.refreshCount++;
/* 104 */     this.lastRefreshTime = System.currentTimeMillis();
/*     */     
/* 106 */     this.logger.debug("Target refreshed successfully");
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized long getRefreshCount() {
/* 111 */     return this.refreshCount;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized long getLastRefreshTime() {
/* 116 */     return this.lastRefreshTime;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean refreshCheckDelayElapsed() {
/* 121 */     if (this.refreshCheckDelay < 0L) {
/* 122 */       return false;
/*     */     }
/*     */     
/* 125 */     long currentTimeMillis = System.currentTimeMillis();
/*     */     
/* 127 */     if (this.lastRefreshCheck < 0L || currentTimeMillis - this.lastRefreshCheck > this.refreshCheckDelay) {
/*     */       
/* 129 */       this.lastRefreshCheck = currentTimeMillis;
/* 130 */       this.logger.debug("Refresh check delay elapsed - checking whether refresh is required");
/* 131 */       return true;
/*     */     } 
/*     */     
/* 134 */     return false;
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
/*     */   protected boolean requiresRefresh() {
/* 147 */     return true;
/*     */   }
/*     */   
/*     */   protected abstract Object freshTarget();
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\target\dynamic\AbstractRefreshableTargetSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */