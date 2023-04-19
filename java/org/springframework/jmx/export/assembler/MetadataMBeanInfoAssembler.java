/*     */ package org.springframework.jmx.export.assembler;
/*     */ 
/*     */ import java.beans.PropertyDescriptor;
/*     */ import java.lang.reflect.Method;
/*     */ import javax.management.Descriptor;
/*     */ import javax.management.MBeanParameterInfo;
/*     */ import javax.management.modelmbean.ModelMBeanNotificationInfo;
/*     */ import org.springframework.aop.support.AopUtils;
/*     */ import org.springframework.beans.BeanUtils;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.jmx.export.metadata.InvalidMetadataException;
/*     */ import org.springframework.jmx.export.metadata.JmxAttributeSource;
/*     */ import org.springframework.jmx.export.metadata.JmxMetadataUtils;
/*     */ import org.springframework.jmx.export.metadata.ManagedAttribute;
/*     */ import org.springframework.jmx.export.metadata.ManagedMetric;
/*     */ import org.springframework.jmx.export.metadata.ManagedNotification;
/*     */ import org.springframework.jmx.export.metadata.ManagedOperation;
/*     */ import org.springframework.jmx.export.metadata.ManagedOperationParameter;
/*     */ import org.springframework.jmx.export.metadata.ManagedResource;
/*     */ import org.springframework.util.Assert;
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
/*     */ public class MetadataMBeanInfoAssembler
/*     */   extends AbstractReflectiveMBeanInfoAssembler
/*     */   implements AutodetectCapableMBeanInfoAssembler, InitializingBean
/*     */ {
/*     */   private JmxAttributeSource attributeSource;
/*     */   
/*     */   public MetadataMBeanInfoAssembler() {}
/*     */   
/*     */   public MetadataMBeanInfoAssembler(JmxAttributeSource attributeSource) {
/*  76 */     Assert.notNull(attributeSource, "JmxAttributeSource must not be null");
/*  77 */     this.attributeSource = attributeSource;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAttributeSource(JmxAttributeSource attributeSource) {
/*  87 */     Assert.notNull(attributeSource, "JmxAttributeSource must not be null");
/*  88 */     this.attributeSource = attributeSource;
/*     */   }
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() {
/*  93 */     if (this.attributeSource == null) {
/*  94 */       throw new IllegalArgumentException("Property 'attributeSource' is required");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void checkManagedBean(Object managedBean) throws IllegalArgumentException {
/* 105 */     if (AopUtils.isJdkDynamicProxy(managedBean)) {
/* 106 */       throw new IllegalArgumentException("MetadataMBeanInfoAssembler does not support JDK dynamic proxies - export the target beans directly or use CGLIB proxies instead");
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
/*     */   public boolean includeBean(Class<?> beanClass, String beanName) {
/* 120 */     return (this.attributeSource.getManagedResource(getClassToExpose(beanClass)) != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean includeReadAttribute(Method method, String beanKey) {
/* 131 */     return (hasManagedAttribute(method) || hasManagedMetric(method));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean includeWriteAttribute(Method method, String beanKey) {
/* 142 */     return hasManagedAttribute(method);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean includeOperation(Method method, String beanKey) {
/* 153 */     PropertyDescriptor pd = BeanUtils.findPropertyForMethod(method);
/* 154 */     if (pd != null && 
/* 155 */       hasManagedAttribute(method)) {
/* 156 */       return true;
/*     */     }
/*     */     
/* 159 */     return hasManagedOperation(method);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean hasManagedAttribute(Method method) {
/* 166 */     return (this.attributeSource.getManagedAttribute(method) != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean hasManagedMetric(Method method) {
/* 173 */     return (this.attributeSource.getManagedMetric(method) != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean hasManagedOperation(Method method) {
/* 181 */     return (this.attributeSource.getManagedOperation(method) != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getDescription(Object managedBean, String beanKey) {
/* 191 */     ManagedResource mr = this.attributeSource.getManagedResource(getClassToExpose(managedBean));
/* 192 */     return (mr != null) ? mr.getDescription() : "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getAttributeDescription(PropertyDescriptor propertyDescriptor, String beanKey) {
/* 202 */     Method readMethod = propertyDescriptor.getReadMethod();
/* 203 */     Method writeMethod = propertyDescriptor.getWriteMethod();
/*     */ 
/*     */     
/* 206 */     ManagedAttribute getter = (readMethod != null) ? this.attributeSource.getManagedAttribute(readMethod) : null;
/*     */     
/* 208 */     ManagedAttribute setter = (writeMethod != null) ? this.attributeSource.getManagedAttribute(writeMethod) : null;
/*     */     
/* 210 */     if (getter != null && StringUtils.hasText(getter.getDescription())) {
/* 211 */       return getter.getDescription();
/*     */     }
/* 213 */     if (setter != null && StringUtils.hasText(setter.getDescription())) {
/* 214 */       return setter.getDescription();
/*     */     }
/*     */     
/* 217 */     ManagedMetric metric = (readMethod != null) ? this.attributeSource.getManagedMetric(readMethod) : null;
/* 218 */     if (metric != null && StringUtils.hasText(metric.getDescription())) {
/* 219 */       return metric.getDescription();
/*     */     }
/*     */     
/* 222 */     return propertyDescriptor.getDisplayName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getOperationDescription(Method method, String beanKey) {
/* 231 */     PropertyDescriptor pd = BeanUtils.findPropertyForMethod(method);
/* 232 */     if (pd != null) {
/* 233 */       ManagedAttribute ma = this.attributeSource.getManagedAttribute(method);
/* 234 */       if (ma != null && StringUtils.hasText(ma.getDescription())) {
/* 235 */         return ma.getDescription();
/*     */       }
/* 237 */       ManagedMetric metric = this.attributeSource.getManagedMetric(method);
/* 238 */       if (metric != null && StringUtils.hasText(metric.getDescription())) {
/* 239 */         return metric.getDescription();
/*     */       }
/* 241 */       return method.getName();
/*     */     } 
/*     */     
/* 244 */     ManagedOperation mo = this.attributeSource.getManagedOperation(method);
/* 245 */     if (mo != null && StringUtils.hasText(mo.getDescription())) {
/* 246 */       return mo.getDescription();
/*     */     }
/* 248 */     return method.getName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected MBeanParameterInfo[] getOperationParameters(Method method, String beanKey) {
/* 259 */     ManagedOperationParameter[] params = this.attributeSource.getManagedOperationParameters(method);
/* 260 */     if (ObjectUtils.isEmpty((Object[])params)) {
/* 261 */       return super.getOperationParameters(method, beanKey);
/*     */     }
/*     */     
/* 264 */     MBeanParameterInfo[] parameterInfo = new MBeanParameterInfo[params.length];
/* 265 */     Class<?>[] methodParameters = method.getParameterTypes();
/* 266 */     for (int i = 0; i < params.length; i++) {
/* 267 */       ManagedOperationParameter param = params[i];
/* 268 */       parameterInfo[i] = new MBeanParameterInfo(param
/* 269 */           .getName(), methodParameters[i].getName(), param.getDescription());
/*     */     } 
/* 271 */     return parameterInfo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ModelMBeanNotificationInfo[] getNotificationInfo(Object managedBean, String beanKey) {
/* 281 */     ManagedNotification[] notificationAttributes = this.attributeSource.getManagedNotifications(getClassToExpose(managedBean));
/* 282 */     ModelMBeanNotificationInfo[] notificationInfos = new ModelMBeanNotificationInfo[notificationAttributes.length];
/*     */ 
/*     */     
/* 285 */     for (int i = 0; i < notificationAttributes.length; i++) {
/* 286 */       ManagedNotification attribute = notificationAttributes[i];
/* 287 */       notificationInfos[i] = JmxMetadataUtils.convertToModelMBeanNotificationInfo(attribute);
/*     */     } 
/*     */     
/* 290 */     return notificationInfos;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void populateMBeanDescriptor(Descriptor desc, Object managedBean, String beanKey) {
/* 301 */     ManagedResource mr = this.attributeSource.getManagedResource(getClassToExpose(managedBean));
/* 302 */     if (mr == null) {
/* 303 */       throw new InvalidMetadataException("No ManagedResource attribute found for class: " + 
/* 304 */           getClassToExpose(managedBean));
/*     */     }
/*     */     
/* 307 */     applyCurrencyTimeLimit(desc, mr.getCurrencyTimeLimit());
/*     */     
/* 309 */     if (mr.isLog()) {
/* 310 */       desc.setField("log", "true");
/*     */     }
/* 312 */     if (StringUtils.hasLength(mr.getLogFile())) {
/* 313 */       desc.setField("logFile", mr.getLogFile());
/*     */     }
/*     */     
/* 316 */     if (StringUtils.hasLength(mr.getPersistPolicy())) {
/* 317 */       desc.setField("persistPolicy", mr.getPersistPolicy());
/*     */     }
/* 319 */     if (mr.getPersistPeriod() >= 0) {
/* 320 */       desc.setField("persistPeriod", Integer.toString(mr.getPersistPeriod()));
/*     */     }
/* 322 */     if (StringUtils.hasLength(mr.getPersistName())) {
/* 323 */       desc.setField("persistName", mr.getPersistName());
/*     */     }
/* 325 */     if (StringUtils.hasLength(mr.getPersistLocation())) {
/* 326 */       desc.setField("persistLocation", mr.getPersistLocation());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void populateAttributeDescriptor(Descriptor desc, Method getter, Method setter, String beanKey) {
/* 336 */     if (getter != null && hasManagedMetric(getter)) {
/* 337 */       populateMetricDescriptor(desc, this.attributeSource.getManagedMetric(getter));
/*     */     }
/*     */     else {
/*     */       
/* 341 */       ManagedAttribute gma = (getter == null) ? ManagedAttribute.EMPTY : this.attributeSource.getManagedAttribute(getter);
/*     */       
/* 343 */       ManagedAttribute sma = (setter == null) ? ManagedAttribute.EMPTY : this.attributeSource.getManagedAttribute(setter);
/* 344 */       populateAttributeDescriptor(desc, gma, sma);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void populateAttributeDescriptor(Descriptor desc, ManagedAttribute gma, ManagedAttribute sma) {
/* 349 */     applyCurrencyTimeLimit(desc, resolveIntDescriptor(gma.getCurrencyTimeLimit(), sma.getCurrencyTimeLimit()));
/*     */     
/* 351 */     Object defaultValue = resolveObjectDescriptor(gma.getDefaultValue(), sma.getDefaultValue());
/* 352 */     desc.setField("default", defaultValue);
/*     */     
/* 354 */     String persistPolicy = resolveStringDescriptor(gma.getPersistPolicy(), sma.getPersistPolicy());
/* 355 */     if (StringUtils.hasLength(persistPolicy)) {
/* 356 */       desc.setField("persistPolicy", persistPolicy);
/*     */     }
/* 358 */     int persistPeriod = resolveIntDescriptor(gma.getPersistPeriod(), sma.getPersistPeriod());
/* 359 */     if (persistPeriod >= 0) {
/* 360 */       desc.setField("persistPeriod", Integer.toString(persistPeriod));
/*     */     }
/*     */   }
/*     */   
/*     */   private void populateMetricDescriptor(Descriptor desc, ManagedMetric metric) {
/* 365 */     applyCurrencyTimeLimit(desc, metric.getCurrencyTimeLimit());
/*     */     
/* 367 */     if (StringUtils.hasLength(metric.getPersistPolicy())) {
/* 368 */       desc.setField("persistPolicy", metric.getPersistPolicy());
/*     */     }
/* 370 */     if (metric.getPersistPeriod() >= 0) {
/* 371 */       desc.setField("persistPeriod", Integer.toString(metric.getPersistPeriod()));
/*     */     }
/*     */     
/* 374 */     if (StringUtils.hasLength(metric.getDisplayName())) {
/* 375 */       desc.setField("displayName", metric.getDisplayName());
/*     */     }
/*     */     
/* 378 */     if (StringUtils.hasLength(metric.getUnit())) {
/* 379 */       desc.setField("units", metric.getUnit());
/*     */     }
/*     */     
/* 382 */     if (StringUtils.hasLength(metric.getCategory())) {
/* 383 */       desc.setField("metricCategory", metric.getCategory());
/*     */     }
/*     */     
/* 386 */     desc.setField("metricType", metric.getMetricType().toString());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void populateOperationDescriptor(Descriptor desc, Method method, String beanKey) {
/* 396 */     ManagedOperation mo = this.attributeSource.getManagedOperation(method);
/* 397 */     if (mo != null) {
/* 398 */       applyCurrencyTimeLimit(desc, mo.getCurrencyTimeLimit());
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
/*     */   private int resolveIntDescriptor(int getter, int setter) {
/* 412 */     return (getter >= setter) ? getter : setter;
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
/*     */   private Object resolveObjectDescriptor(Object getter, Object setter) {
/* 424 */     return (getter != null) ? getter : setter;
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
/*     */   private String resolveStringDescriptor(String getter, String setter) {
/* 438 */     return StringUtils.hasLength(getter) ? getter : setter;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\jmx\export\assembler\MetadataMBeanInfoAssembler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */