/*     */ package org.springframework.scheduling.config;
/*     */ 
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*     */ import org.springframework.beans.factory.parsing.BeanComponentDefinition;
/*     */ import org.springframework.beans.factory.parsing.ComponentDefinition;
/*     */ import org.springframework.beans.factory.parsing.CompositeComponentDefinition;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionBuilder;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*     */ import org.springframework.beans.factory.xml.BeanDefinitionParser;
/*     */ import org.springframework.beans.factory.xml.ParserContext;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.w3c.dom.Element;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AnnotationDrivenBeanDefinitionParser
/*     */   implements BeanDefinitionParser
/*     */ {
/*     */   private static final String ASYNC_EXECUTION_ASPECT_CLASS_NAME = "org.springframework.scheduling.aspectj.AnnotationAsyncExecutionAspect";
/*     */   
/*     */   public BeanDefinition parse(Element element, ParserContext parserContext) {
/*  50 */     Object source = parserContext.extractSource(element);
/*     */ 
/*     */     
/*  53 */     CompositeComponentDefinition compDefinition = new CompositeComponentDefinition(element.getTagName(), source);
/*  54 */     parserContext.pushContainingComponent(compDefinition);
/*     */ 
/*     */     
/*  57 */     BeanDefinitionRegistry registry = parserContext.getRegistry();
/*     */     
/*  59 */     String mode = element.getAttribute("mode");
/*  60 */     if ("aspectj".equals(mode)) {
/*     */       
/*  62 */       registerAsyncExecutionAspect(element, parserContext);
/*     */ 
/*     */     
/*     */     }
/*  66 */     else if (registry.containsBeanDefinition("org.springframework.context.annotation.internalAsyncAnnotationProcessor")) {
/*  67 */       parserContext.getReaderContext().error("Only one AsyncAnnotationBeanPostProcessor may exist within the context.", source);
/*     */     }
/*     */     else {
/*     */       
/*  71 */       BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition("org.springframework.scheduling.annotation.AsyncAnnotationBeanPostProcessor");
/*     */       
/*  73 */       builder.getRawBeanDefinition().setSource(source);
/*  74 */       String executor = element.getAttribute("executor");
/*  75 */       if (StringUtils.hasText(executor)) {
/*  76 */         builder.addPropertyReference("executor", executor);
/*     */       }
/*  78 */       String exceptionHandler = element.getAttribute("exception-handler");
/*  79 */       if (StringUtils.hasText(exceptionHandler)) {
/*  80 */         builder.addPropertyReference("exceptionHandler", exceptionHandler);
/*     */       }
/*  82 */       if (Boolean.valueOf(element.getAttribute("proxy-target-class")).booleanValue()) {
/*  83 */         builder.addPropertyValue("proxyTargetClass", Boolean.valueOf(true));
/*     */       }
/*  85 */       registerPostProcessor(parserContext, builder, "org.springframework.context.annotation.internalAsyncAnnotationProcessor");
/*     */     } 
/*     */ 
/*     */     
/*  89 */     if (registry.containsBeanDefinition("org.springframework.context.annotation.internalScheduledAnnotationProcessor")) {
/*  90 */       parserContext.getReaderContext().error("Only one ScheduledAnnotationBeanPostProcessor may exist within the context.", source);
/*     */     }
/*     */     else {
/*     */       
/*  94 */       BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition("org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor");
/*     */       
/*  96 */       builder.getRawBeanDefinition().setSource(source);
/*  97 */       String scheduler = element.getAttribute("scheduler");
/*  98 */       if (StringUtils.hasText(scheduler)) {
/*  99 */         builder.addPropertyReference("scheduler", scheduler);
/*     */       }
/* 101 */       registerPostProcessor(parserContext, builder, "org.springframework.context.annotation.internalScheduledAnnotationProcessor");
/*     */     } 
/*     */ 
/*     */     
/* 105 */     parserContext.popAndRegisterContainingComponent();
/*     */     
/* 107 */     return null;
/*     */   }
/*     */   
/*     */   private void registerAsyncExecutionAspect(Element element, ParserContext parserContext) {
/* 111 */     if (!parserContext.getRegistry().containsBeanDefinition("org.springframework.scheduling.config.internalAsyncExecutionAspect")) {
/* 112 */       BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition("org.springframework.scheduling.aspectj.AnnotationAsyncExecutionAspect");
/* 113 */       builder.setFactoryMethod("aspectOf");
/* 114 */       String executor = element.getAttribute("executor");
/* 115 */       if (StringUtils.hasText(executor)) {
/* 116 */         builder.addPropertyReference("executor", executor);
/*     */       }
/* 118 */       String exceptionHandler = element.getAttribute("exception-handler");
/* 119 */       if (StringUtils.hasText(exceptionHandler)) {
/* 120 */         builder.addPropertyReference("exceptionHandler", exceptionHandler);
/*     */       }
/* 122 */       parserContext.registerBeanComponent(new BeanComponentDefinition((BeanDefinition)builder.getBeanDefinition(), "org.springframework.scheduling.config.internalAsyncExecutionAspect"));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void registerPostProcessor(ParserContext parserContext, BeanDefinitionBuilder builder, String beanName) {
/* 130 */     builder.setRole(2);
/* 131 */     parserContext.getRegistry().registerBeanDefinition(beanName, (BeanDefinition)builder.getBeanDefinition());
/* 132 */     BeanDefinitionHolder holder = new BeanDefinitionHolder((BeanDefinition)builder.getBeanDefinition(), beanName);
/* 133 */     parserContext.registerComponent((ComponentDefinition)new BeanComponentDefinition(holder));
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\scheduling\config\AnnotationDrivenBeanDefinitionParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */