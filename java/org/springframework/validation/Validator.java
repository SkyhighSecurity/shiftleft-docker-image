package org.springframework.validation;

public interface Validator {
  boolean supports(Class<?> paramClass);
  
  void validate(Object paramObject, Errors paramErrors);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\validation\Validator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */