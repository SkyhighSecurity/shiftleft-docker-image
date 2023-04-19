/*     */ package org.springframework.expression.common;
/*     */ 
/*     */ import org.springframework.core.convert.TypeDescriptor;
/*     */ import org.springframework.expression.EvaluationContext;
/*     */ import org.springframework.expression.EvaluationException;
/*     */ import org.springframework.expression.Expression;
/*     */ import org.springframework.expression.TypedValue;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CompositeStringExpression
/*     */   implements Expression
/*     */ {
/*     */   private final String expressionString;
/*     */   private final Expression[] expressions;
/*     */   
/*     */   public CompositeStringExpression(String expressionString, Expression[] expressions) {
/*  51 */     this.expressionString = expressionString;
/*  52 */     this.expressions = expressions;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getExpressionString() {
/*  58 */     return this.expressionString;
/*     */   }
/*     */   
/*     */   public final Expression[] getExpressions() {
/*  62 */     return this.expressions;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getValue() throws EvaluationException {
/*  67 */     StringBuilder sb = new StringBuilder();
/*  68 */     for (Expression expression : this.expressions) {
/*  69 */       String value = (String)expression.getValue(String.class);
/*  70 */       if (value != null) {
/*  71 */         sb.append(value);
/*     */       }
/*     */     } 
/*  74 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T getValue(Class<T> expectedResultType) throws EvaluationException {
/*  79 */     Object value = getValue();
/*  80 */     return ExpressionUtils.convertTypedValue(null, new TypedValue(value), expectedResultType);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getValue(Object rootObject) throws EvaluationException {
/*  85 */     StringBuilder sb = new StringBuilder();
/*  86 */     for (Expression expression : this.expressions) {
/*  87 */       String value = (String)expression.getValue(rootObject, String.class);
/*  88 */       if (value != null) {
/*  89 */         sb.append(value);
/*     */       }
/*     */     } 
/*  92 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T getValue(Object rootObject, Class<T> desiredResultType) throws EvaluationException {
/*  97 */     Object value = getValue(rootObject);
/*  98 */     return ExpressionUtils.convertTypedValue(null, new TypedValue(value), desiredResultType);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getValue(EvaluationContext context) throws EvaluationException {
/* 103 */     StringBuilder sb = new StringBuilder();
/* 104 */     for (Expression expression : this.expressions) {
/* 105 */       String value = (String)expression.getValue(context, String.class);
/* 106 */       if (value != null) {
/* 107 */         sb.append(value);
/*     */       }
/*     */     } 
/* 110 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T getValue(EvaluationContext context, Class<T> expectedResultType) throws EvaluationException {
/* 117 */     Object value = getValue(context);
/* 118 */     return ExpressionUtils.convertTypedValue(context, new TypedValue(value), expectedResultType);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getValue(EvaluationContext context, Object rootObject) throws EvaluationException {
/* 123 */     StringBuilder sb = new StringBuilder();
/* 124 */     for (Expression expression : this.expressions) {
/* 125 */       String value = (String)expression.getValue(context, rootObject, String.class);
/* 126 */       if (value != null) {
/* 127 */         sb.append(value);
/*     */       }
/*     */     } 
/* 130 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T getValue(EvaluationContext context, Object rootObject, Class<T> desiredResultType) throws EvaluationException {
/* 137 */     Object value = getValue(context, rootObject);
/* 138 */     return ExpressionUtils.convertTypedValue(context, new TypedValue(value), desiredResultType);
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?> getValueType() {
/* 143 */     return String.class;
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?> getValueType(EvaluationContext context) {
/* 148 */     return String.class;
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?> getValueType(Object rootObject) throws EvaluationException {
/* 153 */     return String.class;
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?> getValueType(EvaluationContext context, Object rootObject) throws EvaluationException {
/* 158 */     return String.class;
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeDescriptor getValueTypeDescriptor() {
/* 163 */     return TypeDescriptor.valueOf(String.class);
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeDescriptor getValueTypeDescriptor(Object rootObject) throws EvaluationException {
/* 168 */     return TypeDescriptor.valueOf(String.class);
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeDescriptor getValueTypeDescriptor(EvaluationContext context) {
/* 173 */     return TypeDescriptor.valueOf(String.class);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypeDescriptor getValueTypeDescriptor(EvaluationContext context, Object rootObject) throws EvaluationException {
/* 180 */     return TypeDescriptor.valueOf(String.class);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isWritable(Object rootObject) throws EvaluationException {
/* 185 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isWritable(EvaluationContext context) {
/* 190 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isWritable(EvaluationContext context, Object rootObject) throws EvaluationException {
/* 195 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setValue(Object rootObject, Object value) throws EvaluationException {
/* 200 */     throw new EvaluationException(this.expressionString, "Cannot call setValue on a composite expression");
/*     */   }
/*     */ 
/*     */   
/*     */   public void setValue(EvaluationContext context, Object value) throws EvaluationException {
/* 205 */     throw new EvaluationException(this.expressionString, "Cannot call setValue on a composite expression");
/*     */   }
/*     */ 
/*     */   
/*     */   public void setValue(EvaluationContext context, Object rootObject, Object value) throws EvaluationException {
/* 210 */     throw new EvaluationException(this.expressionString, "Cannot call setValue on a composite expression");
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\expression\common\CompositeStringExpression.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */