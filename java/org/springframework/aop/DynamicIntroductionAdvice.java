package org.springframework.aop;

import org.aopalliance.aop.Advice;

public interface DynamicIntroductionAdvice extends Advice {
  boolean implementsInterface(Class<?> paramClass);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\DynamicIntroductionAdvice.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */