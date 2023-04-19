/*    */ package org.springframework.web.jsf.el;
/*    */ 
/*    */ import javax.el.ELContext;
/*    */ import javax.faces.context.FacesContext;
/*    */ import org.springframework.beans.factory.BeanFactory;
/*    */ import org.springframework.beans.factory.access.el.SpringBeanELResolver;
/*    */ import org.springframework.web.context.WebApplicationContext;
/*    */ import org.springframework.web.jsf.FacesContextUtils;
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
/*    */ public class SpringBeanFacesELResolver
/*    */   extends SpringBeanELResolver
/*    */ {
/*    */   protected BeanFactory getBeanFactory(ELContext elContext) {
/* 78 */     return (BeanFactory)getWebApplicationContext(elContext);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected WebApplicationContext getWebApplicationContext(ELContext elContext) {
/* 89 */     FacesContext facesContext = FacesContext.getCurrentInstance();
/* 90 */     return FacesContextUtils.getRequiredWebApplicationContext(facesContext);
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\jsf\el\SpringBeanFacesELResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */