/*     */ package org.springframework.beans.factory.config;
/*     */ 
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Proxy;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.SimpleTypeConverter;
/*     */ import org.springframework.beans.TypeConverter;
/*     */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryAware;
/*     */ import org.springframework.beans.factory.DisposableBean;
/*     */ import org.springframework.beans.factory.FactoryBean;
/*     */ import org.springframework.beans.factory.FactoryBeanNotInitializedException;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.util.ReflectionUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractFactoryBean<T>
/*     */   implements FactoryBean<T>, BeanClassLoaderAware, BeanFactoryAware, InitializingBean, DisposableBean
/*     */ {
/*  65 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*     */   private boolean singleton = true;
/*     */   
/*  69 */   private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
/*     */ 
/*     */   
/*     */   private BeanFactory beanFactory;
/*     */ 
/*     */   
/*     */   private boolean initialized = false;
/*     */ 
/*     */   
/*     */   private T singletonInstance;
/*     */ 
/*     */   
/*     */   private T earlySingletonInstance;
/*     */ 
/*     */   
/*     */   public void setSingleton(boolean singleton) {
/*  85 */     this.singleton = singleton;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSingleton() {
/*  90 */     return this.singleton;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBeanClassLoader(ClassLoader classLoader) {
/*  95 */     this.beanClassLoader = classLoader;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBeanFactory(BeanFactory beanFactory) {
/* 100 */     this.beanFactory = beanFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected BeanFactory getBeanFactory() {
/* 107 */     return this.beanFactory;
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
/*     */   protected TypeConverter getBeanTypeConverter() {
/* 119 */     BeanFactory beanFactory = getBeanFactory();
/* 120 */     if (beanFactory instanceof ConfigurableBeanFactory) {
/* 121 */       return ((ConfigurableBeanFactory)beanFactory).getTypeConverter();
/*     */     }
/*     */     
/* 124 */     return (TypeConverter)new SimpleTypeConverter();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() throws Exception {
/* 133 */     if (isSingleton()) {
/* 134 */       this.initialized = true;
/* 135 */       this.singletonInstance = createInstance();
/* 136 */       this.earlySingletonInstance = null;
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
/*     */   public final T getObject() throws Exception {
/* 148 */     if (isSingleton()) {
/* 149 */       return this.initialized ? this.singletonInstance : getEarlySingletonInstance();
/*     */     }
/*     */     
/* 152 */     return createInstance();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private T getEarlySingletonInstance() throws Exception {
/* 162 */     Class<?>[] ifcs = getEarlySingletonInterfaces();
/* 163 */     if (ifcs == null) {
/* 164 */       throw new FactoryBeanNotInitializedException(
/* 165 */           getClass().getName() + " does not support circular references");
/*     */     }
/* 167 */     if (this.earlySingletonInstance == null) {
/* 168 */       this.earlySingletonInstance = (T)Proxy.newProxyInstance(this.beanClassLoader, ifcs, new EarlySingletonInvocationHandler());
/*     */     }
/*     */     
/* 171 */     return this.earlySingletonInstance;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private T getSingletonInstance() throws IllegalStateException {
/* 180 */     Assert.state(this.initialized, "Singleton instance not initialized yet");
/* 181 */     return this.singletonInstance;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void destroy() throws Exception {
/* 190 */     if (isSingleton()) {
/* 191 */       destroyInstance(this.singletonInstance);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Class<?>[] getEarlySingletonInterfaces() {
/* 228 */     Class<?> type = getObjectType();
/* 229 */     (new Class[1])[0] = type; return (type != null && type.isInterface()) ? new Class[1] : null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void destroyInstance(T instance) throws Exception {}
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract Class<?> getObjectType();
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract T createInstance() throws Exception;
/*     */ 
/*     */   
/*     */   private class EarlySingletonInvocationHandler
/*     */     implements InvocationHandler
/*     */   {
/*     */     private EarlySingletonInvocationHandler() {}
/*     */ 
/*     */     
/*     */     public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
/* 252 */       if (ReflectionUtils.isEqualsMethod(method))
/*     */       {
/* 254 */         return Boolean.valueOf((proxy == args[0]));
/*     */       }
/* 256 */       if (ReflectionUtils.isHashCodeMethod(method))
/*     */       {
/* 258 */         return Integer.valueOf(System.identityHashCode(proxy));
/*     */       }
/* 260 */       if (!AbstractFactoryBean.this.initialized && ReflectionUtils.isToStringMethod(method)) {
/* 261 */         return "Early singleton proxy for interfaces " + 
/* 262 */           ObjectUtils.nullSafeToString((Object[])AbstractFactoryBean.this.getEarlySingletonInterfaces());
/*     */       }
/*     */       try {
/* 265 */         return method.invoke(AbstractFactoryBean.this.getSingletonInstance(), args);
/*     */       }
/* 267 */       catch (InvocationTargetException ex) {
/* 268 */         throw ex.getTargetException();
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\config\AbstractFactoryBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */