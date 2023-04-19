/*    */ package org.springframework.aop.framework;
/*    */ 
/*    */ import org.springframework.core.NamedThreadLocal;
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
/*    */ 
/*    */ 
/*    */ public abstract class AopContext
/*    */ {
/* 49 */   private static final ThreadLocal<Object> currentProxy = (ThreadLocal<Object>)new NamedThreadLocal("Current AOP proxy");
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
/*    */   public static Object currentProxy() throws IllegalStateException {
/* 62 */     Object proxy = currentProxy.get();
/* 63 */     if (proxy == null) {
/* 64 */       throw new IllegalStateException("Cannot find current proxy: Set 'exposeProxy' property on Advised to 'true' to make it available.");
/*    */     }
/*    */     
/* 67 */     return proxy;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static Object setCurrentProxy(Object proxy) {
/* 78 */     Object old = currentProxy.get();
/* 79 */     if (proxy != null) {
/* 80 */       currentProxy.set(proxy);
/*    */     } else {
/*    */       
/* 83 */       currentProxy.remove();
/*    */     } 
/* 85 */     return old;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\framework\AopContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */