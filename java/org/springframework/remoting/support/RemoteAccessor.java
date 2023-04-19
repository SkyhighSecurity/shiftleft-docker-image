/*    */ package org.springframework.remoting.support;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class RemoteAccessor
/*    */   extends RemotingSupport
/*    */ {
/*    */   private Class<?> serviceInterface;
/*    */   
/*    */   public void setServiceInterface(Class<?> serviceInterface) {
/* 49 */     if (serviceInterface != null && !serviceInterface.isInterface()) {
/* 50 */       throw new IllegalArgumentException("'serviceInterface' must be an interface");
/*    */     }
/* 52 */     this.serviceInterface = serviceInterface;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Class<?> getServiceInterface() {
/* 59 */     return this.serviceInterface;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\remoting\support\RemoteAccessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */