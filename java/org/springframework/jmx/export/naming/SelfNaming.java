package org.springframework.jmx.export.naming;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

public interface SelfNaming {
  ObjectName getObjectName() throws MalformedObjectNameException;
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\jmx\export\naming\SelfNaming.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */