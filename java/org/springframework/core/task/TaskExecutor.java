package org.springframework.core.task;

import java.util.concurrent.Executor;

public interface TaskExecutor extends Executor {
  void execute(Runnable paramRunnable);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\task\TaskExecutor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */