/*     */ package org.springframework.beans.factory.access.el;
/*     */ 
/*     */ import java.beans.FeatureDescriptor;
/*     */ import java.util.Iterator;
/*     */ import javax.el.ELContext;
/*     */ import javax.el.ELException;
/*     */ import javax.el.ELResolver;
/*     */ import javax.el.PropertyNotWritableException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class SpringBeanELResolver
/*     */   extends ELResolver
/*     */ {
/*  42 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getValue(ELContext elContext, Object base, Object property) throws ELException {
/*  47 */     if (base == null) {
/*  48 */       String beanName = property.toString();
/*  49 */       BeanFactory bf = getBeanFactory(elContext);
/*  50 */       if (bf.containsBean(beanName)) {
/*  51 */         if (this.logger.isTraceEnabled()) {
/*  52 */           this.logger.trace("Successfully resolved variable '" + beanName + "' in Spring BeanFactory");
/*     */         }
/*  54 */         elContext.setPropertyResolved(true);
/*  55 */         return bf.getBean(beanName);
/*     */       } 
/*     */     } 
/*  58 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?> getType(ELContext elContext, Object base, Object property) throws ELException {
/*  63 */     if (base == null) {
/*  64 */       String beanName = property.toString();
/*  65 */       BeanFactory bf = getBeanFactory(elContext);
/*  66 */       if (bf.containsBean(beanName)) {
/*  67 */         elContext.setPropertyResolved(true);
/*  68 */         return bf.getType(beanName);
/*     */       } 
/*     */     } 
/*  71 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setValue(ELContext elContext, Object base, Object property, Object value) throws ELException {
/*  76 */     if (base == null) {
/*  77 */       String beanName = property.toString();
/*  78 */       BeanFactory bf = getBeanFactory(elContext);
/*  79 */       if (bf.containsBean(beanName)) {
/*  80 */         if (value == bf.getBean(beanName)) {
/*     */           
/*  82 */           elContext.setPropertyResolved(true);
/*     */         } else {
/*     */           
/*  85 */           throw new PropertyNotWritableException("Variable '" + beanName + "' refers to a Spring bean which by definition is not writable");
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isReadOnly(ELContext elContext, Object base, Object property) throws ELException {
/*  94 */     if (base == null) {
/*  95 */       String beanName = property.toString();
/*  96 */       BeanFactory bf = getBeanFactory(elContext);
/*  97 */       if (bf.containsBean(beanName)) {
/*  98 */         return true;
/*     */       }
/*     */     } 
/* 101 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext elContext, Object base) {
/* 106 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?> getCommonPropertyType(ELContext elContext, Object base) {
/* 111 */     return Object.class;
/*     */   }
/*     */   
/*     */   protected abstract BeanFactory getBeanFactory(ELContext paramELContext);
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\access\el\SpringBeanELResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */