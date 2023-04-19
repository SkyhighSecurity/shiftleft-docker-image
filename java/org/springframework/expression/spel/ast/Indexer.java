/*     */ package org.springframework.expression.spel.ast;
/*     */ 
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.springframework.asm.MethodVisitor;
/*     */ import org.springframework.core.convert.TypeDescriptor;
/*     */ import org.springframework.expression.AccessException;
/*     */ import org.springframework.expression.EvaluationContext;
/*     */ import org.springframework.expression.EvaluationException;
/*     */ import org.springframework.expression.PropertyAccessor;
/*     */ import org.springframework.expression.TypeConverter;
/*     */ import org.springframework.expression.TypedValue;
/*     */ import org.springframework.expression.spel.CodeFlow;
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
/*     */ public class Indexer
/*     */   extends SpelNodeImpl
/*     */ {
/*     */   private String cachedReadName;
/*     */   private Class<?> cachedReadTargetType;
/*     */   private PropertyAccessor cachedReadAccessor;
/*     */   private String cachedWriteName;
/*     */   private Class<?> cachedWriteTargetType;
/*     */   private PropertyAccessor cachedWriteAccessor;
/*     */   private IndexedType indexedType;
/*     */   
/*     */   private enum IndexedType
/*     */   {
/*  54 */     ARRAY, LIST, MAP, STRING, OBJECT;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Indexer(int pos, SpelNodeImpl expr) {
/*  83 */     super(pos, new SpelNodeImpl[] { expr });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public TypedValue getValueInternal(ExpressionState state) throws EvaluationException {
/*  89 */     return getValueRef(state).getValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setValue(ExpressionState state, Object newValue) throws EvaluationException {
/*  94 */     getValueRef(state).setValue(newValue);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isWritable(ExpressionState expressionState) throws SpelEvaluationException {
/*  99 */     return true;
/*     */   }
/*     */   
/*     */   protected ValueRef getValueRef(ExpressionState state) throws EvaluationException {
/*     */     TypedValue indexValue;
/*     */     Object index;
/* 105 */     TypedValue context = state.getActiveContextObject();
/* 106 */     Object target = context.getValue();
/* 107 */     TypeDescriptor targetDescriptor = context.getTypeDescriptor();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 112 */     if (target instanceof Map && this.children[0] instanceof PropertyOrFieldReference) {
/* 113 */       PropertyOrFieldReference reference = (PropertyOrFieldReference)this.children[0];
/* 114 */       index = reference.getName();
/* 115 */       indexValue = new TypedValue(index);
/*     */     } else {
/*     */ 
/*     */       
/*     */       try {
/*     */         
/* 121 */         state.pushActiveContextObject(state.getRootContextObject());
/* 122 */         indexValue = this.children[0].getValueInternal(state);
/* 123 */         index = indexValue.getValue();
/*     */       } finally {
/*     */         
/* 126 */         state.popActiveContextObject();
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 131 */     if (target == null) {
/* 132 */       throw new SpelEvaluationException(getStartPosition(), SpelMessage.CANNOT_INDEX_INTO_NULL_VALUE, new Object[0]);
/*     */     }
/*     */ 
/*     */     
/* 136 */     if (target instanceof Map) {
/* 137 */       Object key = index;
/* 138 */       if (targetDescriptor.getMapKeyTypeDescriptor() != null) {
/* 139 */         key = state.convertValue(key, targetDescriptor.getMapKeyTypeDescriptor());
/*     */       }
/* 141 */       this.indexedType = IndexedType.MAP;
/* 142 */       return new MapIndexingValueRef(state.getTypeConverter(), (Map)target, key, targetDescriptor);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 147 */     if (target.getClass().isArray() || target instanceof Collection || target instanceof String) {
/* 148 */       int idx = ((Integer)state.convertValue(index, TypeDescriptor.valueOf(Integer.class))).intValue();
/* 149 */       if (target.getClass().isArray()) {
/* 150 */         this.indexedType = IndexedType.ARRAY;
/* 151 */         return new ArrayIndexingValueRef(state.getTypeConverter(), target, idx, targetDescriptor);
/*     */       } 
/* 153 */       if (target instanceof Collection) {
/* 154 */         if (target instanceof List) {
/* 155 */           this.indexedType = IndexedType.LIST;
/*     */         }
/* 157 */         return new CollectionIndexingValueRef((Collection)target, idx, targetDescriptor, state
/* 158 */             .getTypeConverter(), state.getConfiguration().isAutoGrowCollections(), state
/* 159 */             .getConfiguration().getMaximumAutoGrowSize());
/*     */       } 
/*     */       
/* 162 */       this.indexedType = IndexedType.STRING;
/* 163 */       return new StringIndexingLValue((String)target, idx, targetDescriptor);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 169 */     if (String.class == indexValue.getTypeDescriptor().getType()) {
/* 170 */       this.indexedType = IndexedType.OBJECT;
/* 171 */       return new PropertyIndexingValueRef(target, (String)index, state
/* 172 */           .getEvaluationContext(), targetDescriptor);
/*     */     } 
/*     */     
/* 175 */     throw new SpelEvaluationException(
/* 176 */         getStartPosition(), SpelMessage.INDEXING_NOT_SUPPORTED_FOR_TYPE, new Object[] { targetDescriptor });
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCompilable() {
/* 181 */     if (this.indexedType == IndexedType.ARRAY) {
/* 182 */       return (this.exitTypeDescriptor != null);
/*     */     }
/* 184 */     if (this.indexedType == IndexedType.LIST) {
/* 185 */       return this.children[0].isCompilable();
/*     */     }
/* 187 */     if (this.indexedType == IndexedType.MAP) {
/* 188 */       return (this.children[0] instanceof PropertyOrFieldReference || this.children[0].isCompilable());
/*     */     }
/* 190 */     if (this.indexedType == IndexedType.OBJECT)
/*     */     {
/* 192 */       return (this.cachedReadAccessor != null && this.cachedReadAccessor instanceof ReflectivePropertyAccessor.OptimalPropertyAccessor && 
/*     */         
/* 194 */         getChild(0) instanceof StringLiteral);
/*     */     }
/* 196 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void generateCode(MethodVisitor mv, CodeFlow cf) {
/* 201 */     String descriptor = cf.lastDescriptor();
/* 202 */     if (descriptor == null)
/*     */     {
/* 204 */       cf.loadTarget(mv);
/*     */     }
/*     */     
/* 207 */     if (this.indexedType == IndexedType.ARRAY) {
/*     */       int insn;
/* 209 */       if ("D".equals(this.exitTypeDescriptor)) {
/* 210 */         mv.visitTypeInsn(192, "[D");
/* 211 */         insn = 49;
/*     */       }
/* 213 */       else if ("F".equals(this.exitTypeDescriptor)) {
/* 214 */         mv.visitTypeInsn(192, "[F");
/* 215 */         insn = 48;
/*     */       }
/* 217 */       else if ("J".equals(this.exitTypeDescriptor)) {
/* 218 */         mv.visitTypeInsn(192, "[J");
/* 219 */         insn = 47;
/*     */       }
/* 221 */       else if ("I".equals(this.exitTypeDescriptor)) {
/* 222 */         mv.visitTypeInsn(192, "[I");
/* 223 */         insn = 46;
/*     */       }
/* 225 */       else if ("S".equals(this.exitTypeDescriptor)) {
/* 226 */         mv.visitTypeInsn(192, "[S");
/* 227 */         insn = 53;
/*     */       }
/* 229 */       else if ("B".equals(this.exitTypeDescriptor)) {
/* 230 */         mv.visitTypeInsn(192, "[B");
/* 231 */         insn = 51;
/*     */       }
/* 233 */       else if ("C".equals(this.exitTypeDescriptor)) {
/* 234 */         mv.visitTypeInsn(192, "[C");
/* 235 */         insn = 52;
/*     */       } else {
/*     */         
/* 238 */         mv.visitTypeInsn(192, "[" + this.exitTypeDescriptor + (
/* 239 */             CodeFlow.isPrimitiveArray(this.exitTypeDescriptor) ? "" : ";"));
/*     */         
/* 241 */         insn = 50;
/*     */       } 
/* 243 */       SpelNodeImpl index = this.children[0];
/* 244 */       cf.enterCompilationScope();
/* 245 */       index.generateCode(mv, cf);
/* 246 */       cf.exitCompilationScope();
/* 247 */       mv.visitInsn(insn);
/*     */     
/*     */     }
/* 250 */     else if (this.indexedType == IndexedType.LIST) {
/* 251 */       mv.visitTypeInsn(192, "java/util/List");
/* 252 */       cf.enterCompilationScope();
/* 253 */       this.children[0].generateCode(mv, cf);
/* 254 */       cf.exitCompilationScope();
/* 255 */       mv.visitMethodInsn(185, "java/util/List", "get", "(I)Ljava/lang/Object;", true);
/*     */     
/*     */     }
/* 258 */     else if (this.indexedType == IndexedType.MAP) {
/* 259 */       mv.visitTypeInsn(192, "java/util/Map");
/*     */ 
/*     */       
/* 262 */       if (this.children[0] instanceof PropertyOrFieldReference) {
/* 263 */         PropertyOrFieldReference reference = (PropertyOrFieldReference)this.children[0];
/* 264 */         String mapKeyName = reference.getName();
/* 265 */         mv.visitLdcInsn(mapKeyName);
/*     */       } else {
/*     */         
/* 268 */         cf.enterCompilationScope();
/* 269 */         this.children[0].generateCode(mv, cf);
/* 270 */         cf.exitCompilationScope();
/*     */       } 
/* 272 */       mv.visitMethodInsn(185, "java/util/Map", "get", "(Ljava/lang/Object;)Ljava/lang/Object;", true);
/*     */ 
/*     */     
/*     */     }
/* 276 */     else if (this.indexedType == IndexedType.OBJECT) {
/* 277 */       ReflectivePropertyAccessor.OptimalPropertyAccessor accessor = (ReflectivePropertyAccessor.OptimalPropertyAccessor)this.cachedReadAccessor;
/*     */       
/* 279 */       Member member = accessor.member;
/* 280 */       boolean isStatic = Modifier.isStatic(member.getModifiers());
/* 281 */       String classDesc = member.getDeclaringClass().getName().replace('.', '/');
/*     */       
/* 283 */       if (!isStatic) {
/* 284 */         if (descriptor == null) {
/* 285 */           cf.loadTarget(mv);
/*     */         }
/* 287 */         if (descriptor == null || !classDesc.equals(descriptor.substring(1))) {
/* 288 */           mv.visitTypeInsn(192, classDesc);
/*     */         }
/*     */       } 
/*     */       
/* 292 */       if (member instanceof Method) {
/* 293 */         mv.visitMethodInsn(isStatic ? 184 : 182, classDesc, member.getName(), 
/* 294 */             CodeFlow.createSignatureDescriptor((Method)member), false);
/*     */       } else {
/*     */         
/* 297 */         mv.visitFieldInsn(isStatic ? 178 : 180, classDesc, member.getName(), 
/* 298 */             CodeFlow.toJvmDescriptor(((Field)member).getType()));
/*     */       } 
/*     */     } 
/*     */     
/* 302 */     cf.pushDescriptor(this.exitTypeDescriptor);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toStringAST() {
/* 307 */     StringBuilder sb = new StringBuilder("[");
/* 308 */     for (int i = 0; i < getChildCount(); i++) {
/* 309 */       if (i > 0) {
/* 310 */         sb.append(",");
/*     */       }
/* 312 */       sb.append(getChild(i).toStringAST());
/*     */     } 
/* 314 */     sb.append("]");
/* 315 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setArrayElement(TypeConverter converter, Object ctx, int idx, Object newValue, Class<?> arrayComponentType) throws EvaluationException {
/* 322 */     if (arrayComponentType == boolean.class) {
/* 323 */       boolean[] array = (boolean[])ctx;
/* 324 */       checkAccess(array.length, idx);
/* 325 */       array[idx] = ((Boolean)convertValue(converter, newValue, (Class)Boolean.class)).booleanValue();
/*     */     }
/* 327 */     else if (arrayComponentType == byte.class) {
/* 328 */       byte[] array = (byte[])ctx;
/* 329 */       checkAccess(array.length, idx);
/* 330 */       array[idx] = ((Byte)convertValue(converter, newValue, (Class)Byte.class)).byteValue();
/*     */     }
/* 332 */     else if (arrayComponentType == char.class) {
/* 333 */       char[] array = (char[])ctx;
/* 334 */       checkAccess(array.length, idx);
/* 335 */       array[idx] = ((Character)convertValue(converter, newValue, (Class)Character.class)).charValue();
/*     */     }
/* 337 */     else if (arrayComponentType == double.class) {
/* 338 */       double[] array = (double[])ctx;
/* 339 */       checkAccess(array.length, idx);
/* 340 */       array[idx] = ((Double)convertValue(converter, newValue, (Class)Double.class)).doubleValue();
/*     */     }
/* 342 */     else if (arrayComponentType == float.class) {
/* 343 */       float[] array = (float[])ctx;
/* 344 */       checkAccess(array.length, idx);
/* 345 */       array[idx] = ((Float)convertValue(converter, newValue, (Class)Float.class)).floatValue();
/*     */     }
/* 347 */     else if (arrayComponentType == int.class) {
/* 348 */       int[] array = (int[])ctx;
/* 349 */       checkAccess(array.length, idx);
/* 350 */       array[idx] = ((Integer)convertValue(converter, newValue, (Class)Integer.class)).intValue();
/*     */     }
/* 352 */     else if (arrayComponentType == long.class) {
/* 353 */       long[] array = (long[])ctx;
/* 354 */       checkAccess(array.length, idx);
/* 355 */       array[idx] = ((Long)convertValue(converter, newValue, (Class)Long.class)).longValue();
/*     */     }
/* 357 */     else if (arrayComponentType == short.class) {
/* 358 */       short[] array = (short[])ctx;
/* 359 */       checkAccess(array.length, idx);
/* 360 */       array[idx] = ((Short)convertValue(converter, newValue, (Class)Short.class)).shortValue();
/*     */     } else {
/*     */       
/* 363 */       Object[] array = (Object[])ctx;
/* 364 */       checkAccess(array.length, idx);
/* 365 */       array[idx] = convertValue(converter, newValue, arrayComponentType);
/*     */     } 
/*     */   }
/*     */   
/*     */   private Object accessArrayElement(Object ctx, int idx) throws SpelEvaluationException {
/* 370 */     Class<?> arrayComponentType = ctx.getClass().getComponentType();
/* 371 */     if (arrayComponentType == boolean.class) {
/* 372 */       boolean[] arrayOfBoolean = (boolean[])ctx;
/* 373 */       checkAccess(arrayOfBoolean.length, idx);
/* 374 */       this.exitTypeDescriptor = "Z";
/* 375 */       return Boolean.valueOf(arrayOfBoolean[idx]);
/*     */     } 
/* 377 */     if (arrayComponentType == byte.class) {
/* 378 */       byte[] arrayOfByte = (byte[])ctx;
/* 379 */       checkAccess(arrayOfByte.length, idx);
/* 380 */       this.exitTypeDescriptor = "B";
/* 381 */       return Byte.valueOf(arrayOfByte[idx]);
/*     */     } 
/* 383 */     if (arrayComponentType == char.class) {
/* 384 */       char[] arrayOfChar = (char[])ctx;
/* 385 */       checkAccess(arrayOfChar.length, idx);
/* 386 */       this.exitTypeDescriptor = "C";
/* 387 */       return Character.valueOf(arrayOfChar[idx]);
/*     */     } 
/* 389 */     if (arrayComponentType == double.class) {
/* 390 */       double[] arrayOfDouble = (double[])ctx;
/* 391 */       checkAccess(arrayOfDouble.length, idx);
/* 392 */       this.exitTypeDescriptor = "D";
/* 393 */       return Double.valueOf(arrayOfDouble[idx]);
/*     */     } 
/* 395 */     if (arrayComponentType == float.class) {
/* 396 */       float[] arrayOfFloat = (float[])ctx;
/* 397 */       checkAccess(arrayOfFloat.length, idx);
/* 398 */       this.exitTypeDescriptor = "F";
/* 399 */       return Float.valueOf(arrayOfFloat[idx]);
/*     */     } 
/* 401 */     if (arrayComponentType == int.class) {
/* 402 */       int[] arrayOfInt = (int[])ctx;
/* 403 */       checkAccess(arrayOfInt.length, idx);
/* 404 */       this.exitTypeDescriptor = "I";
/* 405 */       return Integer.valueOf(arrayOfInt[idx]);
/*     */     } 
/* 407 */     if (arrayComponentType == long.class) {
/* 408 */       long[] arrayOfLong = (long[])ctx;
/* 409 */       checkAccess(arrayOfLong.length, idx);
/* 410 */       this.exitTypeDescriptor = "J";
/* 411 */       return Long.valueOf(arrayOfLong[idx]);
/*     */     } 
/* 413 */     if (arrayComponentType == short.class) {
/* 414 */       short[] arrayOfShort = (short[])ctx;
/* 415 */       checkAccess(arrayOfShort.length, idx);
/* 416 */       this.exitTypeDescriptor = "S";
/* 417 */       return Short.valueOf(arrayOfShort[idx]);
/*     */     } 
/*     */     
/* 420 */     Object[] array = (Object[])ctx;
/* 421 */     checkAccess(array.length, idx);
/* 422 */     Object retValue = array[idx];
/* 423 */     this.exitTypeDescriptor = CodeFlow.toDescriptor(arrayComponentType);
/* 424 */     return retValue;
/*     */   }
/*     */ 
/*     */   
/*     */   private void checkAccess(int arrayLength, int index) throws SpelEvaluationException {
/* 429 */     if (index > arrayLength) {
/* 430 */       throw new SpelEvaluationException(getStartPosition(), SpelMessage.ARRAY_INDEX_OUT_OF_BOUNDS, new Object[] {
/* 431 */             Integer.valueOf(arrayLength), Integer.valueOf(index)
/*     */           });
/*     */     }
/*     */   }
/*     */   
/*     */   private <T> T convertValue(TypeConverter converter, Object value, Class<T> targetType) {
/* 437 */     return (T)converter.convertValue(value, TypeDescriptor.forObject(value), TypeDescriptor.valueOf(targetType));
/*     */   }
/*     */ 
/*     */   
/*     */   private class ArrayIndexingValueRef
/*     */     implements ValueRef
/*     */   {
/*     */     private final TypeConverter typeConverter;
/*     */     
/*     */     private final Object array;
/*     */     
/*     */     private final int index;
/*     */     private final TypeDescriptor typeDescriptor;
/*     */     
/*     */     ArrayIndexingValueRef(TypeConverter typeConverter, Object array, int index, TypeDescriptor typeDescriptor) {
/* 452 */       this.typeConverter = typeConverter;
/* 453 */       this.array = array;
/* 454 */       this.index = index;
/* 455 */       this.typeDescriptor = typeDescriptor;
/*     */     }
/*     */ 
/*     */     
/*     */     public TypedValue getValue() {
/* 460 */       Object arrayElement = Indexer.this.accessArrayElement(this.array, this.index);
/* 461 */       return new TypedValue(arrayElement, this.typeDescriptor.elementTypeDescriptor(arrayElement));
/*     */     }
/*     */ 
/*     */     
/*     */     public void setValue(Object newValue) {
/* 466 */       Indexer.this.setArrayElement(this.typeConverter, this.array, this.index, newValue, this.typeDescriptor
/* 467 */           .getElementTypeDescriptor().getType());
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isWritable() {
/* 472 */       return true;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private class MapIndexingValueRef
/*     */     implements ValueRef
/*     */   {
/*     */     private final TypeConverter typeConverter;
/*     */     
/*     */     private final Map map;
/*     */     
/*     */     private final Object key;
/*     */     
/*     */     private final TypeDescriptor mapEntryDescriptor;
/*     */     
/*     */     public MapIndexingValueRef(TypeConverter typeConverter, Map map, Object key, TypeDescriptor mapEntryDescriptor) {
/* 489 */       this.typeConverter = typeConverter;
/* 490 */       this.map = map;
/* 491 */       this.key = key;
/* 492 */       this.mapEntryDescriptor = mapEntryDescriptor;
/*     */     }
/*     */ 
/*     */     
/*     */     public TypedValue getValue() {
/* 497 */       Object value = this.map.get(this.key);
/* 498 */       Indexer.this.exitTypeDescriptor = CodeFlow.toDescriptor(Object.class);
/* 499 */       return new TypedValue(value, this.mapEntryDescriptor.getMapValueTypeDescriptor(value));
/*     */     }
/*     */ 
/*     */     
/*     */     public void setValue(Object newValue) {
/* 504 */       if (this.mapEntryDescriptor.getMapValueTypeDescriptor() != null) {
/* 505 */         newValue = this.typeConverter.convertValue(newValue, TypeDescriptor.forObject(newValue), this.mapEntryDescriptor
/* 506 */             .getMapValueTypeDescriptor());
/*     */       }
/* 508 */       this.map.put(this.key, newValue);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isWritable() {
/* 513 */       return true;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private class PropertyIndexingValueRef
/*     */     implements ValueRef
/*     */   {
/*     */     private final Object targetObject;
/*     */     
/*     */     private final String name;
/*     */     
/*     */     private final EvaluationContext evaluationContext;
/*     */     
/*     */     private final TypeDescriptor targetObjectTypeDescriptor;
/*     */ 
/*     */     
/*     */     public PropertyIndexingValueRef(Object targetObject, String value, EvaluationContext evaluationContext, TypeDescriptor targetObjectTypeDescriptor) {
/* 531 */       this.targetObject = targetObject;
/* 532 */       this.name = value;
/* 533 */       this.evaluationContext = evaluationContext;
/* 534 */       this.targetObjectTypeDescriptor = targetObjectTypeDescriptor;
/*     */     }
/*     */ 
/*     */     
/*     */     public TypedValue getValue() {
/* 539 */       Class<?> targetObjectRuntimeClass = Indexer.this.getObjectClass(this.targetObject);
/*     */       try {
/* 541 */         if (Indexer.this.cachedReadName != null && Indexer.this.cachedReadName.equals(this.name) && Indexer.this
/* 542 */           .cachedReadTargetType != null && Indexer.this
/* 543 */           .cachedReadTargetType.equals(targetObjectRuntimeClass))
/*     */         {
/* 545 */           return Indexer.this.cachedReadAccessor.read(this.evaluationContext, this.targetObject, this.name);
/*     */         }
/* 547 */         List<PropertyAccessor> accessorsToTry = AstUtils.getPropertyAccessorsToTry(targetObjectRuntimeClass, this.evaluationContext
/* 548 */             .getPropertyAccessors());
/* 549 */         if (accessorsToTry != null) {
/* 550 */           for (PropertyAccessor accessor : accessorsToTry) {
/* 551 */             if (accessor.canRead(this.evaluationContext, this.targetObject, this.name)) {
/* 552 */               if (accessor instanceof ReflectivePropertyAccessor) {
/* 553 */                 accessor = ((ReflectivePropertyAccessor)accessor).createOptimalAccessor(this.evaluationContext, this.targetObject, this.name);
/*     */               }
/*     */               
/* 556 */               Indexer.this.cachedReadAccessor = accessor;
/* 557 */               Indexer.this.cachedReadName = this.name;
/* 558 */               Indexer.this.cachedReadTargetType = targetObjectRuntimeClass;
/* 559 */               if (accessor instanceof ReflectivePropertyAccessor.OptimalPropertyAccessor) {
/* 560 */                 ReflectivePropertyAccessor.OptimalPropertyAccessor optimalAccessor = (ReflectivePropertyAccessor.OptimalPropertyAccessor)accessor;
/*     */                 
/* 562 */                 Member member = optimalAccessor.member;
/* 563 */                 Indexer.this.exitTypeDescriptor = CodeFlow.toDescriptor((member instanceof Method) ? ((Method)member)
/* 564 */                     .getReturnType() : ((Field)member).getType());
/*     */               } 
/* 566 */               return accessor.read(this.evaluationContext, this.targetObject, this.name);
/*     */             }
/*     */           
/*     */           } 
/*     */         }
/* 571 */       } catch (AccessException ex) {
/* 572 */         throw new SpelEvaluationException(Indexer.this.getStartPosition(), ex, SpelMessage.INDEXING_NOT_SUPPORTED_FOR_TYPE, new Object[] { this.targetObjectTypeDescriptor
/* 573 */               .toString() });
/*     */       } 
/* 575 */       throw new SpelEvaluationException(Indexer.this.getStartPosition(), SpelMessage.INDEXING_NOT_SUPPORTED_FOR_TYPE, new Object[] { this.targetObjectTypeDescriptor
/* 576 */             .toString() });
/*     */     }
/*     */ 
/*     */     
/*     */     public void setValue(Object newValue) {
/* 581 */       Class<?> contextObjectClass = Indexer.this.getObjectClass(this.targetObject);
/*     */       try {
/* 583 */         if (Indexer.this.cachedWriteName != null && Indexer.this.cachedWriteName.equals(this.name) && Indexer.this
/* 584 */           .cachedWriteTargetType != null && Indexer.this
/* 585 */           .cachedWriteTargetType.equals(contextObjectClass)) {
/*     */           
/* 587 */           Indexer.this.cachedWriteAccessor.write(this.evaluationContext, this.targetObject, this.name, newValue);
/*     */           
/*     */           return;
/*     */         } 
/* 591 */         List<PropertyAccessor> accessorsToTry = AstUtils.getPropertyAccessorsToTry(contextObjectClass, this.evaluationContext.getPropertyAccessors());
/* 592 */         if (accessorsToTry != null) {
/* 593 */           for (PropertyAccessor accessor : accessorsToTry) {
/* 594 */             if (accessor.canWrite(this.evaluationContext, this.targetObject, this.name)) {
/* 595 */               Indexer.this.cachedWriteName = this.name;
/* 596 */               Indexer.this.cachedWriteTargetType = contextObjectClass;
/* 597 */               Indexer.this.cachedWriteAccessor = accessor;
/* 598 */               accessor.write(this.evaluationContext, this.targetObject, this.name, newValue);
/*     */               
/*     */               return;
/*     */             } 
/*     */           } 
/*     */         }
/* 604 */       } catch (AccessException ex) {
/* 605 */         throw new SpelEvaluationException(Indexer.this.getStartPosition(), ex, SpelMessage.EXCEPTION_DURING_PROPERTY_WRITE, new Object[] { this.name, ex
/* 606 */               .getMessage() });
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isWritable() {
/* 612 */       return true;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class CollectionIndexingValueRef
/*     */     implements ValueRef
/*     */   {
/*     */     private final Collection collection;
/*     */     
/*     */     private final int index;
/*     */     
/*     */     private final TypeDescriptor collectionEntryDescriptor;
/*     */     
/*     */     private final TypeConverter typeConverter;
/*     */     
/*     */     private final boolean growCollection;
/*     */     
/*     */     private final int maximumSize;
/*     */ 
/*     */     
/*     */     public CollectionIndexingValueRef(Collection collection, int index, TypeDescriptor collectionEntryDescriptor, TypeConverter typeConverter, boolean growCollection, int maximumSize) {
/* 635 */       this.collection = collection;
/* 636 */       this.index = index;
/* 637 */       this.collectionEntryDescriptor = collectionEntryDescriptor;
/* 638 */       this.typeConverter = typeConverter;
/* 639 */       this.growCollection = growCollection;
/* 640 */       this.maximumSize = maximumSize;
/*     */     }
/*     */ 
/*     */     
/*     */     public TypedValue getValue() {
/* 645 */       growCollectionIfNecessary();
/* 646 */       if (this.collection instanceof List) {
/* 647 */         Object o = ((List)this.collection).get(this.index);
/* 648 */         Indexer.this.exitTypeDescriptor = CodeFlow.toDescriptor(Object.class);
/* 649 */         return new TypedValue(o, this.collectionEntryDescriptor.elementTypeDescriptor(o));
/*     */       } 
/* 651 */       int pos = 0;
/* 652 */       for (Object o : this.collection) {
/* 653 */         if (pos == this.index) {
/* 654 */           return new TypedValue(o, this.collectionEntryDescriptor.elementTypeDescriptor(o));
/*     */         }
/* 656 */         pos++;
/*     */       } 
/* 658 */       throw new IllegalStateException("Failed to find indexed element " + this.index + ": " + this.collection);
/*     */     }
/*     */ 
/*     */     
/*     */     public void setValue(Object newValue) {
/* 663 */       growCollectionIfNecessary();
/* 664 */       if (this.collection instanceof List) {
/* 665 */         List<Object> list = (List)this.collection;
/* 666 */         if (this.collectionEntryDescriptor.getElementTypeDescriptor() != null) {
/* 667 */           newValue = this.typeConverter.convertValue(newValue, TypeDescriptor.forObject(newValue), this.collectionEntryDescriptor
/* 668 */               .getElementTypeDescriptor());
/*     */         }
/* 670 */         list.set(this.index, newValue);
/*     */       } else {
/*     */         
/* 673 */         throw new SpelEvaluationException(Indexer.this.getStartPosition(), SpelMessage.INDEXING_NOT_SUPPORTED_FOR_TYPE, new Object[] { this.collectionEntryDescriptor
/* 674 */               .toString() });
/*     */       } 
/*     */     }
/*     */     
/*     */     private void growCollectionIfNecessary() {
/* 679 */       if (this.index >= this.collection.size()) {
/* 680 */         if (!this.growCollection)
/* 681 */           throw new SpelEvaluationException(Indexer.this.getStartPosition(), SpelMessage.COLLECTION_INDEX_OUT_OF_BOUNDS, new Object[] {
/* 682 */                 Integer.valueOf(this.collection.size()), Integer.valueOf(this.index)
/*     */               }); 
/* 684 */         if (this.index >= this.maximumSize) {
/* 685 */           throw new SpelEvaluationException(Indexer.this.getStartPosition(), SpelMessage.UNABLE_TO_GROW_COLLECTION, new Object[0]);
/*     */         }
/* 687 */         if (this.collectionEntryDescriptor.getElementTypeDescriptor() == null) {
/* 688 */           throw new SpelEvaluationException(Indexer.this
/* 689 */               .getStartPosition(), SpelMessage.UNABLE_TO_GROW_COLLECTION_UNKNOWN_ELEMENT_TYPE, new Object[0]);
/*     */         }
/* 691 */         TypeDescriptor elementType = this.collectionEntryDescriptor.getElementTypeDescriptor();
/*     */         try {
/* 693 */           int newElements = this.index - this.collection.size();
/* 694 */           while (newElements >= 0) {
/* 695 */             this.collection.add(elementType.getType().newInstance());
/* 696 */             newElements--;
/*     */           }
/*     */         
/* 699 */         } catch (Throwable ex) {
/* 700 */           throw new SpelEvaluationException(Indexer.this.getStartPosition(), ex, SpelMessage.UNABLE_TO_GROW_COLLECTION, new Object[0]);
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isWritable() {
/* 707 */       return true;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private class StringIndexingLValue
/*     */     implements ValueRef
/*     */   {
/*     */     private final String target;
/*     */     
/*     */     private final int index;
/*     */     private final TypeDescriptor typeDescriptor;
/*     */     
/*     */     public StringIndexingLValue(String target, int index, TypeDescriptor typeDescriptor) {
/* 721 */       this.target = target;
/* 722 */       this.index = index;
/* 723 */       this.typeDescriptor = typeDescriptor;
/*     */     }
/*     */ 
/*     */     
/*     */     public TypedValue getValue() {
/* 728 */       if (this.index >= this.target.length())
/* 729 */         throw new SpelEvaluationException(Indexer.this.getStartPosition(), SpelMessage.STRING_INDEX_OUT_OF_BOUNDS, new Object[] {
/* 730 */               Integer.valueOf(this.target.length()), Integer.valueOf(this.index)
/*     */             }); 
/* 732 */       return new TypedValue(String.valueOf(this.target.charAt(this.index)));
/*     */     }
/*     */ 
/*     */     
/*     */     public void setValue(Object newValue) {
/* 737 */       throw new SpelEvaluationException(Indexer.this.getStartPosition(), SpelMessage.INDEXING_NOT_SUPPORTED_FOR_TYPE, new Object[] { this.typeDescriptor
/* 738 */             .toString() });
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isWritable() {
/* 743 */       return true;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\expression\spel\ast\Indexer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */