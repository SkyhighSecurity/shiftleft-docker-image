/*    */ package org.springframework.scheduling.concurrent;
/*    */ 
/*    */ import java.util.Properties;
/*    */ import java.util.concurrent.Executor;
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
/*    */ public class DefaultManagedTaskExecutor
/*    */   extends ConcurrentTaskExecutor
/*    */   implements InitializingBean
/*    */ {
/* 41 */   private JndiLocatorDelegate jndiLocator = new JndiLocatorDelegate();
/*    */   
/* 43 */   private String jndiName = "java:comp/DefaultManagedExecutorService";
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
/* 86 */     if (this.jndiName != null)
/* 87 */       setConcurrentExecutor((Executor)this.jndiLocator.lookup(this.jndiName, Executor.class)); 
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\scheduling\concurrent\DefaultManagedTaskExecutor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */