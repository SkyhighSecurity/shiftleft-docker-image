package org.springframework.beans.factory.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Configurable {
  String value() default "";
  
  Autowire autowire() default Autowire.NO;
  
  boolean dependencyCheck() default false;
  
  boolean preConstruction() default false;
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\annotation\Configurable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */