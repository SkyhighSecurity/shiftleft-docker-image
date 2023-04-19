package lombok.experimental;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
public @interface ExtensionMethod {
  Class<?>[] value();
  
  boolean suppressBaseMethods() default true;
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\lombok\experimental\ExtensionMethod.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */