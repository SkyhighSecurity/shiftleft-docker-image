/*     */ package org.springframework.context.expression;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Arrays;
/*     */ import org.springframework.core.ParameterNameDiscoverer;
/*     */ import org.springframework.expression.spel.support.StandardEvaluationContext;
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
/*     */ public class MethodBasedEvaluationContext
/*     */   extends StandardEvaluationContext
/*     */ {
/*     */   private final Method method;
/*     */   private final Object[] arguments;
/*     */   private final ParameterNameDiscoverer parameterNameDiscoverer;
/*     */   private boolean argumentsLoaded = false;
/*     */   
/*     */   public MethodBasedEvaluationContext(Object rootObject, Method method, Object[] arguments, ParameterNameDiscoverer parameterNameDiscoverer) {
/*  55 */     super(rootObject);
/*  56 */     this.method = method;
/*  57 */     this.arguments = arguments;
/*  58 */     this.parameterNameDiscoverer = parameterNameDiscoverer;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object lookupVariable(String name) {
/*  64 */     Object variable = super.lookupVariable(name);
/*  65 */     if (variable != null) {
/*  66 */       return variable;
/*     */     }
/*  68 */     if (!this.argumentsLoaded) {
/*  69 */       lazyLoadArguments();
/*  70 */       this.argumentsLoaded = true;
/*  71 */       variable = super.lookupVariable(name);
/*     */     } 
/*  73 */     return variable;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void lazyLoadArguments() {
/*  81 */     if (ObjectUtils.isEmpty(this.arguments)) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/*  86 */     String[] paramNames = this.parameterNameDiscoverer.getParameterNames(this.method);
/*  87 */     int paramCount = (paramNames != null) ? paramNames.length : (this.method.getParameterTypes()).length;
/*  88 */     int argsCount = this.arguments.length;
/*     */     
/*  90 */     for (int i = 0; i < paramCount; i++) {
/*  91 */       Object value = null;
/*  92 */       if (argsCount > paramCount && i == paramCount - 1) {
/*     */         
/*  94 */         value = Arrays.copyOfRange(this.arguments, i, argsCount);
/*     */       }
/*  96 */       else if (argsCount > i) {
/*     */         
/*  98 */         value = this.arguments[i];
/*     */       } 
/* 100 */       setVariable("a" + i, value);
/* 101 */       setVariable("p" + i, value);
/* 102 */       if (paramNames != null)
/* 103 */         setVariable(paramNames[i], value); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\expression\MethodBasedEvaluationContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */