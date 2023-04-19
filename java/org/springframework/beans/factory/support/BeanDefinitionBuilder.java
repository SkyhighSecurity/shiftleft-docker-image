/*     */ package org.springframework.beans.factory.support;
/*     */ 
/*     */ import org.springframework.beans.factory.config.RuntimeBeanReference;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BeanDefinitionBuilder
/*     */ {
/*     */   private AbstractBeanDefinition beanDefinition;
/*     */   private int constructorArgIndex;
/*     */   
/*     */   public static BeanDefinitionBuilder genericBeanDefinition() {
/*  39 */     BeanDefinitionBuilder builder = new BeanDefinitionBuilder();
/*  40 */     builder.beanDefinition = new GenericBeanDefinition();
/*  41 */     return builder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static BeanDefinitionBuilder genericBeanDefinition(Class<?> beanClass) {
/*  49 */     BeanDefinitionBuilder builder = new BeanDefinitionBuilder();
/*  50 */     builder.beanDefinition = new GenericBeanDefinition();
/*  51 */     builder.beanDefinition.setBeanClass(beanClass);
/*  52 */     return builder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static BeanDefinitionBuilder genericBeanDefinition(String beanClassName) {
/*  60 */     BeanDefinitionBuilder builder = new BeanDefinitionBuilder();
/*  61 */     builder.beanDefinition = new GenericBeanDefinition();
/*  62 */     builder.beanDefinition.setBeanClassName(beanClassName);
/*  63 */     return builder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static BeanDefinitionBuilder rootBeanDefinition(Class<?> beanClass) {
/*  71 */     return rootBeanDefinition(beanClass, (String)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static BeanDefinitionBuilder rootBeanDefinition(Class<?> beanClass, String factoryMethodName) {
/*  80 */     BeanDefinitionBuilder builder = new BeanDefinitionBuilder();
/*  81 */     builder.beanDefinition = new RootBeanDefinition();
/*  82 */     builder.beanDefinition.setBeanClass(beanClass);
/*  83 */     builder.beanDefinition.setFactoryMethodName(factoryMethodName);
/*  84 */     return builder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static BeanDefinitionBuilder rootBeanDefinition(String beanClassName) {
/*  92 */     return rootBeanDefinition(beanClassName, (String)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static BeanDefinitionBuilder rootBeanDefinition(String beanClassName, String factoryMethodName) {
/* 101 */     BeanDefinitionBuilder builder = new BeanDefinitionBuilder();
/* 102 */     builder.beanDefinition = new RootBeanDefinition();
/* 103 */     builder.beanDefinition.setBeanClassName(beanClassName);
/* 104 */     builder.beanDefinition.setFactoryMethodName(factoryMethodName);
/* 105 */     return builder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static BeanDefinitionBuilder childBeanDefinition(String parentName) {
/* 113 */     BeanDefinitionBuilder builder = new BeanDefinitionBuilder();
/* 114 */     builder.beanDefinition = new ChildBeanDefinition(parentName);
/* 115 */     return builder;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractBeanDefinition getRawBeanDefinition() {
/* 141 */     return this.beanDefinition;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractBeanDefinition getBeanDefinition() {
/* 148 */     this.beanDefinition.validate();
/* 149 */     return this.beanDefinition;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanDefinitionBuilder setParentName(String parentName) {
/* 157 */     this.beanDefinition.setParentName(parentName);
/* 158 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanDefinitionBuilder setFactoryMethod(String factoryMethod) {
/* 166 */     this.beanDefinition.setFactoryMethodName(factoryMethod);
/* 167 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanDefinitionBuilder setFactoryMethodOnBean(String factoryMethod, String factoryBean) {
/* 176 */     this.beanDefinition.setFactoryMethodName(factoryMethod);
/* 177 */     this.beanDefinition.setFactoryBeanName(factoryBean);
/* 178 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public BeanDefinitionBuilder addConstructorArg(Object value) {
/* 189 */     return addConstructorArgValue(value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanDefinitionBuilder addConstructorArgValue(Object value) {
/* 197 */     this.beanDefinition.getConstructorArgumentValues().addIndexedArgumentValue(this.constructorArgIndex++, value);
/*     */     
/* 199 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanDefinitionBuilder addConstructorArgReference(String beanName) {
/* 207 */     this.beanDefinition.getConstructorArgumentValues().addIndexedArgumentValue(this.constructorArgIndex++, new RuntimeBeanReference(beanName));
/*     */     
/* 209 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanDefinitionBuilder addPropertyValue(String name, Object value) {
/* 216 */     this.beanDefinition.getPropertyValues().add(name, value);
/* 217 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanDefinitionBuilder addPropertyReference(String name, String beanName) {
/* 226 */     this.beanDefinition.getPropertyValues().add(name, new RuntimeBeanReference(beanName));
/* 227 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanDefinitionBuilder setInitMethodName(String methodName) {
/* 234 */     this.beanDefinition.setInitMethodName(methodName);
/* 235 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanDefinitionBuilder setDestroyMethodName(String methodName) {
/* 242 */     this.beanDefinition.setDestroyMethodName(methodName);
/* 243 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanDefinitionBuilder setScope(String scope) {
/* 253 */     this.beanDefinition.setScope(scope);
/* 254 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanDefinitionBuilder setAbstract(boolean flag) {
/* 261 */     this.beanDefinition.setAbstract(flag);
/* 262 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanDefinitionBuilder setLazyInit(boolean lazy) {
/* 269 */     this.beanDefinition.setLazyInit(lazy);
/* 270 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanDefinitionBuilder setAutowireMode(int autowireMode) {
/* 277 */     this.beanDefinition.setAutowireMode(autowireMode);
/* 278 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanDefinitionBuilder setDependencyCheck(int dependencyCheck) {
/* 285 */     this.beanDefinition.setDependencyCheck(dependencyCheck);
/* 286 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanDefinitionBuilder addDependsOn(String beanName) {
/* 294 */     if (this.beanDefinition.getDependsOn() == null) {
/* 295 */       this.beanDefinition.setDependsOn(new String[] { beanName });
/*     */     } else {
/*     */       
/* 298 */       String[] added = (String[])ObjectUtils.addObjectToArray((Object[])this.beanDefinition.getDependsOn(), beanName);
/* 299 */       this.beanDefinition.setDependsOn(added);
/*     */     } 
/* 301 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanDefinitionBuilder setRole(int role) {
/* 308 */     this.beanDefinition.setRole(role);
/* 309 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\support\BeanDefinitionBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */