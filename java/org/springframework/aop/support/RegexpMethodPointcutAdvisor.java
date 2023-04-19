/*     */ package org.springframework.aop.support;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import org.aopalliance.aop.Advice;
/*     */ import org.springframework.aop.Pointcut;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RegexpMethodPointcutAdvisor
/*     */   extends AbstractGenericPointcutAdvisor
/*     */ {
/*     */   private String[] patterns;
/*     */   private AbstractRegexpMethodPointcut pointcut;
/*  51 */   private final Object pointcutMonitor = new SerializableMonitor();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RegexpMethodPointcutAdvisor() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RegexpMethodPointcutAdvisor(Advice advice) {
/*  71 */     setAdvice(advice);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RegexpMethodPointcutAdvisor(String pattern, Advice advice) {
/*  80 */     setPattern(pattern);
/*  81 */     setAdvice(advice);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RegexpMethodPointcutAdvisor(String[] patterns, Advice advice) {
/*  90 */     setPatterns(patterns);
/*  91 */     setAdvice(advice);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPattern(String pattern) {
/* 101 */     setPatterns(new String[] { pattern });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPatterns(String... patterns) {
/* 112 */     this.patterns = patterns;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Pointcut getPointcut() {
/* 121 */     synchronized (this.pointcutMonitor) {
/* 122 */       if (this.pointcut == null) {
/* 123 */         this.pointcut = createPointcut();
/* 124 */         this.pointcut.setPatterns(this.patterns);
/*     */       } 
/* 126 */       return this.pointcut;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractRegexpMethodPointcut createPointcut() {
/* 136 */     return new JdkRegexpMethodPointcut();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 141 */     return getClass().getName() + ": advice [" + getAdvice() + "], pointcut patterns " + 
/* 142 */       ObjectUtils.nullSafeToString((Object[])this.patterns);
/*     */   }
/*     */   
/*     */   private static class SerializableMonitor implements Serializable {
/*     */     private SerializableMonitor() {}
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\support\RegexpMethodPointcutAdvisor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */