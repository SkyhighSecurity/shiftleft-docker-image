/*     */ package org.springframework.aop.framework;
/*     */ 
/*     */ import java.lang.reflect.AccessibleObject;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.aopalliance.intercept.MethodInterceptor;
/*     */ import org.aopalliance.intercept.MethodInvocation;
/*     */ import org.springframework.aop.ProxyMethodInvocation;
/*     */ import org.springframework.aop.support.AopUtils;
/*     */ import org.springframework.core.BridgeMethodResolver;
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
/*     */ public class ReflectiveMethodInvocation
/*     */   implements ProxyMethodInvocation, Cloneable
/*     */ {
/*     */   protected final Object proxy;
/*     */   protected final Object target;
/*     */   protected final Method method;
/*     */   protected Object[] arguments;
/*     */   private final Class<?> targetClass;
/*     */   private Map<String, Object> userAttributes;
/*     */   protected final List<?> interceptorsAndDynamicMethodMatchers;
/*  88 */   private int currentInterceptorIndex = -1;
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
/*     */   protected ReflectiveMethodInvocation(Object proxy, Object target, Method method, Object[] arguments, Class<?> targetClass, List<Object> interceptorsAndDynamicMethodMatchers) {
/* 108 */     this.proxy = proxy;
/* 109 */     this.target = target;
/* 110 */     this.targetClass = targetClass;
/* 111 */     this.method = BridgeMethodResolver.findBridgedMethod(method);
/* 112 */     this.arguments = AopProxyUtils.adaptArgumentsIfNecessary(method, arguments);
/* 113 */     this.interceptorsAndDynamicMethodMatchers = interceptorsAndDynamicMethodMatchers;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final Object getProxy() {
/* 119 */     return this.proxy;
/*     */   }
/*     */ 
/*     */   
/*     */   public final Object getThis() {
/* 124 */     return this.target;
/*     */   }
/*     */ 
/*     */   
/*     */   public final AccessibleObject getStaticPart() {
/* 129 */     return this.method;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Method getMethod() {
/* 139 */     return this.method;
/*     */   }
/*     */ 
/*     */   
/*     */   public final Object[] getArguments() {
/* 144 */     return (this.arguments != null) ? this.arguments : new Object[0];
/*     */   }
/*     */ 
/*     */   
/*     */   public void setArguments(Object... arguments) {
/* 149 */     this.arguments = arguments;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object proceed() throws Throwable {
/* 156 */     if (this.currentInterceptorIndex == this.interceptorsAndDynamicMethodMatchers.size() - 1) {
/* 157 */       return invokeJoinpoint();
/*     */     }
/*     */ 
/*     */     
/* 161 */     Object interceptorOrInterceptionAdvice = this.interceptorsAndDynamicMethodMatchers.get(++this.currentInterceptorIndex);
/* 162 */     if (interceptorOrInterceptionAdvice instanceof InterceptorAndDynamicMethodMatcher) {
/*     */ 
/*     */       
/* 165 */       InterceptorAndDynamicMethodMatcher dm = (InterceptorAndDynamicMethodMatcher)interceptorOrInterceptionAdvice;
/*     */       
/* 167 */       if (dm.methodMatcher.matches(this.method, this.targetClass, this.arguments)) {
/* 168 */         return dm.interceptor.invoke((MethodInvocation)this);
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 173 */       return proceed();
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 179 */     return ((MethodInterceptor)interceptorOrInterceptionAdvice).invoke((MethodInvocation)this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object invokeJoinpoint() throws Throwable {
/* 190 */     return AopUtils.invokeJoinpointUsingReflection(this.target, this.method, this.arguments);
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
/*     */   public MethodInvocation invocableClone() {
/* 204 */     Object[] cloneArguments = null;
/* 205 */     if (this.arguments != null) {
/*     */       
/* 207 */       cloneArguments = new Object[this.arguments.length];
/* 208 */       System.arraycopy(this.arguments, 0, cloneArguments, 0, this.arguments.length);
/*     */     } 
/* 210 */     return invocableClone(cloneArguments);
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
/*     */   public MethodInvocation invocableClone(Object... arguments) {
/* 225 */     if (this.userAttributes == null) {
/* 226 */       this.userAttributes = new HashMap<String, Object>();
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/* 231 */       ReflectiveMethodInvocation clone = (ReflectiveMethodInvocation)clone();
/* 232 */       clone.arguments = arguments;
/* 233 */       return (MethodInvocation)clone;
/*     */     }
/* 235 */     catch (CloneNotSupportedException ex) {
/* 236 */       throw new IllegalStateException("Should be able to clone object of type [" + 
/* 237 */           getClass() + "]: " + ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUserAttribute(String key, Object value) {
/* 244 */     if (value != null) {
/* 245 */       if (this.userAttributes == null) {
/* 246 */         this.userAttributes = new HashMap<String, Object>();
/*     */       }
/* 248 */       this.userAttributes.put(key, value);
/*     */     
/*     */     }
/* 251 */     else if (this.userAttributes != null) {
/* 252 */       this.userAttributes.remove(key);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getUserAttribute(String key) {
/* 259 */     return (this.userAttributes != null) ? this.userAttributes.get(key) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, Object> getUserAttributes() {
/* 270 */     if (this.userAttributes == null) {
/* 271 */       this.userAttributes = new HashMap<String, Object>();
/*     */     }
/* 273 */     return this.userAttributes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 280 */     StringBuilder sb = new StringBuilder("ReflectiveMethodInvocation: ");
/* 281 */     sb.append(this.method).append("; ");
/* 282 */     if (this.target == null) {
/* 283 */       sb.append("target is null");
/*     */     } else {
/*     */       
/* 286 */       sb.append("target is of class [").append(this.target.getClass().getName()).append(']');
/*     */     } 
/* 288 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\framework\ReflectiveMethodInvocation.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */