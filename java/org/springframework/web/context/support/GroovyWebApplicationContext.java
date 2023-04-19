/*     */ package org.springframework.web.context.support;
/*     */ 
/*     */ import groovy.lang.GroovyObject;
/*     */ import groovy.lang.GroovySystem;
/*     */ import groovy.lang.MetaClass;
/*     */ import java.io.IOException;
/*     */ import org.springframework.beans.BeanWrapper;
/*     */ import org.springframework.beans.BeanWrapperImpl;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*     */ import org.springframework.beans.factory.groovy.GroovyBeanDefinitionReader;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*     */ import org.springframework.beans.factory.support.DefaultListableBeanFactory;
/*     */ import org.springframework.core.env.Environment;
/*     */ import org.springframework.core.io.ResourceLoader;
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
/*     */ public class GroovyWebApplicationContext
/*     */   extends AbstractRefreshableWebApplicationContext
/*     */   implements GroovyObject
/*     */ {
/*     */   public static final String DEFAULT_CONFIG_LOCATION = "/WEB-INF/applicationContext.groovy";
/*     */   public static final String DEFAULT_CONFIG_LOCATION_PREFIX = "/WEB-INF/";
/*     */   public static final String DEFAULT_CONFIG_LOCATION_SUFFIX = ".groovy";
/*  81 */   private final BeanWrapper contextWrapper = (BeanWrapper)new BeanWrapperImpl(this);
/*     */   
/*  83 */   private MetaClass metaClass = GroovySystem.getMetaClassRegistry().getMetaClass(getClass());
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
/*     */   protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) throws BeansException, IOException {
/*  95 */     GroovyBeanDefinitionReader beanDefinitionReader = new GroovyBeanDefinitionReader((BeanDefinitionRegistry)beanFactory);
/*     */ 
/*     */ 
/*     */     
/*  99 */     beanDefinitionReader.setEnvironment((Environment)getEnvironment());
/* 100 */     beanDefinitionReader.setResourceLoader((ResourceLoader)this);
/*     */ 
/*     */ 
/*     */     
/* 104 */     initBeanDefinitionReader(beanDefinitionReader);
/* 105 */     loadBeanDefinitions(beanDefinitionReader);
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
/*     */   protected void initBeanDefinitionReader(GroovyBeanDefinitionReader beanDefinitionReader) {}
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
/*     */   protected void loadBeanDefinitions(GroovyBeanDefinitionReader reader) throws IOException {
/* 130 */     String[] configLocations = getConfigLocations();
/* 131 */     if (configLocations != null) {
/* 132 */       for (String configLocation : configLocations) {
/* 133 */         reader.loadBeanDefinitions(configLocation);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String[] getDefaultConfigLocations() {
/* 145 */     if (getNamespace() != null) {
/* 146 */       return new String[] { "/WEB-INF/" + getNamespace() + ".groovy" };
/*     */     }
/*     */     
/* 149 */     return new String[] { "/WEB-INF/applicationContext.groovy" };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMetaClass(MetaClass metaClass) {
/* 157 */     this.metaClass = metaClass;
/*     */   }
/*     */   
/*     */   public MetaClass getMetaClass() {
/* 161 */     return this.metaClass;
/*     */   }
/*     */   
/*     */   public Object invokeMethod(String name, Object args) {
/* 165 */     return this.metaClass.invokeMethod(this, name, args);
/*     */   }
/*     */   
/*     */   public void setProperty(String property, Object newValue) {
/* 169 */     this.metaClass.setProperty(this, property, newValue);
/*     */   }
/*     */   
/*     */   public Object getProperty(String property) {
/* 173 */     if (containsBean(property)) {
/* 174 */       return getBean(property);
/*     */     }
/* 176 */     if (this.contextWrapper.isReadableProperty(property)) {
/* 177 */       return this.contextWrapper.getPropertyValue(property);
/*     */     }
/* 179 */     throw new NoSuchBeanDefinitionException(property);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\context\support\GroovyWebApplicationContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */