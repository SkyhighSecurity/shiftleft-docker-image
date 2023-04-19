/*     */ package org.springframework.expression.spel.ast;
/*     */ 
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.springframework.expression.EvaluationException;
/*     */ import org.springframework.expression.TypedValue;
/*     */ import org.springframework.expression.spel.ExpressionState;
/*     */ import org.springframework.expression.spel.SpelEvaluationException;
/*     */ import org.springframework.expression.spel.SpelMessage;
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
/*     */ public class Projection
/*     */   extends SpelNodeImpl
/*     */ {
/*     */   private final boolean nullSafe;
/*     */   
/*     */   public Projection(boolean nullSafe, int pos, SpelNodeImpl expression) {
/*  49 */     super(pos, new SpelNodeImpl[] { expression });
/*  50 */     this.nullSafe = nullSafe;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public TypedValue getValueInternal(ExpressionState state) throws EvaluationException {
/*  56 */     return getValueRef(state).getValue();
/*     */   }
/*     */ 
/*     */   
/*     */   protected ValueRef getValueRef(ExpressionState state) throws EvaluationException {
/*  61 */     TypedValue op = state.getActiveContextObject();
/*     */     
/*  63 */     Object operand = op.getValue();
/*  64 */     boolean operandIsArray = ObjectUtils.isArray(operand);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  72 */     if (operand instanceof Map) {
/*  73 */       Map<?, ?> mapData = (Map<?, ?>)operand;
/*  74 */       List<Object> result = new ArrayList();
/*  75 */       for (Map.Entry<?, ?> entry : mapData.entrySet()) {
/*     */         try {
/*  77 */           state.pushActiveContextObject(new TypedValue(entry));
/*  78 */           state.enterScope();
/*  79 */           result.add(this.children[0].getValueInternal(state).getValue());
/*     */         } finally {
/*     */           
/*  82 */           state.popActiveContextObject();
/*  83 */           state.exitScope();
/*     */         } 
/*     */       } 
/*  86 */       return new ValueRef.TypedValueHolderValueRef(new TypedValue(result), this);
/*     */     } 
/*     */     
/*  89 */     if (operand instanceof Iterable || operandIsArray) {
/*     */       
/*  91 */       Iterable<?> data = (operand instanceof Iterable) ? (Iterable)operand : Arrays.asList(ObjectUtils.toObjectArray(operand));
/*     */       
/*  93 */       List<Object> result = new ArrayList();
/*  94 */       int idx = 0;
/*  95 */       Class<?> arrayElementType = null;
/*  96 */       for (Object element : data) {
/*     */         try {
/*  98 */           state.pushActiveContextObject(new TypedValue(element));
/*  99 */           state.enterScope("index", Integer.valueOf(idx));
/* 100 */           Object value = this.children[0].getValueInternal(state).getValue();
/* 101 */           if (value != null && operandIsArray) {
/* 102 */             arrayElementType = determineCommonType(arrayElementType, value.getClass());
/*     */           }
/* 104 */           result.add(value);
/*     */         } finally {
/*     */           
/* 107 */           state.exitScope();
/* 108 */           state.popActiveContextObject();
/*     */         } 
/* 110 */         idx++;
/*     */       } 
/*     */       
/* 113 */       if (operandIsArray) {
/* 114 */         if (arrayElementType == null) {
/* 115 */           arrayElementType = Object.class;
/*     */         }
/* 117 */         Object resultArray = Array.newInstance(arrayElementType, result.size());
/* 118 */         System.arraycopy(result.toArray(), 0, resultArray, 0, result.size());
/* 119 */         return new ValueRef.TypedValueHolderValueRef(new TypedValue(resultArray), this);
/*     */       } 
/*     */       
/* 122 */       return new ValueRef.TypedValueHolderValueRef(new TypedValue(result), this);
/*     */     } 
/*     */     
/* 125 */     if (operand == null) {
/* 126 */       if (this.nullSafe) {
/* 127 */         return ValueRef.NullValueRef.INSTANCE;
/*     */       }
/* 129 */       throw new SpelEvaluationException(getStartPosition(), SpelMessage.PROJECTION_NOT_SUPPORTED_ON_TYPE, new Object[] { "null" });
/*     */     } 
/*     */     
/* 132 */     throw new SpelEvaluationException(getStartPosition(), SpelMessage.PROJECTION_NOT_SUPPORTED_ON_TYPE, new Object[] { operand
/* 133 */           .getClass().getName() });
/*     */   }
/*     */ 
/*     */   
/*     */   public String toStringAST() {
/* 138 */     return "![" + getChild(0).toStringAST() + "]";
/*     */   }
/*     */   
/*     */   private Class<?> determineCommonType(Class<?> oldType, Class<?> newType) {
/* 142 */     if (oldType == null) {
/* 143 */       return newType;
/*     */     }
/* 145 */     if (oldType.isAssignableFrom(newType)) {
/* 146 */       return oldType;
/*     */     }
/* 148 */     Class<?> nextType = newType;
/* 149 */     while (nextType != Object.class) {
/* 150 */       if (nextType.isAssignableFrom(oldType)) {
/* 151 */         return nextType;
/*     */       }
/* 153 */       nextType = nextType.getSuperclass();
/*     */     } 
/* 155 */     for (Class<?> nextInterface : (Iterable<Class<?>>)ClassUtils.getAllInterfacesForClassAsSet(newType)) {
/* 156 */       if (nextInterface.isAssignableFrom(oldType)) {
/* 157 */         return nextInterface;
/*     */       }
/*     */     } 
/* 160 */     return Object.class;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\expression\spel\ast\Projection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */