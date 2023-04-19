/*     */ package org.springframework.beans.factory.support;
/*     */ 
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.ArrayList;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.TypeConverter;
/*     */ import org.springframework.beans.factory.BeanCreationException;
/*     */ import org.springframework.beans.factory.BeanDefinitionStoreException;
/*     */ import org.springframework.beans.factory.FactoryBean;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*     */ import org.springframework.beans.factory.config.RuntimeBeanNameReference;
/*     */ import org.springframework.beans.factory.config.RuntimeBeanReference;
/*     */ import org.springframework.beans.factory.config.TypedStringValue;
/*     */ import org.springframework.util.ClassUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class BeanDefinitionValueResolver
/*     */ {
/*     */   private final AbstractBeanFactory beanFactory;
/*     */   private final String beanName;
/*     */   private final BeanDefinition beanDefinition;
/*     */   private final TypeConverter typeConverter;
/*     */   
/*     */   public BeanDefinitionValueResolver(AbstractBeanFactory beanFactory, String beanName, BeanDefinition beanDefinition, TypeConverter typeConverter) {
/*  78 */     this.beanFactory = beanFactory;
/*  79 */     this.beanName = beanName;
/*  80 */     this.beanDefinition = beanDefinition;
/*  81 */     this.typeConverter = typeConverter;
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
/*     */   public Object resolveValueIfNecessary(Object argName, Object value) {
/* 106 */     if (value instanceof RuntimeBeanReference) {
/* 107 */       RuntimeBeanReference ref = (RuntimeBeanReference)value;
/* 108 */       return resolveReference(argName, ref);
/*     */     } 
/* 110 */     if (value instanceof RuntimeBeanNameReference) {
/* 111 */       String refName = ((RuntimeBeanNameReference)value).getBeanName();
/* 112 */       refName = String.valueOf(doEvaluate(refName));
/* 113 */       if (!this.beanFactory.containsBean(refName)) {
/* 114 */         throw new BeanDefinitionStoreException("Invalid bean name '" + refName + "' in bean reference for " + argName);
/*     */       }
/*     */       
/* 117 */       return refName;
/*     */     } 
/* 119 */     if (value instanceof BeanDefinitionHolder) {
/*     */       
/* 121 */       BeanDefinitionHolder bdHolder = (BeanDefinitionHolder)value;
/* 122 */       return resolveInnerBean(argName, bdHolder.getBeanName(), bdHolder.getBeanDefinition());
/*     */     } 
/* 124 */     if (value instanceof BeanDefinition) {
/*     */       
/* 126 */       BeanDefinition bd = (BeanDefinition)value;
/*     */       
/* 128 */       String innerBeanName = "(inner bean)#" + ObjectUtils.getIdentityHexString(bd);
/* 129 */       return resolveInnerBean(argName, innerBeanName, bd);
/*     */     } 
/* 131 */     if (value instanceof ManagedArray) {
/*     */       
/* 133 */       ManagedArray array = (ManagedArray)value;
/* 134 */       Class<?> elementType = array.resolvedElementType;
/* 135 */       if (elementType == null) {
/* 136 */         String elementTypeName = array.getElementTypeName();
/* 137 */         if (StringUtils.hasText(elementTypeName)) {
/*     */           try {
/* 139 */             elementType = ClassUtils.forName(elementTypeName, this.beanFactory.getBeanClassLoader());
/* 140 */             array.resolvedElementType = elementType;
/*     */           }
/* 142 */           catch (Throwable ex) {
/*     */             
/* 144 */             throw new BeanCreationException(this.beanDefinition
/* 145 */                 .getResourceDescription(), this.beanName, "Error resolving array type for " + argName, ex);
/*     */           }
/*     */         
/*     */         } else {
/*     */           
/* 150 */           elementType = Object.class;
/*     */         } 
/*     */       } 
/* 153 */       return resolveManagedArray(argName, (List)value, elementType);
/*     */     } 
/* 155 */     if (value instanceof ManagedList)
/*     */     {
/* 157 */       return resolveManagedList(argName, (List)value);
/*     */     }
/* 159 */     if (value instanceof ManagedSet)
/*     */     {
/* 161 */       return resolveManagedSet(argName, (Set)value);
/*     */     }
/* 163 */     if (value instanceof ManagedMap)
/*     */     {
/* 165 */       return resolveManagedMap(argName, (Map<?, ?>)value);
/*     */     }
/* 167 */     if (value instanceof ManagedProperties) {
/* 168 */       Properties original = (Properties)value;
/* 169 */       Properties copy = new Properties();
/* 170 */       for (Map.Entry<Object, Object> propEntry : original.entrySet()) {
/* 171 */         Object propKey = propEntry.getKey();
/* 172 */         Object propValue = propEntry.getValue();
/* 173 */         if (propKey instanceof TypedStringValue) {
/* 174 */           propKey = evaluate((TypedStringValue)propKey);
/*     */         }
/* 176 */         if (propValue instanceof TypedStringValue) {
/* 177 */           propValue = evaluate((TypedStringValue)propValue);
/*     */         }
/* 179 */         copy.put(propKey, propValue);
/*     */       } 
/* 181 */       return copy;
/*     */     } 
/* 183 */     if (value instanceof TypedStringValue) {
/*     */       
/* 185 */       TypedStringValue typedStringValue = (TypedStringValue)value;
/* 186 */       Object valueObject = evaluate(typedStringValue);
/*     */       try {
/* 188 */         Class<?> resolvedTargetType = resolveTargetType(typedStringValue);
/* 189 */         if (resolvedTargetType != null) {
/* 190 */           return this.typeConverter.convertIfNecessary(valueObject, resolvedTargetType);
/*     */         }
/*     */         
/* 193 */         return valueObject;
/*     */       
/*     */       }
/* 196 */       catch (Throwable ex) {
/*     */         
/* 198 */         throw new BeanCreationException(this.beanDefinition
/* 199 */             .getResourceDescription(), this.beanName, "Error converting typed String value for " + argName, ex);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 204 */     return evaluate(value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object evaluate(TypedStringValue value) {
/* 214 */     Object result = doEvaluate(value.getValue());
/* 215 */     if (!ObjectUtils.nullSafeEquals(result, value.getValue())) {
/* 216 */       value.setDynamic();
/*     */     }
/* 218 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object evaluate(Object value) {
/* 227 */     if (value instanceof String) {
/* 228 */       return doEvaluate((String)value);
/*     */     }
/* 230 */     if (value instanceof String[]) {
/* 231 */       String[] values = (String[])value;
/* 232 */       boolean actuallyResolved = false;
/* 233 */       Object[] resolvedValues = new Object[values.length];
/* 234 */       for (int i = 0; i < values.length; i++) {
/* 235 */         String originalValue = values[i];
/* 236 */         Object resolvedValue = doEvaluate(originalValue);
/* 237 */         if (resolvedValue != originalValue) {
/* 238 */           actuallyResolved = true;
/*     */         }
/* 240 */         resolvedValues[i] = resolvedValue;
/*     */       } 
/* 242 */       return actuallyResolved ? resolvedValues : values;
/*     */     } 
/*     */     
/* 245 */     return value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Object doEvaluate(String value) {
/* 255 */     return this.beanFactory.evaluateBeanDefinitionString(value, this.beanDefinition);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Class<?> resolveTargetType(TypedStringValue value) throws ClassNotFoundException {
/* 266 */     if (value.hasTargetType()) {
/* 267 */       return value.getTargetType();
/*     */     }
/* 269 */     return value.resolveTargetType(this.beanFactory.getBeanClassLoader());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Object resolveInnerBean(Object argName, String innerBeanName, BeanDefinition innerBd) {
/* 280 */     RootBeanDefinition mbd = null;
/*     */     try {
/* 282 */       mbd = this.beanFactory.getMergedBeanDefinition(innerBeanName, innerBd, this.beanDefinition);
/*     */ 
/*     */       
/* 285 */       String actualInnerBeanName = innerBeanName;
/* 286 */       if (mbd.isSingleton()) {
/* 287 */         actualInnerBeanName = adaptInnerBeanName(innerBeanName);
/*     */       }
/* 289 */       this.beanFactory.registerContainedBean(actualInnerBeanName, this.beanName);
/*     */       
/* 291 */       String[] dependsOn = mbd.getDependsOn();
/* 292 */       if (dependsOn != null) {
/* 293 */         for (String dependsOnBean : dependsOn) {
/* 294 */           this.beanFactory.registerDependentBean(dependsOnBean, actualInnerBeanName);
/* 295 */           this.beanFactory.getBean(dependsOnBean);
/*     */         } 
/*     */       }
/*     */       
/* 299 */       Object innerBean = this.beanFactory.createBean(actualInnerBeanName, mbd, (Object[])null);
/* 300 */       if (innerBean instanceof FactoryBean) {
/* 301 */         boolean synthetic = mbd.isSynthetic();
/* 302 */         return this.beanFactory.getObjectFromFactoryBean((FactoryBean)innerBean, actualInnerBeanName, !synthetic);
/*     */       } 
/*     */ 
/*     */       
/* 306 */       return innerBean;
/*     */     
/*     */     }
/* 309 */     catch (BeansException ex) {
/* 310 */       throw new BeanCreationException(this.beanDefinition
/* 311 */           .getResourceDescription(), this.beanName, "Cannot create inner bean '" + innerBeanName + "' " + ((mbd != null && mbd
/*     */           
/* 313 */           .getBeanClassName() != null) ? ("of type [" + mbd.getBeanClassName() + "] ") : "") + "while setting " + argName, ex);
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
/*     */   private String adaptInnerBeanName(String innerBeanName) {
/* 325 */     String actualInnerBeanName = innerBeanName;
/* 326 */     int counter = 0;
/* 327 */     while (this.beanFactory.isBeanNameInUse(actualInnerBeanName)) {
/* 328 */       counter++;
/* 329 */       actualInnerBeanName = innerBeanName + "#" + counter;
/*     */     } 
/* 331 */     return actualInnerBeanName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Object resolveReference(Object argName, RuntimeBeanReference ref) {
/*     */     try {
/* 339 */       String refName = ref.getBeanName();
/* 340 */       refName = String.valueOf(doEvaluate(refName));
/* 341 */       if (ref.isToParent()) {
/* 342 */         if (this.beanFactory.getParentBeanFactory() == null) {
/* 343 */           throw new BeanCreationException(this.beanDefinition
/* 344 */               .getResourceDescription(), this.beanName, "Can't resolve reference to bean '" + refName + "' in parent factory: no parent factory available");
/*     */         }
/*     */ 
/*     */         
/* 348 */         return this.beanFactory.getParentBeanFactory().getBean(refName);
/*     */       } 
/*     */       
/* 351 */       Object bean = this.beanFactory.getBean(refName);
/* 352 */       this.beanFactory.registerDependentBean(refName, this.beanName);
/* 353 */       return bean;
/*     */     
/*     */     }
/* 356 */     catch (BeansException ex) {
/* 357 */       throw new BeanCreationException(this.beanDefinition
/* 358 */           .getResourceDescription(), this.beanName, "Cannot resolve reference to bean '" + ref
/* 359 */           .getBeanName() + "' while setting " + argName, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Object resolveManagedArray(Object argName, List<?> ml, Class<?> elementType) {
/* 367 */     Object resolved = Array.newInstance(elementType, ml.size());
/* 368 */     for (int i = 0; i < ml.size(); i++) {
/* 369 */       Array.set(resolved, i, 
/* 370 */           resolveValueIfNecessary(new KeyedArgName(argName, Integer.valueOf(i)), ml.get(i)));
/*     */     }
/* 372 */     return resolved;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<?> resolveManagedList(Object argName, List<?> ml) {
/* 379 */     List<Object> resolved = new ArrayList(ml.size());
/* 380 */     for (int i = 0; i < ml.size(); i++) {
/* 381 */       resolved.add(
/* 382 */           resolveValueIfNecessary(new KeyedArgName(argName, Integer.valueOf(i)), ml.get(i)));
/*     */     }
/* 384 */     return resolved;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Set<?> resolveManagedSet(Object argName, Set<?> ms) {
/* 391 */     Set<Object> resolved = new LinkedHashSet(ms.size());
/* 392 */     int i = 0;
/* 393 */     for (Object m : ms) {
/* 394 */       resolved.add(resolveValueIfNecessary(new KeyedArgName(argName, Integer.valueOf(i)), m));
/* 395 */       i++;
/*     */     } 
/* 397 */     return resolved;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Map<?, ?> resolveManagedMap(Object argName, Map<?, ?> mm) {
/* 404 */     Map<Object, Object> resolved = new LinkedHashMap<Object, Object>(mm.size());
/* 405 */     for (Map.Entry<?, ?> entry : mm.entrySet()) {
/* 406 */       Object resolvedKey = resolveValueIfNecessary(argName, entry.getKey());
/* 407 */       Object resolvedValue = resolveValueIfNecessary(new KeyedArgName(argName, entry
/* 408 */             .getKey()), entry.getValue());
/* 409 */       resolved.put(resolvedKey, resolvedValue);
/*     */     } 
/* 411 */     return resolved;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class KeyedArgName
/*     */   {
/*     */     private final Object argName;
/*     */ 
/*     */     
/*     */     private final Object key;
/*     */ 
/*     */     
/*     */     public KeyedArgName(Object argName, Object key) {
/* 425 */       this.argName = argName;
/* 426 */       this.key = key;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 431 */       return this.argName + " with key " + "[" + this.key + "]";
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\support\BeanDefinitionValueResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */