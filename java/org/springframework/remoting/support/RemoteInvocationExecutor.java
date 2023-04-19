package org.springframework.remoting.support;

import java.lang.reflect.InvocationTargetException;

public interface RemoteInvocationExecutor {
  Object invoke(RemoteInvocation paramRemoteInvocation, Object paramObject) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException;
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\remoting\support\RemoteInvocationExecutor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */