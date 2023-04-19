/*    */ package org.springframework.beans.factory.support;
/*    */ 
/*    */ import java.security.AccessControlContext;
/*    */ import java.security.AccessController;
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
/*    */ public class SimpleSecurityContextProvider
/*    */   implements SecurityContextProvider
/*    */ {
/*    */   private final AccessControlContext acc;
/*    */   
/*    */   public SimpleSecurityContextProvider() {
/* 39 */     this(null);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public SimpleSecurityContextProvider(AccessControlContext acc) {
/* 50 */     this.acc = acc;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public AccessControlContext getAccessControlContext() {
/* 56 */     return (this.acc != null) ? this.acc : AccessController.getContext();
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\support\SimpleSecurityContextProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */