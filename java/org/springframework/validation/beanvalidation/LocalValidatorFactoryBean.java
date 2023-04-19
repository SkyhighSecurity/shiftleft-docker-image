/*     */ package org.springframework.validation.beanvalidation;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import javax.validation.Configuration;
/*     */ import javax.validation.ConstraintValidatorFactory;
/*     */ import javax.validation.MessageInterpolator;
/*     */ import javax.validation.TraversableResolver;
/*     */ import javax.validation.Validation;
/*     */ import javax.validation.ValidationException;
/*     */ import javax.validation.ValidationProviderResolver;
/*     */ import javax.validation.Validator;
/*     */ import javax.validation.ValidatorContext;
/*     */ import javax.validation.ValidatorFactory;
/*     */ import javax.validation.bootstrap.GenericBootstrap;
/*     */ import javax.validation.bootstrap.ProviderSpecificBootstrap;
/*     */ import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
/*     */ import org.springframework.beans.factory.DisposableBean;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.context.ApplicationContextAware;
/*     */ import org.springframework.context.MessageSource;
/*     */ import org.springframework.core.DefaultParameterNameDiscoverer;
/*     */ import org.springframework.core.ParameterNameDiscoverer;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.util.ReflectionUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LocalValidatorFactoryBean
/*     */   extends SpringValidatorAdapter
/*     */   implements ValidatorFactory, ApplicationContextAware, InitializingBean, DisposableBean
/*     */ {
/*  94 */   private static final Method closeMethod = ClassUtils.getMethodIfAvailable(ValidatorFactory.class, "close", new Class[0]);
/*     */ 
/*     */   
/*     */   private Class providerClass;
/*     */ 
/*     */   
/*     */   private ValidationProviderResolver validationProviderResolver;
/*     */   
/*     */   private MessageInterpolator messageInterpolator;
/*     */   
/*     */   private TraversableResolver traversableResolver;
/*     */   
/*     */   private ConstraintValidatorFactory constraintValidatorFactory;
/*     */   
/* 108 */   private ParameterNameDiscoverer parameterNameDiscoverer = (ParameterNameDiscoverer)new DefaultParameterNameDiscoverer();
/*     */   
/*     */   private Resource[] mappingLocations;
/*     */   
/* 112 */   private final Map<String, String> validationPropertyMap = new HashMap<String, String>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ApplicationContext applicationContext;
/*     */ 
/*     */ 
/*     */   
/*     */   private ValidatorFactory validatorFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProviderClass(Class providerClass) {
/* 127 */     this.providerClass = providerClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValidationProviderResolver(ValidationProviderResolver validationProviderResolver) {
/* 136 */     this.validationProviderResolver = validationProviderResolver;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMessageInterpolator(MessageInterpolator messageInterpolator) {
/* 144 */     this.messageInterpolator = messageInterpolator;
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
/*     */   public void setValidationMessageSource(MessageSource messageSource) {
/* 167 */     this.messageInterpolator = HibernateValidatorDelegate.buildMessageInterpolator(messageSource);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTraversableResolver(TraversableResolver traversableResolver) {
/* 175 */     this.traversableResolver = traversableResolver;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setConstraintValidatorFactory(ConstraintValidatorFactory constraintValidatorFactory) {
/* 184 */     this.constraintValidatorFactory = constraintValidatorFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setParameterNameDiscoverer(ParameterNameDiscoverer parameterNameDiscoverer) {
/* 193 */     this.parameterNameDiscoverer = parameterNameDiscoverer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMappingLocations(Resource... mappingLocations) {
/* 200 */     this.mappingLocations = mappingLocations;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValidationProperties(Properties jpaProperties) {
/* 210 */     CollectionUtils.mergePropertiesIntoMap(jpaProperties, this.validationPropertyMap);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValidationPropertyMap(Map<String, String> validationProperties) {
/* 219 */     if (validationProperties != null) {
/* 220 */       this.validationPropertyMap.putAll(validationProperties);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, String> getValidationPropertyMap() {
/* 230 */     return this.validationPropertyMap;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setApplicationContext(ApplicationContext applicationContext) {
/* 235 */     this.applicationContext = applicationContext;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() {
/*     */     Configuration<?> configuration;
/* 243 */     if (this.providerClass != null) {
/* 244 */       ProviderSpecificBootstrap bootstrap = Validation.byProvider(this.providerClass);
/* 245 */       if (this.validationProviderResolver != null) {
/* 246 */         bootstrap = bootstrap.providerResolver(this.validationProviderResolver);
/*     */       }
/* 248 */       configuration = bootstrap.configure();
/*     */     } else {
/*     */       
/* 251 */       GenericBootstrap bootstrap = Validation.byDefaultProvider();
/* 252 */       if (this.validationProviderResolver != null) {
/* 253 */         bootstrap = bootstrap.providerResolver(this.validationProviderResolver);
/*     */       }
/* 255 */       configuration = bootstrap.configure();
/*     */     } 
/*     */ 
/*     */     
/* 259 */     if (this.applicationContext != null) {
/*     */       try {
/* 261 */         Method eclMethod = configuration.getClass().getMethod("externalClassLoader", new Class[] { ClassLoader.class });
/* 262 */         ReflectionUtils.invokeMethod(eclMethod, configuration, new Object[] { this.applicationContext.getClassLoader() });
/*     */       }
/* 264 */       catch (NoSuchMethodException noSuchMethodException) {}
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 269 */     MessageInterpolator targetInterpolator = this.messageInterpolator;
/* 270 */     if (targetInterpolator == null) {
/* 271 */       targetInterpolator = configuration.getDefaultMessageInterpolator();
/*     */     }
/* 273 */     configuration.messageInterpolator(new LocaleContextMessageInterpolator(targetInterpolator));
/*     */     
/* 275 */     if (this.traversableResolver != null) {
/* 276 */       configuration.traversableResolver(this.traversableResolver);
/*     */     }
/*     */     
/* 279 */     ConstraintValidatorFactory targetConstraintValidatorFactory = this.constraintValidatorFactory;
/* 280 */     if (targetConstraintValidatorFactory == null && this.applicationContext != null)
/*     */     {
/* 282 */       targetConstraintValidatorFactory = new SpringConstraintValidatorFactory(this.applicationContext.getAutowireCapableBeanFactory());
/*     */     }
/* 284 */     if (targetConstraintValidatorFactory != null) {
/* 285 */       configuration.constraintValidatorFactory(targetConstraintValidatorFactory);
/*     */     }
/*     */     
/* 288 */     if (this.parameterNameDiscoverer != null) {
/* 289 */       configureParameterNameProviderIfPossible(configuration);
/*     */     }
/*     */     
/* 292 */     if (this.mappingLocations != null) {
/* 293 */       for (Resource location : this.mappingLocations) {
/*     */         try {
/* 295 */           configuration.addMapping(location.getInputStream());
/*     */         }
/* 297 */         catch (IOException ex) {
/* 298 */           throw new IllegalStateException("Cannot read mapping resource: " + location);
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/* 303 */     for (Map.Entry<String, String> entry : this.validationPropertyMap.entrySet()) {
/* 304 */       configuration.addProperty(entry.getKey(), entry.getValue());
/*     */     }
/*     */ 
/*     */     
/* 308 */     postProcessConfiguration(configuration);
/*     */     
/* 310 */     this.validatorFactory = configuration.buildValidatorFactory();
/* 311 */     setTargetValidator(this.validatorFactory.getValidator());
/*     */   }
/*     */ 
/*     */   
/*     */   private void configureParameterNameProviderIfPossible(Configuration<?> configuration) {
/*     */     try {
/* 317 */       Class<?> parameterNameProviderClass = ClassUtils.forName("javax.validation.ParameterNameProvider", getClass().getClassLoader());
/*     */       
/* 319 */       Method parameterNameProviderMethod = Configuration.class.getMethod("parameterNameProvider", new Class[] { parameterNameProviderClass });
/* 320 */       final Object defaultProvider = ReflectionUtils.invokeMethod(Configuration.class
/* 321 */           .getMethod("getDefaultParameterNameProvider", new Class[0]), configuration);
/* 322 */       final ParameterNameDiscoverer discoverer = this.parameterNameDiscoverer;
/* 323 */       Object parameterNameProvider = Proxy.newProxyInstance(getClass().getClassLoader(), new Class[] { parameterNameProviderClass }, new InvocationHandler()
/*     */           {
/*     */             public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
/*     */             {
/* 327 */               if (method.getName().equals("getParameterNames")) {
/* 328 */                 String[] result = null;
/* 329 */                 if (args[0] instanceof Constructor) {
/* 330 */                   result = discoverer.getParameterNames((Constructor)args[0]);
/*     */                 }
/* 332 */                 else if (args[0] instanceof Method) {
/* 333 */                   result = discoverer.getParameterNames((Method)args[0]);
/*     */                 } 
/* 335 */                 if (result != null) {
/* 336 */                   return Arrays.asList(result);
/*     */                 }
/*     */                 
/*     */                 try {
/* 340 */                   return method.invoke(defaultProvider, args);
/*     */                 }
/* 342 */                 catch (InvocationTargetException ex) {
/* 343 */                   throw ex.getTargetException();
/*     */                 } 
/*     */               } 
/*     */ 
/*     */ 
/*     */               
/*     */               try {
/* 350 */                 return method.invoke(this, args);
/*     */               }
/* 352 */               catch (InvocationTargetException ex) {
/* 353 */                 throw ex.getTargetException();
/*     */               } 
/*     */             }
/*     */           });
/*     */       
/* 358 */       ReflectionUtils.invokeMethod(parameterNameProviderMethod, configuration, new Object[] { parameterNameProvider });
/*     */     
/*     */     }
/* 361 */     catch (Throwable throwable) {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void postProcessConfiguration(Configuration<?> configuration) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Validator getValidator() {
/* 379 */     Assert.notNull(this.validatorFactory, "No target ValidatorFactory set");
/* 380 */     return this.validatorFactory.getValidator();
/*     */   }
/*     */ 
/*     */   
/*     */   public ValidatorContext usingContext() {
/* 385 */     Assert.notNull(this.validatorFactory, "No target ValidatorFactory set");
/* 386 */     return this.validatorFactory.usingContext();
/*     */   }
/*     */ 
/*     */   
/*     */   public MessageInterpolator getMessageInterpolator() {
/* 391 */     Assert.notNull(this.validatorFactory, "No target ValidatorFactory set");
/* 392 */     return this.validatorFactory.getMessageInterpolator();
/*     */   }
/*     */ 
/*     */   
/*     */   public TraversableResolver getTraversableResolver() {
/* 397 */     Assert.notNull(this.validatorFactory, "No target ValidatorFactory set");
/* 398 */     return this.validatorFactory.getTraversableResolver();
/*     */   }
/*     */ 
/*     */   
/*     */   public ConstraintValidatorFactory getConstraintValidatorFactory() {
/* 403 */     Assert.notNull(this.validatorFactory, "No target ValidatorFactory set");
/* 404 */     return this.validatorFactory.getConstraintValidatorFactory();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T unwrap(Class<T> type) {
/* 410 */     if (type == null || !ValidatorFactory.class.isAssignableFrom(type)) {
/*     */       try {
/* 412 */         return super.unwrap(type);
/*     */       }
/* 414 */       catch (ValidationException validationException) {}
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/* 419 */       return (T)this.validatorFactory.unwrap(type);
/*     */     }
/* 421 */     catch (ValidationException ex) {
/*     */       
/* 423 */       if (ValidatorFactory.class == type) {
/* 424 */         return (T)this.validatorFactory;
/*     */       }
/* 426 */       throw ex;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/* 432 */     if (closeMethod != null && this.validatorFactory != null) {
/* 433 */       ReflectionUtils.invokeMethod(closeMethod, this.validatorFactory);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void destroy() {
/* 439 */     close();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class HibernateValidatorDelegate
/*     */   {
/*     */     public static MessageInterpolator buildMessageInterpolator(MessageSource messageSource) {
/* 449 */       return (MessageInterpolator)new ResourceBundleMessageInterpolator(new MessageSourceResourceBundleLocator(messageSource));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\validation\beanvalidation\LocalValidatorFactoryBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */