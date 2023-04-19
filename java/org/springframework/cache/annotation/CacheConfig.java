package org.springframework.cache.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CacheConfig {
  String[] cacheNames() default {};
  
  String keyGenerator() default "";
  
  String cacheManager() default "";
  
  String cacheResolver() default "";
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\cache\annotation\CacheConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */