package lombok.extern.log4j;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.TYPE})
public @interface Log4j2 {
  String topic() default "";
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\lombok\extern\log4j\Log4j2.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */