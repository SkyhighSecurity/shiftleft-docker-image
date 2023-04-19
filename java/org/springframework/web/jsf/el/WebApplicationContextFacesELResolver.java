/*     */ package org.springframework.web.jsf.el;
/*     */ 
/*     */ import java.beans.FeatureDescriptor;
/*     */ import java.util.Iterator;
/*     */ import javax.el.ELContext;
/*     */ import javax.el.ELException;
/*     */ import javax.el.ELResolver;
/*     */ import javax.faces.context.FacesContext;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.web.context.WebApplicationContext;
/*     */ import org.springframework.web.jsf.FacesContextUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WebApplicationContextFacesELResolver
/*     */   extends ELResolver
/*     */ {
/*     */   public static final String WEB_APPLICATION_CONTEXT_VARIABLE_NAME = "webApplicationContext";
/*  65 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getValue(ELContext elContext, Object base, Object property) throws ELException {
/*  70 */     if (base != null) {
/*  71 */       if (base instanceof WebApplicationContext) {
/*  72 */         WebApplicationContext wac = (WebApplicationContext)base;
/*  73 */         String beanName = property.toString();
/*  74 */         if (this.logger.isTraceEnabled()) {
/*  75 */           this.logger.trace("Attempting to resolve property '" + beanName + "' in root WebApplicationContext");
/*     */         }
/*  77 */         if (wac.containsBean(beanName)) {
/*  78 */           if (this.logger.isDebugEnabled()) {
/*  79 */             this.logger.debug("Successfully resolved property '" + beanName + "' in root WebApplicationContext");
/*     */           }
/*  81 */           elContext.setPropertyResolved(true);
/*     */           try {
/*  83 */             return wac.getBean(beanName);
/*     */           }
/*  85 */           catch (BeansException ex) {
/*  86 */             throw new ELException(ex);
/*     */           } 
/*     */         } 
/*     */ 
/*     */         
/*  91 */         return null;
/*     */       
/*     */       }
/*     */     
/*     */     }
/*  96 */     else if ("webApplicationContext".equals(property)) {
/*  97 */       elContext.setPropertyResolved(true);
/*  98 */       return getWebApplicationContext(elContext);
/*     */     } 
/*     */ 
/*     */     
/* 102 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?> getType(ELContext elContext, Object base, Object property) throws ELException {
/* 107 */     if (base != null) {
/* 108 */       if (base instanceof WebApplicationContext) {
/* 109 */         WebApplicationContext wac = (WebApplicationContext)base;
/* 110 */         String beanName = property.toString();
/* 111 */         if (this.logger.isDebugEnabled()) {
/* 112 */           this.logger.debug("Attempting to resolve property '" + beanName + "' in root WebApplicationContext");
/*     */         }
/* 114 */         if (wac.containsBean(beanName)) {
/* 115 */           if (this.logger.isDebugEnabled()) {
/* 116 */             this.logger.debug("Successfully resolved property '" + beanName + "' in root WebApplicationContext");
/*     */           }
/* 118 */           elContext.setPropertyResolved(true);
/*     */           try {
/* 120 */             return wac.getType(beanName);
/*     */           }
/* 122 */           catch (BeansException ex) {
/* 123 */             throw new ELException(ex);
/*     */           } 
/*     */         } 
/*     */ 
/*     */         
/* 128 */         return null;
/*     */       
/*     */       }
/*     */     
/*     */     }
/* 133 */     else if ("webApplicationContext".equals(property)) {
/* 134 */       elContext.setPropertyResolved(true);
/* 135 */       return WebApplicationContext.class;
/*     */     } 
/*     */ 
/*     */     
/* 139 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValue(ELContext elContext, Object base, Object property, Object value) throws ELException {}
/*     */ 
/*     */   
/*     */   public boolean isReadOnly(ELContext elContext, Object base, Object property) throws ELException {
/* 148 */     if (base instanceof WebApplicationContext) {
/* 149 */       elContext.setPropertyResolved(true);
/* 150 */       return true;
/*     */     } 
/* 152 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext elContext, Object base) {
/* 157 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?> getCommonPropertyType(ELContext elContext, Object base) {
/* 162 */     return Object.class;
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
/*     */   protected WebApplicationContext getWebApplicationContext(ELContext elContext) {
/* 175 */     FacesContext facesContext = FacesContext.getCurrentInstance();
/* 176 */     return FacesContextUtils.getRequiredWebApplicationContext(facesContext);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\jsf\el\WebApplicationContextFacesELResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */