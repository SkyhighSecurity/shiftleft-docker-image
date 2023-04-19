/*     */ package org.springframework.web.method.support;
/*     */ 
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Arrays;
/*     */ import org.springframework.core.DefaultParameterNameDiscoverer;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.core.ParameterNameDiscoverer;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ReflectionUtils;
/*     */ import org.springframework.web.bind.support.WebDataBinderFactory;
/*     */ import org.springframework.web.context.request.NativeWebRequest;
/*     */ import org.springframework.web.method.HandlerMethod;
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
/*     */ public class InvocableHandlerMethod
/*     */   extends HandlerMethod
/*     */ {
/*     */   private WebDataBinderFactory dataBinderFactory;
/*  52 */   private HandlerMethodArgumentResolverComposite argumentResolvers = new HandlerMethodArgumentResolverComposite();
/*     */   
/*  54 */   private ParameterNameDiscoverer parameterNameDiscoverer = (ParameterNameDiscoverer)new DefaultParameterNameDiscoverer();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InvocableHandlerMethod(HandlerMethod handlerMethod) {
/*  61 */     super(handlerMethod);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InvocableHandlerMethod(Object bean, Method method) {
/*  68 */     super(bean, method);
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
/*     */   public InvocableHandlerMethod(Object bean, String methodName, Class<?>... parameterTypes) throws NoSuchMethodException {
/*  81 */     super(bean, methodName, parameterTypes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDataBinderFactory(WebDataBinderFactory dataBinderFactory) {
/*  91 */     this.dataBinderFactory = dataBinderFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHandlerMethodArgumentResolvers(HandlerMethodArgumentResolverComposite argumentResolvers) {
/*  98 */     this.argumentResolvers = argumentResolvers;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setParameterNameDiscoverer(ParameterNameDiscoverer parameterNameDiscoverer) {
/* 107 */     this.parameterNameDiscoverer = parameterNameDiscoverer;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object invokeForRequest(NativeWebRequest request, ModelAndViewContainer mavContainer, Object... providedArgs) throws Exception {
/* 128 */     Object[] args = getMethodArgumentValues(request, mavContainer, providedArgs);
/* 129 */     if (this.logger.isTraceEnabled()) {
/* 130 */       this.logger.trace("Invoking '" + ClassUtils.getQualifiedMethodName(getMethod(), getBeanType()) + "' with arguments " + 
/* 131 */           Arrays.toString(args));
/*     */     }
/* 133 */     Object returnValue = doInvoke(args);
/* 134 */     if (this.logger.isTraceEnabled()) {
/* 135 */       this.logger.trace("Method [" + ClassUtils.getQualifiedMethodName(getMethod(), getBeanType()) + "] returned [" + returnValue + "]");
/*     */     }
/*     */     
/* 138 */     return returnValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Object[] getMethodArgumentValues(NativeWebRequest request, ModelAndViewContainer mavContainer, Object... providedArgs) throws Exception {
/* 147 */     MethodParameter[] parameters = getMethodParameters();
/* 148 */     Object[] args = new Object[parameters.length];
/* 149 */     for (int i = 0; i < parameters.length; i++) {
/* 150 */       MethodParameter parameter = parameters[i];
/* 151 */       parameter.initParameterNameDiscovery(this.parameterNameDiscoverer);
/* 152 */       args[i] = resolveProvidedArgument(parameter, providedArgs);
/* 153 */       if (args[i] == null)
/*     */       {
/*     */         
/* 156 */         if (this.argumentResolvers.supportsParameter(parameter)) {
/*     */           try {
/* 158 */             args[i] = this.argumentResolvers.resolveArgument(parameter, mavContainer, request, this.dataBinderFactory);
/*     */ 
/*     */           
/*     */           }
/* 162 */           catch (Exception ex) {
/* 163 */             if (this.logger.isDebugEnabled()) {
/* 164 */               this.logger.debug(getArgumentResolutionErrorMessage("Failed to resolve", i), ex);
/*     */             }
/* 166 */             throw ex;
/*     */           }
/*     */         
/* 169 */         } else if (args[i] == null) {
/* 170 */           throw new IllegalStateException("Could not resolve method parameter at index " + parameter
/* 171 */               .getParameterIndex() + " in " + parameter.getMethod().toGenericString() + ": " + 
/* 172 */               getArgumentResolutionErrorMessage("No suitable resolver for", i));
/*     */         }  } 
/*     */     } 
/* 175 */     return args;
/*     */   }
/*     */   
/*     */   private String getArgumentResolutionErrorMessage(String text, int index) {
/* 179 */     Class<?> paramType = getMethodParameters()[index].getParameterType();
/* 180 */     return text + " argument " + index + " of type '" + paramType.getName() + "'";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Object resolveProvidedArgument(MethodParameter parameter, Object... providedArgs) {
/* 187 */     if (providedArgs == null) {
/* 188 */       return null;
/*     */     }
/* 190 */     for (Object providedArg : providedArgs) {
/* 191 */       if (parameter.getParameterType().isInstance(providedArg)) {
/* 192 */         return providedArg;
/*     */       }
/*     */     } 
/* 195 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object doInvoke(Object... args) throws Exception {
/* 203 */     ReflectionUtils.makeAccessible(getBridgedMethod());
/*     */     try {
/* 205 */       return getBridgedMethod().invoke(getBean(), args);
/*     */     }
/* 207 */     catch (IllegalArgumentException ex) {
/* 208 */       assertTargetBean(getBridgedMethod(), getBean(), args);
/* 209 */       String text = (ex.getMessage() != null) ? ex.getMessage() : "Illegal argument";
/* 210 */       throw new IllegalStateException(getInvocationErrorMessage(text, args), ex);
/*     */     }
/* 212 */     catch (InvocationTargetException ex) {
/*     */       
/* 214 */       Throwable targetException = ex.getTargetException();
/* 215 */       if (targetException instanceof RuntimeException) {
/* 216 */         throw (RuntimeException)targetException;
/*     */       }
/* 218 */       if (targetException instanceof Error) {
/* 219 */         throw (Error)targetException;
/*     */       }
/* 221 */       if (targetException instanceof Exception) {
/* 222 */         throw (Exception)targetException;
/*     */       }
/*     */       
/* 225 */       String text = getInvocationErrorMessage("Failed to invoke handler method", args);
/* 226 */       throw new IllegalStateException(text, targetException);
/*     */     } 
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
/*     */   private void assertTargetBean(Method method, Object targetBean, Object[] args) {
/* 239 */     Class<?> methodDeclaringClass = method.getDeclaringClass();
/* 240 */     Class<?> targetBeanClass = targetBean.getClass();
/* 241 */     if (!methodDeclaringClass.isAssignableFrom(targetBeanClass)) {
/*     */ 
/*     */       
/* 244 */       String text = "The mapped handler method class '" + methodDeclaringClass.getName() + "' is not an instance of the actual controller bean class '" + targetBeanClass.getName() + "'. If the controller requires proxying (e.g. due to @Transactional), please use class-based proxying.";
/*     */       
/* 246 */       throw new IllegalStateException(getInvocationErrorMessage(text, args));
/*     */     } 
/*     */   }
/*     */   
/*     */   private String getInvocationErrorMessage(String text, Object[] resolvedArgs) {
/* 251 */     StringBuilder sb = new StringBuilder(getDetailedErrorMessage(text));
/* 252 */     sb.append("Resolved arguments: \n");
/* 253 */     for (int i = 0; i < resolvedArgs.length; i++) {
/* 254 */       sb.append("[").append(i).append("] ");
/* 255 */       if (resolvedArgs[i] == null) {
/* 256 */         sb.append("[null] \n");
/*     */       } else {
/*     */         
/* 259 */         sb.append("[type=").append(resolvedArgs[i].getClass().getName()).append("] ");
/* 260 */         sb.append("[value=").append(resolvedArgs[i]).append("]\n");
/*     */       } 
/*     */     } 
/* 263 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getDetailedErrorMessage(String text) {
/* 271 */     StringBuilder sb = (new StringBuilder(text)).append("\n");
/* 272 */     sb.append("HandlerMethod details: \n");
/* 273 */     sb.append("Controller [").append(getBeanType().getName()).append("]\n");
/* 274 */     sb.append("Method [").append(getBridgedMethod().toGenericString()).append("]\n");
/* 275 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\method\support\InvocableHandlerMethod.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */