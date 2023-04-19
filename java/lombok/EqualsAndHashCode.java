package lombok;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
public @interface EqualsAndHashCode {
  String[] exclude() default {};
  
  String[] of() default {};
  
  boolean callSuper() default false;
  
  boolean doNotUseGetters() default false;
  
  AnyAnnotation[] onParam() default {};
  
  boolean onlyExplicitlyIncluded() default false;
  
  @Deprecated
  @Retention(RetentionPolicy.SOURCE)
  @Target({})
  public static @interface AnyAnnotation {}
  
  @Target({ElementType.FIELD})
  @Retention(RetentionPolicy.SOURCE)
  public static @interface Exclude {}
  
  @Target({ElementType.FIELD, ElementType.METHOD})
  @Retention(RetentionPolicy.SOURCE)
  public static @interface Include {
    String replaces() default "";
  }
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\lombok\EqualsAndHashCode.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */