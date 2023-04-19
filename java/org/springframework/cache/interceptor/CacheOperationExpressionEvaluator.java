/*     */ package org.springframework.cache.interceptor;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.springframework.aop.support.AopUtils;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.cache.Cache;
/*     */ import org.springframework.context.expression.AnnotatedElementKey;
/*     */ import org.springframework.context.expression.BeanFactoryResolver;
/*     */ import org.springframework.context.expression.CachedExpressionEvaluator;
/*     */ import org.springframework.expression.BeanResolver;
/*     */ import org.springframework.expression.EvaluationContext;
/*     */ import org.springframework.expression.Expression;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class CacheOperationExpressionEvaluator
/*     */   extends CachedExpressionEvaluator
/*     */ {
/*  51 */   public static final Object NO_RESULT = new Object();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  56 */   public static final Object RESULT_UNAVAILABLE = new Object();
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String RESULT_VARIABLE = "result";
/*     */ 
/*     */ 
/*     */   
/*  64 */   private final Map<CachedExpressionEvaluator.ExpressionKey, Expression> keyCache = new ConcurrentHashMap<CachedExpressionEvaluator.ExpressionKey, Expression>(64);
/*     */   
/*  66 */   private final Map<CachedExpressionEvaluator.ExpressionKey, Expression> conditionCache = new ConcurrentHashMap<CachedExpressionEvaluator.ExpressionKey, Expression>(64);
/*     */   
/*  68 */   private final Map<CachedExpressionEvaluator.ExpressionKey, Expression> unlessCache = new ConcurrentHashMap<CachedExpressionEvaluator.ExpressionKey, Expression>(64);
/*     */   
/*  70 */   private final Map<AnnotatedElementKey, Method> targetMethodCache = new ConcurrentHashMap<AnnotatedElementKey, Method>(64);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EvaluationContext createEvaluationContext(Collection<? extends Cache> caches, Method method, Object[] args, Object target, Class<?> targetClass, BeanFactory beanFactory) {
/*  81 */     return createEvaluationContext(caches, method, args, target, targetClass, NO_RESULT, beanFactory);
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
/*     */   public EvaluationContext createEvaluationContext(Collection<? extends Cache> caches, Method method, Object[] args, Object target, Class<?> targetClass, Object result, BeanFactory beanFactory) {
/*  99 */     CacheExpressionRootObject rootObject = new CacheExpressionRootObject(caches, method, args, target, targetClass);
/*     */     
/* 101 */     Method targetMethod = getTargetMethod(targetClass, method);
/*     */     
/* 103 */     CacheEvaluationContext evaluationContext = new CacheEvaluationContext(rootObject, targetMethod, args, getParameterNameDiscoverer());
/* 104 */     if (result == RESULT_UNAVAILABLE) {
/* 105 */       evaluationContext.addUnavailableVariable("result");
/*     */     }
/* 107 */     else if (result != NO_RESULT) {
/* 108 */       evaluationContext.setVariable("result", result);
/*     */     } 
/* 110 */     if (beanFactory != null) {
/* 111 */       evaluationContext.setBeanResolver((BeanResolver)new BeanFactoryResolver(beanFactory));
/*     */     }
/* 113 */     return (EvaluationContext)evaluationContext;
/*     */   }
/*     */   
/*     */   public Object key(String keyExpression, AnnotatedElementKey methodKey, EvaluationContext evalContext) {
/* 117 */     return getExpression(this.keyCache, methodKey, keyExpression).getValue(evalContext);
/*     */   }
/*     */   
/*     */   public boolean condition(String conditionExpression, AnnotatedElementKey methodKey, EvaluationContext evalContext) {
/* 121 */     return ((Boolean)getExpression(this.conditionCache, methodKey, conditionExpression).getValue(evalContext, boolean.class)).booleanValue();
/*     */   }
/*     */   
/*     */   public boolean unless(String unlessExpression, AnnotatedElementKey methodKey, EvaluationContext evalContext) {
/* 125 */     return ((Boolean)getExpression(this.unlessCache, methodKey, unlessExpression).getValue(evalContext, boolean.class)).booleanValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void clear() {
/* 132 */     this.keyCache.clear();
/* 133 */     this.conditionCache.clear();
/* 134 */     this.unlessCache.clear();
/* 135 */     this.targetMethodCache.clear();
/*     */   }
/*     */   
/*     */   private Method getTargetMethod(Class<?> targetClass, Method method) {
/* 139 */     AnnotatedElementKey methodKey = new AnnotatedElementKey(method, targetClass);
/* 140 */     Method targetMethod = this.targetMethodCache.get(methodKey);
/* 141 */     if (targetMethod == null) {
/* 142 */       targetMethod = AopUtils.getMostSpecificMethod(method, targetClass);
/* 143 */       this.targetMethodCache.put(methodKey, targetMethod);
/*     */     } 
/* 145 */     return targetMethod;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\cache\interceptor\CacheOperationExpressionEvaluator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */