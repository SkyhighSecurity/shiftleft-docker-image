package org.springframework.scheduling;

import java.util.Date;

public interface Trigger {
  Date nextExecutionTime(TriggerContext paramTriggerContext);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\scheduling\Trigger.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */