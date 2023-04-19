/*     */ package org.springframework.beans.factory.support;
/*     */ 
/*     */ import java.security.AccessControlContext;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.BeanCreationException;
/*     */ import org.springframework.beans.factory.BeanCurrentlyInCreationException;
/*     */ import org.springframework.beans.factory.FactoryBean;
/*     */ import org.springframework.beans.factory.FactoryBeanNotInitializedException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class FactoryBeanRegistrySupport
/*     */   extends DefaultSingletonBeanRegistry
/*     */ {
/*  46 */   private final Map<String, Object> factoryBeanObjectCache = new ConcurrentHashMap<String, Object>(16);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Class<?> getTypeForFactoryBean(final FactoryBean<?> factoryBean) {
/*     */     try {
/*  57 */       if (System.getSecurityManager() != null) {
/*  58 */         return AccessController.<Class<?>>doPrivileged(new PrivilegedAction<Class<?>>()
/*     */             {
/*     */               public Class<?> run() {
/*  61 */                 return factoryBean.getObjectType();
/*     */               }
/*  63 */             },  getAccessControlContext());
/*     */       }
/*     */       
/*  66 */       return factoryBean.getObjectType();
/*     */     
/*     */     }
/*  69 */     catch (Throwable ex) {
/*     */       
/*  71 */       this.logger.warn("FactoryBean threw exception from getObjectType, despite the contract saying that it should return null if the type of its object cannot be determined yet", ex);
/*     */       
/*  73 */       return null;
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
/*     */   protected Object getCachedObjectForFactoryBean(String beanName) {
/*  85 */     Object object = this.factoryBeanObjectCache.get(beanName);
/*  86 */     return (object != NULL_OBJECT) ? object : null;
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
/*     */   protected Object getObjectFromFactoryBean(FactoryBean<?> factory, String beanName, boolean shouldPostProcess) {
/*  99 */     if (factory.isSingleton() && containsSingleton(beanName)) {
/* 100 */       synchronized (getSingletonMutex()) {
/* 101 */         Object object1 = this.factoryBeanObjectCache.get(beanName);
/* 102 */         if (object1 == null) {
/* 103 */           object1 = doGetObjectFromFactoryBean(factory, beanName);
/*     */ 
/*     */           
/* 106 */           Object alreadyThere = this.factoryBeanObjectCache.get(beanName);
/* 107 */           if (alreadyThere != null) {
/* 108 */             object1 = alreadyThere;
/*     */           } else {
/*     */             
/* 111 */             if (object1 != null && shouldPostProcess) {
/* 112 */               if (isSingletonCurrentlyInCreation(beanName))
/*     */               {
/* 114 */                 return object1;
/*     */               }
/* 116 */               beforeSingletonCreation(beanName);
/*     */               try {
/* 118 */                 object1 = postProcessObjectFromFactoryBean(object1, beanName);
/*     */               }
/* 120 */               catch (Throwable ex) {
/* 121 */                 throw new BeanCreationException(beanName, "Post-processing of FactoryBean's singleton object failed", ex);
/*     */               }
/*     */               finally {
/*     */                 
/* 125 */                 afterSingletonCreation(beanName);
/*     */               } 
/*     */             } 
/* 128 */             if (containsSingleton(beanName)) {
/* 129 */               this.factoryBeanObjectCache.put(beanName, (object1 != null) ? object1 : NULL_OBJECT);
/*     */             }
/*     */           } 
/*     */         } 
/* 133 */         return (object1 != NULL_OBJECT) ? object1 : null;
/*     */       } 
/*     */     }
/*     */     
/* 137 */     Object object = doGetObjectFromFactoryBean(factory, beanName);
/* 138 */     if (object != null && shouldPostProcess) {
/*     */       try {
/* 140 */         object = postProcessObjectFromFactoryBean(object, beanName);
/*     */       }
/* 142 */       catch (Throwable ex) {
/* 143 */         throw new BeanCreationException(beanName, "Post-processing of FactoryBean's object failed", ex);
/*     */       } 
/*     */     }
/* 146 */     return object;
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
/*     */   private Object doGetObjectFromFactoryBean(final FactoryBean<?> factory, String beanName) throws BeanCreationException {
/*     */     Object object;
/*     */     try {
/* 163 */       if (System.getSecurityManager() != null) {
/* 164 */         AccessControlContext acc = getAccessControlContext();
/*     */         try {
/* 166 */           object = AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */               {
/*     */                 public Object run() throws Exception {
/* 169 */                   return factory.getObject();
/*     */                 }
/*     */               }, 
/*     */               acc);
/* 173 */         } catch (PrivilegedActionException pae) {
/* 174 */           throw pae.getException();
/*     */         } 
/*     */       } else {
/*     */         
/* 178 */         object = factory.getObject();
/*     */       }
/*     */     
/* 181 */     } catch (FactoryBeanNotInitializedException ex) {
/* 182 */       throw new BeanCurrentlyInCreationException(beanName, ex.toString());
/*     */     }
/* 184 */     catch (Throwable ex) {
/* 185 */       throw new BeanCreationException(beanName, "FactoryBean threw exception on object creation", ex);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 190 */     if (object == null && isSingletonCurrentlyInCreation(beanName)) {
/* 191 */       throw new BeanCurrentlyInCreationException(beanName, "FactoryBean which is currently in creation returned null from getObject");
/*     */     }
/*     */     
/* 194 */     return object;
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
/*     */   protected Object postProcessObjectFromFactoryBean(Object object, String beanName) throws BeansException {
/* 208 */     return object;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected FactoryBean<?> getFactoryBean(String beanName, Object beanInstance) throws BeansException {
/* 219 */     if (!(beanInstance instanceof FactoryBean)) {
/* 220 */       throw new BeanCreationException(beanName, "Bean instance of type [" + beanInstance
/* 221 */           .getClass() + "] is not a FactoryBean");
/*     */     }
/* 223 */     return (FactoryBean)beanInstance;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void removeSingleton(String beanName) {
/* 231 */     synchronized (getSingletonMutex()) {
/* 232 */       super.removeSingleton(beanName);
/* 233 */       this.factoryBeanObjectCache.remove(beanName);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void clearSingletonCache() {
/* 242 */     synchronized (getSingletonMutex()) {
/* 243 */       super.clearSingletonCache();
/* 244 */       this.factoryBeanObjectCache.clear();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AccessControlContext getAccessControlContext() {
/* 255 */     return AccessController.getContext();
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\support\FactoryBeanRegistrySupport.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */