package org.springframework.expression;

public interface PropertyAccessor {
  Class<?>[] getSpecificTargetClasses();
  
  boolean canRead(EvaluationContext paramEvaluationContext, Object paramObject, String paramString) throws AccessException;
  
  TypedValue read(EvaluationContext paramEvaluationContext, Object paramObject, String paramString) throws AccessException;
  
  boolean canWrite(EvaluationContext paramEvaluationContext, Object paramObject, String paramString) throws AccessException;
  
  void write(EvaluationContext paramEvaluationContext, Object paramObject1, String paramString, Object paramObject2) throws AccessException;
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\expression\PropertyAccessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */