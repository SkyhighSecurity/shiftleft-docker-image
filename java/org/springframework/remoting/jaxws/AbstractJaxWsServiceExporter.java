/*     */ package org.springframework.remoting.jaxws;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.Executor;
/*     */ import javax.jws.WebService;
/*     */ import javax.xml.ws.Endpoint;
/*     */ import javax.xml.ws.WebServiceFeature;
/*     */ import javax.xml.ws.WebServiceProvider;
/*     */ import org.springframework.beans.BeanUtils;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryAware;
/*     */ import org.springframework.beans.factory.CannotLoadBeanClassException;
/*     */ import org.springframework.beans.factory.DisposableBean;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.beans.factory.ListableBeanFactory;
/*     */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
/*     */ import org.springframework.lang.UsesJava7;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractJaxWsServiceExporter
/*     */   implements BeanFactoryAware, InitializingBean, DisposableBean
/*     */ {
/*     */   private Map<String, Object> endpointProperties;
/*     */   private Executor executor;
/*     */   private String bindingType;
/*     */   private WebServiceFeature[] endpointFeatures;
/*     */   private Object[] webServiceFeatures;
/*     */   private ListableBeanFactory beanFactory;
/*  70 */   private final Set<Endpoint> publishedEndpoints = new LinkedHashSet<Endpoint>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEndpointProperties(Map<String, Object> endpointProperties) {
/*  81 */     this.endpointProperties = endpointProperties;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExecutor(Executor executor) {
/*  90 */     this.executor = executor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBindingType(String bindingType) {
/*  98 */     this.bindingType = bindingType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEndpointFeatures(WebServiceFeature... endpointFeatures) {
/* 107 */     this.endpointFeatures = endpointFeatures;
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
/*     */   @Deprecated
/*     */   public void setWebServiceFeatures(Object[] webServiceFeatures) {
/* 121 */     this.webServiceFeatures = webServiceFeatures;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBeanFactory(BeanFactory beanFactory) {
/* 129 */     if (!(beanFactory instanceof ListableBeanFactory)) {
/* 130 */       throw new IllegalStateException(getClass().getSimpleName() + " requires a ListableBeanFactory");
/*     */     }
/* 132 */     this.beanFactory = (ListableBeanFactory)beanFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() throws Exception {
/* 142 */     publishEndpoints();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void publishEndpoints() {
/* 151 */     Set<String> beanNames = new LinkedHashSet<String>(this.beanFactory.getBeanDefinitionCount());
/* 152 */     beanNames.addAll(Arrays.asList(this.beanFactory.getBeanDefinitionNames()));
/* 153 */     if (this.beanFactory instanceof ConfigurableBeanFactory) {
/* 154 */       beanNames.addAll(Arrays.asList(((ConfigurableBeanFactory)this.beanFactory).getSingletonNames()));
/*     */     }
/* 156 */     for (String beanName : beanNames) {
/*     */       try {
/* 158 */         Class<?> type = this.beanFactory.getType(beanName);
/* 159 */         if (type != null && !type.isInterface()) {
/* 160 */           WebService wsAnnotation = type.<WebService>getAnnotation(WebService.class);
/* 161 */           WebServiceProvider wsProviderAnnotation = type.<WebServiceProvider>getAnnotation(WebServiceProvider.class);
/* 162 */           if (wsAnnotation != null || wsProviderAnnotation != null) {
/* 163 */             Endpoint endpoint = createEndpoint(this.beanFactory.getBean(beanName));
/* 164 */             if (this.endpointProperties != null) {
/* 165 */               endpoint.setProperties(this.endpointProperties);
/*     */             }
/* 167 */             if (this.executor != null) {
/* 168 */               endpoint.setExecutor(this.executor);
/*     */             }
/* 170 */             if (wsAnnotation != null) {
/* 171 */               publishEndpoint(endpoint, wsAnnotation);
/*     */             } else {
/*     */               
/* 174 */               publishEndpoint(endpoint, wsProviderAnnotation);
/*     */             } 
/* 176 */             this.publishedEndpoints.add(endpoint);
/*     */           }
/*     */         
/*     */         } 
/* 180 */       } catch (CannotLoadBeanClassException cannotLoadBeanClassException) {}
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
/*     */   @UsesJava7
/*     */   protected Endpoint createEndpoint(Object bean) {
/* 195 */     if (this.endpointFeatures != null || this.webServiceFeatures != null) {
/* 196 */       WebServiceFeature[] endpointFeaturesToUse = this.endpointFeatures;
/* 197 */       if (endpointFeaturesToUse == null) {
/* 198 */         endpointFeaturesToUse = new WebServiceFeature[this.webServiceFeatures.length];
/* 199 */         for (int i = 0; i < this.webServiceFeatures.length; i++) {
/* 200 */           endpointFeaturesToUse[i] = convertWebServiceFeature(this.webServiceFeatures[i]);
/*     */         }
/*     */       } 
/* 203 */       return Endpoint.create(this.bindingType, bean, endpointFeaturesToUse);
/*     */     } 
/*     */     
/* 206 */     return Endpoint.create(this.bindingType, bean);
/*     */   }
/*     */ 
/*     */   
/*     */   private WebServiceFeature convertWebServiceFeature(Object feature) {
/* 211 */     Assert.notNull(feature, "WebServiceFeature specification object must not be null");
/* 212 */     if (feature instanceof WebServiceFeature) {
/* 213 */       return (WebServiceFeature)feature;
/*     */     }
/* 215 */     if (feature instanceof Class) {
/* 216 */       return (WebServiceFeature)BeanUtils.instantiate((Class)feature);
/*     */     }
/* 218 */     if (feature instanceof String) {
/*     */       try {
/* 220 */         Class<?> featureClass = getBeanClassLoader().loadClass((String)feature);
/* 221 */         return (WebServiceFeature)BeanUtils.instantiate(featureClass);
/*     */       }
/* 223 */       catch (ClassNotFoundException ex) {
/* 224 */         throw new IllegalArgumentException("Could not load WebServiceFeature class [" + feature + "]");
/*     */       } 
/*     */     }
/*     */     
/* 228 */     throw new IllegalArgumentException("Unknown WebServiceFeature specification type: " + feature.getClass());
/*     */   }
/*     */ 
/*     */   
/*     */   private ClassLoader getBeanClassLoader() {
/* 233 */     return (this.beanFactory instanceof ConfigurableBeanFactory) ? ((ConfigurableBeanFactory)this.beanFactory)
/* 234 */       .getBeanClassLoader() : ClassUtils.getDefaultClassLoader();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void publishEndpoint(Endpoint paramEndpoint, WebService paramWebService);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void publishEndpoint(Endpoint paramEndpoint, WebServiceProvider paramWebServiceProvider);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void destroy() {
/* 258 */     for (Endpoint endpoint : this.publishedEndpoints)
/* 259 */       endpoint.stop(); 
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\remoting\jaxws\AbstractJaxWsServiceExporter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */