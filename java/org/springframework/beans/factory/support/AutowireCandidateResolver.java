package org.springframework.beans.factory.support;

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.DependencyDescriptor;

public interface AutowireCandidateResolver {
  boolean isAutowireCandidate(BeanDefinitionHolder paramBeanDefinitionHolder, DependencyDescriptor paramDependencyDescriptor);
  
  Object getSuggestedValue(DependencyDescriptor paramDependencyDescriptor);
  
  Object getLazyResolutionProxyIfNecessary(DependencyDescriptor paramDependencyDescriptor, String paramString);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\support\AutowireCandidateResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */