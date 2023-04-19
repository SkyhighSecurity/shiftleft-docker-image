/*     */ package org.springframework.aop.aspectj.annotation;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Method;
/*     */ import org.aopalliance.aop.Advice;
/*     */ import org.aspectj.lang.reflect.PerClauseKind;
/*     */ import org.springframework.aop.Pointcut;
/*     */ import org.springframework.aop.aspectj.AspectJExpressionPointcut;
/*     */ import org.springframework.aop.aspectj.AspectJPrecedenceInformation;
/*     */ import org.springframework.aop.aspectj.InstantiationModelAwarePointcutAdvisor;
/*     */ import org.springframework.aop.support.DynamicMethodMatcherPointcut;
/*     */ import org.springframework.aop.support.Pointcuts;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class InstantiationModelAwarePointcutAdvisorImpl
/*     */   implements InstantiationModelAwarePointcutAdvisor, AspectJPrecedenceInformation, Serializable
/*     */ {
/*     */   private final AspectJExpressionPointcut declaredPointcut;
/*     */   private final Class<?> declaringClass;
/*     */   private final String methodName;
/*     */   private final Class<?>[] parameterTypes;
/*     */   private transient Method aspectJAdviceMethod;
/*     */   private final AspectJAdvisorFactory aspectJAdvisorFactory;
/*     */   private final MetadataAwareAspectInstanceFactory aspectInstanceFactory;
/*     */   private final int declarationOrder;
/*     */   private final String aspectName;
/*     */   private final Pointcut pointcut;
/*     */   private final boolean lazy;
/*     */   private Advice instantiatedAdvice;
/*     */   private Boolean isBeforeAdvice;
/*     */   private Boolean isAfterAdvice;
/*     */   
/*     */   public InstantiationModelAwarePointcutAdvisorImpl(AspectJExpressionPointcut declaredPointcut, Method aspectJAdviceMethod, AspectJAdvisorFactory aspectJAdvisorFactory, MetadataAwareAspectInstanceFactory aspectInstanceFactory, int declarationOrder, String aspectName) {
/*  80 */     this.declaredPointcut = declaredPointcut;
/*  81 */     this.declaringClass = aspectJAdviceMethod.getDeclaringClass();
/*  82 */     this.methodName = aspectJAdviceMethod.getName();
/*  83 */     this.parameterTypes = aspectJAdviceMethod.getParameterTypes();
/*  84 */     this.aspectJAdviceMethod = aspectJAdviceMethod;
/*  85 */     this.aspectJAdvisorFactory = aspectJAdvisorFactory;
/*  86 */     this.aspectInstanceFactory = aspectInstanceFactory;
/*  87 */     this.declarationOrder = declarationOrder;
/*  88 */     this.aspectName = aspectName;
/*     */     
/*  90 */     if (aspectInstanceFactory.getAspectMetadata().isLazilyInstantiated()) {
/*     */       
/*  92 */       Pointcut preInstantiationPointcut = Pointcuts.union(aspectInstanceFactory
/*  93 */           .getAspectMetadata().getPerClausePointcut(), (Pointcut)this.declaredPointcut);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  98 */       this.pointcut = (Pointcut)new PerTargetInstantiationModelPointcut(this.declaredPointcut, preInstantiationPointcut, aspectInstanceFactory);
/*     */       
/* 100 */       this.lazy = true;
/*     */     }
/*     */     else {
/*     */       
/* 104 */       this.pointcut = (Pointcut)this.declaredPointcut;
/* 105 */       this.lazy = false;
/* 106 */       this.instantiatedAdvice = instantiateAdvice(this.declaredPointcut);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Pointcut getPointcut() {
/* 117 */     return this.pointcut;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isLazy() {
/* 122 */     return this.lazy;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean isAdviceInstantiated() {
/* 127 */     return (this.instantiatedAdvice != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized Advice getAdvice() {
/* 135 */     if (this.instantiatedAdvice == null) {
/* 136 */       this.instantiatedAdvice = instantiateAdvice(this.declaredPointcut);
/*     */     }
/* 138 */     return this.instantiatedAdvice;
/*     */   }
/*     */   
/*     */   private Advice instantiateAdvice(AspectJExpressionPointcut pcut) {
/* 142 */     return this.aspectJAdvisorFactory.getAdvice(this.aspectJAdviceMethod, pcut, this.aspectInstanceFactory, this.declarationOrder, this.aspectName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isPerInstance() {
/* 153 */     return (getAspectMetadata().getAjType().getPerClause().getKind() != PerClauseKind.SINGLETON);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AspectMetadata getAspectMetadata() {
/* 160 */     return this.aspectInstanceFactory.getAspectMetadata();
/*     */   }
/*     */   
/*     */   public MetadataAwareAspectInstanceFactory getAspectInstanceFactory() {
/* 164 */     return this.aspectInstanceFactory;
/*     */   }
/*     */   
/*     */   public AspectJExpressionPointcut getDeclaredPointcut() {
/* 168 */     return this.declaredPointcut;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getOrder() {
/* 173 */     return this.aspectInstanceFactory.getOrder();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getAspectName() {
/* 178 */     return this.aspectName;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDeclarationOrder() {
/* 183 */     return this.declarationOrder;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isBeforeAdvice() {
/* 188 */     if (this.isBeforeAdvice == null) {
/* 189 */       determineAdviceType();
/*     */     }
/* 191 */     return this.isBeforeAdvice.booleanValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAfterAdvice() {
/* 196 */     if (this.isAfterAdvice == null) {
/* 197 */       determineAdviceType();
/*     */     }
/* 199 */     return this.isAfterAdvice.booleanValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void determineAdviceType() {
/* 208 */     AbstractAspectJAdvisorFactory.AspectJAnnotation<?> aspectJAnnotation = AbstractAspectJAdvisorFactory.findAspectJAnnotationOnMethod(this.aspectJAdviceMethod);
/* 209 */     if (aspectJAnnotation == null) {
/* 210 */       this.isBeforeAdvice = Boolean.valueOf(false);
/* 211 */       this.isAfterAdvice = Boolean.valueOf(false);
/*     */     } else {
/*     */       
/* 214 */       switch (aspectJAnnotation.getAnnotationType()) {
/*     */         case AtPointcut:
/*     */         case AtAround:
/* 217 */           this.isBeforeAdvice = Boolean.valueOf(false);
/* 218 */           this.isAfterAdvice = Boolean.valueOf(false);
/*     */           break;
/*     */         case AtBefore:
/* 221 */           this.isBeforeAdvice = Boolean.valueOf(true);
/* 222 */           this.isAfterAdvice = Boolean.valueOf(false);
/*     */           break;
/*     */         case AtAfter:
/*     */         case AtAfterReturning:
/*     */         case AtAfterThrowing:
/* 227 */           this.isBeforeAdvice = Boolean.valueOf(false);
/* 228 */           this.isAfterAdvice = Boolean.valueOf(true);
/*     */           break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
/* 236 */     inputStream.defaultReadObject();
/*     */     try {
/* 238 */       this.aspectJAdviceMethod = this.declaringClass.getMethod(this.methodName, this.parameterTypes);
/*     */     }
/* 240 */     catch (NoSuchMethodException ex) {
/* 241 */       throw new IllegalStateException("Failed to find advice method on deserialization", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 247 */     return "InstantiationModelAwarePointcutAdvisor: expression [" + getDeclaredPointcut().getExpression() + "]; advice method [" + this.aspectJAdviceMethod + "]; perClauseKind=" + this.aspectInstanceFactory
/*     */       
/* 249 */       .getAspectMetadata().getAjType().getPerClause().getKind();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class PerTargetInstantiationModelPointcut
/*     */     extends DynamicMethodMatcherPointcut
/*     */   {
/*     */     private final AspectJExpressionPointcut declaredPointcut;
/*     */ 
/*     */     
/*     */     private final Pointcut preInstantiationPointcut;
/*     */ 
/*     */     
/*     */     private LazySingletonAspectInstanceFactoryDecorator aspectInstanceFactory;
/*     */ 
/*     */ 
/*     */     
/*     */     public PerTargetInstantiationModelPointcut(AspectJExpressionPointcut declaredPointcut, Pointcut preInstantiationPointcut, MetadataAwareAspectInstanceFactory aspectInstanceFactory) {
/* 269 */       this.declaredPointcut = declaredPointcut;
/* 270 */       this.preInstantiationPointcut = preInstantiationPointcut;
/* 271 */       if (aspectInstanceFactory instanceof LazySingletonAspectInstanceFactoryDecorator) {
/* 272 */         this.aspectInstanceFactory = (LazySingletonAspectInstanceFactoryDecorator)aspectInstanceFactory;
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean matches(Method method, Class<?> targetClass) {
/* 280 */       return ((isAspectMaterialized() && this.declaredPointcut.matches(method, targetClass)) || this.preInstantiationPointcut
/* 281 */         .getMethodMatcher().matches(method, targetClass));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean matches(Method method, Class<?> targetClass, Object... args) {
/* 287 */       return (isAspectMaterialized() && this.declaredPointcut.matches(method, targetClass));
/*     */     }
/*     */     
/*     */     private boolean isAspectMaterialized() {
/* 291 */       return (this.aspectInstanceFactory == null || this.aspectInstanceFactory.isMaterialized());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\aspectj\annotation\InstantiationModelAwarePointcutAdvisorImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */