/*    */ package org.springframework.scripting.config;
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
/*    */ public class LangNamespaceHandler
/*    */   extends NamespaceHandlerSupport
/*    */ {
/*    */   public void init() {
/* 44 */     registerScriptBeanDefinitionParser("groovy", "org.springframework.scripting.groovy.GroovyScriptFactory");
/* 45 */     registerScriptBeanDefinitionParser("jruby", "org.springframework.scripting.jruby.JRubyScriptFactory");
/* 46 */     registerScriptBeanDefinitionParser("bsh", "org.springframework.scripting.bsh.BshScriptFactory");
/* 47 */     registerScriptBeanDefinitionParser("std", "org.springframework.scripting.support.StandardScriptFactory");
/* 48 */     registerBeanDefinitionParser("defaults", new ScriptingDefaultsParser());
/*    */   }
/*    */   
/*    */   private void registerScriptBeanDefinitionParser(String key, String scriptFactoryClassName) {
/* 52 */     registerBeanDefinitionParser(key, (BeanDefinitionParser)new ScriptBeanDefinitionParser(scriptFactoryClassName));
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\scripting\config\LangNamespaceHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */