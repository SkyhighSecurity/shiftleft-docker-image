/*    */ package org.springframework.core;
/*    */ 
/*    */ import java.lang.reflect.Constructor;
/*    */ import java.lang.reflect.Method;
/*    */ import java.lang.reflect.Parameter;
/*    */ import org.springframework.lang.UsesJava8;
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
/*    */ @UsesJava8
/*    */ public class StandardReflectionParameterNameDiscoverer
/*    */   implements ParameterNameDiscoverer
/*    */ {
/*    */   public String[] getParameterNames(Method method) {
/* 39 */     Parameter[] parameters = method.getParameters();
/* 40 */     String[] parameterNames = new String[parameters.length];
/* 41 */     for (int i = 0; i < parameters.length; i++) {
/* 42 */       Parameter param = parameters[i];
/* 43 */       if (!param.isNamePresent()) {
/* 44 */         return null;
/*    */       }
/* 46 */       parameterNames[i] = param.getName();
/*    */     } 
/* 48 */     return parameterNames;
/*    */   }
/*    */ 
/*    */   
/*    */   public String[] getParameterNames(Constructor<?> ctor) {
/* 53 */     Parameter[] parameters = ctor.getParameters();
/* 54 */     String[] parameterNames = new String[parameters.length];
/* 55 */     for (int i = 0; i < parameters.length; i++) {
/* 56 */       Parameter param = parameters[i];
/* 57 */       if (!param.isNamePresent()) {
/* 58 */         return null;
/*    */       }
/* 60 */       parameterNames[i] = param.getName();
/*    */     } 
/* 62 */     return parameterNames;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\StandardReflectionParameterNameDiscoverer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */