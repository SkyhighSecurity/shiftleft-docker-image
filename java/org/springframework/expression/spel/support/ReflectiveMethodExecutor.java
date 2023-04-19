/*     */ package org.springframework.expression.spel.support;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.core.convert.TypeDescriptor;
/*     */ import org.springframework.expression.AccessException;
/*     */ import org.springframework.expression.EvaluationContext;
/*     */ import org.springframework.expression.MethodExecutor;
/*     */ import org.springframework.expression.TypedValue;
/*     */ import org.springframework.util.ReflectionUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ReflectiveMethodExecutor
/*     */   implements MethodExecutor
/*     */ {
/*     */   private final Method method;
/*     */   private final Integer varargsPosition;
/*     */   private boolean computedPublicDeclaringClass = false;
/*     */   private Class<?> publicDeclaringClass;
/*     */   private boolean argumentConversionOccurred = false;
/*     */   
/*     */   public ReflectiveMethodExecutor(Method method) {
/*  49 */     this.method = method;
/*  50 */     if (method.isVarArgs()) {
/*  51 */       Class<?>[] paramTypes = method.getParameterTypes();
/*  52 */       this.varargsPosition = Integer.valueOf(paramTypes.length - 1);
/*     */     } else {
/*     */       
/*  55 */       this.varargsPosition = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Method getMethod() {
/*  61 */     return this.method;
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
/*     */   public Class<?> getPublicDeclaringClass() {
/*  73 */     if (!this.computedPublicDeclaringClass) {
/*  74 */       this.publicDeclaringClass = discoverPublicClass(this.method, this.method.getDeclaringClass());
/*  75 */       this.computedPublicDeclaringClass = true;
/*     */     } 
/*  77 */     return this.publicDeclaringClass;
/*     */   }
/*     */   
/*     */   private Class<?> discoverPublicClass(Method method, Class<?> clazz) {
/*  81 */     if (Modifier.isPublic(clazz.getModifiers())) {
/*     */       try {
/*  83 */         clazz.getDeclaredMethod(method.getName(), method.getParameterTypes());
/*  84 */         return clazz;
/*     */       }
/*  86 */       catch (NoSuchMethodException noSuchMethodException) {}
/*     */     }
/*     */ 
/*     */     
/*  90 */     Class<?>[] ifcs = clazz.getInterfaces();
/*  91 */     for (Class<?> ifc : ifcs) {
/*  92 */       discoverPublicClass(method, ifc);
/*     */     }
/*  94 */     if (clazz.getSuperclass() != null) {
/*  95 */       return discoverPublicClass(method, clazz.getSuperclass());
/*     */     }
/*  97 */     return null;
/*     */   }
/*     */   
/*     */   public boolean didArgumentConversionOccur() {
/* 101 */     return this.argumentConversionOccurred;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public TypedValue execute(EvaluationContext context, Object target, Object... arguments) throws AccessException {
/*     */     try {
/* 108 */       if (arguments != null) {
/* 109 */         this.argumentConversionOccurred = ReflectionHelper.convertArguments(context
/* 110 */             .getTypeConverter(), arguments, this.method, this.varargsPosition);
/* 111 */         if (this.method.isVarArgs()) {
/* 112 */           arguments = ReflectionHelper.setupArgumentsForVarargsInvocation(this.method
/* 113 */               .getParameterTypes(), arguments);
/*     */         }
/*     */       } 
/* 116 */       ReflectionUtils.makeAccessible(this.method);
/* 117 */       Object value = this.method.invoke(target, arguments);
/* 118 */       return new TypedValue(value, (new TypeDescriptor(new MethodParameter(this.method, -1))).narrow(value));
/*     */     }
/* 120 */     catch (Exception ex) {
/* 121 */       throw new AccessException("Problem invoking method: " + this.method, ex);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\expression\spel\support\ReflectiveMethodExecutor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */