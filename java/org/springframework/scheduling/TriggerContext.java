package org.springframework.scheduling;

import java.util.Date;

public interface TriggerContext {
  Date lastScheduledExecutionTime();
  
  Date lastActualExecutionTime();
  
  Date lastCompletionTime();
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\scheduling\TriggerContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */