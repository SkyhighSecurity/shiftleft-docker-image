package org.springframework.context.annotation;

import org.springframework.core.type.AnnotatedTypeMetadata;

public interface Condition {
  boolean matches(ConditionContext paramConditionContext, AnnotatedTypeMetadata paramAnnotatedTypeMetadata);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\annotation\Condition.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */