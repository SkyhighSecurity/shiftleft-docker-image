/*    */ package org.springframework.ejb.config;
/*    */ 
/*    */ import org.w3c.dom.Element;
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
/*    */ class RemoteStatelessSessionBeanDefinitionParser
/*    */   extends AbstractJndiLocatingBeanDefinitionParser
/*    */ {
/*    */   protected String getBeanClassName(Element element) {
/* 36 */     return "org.springframework.ejb.access.SimpleRemoteStatelessSessionProxyFactoryBean";
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\ejb\config\RemoteStatelessSessionBeanDefinitionParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */