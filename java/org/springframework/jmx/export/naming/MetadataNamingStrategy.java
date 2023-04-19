/*     */ package org.springframework.jmx.export.naming;
/*     */ 
/*     */ import java.util.Hashtable;
/*     */ import javax.management.MalformedObjectNameException;
/*     */ import javax.management.ObjectName;
/*     */ import org.springframework.aop.support.AopUtils;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.jmx.export.metadata.JmxAttributeSource;
/*     */ import org.springframework.jmx.export.metadata.ManagedResource;
/*     */ import org.springframework.jmx.support.ObjectNameManager;
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
/*     */ 
/*     */ public class MetadataNamingStrategy
/*     */   implements ObjectNamingStrategy, InitializingBean
/*     */ {
/*     */   private JmxAttributeSource attributeSource;
/*     */   private String defaultDomain;
/*     */   
/*     */   public MetadataNamingStrategy() {}
/*     */   
/*     */   public MetadataNamingStrategy(JmxAttributeSource attributeSource) {
/*  72 */     Assert.notNull(attributeSource, "JmxAttributeSource must not be null");
/*  73 */     this.attributeSource = attributeSource;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAttributeSource(JmxAttributeSource attributeSource) {
/*  82 */     Assert.notNull(attributeSource, "JmxAttributeSource must not be null");
/*  83 */     this.attributeSource = attributeSource;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDefaultDomain(String defaultDomain) {
/*  94 */     this.defaultDomain = defaultDomain;
/*     */   }
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() {
/*  99 */     if (this.attributeSource == null) {
/* 100 */       throw new IllegalArgumentException("Property 'attributeSource' is required");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjectName getObjectName(Object managedBean, String beanKey) throws MalformedObjectNameException {
/* 111 */     Class<?> managedClass = AopUtils.getTargetClass(managedBean);
/* 112 */     ManagedResource mr = this.attributeSource.getManagedResource(managedClass);
/*     */ 
/*     */     
/* 115 */     if (mr != null && StringUtils.hasText(mr.getObjectName())) {
/* 116 */       return ObjectNameManager.getInstance(mr.getObjectName());
/*     */     }
/*     */     
/*     */     try {
/* 120 */       return ObjectNameManager.getInstance(beanKey);
/*     */     }
/* 122 */     catch (MalformedObjectNameException ex) {
/* 123 */       String domain = this.defaultDomain;
/* 124 */       if (domain == null) {
/* 125 */         domain = ClassUtils.getPackageName(managedClass);
/*     */       }
/* 127 */       Hashtable<String, String> properties = new Hashtable<String, String>();
/* 128 */       properties.put("type", ClassUtils.getShortName(managedClass));
/* 129 */       properties.put("name", beanKey);
/* 130 */       return ObjectNameManager.getInstance(domain, properties);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\jmx\export\naming\MetadataNamingStrategy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */