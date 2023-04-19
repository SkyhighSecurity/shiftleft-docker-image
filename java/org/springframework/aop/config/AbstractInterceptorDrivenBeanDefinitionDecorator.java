/*     */ package org.springframework.aop.config;
/*     */ 
/*     */ import java.util.List;
/*     */ import org.springframework.aop.framework.ProxyFactoryBean;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*     */ import org.springframework.beans.factory.support.AbstractBeanDefinition;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*     */ import org.springframework.beans.factory.support.ManagedList;
/*     */ import org.springframework.beans.factory.support.RootBeanDefinition;
/*     */ import org.springframework.beans.factory.xml.BeanDefinitionDecorator;
/*     */ import org.springframework.beans.factory.xml.ParserContext;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.w3c.dom.Node;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractInterceptorDrivenBeanDefinitionDecorator
/*     */   implements BeanDefinitionDecorator
/*     */ {
/*     */   public final BeanDefinitionHolder decorate(Node node, BeanDefinitionHolder definitionHolder, ParserContext parserContext) {
/*  63 */     BeanDefinitionRegistry registry = parserContext.getRegistry();
/*     */ 
/*     */     
/*  66 */     String existingBeanName = definitionHolder.getBeanName();
/*  67 */     BeanDefinition targetDefinition = definitionHolder.getBeanDefinition();
/*  68 */     BeanDefinitionHolder targetHolder = new BeanDefinitionHolder(targetDefinition, existingBeanName + ".TARGET");
/*     */ 
/*     */     
/*  71 */     BeanDefinition interceptorDefinition = createInterceptorDefinition(node);
/*     */ 
/*     */     
/*  74 */     String interceptorName = existingBeanName + '.' + getInterceptorNameSuffix(interceptorDefinition);
/*  75 */     BeanDefinitionReaderUtils.registerBeanDefinition(new BeanDefinitionHolder(interceptorDefinition, interceptorName), registry);
/*     */ 
/*     */     
/*  78 */     BeanDefinitionHolder result = definitionHolder;
/*     */     
/*  80 */     if (!isProxyFactoryBeanDefinition(targetDefinition)) {
/*     */       
/*  82 */       RootBeanDefinition proxyDefinition = new RootBeanDefinition();
/*     */       
/*  84 */       proxyDefinition.setBeanClass(ProxyFactoryBean.class);
/*  85 */       proxyDefinition.setScope(targetDefinition.getScope());
/*  86 */       proxyDefinition.setLazyInit(targetDefinition.isLazyInit());
/*     */       
/*  88 */       proxyDefinition.setDecoratedDefinition(targetHolder);
/*  89 */       proxyDefinition.getPropertyValues().add("target", targetHolder);
/*     */       
/*  91 */       proxyDefinition.getPropertyValues().add("interceptorNames", new ManagedList());
/*     */       
/*  93 */       proxyDefinition.setAutowireCandidate(targetDefinition.isAutowireCandidate());
/*  94 */       proxyDefinition.setPrimary(targetDefinition.isPrimary());
/*  95 */       if (targetDefinition instanceof AbstractBeanDefinition) {
/*  96 */         proxyDefinition.copyQualifiersFrom((AbstractBeanDefinition)targetDefinition);
/*     */       }
/*     */       
/*  99 */       result = new BeanDefinitionHolder((BeanDefinition)proxyDefinition, existingBeanName);
/*     */     } 
/*     */     
/* 102 */     addInterceptorNameToList(interceptorName, result.getBeanDefinition());
/* 103 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void addInterceptorNameToList(String interceptorName, BeanDefinition beanDefinition) {
/* 109 */     List<String> list = (List<String>)beanDefinition.getPropertyValues().getPropertyValue("interceptorNames").getValue();
/* 110 */     list.add(interceptorName);
/*     */   }
/*     */   
/*     */   private boolean isProxyFactoryBeanDefinition(BeanDefinition existingDefinition) {
/* 114 */     return ProxyFactoryBean.class.getName().equals(existingDefinition.getBeanClassName());
/*     */   }
/*     */   
/*     */   protected String getInterceptorNameSuffix(BeanDefinition interceptorDefinition) {
/* 118 */     return StringUtils.uncapitalize(ClassUtils.getShortName(interceptorDefinition.getBeanClassName()));
/*     */   }
/*     */   
/*     */   protected abstract BeanDefinition createInterceptorDefinition(Node paramNode);
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\config\AbstractInterceptorDrivenBeanDefinitionDecorator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */