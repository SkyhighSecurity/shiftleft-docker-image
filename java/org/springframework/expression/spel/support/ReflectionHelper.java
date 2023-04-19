/*     */ package org.springframework.expression.spel.support;
/*     */ 
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.List;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.core.convert.TypeDescriptor;
/*     */ import org.springframework.expression.EvaluationException;
/*     */ import org.springframework.expression.TypeConverter;
/*     */ import org.springframework.expression.spel.SpelEvaluationException;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.CollectionUtils;
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
/*     */ public class ReflectionHelper
/*     */ {
/*     */   static ArgumentsMatchInfo compareArguments(List<TypeDescriptor> expectedArgTypes, List<TypeDescriptor> suppliedArgTypes, TypeConverter typeConverter) {
/*  56 */     Assert.isTrue((expectedArgTypes.size() == suppliedArgTypes.size()), "Expected argument types and supplied argument types should be arrays of same length");
/*     */ 
/*     */     
/*  59 */     ArgumentsMatchKind match = ArgumentsMatchKind.EXACT;
/*  60 */     for (int i = 0; i < expectedArgTypes.size() && match != null; i++) {
/*  61 */       TypeDescriptor suppliedArg = suppliedArgTypes.get(i);
/*  62 */       TypeDescriptor expectedArg = expectedArgTypes.get(i);
/*  63 */       if (!expectedArg.equals(suppliedArg))
/*     */       {
/*  65 */         if (suppliedArg == null) {
/*  66 */           if (expectedArg.isPrimitive()) {
/*  67 */             match = null;
/*     */           
/*     */           }
/*     */         }
/*  71 */         else if (suppliedArg.isAssignableTo(expectedArg)) {
/*  72 */           if (match != ArgumentsMatchKind.REQUIRES_CONVERSION) {
/*  73 */             match = ArgumentsMatchKind.CLOSE;
/*     */           }
/*     */         }
/*  76 */         else if (typeConverter.canConvert(suppliedArg, expectedArg)) {
/*  77 */           match = ArgumentsMatchKind.REQUIRES_CONVERSION;
/*     */         } else {
/*     */           
/*  80 */           match = null;
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/*  85 */     return (match != null) ? new ArgumentsMatchInfo(match) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getTypeDifferenceWeight(List<TypeDescriptor> paramTypes, List<TypeDescriptor> argTypes) {
/*  92 */     int result = 0;
/*  93 */     for (int i = 0; i < paramTypes.size(); i++) {
/*  94 */       TypeDescriptor paramType = paramTypes.get(i);
/*  95 */       TypeDescriptor argType = (i < argTypes.size()) ? argTypes.get(i) : null;
/*  96 */       if (argType == null) {
/*  97 */         if (paramType.isPrimitive()) {
/*  98 */           return Integer.MAX_VALUE;
/*     */         }
/*     */       } else {
/*     */         
/* 102 */         Class<?> paramTypeClazz = paramType.getType();
/* 103 */         if (!ClassUtils.isAssignable(paramTypeClazz, argType.getType())) {
/* 104 */           return Integer.MAX_VALUE;
/*     */         }
/* 106 */         if (paramTypeClazz.isPrimitive()) {
/* 107 */           paramTypeClazz = Object.class;
/*     */         }
/* 109 */         Class<?> superClass = argType.getType().getSuperclass();
/* 110 */         while (superClass != null) {
/* 111 */           if (paramTypeClazz.equals(superClass)) {
/* 112 */             result += 2;
/* 113 */             superClass = null; continue;
/*     */           } 
/* 115 */           if (ClassUtils.isAssignable(paramTypeClazz, superClass)) {
/* 116 */             result += 2;
/* 117 */             superClass = superClass.getSuperclass();
/*     */             continue;
/*     */           } 
/* 120 */           superClass = null;
/*     */         } 
/*     */         
/* 123 */         if (paramTypeClazz.isInterface()) {
/* 124 */           result++;
/*     */         }
/*     */       } 
/*     */     } 
/* 128 */     return result;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static ArgumentsMatchInfo compareArgumentsVarargs(List<TypeDescriptor> expectedArgTypes, List<TypeDescriptor> suppliedArgTypes, TypeConverter typeConverter) {
/* 145 */     Assert.isTrue(!CollectionUtils.isEmpty(expectedArgTypes), "Expected arguments must at least include one array (the varargs parameter)");
/*     */     
/* 147 */     Assert.isTrue(((TypeDescriptor)expectedArgTypes.get(expectedArgTypes.size() - 1)).isArray(), "Final expected argument should be array type (the varargs parameter)");
/*     */ 
/*     */     
/* 150 */     ArgumentsMatchKind match = ArgumentsMatchKind.EXACT;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 155 */     int argCountUpToVarargs = expectedArgTypes.size() - 1;
/* 156 */     for (int i = 0; i < argCountUpToVarargs && match != null; i++) {
/* 157 */       TypeDescriptor suppliedArg = suppliedArgTypes.get(i);
/* 158 */       TypeDescriptor expectedArg = expectedArgTypes.get(i);
/* 159 */       if (suppliedArg == null) {
/* 160 */         if (expectedArg.isPrimitive()) {
/* 161 */           match = null;
/*     */         
/*     */         }
/*     */       }
/* 165 */       else if (!expectedArg.equals(suppliedArg)) {
/* 166 */         if (suppliedArg.isAssignableTo(expectedArg)) {
/* 167 */           if (match != ArgumentsMatchKind.REQUIRES_CONVERSION) {
/* 168 */             match = ArgumentsMatchKind.CLOSE;
/*     */           }
/*     */         }
/* 171 */         else if (typeConverter.canConvert(suppliedArg, expectedArg)) {
/* 172 */           match = ArgumentsMatchKind.REQUIRES_CONVERSION;
/*     */         } else {
/*     */           
/* 175 */           match = null;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 182 */     if (match == null) {
/* 183 */       return null;
/*     */     }
/*     */     
/* 186 */     if (suppliedArgTypes.size() != expectedArgTypes.size() || 
/* 187 */       !((TypeDescriptor)expectedArgTypes.get(expectedArgTypes.size() - 1)).equals(suppliedArgTypes
/* 188 */         .get(suppliedArgTypes.size() - 1))) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 195 */       TypeDescriptor varargsDesc = expectedArgTypes.get(expectedArgTypes.size() - 1);
/* 196 */       Class<?> varargsParamType = varargsDesc.getElementTypeDescriptor().getType();
/*     */ 
/*     */       
/* 199 */       for (int j = expectedArgTypes.size() - 1; j < suppliedArgTypes.size(); j++) {
/* 200 */         TypeDescriptor suppliedArg = suppliedArgTypes.get(j);
/* 201 */         if (suppliedArg == null) {
/* 202 */           if (varargsParamType.isPrimitive()) {
/* 203 */             match = null;
/*     */           
/*     */           }
/*     */         }
/* 207 */         else if (varargsParamType != suppliedArg.getType()) {
/* 208 */           if (ClassUtils.isAssignable(varargsParamType, suppliedArg.getType())) {
/* 209 */             if (match != ArgumentsMatchKind.REQUIRES_CONVERSION) {
/* 210 */               match = ArgumentsMatchKind.CLOSE;
/*     */             }
/*     */           }
/* 213 */           else if (typeConverter.canConvert(suppliedArg, TypeDescriptor.valueOf(varargsParamType))) {
/* 214 */             match = ArgumentsMatchKind.REQUIRES_CONVERSION;
/*     */           } else {
/*     */             
/* 217 */             match = null;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 224 */     return (match != null) ? new ArgumentsMatchInfo(match) : null;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean convertAllArguments(TypeConverter converter, Object[] arguments, Method method) throws SpelEvaluationException {
/* 245 */     Integer varargsPosition = method.isVarArgs() ? Integer.valueOf((method.getParameterTypes()).length - 1) : null;
/* 246 */     return convertArguments(converter, arguments, method, varargsPosition);
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
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean convertArguments(TypeConverter converter, Object[] arguments, Object methodOrCtor, Integer varargsPosition) throws EvaluationException {
/*     */     int i;
/* 263 */     boolean conversionOccurred = false;
/* 264 */     if (varargsPosition == null) {
/* 265 */       for (int j = 0; j < arguments.length; j++) {
/* 266 */         TypeDescriptor targetType = new TypeDescriptor(MethodParameter.forMethodOrConstructor(methodOrCtor, j));
/* 267 */         Object argument = arguments[j];
/* 268 */         arguments[j] = converter.convertValue(argument, TypeDescriptor.forObject(argument), targetType);
/* 269 */         i = conversionOccurred | ((argument != arguments[j]) ? 1 : 0);
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 274 */       for (int j = 0; j < varargsPosition.intValue(); j++) {
/* 275 */         TypeDescriptor targetType = new TypeDescriptor(MethodParameter.forMethodOrConstructor(methodOrCtor, j));
/* 276 */         Object argument = arguments[j];
/* 277 */         arguments[j] = converter.convertValue(argument, TypeDescriptor.forObject(argument), targetType);
/* 278 */         i |= (argument != arguments[j]) ? 1 : 0;
/*     */       } 
/* 280 */       MethodParameter methodParam = MethodParameter.forMethodOrConstructor(methodOrCtor, varargsPosition.intValue());
/* 281 */       if (varargsPosition.intValue() == arguments.length - 1) {
/*     */ 
/*     */         
/* 284 */         TypeDescriptor targetType = new TypeDescriptor(methodParam);
/* 285 */         Object argument = arguments[varargsPosition.intValue()];
/* 286 */         TypeDescriptor sourceType = TypeDescriptor.forObject(argument);
/* 287 */         arguments[varargsPosition.intValue()] = converter.convertValue(argument, sourceType, targetType);
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 292 */         if (argument != arguments[varargsPosition.intValue()] && 
/* 293 */           !isFirstEntryInArray(argument, arguments[varargsPosition.intValue()])) {
/* 294 */           i = 1;
/*     */         }
/*     */       }
/*     */       else {
/*     */         
/* 299 */         TypeDescriptor targetType = (new TypeDescriptor(methodParam)).getElementTypeDescriptor();
/* 300 */         for (int k = varargsPosition.intValue(); k < arguments.length; k++) {
/* 301 */           Object argument = arguments[k];
/* 302 */           arguments[k] = converter.convertValue(argument, TypeDescriptor.forObject(argument), targetType);
/* 303 */           i |= (argument != arguments[k]) ? 1 : 0;
/*     */         } 
/*     */       } 
/*     */     } 
/* 307 */     return i;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isFirstEntryInArray(Object value, Object possibleArray) {
/* 317 */     if (possibleArray == null) {
/* 318 */       return false;
/*     */     }
/* 320 */     Class<?> type = possibleArray.getClass();
/* 321 */     if (!type.isArray() || Array.getLength(possibleArray) == 0 || 
/* 322 */       !ClassUtils.isAssignableValue(type.getComponentType(), value)) {
/* 323 */       return false;
/*     */     }
/* 325 */     Object arrayValue = Array.get(possibleArray, 0);
/* 326 */     return type.getComponentType().isPrimitive() ? arrayValue.equals(value) : ((arrayValue == value));
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
/*     */   
/*     */   public static Object[] setupArgumentsForVarargsInvocation(Class<?>[] requiredParameterTypes, Object... args) {
/* 340 */     int parameterCount = requiredParameterTypes.length;
/* 341 */     int argumentCount = args.length;
/*     */ 
/*     */     
/* 344 */     if (parameterCount != args.length || requiredParameterTypes[parameterCount - 1] != ((args[argumentCount - 1] != null) ? args[argumentCount - 1]
/*     */       
/* 346 */       .getClass() : null)) {
/*     */       
/* 348 */       int arraySize = 0;
/* 349 */       if (argumentCount >= parameterCount) {
/* 350 */         arraySize = argumentCount - parameterCount - 1;
/*     */       }
/*     */ 
/*     */       
/* 354 */       Object[] newArgs = new Object[parameterCount];
/* 355 */       System.arraycopy(args, 0, newArgs, 0, newArgs.length - 1);
/*     */ 
/*     */ 
/*     */       
/* 359 */       Class<?> componentType = requiredParameterTypes[parameterCount - 1].getComponentType();
/* 360 */       Object repackagedArgs = Array.newInstance(componentType, arraySize);
/* 361 */       for (int i = 0; i < arraySize; i++) {
/* 362 */         Array.set(repackagedArgs, i, args[parameterCount - 1 + i]);
/*     */       }
/* 364 */       newArgs[newArgs.length - 1] = repackagedArgs;
/* 365 */       return newArgs;
/*     */     } 
/* 367 */     return args;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   enum ArgumentsMatchKind
/*     */   {
/* 374 */     EXACT,
/*     */ 
/*     */     
/* 377 */     CLOSE,
/*     */ 
/*     */     
/* 380 */     REQUIRES_CONVERSION;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class ArgumentsMatchInfo
/*     */   {
/*     */     private final ReflectionHelper.ArgumentsMatchKind kind;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     ArgumentsMatchInfo(ReflectionHelper.ArgumentsMatchKind kind) {
/* 396 */       this.kind = kind;
/*     */     }
/*     */     
/*     */     public boolean isExactMatch() {
/* 400 */       return (this.kind == ReflectionHelper.ArgumentsMatchKind.EXACT);
/*     */     }
/*     */     
/*     */     public boolean isCloseMatch() {
/* 404 */       return (this.kind == ReflectionHelper.ArgumentsMatchKind.CLOSE);
/*     */     }
/*     */     
/*     */     public boolean isMatchRequiringConversion() {
/* 408 */       return (this.kind == ReflectionHelper.ArgumentsMatchKind.REQUIRES_CONVERSION);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 413 */       return "ArgumentMatchInfo: " + this.kind;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\expression\spel\support\ReflectionHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */