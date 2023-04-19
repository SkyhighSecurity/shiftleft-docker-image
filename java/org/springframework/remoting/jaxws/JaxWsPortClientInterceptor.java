/*     */ package org.springframework.remoting.jaxws;
/*     */ 
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.jws.WebService;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.ws.BindingProvider;
/*     */ import javax.xml.ws.ProtocolException;
/*     */ import javax.xml.ws.Service;
/*     */ import javax.xml.ws.WebServiceException;
/*     */ import javax.xml.ws.WebServiceFeature;
/*     */ import javax.xml.ws.soap.SOAPFaultException;
/*     */ import org.aopalliance.intercept.MethodInterceptor;
/*     */ import org.aopalliance.intercept.MethodInvocation;
/*     */ import org.springframework.aop.support.AopUtils;
/*     */ import org.springframework.beans.BeanUtils;
/*     */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.remoting.RemoteAccessException;
/*     */ import org.springframework.remoting.RemoteConnectFailureException;
/*     */ import org.springframework.remoting.RemoteLookupFailureException;
/*     */ import org.springframework.remoting.RemoteProxyFailureException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JaxWsPortClientInterceptor
/*     */   extends LocalJaxWsServiceFactory
/*     */   implements MethodInterceptor, BeanClassLoaderAware, InitializingBean
/*     */ {
/*     */   private Service jaxWsService;
/*     */   private String portName;
/*     */   private String username;
/*     */   private String password;
/*     */   private String endpointAddress;
/*     */   private boolean maintainSession;
/*     */   private boolean useSoapAction;
/*     */   private String soapActionUri;
/*     */   private Map<String, Object> customProperties;
/*     */   private WebServiceFeature[] portFeatures;
/*     */   private Object[] webServiceFeatures;
/*     */   private Class<?> serviceInterface;
/*     */   private boolean lookupServiceOnStartup = true;
/*  95 */   private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
/*     */   
/*     */   private QName portQName;
/*     */   
/*     */   private Object portStub;
/*     */   
/* 101 */   private final Object preparationMonitor = new Object();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setJaxWsService(Service jaxWsService) {
/* 114 */     this.jaxWsService = jaxWsService;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Service getJaxWsService() {
/* 121 */     return this.jaxWsService;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPortName(String portName) {
/* 129 */     this.portName = portName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPortName() {
/* 136 */     return this.portName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUsername(String username) {
/* 144 */     this.username = username;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getUsername() {
/* 151 */     return this.username;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPassword(String password) {
/* 159 */     this.password = password;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPassword() {
/* 166 */     return this.password;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEndpointAddress(String endpointAddress) {
/* 174 */     this.endpointAddress = endpointAddress;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getEndpointAddress() {
/* 181 */     return this.endpointAddress;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaintainSession(boolean maintainSession) {
/* 189 */     this.maintainSession = maintainSession;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isMaintainSession() {
/* 196 */     return this.maintainSession;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUseSoapAction(boolean useSoapAction) {
/* 204 */     this.useSoapAction = useSoapAction;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isUseSoapAction() {
/* 211 */     return this.useSoapAction;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSoapActionUri(String soapActionUri) {
/* 219 */     this.soapActionUri = soapActionUri;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSoapActionUri() {
/* 226 */     return this.soapActionUri;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCustomProperties(Map<String, Object> customProperties) {
/* 236 */     this.customProperties = customProperties;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, Object> getCustomProperties() {
/* 247 */     if (this.customProperties == null) {
/* 248 */       this.customProperties = new HashMap<String, Object>();
/*     */     }
/* 250 */     return this.customProperties;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addCustomProperty(String name, Object value) {
/* 260 */     getCustomProperties().put(name, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPortFeatures(WebServiceFeature... features) {
/* 271 */     this.portFeatures = features;
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
/*     */   @Deprecated
/*     */   public void setWebServiceFeatures(Object[] webServiceFeatures) {
/* 287 */     this.webServiceFeatures = webServiceFeatures;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setServiceInterface(Class<?> serviceInterface) {
/* 294 */     if (serviceInterface != null && !serviceInterface.isInterface()) {
/* 295 */       throw new IllegalArgumentException("'serviceInterface' must be an interface");
/*     */     }
/* 297 */     this.serviceInterface = serviceInterface;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?> getServiceInterface() {
/* 304 */     return this.serviceInterface;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLookupServiceOnStartup(boolean lookupServiceOnStartup) {
/* 314 */     this.lookupServiceOnStartup = lookupServiceOnStartup;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBeanClassLoader(ClassLoader classLoader) {
/* 325 */     this.beanClassLoader = classLoader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ClassLoader getBeanClassLoader() {
/* 332 */     return this.beanClassLoader;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() {
/* 338 */     if (this.lookupServiceOnStartup) {
/* 339 */       prepare();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void prepare() {
/* 347 */     Class<?> ifc = getServiceInterface();
/* 348 */     if (ifc == null) {
/* 349 */       throw new IllegalArgumentException("Property 'serviceInterface' is required");
/*     */     }
/* 351 */     WebService ann = ifc.<WebService>getAnnotation(WebService.class);
/* 352 */     if (ann != null) {
/* 353 */       applyDefaultsFromAnnotation(ann);
/*     */     }
/* 355 */     Service serviceToUse = getJaxWsService();
/* 356 */     if (serviceToUse == null) {
/* 357 */       serviceToUse = createJaxWsService();
/*     */     }
/* 359 */     this.portQName = getQName((getPortName() != null) ? getPortName() : getServiceInterface().getName());
/* 360 */     Object stub = getPortStub(serviceToUse, (getPortName() != null) ? this.portQName : null);
/* 361 */     preparePortStub(stub);
/* 362 */     this.portStub = stub;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void applyDefaultsFromAnnotation(WebService ann) {
/* 373 */     if (getWsdlDocumentUrl() == null) {
/* 374 */       String wsdl = ann.wsdlLocation();
/* 375 */       if (StringUtils.hasText(wsdl)) {
/*     */         try {
/* 377 */           setWsdlDocumentUrl(new URL(wsdl));
/*     */         }
/* 379 */         catch (MalformedURLException ex) {
/* 380 */           throw new IllegalStateException("Encountered invalid @Service wsdlLocation value [" + wsdl + "]", ex);
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 385 */     if (getNamespaceUri() == null) {
/* 386 */       String ns = ann.targetNamespace();
/* 387 */       if (StringUtils.hasText(ns)) {
/* 388 */         setNamespaceUri(ns);
/*     */       }
/*     */     } 
/* 391 */     if (getServiceName() == null) {
/* 392 */       String sn = ann.serviceName();
/* 393 */       if (StringUtils.hasText(sn)) {
/* 394 */         setServiceName(sn);
/*     */       }
/*     */     } 
/* 397 */     if (getPortName() == null) {
/* 398 */       String pn = ann.portName();
/* 399 */       if (StringUtils.hasText(pn)) {
/* 400 */         setPortName(pn);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isPrepared() {
/* 410 */     synchronized (this.preparationMonitor) {
/* 411 */       return (this.portStub != null);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final QName getPortQName() {
/* 421 */     return this.portQName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object getPortStub(Service service, QName portQName) {
/* 432 */     if (this.portFeatures != null || this.webServiceFeatures != null) {
/* 433 */       WebServiceFeature[] portFeaturesToUse = this.portFeatures;
/* 434 */       if (portFeaturesToUse == null) {
/* 435 */         portFeaturesToUse = new WebServiceFeature[this.webServiceFeatures.length];
/* 436 */         for (int i = 0; i < this.webServiceFeatures.length; i++) {
/* 437 */           portFeaturesToUse[i] = convertWebServiceFeature(this.webServiceFeatures[i]);
/*     */         }
/*     */       } 
/* 440 */       return (portQName != null) ? service.getPort(portQName, getServiceInterface(), portFeaturesToUse) : service
/* 441 */         .getPort(getServiceInterface(), portFeaturesToUse);
/*     */     } 
/*     */     
/* 444 */     return (portQName != null) ? service.getPort(portQName, getServiceInterface()) : service
/* 445 */       .getPort(getServiceInterface());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private WebServiceFeature convertWebServiceFeature(Object feature) {
/* 456 */     Assert.notNull(feature, "WebServiceFeature specification object must not be null");
/* 457 */     if (feature instanceof WebServiceFeature) {
/* 458 */       return (WebServiceFeature)feature;
/*     */     }
/* 460 */     if (feature instanceof Class) {
/* 461 */       return (WebServiceFeature)BeanUtils.instantiate((Class)feature);
/*     */     }
/* 463 */     if (feature instanceof String) {
/*     */       try {
/* 465 */         Class<?> featureClass = getBeanClassLoader().loadClass((String)feature);
/* 466 */         return (WebServiceFeature)BeanUtils.instantiate(featureClass);
/*     */       }
/* 468 */       catch (ClassNotFoundException ex) {
/* 469 */         throw new IllegalArgumentException("Could not load WebServiceFeature class [" + feature + "]");
/*     */       } 
/*     */     }
/*     */     
/* 473 */     throw new IllegalArgumentException("Unknown WebServiceFeature specification type: " + feature.getClass());
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
/*     */   protected void preparePortStub(Object stub) {
/* 488 */     Map<String, Object> stubProperties = new HashMap<String, Object>();
/* 489 */     String username = getUsername();
/* 490 */     if (username != null) {
/* 491 */       stubProperties.put("javax.xml.ws.security.auth.username", username);
/*     */     }
/* 493 */     String password = getPassword();
/* 494 */     if (password != null) {
/* 495 */       stubProperties.put("javax.xml.ws.security.auth.password", password);
/*     */     }
/* 497 */     String endpointAddress = getEndpointAddress();
/* 498 */     if (endpointAddress != null) {
/* 499 */       stubProperties.put("javax.xml.ws.service.endpoint.address", endpointAddress);
/*     */     }
/* 501 */     if (isMaintainSession()) {
/* 502 */       stubProperties.put("javax.xml.ws.session.maintain", Boolean.TRUE);
/*     */     }
/* 504 */     if (isUseSoapAction()) {
/* 505 */       stubProperties.put("javax.xml.ws.soap.http.soapaction.use", Boolean.TRUE);
/*     */     }
/* 507 */     String soapActionUri = getSoapActionUri();
/* 508 */     if (soapActionUri != null) {
/* 509 */       stubProperties.put("javax.xml.ws.soap.http.soapaction.uri", soapActionUri);
/*     */     }
/* 511 */     stubProperties.putAll(getCustomProperties());
/* 512 */     if (!stubProperties.isEmpty()) {
/* 513 */       if (!(stub instanceof BindingProvider)) {
/* 514 */         throw new RemoteLookupFailureException("Port stub of class [" + stub.getClass().getName() + "] is not a customizable JAX-WS stub: it does not implement interface [javax.xml.ws.BindingProvider]");
/*     */       }
/*     */       
/* 517 */       ((BindingProvider)stub).getRequestContext().putAll(stubProperties);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object getPortStub() {
/* 526 */     return this.portStub;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object invoke(MethodInvocation invocation) throws Throwable {
/* 532 */     if (AopUtils.isToStringMethod(invocation.getMethod())) {
/* 533 */       return "JAX-WS proxy for port [" + getPortName() + "] of service [" + getServiceName() + "]";
/*     */     }
/*     */     
/* 536 */     synchronized (this.preparationMonitor) {
/* 537 */       if (!isPrepared()) {
/* 538 */         prepare();
/*     */       }
/*     */     } 
/* 541 */     return doInvoke(invocation);
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
/*     */   protected Object doInvoke(MethodInvocation invocation) throws Throwable {
/*     */     try {
/* 554 */       return doInvoke(invocation, getPortStub());
/*     */     }
/* 556 */     catch (SOAPFaultException ex) {
/* 557 */       throw new JaxWsSoapFaultException(ex);
/*     */     }
/* 559 */     catch (ProtocolException ex) {
/* 560 */       throw new RemoteConnectFailureException("Could not connect to remote service [" + 
/* 561 */           getEndpointAddress() + "]", ex);
/*     */     }
/* 563 */     catch (WebServiceException ex) {
/* 564 */       throw new RemoteAccessException("Could not access remote service at [" + 
/* 565 */           getEndpointAddress() + "]", ex);
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
/*     */   protected Object doInvoke(MethodInvocation invocation, Object portStub) throws Throwable {
/* 578 */     Method method = invocation.getMethod();
/*     */     try {
/* 580 */       return method.invoke(portStub, invocation.getArguments());
/*     */     }
/* 582 */     catch (InvocationTargetException ex) {
/* 583 */       throw ex.getTargetException();
/*     */     }
/* 585 */     catch (Throwable ex) {
/* 586 */       throw new RemoteProxyFailureException("Invocation of stub method failed: " + method, ex);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\remoting\jaxws\JaxWsPortClientInterceptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */