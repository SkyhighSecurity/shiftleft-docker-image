package org.springframework.aop.framework;

import java.lang.reflect.Method;
import java.util.List;

public interface AdvisorChainFactory {
  List<Object> getInterceptorsAndDynamicInterceptionAdvice(Advised paramAdvised, Method paramMethod, Class<?> paramClass);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\framework\AdvisorChainFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */