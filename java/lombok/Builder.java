package lombok;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.SOURCE)
public @interface Builder {
  String builderMethodName() default "builder";
  
  String buildMethodName() default "build";
  
  String builderClassName() default "";
  
  boolean toBuilder() default false;
  
  AccessLevel access() default AccessLevel.PUBLIC;
  
  @Target({ElementType.FIELD})
  @Retention(RetentionPolicy.SOURCE)
  public static @interface Default {}
  
  @Target({ElementType.FIELD, ElementType.PARAMETER})
  @Retention(RetentionPolicy.SOURCE)
  public static @interface ObtainVia {
    String field() default "";
    
    String method() default "";
    
    boolean isStatic() default false;
  }
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\lombok\Builder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */