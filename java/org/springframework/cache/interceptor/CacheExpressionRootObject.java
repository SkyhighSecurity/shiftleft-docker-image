/*    */ package org.springframework.cache.interceptor;
/*    */ 
/*    */ import java.lang.reflect.Method;
/*    */ import java.util.Collection;
/*    */ import org.springframework.cache.Cache;
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
/*    */ 
/*    */ class CacheExpressionRootObject
/*    */ {
/*    */   private final Collection<? extends Cache> caches;
/*    */   private final Method method;
/*    */   private final Object[] args;
/*    */   private final Object target;
/*    */   private final Class<?> targetClass;
/*    */   
/*    */   public CacheExpressionRootObject(Collection<? extends Cache> caches, Method method, Object[] args, Object target, Class<?> targetClass) {
/* 48 */     Assert.notNull(method, "Method is required");
/* 49 */     Assert.notNull(targetClass, "targetClass is required");
/* 50 */     this.method = method;
/* 51 */     this.target = target;
/* 52 */     this.targetClass = targetClass;
/* 53 */     this.args = args;
/* 54 */     this.caches = caches;
/*    */   }
/*    */ 
/*    */   
/*    */   public Collection<? extends Cache> getCaches() {
/* 59 */     return this.caches;
/*    */   }
/*    */   
/*    */   public Method getMethod() {
/* 63 */     return this.method;
/*    */   }
/*    */   
/*    */   public String getMethodName() {
/* 67 */     return this.method.getName();
/*    */   }
/*    */   
/*    */   public Object[] getArgs() {
/* 71 */     return this.args;
/*    */   }
/*    */   
/*    */   public Object getTarget() {
/* 75 */     return this.target;
/*    */   }
/*    */   
/*    */   public Class<?> getTargetClass() {
/* 79 */     return this.targetClass;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\cache\interceptor\CacheExpressionRootObject.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */