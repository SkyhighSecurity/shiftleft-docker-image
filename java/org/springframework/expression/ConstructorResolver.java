package org.springframework.expression;

import java.util.List;
import org.springframework.core.convert.TypeDescriptor;

public interface ConstructorResolver {
  ConstructorExecutor resolve(EvaluationContext paramEvaluationContext, String paramString, List<TypeDescriptor> paramList) throws AccessException;
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\expression\ConstructorResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */