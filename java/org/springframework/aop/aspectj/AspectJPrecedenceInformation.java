package org.springframework.aop.aspectj;

import org.springframework.core.Ordered;

public interface AspectJPrecedenceInformation extends Ordered {
  String getAspectName();
  
  int getDeclarationOrder();
  
  boolean isBeforeAdvice();
  
  boolean isAfterAdvice();
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\aspectj\AspectJPrecedenceInformation.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */