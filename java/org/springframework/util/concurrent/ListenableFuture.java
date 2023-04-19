package org.springframework.util.concurrent;

import java.util.concurrent.Future;

public interface ListenableFuture<T> extends Future<T> {
  void addCallback(ListenableFutureCallback<? super T> paramListenableFutureCallback);
  
  void addCallback(SuccessCallback<? super T> paramSuccessCallback, FailureCallback paramFailureCallback);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframewor\\util\concurrent\ListenableFuture.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */