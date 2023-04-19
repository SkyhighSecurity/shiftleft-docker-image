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
/*    */ public class CachePutOperation
/*    */   extends CacheOperation
/*    */ {
/*    */   private final String unless;
/*    */   
/*    */   public CachePutOperation(Builder b) {
/* 36 */     super(b);
/* 37 */     this.unless = b.unless;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getUnless() {
/* 42 */     return this.unless;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static class Builder
/*    */     extends CacheOperation.Builder
/*    */   {
/*    */     private String unless;
/*    */ 
/*    */     
/*    */     public void setUnless(String unless) {
/* 54 */       this.unless = unless;
/*    */     }
/*    */ 
/*    */     
/*    */     protected StringBuilder getOperationDescription() {
/* 59 */       StringBuilder sb = super.getOperationDescription();
/* 60 */       sb.append(" | unless='");
/* 61 */       sb.append(this.unless);
/* 62 */       sb.append("'");
/* 63 */       return sb;
/*    */     }
/*    */     
/*    */     public CachePutOperation build() {
/* 67 */       return new CachePutOperation(this);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\cache\interceptor\CachePutOperation.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */