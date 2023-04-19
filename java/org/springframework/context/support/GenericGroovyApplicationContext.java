/*     */ package org.springframework.context.support;
/*     */ 
/*     */ import groovy.lang.GroovyObject;
/*     */ import groovy.lang.GroovySystem;
/*     */ import groovy.lang.MetaClass;
/*     */ import org.springframework.beans.BeanWrapper;
/*     */ import org.springframework.beans.BeanWrapperImpl;
/*     */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.groovy.GroovyBeanDefinitionReader;
/*     */ import org.springframework.core.env.ConfigurableEnvironment;
/*     */ import org.springframework.core.env.Environment;
/*     */ import org.springframework.core.io.ClassPathResource;
/*     */ import org.springframework.core.io.Resource;
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
/*     */ public class GenericGroovyApplicationContext
/*     */   extends GenericApplicationContext
/*     */   implements GroovyObject
/*     */ {
/* 120 */   private final GroovyBeanDefinitionReader reader = new GroovyBeanDefinitionReader(this);
/*     */   
/* 122 */   private final BeanWrapper contextWrapper = (BeanWrapper)new BeanWrapperImpl(this);
/*     */   
/* 124 */   private MetaClass metaClass = GroovySystem.getMetaClassRegistry().getMetaClass(getClass());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GenericGroovyApplicationContext() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GenericGroovyApplicationContext(Resource... resources) {
/* 140 */     load(resources);
/* 141 */     refresh();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GenericGroovyApplicationContext(String... resourceLocations) {
/* 150 */     load(resourceLocations);
/* 151 */     refresh();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GenericGroovyApplicationContext(Class<?> relativeClass, String... resourceNames) {
/* 162 */     load(relativeClass, resourceNames);
/* 163 */     refresh();
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
/*     */   public final GroovyBeanDefinitionReader getReader() {
/* 175 */     return this.reader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEnvironment(ConfigurableEnvironment environment) {
/* 184 */     super.setEnvironment(environment);
/* 185 */     this.reader.setEnvironment((Environment)getEnvironment());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void load(Resource... resources) {
/* 195 */     this.reader.loadBeanDefinitions(resources);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void load(String... resourceLocations) {
/* 205 */     this.reader.loadBeanDefinitions(resourceLocations);
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
/*     */   public void load(Class<?> relativeClass, String... resourceNames) {
/* 217 */     Resource[] resources = new Resource[resourceNames.length];
/* 218 */     for (int i = 0; i < resourceNames.length; i++) {
/* 219 */       resources[i] = (Resource)new ClassPathResource(resourceNames[i], relativeClass);
/*     */     }
/* 221 */     load(resources);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMetaClass(MetaClass metaClass) {
/* 228 */     this.metaClass = metaClass;
/*     */   }
/*     */   
/*     */   public MetaClass getMetaClass() {
/* 232 */     return this.metaClass;
/*     */   }
/*     */   
/*     */   public Object invokeMethod(String name, Object args) {
/* 236 */     return this.metaClass.invokeMethod(this, name, args);
/*     */   }
/*     */   
/*     */   public void setProperty(String property, Object newValue) {
/* 240 */     if (newValue instanceof BeanDefinition) {
/* 241 */       registerBeanDefinition(property, (BeanDefinition)newValue);
/*     */     } else {
/*     */       
/* 244 */       this.metaClass.setProperty(this, property, newValue);
/*     */     } 
/*     */   }
/*     */   
/*     */   public Object getProperty(String property) {
/* 249 */     if (containsBean(property)) {
/* 250 */       return getBean(property);
/*     */     }
/* 252 */     if (this.contextWrapper.isReadableProperty(property)) {
/* 253 */       return this.contextWrapper.getPropertyValue(property);
/*     */     }
/* 255 */     throw new NoSuchBeanDefinitionException(property);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\support\GenericGroovyApplicationContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */