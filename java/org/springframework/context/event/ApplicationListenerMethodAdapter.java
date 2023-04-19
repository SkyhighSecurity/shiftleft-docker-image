/*     */ package org.springframework.context.event;
/*     */ 
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.UndeclaredThrowableException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.context.ApplicationEvent;
/*     */ import org.springframework.context.PayloadApplicationEvent;
/*     */ import org.springframework.context.expression.AnnotatedElementKey;
/*     */ import org.springframework.core.BridgeMethodResolver;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.core.annotation.AnnotatedElementUtils;
/*     */ import org.springframework.core.annotation.Order;
/*     */ import org.springframework.expression.EvaluationContext;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.util.ReflectionUtils;
/*     */ import org.springframework.util.StringUtils;
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
/*     */ public class ApplicationListenerMethodAdapter
/*     */   implements GenericApplicationListener
/*     */ {
/*  62 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*     */   private final String beanName;
/*     */   
/*     */   private final Method method;
/*     */   
/*     */   private final Class<?> targetClass;
/*     */   
/*     */   private final Method bridgedMethod;
/*     */   
/*     */   private final List<ResolvableType> declaredEventTypes;
/*     */   
/*     */   private final String condition;
/*     */   
/*     */   private final int order;
/*     */   
/*     */   private final AnnotatedElementKey methodKey;
/*     */   
/*     */   private ApplicationContext applicationContext;
/*     */   
/*     */   private EventExpressionEvaluator evaluator;
/*     */ 
/*     */   
/*     */   public ApplicationListenerMethodAdapter(String beanName, Class<?> targetClass, Method method) {
/*  86 */     this.beanName = beanName;
/*  87 */     this.method = method;
/*  88 */     this.targetClass = targetClass;
/*  89 */     this.bridgedMethod = BridgeMethodResolver.findBridgedMethod(method);
/*     */     
/*  91 */     Method targetMethod = ClassUtils.getMostSpecificMethod(method, targetClass);
/*  92 */     EventListener ann = (EventListener)AnnotatedElementUtils.findMergedAnnotation(targetMethod, EventListener.class);
/*     */     
/*  94 */     this.declaredEventTypes = resolveDeclaredEventTypes(method, ann);
/*  95 */     this.condition = (ann != null) ? ann.condition() : null;
/*  96 */     this.order = resolveOrder(method);
/*     */     
/*  98 */     this.methodKey = new AnnotatedElementKey(method, targetClass);
/*     */   }
/*     */ 
/*     */   
/*     */   private List<ResolvableType> resolveDeclaredEventTypes(Method method, EventListener ann) {
/* 103 */     int count = (method.getParameterTypes()).length;
/* 104 */     if (count > 1) {
/* 105 */       throw new IllegalStateException("Maximum one parameter is allowed for event listener method: " + method);
/*     */     }
/*     */     
/* 108 */     if (ann != null && (ann.classes()).length > 0) {
/* 109 */       List<ResolvableType> types = new ArrayList<ResolvableType>((ann.classes()).length);
/* 110 */       for (Class<?> eventType : ann.classes()) {
/* 111 */         types.add(ResolvableType.forClass(eventType));
/*     */       }
/* 113 */       return types;
/*     */     } 
/*     */     
/* 116 */     if (count == 0) {
/* 117 */       throw new IllegalStateException("Event parameter is mandatory for event listener method: " + method);
/*     */     }
/*     */     
/* 120 */     return Collections.singletonList(ResolvableType.forMethodParameter(method, 0));
/*     */   }
/*     */ 
/*     */   
/*     */   private int resolveOrder(Method method) {
/* 125 */     Order ann = (Order)AnnotatedElementUtils.findMergedAnnotation(method, Order.class);
/* 126 */     return (ann != null) ? ann.value() : 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void init(ApplicationContext applicationContext, EventExpressionEvaluator evaluator) {
/* 133 */     this.applicationContext = applicationContext;
/* 134 */     this.evaluator = evaluator;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onApplicationEvent(ApplicationEvent event) {
/* 140 */     processEvent(event);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsEventType(ResolvableType eventType) {
/* 145 */     for (ResolvableType declaredEventType : this.declaredEventTypes) {
/* 146 */       if (declaredEventType.isAssignableFrom(eventType)) {
/* 147 */         return true;
/*     */       }
/* 149 */       if (PayloadApplicationEvent.class.isAssignableFrom(eventType.getRawClass())) {
/* 150 */         ResolvableType payloadType = eventType.as(PayloadApplicationEvent.class).getGeneric(new int[0]);
/* 151 */         if (declaredEventType.isAssignableFrom(payloadType)) {
/* 152 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/* 156 */     return eventType.hasUnresolvableGenerics();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsSourceType(Class<?> sourceType) {
/* 161 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getOrder() {
/* 166 */     return this.order;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void processEvent(ApplicationEvent event) {
/* 175 */     Object[] args = resolveArguments(event);
/* 176 */     if (shouldHandle(event, args)) {
/* 177 */       Object result = doInvoke(args);
/* 178 */       if (result != null) {
/* 179 */         handleResult(result);
/*     */       } else {
/*     */         
/* 182 */         this.logger.trace("No result object given - no result to handle");
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
/*     */   protected Object[] resolveArguments(ApplicationEvent event) {
/* 194 */     ResolvableType declaredEventType = getResolvableType(event);
/* 195 */     if (declaredEventType == null) {
/* 196 */       return null;
/*     */     }
/* 198 */     if ((this.method.getParameterTypes()).length == 0) {
/* 199 */       return new Object[0];
/*     */     }
/* 201 */     if (!ApplicationEvent.class.isAssignableFrom(declaredEventType.getRawClass()) && event instanceof PayloadApplicationEvent)
/*     */     {
/* 203 */       return new Object[] { ((PayloadApplicationEvent)event).getPayload() };
/*     */     }
/*     */     
/* 206 */     return new Object[] { event };
/*     */   }
/*     */ 
/*     */   
/*     */   protected void handleResult(Object result) {
/* 211 */     if (result.getClass().isArray()) {
/* 212 */       Object[] events = ObjectUtils.toObjectArray(result);
/* 213 */       for (Object event : events) {
/* 214 */         publishEvent(event);
/*     */       }
/*     */     }
/* 217 */     else if (result instanceof Collection) {
/* 218 */       Collection<?> events = (Collection)result;
/* 219 */       for (Object event : events) {
/* 220 */         publishEvent(event);
/*     */       }
/*     */     } else {
/*     */       
/* 224 */       publishEvent(result);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void publishEvent(Object event) {
/* 229 */     if (event != null) {
/* 230 */       Assert.notNull(this.applicationContext, "ApplicationContext must not be null");
/* 231 */       this.applicationContext.publishEvent(event);
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean shouldHandle(ApplicationEvent event, Object[] args) {
/* 236 */     if (args == null) {
/* 237 */       return false;
/*     */     }
/* 239 */     String condition = getCondition();
/* 240 */     if (StringUtils.hasText(condition)) {
/* 241 */       Assert.notNull(this.evaluator, "EventExpressionEvaluator must no be null");
/* 242 */       EvaluationContext evaluationContext = this.evaluator.createEvaluationContext(event, this.targetClass, this.method, args, (BeanFactory)this.applicationContext);
/*     */       
/* 244 */       return this.evaluator.condition(condition, this.methodKey, evaluationContext);
/*     */     } 
/* 246 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object doInvoke(Object... args) {
/* 253 */     Object bean = getTargetBean();
/* 254 */     ReflectionUtils.makeAccessible(this.bridgedMethod);
/*     */     try {
/* 256 */       return this.bridgedMethod.invoke(bean, args);
/*     */     }
/* 258 */     catch (IllegalArgumentException ex) {
/* 259 */       assertTargetBean(this.bridgedMethod, bean, args);
/* 260 */       throw new IllegalStateException(getInvocationErrorMessage(bean, ex.getMessage(), args), ex);
/*     */     }
/* 262 */     catch (IllegalAccessException ex) {
/* 263 */       throw new IllegalStateException(getInvocationErrorMessage(bean, ex.getMessage(), args), ex);
/*     */     }
/* 265 */     catch (InvocationTargetException ex) {
/*     */       
/* 267 */       Throwable targetException = ex.getTargetException();
/* 268 */       if (targetException instanceof RuntimeException) {
/* 269 */         throw (RuntimeException)targetException;
/*     */       }
/*     */       
/* 272 */       String msg = getInvocationErrorMessage(bean, "Failed to invoke event listener method", args);
/* 273 */       throw new UndeclaredThrowableException(targetException, msg);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object getTargetBean() {
/* 282 */     Assert.notNull(this.applicationContext, "ApplicationContext must no be null");
/* 283 */     return this.applicationContext.getBean(this.beanName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getCondition() {
/* 293 */     return this.condition;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getDetailedErrorMessage(Object bean, String message) {
/* 302 */     StringBuilder sb = (new StringBuilder(message)).append("\n");
/* 303 */     sb.append("HandlerMethod details: \n");
/* 304 */     sb.append("Bean [").append(bean.getClass().getName()).append("]\n");
/* 305 */     sb.append("Method [").append(this.bridgedMethod.toGenericString()).append("]\n");
/* 306 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void assertTargetBean(Method method, Object targetBean, Object[] args) {
/* 317 */     Class<?> methodDeclaringClass = method.getDeclaringClass();
/* 318 */     Class<?> targetBeanClass = targetBean.getClass();
/* 319 */     if (!methodDeclaringClass.isAssignableFrom(targetBeanClass)) {
/*     */ 
/*     */       
/* 322 */       String msg = "The event listener method class '" + methodDeclaringClass.getName() + "' is not an instance of the actual bean class '" + targetBeanClass.getName() + "'. If the bean requires proxying (e.g. due to @Transactional), please use class-based proxying.";
/*     */       
/* 324 */       throw new IllegalStateException(getInvocationErrorMessage(targetBean, msg, args));
/*     */     } 
/*     */   }
/*     */   
/*     */   private String getInvocationErrorMessage(Object bean, String message, Object[] resolvedArgs) {
/* 329 */     StringBuilder sb = new StringBuilder(getDetailedErrorMessage(bean, message));
/* 330 */     sb.append("Resolved arguments: \n");
/* 331 */     for (int i = 0; i < resolvedArgs.length; i++) {
/* 332 */       sb.append("[").append(i).append("] ");
/* 333 */       if (resolvedArgs[i] == null) {
/* 334 */         sb.append("[null] \n");
/*     */       } else {
/*     */         
/* 337 */         sb.append("[type=").append(resolvedArgs[i].getClass().getName()).append("] ");
/* 338 */         sb.append("[value=").append(resolvedArgs[i]).append("]\n");
/*     */       } 
/*     */     } 
/* 341 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   private ResolvableType getResolvableType(ApplicationEvent event) {
/* 346 */     ResolvableType payloadType = null;
/* 347 */     if (event instanceof PayloadApplicationEvent) {
/* 348 */       PayloadApplicationEvent<?> payloadEvent = (PayloadApplicationEvent)event;
/* 349 */       payloadType = payloadEvent.getResolvableType().as(PayloadApplicationEvent.class).getGeneric(new int[0]);
/*     */     } 
/* 351 */     for (ResolvableType declaredEventType : this.declaredEventTypes) {
/* 352 */       if (!ApplicationEvent.class.isAssignableFrom(declaredEventType.getRawClass()) && payloadType != null && 
/* 353 */         declaredEventType.isAssignableFrom(payloadType)) {
/* 354 */         return declaredEventType;
/*     */       }
/*     */       
/* 357 */       if (declaredEventType.getRawClass().isInstance(event)) {
/* 358 */         return declaredEventType;
/*     */       }
/*     */     } 
/* 361 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 367 */     return this.method.toGenericString();
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\event\ApplicationListenerMethodAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */