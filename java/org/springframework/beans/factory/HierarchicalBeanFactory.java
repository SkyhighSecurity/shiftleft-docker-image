package org.springframework.beans.factory;

public interface HierarchicalBeanFactory extends BeanFactory {
  BeanFactory getParentBeanFactory();
  
  boolean containsLocalBean(String paramString);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\HierarchicalBeanFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */