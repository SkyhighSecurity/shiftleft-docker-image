package com.fasterxml.jackson.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotation
public @interface JsonIdentityInfo {
  String property() default "@id";
  
  Class<? extends ObjectIdGenerator<?>> generator();
  
  Class<? extends ObjectIdResolver> resolver() default SimpleObjectIdResolver.class;
  
  Class<?> scope() default Object.class;
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\annotation\JsonIdentityInfo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */