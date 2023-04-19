package org.springframework.context.annotation;

import org.springframework.beans.factory.Aware;
import org.springframework.core.type.AnnotationMetadata;

public interface ImportAware extends Aware {
  void setImportMetadata(AnnotationMetadata paramAnnotationMetadata);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\annotation\ImportAware.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */