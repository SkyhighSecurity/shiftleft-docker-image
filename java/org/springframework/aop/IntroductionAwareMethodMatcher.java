package org.springframework.aop;

import java.lang.reflect.Method;

public interface IntroductionAwareMethodMatcher extends MethodMatcher {
  boolean matches(Method paramMethod, Class<?> paramClass, boolean paramBoolean);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\IntroductionAwareMethodMatcher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */