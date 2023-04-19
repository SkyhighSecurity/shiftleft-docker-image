/*    */ package org.springframework.remoting.caucho;
/*    */ 
/*    */ import org.aopalliance.intercept.Interceptor;
/*    */ import org.springframework.aop.framework.ProxyFactory;
/*    */ import org.springframework.beans.factory.FactoryBean;
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
/*    */ @Deprecated
/*    */ public class BurlapProxyFactoryBean
/*    */   extends BurlapClientInterceptor
/*    */   implements FactoryBean<Object>
/*    */ {
/*    */   private Object serviceProxy;
/*    */   
/*    */   public void afterPropertiesSet() {
/* 53 */     super.afterPropertiesSet();
/* 54 */     this.serviceProxy = (new ProxyFactory(getServiceInterface(), (Interceptor)this)).getProxy(getBeanClassLoader());
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Object getObject() {
/* 60 */     return this.serviceProxy;
/*    */   }
/*    */ 
/*    */   
/*    */   public Class<?> getObjectType() {
/* 65 */     return getServiceInterface();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isSingleton() {
/* 70 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\remoting\caucho\BurlapProxyFactoryBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */