package org.springframework.expression;

import org.springframework.core.convert.TypeDescriptor;

public interface Expression {
  String getExpressionString();
  
  Object getValue() throws EvaluationException;
  
  <T> T getValue(Class<T> paramClass) throws EvaluationException;
  
  Object getValue(Object paramObject) throws EvaluationException;
  
  <T> T getValue(Object paramObject, Class<T> paramClass) throws EvaluationException;
  
  Object getValue(EvaluationContext paramEvaluationContext) throws EvaluationException;
  
  Object getValue(EvaluationContext paramEvaluationContext, Object paramObject) throws EvaluationException;
  
  <T> T getValue(EvaluationContext paramEvaluationContext, Class<T> paramClass) throws EvaluationException;
  
  <T> T getValue(EvaluationContext paramEvaluationContext, Object paramObject, Class<T> paramClass) throws EvaluationException;
  
  Class<?> getValueType() throws EvaluationException;
  
  Class<?> getValueType(Object paramObject) throws EvaluationException;
  
  Class<?> getValueType(EvaluationContext paramEvaluationContext) throws EvaluationException;
  
  Class<?> getValueType(EvaluationContext paramEvaluationContext, Object paramObject) throws EvaluationException;
  
  TypeDescriptor getValueTypeDescriptor() throws EvaluationException;
  
  TypeDescriptor getValueTypeDescriptor(Object paramObject) throws EvaluationException;
  
  TypeDescriptor getValueTypeDescriptor(EvaluationContext paramEvaluationContext) throws EvaluationException;
  
  TypeDescriptor getValueTypeDescriptor(EvaluationContext paramEvaluationContext, Object paramObject) throws EvaluationException;
  
  boolean isWritable(Object paramObject) throws EvaluationException;
  
  boolean isWritable(EvaluationContext paramEvaluationContext) throws EvaluationException;
  
  boolean isWritable(EvaluationContext paramEvaluationContext, Object paramObject) throws EvaluationException;
  
  void setValue(Object paramObject1, Object paramObject2) throws EvaluationException;
  
  void setValue(EvaluationContext paramEvaluationContext, Object paramObject) throws EvaluationException;
  
  void setValue(EvaluationContext paramEvaluationContext, Object paramObject1, Object paramObject2) throws EvaluationException;
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\expression\Expression.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */