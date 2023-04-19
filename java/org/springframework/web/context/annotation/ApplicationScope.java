package org.springframework.web.context.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.annotation.AliasFor;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Scope("application")
public @interface ApplicationScope {
  @AliasFor(annotation = Scope.class)
  ScopedProxyMode proxyMode() default ScopedProxyMode.TARGET_CLASS;
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\context\annotation\ApplicationScope.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */