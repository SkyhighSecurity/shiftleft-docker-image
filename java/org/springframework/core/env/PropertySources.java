package org.springframework.core.env;

public interface PropertySources extends Iterable<PropertySource<?>> {
  boolean contains(String paramString);
  
  PropertySource<?> get(String paramString);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\env\PropertySources.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */