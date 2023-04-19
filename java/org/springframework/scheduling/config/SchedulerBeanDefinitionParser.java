/*    */ package org.springframework.scheduling.config;
/*    */ 
/*    */ import org.springframework.beans.factory.support.BeanDefinitionBuilder;
/*    */ import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
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
/*    */ public class SchedulerBeanDefinitionParser
/*    */   extends AbstractSingleBeanDefinitionParser
/*    */ {
/*    */   protected String getBeanClassName(Element element) {
/* 35 */     return "org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler";
/*    */   }
/*    */ 
/*    */   
/*    */   protected void doParse(Element element, BeanDefinitionBuilder builder) {
/* 40 */     String poolSize = element.getAttribute("pool-size");
/* 41 */     if (StringUtils.hasText(poolSize))
/* 42 */       builder.addPropertyValue("poolSize", poolSize); 
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\scheduling\config\SchedulerBeanDefinitionParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */