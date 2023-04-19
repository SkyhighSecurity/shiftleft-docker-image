package org.springframework.expression.spel;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.EvaluationException;

public abstract class CompiledExpression {
  public abstract Object getValue(Object paramObject, EvaluationContext paramEvaluationContext) throws EvaluationException;
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\expression\spel\CompiledExpression.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */