package org.springframework.context.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.core.annotation.AliasFor;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Scope {
  @AliasFor("scopeName")
  String value() default "";
  
  @AliasFor("value")
  String scopeName() default "";
  
  ScopedProxyMode proxyMode() default ScopedProxyMode.DEFAULT;
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\annotation\Scope.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */