/*     */ package org.springframework.aop.aspectj.annotation;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.StringTokenizer;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.aspectj.lang.annotation.After;
/*     */ import org.aspectj.lang.annotation.AfterReturning;
/*     */ import org.aspectj.lang.annotation.AfterThrowing;
/*     */ import org.aspectj.lang.annotation.Around;
/*     */ import org.aspectj.lang.annotation.Aspect;
/*     */ import org.aspectj.lang.annotation.Before;
/*     */ import org.aspectj.lang.annotation.Pointcut;
/*     */ import org.aspectj.lang.reflect.AjType;
/*     */ import org.aspectj.lang.reflect.AjTypeSystem;
/*     */ import org.aspectj.lang.reflect.PerClauseKind;
/*     */ import org.springframework.aop.framework.AopConfigException;
/*     */ import org.springframework.core.ParameterNameDiscoverer;
/*     */ import org.springframework.core.annotation.AnnotationUtils;
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
/*     */ public abstract class AbstractAspectJAdvisorFactory
/*     */   implements AspectJAdvisorFactory
/*     */ {
/*     */   private static final String AJC_MAGIC = "ajc$";
/*  62 */   private static final Class<?>[] ASPECTJ_ANNOTATION_CLASSES = new Class[] { Pointcut.class, Around.class, Before.class, After.class, AfterReturning.class, AfterThrowing.class };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  67 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*  69 */   protected final ParameterNameDiscoverer parameterNameDiscoverer = new AspectJAnnotationParameterNameDiscoverer();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAspect(Class<?> clazz) {
/*  80 */     return (hasAspectAnnotation(clazz) && !compiledByAjc(clazz));
/*     */   }
/*     */   
/*     */   private boolean hasAspectAnnotation(Class<?> clazz) {
/*  84 */     return (AnnotationUtils.findAnnotation(clazz, Aspect.class) != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean compiledByAjc(Class<?> clazz) {
/*  95 */     for (Field field : clazz.getDeclaredFields()) {
/*  96 */       if (field.getName().startsWith("ajc$")) {
/*  97 */         return true;
/*     */       }
/*     */     } 
/* 100 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void validate(Class<?> aspectClass) throws AopConfigException {
/* 106 */     if (aspectClass.getSuperclass().getAnnotation(Aspect.class) != null && 
/* 107 */       !Modifier.isAbstract(aspectClass.getSuperclass().getModifiers())) {
/* 108 */       throw new AopConfigException("[" + aspectClass.getName() + "] cannot extend concrete aspect [" + aspectClass
/* 109 */           .getSuperclass().getName() + "]");
/*     */     }
/*     */     
/* 112 */     AjType<?> ajType = AjTypeSystem.getAjType(aspectClass);
/* 113 */     if (!ajType.isAspect()) {
/* 114 */       throw new NotAnAtAspectException(aspectClass);
/*     */     }
/* 116 */     if (ajType.getPerClause().getKind() == PerClauseKind.PERCFLOW) {
/* 117 */       throw new AopConfigException(aspectClass.getName() + " uses percflow instantiation model: This is not supported in Spring AOP.");
/*     */     }
/*     */     
/* 120 */     if (ajType.getPerClause().getKind() == PerClauseKind.PERCFLOWBELOW) {
/* 121 */       throw new AopConfigException(aspectClass.getName() + " uses percflowbelow instantiation model: This is not supported in Spring AOP.");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static AspectJAnnotation<?> findAspectJAnnotationOnMethod(Method method) {
/* 132 */     for (Class<?> clazz : ASPECTJ_ANNOTATION_CLASSES) {
/* 133 */       AspectJAnnotation<?> foundAnnotation = findAnnotation(method, clazz);
/* 134 */       if (foundAnnotation != null) {
/* 135 */         return foundAnnotation;
/*     */       }
/*     */     } 
/* 138 */     return null;
/*     */   }
/*     */   
/*     */   private static <A extends Annotation> AspectJAnnotation<A> findAnnotation(Method method, Class<A> toLookFor) {
/* 142 */     Annotation annotation = AnnotationUtils.findAnnotation(method, toLookFor);
/* 143 */     if (annotation != null) {
/* 144 */       return new AspectJAnnotation<A>((A)annotation);
/*     */     }
/*     */     
/* 147 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected enum AspectJAnnotationType
/*     */   {
/* 158 */     AtPointcut, AtAround, AtBefore, AtAfter, AtAfterReturning, AtAfterThrowing;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static class AspectJAnnotation<A extends Annotation>
/*     */   {
/* 168 */     private static final String[] EXPRESSION_ATTRIBUTES = new String[] { "pointcut", "value" };
/*     */     
/* 170 */     private static Map<Class<?>, AbstractAspectJAdvisorFactory.AspectJAnnotationType> annotationTypeMap = new HashMap<Class<?>, AbstractAspectJAdvisorFactory.AspectJAnnotationType>(8);
/*     */     private final A annotation;
/*     */     
/*     */     static {
/* 174 */       annotationTypeMap.put(Pointcut.class, AbstractAspectJAdvisorFactory.AspectJAnnotationType.AtPointcut);
/* 175 */       annotationTypeMap.put(Around.class, AbstractAspectJAdvisorFactory.AspectJAnnotationType.AtAround);
/* 176 */       annotationTypeMap.put(Before.class, AbstractAspectJAdvisorFactory.AspectJAnnotationType.AtBefore);
/* 177 */       annotationTypeMap.put(After.class, AbstractAspectJAdvisorFactory.AspectJAnnotationType.AtAfter);
/* 178 */       annotationTypeMap.put(AfterReturning.class, AbstractAspectJAdvisorFactory.AspectJAnnotationType.AtAfterReturning);
/* 179 */       annotationTypeMap.put(AfterThrowing.class, AbstractAspectJAdvisorFactory.AspectJAnnotationType.AtAfterThrowing);
/*     */     }
/*     */ 
/*     */     
/*     */     private final AbstractAspectJAdvisorFactory.AspectJAnnotationType annotationType;
/*     */     
/*     */     private final String pointcutExpression;
/*     */     
/*     */     private final String argumentNames;
/*     */ 
/*     */     
/*     */     public AspectJAnnotation(A annotation) {
/* 191 */       this.annotation = annotation;
/* 192 */       this.annotationType = determineAnnotationType(annotation);
/*     */       try {
/* 194 */         this.pointcutExpression = resolveExpression(annotation);
/* 195 */         this.argumentNames = (String)AnnotationUtils.getValue((Annotation)annotation, "argNames");
/*     */       }
/* 197 */       catch (Exception ex) {
/* 198 */         throw new IllegalArgumentException((new StringBuilder()).append(annotation).append(" is not a valid AspectJ annotation").toString(), ex);
/*     */       } 
/*     */     }
/*     */     
/*     */     private AbstractAspectJAdvisorFactory.AspectJAnnotationType determineAnnotationType(A annotation) {
/* 203 */       AbstractAspectJAdvisorFactory.AspectJAnnotationType type = annotationTypeMap.get(annotation.annotationType());
/* 204 */       if (type != null) {
/* 205 */         return type;
/*     */       }
/* 207 */       throw new IllegalStateException("Unknown annotation type: " + annotation);
/*     */     }
/*     */     
/*     */     private String resolveExpression(A annotation) {
/* 211 */       for (String attributeName : EXPRESSION_ATTRIBUTES) {
/* 212 */         String candidate = (String)AnnotationUtils.getValue((Annotation)annotation, attributeName);
/* 213 */         if (StringUtils.hasText(candidate)) {
/* 214 */           return candidate;
/*     */         }
/*     */       } 
/* 217 */       throw new IllegalStateException("Failed to resolve expression: " + annotation);
/*     */     }
/*     */     
/*     */     public AbstractAspectJAdvisorFactory.AspectJAnnotationType getAnnotationType() {
/* 221 */       return this.annotationType;
/*     */     }
/*     */     
/*     */     public A getAnnotation() {
/* 225 */       return this.annotation;
/*     */     }
/*     */     
/*     */     public String getPointcutExpression() {
/* 229 */       return this.pointcutExpression;
/*     */     }
/*     */     
/*     */     public String getArgumentNames() {
/* 233 */       return this.argumentNames;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 238 */       return this.annotation.toString();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class AspectJAnnotationParameterNameDiscoverer
/*     */     implements ParameterNameDiscoverer
/*     */   {
/*     */     private AspectJAnnotationParameterNameDiscoverer() {}
/*     */ 
/*     */     
/*     */     public String[] getParameterNames(Method method) {
/* 251 */       if ((method.getParameterTypes()).length == 0) {
/* 252 */         return new String[0];
/*     */       }
/* 254 */       AbstractAspectJAdvisorFactory.AspectJAnnotation<?> annotation = AbstractAspectJAdvisorFactory.findAspectJAnnotationOnMethod(method);
/* 255 */       if (annotation == null) {
/* 256 */         return null;
/*     */       }
/* 258 */       StringTokenizer nameTokens = new StringTokenizer(annotation.getArgumentNames(), ",");
/* 259 */       if (nameTokens.countTokens() > 0) {
/* 260 */         String[] names = new String[nameTokens.countTokens()];
/* 261 */         for (int i = 0; i < names.length; i++) {
/* 262 */           names[i] = nameTokens.nextToken();
/*     */         }
/* 264 */         return names;
/*     */       } 
/*     */       
/* 267 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String[] getParameterNames(Constructor<?> ctor) {
/* 273 */       throw new UnsupportedOperationException("Spring AOP cannot handle constructor advice");
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\aspectj\annotation\AbstractAspectJAdvisorFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */