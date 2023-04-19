/*     */ package org.springframework.context.annotation;
/*     */ 
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.springframework.beans.factory.parsing.Location;
/*     */ import org.springframework.beans.factory.parsing.Problem;
/*     */ import org.springframework.beans.factory.parsing.ProblemReporter;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionReader;
/*     */ import org.springframework.core.io.DescriptiveResource;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.type.AnnotationMetadata;
/*     */ import org.springframework.core.type.StandardAnnotationMetadata;
/*     */ import org.springframework.core.type.classreading.MetadataReader;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
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
/*     */ final class ConfigurationClass
/*     */ {
/*     */   private final AnnotationMetadata metadata;
/*     */   private final Resource resource;
/*     */   private String beanName;
/*  57 */   private final Set<ConfigurationClass> importedBy = new LinkedHashSet<ConfigurationClass>(1);
/*     */   
/*  59 */   private final Set<BeanMethod> beanMethods = new LinkedHashSet<BeanMethod>();
/*     */   
/*  61 */   private final Map<String, Class<? extends BeanDefinitionReader>> importedResources = new LinkedHashMap<String, Class<? extends BeanDefinitionReader>>();
/*     */ 
/*     */   
/*  64 */   private final Map<ImportBeanDefinitionRegistrar, AnnotationMetadata> importBeanDefinitionRegistrars = new LinkedHashMap<ImportBeanDefinitionRegistrar, AnnotationMetadata>();
/*     */ 
/*     */   
/*  67 */   final Set<String> skippedBeanMethods = new HashSet<String>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConfigurationClass(MetadataReader metadataReader, String beanName) {
/*  77 */     Assert.hasText(beanName, "Bean name must not be null");
/*  78 */     this.metadata = metadataReader.getAnnotationMetadata();
/*  79 */     this.resource = metadataReader.getResource();
/*  80 */     this.beanName = beanName;
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
/*     */   public ConfigurationClass(MetadataReader metadataReader, ConfigurationClass importedBy) {
/*  92 */     this.metadata = metadataReader.getAnnotationMetadata();
/*  93 */     this.resource = metadataReader.getResource();
/*  94 */     this.importedBy.add(importedBy);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConfigurationClass(Class<?> clazz, String beanName) {
/* 104 */     Assert.hasText(beanName, "Bean name must not be null");
/* 105 */     this.metadata = (AnnotationMetadata)new StandardAnnotationMetadata(clazz, true);
/* 106 */     this.resource = (Resource)new DescriptiveResource(clazz.getName());
/* 107 */     this.beanName = beanName;
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
/*     */   public ConfigurationClass(Class<?> clazz, ConfigurationClass importedBy) {
/* 119 */     this.metadata = (AnnotationMetadata)new StandardAnnotationMetadata(clazz, true);
/* 120 */     this.resource = (Resource)new DescriptiveResource(clazz.getName());
/* 121 */     this.importedBy.add(importedBy);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConfigurationClass(AnnotationMetadata metadata, String beanName) {
/* 131 */     Assert.hasText(beanName, "Bean name must not be null");
/* 132 */     this.metadata = metadata;
/* 133 */     this.resource = (Resource)new DescriptiveResource(metadata.getClassName());
/* 134 */     this.beanName = beanName;
/*     */   }
/*     */ 
/*     */   
/*     */   public AnnotationMetadata getMetadata() {
/* 139 */     return this.metadata;
/*     */   }
/*     */   
/*     */   public Resource getResource() {
/* 143 */     return this.resource;
/*     */   }
/*     */   
/*     */   public String getSimpleName() {
/* 147 */     return ClassUtils.getShortName(getMetadata().getClassName());
/*     */   }
/*     */   
/*     */   public void setBeanName(String beanName) {
/* 151 */     this.beanName = beanName;
/*     */   }
/*     */   
/*     */   public String getBeanName() {
/* 155 */     return this.beanName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isImported() {
/* 165 */     return !this.importedBy.isEmpty();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void mergeImportedBy(ConfigurationClass otherConfigClass) {
/* 173 */     this.importedBy.addAll(otherConfigClass.importedBy);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<ConfigurationClass> getImportedBy() {
/* 183 */     return this.importedBy;
/*     */   }
/*     */   
/*     */   public void addBeanMethod(BeanMethod method) {
/* 187 */     this.beanMethods.add(method);
/*     */   }
/*     */   
/*     */   public Set<BeanMethod> getBeanMethods() {
/* 191 */     return this.beanMethods;
/*     */   }
/*     */   
/*     */   public void addImportedResource(String importedResource, Class<? extends BeanDefinitionReader> readerClass) {
/* 195 */     this.importedResources.put(importedResource, readerClass);
/*     */   }
/*     */   
/*     */   public void addImportBeanDefinitionRegistrar(ImportBeanDefinitionRegistrar registrar, AnnotationMetadata importingClassMetadata) {
/* 199 */     this.importBeanDefinitionRegistrars.put(registrar, importingClassMetadata);
/*     */   }
/*     */   
/*     */   public Map<ImportBeanDefinitionRegistrar, AnnotationMetadata> getImportBeanDefinitionRegistrars() {
/* 203 */     return this.importBeanDefinitionRegistrars;
/*     */   }
/*     */   
/*     */   public Map<String, Class<? extends BeanDefinitionReader>> getImportedResources() {
/* 207 */     return this.importedResources;
/*     */   }
/*     */ 
/*     */   
/*     */   public void validate(ProblemReporter problemReporter) {
/* 212 */     if (getMetadata().isAnnotated(Configuration.class.getName()) && 
/* 213 */       getMetadata().isFinal()) {
/* 214 */       problemReporter.error(new FinalConfigurationProblem());
/*     */     }
/*     */ 
/*     */     
/* 218 */     for (BeanMethod beanMethod : this.beanMethods) {
/* 219 */       beanMethod.validate(problemReporter);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 225 */     return (this == other || (other instanceof ConfigurationClass && 
/* 226 */       getMetadata().getClassName().equals(((ConfigurationClass)other).getMetadata().getClassName())));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 231 */     return getMetadata().getClassName().hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 236 */     return "ConfigurationClass: beanName '" + this.beanName + "', " + this.resource;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class FinalConfigurationProblem
/*     */     extends Problem
/*     */   {
/*     */     public FinalConfigurationProblem() {
/* 246 */       super(String.format("@Configuration class '%s' may not be final. Remove the final modifier to continue.", new Object[] { this$0
/* 247 */               .getSimpleName() }), new Location(ConfigurationClass.this.getResource(), ConfigurationClass.this.getMetadata()));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\annotation\ConfigurationClass.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */