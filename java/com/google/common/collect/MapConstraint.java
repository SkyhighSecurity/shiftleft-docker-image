package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import javax.annotation.Nullable;

@GwtCompatible
interface MapConstraint<K, V> {
  void checkKeyValue(@Nullable K paramK, @Nullable V paramV);
  
  String toString();
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\google\common\collect\MapConstraint.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */