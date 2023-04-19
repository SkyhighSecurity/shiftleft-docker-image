/*     */ package org.springframework.expression.spel.ast;
/*     */ 
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.springframework.expression.EvaluationException;
/*     */ import org.springframework.expression.TypedValue;
/*     */ import org.springframework.expression.spel.ExpressionState;
/*     */ import org.springframework.expression.spel.SpelEvaluationException;
/*     */ import org.springframework.expression.spel.SpelMessage;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Selection
/*     */   extends SpelNodeImpl
/*     */ {
/*     */   public static final int ALL = 0;
/*     */   public static final int FIRST = 1;
/*     */   public static final int LAST = 2;
/*     */   private final int variant;
/*     */   private final boolean nullSafe;
/*     */   
/*     */   public Selection(boolean nullSafe, int variant, int pos, SpelNodeImpl expression) {
/*  62 */     super(pos, new SpelNodeImpl[] { expression });
/*  63 */     Assert.notNull(expression, "Expression must not be null");
/*  64 */     this.nullSafe = nullSafe;
/*  65 */     this.variant = variant;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public TypedValue getValueInternal(ExpressionState state) throws EvaluationException {
/*  71 */     return getValueRef(state).getValue();
/*     */   }
/*     */ 
/*     */   
/*     */   protected ValueRef getValueRef(ExpressionState state) throws EvaluationException {
/*  76 */     TypedValue op = state.getActiveContextObject();
/*  77 */     Object operand = op.getValue();
/*  78 */     SpelNodeImpl selectionCriteria = this.children[0];
/*     */     
/*  80 */     if (operand instanceof Map) {
/*  81 */       Map<?, ?> mapdata = (Map<?, ?>)operand;
/*     */       
/*  83 */       Map<Object, Object> result = new HashMap<Object, Object>();
/*  84 */       Object lastKey = null;
/*     */       
/*  86 */       for (Map.Entry<?, ?> entry : mapdata.entrySet()) {
/*     */         try {
/*  88 */           TypedValue kvPair = new TypedValue(entry);
/*  89 */           state.pushActiveContextObject(kvPair);
/*  90 */           state.enterScope();
/*  91 */           Object val = selectionCriteria.getValueInternal(state).getValue();
/*  92 */           if (val instanceof Boolean) {
/*  93 */             if (((Boolean)val).booleanValue()) {
/*  94 */               if (this.variant == 1) {
/*  95 */                 result.put(entry.getKey(), entry.getValue());
/*  96 */                 return new ValueRef.TypedValueHolderValueRef(new TypedValue(result), this);
/*     */               } 
/*  98 */               result.put(entry.getKey(), entry.getValue());
/*  99 */               lastKey = entry.getKey();
/*     */             } 
/*     */           } else {
/*     */             
/* 103 */             throw new SpelEvaluationException(selectionCriteria.getStartPosition(), SpelMessage.RESULT_OF_SELECTION_CRITERIA_IS_NOT_BOOLEAN, new Object[0]);
/*     */           }
/*     */         
/*     */         } finally {
/*     */           
/* 108 */           state.popActiveContextObject();
/* 109 */           state.exitScope();
/*     */         } 
/*     */       } 
/*     */       
/* 113 */       if ((this.variant == 1 || this.variant == 2) && result.isEmpty()) {
/* 114 */         return new ValueRef.TypedValueHolderValueRef(new TypedValue(null), this);
/*     */       }
/*     */       
/* 117 */       if (this.variant == 2) {
/* 118 */         Map<Object, Object> resultMap = new HashMap<Object, Object>();
/* 119 */         Object lastValue = result.get(lastKey);
/* 120 */         resultMap.put(lastKey, lastValue);
/* 121 */         return new ValueRef.TypedValueHolderValueRef(new TypedValue(resultMap), this);
/*     */       } 
/*     */       
/* 124 */       return new ValueRef.TypedValueHolderValueRef(new TypedValue(result), this);
/*     */     } 
/*     */     
/* 127 */     if (operand instanceof Iterable || ObjectUtils.isArray(operand)) {
/*     */       
/* 129 */       Iterable<?> data = (operand instanceof Iterable) ? (Iterable)operand : Arrays.asList(ObjectUtils.toObjectArray(operand));
/*     */       
/* 131 */       List<Object> result = new ArrayList();
/* 132 */       int index = 0;
/* 133 */       for (Object element : data) {
/*     */         try {
/* 135 */           state.pushActiveContextObject(new TypedValue(element));
/* 136 */           state.enterScope("index", Integer.valueOf(index));
/* 137 */           Object val = selectionCriteria.getValueInternal(state).getValue();
/* 138 */           if (val instanceof Boolean) {
/* 139 */             if (((Boolean)val).booleanValue()) {
/* 140 */               if (this.variant == 1) {
/* 141 */                 return new ValueRef.TypedValueHolderValueRef(new TypedValue(element), this);
/*     */               }
/* 143 */               result.add(element);
/*     */             } 
/*     */           } else {
/*     */             
/* 147 */             throw new SpelEvaluationException(selectionCriteria.getStartPosition(), SpelMessage.RESULT_OF_SELECTION_CRITERIA_IS_NOT_BOOLEAN, new Object[0]);
/*     */           } 
/*     */           
/* 150 */           index++;
/*     */         } finally {
/*     */           
/* 153 */           state.exitScope();
/* 154 */           state.popActiveContextObject();
/*     */         } 
/*     */       } 
/*     */       
/* 158 */       if ((this.variant == 1 || this.variant == 2) && result.isEmpty()) {
/* 159 */         return ValueRef.NullValueRef.INSTANCE;
/*     */       }
/*     */       
/* 162 */       if (this.variant == 2) {
/* 163 */         return new ValueRef.TypedValueHolderValueRef(new TypedValue(result.get(result.size() - 1)), this);
/*     */       }
/*     */       
/* 166 */       if (operand instanceof Iterable) {
/* 167 */         return new ValueRef.TypedValueHolderValueRef(new TypedValue(result), this);
/*     */       }
/*     */       
/* 170 */       Class<?> elementType = ClassUtils.resolvePrimitiveIfNecessary(op
/* 171 */           .getTypeDescriptor().getElementTypeDescriptor().getType());
/* 172 */       Object resultArray = Array.newInstance(elementType, result.size());
/* 173 */       System.arraycopy(result.toArray(), 0, resultArray, 0, result.size());
/* 174 */       return new ValueRef.TypedValueHolderValueRef(new TypedValue(resultArray), this);
/*     */     } 
/* 176 */     if (operand == null) {
/* 177 */       if (this.nullSafe) {
/* 178 */         return ValueRef.NullValueRef.INSTANCE;
/*     */       }
/* 180 */       throw new SpelEvaluationException(getStartPosition(), SpelMessage.INVALID_TYPE_FOR_SELECTION, new Object[] { "null" });
/*     */     } 
/* 182 */     throw new SpelEvaluationException(getStartPosition(), SpelMessage.INVALID_TYPE_FOR_SELECTION, new Object[] { operand
/* 183 */           .getClass().getName() });
/*     */   }
/*     */ 
/*     */   
/*     */   public String toStringAST() {
/* 188 */     StringBuilder sb = new StringBuilder();
/* 189 */     switch (this.variant) {
/*     */       case 0:
/* 191 */         sb.append("?[");
/*     */         break;
/*     */       case 1:
/* 194 */         sb.append("^[");
/*     */         break;
/*     */       case 2:
/* 197 */         sb.append("$[");
/*     */         break;
/*     */     } 
/* 200 */     return sb.append(getChild(0).toStringAST()).append("]").toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\expression\spel\ast\Selection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */