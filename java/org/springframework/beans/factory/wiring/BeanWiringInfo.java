/*     */ package org.springframework.beans.factory.wiring;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BeanWiringInfo
/*     */ {
/*     */   public static final int AUTOWIRE_BY_NAME = 1;
/*     */   public static final int AUTOWIRE_BY_TYPE = 2;
/*  51 */   private String beanName = null;
/*     */   
/*     */   private boolean isDefaultBeanName = false;
/*     */   
/*  55 */   private int autowireMode = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean dependencyCheck = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanWiringInfo() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanWiringInfo(String beanName) {
/*  74 */     this(beanName, false);
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
/*     */   public BeanWiringInfo(String beanName, boolean isDefaultBeanName) {
/*  86 */     Assert.hasText(beanName, "'beanName' must not be empty");
/*  87 */     this.beanName = beanName;
/*  88 */     this.isDefaultBeanName = isDefaultBeanName;
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
/*     */   public BeanWiringInfo(int autowireMode, boolean dependencyCheck) {
/* 103 */     if (autowireMode != 1 && autowireMode != 2) {
/* 104 */       throw new IllegalArgumentException("Only constants AUTOWIRE_BY_NAME and AUTOWIRE_BY_TYPE supported");
/*     */     }
/* 106 */     this.autowireMode = autowireMode;
/* 107 */     this.dependencyCheck = dependencyCheck;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean indicatesAutowiring() {
/* 115 */     return (this.beanName == null);
/*     */   }
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
/*     */   public boolean isDefaultBeanName() {
/* 130 */     return this.isDefaultBeanName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getAutowireMode() {
/* 138 */     return this.autowireMode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getDependencyCheck() {
/* 146 */     return this.dependencyCheck;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\wiring\BeanWiringInfo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */