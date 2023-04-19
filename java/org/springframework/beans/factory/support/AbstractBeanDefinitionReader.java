/*     */ package org.springframework.beans.factory.support;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.factory.BeanDefinitionStoreException;
/*     */ import org.springframework.core.env.Environment;
/*     */ import org.springframework.core.env.EnvironmentCapable;
/*     */ import org.springframework.core.env.StandardEnvironment;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.ResourceLoader;
/*     */ import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
/*     */ import org.springframework.core.io.support.ResourcePatternResolver;
/*     */ import org.springframework.util.Assert;
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
/*     */ public abstract class AbstractBeanDefinitionReader
/*     */   implements EnvironmentCapable, BeanDefinitionReader
/*     */ {
/*  50 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*     */   private final BeanDefinitionRegistry registry;
/*     */   
/*     */   private ResourceLoader resourceLoader;
/*     */   
/*     */   private ClassLoader beanClassLoader;
/*     */   
/*     */   private Environment environment;
/*     */   
/*  60 */   private BeanNameGenerator beanNameGenerator = new DefaultBeanNameGenerator();
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
/*     */   protected AbstractBeanDefinitionReader(BeanDefinitionRegistry registry) {
/*  81 */     Assert.notNull(registry, "BeanDefinitionRegistry must not be null");
/*  82 */     this.registry = registry;
/*     */ 
/*     */     
/*  85 */     if (this.registry instanceof ResourceLoader) {
/*  86 */       this.resourceLoader = (ResourceLoader)this.registry;
/*     */     } else {
/*     */       
/*  89 */       this.resourceLoader = (ResourceLoader)new PathMatchingResourcePatternResolver();
/*     */     } 
/*     */ 
/*     */     
/*  93 */     if (this.registry instanceof EnvironmentCapable) {
/*  94 */       this.environment = ((EnvironmentCapable)this.registry).getEnvironment();
/*     */     } else {
/*     */       
/*  97 */       this.environment = (Environment)new StandardEnvironment();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final BeanDefinitionRegistry getBeanFactory() {
/* 103 */     return this.registry;
/*     */   }
/*     */ 
/*     */   
/*     */   public final BeanDefinitionRegistry getRegistry() {
/* 108 */     return this.registry;
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
/*     */ 
/*     */ 
/*     */   
/*     */   public void setResourceLoader(ResourceLoader resourceLoader) {
/* 123 */     this.resourceLoader = resourceLoader;
/*     */   }
/*     */ 
/*     */   
/*     */   public ResourceLoader getResourceLoader() {
/* 128 */     return this.resourceLoader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBeanClassLoader(ClassLoader beanClassLoader) {
/* 139 */     this.beanClassLoader = beanClassLoader;
/*     */   }
/*     */ 
/*     */   
/*     */   public ClassLoader getBeanClassLoader() {
/* 144 */     return this.beanClassLoader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEnvironment(Environment environment) {
/* 153 */     this.environment = environment;
/*     */   }
/*     */ 
/*     */   
/*     */   public Environment getEnvironment() {
/* 158 */     return this.environment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBeanNameGenerator(BeanNameGenerator beanNameGenerator) {
/* 167 */     this.beanNameGenerator = (beanNameGenerator != null) ? beanNameGenerator : new DefaultBeanNameGenerator();
/*     */   }
/*     */ 
/*     */   
/*     */   public BeanNameGenerator getBeanNameGenerator() {
/* 172 */     return this.beanNameGenerator;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int loadBeanDefinitions(Resource... resources) throws BeanDefinitionStoreException {
/* 178 */     Assert.notNull(resources, "Resource array must not be null");
/* 179 */     int counter = 0;
/* 180 */     for (Resource resource : resources) {
/* 181 */       counter += loadBeanDefinitions(resource);
/*     */     }
/* 183 */     return counter;
/*     */   }
/*     */ 
/*     */   
/*     */   public int loadBeanDefinitions(String location) throws BeanDefinitionStoreException {
/* 188 */     return loadBeanDefinitions(location, null);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int loadBeanDefinitions(String location, Set<Resource> actualResources) throws BeanDefinitionStoreException {
/* 207 */     ResourceLoader resourceLoader = getResourceLoader();
/* 208 */     if (resourceLoader == null) {
/* 209 */       throw new BeanDefinitionStoreException("Cannot import bean definitions from location [" + location + "]: no ResourceLoader available");
/*     */     }
/*     */ 
/*     */     
/* 213 */     if (resourceLoader instanceof ResourcePatternResolver) {
/*     */       
/*     */       try {
/* 216 */         Resource[] resources = ((ResourcePatternResolver)resourceLoader).getResources(location);
/* 217 */         int i = loadBeanDefinitions(resources);
/* 218 */         if (actualResources != null) {
/* 219 */           for (Resource resource1 : resources) {
/* 220 */             actualResources.add(resource1);
/*     */           }
/*     */         }
/* 223 */         if (this.logger.isDebugEnabled()) {
/* 224 */           this.logger.debug("Loaded " + i + " bean definitions from location pattern [" + location + "]");
/*     */         }
/* 226 */         return i;
/*     */       }
/* 228 */       catch (IOException ex) {
/* 229 */         throw new BeanDefinitionStoreException("Could not resolve bean definition resource pattern [" + location + "]", ex);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 235 */     Resource resource = resourceLoader.getResource(location);
/* 236 */     int loadCount = loadBeanDefinitions(resource);
/* 237 */     if (actualResources != null) {
/* 238 */       actualResources.add(resource);
/*     */     }
/* 240 */     if (this.logger.isDebugEnabled()) {
/* 241 */       this.logger.debug("Loaded " + loadCount + " bean definitions from location [" + location + "]");
/*     */     }
/* 243 */     return loadCount;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int loadBeanDefinitions(String... locations) throws BeanDefinitionStoreException {
/* 249 */     Assert.notNull(locations, "Location array must not be null");
/* 250 */     int counter = 0;
/* 251 */     for (String location : locations) {
/* 252 */       counter += loadBeanDefinitions(location);
/*     */     }
/* 254 */     return counter;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\support\AbstractBeanDefinitionReader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */