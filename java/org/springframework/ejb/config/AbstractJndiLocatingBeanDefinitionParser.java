/*    */ package org.springframework.ejb.config;
/*    */ 
/*    */ import org.springframework.beans.factory.config.RuntimeBeanReference;
/*    */ import org.springframework.beans.factory.support.BeanDefinitionBuilder;
/*    */ import org.springframework.beans.factory.xml.AbstractSimpleBeanDefinitionParser;
/*    */ import org.springframework.util.StringUtils;
/*    */ import org.springframework.util.xml.DomUtils;
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
/*    */ abstract class AbstractJndiLocatingBeanDefinitionParser
/*    */   extends AbstractSimpleBeanDefinitionParser
/*    */ {
/*    */   public static final String ENVIRONMENT = "environment";
/*    */   public static final String ENVIRONMENT_REF = "environment-ref";
/*    */   public static final String JNDI_ENVIRONMENT = "jndiEnvironment";
/*    */   
/*    */   protected boolean isEligibleAttribute(String attributeName) {
/* 50 */     return (super.isEligibleAttribute(attributeName) && !"environment-ref".equals(attributeName) && 
/* 51 */       !"lazy-init".equals(attributeName));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void postProcess(BeanDefinitionBuilder definitionBuilder, Element element) {
/* 56 */     Object envValue = DomUtils.getChildElementValueByTagName(element, "environment");
/* 57 */     if (envValue != null) {
/*    */       
/* 59 */       definitionBuilder.addPropertyValue("jndiEnvironment", envValue);
/*    */     }
/*    */     else {
/*    */       
/* 63 */       String envRef = element.getAttribute("environment-ref");
/* 64 */       if (StringUtils.hasLength(envRef)) {
/* 65 */         definitionBuilder.addPropertyValue("jndiEnvironment", new RuntimeBeanReference(envRef));
/*    */       }
/*    */     } 
/*    */     
/* 69 */     String lazyInit = element.getAttribute("lazy-init");
/* 70 */     if (StringUtils.hasText(lazyInit) && !"default".equals(lazyInit))
/* 71 */       definitionBuilder.setLazyInit("true".equals(lazyInit)); 
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\ejb\config\AbstractJndiLocatingBeanDefinitionParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */