/*    */ package org.springframework.beans.factory.serviceloader;
/*    */ 
/*    */ import java.util.ServiceLoader;
/*    */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*    */ import org.springframework.beans.factory.config.AbstractFactoryBean;
/*    */ import org.springframework.util.Assert;
/*    */ import org.springframework.util.ClassUtils;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class AbstractServiceLoaderBasedFactoryBean
/*    */   extends AbstractFactoryBean<Object>
/*    */   implements BeanClassLoaderAware
/*    */ {
/*    */   private Class<?> serviceType;
/* 39 */   private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setServiceType(Class<?> serviceType) {
/* 46 */     this.serviceType = serviceType;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Class<?> getServiceType() {
/* 53 */     return this.serviceType;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setBeanClassLoader(ClassLoader beanClassLoader) {
/* 58 */     this.beanClassLoader = beanClassLoader;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected Object createInstance() {
/* 68 */     Assert.notNull(getServiceType(), "Property 'serviceType' is required");
/* 69 */     return getObjectToExpose(ServiceLoader.load(getServiceType(), this.beanClassLoader));
/*    */   }
/*    */   
/*    */   protected abstract Object getObjectToExpose(ServiceLoader<?> paramServiceLoader);
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\serviceloader\AbstractServiceLoaderBasedFactoryBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */