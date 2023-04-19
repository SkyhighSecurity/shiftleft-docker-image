/*     */ package org.springframework.beans.factory.annotation;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.Serializable;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.Collection;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.BeanCreationException;
/*     */ import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;
/*     */ import org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor;
/*     */ import org.springframework.beans.factory.support.RootBeanDefinition;
/*     */ import org.springframework.core.PriorityOrdered;
/*     */ import org.springframework.util.ClassUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class InitDestroyAnnotationBeanPostProcessor
/*     */   implements DestructionAwareBeanPostProcessor, MergedBeanDefinitionPostProcessor, PriorityOrdered, Serializable
/*     */ {
/*  78 */   protected transient Log logger = LogFactory.getLog(getClass());
/*     */   
/*     */   private Class<? extends Annotation> initAnnotationType;
/*     */   
/*     */   private Class<? extends Annotation> destroyAnnotationType;
/*     */   
/*  84 */   private int order = Integer.MAX_VALUE;
/*     */   
/*  86 */   private final transient Map<Class<?>, LifecycleMetadata> lifecycleMetadataCache = new ConcurrentHashMap<Class<?>, LifecycleMetadata>(256);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInitAnnotationType(Class<? extends Annotation> initAnnotationType) {
/*  98 */     this.initAnnotationType = initAnnotationType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDestroyAnnotationType(Class<? extends Annotation> destroyAnnotationType) {
/* 109 */     this.destroyAnnotationType = destroyAnnotationType;
/*     */   }
/*     */   
/*     */   public void setOrder(int order) {
/* 113 */     this.order = order;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getOrder() {
/* 118 */     return this.order;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void postProcessMergedBeanDefinition(RootBeanDefinition beanDefinition, Class<?> beanType, String beanName) {
/* 124 */     if (beanType != null) {
/* 125 */       LifecycleMetadata metadata = findLifecycleMetadata(beanType);
/* 126 */       metadata.checkConfigMembers(beanDefinition);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
/* 132 */     LifecycleMetadata metadata = findLifecycleMetadata(bean.getClass());
/*     */     try {
/* 134 */       metadata.invokeInitMethods(bean, beanName);
/*     */     }
/* 136 */     catch (InvocationTargetException ex) {
/* 137 */       throw new BeanCreationException(beanName, "Invocation of init method failed", ex.getTargetException());
/*     */     }
/* 139 */     catch (Throwable ex) {
/* 140 */       throw new BeanCreationException(beanName, "Failed to invoke init method", ex);
/*     */     } 
/* 142 */     return bean;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
/* 147 */     return bean;
/*     */   }
/*     */ 
/*     */   
/*     */   public void postProcessBeforeDestruction(Object bean, String beanName) throws BeansException {
/* 152 */     LifecycleMetadata metadata = findLifecycleMetadata(bean.getClass());
/*     */     try {
/* 154 */       metadata.invokeDestroyMethods(bean, beanName);
/*     */     }
/* 156 */     catch (InvocationTargetException ex) {
/* 157 */       String msg = "Invocation of destroy method failed on bean with name '" + beanName + "'";
/* 158 */       if (this.logger.isDebugEnabled()) {
/* 159 */         this.logger.warn(msg, ex.getTargetException());
/*     */       } else {
/*     */         
/* 162 */         this.logger.warn(msg + ": " + ex.getTargetException());
/*     */       }
/*     */     
/* 165 */     } catch (Throwable ex) {
/* 166 */       this.logger.error("Failed to invoke destroy method on bean with name '" + beanName + "'", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean requiresDestruction(Object bean) {
/* 172 */     return findLifecycleMetadata(bean.getClass()).hasDestroyMethods();
/*     */   }
/*     */ 
/*     */   
/*     */   private LifecycleMetadata findLifecycleMetadata(Class<?> clazz) {
/* 177 */     if (this.lifecycleMetadataCache == null)
/*     */     {
/* 179 */       return buildLifecycleMetadata(clazz);
/*     */     }
/*     */     
/* 182 */     LifecycleMetadata metadata = this.lifecycleMetadataCache.get(clazz);
/* 183 */     if (metadata == null) {
/* 184 */       synchronized (this.lifecycleMetadataCache) {
/* 185 */         metadata = this.lifecycleMetadataCache.get(clazz);
/* 186 */         if (metadata == null) {
/* 187 */           metadata = buildLifecycleMetadata(clazz);
/* 188 */           this.lifecycleMetadataCache.put(clazz, metadata);
/*     */         } 
/* 190 */         return metadata;
/*     */       } 
/*     */     }
/* 193 */     return metadata;
/*     */   }
/*     */   
/*     */   private LifecycleMetadata buildLifecycleMetadata(final Class<?> clazz) {
/* 197 */     final boolean debug = this.logger.isDebugEnabled();
/* 198 */     LinkedList<LifecycleElement> initMethods = new LinkedList<LifecycleElement>();
/* 199 */     LinkedList<LifecycleElement> destroyMethods = new LinkedList<LifecycleElement>();
/* 200 */     Class<?> targetClass = clazz;
/*     */     
/*     */     do {
/* 203 */       final LinkedList<LifecycleElement> currInitMethods = new LinkedList<LifecycleElement>();
/* 204 */       final LinkedList<LifecycleElement> currDestroyMethods = new LinkedList<LifecycleElement>();
/*     */       
/* 206 */       ReflectionUtils.doWithLocalMethods(targetClass, new ReflectionUtils.MethodCallback()
/*     */           {
/*     */             public void doWith(Method method) throws IllegalArgumentException, IllegalAccessException {
/* 209 */               if (InitDestroyAnnotationBeanPostProcessor.this.initAnnotationType != null && 
/* 210 */                 method.getAnnotation((Class)InitDestroyAnnotationBeanPostProcessor.this.initAnnotationType) != null) {
/* 211 */                 InitDestroyAnnotationBeanPostProcessor.LifecycleElement element = new InitDestroyAnnotationBeanPostProcessor.LifecycleElement(method);
/* 212 */                 currInitMethods.add(element);
/* 213 */                 if (debug) {
/* 214 */                   InitDestroyAnnotationBeanPostProcessor.this.logger.debug("Found init method on class [" + clazz.getName() + "]: " + method);
/*     */                 }
/*     */               } 
/*     */               
/* 218 */               if (InitDestroyAnnotationBeanPostProcessor.this.destroyAnnotationType != null && 
/* 219 */                 method.getAnnotation((Class)InitDestroyAnnotationBeanPostProcessor.this.destroyAnnotationType) != null) {
/* 220 */                 currDestroyMethods.add(new InitDestroyAnnotationBeanPostProcessor.LifecycleElement(method));
/* 221 */                 if (debug) {
/* 222 */                   InitDestroyAnnotationBeanPostProcessor.this.logger.debug("Found destroy method on class [" + clazz.getName() + "]: " + method);
/*     */                 }
/*     */               } 
/*     */             }
/*     */           });
/*     */ 
/*     */       
/* 229 */       initMethods.addAll(0, currInitMethods);
/* 230 */       destroyMethods.addAll(currDestroyMethods);
/* 231 */       targetClass = targetClass.getSuperclass();
/*     */     }
/* 233 */     while (targetClass != null && targetClass != Object.class);
/*     */     
/* 235 */     return new LifecycleMetadata(clazz, initMethods, destroyMethods);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
/* 245 */     ois.defaultReadObject();
/*     */ 
/*     */     
/* 248 */     this.logger = LogFactory.getLog(getClass());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class LifecycleMetadata
/*     */   {
/*     */     private final Class<?> targetClass;
/*     */ 
/*     */     
/*     */     private final Collection<InitDestroyAnnotationBeanPostProcessor.LifecycleElement> initMethods;
/*     */ 
/*     */     
/*     */     private final Collection<InitDestroyAnnotationBeanPostProcessor.LifecycleElement> destroyMethods;
/*     */ 
/*     */     
/*     */     private volatile Set<InitDestroyAnnotationBeanPostProcessor.LifecycleElement> checkedInitMethods;
/*     */     
/*     */     private volatile Set<InitDestroyAnnotationBeanPostProcessor.LifecycleElement> checkedDestroyMethods;
/*     */ 
/*     */     
/*     */     public LifecycleMetadata(Class<?> targetClass, Collection<InitDestroyAnnotationBeanPostProcessor.LifecycleElement> initMethods, Collection<InitDestroyAnnotationBeanPostProcessor.LifecycleElement> destroyMethods) {
/* 270 */       this.targetClass = targetClass;
/* 271 */       this.initMethods = initMethods;
/* 272 */       this.destroyMethods = destroyMethods;
/*     */     }
/*     */     
/*     */     public void checkConfigMembers(RootBeanDefinition beanDefinition) {
/* 276 */       Set<InitDestroyAnnotationBeanPostProcessor.LifecycleElement> checkedInitMethods = new LinkedHashSet<InitDestroyAnnotationBeanPostProcessor.LifecycleElement>(this.initMethods.size());
/* 277 */       for (InitDestroyAnnotationBeanPostProcessor.LifecycleElement element : this.initMethods) {
/* 278 */         String methodIdentifier = element.getIdentifier();
/* 279 */         if (!beanDefinition.isExternallyManagedInitMethod(methodIdentifier)) {
/* 280 */           beanDefinition.registerExternallyManagedInitMethod(methodIdentifier);
/* 281 */           checkedInitMethods.add(element);
/* 282 */           if (InitDestroyAnnotationBeanPostProcessor.this.logger.isDebugEnabled()) {
/* 283 */             InitDestroyAnnotationBeanPostProcessor.this.logger.debug("Registered init method on class [" + this.targetClass.getName() + "]: " + element);
/*     */           }
/*     */         } 
/*     */       } 
/* 287 */       Set<InitDestroyAnnotationBeanPostProcessor.LifecycleElement> checkedDestroyMethods = new LinkedHashSet<InitDestroyAnnotationBeanPostProcessor.LifecycleElement>(this.destroyMethods.size());
/* 288 */       for (InitDestroyAnnotationBeanPostProcessor.LifecycleElement element : this.destroyMethods) {
/* 289 */         String methodIdentifier = element.getIdentifier();
/* 290 */         if (!beanDefinition.isExternallyManagedDestroyMethod(methodIdentifier)) {
/* 291 */           beanDefinition.registerExternallyManagedDestroyMethod(methodIdentifier);
/* 292 */           checkedDestroyMethods.add(element);
/* 293 */           if (InitDestroyAnnotationBeanPostProcessor.this.logger.isDebugEnabled()) {
/* 294 */             InitDestroyAnnotationBeanPostProcessor.this.logger.debug("Registered destroy method on class [" + this.targetClass.getName() + "]: " + element);
/*     */           }
/*     */         } 
/*     */       } 
/* 298 */       this.checkedInitMethods = checkedInitMethods;
/* 299 */       this.checkedDestroyMethods = checkedDestroyMethods;
/*     */     }
/*     */     
/*     */     public void invokeInitMethods(Object target, String beanName) throws Throwable {
/* 303 */       Collection<InitDestroyAnnotationBeanPostProcessor.LifecycleElement> initMethodsToIterate = (this.checkedInitMethods != null) ? this.checkedInitMethods : this.initMethods;
/*     */       
/* 305 */       if (!initMethodsToIterate.isEmpty()) {
/* 306 */         boolean debug = InitDestroyAnnotationBeanPostProcessor.this.logger.isDebugEnabled();
/* 307 */         for (InitDestroyAnnotationBeanPostProcessor.LifecycleElement element : initMethodsToIterate) {
/* 308 */           if (debug) {
/* 309 */             InitDestroyAnnotationBeanPostProcessor.this.logger.debug("Invoking init method on bean '" + beanName + "': " + element.getMethod());
/*     */           }
/* 311 */           element.invoke(target);
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     public void invokeDestroyMethods(Object target, String beanName) throws Throwable {
/* 317 */       Collection<InitDestroyAnnotationBeanPostProcessor.LifecycleElement> destroyMethodsToUse = (this.checkedDestroyMethods != null) ? this.checkedDestroyMethods : this.destroyMethods;
/*     */       
/* 319 */       if (!destroyMethodsToUse.isEmpty()) {
/* 320 */         boolean debug = InitDestroyAnnotationBeanPostProcessor.this.logger.isDebugEnabled();
/* 321 */         for (InitDestroyAnnotationBeanPostProcessor.LifecycleElement element : destroyMethodsToUse) {
/* 322 */           if (debug) {
/* 323 */             InitDestroyAnnotationBeanPostProcessor.this.logger.debug("Invoking destroy method on bean '" + beanName + "': " + element.getMethod());
/*     */           }
/* 325 */           element.invoke(target);
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     public boolean hasDestroyMethods() {
/* 331 */       Collection<InitDestroyAnnotationBeanPostProcessor.LifecycleElement> destroyMethodsToUse = (this.checkedDestroyMethods != null) ? this.checkedDestroyMethods : this.destroyMethods;
/*     */       
/* 333 */       return !destroyMethodsToUse.isEmpty();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class LifecycleElement
/*     */   {
/*     */     private final Method method;
/*     */ 
/*     */     
/*     */     private final String identifier;
/*     */ 
/*     */     
/*     */     public LifecycleElement(Method method) {
/* 348 */       if ((method.getParameterTypes()).length != 0) {
/* 349 */         throw new IllegalStateException("Lifecycle method annotation requires a no-arg method: " + method);
/*     */       }
/* 351 */       this.method = method;
/* 352 */       this
/* 353 */         .identifier = Modifier.isPrivate(method.getModifiers()) ? ClassUtils.getQualifiedMethodName(method) : method.getName();
/*     */     }
/*     */     
/*     */     public Method getMethod() {
/* 357 */       return this.method;
/*     */     }
/*     */     
/*     */     public String getIdentifier() {
/* 361 */       return this.identifier;
/*     */     }
/*     */     
/*     */     public void invoke(Object target) throws Throwable {
/* 365 */       ReflectionUtils.makeAccessible(this.method);
/* 366 */       this.method.invoke(target, (Object[])null);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object other) {
/* 371 */       if (this == other) {
/* 372 */         return true;
/*     */       }
/* 374 */       if (!(other instanceof LifecycleElement)) {
/* 375 */         return false;
/*     */       }
/* 377 */       LifecycleElement otherElement = (LifecycleElement)other;
/* 378 */       return this.identifier.equals(otherElement.identifier);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 383 */       return this.identifier.hashCode();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\annotation\InitDestroyAnnotationBeanPostProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */