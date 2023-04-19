package org.springframework.jmx.export;

import javax.management.ObjectName;

public interface MBeanExporterListener {
  void mbeanRegistered(ObjectName paramObjectName);
  
  void mbeanUnregistered(ObjectName paramObjectName);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\jmx\export\MBeanExporterListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */