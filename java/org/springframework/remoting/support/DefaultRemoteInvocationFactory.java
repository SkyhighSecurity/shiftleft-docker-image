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
/*    */ public class DefaultRemoteInvocationFactory
/*    */   implements RemoteInvocationFactory
/*    */ {
/*    */   public RemoteInvocation createRemoteInvocation(MethodInvocation methodInvocation) {
/* 32 */     return new RemoteInvocation(methodInvocation);
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\remoting\support\DefaultRemoteInvocationFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */