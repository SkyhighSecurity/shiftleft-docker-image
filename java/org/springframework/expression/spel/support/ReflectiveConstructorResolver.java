/*     */ package org.springframework.expression.spel.support;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.core.convert.TypeDescriptor;
/*     */ import org.springframework.expression.AccessException;
/*     */ import org.springframework.expression.ConstructorExecutor;
/*     */ import org.springframework.expression.ConstructorResolver;
/*     */ import org.springframework.expression.EvaluationContext;
/*     */ import org.springframework.expression.EvaluationException;
/*     */ import org.springframework.expression.TypeConverter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ReflectiveConstructorResolver
/*     */   implements ConstructorResolver
/*     */ {
/*     */   public ConstructorExecutor resolve(EvaluationContext context, String typeName, List<TypeDescriptor> argumentTypes) throws AccessException {
/*     */     try {
/*  57 */       TypeConverter typeConverter = context.getTypeConverter();
/*  58 */       Class<?> type = context.getTypeLocator().findType(typeName);
/*  59 */       Constructor[] arrayOfConstructor = (Constructor[])type.getConstructors();
/*     */       
/*  61 */       Arrays.sort(arrayOfConstructor, (Comparator)new Comparator<Constructor<?>>()
/*     */           {
/*     */             public int compare(Constructor<?> c1, Constructor<?> c2) {
/*  64 */               int c1pl = (c1.getParameterTypes()).length;
/*  65 */               int c2pl = (c2.getParameterTypes()).length;
/*  66 */               return (c1pl < c2pl) ? -1 : ((c1pl > c2pl) ? 1 : 0);
/*     */             }
/*     */           });
/*     */       
/*  70 */       Constructor<?> closeMatch = null;
/*  71 */       Constructor<?> matchRequiringConversion = null;
/*     */       
/*  73 */       for (Constructor<?> ctor : arrayOfConstructor) {
/*  74 */         Class<?>[] paramTypes = ctor.getParameterTypes();
/*  75 */         List<TypeDescriptor> paramDescriptors = new ArrayList<TypeDescriptor>(paramTypes.length);
/*  76 */         for (int i = 0; i < paramTypes.length; i++) {
/*  77 */           paramDescriptors.add(new TypeDescriptor(new MethodParameter(ctor, i)));
/*     */         }
/*  79 */         ReflectionHelper.ArgumentsMatchInfo matchInfo = null;
/*  80 */         if (ctor.isVarArgs() && argumentTypes.size() >= paramTypes.length - 1) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*  87 */           matchInfo = ReflectionHelper.compareArgumentsVarargs(paramDescriptors, argumentTypes, typeConverter);
/*     */         }
/*  89 */         else if (paramTypes.length == argumentTypes.size()) {
/*     */           
/*  91 */           matchInfo = ReflectionHelper.compareArguments(paramDescriptors, argumentTypes, typeConverter);
/*     */         } 
/*  93 */         if (matchInfo != null) {
/*  94 */           if (matchInfo.isExactMatch()) {
/*  95 */             return new ReflectiveConstructorExecutor(ctor);
/*     */           }
/*  97 */           if (matchInfo.isCloseMatch()) {
/*  98 */             closeMatch = ctor;
/*     */           }
/* 100 */           else if (matchInfo.isMatchRequiringConversion()) {
/* 101 */             matchRequiringConversion = ctor;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 106 */       if (closeMatch != null) {
/* 107 */         return new ReflectiveConstructorExecutor(closeMatch);
/*     */       }
/* 109 */       if (matchRequiringConversion != null) {
/* 110 */         return new ReflectiveConstructorExecutor(matchRequiringConversion);
/*     */       }
/*     */       
/* 113 */       return null;
/*     */     
/*     */     }
/* 116 */     catch (EvaluationException ex) {
/* 117 */       throw new AccessException("Failed to resolve constructor", ex);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\expression\spel\support\ReflectiveConstructorResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */