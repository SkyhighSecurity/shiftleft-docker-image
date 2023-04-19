/*     */ package org.springframework.expression.spel.ast;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import org.springframework.asm.Label;
/*     */ import org.springframework.asm.MethodVisitor;
/*     */ import org.springframework.core.convert.TypeDescriptor;
/*     */ import org.springframework.expression.AccessException;
/*     */ import org.springframework.expression.EvaluationContext;
/*     */ import org.springframework.expression.EvaluationException;
/*     */ import org.springframework.expression.ExpressionInvocationTargetException;
/*     */ import org.springframework.expression.MethodExecutor;
/*     */ import org.springframework.expression.MethodResolver;
/*     */ import org.springframework.expression.TypedValue;
/*     */ import org.springframework.expression.spel.CodeFlow;
/*     */ import org.springframework.expression.spel.ExpressionState;
/*     */ import org.springframework.expression.spel.SpelEvaluationException;
/*     */ import org.springframework.expression.spel.SpelMessage;
/*     */ import org.springframework.expression.spel.support.ReflectiveMethodExecutor;
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
/*     */ public class MethodReference
/*     */   extends SpelNodeImpl
/*     */ {
/*     */   private final String name;
/*     */   private final boolean nullSafe;
/*     */   private String originalPrimitiveExitTypeDescriptor;
/*     */   private volatile CachedMethodExecutor cachedExecutor;
/*     */   
/*     */   public MethodReference(boolean nullSafe, String methodName, int pos, SpelNodeImpl... arguments) {
/*  64 */     super(pos, arguments);
/*  65 */     this.name = methodName;
/*  66 */     this.nullSafe = nullSafe;
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getName() {
/*  71 */     return this.name;
/*     */   }
/*     */ 
/*     */   
/*     */   protected ValueRef getValueRef(ExpressionState state) throws EvaluationException {
/*  76 */     Object[] arguments = getArguments(state);
/*  77 */     if (state.getActiveContextObject().getValue() == null) {
/*  78 */       throwIfNotNullSafe(getArgumentTypes(arguments));
/*  79 */       return ValueRef.NullValueRef.INSTANCE;
/*     */     } 
/*  81 */     return new MethodValueRef(state, arguments);
/*     */   }
/*     */ 
/*     */   
/*     */   public TypedValue getValueInternal(ExpressionState state) throws EvaluationException {
/*  86 */     EvaluationContext evaluationContext = state.getEvaluationContext();
/*  87 */     Object value = state.getActiveContextObject().getValue();
/*  88 */     TypeDescriptor targetType = state.getActiveContextObject().getTypeDescriptor();
/*  89 */     Object[] arguments = getArguments(state);
/*  90 */     TypedValue result = getValueInternal(evaluationContext, value, targetType, arguments);
/*  91 */     updateExitTypeDescriptor();
/*  92 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private TypedValue getValueInternal(EvaluationContext evaluationContext, Object value, TypeDescriptor targetType, Object[] arguments) {
/*  98 */     List<TypeDescriptor> argumentTypes = getArgumentTypes(arguments);
/*  99 */     if (value == null) {
/* 100 */       throwIfNotNullSafe(argumentTypes);
/* 101 */       return TypedValue.NULL;
/*     */     } 
/*     */     
/* 104 */     MethodExecutor executorToUse = getCachedExecutor(evaluationContext, value, targetType, argumentTypes);
/* 105 */     if (executorToUse != null) {
/*     */       try {
/* 107 */         return executorToUse.execute(evaluationContext, value, arguments);
/*     */       }
/* 109 */       catch (AccessException ex) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 121 */         throwSimpleExceptionIfPossible(value, ex);
/*     */ 
/*     */ 
/*     */         
/* 125 */         this.cachedExecutor = null;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 130 */     executorToUse = findAccessorForMethod(argumentTypes, value, evaluationContext);
/* 131 */     this.cachedExecutor = new CachedMethodExecutor(executorToUse, (value instanceof Class) ? (Class)value : null, targetType, argumentTypes);
/*     */     
/*     */     try {
/* 134 */       return executorToUse.execute(evaluationContext, value, arguments);
/*     */     }
/* 136 */     catch (AccessException ex) {
/*     */       
/* 138 */       throwSimpleExceptionIfPossible(value, ex);
/* 139 */       throw new SpelEvaluationException(getStartPosition(), ex, SpelMessage.EXCEPTION_DURING_METHOD_INVOCATION, new Object[] { this.name, value
/*     */             
/* 141 */             .getClass().getName(), ex.getMessage() });
/*     */     } 
/*     */   }
/*     */   
/*     */   private void throwIfNotNullSafe(List<TypeDescriptor> argumentTypes) {
/* 146 */     if (!this.nullSafe)
/* 147 */       throw new SpelEvaluationException(getStartPosition(), SpelMessage.METHOD_CALL_ON_NULL_OBJECT_NOT_ALLOWED, new Object[] {
/*     */             
/* 149 */             FormatHelper.formatMethodForMessage(this.name, argumentTypes)
/*     */           }); 
/*     */   }
/*     */   
/*     */   private Object[] getArguments(ExpressionState state) {
/* 154 */     Object[] arguments = new Object[getChildCount()];
/* 155 */     for (int i = 0; i < arguments.length; i++) {
/*     */ 
/*     */       
/* 158 */       try { state.pushActiveContextObject(state.getScopeRootContextObject());
/* 159 */         arguments[i] = this.children[i].getValueInternal(state).getValue();
/*     */ 
/*     */         
/* 162 */         state.popActiveContextObject(); } finally { state.popActiveContextObject(); }
/*     */     
/*     */     } 
/* 165 */     return arguments;
/*     */   }
/*     */   
/*     */   private List<TypeDescriptor> getArgumentTypes(Object... arguments) {
/* 169 */     List<TypeDescriptor> descriptors = new ArrayList<TypeDescriptor>(arguments.length);
/* 170 */     for (Object argument : arguments) {
/* 171 */       descriptors.add(TypeDescriptor.forObject(argument));
/*     */     }
/* 173 */     return Collections.unmodifiableList(descriptors);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private MethodExecutor getCachedExecutor(EvaluationContext evaluationContext, Object value, TypeDescriptor target, List<TypeDescriptor> argumentTypes) {
/* 179 */     List<MethodResolver> methodResolvers = evaluationContext.getMethodResolvers();
/* 180 */     if (methodResolvers == null || methodResolvers.size() != 1 || 
/* 181 */       !(methodResolvers.get(0) instanceof org.springframework.expression.spel.support.ReflectiveMethodResolver))
/*     */     {
/* 183 */       return null;
/*     */     }
/*     */     
/* 186 */     CachedMethodExecutor executorToCheck = this.cachedExecutor;
/* 187 */     if (executorToCheck != null && executorToCheck.isSuitable(value, target, argumentTypes)) {
/* 188 */       return executorToCheck.get();
/*     */     }
/* 190 */     this.cachedExecutor = null;
/* 191 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private MethodExecutor findAccessorForMethod(List<TypeDescriptor> argumentTypes, Object targetObject, EvaluationContext evaluationContext) throws SpelEvaluationException {
/* 197 */     AccessException accessException = null;
/* 198 */     List<MethodResolver> methodResolvers = evaluationContext.getMethodResolvers();
/* 199 */     for (MethodResolver methodResolver : methodResolvers) {
/*     */       try {
/* 201 */         MethodExecutor methodExecutor = methodResolver.resolve(evaluationContext, targetObject, this.name, argumentTypes);
/*     */         
/* 203 */         if (methodExecutor != null) {
/* 204 */           return methodExecutor;
/*     */         }
/*     */       }
/* 207 */       catch (AccessException ex) {
/* 208 */         accessException = ex;
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/* 213 */     String method = FormatHelper.formatMethodForMessage(this.name, argumentTypes);
/* 214 */     String className = FormatHelper.formatClassNameForMessage((targetObject instanceof Class) ? (Class)targetObject : targetObject
/* 215 */         .getClass());
/* 216 */     if (accessException != null) {
/* 217 */       throw new SpelEvaluationException(
/* 218 */           getStartPosition(), accessException, SpelMessage.PROBLEM_LOCATING_METHOD, new Object[] { method, className });
/*     */     }
/*     */     
/* 221 */     throw new SpelEvaluationException(getStartPosition(), SpelMessage.METHOD_NOT_FOUND, new Object[] { method, className });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void throwSimpleExceptionIfPossible(Object value, AccessException ex) {
/* 230 */     if (ex.getCause() instanceof java.lang.reflect.InvocationTargetException) {
/* 231 */       Throwable rootCause = ex.getCause().getCause();
/* 232 */       if (rootCause instanceof RuntimeException) {
/* 233 */         throw (RuntimeException)rootCause;
/*     */       }
/* 235 */       throw new ExpressionInvocationTargetException(getStartPosition(), "A problem occurred when trying to execute method '" + this.name + "' on object of type [" + value
/*     */           
/* 237 */           .getClass().getName() + "]", rootCause);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void updateExitTypeDescriptor() {
/* 242 */     CachedMethodExecutor executorToCheck = this.cachedExecutor;
/* 243 */     if (executorToCheck != null && executorToCheck.get() instanceof ReflectiveMethodExecutor) {
/* 244 */       Method method = ((ReflectiveMethodExecutor)executorToCheck.get()).getMethod();
/* 245 */       String descriptor = CodeFlow.toDescriptor(method.getReturnType());
/* 246 */       if (this.nullSafe && CodeFlow.isPrimitive(descriptor)) {
/* 247 */         this.originalPrimitiveExitTypeDescriptor = descriptor;
/* 248 */         this.exitTypeDescriptor = CodeFlow.toBoxedDescriptor(descriptor);
/*     */       } else {
/*     */         
/* 251 */         this.exitTypeDescriptor = descriptor;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String toStringAST() {
/* 258 */     StringBuilder sb = new StringBuilder(this.name);
/* 259 */     sb.append("(");
/* 260 */     for (int i = 0; i < getChildCount(); i++) {
/* 261 */       if (i > 0) {
/* 262 */         sb.append(",");
/*     */       }
/* 264 */       sb.append(getChild(i).toStringAST());
/*     */     } 
/* 266 */     sb.append(")");
/* 267 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isCompilable() {
/* 276 */     CachedMethodExecutor executorToCheck = this.cachedExecutor;
/* 277 */     if (executorToCheck == null || executorToCheck.hasProxyTarget() || 
/* 278 */       !(executorToCheck.get() instanceof ReflectiveMethodExecutor)) {
/* 279 */       return false;
/*     */     }
/*     */     
/* 282 */     for (SpelNodeImpl child : this.children) {
/* 283 */       if (!child.isCompilable()) {
/* 284 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 288 */     ReflectiveMethodExecutor executor = (ReflectiveMethodExecutor)executorToCheck.get();
/* 289 */     if (executor.didArgumentConversionOccur()) {
/* 290 */       return false;
/*     */     }
/* 292 */     Class<?> clazz = executor.getMethod().getDeclaringClass();
/* 293 */     if (!Modifier.isPublic(clazz.getModifiers()) && executor.getPublicDeclaringClass() == null) {
/* 294 */       return false;
/*     */     }
/*     */     
/* 297 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void generateCode(MethodVisitor mv, CodeFlow cf) {
/* 302 */     CachedMethodExecutor executorToCheck = this.cachedExecutor;
/* 303 */     if (executorToCheck == null || !(executorToCheck.get() instanceof ReflectiveMethodExecutor)) {
/* 304 */       throw new IllegalStateException("No applicable cached executor found: " + executorToCheck);
/*     */     }
/*     */     
/* 307 */     ReflectiveMethodExecutor methodExecutor = (ReflectiveMethodExecutor)executorToCheck.get();
/* 308 */     Method method = methodExecutor.getMethod();
/* 309 */     boolean isStaticMethod = Modifier.isStatic(method.getModifiers());
/* 310 */     String descriptor = cf.lastDescriptor();
/*     */     
/* 312 */     Label skipIfNull = null;
/* 313 */     if (descriptor == null && !isStaticMethod)
/*     */     {
/* 315 */       cf.loadTarget(mv);
/*     */     }
/* 317 */     if ((descriptor != null || !isStaticMethod) && this.nullSafe) {
/* 318 */       mv.visitInsn(89);
/* 319 */       skipIfNull = new Label();
/* 320 */       Label continueLabel = new Label();
/* 321 */       mv.visitJumpInsn(199, continueLabel);
/* 322 */       CodeFlow.insertCheckCast(mv, this.exitTypeDescriptor);
/* 323 */       mv.visitJumpInsn(167, skipIfNull);
/* 324 */       mv.visitLabel(continueLabel);
/*     */     } 
/* 326 */     if (descriptor != null && isStaticMethod)
/*     */     {
/* 328 */       mv.visitInsn(87);
/*     */     }
/*     */     
/* 331 */     if (CodeFlow.isPrimitive(descriptor)) {
/* 332 */       CodeFlow.insertBoxIfNecessary(mv, descriptor.charAt(0));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 337 */     String classDesc = Modifier.isPublic(method.getDeclaringClass().getModifiers()) ? method.getDeclaringClass().getName().replace('.', '/') : methodExecutor.getPublicDeclaringClass().getName().replace('.', '/');
/* 338 */     if (!isStaticMethod && (descriptor == null || !descriptor.substring(1).equals(classDesc))) {
/* 339 */       CodeFlow.insertCheckCast(mv, "L" + classDesc);
/*     */     }
/*     */     
/* 342 */     generateCodeForArguments(mv, cf, method, this.children);
/* 343 */     mv.visitMethodInsn(isStaticMethod ? 184 : 182, classDesc, method.getName(), 
/* 344 */         CodeFlow.createSignatureDescriptor(method), method.getDeclaringClass().isInterface());
/* 345 */     cf.pushDescriptor(this.exitTypeDescriptor);
/*     */     
/* 347 */     if (this.originalPrimitiveExitTypeDescriptor != null)
/*     */     {
/*     */       
/* 350 */       CodeFlow.insertBoxIfNecessary(mv, this.originalPrimitiveExitTypeDescriptor);
/*     */     }
/* 352 */     if (skipIfNull != null) {
/* 353 */       mv.visitLabel(skipIfNull);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private class MethodValueRef
/*     */     implements ValueRef
/*     */   {
/*     */     private final EvaluationContext evaluationContext;
/*     */     
/*     */     private final Object value;
/*     */     
/*     */     private final TypeDescriptor targetType;
/*     */     private final Object[] arguments;
/*     */     
/*     */     public MethodValueRef(ExpressionState state, Object[] arguments) {
/* 369 */       this.evaluationContext = state.getEvaluationContext();
/* 370 */       this.value = state.getActiveContextObject().getValue();
/* 371 */       this.targetType = state.getActiveContextObject().getTypeDescriptor();
/* 372 */       this.arguments = arguments;
/*     */     }
/*     */ 
/*     */     
/*     */     public TypedValue getValue() {
/* 377 */       TypedValue result = MethodReference.this.getValueInternal(this.evaluationContext, this.value, this.targetType, this.arguments);
/*     */       
/* 379 */       MethodReference.this.updateExitTypeDescriptor();
/* 380 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setValue(Object newValue) {
/* 385 */       throw new IllegalAccessError();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isWritable() {
/* 390 */       return false;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class CachedMethodExecutor
/*     */   {
/*     */     private final MethodExecutor methodExecutor;
/*     */     
/*     */     private final Class<?> staticClass;
/*     */     
/*     */     private final TypeDescriptor target;
/*     */     
/*     */     private final List<TypeDescriptor> argumentTypes;
/*     */ 
/*     */     
/*     */     public CachedMethodExecutor(MethodExecutor methodExecutor, Class<?> staticClass, TypeDescriptor target, List<TypeDescriptor> argumentTypes) {
/* 408 */       this.methodExecutor = methodExecutor;
/* 409 */       this.staticClass = staticClass;
/* 410 */       this.target = target;
/* 411 */       this.argumentTypes = argumentTypes;
/*     */     }
/*     */     
/*     */     public boolean isSuitable(Object value, TypeDescriptor target, List<TypeDescriptor> argumentTypes) {
/* 415 */       return ((this.staticClass == null || this.staticClass == value) && 
/* 416 */         ObjectUtils.nullSafeEquals(this.target, target) && this.argumentTypes.equals(argumentTypes));
/*     */     }
/*     */     
/*     */     public boolean hasProxyTarget() {
/* 420 */       return (this.target != null && Proxy.isProxyClass(this.target.getType()));
/*     */     }
/*     */     
/*     */     public MethodExecutor get() {
/* 424 */       return this.methodExecutor;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\expression\spel\ast\MethodReference.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */