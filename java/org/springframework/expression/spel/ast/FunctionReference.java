/*     */ package org.springframework.expression.spel.ast;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import org.springframework.asm.MethodVisitor;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.core.convert.TypeDescriptor;
/*     */ import org.springframework.expression.EvaluationException;
/*     */ import org.springframework.expression.TypeConverter;
/*     */ import org.springframework.expression.TypedValue;
/*     */ import org.springframework.expression.spel.CodeFlow;
/*     */ import org.springframework.expression.spel.ExpressionState;
/*     */ import org.springframework.expression.spel.SpelEvaluationException;
/*     */ import org.springframework.expression.spel.SpelMessage;
/*     */ import org.springframework.expression.spel.support.ReflectionHelper;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
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
/*     */ public class FunctionReference
/*     */   extends SpelNodeImpl
/*     */ {
/*     */   private final String name;
/*     */   private volatile Method method;
/*     */   
/*     */   public FunctionReference(String functionName, int pos, SpelNodeImpl... arguments) {
/*  59 */     super(pos, arguments);
/*  60 */     this.name = functionName;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public TypedValue getValueInternal(ExpressionState state) throws EvaluationException {
/*  66 */     TypedValue value = state.lookupVariable(this.name);
/*  67 */     if (value == TypedValue.NULL) {
/*  68 */       throw new SpelEvaluationException(getStartPosition(), SpelMessage.FUNCTION_NOT_DEFINED, new Object[] { this.name });
/*     */     }
/*  70 */     if (!(value.getValue() instanceof Method))
/*     */     {
/*  72 */       throw new SpelEvaluationException(SpelMessage.FUNCTION_REFERENCE_CANNOT_BE_INVOKED, new Object[] { this.name, value
/*  73 */             .getClass() });
/*     */     }
/*     */     
/*     */     try {
/*  77 */       return executeFunctionJLRMethod(state, (Method)value.getValue());
/*     */     }
/*  79 */     catch (SpelEvaluationException ex) {
/*  80 */       ex.setPosition(getStartPosition());
/*  81 */       throw ex;
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
/*     */   private TypedValue executeFunctionJLRMethod(ExpressionState state, Method method) throws EvaluationException {
/*  93 */     Object[] functionArgs = getArguments(state);
/*     */     
/*  95 */     if (!method.isVarArgs()) {
/*  96 */       int declaredParamCount = (method.getParameterTypes()).length;
/*  97 */       if (declaredParamCount != functionArgs.length)
/*  98 */         throw new SpelEvaluationException(SpelMessage.INCORRECT_NUMBER_OF_ARGUMENTS_TO_FUNCTION, new Object[] {
/*  99 */               Integer.valueOf(functionArgs.length), Integer.valueOf(declaredParamCount)
/*     */             }); 
/*     */     } 
/* 102 */     if (!Modifier.isStatic(method.getModifiers())) {
/* 103 */       throw new SpelEvaluationException(getStartPosition(), SpelMessage.FUNCTION_MUST_BE_STATIC, new Object[] {
/* 104 */             ClassUtils.getQualifiedMethodName(method), this.name
/*     */           });
/*     */     }
/*     */     
/* 108 */     TypeConverter converter = state.getEvaluationContext().getTypeConverter();
/* 109 */     boolean argumentConversionOccurred = ReflectionHelper.convertAllArguments(converter, functionArgs, method);
/* 110 */     if (method.isVarArgs()) {
/* 111 */       functionArgs = ReflectionHelper.setupArgumentsForVarargsInvocation(method
/* 112 */           .getParameterTypes(), functionArgs);
/*     */     }
/* 114 */     boolean compilable = false;
/*     */     
/*     */     try {
/* 117 */       ReflectionUtils.makeAccessible(method);
/* 118 */       Object result = method.invoke(method.getClass(), functionArgs);
/* 119 */       compilable = !argumentConversionOccurred;
/* 120 */       return new TypedValue(result, (new TypeDescriptor(new MethodParameter(method, -1))).narrow(result));
/*     */     }
/* 122 */     catch (Exception ex) {
/* 123 */       throw new SpelEvaluationException(getStartPosition(), ex, SpelMessage.EXCEPTION_DURING_FUNCTION_CALL, new Object[] { this.name, ex
/* 124 */             .getMessage() });
/*     */     } finally {
/*     */       
/* 127 */       if (compilable) {
/* 128 */         this.exitTypeDescriptor = CodeFlow.toDescriptor(method.getReturnType());
/* 129 */         this.method = method;
/*     */       } else {
/*     */         
/* 132 */         this.exitTypeDescriptor = null;
/* 133 */         this.method = null;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String toStringAST() {
/* 140 */     StringBuilder sb = (new StringBuilder("#")).append(this.name);
/* 141 */     sb.append("(");
/* 142 */     for (int i = 0; i < getChildCount(); i++) {
/* 143 */       if (i > 0) {
/* 144 */         sb.append(",");
/*     */       }
/* 146 */       sb.append(getChild(i).toStringAST());
/*     */     } 
/* 148 */     sb.append(")");
/* 149 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Object[] getArguments(ExpressionState state) throws EvaluationException {
/* 158 */     Object[] arguments = new Object[getChildCount()];
/* 159 */     for (int i = 0; i < arguments.length; i++) {
/* 160 */       arguments[i] = this.children[i].getValueInternal(state).getValue();
/*     */     }
/* 162 */     return arguments;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCompilable() {
/* 167 */     Method method = this.method;
/* 168 */     if (method == null) {
/* 169 */       return false;
/*     */     }
/* 171 */     int methodModifiers = method.getModifiers();
/* 172 */     if (!Modifier.isStatic(methodModifiers) || !Modifier.isPublic(methodModifiers) || 
/* 173 */       !Modifier.isPublic(method.getDeclaringClass().getModifiers())) {
/* 174 */       return false;
/*     */     }
/* 176 */     for (SpelNodeImpl child : this.children) {
/* 177 */       if (!child.isCompilable()) {
/* 178 */         return false;
/*     */       }
/*     */     } 
/* 181 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void generateCode(MethodVisitor mv, CodeFlow cf) {
/* 186 */     Method method = this.method;
/* 187 */     Assert.state((method != null), "No method handle");
/* 188 */     String classDesc = method.getDeclaringClass().getName().replace('.', '/');
/* 189 */     generateCodeForArguments(mv, cf, method, this.children);
/* 190 */     mv.visitMethodInsn(184, classDesc, method.getName(), 
/* 191 */         CodeFlow.createSignatureDescriptor(method), false);
/* 192 */     cf.pushDescriptor(this.exitTypeDescriptor);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\expression\spel\ast\FunctionReference.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */