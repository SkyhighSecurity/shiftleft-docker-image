package lombok;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
public @interface ToString {
  boolean includeFieldNames() default true;
  
  String[] exclude() default {};
  
  String[] of() default {};
  
  boolean callSuper() default false;
  
  boolean doNotUseGetters() default false;
  
  boolean onlyExplicitlyIncluded() default false;
  
  @Target({ElementType.FIELD})
  @Retention(RetentionPolicy.SOURCE)
  public static @interface Exclude {}
  
  @Target({ElementType.FIELD, ElementType.METHOD})
  @Retention(RetentionPolicy.SOURCE)
  public static @interface Include {
    int rank() default 0;
    
    String name() default "";
  }
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\lombok\ToString.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */