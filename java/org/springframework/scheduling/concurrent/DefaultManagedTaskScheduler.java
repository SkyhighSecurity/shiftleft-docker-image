/*    */ package org.springframework.scheduling.concurrent;
/*    */ 
/*    */ import java.util.Properties;
/*    */ import java.util.concurrent.ScheduledExecutorService;
/*    */ import javax.naming.NamingException;
/*    */ import org.springframework.beans.factory.InitializingBean;
/*    */ import org.springframework.jndi.JndiLocatorDelegate;
/*    */ import org.springframework.jndi.JndiTemplate;
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
/*    */ public class DefaultManagedTaskScheduler
/*    */   extends ConcurrentTaskScheduler
/*    */   implements InitializingBean
/*    */ {
/* 41 */   private JndiLocatorDelegate jndiLocator = new JndiLocatorDelegate();
/*    */   
/* 43 */   private String jndiName = "java:comp/DefaultManagedScheduledExecutorService";
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setJndiTemplate(JndiTemplate jndiTemplate) {
/* 51 */     this.jndiLocator.setJndiTemplate(jndiTemplate);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setJndiEnvironment(Properties jndiEnvironment) {
/* 59 */     this.jndiLocator.setJndiEnvironment(jndiEnvironment);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setResourceRef(boolean resourceRef) {
/* 69 */     this.jndiLocator.setResourceRef(resourceRef);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setJndiName(String jndiName) {
/* 81 */     this.jndiName = jndiName;
/*    */   }
/*    */ 
/*    */   
/*    */   public void afterPropertiesSet() throws NamingException {
/* 86 */     if (this.jndiName != null) {
/* 87 */       ScheduledExecutorService executor = (ScheduledExecutorService)this.jndiLocator.lookup(this.jndiName, ScheduledExecutorService.class);
/* 88 */       setConcurrentExecutor(executor);
/* 89 */       setScheduledExecutor(executor);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\scheduling\concurrent\DefaultManagedTaskScheduler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */