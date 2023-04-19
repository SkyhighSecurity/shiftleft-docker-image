package org.springframework.remoting.support;

import org.aopalliance.intercept.MethodInvocation;

public interface RemoteInvocationFactory {
  RemoteInvocation createRemoteInvocation(MethodInvocation paramMethodInvocation);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\remoting\support\RemoteInvocationFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */