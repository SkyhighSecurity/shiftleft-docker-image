package org.springframework.aop.framework.adapter;

import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.Advisor;

public interface AdvisorAdapterRegistry {
  Advisor wrap(Object paramObject) throws UnknownAdviceTypeException;
  
  MethodInterceptor[] getInterceptors(Advisor paramAdvisor) throws UnknownAdviceTypeException;
  
  void registerAdvisorAdapter(AdvisorAdapter paramAdvisorAdapter);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\framework\adapter\AdvisorAdapterRegistry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */