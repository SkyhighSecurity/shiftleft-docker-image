package org.springframework.beans.factory.support;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;

public interface InstantiationStrategy {
  Object instantiate(RootBeanDefinition paramRootBeanDefinition, String paramString, BeanFactory paramBeanFactory) throws BeansException;
  
  Object instantiate(RootBeanDefinition paramRootBeanDefinition, String paramString, BeanFactory paramBeanFactory, Constructor<?> paramConstructor, Object... paramVarArgs) throws BeansException;
  
  Object instantiate(RootBeanDefinition paramRootBeanDefinition, String paramString, BeanFactory paramBeanFactory, Object paramObject, Method paramMethod, Object... paramVarArgs) throws BeansException;
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\support\InstantiationStrategy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */