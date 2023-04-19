package org.springframework.context;

public interface MessageSourceResolvable {
  String[] getCodes();
  
  Object[] getArguments();
  
  String getDefaultMessage();
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\MessageSourceResolvable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */