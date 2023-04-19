package org.springframework.validation;

public interface SmartValidator extends Validator {
  void validate(Object paramObject, Errors paramErrors, Object... paramVarArgs);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\validation\SmartValidator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */