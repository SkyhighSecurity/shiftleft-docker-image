/*      */ package org.springframework.jmx.export;
/*      */ 
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import javax.management.DynamicMBean;
/*      */ import javax.management.JMException;
/*      */ import javax.management.MBeanException;
/*      */ import javax.management.MalformedObjectNameException;
/*      */ import javax.management.NotCompliantMBeanException;
/*      */ import javax.management.NotificationListener;
/*      */ import javax.management.ObjectName;
/*      */ import javax.management.StandardMBean;
/*      */ import javax.management.modelmbean.ModelMBean;
/*      */ import javax.management.modelmbean.ModelMBeanInfo;
/*      */ import javax.management.modelmbean.RequiredModelMBean;
/*      */ import org.springframework.aop.TargetSource;
/*      */ import org.springframework.aop.framework.ProxyFactory;
/*      */ import org.springframework.aop.scope.ScopedProxyUtils;
/*      */ import org.springframework.aop.support.AopUtils;
/*      */ import org.springframework.aop.target.LazyInitTargetSource;
/*      */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*      */ import org.springframework.beans.factory.BeanFactory;
/*      */ import org.springframework.beans.factory.BeanFactoryAware;
/*      */ import org.springframework.beans.factory.CannotLoadBeanClassException;
/*      */ import org.springframework.beans.factory.DisposableBean;
/*      */ import org.springframework.beans.factory.InitializingBean;
/*      */ import org.springframework.beans.factory.ListableBeanFactory;
/*      */ import org.springframework.beans.factory.SmartInitializingSingleton;
/*      */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
/*      */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*      */ import org.springframework.core.Constants;
/*      */ import org.springframework.jmx.export.assembler.AutodetectCapableMBeanInfoAssembler;
/*      */ import org.springframework.jmx.export.assembler.MBeanInfoAssembler;
/*      */ import org.springframework.jmx.export.assembler.SimpleReflectiveMBeanInfoAssembler;
/*      */ import org.springframework.jmx.export.naming.KeyNamingStrategy;
/*      */ import org.springframework.jmx.export.naming.ObjectNamingStrategy;
/*      */ import org.springframework.jmx.export.naming.SelfNaming;
/*      */ import org.springframework.jmx.export.notification.ModelMBeanNotificationPublisher;
/*      */ import org.springframework.jmx.export.notification.NotificationPublisher;
/*      */ import org.springframework.jmx.export.notification.NotificationPublisherAware;
/*      */ import org.springframework.jmx.support.JmxUtils;
/*      */ import org.springframework.jmx.support.MBeanRegistrationSupport;
/*      */ import org.springframework.util.Assert;
/*      */ import org.springframework.util.ClassUtils;
/*      */ import org.springframework.util.CollectionUtils;
/*      */ import org.springframework.util.ObjectUtils;
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
/*      */ public class MBeanExporter
/*      */   extends MBeanRegistrationSupport
/*      */   implements MBeanExportOperations, BeanClassLoaderAware, BeanFactoryAware, InitializingBean, SmartInitializingSingleton, DisposableBean
/*      */ {
/*      */   public static final int AUTODETECT_NONE = 0;
/*      */   public static final int AUTODETECT_MBEAN = 1;
/*      */   public static final int AUTODETECT_ASSEMBLER = 2;
/*      */   public static final int AUTODETECT_ALL = 3;
/*      */   private static final String WILDCARD = "*";
/*      */   private static final String MR_TYPE_OBJECT_REFERENCE = "ObjectReference";
/*      */   private static final String CONSTANT_PREFIX_AUTODETECT = "AUTODETECT_";
/*  140 */   private static final Constants constants = new Constants(MBeanExporter.class);
/*      */ 
/*      */   
/*      */   private Map<String, Object> beans;
/*      */ 
/*      */   
/*      */   private Integer autodetectMode;
/*      */ 
/*      */   
/*      */   private boolean allowEagerInit = false;
/*      */ 
/*      */   
/*  152 */   private MBeanInfoAssembler assembler = (MBeanInfoAssembler)new SimpleReflectiveMBeanInfoAssembler();
/*      */ 
/*      */   
/*  155 */   private ObjectNamingStrategy namingStrategy = (ObjectNamingStrategy)new KeyNamingStrategy();
/*      */ 
/*      */   
/*      */   private boolean ensureUniqueRuntimeObjectNames = true;
/*      */ 
/*      */   
/*      */   private boolean exposeManagedResourceClassLoader = true;
/*      */ 
/*      */   
/*  164 */   private Set<String> excludedBeans = new HashSet<String>();
/*      */ 
/*      */   
/*      */   private MBeanExporterListener[] listeners;
/*      */ 
/*      */   
/*      */   private NotificationListenerBean[] notificationListeners;
/*      */ 
/*      */   
/*  173 */   private final Map<NotificationListenerBean, ObjectName[]> registeredNotificationListeners = (Map)new LinkedHashMap<NotificationListenerBean, ObjectName>();
/*      */ 
/*      */ 
/*      */   
/*  177 */   private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
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
/*      */   private ListableBeanFactory beanFactory;
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
/*      */   public void setBeans(Map<String, Object> beans) {
/*  201 */     this.beans = beans;
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
/*      */   public void setAutodetect(boolean autodetect) {
/*  215 */     this.autodetectMode = Integer.valueOf(autodetect ? 3 : 0);
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
/*      */   public void setAutodetectMode(int autodetectMode) {
/*  229 */     if (!constants.getValues("AUTODETECT_").contains(Integer.valueOf(autodetectMode))) {
/*  230 */       throw new IllegalArgumentException("Only values of autodetect constants allowed");
/*      */     }
/*  232 */     this.autodetectMode = Integer.valueOf(autodetectMode);
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
/*      */   public void setAutodetectModeName(String constantName) {
/*  246 */     if (constantName == null || !constantName.startsWith("AUTODETECT_")) {
/*  247 */       throw new IllegalArgumentException("Only autodetect constants allowed");
/*      */     }
/*  249 */     this.autodetectMode = (Integer)constants.asNumber(constantName);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAllowEagerInit(boolean allowEagerInit) {
/*  260 */     this.allowEagerInit = allowEagerInit;
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
/*      */   public void setAssembler(MBeanInfoAssembler assembler) {
/*  275 */     this.assembler = assembler;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setNamingStrategy(ObjectNamingStrategy namingStrategy) {
/*  285 */     this.namingStrategy = namingStrategy;
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
/*      */   public void setEnsureUniqueRuntimeObjectNames(boolean ensureUniqueRuntimeObjectNames) {
/*  298 */     this.ensureUniqueRuntimeObjectNames = ensureUniqueRuntimeObjectNames;
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
/*      */   public void setExposeManagedResourceClassLoader(boolean exposeManagedResourceClassLoader) {
/*  310 */     this.exposeManagedResourceClassLoader = exposeManagedResourceClassLoader;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setExcludedBeans(String... excludedBeans) {
/*  317 */     this.excludedBeans.clear();
/*  318 */     if (excludedBeans != null) {
/*  319 */       this.excludedBeans.addAll(Arrays.asList(excludedBeans));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addExcludedBean(String excludedBean) {
/*  327 */     Assert.notNull(excludedBean, "ExcludedBean must not be null");
/*  328 */     this.excludedBeans.add(excludedBean);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setListeners(MBeanExporterListener... listeners) {
/*  337 */     this.listeners = listeners;
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
/*      */   public void setNotificationListeners(NotificationListenerBean... notificationListeners) {
/*  349 */     this.notificationListeners = notificationListeners;
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
/*      */   public void setNotificationListenerMappings(Map<?, ? extends NotificationListener> listeners) {
/*  367 */     Assert.notNull(listeners, "'listeners' must not be null");
/*      */     
/*  369 */     List<NotificationListenerBean> notificationListeners = new ArrayList<NotificationListenerBean>(listeners.size());
/*      */     
/*  371 */     for (Map.Entry<?, ? extends NotificationListener> entry : listeners.entrySet()) {
/*      */       
/*  373 */       NotificationListenerBean bean = new NotificationListenerBean(entry.getValue());
/*      */       
/*  375 */       Object key = entry.getKey();
/*  376 */       if (key != null && !"*".equals(key))
/*      */       {
/*  378 */         bean.setMappedObjectName(entry.getKey());
/*      */       }
/*  380 */       notificationListeners.add(bean);
/*      */     } 
/*      */     
/*  383 */     this
/*  384 */       .notificationListeners = notificationListeners.<NotificationListenerBean>toArray(new NotificationListenerBean[notificationListeners.size()]);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setBeanClassLoader(ClassLoader classLoader) {
/*  389 */     this.beanClassLoader = classLoader;
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
/*      */   public void setBeanFactory(BeanFactory beanFactory) {
/*  402 */     if (beanFactory instanceof ListableBeanFactory) {
/*  403 */       this.beanFactory = (ListableBeanFactory)beanFactory;
/*      */     } else {
/*      */       
/*  406 */       this.logger.info("MBeanExporter not running in a ListableBeanFactory: autodetection of MBeans not available.");
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
/*      */   public void afterPropertiesSet() {
/*  419 */     if (this.server == null) {
/*  420 */       this.server = JmxUtils.locateMBeanServer();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void afterSingletonsInstantiated() {
/*      */     try {
/*  431 */       this.logger.info("Registering beans for JMX exposure on startup");
/*  432 */       registerBeans();
/*  433 */       registerNotificationListeners();
/*      */     }
/*  435 */     catch (RuntimeException ex) {
/*      */       
/*  437 */       unregisterNotificationListeners();
/*  438 */       unregisterBeans();
/*  439 */       throw ex;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void destroy() {
/*  449 */     this.logger.info("Unregistering JMX-exposed beans on shutdown");
/*  450 */     unregisterNotificationListeners();
/*  451 */     unregisterBeans();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectName registerManagedResource(Object managedResource) throws MBeanExportException {
/*      */     ObjectName objectName;
/*  461 */     Assert.notNull(managedResource, "Managed resource must not be null");
/*      */     
/*      */     try {
/*  464 */       objectName = getObjectName(managedResource, (String)null);
/*  465 */       if (this.ensureUniqueRuntimeObjectNames) {
/*  466 */         objectName = JmxUtils.appendIdentityToObjectName(objectName, managedResource);
/*      */       }
/*      */     }
/*  469 */     catch (Throwable ex) {
/*  470 */       throw new MBeanExportException("Unable to generate ObjectName for MBean [" + managedResource + "]", ex);
/*      */     } 
/*  472 */     registerManagedResource(managedResource, objectName);
/*  473 */     return objectName;
/*      */   }
/*      */ 
/*      */   
/*      */   public void registerManagedResource(Object managedResource, ObjectName objectName) throws MBeanExportException {
/*  478 */     Assert.notNull(managedResource, "Managed resource must not be null");
/*  479 */     Assert.notNull(objectName, "ObjectName must not be null");
/*      */     try {
/*  481 */       if (isMBean(managedResource.getClass())) {
/*  482 */         doRegister(managedResource, objectName);
/*      */       } else {
/*      */         
/*  485 */         ModelMBean mbean = createAndConfigureMBean(managedResource, managedResource.getClass().getName());
/*  486 */         doRegister(mbean, objectName);
/*  487 */         injectNotificationPublisherIfNecessary(managedResource, mbean, objectName);
/*      */       }
/*      */     
/*  490 */     } catch (JMException ex) {
/*  491 */       throw new UnableToRegisterMBeanException("Unable to register MBean [" + managedResource + "] with object name [" + objectName + "]", ex);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void unregisterManagedResource(ObjectName objectName) {
/*  498 */     Assert.notNull(objectName, "ObjectName must not be null");
/*  499 */     doUnregister(objectName);
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
/*      */   protected void registerBeans() {
/*  522 */     if (this.beans == null) {
/*  523 */       this.beans = new HashMap<String, Object>();
/*      */       
/*  525 */       if (this.autodetectMode == null) {
/*  526 */         this.autodetectMode = Integer.valueOf(3);
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/*  531 */     int mode = (this.autodetectMode != null) ? this.autodetectMode.intValue() : 0;
/*  532 */     if (mode != 0) {
/*  533 */       if (this.beanFactory == null) {
/*  534 */         throw new MBeanExportException("Cannot autodetect MBeans if not running in a BeanFactory");
/*      */       }
/*  536 */       if (mode == 1 || mode == 3) {
/*      */         
/*  538 */         this.logger.debug("Autodetecting user-defined JMX MBeans");
/*  539 */         autodetectMBeans();
/*      */       } 
/*      */       
/*  542 */       if ((mode == 2 || mode == 3) && this.assembler instanceof AutodetectCapableMBeanInfoAssembler)
/*      */       {
/*  544 */         autodetectBeans((AutodetectCapableMBeanInfoAssembler)this.assembler);
/*      */       }
/*      */     } 
/*      */     
/*  548 */     if (!this.beans.isEmpty()) {
/*  549 */       for (Map.Entry<String, Object> entry : this.beans.entrySet()) {
/*  550 */         registerBeanNameOrInstance(entry.getValue(), entry.getKey());
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
/*      */   protected boolean isBeanDefinitionLazyInit(ListableBeanFactory beanFactory, String beanName) {
/*  563 */     return (beanFactory instanceof ConfigurableListableBeanFactory && beanFactory.containsBeanDefinition(beanName) && ((ConfigurableListableBeanFactory)beanFactory)
/*  564 */       .getBeanDefinition(beanName).isLazyInit());
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
/*      */   protected ObjectName registerBeanNameOrInstance(Object mapValue, String beanKey) throws MBeanExportException {
/*      */     try {
/*  590 */       if (mapValue instanceof String)
/*      */       {
/*  592 */         if (this.beanFactory == null) {
/*  593 */           throw new MBeanExportException("Cannot resolve bean names if not running in a BeanFactory");
/*      */         }
/*  595 */         String beanName = (String)mapValue;
/*  596 */         if (isBeanDefinitionLazyInit(this.beanFactory, beanName)) {
/*  597 */           ObjectName objectName = registerLazyInit(beanName, beanKey);
/*  598 */           replaceNotificationListenerBeanNameKeysIfNecessary(beanName, objectName);
/*  599 */           return objectName;
/*      */         } 
/*      */         
/*  602 */         Object bean = this.beanFactory.getBean(beanName);
/*  603 */         if (bean != null) {
/*  604 */           ObjectName objectName = registerBeanInstance(bean, beanKey);
/*  605 */           replaceNotificationListenerBeanNameKeysIfNecessary(beanName, objectName);
/*  606 */           return objectName;
/*      */         }
/*      */       
/*      */       }
/*  610 */       else if (mapValue != null)
/*      */       {
/*  612 */         if (this.beanFactory != null) {
/*      */           
/*  614 */           Map<String, ?> beansOfSameType = this.beanFactory.getBeansOfType(mapValue.getClass(), false, this.allowEagerInit);
/*  615 */           for (Map.Entry<String, ?> entry : beansOfSameType.entrySet()) {
/*  616 */             if (entry.getValue() == mapValue) {
/*  617 */               String beanName = entry.getKey();
/*  618 */               ObjectName objectName = registerBeanInstance(mapValue, beanKey);
/*  619 */               replaceNotificationListenerBeanNameKeysIfNecessary(beanName, objectName);
/*  620 */               return objectName;
/*      */             } 
/*      */           } 
/*      */         } 
/*  624 */         return registerBeanInstance(mapValue, beanKey);
/*      */       }
/*      */     
/*  627 */     } catch (Throwable ex) {
/*  628 */       throw new UnableToRegisterMBeanException("Unable to register MBean [" + mapValue + "] with key '" + beanKey + "'", ex);
/*      */     } 
/*      */     
/*  631 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void replaceNotificationListenerBeanNameKeysIfNecessary(String beanName, ObjectName objectName) {
/*  642 */     if (this.notificationListeners != null) {
/*  643 */       for (NotificationListenerBean notificationListener : this.notificationListeners) {
/*  644 */         notificationListener.replaceObjectName(beanName, objectName);
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
/*      */   private ObjectName registerBeanInstance(Object bean, String beanKey) throws JMException {
/*  658 */     ObjectName objectName = getObjectName(bean, beanKey);
/*  659 */     Object mbeanToExpose = null;
/*  660 */     if (isMBean(bean.getClass())) {
/*  661 */       mbeanToExpose = bean;
/*      */     } else {
/*      */       
/*  664 */       DynamicMBean adaptedBean = adaptMBeanIfPossible(bean);
/*  665 */       if (adaptedBean != null) {
/*  666 */         mbeanToExpose = adaptedBean;
/*      */       }
/*      */     } 
/*      */     
/*  670 */     if (mbeanToExpose != null) {
/*  671 */       if (this.logger.isInfoEnabled()) {
/*  672 */         this.logger.info("Located MBean '" + beanKey + "': registering with JMX server as MBean [" + objectName + "]");
/*      */       }
/*      */       
/*  675 */       doRegister(mbeanToExpose, objectName);
/*      */     } else {
/*      */       
/*  678 */       if (this.logger.isInfoEnabled()) {
/*  679 */         this.logger.info("Located managed bean '" + beanKey + "': registering with JMX server as MBean [" + objectName + "]");
/*      */       }
/*      */       
/*  682 */       ModelMBean mbean = createAndConfigureMBean(bean, beanKey);
/*  683 */       doRegister(mbean, objectName);
/*  684 */       injectNotificationPublisherIfNecessary(bean, mbean, objectName);
/*      */     } 
/*      */     
/*  687 */     return objectName;
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
/*      */   private ObjectName registerLazyInit(String beanName, String beanKey) throws JMException {
/*  699 */     ProxyFactory proxyFactory = new ProxyFactory();
/*  700 */     proxyFactory.setProxyTargetClass(true);
/*  701 */     proxyFactory.setFrozen(true);
/*      */     
/*  703 */     if (isMBean(this.beanFactory.getType(beanName))) {
/*      */       
/*  705 */       LazyInitTargetSource lazyInitTargetSource = new LazyInitTargetSource();
/*  706 */       lazyInitTargetSource.setTargetBeanName(beanName);
/*  707 */       lazyInitTargetSource.setBeanFactory((BeanFactory)this.beanFactory);
/*  708 */       proxyFactory.setTargetSource((TargetSource)lazyInitTargetSource);
/*      */       
/*  710 */       Object object = proxyFactory.getProxy(this.beanClassLoader);
/*  711 */       ObjectName objectName1 = getObjectName(object, beanKey);
/*  712 */       if (this.logger.isDebugEnabled()) {
/*  713 */         this.logger.debug("Located MBean '" + beanKey + "': registering with JMX server as lazy-init MBean [" + objectName1 + "]");
/*      */       }
/*      */       
/*  716 */       doRegister(object, objectName1);
/*  717 */       return objectName1;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  722 */     NotificationPublisherAwareLazyTargetSource targetSource = new NotificationPublisherAwareLazyTargetSource();
/*  723 */     targetSource.setTargetBeanName(beanName);
/*  724 */     targetSource.setBeanFactory((BeanFactory)this.beanFactory);
/*  725 */     proxyFactory.setTargetSource((TargetSource)targetSource);
/*      */     
/*  727 */     Object proxy = proxyFactory.getProxy(this.beanClassLoader);
/*  728 */     ObjectName objectName = getObjectName(proxy, beanKey);
/*  729 */     if (this.logger.isDebugEnabled()) {
/*  730 */       this.logger.debug("Located simple bean '" + beanKey + "': registering with JMX server as lazy-init MBean [" + objectName + "]");
/*      */     }
/*      */     
/*  733 */     ModelMBean mbean = createAndConfigureMBean(proxy, beanKey);
/*  734 */     targetSource.setModelMBean(mbean);
/*  735 */     targetSource.setObjectName(objectName);
/*  736 */     doRegister(mbean, objectName);
/*  737 */     return objectName;
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
/*      */   protected ObjectName getObjectName(Object bean, String beanKey) throws MalformedObjectNameException {
/*  753 */     if (bean instanceof SelfNaming) {
/*  754 */       return ((SelfNaming)bean).getObjectName();
/*      */     }
/*      */     
/*  757 */     return this.namingStrategy.getObjectName(bean, beanKey);
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
/*      */   protected boolean isMBean(Class<?> beanClass) {
/*  772 */     return JmxUtils.isMBean(beanClass);
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
/*      */   protected DynamicMBean adaptMBeanIfPossible(Object bean) throws JMException {
/*  785 */     Class<?> targetClass = AopUtils.getTargetClass(bean);
/*  786 */     if (targetClass != bean.getClass()) {
/*  787 */       Class<?> ifc = JmxUtils.getMXBeanInterface(targetClass);
/*  788 */       if (ifc != null) {
/*  789 */         if (!ifc.isInstance(bean)) {
/*  790 */           throw new NotCompliantMBeanException("Managed bean [" + bean + "] has a target class with an MXBean interface but does not expose it in the proxy");
/*      */         }
/*      */         
/*  793 */         return new StandardMBean((T)bean, (Class)ifc, true);
/*      */       } 
/*      */       
/*  796 */       ifc = JmxUtils.getMBeanInterface(targetClass);
/*  797 */       if (ifc != null) {
/*  798 */         if (!ifc.isInstance(bean)) {
/*  799 */           throw new NotCompliantMBeanException("Managed bean [" + bean + "] has a target class with an MBean interface but does not expose it in the proxy");
/*      */         }
/*      */         
/*  802 */         return new StandardMBean((T)bean, (Class)ifc);
/*      */       } 
/*      */     } 
/*      */     
/*  806 */     return null;
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
/*      */   protected ModelMBean createAndConfigureMBean(Object managedResource, String beanKey) throws MBeanExportException {
/*      */     try {
/*  820 */       ModelMBean mbean = createModelMBean();
/*  821 */       mbean.setModelMBeanInfo(getMBeanInfo(managedResource, beanKey));
/*  822 */       mbean.setManagedResource(managedResource, "ObjectReference");
/*  823 */       return mbean;
/*      */     }
/*  825 */     catch (Throwable ex) {
/*  826 */       throw new MBeanExportException("Could not create ModelMBean for managed resource [" + managedResource + "] with key '" + beanKey + "'", ex);
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
/*      */   protected ModelMBean createModelMBean() throws MBeanException {
/*  840 */     return this.exposeManagedResourceClassLoader ? new SpringModelMBean() : new RequiredModelMBean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ModelMBeanInfo getMBeanInfo(Object managedBean, String beanKey) throws JMException {
/*  848 */     ModelMBeanInfo info = this.assembler.getMBeanInfo(managedBean, beanKey);
/*  849 */     if (this.logger.isWarnEnabled() && ObjectUtils.isEmpty((Object[])info.getAttributes()) && 
/*  850 */       ObjectUtils.isEmpty((Object[])info.getOperations())) {
/*  851 */       this.logger.warn("Bean with key '" + beanKey + "' has been registered as an MBean but has no exposed attributes or operations");
/*      */     }
/*      */     
/*  854 */     return info;
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
/*      */   private void autodetectBeans(final AutodetectCapableMBeanInfoAssembler assembler) {
/*  871 */     autodetect(new AutodetectCallback()
/*      */         {
/*      */           public boolean include(Class<?> beanClass, String beanName) {
/*  874 */             return assembler.includeBean(beanClass, beanName);
/*      */           }
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void autodetectMBeans() {
/*  884 */     autodetect(new AutodetectCallback()
/*      */         {
/*      */           public boolean include(Class<?> beanClass, String beanName) {
/*  887 */             return MBeanExporter.this.isMBean(beanClass);
/*      */           }
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void autodetect(AutodetectCallback callback) {
/*  900 */     Set<String> beanNames = new LinkedHashSet<String>(this.beanFactory.getBeanDefinitionCount());
/*  901 */     beanNames.addAll(Arrays.asList(this.beanFactory.getBeanDefinitionNames()));
/*  902 */     if (this.beanFactory instanceof ConfigurableBeanFactory) {
/*  903 */       beanNames.addAll(Arrays.asList(((ConfigurableBeanFactory)this.beanFactory).getSingletonNames()));
/*      */     }
/*  905 */     for (String beanName : beanNames) {
/*  906 */       if (!isExcluded(beanName) && !isBeanDefinitionAbstract(this.beanFactory, beanName)) {
/*      */         try {
/*  908 */           Class<?> beanClass = this.beanFactory.getType(beanName);
/*  909 */           if (beanClass != null && callback.include(beanClass, beanName)) {
/*  910 */             boolean lazyInit = isBeanDefinitionLazyInit(this.beanFactory, beanName);
/*  911 */             Object beanInstance = !lazyInit ? this.beanFactory.getBean(beanName) : null;
/*  912 */             if (!ScopedProxyUtils.isScopedTarget(beanName) && !this.beans.containsValue(beanName) && (beanInstance == null || 
/*      */               
/*  914 */               !CollectionUtils.containsInstance(this.beans.values(), beanInstance))) {
/*      */               
/*  916 */               this.beans.put(beanName, (beanInstance != null) ? beanInstance : beanName);
/*  917 */               if (this.logger.isInfoEnabled()) {
/*  918 */                 this.logger.info("Bean with name '" + beanName + "' has been autodetected for JMX exposure");
/*      */               }
/*      */               continue;
/*      */             } 
/*  922 */             if (this.logger.isDebugEnabled()) {
/*  923 */               this.logger.debug("Bean with name '" + beanName + "' is already registered for JMX exposure");
/*      */             }
/*      */           }
/*      */         
/*      */         }
/*  928 */         catch (CannotLoadBeanClassException ex) {
/*  929 */           if (this.allowEagerInit) {
/*  930 */             throw ex;
/*      */           }
/*      */         } 
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean isExcluded(String beanName) {
/*  942 */     return (this.excludedBeans.contains(beanName) || (beanName
/*  943 */       .startsWith("&") && this.excludedBeans
/*  944 */       .contains(beanName.substring("&".length()))));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean isBeanDefinitionAbstract(ListableBeanFactory beanFactory, String beanName) {
/*  951 */     return (beanFactory instanceof ConfigurableListableBeanFactory && beanFactory.containsBeanDefinition(beanName) && ((ConfigurableListableBeanFactory)beanFactory)
/*  952 */       .getBeanDefinition(beanName).isAbstract());
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
/*      */   private void injectNotificationPublisherIfNecessary(Object managedResource, ModelMBean modelMBean, ObjectName objectName) {
/*  967 */     if (managedResource instanceof NotificationPublisherAware) {
/*  968 */       ((NotificationPublisherAware)managedResource).setNotificationPublisher((NotificationPublisher)new ModelMBeanNotificationPublisher(modelMBean, objectName, managedResource));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void registerNotificationListeners() throws MBeanExportException {
/*  978 */     if (this.notificationListeners != null) {
/*  979 */       for (NotificationListenerBean bean : this.notificationListeners) {
/*      */         try {
/*  981 */           ObjectName[] mappedObjectNames = bean.getResolvedObjectNames();
/*  982 */           if (mappedObjectNames == null)
/*      */           {
/*  984 */             mappedObjectNames = getRegisteredObjectNames();
/*      */           }
/*  986 */           if (this.registeredNotificationListeners.put(bean, mappedObjectNames) == null) {
/*  987 */             for (ObjectName mappedObjectName : mappedObjectNames) {
/*  988 */               this.server.addNotificationListener(mappedObjectName, bean.getNotificationListener(), bean
/*  989 */                   .getNotificationFilter(), bean.getHandback());
/*      */             }
/*      */           }
/*      */         }
/*  993 */         catch (Throwable ex) {
/*  994 */           throw new MBeanExportException("Unable to register NotificationListener", ex);
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void unregisterNotificationListeners() {
/* 1005 */     for (Map.Entry<NotificationListenerBean, ObjectName[]> entry : this.registeredNotificationListeners.entrySet()) {
/* 1006 */       NotificationListenerBean bean = entry.getKey();
/* 1007 */       ObjectName[] mappedObjectNames = entry.getValue();
/* 1008 */       for (ObjectName mappedObjectName : mappedObjectNames) {
/*      */         try {
/* 1010 */           this.server.removeNotificationListener(mappedObjectName, bean.getNotificationListener(), bean
/* 1011 */               .getNotificationFilter(), bean.getHandback());
/*      */         }
/* 1013 */         catch (Throwable ex) {
/* 1014 */           if (this.logger.isDebugEnabled()) {
/* 1015 */             this.logger.debug("Unable to unregister NotificationListener", ex);
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/* 1020 */     this.registeredNotificationListeners.clear();
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
/*      */   protected void onRegister(ObjectName objectName) {
/* 1035 */     notifyListenersOfRegistration(objectName);
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
/*      */   protected void onUnregister(ObjectName objectName) {
/* 1050 */     notifyListenersOfUnregistration(objectName);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void notifyListenersOfRegistration(ObjectName objectName) {
/* 1059 */     if (this.listeners != null) {
/* 1060 */       for (MBeanExporterListener listener : this.listeners) {
/* 1061 */         listener.mbeanRegistered(objectName);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void notifyListenersOfUnregistration(ObjectName objectName) {
/* 1071 */     if (this.listeners != null) {
/* 1072 */       for (MBeanExporterListener listener : this.listeners) {
/* 1073 */         listener.mbeanUnregistered(objectName);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static interface AutodetectCallback
/*      */   {
/*      */     boolean include(Class<?> param1Class, String param1String);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private class NotificationPublisherAwareLazyTargetSource
/*      */     extends LazyInitTargetSource
/*      */   {
/*      */     private ModelMBean modelMBean;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private ObjectName objectName;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private NotificationPublisherAwareLazyTargetSource() {}
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setModelMBean(ModelMBean modelMBean) {
/* 1111 */       this.modelMBean = modelMBean;
/*      */     }
/*      */     
/*      */     public void setObjectName(ObjectName objectName) {
/* 1115 */       this.objectName = objectName;
/*      */     }
/*      */ 
/*      */     
/*      */     public Object getTarget() {
/*      */       try {
/* 1121 */         return super.getTarget();
/*      */       }
/* 1123 */       catch (RuntimeException ex) {
/* 1124 */         if (this.logger.isWarnEnabled()) {
/* 1125 */           this.logger.warn("Failed to retrieve target for JMX-exposed bean [" + this.objectName + "]: " + ex);
/*      */         }
/* 1127 */         throw ex;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     protected void postProcessTargetObject(Object targetObject) {
/* 1133 */       MBeanExporter.this.injectNotificationPublisherIfNecessary(targetObject, this.modelMBean, this.objectName);
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\jmx\export\MBeanExporter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */