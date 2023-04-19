/*    */ package org.springframework.cache.support;
/*    */ 
/*    */ import org.springframework.cache.Cache;
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
/*    */ public class SimpleValueWrapper
/*    */   implements Cache.ValueWrapper
/*    */ {
/*    */   private final Object value;
/*    */   
/*    */   public SimpleValueWrapper(Object value) {
/* 38 */     this.value = value;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Object get() {
/* 47 */     return this.value;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\cache\support\SimpleValueWrapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */