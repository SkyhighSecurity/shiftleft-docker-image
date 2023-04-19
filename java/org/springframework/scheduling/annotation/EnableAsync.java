package org.springframework.scheduling.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Import;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({AsyncConfigurationSelector.class})
public @interface EnableAsync {
  Class<? extends Annotation> annotation() default Annotation.class;
  
  boolean proxyTargetClass() default false;
  
  AdviceMode mode() default AdviceMode.PROXY;
  
  int order() default 2147483647;
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\scheduling\annotation\EnableAsync.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */