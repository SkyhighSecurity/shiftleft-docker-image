/*    */ package org.springframework.beans.factory.serviceloader;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ import java.util.ServiceLoader;
/*    */ import org.springframework.beans.factory.BeanClassLoaderAware;
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
/*    */ public class ServiceFactoryBean
/*    */   extends AbstractServiceLoaderBasedFactoryBean
/*    */   implements BeanClassLoaderAware
/*    */ {
/*    */   protected Object getObjectToExpose(ServiceLoader<?> serviceLoader) {
/* 37 */     Iterator<?> it = serviceLoader.iterator();
/* 38 */     if (!it.hasNext()) {
/* 39 */       throw new IllegalStateException("ServiceLoader could not find service for type [" + 
/* 40 */           getServiceType() + "]");
/*    */     }
/* 42 */     return it.next();
/*    */   }
/*    */ 
/*    */   
/*    */   public Class<?> getObjectType() {
/* 47 */     return getServiceType();
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\serviceloader\ServiceFactoryBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */