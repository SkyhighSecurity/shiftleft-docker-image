/*     */ package org.springframework.aop.config;
/*     */ 
/*     */ import org.springframework.beans.MutablePropertyValues;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.BeanReference;
/*     */ import org.springframework.beans.factory.parsing.AbstractComponentDefinition;
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
/*     */ public class AdvisorComponentDefinition
/*     */   extends AbstractComponentDefinition
/*     */ {
/*     */   private final String advisorBeanName;
/*     */   private final BeanDefinition advisorDefinition;
/*     */   private String description;
/*     */   private BeanReference[] beanReferences;
/*     */   private BeanDefinition[] beanDefinitions;
/*     */   
/*     */   public AdvisorComponentDefinition(String advisorBeanName, BeanDefinition advisorDefinition) {
/*  49 */     this(advisorBeanName, advisorDefinition, null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public AdvisorComponentDefinition(String advisorBeanName, BeanDefinition advisorDefinition, BeanDefinition pointcutDefinition) {
/*  55 */     Assert.notNull(advisorBeanName, "'advisorBeanName' must not be null");
/*  56 */     Assert.notNull(advisorDefinition, "'advisorDefinition' must not be null");
/*  57 */     this.advisorBeanName = advisorBeanName;
/*  58 */     this.advisorDefinition = advisorDefinition;
/*  59 */     unwrapDefinitions(advisorDefinition, pointcutDefinition);
/*     */   }
/*     */ 
/*     */   
/*     */   private void unwrapDefinitions(BeanDefinition advisorDefinition, BeanDefinition pointcutDefinition) {
/*  64 */     MutablePropertyValues pvs = advisorDefinition.getPropertyValues();
/*  65 */     BeanReference adviceReference = (BeanReference)pvs.getPropertyValue("adviceBeanName").getValue();
/*     */     
/*  67 */     if (pointcutDefinition != null) {
/*  68 */       this.beanReferences = new BeanReference[] { adviceReference };
/*  69 */       this.beanDefinitions = new BeanDefinition[] { advisorDefinition, pointcutDefinition };
/*  70 */       this.description = buildDescription(adviceReference, pointcutDefinition);
/*     */     } else {
/*     */       
/*  73 */       BeanReference pointcutReference = (BeanReference)pvs.getPropertyValue("pointcut").getValue();
/*  74 */       this.beanReferences = new BeanReference[] { adviceReference, pointcutReference };
/*  75 */       this.beanDefinitions = new BeanDefinition[] { advisorDefinition };
/*  76 */       this.description = buildDescription(adviceReference, pointcutReference);
/*     */     } 
/*     */   }
/*     */   
/*     */   private String buildDescription(BeanReference adviceReference, BeanDefinition pointcutDefinition) {
/*  81 */     return "Advisor <advice(ref)='" + adviceReference
/*  82 */       .getBeanName() + "', pointcut(expression)=[" + pointcutDefinition
/*  83 */       .getPropertyValues().getPropertyValue("expression").getValue() + "]>";
/*     */   }
/*     */ 
/*     */   
/*     */   private String buildDescription(BeanReference adviceReference, BeanReference pointcutReference) {
/*  88 */     return "Advisor <advice(ref)='" + adviceReference
/*  89 */       .getBeanName() + "', pointcut(ref)='" + pointcutReference
/*  90 */       .getBeanName() + "'>";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/*  96 */     return this.advisorBeanName;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDescription() {
/* 101 */     return this.description;
/*     */   }
/*     */ 
/*     */   
/*     */   public BeanDefinition[] getBeanDefinitions() {
/* 106 */     return this.beanDefinitions;
/*     */   }
/*     */ 
/*     */   
/*     */   public BeanReference[] getBeanReferences() {
/* 111 */     return this.beanReferences;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getSource() {
/* 116 */     return this.advisorDefinition.getSource();
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\config\AdvisorComponentDefinition.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */