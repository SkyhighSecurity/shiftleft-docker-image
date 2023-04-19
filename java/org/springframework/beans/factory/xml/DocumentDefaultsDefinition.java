/*     */ package org.springframework.beans.factory.xml;
/*     */ 
/*     */ import org.springframework.beans.factory.parsing.DefaultsDefinition;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DocumentDefaultsDefinition
/*     */   implements DefaultsDefinition
/*     */ {
/*     */   private String lazyInit;
/*     */   private String merge;
/*     */   private String autowire;
/*     */   private String dependencyCheck;
/*     */   private String autowireCandidates;
/*     */   private String initMethod;
/*     */   private String destroyMethod;
/*     */   private Object source;
/*     */   
/*     */   public void setLazyInit(String lazyInit) {
/*  52 */     this.lazyInit = lazyInit;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getLazyInit() {
/*  59 */     return this.lazyInit;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMerge(String merge) {
/*  66 */     this.merge = merge;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMerge() {
/*  73 */     return this.merge;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAutowire(String autowire) {
/*  80 */     this.autowire = autowire;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getAutowire() {
/*  87 */     return this.autowire;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDependencyCheck(String dependencyCheck) {
/*  94 */     this.dependencyCheck = dependencyCheck;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDependencyCheck() {
/* 101 */     return this.dependencyCheck;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAutowireCandidates(String autowireCandidates) {
/* 109 */     this.autowireCandidates = autowireCandidates;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getAutowireCandidates() {
/* 117 */     return this.autowireCandidates;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInitMethod(String initMethod) {
/* 124 */     this.initMethod = initMethod;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getInitMethod() {
/* 131 */     return this.initMethod;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDestroyMethod(String destroyMethod) {
/* 138 */     this.destroyMethod = destroyMethod;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDestroyMethod() {
/* 145 */     return this.destroyMethod;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSource(Object source) {
/* 153 */     this.source = source;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getSource() {
/* 158 */     return this.source;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\xml\DocumentDefaultsDefinition.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */