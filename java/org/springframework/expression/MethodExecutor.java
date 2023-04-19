package org.springframework.expression;

public interface MethodExecutor {
  TypedValue execute(EvaluationContext paramEvaluationContext, Object paramObject, Object... paramVarArgs) throws AccessException;
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\expression\MethodExecutor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */