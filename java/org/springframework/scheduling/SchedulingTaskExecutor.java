package org.springframework.scheduling;

import org.springframework.core.task.AsyncTaskExecutor;

public interface SchedulingTaskExecutor extends AsyncTaskExecutor {
  boolean prefersShortLivedTasks();
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\scheduling\SchedulingTaskExecutor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */