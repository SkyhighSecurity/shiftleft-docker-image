/*     */ package org.springframework.beans.factory.config;
/*     */ 
/*     */ import org.springframework.beans.BeanMetadataElement;
/*     */ import org.springframework.beans.factory.BeanFactoryUtils;
/*     */ import org.springframework.util.Assert;
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
/*     */ public class BeanDefinitionHolder
/*     */   implements BeanMetadataElement
/*     */ {
/*     */   private final BeanDefinition beanDefinition;
/*     */   private final String beanName;
/*     */   private final String[] aliases;
/*     */   
/*     */   public BeanDefinitionHolder(BeanDefinition beanDefinition, String beanName) {
/*  54 */     this(beanDefinition, beanName, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanDefinitionHolder(BeanDefinition beanDefinition, String beanName, String[] aliases) {
/*  64 */     Assert.notNull(beanDefinition, "BeanDefinition must not be null");
/*  65 */     Assert.notNull(beanName, "Bean name must not be null");
/*  66 */     this.beanDefinition = beanDefinition;
/*  67 */     this.beanName = beanName;
/*  68 */     this.aliases = aliases;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanDefinitionHolder(BeanDefinitionHolder beanDefinitionHolder) {
/*  79 */     Assert.notNull(beanDefinitionHolder, "BeanDefinitionHolder must not be null");
/*  80 */     this.beanDefinition = beanDefinitionHolder.getBeanDefinition();
/*  81 */     this.beanName = beanDefinitionHolder.getBeanName();
/*  82 */     this.aliases = beanDefinitionHolder.getAliases();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanDefinition getBeanDefinition() {
/*  90 */     return this.beanDefinition;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getBeanName() {
/*  97 */     return this.beanName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getAliases() {
/* 105 */     return this.aliases;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getSource() {
/* 114 */     return this.beanDefinition.getSource();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean matchesName(String candidateName) {
/* 122 */     return (candidateName != null && (candidateName.equals(this.beanName) || candidateName
/* 123 */       .equals(BeanFactoryUtils.transformedBeanName(this.beanName)) || 
/* 124 */       ObjectUtils.containsElement((Object[])this.aliases, candidateName)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getShortDescription() {
/* 134 */     StringBuilder sb = new StringBuilder();
/* 135 */     sb.append("Bean definition with name '").append(this.beanName).append("'");
/* 136 */     if (this.aliases != null) {
/* 137 */       sb.append(" and aliases [").append(StringUtils.arrayToCommaDelimitedString((Object[])this.aliases)).append("]");
/*     */     }
/* 139 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getLongDescription() {
/* 149 */     StringBuilder sb = new StringBuilder(getShortDescription());
/* 150 */     sb.append(": ").append(this.beanDefinition);
/* 151 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 162 */     return getLongDescription();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 168 */     if (this == other) {
/* 169 */       return true;
/*     */     }
/* 171 */     if (!(other instanceof BeanDefinitionHolder)) {
/* 172 */       return false;
/*     */     }
/* 174 */     BeanDefinitionHolder otherHolder = (BeanDefinitionHolder)other;
/* 175 */     return (this.beanDefinition.equals(otherHolder.beanDefinition) && this.beanName
/* 176 */       .equals(otherHolder.beanName) && 
/* 177 */       ObjectUtils.nullSafeEquals(this.aliases, otherHolder.aliases));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 182 */     int hashCode = this.beanDefinition.hashCode();
/* 183 */     hashCode = 29 * hashCode + this.beanName.hashCode();
/* 184 */     hashCode = 29 * hashCode + ObjectUtils.nullSafeHashCode((Object[])this.aliases);
/* 185 */     return hashCode;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\config\BeanDefinitionHolder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */