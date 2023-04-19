/*    */ package org.springframework.cache.interceptor;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import java.lang.reflect.Method;
/*    */ import org.springframework.aop.support.StaticMethodMatcherPointcut;
/*    */ import org.springframework.util.CollectionUtils;
/*    */ import org.springframework.util.ObjectUtils;
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
/*    */ abstract class CacheOperationSourcePointcut
/*    */   extends StaticMethodMatcherPointcut
/*    */   implements Serializable
/*    */ {
/*    */   public boolean matches(Method method, Class<?> targetClass) {
/* 38 */     CacheOperationSource cas = getCacheOperationSource();
/* 39 */     return (cas != null && !CollectionUtils.isEmpty(cas.getCacheOperations(method, targetClass)));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object other) {
/* 44 */     if (this == other) {
/* 45 */       return true;
/*    */     }
/* 47 */     if (!(other instanceof CacheOperationSourcePointcut)) {
/* 48 */       return false;
/*    */     }
/* 50 */     CacheOperationSourcePointcut otherPc = (CacheOperationSourcePointcut)other;
/* 51 */     return ObjectUtils.nullSafeEquals(getCacheOperationSource(), otherPc.getCacheOperationSource());
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 56 */     return CacheOperationSourcePointcut.class.hashCode();
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 61 */     return getClass().getName() + ": " + getCacheOperationSource();
/*    */   }
/*    */   
/*    */   protected abstract CacheOperationSource getCacheOperationSource();
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\cache\interceptor\CacheOperationSourcePointcut.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */