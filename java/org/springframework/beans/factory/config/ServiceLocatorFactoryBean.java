/*     */ package org.springframework.beans.factory.config;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.util.Properties;
/*     */ import org.springframework.beans.BeanUtils;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.FatalBeanException;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryAware;
/*     */ import org.springframework.beans.factory.FactoryBean;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.beans.factory.ListableBeanFactory;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ServiceLocatorFactoryBean
/*     */   implements FactoryBean<Object>, BeanFactoryAware, InitializingBean
/*     */ {
/*     */   private Class<?> serviceLocatorInterface;
/*     */   private Constructor<Exception> serviceLocatorExceptionConstructor;
/*     */   private Properties serviceMappings;
/*     */   private ListableBeanFactory beanFactory;
/*     */   private Object proxy;
/*     */   
/*     */   public void setServiceLocatorInterface(Class<?> interfaceType) {
/* 210 */     this.serviceLocatorInterface = interfaceType;
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
/*     */   public void setServiceLocatorExceptionClass(Class<? extends Exception> serviceLocatorExceptionClass) {
/* 226 */     if (serviceLocatorExceptionClass != null && !Exception.class.isAssignableFrom(serviceLocatorExceptionClass)) {
/* 227 */       throw new IllegalArgumentException("serviceLocatorException [" + serviceLocatorExceptionClass
/* 228 */           .getName() + "] is not a subclass of Exception");
/*     */     }
/* 230 */     this
/* 231 */       .serviceLocatorExceptionConstructor = determineServiceLocatorExceptionConstructor(serviceLocatorExceptionClass);
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
/*     */   public void setServiceMappings(Properties serviceMappings) {
/* 245 */     this.serviceMappings = serviceMappings;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
/* 250 */     if (!(beanFactory instanceof ListableBeanFactory)) {
/* 251 */       throw new FatalBeanException("ServiceLocatorFactoryBean needs to run in a BeanFactory that is a ListableBeanFactory");
/*     */     }
/*     */     
/* 254 */     this.beanFactory = (ListableBeanFactory)beanFactory;
/*     */   }
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() {
/* 259 */     if (this.serviceLocatorInterface == null) {
/* 260 */       throw new IllegalArgumentException("Property 'serviceLocatorInterface' is required");
/*     */     }
/*     */ 
/*     */     
/* 264 */     this.proxy = Proxy.newProxyInstance(this.serviceLocatorInterface
/* 265 */         .getClassLoader(), new Class[] { this.serviceLocatorInterface }, new ServiceLocatorInvocationHandler());
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
/*     */   protected Constructor<Exception> determineServiceLocatorExceptionConstructor(Class<? extends Exception> exceptionClass) {
/*     */     try {
/* 284 */       return (Constructor)exceptionClass.getConstructor(new Class[] { String.class, Throwable.class });
/*     */     }
/* 286 */     catch (NoSuchMethodException ex) {
/*     */       try {
/* 288 */         return (Constructor)exceptionClass.getConstructor(new Class[] { Throwable.class });
/*     */       }
/* 290 */       catch (NoSuchMethodException ex2) {
/*     */         try {
/* 292 */           return (Constructor)exceptionClass.getConstructor(new Class[] { String.class });
/*     */         }
/* 294 */         catch (NoSuchMethodException ex3) {
/* 295 */           throw new IllegalArgumentException("Service locator exception [" + exceptionClass
/* 296 */               .getName() + "] neither has a (String, Throwable) constructor nor a (String) constructor");
/*     */         } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Exception createServiceLocatorException(Constructor<Exception> exceptionConstructor, BeansException cause) {
/* 314 */     Class<?>[] paramTypes = exceptionConstructor.getParameterTypes();
/* 315 */     Object[] args = new Object[paramTypes.length];
/* 316 */     for (int i = 0; i < paramTypes.length; i++) {
/* 317 */       if (String.class == paramTypes[i]) {
/* 318 */         args[i] = cause.getMessage();
/*     */       }
/* 320 */       else if (paramTypes[i].isInstance(cause)) {
/* 321 */         args[i] = cause;
/*     */       } 
/*     */     } 
/* 324 */     return (Exception)BeanUtils.instantiateClass(exceptionConstructor, args);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getObject() {
/* 330 */     return this.proxy;
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?> getObjectType() {
/* 335 */     return this.serviceLocatorInterface;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSingleton() {
/* 340 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   private class ServiceLocatorInvocationHandler
/*     */     implements InvocationHandler
/*     */   {
/*     */     private ServiceLocatorInvocationHandler() {}
/*     */ 
/*     */     
/*     */     public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
/* 351 */       if (ReflectionUtils.isEqualsMethod(method))
/*     */       {
/* 353 */         return Boolean.valueOf((proxy == args[0]));
/*     */       }
/* 355 */       if (ReflectionUtils.isHashCodeMethod(method))
/*     */       {
/* 357 */         return Integer.valueOf(System.identityHashCode(proxy));
/*     */       }
/* 359 */       if (ReflectionUtils.isToStringMethod(method)) {
/* 360 */         return "Service locator: " + ServiceLocatorFactoryBean.this.serviceLocatorInterface;
/*     */       }
/*     */       
/* 363 */       return invokeServiceLocatorMethod(method, args);
/*     */     }
/*     */ 
/*     */     
/*     */     private Object invokeServiceLocatorMethod(Method method, Object[] args) throws Exception {
/* 368 */       Class<?> serviceLocatorMethodReturnType = getServiceLocatorMethodReturnType(method);
/*     */       try {
/* 370 */         String beanName = tryGetBeanName(args);
/* 371 */         if (StringUtils.hasLength(beanName))
/*     */         {
/* 373 */           return ServiceLocatorFactoryBean.this.beanFactory.getBean(beanName, serviceLocatorMethodReturnType);
/*     */         }
/*     */ 
/*     */         
/* 377 */         return ServiceLocatorFactoryBean.this.beanFactory.getBean(serviceLocatorMethodReturnType);
/*     */       
/*     */       }
/* 380 */       catch (BeansException ex) {
/* 381 */         if (ServiceLocatorFactoryBean.this.serviceLocatorExceptionConstructor != null) {
/* 382 */           throw ServiceLocatorFactoryBean.this.createServiceLocatorException(ServiceLocatorFactoryBean.this.serviceLocatorExceptionConstructor, ex);
/*     */         }
/* 384 */         throw ex;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private String tryGetBeanName(Object[] args) {
/* 392 */       String beanName = "";
/* 393 */       if (args != null && args.length == 1 && args[0] != null) {
/* 394 */         beanName = args[0].toString();
/*     */       }
/*     */       
/* 397 */       if (ServiceLocatorFactoryBean.this.serviceMappings != null) {
/* 398 */         String mappedName = ServiceLocatorFactoryBean.this.serviceMappings.getProperty(beanName);
/* 399 */         if (mappedName != null) {
/* 400 */           beanName = mappedName;
/*     */         }
/*     */       } 
/* 403 */       return beanName;
/*     */     }
/*     */     
/*     */     private Class<?> getServiceLocatorMethodReturnType(Method method) throws NoSuchMethodException {
/* 407 */       Class<?>[] paramTypes = method.getParameterTypes();
/* 408 */       Method interfaceMethod = ServiceLocatorFactoryBean.this.serviceLocatorInterface.getMethod(method.getName(), paramTypes);
/* 409 */       Class<?> serviceLocatorReturnType = interfaceMethod.getReturnType();
/*     */ 
/*     */       
/* 412 */       if (paramTypes.length > 1 || void.class == serviceLocatorReturnType) {
/* 413 */         throw new UnsupportedOperationException("May only call methods with signature '<type> xxx()' or '<type> xxx(<idtype> id)' on factory interface, but tried to call: " + interfaceMethod);
/*     */       }
/*     */ 
/*     */       
/* 417 */       return serviceLocatorReturnType;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\config\ServiceLocatorFactoryBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */