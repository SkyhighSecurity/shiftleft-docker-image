package org.springframework.expression;

import java.util.List;
import org.springframework.core.convert.TypeDescriptor;

public interface MethodResolver {
  MethodExecutor resolve(EvaluationContext paramEvaluationContext, Object paramObject, String paramString, List<TypeDescriptor> paramList) throws AccessException;
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\expression\MethodResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */