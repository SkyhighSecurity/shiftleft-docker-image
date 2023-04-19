/*    */ package org.springframework.context.config;
/*    */ 
/*    */ import org.springframework.beans.factory.support.AbstractBeanDefinition;
/*    */ import org.springframework.beans.factory.support.RootBeanDefinition;
/*    */ import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
/*    */ import org.springframework.beans.factory.xml.ParserContext;
/*    */ import org.springframework.jmx.support.MBeanServerFactoryBean;
/*    */ import org.springframework.jmx.support.WebSphereMBeanServerFactoryBean;
/*    */ import org.springframework.jndi.JndiObjectFactoryBean;
/*    */ import org.springframework.util.ClassUtils;
/*    */ import org.springframework.util.StringUtils;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class MBeanServerBeanDefinitionParser
/*    */   extends AbstractBeanDefinitionParser
/*    */ {
/*    */   private static final String MBEAN_SERVER_BEAN_NAME = "mbeanServer";
/*    */   private static final String AGENT_ID_ATTRIBUTE = "agent-id";
/* 51 */   private static final boolean weblogicPresent = ClassUtils.isPresent("weblogic.management.Helper", MBeanServerBeanDefinitionParser.class
/* 52 */       .getClassLoader());
/*    */   
/* 54 */   private static final boolean webspherePresent = ClassUtils.isPresent("com.ibm.websphere.management.AdminServiceFactory", MBeanServerBeanDefinitionParser.class
/* 55 */       .getClassLoader());
/*    */ 
/*    */ 
/*    */   
/*    */   protected String resolveId(Element element, AbstractBeanDefinition definition, ParserContext parserContext) {
/* 60 */     String id = element.getAttribute("id");
/* 61 */     return StringUtils.hasText(id) ? id : "mbeanServer";
/*    */   }
/*    */ 
/*    */   
/*    */   protected AbstractBeanDefinition parseInternal(Element element, ParserContext parserContext) {
/* 66 */     String agentId = element.getAttribute("agent-id");
/* 67 */     if (StringUtils.hasText(agentId)) {
/* 68 */       RootBeanDefinition rootBeanDefinition = new RootBeanDefinition(MBeanServerFactoryBean.class);
/* 69 */       rootBeanDefinition.getPropertyValues().add("agentId", agentId);
/* 70 */       return (AbstractBeanDefinition)rootBeanDefinition;
/*    */     } 
/* 72 */     AbstractBeanDefinition specialServer = findServerForSpecialEnvironment();
/* 73 */     if (specialServer != null) {
/* 74 */       return specialServer;
/*    */     }
/* 76 */     RootBeanDefinition bd = new RootBeanDefinition(MBeanServerFactoryBean.class);
/* 77 */     bd.getPropertyValues().add("locateExistingServerIfPossible", Boolean.TRUE);
/*    */ 
/*    */     
/* 80 */     bd.setRole(2);
/* 81 */     bd.setSource(parserContext.extractSource(element));
/* 82 */     return (AbstractBeanDefinition)bd;
/*    */   }
/*    */   
/*    */   static AbstractBeanDefinition findServerForSpecialEnvironment() {
/* 86 */     if (weblogicPresent) {
/* 87 */       RootBeanDefinition bd = new RootBeanDefinition(JndiObjectFactoryBean.class);
/* 88 */       bd.getPropertyValues().add("jndiName", "java:comp/env/jmx/runtime");
/* 89 */       return (AbstractBeanDefinition)bd;
/*    */     } 
/* 91 */     if (webspherePresent) {
/* 92 */       return (AbstractBeanDefinition)new RootBeanDefinition(WebSphereMBeanServerFactoryBean.class);
/*    */     }
/*    */     
/* 95 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\config\MBeanServerBeanDefinitionParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */