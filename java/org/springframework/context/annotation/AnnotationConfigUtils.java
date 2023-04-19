/*     */ package org.springframework.context.annotation;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
/*     */ import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
/*     */ import org.springframework.beans.factory.annotation.RequiredAnnotationBeanPostProcessor;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*     */ import org.springframework.beans.factory.support.AbstractBeanDefinition;
/*     */ import org.springframework.beans.factory.support.AutowireCandidateResolver;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*     */ import org.springframework.beans.factory.support.DefaultListableBeanFactory;
/*     */ import org.springframework.beans.factory.support.RootBeanDefinition;
/*     */ import org.springframework.context.event.DefaultEventListenerFactory;
/*     */ import org.springframework.context.event.EventListenerMethodProcessor;
/*     */ import org.springframework.context.support.GenericApplicationContext;
/*     */ import org.springframework.core.annotation.AnnotationAttributes;
/*     */ import org.springframework.core.annotation.AnnotationAwareOrderComparator;
/*     */ import org.springframework.core.type.AnnotatedTypeMetadata;
/*     */ import org.springframework.core.type.AnnotationMetadata;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AnnotationConfigUtils
/*     */ {
/*     */   public static final String CONFIGURATION_ANNOTATION_PROCESSOR_BEAN_NAME = "org.springframework.context.annotation.internalConfigurationAnnotationProcessor";
/*     */   public static final String CONFIGURATION_BEAN_NAME_GENERATOR = "org.springframework.context.annotation.internalConfigurationBeanNameGenerator";
/*     */   public static final String AUTOWIRED_ANNOTATION_PROCESSOR_BEAN_NAME = "org.springframework.context.annotation.internalAutowiredAnnotationProcessor";
/*     */   public static final String REQUIRED_ANNOTATION_PROCESSOR_BEAN_NAME = "org.springframework.context.annotation.internalRequiredAnnotationProcessor";
/*     */   public static final String COMMON_ANNOTATION_PROCESSOR_BEAN_NAME = "org.springframework.context.annotation.internalCommonAnnotationProcessor";
/*     */   public static final String PERSISTENCE_ANNOTATION_PROCESSOR_BEAN_NAME = "org.springframework.context.annotation.internalPersistenceAnnotationProcessor";
/*     */   private static final String PERSISTENCE_ANNOTATION_PROCESSOR_CLASS_NAME = "org.springframework.orm.jpa.support.PersistenceAnnotationBeanPostProcessor";
/*     */   public static final String EVENT_LISTENER_PROCESSOR_BEAN_NAME = "org.springframework.context.event.internalEventListenerProcessor";
/*     */   public static final String EVENT_LISTENER_FACTORY_BEAN_NAME = "org.springframework.context.event.internalEventListenerFactory";
/* 121 */   private static final boolean jsr250Present = ClassUtils.isPresent("javax.annotation.Resource", AnnotationConfigUtils.class.getClassLoader());
/*     */   
/* 123 */   private static final boolean jpaPresent = (
/* 124 */     ClassUtils.isPresent("javax.persistence.EntityManagerFactory", AnnotationConfigUtils.class.getClassLoader()) && 
/* 125 */     ClassUtils.isPresent("org.springframework.orm.jpa.support.PersistenceAnnotationBeanPostProcessor", AnnotationConfigUtils.class.getClassLoader()));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void registerAnnotationConfigProcessors(BeanDefinitionRegistry registry) {
/* 133 */     registerAnnotationConfigProcessors(registry, null);
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
/*     */   public static Set<BeanDefinitionHolder> registerAnnotationConfigProcessors(BeanDefinitionRegistry registry, Object source) {
/* 147 */     DefaultListableBeanFactory beanFactory = unwrapDefaultListableBeanFactory(registry);
/* 148 */     if (beanFactory != null) {
/* 149 */       if (!(beanFactory.getDependencyComparator() instanceof AnnotationAwareOrderComparator)) {
/* 150 */         beanFactory.setDependencyComparator((Comparator)AnnotationAwareOrderComparator.INSTANCE);
/*     */       }
/* 152 */       if (!(beanFactory.getAutowireCandidateResolver() instanceof ContextAnnotationAutowireCandidateResolver)) {
/* 153 */         beanFactory.setAutowireCandidateResolver((AutowireCandidateResolver)new ContextAnnotationAutowireCandidateResolver());
/*     */       }
/*     */     } 
/*     */     
/* 157 */     Set<BeanDefinitionHolder> beanDefs = new LinkedHashSet<BeanDefinitionHolder>(8);
/*     */     
/* 159 */     if (!registry.containsBeanDefinition("org.springframework.context.annotation.internalConfigurationAnnotationProcessor")) {
/* 160 */       RootBeanDefinition def = new RootBeanDefinition(ConfigurationClassPostProcessor.class);
/* 161 */       def.setSource(source);
/* 162 */       beanDefs.add(registerPostProcessor(registry, def, "org.springframework.context.annotation.internalConfigurationAnnotationProcessor"));
/*     */     } 
/*     */     
/* 165 */     if (!registry.containsBeanDefinition("org.springframework.context.annotation.internalAutowiredAnnotationProcessor")) {
/* 166 */       RootBeanDefinition def = new RootBeanDefinition(AutowiredAnnotationBeanPostProcessor.class);
/* 167 */       def.setSource(source);
/* 168 */       beanDefs.add(registerPostProcessor(registry, def, "org.springframework.context.annotation.internalAutowiredAnnotationProcessor"));
/*     */     } 
/*     */     
/* 171 */     if (!registry.containsBeanDefinition("org.springframework.context.annotation.internalRequiredAnnotationProcessor")) {
/* 172 */       RootBeanDefinition def = new RootBeanDefinition(RequiredAnnotationBeanPostProcessor.class);
/* 173 */       def.setSource(source);
/* 174 */       beanDefs.add(registerPostProcessor(registry, def, "org.springframework.context.annotation.internalRequiredAnnotationProcessor"));
/*     */     } 
/*     */ 
/*     */     
/* 178 */     if (jsr250Present && !registry.containsBeanDefinition("org.springframework.context.annotation.internalCommonAnnotationProcessor")) {
/* 179 */       RootBeanDefinition def = new RootBeanDefinition(CommonAnnotationBeanPostProcessor.class);
/* 180 */       def.setSource(source);
/* 181 */       beanDefs.add(registerPostProcessor(registry, def, "org.springframework.context.annotation.internalCommonAnnotationProcessor"));
/*     */     } 
/*     */ 
/*     */     
/* 185 */     if (jpaPresent && !registry.containsBeanDefinition("org.springframework.context.annotation.internalPersistenceAnnotationProcessor")) {
/* 186 */       RootBeanDefinition def = new RootBeanDefinition();
/*     */       try {
/* 188 */         def.setBeanClass(ClassUtils.forName("org.springframework.orm.jpa.support.PersistenceAnnotationBeanPostProcessor", AnnotationConfigUtils.class
/* 189 */               .getClassLoader()));
/*     */       }
/* 191 */       catch (ClassNotFoundException ex) {
/* 192 */         throw new IllegalStateException("Cannot load optional framework class: org.springframework.orm.jpa.support.PersistenceAnnotationBeanPostProcessor", ex);
/*     */       } 
/*     */       
/* 195 */       def.setSource(source);
/* 196 */       beanDefs.add(registerPostProcessor(registry, def, "org.springframework.context.annotation.internalPersistenceAnnotationProcessor"));
/*     */     } 
/*     */     
/* 199 */     if (!registry.containsBeanDefinition("org.springframework.context.event.internalEventListenerProcessor")) {
/* 200 */       RootBeanDefinition def = new RootBeanDefinition(EventListenerMethodProcessor.class);
/* 201 */       def.setSource(source);
/* 202 */       beanDefs.add(registerPostProcessor(registry, def, "org.springframework.context.event.internalEventListenerProcessor"));
/*     */     } 
/*     */     
/* 205 */     if (!registry.containsBeanDefinition("org.springframework.context.event.internalEventListenerFactory")) {
/* 206 */       RootBeanDefinition def = new RootBeanDefinition(DefaultEventListenerFactory.class);
/* 207 */       def.setSource(source);
/* 208 */       beanDefs.add(registerPostProcessor(registry, def, "org.springframework.context.event.internalEventListenerFactory"));
/*     */     } 
/*     */     
/* 211 */     return beanDefs;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static BeanDefinitionHolder registerPostProcessor(BeanDefinitionRegistry registry, RootBeanDefinition definition, String beanName) {
/* 217 */     definition.setRole(2);
/* 218 */     registry.registerBeanDefinition(beanName, (BeanDefinition)definition);
/* 219 */     return new BeanDefinitionHolder((BeanDefinition)definition, beanName);
/*     */   }
/*     */   
/*     */   private static DefaultListableBeanFactory unwrapDefaultListableBeanFactory(BeanDefinitionRegistry registry) {
/* 223 */     if (registry instanceof DefaultListableBeanFactory) {
/* 224 */       return (DefaultListableBeanFactory)registry;
/*     */     }
/* 226 */     if (registry instanceof GenericApplicationContext) {
/* 227 */       return ((GenericApplicationContext)registry).getDefaultListableBeanFactory();
/*     */     }
/*     */     
/* 230 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void processCommonDefinitionAnnotations(AnnotatedBeanDefinition abd) {
/* 235 */     processCommonDefinitionAnnotations(abd, (AnnotatedTypeMetadata)abd.getMetadata());
/*     */   }
/*     */   
/*     */   static void processCommonDefinitionAnnotations(AnnotatedBeanDefinition abd, AnnotatedTypeMetadata metadata) {
/* 239 */     if (metadata.isAnnotated(Lazy.class.getName())) {
/* 240 */       abd.setLazyInit(attributesFor(metadata, Lazy.class).getBoolean("value"));
/*     */     }
/* 242 */     else if (abd.getMetadata() != metadata && abd.getMetadata().isAnnotated(Lazy.class.getName())) {
/* 243 */       abd.setLazyInit(attributesFor((AnnotatedTypeMetadata)abd.getMetadata(), Lazy.class).getBoolean("value"));
/*     */     } 
/*     */     
/* 246 */     if (metadata.isAnnotated(Primary.class.getName())) {
/* 247 */       abd.setPrimary(true);
/*     */     }
/* 249 */     if (metadata.isAnnotated(DependsOn.class.getName())) {
/* 250 */       abd.setDependsOn(attributesFor(metadata, DependsOn.class).getStringArray("value"));
/*     */     }
/*     */     
/* 253 */     if (abd instanceof AbstractBeanDefinition) {
/* 254 */       AbstractBeanDefinition absBd = (AbstractBeanDefinition)abd;
/* 255 */       if (metadata.isAnnotated(Role.class.getName())) {
/* 256 */         absBd.setRole(attributesFor(metadata, Role.class).getNumber("value").intValue());
/*     */       }
/* 258 */       if (metadata.isAnnotated(Description.class.getName())) {
/* 259 */         absBd.setDescription(attributesFor(metadata, Description.class).getString("value"));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static BeanDefinitionHolder applyScopedProxyMode(ScopeMetadata metadata, BeanDefinitionHolder definition, BeanDefinitionRegistry registry) {
/* 267 */     ScopedProxyMode scopedProxyMode = metadata.getScopedProxyMode();
/* 268 */     if (scopedProxyMode.equals(ScopedProxyMode.NO)) {
/* 269 */       return definition;
/*     */     }
/* 271 */     boolean proxyTargetClass = scopedProxyMode.equals(ScopedProxyMode.TARGET_CLASS);
/* 272 */     return ScopedProxyCreator.createScopedProxy(definition, registry, proxyTargetClass);
/*     */   }
/*     */   
/*     */   static AnnotationAttributes attributesFor(AnnotatedTypeMetadata metadata, Class<?> annotationClass) {
/* 276 */     return attributesFor(metadata, annotationClass.getName());
/*     */   }
/*     */   
/*     */   static AnnotationAttributes attributesFor(AnnotatedTypeMetadata metadata, String annotationClassName) {
/* 280 */     return AnnotationAttributes.fromMap(metadata.getAnnotationAttributes(annotationClassName, false));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static Set<AnnotationAttributes> attributesForRepeatable(AnnotationMetadata metadata, Class<?> containerClass, Class<?> annotationClass) {
/* 286 */     return attributesForRepeatable(metadata, containerClass.getName(), annotationClass.getName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static Set<AnnotationAttributes> attributesForRepeatable(AnnotationMetadata metadata, String containerClassName, String annotationClassName) {
/* 293 */     Set<AnnotationAttributes> result = new LinkedHashSet<AnnotationAttributes>();
/*     */ 
/*     */     
/* 296 */     addAttributesIfNotNull(result, metadata.getAnnotationAttributes(annotationClassName, false));
/*     */ 
/*     */     
/* 299 */     Map<String, Object> container = metadata.getAnnotationAttributes(containerClassName, false);
/* 300 */     if (container != null && container.containsKey("value")) {
/* 301 */       for (Map<String, Object> containedAttributes : (Map[])container.get("value")) {
/* 302 */         addAttributesIfNotNull(result, containedAttributes);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/* 307 */     return Collections.unmodifiableSet(result);
/*     */   }
/*     */   
/*     */   private static void addAttributesIfNotNull(Set<AnnotationAttributes> result, Map<String, Object> attributes) {
/* 311 */     if (attributes != null)
/* 312 */       result.add(AnnotationAttributes.fromMap(attributes)); 
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\annotation\AnnotationConfigUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */