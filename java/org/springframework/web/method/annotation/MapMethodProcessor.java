/*    */ package org.springframework.web.method.annotation;
/*    */ 
/*    */ import java.util.Map;
/*    */ import org.springframework.core.MethodParameter;
/*    */ import org.springframework.web.bind.support.WebDataBinderFactory;
/*    */ import org.springframework.web.context.request.NativeWebRequest;
/*    */ import org.springframework.web.method.support.HandlerMethodArgumentResolver;
/*    */ import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
/*    */ import org.springframework.web.method.support.ModelAndViewContainer;
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
/*    */ public class MapMethodProcessor
/*    */   implements HandlerMethodArgumentResolver, HandlerMethodReturnValueHandler
/*    */ {
/*    */   public boolean supportsParameter(MethodParameter parameter) {
/* 43 */     return Map.class.isAssignableFrom(parameter.getParameterType());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
/* 50 */     return mavContainer.getModel();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean supportsReturnType(MethodParameter returnType) {
/* 55 */     return Map.class.isAssignableFrom(returnType.getParameterType());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
/* 63 */     if (returnValue == null) {
/*    */       return;
/*    */     }
/* 66 */     if (returnValue instanceof Map) {
/* 67 */       mavContainer.addAllAttributes((Map)returnValue);
/*    */     }
/*    */     else {
/*    */       
/* 71 */       throw new UnsupportedOperationException("Unexpected return type: " + returnType
/* 72 */           .getParameterType().getName() + " in method: " + returnType.getMethod());
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\method\annotation\MapMethodProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */