/*     */ package org.springframework.expression.spel.ast;
/*     */ 
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.springframework.asm.MethodVisitor;
/*     */ import org.springframework.core.convert.TypeDescriptor;
/*     */ import org.springframework.expression.AccessException;
/*     */ import org.springframework.expression.ConstructorExecutor;
/*     */ import org.springframework.expression.ConstructorResolver;
/*     */ import org.springframework.expression.EvaluationContext;
/*     */ import org.springframework.expression.EvaluationException;
/*     */ import org.springframework.expression.TypeConverter;
/*     */ import org.springframework.expression.TypedValue;
/*     */ import org.springframework.expression.common.ExpressionUtils;
/*     */ import org.springframework.expression.spel.CodeFlow;
/*     */ import org.springframework.expression.spel.ExpressionState;
/*     */ import org.springframework.expression.spel.SpelEvaluationException;
/*     */ import org.springframework.expression.spel.SpelMessage;
/*     */ import org.springframework.expression.spel.SpelNode;
/*     */ import org.springframework.expression.spel.support.ReflectiveConstructorExecutor;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ConstructorReference
/*     */   extends SpelNodeImpl
/*     */ {
/*     */   private boolean isArrayConstructor = false;
/*     */   private SpelNodeImpl[] dimensions;
/*     */   private volatile ConstructorExecutor cachedExecutor;
/*     */   
/*     */   public ConstructorReference(int pos, SpelNodeImpl... arguments) {
/*  74 */     super(pos, arguments);
/*  75 */     this.isArrayConstructor = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConstructorReference(int pos, SpelNodeImpl[] dimensions, SpelNodeImpl... arguments) {
/*  83 */     super(pos, arguments);
/*  84 */     this.isArrayConstructor = true;
/*  85 */     this.dimensions = dimensions;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypedValue getValueInternal(ExpressionState state) throws EvaluationException {
/*  94 */     if (this.isArrayConstructor) {
/*  95 */       return createArray(state);
/*     */     }
/*     */     
/*  98 */     return createNewInstance(state);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private TypedValue createNewInstance(ExpressionState state) throws EvaluationException {
/* 109 */     Object[] arguments = new Object[getChildCount() - 1];
/* 110 */     List<TypeDescriptor> argumentTypes = new ArrayList<TypeDescriptor>(getChildCount() - 1);
/* 111 */     for (int i = 0; i < arguments.length; i++) {
/* 112 */       TypedValue childValue = this.children[i + 1].getValueInternal(state);
/* 113 */       Object value = childValue.getValue();
/* 114 */       arguments[i] = value;
/* 115 */       argumentTypes.add(TypeDescriptor.forObject(value));
/*     */     } 
/*     */     
/* 118 */     ConstructorExecutor executorToUse = this.cachedExecutor;
/* 119 */     if (executorToUse != null) {
/*     */       try {
/* 121 */         return executorToUse.execute(state.getEvaluationContext(), arguments);
/*     */       }
/* 123 */       catch (AccessException ex) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 134 */         if (ex.getCause() instanceof java.lang.reflect.InvocationTargetException) {
/*     */           
/* 136 */           Throwable rootCause = ex.getCause().getCause();
/* 137 */           if (rootCause instanceof RuntimeException) {
/* 138 */             throw (RuntimeException)rootCause;
/*     */           }
/*     */           
/* 141 */           String str = (String)this.children[0].getValueInternal(state).getValue();
/* 142 */           throw new SpelEvaluationException(getStartPosition(), rootCause, SpelMessage.CONSTRUCTOR_INVOCATION_PROBLEM, new Object[] { str, 
/*     */                 
/* 144 */                 FormatHelper.formatMethodForMessage("", argumentTypes) });
/*     */         } 
/*     */ 
/*     */ 
/*     */         
/* 149 */         this.cachedExecutor = null;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 154 */     String typeName = (String)this.children[0].getValueInternal(state).getValue();
/* 155 */     executorToUse = findExecutorForConstructor(typeName, argumentTypes, state);
/*     */     try {
/* 157 */       this.cachedExecutor = executorToUse;
/* 158 */       if (this.cachedExecutor instanceof ReflectiveConstructorExecutor) {
/* 159 */         this.exitTypeDescriptor = CodeFlow.toDescriptor(((ReflectiveConstructorExecutor)this.cachedExecutor)
/* 160 */             .getConstructor().getDeclaringClass());
/*     */       }
/*     */       
/* 163 */       return executorToUse.execute(state.getEvaluationContext(), arguments);
/*     */     }
/* 165 */     catch (AccessException ex) {
/* 166 */       throw new SpelEvaluationException(getStartPosition(), ex, SpelMessage.CONSTRUCTOR_INVOCATION_PROBLEM, new Object[] { typeName, 
/*     */             
/* 168 */             FormatHelper.formatMethodForMessage("", argumentTypes) });
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
/*     */   private ConstructorExecutor findExecutorForConstructor(String typeName, List<TypeDescriptor> argumentTypes, ExpressionState state) throws SpelEvaluationException {
/* 185 */     EvaluationContext evalContext = state.getEvaluationContext();
/* 186 */     List<ConstructorResolver> ctorResolvers = evalContext.getConstructorResolvers();
/* 187 */     if (ctorResolvers != null) {
/* 188 */       for (ConstructorResolver ctorResolver : ctorResolvers) {
/*     */         try {
/* 190 */           ConstructorExecutor ce = ctorResolver.resolve(state.getEvaluationContext(), typeName, argumentTypes);
/* 191 */           if (ce != null) {
/* 192 */             return ce;
/*     */           }
/*     */         }
/* 195 */         catch (AccessException ex) {
/* 196 */           throw new SpelEvaluationException(getStartPosition(), ex, SpelMessage.CONSTRUCTOR_INVOCATION_PROBLEM, new Object[] { typeName, 
/*     */                 
/* 198 */                 FormatHelper.formatMethodForMessage("", argumentTypes) });
/*     */         } 
/*     */       } 
/*     */     }
/* 202 */     throw new SpelEvaluationException(getStartPosition(), SpelMessage.CONSTRUCTOR_NOT_FOUND, new Object[] { typeName, 
/* 203 */           FormatHelper.formatMethodForMessage("", argumentTypes) });
/*     */   }
/*     */ 
/*     */   
/*     */   public String toStringAST() {
/* 208 */     StringBuilder sb = new StringBuilder("new ");
/* 209 */     int index = 0;
/* 210 */     sb.append(getChild(index++).toStringAST());
/* 211 */     sb.append("(");
/* 212 */     for (int i = index; i < getChildCount(); i++) {
/* 213 */       if (i > index) {
/* 214 */         sb.append(",");
/*     */       }
/* 216 */       sb.append(getChild(i).toStringAST());
/*     */     } 
/* 218 */     sb.append(")");
/* 219 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private TypedValue createArray(ExpressionState state) throws EvaluationException {
/*     */     Class<?> componentType;
/* 230 */     Object newArray, intendedArrayType = getChild(0).getValue(state);
/* 231 */     if (!(intendedArrayType instanceof String))
/* 232 */       throw new SpelEvaluationException(getChild(0).getStartPosition(), SpelMessage.TYPE_NAME_EXPECTED_FOR_ARRAY_CONSTRUCTION, new Object[] {
/*     */             
/* 234 */             FormatHelper.formatClassNameForMessage(intendedArrayType.getClass())
/*     */           }); 
/* 236 */     String type = (String)intendedArrayType;
/*     */     
/* 238 */     TypeCode arrayTypeCode = TypeCode.forName(type);
/* 239 */     if (arrayTypeCode == TypeCode.OBJECT) {
/* 240 */       componentType = state.findType(type);
/*     */     } else {
/*     */       
/* 243 */       componentType = arrayTypeCode.getType();
/*     */     } 
/*     */     
/* 246 */     if (!hasInitializer()) {
/*     */       
/* 248 */       for (SpelNodeImpl dimension : this.dimensions) {
/* 249 */         if (dimension == null) {
/* 250 */           throw new SpelEvaluationException(getStartPosition(), SpelMessage.MISSING_ARRAY_DIMENSION, new Object[0]);
/*     */         }
/*     */       } 
/* 253 */       TypeConverter typeConverter = state.getEvaluationContext().getTypeConverter();
/*     */ 
/*     */       
/* 256 */       if (this.dimensions.length == 1) {
/* 257 */         TypedValue o = this.dimensions[0].getTypedValue(state);
/* 258 */         int arraySize = ExpressionUtils.toInt(typeConverter, o);
/* 259 */         newArray = Array.newInstance(componentType, arraySize);
/*     */       }
/*     */       else {
/*     */         
/* 263 */         int[] dims = new int[this.dimensions.length];
/* 264 */         for (int d = 0; d < this.dimensions.length; d++) {
/* 265 */           TypedValue o = this.dimensions[d].getTypedValue(state);
/* 266 */           dims[d] = ExpressionUtils.toInt(typeConverter, o);
/*     */         } 
/* 268 */         newArray = Array.newInstance(componentType, dims);
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 273 */       if (this.dimensions.length > 1)
/*     */       {
/*     */         
/* 276 */         throw new SpelEvaluationException(getStartPosition(), SpelMessage.MULTIDIM_ARRAY_INITIALIZER_NOT_SUPPORTED, new Object[0]);
/*     */       }
/*     */       
/* 279 */       TypeConverter typeConverter = state.getEvaluationContext().getTypeConverter();
/* 280 */       InlineList initializer = (InlineList)getChild(1);
/*     */       
/* 282 */       if (this.dimensions[0] != null) {
/* 283 */         TypedValue dValue = this.dimensions[0].getTypedValue(state);
/* 284 */         int i = ExpressionUtils.toInt(typeConverter, dValue);
/* 285 */         if (i != initializer.getChildCount()) {
/* 286 */           throw new SpelEvaluationException(getStartPosition(), SpelMessage.INITIALIZER_LENGTH_INCORRECT, new Object[0]);
/*     */         }
/*     */       } 
/*     */       
/* 290 */       int arraySize = initializer.getChildCount();
/* 291 */       newArray = Array.newInstance(componentType, arraySize);
/* 292 */       if (arrayTypeCode == TypeCode.OBJECT) {
/* 293 */         populateReferenceTypeArray(state, newArray, typeConverter, initializer, componentType);
/*     */       }
/* 295 */       else if (arrayTypeCode == TypeCode.BOOLEAN) {
/* 296 */         populateBooleanArray(state, newArray, typeConverter, initializer);
/*     */       }
/* 298 */       else if (arrayTypeCode == TypeCode.BYTE) {
/* 299 */         populateByteArray(state, newArray, typeConverter, initializer);
/*     */       }
/* 301 */       else if (arrayTypeCode == TypeCode.CHAR) {
/* 302 */         populateCharArray(state, newArray, typeConverter, initializer);
/*     */       }
/* 304 */       else if (arrayTypeCode == TypeCode.DOUBLE) {
/* 305 */         populateDoubleArray(state, newArray, typeConverter, initializer);
/*     */       }
/* 307 */       else if (arrayTypeCode == TypeCode.FLOAT) {
/* 308 */         populateFloatArray(state, newArray, typeConverter, initializer);
/*     */       }
/* 310 */       else if (arrayTypeCode == TypeCode.INT) {
/* 311 */         populateIntArray(state, newArray, typeConverter, initializer);
/*     */       }
/* 313 */       else if (arrayTypeCode == TypeCode.LONG) {
/* 314 */         populateLongArray(state, newArray, typeConverter, initializer);
/*     */       }
/* 316 */       else if (arrayTypeCode == TypeCode.SHORT) {
/* 317 */         populateShortArray(state, newArray, typeConverter, initializer);
/*     */       } else {
/*     */         
/* 320 */         throw new IllegalStateException(arrayTypeCode.name());
/*     */       } 
/*     */     } 
/* 323 */     return new TypedValue(newArray);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void populateReferenceTypeArray(ExpressionState state, Object newArray, TypeConverter typeConverter, InlineList initializer, Class<?> componentType) {
/* 329 */     TypeDescriptor toTypeDescriptor = TypeDescriptor.valueOf(componentType);
/* 330 */     Object[] newObjectArray = (Object[])newArray;
/* 331 */     for (int i = 0; i < newObjectArray.length; i++) {
/* 332 */       SpelNode elementNode = initializer.getChild(i);
/* 333 */       Object arrayEntry = elementNode.getValue(state);
/* 334 */       newObjectArray[i] = typeConverter.convertValue(arrayEntry, 
/* 335 */           TypeDescriptor.forObject(arrayEntry), toTypeDescriptor);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void populateByteArray(ExpressionState state, Object newArray, TypeConverter typeConverter, InlineList initializer) {
/* 342 */     byte[] newByteArray = (byte[])newArray;
/* 343 */     for (int i = 0; i < newByteArray.length; i++) {
/* 344 */       TypedValue typedValue = initializer.getChild(i).getTypedValue(state);
/* 345 */       newByteArray[i] = ExpressionUtils.toByte(typeConverter, typedValue);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void populateFloatArray(ExpressionState state, Object newArray, TypeConverter typeConverter, InlineList initializer) {
/* 352 */     float[] newFloatArray = (float[])newArray;
/* 353 */     for (int i = 0; i < newFloatArray.length; i++) {
/* 354 */       TypedValue typedValue = initializer.getChild(i).getTypedValue(state);
/* 355 */       newFloatArray[i] = ExpressionUtils.toFloat(typeConverter, typedValue);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void populateDoubleArray(ExpressionState state, Object newArray, TypeConverter typeConverter, InlineList initializer) {
/* 362 */     double[] newDoubleArray = (double[])newArray;
/* 363 */     for (int i = 0; i < newDoubleArray.length; i++) {
/* 364 */       TypedValue typedValue = initializer.getChild(i).getTypedValue(state);
/* 365 */       newDoubleArray[i] = ExpressionUtils.toDouble(typeConverter, typedValue);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void populateShortArray(ExpressionState state, Object newArray, TypeConverter typeConverter, InlineList initializer) {
/* 372 */     short[] newShortArray = (short[])newArray;
/* 373 */     for (int i = 0; i < newShortArray.length; i++) {
/* 374 */       TypedValue typedValue = initializer.getChild(i).getTypedValue(state);
/* 375 */       newShortArray[i] = ExpressionUtils.toShort(typeConverter, typedValue);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void populateLongArray(ExpressionState state, Object newArray, TypeConverter typeConverter, InlineList initializer) {
/* 382 */     long[] newLongArray = (long[])newArray;
/* 383 */     for (int i = 0; i < newLongArray.length; i++) {
/* 384 */       TypedValue typedValue = initializer.getChild(i).getTypedValue(state);
/* 385 */       newLongArray[i] = ExpressionUtils.toLong(typeConverter, typedValue);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void populateCharArray(ExpressionState state, Object newArray, TypeConverter typeConverter, InlineList initializer) {
/* 392 */     char[] newCharArray = (char[])newArray;
/* 393 */     for (int i = 0; i < newCharArray.length; i++) {
/* 394 */       TypedValue typedValue = initializer.getChild(i).getTypedValue(state);
/* 395 */       newCharArray[i] = ExpressionUtils.toChar(typeConverter, typedValue);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void populateBooleanArray(ExpressionState state, Object newArray, TypeConverter typeConverter, InlineList initializer) {
/* 402 */     boolean[] newBooleanArray = (boolean[])newArray;
/* 403 */     for (int i = 0; i < newBooleanArray.length; i++) {
/* 404 */       TypedValue typedValue = initializer.getChild(i).getTypedValue(state);
/* 405 */       newBooleanArray[i] = ExpressionUtils.toBoolean(typeConverter, typedValue);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void populateIntArray(ExpressionState state, Object newArray, TypeConverter typeConverter, InlineList initializer) {
/* 412 */     int[] newIntArray = (int[])newArray;
/* 413 */     for (int i = 0; i < newIntArray.length; i++) {
/* 414 */       TypedValue typedValue = initializer.getChild(i).getTypedValue(state);
/* 415 */       newIntArray[i] = ExpressionUtils.toInt(typeConverter, typedValue);
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean hasInitializer() {
/* 420 */     return (getChildCount() > 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCompilable() {
/* 425 */     if (!(this.cachedExecutor instanceof ReflectiveConstructorExecutor) || this.exitTypeDescriptor == null)
/*     */     {
/* 427 */       return false;
/*     */     }
/*     */     
/* 430 */     if (getChildCount() > 1) {
/* 431 */       for (int c = 1, max = getChildCount(); c < max; c++) {
/* 432 */         if (!this.children[c].isCompilable()) {
/* 433 */           return false;
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/* 438 */     ReflectiveConstructorExecutor executor = (ReflectiveConstructorExecutor)this.cachedExecutor;
/* 439 */     Constructor<?> constructor = executor.getConstructor();
/* 440 */     return (Modifier.isPublic(constructor.getModifiers()) && 
/* 441 */       Modifier.isPublic(constructor.getDeclaringClass().getModifiers()));
/*     */   }
/*     */ 
/*     */   
/*     */   public void generateCode(MethodVisitor mv, CodeFlow cf) {
/* 446 */     ReflectiveConstructorExecutor executor = (ReflectiveConstructorExecutor)this.cachedExecutor;
/* 447 */     Constructor<?> constructor = executor.getConstructor();
/* 448 */     String classDesc = constructor.getDeclaringClass().getName().replace('.', '/');
/* 449 */     mv.visitTypeInsn(187, classDesc);
/* 450 */     mv.visitInsn(89);
/*     */     
/* 452 */     SpelNodeImpl[] arguments = new SpelNodeImpl[this.children.length - 1];
/* 453 */     System.arraycopy(this.children, 1, arguments, 0, this.children.length - 1);
/* 454 */     generateCodeForArguments(mv, cf, constructor, arguments);
/* 455 */     mv.visitMethodInsn(183, classDesc, "<init>", CodeFlow.createSignatureDescriptor(constructor), false);
/* 456 */     cf.pushDescriptor(this.exitTypeDescriptor);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\expression\spel\ast\ConstructorReference.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */