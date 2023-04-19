/*    */ package org.springframework.ejb.config;
/*    */ 
/*    */ import org.springframework.beans.factory.xml.BeanDefinitionParser;
/*    */ import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
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
/*    */ public class JeeNamespaceHandler
/*    */   extends NamespaceHandlerSupport
/*    */ {
/*    */   public void init() {
/* 32 */     registerBeanDefinitionParser("jndi-lookup", (BeanDefinitionParser)new JndiLookupBeanDefinitionParser());
/* 33 */     registerBeanDefinitionParser("local-slsb", (BeanDefinitionParser)new LocalStatelessSessionBeanDefinitionParser());
/* 34 */     registerBeanDefinitionParser("remote-slsb", (BeanDefinitionParser)new RemoteStatelessSessionBeanDefinitionParser());
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\ejb\config\JeeNamespaceHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */