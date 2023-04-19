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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface CacheOperationInvoker
/*    */ {
/*    */   Object invoke() throws ThrowableWrapper;
/*    */   
/*    */   public static class ThrowableWrapper
/*    */     extends RuntimeException
/*    */   {
/*    */     private final Throwable original;
/*    */     
/*    */     public ThrowableWrapper(Throwable original) {
/* 50 */       super(original.getMessage(), original);
/* 51 */       this.original = original;
/*    */     }
/*    */     
/*    */     public Throwable getOriginal() {
/* 55 */       return this.original;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\cache\interceptor\CacheOperationInvoker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */