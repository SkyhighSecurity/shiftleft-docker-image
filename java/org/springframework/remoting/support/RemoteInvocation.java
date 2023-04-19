/*     */ package org.springframework.remoting.support;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.aopalliance.intercept.MethodInvocation;
/*     */ import org.springframework.util.ClassUtils;
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
/*     */ public class RemoteInvocation
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 6876024250231820554L;
/*     */   private String methodName;
/*     */   private Class<?>[] parameterTypes;
/*     */   private Object[] arguments;
/*     */   private Map<String, Serializable> attributes;
/*     */   
/*     */   public RemoteInvocation(MethodInvocation methodInvocation) {
/*  69 */     this.methodName = methodInvocation.getMethod().getName();
/*  70 */     this.parameterTypes = methodInvocation.getMethod().getParameterTypes();
/*  71 */     this.arguments = methodInvocation.getArguments();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RemoteInvocation(String methodName, Class<?>[] parameterTypes, Object[] arguments) {
/*  81 */     this.methodName = methodName;
/*  82 */     this.parameterTypes = parameterTypes;
/*  83 */     this.arguments = arguments;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RemoteInvocation() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMethodName(String methodName) {
/*  99 */     this.methodName = methodName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMethodName() {
/* 106 */     return this.methodName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setParameterTypes(Class<?>[] parameterTypes) {
/* 114 */     this.parameterTypes = parameterTypes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?>[] getParameterTypes() {
/* 121 */     return this.parameterTypes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setArguments(Object[] arguments) {
/* 129 */     this.arguments = arguments;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object[] getArguments() {
/* 136 */     return this.arguments;
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
/*     */   public void addAttribute(String key, Serializable value) throws IllegalStateException {
/* 152 */     if (this.attributes == null) {
/* 153 */       this.attributes = new HashMap<String, Serializable>();
/*     */     }
/* 155 */     if (this.attributes.containsKey(key)) {
/* 156 */       throw new IllegalStateException("There is already an attribute with key '" + key + "' bound");
/*     */     }
/* 158 */     this.attributes.put(key, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Serializable getAttribute(String key) {
/* 169 */     if (this.attributes == null) {
/* 170 */       return null;
/*     */     }
/* 172 */     return this.attributes.get(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAttributes(Map<String, Serializable> attributes) {
/* 183 */     this.attributes = attributes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, Serializable> getAttributes() {
/* 194 */     return this.attributes;
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
/*     */   public Object invoke(Object targetObject) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
/* 211 */     Method method = targetObject.getClass().getMethod(this.methodName, this.parameterTypes);
/* 212 */     return method.invoke(targetObject, this.arguments);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 218 */     return "RemoteInvocation: method name '" + this.methodName + "'; parameter types " + 
/* 219 */       ClassUtils.classNamesToString(this.parameterTypes);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\remoting\support\RemoteInvocation.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */