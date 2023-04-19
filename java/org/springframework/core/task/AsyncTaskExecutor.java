package org.springframework.core.task;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public interface AsyncTaskExecutor extends TaskExecutor {
  public static final long TIMEOUT_IMMEDIATE = 0L;
  
  public static final long TIMEOUT_INDEFINITE = 9223372036854775807L;
  
  void execute(Runnable paramRunnable, long paramLong);
  
  Future<?> submit(Runnable paramRunnable);
  
  <T> Future<T> submit(Callable<T> paramCallable);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\task\AsyncTaskExecutor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */