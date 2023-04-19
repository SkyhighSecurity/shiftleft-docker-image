package org.springframework.context.annotation;

import org.springframework.core.type.AnnotationMetadata;

public interface ImportSelector {
  String[] selectImports(AnnotationMetadata paramAnnotationMetadata);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\annotation\ImportSelector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */