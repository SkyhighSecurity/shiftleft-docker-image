/*     */ package org.springframework.beans.factory.xml;
/*     */ 
/*     */ import org.springframework.beans.factory.support.AbstractBeanDefinition;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionBuilder;
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
/*     */ public abstract class AbstractSingleBeanDefinitionParser
/*     */   extends AbstractBeanDefinitionParser
/*     */ {
/*     */   protected final AbstractBeanDefinition parseInternal(Element element, ParserContext parserContext) {
/*  61 */     BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition();
/*  62 */     String parentName = getParentName(element);
/*  63 */     if (parentName != null) {
/*  64 */       builder.getRawBeanDefinition().setParentName(parentName);
/*     */     }
/*  66 */     Class<?> beanClass = getBeanClass(element);
/*  67 */     if (beanClass != null) {
/*  68 */       builder.getRawBeanDefinition().setBeanClass(beanClass);
/*     */     } else {
/*     */       
/*  71 */       String beanClassName = getBeanClassName(element);
/*  72 */       if (beanClassName != null) {
/*  73 */         builder.getRawBeanDefinition().setBeanClassName(beanClassName);
/*     */       }
/*     */     } 
/*  76 */     builder.getRawBeanDefinition().setSource(parserContext.extractSource(element));
/*  77 */     if (parserContext.isNested())
/*     */     {
/*  79 */       builder.setScope(parserContext.getContainingBeanDefinition().getScope());
/*     */     }
/*  81 */     if (parserContext.isDefaultLazyInit())
/*     */     {
/*  83 */       builder.setLazyInit(true);
/*     */     }
/*  85 */     doParse(element, parserContext, builder);
/*  86 */     return builder.getBeanDefinition();
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
/*     */   protected String getParentName(Element element) {
/*  99 */     return null;
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
/*     */   protected Class<?> getBeanClass(Element element) {
/* 115 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getBeanClassName(Element element) {
/* 126 */     return null;
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
/*     */   protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
/* 140 */     doParse(element, builder);
/*     */   }
/*     */   
/*     */   protected void doParse(Element element, BeanDefinitionBuilder builder) {}
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\xml\AbstractSingleBeanDefinitionParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */