package org.springframework.context.event;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.core.annotation.AliasFor;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EventListener {
  @AliasFor("classes")
  Class<?>[] value() default {};
  
  @AliasFor("value")
  Class<?>[] classes() default {};
  
  String condition() default "";
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\event\EventListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */