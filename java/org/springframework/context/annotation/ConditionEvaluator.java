/*     */ package org.springframework.context.annotation;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import org.springframework.beans.BeanUtils;
/*     */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*     */ import org.springframework.context.ConfigurableApplicationContext;
/*     */ import org.springframework.core.annotation.AnnotationAwareOrderComparator;
/*     */ import org.springframework.core.env.Environment;
/*     */ import org.springframework.core.env.EnvironmentCapable;
/*     */ import org.springframework.core.io.ResourceLoader;
/*     */ import org.springframework.core.type.AnnotatedTypeMetadata;
/*     */ import org.springframework.core.type.AnnotationMetadata;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.MultiValueMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class ConditionEvaluator
/*     */ {
/*     */   private final ConditionContextImpl context;
/*     */   
/*     */   public ConditionEvaluator(BeanDefinitionRegistry registry, Environment environment, ResourceLoader resourceLoader) {
/*  52 */     this.context = new ConditionContextImpl(registry, environment, resourceLoader);
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
/*     */   public boolean shouldSkip(AnnotatedTypeMetadata metadata) {
/*  64 */     return shouldSkip(metadata, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean shouldSkip(AnnotatedTypeMetadata metadata, ConfigurationCondition.ConfigurationPhase phase) {
/*  74 */     if (metadata == null || !metadata.isAnnotated(Conditional.class.getName())) {
/*  75 */       return false;
/*     */     }
/*     */     
/*  78 */     if (phase == null) {
/*  79 */       if (metadata instanceof AnnotationMetadata && 
/*  80 */         ConfigurationClassUtils.isConfigurationCandidate((AnnotationMetadata)metadata)) {
/*  81 */         return shouldSkip(metadata, ConfigurationCondition.ConfigurationPhase.PARSE_CONFIGURATION);
/*     */       }
/*  83 */       return shouldSkip(metadata, ConfigurationCondition.ConfigurationPhase.REGISTER_BEAN);
/*     */     } 
/*     */     
/*  86 */     List<Condition> conditions = new ArrayList<Condition>();
/*  87 */     for (String[] conditionClasses : getConditionClasses(metadata)) {
/*  88 */       for (String conditionClass : conditionClasses) {
/*  89 */         Condition condition = getCondition(conditionClass, this.context.getClassLoader());
/*  90 */         conditions.add(condition);
/*     */       } 
/*     */     } 
/*     */     
/*  94 */     AnnotationAwareOrderComparator.sort(conditions);
/*     */     
/*  96 */     for (Condition condition : conditions) {
/*  97 */       ConfigurationCondition.ConfigurationPhase requiredPhase = null;
/*  98 */       if (condition instanceof ConfigurationCondition) {
/*  99 */         requiredPhase = ((ConfigurationCondition)condition).getConfigurationPhase();
/*     */       }
/* 101 */       if ((requiredPhase == null || requiredPhase == phase) && 
/* 102 */         !condition.matches(this.context, metadata)) {
/* 103 */         return true;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 108 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private List<String[]> getConditionClasses(AnnotatedTypeMetadata metadata) {
/* 113 */     MultiValueMap<String, Object> attributes = metadata.getAllAnnotationAttributes(Conditional.class.getName(), true);
/* 114 */     Object values = (attributes != null) ? attributes.get("value") : null;
/* 115 */     return (values != null) ? (List<String[]>)values : (List)Collections.<String[]>emptyList();
/*     */   }
/*     */   
/*     */   private Condition getCondition(String conditionClassName, ClassLoader classloader) {
/* 119 */     Class<?> conditionClass = ClassUtils.resolveClassName(conditionClassName, classloader);
/* 120 */     return (Condition)BeanUtils.instantiateClass(conditionClass);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class ConditionContextImpl
/*     */     implements ConditionContext
/*     */   {
/*     */     private final BeanDefinitionRegistry registry;
/*     */     
/*     */     private final ConfigurableListableBeanFactory beanFactory;
/*     */     
/*     */     private final Environment environment;
/*     */     
/*     */     private final ResourceLoader resourceLoader;
/*     */ 
/*     */     
/*     */     public ConditionContextImpl(BeanDefinitionRegistry registry, Environment environment, ResourceLoader resourceLoader) {
/* 138 */       this.registry = registry;
/* 139 */       this.beanFactory = deduceBeanFactory(registry);
/* 140 */       this.environment = (environment != null) ? environment : deduceEnvironment(registry);
/* 141 */       this.resourceLoader = (resourceLoader != null) ? resourceLoader : deduceResourceLoader(registry);
/*     */     }
/*     */     
/*     */     private ConfigurableListableBeanFactory deduceBeanFactory(BeanDefinitionRegistry source) {
/* 145 */       if (source instanceof ConfigurableListableBeanFactory) {
/* 146 */         return (ConfigurableListableBeanFactory)source;
/*     */       }
/* 148 */       if (source instanceof ConfigurableApplicationContext) {
/* 149 */         return ((ConfigurableApplicationContext)source).getBeanFactory();
/*     */       }
/* 151 */       return null;
/*     */     }
/*     */     
/*     */     private Environment deduceEnvironment(BeanDefinitionRegistry source) {
/* 155 */       if (source instanceof EnvironmentCapable) {
/* 156 */         return ((EnvironmentCapable)source).getEnvironment();
/*     */       }
/* 158 */       return null;
/*     */     }
/*     */     
/*     */     private ResourceLoader deduceResourceLoader(BeanDefinitionRegistry source) {
/* 162 */       if (source instanceof ResourceLoader) {
/* 163 */         return (ResourceLoader)source;
/*     */       }
/* 165 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public BeanDefinitionRegistry getRegistry() {
/* 170 */       return this.registry;
/*     */     }
/*     */ 
/*     */     
/*     */     public ConfigurableListableBeanFactory getBeanFactory() {
/* 175 */       return this.beanFactory;
/*     */     }
/*     */ 
/*     */     
/*     */     public Environment getEnvironment() {
/* 180 */       return this.environment;
/*     */     }
/*     */ 
/*     */     
/*     */     public ResourceLoader getResourceLoader() {
/* 185 */       return this.resourceLoader;
/*     */     }
/*     */ 
/*     */     
/*     */     public ClassLoader getClassLoader() {
/* 190 */       if (this.resourceLoader != null) {
/* 191 */         return this.resourceLoader.getClassLoader();
/*     */       }
/* 193 */       if (this.beanFactory != null) {
/* 194 */         return this.beanFactory.getBeanClassLoader();
/*     */       }
/* 196 */       return null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\annotation\ConditionEvaluator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */