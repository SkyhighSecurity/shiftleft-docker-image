/*    */ package org.springframework.web.method;
/*    */ 
/*    */ import java.lang.reflect.Method;
/*    */ import java.util.Set;
/*    */ import org.springframework.core.MethodIntrospector;
/*    */ import org.springframework.util.ReflectionUtils;
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
/*    */ public abstract class HandlerMethodSelector
/*    */ {
/*    */   public static Set<Method> selectMethods(Class<?> handlerType, ReflectionUtils.MethodFilter handlerMethodFilter) {
/* 45 */     return MethodIntrospector.selectMethods(handlerType, handlerMethodFilter);
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\method\HandlerMethodSelector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */