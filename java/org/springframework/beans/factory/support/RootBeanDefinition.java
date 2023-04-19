/*     */ package org.springframework.beans.factory.support;
/*     */ 
/*     */ import java.lang.reflect.AnnotatedElement;
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import org.springframework.beans.MutablePropertyValues;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*     */ import org.springframework.beans.factory.config.ConstructorArgumentValues;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.util.Assert;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RootBeanDefinition
/*     */   extends AbstractBeanDefinition
/*     */ {
/*     */   private BeanDefinitionHolder decoratedDefinition;
/*     */   private AnnotatedElement qualifiedElement;
/*     */   boolean allowCaching = true;
/*     */   boolean isFactoryMethodUnique = false;
/*     */   volatile ResolvableType targetType;
/*     */   volatile Class<?> resolvedTargetType;
/*     */   volatile ResolvableType factoryMethodReturnType;
/*  70 */   final Object constructorArgumentLock = new Object();
/*     */ 
/*     */   
/*     */   Object resolvedConstructorOrFactoryMethod;
/*     */ 
/*     */   
/*     */   boolean constructorArgumentsResolved = false;
/*     */ 
/*     */   
/*     */   Object[] resolvedConstructorArguments;
/*     */ 
/*     */   
/*     */   Object[] preparedConstructorArguments;
/*     */ 
/*     */   
/*  85 */   final Object postProcessingLock = new Object();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean postProcessed = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   volatile Boolean beforeInstantiationResolved;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Set<Member> externallyManagedConfigMembers;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Set<String> externallyManagedInitMethods;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Set<String> externallyManagedDestroyMethods;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RootBeanDefinition(Class<?> beanClass) {
/* 119 */     setBeanClass(beanClass);
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
/*     */   public RootBeanDefinition(Class<?> beanClass, int autowireMode, boolean dependencyCheck) {
/* 132 */     setBeanClass(beanClass);
/* 133 */     setAutowireMode(autowireMode);
/* 134 */     if (dependencyCheck && getResolvedAutowireMode() != 3) {
/* 135 */       setDependencyCheck(1);
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
/*     */   public RootBeanDefinition(Class<?> beanClass, ConstructorArgumentValues cargs, MutablePropertyValues pvs) {
/* 147 */     super(cargs, pvs);
/* 148 */     setBeanClass(beanClass);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RootBeanDefinition(String beanClassName) {
/* 158 */     setBeanClassName(beanClassName);
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
/*     */   public RootBeanDefinition(String beanClassName, ConstructorArgumentValues cargs, MutablePropertyValues pvs) {
/* 170 */     super(cargs, pvs);
/* 171 */     setBeanClassName(beanClassName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RootBeanDefinition(RootBeanDefinition original) {
/* 180 */     super(original);
/* 181 */     this.decoratedDefinition = original.decoratedDefinition;
/* 182 */     this.qualifiedElement = original.qualifiedElement;
/* 183 */     this.allowCaching = original.allowCaching;
/* 184 */     this.isFactoryMethodUnique = original.isFactoryMethodUnique;
/* 185 */     this.targetType = original.targetType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   RootBeanDefinition(BeanDefinition original) {
/* 194 */     super(original);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getParentName() {
/* 200 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setParentName(String parentName) {
/* 205 */     if (parentName != null) {
/* 206 */       throw new IllegalArgumentException("Root bean cannot be changed into a child bean with parent reference");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDecoratedDefinition(BeanDefinitionHolder decoratedDefinition) {
/* 214 */     this.decoratedDefinition = decoratedDefinition;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanDefinitionHolder getDecoratedDefinition() {
/* 221 */     return this.decoratedDefinition;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setQualifiedElement(AnnotatedElement qualifiedElement) {
/* 232 */     this.qualifiedElement = qualifiedElement;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotatedElement getQualifiedElement() {
/* 241 */     return this.qualifiedElement;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTargetType(ResolvableType targetType) {
/* 249 */     this.targetType = targetType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTargetType(Class<?> targetType) {
/* 257 */     this.targetType = (targetType != null) ? ResolvableType.forClass(targetType) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?> getTargetType() {
/* 266 */     if (this.resolvedTargetType != null) {
/* 267 */       return this.resolvedTargetType;
/*     */     }
/* 269 */     return (this.targetType != null) ? this.targetType.resolve() : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUniqueFactoryMethodName(String name) {
/* 276 */     Assert.hasText(name, "Factory method name must not be empty");
/* 277 */     setFactoryMethodName(name);
/* 278 */     this.isFactoryMethodUnique = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFactoryMethod(Method candidate) {
/* 285 */     return (candidate != null && candidate.getName().equals(getFactoryMethodName()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Method getResolvedFactoryMethod() {
/* 293 */     synchronized (this.constructorArgumentLock) {
/* 294 */       Object candidate = this.resolvedConstructorOrFactoryMethod;
/* 295 */       return (candidate instanceof Method) ? (Method)candidate : null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void registerExternallyManagedConfigMember(Member configMember) {
/* 300 */     synchronized (this.postProcessingLock) {
/* 301 */       if (this.externallyManagedConfigMembers == null) {
/* 302 */         this.externallyManagedConfigMembers = new HashSet<Member>(1);
/*     */       }
/* 304 */       this.externallyManagedConfigMembers.add(configMember);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isExternallyManagedConfigMember(Member configMember) {
/* 309 */     synchronized (this.postProcessingLock) {
/* 310 */       return (this.externallyManagedConfigMembers != null && this.externallyManagedConfigMembers
/* 311 */         .contains(configMember));
/*     */     } 
/*     */   }
/*     */   
/*     */   public void registerExternallyManagedInitMethod(String initMethod) {
/* 316 */     synchronized (this.postProcessingLock) {
/* 317 */       if (this.externallyManagedInitMethods == null) {
/* 318 */         this.externallyManagedInitMethods = new HashSet<String>(1);
/*     */       }
/* 320 */       this.externallyManagedInitMethods.add(initMethod);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isExternallyManagedInitMethod(String initMethod) {
/* 325 */     synchronized (this.postProcessingLock) {
/* 326 */       return (this.externallyManagedInitMethods != null && this.externallyManagedInitMethods
/* 327 */         .contains(initMethod));
/*     */     } 
/*     */   }
/*     */   
/*     */   public void registerExternallyManagedDestroyMethod(String destroyMethod) {
/* 332 */     synchronized (this.postProcessingLock) {
/* 333 */       if (this.externallyManagedDestroyMethods == null) {
/* 334 */         this.externallyManagedDestroyMethods = new HashSet<String>(1);
/*     */       }
/* 336 */       this.externallyManagedDestroyMethods.add(destroyMethod);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isExternallyManagedDestroyMethod(String destroyMethod) {
/* 341 */     synchronized (this.postProcessingLock) {
/* 342 */       return (this.externallyManagedDestroyMethods != null && this.externallyManagedDestroyMethods
/* 343 */         .contains(destroyMethod));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public RootBeanDefinition cloneBeanDefinition() {
/* 350 */     return new RootBeanDefinition(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 355 */     return (this == other || (other instanceof RootBeanDefinition && super.equals(other)));
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 360 */     return "Root bean: " + super.toString();
/*     */   }
/*     */   
/*     */   public RootBeanDefinition() {}
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\support\RootBeanDefinition.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */