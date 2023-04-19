/*     */ package org.springframework.expression.spel.ast;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.springframework.asm.Label;
/*     */ import org.springframework.asm.MethodVisitor;
/*     */ import org.springframework.core.convert.TypeDescriptor;
/*     */ import org.springframework.expression.AccessException;
/*     */ import org.springframework.expression.EvaluationContext;
/*     */ import org.springframework.expression.EvaluationException;
/*     */ import org.springframework.expression.PropertyAccessor;
/*     */ import org.springframework.expression.TypedValue;
/*     */ import org.springframework.expression.spel.CodeFlow;
/*     */ import org.springframework.expression.spel.CompilablePropertyAccessor;
/*     */ import org.springframework.expression.spel.ExpressionState;
/*     */ import org.springframework.expression.spel.SpelEvaluationException;
/*     */ import org.springframework.expression.spel.SpelMessage;
/*     */ import org.springframework.expression.spel.support.ReflectivePropertyAccessor;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PropertyOrFieldReference
/*     */   extends SpelNodeImpl
/*     */ {
/*     */   private final boolean nullSafe;
/*  51 */   private String originalPrimitiveExitTypeDescriptor = null;
/*     */   
/*     */   private final String name;
/*     */   
/*     */   private volatile PropertyAccessor cachedReadAccessor;
/*     */   
/*     */   private volatile PropertyAccessor cachedWriteAccessor;
/*     */ 
/*     */   
/*     */   public PropertyOrFieldReference(boolean nullSafe, String propertyOrFieldName, int pos) {
/*  61 */     super(pos, new SpelNodeImpl[0]);
/*  62 */     this.nullSafe = nullSafe;
/*  63 */     this.name = propertyOrFieldName;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isNullSafe() {
/*  68 */     return this.nullSafe;
/*     */   }
/*     */   
/*     */   public String getName() {
/*  72 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ValueRef getValueRef(ExpressionState state) throws EvaluationException {
/*  78 */     return new AccessorLValue(this, state.getActiveContextObject(), state.getEvaluationContext(), state
/*  79 */         .getConfiguration().isAutoGrowNullReferences());
/*     */   }
/*     */ 
/*     */   
/*     */   public TypedValue getValueInternal(ExpressionState state) throws EvaluationException {
/*  84 */     TypedValue tv = getValueInternal(state.getActiveContextObject(), state.getEvaluationContext(), state
/*  85 */         .getConfiguration().isAutoGrowNullReferences());
/*  86 */     PropertyAccessor accessorToUse = this.cachedReadAccessor;
/*  87 */     if (accessorToUse instanceof CompilablePropertyAccessor) {
/*  88 */       CompilablePropertyAccessor accessor = (CompilablePropertyAccessor)accessorToUse;
/*  89 */       setExitTypeDescriptor(CodeFlow.toDescriptor(accessor.getPropertyType()));
/*     */     } 
/*  91 */     return tv;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private TypedValue getValueInternal(TypedValue contextObject, EvaluationContext evalContext, boolean isAutoGrowNullReferences) throws EvaluationException {
/*  97 */     TypedValue result = readProperty(contextObject, evalContext, this.name);
/*     */ 
/*     */     
/* 100 */     if (result.getValue() == null && isAutoGrowNullReferences && 
/* 101 */       nextChildIs(new Class[] { Indexer.class, PropertyOrFieldReference.class })) {
/* 102 */       TypeDescriptor resultDescriptor = result.getTypeDescriptor();
/*     */       
/* 104 */       if (List.class == resultDescriptor.getType()) {
/*     */         try {
/* 106 */           if (isWritableProperty(this.name, contextObject, evalContext)) {
/* 107 */             List<?> newList = ArrayList.class.newInstance();
/* 108 */             writeProperty(contextObject, evalContext, this.name, newList);
/* 109 */             result = readProperty(contextObject, evalContext, this.name);
/*     */           }
/*     */         
/* 112 */         } catch (InstantiationException ex) {
/* 113 */           throw new SpelEvaluationException(getStartPosition(), ex, SpelMessage.UNABLE_TO_CREATE_LIST_FOR_INDEXING, new Object[0]);
/*     */         
/*     */         }
/* 116 */         catch (IllegalAccessException ex) {
/* 117 */           throw new SpelEvaluationException(getStartPosition(), ex, SpelMessage.UNABLE_TO_CREATE_LIST_FOR_INDEXING, new Object[0]);
/*     */         }
/*     */       
/*     */       }
/* 121 */       else if (Map.class == resultDescriptor.getType()) {
/*     */         try {
/* 123 */           if (isWritableProperty(this.name, contextObject, evalContext)) {
/* 124 */             Map<?, ?> newMap = HashMap.class.newInstance();
/* 125 */             writeProperty(contextObject, evalContext, this.name, newMap);
/* 126 */             result = readProperty(contextObject, evalContext, this.name);
/*     */           }
/*     */         
/* 129 */         } catch (InstantiationException ex) {
/* 130 */           throw new SpelEvaluationException(getStartPosition(), ex, SpelMessage.UNABLE_TO_CREATE_MAP_FOR_INDEXING, new Object[0]);
/*     */         
/*     */         }
/* 133 */         catch (IllegalAccessException ex) {
/* 134 */           throw new SpelEvaluationException(getStartPosition(), ex, SpelMessage.UNABLE_TO_CREATE_MAP_FOR_INDEXING, new Object[0]);
/*     */         } 
/*     */       } else {
/*     */ 
/*     */         
/*     */         try {
/*     */           
/* 141 */           if (isWritableProperty(this.name, contextObject, evalContext)) {
/* 142 */             Object newObject = result.getTypeDescriptor().getType().newInstance();
/* 143 */             writeProperty(contextObject, evalContext, this.name, newObject);
/* 144 */             result = readProperty(contextObject, evalContext, this.name);
/*     */           }
/*     */         
/* 147 */         } catch (InstantiationException ex) {
/* 148 */           throw new SpelEvaluationException(getStartPosition(), ex, SpelMessage.UNABLE_TO_DYNAMICALLY_CREATE_OBJECT, new Object[] { result
/* 149 */                 .getTypeDescriptor().getType() });
/*     */         }
/* 151 */         catch (IllegalAccessException ex) {
/* 152 */           throw new SpelEvaluationException(getStartPosition(), ex, SpelMessage.UNABLE_TO_DYNAMICALLY_CREATE_OBJECT, new Object[] { result
/* 153 */                 .getTypeDescriptor().getType() });
/*     */         } 
/*     */       } 
/*     */     } 
/* 157 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setValue(ExpressionState state, Object newValue) throws EvaluationException {
/* 162 */     writeProperty(state.getActiveContextObject(), state.getEvaluationContext(), this.name, newValue);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isWritable(ExpressionState state) throws EvaluationException {
/* 167 */     return isWritableProperty(this.name, state.getActiveContextObject(), state.getEvaluationContext());
/*     */   }
/*     */ 
/*     */   
/*     */   public String toStringAST() {
/* 172 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private TypedValue readProperty(TypedValue contextObject, EvaluationContext evalContext, String name) throws EvaluationException {
/* 183 */     Object targetObject = contextObject.getValue();
/* 184 */     if (targetObject == null && this.nullSafe) {
/* 185 */       return TypedValue.NULL;
/*     */     }
/*     */     
/* 188 */     PropertyAccessor accessorToUse = this.cachedReadAccessor;
/* 189 */     if (accessorToUse != null) {
/* 190 */       if (evalContext.getPropertyAccessors().contains(accessorToUse)) {
/*     */         try {
/* 192 */           return accessorToUse.read(evalContext, contextObject.getValue(), name);
/*     */         }
/* 194 */         catch (Exception exception) {}
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 199 */       this.cachedReadAccessor = null;
/*     */     } 
/*     */ 
/*     */     
/* 203 */     List<PropertyAccessor> accessorsToTry = getPropertyAccessorsToTry(contextObject.getValue(), evalContext.getPropertyAccessors());
/*     */ 
/*     */ 
/*     */     
/* 207 */     if (accessorsToTry != null) {
/*     */       try {
/* 209 */         for (PropertyAccessor accessor : accessorsToTry) {
/* 210 */           if (accessor.canRead(evalContext, contextObject.getValue(), name)) {
/* 211 */             if (accessor instanceof ReflectivePropertyAccessor) {
/* 212 */               accessor = ((ReflectivePropertyAccessor)accessor).createOptimalAccessor(evalContext, contextObject
/* 213 */                   .getValue(), name);
/*     */             }
/* 215 */             this.cachedReadAccessor = accessor;
/* 216 */             return accessor.read(evalContext, contextObject.getValue(), name);
/*     */           }
/*     */         
/*     */         } 
/* 220 */       } catch (Exception ex) {
/* 221 */         throw new SpelEvaluationException(ex, SpelMessage.EXCEPTION_DURING_PROPERTY_READ, new Object[] { name, ex.getMessage() });
/*     */       } 
/*     */     }
/* 224 */     if (contextObject.getValue() == null) {
/* 225 */       throw new SpelEvaluationException(SpelMessage.PROPERTY_OR_FIELD_NOT_READABLE_ON_NULL, new Object[] { name });
/*     */     }
/*     */     
/* 228 */     throw new SpelEvaluationException(getStartPosition(), SpelMessage.PROPERTY_OR_FIELD_NOT_READABLE, new Object[] { name, 
/* 229 */           FormatHelper.formatClassNameForMessage(getObjectClass(contextObject.getValue())) });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeProperty(TypedValue contextObject, EvaluationContext evalContext, String name, Object newValue) throws EvaluationException {
/* 236 */     if (contextObject.getValue() == null && this.nullSafe) {
/*     */       return;
/*     */     }
/*     */     
/* 240 */     PropertyAccessor accessorToUse = this.cachedWriteAccessor;
/* 241 */     if (accessorToUse != null) {
/* 242 */       if (evalContext.getPropertyAccessors().contains(accessorToUse)) {
/*     */         try {
/* 244 */           accessorToUse.write(evalContext, contextObject.getValue(), name, newValue);
/*     */           
/*     */           return;
/* 247 */         } catch (Exception exception) {}
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 252 */       this.cachedWriteAccessor = null;
/*     */     } 
/*     */ 
/*     */     
/* 256 */     List<PropertyAccessor> accessorsToTry = getPropertyAccessorsToTry(contextObject.getValue(), evalContext.getPropertyAccessors());
/* 257 */     if (accessorsToTry != null) {
/*     */       try {
/* 259 */         for (PropertyAccessor accessor : accessorsToTry) {
/* 260 */           if (accessor.canWrite(evalContext, contextObject.getValue(), name)) {
/* 261 */             this.cachedWriteAccessor = accessor;
/* 262 */             accessor.write(evalContext, contextObject.getValue(), name, newValue);
/*     */             
/*     */             return;
/*     */           } 
/*     */         } 
/* 267 */       } catch (AccessException ex) {
/* 268 */         throw new SpelEvaluationException(getStartPosition(), ex, SpelMessage.EXCEPTION_DURING_PROPERTY_WRITE, new Object[] { name, ex
/* 269 */               .getMessage() });
/*     */       } 
/*     */     }
/* 272 */     if (contextObject.getValue() == null) {
/* 273 */       throw new SpelEvaluationException(getStartPosition(), SpelMessage.PROPERTY_OR_FIELD_NOT_WRITABLE_ON_NULL, new Object[] { name });
/*     */     }
/*     */     
/* 276 */     throw new SpelEvaluationException(getStartPosition(), SpelMessage.PROPERTY_OR_FIELD_NOT_WRITABLE, new Object[] { name, 
/* 277 */           FormatHelper.formatClassNameForMessage(getObjectClass(contextObject.getValue())) });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isWritableProperty(String name, TypedValue contextObject, EvaluationContext evalContext) throws EvaluationException {
/* 285 */     List<PropertyAccessor> accessorsToTry = getPropertyAccessorsToTry(contextObject.getValue(), evalContext.getPropertyAccessors());
/* 286 */     if (accessorsToTry != null) {
/* 287 */       for (PropertyAccessor accessor : accessorsToTry) {
/*     */         try {
/* 289 */           if (accessor.canWrite(evalContext, contextObject.getValue(), name)) {
/* 290 */             return true;
/*     */           }
/*     */         }
/* 293 */         catch (AccessException accessException) {}
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 298 */     return false;
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
/*     */   private List<PropertyAccessor> getPropertyAccessorsToTry(Object contextObject, List<PropertyAccessor> propertyAccessors) {
/* 314 */     Class<?> targetType = (contextObject != null) ? contextObject.getClass() : null;
/*     */     
/* 316 */     List<PropertyAccessor> specificAccessors = new ArrayList<PropertyAccessor>();
/* 317 */     List<PropertyAccessor> generalAccessors = new ArrayList<PropertyAccessor>();
/* 318 */     for (PropertyAccessor resolver : propertyAccessors) {
/* 319 */       Class<?>[] targets = resolver.getSpecificTargetClasses();
/* 320 */       if (targets == null) {
/*     */         
/* 322 */         generalAccessors.add(resolver); continue;
/*     */       } 
/* 324 */       if (targetType != null) {
/* 325 */         for (Class<?> clazz : targets) {
/* 326 */           if (clazz == targetType) {
/* 327 */             specificAccessors.add(resolver);
/*     */             break;
/*     */           } 
/* 330 */           if (clazz.isAssignableFrom(targetType)) {
/* 331 */             generalAccessors.add(resolver);
/*     */           }
/*     */         } 
/*     */       }
/*     */     } 
/* 336 */     List<PropertyAccessor> resolvers = new ArrayList<PropertyAccessor>();
/* 337 */     resolvers.addAll(specificAccessors);
/* 338 */     generalAccessors.removeAll(specificAccessors);
/* 339 */     resolvers.addAll(generalAccessors);
/* 340 */     return resolvers;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCompilable() {
/* 345 */     PropertyAccessor accessorToUse = this.cachedReadAccessor;
/* 346 */     return (accessorToUse instanceof CompilablePropertyAccessor && ((CompilablePropertyAccessor)accessorToUse)
/* 347 */       .isCompilable());
/*     */   }
/*     */ 
/*     */   
/*     */   public void generateCode(MethodVisitor mv, CodeFlow cf) {
/* 352 */     PropertyAccessor accessorToUse = this.cachedReadAccessor;
/* 353 */     if (!(accessorToUse instanceof CompilablePropertyAccessor)) {
/* 354 */       throw new IllegalStateException("Property accessor is not compilable: " + accessorToUse);
/*     */     }
/* 356 */     Label skipIfNull = null;
/* 357 */     if (this.nullSafe) {
/* 358 */       mv.visitInsn(89);
/* 359 */       skipIfNull = new Label();
/* 360 */       Label continueLabel = new Label();
/* 361 */       mv.visitJumpInsn(199, continueLabel);
/* 362 */       CodeFlow.insertCheckCast(mv, this.exitTypeDescriptor);
/* 363 */       mv.visitJumpInsn(167, skipIfNull);
/* 364 */       mv.visitLabel(continueLabel);
/*     */     } 
/* 366 */     ((CompilablePropertyAccessor)accessorToUse).generateCode(this.name, mv, cf);
/* 367 */     cf.pushDescriptor(this.exitTypeDescriptor);
/* 368 */     if (this.originalPrimitiveExitTypeDescriptor != null)
/*     */     {
/*     */ 
/*     */       
/* 372 */       CodeFlow.insertBoxIfNecessary(mv, this.originalPrimitiveExitTypeDescriptor);
/*     */     }
/* 374 */     if (skipIfNull != null) {
/* 375 */       mv.visitLabel(skipIfNull);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setExitTypeDescriptor(String descriptor) {
/* 383 */     if (this.nullSafe && CodeFlow.isPrimitive(descriptor)) {
/* 384 */       this.originalPrimitiveExitTypeDescriptor = descriptor;
/* 385 */       this.exitTypeDescriptor = CodeFlow.toBoxedDescriptor(descriptor);
/*     */     } else {
/*     */       
/* 388 */       this.exitTypeDescriptor = descriptor;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static class AccessorLValue
/*     */     implements ValueRef
/*     */   {
/*     */     private final PropertyOrFieldReference ref;
/*     */     
/*     */     private final TypedValue contextObject;
/*     */     
/*     */     private final EvaluationContext evalContext;
/*     */     
/*     */     private final boolean autoGrowNullReferences;
/*     */     
/*     */     public AccessorLValue(PropertyOrFieldReference propertyOrFieldReference, TypedValue activeContextObject, EvaluationContext evalContext, boolean autoGrowNullReferences) {
/* 405 */       this.ref = propertyOrFieldReference;
/* 406 */       this.contextObject = activeContextObject;
/* 407 */       this.evalContext = evalContext;
/* 408 */       this.autoGrowNullReferences = autoGrowNullReferences;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public TypedValue getValue() {
/* 414 */       TypedValue value = this.ref.getValueInternal(this.contextObject, this.evalContext, this.autoGrowNullReferences);
/* 415 */       PropertyAccessor accessorToUse = this.ref.cachedReadAccessor;
/* 416 */       if (accessorToUse instanceof CompilablePropertyAccessor) {
/* 417 */         this.ref.setExitTypeDescriptor(CodeFlow.toDescriptor(((CompilablePropertyAccessor)accessorToUse).getPropertyType()));
/*     */       }
/* 419 */       return value;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setValue(Object newValue) {
/* 424 */       this.ref.writeProperty(this.contextObject, this.evalContext, this.ref.name, newValue);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isWritable() {
/* 429 */       return this.ref.isWritableProperty(this.ref.name, this.contextObject, this.evalContext);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\expression\spel\ast\PropertyOrFieldReference.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */