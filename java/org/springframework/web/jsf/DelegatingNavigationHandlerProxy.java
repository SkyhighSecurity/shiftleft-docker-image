/*     */ package org.springframework.web.jsf;
/*     */ 
/*     */ import javax.faces.application.NavigationHandler;
/*     */ import javax.faces.context.FacesContext;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.web.context.WebApplicationContext;
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
/*     */ public class DelegatingNavigationHandlerProxy
/*     */   extends NavigationHandler
/*     */ {
/*     */   public static final String DEFAULT_TARGET_BEAN_NAME = "jsfNavigationHandler";
/*     */   private NavigationHandler originalNavigationHandler;
/*     */   
/*     */   public DelegatingNavigationHandlerProxy() {}
/*     */   
/*     */   public DelegatingNavigationHandlerProxy(NavigationHandler originalNavigationHandler) {
/*  95 */     this.originalNavigationHandler = originalNavigationHandler;
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
/*     */   public void handleNavigation(FacesContext facesContext, String fromAction, String outcome) {
/* 110 */     NavigationHandler handler = getDelegate(facesContext);
/* 111 */     if (handler instanceof DecoratingNavigationHandler) {
/* 112 */       ((DecoratingNavigationHandler)handler).handleNavigation(facesContext, fromAction, outcome, this.originalNavigationHandler);
/*     */     }
/*     */     else {
/*     */       
/* 116 */       handler.handleNavigation(facesContext, fromAction, outcome);
/*     */     } 
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
/*     */   protected NavigationHandler getDelegate(FacesContext facesContext) {
/* 130 */     String targetBeanName = getTargetBeanName(facesContext);
/* 131 */     return (NavigationHandler)getBeanFactory(facesContext).getBean(targetBeanName, NavigationHandler.class);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getTargetBeanName(FacesContext facesContext) {
/* 141 */     return "jsfNavigationHandler";
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
/*     */   protected BeanFactory getBeanFactory(FacesContext facesContext) {
/* 154 */     return (BeanFactory)getWebApplicationContext(facesContext);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected WebApplicationContext getWebApplicationContext(FacesContext facesContext) {
/* 165 */     return FacesContextUtils.getRequiredWebApplicationContext(facesContext);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\jsf\DelegatingNavigationHandlerProxy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */