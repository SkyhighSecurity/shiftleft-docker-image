package org.springframework.remoting.rmi;

import java.lang.reflect.InvocationTargetException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import org.springframework.remoting.support.RemoteInvocation;

public interface RmiInvocationHandler extends Remote {
  String getTargetInterfaceName() throws RemoteException;
  
  Object invoke(RemoteInvocation paramRemoteInvocation) throws RemoteException, NoSuchMethodException, IllegalAccessException, InvocationTargetException;
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\remoting\rmi\RmiInvocationHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */