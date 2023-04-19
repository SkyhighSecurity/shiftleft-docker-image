package org.springframework.aop.aspectj;

import org.springframework.aop.PointcutAdvisor;

public interface InstantiationModelAwarePointcutAdvisor extends PointcutAdvisor {
  boolean isLazy();
  
  boolean isAdviceInstantiated();
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\aspectj\InstantiationModelAwarePointcutAdvisor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */