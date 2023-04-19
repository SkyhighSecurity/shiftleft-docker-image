/*     */ package org.springframework.context.support;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.MutablePropertyValues;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.support.GenericBeanDefinition;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StaticApplicationContext
/*     */   extends GenericApplicationContext
/*     */ {
/*     */   private final StaticMessageSource staticMessageSource;
/*     */   
/*     */   public StaticApplicationContext() throws BeansException {
/*  52 */     this((ApplicationContext)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StaticApplicationContext(ApplicationContext parent) throws BeansException {
/*  63 */     super(parent);
/*     */ 
/*     */     
/*  66 */     this.staticMessageSource = new StaticMessageSource();
/*  67 */     getBeanFactory().registerSingleton("messageSource", this.staticMessageSource);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void assertBeanFactoryActive() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final StaticMessageSource getStaticMessageSource() {
/*  84 */     return this.staticMessageSource;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerSingleton(String name, Class<?> clazz) throws BeansException {
/*  93 */     GenericBeanDefinition bd = new GenericBeanDefinition();
/*  94 */     bd.setBeanClass(clazz);
/*  95 */     getDefaultListableBeanFactory().registerBeanDefinition(name, (BeanDefinition)bd);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerSingleton(String name, Class<?> clazz, MutablePropertyValues pvs) throws BeansException {
/* 104 */     GenericBeanDefinition bd = new GenericBeanDefinition();
/* 105 */     bd.setBeanClass(clazz);
/* 106 */     bd.setPropertyValues(pvs);
/* 107 */     getDefaultListableBeanFactory().registerBeanDefinition(name, (BeanDefinition)bd);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerPrototype(String name, Class<?> clazz) throws BeansException {
/* 116 */     GenericBeanDefinition bd = new GenericBeanDefinition();
/* 117 */     bd.setScope("prototype");
/* 118 */     bd.setBeanClass(clazz);
/* 119 */     getDefaultListableBeanFactory().registerBeanDefinition(name, (BeanDefinition)bd);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerPrototype(String name, Class<?> clazz, MutablePropertyValues pvs) throws BeansException {
/* 128 */     GenericBeanDefinition bd = new GenericBeanDefinition();
/* 129 */     bd.setScope("prototype");
/* 130 */     bd.setBeanClass(clazz);
/* 131 */     bd.setPropertyValues(pvs);
/* 132 */     getDefaultListableBeanFactory().registerBeanDefinition(name, (BeanDefinition)bd);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addMessage(String code, Locale locale, String defaultMessage) {
/* 143 */     getStaticMessageSource().addMessage(code, locale, defaultMessage);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\support\StaticApplicationContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */