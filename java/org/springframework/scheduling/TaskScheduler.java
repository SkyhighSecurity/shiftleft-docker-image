package org.springframework.scheduling;

import java.util.Date;
import java.util.concurrent.ScheduledFuture;

public interface TaskScheduler {
  ScheduledFuture<?> schedule(Runnable paramRunnable, Trigger paramTrigger);
  
  ScheduledFuture<?> schedule(Runnable paramRunnable, Date paramDate);
  
  ScheduledFuture<?> scheduleAtFixedRate(Runnable paramRunnable, Date paramDate, long paramLong);
  
  ScheduledFuture<?> scheduleAtFixedRate(Runnable paramRunnable, long paramLong);
  
  ScheduledFuture<?> scheduleWithFixedDelay(Runnable paramRunnable, Date paramDate, long paramLong);
  
  ScheduledFuture<?> scheduleWithFixedDelay(Runnable paramRunnable, long paramLong);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\scheduling\TaskScheduler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */