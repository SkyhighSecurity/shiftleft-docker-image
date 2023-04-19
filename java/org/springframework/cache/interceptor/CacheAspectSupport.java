/*     */ package org.springframework.cache.interceptor;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.aop.framework.AopProxyUtils;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryAware;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*     */ import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
/*     */ import org.springframework.beans.factory.SmartInitializingSingleton;
/*     */ import org.springframework.beans.factory.annotation.BeanFactoryAnnotationUtils;
/*     */ import org.springframework.cache.Cache;
/*     */ import org.springframework.cache.CacheManager;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.context.expression.AnnotatedElementKey;
/*     */ import org.springframework.expression.EvaluationContext;
/*     */ import org.springframework.lang.UsesJava8;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.util.LinkedMultiValueMap;
/*     */ import org.springframework.util.MultiValueMap;
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
/*     */ public abstract class CacheAspectSupport
/*     */   extends AbstractCacheInvoker
/*     */   implements BeanFactoryAware, InitializingBean, SmartInitializingSingleton
/*     */ {
/*  82 */   private static Class<?> javaUtilOptionalClass = null;
/*     */ 
/*     */   
/*     */   static {
/*     */     try {
/*  87 */       javaUtilOptionalClass = ClassUtils.forName("java.util.Optional", CacheAspectSupport.class.getClassLoader());
/*     */     }
/*  89 */     catch (ClassNotFoundException classNotFoundException) {}
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  94 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*  96 */   private final Map<CacheOperationCacheKey, CacheOperationMetadata> metadataCache = new ConcurrentHashMap<CacheOperationCacheKey, CacheOperationMetadata>(1024);
/*     */ 
/*     */   
/*  99 */   private final CacheOperationExpressionEvaluator evaluator = new CacheOperationExpressionEvaluator();
/*     */   
/*     */   private CacheOperationSource cacheOperationSource;
/*     */   
/* 103 */   private KeyGenerator keyGenerator = new SimpleKeyGenerator();
/*     */ 
/*     */ 
/*     */   
/*     */   private CacheResolver cacheResolver;
/*     */ 
/*     */   
/*     */   private BeanFactory beanFactory;
/*     */ 
/*     */   
/*     */   private boolean initialized = false;
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCacheOperationSources(CacheOperationSource... cacheOperationSources) {
/* 118 */     Assert.notEmpty((Object[])cacheOperationSources, "At least 1 CacheOperationSource needs to be specified");
/* 119 */     this.cacheOperationSource = (cacheOperationSources.length > 1) ? new CompositeCacheOperationSource(cacheOperationSources) : cacheOperationSources[0];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CacheOperationSource getCacheOperationSource() {
/* 127 */     return this.cacheOperationSource;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setKeyGenerator(KeyGenerator keyGenerator) {
/* 136 */     this.keyGenerator = keyGenerator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public KeyGenerator getKeyGenerator() {
/* 143 */     return this.keyGenerator;
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
/*     */   public void setCacheResolver(CacheResolver cacheResolver) {
/* 155 */     this.cacheResolver = cacheResolver;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CacheResolver getCacheResolver() {
/* 162 */     return this.cacheResolver;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCacheManager(CacheManager cacheManager) {
/* 172 */     this.cacheResolver = new SimpleCacheResolver(cacheManager);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBeanFactory(BeanFactory beanFactory) {
/* 182 */     this.beanFactory = beanFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void setApplicationContext(ApplicationContext applicationContext) {
/* 190 */     this.beanFactory = (BeanFactory)applicationContext;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() {
/* 196 */     Assert.state((getCacheOperationSource() != null), "The 'cacheOperationSources' property is required: If there are no cacheable methods, then don't use a cache aspect.");
/*     */     
/* 198 */     Assert.state((getErrorHandler() != null), "The 'errorHandler' property is required");
/*     */   }
/*     */ 
/*     */   
/*     */   public void afterSingletonsInstantiated() {
/* 203 */     if (getCacheResolver() == null) {
/*     */       
/*     */       try {
/* 206 */         setCacheManager((CacheManager)this.beanFactory.getBean(CacheManager.class));
/*     */       }
/* 208 */       catch (NoUniqueBeanDefinitionException ex) {
/* 209 */         throw new IllegalStateException("No CacheResolver specified, and no unique bean of type CacheManager found. Mark one as primary or declare a specific CacheManager to use.");
/*     */       
/*     */       }
/* 212 */       catch (NoSuchBeanDefinitionException ex) {
/* 213 */         throw new IllegalStateException("No CacheResolver specified, and no bean of type CacheManager found. Register a CacheManager bean or remove the @EnableCaching annotation from your configuration.");
/*     */       } 
/*     */     }
/*     */     
/* 217 */     this.initialized = true;
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
/*     */   protected String methodIdentification(Method method, Class<?> targetClass) {
/* 231 */     Method specificMethod = ClassUtils.getMostSpecificMethod(method, targetClass);
/* 232 */     return ClassUtils.getQualifiedMethodName(specificMethod);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Collection<? extends Cache> getCaches(CacheOperationInvocationContext<CacheOperation> context, CacheResolver cacheResolver) {
/* 238 */     Collection<? extends Cache> caches = cacheResolver.resolveCaches(context);
/* 239 */     if (caches.isEmpty()) {
/* 240 */       throw new IllegalStateException("No cache could be resolved for '" + context
/* 241 */           .getOperation() + "' using resolver '" + cacheResolver + "'. At least one cache should be provided per cache operation.");
/*     */     }
/*     */     
/* 244 */     return caches;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected CacheOperationContext getOperationContext(CacheOperation operation, Method method, Object[] args, Object target, Class<?> targetClass) {
/* 250 */     CacheOperationMetadata metadata = getCacheOperationMetadata(operation, method, targetClass);
/* 251 */     return new CacheOperationContext(metadata, args, target);
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
/*     */   protected CacheOperationMetadata getCacheOperationMetadata(CacheOperation operation, Method method, Class<?> targetClass) {
/* 266 */     CacheOperationCacheKey cacheKey = new CacheOperationCacheKey(operation, method, targetClass);
/* 267 */     CacheOperationMetadata metadata = this.metadataCache.get(cacheKey);
/* 268 */     if (metadata == null) {
/*     */       KeyGenerator operationKeyGenerator; CacheResolver operationCacheResolver;
/* 270 */       if (StringUtils.hasText(operation.getKeyGenerator())) {
/* 271 */         operationKeyGenerator = getBean(operation.getKeyGenerator(), KeyGenerator.class);
/*     */       } else {
/*     */         
/* 274 */         operationKeyGenerator = getKeyGenerator();
/*     */       } 
/*     */       
/* 277 */       if (StringUtils.hasText(operation.getCacheResolver())) {
/* 278 */         operationCacheResolver = getBean(operation.getCacheResolver(), CacheResolver.class);
/*     */       }
/* 280 */       else if (StringUtils.hasText(operation.getCacheManager())) {
/* 281 */         CacheManager cacheManager = getBean(operation.getCacheManager(), CacheManager.class);
/* 282 */         operationCacheResolver = new SimpleCacheResolver(cacheManager);
/*     */       } else {
/*     */         
/* 285 */         operationCacheResolver = getCacheResolver();
/*     */       } 
/* 287 */       metadata = new CacheOperationMetadata(operation, method, targetClass, operationKeyGenerator, operationCacheResolver);
/*     */       
/* 289 */       this.metadataCache.put(cacheKey, metadata);
/*     */     } 
/* 291 */     return metadata;
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
/*     */   protected <T> T getBean(String beanName, Class<T> expectedType) {
/* 306 */     return (T)BeanFactoryAnnotationUtils.qualifiedBeanOfType(this.beanFactory, expectedType, beanName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void clearMetadataCache() {
/* 313 */     this.metadataCache.clear();
/* 314 */     this.evaluator.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   protected Object execute(CacheOperationInvoker invoker, Object target, Method method, Object[] args) {
/* 319 */     if (this.initialized) {
/* 320 */       Class<?> targetClass = getTargetClass(target);
/* 321 */       Collection<CacheOperation> operations = getCacheOperationSource().getCacheOperations(method, targetClass);
/* 322 */       if (!CollectionUtils.isEmpty(operations)) {
/* 323 */         return execute(invoker, method, new CacheOperationContexts(operations, method, args, target, targetClass));
/*     */       }
/*     */     } 
/*     */     
/* 327 */     return invoker.invoke();
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
/*     */   protected Object invokeOperation(CacheOperationInvoker invoker) {
/* 341 */     return invoker.invoke();
/*     */   }
/*     */   
/*     */   private Class<?> getTargetClass(Object target) {
/* 345 */     Class<?> targetClass = AopProxyUtils.ultimateTargetClass(target);
/* 346 */     if (targetClass == null && target != null) {
/* 347 */       targetClass = target.getClass();
/*     */     }
/* 349 */     return targetClass;
/*     */   }
/*     */   
/*     */   private Object execute(final CacheOperationInvoker invoker, Method method, CacheOperationContexts contexts) {
/*     */     Object cacheValue, returnValue;
/* 354 */     if (contexts.isSynchronized()) {
/* 355 */       CacheOperationContext context = contexts.get((Class)CacheableOperation.class).iterator().next();
/* 356 */       if (isConditionPassing(context, CacheOperationExpressionEvaluator.NO_RESULT)) {
/* 357 */         Object key = generateKey(context, CacheOperationExpressionEvaluator.NO_RESULT);
/* 358 */         Cache cache = context.getCaches().iterator().next();
/*     */         try {
/* 360 */           return wrapCacheValue(method, cache.get(key, new Callable()
/*     */                 {
/*     */                   public Object call() throws Exception {
/* 363 */                     return CacheAspectSupport.this.unwrapReturnValue(CacheAspectSupport.this.invokeOperation(invoker));
/*     */                   }
/*     */                 }));
/*     */         }
/* 367 */         catch (org.springframework.cache.Cache.ValueRetrievalException ex) {
/*     */ 
/*     */           
/* 370 */           throw (CacheOperationInvoker.ThrowableWrapper)ex.getCause();
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 375 */       return invokeOperation(invoker);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 381 */     processCacheEvicts(contexts.get((Class)CacheEvictOperation.class), true, CacheOperationExpressionEvaluator.NO_RESULT);
/*     */ 
/*     */ 
/*     */     
/* 385 */     Cache.ValueWrapper cacheHit = findCachedItem(contexts.get((Class)CacheableOperation.class));
/*     */ 
/*     */     
/* 388 */     List<CachePutRequest> cachePutRequests = new LinkedList<CachePutRequest>();
/* 389 */     if (cacheHit == null) {
/* 390 */       collectPutRequests(contexts.get((Class)CacheableOperation.class), CacheOperationExpressionEvaluator.NO_RESULT, cachePutRequests);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 397 */     if (cacheHit != null && cachePutRequests.isEmpty() && !hasCachePut(contexts)) {
/*     */       
/* 399 */       cacheValue = cacheHit.get();
/* 400 */       returnValue = wrapCacheValue(method, cacheValue);
/*     */     }
/*     */     else {
/*     */       
/* 404 */       returnValue = invokeOperation(invoker);
/* 405 */       cacheValue = unwrapReturnValue(returnValue);
/*     */     } 
/*     */ 
/*     */     
/* 409 */     collectPutRequests(contexts.get((Class)CachePutOperation.class), cacheValue, cachePutRequests);
/*     */ 
/*     */     
/* 412 */     for (CachePutRequest cachePutRequest : cachePutRequests) {
/* 413 */       cachePutRequest.apply(cacheValue);
/*     */     }
/*     */ 
/*     */     
/* 417 */     processCacheEvicts(contexts.get((Class)CacheEvictOperation.class), false, cacheValue);
/*     */     
/* 419 */     return returnValue;
/*     */   }
/*     */   
/*     */   private Object wrapCacheValue(Method method, Object cacheValue) {
/* 423 */     if (method.getReturnType() == javaUtilOptionalClass && (cacheValue == null || cacheValue
/* 424 */       .getClass() != javaUtilOptionalClass)) {
/* 425 */       return OptionalUnwrapper.wrap(cacheValue);
/*     */     }
/* 427 */     return cacheValue;
/*     */   }
/*     */   
/*     */   private Object unwrapReturnValue(Object returnValue) {
/* 431 */     if (returnValue != null && returnValue.getClass() == javaUtilOptionalClass) {
/* 432 */       return OptionalUnwrapper.unwrap(returnValue);
/*     */     }
/* 434 */     return returnValue;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean hasCachePut(CacheOperationContexts contexts) {
/* 439 */     Collection<CacheOperationContext> cachePutContexts = contexts.get((Class)CachePutOperation.class);
/* 440 */     Collection<CacheOperationContext> excluded = new ArrayList<CacheOperationContext>();
/* 441 */     for (CacheOperationContext context : cachePutContexts) {
/*     */       try {
/* 443 */         if (!context.isConditionPassing(CacheOperationExpressionEvaluator.RESULT_UNAVAILABLE)) {
/* 444 */           excluded.add(context);
/*     */         }
/*     */       }
/* 447 */       catch (VariableNotAvailableException variableNotAvailableException) {}
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 452 */     return (cachePutContexts.size() != excluded.size());
/*     */   }
/*     */   
/*     */   private void processCacheEvicts(Collection<CacheOperationContext> contexts, boolean beforeInvocation, Object result) {
/* 456 */     for (CacheOperationContext context : contexts) {
/* 457 */       CacheEvictOperation operation = (CacheEvictOperation)context.metadata.operation;
/* 458 */       if (beforeInvocation == operation.isBeforeInvocation() && isConditionPassing(context, result)) {
/* 459 */         performCacheEvict(context, operation, result);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void performCacheEvict(CacheOperationContext context, CacheEvictOperation operation, Object result) {
/* 465 */     Object key = null;
/* 466 */     for (Cache cache : context.getCaches()) {
/* 467 */       if (operation.isCacheWide()) {
/* 468 */         logInvalidating(context, operation, (Object)null);
/* 469 */         doClear(cache);
/*     */         continue;
/*     */       } 
/* 472 */       if (key == null) {
/* 473 */         key = context.generateKey(result);
/*     */       }
/* 475 */       logInvalidating(context, operation, key);
/* 476 */       doEvict(cache, key);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void logInvalidating(CacheOperationContext context, CacheEvictOperation operation, Object key) {
/* 482 */     if (this.logger.isTraceEnabled()) {
/* 483 */       this.logger.trace("Invalidating " + ((key != null) ? ("cache key [" + key + "]") : "entire cache") + " for operation " + operation + " on method " + 
/* 484 */           context.metadata.method);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Cache.ValueWrapper findCachedItem(Collection<CacheOperationContext> contexts) {
/* 495 */     Object result = CacheOperationExpressionEvaluator.NO_RESULT;
/* 496 */     for (CacheOperationContext context : contexts) {
/* 497 */       if (isConditionPassing(context, result)) {
/* 498 */         Object key = generateKey(context, result);
/* 499 */         Cache.ValueWrapper cached = findInCaches(context, key);
/* 500 */         if (cached != null) {
/* 501 */           return cached;
/*     */         }
/*     */         
/* 504 */         if (this.logger.isTraceEnabled()) {
/* 505 */           this.logger.trace("No cache entry for key '" + key + "' in cache(s) " + context.getCacheNames());
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 510 */     return null;
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
/*     */   private void collectPutRequests(Collection<CacheOperationContext> contexts, Object result, Collection<CachePutRequest> putRequests) {
/* 523 */     for (CacheOperationContext context : contexts) {
/* 524 */       if (isConditionPassing(context, result)) {
/* 525 */         Object key = generateKey(context, result);
/* 526 */         putRequests.add(new CachePutRequest(context, key));
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private Cache.ValueWrapper findInCaches(CacheOperationContext context, Object key) {
/* 532 */     for (Cache cache : context.getCaches()) {
/* 533 */       Cache.ValueWrapper wrapper = doGet(cache, key);
/* 534 */       if (wrapper != null) {
/* 535 */         if (this.logger.isTraceEnabled()) {
/* 536 */           this.logger.trace("Cache entry for key '" + key + "' found in cache '" + cache.getName() + "'");
/*     */         }
/* 538 */         return wrapper;
/*     */       } 
/*     */     } 
/* 541 */     return null;
/*     */   }
/*     */   
/*     */   private boolean isConditionPassing(CacheOperationContext context, Object result) {
/* 545 */     boolean passing = context.isConditionPassing(result);
/* 546 */     if (!passing && this.logger.isTraceEnabled()) {
/* 547 */       this.logger.trace("Cache condition failed on method " + context.metadata.method + " for operation " + 
/* 548 */           context.metadata.operation);
/*     */     }
/* 550 */     return passing;
/*     */   }
/*     */   
/*     */   private Object generateKey(CacheOperationContext context, Object result) {
/* 554 */     Object key = context.generateKey(result);
/* 555 */     if (key == null) {
/* 556 */       throw new IllegalArgumentException("Null key returned for cache operation (maybe you are using named params on classes without debug info?) " + 
/* 557 */           context.metadata.operation);
/*     */     }
/* 559 */     if (this.logger.isTraceEnabled()) {
/* 560 */       this.logger.trace("Computed cache key '" + key + "' for operation " + context.metadata.operation);
/*     */     }
/* 562 */     return key;
/*     */   }
/*     */ 
/*     */   
/*     */   private class CacheOperationContexts
/*     */   {
/* 568 */     private final MultiValueMap<Class<? extends CacheOperation>, CacheAspectSupport.CacheOperationContext> contexts = (MultiValueMap<Class<? extends CacheOperation>, CacheAspectSupport.CacheOperationContext>)new LinkedMultiValueMap();
/*     */ 
/*     */     
/*     */     private final boolean sync;
/*     */ 
/*     */ 
/*     */     
/*     */     public CacheOperationContexts(Collection<? extends CacheOperation> operations, Method method, Object[] args, Object target, Class<?> targetClass) {
/* 576 */       for (CacheOperation operation : operations) {
/* 577 */         this.contexts.add(operation.getClass(), CacheAspectSupport.this.getOperationContext(operation, method, args, target, targetClass));
/*     */       }
/* 579 */       this.sync = determineSyncFlag(method);
/*     */     }
/*     */     
/*     */     public Collection<CacheAspectSupport.CacheOperationContext> get(Class<? extends CacheOperation> operationClass) {
/* 583 */       Collection<CacheAspectSupport.CacheOperationContext> result = (Collection<CacheAspectSupport.CacheOperationContext>)this.contexts.get(operationClass);
/* 584 */       return (result != null) ? result : Collections.<CacheAspectSupport.CacheOperationContext>emptyList();
/*     */     }
/*     */     
/*     */     public boolean isSynchronized() {
/* 588 */       return this.sync;
/*     */     }
/*     */     
/*     */     private boolean determineSyncFlag(Method method) {
/* 592 */       List<CacheAspectSupport.CacheOperationContext> cacheOperationContexts = (List<CacheAspectSupport.CacheOperationContext>)this.contexts.get(CacheableOperation.class);
/* 593 */       if (cacheOperationContexts == null) {
/* 594 */         return false;
/*     */       }
/* 596 */       boolean syncEnabled = false;
/* 597 */       for (CacheAspectSupport.CacheOperationContext cacheOperationContext : cacheOperationContexts) {
/* 598 */         if (((CacheableOperation)cacheOperationContext.getOperation()).isSync()) {
/* 599 */           syncEnabled = true;
/*     */           break;
/*     */         } 
/*     */       } 
/* 603 */       if (syncEnabled) {
/* 604 */         if (this.contexts.size() > 1) {
/* 605 */           throw new IllegalStateException("@Cacheable(sync=true) cannot be combined with other cache operations on '" + method + "'");
/*     */         }
/* 607 */         if (cacheOperationContexts.size() > 1) {
/* 608 */           throw new IllegalStateException("Only one @Cacheable(sync=true) entry is allowed on '" + method + "'");
/*     */         }
/* 610 */         CacheAspectSupport.CacheOperationContext cacheOperationContext = cacheOperationContexts.iterator().next();
/* 611 */         CacheableOperation operation = (CacheableOperation)cacheOperationContext.getOperation();
/* 612 */         if (cacheOperationContext.getCaches().size() > 1) {
/* 613 */           throw new IllegalStateException("@Cacheable(sync=true) only allows a single cache on '" + operation + "'");
/*     */         }
/* 615 */         if (StringUtils.hasText(operation.getUnless())) {
/* 616 */           throw new IllegalStateException("@Cacheable(sync=true) does not support unless attribute on '" + operation + "'");
/*     */         }
/* 618 */         return true;
/*     */       } 
/* 620 */       return false;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected static class CacheOperationMetadata
/*     */   {
/*     */     private final CacheOperation operation;
/*     */ 
/*     */     
/*     */     private final Method method;
/*     */ 
/*     */     
/*     */     private final Class<?> targetClass;
/*     */ 
/*     */     
/*     */     private final KeyGenerator keyGenerator;
/*     */ 
/*     */     
/*     */     private final CacheResolver cacheResolver;
/*     */ 
/*     */     
/*     */     public CacheOperationMetadata(CacheOperation operation, Method method, Class<?> targetClass, KeyGenerator keyGenerator, CacheResolver cacheResolver) {
/* 644 */       this.operation = operation;
/* 645 */       this.method = method;
/* 646 */       this.targetClass = targetClass;
/* 647 */       this.keyGenerator = keyGenerator;
/* 648 */       this.cacheResolver = cacheResolver;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected class CacheOperationContext
/*     */     implements CacheOperationInvocationContext<CacheOperation>
/*     */   {
/*     */     private final CacheAspectSupport.CacheOperationMetadata metadata;
/*     */     
/*     */     private final Object[] args;
/*     */     
/*     */     private final Object target;
/*     */     
/*     */     private final Collection<? extends Cache> caches;
/*     */     
/*     */     private final Collection<String> cacheNames;
/*     */     
/*     */     private final AnnotatedElementKey methodCacheKey;
/*     */ 
/*     */     
/*     */     public CacheOperationContext(CacheAspectSupport.CacheOperationMetadata metadata, Object[] args, Object target) {
/* 671 */       this.metadata = metadata;
/* 672 */       this.args = extractArgs(metadata.method, args);
/* 673 */       this.target = target;
/* 674 */       this.caches = CacheAspectSupport.this.getCaches(this, metadata.cacheResolver);
/* 675 */       this.cacheNames = createCacheNames(this.caches);
/* 676 */       this.methodCacheKey = new AnnotatedElementKey(metadata.method, metadata.targetClass);
/*     */     }
/*     */ 
/*     */     
/*     */     public CacheOperation getOperation() {
/* 681 */       return this.metadata.operation;
/*     */     }
/*     */ 
/*     */     
/*     */     public Object getTarget() {
/* 686 */       return this.target;
/*     */     }
/*     */ 
/*     */     
/*     */     public Method getMethod() {
/* 691 */       return this.metadata.method;
/*     */     }
/*     */ 
/*     */     
/*     */     public Object[] getArgs() {
/* 696 */       return this.args;
/*     */     }
/*     */     
/*     */     private Object[] extractArgs(Method method, Object[] args) {
/* 700 */       if (!method.isVarArgs()) {
/* 701 */         return args;
/*     */       }
/* 703 */       Object[] varArgs = ObjectUtils.toObjectArray(args[args.length - 1]);
/* 704 */       Object[] combinedArgs = new Object[args.length - 1 + varArgs.length];
/* 705 */       System.arraycopy(args, 0, combinedArgs, 0, args.length - 1);
/* 706 */       System.arraycopy(varArgs, 0, combinedArgs, args.length - 1, varArgs.length);
/* 707 */       return combinedArgs;
/*     */     }
/*     */     
/*     */     protected boolean isConditionPassing(Object result) {
/* 711 */       if (StringUtils.hasText(this.metadata.operation.getCondition())) {
/* 712 */         EvaluationContext evaluationContext = createEvaluationContext(result);
/* 713 */         return CacheAspectSupport.this.evaluator.condition(this.metadata.operation.getCondition(), this.methodCacheKey, evaluationContext);
/*     */       } 
/*     */       
/* 716 */       return true;
/*     */     }
/*     */     
/*     */     protected boolean canPutToCache(Object value) {
/* 720 */       String unless = "";
/* 721 */       if (this.metadata.operation instanceof CacheableOperation) {
/* 722 */         unless = ((CacheableOperation)this.metadata.operation).getUnless();
/*     */       }
/* 724 */       else if (this.metadata.operation instanceof CachePutOperation) {
/* 725 */         unless = ((CachePutOperation)this.metadata.operation).getUnless();
/*     */       } 
/* 727 */       if (StringUtils.hasText(unless)) {
/* 728 */         EvaluationContext evaluationContext = createEvaluationContext(value);
/* 729 */         return !CacheAspectSupport.this.evaluator.unless(unless, this.methodCacheKey, evaluationContext);
/*     */       } 
/* 731 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected Object generateKey(Object result) {
/* 739 */       if (StringUtils.hasText(this.metadata.operation.getKey())) {
/* 740 */         EvaluationContext evaluationContext = createEvaluationContext(result);
/* 741 */         return CacheAspectSupport.this.evaluator.key(this.metadata.operation.getKey(), this.methodCacheKey, evaluationContext);
/*     */       } 
/* 743 */       return this.metadata.keyGenerator.generate(this.target, this.metadata.method, this.args);
/*     */     }
/*     */     
/*     */     private EvaluationContext createEvaluationContext(Object result) {
/* 747 */       return CacheAspectSupport.this.evaluator.createEvaluationContext(this.caches, this.metadata.method, this.args, this.target, this.metadata
/* 748 */           .targetClass, result, CacheAspectSupport.this.beanFactory);
/*     */     }
/*     */     
/*     */     protected Collection<? extends Cache> getCaches() {
/* 752 */       return this.caches;
/*     */     }
/*     */     
/*     */     protected Collection<String> getCacheNames() {
/* 756 */       return this.cacheNames;
/*     */     }
/*     */     
/*     */     private Collection<String> createCacheNames(Collection<? extends Cache> caches) {
/* 760 */       Collection<String> names = new ArrayList<String>();
/* 761 */       for (Cache cache : caches) {
/* 762 */         names.add(cache.getName());
/*     */       }
/* 764 */       return names;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private class CachePutRequest
/*     */   {
/*     */     private final CacheAspectSupport.CacheOperationContext context;
/*     */     
/*     */     private final Object key;
/*     */     
/*     */     public CachePutRequest(CacheAspectSupport.CacheOperationContext context, Object key) {
/* 776 */       this.context = context;
/* 777 */       this.key = key;
/*     */     }
/*     */     
/*     */     public void apply(Object result) {
/* 781 */       if (this.context.canPutToCache(result)) {
/* 782 */         for (Cache cache : this.context.getCaches()) {
/* 783 */           CacheAspectSupport.this.doPut(cache, this.key, result);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class CacheOperationCacheKey
/*     */     implements Comparable<CacheOperationCacheKey>
/*     */   {
/*     */     private final CacheOperation cacheOperation;
/*     */     private final AnnotatedElementKey methodCacheKey;
/*     */     
/*     */     private CacheOperationCacheKey(CacheOperation cacheOperation, Method method, Class<?> targetClass) {
/* 797 */       this.cacheOperation = cacheOperation;
/* 798 */       this.methodCacheKey = new AnnotatedElementKey(method, targetClass);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object other) {
/* 803 */       if (this == other) {
/* 804 */         return true;
/*     */       }
/* 806 */       if (!(other instanceof CacheOperationCacheKey)) {
/* 807 */         return false;
/*     */       }
/* 809 */       CacheOperationCacheKey otherKey = (CacheOperationCacheKey)other;
/* 810 */       return (this.cacheOperation.equals(otherKey.cacheOperation) && this.methodCacheKey
/* 811 */         .equals(otherKey.methodCacheKey));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 816 */       return this.cacheOperation.hashCode() * 31 + this.methodCacheKey.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 821 */       return this.cacheOperation + " on " + this.methodCacheKey;
/*     */     }
/*     */ 
/*     */     
/*     */     public int compareTo(CacheOperationCacheKey other) {
/* 826 */       int result = this.cacheOperation.getName().compareTo(other.cacheOperation.getName());
/* 827 */       if (result == 0) {
/* 828 */         result = this.methodCacheKey.compareTo(other.methodCacheKey);
/*     */       }
/* 830 */       return result;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @UsesJava8
/*     */   private static class OptionalUnwrapper
/*     */   {
/*     */     public static Object unwrap(Object optionalObject) {
/* 842 */       Optional<?> optional = (Optional)optionalObject;
/* 843 */       if (!optional.isPresent()) {
/* 844 */         return null;
/*     */       }
/* 846 */       Object result = optional.get();
/* 847 */       Assert.isTrue(!(result instanceof Optional), "Multi-level Optional usage not supported");
/* 848 */       return result;
/*     */     }
/*     */     
/*     */     public static Object wrap(Object value) {
/* 852 */       return Optional.ofNullable(value);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\cache\interceptor\CacheAspectSupport.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */