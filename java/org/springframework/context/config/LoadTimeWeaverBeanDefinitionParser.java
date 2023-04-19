/*     */ package org.springframework.context.config;
/*     */ 
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.parsing.BeanComponentDefinition;
/*     */ import org.springframework.beans.factory.support.AbstractBeanDefinition;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionBuilder;
/*     */ import org.springframework.beans.factory.support.RootBeanDefinition;
/*     */ import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
/*     */ import org.springframework.beans.factory.xml.ParserContext;
/*     */ import org.springframework.util.ClassUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class LoadTimeWeaverBeanDefinitionParser
/*     */   extends AbstractSingleBeanDefinitionParser
/*     */ {
/*     */   public static final String ASPECTJ_WEAVING_ENABLER_BEAN_NAME = "org.springframework.context.config.internalAspectJWeavingEnabler";
/*     */   private static final String ASPECTJ_WEAVING_ENABLER_CLASS_NAME = "org.springframework.context.weaving.AspectJWeavingEnabler";
/*     */   private static final String DEFAULT_LOAD_TIME_WEAVER_CLASS_NAME = "org.springframework.context.weaving.DefaultContextLoadTimeWeaver";
/*     */   private static final String WEAVER_CLASS_ATTRIBUTE = "weaver-class";
/*     */   private static final String ASPECTJ_WEAVING_ATTRIBUTE = "aspectj-weaving";
/*     */   
/*     */   protected String getBeanClassName(Element element) {
/*  60 */     if (element.hasAttribute("weaver-class")) {
/*  61 */       return element.getAttribute("weaver-class");
/*     */     }
/*  63 */     return "org.springframework.context.weaving.DefaultContextLoadTimeWeaver";
/*     */   }
/*     */ 
/*     */   
/*     */   protected String resolveId(Element element, AbstractBeanDefinition definition, ParserContext parserContext) {
/*  68 */     return "loadTimeWeaver";
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
/*  73 */     builder.setRole(2);
/*     */     
/*  75 */     if (isAspectJWeavingEnabled(element.getAttribute("aspectj-weaving"), parserContext)) {
/*  76 */       if (!parserContext.getRegistry().containsBeanDefinition("org.springframework.context.config.internalAspectJWeavingEnabler")) {
/*  77 */         RootBeanDefinition def = new RootBeanDefinition("org.springframework.context.weaving.AspectJWeavingEnabler");
/*  78 */         parserContext.registerBeanComponent(new BeanComponentDefinition((BeanDefinition)def, "org.springframework.context.config.internalAspectJWeavingEnabler"));
/*     */       } 
/*     */ 
/*     */       
/*  82 */       if (isBeanConfigurerAspectEnabled(parserContext.getReaderContext().getBeanClassLoader())) {
/*  83 */         (new SpringConfiguredBeanDefinitionParser()).parse(element, parserContext);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   protected boolean isAspectJWeavingEnabled(String value, ParserContext parserContext) {
/*  89 */     if ("on".equals(value)) {
/*  90 */       return true;
/*     */     }
/*  92 */     if ("off".equals(value)) {
/*  93 */       return false;
/*     */     }
/*     */ 
/*     */     
/*  97 */     ClassLoader cl = parserContext.getReaderContext().getResourceLoader().getClassLoader();
/*  98 */     return (cl.getResource("META-INF/aop.xml") != null);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isBeanConfigurerAspectEnabled(ClassLoader beanClassLoader) {
/* 103 */     return ClassUtils.isPresent("org.springframework.beans.factory.aspectj.AnnotationBeanConfigurerAspect", beanClassLoader);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\config\LoadTimeWeaverBeanDefinitionParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */