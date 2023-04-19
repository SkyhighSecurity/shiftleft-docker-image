package org.springframework.remoting.httpinvoker;

import org.springframework.remoting.support.RemoteInvocation;
import org.springframework.remoting.support.RemoteInvocationResult;

public interface HttpInvokerRequestExecutor {
  RemoteInvocationResult executeRequest(HttpInvokerClientConfiguration paramHttpInvokerClientConfiguration, RemoteInvocation paramRemoteInvocation) throws Exception;
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\remoting\httpinvoker\HttpInvokerRequestExecutor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */