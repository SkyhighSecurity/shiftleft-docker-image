package org.springframework.jndi;

import javax.naming.Context;
import javax.naming.NamingException;

public interface JndiCallback<T> {
  T doInContext(Context paramContext) throws NamingException;
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\jndi\JndiCallback.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */