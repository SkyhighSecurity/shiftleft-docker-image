/*     */ package org.springframework.beans.factory.xml;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*     */ import org.springframework.beans.factory.config.ConstructorArgumentValues;
/*     */ import org.springframework.beans.factory.config.RuntimeBeanReference;
/*     */ import org.springframework.core.Conventions;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.w3c.dom.Attr;
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
/*     */ public class SimpleConstructorNamespaceHandler
/*     */   implements NamespaceHandler
/*     */ {
/*     */   private static final String REF_SUFFIX = "-ref";
/*     */   private static final String DELIMITER_PREFIX = "_";
/*     */   
/*     */   public void init() {}
/*     */   
/*     */   public BeanDefinition parse(Element element, ParserContext parserContext) {
/*  72 */     parserContext.getReaderContext().error("Class [" + 
/*  73 */         getClass().getName() + "] does not support custom elements.", element);
/*  74 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public BeanDefinitionHolder decorate(Node node, BeanDefinitionHolder definition, ParserContext parserContext) {
/*  79 */     if (node instanceof Attr) {
/*  80 */       Attr attr = (Attr)node;
/*  81 */       String argName = StringUtils.trimWhitespace(parserContext.getDelegate().getLocalName(attr));
/*  82 */       String argValue = StringUtils.trimWhitespace(attr.getValue());
/*     */       
/*  84 */       ConstructorArgumentValues cvs = definition.getBeanDefinition().getConstructorArgumentValues();
/*  85 */       boolean ref = false;
/*     */ 
/*     */       
/*  88 */       if (argName.endsWith("-ref")) {
/*  89 */         ref = true;
/*  90 */         argName = argName.substring(0, argName.length() - "-ref".length());
/*     */       } 
/*     */       
/*  93 */       ConstructorArgumentValues.ValueHolder valueHolder = new ConstructorArgumentValues.ValueHolder(ref ? new RuntimeBeanReference(argValue) : argValue);
/*  94 */       valueHolder.setSource(parserContext.getReaderContext().extractSource(attr));
/*     */ 
/*     */       
/*  97 */       if (argName.startsWith("_")) {
/*  98 */         String arg = argName.substring(1).trim();
/*     */ 
/*     */         
/* 101 */         if (!StringUtils.hasText(arg)) {
/* 102 */           cvs.addGenericArgumentValue(valueHolder);
/*     */         }
/*     */         else {
/*     */           
/* 106 */           int index = -1;
/*     */           try {
/* 108 */             index = Integer.parseInt(arg);
/*     */           }
/* 110 */           catch (NumberFormatException ex) {
/* 111 */             parserContext.getReaderContext().error("Constructor argument '" + argName + "' specifies an invalid integer", attr);
/*     */           } 
/*     */           
/* 114 */           if (index < 0) {
/* 115 */             parserContext.getReaderContext().error("Constructor argument '" + argName + "' specifies a negative index", attr);
/*     */           }
/*     */ 
/*     */           
/* 119 */           if (cvs.hasIndexedArgumentValue(index)) {
/* 120 */             parserContext.getReaderContext().error("Constructor argument '" + argName + "' with index " + index + " already defined using <constructor-arg>. Only one approach may be used per argument.", attr);
/*     */           }
/*     */ 
/*     */ 
/*     */           
/* 125 */           cvs.addIndexedArgumentValue(index, valueHolder);
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/* 130 */         String name = Conventions.attributeNameToPropertyName(argName);
/* 131 */         if (containsArgWithName(name, cvs)) {
/* 132 */           parserContext.getReaderContext().error("Constructor argument '" + argName + "' already defined using <constructor-arg>. Only one approach may be used per argument.", attr);
/*     */         }
/*     */ 
/*     */         
/* 136 */         valueHolder.setName(Conventions.attributeNameToPropertyName(argName));
/* 137 */         cvs.addGenericArgumentValue(valueHolder);
/*     */       } 
/*     */     } 
/* 140 */     return definition;
/*     */   }
/*     */   
/*     */   private boolean containsArgWithName(String name, ConstructorArgumentValues cvs) {
/* 144 */     return (checkName(name, cvs.getGenericArgumentValues()) || 
/* 145 */       checkName(name, cvs.getIndexedArgumentValues().values()));
/*     */   }
/*     */   
/*     */   private boolean checkName(String name, Collection<ConstructorArgumentValues.ValueHolder> values) {
/* 149 */     for (ConstructorArgumentValues.ValueHolder holder : values) {
/* 150 */       if (name.equals(holder.getName())) {
/* 151 */         return true;
/*     */       }
/*     */     } 
/* 154 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\xml\SimpleConstructorNamespaceHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */