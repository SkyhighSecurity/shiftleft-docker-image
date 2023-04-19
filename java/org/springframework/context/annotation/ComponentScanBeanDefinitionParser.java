/*     */ package org.springframework.context.annotation;
/*     */ 
/*     */ import java.util.Set;
/*     */ import java.util.regex.Pattern;
/*     */ import org.springframework.beans.BeanUtils;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*     */ import org.springframework.beans.factory.parsing.BeanComponentDefinition;
/*     */ import org.springframework.beans.factory.parsing.ComponentDefinition;
/*     */ import org.springframework.beans.factory.parsing.CompositeComponentDefinition;
/*     */ import org.springframework.beans.factory.support.BeanNameGenerator;
/*     */ import org.springframework.beans.factory.xml.BeanDefinitionParser;
/*     */ import org.springframework.beans.factory.xml.ParserContext;
/*     */ import org.springframework.beans.factory.xml.XmlReaderContext;
/*     */ import org.springframework.core.type.filter.AnnotationTypeFilter;
/*     */ import org.springframework.core.type.filter.AspectJTypeFilter;
/*     */ import org.springframework.core.type.filter.AssignableTypeFilter;
/*     */ import org.springframework.core.type.filter.RegexPatternTypeFilter;
/*     */ import org.springframework.core.type.filter.TypeFilter;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ComponentScanBeanDefinitionParser
/*     */   implements BeanDefinitionParser
/*     */ {
/*     */   private static final String BASE_PACKAGE_ATTRIBUTE = "base-package";
/*     */   private static final String RESOURCE_PATTERN_ATTRIBUTE = "resource-pattern";
/*     */   private static final String USE_DEFAULT_FILTERS_ATTRIBUTE = "use-default-filters";
/*     */   private static final String ANNOTATION_CONFIG_ATTRIBUTE = "annotation-config";
/*     */   private static final String NAME_GENERATOR_ATTRIBUTE = "name-generator";
/*     */   private static final String SCOPE_RESOLVER_ATTRIBUTE = "scope-resolver";
/*     */   private static final String SCOPED_PROXY_ATTRIBUTE = "scoped-proxy";
/*     */   private static final String EXCLUDE_FILTER_ELEMENT = "exclude-filter";
/*     */   private static final String INCLUDE_FILTER_ELEMENT = "include-filter";
/*     */   private static final String FILTER_TYPE_ATTRIBUTE = "type";
/*     */   private static final String FILTER_EXPRESSION_ATTRIBUTE = "expression";
/*     */   
/*     */   public BeanDefinition parse(Element element, ParserContext parserContext) {
/*  80 */     String basePackage = element.getAttribute("base-package");
/*  81 */     basePackage = parserContext.getReaderContext().getEnvironment().resolvePlaceholders(basePackage);
/*  82 */     String[] basePackages = StringUtils.tokenizeToStringArray(basePackage, ",; \t\n");
/*     */ 
/*     */ 
/*     */     
/*  86 */     ClassPathBeanDefinitionScanner scanner = configureScanner(parserContext, element);
/*  87 */     Set<BeanDefinitionHolder> beanDefinitions = scanner.doScan(basePackages);
/*  88 */     registerComponents(parserContext.getReaderContext(), beanDefinitions, element);
/*     */     
/*  90 */     return null;
/*     */   }
/*     */   
/*     */   protected ClassPathBeanDefinitionScanner configureScanner(ParserContext parserContext, Element element) {
/*  94 */     boolean useDefaultFilters = true;
/*  95 */     if (element.hasAttribute("use-default-filters")) {
/*  96 */       useDefaultFilters = Boolean.valueOf(element.getAttribute("use-default-filters")).booleanValue();
/*     */     }
/*     */ 
/*     */     
/* 100 */     ClassPathBeanDefinitionScanner scanner = createScanner(parserContext.getReaderContext(), useDefaultFilters);
/* 101 */     scanner.setBeanDefinitionDefaults(parserContext.getDelegate().getBeanDefinitionDefaults());
/* 102 */     scanner.setAutowireCandidatePatterns(parserContext.getDelegate().getAutowireCandidatePatterns());
/*     */     
/* 104 */     if (element.hasAttribute("resource-pattern")) {
/* 105 */       scanner.setResourcePattern(element.getAttribute("resource-pattern"));
/*     */     }
/*     */     
/*     */     try {
/* 109 */       parseBeanNameGenerator(element, scanner);
/*     */     }
/* 111 */     catch (Exception ex) {
/* 112 */       parserContext.getReaderContext().error(ex.getMessage(), parserContext.extractSource(element), ex.getCause());
/*     */     } 
/*     */     
/*     */     try {
/* 116 */       parseScope(element, scanner);
/*     */     }
/* 118 */     catch (Exception ex) {
/* 119 */       parserContext.getReaderContext().error(ex.getMessage(), parserContext.extractSource(element), ex.getCause());
/*     */     } 
/*     */     
/* 122 */     parseTypeFilters(element, scanner, parserContext);
/*     */     
/* 124 */     return scanner;
/*     */   }
/*     */   
/*     */   protected ClassPathBeanDefinitionScanner createScanner(XmlReaderContext readerContext, boolean useDefaultFilters) {
/* 128 */     return new ClassPathBeanDefinitionScanner(readerContext.getRegistry(), useDefaultFilters, readerContext
/* 129 */         .getEnvironment(), readerContext.getResourceLoader());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void registerComponents(XmlReaderContext readerContext, Set<BeanDefinitionHolder> beanDefinitions, Element element) {
/* 135 */     Object source = readerContext.extractSource(element);
/* 136 */     CompositeComponentDefinition compositeDef = new CompositeComponentDefinition(element.getTagName(), source);
/*     */     
/* 138 */     for (BeanDefinitionHolder beanDefHolder : beanDefinitions) {
/* 139 */       compositeDef.addNestedComponent((ComponentDefinition)new BeanComponentDefinition(beanDefHolder));
/*     */     }
/*     */ 
/*     */     
/* 143 */     boolean annotationConfig = true;
/* 144 */     if (element.hasAttribute("annotation-config")) {
/* 145 */       annotationConfig = Boolean.valueOf(element.getAttribute("annotation-config")).booleanValue();
/*     */     }
/* 147 */     if (annotationConfig) {
/*     */       
/* 149 */       Set<BeanDefinitionHolder> processorDefinitions = AnnotationConfigUtils.registerAnnotationConfigProcessors(readerContext.getRegistry(), source);
/* 150 */       for (BeanDefinitionHolder processorDefinition : processorDefinitions) {
/* 151 */         compositeDef.addNestedComponent((ComponentDefinition)new BeanComponentDefinition(processorDefinition));
/*     */       }
/*     */     } 
/*     */     
/* 155 */     readerContext.fireComponentRegistered((ComponentDefinition)compositeDef);
/*     */   }
/*     */   
/*     */   protected void parseBeanNameGenerator(Element element, ClassPathBeanDefinitionScanner scanner) {
/* 159 */     if (element.hasAttribute("name-generator")) {
/* 160 */       BeanNameGenerator beanNameGenerator = (BeanNameGenerator)instantiateUserDefinedStrategy(element
/* 161 */           .getAttribute("name-generator"), BeanNameGenerator.class, scanner
/* 162 */           .getResourceLoader().getClassLoader());
/* 163 */       scanner.setBeanNameGenerator(beanNameGenerator);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void parseScope(Element element, ClassPathBeanDefinitionScanner scanner) {
/* 169 */     if (element.hasAttribute("scope-resolver")) {
/* 170 */       if (element.hasAttribute("scoped-proxy")) {
/* 171 */         throw new IllegalArgumentException("Cannot define both 'scope-resolver' and 'scoped-proxy' on <component-scan> tag");
/*     */       }
/*     */       
/* 174 */       ScopeMetadataResolver scopeMetadataResolver = (ScopeMetadataResolver)instantiateUserDefinedStrategy(element
/* 175 */           .getAttribute("scope-resolver"), ScopeMetadataResolver.class, scanner
/* 176 */           .getResourceLoader().getClassLoader());
/* 177 */       scanner.setScopeMetadataResolver(scopeMetadataResolver);
/*     */     } 
/*     */     
/* 180 */     if (element.hasAttribute("scoped-proxy")) {
/* 181 */       String mode = element.getAttribute("scoped-proxy");
/* 182 */       if ("targetClass".equals(mode)) {
/* 183 */         scanner.setScopedProxyMode(ScopedProxyMode.TARGET_CLASS);
/*     */       }
/* 185 */       else if ("interfaces".equals(mode)) {
/* 186 */         scanner.setScopedProxyMode(ScopedProxyMode.INTERFACES);
/*     */       }
/* 188 */       else if ("no".equals(mode)) {
/* 189 */         scanner.setScopedProxyMode(ScopedProxyMode.NO);
/*     */       } else {
/*     */         
/* 192 */         throw new IllegalArgumentException("scoped-proxy only supports 'no', 'interfaces' and 'targetClass'");
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void parseTypeFilters(Element element, ClassPathBeanDefinitionScanner scanner, ParserContext parserContext) {
/* 199 */     ClassLoader classLoader = scanner.getResourceLoader().getClassLoader();
/* 200 */     NodeList nodeList = element.getChildNodes();
/* 201 */     for (int i = 0; i < nodeList.getLength(); i++) {
/* 202 */       Node node = nodeList.item(i);
/* 203 */       if (node.getNodeType() == 1) {
/* 204 */         String localName = parserContext.getDelegate().getLocalName(node);
/*     */         try {
/* 206 */           if ("include-filter".equals(localName)) {
/* 207 */             TypeFilter typeFilter = createTypeFilter((Element)node, classLoader, parserContext);
/* 208 */             scanner.addIncludeFilter(typeFilter);
/*     */           }
/* 210 */           else if ("exclude-filter".equals(localName)) {
/* 211 */             TypeFilter typeFilter = createTypeFilter((Element)node, classLoader, parserContext);
/* 212 */             scanner.addExcludeFilter(typeFilter);
/*     */           }
/*     */         
/* 215 */         } catch (ClassNotFoundException ex) {
/* 216 */           parserContext.getReaderContext().warning("Ignoring non-present type filter class: " + ex, parserContext
/* 217 */               .extractSource(element));
/*     */         }
/* 219 */         catch (Exception ex) {
/* 220 */           parserContext.getReaderContext().error(ex
/* 221 */               .getMessage(), parserContext.extractSource(element), ex.getCause());
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected TypeFilter createTypeFilter(Element element, ClassLoader classLoader, ParserContext parserContext) throws ClassNotFoundException {
/* 231 */     String filterType = element.getAttribute("type");
/* 232 */     String expression = element.getAttribute("expression");
/* 233 */     expression = parserContext.getReaderContext().getEnvironment().resolvePlaceholders(expression);
/* 234 */     if ("annotation".equals(filterType)) {
/* 235 */       return (TypeFilter)new AnnotationTypeFilter(ClassUtils.forName(expression, classLoader));
/*     */     }
/* 237 */     if ("assignable".equals(filterType)) {
/* 238 */       return (TypeFilter)new AssignableTypeFilter(ClassUtils.forName(expression, classLoader));
/*     */     }
/* 240 */     if ("aspectj".equals(filterType)) {
/* 241 */       return (TypeFilter)new AspectJTypeFilter(expression, classLoader);
/*     */     }
/* 243 */     if ("regex".equals(filterType)) {
/* 244 */       return (TypeFilter)new RegexPatternTypeFilter(Pattern.compile(expression));
/*     */     }
/* 246 */     if ("custom".equals(filterType)) {
/* 247 */       Class<?> filterClass = ClassUtils.forName(expression, classLoader);
/* 248 */       if (!TypeFilter.class.isAssignableFrom(filterClass)) {
/* 249 */         throw new IllegalArgumentException("Class is not assignable to [" + TypeFilter.class
/* 250 */             .getName() + "]: " + expression);
/*     */       }
/* 252 */       return (TypeFilter)BeanUtils.instantiateClass(filterClass);
/*     */     } 
/*     */     
/* 255 */     throw new IllegalArgumentException("Unsupported filter type: " + filterType);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Object instantiateUserDefinedStrategy(String className, Class<?> strategyType, ClassLoader classLoader) {
/*     */     Object result;
/*     */     try {
/* 263 */       result = classLoader.loadClass(className).newInstance();
/*     */     }
/* 265 */     catch (ClassNotFoundException ex) {
/* 266 */       throw new IllegalArgumentException("Class [" + className + "] for strategy [" + strategyType
/* 267 */           .getName() + "] not found", ex);
/*     */     }
/* 269 */     catch (Throwable ex) {
/* 270 */       throw new IllegalArgumentException("Unable to instantiate class [" + className + "] for strategy [" + strategyType
/* 271 */           .getName() + "]: a zero-argument constructor is required", ex);
/*     */     } 
/*     */     
/* 274 */     if (!strategyType.isAssignableFrom(result.getClass())) {
/* 275 */       throw new IllegalArgumentException("Provided class name must be an implementation of " + strategyType);
/*     */     }
/* 277 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\annotation\ComponentScanBeanDefinitionParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */