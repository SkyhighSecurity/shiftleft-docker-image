/*    */ package org.springframework.remoting.jaxws;
/*    */ 
/*    */ import javax.xml.ws.Service;
/*    */ import org.springframework.beans.factory.FactoryBean;
/*    */ import org.springframework.beans.factory.InitializingBean;
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
/*    */ public class LocalJaxWsServiceFactoryBean
/*    */   extends LocalJaxWsServiceFactory
/*    */   implements FactoryBean<Service>, InitializingBean
/*    */ {
/*    */   private Service service;
/*    */   
/*    */   public void afterPropertiesSet() {
/* 46 */     this.service = createJaxWsService();
/*    */   }
/*    */ 
/*    */   
/*    */   public Service getObject() {
/* 51 */     return this.service;
/*    */   }
/*    */ 
/*    */   
/*    */   public Class<? extends Service> getObjectType() {
/* 56 */     return (this.service != null) ? (Class)this.service.getClass() : Service.class;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isSingleton() {
/* 61 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\remoting\jaxws\LocalJaxWsServiceFactoryBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */