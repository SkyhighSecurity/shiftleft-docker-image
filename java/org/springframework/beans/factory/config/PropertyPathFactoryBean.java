/*     */ package org.springframework.beans.factory.config;
/*     */ 
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.BeanWrapper;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.PropertyAccessorFactory;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryAware;
/*     */ import org.springframework.beans.factory.BeanFactoryUtils;
/*     */ import org.springframework.beans.factory.BeanNameAware;
/*     */ import org.springframework.beans.factory.FactoryBean;
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
/*     */ public class PropertyPathFactoryBean
/*     */   implements FactoryBean<Object>, BeanNameAware, BeanFactoryAware
/*     */ {
/*  86 */   private static final Log logger = LogFactory.getLog(PropertyPathFactoryBean.class);
/*     */ 
/*     */   
/*     */   private BeanWrapper targetBeanWrapper;
/*     */ 
/*     */   
/*     */   private String targetBeanName;
/*     */ 
/*     */   
/*     */   private String propertyPath;
/*     */ 
/*     */   
/*     */   private Class<?> resultType;
/*     */ 
/*     */   
/*     */   private String beanName;
/*     */ 
/*     */   
/*     */   private BeanFactory beanFactory;
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTargetObject(Object targetObject) {
/* 109 */     this.targetBeanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(targetObject);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTargetBeanName(String targetBeanName) {
/* 120 */     this.targetBeanName = StringUtils.trimAllWhitespace(targetBeanName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPropertyPath(String propertyPath) {
/* 129 */     this.propertyPath = StringUtils.trimAllWhitespace(propertyPath);
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
/*     */   public void setResultType(Class<?> resultType) {
/* 141 */     this.resultType = resultType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBeanName(String beanName) {
/* 152 */     this.beanName = StringUtils.trimAllWhitespace(BeanFactoryUtils.originalBeanName(beanName));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBeanFactory(BeanFactory beanFactory) {
/* 158 */     this.beanFactory = beanFactory;
/*     */     
/* 160 */     if (this.targetBeanWrapper != null && this.targetBeanName != null) {
/* 161 */       throw new IllegalArgumentException("Specify either 'targetObject' or 'targetBeanName', not both");
/*     */     }
/*     */     
/* 164 */     if (this.targetBeanWrapper == null && this.targetBeanName == null) {
/* 165 */       if (this.propertyPath != null) {
/* 166 */         throw new IllegalArgumentException("Specify 'targetObject' or 'targetBeanName' in combination with 'propertyPath'");
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 171 */       int dotIndex = this.beanName.indexOf('.');
/* 172 */       if (dotIndex == -1) {
/* 173 */         throw new IllegalArgumentException("Neither 'targetObject' nor 'targetBeanName' specified, and PropertyPathFactoryBean bean name '" + this.beanName + "' does not follow 'beanName.property' syntax");
/*     */       }
/*     */ 
/*     */       
/* 177 */       this.targetBeanName = this.beanName.substring(0, dotIndex);
/* 178 */       this.propertyPath = this.beanName.substring(dotIndex + 1);
/*     */     
/*     */     }
/* 181 */     else if (this.propertyPath == null) {
/*     */       
/* 183 */       throw new IllegalArgumentException("'propertyPath' is required");
/*     */     } 
/*     */     
/* 186 */     if (this.targetBeanWrapper == null && this.beanFactory.isSingleton(this.targetBeanName)) {
/*     */       
/* 188 */       Object bean = this.beanFactory.getBean(this.targetBeanName);
/* 189 */       this.targetBeanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(bean);
/* 190 */       this.resultType = this.targetBeanWrapper.getPropertyType(this.propertyPath);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getObject() throws BeansException {
/* 197 */     BeanWrapper target = this.targetBeanWrapper;
/* 198 */     if (target != null) {
/* 199 */       if (logger.isWarnEnabled() && this.targetBeanName != null && this.beanFactory instanceof ConfigurableBeanFactory && ((ConfigurableBeanFactory)this.beanFactory)
/*     */         
/* 201 */         .isCurrentlyInCreation(this.targetBeanName)) {
/* 202 */         logger.warn("Target bean '" + this.targetBeanName + "' is still in creation due to a circular reference - obtained value for property '" + this.propertyPath + "' may be outdated!");
/*     */       
/*     */       }
/*     */     }
/*     */     else {
/*     */       
/* 208 */       Object bean = this.beanFactory.getBean(this.targetBeanName);
/* 209 */       target = PropertyAccessorFactory.forBeanPropertyAccess(bean);
/*     */     } 
/* 211 */     return target.getPropertyValue(this.propertyPath);
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?> getObjectType() {
/* 216 */     return this.resultType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSingleton() {
/* 227 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\config\PropertyPathFactoryBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */