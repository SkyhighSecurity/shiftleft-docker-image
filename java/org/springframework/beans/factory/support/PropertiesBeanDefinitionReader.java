/*     */ package org.springframework.beans.factory.support;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.ResourceBundle;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.MutablePropertyValues;
/*     */ import org.springframework.beans.factory.BeanDefinitionStoreException;
/*     */ import org.springframework.beans.factory.CannotLoadBeanClassException;
/*     */ import org.springframework.beans.factory.config.ConstructorArgumentValues;
/*     */ import org.springframework.beans.factory.config.RuntimeBeanReference;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.support.EncodedResource;
/*     */ import org.springframework.util.DefaultPropertiesPersister;
/*     */ import org.springframework.util.PropertiesPersister;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PropertiesBeanDefinitionReader
/*     */   extends AbstractBeanDefinitionReader
/*     */ {
/*     */   public static final String TRUE_VALUE = "true";
/*     */   public static final String SEPARATOR = ".";
/*     */   public static final String CLASS_KEY = "(class)";
/*     */   public static final String PARENT_KEY = "(parent)";
/*     */   public static final String SCOPE_KEY = "(scope)";
/*     */   public static final String SINGLETON_KEY = "(singleton)";
/*     */   public static final String ABSTRACT_KEY = "(abstract)";
/*     */   public static final String LAZY_INIT_KEY = "(lazy-init)";
/*     */   public static final String REF_SUFFIX = "(ref)";
/*     */   public static final String REF_PREFIX = "*";
/*     */   public static final String CONSTRUCTOR_ARG_PREFIX = "$";
/*     */   private String defaultParentBean;
/* 145 */   private PropertiesPersister propertiesPersister = (PropertiesPersister)new DefaultPropertiesPersister();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PropertiesBeanDefinitionReader(BeanDefinitionRegistry registry) {
/* 154 */     super(registry);
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
/*     */   public void setDefaultParentBean(String defaultParentBean) {
/* 171 */     this.defaultParentBean = defaultParentBean;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDefaultParentBean() {
/* 178 */     return this.defaultParentBean;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPropertiesPersister(PropertiesPersister propertiesPersister) {
/* 187 */     this.propertiesPersister = (propertiesPersister != null) ? propertiesPersister : (PropertiesPersister)new DefaultPropertiesPersister();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PropertiesPersister getPropertiesPersister() {
/* 195 */     return this.propertiesPersister;
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
/*     */   public int loadBeanDefinitions(Resource resource) throws BeanDefinitionStoreException {
/* 209 */     return loadBeanDefinitions(new EncodedResource(resource), (String)null);
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
/*     */   public int loadBeanDefinitions(Resource resource, String prefix) throws BeanDefinitionStoreException {
/* 221 */     return loadBeanDefinitions(new EncodedResource(resource), prefix);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int loadBeanDefinitions(EncodedResource encodedResource) throws BeanDefinitionStoreException {
/* 232 */     return loadBeanDefinitions(encodedResource, (String)null);
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
/*     */   public int loadBeanDefinitions(EncodedResource encodedResource, String prefix) throws BeanDefinitionStoreException {
/* 247 */     Properties props = new Properties();
/*     */     try {
/* 249 */       InputStream is = encodedResource.getResource().getInputStream();
/*     */       try {
/* 251 */         if (encodedResource.getEncoding() != null) {
/* 252 */           getPropertiesPersister().load(props, new InputStreamReader(is, encodedResource.getEncoding()));
/*     */         } else {
/*     */           
/* 255 */           getPropertiesPersister().load(props, is);
/*     */         } 
/*     */       } finally {
/*     */         
/* 259 */         is.close();
/*     */       } 
/* 261 */       return registerBeanDefinitions(props, prefix, encodedResource.getResource().getDescription());
/*     */     }
/* 263 */     catch (IOException ex) {
/* 264 */       throw new BeanDefinitionStoreException("Could not parse properties from " + encodedResource.getResource(), ex);
/*     */     } 
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
/*     */   public int registerBeanDefinitions(ResourceBundle rb) throws BeanDefinitionStoreException {
/* 277 */     return registerBeanDefinitions(rb, (String)null);
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
/*     */   public int registerBeanDefinitions(ResourceBundle rb, String prefix) throws BeanDefinitionStoreException {
/* 292 */     Map<String, Object> map = new HashMap<String, Object>();
/* 293 */     Enumeration<String> keys = rb.getKeys();
/* 294 */     while (keys.hasMoreElements()) {
/* 295 */       String key = keys.nextElement();
/* 296 */       map.put(key, rb.getObject(key));
/*     */     } 
/* 298 */     return registerBeanDefinitions(map, prefix);
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
/*     */   public int registerBeanDefinitions(Map<?, ?> map) throws BeansException {
/* 313 */     return registerBeanDefinitions(map, (String)null);
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
/*     */   public int registerBeanDefinitions(Map<?, ?> map, String prefix) throws BeansException {
/* 328 */     return registerBeanDefinitions(map, prefix, "Map " + map);
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
/*     */   
/*     */   public int registerBeanDefinitions(Map<?, ?> map, String prefix, String resourceDescription) throws BeansException {
/* 348 */     if (prefix == null) {
/* 349 */       prefix = "";
/*     */     }
/* 351 */     int beanCount = 0;
/*     */     
/* 353 */     for (Object key : map.keySet()) {
/* 354 */       if (!(key instanceof String)) {
/* 355 */         throw new IllegalArgumentException("Illegal key [" + key + "]: only Strings allowed");
/*     */       }
/* 357 */       String keyString = (String)key;
/* 358 */       if (keyString.startsWith(prefix)) {
/*     */         
/* 360 */         String nameAndProperty = keyString.substring(prefix.length());
/*     */         
/* 362 */         int sepIdx = -1;
/* 363 */         int propKeyIdx = nameAndProperty.indexOf("[");
/* 364 */         if (propKeyIdx != -1) {
/* 365 */           sepIdx = nameAndProperty.lastIndexOf(".", propKeyIdx);
/*     */         } else {
/*     */           
/* 368 */           sepIdx = nameAndProperty.lastIndexOf(".");
/*     */         } 
/* 370 */         if (sepIdx != -1) {
/* 371 */           String beanName = nameAndProperty.substring(0, sepIdx);
/* 372 */           if (this.logger.isDebugEnabled()) {
/* 373 */             this.logger.debug("Found bean name '" + beanName + "'");
/*     */           }
/* 375 */           if (!getRegistry().containsBeanDefinition(beanName)) {
/*     */             
/* 377 */             registerBeanDefinition(beanName, map, prefix + beanName, resourceDescription);
/* 378 */             beanCount++;
/*     */           } 
/*     */           
/*     */           continue;
/*     */         } 
/*     */         
/* 384 */         if (this.logger.isDebugEnabled()) {
/* 385 */           this.logger.debug("Invalid bean name and property [" + nameAndProperty + "]");
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 391 */     return beanCount;
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
/*     */   protected void registerBeanDefinition(String beanName, Map<?, ?> map, String prefix, String resourceDescription) throws BeansException {
/* 407 */     String className = null;
/* 408 */     String parent = null;
/* 409 */     String scope = "singleton";
/* 410 */     boolean isAbstract = false;
/* 411 */     boolean lazyInit = false;
/*     */     
/* 413 */     ConstructorArgumentValues cas = new ConstructorArgumentValues();
/* 414 */     MutablePropertyValues pvs = new MutablePropertyValues();
/*     */     
/* 416 */     for (Map.Entry<?, ?> entry : map.entrySet()) {
/* 417 */       String key = StringUtils.trimWhitespace((String)entry.getKey());
/* 418 */       if (key.startsWith(prefix + ".")) {
/* 419 */         String property = key.substring(prefix.length() + ".".length());
/* 420 */         if ("(class)".equals(property)) {
/* 421 */           className = StringUtils.trimWhitespace((String)entry.getValue()); continue;
/*     */         } 
/* 423 */         if ("(parent)".equals(property)) {
/* 424 */           parent = StringUtils.trimWhitespace((String)entry.getValue()); continue;
/*     */         } 
/* 426 */         if ("(abstract)".equals(property)) {
/* 427 */           String val = StringUtils.trimWhitespace((String)entry.getValue());
/* 428 */           isAbstract = "true".equals(val); continue;
/*     */         } 
/* 430 */         if ("(scope)".equals(property)) {
/*     */           
/* 432 */           scope = StringUtils.trimWhitespace((String)entry.getValue()); continue;
/*     */         } 
/* 434 */         if ("(singleton)".equals(property)) {
/*     */           
/* 436 */           String val = StringUtils.trimWhitespace((String)entry.getValue());
/* 437 */           scope = (val == null || "true".equals(val)) ? "singleton" : "prototype";
/*     */           continue;
/*     */         } 
/* 440 */         if ("(lazy-init)".equals(property)) {
/* 441 */           String val = StringUtils.trimWhitespace((String)entry.getValue());
/* 442 */           lazyInit = "true".equals(val); continue;
/*     */         } 
/* 444 */         if (property.startsWith("$")) {
/* 445 */           if (property.endsWith("(ref)")) {
/* 446 */             int i = Integer.parseInt(property.substring(1, property.length() - "(ref)".length()));
/* 447 */             cas.addIndexedArgumentValue(i, new RuntimeBeanReference(entry.getValue().toString()));
/*     */             continue;
/*     */           } 
/* 450 */           int index = Integer.parseInt(property.substring(1));
/* 451 */           cas.addIndexedArgumentValue(index, readValue(entry));
/*     */           continue;
/*     */         } 
/* 454 */         if (property.endsWith("(ref)")) {
/*     */ 
/*     */           
/* 457 */           property = property.substring(0, property.length() - "(ref)".length());
/* 458 */           String ref = StringUtils.trimWhitespace((String)entry.getValue());
/*     */ 
/*     */ 
/*     */           
/* 462 */           Object val = new RuntimeBeanReference(ref);
/* 463 */           pvs.add(property, val);
/*     */           
/*     */           continue;
/*     */         } 
/* 467 */         pvs.add(property, readValue(entry));
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 472 */     if (this.logger.isDebugEnabled()) {
/* 473 */       this.logger.debug("Registering bean definition for bean name '" + beanName + "' with " + pvs);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 479 */     if (parent == null && className == null && !beanName.equals(this.defaultParentBean)) {
/* 480 */       parent = this.defaultParentBean;
/*     */     }
/*     */     
/*     */     try {
/* 484 */       AbstractBeanDefinition bd = BeanDefinitionReaderUtils.createBeanDefinition(parent, className, 
/* 485 */           getBeanClassLoader());
/* 486 */       bd.setScope(scope);
/* 487 */       bd.setAbstract(isAbstract);
/* 488 */       bd.setLazyInit(lazyInit);
/* 489 */       bd.setConstructorArgumentValues(cas);
/* 490 */       bd.setPropertyValues(pvs);
/* 491 */       getRegistry().registerBeanDefinition(beanName, bd);
/*     */     }
/* 493 */     catch (ClassNotFoundException ex) {
/* 494 */       throw new CannotLoadBeanClassException(resourceDescription, beanName, className, ex);
/*     */     }
/* 496 */     catch (LinkageError err) {
/* 497 */       throw new CannotLoadBeanClassException(resourceDescription, beanName, className, err);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Object readValue(Map.Entry<?, ?> entry) {
/* 506 */     Object val = entry.getValue();
/* 507 */     if (val instanceof String) {
/* 508 */       String strVal = (String)val;
/*     */       
/* 510 */       if (strVal.startsWith("*")) {
/*     */         
/* 512 */         String targetName = strVal.substring(1);
/* 513 */         if (targetName.startsWith("*")) {
/*     */           
/* 515 */           val = targetName;
/*     */         } else {
/*     */           
/* 518 */           val = new RuntimeBeanReference(targetName);
/*     */         } 
/*     */       } 
/*     */     } 
/* 522 */     return val;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\support\PropertiesBeanDefinitionReader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */