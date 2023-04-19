/*     */ package org.springframework.beans.factory.support;
/*     */ 
/*     */ import org.springframework.beans.factory.BeanDefinitionStoreException;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BeanDefinitionReaderUtils
/*     */ {
/*     */   public static final String GENERATED_BEAN_NAME_SEPARATOR = "#";
/*     */   
/*     */   public static AbstractBeanDefinition createBeanDefinition(String parentName, String className, ClassLoader classLoader) throws ClassNotFoundException {
/*  59 */     GenericBeanDefinition bd = new GenericBeanDefinition();
/*  60 */     bd.setParentName(parentName);
/*  61 */     if (className != null) {
/*  62 */       if (classLoader != null) {
/*  63 */         bd.setBeanClass(ClassUtils.forName(className, classLoader));
/*     */       } else {
/*     */         
/*  66 */         bd.setBeanClassName(className);
/*     */       } 
/*     */     }
/*  69 */     return bd;
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
/*     */   public static String generateBeanName(BeanDefinition beanDefinition, BeanDefinitionRegistry registry) throws BeanDefinitionStoreException {
/*  86 */     return generateBeanName(beanDefinition, registry, false);
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
/*     */   public static String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry, boolean isInnerBean) throws BeanDefinitionStoreException {
/* 106 */     String generatedBeanName = definition.getBeanClassName();
/* 107 */     if (generatedBeanName == null) {
/* 108 */       if (definition.getParentName() != null) {
/* 109 */         generatedBeanName = definition.getParentName() + "$child";
/*     */       }
/* 111 */       else if (definition.getFactoryBeanName() != null) {
/* 112 */         generatedBeanName = definition.getFactoryBeanName() + "$created";
/*     */       } 
/*     */     }
/* 115 */     if (!StringUtils.hasText(generatedBeanName)) {
/* 116 */       throw new BeanDefinitionStoreException("Unnamed bean definition specifies neither 'class' nor 'parent' nor 'factory-bean' - can't generate bean name");
/*     */     }
/*     */ 
/*     */     
/* 120 */     String id = generatedBeanName;
/* 121 */     if (isInnerBean) {
/*     */       
/* 123 */       id = generatedBeanName + "#" + ObjectUtils.getIdentityHexString(definition);
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 128 */       int counter = -1;
/* 129 */       while (counter == -1 || registry.containsBeanDefinition(id)) {
/* 130 */         counter++;
/* 131 */         id = generatedBeanName + "#" + counter;
/*     */       } 
/*     */     } 
/* 134 */     return id;
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
/*     */   public static void registerBeanDefinition(BeanDefinitionHolder definitionHolder, BeanDefinitionRegistry registry) throws BeanDefinitionStoreException {
/* 148 */     String beanName = definitionHolder.getBeanName();
/* 149 */     registry.registerBeanDefinition(beanName, definitionHolder.getBeanDefinition());
/*     */ 
/*     */     
/* 152 */     String[] aliases = definitionHolder.getAliases();
/* 153 */     if (aliases != null) {
/* 154 */       for (String alias : aliases) {
/* 155 */         registry.registerAlias(beanName, alias);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String registerWithGeneratedName(AbstractBeanDefinition definition, BeanDefinitionRegistry registry) throws BeanDefinitionStoreException {
/* 173 */     String generatedName = generateBeanName(definition, registry, false);
/* 174 */     registry.registerBeanDefinition(generatedName, definition);
/* 175 */     return generatedName;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\support\BeanDefinitionReaderUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */