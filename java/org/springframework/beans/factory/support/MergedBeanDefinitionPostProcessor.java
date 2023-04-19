package org.springframework.beans.factory.support;

import org.springframework.beans.factory.config.BeanPostProcessor;

public interface MergedBeanDefinitionPostProcessor extends BeanPostProcessor {
  void postProcessMergedBeanDefinition(RootBeanDefinition paramRootBeanDefinition, Class<?> paramClass, String paramString);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\support\MergedBeanDefinitionPostProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */