/*    */ package org.springframework.web.context.support;
/*    */ 
/*    */ import java.util.Enumeration;
/*    */ import java.util.LinkedHashSet;
/*    */ import java.util.Set;
/*    */ import javax.servlet.ServletContext;
/*    */ import org.springframework.context.ConfigurableApplicationContext;
/*    */ import org.springframework.context.support.LiveBeansView;
/*    */ import org.springframework.util.Assert;
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
/*    */ public class ServletContextLiveBeansView
/*    */   extends LiveBeansView
/*    */ {
/*    */   private final ServletContext servletContext;
/*    */   
/*    */   public ServletContextLiveBeansView(ServletContext servletContext) {
/* 44 */     Assert.notNull(servletContext, "ServletContext must not be null");
/* 45 */     this.servletContext = servletContext;
/*    */   }
/*    */ 
/*    */   
/*    */   protected Set<ConfigurableApplicationContext> findApplicationContexts() {
/* 50 */     Set<ConfigurableApplicationContext> contexts = new LinkedHashSet<ConfigurableApplicationContext>();
/* 51 */     Enumeration<String> attrNames = this.servletContext.getAttributeNames();
/* 52 */     while (attrNames.hasMoreElements()) {
/* 53 */       String attrName = attrNames.nextElement();
/* 54 */       Object attrValue = this.servletContext.getAttribute(attrName);
/* 55 */       if (attrValue instanceof ConfigurableApplicationContext) {
/* 56 */         contexts.add((ConfigurableApplicationContext)attrValue);
/*    */       }
/*    */     } 
/* 59 */     return contexts;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\context\support\ServletContextLiveBeansView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */