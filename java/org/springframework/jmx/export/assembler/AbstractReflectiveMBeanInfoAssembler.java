/*     */ package org.springframework.jmx.export.assembler;
/*     */ 
/*     */ import java.beans.PropertyDescriptor;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.management.Descriptor;
/*     */ import javax.management.JMException;
/*     */ import javax.management.MBeanParameterInfo;
/*     */ import javax.management.modelmbean.ModelMBeanAttributeInfo;
/*     */ import javax.management.modelmbean.ModelMBeanOperationInfo;
/*     */ import org.springframework.aop.framework.AopProxyUtils;
/*     */ import org.springframework.aop.support.AopUtils;
/*     */ import org.springframework.beans.BeanUtils;
/*     */ import org.springframework.core.DefaultParameterNameDiscoverer;
/*     */ import org.springframework.core.ParameterNameDiscoverer;
/*     */ import org.springframework.jmx.support.JmxUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractReflectiveMBeanInfoAssembler
/*     */   extends AbstractMBeanInfoAssembler
/*     */ {
/*     */   protected static final String FIELD_GET_METHOD = "getMethod";
/*     */   protected static final String FIELD_SET_METHOD = "setMethod";
/*     */   protected static final String FIELD_ROLE = "role";
/*     */   protected static final String ROLE_GETTER = "getter";
/*     */   protected static final String ROLE_SETTER = "setter";
/*     */   protected static final String ROLE_OPERATION = "operation";
/*     */   protected static final String FIELD_VISIBILITY = "visibility";
/*     */   protected static final int ATTRIBUTE_OPERATION_VISIBILITY = 4;
/*     */   protected static final String FIELD_CLASS = "class";
/*     */   protected static final String FIELD_LOG = "log";
/*     */   protected static final String FIELD_LOG_FILE = "logFile";
/*     */   protected static final String FIELD_CURRENCY_TIME_LIMIT = "currencyTimeLimit";
/*     */   protected static final String FIELD_DEFAULT = "default";
/*     */   protected static final String FIELD_PERSIST_POLICY = "persistPolicy";
/*     */   protected static final String FIELD_PERSIST_PERIOD = "persistPeriod";
/*     */   protected static final String FIELD_PERSIST_LOCATION = "persistLocation";
/*     */   protected static final String FIELD_PERSIST_NAME = "persistName";
/*     */   protected static final String FIELD_DISPLAY_NAME = "displayName";
/*     */   protected static final String FIELD_UNITS = "units";
/*     */   protected static final String FIELD_METRIC_TYPE = "metricType";
/*     */   protected static final String FIELD_METRIC_CATEGORY = "metricCategory";
/*     */   private Integer defaultCurrencyTimeLimit;
/*     */   private boolean useStrictCasing = true;
/*     */   private boolean exposeClassDescriptor = false;
/* 183 */   private ParameterNameDiscoverer parameterNameDiscoverer = (ParameterNameDiscoverer)new DefaultParameterNameDiscoverer();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDefaultCurrencyTimeLimit(Integer defaultCurrencyTimeLimit) {
/* 207 */     this.defaultCurrencyTimeLimit = defaultCurrencyTimeLimit;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Integer getDefaultCurrencyTimeLimit() {
/* 214 */     return this.defaultCurrencyTimeLimit;
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
/* 225 */     this.useStrictCasing = useStrictCasing;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isUseStrictCasing() {
/* 232 */     return this.useStrictCasing;
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
/*     */   public void setExposeClassDescriptor(boolean exposeClassDescriptor) {
/* 252 */     this.exposeClassDescriptor = exposeClassDescriptor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isExposeClassDescriptor() {
/* 259 */     return this.exposeClassDescriptor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setParameterNameDiscoverer(ParameterNameDiscoverer parameterNameDiscoverer) {
/* 268 */     this.parameterNameDiscoverer = parameterNameDiscoverer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ParameterNameDiscoverer getParameterNameDiscoverer() {
/* 276 */     return this.parameterNameDiscoverer;
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
/*     */   protected ModelMBeanAttributeInfo[] getAttributeInfo(Object managedBean, String beanKey) throws JMException {
/* 294 */     PropertyDescriptor[] props = BeanUtils.getPropertyDescriptors(getClassToExpose(managedBean));
/* 295 */     List<ModelMBeanAttributeInfo> infos = new ArrayList<ModelMBeanAttributeInfo>();
/*     */     
/* 297 */     for (PropertyDescriptor prop : props) {
/* 298 */       Method getter = prop.getReadMethod();
/* 299 */       if (getter == null || getter.getDeclaringClass() != Object.class) {
/*     */ 
/*     */         
/* 302 */         if (getter != null && !includeReadAttribute(getter, beanKey)) {
/* 303 */           getter = null;
/*     */         }
/*     */         
/* 306 */         Method setter = prop.getWriteMethod();
/* 307 */         if (setter != null && !includeWriteAttribute(setter, beanKey)) {
/* 308 */           setter = null;
/*     */         }
/*     */         
/* 311 */         if (getter != null || setter != null) {
/*     */           
/* 313 */           String attrName = JmxUtils.getAttributeName(prop, isUseStrictCasing());
/* 314 */           String description = getAttributeDescription(prop, beanKey);
/* 315 */           ModelMBeanAttributeInfo info = new ModelMBeanAttributeInfo(attrName, description, getter, setter);
/*     */           
/* 317 */           Descriptor desc = info.getDescriptor();
/* 318 */           if (getter != null) {
/* 319 */             desc.setField("getMethod", getter.getName());
/*     */           }
/* 321 */           if (setter != null) {
/* 322 */             desc.setField("setMethod", setter.getName());
/*     */           }
/*     */           
/* 325 */           populateAttributeDescriptor(desc, getter, setter, beanKey);
/* 326 */           info.setDescriptor(desc);
/* 327 */           infos.add(info);
/*     */         } 
/*     */       } 
/*     */     } 
/* 331 */     return infos.<ModelMBeanAttributeInfo>toArray(new ModelMBeanAttributeInfo[infos.size()]);
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
/*     */   protected ModelMBeanOperationInfo[] getOperationInfo(Object managedBean, String beanKey) {
/* 348 */     Method[] methods = getClassToExpose(managedBean).getMethods();
/* 349 */     List<ModelMBeanOperationInfo> infos = new ArrayList<ModelMBeanOperationInfo>();
/*     */     
/* 351 */     for (Method method : methods) {
/* 352 */       if (!method.isSynthetic())
/*     */       {
/*     */         
/* 355 */         if (Object.class != method.getDeclaringClass()) {
/*     */ 
/*     */ 
/*     */           
/* 359 */           ModelMBeanOperationInfo info = null;
/* 360 */           PropertyDescriptor pd = BeanUtils.findPropertyForMethod(method);
/* 361 */           if (pd != null && ((method.equals(pd.getReadMethod()) && includeReadAttribute(method, beanKey)) || (method
/* 362 */             .equals(pd.getWriteMethod()) && includeWriteAttribute(method, beanKey)))) {
/*     */ 
/*     */             
/* 365 */             info = createModelMBeanOperationInfo(method, pd.getName(), beanKey);
/* 366 */             Descriptor desc = info.getDescriptor();
/* 367 */             if (method.equals(pd.getReadMethod())) {
/* 368 */               desc.setField("role", "getter");
/*     */             } else {
/*     */               
/* 371 */               desc.setField("role", "setter");
/*     */             } 
/* 373 */             desc.setField("visibility", Integer.valueOf(4));
/* 374 */             if (isExposeClassDescriptor()) {
/* 375 */               desc.setField("class", getClassForDescriptor(managedBean).getName());
/*     */             }
/* 377 */             info.setDescriptor(desc);
/*     */           } 
/*     */ 
/*     */           
/* 381 */           if (info == null && includeOperation(method, beanKey)) {
/* 382 */             info = createModelMBeanOperationInfo(method, method.getName(), beanKey);
/* 383 */             Descriptor desc = info.getDescriptor();
/* 384 */             desc.setField("role", "operation");
/* 385 */             if (isExposeClassDescriptor()) {
/* 386 */               desc.setField("class", getClassForDescriptor(managedBean).getName());
/*     */             }
/* 388 */             populateOperationDescriptor(desc, method, beanKey);
/* 389 */             info.setDescriptor(desc);
/*     */           } 
/*     */           
/* 392 */           if (info != null)
/* 393 */             infos.add(info); 
/*     */         } 
/*     */       }
/*     */     } 
/* 397 */     return infos.<ModelMBeanOperationInfo>toArray(new ModelMBeanOperationInfo[infos.size()]);
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
/*     */   protected ModelMBeanOperationInfo createModelMBeanOperationInfo(Method method, String name, String beanKey) {
/* 411 */     MBeanParameterInfo[] params = getOperationParameters(method, beanKey);
/* 412 */     if (params.length == 0) {
/* 413 */       return new ModelMBeanOperationInfo(getOperationDescription(method, beanKey), method);
/*     */     }
/*     */     
/* 416 */     return new ModelMBeanOperationInfo(method.getName(), 
/* 417 */         getOperationDescription(method, beanKey), 
/* 418 */         getOperationParameters(method, beanKey), method
/* 419 */         .getReturnType().getName(), 3);
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
/*     */   protected Class<?> getClassForDescriptor(Object managedBean) {
/* 436 */     if (AopUtils.isJdkDynamicProxy(managedBean)) {
/* 437 */       return AopProxyUtils.proxiedUserInterfaces(managedBean)[0];
/*     */     }
/* 439 */     return getClassToExpose(managedBean);
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
/*     */   protected abstract boolean includeReadAttribute(Method paramMethod, String paramString);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract boolean includeWriteAttribute(Method paramMethod, String paramString);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract boolean includeOperation(Method paramMethod, String paramString);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getAttributeDescription(PropertyDescriptor propertyDescriptor, String beanKey) {
/* 482 */     return propertyDescriptor.getDisplayName();
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
/*     */   protected String getOperationDescription(Method method, String beanKey) {
/* 495 */     return method.getName();
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
/*     */   protected MBeanParameterInfo[] getOperationParameters(Method method, String beanKey) {
/* 507 */     ParameterNameDiscoverer paramNameDiscoverer = getParameterNameDiscoverer();
/* 508 */     String[] paramNames = (paramNameDiscoverer != null) ? paramNameDiscoverer.getParameterNames(method) : null;
/* 509 */     if (paramNames == null) {
/* 510 */       return new MBeanParameterInfo[0];
/*     */     }
/*     */     
/* 513 */     MBeanParameterInfo[] info = new MBeanParameterInfo[paramNames.length];
/* 514 */     Class<?>[] typeParameters = method.getParameterTypes();
/* 515 */     for (int i = 0; i < info.length; i++) {
/* 516 */       info[i] = new MBeanParameterInfo(paramNames[i], typeParameters[i].getName(), paramNames[i]);
/*     */     }
/*     */     
/* 519 */     return info;
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
/*     */   protected void populateMBeanDescriptor(Descriptor descriptor, Object managedBean, String beanKey) {
/* 535 */     applyDefaultCurrencyTimeLimit(descriptor);
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
/*     */   protected void populateAttributeDescriptor(Descriptor desc, Method getter, Method setter, String beanKey) {
/* 552 */     applyDefaultCurrencyTimeLimit(desc);
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
/*     */   protected void populateOperationDescriptor(Descriptor desc, Method method, String beanKey) {
/* 568 */     applyDefaultCurrencyTimeLimit(desc);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void applyDefaultCurrencyTimeLimit(Descriptor desc) {
/* 578 */     if (getDefaultCurrencyTimeLimit() != null) {
/* 579 */       desc.setField("currencyTimeLimit", getDefaultCurrencyTimeLimit().toString());
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
/*     */   protected void applyCurrencyTimeLimit(Descriptor desc, int currencyTimeLimit) {
/* 595 */     if (currencyTimeLimit > 0) {
/*     */       
/* 597 */       desc.setField("currencyTimeLimit", Integer.toString(currencyTimeLimit));
/*     */     }
/* 599 */     else if (currencyTimeLimit == 0) {
/*     */       
/* 601 */       desc.setField("currencyTimeLimit", Integer.toString(2147483647));
/*     */     }
/*     */     else {
/*     */       
/* 605 */       applyDefaultCurrencyTimeLimit(desc);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\jmx\export\assembler\AbstractReflectiveMBeanInfoAssembler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */