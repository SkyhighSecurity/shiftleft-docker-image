package org.springframework.context;

import org.springframework.beans.factory.Aware;
import org.springframework.core.env.Environment;

public interface EnvironmentAware extends Aware {
  void setEnvironment(Environment paramEnvironment);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\EnvironmentAware.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */