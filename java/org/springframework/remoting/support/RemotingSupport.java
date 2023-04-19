/*    */ package org.springframework.remoting.support;
/*    */ 
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ import org.springframework.beans.factory.BeanClassLoaderAware;
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
/*    */ public abstract class RemotingSupport
/*    */   implements BeanClassLoaderAware
/*    */ {
/* 35 */   protected final Log logger = LogFactory.getLog(getClass());
/*    */   
/* 37 */   private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
/*    */ 
/*    */ 
/*    */   
/*    */   public void setBeanClassLoader(ClassLoader classLoader) {
/* 42 */     this.beanClassLoader = classLoader;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected ClassLoader getBeanClassLoader() {
/* 50 */     return this.beanClassLoader;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected ClassLoader overrideThreadContextClassLoader() {
/* 61 */     return ClassUtils.overrideThreadContextClassLoader(getBeanClassLoader());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void resetThreadContextClassLoader(ClassLoader original) {
/* 70 */     if (original != null)
/* 71 */       Thread.currentThread().setContextClassLoader(original); 
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\remoting\support\RemotingSupport.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */