package org.springframework.expression;

public interface TypeComparator {
  boolean canCompare(Object paramObject1, Object paramObject2);
  
  int compare(Object paramObject1, Object paramObject2) throws EvaluationException;
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\expression\TypeComparator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */