/*    */ package org.springframework.scripting.config;
/*    */ 
/*    */ import org.springframework.beans.factory.config.BeanDefinition;
/*    */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*    */ import org.springframework.beans.factory.support.RootBeanDefinition;
/*    */ import org.springframework.scripting.support.ScriptFactoryPostProcessor;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class LangNamespaceUtils
/*    */ {
/*    */   private static final String SCRIPT_FACTORY_POST_PROCESSOR_BEAN_NAME = "org.springframework.scripting.config.scriptFactoryPostProcessor";
/*    */   
/*    */   public static BeanDefinition registerScriptFactoryPostProcessorIfNecessary(BeanDefinitionRegistry registry) {
/*    */     RootBeanDefinition rootBeanDefinition;
/* 47 */     BeanDefinition beanDefinition = null;
/* 48 */     if (registry.containsBeanDefinition("org.springframework.scripting.config.scriptFactoryPostProcessor")) {
/* 49 */       beanDefinition = registry.getBeanDefinition("org.springframework.scripting.config.scriptFactoryPostProcessor");
/*    */     } else {
/*    */       
/* 52 */       rootBeanDefinition = new RootBeanDefinition(ScriptFactoryPostProcessor.class);
/* 53 */       registry.registerBeanDefinition("org.springframework.scripting.config.scriptFactoryPostProcessor", (BeanDefinition)rootBeanDefinition);
/*    */     } 
/* 55 */     return (BeanDefinition)rootBeanDefinition;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\scripting\config\LangNamespaceUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */