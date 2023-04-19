/*    */ package org.springframework.remoting.support;
/*    */ 
/*    */ import org.aopalliance.intercept.MethodInvocation;
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
/*    */ public abstract class RemoteInvocationBasedAccessor
/*    */   extends UrlBasedRemoteAccessor
/*    */ {
/* 37 */   private RemoteInvocationFactory remoteInvocationFactory = new DefaultRemoteInvocationFactory();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setRemoteInvocationFactory(RemoteInvocationFactory remoteInvocationFactory) {
/* 47 */     this.remoteInvocationFactory = (remoteInvocationFactory != null) ? remoteInvocationFactory : new DefaultRemoteInvocationFactory();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public RemoteInvocationFactory getRemoteInvocationFactory() {
/* 55 */     return this.remoteInvocationFactory;
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
/*    */ 
/*    */ 
/*    */   
/*    */   protected RemoteInvocation createRemoteInvocation(MethodInvocation methodInvocation) {
/* 71 */     return getRemoteInvocationFactory().createRemoteInvocation(methodInvocation);
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
/*    */   
/*    */   protected Object recreateRemoteInvocationResult(RemoteInvocationResult result) throws Throwable {
/* 85 */     return result.recreate();
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\remoting\support\RemoteInvocationBasedAccessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */