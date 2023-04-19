/*     */ package org.springframework.beans.factory.groovy;
/*     */ 
/*     */ import groovy.lang.GroovyObjectSupport;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import org.springframework.beans.BeanWrapper;
/*     */ import org.springframework.beans.BeanWrapperImpl;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*     */ import org.springframework.beans.factory.config.ConstructorArgumentValues;
/*     */ import org.springframework.beans.factory.config.RuntimeBeanReference;
/*     */ import org.springframework.beans.factory.support.AbstractBeanDefinition;
/*     */ import org.springframework.beans.factory.support.GenericBeanDefinition;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class GroovyBeanDefinitionWrapper
/*     */   extends GroovyObjectSupport
/*     */ {
/*     */   private static final String PARENT = "parent";
/*     */   private static final String AUTOWIRE = "autowire";
/*     */   private static final String CONSTRUCTOR_ARGS = "constructorArgs";
/*     */   private static final String FACTORY_BEAN = "factoryBean";
/*     */   private static final String FACTORY_METHOD = "factoryMethod";
/*     */   private static final String INIT_METHOD = "initMethod";
/*     */   private static final String DESTROY_METHOD = "destroyMethod";
/*     */   private static final String SINGLETON = "singleton";
/*  55 */   private static final List<String> dynamicProperties = new ArrayList<String>(8);
/*     */   
/*     */   static {
/*  58 */     dynamicProperties.add("parent");
/*  59 */     dynamicProperties.add("autowire");
/*  60 */     dynamicProperties.add("constructorArgs");
/*  61 */     dynamicProperties.add("factoryBean");
/*  62 */     dynamicProperties.add("factoryMethod");
/*  63 */     dynamicProperties.add("initMethod");
/*  64 */     dynamicProperties.add("destroyMethod");
/*  65 */     dynamicProperties.add("singleton");
/*     */   }
/*     */ 
/*     */   
/*     */   private String beanName;
/*     */   
/*     */   private Class<?> clazz;
/*     */   
/*     */   private Collection<?> constructorArgs;
/*     */   
/*     */   private AbstractBeanDefinition definition;
/*     */   
/*     */   private BeanWrapper definitionWrapper;
/*     */   
/*     */   private String parentName;
/*     */ 
/*     */   
/*     */   public GroovyBeanDefinitionWrapper(String beanName) {
/*  83 */     this.beanName = beanName;
/*     */   }
/*     */   
/*     */   public GroovyBeanDefinitionWrapper(String beanName, Class<?> clazz) {
/*  87 */     this.beanName = beanName;
/*  88 */     this.clazz = clazz;
/*     */   }
/*     */   
/*     */   public GroovyBeanDefinitionWrapper(String beanName, Class<?> clazz, Collection<?> constructorArgs) {
/*  92 */     this.beanName = beanName;
/*  93 */     this.clazz = clazz;
/*  94 */     this.constructorArgs = constructorArgs;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getBeanName() {
/*  99 */     return this.beanName;
/*     */   }
/*     */   
/*     */   public void setBeanDefinition(AbstractBeanDefinition definition) {
/* 103 */     this.definition = definition;
/*     */   }
/*     */   
/*     */   public AbstractBeanDefinition getBeanDefinition() {
/* 107 */     if (this.definition == null) {
/* 108 */       this.definition = createBeanDefinition();
/*     */     }
/* 110 */     return this.definition;
/*     */   }
/*     */   
/*     */   protected AbstractBeanDefinition createBeanDefinition() {
/* 114 */     GenericBeanDefinition genericBeanDefinition = new GenericBeanDefinition();
/* 115 */     genericBeanDefinition.setBeanClass(this.clazz);
/* 116 */     if (!CollectionUtils.isEmpty(this.constructorArgs)) {
/* 117 */       ConstructorArgumentValues cav = new ConstructorArgumentValues();
/* 118 */       for (Object constructorArg : this.constructorArgs) {
/* 119 */         cav.addGenericArgumentValue(constructorArg);
/*     */       }
/* 121 */       genericBeanDefinition.setConstructorArgumentValues(cav);
/*     */     } 
/* 123 */     if (this.parentName != null) {
/* 124 */       genericBeanDefinition.setParentName(this.parentName);
/*     */     }
/* 126 */     this.definitionWrapper = (BeanWrapper)new BeanWrapperImpl(genericBeanDefinition);
/* 127 */     return (AbstractBeanDefinition)genericBeanDefinition;
/*     */   }
/*     */   
/*     */   public void setBeanDefinitionHolder(BeanDefinitionHolder holder) {
/* 131 */     this.definition = (AbstractBeanDefinition)holder.getBeanDefinition();
/* 132 */     this.beanName = holder.getBeanName();
/*     */   }
/*     */   
/*     */   public BeanDefinitionHolder getBeanDefinitionHolder() {
/* 136 */     return new BeanDefinitionHolder((BeanDefinition)getBeanDefinition(), getBeanName());
/*     */   }
/*     */   
/*     */   public void setParent(Object obj) {
/* 140 */     if (obj == null) {
/* 141 */       throw new IllegalArgumentException("Parent bean cannot be set to a null runtime bean reference!");
/*     */     }
/* 143 */     if (obj instanceof String) {
/* 144 */       this.parentName = (String)obj;
/*     */     }
/* 146 */     else if (obj instanceof RuntimeBeanReference) {
/* 147 */       this.parentName = ((RuntimeBeanReference)obj).getBeanName();
/*     */     }
/* 149 */     else if (obj instanceof GroovyBeanDefinitionWrapper) {
/* 150 */       this.parentName = ((GroovyBeanDefinitionWrapper)obj).getBeanName();
/*     */     } 
/* 152 */     getBeanDefinition().setParentName(this.parentName);
/* 153 */     getBeanDefinition().setAbstract(false);
/*     */   }
/*     */   
/*     */   public GroovyBeanDefinitionWrapper addProperty(String propertyName, Object propertyValue) {
/* 157 */     if (propertyValue instanceof GroovyBeanDefinitionWrapper) {
/* 158 */       propertyValue = ((GroovyBeanDefinitionWrapper)propertyValue).getBeanDefinition();
/*     */     }
/* 160 */     getBeanDefinition().getPropertyValues().add(propertyName, propertyValue);
/* 161 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getProperty(String property) {
/* 167 */     if (this.definitionWrapper.isReadableProperty(property)) {
/* 168 */       return this.definitionWrapper.getPropertyValue(property);
/*     */     }
/* 170 */     if (dynamicProperties.contains(property)) {
/* 171 */       return null;
/*     */     }
/* 173 */     return super.getProperty(property);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setProperty(String property, Object newValue) {
/* 178 */     if ("parent".equals(property)) {
/* 179 */       setParent(newValue);
/*     */     } else {
/*     */       
/* 182 */       AbstractBeanDefinition bd = getBeanDefinition();
/* 183 */       if ("autowire".equals(property)) {
/* 184 */         if ("byName".equals(newValue)) {
/* 185 */           bd.setAutowireMode(1);
/*     */         }
/* 187 */         else if ("byType".equals(newValue)) {
/* 188 */           bd.setAutowireMode(2);
/*     */         }
/* 190 */         else if ("constructor".equals(newValue)) {
/* 191 */           bd.setAutowireMode(3);
/*     */         }
/* 193 */         else if (Boolean.TRUE.equals(newValue)) {
/* 194 */           bd.setAutowireMode(1);
/*     */         }
/*     */       
/*     */       }
/* 198 */       else if ("constructorArgs".equals(property) && newValue instanceof List) {
/* 199 */         ConstructorArgumentValues cav = new ConstructorArgumentValues();
/* 200 */         List args = (List)newValue;
/* 201 */         for (Object arg : args) {
/* 202 */           cav.addGenericArgumentValue(arg);
/*     */         }
/* 204 */         bd.setConstructorArgumentValues(cav);
/*     */       
/*     */       }
/* 207 */       else if ("factoryBean".equals(property)) {
/* 208 */         if (newValue != null) {
/* 209 */           bd.setFactoryBeanName(newValue.toString());
/*     */         
/*     */         }
/*     */       }
/* 213 */       else if ("factoryMethod".equals(property)) {
/* 214 */         if (newValue != null) {
/* 215 */           bd.setFactoryMethodName(newValue.toString());
/*     */         }
/*     */       }
/* 218 */       else if ("initMethod".equals(property)) {
/* 219 */         if (newValue != null) {
/* 220 */           bd.setInitMethodName(newValue.toString());
/*     */         
/*     */         }
/*     */       }
/* 224 */       else if ("destroyMethod".equals(property)) {
/* 225 */         if (newValue != null) {
/* 226 */           bd.setDestroyMethodName(newValue.toString());
/*     */         
/*     */         }
/*     */       }
/* 230 */       else if ("singleton".equals(property)) {
/* 231 */         bd.setScope(Boolean.TRUE.equals(newValue) ? "singleton" : "prototype");
/*     */       
/*     */       }
/* 234 */       else if (this.definitionWrapper.isWritableProperty(property)) {
/* 235 */         this.definitionWrapper.setPropertyValue(property, newValue);
/*     */       } else {
/*     */         
/* 238 */         super.setProperty(property, newValue);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\groovy\GroovyBeanDefinitionWrapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */