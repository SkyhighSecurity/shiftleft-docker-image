/*    */ package org.springframework.beans.factory.xml;
/*    */ 
/*    */ import org.springframework.beans.MutablePropertyValues;
/*    */ import org.springframework.beans.factory.config.BeanDefinition;
/*    */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*    */ import org.springframework.beans.factory.config.RuntimeBeanReference;
/*    */ import org.springframework.core.Conventions;
/*    */ import org.w3c.dom.Attr;
/*    */ import org.w3c.dom.Element;
/*    */ import org.w3c.dom.Node;
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
/*    */ public class SimplePropertyNamespaceHandler
/*    */   implements NamespaceHandler
/*    */ {
/*    */   private static final String REF_SUFFIX = "-ref";
/*    */   
/*    */   public void init() {}
/*    */   
/*    */   public BeanDefinition parse(Element element, ParserContext parserContext) {
/* 61 */     parserContext.getReaderContext().error("Class [" + 
/* 62 */         getClass().getName() + "] does not support custom elements.", element);
/* 63 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public BeanDefinitionHolder decorate(Node node, BeanDefinitionHolder definition, ParserContext parserContext) {
/* 68 */     if (node instanceof Attr) {
/* 69 */       Attr attr = (Attr)node;
/* 70 */       String propertyName = parserContext.getDelegate().getLocalName(attr);
/* 71 */       String propertyValue = attr.getValue();
/* 72 */       MutablePropertyValues pvs = definition.getBeanDefinition().getPropertyValues();
/* 73 */       if (pvs.contains(propertyName)) {
/* 74 */         parserContext.getReaderContext().error("Property '" + propertyName + "' is already defined using both <property> and inline syntax. Only one approach may be used per property.", attr);
/*    */       }
/*    */       
/* 77 */       if (propertyName.endsWith("-ref")) {
/* 78 */         propertyName = propertyName.substring(0, propertyName.length() - "-ref".length());
/* 79 */         pvs.add(Conventions.attributeNameToPropertyName(propertyName), new RuntimeBeanReference(propertyValue));
/*    */       } else {
/*    */         
/* 82 */         pvs.add(Conventions.attributeNameToPropertyName(propertyName), propertyValue);
/*    */       } 
/*    */     } 
/* 85 */     return definition;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\xml\SimplePropertyNamespaceHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */