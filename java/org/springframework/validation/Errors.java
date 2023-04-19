package org.springframework.validation;

import java.util.List;

public interface Errors {
  public static final String NESTED_PATH_SEPARATOR = ".";
  
  String getObjectName();
  
  void setNestedPath(String paramString);
  
  String getNestedPath();
  
  void pushNestedPath(String paramString);
  
  void popNestedPath() throws IllegalStateException;
  
  void reject(String paramString);
  
  void reject(String paramString1, String paramString2);
  
  void reject(String paramString1, Object[] paramArrayOfObject, String paramString2);
  
  void rejectValue(String paramString1, String paramString2);
  
  void rejectValue(String paramString1, String paramString2, String paramString3);
  
  void rejectValue(String paramString1, String paramString2, Object[] paramArrayOfObject, String paramString3);
  
  void addAllErrors(Errors paramErrors);
  
  boolean hasErrors();
  
  int getErrorCount();
  
  List<ObjectError> getAllErrors();
  
  boolean hasGlobalErrors();
  
  int getGlobalErrorCount();
  
  List<ObjectError> getGlobalErrors();
  
  ObjectError getGlobalError();
  
  boolean hasFieldErrors();
  
  int getFieldErrorCount();
  
  List<FieldError> getFieldErrors();
  
  FieldError getFieldError();
  
  boolean hasFieldErrors(String paramString);
  
  int getFieldErrorCount(String paramString);
  
  List<FieldError> getFieldErrors(String paramString);
  
  FieldError getFieldError(String paramString);
  
  Object getFieldValue(String paramString);
  
  Class<?> getFieldType(String paramString);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\validation\Errors.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */