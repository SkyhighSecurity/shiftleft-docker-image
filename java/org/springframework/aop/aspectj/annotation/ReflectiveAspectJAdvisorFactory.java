/*     */ package org.springframework.aop.aspectj.annotation;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import org.aopalliance.aop.Advice;
/*     */ import org.aspectj.lang.annotation.After;
/*     */ import org.aspectj.lang.annotation.AfterReturning;
/*     */ import org.aspectj.lang.annotation.AfterThrowing;
/*     */ import org.aspectj.lang.annotation.Around;
/*     */ import org.aspectj.lang.annotation.Before;
/*     */ import org.aspectj.lang.annotation.DeclareParents;
/*     */ import org.aspectj.lang.annotation.Pointcut;
/*     */ import org.springframework.aop.Advisor;
/*     */ import org.springframework.aop.MethodBeforeAdvice;
/*     */ import org.springframework.aop.aspectj.AspectJAfterAdvice;
/*     */ import org.springframework.aop.aspectj.AspectJAfterReturningAdvice;
/*     */ import org.springframework.aop.aspectj.AspectJAfterThrowingAdvice;
/*     */ import org.springframework.aop.aspectj.AspectJAroundAdvice;
/*     */ import org.springframework.aop.aspectj.AspectJExpressionPointcut;
/*     */ import org.springframework.aop.aspectj.AspectJMethodBeforeAdvice;
/*     */ import org.springframework.aop.aspectj.DeclareParentsAdvisor;
/*     */ import org.springframework.aop.framework.AopConfigException;
/*     */ import org.springframework.aop.support.DefaultPointcutAdvisor;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.core.annotation.AnnotationUtils;
/*     */ import org.springframework.core.convert.converter.Converter;
/*     */ import org.springframework.core.convert.converter.ConvertingComparator;
/*     */ import org.springframework.util.ReflectionUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.util.comparator.CompoundComparator;
/*     */ import org.springframework.util.comparator.InstanceComparator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ReflectiveAspectJAdvisorFactory
/*     */   extends AbstractAspectJAdvisorFactory
/*     */   implements Serializable
/*     */ {
/*     */   private static final Comparator<Method> METHOD_COMPARATOR;
/*     */   private final BeanFactory beanFactory;
/*     */   
/*     */   static {
/*  76 */     CompoundComparator<Method> comparator = new CompoundComparator();
/*  77 */     comparator.addComparator((Comparator)new ConvertingComparator((Comparator)new InstanceComparator(new Class[] { Around.class, Before.class, After.class, AfterReturning.class, AfterThrowing.class }, ), new Converter<Method, Annotation>()
/*     */           {
/*     */ 
/*     */ 
/*     */             
/*     */             public Annotation convert(Method method)
/*     */             {
/*  84 */               AbstractAspectJAdvisorFactory.AspectJAnnotation<?> annotation = AbstractAspectJAdvisorFactory.findAspectJAnnotationOnMethod(method);
/*  85 */               return (annotation != null) ? (Annotation)annotation.getAnnotation() : null;
/*     */             }
/*     */           }));
/*  88 */     comparator.addComparator((Comparator)new ConvertingComparator(new Converter<Method, String>()
/*     */           {
/*     */             public String convert(Method method)
/*     */             {
/*  92 */               return method.getName();
/*     */             }
/*     */           }));
/*  95 */     METHOD_COMPARATOR = (Comparator<Method>)comparator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ReflectiveAspectJAdvisorFactory() {
/* 106 */     this((BeanFactory)null);
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
/*     */   public ReflectiveAspectJAdvisorFactory(BeanFactory beanFactory) {
/* 119 */     this.beanFactory = beanFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Advisor> getAdvisors(MetadataAwareAspectInstanceFactory aspectInstanceFactory) {
/* 125 */     Class<?> aspectClass = aspectInstanceFactory.getAspectMetadata().getAspectClass();
/* 126 */     String aspectName = aspectInstanceFactory.getAspectMetadata().getAspectName();
/* 127 */     validate(aspectClass);
/*     */ 
/*     */ 
/*     */     
/* 131 */     MetadataAwareAspectInstanceFactory lazySingletonAspectInstanceFactory = new LazySingletonAspectInstanceFactoryDecorator(aspectInstanceFactory);
/*     */ 
/*     */     
/* 134 */     List<Advisor> advisors = new ArrayList<Advisor>();
/* 135 */     for (Method method : getAdvisorMethods(aspectClass)) {
/* 136 */       Advisor advisor = getAdvisor(method, lazySingletonAspectInstanceFactory, advisors.size(), aspectName);
/* 137 */       if (advisor != null) {
/* 138 */         advisors.add(advisor);
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 143 */     if (!advisors.isEmpty() && lazySingletonAspectInstanceFactory.getAspectMetadata().isLazilyInstantiated()) {
/* 144 */       SyntheticInstantiationAdvisor syntheticInstantiationAdvisor = new SyntheticInstantiationAdvisor(lazySingletonAspectInstanceFactory);
/* 145 */       advisors.add(0, syntheticInstantiationAdvisor);
/*     */     } 
/*     */ 
/*     */     
/* 149 */     for (Field field : aspectClass.getDeclaredFields()) {
/* 150 */       Advisor advisor = getDeclareParentsAdvisor(field);
/* 151 */       if (advisor != null) {
/* 152 */         advisors.add(advisor);
/*     */       }
/*     */     } 
/*     */     
/* 156 */     return advisors;
/*     */   }
/*     */   
/*     */   private List<Method> getAdvisorMethods(Class<?> aspectClass) {
/* 160 */     final List<Method> methods = new ArrayList<Method>();
/* 161 */     ReflectionUtils.doWithMethods(aspectClass, new ReflectionUtils.MethodCallback()
/*     */         {
/*     */           public void doWith(Method method) throws IllegalArgumentException
/*     */           {
/* 165 */             if (AnnotationUtils.getAnnotation(method, Pointcut.class) == null) {
/* 166 */               methods.add(method);
/*     */             }
/*     */           }
/*     */         });
/* 170 */     Collections.sort(methods, METHOD_COMPARATOR);
/* 171 */     return methods;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Advisor getDeclareParentsAdvisor(Field introductionField) {
/* 182 */     DeclareParents declareParents = introductionField.<DeclareParents>getAnnotation(DeclareParents.class);
/* 183 */     if (declareParents == null)
/*     */     {
/* 185 */       return null;
/*     */     }
/*     */     
/* 188 */     if (DeclareParents.class == declareParents.defaultImpl()) {
/* 189 */       throw new IllegalStateException("'defaultImpl' attribute must be set on DeclareParents");
/*     */     }
/*     */     
/* 192 */     return (Advisor)new DeclareParentsAdvisor(introductionField
/* 193 */         .getType(), declareParents.value(), declareParents.defaultImpl());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Advisor getAdvisor(Method candidateAdviceMethod, MetadataAwareAspectInstanceFactory aspectInstanceFactory, int declarationOrderInAspect, String aspectName) {
/* 201 */     validate(aspectInstanceFactory.getAspectMetadata().getAspectClass());
/*     */     
/* 203 */     AspectJExpressionPointcut expressionPointcut = getPointcut(candidateAdviceMethod, aspectInstanceFactory
/* 204 */         .getAspectMetadata().getAspectClass());
/* 205 */     if (expressionPointcut == null) {
/* 206 */       return null;
/*     */     }
/*     */     
/* 209 */     return (Advisor)new InstantiationModelAwarePointcutAdvisorImpl(expressionPointcut, candidateAdviceMethod, this, aspectInstanceFactory, declarationOrderInAspect, aspectName);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private AspectJExpressionPointcut getPointcut(Method candidateAdviceMethod, Class<?> candidateAspectClass) {
/* 215 */     AbstractAspectJAdvisorFactory.AspectJAnnotation<?> aspectJAnnotation = AbstractAspectJAdvisorFactory.findAspectJAnnotationOnMethod(candidateAdviceMethod);
/* 216 */     if (aspectJAnnotation == null) {
/* 217 */       return null;
/*     */     }
/*     */     
/* 220 */     AspectJExpressionPointcut ajexp = new AspectJExpressionPointcut(candidateAspectClass, new String[0], new Class[0]);
/*     */     
/* 222 */     ajexp.setExpression(aspectJAnnotation.getPointcutExpression());
/* 223 */     ajexp.setBeanFactory(this.beanFactory);
/* 224 */     return ajexp; } public Advice getAdvice(Method candidateAdviceMethod, AspectJExpressionPointcut expressionPointcut, MetadataAwareAspectInstanceFactory aspectInstanceFactory, int declarationOrder, String aspectName) {
/*     */     AspectJAroundAdvice aspectJAroundAdvice;
/*     */     AspectJMethodBeforeAdvice aspectJMethodBeforeAdvice;
/*     */     AspectJAfterAdvice aspectJAfterAdvice;
/*     */     AspectJAfterReturningAdvice aspectJAfterReturningAdvice;
/*     */     AspectJAfterThrowingAdvice aspectJAfterThrowingAdvice;
/*     */     AfterReturning afterReturningAnnotation;
/*     */     AfterThrowing afterThrowingAnnotation;
/* 232 */     Class<?> candidateAspectClass = aspectInstanceFactory.getAspectMetadata().getAspectClass();
/* 233 */     validate(candidateAspectClass);
/*     */ 
/*     */     
/* 236 */     AbstractAspectJAdvisorFactory.AspectJAnnotation<?> aspectJAnnotation = AbstractAspectJAdvisorFactory.findAspectJAnnotationOnMethod(candidateAdviceMethod);
/* 237 */     if (aspectJAnnotation == null) {
/* 238 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 243 */     if (!isAspect(candidateAspectClass)) {
/* 244 */       throw new AopConfigException("Advice must be declared inside an aspect type: Offending method '" + candidateAdviceMethod + "' in class [" + candidateAspectClass
/*     */           
/* 246 */           .getName() + "]");
/*     */     }
/*     */     
/* 249 */     if (this.logger.isDebugEnabled()) {
/* 250 */       this.logger.debug("Found AspectJ method: " + candidateAdviceMethod);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 255 */     switch (aspectJAnnotation.getAnnotationType()) {
/*     */       case AtPointcut:
/* 257 */         if (this.logger.isDebugEnabled()) {
/* 258 */           this.logger.debug("Processing pointcut '" + candidateAdviceMethod.getName() + "'");
/*     */         }
/* 260 */         return null;
/*     */       case AtAround:
/* 262 */         aspectJAroundAdvice = new AspectJAroundAdvice(candidateAdviceMethod, expressionPointcut, aspectInstanceFactory);
/*     */         break;
/*     */       
/*     */       case AtBefore:
/* 266 */         aspectJMethodBeforeAdvice = new AspectJMethodBeforeAdvice(candidateAdviceMethod, expressionPointcut, aspectInstanceFactory);
/*     */         break;
/*     */       
/*     */       case AtAfter:
/* 270 */         aspectJAfterAdvice = new AspectJAfterAdvice(candidateAdviceMethod, expressionPointcut, aspectInstanceFactory);
/*     */         break;
/*     */       
/*     */       case AtAfterReturning:
/* 274 */         aspectJAfterReturningAdvice = new AspectJAfterReturningAdvice(candidateAdviceMethod, expressionPointcut, aspectInstanceFactory);
/*     */         
/* 276 */         afterReturningAnnotation = (AfterReturning)aspectJAnnotation.getAnnotation();
/* 277 */         if (StringUtils.hasText(afterReturningAnnotation.returning())) {
/* 278 */           aspectJAfterReturningAdvice.setReturningName(afterReturningAnnotation.returning());
/*     */         }
/*     */         break;
/*     */       case AtAfterThrowing:
/* 282 */         aspectJAfterThrowingAdvice = new AspectJAfterThrowingAdvice(candidateAdviceMethod, expressionPointcut, aspectInstanceFactory);
/*     */         
/* 284 */         afterThrowingAnnotation = (AfterThrowing)aspectJAnnotation.getAnnotation();
/* 285 */         if (StringUtils.hasText(afterThrowingAnnotation.throwing())) {
/* 286 */           aspectJAfterThrowingAdvice.setThrowingName(afterThrowingAnnotation.throwing());
/*     */         }
/*     */         break;
/*     */       default:
/* 290 */         throw new UnsupportedOperationException("Unsupported advice type on method: " + candidateAdviceMethod);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 295 */     aspectJAfterThrowingAdvice.setAspectName(aspectName);
/* 296 */     aspectJAfterThrowingAdvice.setDeclarationOrder(declarationOrder);
/* 297 */     String[] argNames = this.parameterNameDiscoverer.getParameterNames(candidateAdviceMethod);
/* 298 */     if (argNames != null) {
/* 299 */       aspectJAfterThrowingAdvice.setArgumentNamesFromStringArray(argNames);
/*     */     }
/* 301 */     aspectJAfterThrowingAdvice.calculateArgumentBindings();
/*     */     
/* 303 */     return (Advice)aspectJAfterThrowingAdvice;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static class SyntheticInstantiationAdvisor
/*     */     extends DefaultPointcutAdvisor
/*     */   {
/*     */     public SyntheticInstantiationAdvisor(MetadataAwareAspectInstanceFactory aif) {
/* 316 */       super(aif.getAspectMetadata().getPerClausePointcut(), (Advice)new MethodBeforeAdvice(aif)
/*     */           {
/*     */             public void before(Method method, Object[] args, Object target)
/*     */             {
/* 320 */               aif.getAspectInstance();
/*     */             }
/*     */           });
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\aspectj\annotation\ReflectiveAspectJAdvisorFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */