package lombok.experimental;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import lombok.AccessLevel;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
public @interface FieldNameConstants {
  AccessLevel level() default AccessLevel.PUBLIC;
  
  boolean asEnum() default false;
  
  String innerTypeName() default "";
  
  boolean onlyExplicitlyIncluded() default false;
  
  @Target({ElementType.FIELD})
  @Retention(RetentionPolicy.SOURCE)
  public static @interface Exclude {}
  
  @Target({ElementType.FIELD})
  @Retention(RetentionPolicy.SOURCE)
  public static @interface Include {}
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\lombok\experimental\FieldNameConstants.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */