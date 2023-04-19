/*     */ package org.springframework.beans.factory.support;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Method;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.BeanInstantiationException;
/*     */ import org.springframework.beans.BeanUtils;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
/*     */ import org.springframework.cglib.core.ClassGenerator;
/*     */ import org.springframework.cglib.core.DefaultGeneratorStrategy;
/*     */ import org.springframework.cglib.core.GeneratorStrategy;
/*     */ import org.springframework.cglib.core.NamingPolicy;
/*     */ import org.springframework.cglib.core.SpringNamingPolicy;
/*     */ import org.springframework.cglib.proxy.Callback;
/*     */ import org.springframework.cglib.proxy.CallbackFilter;
/*     */ import org.springframework.cglib.proxy.Enhancer;
/*     */ import org.springframework.cglib.proxy.Factory;
/*     */ import org.springframework.cglib.proxy.MethodInterceptor;
/*     */ import org.springframework.cglib.proxy.MethodProxy;
/*     */ import org.springframework.cglib.proxy.NoOp;
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
/*     */ public class CglibSubclassingInstantiationStrategy
/*     */   extends SimpleInstantiationStrategy
/*     */ {
/*     */   private static final int PASSTHROUGH = 0;
/*     */   private static final int LOOKUP_OVERRIDE = 1;
/*     */   private static final int METHOD_REPLACER = 2;
/*     */   
/*     */   protected Object instantiateWithMethodInjection(RootBeanDefinition bd, String beanName, BeanFactory owner) {
/*  75 */     return instantiateWithMethodInjection(bd, beanName, owner, null, new Object[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object instantiateWithMethodInjection(RootBeanDefinition bd, String beanName, BeanFactory owner, Constructor<?> ctor, Object... args) {
/*  83 */     return (new CglibSubclassCreator(bd, owner)).instantiate(ctor, args);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class CglibSubclassCreator
/*     */   {
/*  93 */     private static final Class<?>[] CALLBACK_TYPES = new Class[] { NoOp.class, CglibSubclassingInstantiationStrategy.LookupOverrideMethodInterceptor.class, CglibSubclassingInstantiationStrategy.ReplaceOverrideMethodInterceptor.class };
/*     */     
/*     */     private final RootBeanDefinition beanDefinition;
/*     */     
/*     */     private final BeanFactory owner;
/*     */ 
/*     */     
/*     */     CglibSubclassCreator(RootBeanDefinition beanDefinition, BeanFactory owner) {
/* 101 */       this.beanDefinition = beanDefinition;
/* 102 */       this.owner = owner;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Object instantiate(Constructor<?> ctor, Object... args) {
/*     */       Object instance;
/* 115 */       Class<?> subclass = createEnhancedSubclass(this.beanDefinition);
/*     */       
/* 117 */       if (ctor == null) {
/* 118 */         instance = BeanUtils.instantiateClass(subclass);
/*     */       } else {
/*     */         
/*     */         try {
/* 122 */           Constructor<?> enhancedSubclassConstructor = subclass.getConstructor(ctor.getParameterTypes());
/* 123 */           instance = enhancedSubclassConstructor.newInstance(args);
/*     */         }
/* 125 */         catch (Exception ex) {
/* 126 */           throw new BeanInstantiationException(this.beanDefinition.getBeanClass(), "Failed to invoke constructor for CGLIB enhanced subclass [" + subclass
/* 127 */               .getName() + "]", ex);
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 132 */       Factory factory = (Factory)instance;
/* 133 */       factory.setCallbacks(new Callback[] { (Callback)NoOp.INSTANCE, (Callback)new CglibSubclassingInstantiationStrategy.LookupOverrideMethodInterceptor(this.beanDefinition, this.owner), (Callback)new CglibSubclassingInstantiationStrategy.ReplaceOverrideMethodInterceptor(this.beanDefinition, this.owner) });
/*     */ 
/*     */       
/* 136 */       return instance;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private Class<?> createEnhancedSubclass(RootBeanDefinition beanDefinition) {
/* 144 */       Enhancer enhancer = new Enhancer();
/* 145 */       enhancer.setSuperclass(beanDefinition.getBeanClass());
/* 146 */       enhancer.setNamingPolicy((NamingPolicy)SpringNamingPolicy.INSTANCE);
/* 147 */       if (this.owner instanceof ConfigurableBeanFactory) {
/* 148 */         ClassLoader cl = ((ConfigurableBeanFactory)this.owner).getBeanClassLoader();
/* 149 */         enhancer.setStrategy((GeneratorStrategy)new CglibSubclassingInstantiationStrategy.ClassLoaderAwareGeneratorStrategy(cl));
/*     */       } 
/* 151 */       enhancer.setCallbackFilter(new CglibSubclassingInstantiationStrategy.MethodOverrideCallbackFilter(beanDefinition));
/* 152 */       enhancer.setCallbackTypes(CALLBACK_TYPES);
/* 153 */       return enhancer.createClass();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class CglibIdentitySupport
/*     */   {
/*     */     private final RootBeanDefinition beanDefinition;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public CglibIdentitySupport(RootBeanDefinition beanDefinition) {
/* 168 */       this.beanDefinition = beanDefinition;
/*     */     }
/*     */     
/*     */     public RootBeanDefinition getBeanDefinition() {
/* 172 */       return this.beanDefinition;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object other) {
/* 177 */       return (getClass() == other.getClass() && this.beanDefinition
/* 178 */         .equals(((CglibIdentitySupport)other).beanDefinition));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 183 */       return this.beanDefinition.hashCode();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class ClassLoaderAwareGeneratorStrategy
/*     */     extends DefaultGeneratorStrategy
/*     */   {
/*     */     private final ClassLoader classLoader;
/*     */ 
/*     */ 
/*     */     
/*     */     public ClassLoaderAwareGeneratorStrategy(ClassLoader classLoader) {
/* 198 */       this.classLoader = classLoader;
/*     */     }
/*     */     
/*     */     public byte[] generate(ClassGenerator cg) throws Exception {
/*     */       ClassLoader threadContextClassLoader;
/* 203 */       if (this.classLoader == null) {
/* 204 */         return super.generate(cg);
/*     */       }
/*     */       
/* 207 */       Thread currentThread = Thread.currentThread();
/*     */       
/*     */       try {
/* 210 */         threadContextClassLoader = currentThread.getContextClassLoader();
/*     */       }
/* 212 */       catch (Throwable ex) {
/*     */         
/* 214 */         return super.generate(cg);
/*     */       } 
/*     */       
/* 217 */       boolean overrideClassLoader = !this.classLoader.equals(threadContextClassLoader);
/* 218 */       if (overrideClassLoader) {
/* 219 */         currentThread.setContextClassLoader(this.classLoader);
/*     */       }
/*     */       try {
/* 222 */         return super.generate(cg);
/*     */       } finally {
/*     */         
/* 225 */         if (overrideClassLoader)
/*     */         {
/* 227 */           currentThread.setContextClassLoader(threadContextClassLoader);
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class MethodOverrideCallbackFilter
/*     */     extends CglibIdentitySupport
/*     */     implements CallbackFilter
/*     */   {
/* 239 */     private static final Log logger = LogFactory.getLog(MethodOverrideCallbackFilter.class);
/*     */     
/*     */     public MethodOverrideCallbackFilter(RootBeanDefinition beanDefinition) {
/* 242 */       super(beanDefinition);
/*     */     }
/*     */ 
/*     */     
/*     */     public int accept(Method method) {
/* 247 */       MethodOverride methodOverride = getBeanDefinition().getMethodOverrides().getOverride(method);
/* 248 */       if (logger.isTraceEnabled()) {
/* 249 */         logger.trace("Override for '" + method.getName() + "' is [" + methodOverride + "]");
/*     */       }
/* 251 */       if (methodOverride == null) {
/* 252 */         return 0;
/*     */       }
/* 254 */       if (methodOverride instanceof LookupOverride) {
/* 255 */         return 1;
/*     */       }
/* 257 */       if (methodOverride instanceof ReplaceOverride) {
/* 258 */         return 2;
/*     */       }
/* 260 */       throw new UnsupportedOperationException("Unexpected MethodOverride subclass: " + methodOverride
/* 261 */           .getClass().getName());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class LookupOverrideMethodInterceptor
/*     */     extends CglibIdentitySupport
/*     */     implements MethodInterceptor
/*     */   {
/*     */     private final BeanFactory owner;
/*     */ 
/*     */     
/*     */     public LookupOverrideMethodInterceptor(RootBeanDefinition beanDefinition, BeanFactory owner) {
/* 275 */       super(beanDefinition);
/* 276 */       this.owner = owner;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Object intercept(Object obj, Method method, Object[] args, MethodProxy mp) throws Throwable {
/* 282 */       LookupOverride lo = (LookupOverride)getBeanDefinition().getMethodOverrides().getOverride(method);
/* 283 */       Object[] argsToUse = (args.length > 0) ? args : null;
/* 284 */       if (StringUtils.hasText(lo.getBeanName())) {
/* 285 */         return this.owner.getBean(lo.getBeanName(), argsToUse);
/*     */       }
/*     */       
/* 288 */       return this.owner.getBean(method.getReturnType(), argsToUse);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class ReplaceOverrideMethodInterceptor
/*     */     extends CglibIdentitySupport
/*     */     implements MethodInterceptor
/*     */   {
/*     */     private final BeanFactory owner;
/*     */ 
/*     */ 
/*     */     
/*     */     public ReplaceOverrideMethodInterceptor(RootBeanDefinition beanDefinition, BeanFactory owner) {
/* 303 */       super(beanDefinition);
/* 304 */       this.owner = owner;
/*     */     }
/*     */ 
/*     */     
/*     */     public Object intercept(Object obj, Method method, Object[] args, MethodProxy mp) throws Throwable {
/* 309 */       ReplaceOverride ro = (ReplaceOverride)getBeanDefinition().getMethodOverrides().getOverride(method);
/*     */       
/* 311 */       MethodReplacer mr = (MethodReplacer)this.owner.getBean(ro.getMethodReplacerBeanName(), MethodReplacer.class);
/* 312 */       return mr.reimplement(obj, method, args);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\support\CglibSubclassingInstantiationStrategy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */