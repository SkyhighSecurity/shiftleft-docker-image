package org.springframework.jmx.export.metadata;

import java.lang.reflect.Method;

public interface JmxAttributeSource {
  ManagedResource getManagedResource(Class<?> paramClass) throws InvalidMetadataException;
  
  ManagedAttribute getManagedAttribute(Method paramMethod) throws InvalidMetadataException;
  
  ManagedMetric getManagedMetric(Method paramMethod) throws InvalidMetadataException;
  
  ManagedOperation getManagedOperation(Method paramMethod) throws InvalidMetadataException;
  
  ManagedOperationParameter[] getManagedOperationParameters(Method paramMethod) throws InvalidMetadataException;
  
  ManagedNotification[] getManagedNotifications(Class<?> paramClass) throws InvalidMetadataException;
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\jmx\export\metadata\JmxAttributeSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */