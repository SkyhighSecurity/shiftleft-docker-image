package org.springframework.validation;

import org.springframework.beans.PropertyAccessException;

public interface BindingErrorProcessor {
  void processMissingFieldError(String paramString, BindingResult paramBindingResult);
  
  void processPropertyAccessException(PropertyAccessException paramPropertyAccessException, BindingResult paramBindingResult);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\validation\BindingErrorProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */