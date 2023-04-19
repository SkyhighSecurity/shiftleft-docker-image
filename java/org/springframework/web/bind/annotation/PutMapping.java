package org.springframework.web.bind.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.core.annotation.AliasFor;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@RequestMapping(method = {RequestMethod.PUT})
public @interface PutMapping {
  @AliasFor(annotation = RequestMapping.class)
  String name() default "";
  
  @AliasFor(annotation = RequestMapping.class)
  String[] value() default {};
  
  @AliasFor(annotation = RequestMapping.class)
  String[] path() default {};
  
  @AliasFor(annotation = RequestMapping.class)
  String[] params() default {};
  
  @AliasFor(annotation = RequestMapping.class)
  String[] headers() default {};
  
  @AliasFor(annotation = RequestMapping.class)
  String[] consumes() default {};
  
  @AliasFor(annotation = RequestMapping.class)
  String[] produces() default {};
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\bind\annotation\PutMapping.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */