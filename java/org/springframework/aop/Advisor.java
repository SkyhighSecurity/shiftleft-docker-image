package org.springframework.aop;

import org.aopalliance.aop.Advice;

public interface Advisor {
  Advice getAdvice();
  
  boolean isPerInstance();
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\Advisor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */