package org.springframework.expression;

public interface TypeLocator {
  Class<?> findType(String paramString) throws EvaluationException;
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\expression\TypeLocator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */