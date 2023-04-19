/*     */ package org.springframework.web.context;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import javax.servlet.ServletContext;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.BeanUtils;
/*     */ import org.springframework.beans.factory.access.BeanFactoryLocator;
/*     */ import org.springframework.beans.factory.access.BeanFactoryReference;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.context.ApplicationContextException;
/*     */ import org.springframework.context.ApplicationContextInitializer;
/*     */ import org.springframework.context.ConfigurableApplicationContext;
/*     */ import org.springframework.context.access.ContextSingletonBeanFactoryLocator;
/*     */ import org.springframework.core.GenericTypeResolver;
/*     */ import org.springframework.core.annotation.AnnotationAwareOrderComparator;
/*     */ import org.springframework.core.env.ConfigurableEnvironment;
/*     */ import org.springframework.core.io.ClassPathResource;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.support.PropertiesLoaderUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ContextLoader
/*     */ {
/*     */   public static final String CONTEXT_ID_PARAM = "contextId";
/*     */   public static final String CONFIG_LOCATION_PARAM = "contextConfigLocation";
/*     */   public static final String CONTEXT_CLASS_PARAM = "contextClass";
/*     */   public static final String CONTEXT_INITIALIZER_CLASSES_PARAM = "contextInitializerClasses";
/*     */   public static final String GLOBAL_INITIALIZER_CLASSES_PARAM = "globalInitializerClasses";
/*     */   public static final String LOCATOR_FACTORY_SELECTOR_PARAM = "locatorFactorySelector";
/*     */   public static final String LOCATOR_FACTORY_KEY_PARAM = "parentContextKey";
/*     */   private static final String INIT_PARAM_DELIMITERS = ",; \t\n";
/*     */   private static final String DEFAULT_STRATEGIES_PATH = "ContextLoader.properties";
/*     */   private static final Properties defaultStrategies;
/*     */   
/*     */   static {
/*     */     try {
/* 172 */       ClassPathResource resource = new ClassPathResource("ContextLoader.properties", ContextLoader.class);
/* 173 */       defaultStrategies = PropertiesLoaderUtils.loadProperties((Resource)resource);
/*     */     }
/* 175 */     catch (IOException ex) {
/* 176 */       throw new IllegalStateException("Could not load 'ContextLoader.properties': " + ex.getMessage());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 184 */   private static final Map<ClassLoader, WebApplicationContext> currentContextPerThread = new ConcurrentHashMap<ClassLoader, WebApplicationContext>(1);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static volatile WebApplicationContext currentContext;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private WebApplicationContext context;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private BeanFactoryReference parentContextRef;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 206 */   private final List<ApplicationContextInitializer<ConfigurableApplicationContext>> contextInitializers = new ArrayList<ApplicationContextInitializer<ConfigurableApplicationContext>>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ContextLoader(WebApplicationContext context) {
/* 262 */     this.context = context;
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
/*     */   public void setContextInitializers(ApplicationContextInitializer<?>... initializers) {
/* 275 */     if (initializers != null) {
/* 276 */       for (ApplicationContextInitializer<?> initializer : initializers) {
/* 277 */         this.contextInitializers.add(initializer);
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
/*     */ 
/*     */   
/*     */   public WebApplicationContext initWebApplicationContext(ServletContext servletContext) {
/* 295 */     if (servletContext.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE) != null) {
/* 296 */       throw new IllegalStateException("Cannot initialize context because there is already a root application context present - check whether you have multiple ContextLoader* definitions in your web.xml!");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 301 */     Log logger = LogFactory.getLog(ContextLoader.class);
/* 302 */     servletContext.log("Initializing Spring root WebApplicationContext");
/* 303 */     if (logger.isInfoEnabled()) {
/* 304 */       logger.info("Root WebApplicationContext: initialization started");
/*     */     }
/* 306 */     long startTime = System.currentTimeMillis();
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 311 */       if (this.context == null) {
/* 312 */         this.context = createWebApplicationContext(servletContext);
/*     */       }
/* 314 */       if (this.context instanceof ConfigurableWebApplicationContext) {
/* 315 */         ConfigurableWebApplicationContext cwac = (ConfigurableWebApplicationContext)this.context;
/* 316 */         if (!cwac.isActive()) {
/*     */ 
/*     */           
/* 319 */           if (cwac.getParent() == null) {
/*     */ 
/*     */             
/* 322 */             ApplicationContext parent = loadParentContext(servletContext);
/* 323 */             cwac.setParent(parent);
/*     */           } 
/* 325 */           configureAndRefreshWebApplicationContext(cwac, servletContext);
/*     */         } 
/*     */       } 
/* 328 */       servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, this.context);
/*     */       
/* 330 */       ClassLoader ccl = Thread.currentThread().getContextClassLoader();
/* 331 */       if (ccl == ContextLoader.class.getClassLoader()) {
/* 332 */         currentContext = this.context;
/*     */       }
/* 334 */       else if (ccl != null) {
/* 335 */         currentContextPerThread.put(ccl, this.context);
/*     */       } 
/*     */       
/* 338 */       if (logger.isDebugEnabled()) {
/* 339 */         logger.debug("Published root WebApplicationContext as ServletContext attribute with name [" + WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE + "]");
/*     */       }
/*     */       
/* 342 */       if (logger.isInfoEnabled()) {
/* 343 */         long elapsedTime = System.currentTimeMillis() - startTime;
/* 344 */         logger.info("Root WebApplicationContext: initialization completed in " + elapsedTime + " ms");
/*     */       } 
/*     */       
/* 347 */       return this.context;
/*     */     }
/* 349 */     catch (RuntimeException ex) {
/* 350 */       logger.error("Context initialization failed", ex);
/* 351 */       servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, ex);
/* 352 */       throw ex;
/*     */     }
/* 354 */     catch (Error err) {
/* 355 */       logger.error("Context initialization failed", err);
/* 356 */       servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, err);
/* 357 */       throw err;
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
/*     */   protected WebApplicationContext createWebApplicationContext(ServletContext sc) {
/* 374 */     Class<?> contextClass = determineContextClass(sc);
/* 375 */     if (!ConfigurableWebApplicationContext.class.isAssignableFrom(contextClass)) {
/* 376 */       throw new ApplicationContextException("Custom context class [" + contextClass.getName() + "] is not of type [" + ConfigurableWebApplicationContext.class
/* 377 */           .getName() + "]");
/*     */     }
/* 379 */     return (ConfigurableWebApplicationContext)BeanUtils.instantiateClass(contextClass);
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
/*     */   protected Class<?> determineContextClass(ServletContext servletContext) {
/* 391 */     String contextClassName = servletContext.getInitParameter("contextClass");
/* 392 */     if (contextClassName != null) {
/*     */       try {
/* 394 */         return ClassUtils.forName(contextClassName, ClassUtils.getDefaultClassLoader());
/*     */       }
/* 396 */       catch (ClassNotFoundException ex) {
/* 397 */         throw new ApplicationContextException("Failed to load custom context class [" + contextClassName + "]", ex);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 402 */     contextClassName = defaultStrategies.getProperty(WebApplicationContext.class.getName());
/*     */     try {
/* 404 */       return ClassUtils.forName(contextClassName, ContextLoader.class.getClassLoader());
/*     */     }
/* 406 */     catch (ClassNotFoundException ex) {
/* 407 */       throw new ApplicationContextException("Failed to load default context class [" + contextClassName + "]", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void configureAndRefreshWebApplicationContext(ConfigurableWebApplicationContext wac, ServletContext sc) {
/* 414 */     if (ObjectUtils.identityToString(wac).equals(wac.getId())) {
/*     */ 
/*     */       
/* 417 */       String idParam = sc.getInitParameter("contextId");
/* 418 */       if (idParam != null) {
/* 419 */         wac.setId(idParam);
/*     */       }
/*     */       else {
/*     */         
/* 423 */         wac.setId(ConfigurableWebApplicationContext.APPLICATION_CONTEXT_ID_PREFIX + 
/* 424 */             ObjectUtils.getDisplayString(sc.getContextPath()));
/*     */       } 
/*     */     } 
/*     */     
/* 428 */     wac.setServletContext(sc);
/* 429 */     String configLocationParam = sc.getInitParameter("contextConfigLocation");
/* 430 */     if (configLocationParam != null) {
/* 431 */       wac.setConfigLocation(configLocationParam);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 437 */     ConfigurableEnvironment env = wac.getEnvironment();
/* 438 */     if (env instanceof ConfigurableWebEnvironment) {
/* 439 */       ((ConfigurableWebEnvironment)env).initPropertySources(sc, null);
/*     */     }
/*     */     
/* 442 */     customizeContext(sc, wac);
/* 443 */     wac.refresh();
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
/*     */ 
/*     */   
/*     */   protected void customizeContext(ServletContext sc, ConfigurableWebApplicationContext wac) {
/* 465 */     List<Class<ApplicationContextInitializer<ConfigurableApplicationContext>>> initializerClasses = determineContextInitializerClasses(sc);
/*     */     
/* 467 */     for (Class<ApplicationContextInitializer<ConfigurableApplicationContext>> initializerClass : initializerClasses) {
/*     */       
/* 469 */       Class<?> initializerContextClass = GenericTypeResolver.resolveTypeArgument(initializerClass, ApplicationContextInitializer.class);
/* 470 */       if (initializerContextClass != null && !initializerContextClass.isInstance(wac)) {
/* 471 */         throw new ApplicationContextException(String.format("Could not apply context initializer [%s] since its generic parameter [%s] is not assignable from the type of application context used by this context loader: [%s]", new Object[] { initializerClass
/*     */ 
/*     */                 
/* 474 */                 .getName(), initializerContextClass.getName(), wac
/* 475 */                 .getClass().getName() }));
/*     */       }
/* 477 */       this.contextInitializers.add(BeanUtils.instantiateClass(initializerClass));
/*     */     } 
/*     */     
/* 480 */     AnnotationAwareOrderComparator.sort(this.contextInitializers);
/* 481 */     for (ApplicationContextInitializer<ConfigurableApplicationContext> initializer : this.contextInitializers) {
/* 482 */       initializer.initialize(wac);
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
/*     */   protected List<Class<ApplicationContextInitializer<ConfigurableApplicationContext>>> determineContextInitializerClasses(ServletContext servletContext) {
/* 495 */     List<Class<ApplicationContextInitializer<ConfigurableApplicationContext>>> classes = new ArrayList<Class<ApplicationContextInitializer<ConfigurableApplicationContext>>>();
/*     */ 
/*     */     
/* 498 */     String globalClassNames = servletContext.getInitParameter("globalInitializerClasses");
/* 499 */     if (globalClassNames != null) {
/* 500 */       for (String className : StringUtils.tokenizeToStringArray(globalClassNames, ",; \t\n")) {
/* 501 */         classes.add(loadInitializerClass(className));
/*     */       }
/*     */     }
/*     */     
/* 505 */     String localClassNames = servletContext.getInitParameter("contextInitializerClasses");
/* 506 */     if (localClassNames != null) {
/* 507 */       for (String className : StringUtils.tokenizeToStringArray(localClassNames, ",; \t\n")) {
/* 508 */         classes.add(loadInitializerClass(className));
/*     */       }
/*     */     }
/*     */     
/* 512 */     return classes;
/*     */   }
/*     */ 
/*     */   
/*     */   private Class<ApplicationContextInitializer<ConfigurableApplicationContext>> loadInitializerClass(String className) {
/*     */     try {
/* 518 */       Class<?> clazz = ClassUtils.forName(className, ClassUtils.getDefaultClassLoader());
/* 519 */       if (!ApplicationContextInitializer.class.isAssignableFrom(clazz)) {
/* 520 */         throw new ApplicationContextException("Initializer class does not implement ApplicationContextInitializer interface: " + clazz);
/*     */       }
/*     */       
/* 523 */       return (Class)clazz;
/*     */     }
/* 525 */     catch (ClassNotFoundException ex) {
/* 526 */       throw new ApplicationContextException("Failed to load context initializer class [" + className + "]", ex);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ApplicationContext loadParentContext(ServletContext servletContext) {
/* 551 */     ApplicationContext parentContext = null;
/* 552 */     String locatorFactorySelector = servletContext.getInitParameter("locatorFactorySelector");
/* 553 */     String parentContextKey = servletContext.getInitParameter("parentContextKey");
/*     */     
/* 555 */     if (parentContextKey != null) {
/*     */       
/* 557 */       BeanFactoryLocator locator = ContextSingletonBeanFactoryLocator.getInstance(locatorFactorySelector);
/* 558 */       Log logger = LogFactory.getLog(ContextLoader.class);
/* 559 */       if (logger.isDebugEnabled()) {
/* 560 */         logger.debug("Getting parent context definition: using parent context key of '" + parentContextKey + "' with BeanFactoryLocator");
/*     */       }
/*     */       
/* 563 */       this.parentContextRef = locator.useBeanFactory(parentContextKey);
/* 564 */       parentContext = (ApplicationContext)this.parentContextRef.getFactory();
/*     */     } 
/*     */     
/* 567 */     return parentContext;
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
/*     */   public void closeWebApplicationContext(ServletContext servletContext) {
/* 580 */     servletContext.log("Closing Spring root WebApplicationContext");
/*     */     try {
/* 582 */       if (this.context instanceof ConfigurableWebApplicationContext) {
/* 583 */         ((ConfigurableWebApplicationContext)this.context).close();
/*     */       }
/*     */     } finally {
/*     */       
/* 587 */       ClassLoader ccl = Thread.currentThread().getContextClassLoader();
/* 588 */       if (ccl == ContextLoader.class.getClassLoader()) {
/* 589 */         currentContext = null;
/*     */       }
/* 591 */       else if (ccl != null) {
/* 592 */         currentContextPerThread.remove(ccl);
/*     */       } 
/* 594 */       servletContext.removeAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
/* 595 */       if (this.parentContextRef != null) {
/* 596 */         this.parentContextRef.release();
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
/*     */   public static WebApplicationContext getCurrentWebApplicationContext() {
/* 611 */     ClassLoader ccl = Thread.currentThread().getContextClassLoader();
/* 612 */     if (ccl != null) {
/* 613 */       WebApplicationContext ccpt = currentContextPerThread.get(ccl);
/* 614 */       if (ccpt != null) {
/* 615 */         return ccpt;
/*     */       }
/*     */     } 
/* 618 */     return currentContext;
/*     */   }
/*     */   
/*     */   public ContextLoader() {}
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\context\ContextLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */