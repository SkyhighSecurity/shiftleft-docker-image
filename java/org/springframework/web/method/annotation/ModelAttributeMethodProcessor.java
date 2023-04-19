/*     */ package org.springframework.web.method.annotation;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.util.Map;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.BeanUtils;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.core.annotation.AnnotationUtils;
/*     */ import org.springframework.validation.BindException;
/*     */ import org.springframework.validation.Errors;
/*     */ import org.springframework.validation.annotation.Validated;
/*     */ import org.springframework.web.bind.WebDataBinder;
/*     */ import org.springframework.web.bind.annotation.ModelAttribute;
/*     */ import org.springframework.web.bind.support.WebDataBinderFactory;
/*     */ import org.springframework.web.bind.support.WebRequestDataBinder;
/*     */ import org.springframework.web.context.request.NativeWebRequest;
/*     */ import org.springframework.web.context.request.WebRequest;
/*     */ import org.springframework.web.method.support.HandlerMethodArgumentResolver;
/*     */ import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
/*     */ import org.springframework.web.method.support.ModelAndViewContainer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ModelAttributeMethodProcessor
/*     */   implements HandlerMethodArgumentResolver, HandlerMethodReturnValueHandler
/*     */ {
/*  59 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final boolean annotationNotRequired;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ModelAttributeMethodProcessor(boolean annotationNotRequired) {
/*  71 */     this.annotationNotRequired = annotationNotRequired;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean supportsParameter(MethodParameter parameter) {
/*  82 */     return (parameter.hasParameterAnnotation(ModelAttribute.class) || (this.annotationNotRequired && 
/*  83 */       !BeanUtils.isSimpleProperty(parameter.getParameterType())));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
/*  99 */     String name = ModelFactory.getNameForParameter(parameter);
/* 100 */     ModelAttribute ann = (ModelAttribute)parameter.getParameterAnnotation(ModelAttribute.class);
/* 101 */     if (ann != null) {
/* 102 */       mavContainer.setBinding(name, ann.binding());
/*     */     }
/*     */ 
/*     */     
/* 106 */     Object attribute = mavContainer.containsAttribute(name) ? mavContainer.getModel().get(name) : createAttribute(name, parameter, binderFactory, webRequest);
/*     */     
/* 108 */     WebDataBinder binder = binderFactory.createBinder(webRequest, attribute, name);
/* 109 */     if (binder.getTarget() != null) {
/* 110 */       if (!mavContainer.isBindingDisabled(name)) {
/* 111 */         bindRequestParameters(binder, webRequest);
/*     */       }
/* 113 */       validateIfApplicable(binder, parameter);
/* 114 */       if (binder.getBindingResult().hasErrors() && isBindExceptionRequired(binder, parameter)) {
/* 115 */         throw new BindException(binder.getBindingResult());
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 120 */     Map<String, Object> bindingResultModel = binder.getBindingResult().getModel();
/* 121 */     mavContainer.removeAttributes(bindingResultModel);
/* 122 */     mavContainer.addAllAttributes(bindingResultModel);
/*     */     
/* 124 */     return binder.convertIfNecessary(binder.getTarget(), parameter.getParameterType(), parameter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object createAttribute(String attributeName, MethodParameter parameter, WebDataBinderFactory binderFactory, NativeWebRequest webRequest) throws Exception {
/* 139 */     return BeanUtils.instantiateClass(parameter.getParameterType());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void bindRequestParameters(WebDataBinder binder, NativeWebRequest request) {
/* 148 */     ((WebRequestDataBinder)binder).bind((WebRequest)request);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void validateIfApplicable(WebDataBinder binder, MethodParameter parameter) {
/* 160 */     Annotation[] annotations = parameter.getParameterAnnotations();
/* 161 */     for (Annotation ann : annotations) {
/* 162 */       Validated validatedAnn = (Validated)AnnotationUtils.getAnnotation(ann, Validated.class);
/* 163 */       if (validatedAnn != null || ann.annotationType().getSimpleName().startsWith("Valid")) {
/* 164 */         Object hints = (validatedAnn != null) ? validatedAnn.value() : AnnotationUtils.getValue(ann);
/* 165 */         (new Object[1])[0] = hints; Object[] validationHints = (hints instanceof Object[]) ? (Object[])hints : new Object[1];
/* 166 */         binder.validate(validationHints);
/*     */         break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isBindExceptionRequired(WebDataBinder binder, MethodParameter parameter) {
/* 179 */     int i = parameter.getParameterIndex();
/* 180 */     Class<?>[] paramTypes = parameter.getMethod().getParameterTypes();
/* 181 */     boolean hasBindingResult = (paramTypes.length > i + 1 && Errors.class.isAssignableFrom(paramTypes[i + 1]));
/* 182 */     return !hasBindingResult;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean supportsReturnType(MethodParameter returnType) {
/* 192 */     return (returnType.hasMethodAnnotation(ModelAttribute.class) || (this.annotationNotRequired && 
/* 193 */       !BeanUtils.isSimpleProperty(returnType.getParameterType())));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
/* 203 */     if (returnValue != null) {
/* 204 */       String name = ModelFactory.getNameForReturnValue(returnValue, returnType);
/* 205 */       mavContainer.addAttribute(name, returnValue);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\method\annotation\ModelAttributeMethodProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */