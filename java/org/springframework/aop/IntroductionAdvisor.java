package org.springframework.aop;

public interface IntroductionAdvisor extends Advisor, IntroductionInfo {
  ClassFilter getClassFilter();
  
  void validateInterfaces() throws IllegalArgumentException;
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\IntroductionAdvisor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */