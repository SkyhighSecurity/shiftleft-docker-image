/*     */ package org.springframework.beans.factory.config;
/*     */ 
/*     */ import java.beans.PropertyEditor;
/*     */ import java.util.Map;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.PropertyEditorRegistrar;
/*     */ import org.springframework.core.Ordered;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CustomEditorConfigurer
/*     */   implements BeanFactoryPostProcessor, Ordered
/*     */ {
/*  97 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*  99 */   private int order = Integer.MAX_VALUE;
/*     */   
/*     */   private PropertyEditorRegistrar[] propertyEditorRegistrars;
/*     */   
/*     */   private Map<Class<?>, Class<? extends PropertyEditor>> customEditors;
/*     */ 
/*     */   
/*     */   public void setOrder(int order) {
/* 107 */     this.order = order;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getOrder() {
/* 112 */     return this.order;
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
/*     */   public void setPropertyEditorRegistrars(PropertyEditorRegistrar[] propertyEditorRegistrars) {
/* 126 */     this.propertyEditorRegistrars = propertyEditorRegistrars;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCustomEditors(Map<Class<?>, Class<? extends PropertyEditor>> customEditors) {
/* 136 */     this.customEditors = customEditors;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
/* 142 */     if (this.propertyEditorRegistrars != null) {
/* 143 */       for (PropertyEditorRegistrar propertyEditorRegistrar : this.propertyEditorRegistrars) {
/* 144 */         beanFactory.addPropertyEditorRegistrar(propertyEditorRegistrar);
/*     */       }
/*     */     }
/* 147 */     if (this.customEditors != null)
/* 148 */       for (Map.Entry<Class<?>, Class<? extends PropertyEditor>> entry : this.customEditors.entrySet()) {
/* 149 */         Class<?> requiredType = entry.getKey();
/* 150 */         Class<? extends PropertyEditor> propertyEditorClass = entry.getValue();
/* 151 */         beanFactory.registerCustomEditor(requiredType, propertyEditorClass);
/*     */       }  
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\config\CustomEditorConfigurer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */