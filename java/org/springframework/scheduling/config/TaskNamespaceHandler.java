/*    */ package org.springframework.scheduling.config;
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
/*    */ public class TaskNamespaceHandler
/*    */   extends NamespaceHandlerSupport
/*    */ {
/*    */   public void init() {
/* 31 */     registerBeanDefinitionParser("annotation-driven", new AnnotationDrivenBeanDefinitionParser());
/* 32 */     registerBeanDefinitionParser("executor", (BeanDefinitionParser)new ExecutorBeanDefinitionParser());
/* 33 */     registerBeanDefinitionParser("scheduled-tasks", (BeanDefinitionParser)new ScheduledTasksBeanDefinitionParser());
/* 34 */     registerBeanDefinitionParser("scheduler", (BeanDefinitionParser)new SchedulerBeanDefinitionParser());
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\scheduling\config\TaskNamespaceHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */