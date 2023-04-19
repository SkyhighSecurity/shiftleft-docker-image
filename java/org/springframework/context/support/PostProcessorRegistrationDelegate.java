/*     */ package org.springframework.context.support;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
/*     */ import org.springframework.beans.factory.config.BeanPostProcessor;
/*     */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
/*     */ import org.springframework.beans.factory.support.DefaultListableBeanFactory;
/*     */ import org.springframework.core.OrderComparator;
/*     */ import org.springframework.core.Ordered;
/*     */ import org.springframework.core.PriorityOrdered;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class PostProcessorRegistrationDelegate
/*     */ {
/*     */   public static void invokeBeanFactoryPostProcessors(ConfigurableListableBeanFactory beanFactory, List<BeanFactoryPostProcessor> beanFactoryPostProcessors) {
/*  56 */     Set<String> processedBeans = new HashSet<String>();
/*     */     
/*  58 */     if (beanFactory instanceof BeanDefinitionRegistry) {
/*  59 */       BeanDefinitionRegistry registry = (BeanDefinitionRegistry)beanFactory;
/*  60 */       List<BeanFactoryPostProcessor> regularPostProcessors = new LinkedList<BeanFactoryPostProcessor>();
/*  61 */       List<BeanDefinitionRegistryPostProcessor> registryProcessors = new LinkedList<BeanDefinitionRegistryPostProcessor>();
/*     */       
/*  63 */       for (BeanFactoryPostProcessor postProcessor : beanFactoryPostProcessors) {
/*  64 */         if (postProcessor instanceof BeanDefinitionRegistryPostProcessor) {
/*  65 */           BeanDefinitionRegistryPostProcessor registryProcessor = (BeanDefinitionRegistryPostProcessor)postProcessor;
/*     */           
/*  67 */           registryProcessor.postProcessBeanDefinitionRegistry(registry);
/*  68 */           registryProcessors.add(registryProcessor);
/*     */           continue;
/*     */         } 
/*  71 */         regularPostProcessors.add(postProcessor);
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  79 */       List<BeanDefinitionRegistryPostProcessor> currentRegistryProcessors = new ArrayList<BeanDefinitionRegistryPostProcessor>();
/*     */ 
/*     */ 
/*     */       
/*  83 */       String[] arrayOfString = beanFactory.getBeanNamesForType(BeanDefinitionRegistryPostProcessor.class, true, false);
/*  84 */       for (String ppName : arrayOfString) {
/*  85 */         if (beanFactory.isTypeMatch(ppName, PriorityOrdered.class)) {
/*  86 */           currentRegistryProcessors.add(beanFactory.getBean(ppName, BeanDefinitionRegistryPostProcessor.class));
/*  87 */           processedBeans.add(ppName);
/*     */         } 
/*     */       } 
/*  90 */       sortPostProcessors(currentRegistryProcessors, beanFactory);
/*  91 */       registryProcessors.addAll(currentRegistryProcessors);
/*  92 */       invokeBeanDefinitionRegistryPostProcessors(currentRegistryProcessors, registry);
/*  93 */       currentRegistryProcessors.clear();
/*     */ 
/*     */       
/*  96 */       arrayOfString = beanFactory.getBeanNamesForType(BeanDefinitionRegistryPostProcessor.class, true, false);
/*  97 */       for (String ppName : arrayOfString) {
/*  98 */         if (!processedBeans.contains(ppName) && beanFactory.isTypeMatch(ppName, Ordered.class)) {
/*  99 */           currentRegistryProcessors.add(beanFactory.getBean(ppName, BeanDefinitionRegistryPostProcessor.class));
/* 100 */           processedBeans.add(ppName);
/*     */         } 
/*     */       } 
/* 103 */       sortPostProcessors(currentRegistryProcessors, beanFactory);
/* 104 */       registryProcessors.addAll(currentRegistryProcessors);
/* 105 */       invokeBeanDefinitionRegistryPostProcessors(currentRegistryProcessors, registry);
/* 106 */       currentRegistryProcessors.clear();
/*     */ 
/*     */       
/* 109 */       boolean reiterate = true;
/* 110 */       while (reiterate) {
/* 111 */         reiterate = false;
/* 112 */         arrayOfString = beanFactory.getBeanNamesForType(BeanDefinitionRegistryPostProcessor.class, true, false);
/* 113 */         for (String ppName : arrayOfString) {
/* 114 */           if (!processedBeans.contains(ppName)) {
/* 115 */             currentRegistryProcessors.add(beanFactory.getBean(ppName, BeanDefinitionRegistryPostProcessor.class));
/* 116 */             processedBeans.add(ppName);
/* 117 */             reiterate = true;
/*     */           } 
/*     */         } 
/* 120 */         sortPostProcessors(currentRegistryProcessors, beanFactory);
/* 121 */         registryProcessors.addAll(currentRegistryProcessors);
/* 122 */         invokeBeanDefinitionRegistryPostProcessors(currentRegistryProcessors, registry);
/* 123 */         currentRegistryProcessors.clear();
/*     */       } 
/*     */ 
/*     */       
/* 127 */       invokeBeanFactoryPostProcessors((Collection)registryProcessors, beanFactory);
/* 128 */       invokeBeanFactoryPostProcessors(regularPostProcessors, beanFactory);
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 133 */       invokeBeanFactoryPostProcessors(beanFactoryPostProcessors, beanFactory);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 139 */     String[] postProcessorNames = beanFactory.getBeanNamesForType(BeanFactoryPostProcessor.class, true, false);
/*     */ 
/*     */ 
/*     */     
/* 143 */     List<BeanFactoryPostProcessor> priorityOrderedPostProcessors = new ArrayList<BeanFactoryPostProcessor>();
/* 144 */     List<String> orderedPostProcessorNames = new ArrayList<String>();
/* 145 */     List<String> nonOrderedPostProcessorNames = new ArrayList<String>();
/* 146 */     for (String ppName : postProcessorNames) {
/* 147 */       if (!processedBeans.contains(ppName))
/*     */       {
/*     */         
/* 150 */         if (beanFactory.isTypeMatch(ppName, PriorityOrdered.class)) {
/* 151 */           priorityOrderedPostProcessors.add(beanFactory.getBean(ppName, BeanFactoryPostProcessor.class));
/*     */         }
/* 153 */         else if (beanFactory.isTypeMatch(ppName, Ordered.class)) {
/* 154 */           orderedPostProcessorNames.add(ppName);
/*     */         } else {
/*     */           
/* 157 */           nonOrderedPostProcessorNames.add(ppName);
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 162 */     sortPostProcessors(priorityOrderedPostProcessors, beanFactory);
/* 163 */     invokeBeanFactoryPostProcessors(priorityOrderedPostProcessors, beanFactory);
/*     */ 
/*     */     
/* 166 */     List<BeanFactoryPostProcessor> orderedPostProcessors = new ArrayList<BeanFactoryPostProcessor>();
/* 167 */     for (String postProcessorName : orderedPostProcessorNames) {
/* 168 */       orderedPostProcessors.add(beanFactory.getBean(postProcessorName, BeanFactoryPostProcessor.class));
/*     */     }
/* 170 */     sortPostProcessors(orderedPostProcessors, beanFactory);
/* 171 */     invokeBeanFactoryPostProcessors(orderedPostProcessors, beanFactory);
/*     */ 
/*     */     
/* 174 */     List<BeanFactoryPostProcessor> nonOrderedPostProcessors = new ArrayList<BeanFactoryPostProcessor>();
/* 175 */     for (String postProcessorName : nonOrderedPostProcessorNames) {
/* 176 */       nonOrderedPostProcessors.add(beanFactory.getBean(postProcessorName, BeanFactoryPostProcessor.class));
/*     */     }
/* 178 */     invokeBeanFactoryPostProcessors(nonOrderedPostProcessors, beanFactory);
/*     */ 
/*     */ 
/*     */     
/* 182 */     beanFactory.clearMetadataCache();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory, AbstractApplicationContext applicationContext) {
/* 188 */     String[] postProcessorNames = beanFactory.getBeanNamesForType(BeanPostProcessor.class, true, false);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 193 */     int beanProcessorTargetCount = beanFactory.getBeanPostProcessorCount() + 1 + postProcessorNames.length;
/* 194 */     beanFactory.addBeanPostProcessor(new BeanPostProcessorChecker(beanFactory, beanProcessorTargetCount));
/*     */ 
/*     */ 
/*     */     
/* 198 */     List<BeanPostProcessor> priorityOrderedPostProcessors = new ArrayList<BeanPostProcessor>();
/* 199 */     List<BeanPostProcessor> internalPostProcessors = new ArrayList<BeanPostProcessor>();
/* 200 */     List<String> orderedPostProcessorNames = new ArrayList<String>();
/* 201 */     List<String> nonOrderedPostProcessorNames = new ArrayList<String>();
/* 202 */     for (String ppName : postProcessorNames) {
/* 203 */       if (beanFactory.isTypeMatch(ppName, PriorityOrdered.class)) {
/* 204 */         BeanPostProcessor pp = (BeanPostProcessor)beanFactory.getBean(ppName, BeanPostProcessor.class);
/* 205 */         priorityOrderedPostProcessors.add(pp);
/* 206 */         if (pp instanceof org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor) {
/* 207 */           internalPostProcessors.add(pp);
/*     */         }
/*     */       }
/* 210 */       else if (beanFactory.isTypeMatch(ppName, Ordered.class)) {
/* 211 */         orderedPostProcessorNames.add(ppName);
/*     */       } else {
/*     */         
/* 214 */         nonOrderedPostProcessorNames.add(ppName);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 219 */     sortPostProcessors(priorityOrderedPostProcessors, beanFactory);
/* 220 */     registerBeanPostProcessors(beanFactory, priorityOrderedPostProcessors);
/*     */ 
/*     */     
/* 223 */     List<BeanPostProcessor> orderedPostProcessors = new ArrayList<BeanPostProcessor>();
/* 224 */     for (String ppName : orderedPostProcessorNames) {
/* 225 */       BeanPostProcessor pp = (BeanPostProcessor)beanFactory.getBean(ppName, BeanPostProcessor.class);
/* 226 */       orderedPostProcessors.add(pp);
/* 227 */       if (pp instanceof org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor) {
/* 228 */         internalPostProcessors.add(pp);
/*     */       }
/*     */     } 
/* 231 */     sortPostProcessors(orderedPostProcessors, beanFactory);
/* 232 */     registerBeanPostProcessors(beanFactory, orderedPostProcessors);
/*     */ 
/*     */     
/* 235 */     List<BeanPostProcessor> nonOrderedPostProcessors = new ArrayList<BeanPostProcessor>();
/* 236 */     for (String ppName : nonOrderedPostProcessorNames) {
/* 237 */       BeanPostProcessor pp = (BeanPostProcessor)beanFactory.getBean(ppName, BeanPostProcessor.class);
/* 238 */       nonOrderedPostProcessors.add(pp);
/* 239 */       if (pp instanceof org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor) {
/* 240 */         internalPostProcessors.add(pp);
/*     */       }
/*     */     } 
/* 243 */     registerBeanPostProcessors(beanFactory, nonOrderedPostProcessors);
/*     */ 
/*     */     
/* 246 */     sortPostProcessors(internalPostProcessors, beanFactory);
/* 247 */     registerBeanPostProcessors(beanFactory, internalPostProcessors);
/*     */ 
/*     */ 
/*     */     
/* 251 */     beanFactory.addBeanPostProcessor((BeanPostProcessor)new ApplicationListenerDetector(applicationContext));
/*     */   }
/*     */   private static void sortPostProcessors(List<?> postProcessors, ConfigurableListableBeanFactory beanFactory) {
/*     */     OrderComparator orderComparator;
/* 255 */     Comparator<Object> comparatorToUse = null;
/* 256 */     if (beanFactory instanceof DefaultListableBeanFactory) {
/* 257 */       comparatorToUse = ((DefaultListableBeanFactory)beanFactory).getDependencyComparator();
/*     */     }
/* 259 */     if (comparatorToUse == null) {
/* 260 */       orderComparator = OrderComparator.INSTANCE;
/*     */     }
/* 262 */     Collections.sort(postProcessors, (Comparator<?>)orderComparator);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void invokeBeanDefinitionRegistryPostProcessors(Collection<? extends BeanDefinitionRegistryPostProcessor> postProcessors, BeanDefinitionRegistry registry) {
/* 271 */     for (BeanDefinitionRegistryPostProcessor postProcessor : postProcessors) {
/* 272 */       postProcessor.postProcessBeanDefinitionRegistry(registry);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void invokeBeanFactoryPostProcessors(Collection<? extends BeanFactoryPostProcessor> postProcessors, ConfigurableListableBeanFactory beanFactory) {
/* 282 */     for (BeanFactoryPostProcessor postProcessor : postProcessors) {
/* 283 */       postProcessor.postProcessBeanFactory(beanFactory);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory, List<BeanPostProcessor> postProcessors) {
/* 293 */     for (BeanPostProcessor postProcessor : postProcessors) {
/* 294 */       beanFactory.addBeanPostProcessor(postProcessor);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class BeanPostProcessorChecker
/*     */     implements BeanPostProcessor
/*     */   {
/* 306 */     private static final Log logger = LogFactory.getLog(BeanPostProcessorChecker.class);
/*     */     
/*     */     private final ConfigurableListableBeanFactory beanFactory;
/*     */     
/*     */     private final int beanPostProcessorTargetCount;
/*     */     
/*     */     public BeanPostProcessorChecker(ConfigurableListableBeanFactory beanFactory, int beanPostProcessorTargetCount) {
/* 313 */       this.beanFactory = beanFactory;
/* 314 */       this.beanPostProcessorTargetCount = beanPostProcessorTargetCount;
/*     */     }
/*     */ 
/*     */     
/*     */     public Object postProcessBeforeInitialization(Object bean, String beanName) {
/* 319 */       return bean;
/*     */     }
/*     */ 
/*     */     
/*     */     public Object postProcessAfterInitialization(Object bean, String beanName) {
/* 324 */       if (bean != null && !(bean instanceof BeanPostProcessor) && !isInfrastructureBean(beanName) && this.beanFactory
/* 325 */         .getBeanPostProcessorCount() < this.beanPostProcessorTargetCount && 
/* 326 */         logger.isInfoEnabled()) {
/* 327 */         logger.info("Bean '" + beanName + "' of type [" + bean.getClass().getName() + "] is not eligible for getting processed by all BeanPostProcessors (for example: not eligible for auto-proxying)");
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 332 */       return bean;
/*     */     }
/*     */     
/*     */     private boolean isInfrastructureBean(String beanName) {
/* 336 */       if (beanName != null && this.beanFactory.containsBeanDefinition(beanName)) {
/* 337 */         BeanDefinition bd = this.beanFactory.getBeanDefinition(beanName);
/* 338 */         return (bd.getRole() == 2);
/*     */       } 
/* 340 */       return false;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\support\PostProcessorRegistrationDelegate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */