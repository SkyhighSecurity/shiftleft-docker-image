/*    */ package org.springframework.aop.target;
/*    */ 
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ import org.springframework.aop.TargetSource;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class AbstractLazyCreationTargetSource
/*    */   implements TargetSource
/*    */ {
/* 45 */   protected final Log logger = LogFactory.getLog(getClass());
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private Object lazyTarget;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public synchronized boolean isInitialized() {
/* 56 */     return (this.lazyTarget != null);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public synchronized Class<?> getTargetClass() {
/* 69 */     return (this.lazyTarget != null) ? this.lazyTarget.getClass() : null;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isStatic() {
/* 74 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public synchronized Object getTarget() throws Exception {
/* 84 */     if (this.lazyTarget == null) {
/* 85 */       this.logger.debug("Initializing lazy target object");
/* 86 */       this.lazyTarget = createObject();
/*    */     } 
/* 88 */     return this.lazyTarget;
/*    */   }
/*    */   
/*    */   public void releaseTarget(Object target) throws Exception {}
/*    */   
/*    */   protected abstract Object createObject() throws Exception;
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\target\AbstractLazyCreationTargetSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */