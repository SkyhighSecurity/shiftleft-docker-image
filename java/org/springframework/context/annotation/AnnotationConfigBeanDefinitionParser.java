/*    */ package org.springframework.context.annotation;
/*    */ 
/*    */ import java.util.Set;
/*    */ import org.springframework.beans.factory.config.BeanDefinition;
/*    */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*    */ import org.springframework.beans.factory.parsing.BeanComponentDefinition;
/*    */ import org.springframework.beans.factory.parsing.ComponentDefinition;
/*    */ import org.springframework.beans.factory.parsing.CompositeComponentDefinition;
/*    */ import org.springframework.beans.factory.xml.BeanDefinitionParser;
/*    */ import org.springframework.beans.factory.xml.ParserContext;
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
/*    */ public class AnnotationConfigBeanDefinitionParser
/*    */   implements BeanDefinitionParser
/*    */ {
/*    */   public BeanDefinition parse(Element element, ParserContext parserContext) {
/* 43 */     Object source = parserContext.extractSource(element);
/*    */ 
/*    */ 
/*    */     
/* 47 */     Set<BeanDefinitionHolder> processorDefinitions = AnnotationConfigUtils.registerAnnotationConfigProcessors(parserContext.getRegistry(), source);
/*    */ 
/*    */     
/* 50 */     CompositeComponentDefinition compDefinition = new CompositeComponentDefinition(element.getTagName(), source);
/* 51 */     parserContext.pushContainingComponent(compDefinition);
/*    */ 
/*    */     
/* 54 */     for (BeanDefinitionHolder processorDefinition : processorDefinitions) {
/* 55 */       parserContext.registerComponent((ComponentDefinition)new BeanComponentDefinition(processorDefinition));
/*    */     }
/*    */ 
/*    */     
/* 59 */     parserContext.popAndRegisterContainingComponent();
/*    */     
/* 61 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\annotation\AnnotationConfigBeanDefinitionParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */