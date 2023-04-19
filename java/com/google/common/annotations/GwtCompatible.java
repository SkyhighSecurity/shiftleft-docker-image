package com.google.common.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target({ElementType.TYPE, ElementType.METHOD})
@GwtCompatible
public @interface GwtCompatible {
  boolean serializable() default false;
  
  boolean emulated() default false;
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\google\common\annotations\GwtCompatible.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */