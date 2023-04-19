/*     */ package org.springframework.beans.factory.parsing;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.springframework.beans.MutablePropertyValues;
/*     */ import org.springframework.beans.PropertyValue;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*     */ import org.springframework.beans.factory.config.BeanReference;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BeanComponentDefinition
/*     */   extends BeanDefinitionHolder
/*     */   implements ComponentDefinition
/*     */ {
/*     */   private BeanDefinition[] innerBeanDefinitions;
/*     */   private BeanReference[] beanReferences;
/*     */   
/*     */   public BeanComponentDefinition(BeanDefinition beanDefinition, String beanName) {
/*  49 */     super(beanDefinition, beanName);
/*  50 */     findInnerBeanDefinitionsAndBeanReferences(beanDefinition);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanComponentDefinition(BeanDefinition beanDefinition, String beanName, String[] aliases) {
/*  60 */     super(beanDefinition, beanName, aliases);
/*  61 */     findInnerBeanDefinitionsAndBeanReferences(beanDefinition);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanComponentDefinition(BeanDefinitionHolder holder) {
/*  70 */     super(holder);
/*  71 */     findInnerBeanDefinitionsAndBeanReferences(holder.getBeanDefinition());
/*     */   }
/*     */ 
/*     */   
/*     */   private void findInnerBeanDefinitionsAndBeanReferences(BeanDefinition beanDefinition) {
/*  76 */     List<BeanDefinition> innerBeans = new ArrayList<BeanDefinition>();
/*  77 */     List<BeanReference> references = new ArrayList<BeanReference>();
/*  78 */     MutablePropertyValues mutablePropertyValues = beanDefinition.getPropertyValues();
/*  79 */     for (PropertyValue propertyValue : mutablePropertyValues.getPropertyValues()) {
/*  80 */       Object value = propertyValue.getValue();
/*  81 */       if (value instanceof BeanDefinitionHolder) {
/*  82 */         innerBeans.add(((BeanDefinitionHolder)value).getBeanDefinition());
/*     */       }
/*  84 */       else if (value instanceof BeanDefinition) {
/*  85 */         innerBeans.add((BeanDefinition)value);
/*     */       }
/*  87 */       else if (value instanceof BeanReference) {
/*  88 */         references.add((BeanReference)value);
/*     */       } 
/*     */     } 
/*  91 */     this.innerBeanDefinitions = innerBeans.<BeanDefinition>toArray(new BeanDefinition[innerBeans.size()]);
/*  92 */     this.beanReferences = references.<BeanReference>toArray(new BeanReference[references.size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/*  98 */     return getBeanName();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDescription() {
/* 103 */     return getShortDescription();
/*     */   }
/*     */ 
/*     */   
/*     */   public BeanDefinition[] getBeanDefinitions() {
/* 108 */     return new BeanDefinition[] { getBeanDefinition() };
/*     */   }
/*     */ 
/*     */   
/*     */   public BeanDefinition[] getInnerBeanDefinitions() {
/* 113 */     return this.innerBeanDefinitions;
/*     */   }
/*     */ 
/*     */   
/*     */   public BeanReference[] getBeanReferences() {
/* 118 */     return this.beanReferences;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 128 */     return getDescription();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 137 */     return (this == other || (other instanceof BeanComponentDefinition && super.equals(other)));
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\parsing\BeanComponentDefinition.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */