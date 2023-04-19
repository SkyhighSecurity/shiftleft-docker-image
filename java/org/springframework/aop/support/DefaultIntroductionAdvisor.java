/*     */ package org.springframework.aop.support;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
/*     */ import org.aopalliance.aop.Advice;
/*     */ import org.springframework.aop.ClassFilter;
/*     */ import org.springframework.aop.DynamicIntroductionAdvice;
/*     */ import org.springframework.aop.IntroductionAdvisor;
/*     */ import org.springframework.aop.IntroductionInfo;
/*     */ import org.springframework.core.Ordered;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultIntroductionAdvisor
/*     */   implements IntroductionAdvisor, ClassFilter, Ordered, Serializable
/*     */ {
/*     */   private final Advice advice;
/*  46 */   private final Set<Class<?>> interfaces = new LinkedHashSet<Class<?>>();
/*     */   
/*  48 */   private int order = Integer.MAX_VALUE;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultIntroductionAdvisor(Advice advice) {
/*  58 */     this(advice, (advice instanceof IntroductionInfo) ? (IntroductionInfo)advice : null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultIntroductionAdvisor(Advice advice, IntroductionInfo introductionInfo) {
/*  68 */     Assert.notNull(advice, "Advice must not be null");
/*  69 */     this.advice = advice;
/*  70 */     if (introductionInfo != null) {
/*  71 */       Class<?>[] introducedInterfaces = introductionInfo.getInterfaces();
/*  72 */       if (introducedInterfaces.length == 0) {
/*  73 */         throw new IllegalArgumentException("IntroductionAdviceSupport implements no interfaces");
/*     */       }
/*  75 */       for (Class<?> ifc : introducedInterfaces) {
/*  76 */         addInterface(ifc);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultIntroductionAdvisor(DynamicIntroductionAdvice advice, Class<?> intf) {
/*  87 */     Assert.notNull(advice, "Advice must not be null");
/*  88 */     this.advice = (Advice)advice;
/*  89 */     addInterface(intf);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addInterface(Class<?> intf) {
/*  98 */     Assert.notNull(intf, "Interface must not be null");
/*  99 */     if (!intf.isInterface()) {
/* 100 */       throw new IllegalArgumentException("Specified class [" + intf.getName() + "] must be an interface");
/*     */     }
/* 102 */     this.interfaces.add(intf);
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?>[] getInterfaces() {
/* 107 */     return ClassUtils.toClassArray(this.interfaces);
/*     */   }
/*     */ 
/*     */   
/*     */   public void validateInterfaces() throws IllegalArgumentException {
/* 112 */     for (Class<?> ifc : this.interfaces) {
/* 113 */       if (this.advice instanceof DynamicIntroductionAdvice && 
/* 114 */         !((DynamicIntroductionAdvice)this.advice).implementsInterface(ifc)) {
/* 115 */         throw new IllegalArgumentException("DynamicIntroductionAdvice [" + this.advice + "] does not implement interface [" + ifc
/* 116 */             .getName() + "] specified for introduction");
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setOrder(int order) {
/* 122 */     this.order = order;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getOrder() {
/* 127 */     return this.order;
/*     */   }
/*     */ 
/*     */   
/*     */   public Advice getAdvice() {
/* 132 */     return this.advice;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPerInstance() {
/* 137 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public ClassFilter getClassFilter() {
/* 142 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean matches(Class<?> clazz) {
/* 147 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 153 */     if (this == other) {
/* 154 */       return true;
/*     */     }
/* 156 */     if (!(other instanceof DefaultIntroductionAdvisor)) {
/* 157 */       return false;
/*     */     }
/* 159 */     DefaultIntroductionAdvisor otherAdvisor = (DefaultIntroductionAdvisor)other;
/* 160 */     return (this.advice.equals(otherAdvisor.advice) && this.interfaces.equals(otherAdvisor.interfaces));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 165 */     return this.advice.hashCode() * 13 + this.interfaces.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 170 */     return ClassUtils.getShortName(getClass()) + ": advice [" + this.advice + "]; interfaces " + 
/* 171 */       ClassUtils.classNamesToString(this.interfaces);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\support\DefaultIntroductionAdvisor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */