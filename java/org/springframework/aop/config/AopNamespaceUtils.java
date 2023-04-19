/*    */ package org.springframework.aop.config;
/*    */ 
/*    */ import org.springframework.beans.factory.config.BeanDefinition;
/*    */ import org.springframework.beans.factory.parsing.BeanComponentDefinition;
/*    */ import org.springframework.beans.factory.parsing.ComponentDefinition;
/*    */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*    */ import org.springframework.beans.factory.xml.ParserContext;
/*    */ import org.w3c.dom.Element;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class AopNamespaceUtils
/*    */ {
/*    */   public static final String PROXY_TARGET_CLASS_ATTRIBUTE = "proxy-target-class";
/*    */   private static final String EXPOSE_PROXY_ATTRIBUTE = "expose-proxy";
/*    */   
/*    */   public static void registerAutoProxyCreatorIfNecessary(ParserContext parserContext, Element sourceElement) {
/* 58 */     BeanDefinition beanDefinition = AopConfigUtils.registerAutoProxyCreatorIfNecessary(parserContext
/* 59 */         .getRegistry(), parserContext.extractSource(sourceElement));
/* 60 */     useClassProxyingIfNecessary(parserContext.getRegistry(), sourceElement);
/* 61 */     registerComponentIfNecessary(beanDefinition, parserContext);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static void registerAspectJAutoProxyCreatorIfNecessary(ParserContext parserContext, Element sourceElement) {
/* 67 */     BeanDefinition beanDefinition = AopConfigUtils.registerAspectJAutoProxyCreatorIfNecessary(parserContext
/* 68 */         .getRegistry(), parserContext.extractSource(sourceElement));
/* 69 */     useClassProxyingIfNecessary(parserContext.getRegistry(), sourceElement);
/* 70 */     registerComponentIfNecessary(beanDefinition, parserContext);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static void registerAspectJAnnotationAutoProxyCreatorIfNecessary(ParserContext parserContext, Element sourceElement) {
/* 76 */     BeanDefinition beanDefinition = AopConfigUtils.registerAspectJAnnotationAutoProxyCreatorIfNecessary(parserContext
/* 77 */         .getRegistry(), parserContext.extractSource(sourceElement));
/* 78 */     useClassProxyingIfNecessary(parserContext.getRegistry(), sourceElement);
/* 79 */     registerComponentIfNecessary(beanDefinition, parserContext);
/*    */   }
/*    */   
/*    */   private static void useClassProxyingIfNecessary(BeanDefinitionRegistry registry, Element sourceElement) {
/* 83 */     if (sourceElement != null) {
/* 84 */       boolean proxyTargetClass = Boolean.parseBoolean(sourceElement.getAttribute("proxy-target-class"));
/* 85 */       if (proxyTargetClass) {
/* 86 */         AopConfigUtils.forceAutoProxyCreatorToUseClassProxying(registry);
/*    */       }
/* 88 */       boolean exposeProxy = Boolean.parseBoolean(sourceElement.getAttribute("expose-proxy"));
/* 89 */       if (exposeProxy) {
/* 90 */         AopConfigUtils.forceAutoProxyCreatorToExposeProxy(registry);
/*    */       }
/*    */     } 
/*    */   }
/*    */   
/*    */   private static void registerComponentIfNecessary(BeanDefinition beanDefinition, ParserContext parserContext) {
/* 96 */     if (beanDefinition != null)
/* 97 */       parserContext.registerComponent((ComponentDefinition)new BeanComponentDefinition(beanDefinition, "org.springframework.aop.config.internalAutoProxyCreator")); 
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\config\AopNamespaceUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */