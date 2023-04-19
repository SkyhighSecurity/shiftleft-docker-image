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
/*     */ public class LiteralExpression
/*     */   implements Expression
/*     */ {
/*     */   private final String literalValue;
/*     */   
/*     */   public LiteralExpression(String literalValue) {
/*  42 */     this.literalValue = literalValue;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getExpressionString() {
/*  48 */     return this.literalValue;
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?> getValueType(EvaluationContext context) {
/*  53 */     return String.class;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getValue() {
/*  58 */     return this.literalValue;
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T getValue(Class<T> expectedResultType) throws EvaluationException {
/*  63 */     Object value = getValue();
/*  64 */     return ExpressionUtils.convertTypedValue(null, new TypedValue(value), expectedResultType);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getValue(Object rootObject) {
/*  69 */     return this.literalValue;
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T getValue(Object rootObject, Class<T> desiredResultType) throws EvaluationException {
/*  74 */     Object value = getValue(rootObject);
/*  75 */     return ExpressionUtils.convertTypedValue(null, new TypedValue(value), desiredResultType);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getValue(EvaluationContext context) {
/*  80 */     return this.literalValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T getValue(EvaluationContext context, Class<T> expectedResultType) throws EvaluationException {
/*  87 */     Object value = getValue(context);
/*  88 */     return ExpressionUtils.convertTypedValue(context, new TypedValue(value), expectedResultType);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getValue(EvaluationContext context, Object rootObject) throws EvaluationException {
/*  93 */     return this.literalValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T getValue(EvaluationContext context, Object rootObject, Class<T> desiredResultType) throws EvaluationException {
/* 100 */     Object value = getValue(context, rootObject);
/* 101 */     return ExpressionUtils.convertTypedValue(context, new TypedValue(value), desiredResultType);
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?> getValueType() {
/* 106 */     return String.class;
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?> getValueType(Object rootObject) throws EvaluationException {
/* 111 */     return String.class;
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?> getValueType(EvaluationContext context, Object rootObject) throws EvaluationException {
/* 116 */     return String.class;
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeDescriptor getValueTypeDescriptor() {
/* 121 */     return TypeDescriptor.valueOf(String.class);
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeDescriptor getValueTypeDescriptor(Object rootObject) throws EvaluationException {
/* 126 */     return TypeDescriptor.valueOf(String.class);
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeDescriptor getValueTypeDescriptor(EvaluationContext context) {
/* 131 */     return TypeDescriptor.valueOf(String.class);
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeDescriptor getValueTypeDescriptor(EvaluationContext context, Object rootObject) throws EvaluationException {
/* 136 */     return TypeDescriptor.valueOf(String.class);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isWritable(Object rootObject) throws EvaluationException {
/* 141 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isWritable(EvaluationContext context) {
/* 146 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isWritable(EvaluationContext context, Object rootObject) throws EvaluationException {
/* 151 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setValue(Object rootObject, Object value) throws EvaluationException {
/* 156 */     throw new EvaluationException(this.literalValue, "Cannot call setValue() on a LiteralExpression");
/*     */   }
/*     */ 
/*     */   
/*     */   public void setValue(EvaluationContext context, Object value) throws EvaluationException {
/* 161 */     throw new EvaluationException(this.literalValue, "Cannot call setValue() on a LiteralExpression");
/*     */   }
/*     */ 
/*     */   
/*     */   public void setValue(EvaluationContext context, Object rootObject, Object value) throws EvaluationException {
/* 166 */     throw new EvaluationException(this.literalValue, "Cannot call setValue() on a LiteralExpression");
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\expression\common\LiteralExpression.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */