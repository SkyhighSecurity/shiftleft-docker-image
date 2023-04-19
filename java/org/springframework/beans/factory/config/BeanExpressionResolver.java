package org.springframework.beans.factory.config;

import org.springframework.beans.BeansException;

public interface BeanExpressionResolver {
  Object evaluate(String paramString, BeanExpressionContext paramBeanExpressionContext) throws BeansException;
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\config\BeanExpressionResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */