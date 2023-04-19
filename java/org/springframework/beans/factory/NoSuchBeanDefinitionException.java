/*     */ package org.springframework.beans.factory;
/*     */ 
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.util.ClassUtils;
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
/*     */ public class NoSuchBeanDefinitionException
/*     */   extends BeansException
/*     */ {
/*     */   private String beanName;
/*     */   private ResolvableType resolvableType;
/*     */   
/*     */   public NoSuchBeanDefinitionException(String name) {
/*  49 */     super("No bean named '" + name + "' available");
/*  50 */     this.beanName = name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NoSuchBeanDefinitionException(String name, String message) {
/*  59 */     super("No bean named '" + name + "' available: " + message);
/*  60 */     this.beanName = name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NoSuchBeanDefinitionException(Class<?> type) {
/*  68 */     this(ResolvableType.forClass(type));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NoSuchBeanDefinitionException(Class<?> type, String message) {
/*  77 */     this(ResolvableType.forClass(type), message);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NoSuchBeanDefinitionException(ResolvableType type) {
/*  86 */     super("No qualifying bean of type '" + type + "' available");
/*  87 */     this.resolvableType = type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NoSuchBeanDefinitionException(ResolvableType type, String message) {
/*  97 */     super("No qualifying bean of type '" + type + "' available: " + message);
/*  98 */     this.resolvableType = type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public NoSuchBeanDefinitionException(Class<?> type, String dependencyDescription, String message) {
/* 110 */     super("No qualifying bean" + (!StringUtils.hasLength(dependencyDescription) ? (" of type '" + 
/* 111 */         ClassUtils.getQualifiedName(type) + "'") : "") + " found for dependency" + (
/* 112 */         StringUtils.hasLength(dependencyDescription) ? (" [" + dependencyDescription + "]") : "") + ": " + message);
/*     */     
/* 114 */     this.resolvableType = ResolvableType.forClass(type);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getBeanName() {
/* 122 */     return this.beanName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?> getBeanType() {
/* 130 */     return (this.resolvableType != null) ? this.resolvableType.resolve() : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResolvableType getResolvableType() {
/* 139 */     return this.resolvableType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getNumberOfBeansFound() {
/* 148 */     return 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\NoSuchBeanDefinitionException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */