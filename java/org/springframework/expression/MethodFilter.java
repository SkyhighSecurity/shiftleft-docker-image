package org.springframework.expression;

import java.lang.reflect.Method;
import java.util.List;

public interface MethodFilter {
  List<Method> filter(List<Method> paramList);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\expression\MethodFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */