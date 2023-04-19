/*    */ package org.springframework.aop.framework.adapter;
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
/*    */ public abstract class GlobalAdvisorAdapterRegistry
/*    */ {
/* 32 */   private static AdvisorAdapterRegistry instance = new DefaultAdvisorAdapterRegistry();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static AdvisorAdapterRegistry getInstance() {
/* 38 */     return instance;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static void reset() {
/* 47 */     instance = new DefaultAdvisorAdapterRegistry();
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\framework\adapter\GlobalAdvisorAdapterRegistry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */