/*    */ package org.springframework.aop.framework.autoproxy;
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
/*    */ public class ProxyCreationContext
/*    */ {
/* 32 */   private static final ThreadLocal<String> currentProxiedBeanName = (ThreadLocal<String>)new NamedThreadLocal("Name of currently proxied bean");
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static String getCurrentProxiedBeanName() {
/* 41 */     return currentProxiedBeanName.get();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static void setCurrentProxiedBeanName(String beanName) {
/* 49 */     if (beanName != null) {
/* 50 */       currentProxiedBeanName.set(beanName);
/*    */     } else {
/*    */       
/* 53 */       currentProxiedBeanName.remove();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\framework\autoproxy\ProxyCreationContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */