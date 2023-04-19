package org.springframework.web.method.support;

import java.util.Map;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionService;
import org.springframework.web.util.UriComponentsBuilder;

public interface UriComponentsContributor {
  boolean supportsParameter(MethodParameter paramMethodParameter);
  
  void contributeMethodArgument(MethodParameter paramMethodParameter, Object paramObject, UriComponentsBuilder paramUriComponentsBuilder, Map<String, Object> paramMap, ConversionService paramConversionService);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\method\support\UriComponentsContributor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */