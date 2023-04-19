/*    */ package org.springframework.cache.interceptor;
/*    */ 
/*    */ import java.lang.reflect.Method;
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
/*    */ public class SimpleKeyGenerator
/*    */   implements KeyGenerator
/*    */ {
/*    */   public Object generate(Object target, Method method, Object... params) {
/* 42 */     return generateKey(params);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Object generateKey(Object... params) {
/* 49 */     if (params.length == 0) {
/* 50 */       return SimpleKey.EMPTY;
/*    */     }
/* 52 */     if (params.length == 1) {
/* 53 */       Object param = params[0];
/* 54 */       if (param != null && !param.getClass().isArray()) {
/* 55 */         return param;
/*    */       }
/*    */     } 
/* 58 */     return new SimpleKey(params);
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\cache\interceptor\SimpleKeyGenerator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */