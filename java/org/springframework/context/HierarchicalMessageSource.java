package org.springframework.context;

public interface HierarchicalMessageSource extends MessageSource {
  void setParentMessageSource(MessageSource paramMessageSource);
  
  MessageSource getParentMessageSource();
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\HierarchicalMessageSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */