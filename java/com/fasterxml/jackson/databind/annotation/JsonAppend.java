package com.fasterxml.jackson.databind.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotation;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ser.VirtualBeanPropertyWriter;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotation
public @interface JsonAppend {
  Attr[] attrs() default {};
  
  Prop[] props() default {};
  
  boolean prepend() default false;
  
  public static @interface Prop {
    Class<? extends VirtualBeanPropertyWriter> value();
    
    String name() default "";
    
    String namespace() default "";
    
    JsonInclude.Include include() default JsonInclude.Include.NON_NULL;
    
    boolean required() default false;
    
    Class<?> type() default Object.class;
  }
  
  public static @interface Attr {
    String value();
    
    String propName() default "";
    
    String propNamespace() default "";
    
    JsonInclude.Include include() default JsonInclude.Include.NON_NULL;
    
    boolean required() default false;
  }
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\annotation\JsonAppend.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */