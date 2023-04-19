/*     */ package org.springframework.jndi;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import javax.naming.Context;
/*     */ import javax.naming.NamingException;
/*     */ import org.aopalliance.aop.Advice;
/*     */ import org.aopalliance.intercept.MethodInterceptor;
/*     */ import org.aopalliance.intercept.MethodInvocation;
/*     */ import org.springframework.aop.framework.ProxyFactory;
/*     */ import org.springframework.beans.SimpleTypeConverter;
/*     */ import org.springframework.beans.TypeConverter;
/*     */ import org.springframework.beans.TypeMismatchException;
/*     */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryAware;
/*     */ import org.springframework.beans.factory.FactoryBean;
/*     */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JndiObjectFactoryBean
/*     */   extends JndiObjectLocator
/*     */   implements FactoryBean<Object>, BeanFactoryAware, BeanClassLoaderAware
/*     */ {
/*     */   private Class<?>[] proxyInterfaces;
/*     */   private boolean lookupOnStartup = true;
/*     */   private boolean cache = true;
/*     */   private boolean exposeAccessContext = false;
/*     */   private Object defaultObject;
/*     */   private ConfigurableBeanFactory beanFactory;
/*  85 */   private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Object jndiObject;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProxyInterface(Class<?> proxyInterface) {
/* 100 */     this.proxyInterfaces = new Class[] { proxyInterface };
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
/*     */   public void setProxyInterfaces(Class<?>... proxyInterfaces) {
/* 113 */     this.proxyInterfaces = proxyInterfaces;
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
/*     */   public void setLookupOnStartup(boolean lookupOnStartup) {
/* 125 */     this.lookupOnStartup = lookupOnStartup;
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
/*     */   public void setCache(boolean cache) {
/* 138 */     this.cache = cache;
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
/*     */   public void setExposeAccessContext(boolean exposeAccessContext) {
/* 151 */     this.exposeAccessContext = exposeAccessContext;
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
/*     */   public void setDefaultObject(Object defaultObject) {
/* 168 */     this.defaultObject = defaultObject;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBeanFactory(BeanFactory beanFactory) {
/* 173 */     if (beanFactory instanceof ConfigurableBeanFactory)
/*     */     {
/*     */       
/* 176 */       this.beanFactory = (ConfigurableBeanFactory)beanFactory;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBeanClassLoader(ClassLoader classLoader) {
/* 182 */     this.beanClassLoader = classLoader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() throws IllegalArgumentException, NamingException {
/* 191 */     super.afterPropertiesSet();
/*     */     
/* 193 */     if (this.proxyInterfaces != null || !this.lookupOnStartup || !this.cache || this.exposeAccessContext) {
/*     */       
/* 195 */       if (this.defaultObject != null) {
/* 196 */         throw new IllegalArgumentException("'defaultObject' is not supported in combination with 'proxyInterface'");
/*     */       }
/*     */ 
/*     */       
/* 200 */       this.jndiObject = JndiObjectProxyFactory.createJndiObjectProxy(this);
/*     */     } else {
/*     */       
/* 203 */       if (this.defaultObject != null && getExpectedType() != null && 
/* 204 */         !getExpectedType().isInstance(this.defaultObject)) {
/*     */         
/* 206 */         TypeConverter converter = (this.beanFactory != null) ? this.beanFactory.getTypeConverter() : (TypeConverter)new SimpleTypeConverter();
/*     */         try {
/* 208 */           this.defaultObject = converter.convertIfNecessary(this.defaultObject, getExpectedType());
/*     */         }
/* 210 */         catch (TypeMismatchException ex) {
/* 211 */           throw new IllegalArgumentException("Default object [" + this.defaultObject + "] of type [" + this.defaultObject
/* 212 */               .getClass().getName() + "] is not of expected type [" + 
/* 213 */               getExpectedType().getName() + "] and cannot be converted either", ex);
/*     */         } 
/*     */       } 
/*     */       
/* 217 */       this.jndiObject = lookupWithFallback();
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
/*     */   protected Object lookupWithFallback() throws NamingException {
/* 229 */     ClassLoader originalClassLoader = ClassUtils.overrideThreadContextClassLoader(this.beanClassLoader);
/*     */     try {
/* 231 */       return lookup();
/*     */     }
/* 233 */     catch (TypeMismatchNamingException ex) {
/*     */ 
/*     */       
/* 236 */       throw ex;
/*     */     }
/* 238 */     catch (NamingException ex) {
/* 239 */       if (this.defaultObject != null) {
/* 240 */         if (this.logger.isDebugEnabled()) {
/* 241 */           this.logger.debug("JNDI lookup failed - returning specified default object instead", ex);
/*     */         }
/* 243 */         else if (this.logger.isInfoEnabled()) {
/* 244 */           this.logger.info("JNDI lookup failed - returning specified default object instead: " + ex);
/*     */         } 
/* 246 */         return this.defaultObject;
/*     */       } 
/* 248 */       throw ex;
/*     */     } finally {
/*     */       
/* 251 */       if (originalClassLoader != null) {
/* 252 */         Thread.currentThread().setContextClassLoader(originalClassLoader);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getObject() {
/* 263 */     return this.jndiObject;
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?> getObjectType() {
/* 268 */     if (this.proxyInterfaces != null) {
/* 269 */       if (this.proxyInterfaces.length == 1) {
/* 270 */         return this.proxyInterfaces[0];
/*     */       }
/* 272 */       if (this.proxyInterfaces.length > 1) {
/* 273 */         return createCompositeInterface(this.proxyInterfaces);
/*     */       }
/*     */     } 
/* 276 */     if (this.jndiObject != null) {
/* 277 */       return this.jndiObject.getClass();
/*     */     }
/*     */     
/* 280 */     return getExpectedType();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSingleton() {
/* 286 */     return true;
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
/*     */   protected Class<?> createCompositeInterface(Class<?>[] interfaces) {
/* 300 */     return ClassUtils.createCompositeInterface(interfaces, this.beanClassLoader);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class JndiObjectProxyFactory
/*     */   {
/*     */     private static Object createJndiObjectProxy(JndiObjectFactoryBean jof) throws NamingException {
/* 311 */       JndiObjectTargetSource targetSource = new JndiObjectTargetSource();
/* 312 */       targetSource.setJndiTemplate(jof.getJndiTemplate());
/* 313 */       targetSource.setJndiName(jof.getJndiName());
/* 314 */       targetSource.setExpectedType(jof.getExpectedType());
/* 315 */       targetSource.setResourceRef(jof.isResourceRef());
/* 316 */       targetSource.setLookupOnStartup(jof.lookupOnStartup);
/* 317 */       targetSource.setCache(jof.cache);
/* 318 */       targetSource.afterPropertiesSet();
/*     */ 
/*     */       
/* 321 */       ProxyFactory proxyFactory = new ProxyFactory();
/* 322 */       if (jof.proxyInterfaces != null) {
/* 323 */         proxyFactory.setInterfaces(jof.proxyInterfaces);
/*     */       } else {
/*     */         
/* 326 */         Class<?> targetClass = targetSource.getTargetClass();
/* 327 */         if (targetClass == null) {
/* 328 */           throw new IllegalStateException("Cannot deactivate 'lookupOnStartup' without specifying a 'proxyInterface' or 'expectedType'");
/*     */         }
/*     */         
/* 331 */         Class<?>[] ifcs = ClassUtils.getAllInterfacesForClass(targetClass, jof.beanClassLoader);
/* 332 */         for (Class<?> ifc : ifcs) {
/* 333 */           if (Modifier.isPublic(ifc.getModifiers())) {
/* 334 */             proxyFactory.addInterface(ifc);
/*     */           }
/*     */         } 
/*     */       } 
/* 338 */       if (jof.exposeAccessContext) {
/* 339 */         proxyFactory.addAdvice((Advice)new JndiObjectFactoryBean.JndiContextExposingInterceptor(jof.getJndiTemplate()));
/*     */       }
/* 341 */       proxyFactory.setTargetSource(targetSource);
/* 342 */       return proxyFactory.getProxy(jof.beanClassLoader);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class JndiContextExposingInterceptor
/*     */     implements MethodInterceptor
/*     */   {
/*     */     private final JndiTemplate jndiTemplate;
/*     */ 
/*     */ 
/*     */     
/*     */     public JndiContextExposingInterceptor(JndiTemplate jndiTemplate) {
/* 356 */       this.jndiTemplate = jndiTemplate;
/*     */     }
/*     */ 
/*     */     
/*     */     public Object invoke(MethodInvocation invocation) throws Throwable {
/* 361 */       Context ctx = isEligible(invocation.getMethod()) ? this.jndiTemplate.getContext() : null;
/*     */       try {
/* 363 */         return invocation.proceed();
/*     */       } finally {
/*     */         
/* 366 */         this.jndiTemplate.releaseContext(ctx);
/*     */       } 
/*     */     }
/*     */     
/*     */     protected boolean isEligible(Method method) {
/* 371 */       return (Object.class != method.getDeclaringClass());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\jndi\JndiObjectFactoryBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */