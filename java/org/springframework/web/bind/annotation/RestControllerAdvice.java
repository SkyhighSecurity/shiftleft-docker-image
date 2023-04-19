package org.springframework.web.bind.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.core.annotation.AliasFor;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ControllerAdvice
@ResponseBody
public @interface RestControllerAdvice {
  @AliasFor("basePackages")
  String[] value() default {};
  
  @AliasFor("value")
  String[] basePackages() default {};
  
  Class<?>[] basePackageClasses() default {};
  
  Class<?>[] assignableTypes() default {};
  
  Class<? extends Annotation>[] annotations() default {};
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\bind\annotation\RestControllerAdvice.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */