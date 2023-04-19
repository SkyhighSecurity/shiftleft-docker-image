package org.springframework.web.bind.support;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.context.request.NativeWebRequest;

public interface WebDataBinderFactory {
  WebDataBinder createBinder(NativeWebRequest paramNativeWebRequest, Object paramObject, String paramString) throws Exception;
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\bind\support\WebDataBinderFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */