package org.springframework.context.annotation;

import org.springframework.beans.factory.config.BeanDefinition;

public interface ScopeMetadataResolver {
  ScopeMetadata resolveScopeMetadata(BeanDefinition paramBeanDefinition);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\annotation\ScopeMetadataResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */