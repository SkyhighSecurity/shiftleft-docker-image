/*     */ package org.springframework.web.method;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.core.BridgeMethodResolver;
/*     */ import org.springframework.core.GenericTypeResolver;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.core.annotation.AnnotatedElementUtils;
/*     */ import org.springframework.core.annotation.SynthesizingMethodParameter;
/*     */ import org.springframework.http.HttpStatus;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.web.bind.annotation.ResponseStatus;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HandlerMethod
/*     */ {
/*  56 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */ 
/*     */   
/*     */   private final Object bean;
/*     */ 
/*     */   
/*     */   private final BeanFactory beanFactory;
/*     */ 
/*     */   
/*     */   private final Class<?> beanType;
/*     */   
/*     */   private final Method method;
/*     */   
/*     */   private final Method bridgedMethod;
/*     */   
/*     */   private final MethodParameter[] parameters;
/*     */   
/*     */   private HttpStatus responseStatus;
/*     */   
/*     */   private String responseStatusReason;
/*     */   
/*     */   private HandlerMethod resolvedFromHandlerMethod;
/*     */ 
/*     */   
/*     */   public HandlerMethod(Object bean, Method method) {
/*  81 */     Assert.notNull(bean, "Bean is required");
/*  82 */     Assert.notNull(method, "Method is required");
/*  83 */     this.bean = bean;
/*  84 */     this.beanFactory = null;
/*  85 */     this.beanType = ClassUtils.getUserClass(bean);
/*  86 */     this.method = method;
/*  87 */     this.bridgedMethod = BridgeMethodResolver.findBridgedMethod(method);
/*  88 */     this.parameters = initMethodParameters();
/*  89 */     evaluateResponseStatus();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HandlerMethod(Object bean, String methodName, Class<?>... parameterTypes) throws NoSuchMethodException {
/*  97 */     Assert.notNull(bean, "Bean is required");
/*  98 */     Assert.notNull(methodName, "Method name is required");
/*  99 */     this.bean = bean;
/* 100 */     this.beanFactory = null;
/* 101 */     this.beanType = ClassUtils.getUserClass(bean);
/* 102 */     this.method = bean.getClass().getMethod(methodName, parameterTypes);
/* 103 */     this.bridgedMethod = BridgeMethodResolver.findBridgedMethod(this.method);
/* 104 */     this.parameters = initMethodParameters();
/* 105 */     evaluateResponseStatus();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HandlerMethod(String beanName, BeanFactory beanFactory, Method method) {
/* 114 */     Assert.hasText(beanName, "Bean name is required");
/* 115 */     Assert.notNull(beanFactory, "BeanFactory is required");
/* 116 */     Assert.notNull(method, "Method is required");
/* 117 */     this.bean = beanName;
/* 118 */     this.beanFactory = beanFactory;
/* 119 */     this.beanType = ClassUtils.getUserClass(beanFactory.getType(beanName));
/* 120 */     this.method = method;
/* 121 */     this.bridgedMethod = BridgeMethodResolver.findBridgedMethod(method);
/* 122 */     this.parameters = initMethodParameters();
/* 123 */     evaluateResponseStatus();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected HandlerMethod(HandlerMethod handlerMethod) {
/* 130 */     Assert.notNull(handlerMethod, "HandlerMethod is required");
/* 131 */     this.bean = handlerMethod.bean;
/* 132 */     this.beanFactory = handlerMethod.beanFactory;
/* 133 */     this.beanType = handlerMethod.beanType;
/* 134 */     this.method = handlerMethod.method;
/* 135 */     this.bridgedMethod = handlerMethod.bridgedMethod;
/* 136 */     this.parameters = handlerMethod.parameters;
/* 137 */     this.responseStatus = handlerMethod.responseStatus;
/* 138 */     this.responseStatusReason = handlerMethod.responseStatusReason;
/* 139 */     this.resolvedFromHandlerMethod = handlerMethod.resolvedFromHandlerMethod;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private HandlerMethod(HandlerMethod handlerMethod, Object handler) {
/* 146 */     Assert.notNull(handlerMethod, "HandlerMethod is required");
/* 147 */     Assert.notNull(handler, "Handler object is required");
/* 148 */     this.bean = handler;
/* 149 */     this.beanFactory = handlerMethod.beanFactory;
/* 150 */     this.beanType = handlerMethod.beanType;
/* 151 */     this.method = handlerMethod.method;
/* 152 */     this.bridgedMethod = handlerMethod.bridgedMethod;
/* 153 */     this.parameters = handlerMethod.parameters;
/* 154 */     this.responseStatus = handlerMethod.responseStatus;
/* 155 */     this.responseStatusReason = handlerMethod.responseStatusReason;
/* 156 */     this.resolvedFromHandlerMethod = handlerMethod;
/*     */   }
/*     */ 
/*     */   
/*     */   private MethodParameter[] initMethodParameters() {
/* 161 */     int count = (this.bridgedMethod.getParameterTypes()).length;
/* 162 */     MethodParameter[] result = new MethodParameter[count];
/* 163 */     for (int i = 0; i < count; i++) {
/* 164 */       HandlerMethodParameter parameter = new HandlerMethodParameter(i);
/* 165 */       GenericTypeResolver.resolveParameterType((MethodParameter)parameter, this.beanType);
/* 166 */       result[i] = (MethodParameter)parameter;
/*     */     } 
/* 168 */     return result;
/*     */   }
/*     */   
/*     */   private void evaluateResponseStatus() {
/* 172 */     ResponseStatus annotation = getMethodAnnotation(ResponseStatus.class);
/* 173 */     if (annotation == null) {
/* 174 */       annotation = (ResponseStatus)AnnotatedElementUtils.findMergedAnnotation(getBeanType(), ResponseStatus.class);
/*     */     }
/* 176 */     if (annotation != null) {
/* 177 */       this.responseStatus = annotation.code();
/* 178 */       this.responseStatusReason = annotation.reason();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getBean() {
/* 187 */     return this.bean;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Method getMethod() {
/* 194 */     return this.method;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?> getBeanType() {
/* 203 */     return this.beanType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Method getBridgedMethod() {
/* 211 */     return this.bridgedMethod;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MethodParameter[] getMethodParameters() {
/* 218 */     return this.parameters;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected HttpStatus getResponseStatus() {
/* 227 */     return this.responseStatus;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getResponseStatusReason() {
/* 236 */     return this.responseStatusReason;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MethodParameter getReturnType() {
/* 243 */     return (MethodParameter)new HandlerMethodParameter(-1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MethodParameter getReturnValueType(Object returnValue) {
/* 250 */     return (MethodParameter)new ReturnValueMethodParameter(returnValue);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isVoid() {
/* 257 */     return void.class.equals(getReturnType().getParameterType());
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
/*     */   public <A extends java.lang.annotation.Annotation> A getMethodAnnotation(Class<A> annotationType) {
/* 270 */     return (A)AnnotatedElementUtils.findMergedAnnotation(this.method, annotationType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <A extends java.lang.annotation.Annotation> boolean hasMethodAnnotation(Class<A> annotationType) {
/* 280 */     return AnnotatedElementUtils.hasAnnotation(this.method, annotationType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HandlerMethod getResolvedFromHandlerMethod() {
/* 288 */     return this.resolvedFromHandlerMethod;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HandlerMethod createWithResolvedBean() {
/* 296 */     Object handler = this.bean;
/* 297 */     if (this.bean instanceof String) {
/* 298 */       String beanName = (String)this.bean;
/* 299 */       handler = this.beanFactory.getBean(beanName);
/*     */     } 
/* 301 */     return new HandlerMethod(this, handler);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getShortLogMessage() {
/* 309 */     int args = (this.method.getParameterTypes()).length;
/* 310 */     return getBeanType().getName() + "#" + this.method.getName() + "[" + args + " args]";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 316 */     if (this == other) {
/* 317 */       return true;
/*     */     }
/* 319 */     if (!(other instanceof HandlerMethod)) {
/* 320 */       return false;
/*     */     }
/* 322 */     HandlerMethod otherMethod = (HandlerMethod)other;
/* 323 */     return (this.bean.equals(otherMethod.bean) && this.method.equals(otherMethod.method));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 328 */     return this.bean.hashCode() * 31 + this.method.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 333 */     return this.method.toGenericString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected class HandlerMethodParameter
/*     */     extends SynthesizingMethodParameter
/*     */   {
/*     */     public HandlerMethodParameter(int index) {
/* 343 */       super(HandlerMethod.this.bridgedMethod, index);
/*     */     }
/*     */     
/*     */     protected HandlerMethodParameter(HandlerMethodParameter original) {
/* 347 */       super(original);
/*     */     }
/*     */ 
/*     */     
/*     */     public Class<?> getContainingClass() {
/* 352 */       return HandlerMethod.this.getBeanType();
/*     */     }
/*     */ 
/*     */     
/*     */     public <T extends java.lang.annotation.Annotation> T getMethodAnnotation(Class<T> annotationType) {
/* 357 */       return HandlerMethod.this.getMethodAnnotation(annotationType);
/*     */     }
/*     */ 
/*     */     
/*     */     public <T extends java.lang.annotation.Annotation> boolean hasMethodAnnotation(Class<T> annotationType) {
/* 362 */       return HandlerMethod.this.hasMethodAnnotation(annotationType);
/*     */     }
/*     */ 
/*     */     
/*     */     public HandlerMethodParameter clone() {
/* 367 */       return new HandlerMethodParameter(this);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class ReturnValueMethodParameter
/*     */     extends HandlerMethodParameter
/*     */   {
/*     */     private final Object returnValue;
/*     */ 
/*     */     
/*     */     public ReturnValueMethodParameter(Object returnValue) {
/* 380 */       super(-1);
/* 381 */       this.returnValue = returnValue;
/*     */     }
/*     */     
/*     */     protected ReturnValueMethodParameter(ReturnValueMethodParameter original) {
/* 385 */       super(original);
/* 386 */       this.returnValue = original.returnValue;
/*     */     }
/*     */ 
/*     */     
/*     */     public Class<?> getParameterType() {
/* 391 */       return (this.returnValue != null) ? this.returnValue.getClass() : super.getParameterType();
/*     */     }
/*     */ 
/*     */     
/*     */     public ReturnValueMethodParameter clone() {
/* 396 */       return new ReturnValueMethodParameter(this);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\method\HandlerMethod.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */