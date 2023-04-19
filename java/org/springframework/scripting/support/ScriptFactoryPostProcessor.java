/*     */ package org.springframework.scripting.support;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import org.aopalliance.aop.Advice;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.aop.TargetSource;
/*     */ import org.springframework.aop.framework.ProxyFactory;
/*     */ import org.springframework.aop.support.DelegatingIntroductionInterceptor;
/*     */ import org.springframework.asm.Type;
/*     */ import org.springframework.beans.BeanUtils;
/*     */ import org.springframework.beans.PropertyValue;
/*     */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*     */ import org.springframework.beans.factory.BeanCreationException;
/*     */ import org.springframework.beans.factory.BeanDefinitionStoreException;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryAware;
/*     */ import org.springframework.beans.factory.DisposableBean;
/*     */ import org.springframework.beans.factory.FactoryBean;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.BeanPostProcessor;
/*     */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
/*     */ import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
/*     */ import org.springframework.beans.factory.support.AbstractBeanDefinition;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionValidationException;
/*     */ import org.springframework.beans.factory.support.DefaultListableBeanFactory;
/*     */ import org.springframework.beans.factory.support.GenericBeanDefinition;
/*     */ import org.springframework.cglib.core.Signature;
/*     */ import org.springframework.cglib.proxy.InterfaceMaker;
/*     */ import org.springframework.context.ResourceLoaderAware;
/*     */ import org.springframework.core.Conventions;
/*     */ import org.springframework.core.Ordered;
/*     */ import org.springframework.core.io.DefaultResourceLoader;
/*     */ import org.springframework.core.io.ResourceLoader;
/*     */ import org.springframework.scripting.ScriptFactory;
/*     */ import org.springframework.scripting.ScriptSource;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ObjectUtils;
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
/*     */ public class ScriptFactoryPostProcessor
/*     */   extends InstantiationAwareBeanPostProcessorAdapter
/*     */   implements BeanClassLoaderAware, BeanFactoryAware, ResourceLoaderAware, DisposableBean, Ordered
/*     */ {
/*     */   public static final String INLINE_SCRIPT_PREFIX = "inline:";
/* 150 */   public static final String REFRESH_CHECK_DELAY_ATTRIBUTE = Conventions.getQualifiedAttributeName(ScriptFactoryPostProcessor.class, "refreshCheckDelay");
/*     */ 
/*     */   
/* 153 */   public static final String PROXY_TARGET_CLASS_ATTRIBUTE = Conventions.getQualifiedAttributeName(ScriptFactoryPostProcessor.class, "proxyTargetClass");
/*     */ 
/*     */   
/* 156 */   public static final String LANGUAGE_ATTRIBUTE = Conventions.getQualifiedAttributeName(ScriptFactoryPostProcessor.class, "language");
/*     */ 
/*     */   
/*     */   private static final String SCRIPT_FACTORY_NAME_PREFIX = "scriptFactory.";
/*     */ 
/*     */   
/*     */   private static final String SCRIPTED_OBJECT_NAME_PREFIX = "scriptedObject.";
/*     */ 
/*     */   
/* 165 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/* 167 */   private long defaultRefreshCheckDelay = -1L;
/*     */   
/*     */   private boolean defaultProxyTargetClass = false;
/*     */   
/* 171 */   private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
/*     */   
/*     */   private ConfigurableBeanFactory beanFactory;
/*     */   
/* 175 */   private ResourceLoader resourceLoader = (ResourceLoader)new DefaultResourceLoader();
/*     */   
/* 177 */   final DefaultListableBeanFactory scriptBeanFactory = new DefaultListableBeanFactory();
/*     */ 
/*     */   
/* 180 */   private final Map<String, ScriptSource> scriptSourceCache = new HashMap<String, ScriptSource>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDefaultRefreshCheckDelay(long defaultRefreshCheckDelay) {
/* 191 */     this.defaultRefreshCheckDelay = defaultRefreshCheckDelay;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDefaultProxyTargetClass(boolean defaultProxyTargetClass) {
/* 199 */     this.defaultProxyTargetClass = defaultProxyTargetClass;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBeanClassLoader(ClassLoader classLoader) {
/* 204 */     this.beanClassLoader = classLoader;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBeanFactory(BeanFactory beanFactory) {
/* 209 */     if (!(beanFactory instanceof ConfigurableBeanFactory)) {
/* 210 */       throw new IllegalStateException("ScriptFactoryPostProcessor doesn't work with non-ConfigurableBeanFactory: " + beanFactory
/* 211 */           .getClass());
/*     */     }
/* 213 */     this.beanFactory = (ConfigurableBeanFactory)beanFactory;
/*     */ 
/*     */     
/* 216 */     this.scriptBeanFactory.setParentBeanFactory((BeanFactory)this.beanFactory);
/*     */ 
/*     */     
/* 219 */     this.scriptBeanFactory.copyConfigurationFrom(this.beanFactory);
/*     */ 
/*     */ 
/*     */     
/* 223 */     for (Iterator<BeanPostProcessor> it = this.scriptBeanFactory.getBeanPostProcessors().iterator(); it.hasNext();) {
/* 224 */       if (it.next() instanceof org.springframework.aop.framework.AopInfrastructureBean) {
/* 225 */         it.remove();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setResourceLoader(ResourceLoader resourceLoader) {
/* 232 */     this.resourceLoader = resourceLoader;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getOrder() {
/* 237 */     return Integer.MIN_VALUE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?> predictBeanType(Class<?> beanClass, String beanName) {
/* 244 */     if (!ScriptFactory.class.isAssignableFrom(beanClass)) {
/* 245 */       return null;
/*     */     }
/*     */     
/* 248 */     BeanDefinition bd = this.beanFactory.getMergedBeanDefinition(beanName);
/*     */     
/*     */     try {
/* 251 */       String scriptFactoryBeanName = "scriptFactory." + beanName;
/* 252 */       String scriptedObjectBeanName = "scriptedObject." + beanName;
/* 253 */       prepareScriptBeans(bd, scriptFactoryBeanName, scriptedObjectBeanName);
/*     */       
/* 255 */       ScriptFactory scriptFactory = (ScriptFactory)this.scriptBeanFactory.getBean(scriptFactoryBeanName, ScriptFactory.class);
/* 256 */       ScriptSource scriptSource = getScriptSource(scriptFactoryBeanName, scriptFactory.getScriptSourceLocator());
/* 257 */       Class<?>[] interfaces = scriptFactory.getScriptInterfaces();
/*     */       
/* 259 */       Class<?> scriptedType = scriptFactory.getScriptedObjectType(scriptSource);
/* 260 */       if (scriptedType != null) {
/* 261 */         return scriptedType;
/*     */       }
/* 263 */       if (!ObjectUtils.isEmpty((Object[])interfaces)) {
/* 264 */         return (interfaces.length == 1) ? interfaces[0] : createCompositeInterface(interfaces);
/*     */       }
/*     */       
/* 267 */       if (bd.isSingleton()) {
/* 268 */         Object bean = this.scriptBeanFactory.getBean(scriptedObjectBeanName);
/* 269 */         if (bean != null) {
/* 270 */           return bean.getClass();
/*     */         }
/*     */       }
/*     */     
/*     */     }
/* 275 */     catch (Exception ex) {
/* 276 */       if (ex instanceof BeanCreationException && ((BeanCreationException)ex)
/* 277 */         .getMostSpecificCause() instanceof org.springframework.beans.factory.BeanCurrentlyInCreationException) {
/* 278 */         if (this.logger.isTraceEnabled()) {
/* 279 */           this.logger.trace("Could not determine scripted object type for bean '" + beanName + "': " + ex
/* 280 */               .getMessage());
/*     */         
/*     */         }
/*     */       }
/* 284 */       else if (this.logger.isDebugEnabled()) {
/* 285 */         this.logger.debug("Could not determine scripted object type for bean '" + beanName + "'", ex);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 290 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) {
/* 296 */     if (!ScriptFactory.class.isAssignableFrom(beanClass)) {
/* 297 */       return null;
/*     */     }
/*     */     
/* 300 */     BeanDefinition bd = this.beanFactory.getMergedBeanDefinition(beanName);
/* 301 */     String scriptFactoryBeanName = "scriptFactory." + beanName;
/* 302 */     String scriptedObjectBeanName = "scriptedObject." + beanName;
/* 303 */     prepareScriptBeans(bd, scriptFactoryBeanName, scriptedObjectBeanName);
/*     */     
/* 305 */     ScriptFactory scriptFactory = (ScriptFactory)this.scriptBeanFactory.getBean(scriptFactoryBeanName, ScriptFactory.class);
/* 306 */     ScriptSource scriptSource = getScriptSource(scriptFactoryBeanName, scriptFactory.getScriptSourceLocator());
/* 307 */     boolean isFactoryBean = false;
/*     */     try {
/* 309 */       Class<?> scriptedObjectType = scriptFactory.getScriptedObjectType(scriptSource);
/*     */       
/* 311 */       if (scriptedObjectType != null) {
/* 312 */         isFactoryBean = FactoryBean.class.isAssignableFrom(scriptedObjectType);
/*     */       }
/*     */     }
/* 315 */     catch (Exception ex) {
/* 316 */       throw new BeanCreationException(beanName, "Could not determine scripted object type for " + scriptFactory, ex);
/*     */     } 
/*     */ 
/*     */     
/* 320 */     long refreshCheckDelay = resolveRefreshCheckDelay(bd);
/* 321 */     if (refreshCheckDelay >= 0L) {
/* 322 */       Class<?>[] interfaces = scriptFactory.getScriptInterfaces();
/* 323 */       RefreshableScriptTargetSource ts = new RefreshableScriptTargetSource((BeanFactory)this.scriptBeanFactory, scriptedObjectBeanName, scriptFactory, scriptSource, isFactoryBean);
/*     */       
/* 325 */       boolean proxyTargetClass = resolveProxyTargetClass(bd);
/* 326 */       String language = (String)bd.getAttribute(LANGUAGE_ATTRIBUTE);
/* 327 */       if (proxyTargetClass && (language == null || !language.equals("groovy"))) {
/* 328 */         throw new BeanDefinitionValidationException("Cannot use proxyTargetClass=true with script beans where language is not 'groovy': '" + language + "'");
/*     */       }
/*     */ 
/*     */       
/* 332 */       ts.setRefreshCheckDelay(refreshCheckDelay);
/* 333 */       return createRefreshableProxy((TargetSource)ts, interfaces, proxyTargetClass);
/*     */     } 
/*     */     
/* 336 */     if (isFactoryBean) {
/* 337 */       scriptedObjectBeanName = "&" + scriptedObjectBeanName;
/*     */     }
/* 339 */     return this.scriptBeanFactory.getBean(scriptedObjectBeanName);
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
/*     */   protected void prepareScriptBeans(BeanDefinition bd, String scriptFactoryBeanName, String scriptedObjectBeanName) {
/* 352 */     synchronized (this.scriptBeanFactory) {
/* 353 */       if (!this.scriptBeanFactory.containsBeanDefinition(scriptedObjectBeanName)) {
/*     */         
/* 355 */         this.scriptBeanFactory.registerBeanDefinition(scriptFactoryBeanName, 
/* 356 */             createScriptFactoryBeanDefinition(bd));
/*     */         
/* 358 */         ScriptFactory scriptFactory = (ScriptFactory)this.scriptBeanFactory.getBean(scriptFactoryBeanName, ScriptFactory.class);
/*     */         
/* 360 */         ScriptSource scriptSource = getScriptSource(scriptFactoryBeanName, scriptFactory.getScriptSourceLocator());
/* 361 */         Class<?>[] interfaces = scriptFactory.getScriptInterfaces();
/*     */         
/* 363 */         Class<?>[] scriptedInterfaces = interfaces;
/* 364 */         if (scriptFactory.requiresConfigInterface() && !bd.getPropertyValues().isEmpty()) {
/* 365 */           Class<?> configInterface = createConfigInterface(bd, interfaces);
/* 366 */           scriptedInterfaces = (Class[])ObjectUtils.addObjectToArray((Object[])interfaces, configInterface);
/*     */         } 
/*     */         
/* 369 */         BeanDefinition objectBd = createScriptedObjectBeanDefinition(bd, scriptFactoryBeanName, scriptSource, scriptedInterfaces);
/*     */         
/* 371 */         long refreshCheckDelay = resolveRefreshCheckDelay(bd);
/* 372 */         if (refreshCheckDelay >= 0L) {
/* 373 */           objectBd.setScope("prototype");
/*     */         }
/*     */         
/* 376 */         this.scriptBeanFactory.registerBeanDefinition(scriptedObjectBeanName, objectBd);
/*     */       } 
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
/*     */ 
/*     */   
/*     */   protected long resolveRefreshCheckDelay(BeanDefinition beanDefinition) {
/* 392 */     long refreshCheckDelay = this.defaultRefreshCheckDelay;
/* 393 */     Object attributeValue = beanDefinition.getAttribute(REFRESH_CHECK_DELAY_ATTRIBUTE);
/* 394 */     if (attributeValue instanceof Number) {
/* 395 */       refreshCheckDelay = ((Number)attributeValue).longValue();
/*     */     }
/* 397 */     else if (attributeValue instanceof String) {
/* 398 */       refreshCheckDelay = Long.parseLong((String)attributeValue);
/*     */     }
/* 400 */     else if (attributeValue != null) {
/* 401 */       throw new BeanDefinitionStoreException("Invalid refresh check delay attribute [" + REFRESH_CHECK_DELAY_ATTRIBUTE + "] with value '" + attributeValue + "': needs to be of type Number or String");
/*     */     } 
/*     */ 
/*     */     
/* 405 */     return refreshCheckDelay;
/*     */   }
/*     */   
/*     */   protected boolean resolveProxyTargetClass(BeanDefinition beanDefinition) {
/* 409 */     boolean proxyTargetClass = this.defaultProxyTargetClass;
/* 410 */     Object attributeValue = beanDefinition.getAttribute(PROXY_TARGET_CLASS_ATTRIBUTE);
/* 411 */     if (attributeValue instanceof Boolean) {
/* 412 */       proxyTargetClass = ((Boolean)attributeValue).booleanValue();
/*     */     }
/* 414 */     else if (attributeValue instanceof String) {
/* 415 */       proxyTargetClass = Boolean.valueOf((String)attributeValue).booleanValue();
/*     */     }
/* 417 */     else if (attributeValue != null) {
/* 418 */       throw new BeanDefinitionStoreException("Invalid proxy target class attribute [" + PROXY_TARGET_CLASS_ATTRIBUTE + "] with value '" + attributeValue + "': needs to be of type Boolean or String");
/*     */     } 
/*     */ 
/*     */     
/* 422 */     return proxyTargetClass;
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
/*     */   protected BeanDefinition createScriptFactoryBeanDefinition(BeanDefinition bd) {
/* 434 */     GenericBeanDefinition scriptBd = new GenericBeanDefinition();
/* 435 */     scriptBd.setBeanClassName(bd.getBeanClassName());
/* 436 */     scriptBd.getConstructorArgumentValues().addArgumentValues(bd.getConstructorArgumentValues());
/* 437 */     return (BeanDefinition)scriptBd;
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
/*     */   protected ScriptSource getScriptSource(String beanName, String scriptSourceLocator) {
/* 449 */     synchronized (this.scriptSourceCache) {
/* 450 */       ScriptSource scriptSource = this.scriptSourceCache.get(beanName);
/* 451 */       if (scriptSource == null) {
/* 452 */         scriptSource = convertToScriptSource(beanName, scriptSourceLocator, this.resourceLoader);
/* 453 */         this.scriptSourceCache.put(beanName, scriptSource);
/*     */       } 
/* 455 */       return scriptSource;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ScriptSource convertToScriptSource(String beanName, String scriptSourceLocator, ResourceLoader resourceLoader) {
/* 472 */     if (scriptSourceLocator.startsWith("inline:")) {
/* 473 */       return new StaticScriptSource(scriptSourceLocator.substring("inline:".length()), beanName);
/*     */     }
/*     */     
/* 476 */     return new ResourceScriptSource(resourceLoader.getResource(scriptSourceLocator));
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
/*     */   protected Class<?> createConfigInterface(BeanDefinition bd, Class<?>[] interfaces) {
/* 495 */     InterfaceMaker maker = new InterfaceMaker();
/* 496 */     PropertyValue[] pvs = bd.getPropertyValues().getPropertyValues();
/* 497 */     for (PropertyValue pv : pvs) {
/* 498 */       String propertyName = pv.getName();
/* 499 */       Class<?> propertyType = BeanUtils.findPropertyType(propertyName, interfaces);
/* 500 */       String setterName = "set" + StringUtils.capitalize(propertyName);
/* 501 */       Signature signature = new Signature(setterName, Type.VOID_TYPE, new Type[] { Type.getType(propertyType) });
/* 502 */       maker.add(signature, new Type[0]);
/*     */     } 
/* 504 */     if (bd instanceof AbstractBeanDefinition) {
/* 505 */       AbstractBeanDefinition abd = (AbstractBeanDefinition)bd;
/* 506 */       if (abd.getInitMethodName() != null) {
/* 507 */         Signature signature = new Signature(abd.getInitMethodName(), Type.VOID_TYPE, new Type[0]);
/* 508 */         maker.add(signature, new Type[0]);
/*     */       } 
/* 510 */       if (StringUtils.hasText(abd.getDestroyMethodName())) {
/* 511 */         Signature signature = new Signature(abd.getDestroyMethodName(), Type.VOID_TYPE, new Type[0]);
/* 512 */         maker.add(signature, new Type[0]);
/*     */       } 
/*     */     } 
/* 515 */     return maker.create();
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
/*     */   protected Class<?> createCompositeInterface(Class<?>[] interfaces) {
/* 528 */     return ClassUtils.createCompositeInterface(interfaces, this.beanClassLoader);
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
/*     */   protected BeanDefinition createScriptedObjectBeanDefinition(BeanDefinition bd, String scriptFactoryBeanName, ScriptSource scriptSource, Class<?>[] interfaces) {
/* 545 */     GenericBeanDefinition objectBd = new GenericBeanDefinition(bd);
/* 546 */     objectBd.setFactoryBeanName(scriptFactoryBeanName);
/* 547 */     objectBd.setFactoryMethodName("getScriptedObject");
/* 548 */     objectBd.getConstructorArgumentValues().clear();
/* 549 */     objectBd.getConstructorArgumentValues().addIndexedArgumentValue(0, scriptSource);
/* 550 */     objectBd.getConstructorArgumentValues().addIndexedArgumentValue(1, interfaces);
/* 551 */     return (BeanDefinition)objectBd;
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
/*     */   protected Object createRefreshableProxy(TargetSource ts, Class<?>[] interfaces, boolean proxyTargetClass) {
/* 563 */     ProxyFactory proxyFactory = new ProxyFactory();
/* 564 */     proxyFactory.setTargetSource(ts);
/* 565 */     ClassLoader classLoader = this.beanClassLoader;
/*     */     
/* 567 */     if (interfaces == null) {
/* 568 */       interfaces = ClassUtils.getAllInterfacesForClass(ts.getTargetClass(), this.beanClassLoader);
/*     */     }
/* 570 */     proxyFactory.setInterfaces(interfaces);
/* 571 */     if (proxyTargetClass) {
/* 572 */       classLoader = null;
/* 573 */       proxyFactory.setProxyTargetClass(true);
/*     */     } 
/*     */     
/* 576 */     DelegatingIntroductionInterceptor introduction = new DelegatingIntroductionInterceptor(ts);
/* 577 */     introduction.suppressInterface(TargetSource.class);
/* 578 */     proxyFactory.addAdvice((Advice)introduction);
/*     */     
/* 580 */     return proxyFactory.getProxy(classLoader);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void destroy() {
/* 588 */     this.scriptBeanFactory.destroySingletons();
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\scripting\support\ScriptFactoryPostProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */