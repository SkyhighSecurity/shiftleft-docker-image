package org.springframework.beans;

public interface Mergeable {
  boolean isMergeEnabled();
  
  Object merge(Object paramObject);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\Mergeable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */