package org.springframework.context;

import org.springframework.beans.factory.Aware;
import org.springframework.util.StringValueResolver;

public interface EmbeddedValueResolverAware extends Aware {
  void setEmbeddedValueResolver(StringValueResolver paramStringValueResolver);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\EmbeddedValueResolverAware.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */