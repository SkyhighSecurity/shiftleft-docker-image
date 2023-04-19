/*    */ package org.springframework.scripting.config;
/*    */ 
/*    */ import org.springframework.beans.factory.config.BeanDefinition;
/*    */ import org.springframework.beans.factory.config.TypedStringValue;
/*    */ import org.springframework.beans.factory.xml.BeanDefinitionParser;
/*    */ import org.springframework.beans.factory.xml.ParserContext;
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
/*    */ 
/*    */ class ScriptingDefaultsParser
/*    */   implements BeanDefinitionParser
/*    */ {
/*    */   private static final String REFRESH_CHECK_DELAY_ATTRIBUTE = "refresh-check-delay";
/*    */   private static final String PROXY_TARGET_CLASS_ATTRIBUTE = "proxy-target-class";
/*    */   
/*    */   public BeanDefinition parse(Element element, ParserContext parserContext) {
/* 41 */     BeanDefinition bd = LangNamespaceUtils.registerScriptFactoryPostProcessorIfNecessary(parserContext.getRegistry());
/* 42 */     String refreshCheckDelay = element.getAttribute("refresh-check-delay");
/* 43 */     if (StringUtils.hasText(refreshCheckDelay)) {
/* 44 */       bd.getPropertyValues().add("defaultRefreshCheckDelay", Long.valueOf(refreshCheckDelay));
/*    */     }
/* 46 */     String proxyTargetClass = element.getAttribute("proxy-target-class");
/* 47 */     if (StringUtils.hasText(proxyTargetClass)) {
/* 48 */       bd.getPropertyValues().add("defaultProxyTargetClass", new TypedStringValue(proxyTargetClass, Boolean.class));
/*    */     }
/* 50 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\scripting\config\ScriptingDefaultsParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */