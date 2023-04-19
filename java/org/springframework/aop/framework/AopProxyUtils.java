/*     */ package org.springframework.aop.framework;
/*     */ 
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.util.Arrays;
/*     */ import org.springframework.aop.SpringProxy;
/*     */ import org.springframework.aop.TargetClassAware;
/*     */ import org.springframework.aop.TargetSource;
/*     */ import org.springframework.aop.support.AopUtils;
/*     */ import org.springframework.aop.target.SingletonTargetSource;
/*     */ import org.springframework.core.DecoratingProxy;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ObjectUtils;
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
/*     */ public abstract class AopProxyUtils
/*     */ {
/*     */   public static Object getSingletonTarget(Object candidate) {
/*  56 */     if (candidate instanceof Advised) {
/*  57 */       TargetSource targetSource = ((Advised)candidate).getTargetSource();
/*  58 */       if (targetSource instanceof SingletonTargetSource) {
/*  59 */         return ((SingletonTargetSource)targetSource).getTarget();
/*     */       }
/*     */     } 
/*  62 */     return null;
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
/*     */   public static Class<?> ultimateTargetClass(Object candidate) {
/*  76 */     Assert.notNull(candidate, "Candidate object must not be null");
/*  77 */     Object current = candidate;
/*  78 */     Class<?> result = null;
/*  79 */     while (current instanceof TargetClassAware) {
/*  80 */       result = ((TargetClassAware)current).getTargetClass();
/*  81 */       current = getSingletonTarget(current);
/*     */     } 
/*  83 */     if (result == null) {
/*  84 */       result = AopUtils.isCglibProxy(candidate) ? candidate.getClass().getSuperclass() : candidate.getClass();
/*     */     }
/*  86 */     return result;
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
/*     */   public static Class<?>[] completeProxiedInterfaces(AdvisedSupport advised) {
/* 100 */     return completeProxiedInterfaces(advised, false);
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
/*     */   static Class<?>[] completeProxiedInterfaces(AdvisedSupport advised, boolean decoratingProxy) {
/* 117 */     Class<?>[] specifiedInterfaces = advised.getProxiedInterfaces();
/* 118 */     if (specifiedInterfaces.length == 0) {
/*     */       
/* 120 */       Class<?> targetClass = advised.getTargetClass();
/* 121 */       if (targetClass != null) {
/* 122 */         if (targetClass.isInterface()) {
/* 123 */           advised.setInterfaces(new Class[] { targetClass });
/*     */         }
/* 125 */         else if (Proxy.isProxyClass(targetClass)) {
/* 126 */           advised.setInterfaces(targetClass.getInterfaces());
/*     */         } 
/* 128 */         specifiedInterfaces = advised.getProxiedInterfaces();
/*     */       } 
/*     */     } 
/* 131 */     boolean addSpringProxy = !advised.isInterfaceProxied(SpringProxy.class);
/* 132 */     boolean addAdvised = (!advised.isOpaque() && !advised.isInterfaceProxied(Advised.class));
/* 133 */     boolean addDecoratingProxy = (decoratingProxy && !advised.isInterfaceProxied(DecoratingProxy.class));
/* 134 */     int nonUserIfcCount = 0;
/* 135 */     if (addSpringProxy) {
/* 136 */       nonUserIfcCount++;
/*     */     }
/* 138 */     if (addAdvised) {
/* 139 */       nonUserIfcCount++;
/*     */     }
/* 141 */     if (addDecoratingProxy) {
/* 142 */       nonUserIfcCount++;
/*     */     }
/* 144 */     Class<?>[] proxiedInterfaces = new Class[specifiedInterfaces.length + nonUserIfcCount];
/* 145 */     System.arraycopy(specifiedInterfaces, 0, proxiedInterfaces, 0, specifiedInterfaces.length);
/* 146 */     int index = specifiedInterfaces.length;
/* 147 */     if (addSpringProxy) {
/* 148 */       proxiedInterfaces[index] = SpringProxy.class;
/* 149 */       index++;
/*     */     } 
/* 151 */     if (addAdvised) {
/* 152 */       proxiedInterfaces[index] = Advised.class;
/* 153 */       index++;
/*     */     } 
/* 155 */     if (addDecoratingProxy) {
/* 156 */       proxiedInterfaces[index] = DecoratingProxy.class;
/*     */     }
/* 158 */     return proxiedInterfaces;
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
/*     */   public static Class<?>[] proxiedUserInterfaces(Object proxy) {
/* 170 */     Class<?>[] proxyInterfaces = proxy.getClass().getInterfaces();
/* 171 */     int nonUserIfcCount = 0;
/* 172 */     if (proxy instanceof SpringProxy) {
/* 173 */       nonUserIfcCount++;
/*     */     }
/* 175 */     if (proxy instanceof Advised) {
/* 176 */       nonUserIfcCount++;
/*     */     }
/* 178 */     if (proxy instanceof DecoratingProxy) {
/* 179 */       nonUserIfcCount++;
/*     */     }
/* 181 */     Class<?>[] userInterfaces = new Class[proxyInterfaces.length - nonUserIfcCount];
/* 182 */     System.arraycopy(proxyInterfaces, 0, userInterfaces, 0, userInterfaces.length);
/* 183 */     Assert.notEmpty((Object[])userInterfaces, "JDK proxy must implement one or more interfaces");
/* 184 */     return userInterfaces;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean equalsInProxy(AdvisedSupport a, AdvisedSupport b) {
/* 193 */     return (a == b || (
/* 194 */       equalsProxiedInterfaces(a, b) && equalsAdvisors(a, b) && a.getTargetSource().equals(b.getTargetSource())));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean equalsProxiedInterfaces(AdvisedSupport a, AdvisedSupport b) {
/* 201 */     return Arrays.equals((Object[])a.getProxiedInterfaces(), (Object[])b.getProxiedInterfaces());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean equalsAdvisors(AdvisedSupport a, AdvisedSupport b) {
/* 208 */     return Arrays.equals((Object[])a.getAdvisors(), (Object[])b.getAdvisors());
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
/*     */   static Object[] adaptArgumentsIfNecessary(Method method, Object... arguments) {
/* 222 */     if (method.isVarArgs() && !ObjectUtils.isEmpty(arguments)) {
/* 223 */       Class<?>[] paramTypes = method.getParameterTypes();
/* 224 */       if (paramTypes.length == arguments.length) {
/* 225 */         int varargIndex = paramTypes.length - 1;
/* 226 */         Class<?> varargType = paramTypes[varargIndex];
/* 227 */         if (varargType.isArray()) {
/* 228 */           Object varargArray = arguments[varargIndex];
/* 229 */           if (varargArray instanceof Object[] && !varargType.isInstance(varargArray)) {
/* 230 */             Object[] newArguments = new Object[arguments.length];
/* 231 */             System.arraycopy(arguments, 0, newArguments, 0, varargIndex);
/* 232 */             Class<?> targetElementType = varargType.getComponentType();
/* 233 */             int varargLength = Array.getLength(varargArray);
/* 234 */             Object newVarargArray = Array.newInstance(targetElementType, varargLength);
/* 235 */             System.arraycopy(varargArray, 0, newVarargArray, 0, varargLength);
/* 236 */             newArguments[varargIndex] = newVarargArray;
/* 237 */             return newArguments;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 242 */     return arguments;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\framework\AopProxyUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */