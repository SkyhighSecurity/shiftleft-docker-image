/*     */ package org.springframework.aop.aspectj;
/*     */ 
/*     */ import org.aopalliance.aop.Advice;
/*     */ import org.springframework.aop.ClassFilter;
/*     */ import org.springframework.aop.IntroductionAdvisor;
/*     */ import org.springframework.aop.IntroductionInterceptor;
/*     */ import org.springframework.aop.support.ClassFilters;
/*     */ import org.springframework.aop.support.DelegatePerTargetObjectIntroductionInterceptor;
/*     */ import org.springframework.aop.support.DelegatingIntroductionInterceptor;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DeclareParentsAdvisor
/*     */   implements IntroductionAdvisor
/*     */ {
/*     */   private final Advice advice;
/*     */   private final Class<?> introducedInterface;
/*     */   private final ClassFilter typePatternClassFilter;
/*     */   
/*     */   public DeclareParentsAdvisor(Class<?> interfaceType, String typePattern, Class<?> defaultImpl) {
/*  52 */     this(interfaceType, typePattern, (IntroductionInterceptor)new DelegatePerTargetObjectIntroductionInterceptor(defaultImpl, interfaceType));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DeclareParentsAdvisor(Class<?> interfaceType, String typePattern, Object delegateRef) {
/*  63 */     this(interfaceType, typePattern, (IntroductionInterceptor)new DelegatingIntroductionInterceptor(delegateRef));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private DeclareParentsAdvisor(Class<?> interfaceType, String typePattern, IntroductionInterceptor interceptor) {
/*  74 */     this.advice = (Advice)interceptor;
/*  75 */     this.introducedInterface = interfaceType;
/*     */ 
/*     */     
/*  78 */     ClassFilter typePatternFilter = new TypePatternClassFilter(typePattern);
/*  79 */     ClassFilter exclusion = new ClassFilter()
/*     */       {
/*     */         public boolean matches(Class<?> clazz) {
/*  82 */           return !DeclareParentsAdvisor.this.introducedInterface.isAssignableFrom(clazz);
/*     */         }
/*     */       };
/*  85 */     this.typePatternClassFilter = ClassFilters.intersection(typePatternFilter, exclusion);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassFilter getClassFilter() {
/*  91 */     return this.typePatternClassFilter;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void validateInterfaces() throws IllegalArgumentException {}
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isPerInstance() {
/* 101 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public Advice getAdvice() {
/* 106 */     return this.advice;
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?>[] getInterfaces() {
/* 111 */     return new Class[] { this.introducedInterface };
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\aspectj\DeclareParentsAdvisor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */