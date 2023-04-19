/*     */ package org.springframework.expression.spel.support;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.springframework.core.BridgeMethodResolver;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.core.convert.TypeDescriptor;
/*     */ import org.springframework.expression.AccessException;
/*     */ import org.springframework.expression.EvaluationContext;
/*     */ import org.springframework.expression.EvaluationException;
/*     */ import org.springframework.expression.MethodExecutor;
/*     */ import org.springframework.expression.MethodFilter;
/*     */ import org.springframework.expression.MethodResolver;
/*     */ import org.springframework.expression.TypeConverter;
/*     */ import org.springframework.expression.spel.SpelEvaluationException;
/*     */ import org.springframework.expression.spel.SpelMessage;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ReflectiveMethodResolver
/*     */   implements MethodResolver
/*     */ {
/*     */   private final boolean useDistance;
/*     */   private Map<Class<?>, MethodFilter> filters;
/*     */   
/*     */   public ReflectiveMethodResolver() {
/*  65 */     this.useDistance = true;
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
/*     */   public ReflectiveMethodResolver(boolean useDistance) {
/*  79 */     this.useDistance = useDistance;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerMethodFilter(Class<?> type, MethodFilter filter) {
/*  90 */     if (this.filters == null) {
/*  91 */       this.filters = new HashMap<Class<?>, MethodFilter>();
/*     */     }
/*  93 */     if (filter != null) {
/*  94 */       this.filters.put(type, filter);
/*     */     } else {
/*     */       
/*  97 */       this.filters.remove(type);
/*     */     } 
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
/*     */   public MethodExecutor resolve(EvaluationContext context, Object targetObject, String name, List<TypeDescriptor> argumentTypes) throws AccessException {
/*     */     try {
/* 115 */       TypeConverter typeConverter = context.getTypeConverter();
/* 116 */       Class<?> type = (targetObject instanceof Class) ? (Class)targetObject : targetObject.getClass();
/* 117 */       List<Method> methods = new ArrayList<Method>(getMethods(type, targetObject));
/*     */ 
/*     */       
/* 120 */       MethodFilter filter = (this.filters != null) ? this.filters.get(type) : null;
/* 121 */       if (filter != null) {
/* 122 */         List<Method> filtered = filter.filter(methods);
/* 123 */         methods = (filtered instanceof ArrayList) ? filtered : new ArrayList<Method>(filtered);
/*     */       } 
/*     */ 
/*     */       
/* 127 */       if (methods.size() > 1) {
/* 128 */         Collections.sort(methods, new Comparator<Method>()
/*     */             {
/*     */               public int compare(Method m1, Method m2) {
/* 131 */                 int m1pl = (m1.getParameterTypes()).length;
/* 132 */                 int m2pl = (m2.getParameterTypes()).length;
/*     */                 
/* 134 */                 if (m1pl == m2pl) {
/* 135 */                   if (!m1.isVarArgs() && m2.isVarArgs()) {
/* 136 */                     return -1;
/*     */                   }
/* 138 */                   if (m1.isVarArgs() && !m2.isVarArgs()) {
/* 139 */                     return 1;
/*     */                   }
/*     */                   
/* 142 */                   return 0;
/*     */                 } 
/*     */                 
/* 145 */                 return (m1pl < m2pl) ? -1 : ((m1pl > m2pl) ? 1 : 0);
/*     */               }
/*     */             });
/*     */       }
/*     */ 
/*     */       
/* 151 */       for (int i = 0; i < methods.size(); i++) {
/* 152 */         methods.set(i, BridgeMethodResolver.findBridgedMethod(methods.get(i)));
/*     */       }
/*     */ 
/*     */       
/* 156 */       Set<Method> methodsToIterate = new LinkedHashSet<Method>(methods);
/*     */       
/* 158 */       Method closeMatch = null;
/* 159 */       int closeMatchDistance = Integer.MAX_VALUE;
/* 160 */       Method matchRequiringConversion = null;
/* 161 */       boolean multipleOptions = false;
/*     */       
/* 163 */       for (Method method : methodsToIterate) {
/* 164 */         if (method.getName().equals(name)) {
/* 165 */           Class<?>[] paramTypes = method.getParameterTypes();
/* 166 */           List<TypeDescriptor> paramDescriptors = new ArrayList<TypeDescriptor>(paramTypes.length);
/* 167 */           for (int j = 0; j < paramTypes.length; j++) {
/* 168 */             paramDescriptors.add(new TypeDescriptor(new MethodParameter(method, j)));
/*     */           }
/* 170 */           ReflectionHelper.ArgumentsMatchInfo matchInfo = null;
/* 171 */           if (method.isVarArgs() && argumentTypes.size() >= paramTypes.length - 1) {
/*     */             
/* 173 */             matchInfo = ReflectionHelper.compareArgumentsVarargs(paramDescriptors, argumentTypes, typeConverter);
/*     */           }
/* 175 */           else if (paramTypes.length == argumentTypes.size()) {
/*     */             
/* 177 */             matchInfo = ReflectionHelper.compareArguments(paramDescriptors, argumentTypes, typeConverter);
/*     */           } 
/* 179 */           if (matchInfo != null) {
/* 180 */             if (matchInfo.isExactMatch()) {
/* 181 */               return new ReflectiveMethodExecutor(method);
/*     */             }
/* 183 */             if (matchInfo.isCloseMatch()) {
/* 184 */               if (this.useDistance) {
/* 185 */                 int matchDistance = ReflectionHelper.getTypeDifferenceWeight(paramDescriptors, argumentTypes);
/* 186 */                 if (closeMatch == null || matchDistance < closeMatchDistance) {
/*     */                   
/* 188 */                   closeMatch = method;
/* 189 */                   closeMatchDistance = matchDistance;
/*     */                 } 
/*     */                 
/*     */                 continue;
/*     */               } 
/* 194 */               if (closeMatch == null) {
/* 195 */                 closeMatch = method;
/*     */               }
/*     */               continue;
/*     */             } 
/* 199 */             if (matchInfo.isMatchRequiringConversion()) {
/* 200 */               if (matchRequiringConversion != null) {
/* 201 */                 multipleOptions = true;
/*     */               }
/* 203 */               matchRequiringConversion = method;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/* 208 */       if (closeMatch != null) {
/* 209 */         return new ReflectiveMethodExecutor(closeMatch);
/*     */       }
/* 211 */       if (matchRequiringConversion != null) {
/* 212 */         if (multipleOptions) {
/* 213 */           throw new SpelEvaluationException(SpelMessage.MULTIPLE_POSSIBLE_METHODS, new Object[] { name });
/*     */         }
/* 215 */         return new ReflectiveMethodExecutor(matchRequiringConversion);
/*     */       } 
/*     */       
/* 218 */       return null;
/*     */     
/*     */     }
/* 221 */     catch (EvaluationException ex) {
/* 222 */       throw new AccessException("Failed to resolve method", ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   private Set<Method> getMethods(Class<?> type, Object targetObject) {
/* 227 */     if (targetObject instanceof Class) {
/* 228 */       Set<Method> set = new LinkedHashSet<Method>();
/*     */       
/* 230 */       Method[] arrayOfMethod = getMethods(type);
/* 231 */       for (Method method : arrayOfMethod) {
/* 232 */         if (Modifier.isStatic(method.getModifiers())) {
/* 233 */           set.add(method);
/*     */         }
/*     */       } 
/*     */       
/* 237 */       set.addAll(Arrays.asList(getMethods(Class.class)));
/* 238 */       return set;
/*     */     } 
/* 240 */     if (Proxy.isProxyClass(type)) {
/* 241 */       Set<Method> set = new LinkedHashSet<Method>();
/*     */       
/* 243 */       for (Class<?> ifc : type.getInterfaces()) {
/* 244 */         Method[] arrayOfMethod = getMethods(ifc);
/* 245 */         for (Method method : arrayOfMethod) {
/* 246 */           if (isCandidateForInvocation(method, type)) {
/* 247 */             set.add(method);
/*     */           }
/*     */         } 
/*     */       } 
/* 251 */       return set;
/*     */     } 
/*     */     
/* 254 */     Set<Method> result = new LinkedHashSet<Method>();
/* 255 */     Method[] methods = getMethods(type);
/* 256 */     for (Method method : methods) {
/* 257 */       if (isCandidateForInvocation(method, type)) {
/* 258 */         result.add(method);
/*     */       }
/*     */     } 
/* 261 */     return result;
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
/*     */   protected Method[] getMethods(Class<?> type) {
/* 274 */     return type.getMethods();
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
/*     */   protected boolean isCandidateForInvocation(Method method, Class<?> targetClass) {
/* 287 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\expression\spel\support\ReflectiveMethodResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */