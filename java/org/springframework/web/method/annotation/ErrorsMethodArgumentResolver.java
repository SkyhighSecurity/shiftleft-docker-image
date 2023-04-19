/*    */ package org.springframework.web.method.annotation;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import org.springframework.core.MethodParameter;
/*    */ import org.springframework.ui.ModelMap;
/*    */ import org.springframework.validation.BindingResult;
/*    */ import org.springframework.validation.Errors;
/*    */ import org.springframework.web.bind.support.WebDataBinderFactory;
/*    */ import org.springframework.web.context.request.NativeWebRequest;
/*    */ import org.springframework.web.method.support.HandlerMethodArgumentResolver;
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
/*    */ public class ErrorsMethodArgumentResolver
/*    */   implements HandlerMethodArgumentResolver
/*    */ {
/*    */   public boolean supportsParameter(MethodParameter parameter) {
/* 45 */     Class<?> paramType = parameter.getParameterType();
/* 46 */     return Errors.class.isAssignableFrom(paramType);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
/* 53 */     ModelMap model = mavContainer.getModel();
/* 54 */     if (model.size() > 0) {
/* 55 */       int lastIndex = model.size() - 1;
/* 56 */       String lastKey = (new ArrayList<String>(model.keySet())).get(lastIndex);
/* 57 */       if (lastKey.startsWith(BindingResult.MODEL_KEY_PREFIX)) {
/* 58 */         return model.get(lastKey);
/*    */       }
/*    */     } 
/*    */     
/* 62 */     throw new IllegalStateException("An Errors/BindingResult argument is expected to be declared immediately after the model attribute, the @RequestBody or the @RequestPart arguments to which they apply: " + parameter
/*    */         
/* 64 */         .getMethod());
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\method\annotation\ErrorsMethodArgumentResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */