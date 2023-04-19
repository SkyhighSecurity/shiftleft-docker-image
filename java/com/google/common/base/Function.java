package com.google.common.base;

import com.google.common.annotations.GwtCompatible;
import javax.annotation.Nullable;

@GwtCompatible
public interface Function<F, T> {
  T apply(@Nullable F paramF);
  
  boolean equals(@Nullable Object paramObject);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\google\common\base\Function.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */