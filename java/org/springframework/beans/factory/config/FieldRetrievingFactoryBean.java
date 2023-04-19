/*     */ package org.springframework.beans.factory.config;
/*     */ 
/*     */ import java.lang.reflect.Field;
/*     */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*     */ import org.springframework.beans.factory.BeanFactoryUtils;
/*     */ import org.springframework.beans.factory.BeanNameAware;
/*     */ import org.springframework.beans.factory.FactoryBean;
/*     */ import org.springframework.beans.factory.FactoryBeanNotInitializedException;
/*     */ import org.springframework.beans.factory.InitializingBean;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FieldRetrievingFactoryBean
/*     */   implements FactoryBean<Object>, BeanNameAware, BeanClassLoaderAware, InitializingBean
/*     */ {
/*     */   private Class<?> targetClass;
/*     */   private Object targetObject;
/*     */   private String targetField;
/*     */   private String staticField;
/*     */   private String beanName;
/*  68 */   private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Field fieldObject;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTargetClass(Class<?> targetClass) {
/*  82 */     this.targetClass = targetClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?> getTargetClass() {
/*  89 */     return this.targetClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTargetObject(Object targetObject) {
/* 100 */     this.targetObject = targetObject;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getTargetObject() {
/* 107 */     return this.targetObject;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTargetField(String targetField) {
/* 118 */     this.targetField = StringUtils.trimAllWhitespace(targetField);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTargetField() {
/* 125 */     return this.targetField;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStaticField(String staticField) {
/* 136 */     this.staticField = StringUtils.trimAllWhitespace(staticField);
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
/* 147 */     this.beanName = StringUtils.trimAllWhitespace(BeanFactoryUtils.originalBeanName(beanName));
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBeanClassLoader(ClassLoader classLoader) {
/* 152 */     this.beanClassLoader = classLoader;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() throws ClassNotFoundException, NoSuchFieldException {
/* 158 */     if (this.targetClass != null && this.targetObject != null) {
/* 159 */       throw new IllegalArgumentException("Specify either targetClass or targetObject, not both");
/*     */     }
/*     */     
/* 162 */     if (this.targetClass == null && this.targetObject == null) {
/* 163 */       if (this.targetField != null) {
/* 164 */         throw new IllegalArgumentException("Specify targetClass or targetObject in combination with targetField");
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 169 */       if (this.staticField == null) {
/* 170 */         this.staticField = this.beanName;
/*     */       }
/*     */ 
/*     */       
/* 174 */       int lastDotIndex = this.staticField.lastIndexOf('.');
/* 175 */       if (lastDotIndex == -1 || lastDotIndex == this.staticField.length()) {
/* 176 */         throw new IllegalArgumentException("staticField must be a fully qualified class plus static field name: e.g. 'example.MyExampleClass.MY_EXAMPLE_FIELD'");
/*     */       }
/*     */ 
/*     */       
/* 180 */       String className = this.staticField.substring(0, lastDotIndex);
/* 181 */       String fieldName = this.staticField.substring(lastDotIndex + 1);
/* 182 */       this.targetClass = ClassUtils.forName(className, this.beanClassLoader);
/* 183 */       this.targetField = fieldName;
/*     */     
/*     */     }
/* 186 */     else if (this.targetField == null) {
/*     */       
/* 188 */       throw new IllegalArgumentException("targetField is required");
/*     */     } 
/*     */ 
/*     */     
/* 192 */     Class<?> targetClass = (this.targetObject != null) ? this.targetObject.getClass() : this.targetClass;
/* 193 */     this.fieldObject = targetClass.getField(this.targetField);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getObject() throws IllegalAccessException {
/* 199 */     if (this.fieldObject == null) {
/* 200 */       throw new FactoryBeanNotInitializedException();
/*     */     }
/* 202 */     ReflectionUtils.makeAccessible(this.fieldObject);
/* 203 */     if (this.targetObject != null)
/*     */     {
/* 205 */       return this.fieldObject.get(this.targetObject);
/*     */     }
/*     */ 
/*     */     
/* 209 */     return this.fieldObject.get(null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?> getObjectType() {
/* 215 */     return (this.fieldObject != null) ? this.fieldObject.getType() : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSingleton() {
/* 220 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\config\FieldRetrievingFactoryBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */