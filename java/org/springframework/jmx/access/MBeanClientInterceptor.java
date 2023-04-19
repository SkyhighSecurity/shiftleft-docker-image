/*     */ package org.springframework.jmx.access;
/*     */ 
/*     */ import java.beans.PropertyDescriptor;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.MalformedURLException;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.management.Attribute;
/*     */ import javax.management.InstanceNotFoundException;
/*     */ import javax.management.IntrospectionException;
/*     */ import javax.management.JMException;
/*     */ import javax.management.JMX;
/*     */ import javax.management.MBeanAttributeInfo;
/*     */ import javax.management.MBeanException;
/*     */ import javax.management.MBeanInfo;
/*     */ import javax.management.MBeanOperationInfo;
/*     */ import javax.management.MBeanServerConnection;
/*     */ import javax.management.MBeanServerInvocationHandler;
/*     */ import javax.management.MalformedObjectNameException;
/*     */ import javax.management.ObjectName;
/*     */ import javax.management.OperationsException;
/*     */ import javax.management.ReflectionException;
/*     */ import javax.management.RuntimeErrorException;
/*     */ import javax.management.RuntimeMBeanException;
/*     */ import javax.management.RuntimeOperationsException;
/*     */ import javax.management.openmbean.CompositeData;
/*     */ import javax.management.openmbean.TabularData;
/*     */ import javax.management.remote.JMXServiceURL;
/*     */ import org.aopalliance.intercept.MethodInterceptor;
/*     */ import org.aopalliance.intercept.MethodInvocation;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.BeanUtils;
/*     */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*     */ import org.springframework.beans.factory.DisposableBean;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.core.CollectionFactory;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.jmx.support.JmxUtils;
/*     */ import org.springframework.jmx.support.ObjectNameManager;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ReflectionUtils;
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
/*     */ public class MBeanClientInterceptor
/*     */   implements MethodInterceptor, BeanClassLoaderAware, InitializingBean, DisposableBean
/*     */ {
/*  93 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*     */   private MBeanServerConnection server;
/*     */   
/*     */   private JMXServiceURL serviceUrl;
/*     */   
/*     */   private Map<String, ?> environment;
/*     */   
/*     */   private String agentId;
/*     */   
/*     */   private boolean connectOnStartup = true;
/*     */   
/*     */   private boolean refreshOnConnectFailure = false;
/*     */   
/*     */   private ObjectName objectName;
/*     */   
/*     */   private boolean useStrictCasing = true;
/*     */   
/*     */   private Class<?> managementInterface;
/*     */   
/* 113 */   private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
/*     */   
/* 115 */   private final ConnectorDelegate connector = new ConnectorDelegate();
/*     */   
/*     */   private MBeanServerConnection serverToUse;
/*     */   
/*     */   private MBeanServerInvocationHandler invocationHandler;
/*     */   
/*     */   private Map<String, MBeanAttributeInfo> allowedAttributes;
/*     */   
/*     */   private Map<MethodCacheKey, MBeanOperationInfo> allowedOperations;
/*     */   
/* 125 */   private final Map<Method, String[]> signatureCache = (Map)new HashMap<Method, String>();
/*     */   
/* 127 */   private final Object preparationMonitor = new Object();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setServer(MBeanServerConnection server) {
/* 135 */     this.server = server;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setServiceUrl(String url) throws MalformedURLException {
/* 142 */     this.serviceUrl = new JMXServiceURL(url);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEnvironment(Map<String, ?> environment) {
/* 150 */     this.environment = environment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, ?> getEnvironment() {
/* 161 */     return this.environment;
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
/*     */   public void setAgentId(String agentId) {
/* 173 */     this.agentId = agentId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setConnectOnStartup(boolean connectOnStartup) {
/* 182 */     this.connectOnStartup = connectOnStartup;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRefreshOnConnectFailure(boolean refreshOnConnectFailure) {
/* 192 */     this.refreshOnConnectFailure = refreshOnConnectFailure;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setObjectName(Object objectName) throws MalformedObjectNameException {
/* 200 */     this.objectName = ObjectNameManager.getInstance(objectName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUseStrictCasing(boolean useStrictCasing) {
/* 211 */     this.useStrictCasing = useStrictCasing;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setManagementInterface(Class<?> managementInterface) {
/* 220 */     this.managementInterface = managementInterface;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final Class<?> getManagementInterface() {
/* 228 */     return this.managementInterface;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBeanClassLoader(ClassLoader beanClassLoader) {
/* 233 */     this.beanClassLoader = beanClassLoader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() {
/* 243 */     if (this.server != null && this.refreshOnConnectFailure) {
/* 244 */       throw new IllegalArgumentException("'refreshOnConnectFailure' does not work when setting a 'server' reference. Prefer 'serviceUrl' etc instead.");
/*     */     }
/*     */     
/* 247 */     if (this.connectOnStartup) {
/* 248 */       prepare();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void prepare() {
/* 257 */     synchronized (this.preparationMonitor) {
/* 258 */       if (this.server != null) {
/* 259 */         this.serverToUse = this.server;
/*     */       } else {
/*     */         
/* 262 */         this.serverToUse = null;
/* 263 */         this.serverToUse = this.connector.connect(this.serviceUrl, this.environment, this.agentId);
/*     */       } 
/* 265 */       this.invocationHandler = null;
/* 266 */       if (this.useStrictCasing) {
/*     */         
/* 268 */         this
/* 269 */           .invocationHandler = new MBeanServerInvocationHandler(this.serverToUse, this.objectName, (this.managementInterface != null && JMX.isMXBeanInterface(this.managementInterface)));
/*     */       
/*     */       }
/*     */       else {
/*     */         
/* 274 */         retrieveMBeanInfo();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void retrieveMBeanInfo() throws MBeanInfoRetrievalException {
/*     */     try {
/* 285 */       MBeanInfo info = this.serverToUse.getMBeanInfo(this.objectName);
/*     */       
/* 287 */       MBeanAttributeInfo[] attributeInfo = info.getAttributes();
/* 288 */       this.allowedAttributes = new HashMap<String, MBeanAttributeInfo>(attributeInfo.length);
/* 289 */       for (MBeanAttributeInfo infoEle : attributeInfo) {
/* 290 */         this.allowedAttributes.put(infoEle.getName(), infoEle);
/*     */       }
/*     */       
/* 293 */       MBeanOperationInfo[] operationInfo = info.getOperations();
/* 294 */       this.allowedOperations = new HashMap<MethodCacheKey, MBeanOperationInfo>(operationInfo.length);
/* 295 */       for (MBeanOperationInfo infoEle : operationInfo) {
/* 296 */         Class<?>[] paramTypes = JmxUtils.parameterInfoToTypes(infoEle.getSignature(), this.beanClassLoader);
/* 297 */         this.allowedOperations.put(new MethodCacheKey(infoEle.getName(), paramTypes), infoEle);
/*     */       }
/*     */     
/* 300 */     } catch (ClassNotFoundException ex) {
/* 301 */       throw new MBeanInfoRetrievalException("Unable to locate class specified in method signature", ex);
/*     */     }
/* 303 */     catch (IntrospectionException ex) {
/* 304 */       throw new MBeanInfoRetrievalException("Unable to obtain MBean info for bean [" + this.objectName + "]", ex);
/*     */     }
/* 306 */     catch (InstanceNotFoundException ex) {
/*     */       
/* 308 */       throw new MBeanInfoRetrievalException("Unable to obtain MBean info for bean [" + this.objectName + "]: it is likely that this bean was unregistered during the proxy creation process", ex);
/*     */ 
/*     */     
/*     */     }
/* 312 */     catch (ReflectionException ex) {
/* 313 */       throw new MBeanInfoRetrievalException("Unable to read MBean info for bean [ " + this.objectName + "]", ex);
/*     */     }
/* 315 */     catch (IOException ex) {
/* 316 */       throw new MBeanInfoRetrievalException("An IOException occurred when communicating with the MBeanServer. It is likely that you are communicating with a remote MBeanServer. Check the inner exception for exact details.", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isPrepared() {
/* 327 */     synchronized (this.preparationMonitor) {
/* 328 */       return (this.serverToUse != null);
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
/*     */   public Object invoke(MethodInvocation invocation) throws Throwable {
/* 344 */     synchronized (this.preparationMonitor) {
/* 345 */       if (!isPrepared()) {
/* 346 */         prepare();
/*     */       }
/*     */     } 
/*     */     try {
/* 350 */       return doInvoke(invocation);
/*     */     }
/* 352 */     catch (MBeanConnectFailureException ex) {
/* 353 */       return handleConnectFailure(invocation, (Exception)ex);
/*     */     }
/* 355 */     catch (IOException ex) {
/* 356 */       return handleConnectFailure(invocation, ex);
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
/*     */   protected Object handleConnectFailure(MethodInvocation invocation, Exception ex) throws Throwable {
/* 373 */     if (this.refreshOnConnectFailure) {
/* 374 */       String msg = "Could not connect to JMX server - retrying";
/* 375 */       if (this.logger.isDebugEnabled()) {
/* 376 */         this.logger.warn(msg, ex);
/*     */       }
/* 378 */       else if (this.logger.isWarnEnabled()) {
/* 379 */         this.logger.warn(msg);
/*     */       } 
/* 381 */       prepare();
/* 382 */       return doInvoke(invocation);
/*     */     } 
/*     */     
/* 385 */     throw ex;
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
/*     */   protected Object doInvoke(MethodInvocation invocation) throws Throwable {
/* 398 */     Method method = invocation.getMethod();
/*     */     try {
/* 400 */       Object result = null;
/* 401 */       if (this.invocationHandler != null) {
/* 402 */         result = this.invocationHandler.invoke(invocation.getThis(), method, invocation.getArguments());
/*     */       } else {
/*     */         
/* 405 */         PropertyDescriptor pd = BeanUtils.findPropertyForMethod(method);
/* 406 */         if (pd != null) {
/* 407 */           result = invokeAttribute(pd, invocation);
/*     */         } else {
/*     */           
/* 410 */           result = invokeOperation(method, invocation.getArguments());
/*     */         } 
/*     */       } 
/* 413 */       return convertResultValueIfNecessary(result, new MethodParameter(method, -1));
/*     */     }
/* 415 */     catch (MBeanException ex) {
/* 416 */       throw ex.getTargetException();
/*     */     }
/* 418 */     catch (RuntimeMBeanException ex) {
/* 419 */       throw ex.getTargetException();
/*     */     }
/* 421 */     catch (RuntimeErrorException ex) {
/* 422 */       throw ex.getTargetError();
/*     */     }
/* 424 */     catch (RuntimeOperationsException ex) {
/*     */       
/* 426 */       RuntimeException rex = ex.getTargetException();
/* 427 */       if (rex instanceof RuntimeMBeanException) {
/* 428 */         throw ((RuntimeMBeanException)rex).getTargetException();
/*     */       }
/* 430 */       if (rex instanceof RuntimeErrorException) {
/* 431 */         throw ((RuntimeErrorException)rex).getTargetError();
/*     */       }
/*     */       
/* 434 */       throw rex;
/*     */     
/*     */     }
/* 437 */     catch (OperationsException ex) {
/* 438 */       if (ReflectionUtils.declaresException(method, ex.getClass())) {
/* 439 */         throw ex;
/*     */       }
/*     */       
/* 442 */       throw new InvalidInvocationException(ex.getMessage());
/*     */     
/*     */     }
/* 445 */     catch (JMException ex) {
/* 446 */       if (ReflectionUtils.declaresException(method, ex.getClass())) {
/* 447 */         throw ex;
/*     */       }
/*     */       
/* 450 */       throw new InvocationFailureException("JMX access failed", ex);
/*     */     
/*     */     }
/* 453 */     catch (IOException ex) {
/* 454 */       if (ReflectionUtils.declaresException(method, ex.getClass())) {
/* 455 */         throw ex;
/*     */       }
/*     */       
/* 458 */       throw new MBeanConnectFailureException("I/O failure during JMX access", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Object invokeAttribute(PropertyDescriptor pd, MethodInvocation invocation) throws JMException, IOException {
/* 466 */     String attributeName = JmxUtils.getAttributeName(pd, this.useStrictCasing);
/* 467 */     MBeanAttributeInfo inf = this.allowedAttributes.get(attributeName);
/*     */ 
/*     */     
/* 470 */     if (inf == null) {
/* 471 */       throw new InvalidInvocationException("Attribute '" + pd
/* 472 */           .getName() + "' is not exposed on the management interface");
/*     */     }
/* 474 */     if (invocation.getMethod().equals(pd.getReadMethod())) {
/* 475 */       if (inf.isReadable()) {
/* 476 */         return this.serverToUse.getAttribute(this.objectName, attributeName);
/*     */       }
/*     */       
/* 479 */       throw new InvalidInvocationException("Attribute '" + attributeName + "' is not readable");
/*     */     } 
/*     */     
/* 482 */     if (invocation.getMethod().equals(pd.getWriteMethod())) {
/* 483 */       if (inf.isWritable()) {
/* 484 */         this.serverToUse.setAttribute(this.objectName, new Attribute(attributeName, invocation.getArguments()[0]));
/* 485 */         return null;
/*     */       } 
/*     */       
/* 488 */       throw new InvalidInvocationException("Attribute '" + attributeName + "' is not writable");
/*     */     } 
/*     */ 
/*     */     
/* 492 */     throw new IllegalStateException("Method [" + invocation
/* 493 */         .getMethod() + "] is neither a bean property getter nor a setter");
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
/*     */   private Object invokeOperation(Method method, Object[] args) throws JMException, IOException {
/* 505 */     MethodCacheKey key = new MethodCacheKey(method.getName(), method.getParameterTypes());
/* 506 */     MBeanOperationInfo info = this.allowedOperations.get(key);
/* 507 */     if (info == null) {
/* 508 */       throw new InvalidInvocationException("Operation '" + method.getName() + "' is not exposed on the management interface");
/*     */     }
/*     */     
/* 511 */     String[] signature = null;
/* 512 */     synchronized (this.signatureCache) {
/* 513 */       signature = this.signatureCache.get(method);
/* 514 */       if (signature == null) {
/* 515 */         signature = JmxUtils.getMethodSignature(method);
/* 516 */         this.signatureCache.put(method, signature);
/*     */       } 
/*     */     } 
/* 519 */     return this.serverToUse.invoke(this.objectName, method.getName(), args, signature);
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
/*     */   protected Object convertResultValueIfNecessary(Object result, MethodParameter parameter) {
/* 531 */     Class<?> targetClass = parameter.getParameterType();
/*     */     try {
/* 533 */       if (result == null) {
/* 534 */         return null;
/*     */       }
/* 536 */       if (ClassUtils.isAssignableValue(targetClass, result)) {
/* 537 */         return result;
/*     */       }
/* 539 */       if (result instanceof CompositeData) {
/* 540 */         Method fromMethod = targetClass.getMethod("from", new Class[] { CompositeData.class });
/* 541 */         return ReflectionUtils.invokeMethod(fromMethod, null, new Object[] { result });
/*     */       } 
/* 543 */       if (result instanceof CompositeData[]) {
/* 544 */         CompositeData[] array = (CompositeData[])result;
/* 545 */         if (targetClass.isArray()) {
/* 546 */           return convertDataArrayToTargetArray((Object[])array, targetClass);
/*     */         }
/* 548 */         if (Collection.class.isAssignableFrom(targetClass)) {
/*     */           
/* 550 */           Class<?> elementType = ResolvableType.forMethodParameter(parameter).asCollection().resolveGeneric(new int[0]);
/* 551 */           if (elementType != null) {
/* 552 */             return convertDataArrayToTargetCollection((Object[])array, targetClass, elementType);
/*     */           }
/*     */         } 
/*     */       } else {
/* 556 */         if (result instanceof TabularData) {
/* 557 */           Method fromMethod = targetClass.getMethod("from", new Class[] { TabularData.class });
/* 558 */           return ReflectionUtils.invokeMethod(fromMethod, null, new Object[] { result });
/*     */         } 
/* 560 */         if (result instanceof TabularData[]) {
/* 561 */           TabularData[] array = (TabularData[])result;
/* 562 */           if (targetClass.isArray()) {
/* 563 */             return convertDataArrayToTargetArray((Object[])array, targetClass);
/*     */           }
/* 565 */           if (Collection.class.isAssignableFrom(targetClass)) {
/*     */             
/* 567 */             Class<?> elementType = ResolvableType.forMethodParameter(parameter).asCollection().resolveGeneric(new int[0]);
/* 568 */             if (elementType != null)
/* 569 */               return convertDataArrayToTargetCollection((Object[])array, targetClass, elementType); 
/*     */           } 
/*     */         } 
/*     */       } 
/* 573 */       throw new InvocationFailureException("Incompatible result value [" + result + "] for target type [" + targetClass
/* 574 */           .getName() + "]");
/*     */     }
/* 576 */     catch (NoSuchMethodException ex) {
/* 577 */       throw new InvocationFailureException("Could not obtain 'from(CompositeData)' / 'from(TabularData)' method on target type [" + targetClass
/*     */           
/* 579 */           .getName() + "] for conversion of MXBean data structure [" + result + "]");
/*     */     } 
/*     */   }
/*     */   
/*     */   private Object convertDataArrayToTargetArray(Object[] array, Class<?> targetClass) throws NoSuchMethodException {
/* 584 */     Class<?> targetType = targetClass.getComponentType();
/* 585 */     Method fromMethod = targetType.getMethod("from", new Class[] { array.getClass().getComponentType() });
/* 586 */     Object resultArray = Array.newInstance(targetType, array.length);
/* 587 */     for (int i = 0; i < array.length; i++) {
/* 588 */       Array.set(resultArray, i, ReflectionUtils.invokeMethod(fromMethod, null, new Object[] { array[i] }));
/*     */     } 
/* 590 */     return resultArray;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Collection<?> convertDataArrayToTargetCollection(Object[] array, Class<?> collectionType, Class<?> elementType) throws NoSuchMethodException {
/* 596 */     Method fromMethod = elementType.getMethod("from", new Class[] { array.getClass().getComponentType() });
/* 597 */     Collection<Object> resultColl = CollectionFactory.createCollection(collectionType, Array.getLength(array));
/* 598 */     for (int i = 0; i < array.length; i++) {
/* 599 */       resultColl.add(ReflectionUtils.invokeMethod(fromMethod, null, new Object[] { array[i] }));
/*     */     } 
/* 601 */     return resultColl;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void destroy() {
/* 607 */     this.connector.close();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class MethodCacheKey
/*     */     implements Comparable<MethodCacheKey>
/*     */   {
/*     */     private final String name;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final Class<?>[] parameterTypes;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public MethodCacheKey(String name, Class<?>[] parameterTypes) {
/* 628 */       this.name = name;
/* 629 */       this.parameterTypes = (parameterTypes != null) ? parameterTypes : new Class[0];
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object other) {
/* 634 */       if (this == other) {
/* 635 */         return true;
/*     */       }
/* 637 */       MethodCacheKey otherKey = (MethodCacheKey)other;
/* 638 */       return (this.name.equals(otherKey.name) && Arrays.equals((Object[])this.parameterTypes, (Object[])otherKey.parameterTypes));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 643 */       return this.name.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 648 */       return this.name + "(" + StringUtils.arrayToCommaDelimitedString((Object[])this.parameterTypes) + ")";
/*     */     }
/*     */ 
/*     */     
/*     */     public int compareTo(MethodCacheKey other) {
/* 653 */       int result = this.name.compareTo(other.name);
/* 654 */       if (result != 0) {
/* 655 */         return result;
/*     */       }
/* 657 */       if (this.parameterTypes.length < other.parameterTypes.length) {
/* 658 */         return -1;
/*     */       }
/* 660 */       if (this.parameterTypes.length > other.parameterTypes.length) {
/* 661 */         return 1;
/*     */       }
/* 663 */       for (int i = 0; i < this.parameterTypes.length; i++) {
/* 664 */         result = this.parameterTypes[i].getName().compareTo(other.parameterTypes[i].getName());
/* 665 */         if (result != 0) {
/* 666 */           return result;
/*     */         }
/*     */       } 
/* 669 */       return 0;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\jmx\access\MBeanClientInterceptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */