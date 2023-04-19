package lombok;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
public @interface RequiredArgsConstructor {
  String staticName() default "";
  
  AnyAnnotation[] onConstructor() default {};
  
  AccessLevel access() default AccessLevel.PUBLIC;
  
  @Deprecated
  @Retention(RetentionPolicy.SOURCE)
  @Target({})
  public static @interface AnyAnnotation {}
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\lombok\RequiredArgsConstructor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */