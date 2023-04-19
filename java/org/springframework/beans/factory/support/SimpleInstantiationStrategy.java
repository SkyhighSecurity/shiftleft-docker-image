/*     */ package org.springframework.beans.factory.support;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import org.springframework.beans.BeanInstantiationException;
/*     */ import org.springframework.beans.BeanUtils;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
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
/*     */ public class SimpleInstantiationStrategy
/*     */   implements InstantiationStrategy
/*     */ {
/*  45 */   private static final ThreadLocal<Method> currentlyInvokedFactoryMethod = new ThreadLocal<Method>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Method getCurrentlyInvokedFactoryMethod() {
/*  54 */     return currentlyInvokedFactoryMethod.get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object instantiate(RootBeanDefinition bd, String beanName, BeanFactory owner) {
/*  61 */     if (bd.getMethodOverrides().isEmpty()) {
/*     */       Constructor<?> constructorToUse;
/*  63 */       synchronized (bd.constructorArgumentLock) {
/*  64 */         constructorToUse = (Constructor)bd.resolvedConstructorOrFactoryMethod;
/*  65 */         if (constructorToUse == null) {
/*  66 */           final Class<?> clazz = bd.getBeanClass();
/*  67 */           if (clazz.isInterface()) {
/*  68 */             throw new BeanInstantiationException(clazz, "Specified class is an interface");
/*     */           }
/*     */           try {
/*  71 */             if (System.getSecurityManager() != null) {
/*  72 */               constructorToUse = AccessController.<Constructor>doPrivileged((PrivilegedExceptionAction)new PrivilegedExceptionAction<Constructor<?>>()
/*     */                   {
/*     */                     public Constructor<?> run() throws Exception {
/*  75 */                       return clazz.getDeclaredConstructor((Class[])null);
/*     */                     }
/*     */                   });
/*     */             } else {
/*     */               
/*  80 */               constructorToUse = clazz.getDeclaredConstructor((Class[])null);
/*     */             } 
/*  82 */             bd.resolvedConstructorOrFactoryMethod = constructorToUse;
/*     */           }
/*  84 */           catch (Throwable ex) {
/*  85 */             throw new BeanInstantiationException(clazz, "No default constructor found", ex);
/*     */           } 
/*     */         } 
/*     */       } 
/*  89 */       return BeanUtils.instantiateClass(constructorToUse, new Object[0]);
/*     */     } 
/*     */ 
/*     */     
/*  93 */     return instantiateWithMethodInjection(bd, beanName, owner);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object instantiateWithMethodInjection(RootBeanDefinition bd, String beanName, BeanFactory owner) {
/* 104 */     throw new UnsupportedOperationException("Method Injection not supported in SimpleInstantiationStrategy");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object instantiate(RootBeanDefinition bd, String beanName, BeanFactory owner, final Constructor<?> ctor, Object... args) {
/* 111 */     if (bd.getMethodOverrides().isEmpty()) {
/* 112 */       if (System.getSecurityManager() != null)
/*     */       {
/* 114 */         AccessController.doPrivileged(new PrivilegedAction()
/*     */             {
/*     */               public Object run() {
/* 117 */                 ReflectionUtils.makeAccessible(ctor);
/* 118 */                 return null;
/*     */               }
/*     */             });
/*     */       }
/* 122 */       return BeanUtils.instantiateClass(ctor, args);
/*     */     } 
/*     */     
/* 125 */     return instantiateWithMethodInjection(bd, beanName, owner, ctor, args);
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
/*     */   protected Object instantiateWithMethodInjection(RootBeanDefinition bd, String beanName, BeanFactory owner, Constructor<?> ctor, Object... args) {
/* 138 */     throw new UnsupportedOperationException("Method Injection not supported in SimpleInstantiationStrategy");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object instantiate(RootBeanDefinition bd, String beanName, BeanFactory owner, Object factoryBean, final Method factoryMethod, Object... args) {
/*     */     try {
/* 146 */       if (System.getSecurityManager() != null) {
/* 147 */         AccessController.doPrivileged(new PrivilegedAction()
/*     */             {
/*     */               public Object run() {
/* 150 */                 ReflectionUtils.makeAccessible(factoryMethod);
/* 151 */                 return null;
/*     */               }
/*     */             });
/*     */       } else {
/*     */         
/* 156 */         ReflectionUtils.makeAccessible(factoryMethod);
/*     */       } 
/*     */       
/* 159 */       Method priorInvokedFactoryMethod = currentlyInvokedFactoryMethod.get();
/*     */       try {
/* 161 */         currentlyInvokedFactoryMethod.set(factoryMethod);
/* 162 */         return factoryMethod.invoke(factoryBean, args);
/*     */       } finally {
/*     */         
/* 165 */         if (priorInvokedFactoryMethod != null) {
/* 166 */           currentlyInvokedFactoryMethod.set(priorInvokedFactoryMethod);
/*     */         } else {
/*     */           
/* 169 */           currentlyInvokedFactoryMethod.remove();
/*     */         }
/*     */       
/*     */       } 
/* 173 */     } catch (IllegalArgumentException ex) {
/* 174 */       throw new BeanInstantiationException(factoryMethod, "Illegal arguments to factory method '" + factoryMethod
/* 175 */           .getName() + "'; args: " + 
/* 176 */           StringUtils.arrayToCommaDelimitedString(args), ex);
/*     */     }
/* 178 */     catch (IllegalAccessException ex) {
/* 179 */       throw new BeanInstantiationException(factoryMethod, "Cannot access factory method '" + factoryMethod
/* 180 */           .getName() + "'; is it public?", ex);
/*     */     }
/* 182 */     catch (InvocationTargetException ex) {
/* 183 */       String msg = "Factory method '" + factoryMethod.getName() + "' threw exception";
/* 184 */       if (bd.getFactoryBeanName() != null && owner instanceof ConfigurableBeanFactory && ((ConfigurableBeanFactory)owner)
/* 185 */         .isCurrentlyInCreation(bd.getFactoryBeanName())) {
/* 186 */         msg = "Circular reference involving containing bean '" + bd.getFactoryBeanName() + "' - consider declaring the factory method as static for independence from its containing instance. " + msg;
/*     */       }
/*     */       
/* 189 */       throw new BeanInstantiationException(factoryMethod, msg, ex.getTargetException());
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\support\SimpleInstantiationStrategy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */