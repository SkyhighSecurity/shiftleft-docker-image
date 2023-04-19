/*     */ package org.springframework.beans.factory.config;
/*     */ 
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import org.springframework.beans.TypeConverter;
/*     */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryAware;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.beans.support.ArgumentConvertingMethodInvoker;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MethodInvokingBean
/*     */   extends ArgumentConvertingMethodInvoker
/*     */   implements BeanClassLoaderAware, BeanFactoryAware, InitializingBean
/*     */ {
/*  69 */   private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
/*     */ 
/*     */   
/*     */   private ConfigurableBeanFactory beanFactory;
/*     */ 
/*     */   
/*     */   public void setBeanClassLoader(ClassLoader classLoader) {
/*  76 */     this.beanClassLoader = classLoader;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Class<?> resolveClassName(String className) throws ClassNotFoundException {
/*  81 */     return ClassUtils.forName(className, this.beanClassLoader);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBeanFactory(BeanFactory beanFactory) {
/*  86 */     if (beanFactory instanceof ConfigurableBeanFactory) {
/*  87 */       this.beanFactory = (ConfigurableBeanFactory)beanFactory;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected TypeConverter getDefaultTypeConverter() {
/*  98 */     if (this.beanFactory != null) {
/*  99 */       return this.beanFactory.getTypeConverter();
/*     */     }
/*     */     
/* 102 */     return super.getDefaultTypeConverter();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() throws Exception {
/* 109 */     prepare();
/* 110 */     invokeWithTargetException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object invokeWithTargetException() throws Exception {
/*     */     try {
/* 119 */       return invoke();
/*     */     }
/* 121 */     catch (InvocationTargetException ex) {
/* 122 */       if (ex.getTargetException() instanceof Exception) {
/* 123 */         throw (Exception)ex.getTargetException();
/*     */       }
/* 125 */       if (ex.getTargetException() instanceof Error) {
/* 126 */         throw (Error)ex.getTargetException();
/*     */       }
/* 128 */       throw ex;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\config\MethodInvokingBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */