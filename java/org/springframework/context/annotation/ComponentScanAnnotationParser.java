/*     */ package org.springframework.context.annotation;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.regex.Pattern;
/*     */ import org.springframework.beans.BeanUtils;
/*     */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*     */ import org.springframework.beans.factory.support.BeanNameGenerator;
/*     */ import org.springframework.core.annotation.AnnotationAttributes;
/*     */ import org.springframework.core.env.Environment;
/*     */ import org.springframework.core.io.ResourceLoader;
/*     */ import org.springframework.core.type.filter.AbstractTypeHierarchyTraversingFilter;
/*     */ import org.springframework.core.type.filter.AnnotationTypeFilter;
/*     */ import org.springframework.core.type.filter.AspectJTypeFilter;
/*     */ import org.springframework.core.type.filter.AssignableTypeFilter;
/*     */ import org.springframework.core.type.filter.RegexPatternTypeFilter;
/*     */ import org.springframework.core.type.filter.TypeFilter;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class ComponentScanAnnotationParser
/*     */ {
/*     */   private final Environment environment;
/*     */   private final ResourceLoader resourceLoader;
/*     */   private final BeanNameGenerator beanNameGenerator;
/*     */   private final BeanDefinitionRegistry registry;
/*     */   
/*     */   public ComponentScanAnnotationParser(Environment environment, ResourceLoader resourceLoader, BeanNameGenerator beanNameGenerator, BeanDefinitionRegistry registry) {
/*  69 */     this.environment = environment;
/*  70 */     this.resourceLoader = resourceLoader;
/*  71 */     this.beanNameGenerator = beanNameGenerator;
/*  72 */     this.registry = registry;
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<BeanDefinitionHolder> parse(AnnotationAttributes componentScan, final String declaringClass) {
/*  77 */     Assert.state((this.environment != null), "Environment must not be null");
/*  78 */     Assert.state((this.resourceLoader != null), "ResourceLoader must not be null");
/*     */ 
/*     */     
/*  81 */     ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(this.registry, componentScan.getBoolean("useDefaultFilters"), this.environment, this.resourceLoader);
/*     */     
/*  83 */     Class<? extends BeanNameGenerator> generatorClass = componentScan.getClass("nameGenerator");
/*  84 */     boolean useInheritedGenerator = (BeanNameGenerator.class == generatorClass);
/*  85 */     scanner.setBeanNameGenerator(useInheritedGenerator ? this.beanNameGenerator : 
/*  86 */         (BeanNameGenerator)BeanUtils.instantiateClass(generatorClass));
/*     */     
/*  88 */     ScopedProxyMode scopedProxyMode = (ScopedProxyMode)componentScan.getEnum("scopedProxy");
/*  89 */     if (scopedProxyMode != ScopedProxyMode.DEFAULT) {
/*  90 */       scanner.setScopedProxyMode(scopedProxyMode);
/*     */     } else {
/*     */       
/*  93 */       Class<? extends ScopeMetadataResolver> resolverClass = componentScan.getClass("scopeResolver");
/*  94 */       scanner.setScopeMetadataResolver((ScopeMetadataResolver)BeanUtils.instantiateClass(resolverClass));
/*     */     } 
/*     */     
/*  97 */     scanner.setResourcePattern(componentScan.getString("resourcePattern"));
/*     */     
/*  99 */     for (AnnotationAttributes filter : componentScan.getAnnotationArray("includeFilters")) {
/* 100 */       for (TypeFilter typeFilter : typeFiltersFor(filter)) {
/* 101 */         scanner.addIncludeFilter(typeFilter);
/*     */       }
/*     */     } 
/* 104 */     for (AnnotationAttributes filter : componentScan.getAnnotationArray("excludeFilters")) {
/* 105 */       for (TypeFilter typeFilter : typeFiltersFor(filter)) {
/* 106 */         scanner.addExcludeFilter(typeFilter);
/*     */       }
/*     */     } 
/*     */     
/* 110 */     boolean lazyInit = componentScan.getBoolean("lazyInit");
/* 111 */     if (lazyInit) {
/* 112 */       scanner.getBeanDefinitionDefaults().setLazyInit(true);
/*     */     }
/*     */     
/* 115 */     Set<String> basePackages = new LinkedHashSet<String>();
/* 116 */     String[] basePackagesArray = componentScan.getStringArray("basePackages");
/* 117 */     for (String pkg : basePackagesArray) {
/* 118 */       String[] tokenized = StringUtils.tokenizeToStringArray(this.environment.resolvePlaceholders(pkg), ",; \t\n");
/*     */       
/* 120 */       basePackages.addAll(Arrays.asList(tokenized));
/*     */     } 
/* 122 */     for (Class<?> clazz : componentScan.getClassArray("basePackageClasses")) {
/* 123 */       basePackages.add(ClassUtils.getPackageName(clazz));
/*     */     }
/* 125 */     if (basePackages.isEmpty()) {
/* 126 */       basePackages.add(ClassUtils.getPackageName(declaringClass));
/*     */     }
/*     */     
/* 129 */     scanner.addExcludeFilter((TypeFilter)new AbstractTypeHierarchyTraversingFilter(false, false)
/*     */         {
/*     */           protected boolean matchClassName(String className) {
/* 132 */             return declaringClass.equals(className);
/*     */           }
/*     */         });
/* 135 */     return scanner.doScan(StringUtils.toStringArray(basePackages));
/*     */   }
/*     */   
/*     */   private List<TypeFilter> typeFiltersFor(AnnotationAttributes filterAttributes) {
/* 139 */     List<TypeFilter> typeFilters = new ArrayList<TypeFilter>();
/* 140 */     FilterType filterType = (FilterType)filterAttributes.getEnum("type");
/*     */     
/* 142 */     for (Class<?> filterClass : filterAttributes.getClassArray("classes")) {
/* 143 */       Class<Annotation> annotationType; TypeFilter filter; switch (filterType) {
/*     */         case ANNOTATION:
/* 145 */           Assert.isAssignable(Annotation.class, filterClass, "@ComponentScan ANNOTATION type filter requires an annotation type");
/*     */ 
/*     */           
/* 148 */           annotationType = (Class)filterClass;
/* 149 */           typeFilters.add(new AnnotationTypeFilter(annotationType));
/*     */           break;
/*     */         case ASSIGNABLE_TYPE:
/* 152 */           typeFilters.add(new AssignableTypeFilter(filterClass));
/*     */           break;
/*     */         case CUSTOM:
/* 155 */           Assert.isAssignable(TypeFilter.class, filterClass, "@ComponentScan CUSTOM type filter requires a TypeFilter implementation");
/*     */           
/* 157 */           filter = (TypeFilter)BeanUtils.instantiateClass(filterClass, TypeFilter.class);
/* 158 */           ParserStrategyUtils.invokeAwareMethods(filter, this.environment, this.resourceLoader, this.registry);
/*     */           
/* 160 */           typeFilters.add(filter);
/*     */           break;
/*     */         default:
/* 163 */           throw new IllegalArgumentException("Filter type not supported with Class value: " + filterType);
/*     */       } 
/*     */     
/*     */     } 
/* 167 */     for (String expression : filterAttributes.getStringArray("pattern")) {
/* 168 */       switch (filterType) {
/*     */         case ASPECTJ:
/* 170 */           typeFilters.add(new AspectJTypeFilter(expression, this.resourceLoader.getClassLoader()));
/*     */           break;
/*     */         case REGEX:
/* 173 */           typeFilters.add(new RegexPatternTypeFilter(Pattern.compile(expression)));
/*     */           break;
/*     */         default:
/* 176 */           throw new IllegalArgumentException("Filter type not supported with String pattern: " + filterType);
/*     */       } 
/*     */     
/*     */     } 
/* 180 */     return typeFilters;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\annotation\ComponentScanAnnotationParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */