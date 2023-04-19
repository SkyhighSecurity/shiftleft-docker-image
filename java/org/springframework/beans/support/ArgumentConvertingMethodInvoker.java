/*     */ package org.springframework.beans.support;
/*     */ 
/*     */ import java.beans.PropertyEditor;
/*     */ import java.lang.reflect.Method;
/*     */ import org.springframework.beans.PropertyEditorRegistry;
/*     */ import org.springframework.beans.SimpleTypeConverter;
/*     */ import org.springframework.beans.TypeConverter;
/*     */ import org.springframework.beans.TypeMismatchException;
/*     */ import org.springframework.util.MethodInvoker;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ArgumentConvertingMethodInvoker
/*     */   extends MethodInvoker
/*     */ {
/*     */   private TypeConverter typeConverter;
/*     */   private boolean useDefaultConverter = true;
/*     */   
/*     */   public void setTypeConverter(TypeConverter typeConverter) {
/*  56 */     this.typeConverter = typeConverter;
/*  57 */     this.useDefaultConverter = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypeConverter getTypeConverter() {
/*  68 */     if (this.typeConverter == null && this.useDefaultConverter) {
/*  69 */       this.typeConverter = getDefaultTypeConverter();
/*     */     }
/*  71 */     return this.typeConverter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected TypeConverter getDefaultTypeConverter() {
/*  82 */     return (TypeConverter)new SimpleTypeConverter();
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
/*     */   public void registerCustomEditor(Class<?> requiredType, PropertyEditor propertyEditor) {
/*  96 */     TypeConverter converter = getTypeConverter();
/*  97 */     if (!(converter instanceof PropertyEditorRegistry)) {
/*  98 */       throw new IllegalStateException("TypeConverter does not implement PropertyEditorRegistry interface: " + converter);
/*     */     }
/*     */     
/* 101 */     ((PropertyEditorRegistry)converter).registerCustomEditor(requiredType, propertyEditor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Method findMatchingMethod() {
/* 111 */     Method matchingMethod = super.findMatchingMethod();
/*     */     
/* 113 */     if (matchingMethod == null)
/*     */     {
/* 115 */       matchingMethod = doFindMatchingMethod(getArguments());
/*     */     }
/* 117 */     if (matchingMethod == null)
/*     */     {
/* 119 */       matchingMethod = doFindMatchingMethod(new Object[] { getArguments() });
/*     */     }
/* 121 */     return matchingMethod;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Method doFindMatchingMethod(Object[] arguments) {
/* 131 */     TypeConverter converter = getTypeConverter();
/* 132 */     if (converter != null) {
/* 133 */       String targetMethod = getTargetMethod();
/* 134 */       Method matchingMethod = null;
/* 135 */       int argCount = arguments.length;
/* 136 */       Method[] candidates = ReflectionUtils.getAllDeclaredMethods(getTargetClass());
/* 137 */       int minTypeDiffWeight = Integer.MAX_VALUE;
/* 138 */       Object[] argumentsToUse = null;
/* 139 */       for (Method candidate : candidates) {
/* 140 */         if (candidate.getName().equals(targetMethod)) {
/*     */           
/* 142 */           Class<?>[] paramTypes = candidate.getParameterTypes();
/* 143 */           if (paramTypes.length == argCount) {
/* 144 */             Object[] convertedArguments = new Object[argCount];
/* 145 */             boolean match = true;
/* 146 */             for (int j = 0; j < argCount && match; j++) {
/*     */               
/*     */               try {
/* 149 */                 convertedArguments[j] = converter.convertIfNecessary(arguments[j], paramTypes[j]);
/*     */               }
/* 151 */               catch (TypeMismatchException ex) {
/*     */                 
/* 153 */                 match = false;
/*     */               } 
/*     */             } 
/* 156 */             if (match) {
/* 157 */               int typeDiffWeight = getTypeDifferenceWeight(paramTypes, convertedArguments);
/* 158 */               if (typeDiffWeight < minTypeDiffWeight) {
/* 159 */                 minTypeDiffWeight = typeDiffWeight;
/* 160 */                 matchingMethod = candidate;
/* 161 */                 argumentsToUse = convertedArguments;
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/* 167 */       if (matchingMethod != null) {
/* 168 */         setArguments(argumentsToUse);
/* 169 */         return matchingMethod;
/*     */       } 
/*     */     } 
/* 172 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\support\ArgumentConvertingMethodInvoker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */