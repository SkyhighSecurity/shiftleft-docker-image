package org.springframework.expression;

import java.util.List;

public interface EvaluationContext {
  TypedValue getRootObject();
  
  List<PropertyAccessor> getPropertyAccessors();
  
  List<ConstructorResolver> getConstructorResolvers();
  
  List<MethodResolver> getMethodResolvers();
  
  BeanResolver getBeanResolver();
  
  TypeLocator getTypeLocator();
  
  TypeConverter getTypeConverter();
  
  TypeComparator getTypeComparator();
  
  OperatorOverloader getOperatorOverloader();
  
  void setVariable(String paramString, Object paramObject);
  
  Object lookupVariable(String paramString);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\expression\EvaluationContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */