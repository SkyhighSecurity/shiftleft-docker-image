/*    */ package org.springframework.web.method.annotation;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ import java.util.LinkedHashMap;
/*    */ import java.util.Map;
/*    */ import org.springframework.core.MethodParameter;
/*    */ import org.springframework.http.HttpHeaders;
/*    */ import org.springframework.util.LinkedMultiValueMap;
/*    */ import org.springframework.util.MultiValueMap;
/*    */ import org.springframework.web.bind.annotation.RequestHeader;
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
/*    */ 
/*    */ 
/*    */ public class RequestHeaderMapMethodArgumentResolver
/*    */   implements HandlerMethodArgumentResolver
/*    */ {
/*    */   public boolean supportsParameter(MethodParameter parameter) {
/* 50 */     return (parameter.hasParameterAnnotation(RequestHeader.class) && Map.class
/* 51 */       .isAssignableFrom(parameter.getParameterType()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
/* 58 */     Class<?> paramType = parameter.getParameterType();
/* 59 */     if (MultiValueMap.class.isAssignableFrom(paramType)) {
/*    */       LinkedMultiValueMap linkedMultiValueMap;
/* 61 */       if (HttpHeaders.class.isAssignableFrom(paramType)) {
/* 62 */         HttpHeaders httpHeaders = new HttpHeaders();
/*    */       } else {
/*    */         
/* 65 */         linkedMultiValueMap = new LinkedMultiValueMap();
/*    */       } 
/* 67 */       for (Iterator<String> iterator1 = webRequest.getHeaderNames(); iterator1.hasNext(); ) {
/* 68 */         String headerName = iterator1.next();
/* 69 */         String[] headerValues = webRequest.getHeaderValues(headerName);
/* 70 */         if (headerValues != null) {
/* 71 */           for (String headerValue : headerValues) {
/* 72 */             linkedMultiValueMap.add(headerName, headerValue);
/*    */           }
/*    */         }
/*    */       } 
/* 76 */       return linkedMultiValueMap;
/*    */     } 
/*    */     
/* 79 */     Map<String, String> result = new LinkedHashMap<String, String>();
/* 80 */     for (Iterator<String> iterator = webRequest.getHeaderNames(); iterator.hasNext(); ) {
/* 81 */       String headerName = iterator.next();
/* 82 */       String headerValue = webRequest.getHeader(headerName);
/* 83 */       if (headerValue != null) {
/* 84 */         result.put(headerName, headerValue);
/*    */       }
/*    */     } 
/* 87 */     return result;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\method\annotation\RequestHeaderMapMethodArgumentResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */