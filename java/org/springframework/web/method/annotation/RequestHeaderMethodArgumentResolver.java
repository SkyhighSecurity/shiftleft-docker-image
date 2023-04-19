/*    */ package org.springframework.web.method.annotation;
/*    */ 
/*    */ import java.util.Map;
/*    */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
/*    */ import org.springframework.core.MethodParameter;
/*    */ import org.springframework.web.bind.ServletRequestBindingException;
/*    */ import org.springframework.web.bind.annotation.RequestHeader;
/*    */ import org.springframework.web.context.request.NativeWebRequest;
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
/*    */ public class RequestHeaderMethodArgumentResolver
/*    */   extends AbstractNamedValueMethodArgumentResolver
/*    */ {
/*    */   public RequestHeaderMethodArgumentResolver(ConfigurableBeanFactory beanFactory) {
/* 52 */     super(beanFactory);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean supportsParameter(MethodParameter parameter) {
/* 58 */     return (parameter.hasParameterAnnotation(RequestHeader.class) && 
/* 59 */       !Map.class.isAssignableFrom(parameter.nestedIfOptional().getNestedParameterType()));
/*    */   }
/*    */ 
/*    */   
/*    */   protected AbstractNamedValueMethodArgumentResolver.NamedValueInfo createNamedValueInfo(MethodParameter parameter) {
/* 64 */     RequestHeader annotation = (RequestHeader)parameter.getParameterAnnotation(RequestHeader.class);
/* 65 */     return new RequestHeaderNamedValueInfo(annotation);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Object resolveName(String name, MethodParameter parameter, NativeWebRequest request) throws Exception {
/* 70 */     String[] headerValues = request.getHeaderValues(name);
/* 71 */     if (headerValues != null) {
/* 72 */       return (headerValues.length == 1) ? headerValues[0] : headerValues;
/*    */     }
/*    */     
/* 75 */     return null;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void handleMissingValue(String name, MethodParameter parameter) throws ServletRequestBindingException {
/* 81 */     throw new ServletRequestBindingException("Missing request header '" + name + "' for method parameter of type " + parameter
/* 82 */         .getNestedParameterType().getSimpleName());
/*    */   }
/*    */   
/*    */   private static class RequestHeaderNamedValueInfo
/*    */     extends AbstractNamedValueMethodArgumentResolver.NamedValueInfo
/*    */   {
/*    */     private RequestHeaderNamedValueInfo(RequestHeader annotation) {
/* 89 */       super(annotation.name(), annotation.required(), annotation.defaultValue());
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\method\annotation\RequestHeaderMethodArgumentResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */