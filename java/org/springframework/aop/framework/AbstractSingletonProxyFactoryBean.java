/*     */ package org.springframework.aop.framework;
/*     */ 
/*     */ import org.springframework.aop.TargetSource;
/*     */ import org.springframework.aop.framework.adapter.AdvisorAdapterRegistry;
/*     */ import org.springframework.aop.framework.adapter.GlobalAdvisorAdapterRegistry;
/*     */ import org.springframework.aop.target.SingletonTargetSource;
/*     */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*     */ import org.springframework.beans.factory.FactoryBean;
/*     */ import org.springframework.beans.factory.FactoryBeanNotInitializedException;
/*     */ import org.springframework.beans.factory.InitializingBean;
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
/*     */ public abstract class AbstractSingletonProxyFactoryBean
/*     */   extends ProxyConfig
/*     */   implements FactoryBean<Object>, BeanClassLoaderAware, InitializingBean
/*     */ {
/*     */   private Object target;
/*     */   private Class<?>[] proxyInterfaces;
/*     */   private Object[] preInterceptors;
/*     */   private Object[] postInterceptors;
/*  53 */   private AdvisorAdapterRegistry advisorAdapterRegistry = GlobalAdvisorAdapterRegistry.getInstance();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private transient ClassLoader proxyClassLoader;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Object proxy;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTarget(Object target) {
/*  72 */     this.target = target;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProxyInterfaces(Class<?>[] proxyInterfaces) {
/*  82 */     this.proxyInterfaces = proxyInterfaces;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPreInterceptors(Object[] preInterceptors) {
/*  93 */     this.preInterceptors = preInterceptors;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPostInterceptors(Object[] postInterceptors) {
/* 103 */     this.postInterceptors = postInterceptors;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAdvisorAdapterRegistry(AdvisorAdapterRegistry advisorAdapterRegistry) {
/* 112 */     this.advisorAdapterRegistry = advisorAdapterRegistry;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProxyClassLoader(ClassLoader classLoader) {
/* 122 */     this.proxyClassLoader = classLoader;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBeanClassLoader(ClassLoader classLoader) {
/* 127 */     if (this.proxyClassLoader == null) {
/* 128 */       this.proxyClassLoader = classLoader;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() {
/* 135 */     if (this.target == null) {
/* 136 */       throw new IllegalArgumentException("Property 'target' is required");
/*     */     }
/* 138 */     if (this.target instanceof String) {
/* 139 */       throw new IllegalArgumentException("'target' needs to be a bean reference, not a bean name as value");
/*     */     }
/* 141 */     if (this.proxyClassLoader == null) {
/* 142 */       this.proxyClassLoader = ClassUtils.getDefaultClassLoader();
/*     */     }
/*     */     
/* 145 */     ProxyFactory proxyFactory = new ProxyFactory();
/*     */     
/* 147 */     if (this.preInterceptors != null) {
/* 148 */       for (Object interceptor : this.preInterceptors) {
/* 149 */         proxyFactory.addAdvisor(this.advisorAdapterRegistry.wrap(interceptor));
/*     */       }
/*     */     }
/*     */ 
/*     */     
/* 154 */     proxyFactory.addAdvisor(this.advisorAdapterRegistry.wrap(createMainInterceptor()));
/*     */     
/* 156 */     if (this.postInterceptors != null) {
/* 157 */       for (Object interceptor : this.postInterceptors) {
/* 158 */         proxyFactory.addAdvisor(this.advisorAdapterRegistry.wrap(interceptor));
/*     */       }
/*     */     }
/*     */     
/* 162 */     proxyFactory.copyFrom(this);
/*     */     
/* 164 */     TargetSource targetSource = createTargetSource(this.target);
/* 165 */     proxyFactory.setTargetSource(targetSource);
/*     */     
/* 167 */     if (this.proxyInterfaces != null) {
/* 168 */       proxyFactory.setInterfaces(this.proxyInterfaces);
/*     */     }
/* 170 */     else if (!isProxyTargetClass()) {
/*     */       
/* 172 */       proxyFactory.setInterfaces(
/* 173 */           ClassUtils.getAllInterfacesForClass(targetSource.getTargetClass(), this.proxyClassLoader));
/*     */     } 
/*     */     
/* 176 */     postProcessProxyFactory(proxyFactory);
/*     */     
/* 178 */     this.proxy = proxyFactory.getProxy(this.proxyClassLoader);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected TargetSource createTargetSource(Object target) {
/* 188 */     if (target instanceof TargetSource) {
/* 189 */       return (TargetSource)target;
/*     */     }
/*     */     
/* 192 */     return (TargetSource)new SingletonTargetSource(target);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void postProcessProxyFactory(ProxyFactory proxyFactory) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getObject() {
/* 208 */     if (this.proxy == null) {
/* 209 */       throw new FactoryBeanNotInitializedException();
/*     */     }
/* 211 */     return this.proxy;
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?> getObjectType() {
/* 216 */     if (this.proxy != null) {
/* 217 */       return this.proxy.getClass();
/*     */     }
/* 219 */     if (this.proxyInterfaces != null && this.proxyInterfaces.length == 1) {
/* 220 */       return this.proxyInterfaces[0];
/*     */     }
/* 222 */     if (this.target instanceof TargetSource) {
/* 223 */       return ((TargetSource)this.target).getTargetClass();
/*     */     }
/* 225 */     if (this.target != null) {
/* 226 */       return this.target.getClass();
/*     */     }
/* 228 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isSingleton() {
/* 233 */     return true;
/*     */   }
/*     */   
/*     */   protected abstract Object createMainInterceptor();
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\framework\AbstractSingletonProxyFactoryBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */