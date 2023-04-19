/*    */ package org.springframework.beans.factory.xml;
/*    */ 
/*    */ import org.springframework.beans.BeansException;
/*    */ import org.springframework.beans.factory.BeanFactory;
/*    */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*    */ import org.springframework.beans.factory.support.DefaultListableBeanFactory;
/*    */ import org.springframework.core.io.Resource;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Deprecated
/*    */ public class XmlBeanFactory
/*    */   extends DefaultListableBeanFactory
/*    */ {
/* 57 */   private final XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader((BeanDefinitionRegistry)this);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public XmlBeanFactory(Resource resource) throws BeansException {
/* 67 */     this(resource, null);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public XmlBeanFactory(Resource resource, BeanFactory parentBeanFactory) throws BeansException {
/* 78 */     super(parentBeanFactory);
/* 79 */     this.reader.loadBeanDefinitions(resource);
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\xml\XmlBeanFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */