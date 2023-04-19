/*     */ package org.springframework.cache.config;
/*     */ 
/*     */ import org.springframework.aop.config.AopNamespaceUtils;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.RuntimeBeanReference;
/*     */ import org.springframework.beans.factory.parsing.BeanComponentDefinition;
/*     */ import org.springframework.beans.factory.parsing.ComponentDefinition;
/*     */ import org.springframework.beans.factory.parsing.CompositeComponentDefinition;
/*     */ import org.springframework.beans.factory.support.RootBeanDefinition;
/*     */ import org.springframework.beans.factory.xml.BeanDefinitionParser;
/*     */ import org.springframework.beans.factory.xml.ParserContext;
/*     */ import org.springframework.cache.interceptor.BeanFactoryCacheOperationSourceAdvisor;
/*     */ import org.springframework.cache.interceptor.CacheInterceptor;
/*     */ import org.springframework.util.ClassUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class AnnotationDrivenCacheBeanDefinitionParser
/*     */   implements BeanDefinitionParser
/*     */ {
/*     */   private static final String CACHE_ASPECT_CLASS_NAME = "org.springframework.cache.aspectj.AnnotationCacheAspect";
/*     */   private static final String JCACHE_ASPECT_CLASS_NAME = "org.springframework.cache.aspectj.JCacheCacheAspect";
/*  63 */   private static final boolean jsr107Present = ClassUtils.isPresent("javax.cache.Cache", AnnotationDrivenCacheBeanDefinitionParser.class
/*  64 */       .getClassLoader());
/*     */   
/*  66 */   private static final boolean jcacheImplPresent = ClassUtils.isPresent("org.springframework.cache.jcache.interceptor.DefaultJCacheOperationSource", AnnotationDrivenCacheBeanDefinitionParser.class
/*     */       
/*  68 */       .getClassLoader());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanDefinition parse(Element element, ParserContext parserContext) {
/*  78 */     String mode = element.getAttribute("mode");
/*  79 */     if ("aspectj".equals(mode)) {
/*     */       
/*  81 */       registerCacheAspect(element, parserContext);
/*     */     }
/*     */     else {
/*     */       
/*  85 */       registerCacheAdvisor(element, parserContext);
/*     */     } 
/*     */     
/*  88 */     return null;
/*     */   }
/*     */   
/*     */   private void registerCacheAspect(Element element, ParserContext parserContext) {
/*  92 */     SpringCachingConfigurer.registerCacheAspect(element, parserContext);
/*  93 */     if (jsr107Present && jcacheImplPresent) {
/*  94 */       JCacheCachingConfigurer.registerCacheAspect(element, parserContext);
/*     */     }
/*     */   }
/*     */   
/*     */   private void registerCacheAdvisor(Element element, ParserContext parserContext) {
/*  99 */     AopNamespaceUtils.registerAutoProxyCreatorIfNecessary(parserContext, element);
/* 100 */     SpringCachingConfigurer.registerCacheAdvisor(element, parserContext);
/* 101 */     if (jsr107Present && jcacheImplPresent) {
/* 102 */       JCacheCachingConfigurer.registerCacheAdvisor(element, parserContext);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void parseCacheResolution(Element element, BeanDefinition def, boolean setBoth) {
/* 112 */     String name = element.getAttribute("cache-resolver");
/* 113 */     boolean hasText = StringUtils.hasText(name);
/* 114 */     if (hasText) {
/* 115 */       def.getPropertyValues().add("cacheResolver", new RuntimeBeanReference(name.trim()));
/*     */     }
/* 117 */     if (!hasText || setBoth) {
/* 118 */       def.getPropertyValues().add("cacheManager", new RuntimeBeanReference(
/* 119 */             CacheNamespaceHandler.extractCacheManager(element)));
/*     */     }
/*     */   }
/*     */   
/*     */   private static void parseErrorHandler(Element element, BeanDefinition def) {
/* 124 */     String name = element.getAttribute("error-handler");
/* 125 */     if (StringUtils.hasText(name)) {
/* 126 */       def.getPropertyValues().add("errorHandler", new RuntimeBeanReference(name.trim()));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class SpringCachingConfigurer
/*     */   {
/*     */     private static void registerCacheAdvisor(Element element, ParserContext parserContext) {
/* 137 */       if (!parserContext.getRegistry().containsBeanDefinition("org.springframework.cache.config.internalCacheAdvisor")) {
/* 138 */         Object eleSource = parserContext.extractSource(element);
/*     */ 
/*     */         
/* 141 */         RootBeanDefinition sourceDef = new RootBeanDefinition("org.springframework.cache.annotation.AnnotationCacheOperationSource");
/* 142 */         sourceDef.setSource(eleSource);
/* 143 */         sourceDef.setRole(2);
/* 144 */         String sourceName = parserContext.getReaderContext().registerWithGeneratedName((BeanDefinition)sourceDef);
/*     */ 
/*     */         
/* 147 */         RootBeanDefinition interceptorDef = new RootBeanDefinition(CacheInterceptor.class);
/* 148 */         interceptorDef.setSource(eleSource);
/* 149 */         interceptorDef.setRole(2);
/* 150 */         AnnotationDrivenCacheBeanDefinitionParser.parseCacheResolution(element, (BeanDefinition)interceptorDef, false);
/* 151 */         AnnotationDrivenCacheBeanDefinitionParser.parseErrorHandler(element, (BeanDefinition)interceptorDef);
/* 152 */         CacheNamespaceHandler.parseKeyGenerator(element, (BeanDefinition)interceptorDef);
/* 153 */         interceptorDef.getPropertyValues().add("cacheOperationSources", new RuntimeBeanReference(sourceName));
/* 154 */         String interceptorName = parserContext.getReaderContext().registerWithGeneratedName((BeanDefinition)interceptorDef);
/*     */ 
/*     */         
/* 157 */         RootBeanDefinition advisorDef = new RootBeanDefinition(BeanFactoryCacheOperationSourceAdvisor.class);
/* 158 */         advisorDef.setSource(eleSource);
/* 159 */         advisorDef.setRole(2);
/* 160 */         advisorDef.getPropertyValues().add("cacheOperationSource", new RuntimeBeanReference(sourceName));
/* 161 */         advisorDef.getPropertyValues().add("adviceBeanName", interceptorName);
/* 162 */         if (element.hasAttribute("order")) {
/* 163 */           advisorDef.getPropertyValues().add("order", element.getAttribute("order"));
/*     */         }
/* 165 */         parserContext.getRegistry().registerBeanDefinition("org.springframework.cache.config.internalCacheAdvisor", (BeanDefinition)advisorDef);
/*     */         
/* 167 */         CompositeComponentDefinition compositeDef = new CompositeComponentDefinition(element.getTagName(), eleSource);
/* 168 */         compositeDef.addNestedComponent((ComponentDefinition)new BeanComponentDefinition((BeanDefinition)sourceDef, sourceName));
/* 169 */         compositeDef.addNestedComponent((ComponentDefinition)new BeanComponentDefinition((BeanDefinition)interceptorDef, interceptorName));
/* 170 */         compositeDef.addNestedComponent((ComponentDefinition)new BeanComponentDefinition((BeanDefinition)advisorDef, "org.springframework.cache.config.internalCacheAdvisor"));
/* 171 */         parserContext.registerComponent((ComponentDefinition)compositeDef);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private static void registerCacheAspect(Element element, ParserContext parserContext) {
/* 185 */       if (!parserContext.getRegistry().containsBeanDefinition("org.springframework.cache.config.internalCacheAspect")) {
/* 186 */         RootBeanDefinition def = new RootBeanDefinition();
/* 187 */         def.setBeanClassName("org.springframework.cache.aspectj.AnnotationCacheAspect");
/* 188 */         def.setFactoryMethodName("aspectOf");
/* 189 */         AnnotationDrivenCacheBeanDefinitionParser.parseCacheResolution(element, (BeanDefinition)def, false);
/* 190 */         CacheNamespaceHandler.parseKeyGenerator(element, (BeanDefinition)def);
/* 191 */         parserContext.registerBeanComponent(new BeanComponentDefinition((BeanDefinition)def, "org.springframework.cache.config.internalCacheAspect"));
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class JCacheCachingConfigurer
/*     */   {
/*     */     private static void registerCacheAdvisor(Element element, ParserContext parserContext) {
/* 203 */       if (!parserContext.getRegistry().containsBeanDefinition("org.springframework.cache.config.internalJCacheAdvisor")) {
/* 204 */         Object source = parserContext.extractSource(element);
/*     */ 
/*     */         
/* 207 */         RootBeanDefinition rootBeanDefinition1 = createJCacheOperationSourceBeanDefinition(element, source);
/* 208 */         String sourceName = parserContext.getReaderContext().registerWithGeneratedName((BeanDefinition)rootBeanDefinition1);
/*     */ 
/*     */         
/* 211 */         RootBeanDefinition interceptorDef = new RootBeanDefinition("org.springframework.cache.jcache.interceptor.JCacheInterceptor");
/*     */         
/* 213 */         interceptorDef.setSource(source);
/* 214 */         interceptorDef.setRole(2);
/* 215 */         interceptorDef.getPropertyValues().add("cacheOperationSource", new RuntimeBeanReference(sourceName));
/* 216 */         AnnotationDrivenCacheBeanDefinitionParser.parseErrorHandler(element, (BeanDefinition)interceptorDef);
/* 217 */         String interceptorName = parserContext.getReaderContext().registerWithGeneratedName((BeanDefinition)interceptorDef);
/*     */ 
/*     */         
/* 220 */         RootBeanDefinition advisorDef = new RootBeanDefinition("org.springframework.cache.jcache.interceptor.BeanFactoryJCacheOperationSourceAdvisor");
/*     */         
/* 222 */         advisorDef.setSource(source);
/* 223 */         advisorDef.setRole(2);
/* 224 */         advisorDef.getPropertyValues().add("cacheOperationSource", new RuntimeBeanReference(sourceName));
/* 225 */         advisorDef.getPropertyValues().add("adviceBeanName", interceptorName);
/* 226 */         if (element.hasAttribute("order")) {
/* 227 */           advisorDef.getPropertyValues().add("order", element.getAttribute("order"));
/*     */         }
/* 229 */         parserContext.getRegistry().registerBeanDefinition("org.springframework.cache.config.internalJCacheAdvisor", (BeanDefinition)advisorDef);
/*     */         
/* 231 */         CompositeComponentDefinition compositeDef = new CompositeComponentDefinition(element.getTagName(), source);
/* 232 */         compositeDef.addNestedComponent((ComponentDefinition)new BeanComponentDefinition((BeanDefinition)rootBeanDefinition1, sourceName));
/* 233 */         compositeDef.addNestedComponent((ComponentDefinition)new BeanComponentDefinition((BeanDefinition)interceptorDef, interceptorName));
/* 234 */         compositeDef.addNestedComponent((ComponentDefinition)new BeanComponentDefinition((BeanDefinition)advisorDef, "org.springframework.cache.config.internalJCacheAdvisor"));
/* 235 */         parserContext.registerComponent((ComponentDefinition)compositeDef);
/*     */       } 
/*     */     }
/*     */     
/*     */     private static void registerCacheAspect(Element element, ParserContext parserContext) {
/* 240 */       if (!parserContext.getRegistry().containsBeanDefinition("org.springframework.cache.config.internalJCacheAspect")) {
/* 241 */         Object eleSource = parserContext.extractSource(element);
/* 242 */         RootBeanDefinition def = new RootBeanDefinition();
/* 243 */         def.setBeanClassName("org.springframework.cache.aspectj.JCacheCacheAspect");
/* 244 */         def.setFactoryMethodName("aspectOf");
/* 245 */         RootBeanDefinition rootBeanDefinition1 = createJCacheOperationSourceBeanDefinition(element, eleSource);
/*     */         
/* 247 */         String sourceName = parserContext.getReaderContext().registerWithGeneratedName((BeanDefinition)rootBeanDefinition1);
/* 248 */         def.getPropertyValues().add("cacheOperationSource", new RuntimeBeanReference(sourceName));
/*     */         
/* 250 */         parserContext.registerBeanComponent(new BeanComponentDefinition((BeanDefinition)rootBeanDefinition1, sourceName));
/* 251 */         parserContext.registerBeanComponent(new BeanComponentDefinition((BeanDefinition)def, "org.springframework.cache.config.internalJCacheAspect"));
/*     */       } 
/*     */     }
/*     */     
/*     */     private static RootBeanDefinition createJCacheOperationSourceBeanDefinition(Element element, Object eleSource) {
/* 256 */       RootBeanDefinition sourceDef = new RootBeanDefinition("org.springframework.cache.jcache.interceptor.DefaultJCacheOperationSource");
/*     */       
/* 258 */       sourceDef.setSource(eleSource);
/* 259 */       sourceDef.setRole(2);
/*     */ 
/*     */       
/* 262 */       AnnotationDrivenCacheBeanDefinitionParser.parseCacheResolution(element, (BeanDefinition)sourceDef, true);
/* 263 */       CacheNamespaceHandler.parseKeyGenerator(element, (BeanDefinition)sourceDef);
/* 264 */       return sourceDef;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\cache\config\AnnotationDrivenCacheBeanDefinitionParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */