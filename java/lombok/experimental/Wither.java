package lombok.experimental;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import lombok.AccessLevel;

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
public @interface Wither {
  AccessLevel value() default AccessLevel.PUBLIC;
  
  AnyAnnotation[] onMethod() default {};
  
  AnyAnnotation[] onParam() default {};
  
  @Deprecated
  @Retention(RetentionPolicy.SOURCE)
  @Target({})
  public static @interface AnyAnnotation {}
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\lombok\experimental\Wither.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */