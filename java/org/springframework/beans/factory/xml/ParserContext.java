/*     */ package org.springframework.beans.factory.xml;
/*     */ 
/*     */ import java.util.Stack;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*     */ import org.springframework.beans.factory.parsing.BeanComponentDefinition;
/*     */ import org.springframework.beans.factory.parsing.ComponentDefinition;
/*     */ import org.springframework.beans.factory.parsing.CompositeComponentDefinition;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
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
/*     */ public final class ParserContext
/*     */ {
/*     */   private final XmlReaderContext readerContext;
/*     */   private final BeanDefinitionParserDelegate delegate;
/*     */   private BeanDefinition containingBeanDefinition;
/*  47 */   private final Stack<CompositeComponentDefinition> containingComponents = new Stack<CompositeComponentDefinition>();
/*     */ 
/*     */   
/*     */   public ParserContext(XmlReaderContext readerContext, BeanDefinitionParserDelegate delegate) {
/*  51 */     this.readerContext = readerContext;
/*  52 */     this.delegate = delegate;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ParserContext(XmlReaderContext readerContext, BeanDefinitionParserDelegate delegate, BeanDefinition containingBeanDefinition) {
/*  58 */     this.readerContext = readerContext;
/*  59 */     this.delegate = delegate;
/*  60 */     this.containingBeanDefinition = containingBeanDefinition;
/*     */   }
/*     */ 
/*     */   
/*     */   public final XmlReaderContext getReaderContext() {
/*  65 */     return this.readerContext;
/*     */   }
/*     */   
/*     */   public final BeanDefinitionRegistry getRegistry() {
/*  69 */     return this.readerContext.getRegistry();
/*     */   }
/*     */   
/*     */   public final BeanDefinitionParserDelegate getDelegate() {
/*  73 */     return this.delegate;
/*     */   }
/*     */   
/*     */   public final BeanDefinition getContainingBeanDefinition() {
/*  77 */     return this.containingBeanDefinition;
/*     */   }
/*     */   
/*     */   public final boolean isNested() {
/*  81 */     return (this.containingBeanDefinition != null);
/*     */   }
/*     */   
/*     */   public boolean isDefaultLazyInit() {
/*  85 */     return "true".equals(this.delegate.getDefaults().getLazyInit());
/*     */   }
/*     */   
/*     */   public Object extractSource(Object sourceCandidate) {
/*  89 */     return this.readerContext.extractSource(sourceCandidate);
/*     */   }
/*     */   
/*     */   public CompositeComponentDefinition getContainingComponent() {
/*  93 */     return !this.containingComponents.isEmpty() ? this.containingComponents.lastElement() : null;
/*     */   }
/*     */   
/*     */   public void pushContainingComponent(CompositeComponentDefinition containingComponent) {
/*  97 */     this.containingComponents.push(containingComponent);
/*     */   }
/*     */   
/*     */   public CompositeComponentDefinition popContainingComponent() {
/* 101 */     return this.containingComponents.pop();
/*     */   }
/*     */   
/*     */   public void popAndRegisterContainingComponent() {
/* 105 */     registerComponent((ComponentDefinition)popContainingComponent());
/*     */   }
/*     */   
/*     */   public void registerComponent(ComponentDefinition component) {
/* 109 */     CompositeComponentDefinition containingComponent = getContainingComponent();
/* 110 */     if (containingComponent != null) {
/* 111 */       containingComponent.addNestedComponent(component);
/*     */     } else {
/*     */       
/* 114 */       this.readerContext.fireComponentRegistered(component);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void registerBeanComponent(BeanComponentDefinition component) {
/* 119 */     BeanDefinitionReaderUtils.registerBeanDefinition((BeanDefinitionHolder)component, getRegistry());
/* 120 */     registerComponent((ComponentDefinition)component);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\xml\ParserContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */