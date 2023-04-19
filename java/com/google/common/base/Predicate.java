package com.google.common.base;

import com.google.common.annotations.GwtCompatible;
import javax.annotation.Nullable;

@GwtCompatible
public interface Predicate<T> {
  boolean apply(@Nullable T paramT);
  
  boolean equals(@Nullable Object paramObject);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\google\common\base\Predicate.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */