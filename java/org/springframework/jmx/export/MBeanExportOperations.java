package org.springframework.jmx.export;

import javax.management.ObjectName;

public interface MBeanExportOperations {
  ObjectName registerManagedResource(Object paramObject) throws MBeanExportException;
  
  void registerManagedResource(Object paramObject, ObjectName paramObjectName) throws MBeanExportException;
  
  void unregisterManagedResource(ObjectName paramObjectName);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\jmx\export\MBeanExportOperations.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */