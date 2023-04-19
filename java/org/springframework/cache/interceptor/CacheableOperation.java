/*    */ package org.springframework.cache.interceptor;
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
/*    */ public class CacheableOperation
/*    */   extends CacheOperation
/*    */ {
/*    */   private final String unless;
/*    */   private final boolean sync;
/*    */   
/*    */   public CacheableOperation(Builder b) {
/* 38 */     super(b);
/* 39 */     this.unless = b.unless;
/* 40 */     this.sync = b.sync;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getUnless() {
/* 45 */     return this.unless;
/*    */   }
/*    */   
/*    */   public boolean isSync() {
/* 49 */     return this.sync;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static class Builder
/*    */     extends CacheOperation.Builder
/*    */   {
/*    */     private String unless;
/*    */     
/*    */     private boolean sync;
/*    */ 
/*    */     
/*    */     public void setUnless(String unless) {
/* 63 */       this.unless = unless;
/*    */     }
/*    */     
/*    */     public void setSync(boolean sync) {
/* 67 */       this.sync = sync;
/*    */     }
/*    */ 
/*    */     
/*    */     protected StringBuilder getOperationDescription() {
/* 72 */       StringBuilder sb = super.getOperationDescription();
/* 73 */       sb.append(" | unless='");
/* 74 */       sb.append(this.unless);
/* 75 */       sb.append("'");
/* 76 */       sb.append(" | sync='");
/* 77 */       sb.append(this.sync);
/* 78 */       sb.append("'");
/* 79 */       return sb;
/*    */     }
/*    */ 
/*    */     
/*    */     public CacheableOperation build() {
/* 84 */       return new CacheableOperation(this);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\cache\interceptor\CacheableOperation.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */