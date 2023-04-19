/*      */ package org.springframework.context.support;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.lang.annotation.Annotation;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.Date;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.atomic.AtomicBoolean;
/*      */ import org.apache.commons.logging.Log;
/*      */ import org.apache.commons.logging.LogFactory;
/*      */ import org.springframework.beans.BeansException;
/*      */ import org.springframework.beans.CachedIntrospectionResults;
/*      */ import org.springframework.beans.PropertyEditorRegistrar;
/*      */ import org.springframework.beans.factory.BeanFactory;
/*      */ import org.springframework.beans.factory.DisposableBean;
/*      */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*      */ import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
/*      */ import org.springframework.beans.factory.config.BeanExpressionResolver;
/*      */ import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
/*      */ import org.springframework.beans.factory.config.BeanPostProcessor;
/*      */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*      */ import org.springframework.beans.support.ResourceEditorRegistrar;
/*      */ import org.springframework.context.ApplicationContext;
/*      */ import org.springframework.context.ApplicationContextAware;
/*      */ import org.springframework.context.ApplicationEvent;
/*      */ import org.springframework.context.ApplicationEventPublisher;
/*      */ import org.springframework.context.ApplicationEventPublisherAware;
/*      */ import org.springframework.context.ApplicationListener;
/*      */ import org.springframework.context.ConfigurableApplicationContext;
/*      */ import org.springframework.context.EmbeddedValueResolverAware;
/*      */ import org.springframework.context.EnvironmentAware;
/*      */ import org.springframework.context.HierarchicalMessageSource;
/*      */ import org.springframework.context.LifecycleProcessor;
/*      */ import org.springframework.context.MessageSource;
/*      */ import org.springframework.context.MessageSourceAware;
/*      */ import org.springframework.context.MessageSourceResolvable;
/*      */ import org.springframework.context.NoSuchMessageException;
/*      */ import org.springframework.context.PayloadApplicationEvent;
/*      */ import org.springframework.context.ResourceLoaderAware;
/*      */ import org.springframework.context.event.ApplicationEventMulticaster;
/*      */ import org.springframework.context.event.ContextClosedEvent;
/*      */ import org.springframework.context.event.ContextRefreshedEvent;
/*      */ import org.springframework.context.event.ContextStartedEvent;
/*      */ import org.springframework.context.event.ContextStoppedEvent;
/*      */ import org.springframework.context.event.SimpleApplicationEventMulticaster;
/*      */ import org.springframework.context.expression.StandardBeanExpressionResolver;
/*      */ import org.springframework.context.weaving.LoadTimeWeaverAware;
/*      */ import org.springframework.context.weaving.LoadTimeWeaverAwareProcessor;
/*      */ import org.springframework.core.ResolvableType;
/*      */ import org.springframework.core.convert.ConversionService;
/*      */ import org.springframework.core.env.ConfigurableEnvironment;
/*      */ import org.springframework.core.env.Environment;
/*      */ import org.springframework.core.env.PropertyResolver;
/*      */ import org.springframework.core.env.StandardEnvironment;
/*      */ import org.springframework.core.io.DefaultResourceLoader;
/*      */ import org.springframework.core.io.Resource;
/*      */ import org.springframework.core.io.ResourceLoader;
/*      */ import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
/*      */ import org.springframework.core.io.support.ResourcePatternResolver;
/*      */ import org.springframework.util.Assert;
/*      */ import org.springframework.util.ObjectUtils;
/*      */ import org.springframework.util.ReflectionUtils;
/*      */ import org.springframework.util.StringValueResolver;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class AbstractApplicationContext
/*      */   extends DefaultResourceLoader
/*      */   implements ConfigurableApplicationContext, DisposableBean
/*      */ {
/*      */   public static final String MESSAGE_SOURCE_BEAN_NAME = "messageSource";
/*      */   public static final String LIFECYCLE_PROCESSOR_BEAN_NAME = "lifecycleProcessor";
/*      */   public static final String APPLICATION_EVENT_MULTICASTER_BEAN_NAME = "applicationEventMulticaster";
/*      */   
/*      */   static {
/*  156 */     ContextClosedEvent.class.getName();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*  161 */   protected final Log logger = LogFactory.getLog(getClass());
/*      */ 
/*      */   
/*  164 */   private String id = ObjectUtils.identityToString(this);
/*      */ 
/*      */   
/*  167 */   private String displayName = ObjectUtils.identityToString(this);
/*      */ 
/*      */   
/*      */   private ApplicationContext parent;
/*      */ 
/*      */   
/*      */   private ConfigurableEnvironment environment;
/*      */ 
/*      */   
/*  176 */   private final List<BeanFactoryPostProcessor> beanFactoryPostProcessors = new ArrayList<BeanFactoryPostProcessor>();
/*      */ 
/*      */ 
/*      */   
/*      */   private long startupDate;
/*      */ 
/*      */   
/*  183 */   private final AtomicBoolean active = new AtomicBoolean();
/*      */ 
/*      */   
/*  186 */   private final AtomicBoolean closed = new AtomicBoolean();
/*      */ 
/*      */   
/*  189 */   private final Object startupShutdownMonitor = new Object();
/*      */ 
/*      */   
/*      */   private Thread shutdownHook;
/*      */ 
/*      */   
/*      */   private ResourcePatternResolver resourcePatternResolver;
/*      */ 
/*      */   
/*      */   private LifecycleProcessor lifecycleProcessor;
/*      */ 
/*      */   
/*      */   private MessageSource messageSource;
/*      */ 
/*      */   
/*      */   private ApplicationEventMulticaster applicationEventMulticaster;
/*      */ 
/*      */   
/*  207 */   private final Set<ApplicationListener<?>> applicationListeners = new LinkedHashSet<ApplicationListener<?>>();
/*      */ 
/*      */ 
/*      */   
/*      */   private Set<ApplicationEvent> earlyApplicationEvents;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public AbstractApplicationContext() {
/*  217 */     this.resourcePatternResolver = getResourcePatternResolver();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public AbstractApplicationContext(ApplicationContext parent) {
/*  225 */     this();
/*  226 */     setParent(parent);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setId(String id) {
/*  242 */     this.id = id;
/*      */   }
/*      */ 
/*      */   
/*      */   public String getId() {
/*  247 */     return this.id;
/*      */   }
/*      */ 
/*      */   
/*      */   public String getApplicationName() {
/*  252 */     return "";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDisplayName(String displayName) {
/*  261 */     Assert.hasLength(displayName, "Display name must not be empty");
/*  262 */     this.displayName = displayName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getDisplayName() {
/*  271 */     return this.displayName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ApplicationContext getParent() {
/*  280 */     return this.parent;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setEnvironment(ConfigurableEnvironment environment) {
/*  293 */     this.environment = environment;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ConfigurableEnvironment getEnvironment() {
/*  304 */     if (this.environment == null) {
/*  305 */       this.environment = createEnvironment();
/*      */     }
/*  307 */     return this.environment;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected ConfigurableEnvironment createEnvironment() {
/*  316 */     return (ConfigurableEnvironment)new StandardEnvironment();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public AutowireCapableBeanFactory getAutowireCapableBeanFactory() throws IllegalStateException {
/*  326 */     return (AutowireCapableBeanFactory)getBeanFactory();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getStartupDate() {
/*  334 */     return this.startupDate;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void publishEvent(ApplicationEvent event) {
/*  347 */     publishEvent(event, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void publishEvent(Object event) {
/*  360 */     publishEvent(event, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void publishEvent(Object event, ResolvableType eventType) {
/*      */     PayloadApplicationEvent payloadApplicationEvent;
/*  371 */     Assert.notNull(event, "Event must not be null");
/*  372 */     if (this.logger.isTraceEnabled()) {
/*  373 */       this.logger.trace("Publishing event in " + getDisplayName() + ": " + event);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  378 */     if (event instanceof ApplicationEvent) {
/*  379 */       ApplicationEvent applicationEvent = (ApplicationEvent)event;
/*      */     } else {
/*      */       
/*  382 */       payloadApplicationEvent = new PayloadApplicationEvent(this, event);
/*  383 */       if (eventType == null) {
/*  384 */         eventType = payloadApplicationEvent.getResolvableType();
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/*  389 */     if (this.earlyApplicationEvents != null) {
/*  390 */       this.earlyApplicationEvents.add(payloadApplicationEvent);
/*      */     } else {
/*      */       
/*  393 */       getApplicationEventMulticaster().multicastEvent((ApplicationEvent)payloadApplicationEvent, eventType);
/*      */     } 
/*      */ 
/*      */     
/*  397 */     if (this.parent != null) {
/*  398 */       if (this.parent instanceof AbstractApplicationContext) {
/*  399 */         ((AbstractApplicationContext)this.parent).publishEvent(event, eventType);
/*      */       } else {
/*      */         
/*  402 */         this.parent.publishEvent(event);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   ApplicationEventMulticaster getApplicationEventMulticaster() throws IllegalStateException {
/*  413 */     if (this.applicationEventMulticaster == null) {
/*  414 */       throw new IllegalStateException("ApplicationEventMulticaster not initialized - call 'refresh' before multicasting events via the context: " + this);
/*      */     }
/*      */     
/*  417 */     return this.applicationEventMulticaster;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   LifecycleProcessor getLifecycleProcessor() throws IllegalStateException {
/*  426 */     if (this.lifecycleProcessor == null) {
/*  427 */       throw new IllegalStateException("LifecycleProcessor not initialized - call 'refresh' before invoking lifecycle methods via the context: " + this);
/*      */     }
/*      */     
/*  430 */     return this.lifecycleProcessor;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected ResourcePatternResolver getResourcePatternResolver() {
/*  448 */     return (ResourcePatternResolver)new PathMatchingResourcePatternResolver((ResourceLoader)this);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setParent(ApplicationContext parent) {
/*  466 */     this.parent = parent;
/*  467 */     if (parent != null) {
/*  468 */       Environment parentEnvironment = parent.getEnvironment();
/*  469 */       if (parentEnvironment instanceof ConfigurableEnvironment) {
/*  470 */         getEnvironment().merge((ConfigurableEnvironment)parentEnvironment);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void addBeanFactoryPostProcessor(BeanFactoryPostProcessor postProcessor) {
/*  477 */     Assert.notNull(postProcessor, "BeanFactoryPostProcessor must not be null");
/*  478 */     this.beanFactoryPostProcessors.add(postProcessor);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<BeanFactoryPostProcessor> getBeanFactoryPostProcessors() {
/*  487 */     return this.beanFactoryPostProcessors;
/*      */   }
/*      */ 
/*      */   
/*      */   public void addApplicationListener(ApplicationListener<?> listener) {
/*  492 */     Assert.notNull(listener, "ApplicationListener must not be null");
/*  493 */     if (this.applicationEventMulticaster != null) {
/*  494 */       this.applicationEventMulticaster.addApplicationListener(listener);
/*      */     } else {
/*      */       
/*  497 */       this.applicationListeners.add(listener);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Collection<ApplicationListener<?>> getApplicationListeners() {
/*  505 */     return this.applicationListeners;
/*      */   }
/*      */ 
/*      */   
/*      */   public void refresh() throws BeansException, IllegalStateException {
/*  510 */     synchronized (this.startupShutdownMonitor) {
/*      */       
/*  512 */       prepareRefresh();
/*      */ 
/*      */       
/*  515 */       ConfigurableListableBeanFactory beanFactory = obtainFreshBeanFactory();
/*      */ 
/*      */       
/*  518 */       prepareBeanFactory(beanFactory);
/*      */ 
/*      */       
/*      */       try {
/*  522 */         postProcessBeanFactory(beanFactory);
/*      */ 
/*      */         
/*  525 */         invokeBeanFactoryPostProcessors(beanFactory);
/*      */ 
/*      */         
/*  528 */         registerBeanPostProcessors(beanFactory);
/*      */ 
/*      */         
/*  531 */         initMessageSource();
/*      */ 
/*      */         
/*  534 */         initApplicationEventMulticaster();
/*      */ 
/*      */         
/*  537 */         onRefresh();
/*      */ 
/*      */         
/*  540 */         registerListeners();
/*      */ 
/*      */         
/*  543 */         finishBeanFactoryInitialization(beanFactory);
/*      */ 
/*      */         
/*  546 */         finishRefresh();
/*      */       
/*      */       }
/*  549 */       catch (BeansException ex) {
/*  550 */         if (this.logger.isWarnEnabled()) {
/*  551 */           this.logger.warn("Exception encountered during context initialization - cancelling refresh attempt: " + ex);
/*      */         }
/*      */ 
/*      */ 
/*      */         
/*  556 */         destroyBeans();
/*      */ 
/*      */         
/*  559 */         cancelRefresh(ex);
/*      */ 
/*      */         
/*  562 */         throw ex;
/*      */       
/*      */       }
/*      */       finally {
/*      */ 
/*      */         
/*  568 */         resetCommonCaches();
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void prepareRefresh() {
/*  578 */     this.startupDate = System.currentTimeMillis();
/*  579 */     this.closed.set(false);
/*  580 */     this.active.set(true);
/*      */     
/*  582 */     if (this.logger.isInfoEnabled()) {
/*  583 */       this.logger.info("Refreshing " + this);
/*      */     }
/*      */ 
/*      */     
/*  587 */     initPropertySources();
/*      */ 
/*      */ 
/*      */     
/*  591 */     getEnvironment().validateRequiredProperties();
/*      */ 
/*      */ 
/*      */     
/*  595 */     this.earlyApplicationEvents = new LinkedHashSet<ApplicationEvent>();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void initPropertySources() {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected ConfigurableListableBeanFactory obtainFreshBeanFactory() {
/*  614 */     refreshBeanFactory();
/*  615 */     ConfigurableListableBeanFactory beanFactory = getBeanFactory();
/*  616 */     if (this.logger.isDebugEnabled()) {
/*  617 */       this.logger.debug("Bean factory for " + getDisplayName() + ": " + beanFactory);
/*      */     }
/*  619 */     return beanFactory;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void prepareBeanFactory(ConfigurableListableBeanFactory beanFactory) {
/*  629 */     beanFactory.setBeanClassLoader(getClassLoader());
/*  630 */     beanFactory.setBeanExpressionResolver((BeanExpressionResolver)new StandardBeanExpressionResolver(beanFactory.getBeanClassLoader()));
/*  631 */     beanFactory.addPropertyEditorRegistrar((PropertyEditorRegistrar)new ResourceEditorRegistrar((ResourceLoader)this, (PropertyResolver)getEnvironment()));
/*      */ 
/*      */     
/*  634 */     beanFactory.addBeanPostProcessor(new ApplicationContextAwareProcessor(this));
/*  635 */     beanFactory.ignoreDependencyInterface(EnvironmentAware.class);
/*  636 */     beanFactory.ignoreDependencyInterface(EmbeddedValueResolverAware.class);
/*  637 */     beanFactory.ignoreDependencyInterface(ResourceLoaderAware.class);
/*  638 */     beanFactory.ignoreDependencyInterface(ApplicationEventPublisherAware.class);
/*  639 */     beanFactory.ignoreDependencyInterface(MessageSourceAware.class);
/*  640 */     beanFactory.ignoreDependencyInterface(ApplicationContextAware.class);
/*      */ 
/*      */ 
/*      */     
/*  644 */     beanFactory.registerResolvableDependency(BeanFactory.class, beanFactory);
/*  645 */     beanFactory.registerResolvableDependency(ResourceLoader.class, this);
/*  646 */     beanFactory.registerResolvableDependency(ApplicationEventPublisher.class, this);
/*  647 */     beanFactory.registerResolvableDependency(ApplicationContext.class, this);
/*      */ 
/*      */     
/*  650 */     beanFactory.addBeanPostProcessor((BeanPostProcessor)new ApplicationListenerDetector(this));
/*      */ 
/*      */     
/*  653 */     if (beanFactory.containsBean("loadTimeWeaver")) {
/*  654 */       beanFactory.addBeanPostProcessor((BeanPostProcessor)new LoadTimeWeaverAwareProcessor((BeanFactory)beanFactory));
/*      */       
/*  656 */       beanFactory.setTempClassLoader((ClassLoader)new ContextTypeMatchClassLoader(beanFactory.getBeanClassLoader()));
/*      */     } 
/*      */ 
/*      */     
/*  660 */     if (!beanFactory.containsLocalBean("environment")) {
/*  661 */       beanFactory.registerSingleton("environment", getEnvironment());
/*      */     }
/*  663 */     if (!beanFactory.containsLocalBean("systemProperties")) {
/*  664 */       beanFactory.registerSingleton("systemProperties", getEnvironment().getSystemProperties());
/*      */     }
/*  666 */     if (!beanFactory.containsLocalBean("systemEnvironment")) {
/*  667 */       beanFactory.registerSingleton("systemEnvironment", getEnvironment().getSystemEnvironment());
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void invokeBeanFactoryPostProcessors(ConfigurableListableBeanFactory beanFactory) {
/*  687 */     PostProcessorRegistrationDelegate.invokeBeanFactoryPostProcessors(beanFactory, getBeanFactoryPostProcessors());
/*      */ 
/*      */ 
/*      */     
/*  691 */     if (beanFactory.getTempClassLoader() == null && beanFactory.containsBean("loadTimeWeaver")) {
/*  692 */       beanFactory.addBeanPostProcessor((BeanPostProcessor)new LoadTimeWeaverAwareProcessor((BeanFactory)beanFactory));
/*  693 */       beanFactory.setTempClassLoader((ClassLoader)new ContextTypeMatchClassLoader(beanFactory.getBeanClassLoader()));
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory) {
/*  703 */     PostProcessorRegistrationDelegate.registerBeanPostProcessors(beanFactory, this);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void initMessageSource() {
/*  711 */     ConfigurableListableBeanFactory beanFactory = getBeanFactory();
/*  712 */     if (beanFactory.containsLocalBean("messageSource")) {
/*  713 */       this.messageSource = (MessageSource)beanFactory.getBean("messageSource", MessageSource.class);
/*      */       
/*  715 */       if (this.parent != null && this.messageSource instanceof HierarchicalMessageSource) {
/*  716 */         HierarchicalMessageSource hms = (HierarchicalMessageSource)this.messageSource;
/*  717 */         if (hms.getParentMessageSource() == null)
/*      */         {
/*      */           
/*  720 */           hms.setParentMessageSource(getInternalParentMessageSource());
/*      */         }
/*      */       } 
/*  723 */       if (this.logger.isDebugEnabled()) {
/*  724 */         this.logger.debug("Using MessageSource [" + this.messageSource + "]");
/*      */       }
/*      */     }
/*      */     else {
/*      */       
/*  729 */       DelegatingMessageSource dms = new DelegatingMessageSource();
/*  730 */       dms.setParentMessageSource(getInternalParentMessageSource());
/*  731 */       this.messageSource = (MessageSource)dms;
/*  732 */       beanFactory.registerSingleton("messageSource", this.messageSource);
/*  733 */       if (this.logger.isDebugEnabled()) {
/*  734 */         this.logger.debug("Unable to locate MessageSource with name 'messageSource': using default [" + this.messageSource + "]");
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void initApplicationEventMulticaster() {
/*  746 */     ConfigurableListableBeanFactory beanFactory = getBeanFactory();
/*  747 */     if (beanFactory.containsLocalBean("applicationEventMulticaster")) {
/*  748 */       this
/*  749 */         .applicationEventMulticaster = (ApplicationEventMulticaster)beanFactory.getBean("applicationEventMulticaster", ApplicationEventMulticaster.class);
/*  750 */       if (this.logger.isDebugEnabled()) {
/*  751 */         this.logger.debug("Using ApplicationEventMulticaster [" + this.applicationEventMulticaster + "]");
/*      */       }
/*      */     } else {
/*      */       
/*  755 */       this.applicationEventMulticaster = (ApplicationEventMulticaster)new SimpleApplicationEventMulticaster((BeanFactory)beanFactory);
/*  756 */       beanFactory.registerSingleton("applicationEventMulticaster", this.applicationEventMulticaster);
/*  757 */       if (this.logger.isDebugEnabled()) {
/*  758 */         this.logger.debug("Unable to locate ApplicationEventMulticaster with name 'applicationEventMulticaster': using default [" + this.applicationEventMulticaster + "]");
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void initLifecycleProcessor() {
/*  771 */     ConfigurableListableBeanFactory beanFactory = getBeanFactory();
/*  772 */     if (beanFactory.containsLocalBean("lifecycleProcessor")) {
/*  773 */       this
/*  774 */         .lifecycleProcessor = (LifecycleProcessor)beanFactory.getBean("lifecycleProcessor", LifecycleProcessor.class);
/*  775 */       if (this.logger.isDebugEnabled()) {
/*  776 */         this.logger.debug("Using LifecycleProcessor [" + this.lifecycleProcessor + "]");
/*      */       }
/*      */     } else {
/*      */       
/*  780 */       DefaultLifecycleProcessor defaultProcessor = new DefaultLifecycleProcessor();
/*  781 */       defaultProcessor.setBeanFactory((BeanFactory)beanFactory);
/*  782 */       this.lifecycleProcessor = defaultProcessor;
/*  783 */       beanFactory.registerSingleton("lifecycleProcessor", this.lifecycleProcessor);
/*  784 */       if (this.logger.isDebugEnabled()) {
/*  785 */         this.logger.debug("Unable to locate LifecycleProcessor with name 'lifecycleProcessor': using default [" + this.lifecycleProcessor + "]");
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void onRefresh() throws BeansException {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void registerListeners() {
/*  809 */     for (ApplicationListener<?> listener : getApplicationListeners()) {
/*  810 */       getApplicationEventMulticaster().addApplicationListener(listener);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  815 */     String[] listenerBeanNames = getBeanNamesForType(ApplicationListener.class, true, false);
/*  816 */     for (String listenerBeanName : listenerBeanNames) {
/*  817 */       getApplicationEventMulticaster().addApplicationListenerBean(listenerBeanName);
/*      */     }
/*      */ 
/*      */     
/*  821 */     Set<ApplicationEvent> earlyEventsToProcess = this.earlyApplicationEvents;
/*  822 */     this.earlyApplicationEvents = null;
/*  823 */     if (earlyEventsToProcess != null) {
/*  824 */       for (ApplicationEvent earlyEvent : earlyEventsToProcess) {
/*  825 */         getApplicationEventMulticaster().multicastEvent(earlyEvent);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void finishBeanFactoryInitialization(ConfigurableListableBeanFactory beanFactory) {
/*  836 */     if (beanFactory.containsBean("conversionService") && beanFactory
/*  837 */       .isTypeMatch("conversionService", ConversionService.class)) {
/*  838 */       beanFactory.setConversionService((ConversionService)beanFactory
/*  839 */           .getBean("conversionService", ConversionService.class));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  845 */     if (!beanFactory.hasEmbeddedValueResolver()) {
/*  846 */       beanFactory.addEmbeddedValueResolver(new StringValueResolver()
/*      */           {
/*      */             public String resolveStringValue(String strVal) {
/*  849 */               return AbstractApplicationContext.this.getEnvironment().resolvePlaceholders(strVal);
/*      */             }
/*      */           });
/*      */     }
/*      */ 
/*      */     
/*  855 */     String[] weaverAwareNames = beanFactory.getBeanNamesForType(LoadTimeWeaverAware.class, false, false);
/*  856 */     for (String weaverAwareName : weaverAwareNames) {
/*  857 */       getBean(weaverAwareName);
/*      */     }
/*      */ 
/*      */     
/*  861 */     beanFactory.setTempClassLoader(null);
/*      */ 
/*      */     
/*  864 */     beanFactory.freezeConfiguration();
/*      */ 
/*      */     
/*  867 */     beanFactory.preInstantiateSingletons();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void finishRefresh() {
/*  877 */     initLifecycleProcessor();
/*      */ 
/*      */     
/*  880 */     getLifecycleProcessor().onRefresh();
/*      */ 
/*      */     
/*  883 */     publishEvent((ApplicationEvent)new ContextRefreshedEvent((ApplicationContext)this));
/*      */ 
/*      */     
/*  886 */     LiveBeansView.registerApplicationContext(this);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void cancelRefresh(BeansException ex) {
/*  895 */     this.active.set(false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void resetCommonCaches() {
/*  907 */     ReflectionUtils.clearCache();
/*  908 */     ResolvableType.clearCache();
/*  909 */     CachedIntrospectionResults.clearClassLoader(getClassLoader());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void registerShutdownHook() {
/*  923 */     if (this.shutdownHook == null) {
/*      */       
/*  925 */       this.shutdownHook = new Thread()
/*      */         {
/*      */           public void run() {
/*  928 */             synchronized (AbstractApplicationContext.this.startupShutdownMonitor) {
/*  929 */               AbstractApplicationContext.this.doClose();
/*      */             } 
/*      */           }
/*      */         };
/*  933 */       Runtime.getRuntime().addShutdownHook(this.shutdownHook);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void destroy() {
/*  945 */     close();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void close() {
/*  957 */     synchronized (this.startupShutdownMonitor) {
/*  958 */       doClose();
/*      */ 
/*      */       
/*  961 */       if (this.shutdownHook != null) {
/*      */         try {
/*  963 */           Runtime.getRuntime().removeShutdownHook(this.shutdownHook);
/*      */         }
/*  965 */         catch (IllegalStateException illegalStateException) {}
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void doClose() {
/*  982 */     if (this.active.get() && this.closed.compareAndSet(false, true)) {
/*  983 */       if (this.logger.isInfoEnabled()) {
/*  984 */         this.logger.info("Closing " + this);
/*      */       }
/*      */       
/*  987 */       LiveBeansView.unregisterApplicationContext(this);
/*      */ 
/*      */       
/*      */       try {
/*  991 */         publishEvent((ApplicationEvent)new ContextClosedEvent((ApplicationContext)this));
/*      */       }
/*  993 */       catch (Throwable ex) {
/*  994 */         this.logger.warn("Exception thrown from ApplicationListener handling ContextClosedEvent", ex);
/*      */       } 
/*      */ 
/*      */       
/*  998 */       if (this.lifecycleProcessor != null) {
/*      */         try {
/* 1000 */           this.lifecycleProcessor.onClose();
/*      */         }
/* 1002 */         catch (Throwable ex) {
/* 1003 */           this.logger.warn("Exception thrown from LifecycleProcessor on context close", ex);
/*      */         } 
/*      */       }
/*      */ 
/*      */       
/* 1008 */       destroyBeans();
/*      */ 
/*      */       
/* 1011 */       closeBeanFactory();
/*      */ 
/*      */       
/* 1014 */       onClose();
/*      */       
/* 1016 */       this.active.set(false);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void destroyBeans() {
/* 1032 */     getBeanFactory().destroySingletons();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void onClose() {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isActive() {
/* 1049 */     return this.active.get();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void assertBeanFactoryActive() {
/* 1062 */     if (!this.active.get()) {
/* 1063 */       if (this.closed.get()) {
/* 1064 */         throw new IllegalStateException(getDisplayName() + " has been closed already");
/*      */       }
/*      */       
/* 1067 */       throw new IllegalStateException(getDisplayName() + " has not been refreshed yet");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object getBean(String name) throws BeansException {
/* 1079 */     assertBeanFactoryActive();
/* 1080 */     return getBeanFactory().getBean(name);
/*      */   }
/*      */ 
/*      */   
/*      */   public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
/* 1085 */     assertBeanFactoryActive();
/* 1086 */     return (T)getBeanFactory().getBean(name, requiredType);
/*      */   }
/*      */ 
/*      */   
/*      */   public <T> T getBean(Class<T> requiredType) throws BeansException {
/* 1091 */     assertBeanFactoryActive();
/* 1092 */     return (T)getBeanFactory().getBean(requiredType);
/*      */   }
/*      */ 
/*      */   
/*      */   public Object getBean(String name, Object... args) throws BeansException {
/* 1097 */     assertBeanFactoryActive();
/* 1098 */     return getBeanFactory().getBean(name, args);
/*      */   }
/*      */ 
/*      */   
/*      */   public <T> T getBean(Class<T> requiredType, Object... args) throws BeansException {
/* 1103 */     assertBeanFactoryActive();
/* 1104 */     return (T)getBeanFactory().getBean(requiredType, args);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean containsBean(String name) {
/* 1109 */     return getBeanFactory().containsBean(name);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
/* 1114 */     assertBeanFactoryActive();
/* 1115 */     return getBeanFactory().isSingleton(name);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isPrototype(String name) throws NoSuchBeanDefinitionException {
/* 1120 */     assertBeanFactoryActive();
/* 1121 */     return getBeanFactory().isPrototype(name);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isTypeMatch(String name, ResolvableType typeToMatch) throws NoSuchBeanDefinitionException {
/* 1126 */     assertBeanFactoryActive();
/* 1127 */     return getBeanFactory().isTypeMatch(name, typeToMatch);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isTypeMatch(String name, Class<?> typeToMatch) throws NoSuchBeanDefinitionException {
/* 1132 */     assertBeanFactoryActive();
/* 1133 */     return getBeanFactory().isTypeMatch(name, typeToMatch);
/*      */   }
/*      */ 
/*      */   
/*      */   public Class<?> getType(String name) throws NoSuchBeanDefinitionException {
/* 1138 */     assertBeanFactoryActive();
/* 1139 */     return getBeanFactory().getType(name);
/*      */   }
/*      */ 
/*      */   
/*      */   public String[] getAliases(String name) {
/* 1144 */     return getBeanFactory().getAliases(name);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean containsBeanDefinition(String beanName) {
/* 1154 */     return getBeanFactory().containsBeanDefinition(beanName);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getBeanDefinitionCount() {
/* 1159 */     return getBeanFactory().getBeanDefinitionCount();
/*      */   }
/*      */ 
/*      */   
/*      */   public String[] getBeanDefinitionNames() {
/* 1164 */     return getBeanFactory().getBeanDefinitionNames();
/*      */   }
/*      */ 
/*      */   
/*      */   public String[] getBeanNamesForType(ResolvableType type) {
/* 1169 */     assertBeanFactoryActive();
/* 1170 */     return getBeanFactory().getBeanNamesForType(type);
/*      */   }
/*      */ 
/*      */   
/*      */   public String[] getBeanNamesForType(Class<?> type) {
/* 1175 */     assertBeanFactoryActive();
/* 1176 */     return getBeanFactory().getBeanNamesForType(type);
/*      */   }
/*      */ 
/*      */   
/*      */   public String[] getBeanNamesForType(Class<?> type, boolean includeNonSingletons, boolean allowEagerInit) {
/* 1181 */     assertBeanFactoryActive();
/* 1182 */     return getBeanFactory().getBeanNamesForType(type, includeNonSingletons, allowEagerInit);
/*      */   }
/*      */ 
/*      */   
/*      */   public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException {
/* 1187 */     assertBeanFactoryActive();
/* 1188 */     return getBeanFactory().getBeansOfType(type);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> Map<String, T> getBeansOfType(Class<T> type, boolean includeNonSingletons, boolean allowEagerInit) throws BeansException {
/* 1195 */     assertBeanFactoryActive();
/* 1196 */     return getBeanFactory().getBeansOfType(type, includeNonSingletons, allowEagerInit);
/*      */   }
/*      */ 
/*      */   
/*      */   public String[] getBeanNamesForAnnotation(Class<? extends Annotation> annotationType) {
/* 1201 */     assertBeanFactoryActive();
/* 1202 */     return getBeanFactory().getBeanNamesForAnnotation(annotationType);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> annotationType) throws BeansException {
/* 1209 */     assertBeanFactoryActive();
/* 1210 */     return getBeanFactory().getBeansWithAnnotation(annotationType);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <A extends Annotation> A findAnnotationOnBean(String beanName, Class<A> annotationType) throws NoSuchBeanDefinitionException {
/* 1217 */     assertBeanFactoryActive();
/* 1218 */     return (A)getBeanFactory().findAnnotationOnBean(beanName, annotationType);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public BeanFactory getParentBeanFactory() {
/* 1228 */     return (BeanFactory)getParent();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean containsLocalBean(String name) {
/* 1233 */     return getBeanFactory().containsLocalBean(name);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected BeanFactory getInternalParentBeanFactory() {
/* 1242 */     return (getParent() instanceof ConfigurableApplicationContext) ? (BeanFactory)((ConfigurableApplicationContext)
/* 1243 */       getParent()).getBeanFactory() : (BeanFactory)getParent();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getMessage(String code, Object[] args, String defaultMessage, Locale locale) {
/* 1253 */     return getMessageSource().getMessage(code, args, defaultMessage, locale);
/*      */   }
/*      */ 
/*      */   
/*      */   public String getMessage(String code, Object[] args, Locale locale) throws NoSuchMessageException {
/* 1258 */     return getMessageSource().getMessage(code, args, locale);
/*      */   }
/*      */ 
/*      */   
/*      */   public String getMessage(MessageSourceResolvable resolvable, Locale locale) throws NoSuchMessageException {
/* 1263 */     return getMessageSource().getMessage(resolvable, locale);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private MessageSource getMessageSource() throws IllegalStateException {
/* 1272 */     if (this.messageSource == null) {
/* 1273 */       throw new IllegalStateException("MessageSource not initialized - call 'refresh' before accessing messages via the context: " + this);
/*      */     }
/*      */     
/* 1276 */     return this.messageSource;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected MessageSource getInternalParentMessageSource() {
/* 1284 */     return (getParent() instanceof AbstractApplicationContext) ? ((AbstractApplicationContext)
/* 1285 */       getParent()).messageSource : (MessageSource)getParent();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Resource[] getResources(String locationPattern) throws IOException {
/* 1295 */     return this.resourcePatternResolver.getResources(locationPattern);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void start() {
/* 1305 */     getLifecycleProcessor().start();
/* 1306 */     publishEvent((ApplicationEvent)new ContextStartedEvent((ApplicationContext)this));
/*      */   }
/*      */ 
/*      */   
/*      */   public void stop() {
/* 1311 */     getLifecycleProcessor().stop();
/* 1312 */     publishEvent((ApplicationEvent)new ContextStoppedEvent((ApplicationContext)this));
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isRunning() {
/* 1317 */     return (this.lifecycleProcessor != null && this.lifecycleProcessor.isRunning());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString() {
/* 1366 */     StringBuilder sb = new StringBuilder(getDisplayName());
/* 1367 */     sb.append(": startup date [").append(new Date(getStartupDate()));
/* 1368 */     sb.append("]; ");
/* 1369 */     ApplicationContext parent = getParent();
/* 1370 */     if (parent == null) {
/* 1371 */       sb.append("root of context hierarchy");
/*      */     } else {
/*      */       
/* 1374 */       sb.append("parent: ").append(parent.getDisplayName());
/*      */     } 
/* 1376 */     return sb.toString();
/*      */   }
/*      */   
/*      */   protected abstract void refreshBeanFactory() throws BeansException, IllegalStateException;
/*      */   
/*      */   protected abstract void closeBeanFactory();
/*      */   
/*      */   public abstract ConfigurableListableBeanFactory getBeanFactory() throws IllegalStateException;
/*      */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\support\AbstractApplicationContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */