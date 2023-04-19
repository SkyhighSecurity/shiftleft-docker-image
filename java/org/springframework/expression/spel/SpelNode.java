package org.springframework.expression.spel;

import org.springframework.expression.EvaluationException;
import org.springframework.expression.TypedValue;

public interface SpelNode {
  Object getValue(ExpressionState paramExpressionState) throws EvaluationException;
  
  TypedValue getTypedValue(ExpressionState paramExpressionState) throws EvaluationException;
  
  boolean isWritable(ExpressionState paramExpressionState) throws EvaluationException;
  
  void setValue(ExpressionState paramExpressionState, Object paramObject) throws EvaluationException;
  
  String toStringAST();
  
  int getChildCount();
  
  SpelNode getChild(int paramInt);
  
  Class<?> getObjectClass(Object paramObject);
  
  int getStartPosition();
  
  int getEndPosition();
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\expression\spel\SpelNode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */