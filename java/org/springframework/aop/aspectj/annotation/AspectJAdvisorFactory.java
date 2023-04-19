package org.springframework.aop.aspectj.annotation;

import java.lang.reflect.Method;
import java.util.List;
import org.aopalliance.aop.Advice;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.framework.AopConfigException;

public interface AspectJAdvisorFactory {
  boolean isAspect(Class<?> paramClass);
  
  void validate(Class<?> paramClass) throws AopConfigException;
  
  List<Advisor> getAdvisors(MetadataAwareAspectInstanceFactory paramMetadataAwareAspectInstanceFactory);
  
  Advisor getAdvisor(Method paramMethod, MetadataAwareAspectInstanceFactory paramMetadataAwareAspectInstanceFactory, int paramInt, String paramString);
  
  Advice getAdvice(Method paramMethod, AspectJExpressionPointcut paramAspectJExpressionPointcut, MetadataAwareAspectInstanceFactory paramMetadataAwareAspectInstanceFactory, int paramInt, String paramString);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\aspectj\annotation\AspectJAdvisorFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */