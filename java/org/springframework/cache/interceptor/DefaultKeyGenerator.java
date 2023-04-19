/*    */ package org.springframework.cache.interceptor;
/*    */ 
/*    */ import java.lang.reflect.Method;
/*    */ import java.util.Arrays;
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
/*    */ 
/*    */ @Deprecated
/*    */ public class DefaultKeyGenerator
/*    */   implements KeyGenerator
/*    */ {
/*    */   public static final int NO_PARAM_KEY = 0;
/*    */   public static final int NULL_PARAM_KEY = 53;
/*    */   
/*    */   public Object generate(Object target, Method method, Object... params) {
/* 52 */     if (params.length == 0) {
/* 53 */       return Integer.valueOf(0);
/*    */     }
/* 55 */     if (params.length == 1) {
/* 56 */       Object param = params[0];
/* 57 */       if (param == null) {
/* 58 */         return Integer.valueOf(53);
/*    */       }
/* 60 */       if (!param.getClass().isArray()) {
/* 61 */         return param;
/*    */       }
/*    */     } 
/* 64 */     return Integer.valueOf(Arrays.deepHashCode(params));
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\cache\interceptor\DefaultKeyGenerator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */