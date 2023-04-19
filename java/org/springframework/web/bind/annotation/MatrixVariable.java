package org.springframework.web.bind.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.core.annotation.AliasFor;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MatrixVariable {
  @AliasFor("name")
  String value() default "";
  
  @AliasFor("value")
  String name() default "";
  
  String pathVar() default "\n\t\t\n\t\t\n\n\t\t\t\t\n";
  
  boolean required() default true;
  
  String defaultValue() default "\n\t\t\n\t\t\n\n\t\t\t\t\n";
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\bind\annotation\MatrixVariable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */