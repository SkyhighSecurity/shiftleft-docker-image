/*     */ package org.springframework.beans.factory.config;
/*     */ 
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
/*     */ public class RuntimeBeanReference
/*     */   implements BeanReference
/*     */ {
/*     */   private final String beanName;
/*     */   private final boolean toParent;
/*     */   private Object source;
/*     */   
/*     */   public RuntimeBeanReference(String beanName) {
/*  46 */     this(beanName, false);
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
/*     */   public RuntimeBeanReference(String beanName, boolean toParent) {
/*  58 */     Assert.hasText(beanName, "'beanName' must not be empty");
/*  59 */     this.beanName = beanName;
/*  60 */     this.toParent = toParent;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getBeanName() {
/*  66 */     return this.beanName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isToParent() {
/*  74 */     return this.toParent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSource(Object source) {
/*  82 */     this.source = source;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getSource() {
/*  87 */     return this.source;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/*  93 */     if (this == other) {
/*  94 */       return true;
/*     */     }
/*  96 */     if (!(other instanceof RuntimeBeanReference)) {
/*  97 */       return false;
/*     */     }
/*  99 */     RuntimeBeanReference that = (RuntimeBeanReference)other;
/* 100 */     return (this.beanName.equals(that.beanName) && this.toParent == that.toParent);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 105 */     int result = this.beanName.hashCode();
/* 106 */     result = 29 * result + (this.toParent ? 1 : 0);
/* 107 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 112 */     return '<' + getBeanName() + '>';
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\config\RuntimeBeanReference.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */