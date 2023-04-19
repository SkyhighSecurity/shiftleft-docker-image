/*     */ package org.springframework.beans.factory.xml;
/*     */ 
/*     */ import org.springframework.beans.factory.BeanDefinitionStoreException;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*     */ import org.springframework.beans.factory.parsing.BeanComponentDefinition;
/*     */ import org.springframework.beans.factory.parsing.ComponentDefinition;
/*     */ import org.springframework.beans.factory.support.AbstractBeanDefinition;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.w3c.dom.Element;
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
/*     */ public abstract class AbstractBeanDefinitionParser
/*     */   implements BeanDefinitionParser
/*     */ {
/*     */   public static final String ID_ATTRIBUTE = "id";
/*     */   public static final String NAME_ATTRIBUTE = "name";
/*     */   
/*     */   public final BeanDefinition parse(Element element, ParserContext parserContext) {
/*  61 */     AbstractBeanDefinition definition = parseInternal(element, parserContext);
/*  62 */     if (definition != null && !parserContext.isNested()) {
/*     */       try {
/*  64 */         String id = resolveId(element, definition, parserContext);
/*  65 */         if (!StringUtils.hasText(id)) {
/*  66 */           parserContext.getReaderContext().error("Id is required for element '" + parserContext
/*  67 */               .getDelegate().getLocalName(element) + "' when used as a top-level tag", element);
/*     */         }
/*     */         
/*  70 */         String[] aliases = null;
/*  71 */         if (shouldParseNameAsAliases()) {
/*  72 */           String name = element.getAttribute("name");
/*  73 */           if (StringUtils.hasLength(name)) {
/*  74 */             aliases = StringUtils.trimArrayElements(StringUtils.commaDelimitedListToStringArray(name));
/*     */           }
/*     */         } 
/*  77 */         BeanDefinitionHolder holder = new BeanDefinitionHolder((BeanDefinition)definition, id, aliases);
/*  78 */         registerBeanDefinition(holder, parserContext.getRegistry());
/*  79 */         if (shouldFireEvents()) {
/*  80 */           BeanComponentDefinition componentDefinition = new BeanComponentDefinition(holder);
/*  81 */           postProcessComponentDefinition(componentDefinition);
/*  82 */           parserContext.registerComponent((ComponentDefinition)componentDefinition);
/*     */         }
/*     */       
/*  85 */       } catch (BeanDefinitionStoreException ex) {
/*  86 */         parserContext.getReaderContext().error(ex.getMessage(), element);
/*  87 */         return null;
/*     */       } 
/*     */     }
/*  90 */     return (BeanDefinition)definition;
/*     */   }
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
/*     */   protected String resolveId(Element element, AbstractBeanDefinition definition, ParserContext parserContext) throws BeanDefinitionStoreException {
/* 109 */     if (shouldGenerateId()) {
/* 110 */       return parserContext.getReaderContext().generateBeanName((BeanDefinition)definition);
/*     */     }
/*     */     
/* 113 */     String id = element.getAttribute("id");
/* 114 */     if (!StringUtils.hasText(id) && shouldGenerateIdAsFallback()) {
/* 115 */       id = parserContext.getReaderContext().generateBeanName((BeanDefinition)definition);
/*     */     }
/* 117 */     return id;
/*     */   }
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
/*     */   protected void registerBeanDefinition(BeanDefinitionHolder definition, BeanDefinitionRegistry registry) {
/* 136 */     BeanDefinitionReaderUtils.registerBeanDefinition(definition, registry);
/*     */   }
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
/*     */   protected abstract AbstractBeanDefinition parseInternal(Element paramElement, ParserContext paramParserContext);
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
/*     */   protected boolean shouldGenerateId() {
/* 160 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean shouldGenerateIdAsFallback() {
/* 172 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean shouldParseNameAsAliases() {
/* 183 */     return true;
/*     */   }
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
/*     */   protected boolean shouldFireEvents() {
/* 199 */     return true;
/*     */   }
/*     */   
/*     */   protected void postProcessComponentDefinition(BeanComponentDefinition componentDefinition) {}
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\xml\AbstractBeanDefinitionParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */