/*     */ package org.springframework.aop.aspectj.annotation;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
/*     */ import org.springframework.core.Ordered;
/*     */ import org.springframework.core.annotation.OrderUtils;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BeanFactoryAspectInstanceFactory
/*     */   implements MetadataAwareAspectInstanceFactory, Serializable
/*     */ {
/*     */   private final BeanFactory beanFactory;
/*     */   private final String name;
/*     */   private final AspectMetadata aspectMetadata;
/*     */   
/*     */   public BeanFactoryAspectInstanceFactory(BeanFactory beanFactory, String name) {
/*  61 */     this(beanFactory, name, beanFactory.getType(name));
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
/*     */   public BeanFactoryAspectInstanceFactory(BeanFactory beanFactory, String name, Class<?> type) {
/*  73 */     Assert.notNull(beanFactory, "BeanFactory must not be null");
/*  74 */     Assert.notNull(name, "Bean name must not be null");
/*  75 */     this.beanFactory = beanFactory;
/*  76 */     this.name = name;
/*  77 */     this.aspectMetadata = new AspectMetadata(type, name);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getAspectInstance() {
/*  83 */     return this.beanFactory.getBean(this.name);
/*     */   }
/*     */ 
/*     */   
/*     */   public ClassLoader getAspectClassLoader() {
/*  88 */     return (this.beanFactory instanceof ConfigurableBeanFactory) ? ((ConfigurableBeanFactory)this.beanFactory)
/*  89 */       .getBeanClassLoader() : 
/*  90 */       ClassUtils.getDefaultClassLoader();
/*     */   }
/*     */ 
/*     */   
/*     */   public AspectMetadata getAspectMetadata() {
/*  95 */     return this.aspectMetadata;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getAspectCreationMutex() {
/* 100 */     if (this.beanFactory != null) {
/* 101 */       if (this.beanFactory.isSingleton(this.name))
/*     */       {
/* 103 */         return null;
/*     */       }
/* 105 */       if (this.beanFactory instanceof ConfigurableBeanFactory)
/*     */       {
/*     */ 
/*     */         
/* 109 */         return ((ConfigurableBeanFactory)this.beanFactory).getSingletonMutex();
/*     */       }
/*     */     } 
/* 112 */     return this;
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
/*     */   public int getOrder() {
/* 127 */     Class<?> type = this.beanFactory.getType(this.name);
/* 128 */     if (type != null) {
/* 129 */       if (Ordered.class.isAssignableFrom(type) && this.beanFactory.isSingleton(this.name)) {
/* 130 */         return ((Ordered)this.beanFactory.getBean(this.name)).getOrder();
/*     */       }
/* 132 */       return OrderUtils.getOrder(type, Integer.valueOf(2147483647)).intValue();
/*     */     } 
/* 134 */     return Integer.MAX_VALUE;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 140 */     return getClass().getSimpleName() + ": bean name '" + this.name + "'";
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\aspectj\annotation\BeanFactoryAspectInstanceFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */