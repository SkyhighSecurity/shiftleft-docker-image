/*    */ package org.springframework.context.annotation;
/*    */ 
/*    */ import org.springframework.util.Assert;
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
/*    */ public class ScopeMetadata
/*    */ {
/* 37 */   private String scopeName = "singleton";
/*    */   
/* 39 */   private ScopedProxyMode scopedProxyMode = ScopedProxyMode.NO;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setScopeName(String scopeName) {
/* 46 */     Assert.notNull(scopeName, "'scopeName' must not be null");
/* 47 */     this.scopeName = scopeName;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getScopeName() {
/* 54 */     return this.scopeName;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setScopedProxyMode(ScopedProxyMode scopedProxyMode) {
/* 61 */     Assert.notNull(scopedProxyMode, "'scopedProxyMode' must not be null");
/* 62 */     this.scopedProxyMode = scopedProxyMode;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ScopedProxyMode getScopedProxyMode() {
/* 69 */     return this.scopedProxyMode;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\annotation\ScopeMetadata.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */