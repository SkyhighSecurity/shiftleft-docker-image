package lombok.extern.apachecommons;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.TYPE})
public @interface CommonsLog {
  String topic() default "";
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\lombok\extern\apachecommons\CommonsLog.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */