/*     */ package org.springframework.context.event;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.springframework.aop.framework.AopProxyUtils;
/*     */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryAware;
/*     */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*     */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
/*     */ import org.springframework.context.ApplicationEvent;
/*     */ import org.springframework.context.ApplicationListener;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.core.annotation.AnnotationAwareOrderComparator;
/*     */ import org.springframework.util.ClassUtils;
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
/*     */ public abstract class AbstractApplicationEventMulticaster
/*     */   implements ApplicationEventMulticaster, BeanClassLoaderAware, BeanFactoryAware
/*     */ {
/*  63 */   private final ListenerRetriever defaultRetriever = new ListenerRetriever(false);
/*     */   
/*  65 */   final Map<ListenerCacheKey, ListenerRetriever> retrieverCache = new ConcurrentHashMap<ListenerCacheKey, ListenerRetriever>(64);
/*     */ 
/*     */   
/*     */   private ClassLoader beanClassLoader;
/*     */   
/*     */   private BeanFactory beanFactory;
/*     */   
/*  72 */   private Object retrievalMutex = this.defaultRetriever;
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBeanClassLoader(ClassLoader classLoader) {
/*  77 */     this.beanClassLoader = classLoader;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBeanFactory(BeanFactory beanFactory) {
/*  82 */     this.beanFactory = beanFactory;
/*  83 */     if (beanFactory instanceof ConfigurableBeanFactory) {
/*  84 */       ConfigurableBeanFactory cbf = (ConfigurableBeanFactory)beanFactory;
/*  85 */       if (this.beanClassLoader == null) {
/*  86 */         this.beanClassLoader = cbf.getBeanClassLoader();
/*     */       }
/*  88 */       this.retrievalMutex = cbf.getSingletonMutex();
/*     */     } 
/*     */   }
/*     */   
/*     */   private BeanFactory getBeanFactory() {
/*  93 */     if (this.beanFactory == null) {
/*  94 */       throw new IllegalStateException("ApplicationEventMulticaster cannot retrieve listener beans because it is not associated with a BeanFactory");
/*     */     }
/*     */     
/*  97 */     return this.beanFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addApplicationListener(ApplicationListener<?> listener) {
/* 103 */     synchronized (this.retrievalMutex) {
/*     */ 
/*     */       
/* 106 */       Object singletonTarget = AopProxyUtils.getSingletonTarget(listener);
/* 107 */       if (singletonTarget instanceof ApplicationListener) {
/* 108 */         this.defaultRetriever.applicationListeners.remove(singletonTarget);
/*     */       }
/* 110 */       this.defaultRetriever.applicationListeners.add(listener);
/* 111 */       this.retrieverCache.clear();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void addApplicationListenerBean(String listenerBeanName) {
/* 117 */     synchronized (this.retrievalMutex) {
/* 118 */       this.defaultRetriever.applicationListenerBeans.add(listenerBeanName);
/* 119 */       this.retrieverCache.clear();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeApplicationListener(ApplicationListener<?> listener) {
/* 125 */     synchronized (this.retrievalMutex) {
/* 126 */       this.defaultRetriever.applicationListeners.remove(listener);
/* 127 */       this.retrieverCache.clear();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeApplicationListenerBean(String listenerBeanName) {
/* 133 */     synchronized (this.retrievalMutex) {
/* 134 */       this.defaultRetriever.applicationListenerBeans.remove(listenerBeanName);
/* 135 */       this.retrieverCache.clear();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeAllListeners() {
/* 141 */     synchronized (this.retrievalMutex) {
/* 142 */       this.defaultRetriever.applicationListeners.clear();
/* 143 */       this.defaultRetriever.applicationListenerBeans.clear();
/* 144 */       this.retrieverCache.clear();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Collection<ApplicationListener<?>> getApplicationListeners() {
/* 155 */     synchronized (this.retrievalMutex) {
/* 156 */       return this.defaultRetriever.getApplicationListeners();
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
/*     */   protected Collection<ApplicationListener<?>> getApplicationListeners(ApplicationEvent event, ResolvableType eventType) {
/* 172 */     Object source = event.getSource();
/* 173 */     Class<?> sourceType = (source != null) ? source.getClass() : null;
/* 174 */     ListenerCacheKey cacheKey = new ListenerCacheKey(eventType, sourceType);
/*     */ 
/*     */     
/* 177 */     ListenerRetriever retriever = this.retrieverCache.get(cacheKey);
/* 178 */     if (retriever != null) {
/* 179 */       return retriever.getApplicationListeners();
/*     */     }
/*     */     
/* 182 */     if (this.beanClassLoader == null || (
/* 183 */       ClassUtils.isCacheSafe(event.getClass(), this.beanClassLoader) && (sourceType == null || 
/* 184 */       ClassUtils.isCacheSafe(sourceType, this.beanClassLoader))))
/*     */     {
/* 186 */       synchronized (this.retrievalMutex) {
/* 187 */         retriever = this.retrieverCache.get(cacheKey);
/* 188 */         if (retriever != null) {
/* 189 */           return retriever.getApplicationListeners();
/*     */         }
/* 191 */         retriever = new ListenerRetriever(true);
/*     */         
/* 193 */         Collection<ApplicationListener<?>> listeners = retrieveApplicationListeners(eventType, sourceType, retriever);
/* 194 */         this.retrieverCache.put(cacheKey, retriever);
/* 195 */         return listeners;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 200 */     return retrieveApplicationListeners(eventType, sourceType, null);
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
/*     */   private Collection<ApplicationListener<?>> retrieveApplicationListeners(ResolvableType eventType, Class<?> sourceType, ListenerRetriever retriever) {
/*     */     Set<ApplicationListener<?>> listeners;
/*     */     Set<String> listenerBeans;
/* 214 */     List<ApplicationListener<?>> allListeners = new ArrayList<ApplicationListener<?>>();
/*     */ 
/*     */     
/* 217 */     synchronized (this.retrievalMutex) {
/* 218 */       listeners = new LinkedHashSet<ApplicationListener<?>>(this.defaultRetriever.applicationListeners);
/* 219 */       listenerBeans = new LinkedHashSet<String>(this.defaultRetriever.applicationListenerBeans);
/*     */     } 
/* 221 */     for (ApplicationListener<?> listener : listeners) {
/* 222 */       if (supportsEvent(listener, eventType, sourceType)) {
/* 223 */         if (retriever != null) {
/* 224 */           retriever.applicationListeners.add(listener);
/*     */         }
/* 226 */         allListeners.add(listener);
/*     */       } 
/*     */     } 
/* 229 */     if (!listenerBeans.isEmpty()) {
/* 230 */       BeanFactory beanFactory = getBeanFactory();
/* 231 */       for (String listenerBeanName : listenerBeans) {
/*     */         try {
/* 233 */           Class<?> listenerType = beanFactory.getType(listenerBeanName);
/* 234 */           if (listenerType == null || supportsEvent(listenerType, eventType)) {
/*     */             
/* 236 */             ApplicationListener<?> listener = (ApplicationListener)beanFactory.getBean(listenerBeanName, ApplicationListener.class);
/* 237 */             if (!allListeners.contains(listener) && supportsEvent(listener, eventType, sourceType)) {
/* 238 */               if (retriever != null) {
/* 239 */                 retriever.applicationListenerBeans.add(listenerBeanName);
/*     */               }
/* 241 */               allListeners.add(listener);
/*     */             }
/*     */           
/*     */           } 
/* 245 */         } catch (NoSuchBeanDefinitionException noSuchBeanDefinitionException) {}
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 251 */     AnnotationAwareOrderComparator.sort(allListeners);
/* 252 */     return allListeners;
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
/*     */   protected boolean supportsEvent(Class<?> listenerType, ResolvableType eventType) {
/* 267 */     if (GenericApplicationListener.class.isAssignableFrom(listenerType) || SmartApplicationListener.class
/* 268 */       .isAssignableFrom(listenerType)) {
/* 269 */       return true;
/*     */     }
/* 271 */     ResolvableType declaredEventType = GenericApplicationListenerAdapter.resolveDeclaredEventType(listenerType);
/* 272 */     return (declaredEventType == null || declaredEventType.isAssignableFrom(eventType));
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
/*     */   protected boolean supportsEvent(ApplicationListener<?> listener, ResolvableType eventType, Class<?> sourceType) {
/* 288 */     GenericApplicationListener smartListener = (listener instanceof GenericApplicationListener) ? (GenericApplicationListener)listener : new GenericApplicationListenerAdapter(listener);
/*     */     
/* 290 */     return (smartListener.supportsEventType(eventType) && smartListener.supportsSourceType(sourceType));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class ListenerCacheKey
/*     */     implements Comparable<ListenerCacheKey>
/*     */   {
/*     */     private final ResolvableType eventType;
/*     */     
/*     */     private final Class<?> sourceType;
/*     */ 
/*     */     
/*     */     public ListenerCacheKey(ResolvableType eventType, Class<?> sourceType) {
/* 304 */       this.eventType = eventType;
/* 305 */       this.sourceType = sourceType;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object other) {
/* 310 */       if (this == other) {
/* 311 */         return true;
/*     */       }
/* 313 */       ListenerCacheKey otherKey = (ListenerCacheKey)other;
/* 314 */       return (ObjectUtils.nullSafeEquals(this.eventType, otherKey.eventType) && 
/* 315 */         ObjectUtils.nullSafeEquals(this.sourceType, otherKey.sourceType));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 320 */       return ObjectUtils.nullSafeHashCode(this.eventType) * 29 + ObjectUtils.nullSafeHashCode(this.sourceType);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 325 */       return "ListenerCacheKey [eventType = " + this.eventType + ", sourceType = " + this.sourceType.getName() + "]";
/*     */     }
/*     */ 
/*     */     
/*     */     public int compareTo(ListenerCacheKey other) {
/* 330 */       int result = 0;
/* 331 */       if (this.eventType != null) {
/* 332 */         result = this.eventType.toString().compareTo(other.eventType.toString());
/*     */       }
/* 334 */       if (result == 0 && this.sourceType != null) {
/* 335 */         result = this.sourceType.getName().compareTo(other.sourceType.getName());
/*     */       }
/* 337 */       return result;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class ListenerRetriever
/*     */   {
/* 349 */     public final Set<ApplicationListener<?>> applicationListeners = new LinkedHashSet<ApplicationListener<?>>();
/*     */     
/* 351 */     public final Set<String> applicationListenerBeans = new LinkedHashSet<String>();
/*     */     
/*     */     private final boolean preFiltered;
/*     */     
/*     */     public ListenerRetriever(boolean preFiltered) {
/* 356 */       this.preFiltered = preFiltered;
/*     */     }
/*     */ 
/*     */     
/*     */     public Collection<ApplicationListener<?>> getApplicationListeners() {
/* 361 */       List<ApplicationListener<?>> allListeners = new ArrayList<ApplicationListener<?>>(this.applicationListeners.size() + this.applicationListenerBeans.size());
/* 362 */       allListeners.addAll(this.applicationListeners);
/* 363 */       if (!this.applicationListenerBeans.isEmpty()) {
/* 364 */         BeanFactory beanFactory = AbstractApplicationEventMulticaster.this.getBeanFactory();
/* 365 */         for (String listenerBeanName : this.applicationListenerBeans) {
/*     */           try {
/* 367 */             ApplicationListener<?> listener = (ApplicationListener)beanFactory.getBean(listenerBeanName, ApplicationListener.class);
/* 368 */             if (this.preFiltered || !allListeners.contains(listener)) {
/* 369 */               allListeners.add(listener);
/*     */             }
/*     */           }
/* 372 */           catch (NoSuchBeanDefinitionException noSuchBeanDefinitionException) {}
/*     */         } 
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 378 */       AnnotationAwareOrderComparator.sort(allListeners);
/* 379 */       return allListeners;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\event\AbstractApplicationEventMulticaster.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */