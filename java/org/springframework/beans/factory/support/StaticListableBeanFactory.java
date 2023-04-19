/*     */ package org.springframework.beans.factory.support;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.util.ArrayList;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.BeanCreationException;
/*     */ import org.springframework.beans.factory.BeanFactoryUtils;
/*     */ import org.springframework.beans.factory.BeanIsNotAFactoryException;
/*     */ import org.springframework.beans.factory.BeanNotOfRequiredTypeException;
/*     */ import org.springframework.beans.factory.FactoryBean;
/*     */ import org.springframework.beans.factory.ListableBeanFactory;
/*     */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*     */ import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
/*     */ import org.springframework.beans.factory.SmartFactoryBean;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.core.annotation.AnnotationUtils;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ObjectUtils;
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
/*     */ public class StaticListableBeanFactory
/*     */   implements ListableBeanFactory
/*     */ {
/*     */   private final Map<String, Object> beans;
/*     */   
/*     */   public StaticListableBeanFactory() {
/*  71 */     this.beans = new LinkedHashMap<String, Object>();
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
/*     */   public StaticListableBeanFactory(Map<String, Object> beans) {
/*  85 */     Assert.notNull(beans, "Beans Map must not be null");
/*  86 */     this.beans = beans;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addBean(String name, Object bean) {
/*  97 */     this.beans.put(name, bean);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getBean(String name) throws BeansException {
/* 107 */     String beanName = BeanFactoryUtils.transformedBeanName(name);
/* 108 */     Object bean = this.beans.get(beanName);
/*     */     
/* 110 */     if (bean == null) {
/* 111 */       throw new NoSuchBeanDefinitionException(beanName, "Defined beans are [" + 
/* 112 */           StringUtils.collectionToCommaDelimitedString(this.beans.keySet()) + "]");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 117 */     if (BeanFactoryUtils.isFactoryDereference(name) && !(bean instanceof FactoryBean)) {
/* 118 */       throw new BeanIsNotAFactoryException(beanName, bean.getClass());
/*     */     }
/*     */     
/* 121 */     if (bean instanceof FactoryBean && !BeanFactoryUtils.isFactoryDereference(name)) {
/*     */       try {
/* 123 */         return ((FactoryBean)bean).getObject();
/*     */       }
/* 125 */       catch (Exception ex) {
/* 126 */         throw new BeanCreationException(beanName, "FactoryBean threw exception on object creation", ex);
/*     */       } 
/*     */     }
/*     */     
/* 130 */     return bean;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
/* 137 */     Object bean = getBean(name);
/* 138 */     if (requiredType != null && !requiredType.isInstance(bean)) {
/* 139 */       throw new BeanNotOfRequiredTypeException(name, requiredType, bean.getClass());
/*     */     }
/* 141 */     return (T)bean;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getBean(String name, Object... args) throws BeansException {
/* 146 */     if (!ObjectUtils.isEmpty(args)) {
/* 147 */       throw new UnsupportedOperationException("StaticListableBeanFactory does not support explicit bean creation arguments");
/*     */     }
/*     */     
/* 150 */     return getBean(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T getBean(Class<T> requiredType) throws BeansException {
/* 155 */     String[] beanNames = getBeanNamesForType(requiredType);
/* 156 */     if (beanNames.length == 1) {
/* 157 */       return getBean(beanNames[0], requiredType);
/*     */     }
/* 159 */     if (beanNames.length > 1) {
/* 160 */       throw new NoUniqueBeanDefinitionException(requiredType, beanNames);
/*     */     }
/*     */     
/* 163 */     throw new NoSuchBeanDefinitionException(requiredType);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T getBean(Class<T> requiredType, Object... args) throws BeansException {
/* 169 */     if (!ObjectUtils.isEmpty(args)) {
/* 170 */       throw new UnsupportedOperationException("StaticListableBeanFactory does not support explicit bean creation arguments");
/*     */     }
/*     */     
/* 173 */     return getBean(requiredType);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsBean(String name) {
/* 178 */     return this.beans.containsKey(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
/* 183 */     Object bean = getBean(name);
/*     */     
/* 185 */     return (bean instanceof FactoryBean && ((FactoryBean)bean).isSingleton());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPrototype(String name) throws NoSuchBeanDefinitionException {
/* 190 */     Object bean = getBean(name);
/*     */     
/* 192 */     return ((bean instanceof SmartFactoryBean && ((SmartFactoryBean)bean).isPrototype()) || (bean instanceof FactoryBean && 
/* 193 */       !((FactoryBean)bean).isSingleton()));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isTypeMatch(String name, ResolvableType typeToMatch) throws NoSuchBeanDefinitionException {
/* 198 */     Class<?> type = getType(name);
/* 199 */     return (type != null && typeToMatch.isAssignableFrom(type));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isTypeMatch(String name, Class<?> typeToMatch) throws NoSuchBeanDefinitionException {
/* 204 */     Class<?> type = getType(name);
/* 205 */     return (typeToMatch == null || (type != null && typeToMatch.isAssignableFrom(type)));
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?> getType(String name) throws NoSuchBeanDefinitionException {
/* 210 */     String beanName = BeanFactoryUtils.transformedBeanName(name);
/*     */     
/* 212 */     Object bean = this.beans.get(beanName);
/* 213 */     if (bean == null) {
/* 214 */       throw new NoSuchBeanDefinitionException(beanName, "Defined beans are [" + 
/* 215 */           StringUtils.collectionToCommaDelimitedString(this.beans.keySet()) + "]");
/*     */     }
/*     */     
/* 218 */     if (bean instanceof FactoryBean && !BeanFactoryUtils.isFactoryDereference(name))
/*     */     {
/* 220 */       return ((FactoryBean)bean).getObjectType();
/*     */     }
/* 222 */     return bean.getClass();
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] getAliases(String name) {
/* 227 */     return new String[0];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsBeanDefinition(String name) {
/* 237 */     return this.beans.containsKey(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getBeanDefinitionCount() {
/* 242 */     return this.beans.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] getBeanDefinitionNames() {
/* 247 */     return StringUtils.toStringArray(this.beans.keySet());
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] getBeanNamesForType(ResolvableType type) {
/* 252 */     boolean isFactoryType = false;
/* 253 */     if (type != null) {
/* 254 */       Class<?> resolved = type.resolve();
/* 255 */       if (resolved != null && FactoryBean.class.isAssignableFrom(resolved)) {
/* 256 */         isFactoryType = true;
/*     */       }
/*     */     } 
/* 259 */     List<String> matches = new ArrayList<String>();
/* 260 */     for (Map.Entry<String, Object> entry : this.beans.entrySet()) {
/* 261 */       String name = entry.getKey();
/* 262 */       Object beanInstance = entry.getValue();
/* 263 */       if (beanInstance instanceof FactoryBean && !isFactoryType) {
/* 264 */         Class<?> objectType = ((FactoryBean)beanInstance).getObjectType();
/* 265 */         if (objectType != null && (type == null || type.isAssignableFrom(objectType))) {
/* 266 */           matches.add(name);
/*     */         }
/*     */         continue;
/*     */       } 
/* 270 */       if (type == null || type.isInstance(beanInstance)) {
/* 271 */         matches.add(name);
/*     */       }
/*     */     } 
/*     */     
/* 275 */     return StringUtils.toStringArray(matches);
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] getBeanNamesForType(Class<?> type) {
/* 280 */     return getBeanNamesForType(ResolvableType.forClass(type));
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] getBeanNamesForType(Class<?> type, boolean includeNonSingletons, boolean allowEagerInit) {
/* 285 */     return getBeanNamesForType(ResolvableType.forClass(type));
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException {
/* 290 */     return getBeansOfType(type, true, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> Map<String, T> getBeansOfType(Class<T> type, boolean includeNonSingletons, boolean allowEagerInit) throws BeansException {
/* 298 */     boolean isFactoryType = (type != null && FactoryBean.class.isAssignableFrom(type));
/* 299 */     Map<String, T> matches = new LinkedHashMap<String, T>();
/*     */     
/* 301 */     for (Map.Entry<String, Object> entry : this.beans.entrySet()) {
/* 302 */       String beanName = entry.getKey();
/* 303 */       Object beanInstance = entry.getValue();
/*     */       
/* 305 */       if (beanInstance instanceof FactoryBean && !isFactoryType) {
/*     */         
/* 307 */         FactoryBean<?> factory = (FactoryBean)beanInstance;
/* 308 */         Class<?> objectType = factory.getObjectType();
/* 309 */         if ((includeNonSingletons || factory.isSingleton()) && objectType != null && (type == null || type
/* 310 */           .isAssignableFrom(objectType))) {
/* 311 */           matches.put(beanName, getBean(beanName, type));
/*     */         }
/*     */         continue;
/*     */       } 
/* 315 */       if (type == null || type.isInstance(beanInstance)) {
/*     */ 
/*     */         
/* 318 */         if (isFactoryType) {
/* 319 */           beanName = "&" + beanName;
/*     */         }
/* 321 */         matches.put(beanName, (T)beanInstance);
/*     */       } 
/*     */     } 
/*     */     
/* 325 */     return matches;
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] getBeanNamesForAnnotation(Class<? extends Annotation> annotationType) {
/* 330 */     List<String> results = new ArrayList<String>();
/* 331 */     for (String beanName : this.beans.keySet()) {
/* 332 */       if (findAnnotationOnBean(beanName, annotationType) != null) {
/* 333 */         results.add(beanName);
/*     */       }
/*     */     } 
/* 336 */     return StringUtils.toStringArray(results);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> annotationType) throws BeansException {
/* 343 */     Map<String, Object> results = new LinkedHashMap<String, Object>();
/* 344 */     for (String beanName : this.beans.keySet()) {
/* 345 */       if (findAnnotationOnBean(beanName, annotationType) != null) {
/* 346 */         results.put(beanName, getBean(beanName));
/*     */       }
/*     */     } 
/* 349 */     return results;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <A extends Annotation> A findAnnotationOnBean(String beanName, Class<A> annotationType) throws NoSuchBeanDefinitionException {
/* 356 */     Class<?> beanType = getType(beanName);
/* 357 */     return (beanType != null) ? (A)AnnotationUtils.findAnnotation(beanType, annotationType) : null;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\support\StaticListableBeanFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */