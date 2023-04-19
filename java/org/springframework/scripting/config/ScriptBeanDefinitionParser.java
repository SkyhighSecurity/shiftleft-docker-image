/*     */ package org.springframework.scripting.config;
/*     */ 
/*     */ import java.util.List;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.ConstructorArgumentValues;
/*     */ import org.springframework.beans.factory.config.RuntimeBeanReference;
/*     */ import org.springframework.beans.factory.support.AbstractBeanDefinition;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionDefaults;
/*     */ import org.springframework.beans.factory.support.GenericBeanDefinition;
/*     */ import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
/*     */ import org.springframework.beans.factory.xml.ParserContext;
/*     */ import org.springframework.beans.factory.xml.XmlReaderContext;
/*     */ import org.springframework.scripting.support.ScriptFactoryPostProcessor;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.util.xml.DomUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class ScriptBeanDefinitionParser
/*     */   extends AbstractBeanDefinitionParser
/*     */ {
/*     */   private static final String ENGINE_ATTRIBUTE = "engine";
/*     */   private static final String SCRIPT_SOURCE_ATTRIBUTE = "script-source";
/*     */   private static final String INLINE_SCRIPT_ELEMENT = "inline-script";
/*     */   private static final String SCOPE_ATTRIBUTE = "scope";
/*     */   private static final String AUTOWIRE_ATTRIBUTE = "autowire";
/*     */   private static final String DEPENDENCY_CHECK_ATTRIBUTE = "dependency-check";
/*     */   private static final String DEPENDS_ON_ATTRIBUTE = "depends-on";
/*     */   private static final String INIT_METHOD_ATTRIBUTE = "init-method";
/*     */   private static final String DESTROY_METHOD_ATTRIBUTE = "destroy-method";
/*     */   private static final String SCRIPT_INTERFACES_ATTRIBUTE = "script-interfaces";
/*     */   private static final String REFRESH_CHECK_DELAY_ATTRIBUTE = "refresh-check-delay";
/*     */   private static final String PROXY_TARGET_CLASS_ATTRIBUTE = "proxy-target-class";
/*     */   private static final String CUSTOMIZER_REF_ATTRIBUTE = "customizer-ref";
/*     */   private final String scriptFactoryClassName;
/*     */   
/*     */   public ScriptBeanDefinitionParser(String scriptFactoryClassName) {
/*  98 */     this.scriptFactoryClassName = scriptFactoryClassName;
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
/*     */   protected AbstractBeanDefinition parseInternal(Element element, ParserContext parserContext) {
/* 110 */     String engine = element.getAttribute("engine");
/*     */ 
/*     */     
/* 113 */     String value = resolveScriptSource(element, parserContext.getReaderContext());
/* 114 */     if (value == null) {
/* 115 */       return null;
/*     */     }
/*     */ 
/*     */     
/* 119 */     LangNamespaceUtils.registerScriptFactoryPostProcessorIfNecessary(parserContext.getRegistry());
/*     */ 
/*     */     
/* 122 */     GenericBeanDefinition bd = new GenericBeanDefinition();
/* 123 */     bd.setBeanClassName(this.scriptFactoryClassName);
/* 124 */     bd.setSource(parserContext.extractSource(element));
/* 125 */     bd.setAttribute(ScriptFactoryPostProcessor.LANGUAGE_ATTRIBUTE, element.getLocalName());
/*     */ 
/*     */     
/* 128 */     String scope = element.getAttribute("scope");
/* 129 */     if (StringUtils.hasLength(scope)) {
/* 130 */       bd.setScope(scope);
/*     */     }
/*     */ 
/*     */     
/* 134 */     String autowire = element.getAttribute("autowire");
/* 135 */     int autowireMode = parserContext.getDelegate().getAutowireMode(autowire);
/*     */     
/* 137 */     if (autowireMode == 4) {
/* 138 */       autowireMode = 2;
/*     */     }
/* 140 */     else if (autowireMode == 3) {
/* 141 */       autowireMode = 0;
/*     */     } 
/* 143 */     bd.setAutowireMode(autowireMode);
/*     */ 
/*     */     
/* 146 */     String dependencyCheck = element.getAttribute("dependency-check");
/* 147 */     bd.setDependencyCheck(parserContext.getDelegate().getDependencyCheck(dependencyCheck));
/*     */ 
/*     */     
/* 150 */     String dependsOn = element.getAttribute("depends-on");
/* 151 */     if (StringUtils.hasLength(dependsOn)) {
/* 152 */       bd.setDependsOn(StringUtils.tokenizeToStringArray(dependsOn, ",; "));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 157 */     BeanDefinitionDefaults beanDefinitionDefaults = parserContext.getDelegate().getBeanDefinitionDefaults();
/*     */ 
/*     */     
/* 160 */     String initMethod = element.getAttribute("init-method");
/* 161 */     if (StringUtils.hasLength(initMethod)) {
/* 162 */       bd.setInitMethodName(initMethod);
/*     */     }
/* 164 */     else if (beanDefinitionDefaults.getInitMethodName() != null) {
/* 165 */       bd.setInitMethodName(beanDefinitionDefaults.getInitMethodName());
/*     */     } 
/*     */     
/* 168 */     if (element.hasAttribute("destroy-method")) {
/* 169 */       String destroyMethod = element.getAttribute("destroy-method");
/* 170 */       bd.setDestroyMethodName(destroyMethod);
/*     */     }
/* 172 */     else if (beanDefinitionDefaults.getDestroyMethodName() != null) {
/* 173 */       bd.setDestroyMethodName(beanDefinitionDefaults.getDestroyMethodName());
/*     */     } 
/*     */ 
/*     */     
/* 177 */     String refreshCheckDelay = element.getAttribute("refresh-check-delay");
/* 178 */     if (StringUtils.hasText(refreshCheckDelay)) {
/* 179 */       bd.setAttribute(ScriptFactoryPostProcessor.REFRESH_CHECK_DELAY_ATTRIBUTE, Long.valueOf(refreshCheckDelay));
/*     */     }
/*     */ 
/*     */     
/* 183 */     String proxyTargetClass = element.getAttribute("proxy-target-class");
/* 184 */     if (StringUtils.hasText(proxyTargetClass)) {
/* 185 */       bd.setAttribute(ScriptFactoryPostProcessor.PROXY_TARGET_CLASS_ATTRIBUTE, Boolean.valueOf(proxyTargetClass));
/*     */     }
/*     */ 
/*     */     
/* 189 */     ConstructorArgumentValues cav = bd.getConstructorArgumentValues();
/* 190 */     int constructorArgNum = 0;
/* 191 */     if (StringUtils.hasLength(engine)) {
/* 192 */       cav.addIndexedArgumentValue(constructorArgNum++, engine);
/*     */     }
/* 194 */     cav.addIndexedArgumentValue(constructorArgNum++, value);
/* 195 */     if (element.hasAttribute("script-interfaces")) {
/* 196 */       cav.addIndexedArgumentValue(constructorArgNum++, element
/* 197 */           .getAttribute("script-interfaces"), "java.lang.Class[]");
/*     */     }
/*     */ 
/*     */     
/* 201 */     if (element.hasAttribute("customizer-ref")) {
/* 202 */       String customizerBeanName = element.getAttribute("customizer-ref");
/* 203 */       if (!StringUtils.hasText(customizerBeanName)) {
/* 204 */         parserContext.getReaderContext().error("Attribute 'customizer-ref' has empty value", element);
/*     */       } else {
/*     */         
/* 207 */         cav.addIndexedArgumentValue(constructorArgNum++, new RuntimeBeanReference(customizerBeanName));
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 212 */     parserContext.getDelegate().parsePropertyElements(element, (BeanDefinition)bd);
/*     */     
/* 214 */     return (AbstractBeanDefinition)bd;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String resolveScriptSource(Element element, XmlReaderContext readerContext) {
/* 223 */     boolean hasScriptSource = element.hasAttribute("script-source");
/* 224 */     List<Element> elements = DomUtils.getChildElementsByTagName(element, "inline-script");
/* 225 */     if (hasScriptSource && !elements.isEmpty()) {
/* 226 */       readerContext.error("Only one of 'script-source' and 'inline-script' should be specified.", element);
/* 227 */       return null;
/*     */     } 
/* 229 */     if (hasScriptSource) {
/* 230 */       return element.getAttribute("script-source");
/*     */     }
/* 232 */     if (!elements.isEmpty()) {
/* 233 */       Element inlineElement = elements.get(0);
/* 234 */       return "inline:" + DomUtils.getTextValue(inlineElement);
/*     */     } 
/*     */     
/* 237 */     readerContext.error("Must specify either 'script-source' or 'inline-script'.", element);
/* 238 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean shouldGenerateIdAsFallback() {
/* 247 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\scripting\config\ScriptBeanDefinitionParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */