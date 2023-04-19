/*     */ package org.springframework.beans.factory.xml;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class NamespaceHandlerSupport
/*     */   implements NamespaceHandler
/*     */ {
/*  50 */   private final Map<String, BeanDefinitionParser> parsers = new HashMap<String, BeanDefinitionParser>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  57 */   private final Map<String, BeanDefinitionDecorator> decorators = new HashMap<String, BeanDefinitionDecorator>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  64 */   private final Map<String, BeanDefinitionDecorator> attributeDecorators = new HashMap<String, BeanDefinitionDecorator>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanDefinition parse(Element element, ParserContext parserContext) {
/*  74 */     return findParserForElement(element, parserContext).parse(element, parserContext);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private BeanDefinitionParser findParserForElement(Element element, ParserContext parserContext) {
/*  82 */     String localName = parserContext.getDelegate().getLocalName(element);
/*  83 */     BeanDefinitionParser parser = this.parsers.get(localName);
/*  84 */     if (parser == null) {
/*  85 */       parserContext.getReaderContext().fatal("Cannot locate BeanDefinitionParser for element [" + localName + "]", element);
/*     */     }
/*     */     
/*  88 */     return parser;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanDefinitionHolder decorate(Node node, BeanDefinitionHolder definition, ParserContext parserContext) {
/*  99 */     return findDecoratorForNode(node, parserContext).decorate(node, definition, parserContext);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private BeanDefinitionDecorator findDecoratorForNode(Node node, ParserContext parserContext) {
/* 108 */     BeanDefinitionDecorator decorator = null;
/* 109 */     String localName = parserContext.getDelegate().getLocalName(node);
/* 110 */     if (node instanceof Element) {
/* 111 */       decorator = this.decorators.get(localName);
/*     */     }
/* 113 */     else if (node instanceof org.w3c.dom.Attr) {
/* 114 */       decorator = this.attributeDecorators.get(localName);
/*     */     } else {
/*     */       
/* 117 */       parserContext.getReaderContext().fatal("Cannot decorate based on Nodes of type [" + node
/* 118 */           .getClass().getName() + "]", node);
/*     */     } 
/* 120 */     if (decorator == null) {
/* 121 */       parserContext.getReaderContext().fatal("Cannot locate BeanDefinitionDecorator for " + ((node instanceof Element) ? "element" : "attribute") + " [" + localName + "]", node);
/*     */     }
/*     */     
/* 124 */     return decorator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void registerBeanDefinitionParser(String elementName, BeanDefinitionParser parser) {
/* 134 */     this.parsers.put(elementName, parser);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void registerBeanDefinitionDecorator(String elementName, BeanDefinitionDecorator dec) {
/* 143 */     this.decorators.put(elementName, dec);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void registerBeanDefinitionDecoratorForAttribute(String attrName, BeanDefinitionDecorator dec) {
/* 152 */     this.attributeDecorators.put(attrName, dec);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\xml\NamespaceHandlerSupport.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */